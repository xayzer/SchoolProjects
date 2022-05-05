package myJavabean;

public class Teacher {
	private static final long serialVersionUID = 1L;
	private String tea_id;
	private String tea_name;
	private String tea_pwd;
	private String tea_department;
	private String tea_phone;
	
	public Teacher() {
		// TODO Auto-generated constructor stub
	}
	
	public String getTea_id() {
		return tea_id;
	}

	public void setTea_id(String tea_id) {
		this.tea_id = tea_id;
	}

	public String getTea_name() {
		return tea_name;
	}
	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}
	public String getTea_pwd() {
		return tea_pwd;
	}
	public void setTea_pwd(String tea_pwd) {
		this.tea_pwd = tea_pwd;
	}
	public String getTea_department() {
		return tea_department;
	}
	public void setTea_department(String tea_department) {
		this.tea_department = tea_department;
	}
	public String getTea_phone() {
		return tea_phone;
	}
	public void setTea_phone(String tea_phone) {
		this.tea_phone = tea_phone;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Teacher [tea_id=" + tea_id + ", tea_name=" + tea_name + ", tea_pwd=" + tea_pwd + ", tea_department="
				+ tea_department + ", tea_phone=" + tea_phone + "]";
	}

}
