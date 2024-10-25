package mvc.vo;


// 페이징을 하기 위해서 기준이 되는 데이터를 담는 클래스 
public class Criteria {
	
	private int page= 1;	// 2-1. 페이지 번호를 담는 변수 
	private int perPageNum= 15; // 2-2. 화면에 리스트 게시물 수 num/page :페이지의 번호라는 뜻 15개로 세팅 
	
	
	
	
	
	// 2-3. 세터 게터 생성 
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}

}
