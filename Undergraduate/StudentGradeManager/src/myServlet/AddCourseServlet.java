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
 * Servlet implementation class AddCourseServlet
 */
@WebServlet("/AddCourseServlet")
public class AddCourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCourseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String tea_id = request.getParameter("tea_id");
		String tea_pwd = request.getParameter("tea_pwd");
		HttpSession session = request.getSession();
		session.setAttribute("tea_id", tea_id);
		session.setAttribute("tea_pwd", tea_pwd);
		String course_name = request.getParameter("course_name");
		String course_department = request.getParameter("course_department");
		System.out.println(course_name+" "+course_department+" ");
		if(course_department==null) {
			int i = TeacherOp.linkCourse(tea_id, course_name);
			if(i==1){
				response.sendRedirect("/StudentGradeManager/LoginTeacherServlet");
			}else {
				//response.sendRedirect("/StudentGradeManager/MyJSP/Problem.jsp");
				String error = "教师选课出现问题，您似乎已经选过此课程";
				request.setAttribute("error",error );
				request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
			}
		}else{
			int i = TeacherOp.setCourse(tea_id, course_name, course_department);
			if(i==1){
				response.sendRedirect("/StudentGradeManager/LoginTeacherServlet");
			}else {
				//response.sendRedirect("/StudentGradeManager/MyJSP/Problem.jsp");
				String error = "教师开设新课出现问题";
				request.setAttribute("error",error );
				request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
			}
		}
	}

}
