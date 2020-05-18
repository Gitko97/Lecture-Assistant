package callAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CallAPI {
	
	public static void main(String[] args) {

		String token = "1244a9cc06ce6976036956540394cd65";// 카카오 디벨로퍼 성윤의 토큰값
        String authKey = "KakaoAK "+ token; //
        
        String inputString = null;
        StringBuffer response = new StringBuffer();
        
        File inputFile = new File("src/heykakao.wav");
        AudioInputStream inputFileStream = null;
        
        try {
			inputFileStream = AudioSystem.getAudioInputStream(inputFile);
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
			return;
		}
       
        try {
            String apiURL = "https://kakaoi-newtone-openapi.kakao.com/v1/recognize";
            
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("Transfer-Encoding","chunked");
            con.setRequestProperty("Authorization", authKey);
           
            OutputStream os = con.getOutputStream();
            
            int count;
            byte [] buffer = new byte[18192];
            
            while ((count = inputFileStream.read(buffer)) != -1)
            {
            	os.write(buffer,0,count);
            }
            
            os.close();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
           
            while((inputString = in.readLine()) != null )
            {
            	response.append(inputString);
            }
            in.close();
            System.out.println(response);
            
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

	}	
}

