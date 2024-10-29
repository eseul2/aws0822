<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/loginCheck.jsp" %>  
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<style>

table {
	margin : auto; 
}

input[name="subject"] {
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
    color: white; /* 글씨 색을 흰색으로 설정 */
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
	
	var ans = confirm("저장하시겠습니까?");  // 함수의 값은 참과 거짓 true false로 나눈다. 
	
	if(ans == true) {	
		fm.action="<%=request.getContextPath()%>/board/boardWriteAction.aws"; /* 이거 작성하고 컨트롤러로 가세요 */
		fm.method="post";
		fm.enctype="multipart/form-data";   //파일을 올리기 위해서 지정해야한다. 
		fm.submit();	//파일 업로드를 포함한 폼 데이터를 전송할 때 필요한 인코딩 방식을 지정
	}
		return; 
}
	
</script>


<form name="frm">

<h2 style="text-align:center">글쓰기</h2>

<hr>

<table>
	<tr>
		<td class="header">제목</td>
	</tr>
	<tr>
		<td><input type="text" name= "subject"></td>
	</tr>
	<tr>
		<td>내용</td>
	</tr>
	<tr>
		<td><textarea placeholder="내용을 입력하세요" name="contents"></textarea></td>
	</tr>
	<tr>
		<td style="text-align:center">작성자<input type="text" name="writer" style="width: 150px;" ></td>
	</tr>
	<tr>
		<td style="text-align:center">비밀번호<input type="password" name="password" style="width: 150px;"></td>
	</tr>
	<tr>
		<td>첨부파일<input type="file" name="filename"></td>
	</tr>
	<tr> 
		<td><button type ="button" onclick="check()">저장</button></td>
		<td><button type ="button" onclick="history.back();">취소</button></td>  <!-- 이전화면으로 돌아가기 --> 
	</tr>

</table>
</form>
</body>
</html>