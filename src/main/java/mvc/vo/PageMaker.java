package mvc.vo;


// 2-16. 페이지 하단에 페이징 네비게이션에 필요한 변수들을 담아놓은 클래스
public class PageMaker {
	
	
	private int displayPageNum = 10;		// 2-17. 페이지 목록 번호 리스트 1 2 3 4 5 6 7 8 9 10
	private int startPage; 					// 2-18. 목록의 시작번호를 담는 변수 
	private int endPage;					// 2-19. 목록의 끝번호를 담는 변수 
	private int totalCount;					// 2-20. 총 게시물 수를 담는 변수

	
	private boolean prev; 					// 2-21. 이전버튼
	private boolean next;					// 2-22. 다음버튼 
	
	
	private SearchCriteria scri;   
	
	
	
	// 2-24. 게터 세터 생성 
	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {  // 2-24. 총 게시물이 몇개인지 받는 메소드 
		this.totalCount = totalCount;
		calcData();			// 2-25. 페이지 목록 리스트 번호를 나타내주기 위한 계산식
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}
	
	public SearchCriteria getScri() {
		return scri;
	}

	public void setScri(SearchCriteria scri) {
		this.scri = scri;
	}


	
	// 2-26. 메소드 구현
	private void calcData() {
		
		// 2-27. 기본적으로 1에서부터 10까지 나타나도록 설정한다. (페이지 네비게이션에서)
		endPage = (int)(Math.ceil(scri.getPage()/(double)displayPageNum)*displayPageNum);  // 2.28 모두 올림처리하는 메소드ceil()
		
		// 2-29. endPage가 설정되었으면 시작페이지도 설정한다. 
		startPage = (endPage-displayPageNum) +1;   
		
		// 2.30. 실제 게시물 수에 따라서 endPage를 구하겠다.
		int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
		
		// 2-31. 설정한 endPage와 실제 endPage를 비교해서 최종 endPage 구한다.
		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		
		// 2-32. 이전,다음 버튼 만들기
		prev = (startPage == 1 ? false : true);	// 삼항연산자 사용 startPage가 1이면 이전 버튼(prev)을 비활성화(false)하고, 그렇지 않으면 활성화(true)
		next = (endPage*scri.getPerPageNum() >= totalCount ? false : true);		
	}


}

 // 2-33 이거 다 작성하면 보드 컨트롤러로 가세요 
