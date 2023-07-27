package com.example.skhuclock.api.weather.service;

import com.example.skhuclock.api.weather.dto.WeatherResponseDTO;
import com.example.skhuclock.domain.Weather.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class WeatherService {
    @Value("${serviceKey}")
    private String serviceKey;

    public List<WeatherResponseDTO> weather() {
        List<WeatherResponseDTO> listDto =new ArrayList<>();
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); //공개된 Url
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hour;
        hour = now.minusHours(2).format(DateTimeFormatter.ofPattern("HH00"));     //3시 54분 측정 3시 값 안나옴

        String currentChangeTime = now.format(DateTimeFormatter.ofPattern("yy.MM.dd hh시 mm분"));
        String nx = Integer.toString(57);
        String ny = Integer.toString(125);
        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("60", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hour, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            log.info("request url: {}", url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            String data = sb.toString();


            ArrayList<String> temp = new ArrayList<String>();
            ArrayList<String> rainAmount = new ArrayList<String>();
            ArrayList<String> humid = new ArrayList<String>();
            ArrayList<String> fcstTimes = new ArrayList<String>();

            JSONObject jObject = new JSONObject(data);
            JSONObject response = jObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray jArray = items.getJSONArray("item");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                String category = obj.getString("category");
                String obsrValue = obj.getString("fcstValue");
                String fcstTime = obj.getString("fcstTime");
                switch (category) {
                    case "TMP":
                        temp.add(obsrValue);
                        fcstTimes.add(fcstTime);
                        break;
                    case "POP":
                        rainAmount.add(obsrValue);
                        break;
                    case "REH":
                        humid.add(obsrValue);
                        break;
                }


            }
            for (int i = 0; i < 5; i++) {
                Weather weather = new Weather(temp.get(i), rainAmount.get(i), humid.get(i), fcstTimes.get(i),currentChangeTime);
                WeatherResponseDTO dto = WeatherResponseDTO.builder()
                        .weather(weather)
                        .message("OK").build();
                listDto.add(dto);

            }
            return listDto;


        } catch (IOException e) {
            WeatherResponseDTO dto = WeatherResponseDTO.builder()
                    .weather(null)
                    .message("날씨 정보를 불러오는 중 오류가 발생했습니다").build();
            listDto.add(dto);
            return listDto;
        }
    }
}
