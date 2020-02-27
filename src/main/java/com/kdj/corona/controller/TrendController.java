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
			
			String answer,endDate;
	    	Gson gson = new Gson();
	    	SimpleDateFormat dateFormat;
	    	
	    	SearchTrend trend;
	        Shopping shopping;
	        Status status;
	        List<Status> statusList;
	        List<String> answerList = new ArrayList<String>();
	        List<Integer> quarantinedPatients = new ArrayList<Integer>();
	        List<Integer> treatedPatients = new ArrayList<Integer>();
	        List<Integer> deceasedPersons = new ArrayList<Integer>();
	        List<Integer> inspectings = new ArrayList<Integer>();
	        
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
	    	
	    	
	    	shopping = Shopping.builder()
	    						.query("코로나")
	    						.display(10)
	    						.build();
	    	answer = SearchShooping.connectAPI(shopping);
	    	model.addObject("shopping", answer);
	    	System.out.println(answer);
	    	
	    	statusList = statusService.getAll();
	    
	    	for(int i=0; i<statusList.size(); i++){
	    		status = statusList.get(i);
	    		
	    		quarantinedPatients.add(status.getQuarantinedPatient());
	    		treatedPatients.add(status.getTreatedPatient());
	    		deceasedPersons.add(status.getDeceasedPerson());
	    		inspectings.add(status.getInspecting());
	    		
		    	answer = gson.toJson(status);
		    	answerList.add(answer);
	    	}
	    	quarantinedPatients.sort(Comparator.reverseOrder());
	    	treatedPatients.sort(Comparator.reverseOrder());
	    	deceasedPersons.sort(Comparator.reverseOrder());
	    	inspectings.sort(Comparator.reverseOrder());
	    	
	    	status = Status.builder()
	    			.quarantinedPatient(quarantinedPatients.get(0))
	    			.treatedPatient(treatedPatients.get(0))
	    			.deceasedPerson(deceasedPersons.get(0))
	    			.inspecting(inspectings.get(0))
	    			.build();
	    	
	    	model.addObject("status", answerList);
	    	model.addObject("statusMax", gson.toJson(status));
	    	System.out.println(statusList.get(0).getQuarantinedPatient());
	    	
	    return model;
	}
	
}
