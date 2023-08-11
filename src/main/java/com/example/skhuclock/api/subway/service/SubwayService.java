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

    @Transactional
    public List<SubwayResponseDTO> getSubway() {
        List<SubwayResponseDTO> subwayDto = new ArrayList<>();

        try {
            String stationName = "온수";
            String encodedStationName = URLEncoder.encode(stationName, "UTF-8");
            URL url = new URL("http://swopenapi.seoul.go.kr/api/subway/" + apiKey + "/json/realtimeStationArrival/1/5/" + encodedStationName);

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.has("realtimeArrivalList")) {
                JSONArray realtimeArrivalList = jsonObject.getJSONArray("realtimeArrivalList");

                for (int i = 0; i < realtimeArrivalList.length(); i++) {
                    JSONObject tmp = realtimeArrivalList.getJSONObject(i);

                    Subway subway = new Subway();
                    subway.setSubwayId(tmp.getInt("subwayId"));
                    subway.setUpdnLine(tmp.getString("updnLine"));
                        subway.setTrainLineNm(tmp.getString("trainLineNm"));
                        subway.setStatnNm(tmp.getString("statnNm"));
                        subway.setArvlMsg2(tmp.getString("arvlMsg2"));
                        subway.setArvlMsg3(tmp.getString("arvlMsg3"));
                        subway.setArvlCd(tmp.getInt("arvlCd"));

                    SubwayResponseDTO dto = SubwayResponseDTO.builder()
                            .subway(subway)
                            .build();
                    subwayDto.add(dto);
                }

            } else {
                System.out.println("Key 'realtimeArrivalList' not found in API response.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subwayDto;
    }
}