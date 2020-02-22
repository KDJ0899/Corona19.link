<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Insert title here</title>
<style type="text/css">
	 html, body {
	    position: absolute;
	    width:100%;
	    height: 100%;
	    overflow:auto;
	    margin: 0;
    	padding: 0;
	 }
	.button{
		border-color: #DDDDDD;
		border-width: 1px;
		cursor: pointer;
		color: black;
		margin: 10px auto;
		display: block;
    }
    .under_div{
	    width : 50%; 
	    height : 100%; 
	    text-align: center; 
	    padding: 1%;
    }
    
    @media (max-width:1023px) {
	
	}
</style>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>


<div id="chart_div" style="height : 30%;">
</div>
<div style="height: 65%; display: flex;">
	<div class="under_div">
		<div>
			<img src="/static/img/NShopping.JPG" style="width : 30%;"/>
			
		</div>
		<div id="shoppong_div" style = "overflow: auto; width:93%; height : 80%;  display: inline-block;">
	
		</div>
	</div>
	
	<div class="under_div">
	
	</div>
</div>

<script type="text/javascript">
	google.charts.load('current', {'packages':['corechart']});
	google.charts.setOnLoadCallback(drawChart);

	showShoppingInfo();

	function showShoppingInfo(){
		var obj = JSON.parse('${shopping}');
		
		var div = document.getElementById('shoppong_div');
		var text = "";
		
		for(var i=0; i<obj.items.length; i++){
			var item = obj.items[i];
			text+='<div class="button" style="display:flex;" onclick="window.open(\''+item.link+'\');">'+
			'<div style="width:50%;">'+
			'<img src="'+item.image+'" width="50%"/>'+
			'</div>'+
			'<div style="width:50%;">'+
			'<h3>'+item.title+'</h3>'+
			'<h3>'+item.lprice+'원</h3>'+
			'</div>'+
			'</div>';
		}
		div.innerHTML = text;
	}
	
	function drawChart(){
		
		var arr = [[],[]];
		var obj = JSON.parse('${trend}');
		
		var chart_options = {
				title : '네이버 검색 빈도('+obj.startDate+' ~ '+obj.endDate+')',
				width : '100%',
				bar : {
					groupWidth : '80%' // 예제에서 이 값을 수정
				},
				seriesType : 'bars',
				series : {3 : {type : 'line'}}, // 데이터에서 라인그래프로 만들값을 지정, 3은 순서를 의미하며 0부터 시작
				isStacked : false // 그래프 쌓기(스택), 기본값은 false
			};
		
  		var results = obj.results;
		var date;
		var map = new Map();
		var str;
		
		date = obj.startDate.split('-');
		var startDate = new Date(date[0],date[1]-1,date[2]);
		date = obj.endDate.split('-');
		var endDate = new Date(date[0],date[1]-1,date[2]);
		
		
		var diff = Math.abs(startDate.getTime() - endDate.getTime());
	    diff = Math.ceil(diff / (1000 * 3600 * 24));

		arr = new Array(diff+1);
		
		arr[0] = new Array(results.length+1);
		arr[0][0] = 'Date';
		console.log(diff);
		
		date = (startDate.getMonth()+1)+'-'+startDate.getDate();
		map.set(date,new Array(results.length));
		arr[1] = new Array(results.length+1);
		arr[1][0] = date;
		
		for(var j=2; j<diff+1; j++){ //날짜 입력. 
			startDate.setDate(startDate.getDate()+1);
			date = (startDate.getMonth()+1)+'-'+startDate.getDate();
			map.set(date, new Array(results.length));
			arr[j] = new Array(results.length+1);
			arr[j][0] = date;
		}
		
		
		for(var i=0; i<results.length; i++){ // 값 입력. 날짜에 맞게 입력하기 위해 맵 사용.
			arr[0][i+1] = results[i].title;
			var data = results[i].data;
			for(var j=0; j<data.length; j++){
				str = data[j].period.split('-');
				date = new Date(str[0],str[1]-1,str[2]);
				str = (date.getMonth()+1)+'-'+date.getDate();
				
				map.get(str)[i]=data[j].ratio;
			}
		}
		
		var i=1;
		for(let u of map.keys()){
			var tmpArr = map.get(u);
		    for(var j=0; j<tmpArr.length; j++){
		    	arr[i][j+1] = tmpArr[j];
		    }
		    i++;
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