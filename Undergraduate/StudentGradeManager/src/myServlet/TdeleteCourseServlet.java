package myServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdbc_package.TeacherOp;

/**
 * Servlet implementation class TdeleteCourseServlet
 */
@WebServlet("/TdeleteCourseServlet")
public class TdeleteCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TdeleteCourseServlet() {
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String tea_id = request.getParameter("tea_id");
		String tea_pwd = request.getParameter("tea_pwd");
		HttpSession session = request.getSession();
		session.setAttribute("tea_id", tea_id);
		session.setAttribute("tea_pwd", tea_pwd);

		String course_id = request.getParameter("course_id");
		int i = TeacherOp.deleteCourse(tea_id, course_id);
		if (i == 1) {
			response.sendRedirect("/StudentGradeManager/LoginTeacherServlet");
		} else {
			String error = "教师删课出现问题。可能原因：1.课程号不存在 2.您未选择该课";
			request.setAttribute("error", error);
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
	}
}
