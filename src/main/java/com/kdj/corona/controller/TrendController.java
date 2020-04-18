package com.kdj.corona.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kdj.corona.db.service.StatusService;
import com.kdj.corona.dto.KeyWord;
import com.kdj.corona.dto.News;
import com.kdj.corona.dto.Status;
import com.kdj.corona.dto.SearchForm;
import com.kdj.corona.dto.SearchTrend;
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

		model.setViewName("trend");

		model = trendAPI(model);
		model = shoppingAPI(model);
		model = makeStatus(model);
		model = newsAPI(model);

		return model;
	}

	public ModelAndView trendAPI(ModelAndView model) {
		List<String> keywords = new ArrayList<String>();
		List<String> keywords2 = new ArrayList<String>();
		List<String> keywords3 = new ArrayList<String>();

		String answer, endDate;
		SimpleDateFormat dateFormat;
		SearchTrend trend;

		keywords.add("코로나바이러스");
		keywords.add("코로나");
		keywords2.add("마스크");
		keywords2.add("KF94 마스크");
		keywords2.add("일회용마스크");
		keywords3.add("코로나19");

		// 트렌드 그래프
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		endDate = dateFormat.format(new Date());
		trend = SearchTrend.builder().startDate("2020-01-19").endDate(endDate).timeUnit("date") // date or week 만 가능.
				.keywordGroups(new KeyWord[] { KeyWord.builder().groupName("코로나바이러스").keywords(keywords).build(),
						KeyWord.builder().groupName("코로나19").keywords(keywords3).build(),
						KeyWord.builder().groupName("마스크").keywords(keywords2).build() })
				.device("").ages(new String[] {}).gender("").build();

		answer = DatalabSearch.connectAPI(trend);

		model.addObject("trend", answer);

		return model;

	}

	public ModelAndView shoppingAPI(ModelAndView model) {
		// 쇼핑 api
		SearchForm searchForm;
		String answer;
		searchForm = SearchForm.builder().query("코로나").display(10).build();
		answer = SearchAPI.connectAPI(searchForm, SearchAPI.shop);
		model.addObject("shopping", answer);
		return model;
	}

	public ModelAndView newsAPI(ModelAndView model) {
		SearchForm searchForm;
		String answer;
		Gson gson = new Gson();
		// 뉴스 api
//    	searchForm = SearchForm.builder()
//				.query("확진자 "+statusList.get(0).getQuarantinedPatient()+"명")
//				.display(2)
//				.sort("sim")
//				.build();
//    	answer = SearchAPI.connectAPI(searchForm,SearchAPI.news);
//    	News news = gson.fromJson(answer, News.class );

		searchForm = SearchForm.builder().query("코로나19").display(6).sort("sim").build();
		answer = SearchAPI.connectAPI(searchForm, SearchAPI.news);
		News news2 = gson.fromJson(answer, News.class);

		answer = gson.toJson(news2.getItems());
		model.addObject("news", answer);
		System.out.println(answer);

		return model;
	}

	public ModelAndView makeStatus(ModelAndView model) throws Exception {
		// 환자 현황.
		Gson gson = new Gson();
		String answer;
		Status status, status2, maxStatus;
		;
		List<Status> statusList, statusList2 = new ArrayList<Status>();
		;
		List<String> answerList = new ArrayList<String>();
		List<String> graphList = new ArrayList<String>();
		LocalDateTime date = null;

		statusList = statusService.getAll();

		for (int i = 0; i < statusList.size(); i++) {
			status = statusList.get(i);

			answer = gson.toJson(status);
			answerList.add(answer);
			date = status.getDate();

			if (date.getHour() != 9) {
				answer = gson.toJson(status);
				graphList.add(answer);
				statusList2.add(status);
			}
		}

		status2 = statusList2.get(0);
		maxStatus = Status.builder().deceasedPerson(0).quarantinedPatient(0).treatedPatient(0).build();
		List<String> graphList2 = new ArrayList<String>();

		for (int i = 1; i < statusList2.size(); i++) {
			status = status2;
			status2 = statusList2.get(i);

			status.setQuarantinedPatient(status.getQuarantinedPatient() - status2.getQuarantinedPatient());
			status.setDeceasedPerson(status.getDeceasedPerson() - status2.getDeceasedPerson());
			status.setTreatedPatient(status.getTreatedPatient() - status2.getTreatedPatient());

			if (maxStatus.getDeceasedPerson() < status.getDeceasedPerson())
				maxStatus.setDeceasedPerson(status.getDeceasedPerson());
			if (maxStatus.getQuarantinedPatient() < status.getQuarantinedPatient())
				maxStatus.setQuarantinedPatient(status.getQuarantinedPatient());
			if (maxStatus.getTreatedPatient() < status.getTreatedPatient())
				maxStatus.setTreatedPatient(status.getTreatedPatient());

			answer = gson.toJson(status);
			graphList2.add(answer);
		}

		answer = gson.toJson(status2);
		graphList2.add(answer);
		answer = gson.toJson(maxStatus);

		model.addObject("status", answerList);
		model.addObject("graphList", graphList); // db 값.
		model.addObject("graphList2", graphList2);// 전날 대비 변경 추이값.
		model.addObject("maxStatus", answer);

		return model;
	}

}
