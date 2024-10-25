package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.MemberVo;

public class MemberDao {	// MVC 방식으로 가기전에 첫번째 model1 방식 (설계)
	
	private Connection conn;
	private PreparedStatement pstmt; // 전역변수로 사용해서 페이지 어느곳에서도 사용할 수 있다. 
	
	// 생성자를 통해서 db연결해서 메소드 사용
	public MemberDao() {
		Dbconn dbconn = new Dbconn(); // DB 객체 생성
		conn = dbconn.getConnection(); // 메소드 호출해서 연결객체를 가져온다. 	
	}
	
	
	
	//어디서나 접근가능 public,   리턴값타입은 숫자형int = 메소드타입과 같음,  각 매개변수(파라미터변수-전달변수)
	public int memberInsert(String memberId, String memberPw,
			String memberName,String memberGender,String memberBirth,
			String memberAddr, String memberPhone,String memberEmail,String memberInHobby){
	
	int value=0;   //메소드 지역변수  결과값을 담는다
	String sql ="";
	pstmt = null;   //쿼리 구문클래스 선언
	
	try{
		
		   sql ="insert into member(memberid,memberpw,membername," 
		    		+ "membergender,memberbirth,memberaddr,memberphone,"
		    		+ "memberemail,memberhobby) values(?,?,?,?,?,?,?,?,?)";    
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, memberId);              //문자형 메소드 사용
		    pstmt.setString(2, memberPw); 			 //문자형 메소드 사용  숫자형 setInt(번호,값);
		    pstmt.setString(3, memberName);
		    pstmt.setString(4, memberGender);
		    pstmt.setString(5, memberBirth);
		    pstmt.setString(6, memberAddr);
		    pstmt.setString(7, memberPhone);
		    pstmt.setString(8, memberEmail);
		    pstmt.setString(9, memberInHobby);
		    value = pstmt.executeUpdate();      //구문객체 실행하면 성공시 1 실패시 0리턴		
		
	}catch(Exception e){
		e.printStackTrace();		
	}finally{                    //try를 했던 catch를 했던 꼭 실행해야하는 영역
		//객체 사라지게하고
		//db연결 끊기		
		try{
			pstmt.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	return value;
	}
	
	
	
	// 로그인해서 회원정보를 가져오는 메소드 
	public MemberVo memberLoginCheck(String memberId, String memberPw) {
	
		MemberVo mv = null;
		
		String sql = "select * from member where memberid =? and memberpw =?";
		ResultSet rs = null;	// db에서 결과 데이터를 받아오는 전용 클래스 
		
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);
			rs = pstmt.executeQuery();   // 결과가 나온것을 rs에 담겠다. 
			
			
			if(rs.next()==true) {  // 커서가 이동해서 데이터 값이 있으면. if(rs.next())와 같은 표현
				String memberid =  rs.getString("memberid"); // 결과값에서 아이디값을 뽑는다.
				int midx = rs.getInt("midx"); 	// 결과값에서 회원번호를 뽑는다.
				String membername = rs.getString("membername");   // 이름도 불러오기 
				
				mv = new MemberVo();   // 화면에 가지고 갈 데이터를 담을 MemberVo라는 객체를 생성한다.
				mv.setMemberid(memberid); // 옮겨 담는다.
				mv.setMidx(midx);
				mv.setMembername(membername);   
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return mv;
	}
	
	
								// 멤버의 모든것을 가져올거다
	public ArrayList<MemberVo> memberSelectAll() {  
		
		ArrayList<MemberVo> alist = new ArrayList<MemberVo>();  //한줄에 해당하는 컬럼값을 배열에 담는다.
		String sql = "select * from member where delyn='N' order by midx desc"; // db에서 작성한 쿼리를 가져온다
		ResultSet rs =null; // DB값을 가져오기 위한 전용 클래스 
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//여러개의 값을 담아야 하기 때문에 반복문을 작성해야한다. 
			while(rs.next()) { // 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				int midx = rs.getInt("midx");
				String memberId = rs.getString("memberid");
				String memberName = rs.getString("membername");
				String memberGender = rs.getString("membergender");
				String writeday = rs.getString("writeday");
				
				MemberVo mv = new MemberVo();	// 첫 행부터 mv에 옮겨담기
				mv.setMidx(midx);
				mv.setMemberid(memberId);
				mv.setMembername(memberName); 
				mv.setMembergender(memberGender); 
				mv.setWriteday(writeday);
				alist.add(mv);   // ArrayList 객체에 mv값을 하나씩 넣는다.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();			 // 연결을 끊어준다. 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alist; // alist에 값을 담는다.
	}
	
	
	
	
	
	
	
	// 로그인해서 회원정보를 가져오는 메소드 // 중복체크
		public int memberIdCheck(String memberId) {
		
			MemberVo mv = null;
			
		
			String sql = "select count(*) as cnt from member where memberid =?";
			ResultSet rs = null;	// db에서 결과 데이터를 받아오는 전용 클래스 
			int cnt = 0;
			
			try {
				pstmt = conn.prepareStatement(sql); 
				pstmt.setString(1, memberId);
				rs = pstmt.executeQuery();   // 결과가 나온것을 rs에 담겠다. 
				
				
				if(rs.next()) {  // 커서가 이동해서 데이터 값이 있으면. if(rs.next())와 같은 표현
					cnt = rs.getInt("cnt"); 	// 결과값에서 회원번호를 뽑는다. 
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					rs.close();
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
			return cnt;
		}
		

	}
		
