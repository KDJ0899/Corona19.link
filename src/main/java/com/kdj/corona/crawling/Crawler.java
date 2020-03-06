package com.kdj.corona.crawling;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	
	String nowDate;

	public void run() {
		
		DBConnector db = new DBConnector();
		List<Status> list = null;
		while(list==null) { //db에 저장된 정보 가져오기.
			try {
				list = db.getAll();
				nowDate = list.get(0).getDate();
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
				Status status = clawling();
				
				if(status !=null) {
					
					Date date1 = null;
					Date date2 = null;
					try {
					    DateFormat formatter ; 
					 
					    formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					    date1 = (Date)formatter.parse(status.getDate());
					    date2 = (Date)formatter.parse(nowDate);
					    
					    System.out.println("crawling: "+(date1.getMonth()+1)+"/"+(date1.getDay()+1)+" "+date1.getHours()+":0");
						System.out.println("saved: "+(date2.getMonth()+1)+"/"+(date2.getDay()+1)+" "+date2.getHours()+":0");
					} catch (Exception e) {}
					
					if(date1.getTime()!=date2.getTime()) {
						if(db.insert(status)){
							nowDate = status.getDate();
							Thread.sleep(64800000); //18시간
						}
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
	
	public Status clawling() {
		String url = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=11&ncvContSeq=&contSeq=&board_id=&gubun=";    //크롤링할 url지정
        Document doc = null;        //Document에는 페이지의 전체 소스가 저장된다
        Status status = null;
        
		try {

			doc = Jsoup.connect(url).get();

		} catch (IOException e) {

			e.printStackTrace();

		}

		// select를 이용하여 원하는 태그를 선택한다. select는 원하는 값을 가져오기 위한 중요한 기능이다.
		// ==>원하는 값들이 들어있는 덩어리를 가져온다
		if (doc != null) {
			Elements element = doc.select("div.bv_content"); 
			Iterator<Element> nowStatus = null;
			String str ="";
		  
		    //Iterator을 사용하여 하나씩 값 가져오기
		    //덩어리안에서 필요한부분만 선택하여 가져올 수 있다.
		    Iterator<Element> ie1 = element.select("table.num tbody").iterator();
		    Iterator<Element> ie2 = element.select("p.s_descript").iterator();
		   
		    if(ie1.hasNext()) {
		    	nowStatus = ie1.next().select("td").iterator();
		    }
		    if(ie2.hasNext()) {
		    	str= ie2.next().text();
	    	
		    	String[] strs = str.split("\\(");
		    	strs = strs[1].split(" 기준");
		    	strs = strs[0].split("일 ");
		    	String day = strs[0];
		    	String time = strs[1];
		    	strs = day.split("[.]");
		    	String month = strs[0];
		    	day = strs[1];
		    	strs = time.split("시");
		    	time = strs[0];
		    	
		    	List<String> list = new ArrayList<String>();
		    	while(nowStatus.hasNext()) {
		    		strs=nowStatus.next().text().split("명");
		    		strs[0] = strs[0].replace(",", "");
		    		strs[0] = strs[0].replace(" ", "");
		    		list.add(strs[0]);
		    	}
		    	
		    	int quarantinedPatient = Integer.parseInt(list.get(0));
		    	int treatedPatient = Integer.parseInt(list.get(1));
		    	int deceasedPerson = Integer.parseInt(list.get(2));
		    	int inspecting = Integer.parseInt(list.get(3));
		    	
		    	status = Status.builder()
		    			.quarantinedPatient(quarantinedPatient)
		    			.treatedPatient(treatedPatient)
		    			.deceasedPerson(deceasedPerson)
		    			.inspecting(inspecting)
						.date("2020-"+month+"-"+day+" "+time+":00")
					.build();
		    }
    	}
    	
		
        return status;
        
 
	}

}
