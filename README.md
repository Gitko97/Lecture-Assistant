[PDF를 이미지로 만들고, note클래스에 이미지와 해당 이미지의 필기구간을 설정, 스크립트를 받아와 매핑하는 SW]

1. SW 설명 :
    STT로 받은 1초 단위로 나뉜 스크립트와 Note클래스의 이미지, 필기구간을 매핑하는 소프트웨어

2. SW대상 및 조건 :
   수업 자료를 가진 온라인 강의 수강생, 또한 다수의 이미지로 이루어져있는 수업자료가 아닌
  Text 중심의 수업 자료

3. SW 서비스 :
 (1)Sound To Text를 이용한 음성 스크립트 제공

 (2) 원본 수업 PDF와 수업 진행중 화면의 PDF의 유사도 판별을 통한 교수님의 필기를 따로 저장 (pdf 한 장 전체가 아닌 필기 부분만) 및  종합 pdf 합본 제공

 (3) 음성 스크립트와 따로 저장한 필기 부분의 맵핑. 즉, 해당 필기를 보며 교수님께서 어떻게 설명해주셨는지 한번에 볼 수 있게 서비스 제공

4. Test :
	test/test/TextToImgDemo.java 파일에 main 메소드.
	sttarray에 1분 가량의 스크립트가 포함되어 있고
	0.pdf의 페이지들을 불러와 임의의 시간에 매핑하였다.
	textToImg 클래스를 생성하고 convert()메소드를 호출하면 된다.
	생성자에 전달된 arraylist<string>, arraylist<노트>, arraylist<changedposition>, width, height에 따라 변환이 이뤄진다.
