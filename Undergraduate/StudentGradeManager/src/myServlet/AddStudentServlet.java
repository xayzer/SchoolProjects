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
 * Servlet implementation class AddStudentServlet
 */
@WebServlet("/AddStudentServlet")
public class AddStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStudentServlet() {
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
		request.setCharacterEncoding("UTF-8");//不设置从Jsp中接收的就会是乱码
		String stu_name = request.getParameter("stu_name");
		String stu_pwd = request.getParameter("stu_pwd");
		String stu_department = request.getParameter("stu_department");
		String stu_birth = request.getParameter("stu_birth");
		String stu_phone = request.getParameter("stu_phone");
		String stu_id = StudentOp.addStudent(stu_name, stu_pwd, stu_department, stu_birth, stu_phone);
		HttpSession session = request.getSession();
		if(stu_id!=null) {
			session.setAttribute("stu_pwd", stu_pwd);
			session.setAttribute("stu_id", stu_id);
			response.sendRedirect("/StudentGradeManager/LoginStudentServlet");
			//request.getRequestDispatcher("/LoginStudentServlet").forward(request, response);
		}else {
			String error = "学生注册出现问题。可能原因：信息填写不合法或该学生已被注册";
			request.setAttribute("error",error );
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}		
			
	}

}
