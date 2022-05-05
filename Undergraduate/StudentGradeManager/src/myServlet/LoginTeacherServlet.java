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
		//������ꑽ��Ք���
		request.setCharacterEncoding("UTF-8");
		String tea_id = request.getParameter("tea_id");
		String tea_pwd = request.getParameter("tea_pwd");
		//System.out.println(tea_id+" "+tea_pwd+" "+(String)request.getAttribute("tea_id")+" "+(String)request.getAttribute("tea_pwd"));
		HttpSession session = request.getSession();
		
		//�]����ֱ�ӵ����D
		if(tea_id==null) {
			tea_id = (String)session.getAttribute("tea_id");
			tea_pwd = (String)session.getAttribute("tea_pwd");
		}
		
		//����teacher�
		Teacher teacher = TeacherOp.searchTeacher(tea_id, tea_pwd);
		
		//ͨ�^tea_id �õ��̎����n��
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
			String error = "�˻���½�������⡣ԭ������ǣ�1.�������벻��ȷ 2.�˻�������";
			request.setAttribute("error",error );
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}
		
		
	}

}
