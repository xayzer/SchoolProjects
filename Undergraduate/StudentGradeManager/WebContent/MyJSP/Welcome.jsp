<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to SGM!</title>
<!-- UIkit CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/css/uikit.min.css" />

<!-- UIkit JS -->
<script
	src="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/js/uikit.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/js/uikit-icons.min.js"></script>
<style type="text/css">
	#word{
		font-style: italic;
		color: white;
	}
</style>
</head>

<body>
<body>
	<div class="uk-child-width-1-1@s uk-grid-row-collapse uk-width-expand uk-height-1-1 uk-text-italic"  grid>
		<div class="uk-section uk-padding-remove-vertical">
			<div class="uk-container  uk-height-small uk-width-1-1 uk-flex uk-flex-center uk-flex-middle uk-background-cover" data-src="image/titlepic.jpg" uk-img>
				<div class="uk-position-left">
					<img data-src="image/logo-light.png" data-srcset="image/icon.png" width="150" height="150" uk-img>
				</div>
				<div>
					<h1 class="uk-heading-medium uk-heading-line uk-text-center"><span id="word">XX University</span></h1>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical">
			<div class="uk-container  uk-width-1-1 uk-flex uk-flex-center uk-flex-middle uk-background-cover uk-light uk-height-viewport" uk-height-viewport="expand:true" data-src="image/background.jpg" uk-img>
				<div>
					<h3>Welcome to Student Grade Manager 1.0</h3>
					<hr class="uk-divider-icon">
					<a href="/StudentGradeManager/MyJSP/Login as Student.jsp"><button
						class="uk-button uk-button-default uk-width-1-1 uk-margin-small-bottom">Login
					as Student</button></a> <br>
					<hr class="uk-divider-small">
					<a href="/StudentGradeManager/MyJSP/Register as Student.jsp"><button
						class="uk-button uk-button-default uk-width-1-1 uk-margin-small-bottom">
					Register as Student</button> </a> <br>
					<hr class="uk-divider-small">
					<a href="/StudentGradeManager/MyJSP/Login as Teacher.jsp"><button
						class="uk-button uk-button-default uk-width-1-1 uk-margin-small-bottom">Login
					as Teacher</button></a> <br>
					<hr class="uk-divider-small">
					<a href="/StudentGradeManager/MyJSP/Register as Teacher.jsp"><button
						class="uk-button uk-button-default uk-width-1-1 uk-margin-small-bottom">Register
					as Teacher</button></a>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical">
			<div class="uk-container uk-align-center uk-width-1-1 uk-background-secondary">
				<div class="uk-container uk-text-small uk-text-center">COPYRIGHT© 2019 LiuXiaoyue.ALL RIGHTS RESERVED</div>
				<div class="uk-container uk-text-small uk-text-center">Contact email:xayzer@protonmail.com</div>
			</div>
		</div>
	</div>
</body>
</body>
</html>