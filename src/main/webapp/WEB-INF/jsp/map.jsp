<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <title>간단한 지도 표시하기</title>
    <script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=rjf58lewvd"></script>
	<style type="text/css">
	  html, body {
	     position: absolute;
	     width:100%;
	     height: 100%;
	     overflow:hidden;
	     margin: 0;
    	 padding: 0;
	  }
	  #circle {
		width:25px;
		height:25px;
		border-radius:50%;
		text-align:center;
		margin:0 auto;
		font-size:12px;
		vertical-align:middle;
		line-height:24px;
		border: 1px solid black;
		color: white;
	}
	</style>
</head>
<body>
<div style="width:100%; height:7%;"> 코로나</div>
<div id="map" style="width:100%;height:93%;"></div>
 
<script>

var mapOptions = {
    center: new naver.maps.LatLng(37.3595704, 127.105399),
    zoom: 10
};

var data = ${obj};

var map = new naver.maps.Map('map', mapOptions);
var markers = [];
var infoWindows = [];

for(var i=0; i<data.length; i++){
	var marker = new naver.maps.Marker({
	    position: new naver.maps.LatLng(data[i].latitude, data[i].longitude),
	    map:map,
	    icon: {
	    	content: '<div id="circle" style="background-color:black;">'+data[i].number+'</div>'
	    },
	    
	});
	
	var contentString = [
        '<div>',
        '   <p>'+data[i].number+'번째 확진자</p>',
        '   <p>'+data[i].content+'</p>',
        '</div>'
    ].join('');
	
	var infowindow = new naver.maps.InfoWindow({
	    content: contentString
	});
	
	markers[i] = marker;
	infoWindows[i] = infowindow;
}

for (var i=0; i<markers.length; i++) {
    naver.maps.Event.addListener(markers[i], 'click',  getClickHandler(i));
}

function getClickHandler(seq) {
    return function(e) {
    	
        var marker = markers[seq],
        infoWindow = infoWindows[seq];
        
        if (infoWindow.getMap()) {
            infoWindow.close();
        } else {
            infoWindow.open(map, marker);
        }
        

    }
}

 


</script>
</body>
</html>
