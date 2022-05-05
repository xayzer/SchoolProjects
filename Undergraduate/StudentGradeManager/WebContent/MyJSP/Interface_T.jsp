<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome ${sessionScope.teacher.tea_name } !</title>
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
		var scTd1 = document.createElement("td");
		scTd1.innerHTML = "${course.course_id}";
		var scTd2 = document.createElement("td");
		scTd2.innerHTML = "${course.course_name}";
		scTr.appendChild(scTd1);
		scTr.appendChild(scTd2);
		scTbody.appendChild(scTr);
		</c:forEach>
	}

	function linkCourse() {
		hiddelete("lcSelect");
		document.getElementById("acdiv").removeAttribute("hidden");
		var lcSelect = document.getElementById("lcSelect");
		<c:forEach items="${sessionScope.allCourseData}" var="course" varStatus="status" >
		var lcOption = document.createElement("option");
		lcOption.innerHTML = "${course.course_name}";
		lcOption.setAttribute("value", "${course.course_id}");
		lcSelect.appendChild(lcOption);
		</c:forEach>
		document.getElementById("lcForm").removeAttribute("hidden");
		var lcButton = document.getElementById("lcButton");
	}

	function setCourse() {
		document.getElementById("lcForm").setAttribute("hidden", "hidden");
		document.getElementById("acForm").removeAttribute("hidden");
	}

	function deleteCourse() {
		hidAll();
		document.getElementById("dcdiv").removeAttribute("hidden");

	}

	function setScore() {
		hiddelete("ciSelect");
		document.getElementById("ssdiv").removeAttribute("hidden");
		document.getElementById("s_r").removeAttribute("hidden");
		document.getElementById("ciSelect").removeAttribute("hidden");
		var ciSelect = document.getElementById("ciSelect");
		<c:forEach items="${sessionScope.courseData}" var="course" varStatus="status" >
		var ciOption = document.createElement("option");
		ciOption.innerHTML = "${course.course_name}";
		ciOption.setAttribute("value", "${course.course_id}");
		ciSelect.appendChild(ciOption);
		</c:forEach>
	}

	function deleteChild(val) {
		var f = document.getElementById(val);
		var childs = f.childNodes;
		for (var i = childs.length - 1; i >= 0; i--) {
			f.removeChild(childs[i]);
		}
	}

	function chooseStudent(course_id) {
		deleteChild("ssDiv");
		var ssDiv = document.getElementById("ssDiv");

		var ssTable1 = document.createElement("table");
		ssTable1.setAttribute("border", "1");
		ssTable1.setAttribute("class", "uk-table uk-table-divider");
		var ssTable2 = document.createElement("table");
		ssTable1.setAttribute("border", "1");

		var tr = document.createElement("tr");
		var th1 = document.createElement("th");
		th1.innerHTML = "Student_Id";

		var th2 = document.createElement("th");
		th2.innerHTML = "Student_Name";

		var th3 = document.createElement("th");
		th3.innerHTML = "Student_Score";

		tr.appendChild(th1);
		tr.appendChild(th2);
		tr.appendChild(th3);

		ssTable1.appendChild(tr);
		ssDiv.appendChild(ssTable1);

		var i = 0;
		<c:forEach items="${sessionScope.studentData}" var="student" varStatus="status" >
		if (course_id == "${student.course_id}") {
			i = i + 1;
			var hid = document.createElement("input");
			hid.setAttribute("type", "text");
			hid.setAttribute("name", "sc" + i);
			hid.setAttribute("value", "${student.sc_id}");
			hid.setAttribute("hidden", "hidden");
			ssTable1.appendChild(hid);
			var ssTr = document.createElement("tr");
			var ssTd1 = document.createElement("td");
			ssTd1.innerHTML = "${student.stu_id}";
			var ssTd2 = document.createElement("td");
			ssTd2.innerHTML = "${student.stu_name}";
			var ssTd3 = document.createElement("td");
			var ssInput = document.createElement("input");
			ssInput.setAttribute("type", "text");
			ssInput.setAttribute("class", "uk-input");

			ssInput.setAttribute("value", "${student.score}");
			ssInput.setAttribute("name", "stu" + i);
			ssTd3.appendChild(ssInput);
			ssTr.appendChild(ssTd1);
			ssTr.appendChild(ssTd2);
			ssTr.appendChild(ssTd3);
			ssTable1.appendChild(ssTr);
		}
		</c:forEach>
	}
	function hiddelete(val) {
		deleteChild(val);

		var scdiv = document.getElementById("scdiv");
		var scTable = document.getElementById("scTable");
		var acdiv = document.getElementById("acdiv");
		var lcForm = document.getElementById("lcForm");
		var acForm = document.getElementById("acForm");
		var dcdiv = document.getElementById("dcdiv");
		var ssdiv = document.getElementById("ssdiv");
		var s_r = document.getElementById("s_r");
		var ciSelect = document.getElementById("ciSelect");

		scdiv.setAttribute("hidden", "hidden");
		scTable.setAttribute("hidden", "hidden");
		acdiv.setAttribute("hidden", "hidden");
		lcForm.setAttribute("hidden", "hidden");
		acForm.setAttribute("hidden", "hidden");
		dcdiv.setAttribute("hidden", "hidden");
		ssdiv.setAttribute("hidden", "hidden");
		s_r.setAttribute("hidden", "hidden");
		ciSelect.setAttribute("hidden", "hidden");

	}
	function hidAll() {
		var scdiv = document.getElementById("scdiv");
		var scTable = document.getElementById("scTable");
		var acdiv = document.getElementById("acdiv");
		var lcForm = document.getElementById("lcForm");
		var acForm = document.getElementById("acForm");
		var dcdiv = document.getElementById("dcdiv");
		var ssdiv = document.getElementById("ssdiv");
		var s_r = document.getElementById("s_r");
		var ciSelect = document.getElementById("ciSelect");

		scdiv.setAttribute("hidden", "hidden");
		scTable.setAttribute("hidden", "hidden");
		acdiv.setAttribute("hidden", "hidden");
		lcForm.setAttribute("hidden", "hidden");
		acForm.setAttribute("hidden", "hidden");
		dcdiv.setAttribute("hidden", "hidden");
		ssdiv.setAttribute("hidden", "hidden");
		s_r.setAttribute("hidden", "hidden");
		ciSelect.setAttribute("hidden", "hidden");

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
						<span id="word">Hello there teacher
							${sessionScope.teacher.tea_name } !</span>
					</h1>
				</div>
			</div>
		</div>
		<div class="uk-section uk-padding-remove-vertical">
			<div
				class="uk-container uk-width-1-1 uk-flex uk-background-cover uk-light uk-height-viewport"
				uk-height-viewport="expand:true" data-src="image/background_1.jpg"
				uk-img>
				<div
					class="uk-background-center-center uk-grid-medium uk-child-width-1-1@s uk-width-expand uk-height-1-1"
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
								<!-- 教师所选课程 -->
								<h4>教师所选课程</h4>
								<hr class="uk-divider-icon">
								<table border="1" id="scTable" hidden="hidden"
									class="uk-table uk-table-divider">
									<thead>
										<tr>
											<td>课程编号</td>
											<td>课程名称</td>
										</tr>
									</thead>
									<tbody id=scTbody></tbody>
								</table>
							</div>
						</div>
					</div>
					<div>
						<div class="uk-child-width-expand@s uk-text-center" uk-grid
							id="acdiv" hidden="hidden">
							<div class="">
								<!-- 教师选课(已有) -->
								<h4>教师选课</h4>
								<hr class="uk-divider-icon">
								<form action="/StudentGradeManager/AddCourseServlet"
									method="post" hidden="hidden" id="lcForm"
									class="uk-form-horizontal uk-margin-large">
									<input type="text" hidden="hidden" name="tea_id"
										value="${sessionScope.teacher.tea_id}" /> <input type="text"
										hidden="hidden" name="tea_pwd"
										value="${sessionScope.teacher.tea_pwd}" />
									<div class="uk-margin">
										<label class="uk-form-label" for="form-horizontal-select">Course_Name</label>
										<div class="uk-form-controls">
											<select name="course_name" id="lcSelect" class="uk-select"
												required="required"></select>
										</div>
									</div>
									<hr class="uk-divider-small">
									<div class="uk-margin">
										<div class="uk-form-controls uk-form-controls-text">
											<label><button type="submit"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">提交</button></label>
											<label><button type="button" onclick="setCourse()"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">开设新课</button></label>
										</div>
									</div>
								</form>

								<!-- 教师选课 (未有)-->
								<form action="/StudentGradeManager/AddCourseServlet"
									method="post" hidden="hidden" id="acForm"
									class="uk-form-horizontal uk-margin-large">
									<input type="text" hidden="hidden" name="tea_id"
										value="${sessionScope.teacher.tea_id}" /> <input type="text"
										hidden="hidden" name="tea_pwd"
										value="${sessionScope.teacher.tea_pwd}" />
									<div class="uk-margin">
										<label class="uk-form-label" for="form-horizontal-select">Course_Name</label>
										<div class="uk-form-controls">
											<input type="text" name="course_name" class="uk-input"
												placeholder="课程名称" required="required" />
										</div>
									</div>
									<div class="uk-margin">
										<label class="uk-form-label" for="form-horizontal-select">Course_Department</label>
										<div class="uk-form-controls">
											<select name="course_department" class="uk-select">
												<option value="计算机专业">计算机专业</option>
												<option value="数学专业">数学专业</option>
												<option value="物理专业">物理专业</option>
												<option value="英语专业">英语专业</option>
												<option value="化学专业">化学专业</option>
												<option value="生物专业">生物专业</option>
												<option value="历史专业">历史专业</option>
												<option value="政治专业">政治专业</option>
											</select>
										</div>
									</div>
									<hr class="uk-divider-small">
									<div class="uk-margin">
										<div class="uk-form-controls uk-form-controls-text">
											<label><button type="submit"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">提交</button></label>
											<label><button type="button" onclick="setCourse()"
													class="uk-button uk-button-default uk-button-large uk-width-1-3">开设新课</button></label>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>

					<div>
						<div class="uk-child-width-expand@s uk-text-center" uk-grid
							id="ssdiv" hidden="hidden">
							<div class="">
								<!-- 教师评分 -->
								<form action="/StudentGradeManager/SetScoreServlet"
									method="post" id="ssForm"
									class="uk-form-horizontal uk-margin-large">
									<h4>教师评分</h4>
									<hr class="uk-divider-icon">
									<select name="course_id" id="ciSelect"
										onchange="chooseStudent(this.value)" hidden="hidden"
										class="uk-select">
										<option value="0">请选择评分课程</option>
									</select> <input type="text" hidden="hidden" name="tea_id"
										value="${sessionScope.teacher.tea_id}" /> <input type="text"
										hidden="hidden" name="tea_pwd"
										value="${sessionScope.teacher.tea_pwd}" />
									<hr class="uk-divider-small">
									<div id="ssDiv"></div>
									<hr class="uk-divider-small">
									<div class="uk-margin">
										<div class="uk-form-controls uk-form-controls-text" id="s_r"
											hidden="hidden">
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
								<!-- 教师删课 -->
								<h4>删除课程</h4>
								<hr class="uk-divider-icon">
								<form action="/StudentGradeManager/TdeleteCourseServlet"
									method="post" id="dcForm"
									class="uk-form-horizontal uk-margin-large">
									<input type="text" hidden="hidden" name="tea_id"
										value="${sessionScope.teacher.tea_id}" /> <input type="text"
										hidden="hidden" name="tea_pwd"
										value="${sessionScope.teacher.tea_pwd}" />
									<div class="uk-margin">
										<label class="uk-form-label" for="form-stacked-text">所要删除的课程号</label>
										<div class="uk-form-controls">
											<input class="uk-input" name="course_id" type="text"
												placeholder="Delete_CourseID" required="required">
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
				<!-- 显示教师讯息 -->
				<table class="uk-table uk-nav-left">
					<caption>Teacher_Information</caption>
					<tbody>
						<tr>
							<td>Teacher_ID</td>
							<td>${sessionScope.teacher.tea_id}</td>
						</tr>
						<tr>
							<td>Teacher_Name</td>
							<td>${sessionScope.teacher.tea_name}</td>
						</tr>
						<tr>
							<td>Teacher_Dept</td>
							<td>${sessionScope.teacher.tea_department}</td>
						</tr>
						<tr>
							<td>Teacher_Phone</td>
							<td>${sessionScope.teacher.tea_phone}</td>
						</tr>
					</tbody>
				</table>
				<!-- 显示教师功能 -->
				<table class="uk-table uk-table-divider	uk-nav-center">
					<caption>Teacher_Operation</caption>
					<tbody>
						<tr>
							<td><button type="button" onclick="seekCourse()"
									id="scButton" class="uk-button uk-button-default">查课</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="linkCourse()"
									id="lcButton" class="uk-button uk-button-default">开课</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="setScore()" id="ssButton"
									class="uk-button uk-button-default">评分</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="deleteCourse()"
									id="dcButton" class="uk-button uk-button-default">删课</button></td>
						</tr>
						<tr>
							<td><button type="button" onclick="jump()" id="dcButton"
									class="uk-button uk-button-default">退出</button></td>
						</tr>
					</tbody>
				</table>
				<div>
					<span>That's Life</span>
					<audio autoplay="autoplay" controls="controls" loop="loop"
						src="audio/bgm_t.mp3"></audio>
				</div>

			</ul>
		</div>
	</div>
</body>
</html>