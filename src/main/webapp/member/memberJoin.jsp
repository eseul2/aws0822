<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE> 회원가입</TITLE>

<link href= "../css/style.css" type-"text/css" rel="stylesheet" >
<!-- CDN주소 제이퉈리 사용하는주소 -->
<script src="https://code.jquery.com/jquery-latest.min.js"></script> 
<script>

const email = /[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]$/i;

/* alert(email.test("hello5@naver.com")); */

// 버튼을 눌렀을 때 check함수 작동
function check() {
	
	//유효성 검사하기
	var fm = document.frm;
	
	if(fm.memberid.value == "") {
		alert("아이디를 입력해주세요");
		fm.memberid.focus();  // 커서가 입력안한 해당 자리로 갈수 있도록 
		return;
	}else if(fm.btn.value=="N"){
		alert("아이디를 중복체크를 해주세요");
		fm.memberid.focus(); 
		return;
	}else if(fm.memberpw.value =="") {
		alert("비밀번호를 입력해주세요");
		fm.memberpw.focus(); 
		return;
	}else if(fm.memberpw2.value =="") {
		alert("비밀번호2를 입력해주세요");
		fm.memberpw2.focus(); 
		return;
	}else if(fm.memberpw.value != fm.memberpw2.value) {
		alert("비밀번호가 일치하지 않습니다.");
		fm.memberpw2.value=""; 
		fm.memberpw2.focus(); 
		return;
	}else if(fm.membername.value =="") {
		alert("이름을 입력해주세요");
		fm.membername.focus(); 
		return;
	}else if(fm.memberemail.value =="") {
		alert("이메일을 입력해주세요");
		fm.memberemail.focus(); 
		return;
	}else if(email.test(fm.memberemail.value) == false) {
		alert("이메일 형식이 올바르지 않습니다.");
		fm.memberemail.value=""; 
		fm.memberemail.focus(); 
		return;
	}else if(fm.memberphone.value =="") {
		alert("연락처를 입력해주세요");
		fm.memberphon.focus(); 
		return;
	}else if(fm.memberbirth.value =="") {
		alert("생년월일을 입력해주세요");
		fm.memberbirth.focus(); 
		return;
	}else if(hobbyCheck()==false) {
		alert("취미를 1개 이상 선택해주세요");
		return;
	}
	
	var ans = confirm("저장하시겠습니까?");
	
	if(ans == true) {	
		//해킹을 당할 수 있기 때문에 가상경로를 쓴다. 		가짜경로 형식은 /기능/세부기능.aws 
		fm.action="<%=request.getContextPath()%>/member/memberJoinAction.aws"; 
		fm.method="post";
		fm.submit();	
	}
	
	// html 홈태그 기능을 자바스크립트로 제어 
	/* fm.action="./memberJoinAction.jsp";
	fm.method="post";
	fm.submit();
	 */
	 
	 alert("저장되었습니다!");
	return; // 리턴에 값을 안쓰면 그냥 멈춤 종료
}


function hobbyCheck() {
	
	var arr = document.frm.memberhobby //문서객체안에 폼객체 안에 input객체 선언 
	var flag = false;  //기본값을 false로 설정 // 체크유무 초기값 false 선언
	
	for(var i =0; i<arr.length; i++){   //선택한 여러값을 반복해서 출력
	if(arr[i].checked == true) {		//하나라도 선택했다면 true값 리턴
		flag = true;
		break;
		}	
	}
	/* if(flag==false) {
		alert("취미를 1개 이상 선택해주세요");
		return false;
	} */
	return flag;
}

$(document).ready(function(){
	
	$("#btn").click(function(){
	//alert("중복체크버튼 클릭");
	
	let memberId = $("#memberid").val();  /* 버튼을 클릭하게되면 이 멤버 아이디 변수에 저장하겠다 */
	if(memberId=="") {
		alert("아이디를 입력해주세요");
		return;
	}
	
	$.ajax({	// ajax 형식
		type : "post",	//전송방식
		url : "<%=request.getContextPath()%>/member/memberIdCheck.aws", 
		dataType : "json",	// json타입은 문서에서 {"키값" : "value값","키값2" : "value값2"}
		data : {"memberId" : memberId },
		success : function(result){	//결과가 넘어와서 성공했을 때 받는 영역
			
			//alert("전송 성공 테스트");
			//alert("길이는" + result.length);
			//alert("cnt값은" + result.cnt);
			
			if(result.cnt == 0) {
				alert("사용할 수 있는 아이디입니다.");
				$("#btn").val("Y");
			}else{
				alert("사용할 수 없는 아이디입니다.");
				$("#memberid").val("");		// 입력한 아이디 지우기 입력한 해당 값을 지운다. 
			}
			
			
		},
		error : function() {	// 결과가 실패했을 때 받는 영역 
			
			alert("전송 실패 테스트");
		}
		
	});
	
	});
		
});



</script>
</HEAD>
<BODY>
	<header>	
	<a href="./memberjoin.jsp">회원가입 페이지</a>
	</header>
<nav>
<a href ="./memberLogin.jsp" style="text-decoration : none;">회원로그인 가기 </a> 
</nav>
<section>
<article>

	<style>
	
	 table {
    margin: 0 auto; 
	 border-collapse: collapse; 
  }
  
	</style>

	
	<form name = "frm">
	<table style ="width:500px; "> 
	<tr> 
		<th class="idcolor">아이디</th> 
		<td> <input type = "text" id = "memberid" name = "memberid" maxlength = "20" style = "width:200px" value ="" placeholder="아이디를 입력하세요.">
		<button type="button" name= "btn" id="btn" value="N">아이디 중복체크</button>
		</td> 
	</tr>
	<tr>
		<th class="idcolor">비밀번호</th> 
		<td><input type = "password" name = "memberpw" maxlength = "20" style = "width:200px"></td> 
	</tr>
	<tr> 
		<th>비밀번호 확인</th> 
		<td><input type = "password" name = "memberpw2" maxlength = "20" style = "width:200px"></td> 
	</tr>
	<tr> 
		<th id="name">이름</th> 
		<td><input type = "text" name = "membername" maxlength = "20" style = "width:200px"></td> 
	</tr>
	<tr> 
		<th>이메일</th> 
		<td><input type = "email" name = "memberemail" maxlength = "20" style = "width:200px"></td> 
	</tr>
	<tr> 
		<th>연락처</th> 
		<td><input type = "number" name = "memberphone" maxlength = "20" style = "width:200px"></td> 
	</tr>
	<tr> 
		<th>주소</th> 
		<td>
		<select name = "memberaddr" style = "width:100px;">			
		<option value = "서울">서울</option>
		<option value = "대전" selected>대전</option>
		<option value = "부산">부산</option>
		<option value = "인천">인천</option>
		</select>	
		</td> 
	</tr>
	<tr> 
		<th>성별</th> 
		<td>
		<input type = "radio" name = "membergender" id = "select1" value="M"><label for="select1">남성</label>
		<input type = "radio" name = "membergender" id = "select2" value="F" checked><label for="select2">여성</label>
		</td>
	</tr>
	<tr> 
		<th>생년월일</th> 
		<td><input type = "number" name = "memberbirth" maxlength = "8" style = "width:100px"> 
		예) 20240202 </td> 
	</tr>
	<tr> 
		<th>취미</th> 
	<td>
		<input type="checkbox" name="memberhobby" id="check1" value="야구"><label for="check1">야구</label>
		<input type="checkbox" name="memberhobby" id="check2" value="농구"><label for="check2">농구</label>
		<input type="checkbox" name="memberhobby" id="check3" value="축구"><label for="check3">축구</label>
		</td> 
	</tr>

	<tr>
		<td colspan = 2 style = "text-align: center">
		<button type="button" onclick="check()">
		저장하기
		<!-- <img src ="https://t1.daumcdn.net/daumtop_deco/images/pctop/2023/logo_daum.png" width="50px" height="30px"> 저장하기를 이미지 넣어서 -->
		</button>
		
		<!-- <input type = "submit" name = "btn" value = "회원정보 저장하기">
		<input type = "reset" name = "btn" value = "초기화"> -->
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



