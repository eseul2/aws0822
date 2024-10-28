package mvc.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


// web.xml에 가상경로 요청했어요
@WebServlet("/FrontController")
@MultipartConfig( //멀티파일을 작성한다. 
		fileSizeThreshold = 1024 * 1024 * 1, // 1MB
		maxFileSize	=	 1024 * 1024 * 10,	// 10Mb
		maxRequestSize	=   1024 * 1024 * 15, //15MB
		location = "D:/dev/temp"	// 임시로 보관하는 위치 (물리적으로 만들어놔야한다.)
		
		
)


public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		
		response.setHeader("Content-Type", "text/html;charset=utf-8");
		// 한글깨짐 방지 
	//	request.setCharacterEncoding("UTF-8");
	//	response.setCharacterEncoding("text/html;charset=UTF-8");
		
		
		String uri = request.getRequestURI(); // 전체주소 가져오기
		//		/member/memberJoinAction.aws 
		String[] entity = uri.split("/"); // split으로 잘라주기 
		
		
		if (entity[1].equals("member")) {
			MemberController mc = new MemberController(entity[2]);
			mc.doGet(request, response);
		}else if(entity[1].equals("board")) {
			BoardController bc = new BoardController(entity[2]);  // 보드 컨트롤러로 가게끔 설정한거다
			bc.doGet(request, response);
		}else if(entity[1].equals("comment")) {
			CommentController cc = new CommentController(entity[2]);  
			cc.doGet(request, response);
			
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
