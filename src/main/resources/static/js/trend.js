
function showStatusInfo(statusList) {

	var obj1 = statusList[0];
	var obj2 = statusList[1];
	var div = document.getElementById('status_div');
	var text = "";
	var quarantinedPatient = obj1.quarantinedPatient - obj2.quarantinedPatient;
	var treatedPatient = obj1.treatedPatient - obj2.treatedPatient;
	var deceasedPerson = obj1.deceasedPerson - obj2.deceasedPerson;
	var inspecting = obj1.inspecting - obj2.inspecting;

	if (obj1.quarantinedPatient - obj2.quarantinedPatient > 0) {
		quarantinedPatient = "+" + quarantinedPatient;
	}
	if (obj1.treatedPatient - obj2.treatedPatient >= 0) {
		treatedPatient = "+" + treatedPatient;
	}
	if (obj1.deceasedPerson - obj2.deceasedPerson >= 0) {
		deceasedPerson = "+" + deceasedPerson;
	}
	if (obj1.inspecting - obj2.inspecting > 0) {
		inspecting = "+" + inspecting;
	}

	text += '<h2> 코로나바이러스감염증-19 국내 발생 현황 </h2>' +
		'<table style="width:100%; border-spacing:10px;">' +
		'<tr>' +
		'<td class = "left_td"></td>' + '<td class="date"><b>' + obj2.date + '</b></td>' + '<td class="date"><b>' + obj1.date + '</b></td>' +
		'</tr>' +
		'<tr>' +
		'<td class = "left_td">확진환자</td>' + '<td>' + obj2.quarantinedPatient + '</td>' + '<td>' + obj1.quarantinedPatient + '<b class="red"> (' + quarantinedPatient + ')</b></td>' +
		'</tr>' +
		'<tr>' +
		'<td class = "left_td">격리 해제된 환자</td>' + '<td>' + obj2.treatedPatient + '</td>' + '<td>' + obj1.treatedPatient + '<b class="red"> (' + treatedPatient + ')</b></td>' +
		'</tr>' +
		'<tr>' +
		'<td class = "left_td">사망자</td>' + '<td>' + obj2.deceasedPerson + '</td>' + '<td>' + obj1.deceasedPerson + '<b class="red"> (' + deceasedPerson + ')</b></td>' +
		'</tr>' +
		'<tr>' +
		'<td class = "left_td">검사 진행</td>' + '<td>' + obj2.inspecting + '</td>' + '<td>' + obj1.inspecting + '<b class="red"> (' + inspecting + ')</b></td>' +
		'</tr>' +
		'</table>';

	div.innerHTML = text;

}

function showShoppingInfo() {
	var obj = JSON.parse('${shopping}');

	var div = document.getElementById('shoppong_div');
	var text = "";

	for (var i = 0; i < obj.items.length; i++) {
		var item = obj.items[i];
		text += '<div class="button" style="display:flex;" onclick="window.open(\'' + item.link + '\');">' +
			'<div style="width:50%;">' +
			'<img src="' + item.image + '" width="50%"/>' +
			'</div>' +
			'<div style="width:50%;">' +
			'<h3>' + item.title + '</h3>' +
			'<h3>' + item.lprice + '원</h3>' +
			'</div>' +
			'</div>';
	}
	div.innerHTML = text;


}

function showNewsInfo(list) {
	console.log(list[0]);
	var div = document.getElementById('news_div');
	var text = "";
	var nowDate = new Date();
	var date;
	var dateStr;
	for (var i = 0; i < list.length; i++) {
		var item = list[i];
		date = new Date(item.pubDate);
		if (date.getDate() == nowDate.getDate()) {
			if (date.getHours() == nowDate.getHours()) {
				date = nowDate.getMinutes() - date.getMinutes();
				dateStr = date + "분전";
			}
			else {
				date = nowDate.getHours() - date.getHours();
				dateStr = date + "시간전";
			}
		}
		text += '<div class="button" onclick="window.open(\'' + item.link + '\');">' +
			'<div style="">' +
			'<h3>' + item.title + '</h3>' +
			'<p>' + item.description + '</p>' +
			'<p>' + dateStr + '</p>' +
			'</div>' +
			'</div>';
	}

	div.innerHTML = text;


}
