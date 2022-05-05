package myServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdbc_package.StudentOp;

/**
 * Servlet implementation class ChooseCourseServlet
 */
@WebServlet("/ChooseCourseServlet")
public class ChooseCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChooseCourseServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		
		String stu_id = request.getParameter("stu_id");
		String stu_pwd = request.getParameter("stu_pwd");

		HttpSession session = request.getSession();
		session.setAttribute("stu_id", stu_id);
		session.setAttribute("stu_pwd", stu_pwd);

		String course_id = request.getParameter("course_name");
		String tea_id = request.getParameter("tea_name");
		System.out.println(course_id + " " + tea_id + " ");
		
		int i = StudentOp.addCourse(stu_id, course_id, tea_id);
		
		if (i == 1) {
			response.sendRedirect("/StudentGradeManager/LoginStudentServlet");
		} else {
			// response.sendRedirect("/StudentGradeManager/MyJSP/Problem.jsp");
			String error = "学生选课出现问题。可能原因：您已经选过此课程";
			request.setAttribute("error", error);
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
	}

}
