<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Hello World!</title>
	<script src="jQuery/1.11.3/jquery.min.js"></script>
</head>
<body>
<h2>Hello World!</h2>
<h3><a href="javascript:void(0)" name="click">产生点击次数</a></h3>
<h5>点击次数统计:<span></span></h5>
<h3><a href="javascript:void(0)" name="clean">清除数据</a></h3>
</body>
<script>
	$(function () {
		$('a').on('click', function () {
			$.post('/servlet', {action:this.name}, function () {}, 'json')
		});
		var countFn = function () {
			$.post('/servlet', {action: 'count'}, function (data) {
				$('span').html(data);
				setTimeout(countFn, 300);
			}, 'json')
		};
		countFn();
	});
</script>
</html>
