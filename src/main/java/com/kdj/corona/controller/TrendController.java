package com.kdj.corona.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kdj.corona.dto.KeyWord;
import com.kdj.corona.dto.SearchTrend;

@Controller
@RequestMapping("trend")
public class TrendController {

	@RequestMapping("/")
	public ModelAndView APIExamDatalabTrend(HttpServletRequest request) {
	
		String clientId = "aYQR3UekVkUdLCaYXjcF";//애플리케이션 클라이언트 아이디값";
	    String clientSecret = "ruHZN2NlCd";//애플리케이션 클라이언트 시크릿값";
        Gson json = new Gson();
        String answer ="";
        
        
	    try {
	        String apiURL = "https://openapi.naver.com/v1/datalab/search";
	        List<String> keywords = new ArrayList<String>();
	        List<String> keywords2 = new ArrayList<String>();
	        List<String> keywords3 = new ArrayList<String>();
	        keywords.add("코로나");
	        keywords.add("우한");
	        keywords2.add("마스크");
	        keywords3.add("확진자");
	        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
	        String endDate = format1.format(new Date());
	        SearchTrend body = SearchTrend.builder()
	        					.startDate("2020-01-17")
	        					.endDate(endDate)
	        					.timeUnit("date")
	        					.keywordGroups(new KeyWord[] {
	        							KeyWord.builder()
	        							.groupName("코로나 바이러스")
	        							.keywords(keywords)
	        							.build(),
	        							KeyWord.builder()
	        							.groupName("마스크")
	        							.keywords(keywords2)
	        							.build(),
	        							KeyWord.builder()
	        							.groupName("확진자")
	        							.keywords(keywords3)
	        							.build(),
	        					})
	        					.device("")
	        					.ages(new String[] {})
	        					.gender("")
	        					.build();
	        
	        URL url = new URL(apiURL);
	        HttpURLConnection con = (HttpURLConnection)url.openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("X-Naver-Client-Id", clientId);
	        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	        con.setRequestProperty("Content-Type", "application/json");
	        
	
	        con.setDoOutput(true);
	        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	        wr.write(json.toJson(body).getBytes());
	        wr.flush();
	        wr.close();
	
	        int responseCode = con.getResponseCode();
	        BufferedReader br;
	        if(responseCode==200) { // 정상 호출
	            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	        } else {  // 에러 발생
	            br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
	        }
	
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = br.readLine()) != null) {
	            response.append(inputLine);
	        }
	        br.close();
	        System.out.println(response);
	        answer = response.toString();
//	        result = json.fromJson(response.toString(),ResponseResult.class);
	        
	
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	   
	    return new ModelAndView("welcome","result",answer);
	}
	
}
