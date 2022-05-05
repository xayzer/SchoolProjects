package jdbc_package;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import myJavabean.Student;

public class StudentOp {
	public static void main(String[] args) {
		System.out.println(addCourse("1", "3", "18"));
	}
	
	public static int addStudent(Student stu){
		String sql = "insert into student(stu_name,stu_pwd,stu_department,stu_phone) values(?,?,?,?)";
		try {
			if(JdbcUtil.update(sql,stu.getStu_name(),stu.getStu_pwd(),stu.getStu_department(),stu.getStu_phone())==1) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String addStudent(String stu_name,String stu_pwd,String stu_department, String stu_birth, String stu_phone){
		String sql = "insert into student(stu_name,stu_pwd,stu_department, stu_birth ,stu_phone) values(?,?,?,?,?)";
		try {
			if(JdbcUtil.update(sql,stu_name,stu_pwd,stu_department,stu_birth,stu_phone)==1) {
				String sql1 = "SELECT stu_id FROM `student`where stu_name=? and stu_pwd=? and stu_department=? and stu_birth=? and stu_phone=? ";
				List<Map<String, Object>> dataList = JdbcUtil.select(sql1, stu_name,stu_pwd,stu_department,stu_birth,stu_phone);
				return dataList.get(0).get("stu_id").toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Student searchStudent(String stu_id,String stu_pwd) {
		String sql = "SELECT stu_id,stu_name,stu_pwd,stu_department,stu_birth,stu_phone FROM `student` where stu_id=? and stu_pwd=? and true=true";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql, stu_id,stu_pwd);
			if(dataList.isEmpty()!=true) {
				Student student = new Student();
				student.setStu_id(dataList.get(0).get("stu_id").toString());
				student.setStu_name((String)dataList.get(0).get("stu_name"));
				student.setStu_pwd((String)dataList.get(0).get("stu_pwd"));
				student.setStu_department((String)dataList.get(0).get("stu_department"));
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				Date date = (Date)dataList.get(0).get("stu_birth");
				student.setStu_birth(df.format(date));
				student.setStu_phone((String)dataList.get(0).get("stu_phone"));
				System.out.println(student);
				return student;			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//选课
	public static int addCourse(String stu_id,String course_id,String tea_id) {
		String sql = "insert into stu_course(stu_id,course_id,tea_id) values(?,?,?)";
		int i=0;
		try {
			if(JdbcUtil.update(sql,stu_id,course_id,tea_id)==1) {
				i=1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	//查课
	public static List<Map<String, Object>> searchCourse(String stu_id) {
		String sql = "select course.course_id,course.course_name,course.course_department,teacher.tea_id,teacher.tea_name,stu_course.score\r\n" + 
				"From student,teacher,course,stu_course\r\n" + 
				"where student.stu_id=stu_course.stu_id and teacher.tea_id = stu_course.tea_id and course.course_id=stu_course.course_id and student.stu_id=?";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql, stu_id);
			if(dataList.isEmpty()!=true) {
				return dataList;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//查看所有可选课程
	public static List<Map<String, Object>> allCourse() {
		String sql = "select DISTINCT course.course_id,course.course_name,teacher.tea_id,teacher.tea_name FROM course,teacher,tea_course WHERE teacher.tea_id=tea_course.tea_id AND course.course_id=tea_course.course_id ORDER BY course.course_id ";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql);
			if(dataList.isEmpty()!=true) {
				return dataList;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//退课
	public static int deleteCourse(String stu_id,String course_id,String tea_id) {
		String sql = "delete from stu_course where stu_id=? and course_id=? and tea_id=?";
		try {
			if (JdbcUtil.update(sql,stu_id,course_id,tea_id) == 1) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
}
