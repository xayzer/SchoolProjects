<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome ${sessionScope.student.stu_name } !</title>
<!-- UIkit CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/css/uikit.min.css" />

<!-- UIkit JS -->
<script
	src="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/js/uikit.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/uikit@3.2.3/dist/js/uikit-icons.min.js"></script>
<script type="text/javascript">
	function jump() {
		window.location.href = "./Welcome.jsp";
	}
	function seekCourse() {
		hiddelete("scTbody");
		document.getElementById("scdiv").removeAttribute("hidden");
		document.getElementById("scTable").removeAttribute("hidden");
		var scTbody = document.getElementById("scTbody");
		<c:forEach items="${sessionScope.courseData}" var="course" varStatus="status" >
		var scTr = document.createElement("tr");
		var scTd0 = document.createElement("td");
		scTd0.innerHTML = "${course.course_id}";
		var scTd1 = document.createElement("td");
		scTd1.innerHTML = "${course.course_name}";
		var scTd2 = document.createElement("td");
		scTd2.innerHTML = "${course.course_department}";
		var scTd = document.createElement("td");
		scTd.innerHTML = "${course.tea_id}";
		var scTd3 = document.createElement("td");
		scTd3.innerHTML = "${course.tea_name}";
		var scTd4 = document.createElement("td");
		scTd4.innerHTML = "${course.score}";
		scTr.appendChild(scTd0);
		scTr.appendChild(scTd1);
		scTr.appendChild(scTd2);
		scTr.appendChild(scTd);
		scTr.appendChild(scTd3);
		scTr.appendChild(scTd4);
		scTbody.appendChild(scTr);
		</c:forEach>
	}

	function chooseCourse() {
		hiddelete("ccSelect1");
		document.getElementById("ccdiv").removeAttribute("hidden");
		document.getElementById("ccForm").removeAttribute("hidden");

		var ccSelect1 = document.getElementById("ccSelect1");
		<c:forEach items="${sessionScope.allCourseData}" var="course" varStatus="status" >
		var ccOption = document.createElement("option");
		ccOption.innerHTML = "${course.course_name}";
		ccOption.setAttribute("value", "${course.course_id}");
		ccSelect1.appendChild(ccOption);
		</c:forEach>

		document.getElementById("ccForm").removeAttribute("hidden");
	}

	function deleteChild(val) {
		var f = document.getElementById(val);
		var childs = f.childNodes;
		for (var i = childs.length - 1; i >= 0; i--) {
			f.removeChild(childs[i]);
		}
	}

	function deleteCourse() {
		hidAll();
		document.getElementById("dcdiv").removeAttribute("hidden");
	}

	function chooseTeacher(course_id) {
		deleteChild("ccSelect2");
		<c:forEach items="${sessionScope.teacherData}" var="course" varStatus="status" >
		if (course_id == "${course.course_id}") {
			var ccOption = document.createElement("option");
			ccOption.innerHTML = "${course.tea_name}";
			ccOption.setAttribute("value", "${course.tea_id}");
			ccSelect2.appendChild(ccOption);
		}
		</c:forEach>
	}
	function hiddelete(val) {
		deleteChild(val);
		var scdiv = document.getElementById("scdiv");
		var scTable = document.getElementById("scTable");
		var ccdiv = document.getElementById("ccdiv");
		var dcdiv = document.getElementById("dcdiv");

		scdiv.setAttribute("hidden", "hidden");
		scTable.setAttribute("hidden", "hidden");
		ccdiv.setAttribute("hidden", "hidden");
		dcdiv.setAttribute("hidden", "hidden");

	}
	function hidAll() {
		var scdiv = document.getElementById("scdiv");
		var scTable = document.getElementById("scTable");
		var ccdiv = document.getElementById("ccdiv");
		var dcdiv = document.getElementById("dcdiv");

		scdiv.setAttribute("hidden", "hidden");
		scTable.setAttribute("hidden", "hidden");
		ccdiv.setAttribute("hidden", "hidden");
		dcdiv.setAttribute("hidden", "hidden");

	}
</script>
<style type="text/css">
#word {
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
						<span id="word">Helllo there student
							${sessionScope.student.stu_name } !</span>
					</h1>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical">
			<div
				class="uk-container uk-width-1-1 uk-flex uk-background-cover uk-light uk-height-viewport"
				uk-height-viewport="expand:true" data-src="image/background_2.jpeg"
				uk-img>
				<div
					class="uk-grid-medium uk-child-width-1-1@s uk-width-expand uk-height-1-1"
					grid>
					<div>
						<button class="uk-button uk-button-default uk-width-1-1"
							type="button" uk-toggle="target: #offcanvas-nav-primary">
							<span class="uk-text-emphasis">Supporting Functions</span>
						</button>
					</div>
					<br>
					<div>
						<div class="uk-child-width-expand@s uk-text-center" uk-grid
							id="scdiv" hidden="hidden">
							<div class="">
								<!-- 学生已选课程 -->
								<h4>学生已选课程</h4>
								<hr class="uk-divider-icon">
								<table border="1" id="scTable" hidden="hidden"
									class="uk-table uk-table-divider">
									<thead>
										<tr>
											<td>课程编号</td>
											<td>课程名称</td>
											<td>课程专业</td>
											<td>教师编号</td>
											<td>指导教师</td>
											<td>课程评分</td>
										</tr>
									</thead>
									<tbody id=scTbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div>
						<div class="uk-child-width-expand@s uk-text-center" uk-grid
							id="ccdiv" hidden="hidden">
							<div class="">
								<!-- 学生选课 -->
								<h4>学生选课</h4>
								<hr class="uk-divider-icon">
								<form action="/StudentGradeManager/ChooseCourseServlet"
									method="post" id="ccForm" hidden="hidden"
									class="uk-form-horizontal uk-margin-large">
									<input type="text" hidden="hidden" name="stu_id"
										value="${sessionScope.student.stu_id}" /> <input type="text"
										hidden="hidden" name="stu_pwd"
										value="${sessionScope.student.stu_pwd}" />
									<div class="uk-margin">
										<label class="uk-form-label" for="form-horizontal-select">Course_Name</label>
										<div class="uk-form-controls">
											<select name="course_name" id="ccSelect1"
												onchange="chooseTeacher(this.value)" class="uk-select">
												<option selected>请选择要添加的课程</option>
											</select>
										</div>
									</div>
									<div class="uk-margin">
										<label class="uk-form-label" for="form-horizontal-select">Teacher_Name</label>
										<div class="uk-form-controls">
											<select name="tea_name" id="ccSelect2" class="uk-select">
												<option selected>先选择课程</option>
											</select>
										</div>
									</div>
									<hr class="uk-divider-small">
									<div class="uk-margin">
										<div class="uk-form-controls uk-form-controls-text">
											<label><button type="submit"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">提交</button></label>
											<label><button type="reset"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">重置</button></label>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>

					<div>
						<div class="uk-child-width-expand@s uk-text-center" uk-grid
							id="dcdiv" hidden="hidden">
							<div class="">
								<!-- 学生退课 -->
								<h4>学生退课</h4>
								<hr class="uk-divider-icon">
								<form action="/StudentGradeManager/SdeleteCourseServlet"
									method="post" id="dcForm"
									class="uk-form-horizontal uk-margin-large">
									<input type="text" hidden="hidden" name="stu_id"
										value="${sessionScope.student.stu_id}" /> <input type="text"
										hidden="hidden" name="stu_pwd"
										value="${sessionScope.student.stu_pwd}" />
									<div class="uk-margin">
										<label class="uk-form-label" for="form-stacked-text">所要退课的课程号</label>
										<div class="uk-form-controls">
											<input class="uk-input" name="course_id" type="text"
												placeholder="CourseID" required="required">
										</div>
									</div>
									<div class="uk-margin">
										<label class="uk-form-label" for="form-stacked-text">所要退课的教师号</label>
										<div class="uk-form-controls">
											<input class="uk-input" name="tea_id" type="text"
												placeholder="TeacherID" required="required">
										</div>
									</div>
									<hr class="uk-divider-small">
									<div class="uk-margin">
										<div class="uk-form-controls uk-form-controls-text">
											<label><button type="submit"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">提交</button></label>
											<label><button type="reset"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">重置</button></label>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical uk-position-bottom">
			<div
				class="uk-container uk-align-center uk-width-1-1 uk-background-secondary">
				<div class="uk-container uk-text-small uk-text-center">COPYRIGHT©
					2019 LiuXiaoyue.ALL RIGHTS RESERVED</div>
				<div class="uk-container uk-text-small uk-text-center">Contact
					email:xayzer@protonmail.com</div>
			</div>
		</div>
	</div>

	<div id="offcanvas-nav-primary" uk-offcanvas="overlay: true">
		<div class="uk-offcanvas-bar uk-flex uk-flex-column">
			<ul class="uk-nav uk-nav-primary uk-margin-auto-vertical">
				<!-- 显示学生信息 -->
				<table class="uk-table uk-nav-left">
					<caption>Student_Information</caption>
					<tbody>
						<tr>
							<td>Student_ID</td>
							<td>${sessionScope.student.stu_id}</td>
						</tr>
						<tr>
							<td>Student_Name</td>
							<td>${sessionScope.student.stu_name}</td>
						</tr>
						<tr>
							<td>Student_Dept</td>
							<td>${sessionScope.student.stu_department}</td>
						</tr>
						<tr>
							<td>Student_Phone</td>
							<td>${sessionScope.student.stu_phone}</td>
						</tr>
						<tr>
							<td>Student_Birth</td>
							<td>${sessionScope.student.stu_birth}</td>
						</tr>
					</tbody>
				</table>
				<!-- 显示学生功能 -->
				<table class="uk-table uk-table-divider uk-nav-center ">
					<caption>Student_Operation</caption>
					<tbody>
						<tr>
							<td><button type="button" onclick="seekCourse()"
									id="scButton" class="uk-button uk-button-default">查课</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="chooseCourse()"
									id="ccButton" class="uk-button uk-button-default">选课</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="deleteCourse()"
									id="dcButton" class="uk-button uk-button-default">退课</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="jump()" id="dcButton"
									class="uk-button uk-button-default">退出</button></td>
						</tr>
					</tbody>
				</table>
			</ul>
			<div>
				<span>In My Secret Life</span>
				<audio autoplay="autoplay" controls="controls" loop="loop"
					src="audio/bgm_s.mp3"></audio>
			</div>
		</div>
	</div>
</body>
</html>