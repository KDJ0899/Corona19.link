package com.kdj.corona.navarAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.kdj.corona.dto.SearchTrend;

public class DatalabSearch {
	
	public static String connectAPI(SearchTrend body) {
		String clientId = "aYQR3UekVkUdLCaYXjcF";//애플리케이션 클라이언트 아이디값";
	    String clientSecret = "ruHZN2NlCd";//애플리케이션 클라이언트 시크릿값";
        Gson json = new Gson();
        String answer ="";
        
        
	    try {
	        String apiURL = "https://openapi.naver.com/v1/datalab/search";
	        
	        
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
		return answer;
	}

}
