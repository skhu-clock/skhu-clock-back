package com.example.skhuclock.api.subway.service;

import com.example.skhuclock.api.subway.dto.SubwayResponseDTO;
import com.example.skhuclock.domain.Subway.Subway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubwayService {
    @Value("${apiKey}")
    private String apiKey;

    private static final String REALTIME_ARRIVAL_KEY = "realtimeArrivalList";

    @Transactional
    public List<SubwayResponseDTO> getSubway() {
        List<SubwayResponseDTO> subwayDto = new ArrayList<>();

        try {
            String stationName = "온수";
            String encodedStationName = URLEncoder.encode(stationName, "UTF-8");
            URL url = new URL("http://swopenapi.seoul.go.kr/api/subway/" + apiKey + "/json/realtimeStationArrival/1/5/" + encodedStationName);

            String result = fetchDataFromUrl(url);

            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.has(REALTIME_ARRIVAL_KEY)) {
                JSONArray realtimeArrivalList = jsonObject.getJSONArray(REALTIME_ARRIVAL_KEY);

                for (int i = 0; i < realtimeArrivalList.length(); i++) {
                    JSONObject tmp = realtimeArrivalList.getJSONObject(i);

                    Subway subway = Subway.builder()
                            .subwayId(tmp.getLong("subwayId"))
                            .updnLine(tmp.getString("updnLine"))
                            .trainLineNm(tmp.getString("trainLineNm"))
                            .arvlMsg2(tmp.getString("arvlMsg3"))
                            .arvlMsg1(tmp.getString("arvlMsg2"))
                            .arvlCd(tmp.getInt("arvlCd"))
                            .build();

                    SubwayResponseDTO dto = SubwayResponseDTO.builder()
                            .subway(subway)
                            .build();
                    subwayDto.add(dto);
                }
            } else {
                log.error("Key '{}' not found in API response.", REALTIME_ARRIVAL_KEY);
            }
        } catch (Exception e) {
            log.error("Error while fetching subway information", e);
        }
        return subwayDto;
    }

    private String fetchDataFromUrl(URL url) throws Exception {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }
}