package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.MemberVo;
import mvc.vo.SearchCriteria;

public class BoardDao {
	
	
	private Connection conn; // 4. 연결 객체를 전역적으로 쓴다.
	private PreparedStatement pstmt; // 8. 전역변수를 설정
	
	// 0. 컨트롤러를 생성 했으면 그 다음 dao를 생성해라 
	public BoardDao() { // 1. 맨처음 생성자를 만든다. 왜? : DB연결하는 Dbconn 객체 생성하기 위해... 
						//생성을 해야 mysql접속이 가능하다
	
		Dbconn db = new Dbconn();  // 2. conn 연결 객체 생성하기
		this.conn = db.getConnection(); // 3. 메소드를 실행해서 연결하기  getConnection() : 데이터베이스 연결을 생성하거나 가져오는 메서드
	}
	
	
	// 게시판 목록 구현하기 
	//데이터베이스에서 게시판의 모든 게시글을 조회하여 배열 형태로 반환하는 메소드
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri) { // 4. 구문 클래스 생성하기: 형식부터 만들어라
		
		int page = scri.getPage();    // 페이지번호
		int perPageNum = scri.getPerPageNum();  // 2-44 화면 노출 리스트 갯수
		
		String str = "";   //이미 초기화가 빈값이니까 else를 하지 않아도 된다. 
		String keyword = scri.getKeyword();
		String searchType = scri.getSearchType();
		// 키워드가 존재한다면 like 구문을 활용한다.
		if(!scri.getKeyword().equals("")) {   //키워드가 없는게 아니라면 (존재한다면)
			
			str = "and " + searchType + " like concat('%','"+keyword+"','%')";   // 키워드를 찾는 구문 추가 
		}
		 
		
		// 5. ArrayList 컬렉션 객체에 보드Vo를 담겠다. 보드VO는 컬럼값을 담겠다.
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();    
						
		// 6. db에서 작성한 쿼리를 가져온다
		String sql = "select * from board where delyn='N' "+str+" order by originbidx desc, depth asc limit ?,?"; // 2-41. 리미트 설정 다시 컨트롤러로
		ResultSet rs =null; // 7. DB값을 가져오기 위한 전용 클래스 (SQL 쿼리의 결과를 저장하고 조작하는 데 사용되는 인터페이스)
		
		try {
			pstmt = conn.prepareStatement(sql); // 9. SQL문을 실행하기 위해 준비하는 코드
			pstmt.setInt(1,(page-1)*perPageNum);  // 2-45 
			pstmt.setInt(2,perPageNum);		  // 2-46 하나도 모르겠어... 끝?
			rs = pstmt.executeQuery(); // 10. 데이터베이스 쿼리를 실행하고, 그 결과를 ResultSet 객체에 저장한다.
			
			//11. 여러개의 값을 담아야 하기 때문에 반복문으로 작성해야한다. 
			while(rs.next()) { // 12. 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				int bidx = rs.getInt("bidx");
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String writeday = rs.getString("writeday");
				int level_ = rs.getInt("level_");
				
				
				BoardVo bv = new BoardVo();	// 13. 첫 행부터 vb에 옮겨담기
				bv.setBidx(bidx);
				bv.setSubject(subject);
				bv.setWriter(writer); 
				bv.setViewcnt(viewcnt);
				bv.setRecom(recom);
				bv.setWriteday(writeday);
				bv.setLevel_(level_);
				alist.add(bv);   // 14. ArrayList 객체에 bv값을 하나씩 넣는다.
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
	
	
	//17. 이제 메소드 만든걸 컨트롤러에서 불러야겠죠?? 컨트롤러로 갑니다
	
	
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
	
	
	public int boardInsert(BoardVo bv) {
		int value =0;
		
		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String password = bv.getPassword();
		int midx = bv.getMidx();
		String filename = bv.getFilename();
		String ip = bv.getIp();
		
		String sql ="insert into board(originbidx,depth,level_,subject,contents,writer,password,midx,filename,ip)"
				+ "value(null,0,0,?,?,?,?,?,?,?)";
		
		String spl2 = "update board \r\n"
	            + "set originbidx = (select * from (select max(bidx) from board) as temp) \r\n"
	            + "where bidx = (select * from (select max(bidx) from board) as temp)";
		try {
			conn.setAutoCommit(false);  // 수동 커밋으로 하겠다.
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, password);
			pstmt.setInt(5, midx);
			pstmt.setString(6, filename);
			pstmt.setString(7,ip);
			int exec = pstmt.executeUpdate();	// 실행되면 1 안되면 0
			
			pstmt = conn.prepareStatement(spl2);
			int exec2 = pstmt.executeUpdate();	// 실행되면 1 안되면 0
			
			conn.commit(); // 수동커밋 일괄처리
			
			value = exec + exec2;
			
		} catch (SQLException e) {
		
			try {
				conn.rollback();    // 수동커밋 실행중 오류 발생시 롤백처리하기 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
	
	/*회원정보 가져오기 메소드*/ //게시물 내용 가져오기 ?
	public BoardVo boardSelectOne(int bidx) {
		// 1. 형식부터 만든다. 
		BoardVo bv= null;  /*회원정보가 Vo에 있기 때문에 접근*/ 
		// 2. 사용할 쿼리를 준비한다.
		String sql = "select * from board where delyn='N' and bidx=?";  //넘기는 값에 따라서 bidx가 달라져야 하기 때문에 ?처리
		
		ResultSet rs = null;
		try {
			// 3. 연결 객체에서 쿼리실행 구문 클래스를 불러온다.
			pstmt = conn.prepareStatement(sql); // 멤버변수(전역변수)로 선언한 PreparedStatment 객체로 담은
			pstmt.setInt(1, bidx);	// 첫번째 물음표에 매개변수 bidx값을 담아서 구문을 완성한다.
			rs = pstmt.executeQuery();	// 쿼리를 실행해서 결과값을 컬럼 전용 클래스인 ResultSet 객체에 담는다.(복사기능) 한번에 담기
			System.out.println("Daosql"+sql);
			if(rs.next()==true) {   //rs.next()는 커서를 다음줄로 이동시킨다. 맨 처음 커서는 상단에 위치되어있다. 
				// 값이 존재한다면 BoardVo 객체에 담는다. 
				
				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				String writeday = rs.getString("writeday");
				int viewcnt = rs.getInt("viewcnt");				//화면에 나타나는 데이터들을 전부 꺼내온다. 
				int recom = rs.getInt("recom");
				String filename = rs.getString("filename");
				int rtnBidx = rs.getInt("bidx");			// 답변하기 기능 때문에 다 가져와야 한다. 
				int originbidx = rs.getInt("originbidx");
				int depth = rs.getInt("depth");
				int levle_ = rs.getInt("level_");
				String password = rs.getString("password");
				
				
				 bv = new BoardVo();	// 객체 생성해서 지역변수 bv로 담아서 리턴해서 가져간다. 
				 bv.setSubject(subject);
				 bv.setContents(contents);
				 bv.setWriter(writer);
				 bv.setWriteday(writeday);
				 bv.setViewcnt(viewcnt);
				 bv.setRecom(recom);
				 bv.setFilename(filename);
				 bv.setBidx(rtnBidx);
				 bv.setOriginbidx(originbidx);
				 bv.setDepth(depth);
				 bv.setLevel_(levle_);
				 bv.setPassword(password);
				 System.out.println("Daobv"+bv);
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
			return bv;
		}
	
	
	
	// 게시물 수정하기 
	public int boardUpdate(BoardVo bv) {
		
		int value = 0;   //업데이트 구문 새로운 내용을 입혀라. 
		String sql = "update board set subject =?, contents =?, writer=?, modifyday= now() where bidx=? and password=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,bv.getSubject());
			pstmt.setString(2,bv.getContents());
			pstmt.setString(3,bv.getWriter());
			pstmt.setInt(4,bv.getBidx());
			pstmt.setString(5,bv.getPassword());
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

	
	
	// 조회수 올리기 
	public int boardViewCntUpdate(int bidx) {
		
		int value = 0;
		String sql ="update board SET viewcnt = viewcnt+1 where bidx =?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx);
			value = pstmt.executeUpdate(); // 성공하면 1 실패하면 0

		} catch (SQLException e) {
			e.printStackTrace(); 
			}finally {
				try {
					pstmt.close();
				//    conn.close();		
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}		
		return value;
	}

	
	
		// 추천수 올리기 
	public int boardRecomUpdate(int bidx) {
		
		int value = 0;
		int recom = 0;
		String sql ="update board set recom= recom+1 where bidx=?"; // 추천 수를 증가시키는 SQL 쿼리
		String sql2 = "select recom from board where bidx=?"; // 추천 수를 조회하는 SQL 쿼리
		ResultSet rs = null;  // 결과 집합을 저장할 변수
		
		try {
			pstmt = conn.prepareStatement(sql); // 첫 번째 SQL 쿼리 준비
			pstmt.setInt(1,bidx);
			value = pstmt.executeUpdate(); // 성공하면 1 실패하면 0
			
			pstmt = conn.prepareStatement(sql2); // 두 번째 SQL 쿼리 준비
			pstmt.setInt(1,bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				recom = rs.getInt("recom");
			}

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
		return recom;
	}
	
	
	 // 게시물 삭제 메소드 
	public int boardDelete(int bidx ,String password) {  
		
		int value = 0;
		String sql = "update board set delyn='Y' where bidx = ? and password=?"; //특정 bidx를 가진 게시글의 delyn 컬럼을 'Y'로 업데이트
	
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx);
			pstmt.setString(2,password);
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

	
	//답변달기 
public int boardReply(BoardVo bv) {
		
		int value=0;
		int maxbidx=0;
		String sql="update board set depth= depth+1 where originbidx =?  and depth > ?";   //답글을 추가할 때 기존 답글들의 depth 값을 증가시켜 순서를 조정
		String sql2="insert into board (originbidx,depth,level_,subject,contents,writer,midx,filename,password,ip) "
					 + "values(?,?,?,?,?,?,?,?,?,?)";    						//새로운 게시글 또는 답글을 board 테이블에 추가
		String sql3 ="select max(bidx) as maxbidx from board where originbidx=?"; //가장 최근에 작성된 게시글 또는 답글의 bidx 값을 확인할 때 사용
		
		try {
			conn.setAutoCommit(false);   //수동커밋으로 하겠다
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth());		
			int exec = pstmt.executeUpdate();    //실행되면 1 안되면 0
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth()+1);
			pstmt.setInt(3, bv.getLevel_()+1);
			pstmt.setString(4, bv.getSubject());
			pstmt.setString(5, bv.getContents());
			pstmt.setString(6, bv.getWriter());
			pstmt.setInt(7, bv.getMidx());
			pstmt.setString(8, bv.getFilename());
			pstmt.setString(9, bv.getPassword());
			pstmt.setString(10, bv.getIp());
			
			
			int exec2 = pstmt.executeUpdate();  //실행되면 1 안되면 0
			
			ResultSet rs = null;
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1, bv.getOriginbidx());
			rs = pstmt.executeQuery();
			
			
			if (rs.next()) {
				maxbidx = rs.getInt("maxbidx");
			}
			
			conn.commit();  //일괄처리 커밋
			
			//value = exec+exec2;
			
		} catch (SQLException e) {			
			try {
				conn.rollback();     //실행중 오류발생시 롤백처리
			} catch (SQLException e1) {			
				e1.printStackTrace();
			}  			
			e.printStackTrace();
		}finally {
			try {     // 각 객체도 소멸시키고 DB연결 끊는다			
				pstmt.close();
				conn.close();
			} catch (SQLException e) {			
				e.printStackTrace();
			}	
		}			
		
		return maxbidx;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
