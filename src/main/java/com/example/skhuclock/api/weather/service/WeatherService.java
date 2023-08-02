package com.example.skhuclock.api.weather.service;

import com.example.skhuclock.api.weather.dto.WeatherResponseDTO;
import com.example.skhuclock.domain.Weather.Weather;
import lombok.SneakyThrows;
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

    // url 만들어주는 메소드
    // 조금 더 수정 예정
    @SneakyThrows
    public StringBuilder getUrl() {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); //공개된 Url
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hour;
        hour = now.minusHours(0).format(DateTimeFormatter.ofPattern("HH00"));     //3시 54분 측정 3시 값 안나옴

        String nx = Integer.toString(57);
        String ny = Integer.toString(125);


        // StringBuilder.append 를 남용할 경우 문제 발생 -> 아직은 괜찮을 듯 합니다.
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("60", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hour, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        log.info("request url: {}", urlBuilder);
        return urlBuilder;
    }

    public List<WeatherResponseDTO> weather() {
        LocalDateTime now = LocalDateTime.now();
        List<WeatherResponseDTO> listDto =new ArrayList<>();
        String currentChangeTime = now.format(DateTimeFormatter.ofPattern("yy.MM.dd hh시 mm분"));
        StringBuilder urlBuilder = getUrl();
        try {


            URL url = new URL(urlBuilder.toString());
            log.info("request url: {}", url);
// 어플리케이션과 url 연결 (get)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

// 에러가 나거나 아닌 경우에 따라 값 출력
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

// 수정 해야 할 것 같은 코드 -> requestDto 생성
            ArrayList<String> temp = new ArrayList<String>();
            ArrayList<String> rainAmount = new ArrayList<String>();
            ArrayList<String> humid = new ArrayList<String>();
            ArrayList<String> fcstTimes = new ArrayList<String>();
// jsonObject 관련 dto 생성 예정 or AOP 설정할 수도
            JSONObject jObject = new JSONObject(data);
            JSONObject response = jObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray jArray = items.getJSONArray("item");
// 필요한 내용 찾아주는 메소드
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
// 필요한 내용 DTO로 저장
            for (int i = 0; i < 5; i++) {
                Weather weather = new Weather(temp.get(i), rainAmount.get(i), humid.get(i), fcstTimes.get(i),currentChangeTime);
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
