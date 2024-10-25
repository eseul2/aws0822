<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@page import="mvc.vo.BoardVo" %>   
 
 
<!--  request.getAttribute()는 서블릿이나 다른 JSP 페이지에서 전달된 객체를 가져올 때 사용하는 메서드 -->
<%
 BoardVo bv = (BoardVo)request.getAttribute("bv");   //강제형변환  양쪽형을 맞춰준다 
 %>       
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 수정</title>

<style>

table {
	margin : auto; 
}

input[type="text"] {
    width: 500px;
    height: 30px;
}


textarea {
    width: 500px;
    height: 400px;
}

.header {
	height : 50px;
}


button {
    width: 50px;
    height: 30px;
    font-size: 15px;
    background-color: black;
    color: white;
}


.sub {
    width: 30px;
    height: 20px;
   
}

</style>

</head>
<body>

<script>
function check() {
	
	//유효성 검사하기
	var fm = document.frm;
	
	if(fm.subject.value == "") {
		alert("제목을 입력해주세요");
		fm.subject.focus();  // 커서가 입력안한 해당 자리로 갈수 있도록 
		return;
	} else if(fm.contents.value =="") {
		alert("내용을 입력해주세요");
		fm.contents.focus(); 
		return;
	}else if(fm.writer.value =="") {
		alert("작성자를 입력해주세요");
		fm.writer.focus(); 
		return;
	}else if(fm.password.value =="") {
		alert("비밀번호를 입력해주세요");
		fm.password.focus(); 
		return;
	}
	
	var ans = confirm("수정하시겠습니까?");
	
	
	if(ans == true) {	// 업데이트하고 처리를 하겠다. 
		fm.action="<%=request.getContextPath()%>/board/boardModifyAction.aws";
  /* 이거 설정하고 컨트롤러러로 */
		fm.method="post";
		fm.submit();	
	}
	
	 alert("수정되었습니다!");
		return; 
}
	
</script>


<form name="frm">
<input type="hidden" name="bidx" value="<%=bv.getBidx()%>">  <!-- bidx값이 수정할때 필요해서 hidden으로 안보이게 한 input에 넣어서 controller로 보낸다. -->

<h2>글수정</h2>

<hr>

<table>
	<tr>
		<td class="header">제목</td>
	</tr>
	<tr>
		<td><input type="text" name= "subject" value="<%=bv.getSubject()%>"></td>
	</tr>
	<tr>
		<td>내용</td>
	</tr>
	<tr>
		<td><textarea name="contents"><%=bv.getContents()%></textarea></td>
	</tr>
	<tr>
		<td style="text-align:center">작성자<input type="text" name="writer" value="<%=bv.getWriter()%>" ></td>
	</tr>
	<tr>
		<td style="text-align:center">비밀번호<input type="password" name="password"></td>
	</tr>
	<tr>
		<td>첨부파일<input type="uploadfile"></td>
	</tr>
	<tr>
		<td><button type ="button"  onclick="check()">저장</button></td>
		<td><button type ="button" onclick="history.back();">취소</button></td>  <!-- 이전화면으로 돌아가기 --> 
	</tr>
</table>
</form>

</body>
</html>