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
 * Servlet implementation class SetScoreServlet
 */
@WebServlet("/SetScoreServlet")
public class SetScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetScoreServlet() {
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
		int i=1;
		int k=0;
		String score=null;
		String sc_id=null;
		while(request.getParameter("sc"+i)!=null) {
			score = request.getParameter("stu"+i);
			sc_id=request.getParameter("sc"+i);
			int a = TeacherOp.setScore(sc_id, Integer.parseInt(score));
			if(a!=0) {
				k++;
			}
			i++;
		}
		System.out.println(k);
		if(k!=0) {
			response.sendRedirect("/StudentGradeManager/LoginTeacherServlet");
		}else {
			String error = "进行评分失败。可能原因:未打分或输入分数不合理（0-100分）";
			request.setAttribute("error",error );
			request.getRequestDispatcher("/MyJSP/Problem.jsp").forward(request, response);
		}

	}

}
