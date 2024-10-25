package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.MemberDao;
import mvc.vo.MemberVo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.jasper.tagplugins.jstl.core.Out;




// @ 이거 어노테이션이라고 부른다. 				// httpservlet을 상속받고 있다 
@WebServlet("/MemberController")		// 서블릿 : 자바로 만든 웹페이지 (접속주소는 : /MemberController 이렇게 나타난다)
public class MemberController extends HttpServlet { // http 서블릿을 상속받고 있다. (extends) 그 뜻은 http통신을 하고 있다
	private static final long serialVersionUID = 1L;
	
	private String location;	// 멤버변수(전역) 초기화 => 이동할 페이지 
	
	public MemberController(String location) {
		this.location = location;
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		// 넘어온 모든 값은 여기에서 처리해서 분기한다. - controller의 역할 
		//System.out.println("값이 넘어오나요?");
		
		// 넘어온 주소를 뽑아낸다 (전체주소를 추출)
		String uri = request.getRequestURI();
		//System.out.println("uri" + uri);	// mvc_programming/member/memberJoinAction.aws
		//String[] location = uri.split("/");
		
		String paramMethod ="";   // 전송방식이 sendRedirect면 S라고 하고, forward방식이면 F로 값을 받을것이다. 
		String url="";
		
		if(location.equals("memberJoinAction.aws")) { 
		
			String memberId = request.getParameter("memberid");
			String memberPw = request.getParameter("memberpw");
			//String memberPw2 = request.getParameter("memberpw2"); 확인이라서 굳이 안써도 된다. 회원가입 할때만 필요해서~~
			String memberName = request.getParameter("membername");
			String memberGender = request.getParameter("membergender");
			String memberBirth = request.getParameter("memberbirth");
			String memberAddr = request.getParameter("memberaddr");
			String memberPhone = request.getParameter("memberphone");
			String memberEmail = request.getParameter("memberemail");
			String[]memberHobby = request.getParameterValues("memberhobby");  //배열 타입으로 받겠다. 파라미터 밸류스 사용해서 
			String memberInHobby = "";
			 if (memberHobby == null) {
			     memberInHobby = "No hobbies selected";  // 기본 값 설정
			 } else {
			     for (int i = 0; i < memberHobby.length; i++) {
			         memberInHobby = memberInHobby + memberHobby[i] + ",";
			     }
			 }
		    MemberDao md = new MemberDao();
		 	int value = md.memberInsert(memberId,
			 			memberPw,		//객체안에 생성해놓은 멤버 메소드를 호출해서 값을꺼낸다
			 			memberName,
			 			memberGender,
			 			memberBirth,
			 			memberAddr,
			 			memberPhone,
			 			memberEmail,
			 	   	    memberInHobby);

		   	
		   
		   // value값이 1이면 입력성공! 0이면 입력실패 
		   // 1이면 성공했기 때문에 다른 페이지로 이동시키고 0이면 다시 회원가입 입력 페이지로 간다. 
		   String msg ="";
		   HttpSession session = request.getSession();  // 세션객채 활용
		   
		   if(value==1) {// index.jsp파일은 web.xml 웹 설정파일에 기본등록되어 있기 때문에 생략이 가능하다. 그냥 "/"; 이렇게 작성해도 됩니당
			
			   msg="회원 가입되었습니다";
			   session.setAttribute("msg",msg);
			   
			   url=request.getContextPath()+"/"; //request.getContextPath() : 프로젝트 이름  //메인 페이지입니다~
		   
		   }else {
				msg="회원 가입 오류발생하였습니다";
				session.setAttribute("msg",msg);
				
				url=request.getContextPath()+"/member/memberJoin.jsp";  //실패하면 다시 회원가입 페이지로 가는것~
			  // response.sendRedirect(pageUrl);
		   } 
		   
		   paramMethod ="S";   // 밑에서 센드리다렉트 방식으로 한다. 
		   
		}else if(location.equals("memberJoin.aws")) {
		//	System.out.println("들어왔나?");
			
			url = "/member/memberJoin.jsp";
			//RequestDispatcher rd = request.getRequestDispatcher(uri2);
		    //rd.forward(request, response);  // 포워드 방식 : 내부 안에서 넘겨서 토스하겠다는뜻
			paramMethod = "F"; // 하단에서 포워드로 처리합니다.
		}else if(location.equals("memberLogin.aws")) {   // 회원 로그인 페이지 index로 넘긴다.
		//	System.out.println("들어왔나?");
			
			url = "/member/memberLogin.jsp";
			paramMethod = "F";   //하단에서 포워드로 처리합니다.
		   
		}else if(location.equals("memberLoginAction.aws")) {
			//System.out.println("memberLoginAction 들어왔나?");
			
			
			String memberId = request.getParameter("memberid");   // 값을 받아오기 
			String memberPw = request.getParameter("memberpw");
			
			MemberDao md = new MemberDao();
			MemberVo mv = md.memberLoginCheck(memberId, memberPw);
		//	System.out.println("mv객체가 생겼나요?" +mv);    //객체가 생겼는지 확인 
			
			if(mv == null ) {
				
				url=request.getContextPath()+"/member/memberLogin.aws";
				paramMethod="S";
			}else {
				// 해당되는 로그인 사용자가 있으면 세션에 회원정보를 담아서 메인으로 가라 
				
				String mid = mv.getMemberid(); //아이디 꺼내기
				int midx = mv.getMidx();		// 회원번호 꺼내기
				String memberName = mv.getMembername(); // 이름 꺼내기 
				
				HttpSession session = request.getSession();
				session.setAttribute("mid", mid);
				session.setAttribute("midx", midx);
				session.setAttribute("memberName", memberName);
				
				url = request.getContextPath() + "/";
	            paramMethod = "S";
				}
			}else if(location.equals("memberLogout.aws")) {
			//	System.out.println("memberLogout");
				
				HttpSession session = request.getSession();
				session.removeAttribute("mid");
				session.removeAttribute("midx");
				session.removeAttribute("memberName");
				session.invalidate(); // 모든걸 초기화 시킨다.
				
				url=request.getContextPath()+"/";
				paramMethod="S";
				
			}else if(location.equals("memberList.aws")) {
			//	System.out.println("memberList.aws");
				
				// 1. 메소드 불러서 처리하는 코드를 만들어야 한다.
				MemberDao md = new MemberDao();   // 객체생성
				ArrayList<MemberVo> alist = md.memberSelectAll();
				
				request.setAttribute("alist", alist);
				
				// 2. 보여줄 페이지를 forward방식으로 보여준다. 공유의 특성을 가지고 있다. 
				url= "/member/memberList.jsp";		// 이쪽으로 토스해준다.		
				paramMethod="F";
			} else if(location.equals("memberIdCheck.aws")) {
				System.out.println("memberIdCheck.aws");
				
				String memberId = request.getParameter("memberId");
				
				MemberDao mv = new MemberDao();
				int cnt = mv.memberIdCheck(memberId);
				
				
				System.out.println("cnt: " + cnt);
				
				PrintWriter out = response.getWriter();
				out.println("{\"cnt\":\""+cnt+"\"}");	//제이슨 형태 
			}
		
		
		
		
		
	
		if(paramMethod.equals("F")) {
			RequestDispatcher rd = request.getRequestDispatcher(url);
		    rd.forward(request, response);      //f면 리퀘스트로 
		} else if(paramMethod.equals("S")){
			response.sendRedirect(url);  
		}
	}
	
	
	// 보이지 않는
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
