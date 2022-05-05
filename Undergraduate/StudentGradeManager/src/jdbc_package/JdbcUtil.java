package jdbc_package;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JdbcUtil {
	
	private static String DRIVER="com.mysql.cj.jdbc.Driver";// the MySQL driver
	private static String URL="jdbc:mysql://localhost:3306/myfirstdatabase?characterEncoding=UTF8";// URL points to destination database to manipulate
	private static String USER="root";//user name for the specified database
	private static String PWD="981220";//the corresponding password
	
	static{
		//ע����������
		try {
			
			/**
			 * ��ȡjdbc.properties�ļ�
			 */
			//1)����Properties����
			Properties prop = new Properties();
			//����������
			/**
			 * ���·����  .  ��������ǰĿ¼����ǰĿ¼��������java�������е�Ŀ¼
			 * java��Ŀ��  ��ecplise�У���ǰĿ¼ָ����Ŀ�ĸ�Ŀ¼��
			 * web��Ŀ�� ��ǰĿ¼ָ��%tomcat%/binĿ¼
			 *   1)���ۣ� ��web��Ŀ����ʹ�����·��
			 *   
			 *   web��Ŀ�м��������ļ��� ServletContext.getRealPath()  /  getResourceAsStream() ���ַ�ʽ����jdbcUtil������߶��ԣ��ŵ�java��Ŀ���Ҳ���ServletContext���󣬲�ͨ�õģ�
			 *   2)����ʹ��ServletContext��ȡ�ļ�
			 *   
			 *   3��ʹ����·����ʽ��ȡ�����ļ�
			 *   
			 */
			//1)��ȡ��Ķ���
		    Class clazz = JdbcUtil.class;
		    //2) ʹ����·���Ķ�ȡ����ȥ��ȡ�ļ�
		    /**
		     *   ���б�ܣ�������Ŀ����·���ĸ�Ŀ¼��  ��·���� ��ѯ���Ŀ¼/·��
		     *   java��Ŀ�£� ��·���ĸ�Ŀ¼��ָ����Ŀ��binĿ¼
		     *   web��Ŀ�£���·���ĸ�Ŀ¼��ָ����Ŀ��WEB-INF/classesĿ¼
		     *   
		     *   ֻ�а������ļ�����srcĿ¼�ĸ�Ŀ¼�£���ô��Щ�ļ��ͻ��Զ���������Ŀ����·����Ŀ¼�¡�
		     */
		    InputStream in = clazz.getResourceAsStream("./jdbc.properties");
		    // File f = new File(clazz.getResource("/").getFile());
			// System.out.println(f.getAbsolutePath());
			//����������
		    // InputStream in = new FileInputStream("./src/jdbc.properties");
			//2)�����ļ�
			prop.load(in);
			//3)��ȡ�ļ�����
			URL = prop.getProperty("url");
			USER = prop.getProperty("user");
			PWD = prop.getProperty("pwd");
			DRIVER = prop.getProperty("driver");
			
			// �����Ƿ��properties�ж���
			System.out.println(URL);
			System.out.println(USER);
			System.out.println(PWD);
			System.out.println(DRIVER);
			
			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException {
		JdbcUtil jUtil = new JdbcUtil();
		String sqlString = "SELECT User_id FROM teacher WHERE User_name='���'";
		String sqlString1 = "SELECT User_id FROM teacher";
		String sqlString2 = "insert into teacher(User_name) values(?)";
		List<Map<String, Object>> list = jUtil.select(sqlString1);
		for(Map map:list) {
			System.out.println(map);
		}
		jUtil.update(sqlString2, "������");
	}
	
	//�������ݿ������
	public static Connection getConnection() {
		try {
			Connection connection = DriverManager.getConnection(URL, USER, PWD);
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	public static void close(Statement stmt,Connection conn){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void close(ResultSet rs,Statement stmt,Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	public static int update(String sql,Object... param) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		try{
			con = DriverManager.getConnection(URL,USER,PWD);
			pstmt = con.prepareStatement(sql);
			if(param!=null){
				for(int i=0;i<param.length;i++){
					pstmt.setObject(i+1, param[i]);
				}
			}
			return pstmt.executeUpdate();
		}catch(SQLException e){
			System.out.println("�Բ���ϵͳ��������ϵ����Ա");
			throw e;
		}finally{
			close(null,pstmt,con);
		}
	}

	public static List<Map<String,Object>> select(String sql,Object... param) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try{
			con = DriverManager.getConnection(URL,USER,PWD);
			pstmt = con.prepareStatement(sql);
			if(param!=null){
				for(int i=0;i<param.length;i++){
					pstmt.setObject(i+1, param[i]);
				}
			}
			res = pstmt.executeQuery();
			ResultSetMetaData rsmd = res.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			while(res.next()){
				Map<String,Object> rowData = new HashMap<String,Object>();
				for(int i=1;i<=columnCount;i++){
					String columnLable = rsmd.getColumnLabel(i);
					Object value = res.getObject(i);
					rowData.put(columnLable, value);
				}
				data.add(rowData);
			}
			return data;
		}catch(SQLException e){
			System.out.println("�Բ���ϵͳ��������ϵ����Ա");
			throw e;
		}finally{
			close(res,pstmt,con);
		}
	}
}
