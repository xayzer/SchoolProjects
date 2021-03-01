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

import jdbc_package.TeacherOp;
import myJavabean.Teacher;

/**
 * Servlet implementation class LoginTeacherServlet
 */
@WebServlet("/LoginTeacherServlet")
public class LoginTeacherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginTeacherServlet() {
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
		//正常登陸接收數據
		request.setCharacterEncoding("UTF-8");
		String tea_id = request.getParameter("tea_id");
		String tea_pwd = request.getParameter("tea_pwd");
		//System.out.println(tea_id+" "+tea_pwd+" "+(String)request.getAttribute("tea_id")+" "+(String)request.getAttribute("tea_pwd"));
		HttpSession session = request.getSession();
		
		//註冊後直接的跳轉
		if(tea_id==null) {
			tea_id = (String)session.getAttribute("tea_id");
			tea_pwd = (String)session.getAttribute("tea_pwd");
		}
		
		//生成teacher類
		Teacher teacher = TeacherOp.searchTeacher(tea_id, tea_pwd);
		
		//通過tea_id 得到教師的課程
		List<Map<String, Object>> courseData = TeacherOp.getCourse(tea_id);
		List<Map<String, Object>> allCourseData = TeacherOp.getAllCourse();
		List<Map<String, Object>> studentData = TeacherOp.getStudent(tea_id);
		
		if(teacher!=null) {
			session.setAttribute("teacher",teacher );
			session.setAttribute("courseData",courseData );
			session.setAttribute("allCourseData",allCourseData );
			session.setAttribute("studentData",studentData );
			
			response.sendRedirect("/StudentGradeManager/MyJSP/Interface_T.jsp");
			
		}else {
			String error = "账户登陆出现问题。原因可能是：1.密码输入不正确 2.账户不存在";
			request.setAttribute("error",error );
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
		
		
	}

}
