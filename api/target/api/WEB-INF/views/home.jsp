<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html lang="ko">
<head>
	<title>Home</title>
	<style></style>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${test}. </P>
<div id="result">
	<div></div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script type="text/javascript">

	const testManager = {
		contents:[],
		size:0,
		width:0,
		height:0,
		init:function(size, width, height){
			this.size = size;
			this.width = width;
			this.height = height;
		},
		test:function(){
			alert(this.width);
		},
		draw:function(){ // 결과를 화면에 보여줌
			var obj = $("#result");
			for(var i =0; i < testManager.contents.length; i++){
				var item = testManager.contents[i];
				obj.find("div").html(item.title);
			}
		},
		getTests:function(){
			$.ajax({
				url: "/api/tests",
				type: "get",
				contentType:"application/json; charset=UTF-8",
				success: function(data){ // API 호출을 통해 결과를 가져옴
					testManager.contents = data;
					testManager.draw();
				},
				error: function (request, status, error){
				}
			});
		},
		setTest:function(id, params){
			$.ajax({
				url: "/api/tests/"+id,
				type: "PUT",
				contentType:"application/json; charset=UTF-8",
				dataType:"json",
				data:JSON.stringify(params),
				success: function(data){ // API 호출을 통해 결과를 가져옴
					console.log(data);
				},
				error: function (request, status, error){
				}
			});
		}
	}
	$(document).ready(function(){
		testManager.init(100, 200, 200);
		testManager.setTest(0, {
			no:0,
			title:'bbb'
		});
		testManager.getTests(); // API 호출을 하여 결과를 조회하기 위함
	});
</script>
</body>
</html>
