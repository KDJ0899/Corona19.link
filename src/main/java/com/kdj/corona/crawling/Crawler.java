package com.kdj.corona.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.kdj.corona.dto.Status;

public class Crawler {
	
	public static String clawling() {
		String url = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=&brdGubun=&ncvContSeq=&contSeq=&board_id=&gubun=";    //크롤링할 url지정
        Document doc = null;        //Document에는 페이지의 전체 소스가 저장된다
        List<Element> result = new ArrayList<Element>();
        Status status = null;
        String answer = "";
 
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
		    Iterator<Element> ie1 = element.select("ul.s_listin_dot").iterator();
		    Iterator<Element> ie2 = element.select("p.s_descript").iterator();
		   
		    if(ie1.hasNext())
		    	nowStatus = ie1.next().select("li").iterator();
		    if(ie2.hasNext()) {
		    	str= ie2.next().text();
	    	
		    	String[] strs = str.split("\\(");
		    	strs = strs[1].split(" 기준");
		    	String time = strs[0];
		    	
		    	List<String> list = new ArrayList<String>();
		    	while(nowStatus.hasNext()) {
		    		strs=nowStatus.next().text().split("\\) ");
		    		strs = strs[1].split("명");
		    		strs[0] = strs[0].replace(",", "");
		    		System.out.println(strs[0]);
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
						.date(time)
					.build();
		    }
    	}
    	Gson gson = new Gson();
    	answer = gson.toJson(status);
        return answer;
        
 
	}

}
