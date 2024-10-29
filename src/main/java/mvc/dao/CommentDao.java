package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.CommentVo;
import mvc.vo.Criteria;
import mvc.vo.MemberVo;
import mvc.vo.SearchCriteria;

public class CommentDao {
	
	
	private Connection conn; // 4. 연결 객체를 전역적으로 쓴다.
	private PreparedStatement pstmt; // 8. 전역변수를 설정
	
	// 0. 컨트롤러를 생성 했으면 그 다음 dao를 생성해라 
	public CommentDao() { // 1. 맨처음 생성자를 만든다. 왜? : DB연결하는 Dbconn 객체 생성하기 위해... 
						//생성을 해야 mysql접속이 가능하다
	
		Dbconn db = new Dbconn();  // 2. conn 연결 객체 생성하기
		this.conn = db.getConnection(); // 3. 메소드를 실행해서 연결하기  getConnection() : 데이터베이스 연결을 생성하거나 가져오는 메서드
	}
	
	
	// 게시판 목록 구현하기 
	//데이터베이스에서 게시판의 모든 게시글을 조회하여 배열 형태로 반환하는 메소드
	public ArrayList<CommentVo> commentSelectAll(int bidx) { // 4. 구문 클래스 생성하기: 형식부터 만들어라
		
		ArrayList<CommentVo> alist = new ArrayList<CommentVo>();  
	
		String sql = "select * from comment where delyn='N' and bidx=? order by cidx desc;"; // 2-41. 리미트 설정 다시 컨트롤러로
		ResultSet rs =null; // 7. DB값을 가져오기 위한 전용 클래스 (SQL 쿼리의 결과를 저장하고 조작하는 데 사용되는 인터페이스)
		
		try {
			pstmt = conn.prepareStatement(sql); // 9. SQL문을 실행하기 위해 준비하는 코드
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery(); 
			
			//11. 여러개의 값을 담아야 하기 때문에 반복문으로 작성해야한다. 
			while(rs.next()) { // 12. 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				int cidx = rs.getInt("cidx");
				String ccontents = rs.getString("ccontents");
				String cwriter = rs.getString("cwriter");
				String writeday = rs.getString("writeday");
				String delyn = rs.getString("delyn");
				int midx = rs.getInt("midx");
				
				CommentVo cv = new CommentVo();	// 13. 첫 행부터 cv에 옮겨담기
				
				cv.setCidx(cidx);
				cv.setCcontents(ccontents);
				cv.setCwriter(cwriter);
				cv.setWriteday(writeday); 
				cv.setDelyn(delyn);
				cv.setMidx(midx);
				alist.add(cv);   // 14. ArrayList 객체에 bv값을 하나씩 넣는다.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();	 // 15. 연결을 끊어줘야 합니다. 다른 프로그램 사용자가 사용할 수 없음. 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alist; // alist에 값을 담는다.
	} 
	 

	
		/*
	// 페이지 갯수를 뽑아내는거니까 int형
	// 2-5. 게시판 전체 갯수 구하기 
	public int boardTotalCount(SearchCriteria scri) {
		
		String str = "";   //이미 초기화가 빈값이니까 else를 하지 않아도 된다. 
		String keyword = scri.getKeyword();
		String searchType = scri.getSearchType();
		// 키워드가 존재한다면 like 구문을 활용한다.
		if(!scri.getKeyword().equals("")) {   //키워드가 없는게 아니라면 (존재한다면)
			
			str = "and " + searchType + " like concat('%','"+keyword+"','%')";   // 키워드를 찾는 구문 추가 
		}
		
		int value=0;
		// 2-6. 쿼리 만들기
		String sql = "select count(*) as cnt from board where delyn='N' "+str+" ";
		// 2-7 conn 객체 안에 있는 구문 클래스 호출하기 
		// 2-8 DB 컬럼값을 받는 전용 클래스 REsultSet 호출(특징은 데이터를 그대로 복사하기 떼문에 전달이 빠르다)
		ResultSet rs = null;
		try {
			pstmt=conn.prepareStatement(sql); // 2-8 conn을 전역변수로 설정했기 때문에 호출 
			rs = pstmt.executeQuery();
			
			// 2-9 if구문 작성
			if(rs.next()) { // 2-10 커서를 이동시켜서 첫줄로 옮긴다.
				value = rs.getInt("cnt"); //2-11 지역변수 value에 담아서 리턴해서 가져간다. 
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {				 // 2-12. 각 객체도 소멸시키고 DB연결 끊는다.
				rs.close();
				pstmt.close();
			//	conn.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return value;
	}
	*/
	
	
	public int commentInsert(CommentVo cv) {
		
		int value =0;
		
		String cwriter = cv.getCwriter();
		String ccontents = cv.getCcontents();
		String csubject = cv.getCsubject();
		int bidx = cv.getBidx();
		int midx = cv.getMidx();
		String cip = cv.getCip();
		
		String sql ="insert into comment(csubject,ccontents,cwriter,bidx,midx,cip)"
				+ "value(null,?,?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,ccontents);
			pstmt.setString(2,cwriter);
			pstmt.setInt(3,bidx);
			pstmt.setInt(4,midx);
			pstmt.setString(5,cip);
			value = pstmt.executeUpdate();	// 실행되면 1 안되면 0
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {		
				pstmt.close();
			    conn.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}	
		return value;
	}

	
	
	
	
		
	 // 게시물 삭제 메소드 
	public int commentDelete(int cidx) {  
		
		int value = 0;
		String sql = "update comment set delyn='Y' where cidx = ?"; //특정 cidx를 가진 게시글의 delyn 컬럼을 'Y'로 업데이트
	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,cidx);
			value = pstmt.executeUpdate(); 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
			    conn.close();		
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return value;
	}
	
	}

	

	
	
	
	
	
	
	

