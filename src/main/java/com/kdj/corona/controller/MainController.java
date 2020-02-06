package com.kdj.corona.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MainController {
	
	
	@RequestMapping(value ="/hello" ,method=RequestMethod.GET)
	public String Hello() {
		
		return "welcome";
	}
	
	@RequestMapping(value ="/map" ,method=RequestMethod.GET)
	public String Map() {
		
		return "map";
	}
	
	
}
