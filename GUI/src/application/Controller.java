package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class Controller {
	
	public File pdfFile;
	public File keyFile;
	
	@FXML
	private Text pdfName; //GUI에 단순히 글자 출력을 위함
	
	@FXML
	private Text keyName; //GUI에 단순히 글자 출력을 위함
	
	
	@FXML
	private void start(ActionEvent event) {
		
	}
	
	
	@FXML
	private void exit(ActionEvent event) {
		
	}
	
	@FXML
	 private void pdfChoose(ActionEvent event) {
	        
	        FileChooser fc = new FileChooser();
	        fc.setTitle("Select pdf file");
	        fc.setInitialDirectory(new File("/Users/")); // default 디렉토리 설정
	       
	        
	        ExtensionFilter pdfType = new ExtensionFilter("pdf file", "*.pdf");  // 확장자 제한
	        fc.getExtensionFilters().add(pdfType);
	         
	        pdfFile =  fc.showOpenDialog(null); // showOpenDialog는 창을 띄우는데 어느 위치에 띄울건지 인자를 받고
	                                                      // 그리고 선택한 파일의 경로값을 반환한다.
	        
	        pdfName.setText(pdfFile.getPath());
	        
	        System.out.println(pdfFile);               // 선택한 경로가 출력된다.
	         
//	        // 파일을 InputStream으로 읽어옴
//	        try {
//	            // 파일 읽어오기
//	            FileInputStream fis = new FileInputStream(selectedFile);
//	            BufferedInputStream bis = new BufferedInputStream(fis);
//	          
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        }
	         
	    }
	
	@FXML
	 private void keyChoose(ActionEvent event) {
	        // 키 선택 창
	        FileChooser fc = new FileChooser();
	        fc.setTitle("Select pdf file");
	        fc.setInitialDirectory(new File("/Users/")); // default 디렉토리 설정
	       
	        
	        ExtensionFilter keyType = new ExtensionFilter("key file", "*.json");  // 확장자 제한
	        fc.getExtensionFilters().add(keyType);
	         
	        keyFile =  fc.showOpenDialog(null); // showOpenDialog는 창을 띄우는데 어느 위치에 띄울건지 인자를 받고
	                                                      // 그리고 선택한 파일의 경로값을 반환한다.
	        
	        keyName.setText(keyFile.getPath());      //GUI 파일 경로 표시
	        System.out.println(keyFile);               // 선택한 경로가 출력된다.
	         
	        // 파일을 InputStream으로 읽어옴
//	        try {
//	            // 파일 읽어오기
//	            FileInputStream fis = new FileInputStream(selectedFile);
//	            BufferedInputStream bis = new BufferedInputStream(fis);
//	          
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        }
	         
	    }
	
//	@FXML
//	private void capturing(ActionEvent event){
//		
//	}
	
}
