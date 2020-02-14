<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<style type="text/css">
	  html, body {
	     position: absolute;
	     width:100%;
	     height: 100%;
	     overflow:hidden;
	     margin: 0;
    	 padding: 0;
	  }
</style>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>


<div id="chart_div" style="height : 30%;">
</div>

<script type="text/javascript">
	google.charts.load('current', {'packages':['corechart']});
	google.charts.setOnLoadCallback(drawChart);
	
	
	function drawChart(){
		
		var arr = [[],[]];
		var obj = JSON.parse('${result}');
		
		var chart_options = {
				title : '검색 빈도('+obj.startDate+' ~ '+obj.endDate+')',
				width : '100%',
				bar : {
					groupWidth : '80%' // 예제에서 이 값을 수정
				},
				seriesType : 'bars',
				series : {3 : {type : 'line'}}, // 데이터에서 라인그래프로 만들값을 지정, 3은 순서를 의미하며 0부터 시작
				isStacked : false // 그래프 쌓기(스택), 기본값은 false
			};
		
		var yAxis = obj.results[0].data.length;
		var results = obj.results;
		var date;
		arr = new Array(yAxis);
		
		arr[0] = new Array(results.length+1);
		arr[0][0] = 'Date';
		
		for(var j=1; j<yAxis+1; j++){ //날짜 입력.
			arr[j] = new Array(results.length+1);
			date = results[0].data[j-1].period.split('-');
			arr[j][0] = date[1]+'-'+date[2];
		}
		
		for(var i=0; i<results.length; i++){ // 값 입력.
			arr[0][i+1] = results[i].title;
			var data = results[i].data;
			for(var j=0; j<data.length; j++){
				arr[j+1][i+1] = data[j].ratio;
			}
		}
		
		console.log(arr);
		var data = new google.visualization.arrayToDataTable(arr);
		var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
		chart.draw(data, chart_options);
		window.addEventListener('resize', function() { chart.draw(data, chart_options); }, false);

	}


</script>
</body>
</html>