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
import com.kdj.corona.dto.Shopping;
import com.kdj.corona.navarAPI.DatalabSearch;
import com.kdj.corona.navarAPI.SearchShooping;

@Controller
@RequestMapping("trend")
public class TrendController {

	@RequestMapping("/")
	public ModelAndView APIExamDatalabTrend(HttpServletRequest request) {
			
			ModelAndView model = new ModelAndView();
			
			model.setViewName("welcome");
	
	        List<String> keywords = new ArrayList<String>();
	        List<String> keywords2 = new ArrayList<String>();
	        List<String> keywords3 = new ArrayList<String>();
	        keywords.add("코로나");
	        keywords.add("우한");
	        keywords2.add("마스크");
	        keywords3.add("확진자");
	        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
	        String endDate = format1.format(new Date());
	        SearchTrend trend = SearchTrend.builder()
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
	        
	        String answer = DatalabSearch.connectAPI(trend);       
	        
	    	model.addObject("trend",answer);
	    	
	    	
	    	Shopping shopping = Shopping.builder()
	    						.query("마스크")
	    						.build();
	    	answer = SearchShooping.connectAPI(shopping);
	    	model.addObject("shopping", answer);
	    	System.out.println(answer);
	    return model;
	}
	
}
