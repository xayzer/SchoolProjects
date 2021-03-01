<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error！</title>
<!-- UIkit CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/css/uikit.min.css" />

<!-- UIkit JS -->
<script
	src="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/js/uikit.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/js/uikit-icons.min.js">
</script>

<script type="text/javascript">
	window.onload=function(){
		var error = document.getElementById("error");
		error.innerHTML="${requestScope.error}";
	}
	function jump(){
		window.location.href="./MyJSP/Welcome.jsp";
	}
</script>

</head>
<body>
	<div
		class="uk-child-width-1-1@s uk-grid-row-collapse uk-width-expand uk-height-1-1 uk-text-italic"
		grid>
		<div class="uk-section uk-padding-remove-vertical">
			<div
				class="uk-container  uk-height-small uk-width-1-1 uk-flex uk-flex-center uk-flex-middle uk-background-cover"
				data-src="image/titlepic.jpg" uk-img>
				<div class="uk-position-left">
				</div>
				<div>
					<h1 class="uk-heading-medium uk-heading-line uk-text-center">
						<span id="word">XX University</span>
					</h1>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical">
			<div
				class="uk-container  uk-width-1-1 uk-flex uk-flex-center uk-flex-middle uk-background-cover uk-light uk-height-viewport"
				uk-height-viewport="expand:true" data-src="image/background.jpg"
				uk-img>
				<div>
					<div class="uk-position-center">
						<h2 id="error"></h2>
						<h2>(T n T) 5555555555</h2>
						<hr class="uk-divider-small">
						<h2>Sorry, your operation seems to have caused an problem.</h2>
						<h2>Please contact with the Administrator</h2>
						<hr class="uk-divider-small">
						<h2>对不起您的操作似乎产生了问题，请联系管理员</h2>
						<button type="button" onclick="jump()"
									id="dcButton" class="uk-button uk-button-default">退出</button>
					</div>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical">
			<div
				class="uk-container uk-align-center uk-width-1-1 uk-background-secondary">
				<div class="uk-container uk-text-small uk-text-center">COPYRIGHT©
					2019 LiuXiaoyue.ALL RIGHTS RESERVED</div>
				<div class="uk-container uk-text-small uk-text-center">Contact
					email:xayzer@protonmail.com</div>
			</div>
		</div>
	</div>
</body>
</html>