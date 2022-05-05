<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Teacher Login</title>
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
	<div
	class="uk-child-width-1-1@s uk-grid-row-collapse uk-width-expand uk-height-1-1 uk-text-italic"
	grid>
	<div class="uk-section uk-padding-remove-vertical">
		<div
		class="uk-container  uk-height-small uk-width-1-1 uk-flex uk-flex-center uk-flex-middle uk-background-cover"
		data-src="image/titlepic.jpg" uk-img>
		<div class="uk-position-left">
			<img data-src="image/icon.png" data-srcset="image/icon.png"
			width="150" height="150" uk-img>
		</div>
		<div>
			<h1
			class="uk-heading-medium uk-heading-line uk-text-center uk-text-emphasis">
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
		<form class="uk-form-horizontal uk-margin-large"
		action="/StudentGradeManager/LoginTeacherServlet" method="post">
		<h3>Teacher Login</h3>
		<hr class="uk-divider-icon">
		<div class="uk-margin">
			<div class="uk-margin">
				<div class="uk-inline">
					<span class="uk-form-icon" uk-icon="icon: user"></span> <input
					class="uk-input" type="text" name="tea_id"
					placeholder="Teacher_Id" required="required">
				</div>
			</div>
		</div>
		<div class="uk-margin">
			<div class="uk-margin">
				<div class="uk-inline">
					<span class="uk-form-icon uk-form-icon-flip"
					uk-icon="icon: lock"></span> <input class="uk-input"
					type="password" name="tea_pwd" placeholder="Teacher_Password"
					required="required">
				</div>
			</div>
		</div>
		<hr class="uk-divider-small">
		<div class="uk-margin">
			<div class="uk-form-controls">
				<button type="submit" class="uk-button uk-button-default">提交</button>
				<button type="reset" class="uk-button uk-button-default">重置</button>
			</div>
		</div>
	</form>
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