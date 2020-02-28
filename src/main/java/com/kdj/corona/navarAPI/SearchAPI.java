package com.kdj.corona.navarAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.tomcat.util.buf.Utf8Encoder;

import com.google.gson.Gson;
import com.kdj.corona.dto.SearchForm;

public class SearchAPI extends ApiInfo {
	static String url = "https://openapi.naver.com/v1/search/";
	public static String shop = "shop";
	public static String news = "news";
	public static String connectAPI(SearchForm body,String api) {
        String answer ="";
        String text = "";
        String apiURL = url+api+".json?";
        try {
        	if(body!=null) {
            	text+="query="+URLEncoder.encode(body.getQuery(),"UTF-8");
            	if(body.getDisplay()!=0)
            		text+="&display="+body.getDisplay();
            	if(body.getStart()!=0)
            		text+="&start="+body.getStart();
            	if(body.getSort()!=null)
            		text+="&sort="+body.getSort();
            }
        	apiURL+=text;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            answer = response.toString();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
		return answer;
	}

}
