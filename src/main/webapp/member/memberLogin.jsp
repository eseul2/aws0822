<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE>로그인 페이지</TITLE>
<style>

/* 로그인 글자 가운데 정렬 글자 크기와 두께조정 */
header {
	width:100%;
	height:100px;
	text-align: center;
	margin-top : 50px;
	font-size : 35px;
	font-weight: bold;
}


nav {
	width: 15%;
	height: 400px; 
	float: left; 
}

section { 
	width: 70%; 
	height: 400px; 
	float: left; 
}

aside { 
	width: 15%; 
	height: 400px; 
	float: left; 
}

footer { 
	width: 100%; 
	height: 300px; 
	text-align: center;
	clear: both; 
	}
	
body {
	
	margin: 0px;
	padding: 0px;
	
	}


/* 테이블 테두리랑 색깔 지정 */
/* 테이블 가로 길이 줄이고 세로길이 높임 */
table {
    background-color: white; /* 테이블 전체 배경색 */ 
	border : 15px solid gray;  
	height : 150px;
	margin: 0 auto; 
	border-collapse: collapse;
}
</style>

<script>
//아이디 비밀번호 유효성 검사 
function check() {
	// 이름으로 객체찾기
	let memberid = document.getElementsByName("memberid"); //배열처럼 방에 들어간다. 
	let memberpw = document.getElementsByName("memberpw");
	//alert(memberid[0].value);  디버깅용 알림
	//alert(memberpw[0].value);
	if(memberid[0].value=="") {
		alert("아이디를 입력해주세요");
		memeberid[0].focus();
		return;
	}else if(memberpw[0].value=="") {
		alert("비밀번호를 입력해주세요");
		memberpw[0].focus();
		return;
	}
	var fm = document.frm;
	fm.action="<%=request.getContextPath()%>/member/memberLoginAction.aws";  //가상경로 지정. 액션은 처리한다는 의미 
	fm.method="post"; // 메소드는 포스트 방식으로 할거다
	fm.submit(); //서브밋을 사영해소 이동시틸거다
	
	return;
}


</script>
</HEAD>
 <BODY>
<header>로그인</header>
<nav></nav>
<section>
<article>


<form name="frm">
	<table border=1 style="width:800px;">
		<tr>
		<hr>
		<br>
			<td style="text-align:center;">아이디</td>
			<td>
			<input type="text" name="memberid" maxlength="30" style="width: 150px;" value="">
			</td>
		</tr>
		<tr>
			<td style="text-align:center;">비밀번호</td>
			<td>
			<input type="password" name="memberpw" maxlength="30" style="width: 150px;">
			</td>
		</tr>		
		<tr>
			<td colspan=2 style="text-align:center;">
			<input type="button" name="okay" value="로그인하기" onclick="check();">
			</td>
		</tr>
	</table>
</form>



</article>	
</section>
<aside>
</aside>
<footer>
made by seul.
</footer>
</BODY>
</HTML>
    