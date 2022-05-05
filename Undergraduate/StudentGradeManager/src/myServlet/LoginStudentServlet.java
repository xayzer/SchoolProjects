package myServlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdbc_package.StudentOp;
import jdbc_package.TeacherOp;
import myJavabean.Student;

/**
 * Servlet implementation class LoginStudentServlet
 */
@WebServlet("/LoginStudentServlet")
public class LoginStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginStudentServlet() {
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
		String stu_id = request.getParameter("stu_id");
		String stu_pwd = request.getParameter("stu_pwd");
		//System.out.println(stu_id+" "+stu_pwd+" "+(String)request.getAttribute("stu_id")+" "+(String)request.getAttribute("stu_pwd"));
		HttpSession session = request.getSession();
		
		if(stu_id==null) {
			stu_id = (String)session.getAttribute("stu_id");
			stu_pwd = (String)session.getAttribute("stu_pwd");
		}
		
		Student student = StudentOp.searchStudent(stu_id, stu_pwd);
		List<Map<String, Object>> courseData = StudentOp.searchCourse(stu_id);
		List<Map<String, Object>> allCourseData = TeacherOp.getAllCourse();
		List<Map<String, Object>> teacherData = StudentOp.allCourse();
		
		if(student!=null) {
			session.setAttribute("student",student );
			session.setAttribute("courseData",courseData );
			session.setAttribute("allCourseData",allCourseData );
			session.setAttribute("teacherData",teacherData );
			
			response.sendRedirect("/StudentGradeManager/MyJSP/Interface_S.jsp");
		
		}else {
			String error = "账户登陆出现问题。原因可能是：1.密码输入不正确 2.账户不存在";
			request.setAttribute("error",error );
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
		
		
	}

}
