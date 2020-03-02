package com.kdj.corona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kdj.corona.crawling.Crawler;

@SpringBootApplication
public class CoronaApplication {
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(CoronaApplication.class, args);
		Crawler crawler = new Crawler();
		Thread crawlerThread = new Thread(crawler);
		crawlerThread.run();
	}

}
