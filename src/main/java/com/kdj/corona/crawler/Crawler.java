package com.kdj.corona.crawler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kdj.corona.db.DBConnector;
import com.kdj.corona.dto.Status;

public class Crawler implements Runnable {
	
	LocalDateTime nextDate;

	public void run() {
		
		DBConnector db = new DBConnector();
		List<Status> list = null;
		String date = null;
		while(list==null) { //db에 저장된 정보 가져오기.
			try {
				list = db.getAll();
				date = list.get(0).getDate();
				
				nextDate=LocalDateTime.parse(date,
			    	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")); 
				
				nextDate =nextDate.plusDays(1);
				date= nextDate.getMonthValue()+"월 "+(nextDate.getDayOfMonth())+"일";
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		while(true) { //크롤링.
			try {
				Status status = clawling(date);
				if(status !=null) {
					status.setDate(nextDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					if(db.insert(status)) {
						nextDate = nextDate.plusDays(1);
						System.out.println("Sucsess");
						Thread.sleep(72000000); //20시간
					}
				}
				else
					System.out.println("status is null");
				
				Thread.sleep(600000);//10분
			
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(600000);//10분
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public static Status clawling(String date) {
		String url = "https://www.cdc.go.kr";//크롤링할 url지정
		String board = "/board/board.es?mid=a20501000000&bid=0015";
        Document doc = null;        //Document에는 페이지의 전체 소스가 저장된다
        Status status = null;
        Element aLink,span,element;
        
        String title="코로나바이러스감염증-19 국내 발생 현황("+date+", 0시 기준)";
        
		try {

			doc = Jsoup.connect(url+board).get();

		} catch (IOException e) {

			e.printStackTrace();

		}
		if (doc != null) {
			Elements elements = doc.select("div#listView"); 
			Iterator<Element> nowStatus = null;
		    Iterator<Element> ie1 = elements.select("ul").iterator();
		   
		    while(ie1.hasNext()) {
		    	nowStatus = ie1.next().select("li.title").iterator();
		    	element = nowStatus.next();
		    	span = element.selectFirst("span");
		    	if(title.equals(span.text())) {
		    		board = element.selectFirst("a").attr("href");
		    		System.out.println(board);
		    		try {

		    			doc = Jsoup.connect(url+board).get();
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		    		elements = doc.select("table tbody");
	    			ie1 = elements.select("tr").iterator();
	    			ie1.next();
	    			ie1.next();
	    			ie1.next();
    				nowStatus = ie1.next().select("td").iterator();
    				
    				nowStatus.next();
    				nowStatus.next();
    				
    				int quarantinedPatient =Integer.parseInt(nowStatus.next().text().replace(",", ""));
    		    	int treatedPatient = Integer.parseInt(nowStatus.next().text().replace(",", ""));
    		    	nowStatus.next();
    		    	int deceasedPerson = Integer.parseInt(nowStatus.next().text().replace(",", ""));
    		    	nowStatus.next();
    		    	int inspecting = Integer.parseInt(nowStatus.next().text().replace(",", ""));
    				
    		    	status = Status.builder()
    		    			.quarantinedPatient(quarantinedPatient)
    		    			.treatedPatient(treatedPatient)
    		    			.deceasedPerson(deceasedPerson)
    		    			.inspecting(inspecting)
    					.build();
		    		
		    		break;
		    	}
		    }
		}
        return status;
	}

}
