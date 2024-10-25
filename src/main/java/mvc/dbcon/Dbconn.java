package mvc.dbcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Dbconn {
	
	
	private Connection conn;   // 멤버변수는 선언한 해도 자동 초기화가 된다. 그래서 = null; 이라고 안해도 된다
	private String url="jdbc:mysql://127.0.0.1/aws0822?serverTimezone=UTC";
	private String user="root";
	private String password="1234";
	
	public Connection getConnection() {
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
//	System.out.println("객체생성확인==> " + conn);  // 디버깅 코드입니당 
	return conn;	// 연결객체가 생겨났을 때의 객체 정보를 담고 있는 객체참조변수 
					// null값이면 sql과 연결이 되지 않았다는 뜻
	}
}
