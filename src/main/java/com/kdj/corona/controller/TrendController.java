package com.kdj.corona.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException.Gone;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kdj.corona.crawling.Crawler;
import com.kdj.corona.db.service.StatusService;
import com.kdj.corona.dto.KeyWord;
import com.kdj.corona.dto.SearchTrend;
import com.kdj.corona.dto.Shopping;
import com.kdj.corona.dto.Status;
import com.kdj.corona.navarAPI.DatalabSearch;
import com.kdj.corona.navarAPI.SearchShooping;

@Controller
@RequestMapping("trend")
public class TrendController {
	
	@Autowired
    StatusService statusService;

	@RequestMapping("/")
	public ModelAndView APIExamDatalabTrend(HttpServletRequest request) throws Exception {
			
			ModelAndView model = new ModelAndView();
			
			model.setViewName("trend");
	
	        List<String> keywords = new ArrayList<String>();
	        List<String> keywords2 = new ArrayList<String>();
	        List<String> keywords3 = new ArrayList<String>();
	        keywords.add("코로나바이러스");
	        keywords.add("코로나");
	        keywords2.add("마스크");
	        keywords2.add("KF94마스크");
	        keywords2.add("일회용마스크");
	        keywords3.add("코로나19");
	        
	        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
	        String endDate = format1.format(new Date());
	        SearchTrend trend = SearchTrend.builder()
	        					.startDate("2020-01-17")
	        					.endDate(endDate)
	        					.timeUnit("date")
	        					.keywordGroups(new KeyWord[] {
	        							KeyWord.builder()
	        							.groupName("코로나바이러스")
	        							.keywords(keywords)
	        							.build(),
	        							KeyWord.builder()
	        							.groupName("마스크")
	        							.keywords(keywords2)
	        							.build(),
	        							KeyWord.builder()
	        							.groupName("코로나19")
	        							.keywords(keywords3)
	        							.build()
	        					})
	        					.device("")
	        					.ages(new String[] {})
	        					.gender("")
	        					.build();
	        
	        String answer = DatalabSearch.connectAPI(trend);       
	        
	    	model.addObject("trend",answer);
	    	
	    	
	    	Shopping shopping = Shopping.builder()
	    						.query("코로나")
	    						.display(10)
	    						.build();
	    	answer = SearchShooping.connectAPI(shopping);
	    	model.addObject("shopping", answer);
	    	System.out.println(answer);
	    	
//	    	Status status = Crawler.clawling();
	    	
//	    	statusService.insert(status);
	    	
	    	List<Status> list = statusService.getAll();
	    	
	    	if(list.size()>0) {
		    	Gson gson = new Gson();
		    	
		    	answer = gson.toJson(list.get(0));
		    	
		    	model.addObject("status", answer);
	    	}
	    	
	    	System.out.println(list.get(0).getQuarantinedPatient());
	    	
	    return model;
	}
	
}
