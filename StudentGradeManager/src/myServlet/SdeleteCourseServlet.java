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
 * Servlet implementation class SdeleteCourseServlet
 */
@WebServlet("/SdeleteCourseServlet")
public class SdeleteCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SdeleteCourseServlet() {
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

		String course_id = request.getParameter("course_id");
		String tea_id = request.getParameter("tea_id");
		int i = StudentOp.deleteCourse(stu_id, course_id, tea_id);
		if (i == 1) {
			response.sendRedirect("/StudentGradeManager/LoginStudentServlet");
		} else {
			String error = "学生退课出现问题。可能原因：1.课程号不存在 2.教师号不存在 3.您未选择该课程";
			request.setAttribute("error", error);
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
	}
}
