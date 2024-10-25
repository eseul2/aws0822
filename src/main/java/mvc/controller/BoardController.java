package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import mvc.dao.BoardDao;
import mvc.dao.MemberDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.MemberVo;
import mvc.vo.PageMaker;
import mvc.vo.SearchCriteria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;





@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private String location;	// 멤버변수(전역) 초기화 => 이동할 페이지 
	
	
	//매개변수가 있는 생성자
	public BoardController(String location) {
		this.location = location;
	}
 
   

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String paramMethod ="";   // 전송방식이 sendRedirect면 S라고 하고, forward방식이면 F로 값을 받을것이다. 
		String url="";
		
		if(location.equals("boardList.aws")) { //가상경로 
			
			String page = request.getParameter("page"); // 2-14.
			if (page == null) page= "1";  // 2-15. 실행문이 하나일때 요렇게 써도 된다. 
			int pageInt = Integer.parseInt(page); // 2-34. 문자를 숫자로 변경한다. 
			
			String searchType = request.getParameter("searchType");
			String keyword = request.getParameter("keyword");
			if(keyword == null) keyword = ""; // 키워드가 null이라면 빈값으로 냅눈다? 
			

			SearchCriteria scri = new SearchCriteria();
			scri.setPage(pageInt);
			scri.setSearchType(searchType);
			scri.setKeyword(keyword);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);			// 2-35. PageMaker에 Criteria 담아서 가지고 다닌다. 

			
			BoardDao bd = new BoardDao();
			// 2-4. 페이징 처리하기 위한 전체 데이터 갯수 가져오ㅁ기 
			int boardCnt = bd.boardTotalCount(scri); 
		//	System.out.println("게시물 수는? " + boardCnt); // 2-13.
			pm.setTotalCount(boardCnt);		// 2-36. PageMaker에 전체게시물 수를 담아서 페이지 계산.
			
			//BoardDao bd = new BoardDao();   // 18. Dao에서 만든 메소드 불러오기 
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);  // 19. 불러왔다~    2-42. cri 매개변수 넣기 
		//	System.out.println("alist ==>" + alist);  // 20.객체주소가 나오면 객체가 생성된 것을 짐작할 수 있다
  			
			request.setAttribute("alist", alist);  // 화면까지 가지고 가기 위해 request 객체에 담는다.
			request.setAttribute("pm",pm);    	// 2-37. forward방식으로 넘기기 때문에 공유가 가능하다.  그리고 보드 리스트로 이동
			
			// 21. 이제 jsp로 가서 설정을 해줘야  합니다.
			
			 paramMethod ="F";   
			 url="/board/boardList.jsp"; // 실제 내부경로
			
			}else if(location.equals("boardWrite.aws")) {   //글쓰기 경로로 가세용 
				//System.out.println("boardWrite");
				
				paramMethod ="F";  // 포워드 방식은 내부에서 공유하는 것이기 때문에 내부에서 활동하고 이동한다.
				url= "/board/boardWrite.jsp"; 
			}else if(location.equals("boardWriteAction.aws")) { //boardWriteAction.aws 요청이 들어왔을 때, 
															    //사용자가 작성한 글의 정보를 파라미터로 받아서 처리할 준비를 하는 부분
				
				// 저장될 위치  //파일 업로드 기능 
				String savePath="D:\\dev\\eclipse-workspace\\mvc_programmilng\\src\\main\\webapp\\images\\"; // 업로드된 파일을 저장할 경로
				System.out.println(savePath);
				
				// 업로드 되는 파일 사이즈
				int fsize = (int) request.getPart("filename").getSize();  
				System.out.println("fsize: " + fsize);
				
				// 원본 파일이름
				String originFileName ="";
				if (fsize != 0) {
					Part filePart = (Part) request.getPart("filename"); // 넘어온 멀티파트 형식의 파일을 Part클래스로 담는다.
					System.out.println("filePart ==>" + filePart);
					
					
					originFileName = getFileName(filePart); // 파일이름 추출
					System.out.println("originFilename==>" + originFileName);
					
					System.out.println("저장되는 위치 ==> " + savePath + originFileName);
					
					File file = new File(savePath + originFileName); //파일 객체 생성
					InputStream is = filePart.getInputStream(); // 파일 읽어들이는 스트림 생성
					FileOutputStream fos = null;
					
					  fos = new FileOutputStream(file); // 파일 작성 및 완성하는 스트림 생성
					  
					  int temp = -1;
					  
					  while ((temp = is.read()) != -1) { // 반복문을 돌려서 읽어들인 데이터를 output에 작성한다
						  fos.write(temp); 
					  }
					  is.close();  // input 스트림 객체 소멸
					  fos.close(); // output 스트림 객체소멸
				}else {
					originFileName ="";   // 파일 없을 시에
				}
				
				//System.out.println("boardWriteAction.aws");
				// 1. 파라미터 값을 넘겨받는다. 
				String subject = request.getParameter("subject"); //서브젝트라는 이름의 요청 파라미터를 가져와 변수에 저장
				String contents = request.getParameter("contents");
				String writer = request.getParameter("writer");
				String password = request.getParameter("password");
				
				HttpSession session = request.getSession();   // 세션 객체를 불러와서 
				int midx = Integer.parseInt(session.getAttribute("midx").toString()); // 로그인 할 때 담았던 세션변수 midx값을 꺼낸다.
				
				
				// ip 추출하기 
				String ip="";
				try {
				ip = getUserIp(request);
				System.out.println("ip ===> " + ip);
				}catch (Exception e) {			
					e.printStackTrace();
				}	
				
				
				BoardVo bv = new BoardVo();
				bv.setSubject(subject);
				bv.setContents(contents);
				bv.setWriter(writer);
				bv.setPassword(password);
				bv.setMidx(midx);
				bv.setFilename(originFileName); // 파일이름 db컬럼 추가
				bv.setIp(ip);
				
				//2. DB처리한다.
				BoardDao bd = new BoardDao();
				int value = bd.boardInsert(bv);
				
				if(value == 2) {  //입력성공
					paramMethod="S";
					url= request.getContextPath()+"/board/boardList.aws";
				}else {  //실패했으면
					paramMethod="S";
					url= request.getContextPath()+"/board/boardWrite.aws";			
				}		
			}else if(location.equals("boardContents.aws")) {  //게시물 내용 넘어노느거
				//System.out.println("boardContents.aws");
				
				// 1. 넘어온 값 받기
				String bidx = request.getParameter("bidx");   // String 타입으로 bidx 받아오기
				//System.out.println("bidx -->" + bidx);       // 값이 넘어왔는지 확인
				int bidxInt = Integer.parseInt(bidx);	// 숫자형으로 되어있는 문자를 다시 바꿔준다. 
				//System.out.println("CobidxInt"+bidxInt);
				
				// 2. 처리하기
				BoardDao bd = new BoardDao(); // 객체 생성하고 
				bd.boardViewCntUpdate(bidxInt); // 조회수 업데이트!!
				BoardVo bv = bd.boardSelectOne(bidxInt); // 문자형으로 넘어오기 때문에 숫자형으로 바궈줘야 한다. 생성한 메소드 호출(해당되는 bidx의 게시물 데이터 가져옴)
				
				request.setAttribute("bv",bv);	// 포워드 방식이라 같은 영역안에 있어서 공유해서 jsp페이지에서 꺼내 쓸 수 있다.
				//System.out.println("Cobv"+bv);
				
				
				// 3. 이동해서 화면 보여주기
				paramMethod = "F";   // 화면을 보여주기 위해서 같은 영역 내부안에 jsp페이지를 보여준다. 
				url= "/board/boardContents.jsp"; // 포워드 방식이기 때문에 바로 보여주는거라 리퀘스트 겟 컨텐츠 안해도된다. 	
			}else if(location.equals("boardModify.aws")) {  // 글 수정 화면으로 넘어가는거
				//System.out.println("boardModify.aws");
				
				String bidx = request.getParameter("bidx"); // 파라미터 bidx값 가져오기 
				int bidxInt = Integer.parseInt(bidx);	// 원래 숫자형인데 문자열로 가져왔어서 다시 숫자형으로 변경
				BoardDao bd = new BoardDao();	// 객체 생성하기
				BoardVo bv = bd.boardSelectOne(bidxInt); // 회원 정보를 가져오는 메소드 호출
				
				request.setAttribute("bv",bv);	// 포워드 방식이라 같은 영역안에 있어서 공유해서 jsp페이지에서 꺼내 쓸 수 있다.
				
				paramMethod = "F"; 
				url="/board/boardModify.jsp";	
				

			}else if(location.equals("boardModifyAction.aws")) {  //글 수정 해서 값 넘겨받고 객체에 새로 담는다.
				System.out.println("boardModifyAction.aws");
				
				// 1. 파라미터 값을 넘겨받는다. 파라미터 값은 무조건 문자형으로 받는다.
				String subject = request.getParameter("subject");
				String contents = request.getParameter("contents"); // 인터넷 통신을 통해서 넘어오는 것들은 다 문자형으로 넘어온다.
				String writer = request.getParameter("writer");
				String password = request.getParameter("password");	// 비밀번호가 맞는지 체크를 해야한다. 
				String bidx = request.getParameter("bidx");
				int bidxInt = Integer.parseInt(bidx);	// 원래 숫자형인데 문자열로 가져왔어서 다시 숫자형으로 변경
				
				BoardDao bd = new BoardDao();	// 객체 생성하기
				BoardVo bv = bd.boardSelectOne(bidxInt); // 회원 정보를 가져오는 메소드 호출
			
				paramMethod="S";  // 전역적으로 쓰면 됩니다. 
				// 비밀번호 체크 
				if(password.equals(bv.getPassword())) {
					// 같으면
					BoardDao bd2 = new BoardDao(); // 객체 생성 또 하나 하고 
					BoardVo bv2 = new BoardVo();  // 여기에 넘어온 값을 담기
					
					bv2.setSubject(subject);
					bv2.setContents(contents);
					bv2.setWriter(writer);			// 이거 담을거임
					bv2.setPassword(password);
					bv2.setBidx(bidxInt);
					int value = bd2.boardUpdate(bv2);
					
					if(value == 1) {  //입력성공
						url= request.getContextPath()+"/board/boardContents.aws?bidx="+bidx;	
					}else {  //입력실패
						url= request.getContextPath()+"/board/boardModify.aws?bidx="+bidx;		
					}		
					
				}  else {
		             // 비밀번호가 다르면
		            response.setContentType("text/html; charset=UTF-8");  // 응답 콘텐츠 타입 설정
		             PrintWriter out = response.getWriter();  // PrintWriter 객체 가져오기
		            
		             out.println("<script>");
		             out.println("alert('비밀번호가 다릅니다.');");
		             out.println("location.href='" + request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx + "';");
		             out.println("</script>");
		             out.flush();
		         }
			}else if(location.equals("boardRecom.aws")) { // 조회수 증가 메소드
				
				String bidx = request.getParameter("bidx");
				int bidxInt = Integer.parseInt(bidx);  // 숫자 형변환
				
				BoardDao bd = new BoardDao(); 
				int recom = bd.boardRecomUpdate(bidxInt); // 추천수 업데이트 !!
				
				PrintWriter out = response.getWriter();
				out.println("{\"recom\":\""+recom+"\"}");	//제이슨 형태 
				
			}
				//게시글 삭제 요청
			else if (location.equals("boardDelete.aws")) {
				String bidx = request.getParameter("bidx");
				
				request.setAttribute("bidx", bidx);
				
				paramMethod="F";
				url="/board/boardDelete.jsp";
				
			}
			//게시글 삭제 요청을 처리하는 로직
			else if (location.equals("boardDeleteAction.aws")) {
				
				String bidx = request.getParameter("bidx");
				String password = request.getParameter("password");
				
				//처리하기
				BoardDao bd = new BoardDao();
				int value = bd.boardDelete(Integer.parseInt(bidx), password);   // 0,1
				System.out.println("value"+value);
				
				paramMethod="S";
				
				if (value ==1) {				
					// 성공적으로 삭제된 경우 게시판 목록으로 이동
					url=request.getContextPath()+"/board/boardList.aws";
				}else {				
					// 비밀번호가 틀렸을 때 메시지를 설정
					url=request.getContextPath()+"/board/boardDelete.aws?bidx="+bidx;						
				}
			}
	
			//답변하기
			else if(location.equals("boardReply.aws")) { 
				String bidx = request.getParameter("bidx");
				
				BoardDao bd = new BoardDao();	// 객체 생성하기
				BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx)); // 회원 정보를 가져오는 메소드 호출
				int originbidx = bv.getOriginbidx();	//원본 게시글의 ID 가져오기
				int depth = bv.getDepth();		 // 현재 게시글의 depth(답글 깊이) 가져오기
				int level_ = bv.getLevel_();		// 현재 게시글의 level(답글 레벨) 가져오기
				
				request.setAttribute("bidx",Integer.parseInt(bidx));
				request.setAttribute("originbidx",originbidx);
				request.setAttribute("depth",depth);
				request.setAttribute("level_",level_);
			
				
				paramMethod="F";
				url="/board/boardReply.jsp";
				
			}else if(location.equals("boardReplyAction.aws")) {
				System.out.println("boardReplyAction.aws");
				
				// 저장될 위치  //파일 업로드 기능 
				String savePath="D:\\dev\\eclipse-workspace\\mvc_programmilng\\src\\main\\webapp\\images\\"; // 업로드된 파일을 저장할 경로
				System.out.println(savePath);
				
				// 업로드 되는 파일 사이즈
				int fsize = (int) request.getPart("filename").getSize();  
				System.out.println("fsize: " + fsize);
				
				// 원본 파일이름
				String originFileName ="";
				if (fsize != 0) {
					Part filePart = (Part) request.getPart("filename"); // 넘어온 멀티파트 형식의 파일을 Part클래스로 담는다.
					System.out.println("filePart ==>" + filePart);
					
					
					originFileName = getFileName(filePart); // 파일이름 추출
					System.out.println("originFilename==>" + originFileName);
					
					System.out.println("저장되는 위치 ==> " + savePath + originFileName);
					
					File file = new File(savePath + originFileName); //파일 객체 생성
					InputStream is = filePart.getInputStream(); // 파일 읽어들이는 스트림 생성
					FileOutputStream fos = null;
					
					
					  fos = new FileOutputStream(file); // 파일 작성 및 완성하는 스트림 생성
					  
					  int temp = -1;
					  
					  while ((temp = is.read()) != -1) { // 반복문을 돌려서 읽어들인 데이터를 output에 작성한다
						  fos.write(temp); 
					  }
					  is.close();  // input 스트림 객체 소멸
					  fos.close(); // output 스트림 객체소멸
				}else {
					originFileName ="";
				}
				
				//System.out.println("boardWriteAction.aws");
				// 1. 파라미터 값을 넘겨받는다. 
				String subject = request.getParameter("subject"); //서브젝트라는 이름의 요청 파라미터를 가져와 변수에 저장
				String contents = request.getParameter("contents");
				String writer = request.getParameter("writer");
				String password = request.getParameter("password");
				String bidx = request.getParameter("bidx");
				String originbidx = request.getParameter("originbidx");
				String depth = request.getParameter("depth");
				String level_ = request.getParameter("level_");
				
				HttpSession session = request.getSession();   // 세션 객체를 불러와서 
				int midx = Integer.parseInt(session.getAttribute("midx").toString()); // 로그인 할 때 담았던 세션변수 midx값을 꺼낸다.
				
				// ip 추출하기 
				String ip="";
				try {
				ip = getUserIp(request);
				System.out.println("ip ===> " + ip);
				}catch (Exception e) {			
					e.printStackTrace();
				}	
				
				
				BoardVo bv = new BoardVo();
				bv.setSubject(subject);
				bv.setContents(contents);
				bv.setWriter(writer);
				bv.setPassword(password);
				bv.setMidx(midx);
				bv.setFilename(originFileName); // 파일이름 db컬럼 추가
				bv.setBidx(Integer.parseInt(bidx));
				bv.setOriginbidx(Integer.parseInt(originbidx));
				bv.setDepth(Integer.parseInt(depth));
				bv.setLevel_(Integer.parseInt(level_));
				bv.setIp(ip);
				
				
				BoardDao bd = new BoardDao();
				int maxbidx = bd.boardReply(bv);	// 2가 나와야 성공 
				
				paramMethod ="S";
				if(maxbidx != 0) {
					url=request.getContextPath()+"/board/boardContents.aws?bidx=" + maxbidx; 
				}else {
					url=request.getContextPath()+"/board/boardReply.aws?bidx=" + bidx;  // 실패시 원글을 보여주도록 한다. 
				}
			}
			
			// 게시물에 있는 파일 다운로드 하기 // 파일 다운로드 요청 처리
			else if(location.equals("boardDownload.aws")) {
				System.out.println("boardDownload.aws");  // 디버깅
				
				String filename = request.getParameter("filename");     // 클라이언트가 요청한 파일 이름을 파라미터로 받음
				// 저장될 위치  //파일 업로드 기능 
				String savePath="D:\\dev\\eclipse-workspace\\mvc_programmilng\\src\\main\\webapp\\images\\"; // 업로드된 파일을 저장할 경로
			
				ServletOutputStream sos = response.getOutputStream();  // 응답 객체에서 출력 스트림(ServletOutputStream) 얻기
				
				String downfile = savePath+filename;  // 다운로드할 파일의 전체 경로 설정 (저장 경로 + 파일 이름
				System.out.println("downfile :" + downfile);   //디버깅
				
				File f = new File(downfile); // 파일 생성자 호출/ 다운파일을 가지고 와서 객체로 만들것이다. 
				
				String header = request.getHeader("User-Agent");
				
				String fileName ="";
				response.setHeader("Cache-Control", "no=cache");
				if(header.contains("Chrome") || header.contains("Opera")) {
				
					fileName = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
					response.setHeader("Content-Disposition", "attachment;filename="+fileName);
					
					
				}else if(header.contains("MSIE") || header.contains("Trident") || header.contains("Edgs")) {
					
					fileName = URLEncoder.encode(filename,"UTF-8").replaceAll("\\+", "%20");
					response.setHeader("Content-Disposition", "attachment;filename="+fileName);	
			
				}
				else {
					response.setHeader("Content-Disposition", "attachment;filename="+fileName);
				}
				
				// 파일을 읽기 위한 FileInputStream 생성
				FileInputStream in = new FileInputStream(f);  // 파일을 버퍼로 읽어와서 출력한다. 
				
				 // 파일을 8KB씩 읽어오기 위한 버퍼 생성
				byte[] buffer = new byte[1024*8];
				
				// 파일의 끝까지 반복하며 버퍼로 읽고, 클라이언트에 전송
				while(true) {
					int count = in.read(buffer);   //바이트 단위로 넣겠다.
					if(count == -1) {	// 더 이상 읽을 내용이 없으면 반복 종료
						break;
					}
					sos.write(buffer, 0, count); // 읽은 만큼 클라이언트로 전송
				}
				
				in.close();
				sos.close();
			}
			
			
	
		
		
	

		
		
		
		
		
		
		
		
		
		
		if(paramMethod.equals("F")) {
			RequestDispatcher rd = request.getRequestDispatcher(url);
		    rd.forward(request, response);      //f면 리퀘스트로 
		} else if(paramMethod.equals("S")){
			response.sendRedirect(url);  
		
		}
	}


	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	// 파일 이름을 추출하는 메소드 
	// 파일 클래스 :파일을 저장,수정, 하는 클래스 
	public String getFileName(Part filePart) {
		
		for(String filePartData : filePart.getHeader("Content-Disposition").split(";")) {
			System.out.println(filePartData);
			
			if(filePartData.trim().startsWith("filename")) {
				return filePartData.substring(filePartData.indexOf("=") + 1).trim().replace("\"","");
				
			}
			
		}
		
		return null;
	}
	
	
	// 서버 ip 추출하는 메소드 
	public String getUserIp(HttpServletRequest request) throws Exception {
		
        String ip = null;
      //  HttpServletRequest request = 
      // ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        
        
        if(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
        	InetAddress address = InetAddress.getLocalHost();
        	ip = address.getHostAddress();
        }
		return ip;
	}
	
	

}
