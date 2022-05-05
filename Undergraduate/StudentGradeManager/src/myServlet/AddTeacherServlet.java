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
 * Servlet implementation class AddTeacherServlet
 */
@WebServlet("/AddTeacherServlet")
public class AddTeacherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTeacherServlet() {
        super();
        // TODO Auto-generated constructor teab
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method teab
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method teab
		request.setCharacterEncoding("UTF-8");//不设置从Jsp中接收的就会是乱码
		String tea_name = request.getParameter("tea_name");
		String tea_pwd = request.getParameter("tea_pwd");
		String tea_department = request.getParameter("tea_department");
		String tea_phone = request.getParameter("tea_phone");
		String tea_id = TeacherOp.addTeacher(tea_name, tea_pwd, tea_department, tea_phone);
		HttpSession session = request.getSession();
		if(tea_id!=null) {
			session.setAttribute("tea_pwd", tea_pwd);
			session.setAttribute("tea_id", tea_id);
			response.sendRedirect("/StudentGradeManager/LoginTeacherServlet");
			//request.getRequestDispatcher("/LoginTeacherServlet").forward(request, response);
		}else {
			String error = "教师注册出现问题。可能原因：信息填写不合法或该教师已被注册";
			request.setAttribute("error",error );
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
	}

}
