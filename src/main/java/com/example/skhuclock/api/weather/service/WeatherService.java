package com.example.skhuclock.api.weather.service;

import com.example.skhuclock.api.weather.dto.WeatherResponseDTO;
import com.example.skhuclock.domain.Weather.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@Slf4j
public class WeatherService {
    @Value("${serviceKey}")
    private String serviceKey;

    // 시간을 구하는 메소드 (단기일 때) , 초단기일때는 그대로 나옴
    public String getHour(LocalDateTime now){

        String hour;
        // 단기 예보에서는 발표시각이 3시간에 한번이기 때문에 함수 필요
        if(now.getHour() %3 == 2){
            hour = now.format(DateTimeFormatter.ofPattern("HH00"));
        } else {
            hour = now.minusHours(now.getHour() %3 + 1).format(DateTimeFormatter.ofPattern("HH00"));
        }
        return hour;
    }
    // url 만들어주는 메소드
    public UriComponents getUrl() {
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd;
        String hour = getHour(now);
        if(now.getHour()<2){
            yyyyMMdd = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else {
            yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        String nx = Integer.toString(57);
        String ny = Integer.toString(125);
        UriComponents uri = UriComponentsBuilder
                .fromUriString("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey={serviceKey}")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 150)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", yyyyMMdd)
                .queryParam("base_time", hour)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .buildAndExpand(serviceKey);


        return uri;
    }

    // 어플리케이션과 url 연결 (get)
    // 에러가 나거나 아닌 경우에 따라 값 출력
    public String ErrorApi(UriComponents urlBuilder) throws IOException{
        URL url = new URL(urlBuilder.toString());
        log.info("request url: {}", url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader rd;
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return sb.toString();
        } catch (JSONException e){
            log.info(url.toString());
            log.info(((HttpURLConnection) url.openConnection()).getErrorStream().toString());
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            line = rd.readLine();
            sb.append(line);
            return sb.toString();
        }

    }

    @Transactional
    public List<WeatherResponseDTO> getWeather()  {
        List<WeatherResponseDTO> listDto = new ArrayList<>();
        UriComponents urlBuilder = getUrl(); //공개된 Url
        try {
            String data = ErrorApi(urlBuilder);

            // 수정 해야 할 것 같은 코드 -> requestDto 생성
            ArrayList<String> temp = new ArrayList<>();
            ArrayList<String> rainAmount = new ArrayList<>();
            ArrayList<String> humid = new ArrayList<>();
            ArrayList<String> fcstTimes = new ArrayList<>();
            ArrayList<String> skyData = new ArrayList<>();

            JSONObject jObject = new JSONObject(data);
            JSONObject response = jObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray jArray = items.getJSONArray("item");

            // 필요한 내용 찾아주는 메소드 -> 추 후 단기예보 초단기예보 같이 쓸 경우 고칠 필요가 있어보임
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
                    case "SKY":
                        skyData.add(obsrValue);
                        break;
                    case "REH":
                        humid.add(obsrValue);
                        break;
                }


            }
            // 필요한 내용 DTO로 저장
            for (int i = 0; i < 12; i++) {
                Weather weather = new Weather(temp.get(i), rainAmount.get(i), humid.get(i), skyData.get(i),fcstTimes.get(i));
                WeatherResponseDTO dto = WeatherResponseDTO.builder()
                        .weather(weather)
                        .message("OK").build();
                listDto.add(dto);
            }
            return listDto;


        } catch (IOException e) {
            // 오류 코드
            WeatherResponseDTO dto = WeatherResponseDTO.builder()
                    .weather(null)
                    .message("날씨 정보를 불러오는 중 오류가 발생했습니다").build();
            listDto.add(dto);
            return listDto;
        }
    }

}
