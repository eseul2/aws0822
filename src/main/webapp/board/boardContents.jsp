<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@page import="mvc.vo.BoardVo" %>   
 
 <!--  request.getAttribute()는 서블릿이나 다른 JSP 페이지에서 전달된 객체를 가져올 때 사용하는 메서드 -->
 <%
 BoardVo bv = (BoardVo)request.getAttribute("bv");   //강제형변환  양쪽형을 맞춰준다 
 %>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="../css/style2.css" rel="stylesheet">
<!-- 제이쿼리 cdn 주소 -->
<script src="https://code.jquery.com/jquery-latest.min.js"></script> 
<script> 

function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;
	  
	  if (fm.content.value == "") {
		  alert("내용을 입력해주세요");
		  fm.content.focus();
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?");
	  
	  if (ans == true) {
		  fm.action="./detail.html";
		  fm.method="post";
		  fm.submit();
	  }	  
	  
	  return;
}


// 추천하기 
$(document).ready(function(){
	
	$("#btn").click(function(){
	//alert("추천버튼 클릭");
	
	$.ajax({	// ajax 형식
		type : "get",	//전송방식 겟방식
		url : "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>", 
		dataType : "json",	// json타입은 문서에서 {"키값" : "value값","키값2" : "value값2"}
		success : function(result){	//결과가 넘어와서 성공했을 때 받는 영역
			
		//	alert("전송 성공 테스트");
		
			var str = "추천("+result.recom+")";
			$("#btn").val(str);
		},
		error : function() {	// 결과가 실패했을 때 받는 영역 
			alert("전송 실패 테스트");
		}	
	});
	
	});
		
});





</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<h2 class="contentTitle"><%=bv.getSubject() %> (조회수:<%=bv.getViewcnt() %>)
	<input type="button" id="btn" value="추천(<%=bv.getRecom()%>)">
	</h2>
	<p class="write"><%=bv.getWriter() %> (<%=bv.getWriteday() %>)</p>
	<hr>
	<div class="content">
		<%=bv.getContents() %>	
		
	</div>
	<% if (bv.getFilename() == null || bv.getFilename().equals("") ) {}else { %>
	<img src="<%=request.getContextPath()%>/images/<%=bv.getFilename() %>">
	<P>
	<a href="<%=request.getContextPath()%>/board/boardDownload.aws?filename=<%=bv.getFilename()%>" class="fileDown">	
	첨부파일 다운로드
	</a>
	</P>
	<%} %>
	
	
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardDelete.aws?bidx=<%=bv.getBidx()%>">삭제</a>
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardReply.aws?bidx=<%=bv.getBidx()%>">답변</a>
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardList.aws">목록</a>
</div>

<article class="commentContents">
	<form name="frm">
		<p class="commentWriter">admin</p>	
		<input type="text" name="content">
		<button type="button" class="replyBtn" onclick="check();">댓글쓰기</button>
	</form>
	
	
	<table class="replyTable">
		<tr>
			<th>번호</th>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>DEL</th>
		</tr>
		<tr>
			<td>1</td>
			<td>홍길동</td>
			<td class="content">댓글입니다</td>
			<td>2024-10-18</td>
			<td>sss</td>
		</tr>
	</table>
</article>

</body>
</html>