package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {
	@FXML
	private void start(ActionEvent event) {
		
	}
	
	@FXML
	private void stop(ActionEvent event) {
		
	}
	
	@FXML
	private void exit(ActionEvent event) {
		
	}
	
	@FXML
	 private void pdfChoose(ActionEvent event) {
	        // 사진 선택 창
	        FileChooser fc = new FileChooser();
	        fc.setTitle("Select pdf file");
	        fc.setInitialDirectory(new File("/Users/changseon")); // default 디렉토리 설정
	        // 선택한 파일 정보 추출
	        
	        ExtensionFilter pdfType = new ExtensionFilter("pdf file", "*.pdf", "*.pptx");  // 확장자 제한
	        fc.getExtensionFilters().add(pdfType);
	         
	        File selectedFile =  fc.showOpenDialog(null); // showOpenDialog는 창을 띄우는데 어느 위치에 띄울건지 인자를 받고
	                                                      // 그리고 선택한 파일의 경로값을 반환한다.
	        System.out.println(selectedFile);               // 선택한 경로가 출력된다.
	         
	        // 파일을 InputStream으로 읽어옴
	        try {
	            // 파일 읽어오기
	            FileInputStream fis = new FileInputStream(selectedFile);
	            BufferedInputStream bis = new BufferedInputStream(fis);
	          
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	         
	    }
	
	@FXML
	 private void keyChoose(ActionEvent event) {
	        // 사진 선택 창
	        FileChooser fc = new FileChooser();
	        fc.setTitle("Select pdf file");
	        fc.setInitialDirectory(new File("/Users/changseon")); // default 디렉토리 설정
	        // 선택한 파일 정보 추출
	        
	        ExtensionFilter keyType = new ExtensionFilter("key file", "*.json");  // 확장자 제한
	        fc.getExtensionFilters().add(keyType);
	         
	        File selectedFile =  fc.showOpenDialog(null); // showOpenDialog는 창을 띄우는데 어느 위치에 띄울건지 인자를 받고
	                                                      // 그리고 선택한 파일의 경로값을 반환한다.
	        System.out.println(selectedFile);               // 선택한 경로가 출력된다.
	         
	        // 파일을 InputStream으로 읽어옴
	        try {
	            // 파일 읽어오기
	            FileInputStream fis = new FileInputStream(selectedFile);
	            BufferedInputStream bis = new BufferedInputStream(fis);
	          
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	         
	    }
	
}
