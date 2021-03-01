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
		//注册驱动程序
		try {
			
			/**
			 * 读取jdbc.properties文件
			 */
			//1)创建Properties对象
			Properties prop = new Properties();
			//构造输入流
			/**
			 * 相对路径：  .  这个点代表当前目录。当前目录本质上是java命令运行的目录
			 * java项目：  在ecplise中，当前目录指向项目的根目录。
			 * web项目： 当前目录指向%tomcat%/bin目录
			 *   1)结论： 在web项目不能使用相对路径
			 *   
			 *   web项目中加载配置文件： ServletContext.getRealPath()  /  getResourceAsStream() 这种方式对于jdbcUtil这个工具而言，放到java项目中找不到ServletContext对象，不通用的！
			 *   2)不能使用ServletContext读取文件
			 *   
			 *   3）使用类路径方式读取配置文件
			 *   
			 */
			//1)获取类的对象
		    Class clazz = JdbcUtil.class;
		    //2) 使用类路径的读取方法去读取文件
		    /**
		     *   这个斜杠：代表项目的类路径的根目录。  类路径： 查询类的目录/路径
		     *   java项目下： 类路径的根目录，指向项目的bin目录
		     *   web项目下：类路径的根目录，指向项目的WEB-INF/classes目录
		     *   
		     *   只有把配置文件放在src目录的根目录下，那么这些文件就会自动拷贝到项目的类路径根目录下。
		     */
		    InputStream in = clazz.getResourceAsStream("./jdbc.properties");
		    // File f = new File(clazz.getResource("/").getFile());
			// System.out.println(f.getAbsolutePath());
			//构造输入流
		    // InputStream in = new FileInputStream("./src/jdbc.properties");
			//2)加载文件
			prop.load(in);
			//3)读取文件内容
			URL = prop.getProperty("url");
			USER = prop.getProperty("user");
			PWD = prop.getProperty("pwd");
			DRIVER = prop.getProperty("driver");
			
			// 测试是否从properties中读到
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
		String sqlString = "SELECT User_id FROM teacher WHERE User_name='大飞'";
		String sqlString1 = "SELECT User_id FROM teacher";
		String sqlString2 = "insert into teacher(User_name) values(?)";
		List<Map<String, Object>> list = jUtil.select(sqlString1);
		for(Map map:list) {
			System.out.println(map);
		}
		jUtil.update(sqlString2, "甘霖娘");
	}
	
	//建立数据库的连接
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
			System.out.println("对不起系统错误，请联系管理员");
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
			System.out.println("对不起系统错误，请联系管理员");
			throw e;
		}finally{
			close(res,pstmt,con);
		}
	}
}
