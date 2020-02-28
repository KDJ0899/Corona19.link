package com.kdj.corona.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import com.google.gson.JsonArray;
import com.kdj.corona.crawling.Crawler;
import com.kdj.corona.db.service.StatusService;
import com.kdj.corona.dto.NewsItem;
import com.kdj.corona.dto.KeyWord;
import com.kdj.corona.dto.News;
import com.kdj.corona.dto.SearchTrend;
import com.kdj.corona.dto.SearchForm;
import com.kdj.corona.dto.Status;
import com.kdj.corona.navarAPI.DatalabSearch;
import com.kdj.corona.navarAPI.SearchAPI;

@Controller
@RequestMapping("")
public class TrendController {
	
	@Autowired
    StatusService statusService;

	@RequestMapping("/")
	public ModelAndView APIExamDatalabTrend(HttpServletRequest request) throws Exception {
			ModelAndView model = new ModelAndView();
			
			String answer,endDate;
	    	Gson gson = new Gson();
	    	SimpleDateFormat dateFormat;
	    	
	    	SearchTrend trend;
	        SearchForm searchForm;
	        Status status;
	        List<Status> statusList;
	        List<String> answerList = new ArrayList<String>();
	        List<String> graphList = new ArrayList<String>();
	        
			model.setViewName("trend");
	
	        List<String> keywords = new ArrayList<String>();
	        List<String> keywords2 = new ArrayList<String>();
	        List<String> keywords3 = new ArrayList<String>();
	        
	        keywords.add("코로나바이러스");
	        keywords.add("코로나");
	        keywords3.add("코로나19");
	        
	        dateFormat = new SimpleDateFormat ( "yyyy-MM-dd");
	        endDate = dateFormat.format(new Date());
	        trend = SearchTrend.builder()
	        					.startDate("2020-01-19")
	        					.endDate(endDate)
	        					.timeUnit("date") // date or week 만 가능.
	        					.keywordGroups(new KeyWord[] {
	        							KeyWord.builder()
	        							.groupName("코로나바이러스")
	        							.keywords(keywords)
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
	        
	        answer = DatalabSearch.connectAPI(trend);       
	        
	    	model.addObject("trend",answer);
	    	
	    	
	    	searchForm = SearchForm.builder()
	    						.query("코로나")
	    						.display(10)
	    						.build();
	    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.shop);
	    	model.addObject("shopping", answer);
	    	
	    	statusList = statusService.getAll();
	    	
	    	status=statusList.get(0);
	    	
	    	if(status.getDate().contains("09시")) {
	    		System.out.println("h");
	    		status.setDate("2.5'일'");
	    		answer = gson.toJson(status);
	    		graphList.add(answer);
	    		System.out.println(answer);
	    	}
	    
	    	for(int i=0; i<statusList.size(); i++){
	    		status = statusList.get(i);
	    		
		    	answer = gson.toJson(status);
		    	answerList.add(answer);
		    	
		    	if(status.getDate().contains("16시")) {
	    			graphList.add(answer);
	    		}
	    	}
	    	
	    	
	    	
	    	model.addObject("status", answerList);
	    	model.addObject("graphList", graphList);
	    	System.out.println(statusList.get(0).getQuarantinedPatient());
	    	
	    	//뉴스 api
	    	searchForm = SearchForm.builder()
					.query("확진자 "+statusList.get(0).getQuarantinedPatient()+"명")
					.display(2)
					.sort("sim")
					.build();
	    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.news);
	    	News news1 = gson.fromJson(answer, News.class );
	    	
	    	searchForm = SearchForm.builder()
					.query("코로나19")
					.display(4)
					.sort("sim")
					.build();
	    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.news);
	    	News news2 = gson.fromJson(answer, News.class );
	    	
	    	news1.getItems().addAll(news2.getItems());
	    	answer = gson.toJson(news1.getItems());
	    	model.addObject("news1", answer);
	    	System.out.println(answer);

	    	
	    return model;
	}
	
}
