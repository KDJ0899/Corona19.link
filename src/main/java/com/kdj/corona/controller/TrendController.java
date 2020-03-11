package com.kdj.corona.controller;

import java.text.DateFormat;
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
import com.kdj.corona.crawler.Crawler;
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
	        keywords2.add("마스크");
	        keywords2.add("KF94 마스크");
	        keywords2.add("일회용마스크");
	        keywords3.add("코로나19");
	        
	        //트렌드 그래프
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
	        							.build(),
	        							KeyWord.builder()
	        							.groupName("마스크")
	        							.keywords(keywords2)
	        							.build()
	        					})
	        					.device("")
	        					.ages(new String[] {})
	        					.gender("")
	        					.build();
	        
	        answer = DatalabSearch.connectAPI(trend);       
	        
	    	model.addObject("trend",answer);
	    	
	    	//쇼핑 api
	    	searchForm = SearchForm.builder()
	    						.query("코로나")
	    						.display(10)
	    						.build();
	    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.shop);
	    	model.addObject("shopping", answer);
	    	
	    	
	    	//환자 현황.
	    	statusList = statusService.getAll();
	    	
	    	status=statusList.get(0);
	    	Status status2,maxStatus;
	    	List<Status> statusList2 = new ArrayList<Status>();
	    	Date date = null;
			
	    	for(int i=0; i<statusList.size(); i++){
	    		status = statusList.get(i);
	    		
	    		answer = gson.toJson(status);
		    	answerList.add(answer);
	    		try {
				    DateFormat formatter ; 
				 
				    formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				    date = (Date)formatter.parse(status.getDate());
				    
				    status.setDate((date.getMonth()+1)+"-"+date.getDate());
				    
				} catch (Exception e) {}
	    		
		    	
		    	
		    	if(date.getHours()!=9) {
		    		answer = gson.toJson(status);
	    			graphList.add(answer);
	    			statusList2.add(status);
	    		}
	    	}
	    	status2 = statusList2.get(0);
	    	maxStatus =Status.builder()
	    			.deceasedPerson(0)
	    			.quarantinedPatient(0)
	    			.treatedPatient(0).build();
	    	List<String> graphList2 = new ArrayList<String>();
	    	
	    	for(int i=1; i<statusList2.size(); i++) {
	    		status = status2;
	    		status2 = statusList2.get(i);
	    		
	    		status.setQuarantinedPatient(status.getQuarantinedPatient()-status2.getQuarantinedPatient());
	    		status.setDeceasedPerson(status.getDeceasedPerson()-status2.getDeceasedPerson());
	    		status.setTreatedPatient(status.getTreatedPatient()-status2.getTreatedPatient());
	    		
	    		if(maxStatus.getDeceasedPerson()<status.getDeceasedPerson())
	    			maxStatus.setDeceasedPerson(status.getDeceasedPerson());
	    		if(maxStatus.getQuarantinedPatient()<status.getQuarantinedPatient())
	    			maxStatus.setQuarantinedPatient(status.getQuarantinedPatient());
	    		if(maxStatus.getTreatedPatient()<status.getTreatedPatient())
	    			maxStatus.setTreatedPatient(status.getTreatedPatient());
	    		
	    		answer = gson.toJson(status);
    			graphList2.add(answer);
	    	}
	    	System.out.println(status2.toString());
	    	
	    	answer = gson.toJson(status2);
			graphList2.add(answer);
			answer = gson.toJson(maxStatus);
	    	
	    	model.addObject("status", answerList);
	    	model.addObject("graphList", graphList);
	    	model.addObject("graphList2", graphList2);
	    	model.addObject("maxStatus", answer);
	    	
	    	System.out.println(statusList.get(0).getQuarantinedPatient());
	    	
	    	//뉴스 api
	    	searchForm = SearchForm.builder()
					.query("확진자 "+statusList.get(0).getQuarantinedPatient()+"명")
					.display(2)
					.sort("sim")
					.build();
	    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.news);
	    	News news = gson.fromJson(answer, News.class );
	    	
	    	searchForm = SearchForm.builder()
					.query("코로나19")
					.display(4)
					.sort("sim")
					.build();
	    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.news);
	    	News news2 = gson.fromJson(answer, News.class );
	    	
	    	news.getItems().addAll(news2.getItems());
	    	answer = gson.toJson(news.getItems());
	    	model.addObject("news", answer);
	    	System.out.println(answer);

	    	
	    return model;
	}
	
}
