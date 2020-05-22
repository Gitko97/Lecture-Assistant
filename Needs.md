[필요한 기술]
1.Sound-To-Text (아직 완벽한것 못찾음)
	 조건 : 완성된 음성파일이 아닌 실시간 컴퓨터 음성이나 html 음성을 계속 text로 줄 수 있는가?
	 
	1) Google Speech API (https://jungwoon.github.io/google%20cloud/2018/01/17/Speech-Api/)

2. 특정 화면 캡쳐 : (찾음)
	1) html 화면 캡쳐 (html2canvas 이용 : https://itinerant.tistory.com/51)

	2)자바 Robot 클래스 이용 (https://m.blog.naver.com/PostView.nhn?blogId=gckcs2&logNo=10037702424&proxyReferer=https:%2F%2Fwww.google.com%2F)

3. 강의 자료 pdf를 이미지로 : (찾음)

	1) 자바 라이브러리 PDFBox 이용 (https://elfinlas.github.io/2019/01/25/java-pdf-img/)

3.5. 캡쳐 이미지를 pdf로 (찾음)

	1) 자바 라이브러리 PDFBox 이용 (https://m.blog.naver.com/PostView.nhn?blogId=bb_&logNo=221329660551&proxyReferer=https:%2F%2Fwww.google.com%2F)

4. 캡쳐 이미지 분석 및 필기를 가져오는것은 이미지를 픽셀로 비교하는 알고리즘을 만들어야 한다. (못 찾음)

	ex)  1)   https://m.blog.naver.com/PostView.nhn?blogId=ndb796&logNo=221047683553&proxyReferer=https:%2F%2Fwww.google.com%2F
	     2)  OpenCV 에 대해서도 알아보기


					<5월 9일 회의 결과>
	1. 음성 STT 부분

		- 자바 Capturing Audio (https://docs.oracle.com/javase/tutorial/sound/capturing.html) 를 이용한 컴퓨터 사운드를 byte단위로 변환 후 실시간으로 카카오톡 API 이용, 
			다시 우리가 만든 프로그램에 text를 전달
	
	2. 강의 화면 캡처

		- 자바 Robot 클래스를 이용한 0.5초,1초 등 주기적인 Screen Capture를 통해 강의 화면 분석 (현재는 캡처 이미지를 픽셀로 변경하여 분석할 예정)

		- 혹은 OpenCV를 이용한 동영상 그 자체의 분석

	3. 필기의 분석
		
		- 스크린샷의 비교로 필기들을 추출 할 수 있지만 언제부터 언제까지가 하나의 필기로써 음성 Text 에 맵핑 되어야하는지 해결 방안이 필요함

	<3번 예시 해결방안 2가지>

		1. pdf 화면을 N등분 하여 각 구역의 필기가 진행 되었을때 그것을 하나의 필기로 인식하고 다른 구역으로 넘어가게되면 또 다른 필기
			
		2. 새로운 필기가 즉, 교수님의 입력이 일정시간 없었다면 그 전까지의 필기를 하나의 필기로 인식 및 text와 맵핑 

					<5월 22일 회의 결과>

	1. 음성 STT 부분 - 김성윤

		- 모듈화 및 음성이 끊기는 부분에서 카카오톡API 가 뒷 부분을 버려버리는 오류 수정 필요
			=> 카카오톡 API가 불가능하면 Google API 사용

	2. 음성 녹음 부분 - 김동규
		- 음성 녹음 및  AudioInputStream 을 성윤님 클래스의 메소드를 이용하여 매개변수로 전달 및 String 혹은 StringBuffer를 return 값으로 
			받아오는 것이 목표
		- 모듈화 진행
			=> Google API 를 사용하면 필요 없음

	3. PDF 비교 - 박호연
		- Capturing 클래스의 static ArrayList<Bufferedimage>를 참조해 원본 PDF와 비교, 화면 전환을 판단 후 
			화면 전환 index의 이전 ArrayList<Bufferedimage> 의 index를 저장
		- 모듈화 진행

	4. 발표 자료 및 전체 GUI, Thread -전준형, 창선
		- 발표자료 만들기
		- 전체적인 GUI와 전체 프로그램의 흐름 계산