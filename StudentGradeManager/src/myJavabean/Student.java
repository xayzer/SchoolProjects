package myJavabean;

import java.io.Serializable;

public class Student implements Serializable {
	private static final long serialVersionUID = 1L;
	private String stu_id;
	private String stu_name;
	private String stu_pwd;
	private String stu_department;
	private String stu_birth;
	
	
	public String getStu_id() {
		return stu_id;
	}

	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}

	public String getStu_birth() {
		return stu_birth;
	}

	public void setStu_birth(String stu_birth) {
		this.stu_birth = stu_birth;
	}

	private String stu_phone;

	// private String stu_icon;
	public Student() {
		// TODO Auto-generated constructor stub
	}

	public String getStu_name() {
		return stu_name;
	}

	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}

	public String getStu_pwd() {
		return stu_pwd;
	}

	public void setStu_pwd(String stu_pwd) {
		this.stu_pwd = stu_pwd;
	}

	public String getStu_department() {
		return stu_department;
	}

	public void setStu_department(String stu_department) {
		this.stu_department = stu_department;
	}

	public String getStu_phone() {
		return stu_phone;
	}

	public void setStu_phone(String stu_phone) {
		this.stu_phone = stu_phone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Student [stu_id=" + stu_id + ", stu_name=" + stu_name + ", stu_pwd=" + stu_pwd + ", stu_department="
				+ stu_department + ", stu_birth=" + stu_birth + ", stu_phone=" + stu_phone + "]";
	}
	
}
