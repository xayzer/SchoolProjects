package jdbc_package;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import myJavabean.Teacher;

public class TeacherOp {

	public static void main(String[] args) {
		System.out.println(setScore("1", 100));
	}

	public static int addTeacher(Teacher tea) {
		String sql = "insert into Teacher(tea_name,tea_pwd,tea_department,tea_phone) values(?,?,?,?)";
		try {
			if (JdbcUtil.update(sql, tea.getTea_name(), tea.getTea_pwd(), tea.getTea_department(),
					tea.getTea_phone()) == 1) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String addTeacher(String tea_name, String tea_pwd, String tea_department, String tea_phone) {
		String sql = "insert into teacher(tea_name,tea_pwd,tea_department,tea_phone) values(?,?,?,?)";
		try {
			if (JdbcUtil.update(sql, tea_name, tea_pwd, tea_department, tea_phone) == 1) {
				String sql1 = "SELECT tea_id FROM `teacher`where tea_name=? and tea_pwd=? and tea_department=? and tea_phone=? ";
				List<Map<String, Object>> dataList = JdbcUtil.select(sql1, tea_name, tea_pwd, tea_department,
						tea_phone);
				return dataList.get(0).get("tea_id").toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Teacher searchTeacher(String tea_id, String tea_pwd) {
		String sql = "SELECT tea_id,tea_name,tea_pwd,tea_department,tea_phone FROM `teacher` where tea_id=? and tea_pwd=? and true=true";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql, tea_id, tea_pwd);
			if (dataList.isEmpty() != true) {
				Teacher teacher = new Teacher();
				teacher.setTea_id(dataList.get(0).get("tea_id").toString());
				teacher.setTea_name((String) dataList.get(0).get("tea_name"));
				teacher.setTea_pwd((String) dataList.get(0).get("tea_pwd"));
				teacher.setTea_department((String) dataList.get(0).get("tea_department"));
				teacher.setTea_phone((String) dataList.get(0).get("tea_phone"));
				return teacher;
			}

			// teacher.setTea_id();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 选课
	public static int linkCourse(String tea_id, String course_id) {
		String sql = "insert into tea_course(tea_id,course_id) values(?,?)";
		try {
			if (JdbcUtil.update(sql, tea_id, course_id) == 1) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// 开课
	public static int setCourse(String tea_id, String course_name, String course_department) {
		String sql = "insert into course(course_name,course_department) values(?,?)";
		int i = 0;
		try {
			if (JdbcUtil.update(sql, course_name, course_department) == 1) {
				String sql1 = "SELECT course_id FROM `course`where course_name=? and course_department=?";
				List<Map<String, Object>> dataList = JdbcUtil.select(sql1, course_name, course_department);
				String course_id = dataList.get(0).get("course_id").toString();
				i = linkCourse(tea_id, course_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	// 获得sc_id
	public static String getSc_id(String stu_id, String course_id, String tea_id) {
		String sc_id = null;
		String sql = "SELECT distinct sc_id FROM `stu_course`where stu_id=? and course_id=? and tea_id=?";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql, stu_id, course_id, tea_id);
			sc_id = dataList.get(0).get("sc_id").toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sc_id;
	}

	// 评分
	public static int setScore(String sc_id, int score) {
		String sql = "update stu_course set score=? where sc_id=?";
		try {
			if (score <= 100 && score > 0) {
				if (JdbcUtil.update(sql, score, sc_id) == 1) {
					return 1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	//获取教师所教学生
	public static List<Map<String, Object>> getStudent(String tea_id) {
		String sql = "select DISTINCT stu_course.sc_id,student.stu_id,student.stu_name,course.course_id,course.course_name,stu_course.score\r\n" + 
				"From student,teacher,course,stu_course\r\n" + 
				"where student.stu_id=stu_course.stu_id and teacher.tea_id = stu_course.tea_id and course.course_id=stu_course.course_id and teacher.tea_id=? ORDER BY stu_course.sc_id";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql, tea_id);
			if (dataList.isEmpty() != true) {
				return dataList;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//获取教师所教课程
	public static List<Map<String, Object>> getCourse(String tea_id) {
		String sql = "select DISTINCT course.course_id,course.course_name FROM course,tea_course where "
				+ "tea_course.course_id=course.course_id and tea_course.tea_id=? ORDER BY course.course_id";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql, tea_id);
			if (dataList.isEmpty() != true) {
				return dataList;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//已开设的所有课程
	public static List<Map<String, Object>> getAllCourse() {
		String sql = "select DISTINCT course.course_id,course.course_name FROM course ORDER BY course.course_id";
		try {
			List<Map<String, Object>> dataList = JdbcUtil.select(sql);
			if (dataList.isEmpty() != true) {
				return dataList;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//删除课程
	public static int deleteCourse(String tea_id, String course_id) {
		String sql = "delete from tea_course where tea_id=? and course_id=?";
		try {
			if (JdbcUtil.update(sql,tea_id,course_id) == 1) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
