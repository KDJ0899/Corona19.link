<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>코로나19</title>
<link rel= "stylesheet" type="text/css" href="/static/css/trend.css">
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript" src="/static/js/trend.js"></script>
</head>
<body>
<div style = "width:90%; display: inline-block; text-align: center;">
	<div>
		<h1 style="background-color: darkolivegreen; color: white;">코로나19 현황</h1>
		<div id="status_div" style = "margin-bottom: 5%;">
				
		</div>
		<div id="status_chart_div" style="margin-bottom: 5%;">
		</div>
		
		<div id="chart_div" style="margin-bottom: 5%;">
		</div>
		
		<div style="width:70%; display: inline-block;">
			<h2>관련 뉴스</h2>
			<div id="news_div"  style=" text-align: left;">
			</div>
		</div>
		<div style="background-color: darkolivegreen; color: white;">
		<p>만든이 : 김동진</p>
		<p>dongjin0899@gmail.com</p>
		</div>
	</div>
	<%--<div style="height: 65%; display: flex;">
		<div>
			<img src="/static/img/NShopping.JPG" style="width : 30%;"/>
			
		</div>
		<div id="shoppong_div" style = "overflow: auto; width:93%; height : 80%;  display: inline-block;">
	
		</div>
	</div> --%>
</div>

<script type="text/javascript">

var statusList = JSON.parse('${status}');
var newsList = JSON.parse('${news}');

showStatusInfo(statusList);
showNewsInfo(newsList)

google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);
google.charts.setOnLoadCallback(drawStatusChart);

function drawStatusChart() {
	
	var graphList = JSON.parse('${graphList}');
	var graphList2 = JSON.parse('${graphList2}');
	var statusMax2 = JSON.parse('${maxStatus}');
	
	var statusMax = graphList[0];
	var arr = [[], []];
	console.log(statusMax.quarantinedPatient);
	var chart_options = {
		title: '국내 환자 현황 그래프 (단위: %)',
		width: '100%',
		seriesType: 'line',
		bar: {
			groupWidth: '80%'
		},
		hAxis: { textStyle: { fontSize: 12 } },
		titleTextStyle: {fontSize: 20}
	};

	arr = new Array(graphList.length + 1);

	arr[0] = new Array(4);
	arr[0][0] = 'Date';
	arr[0][1] = '확진환자';
	arr[0][2] = '격리해제';
	arr[0][3] = '사망자';

	var index = 1;
	for (var i = graphList.length - 1; i >= 0; i--) { //날짜 입력. 
		arr[index] = new Array(4);
		var obj = graphList[i];
		arr[index][0] = obj.date;
		arr[index][1] = obj.quarantinedPatient / statusMax.quarantinedPatient * 100;
		arr[index][2] = obj.treatedPatient / statusMax.treatedPatient * 100;
		arr[index][3] = obj.deceasedPerson / statusMax.deceasedPerson * 100;
		index++;

	} 
	
	/* var index = 1;
	for (var i = graphList2.length - 1; i >= 0; i--) { //날짜 입력. 
		arr[index] = new Array(4);
		var obj = graphList2[i];
		arr[index][0] = obj.date;
		arr[index][1] = obj.quarantinedPatient / statusMax2.quarantinedPatient * 100;
		arr[index][2] = obj.treatedPatient / statusMax2.treatedPatient * 100;
		arr[index][3] = obj.deceasedPerson / statusMax2.deceasedPerson * 100;
		index++;

	} */

	console.log(arr);
	var data = new google.visualization.arrayToDataTable(arr);
	var chart = new google.visualization.ComboChart(document.getElementById('status_chart_div'));
	chart.draw(data, chart_options);
	window.addEventListener('resize', function () { chart.draw(data, chart_options); }, false);
}

function drawChart() {
	var trendObj = JSON.parse('${trend}');
	var graphList = JSON.parse('${graphList2}');
	var statusMax = JSON.parse('${maxStatus}');
	
	var arr = [[], []];
	var chart_options = {
		title: '네이버 키워드 검색 빈도 (' + trendObj.startDate + ' ~ ' + trendObj.endDate + ')',
		width: '100%',
		bar: {
			groupWidth: '80%' // 예제에서 이 값을 수정
		},
		seriesType: 'bars',
		series: { 3: { type: 'line' } }, // 데이터에서 라인그래프로 만들값을 지정, 3은 순서를 의미하며 0부터 시작
		hAxis: { textStyle: { fontSize: 12 } },
		titleTextStyle: {fontSize: 20}
	};

	var results = trendObj.results;
	var date, str, dateType;
	var map = new Map();

	if (trendObj.timeUnit == "date")
		dateType = 1;
	else if (trendObj.timeUnit == "week")
		dateType = 7;

	date = trendObj.startDate.split('-');
	var startDate = new Date(date[0], date[1] - 1, date[2]);
	date = trendObj.endDate.split('-');
	var endDate = new Date(date[0], date[1] - 1, date[2]);


	var diff = Math.abs(startDate.getTime() - endDate.getTime());
	diff = Math.ceil(diff / (1000 * 3600 * 24 * dateType));

	arr = new Array(diff);

	arr[0] = new Array(results.length + 2);
	arr[0][0] = 'Date';
	console.log(diff);

	date = (startDate.getMonth() + 1) + '-' + startDate.getDate();
	map.set(date, new Array(results.length));
	arr[1] = new Array(results.length + 2);
	arr[1][0] = date;

	for (var j = 2; j < diff + 1; j++) { //날짜 입력. 
		startDate.setDate(startDate.getDate() + dateType);
		date = (startDate.getMonth() + 1) + '-' + startDate.getDate();
		map.set(date, new Array(results.length+1));
		arr[j] = new Array(results.length + 2);
		arr[j][0] = date;
	}


	for (var i = 0; i < results.length; i++) { // 값 입력. 날짜에 맞게 입력하기 위해 맵 사용.
		arr[0][i + 1] = results[i].title;
		var data = results[i].data;
		for (var j = 0; j < data.length; j++) {
			str = data[j].period.split('-');
			date = new Date(str[0], str[1] - 1, str[2]);
			str = (date.getMonth() + 1) + '-' + date.getDate();
			map.get(str)[i] = data[j].ratio;
		}
	}

	arr[0][4] = '확진환자';
	for(var i=0; i<graphList.length; i++){
		var obj = graphList[i];
		console.log(obj.date);
		if(map.has(obj.date))
			map.get(obj.date)[3] = obj.quarantinedPatient / statusMax.quarantinedPatient * 100;
	}

	var i = 1;
	for (let u of map.keys()) {
		var tmpArr = map.get(u);
		for (var j = 0; j < tmpArr.length; j++) {
			arr[i][j + 1] = tmpArr[j];
		}
		i++;
	}

	console.log(arr);
	
	var data = new google.visualization.arrayToDataTable(arr);
	var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
	chart.draw(data, chart_options);
	window.addEventListener('resize', function () { chart.draw(data, chart_options); }, false);

}

//showShoppingInfo();


</script>
</body>
</html>