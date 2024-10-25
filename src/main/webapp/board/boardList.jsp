<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import = "java.util.*" %>
<%@page import = "mvc.vo.*" %>
    
 <%
 ArrayList<BoardVo> alist = (ArrayList<BoardVo>)request.getAttribute("alist");
 //System.out.println("alist==>" + alist);
 PageMaker pm = (PageMaker)request.getAttribute("pm"); //2-39. 뭐야...
 
 // 게시물 목록 순서 나타내기 
 int totalCount = pm.getTotalCount();  //전체갯수를 뽑아왔어 
 
 String keyword = pm.getScri().getKeyword();
 String searchType = pm.getScri().getSearchType();
 
 String param = "keywprd="+keyword+"&searchType="+searchType+"";
 %>   
 <!-- 22. 이거 설정해주셔야 해요  -->   
 
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="../css/style2.css" rel="stylesheet">
</head>
<body>
<header>
	<h2 class="mainTitle">글목록</h2>
	<form class="search" name="frm" action="<%=request.getContextPath()%>/board/boardList.aws" method="get">
		<select name = "searchType">  <!-- 어떤걸 선택할래? -->
			<option value = "subject">제목</option>
			<option value = "writer">작성자</option>
		</select>
		<input type="text" name ="keyword"> <!-- 나는 키워드로 입력할거야  -->
		<button type="submit" class="btn">검색</button>
	</form>
</header>

<section>	
	<table class="listTable">
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회</th>
			<th>추천</th>
			<th>날짜</th>
		</tr>   <!--스크립틀릿 : 서버에서 실행될 Java 코드를 JSP 페이지에 삽입하여 동적으로 HTML을 생성하는 역할 -->
		<%int num = totalCount - (pm.getScri().getPage()-1)*pm.getScri().getPerPageNum(); // 전체갯수중에 15개를 빼면??
		for(BoardVo bv : alist) { //23. 향상된 for문. alist라는 리스트를 순회하면서 각 요소를 변수에 담아 반복 작업을 수행 
		 
			String lvlStr = ""; // 초기화 
			for(int i=1; i<=bv.getLevel_(); i++) {
				lvlStr = lvlStr + "&nbsp;&nbsp;"; // html에서 띄어쓰기 기호
				if(i == bv.getLevel_()) { // i가 level과 같으면     // level이 증가하면 칸을 옮기고 ㄴ자를 표시해라. 
					lvlStr = lvlStr + "ㄴ"; // ㄴ자 표시를 해준다. 
				}
			}
			
		%> 
		<tr>
			<td><%=num%></td>
			<td class="title">
			<%=lvlStr%>    <!-- 반복문이 여기에 표시가 된다. -->
			<a href="<%=request.getContextPath() %>/board/boardContents.aws?bidx=<%=bv.getBidx() %>"><%=bv.getSubject() %></a></td>
			<td><%=bv.getWriter()%></td>	<!-- 24. dao 생성한 메소드 여기에 넣어줘야 한다. -->		
			<td><%=bv.getViewcnt()%></td>
			<td><%=bv.getRecom()%></td>
			<td><%=bv.getWriteday()%></td>
		</tr>
		<% 
		num = num-1;
		}%>
	
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardWrite.aws">글쓰기</a>
	</div>
	
	 <!--  2-40. 이거 다 하면 보드Dao로 가세요 -->
	<div class="page">
		<ul>
		<%if (pm.isPrev()==true) { %>
		<li><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getStartPage()-1%>&<%=param%>">←</a></li>
		<% } %>
		
		<% for(int i = pm.getStartPage(); i<=pm.getEndPage(); i++) { %>  
			<li <%if (i==pm.getScri().getPage()) {%> class="on"<%}%> > 
			<a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=i%>&<%=param%>"><%=i%> <!-- 해당 검색어가 있으면 그 다음페이지에도 있어야 하니까? -->
			</a>
			</li>
			<% }%>
	
		<%if(pm.isNext()==true && pm.getEndPage()>0){ %>
		<li><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getEndPage()+1%>&<%=param%>">→</a></li>
		<% } %>
		</ul>
	</div>
</section>

</body>
</html>