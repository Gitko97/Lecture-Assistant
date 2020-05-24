CallAPI 클래스

1. CallAPI(String keyPath) :

	-생성자로 key의 경로값을 받아 인증을 실행하고 boolean exit변수를 false로 초기화 한다.

2. Exit() :

	-종료 메소드, 쓰레드의 while문을 이 메소드를 통해 벗어난다.

3. run() :
	
	-STT 기능의 실행, inner 함수 onResponse에서 String 값을 받아온다.