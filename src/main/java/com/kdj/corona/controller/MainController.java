package com.kdj.corona.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kdj.corona.dto.PatientRoute;


@Controller
public class MainController {
	
	
	@RequestMapping(value ="/hello" ,method=RequestMethod.GET)
	public String Hello() {
		
		return "welcome";
	}
	
	@RequestMapping(value ="/map" ,method=RequestMethod.GET)
	public ModelAndView Map() {
		Gson gson = new Gson();
		
		PatientRoute obj = PatientRoute.builder()
							.id(1)
							.number(1)
							.latitude(37.3595704)
							.longitude(127.105399)
							.content("test")
							.build();
		List<PatientRoute> list = new ArrayList<PatientRoute>();
		list.add(obj);
		return new ModelAndView("map","obj",gson.toJson(list));
	}
	
	
}
