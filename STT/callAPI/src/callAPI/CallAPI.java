package callAPI;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;

public class CallAPI
{
	
	static SpeechClient client = null;
	
	public static void main(String[] args) throws Exception  // 실행 메소드 run
	{	
		authExplicit("key/key.json");
		streamingMicRecognize();
	}
	
	public static void runRecognition() {
		try
		{
			authExplicit("key/key.json");
			streamingMicRecognize();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	static void authExplicit(String jsonPath) throws IOException  // key/key.json 파일 이용해  Credential 객체 생성 후 이를 이용해 SpeechClient 객체를 생성하여 스태틱 client에 할당
	{	
		CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream(jsonPath)));
		SpeechSettings settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		client = SpeechClient.create(settings);
	}
	
	static void streamingMicRecognize() throws Exception {
		
		
		ResponseObserver<StreamingRecognizeResponse> responseObserver = null;

		try {
	        responseObserver =
	            new ResponseObserver<StreamingRecognizeResponse>() {
	              ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

	              public void onStart(StreamController controller) 
	              {
	            	  
	              }

	              public void onResponse(StreamingRecognizeResponse response) // onResponse는 response가 올 때 마다 콜백되는 함수. 중간중간 결과값을 출력하는 코드
	              {
	            	  StreamingRecognitionResult result = response.getResultsList().get(0);
	                  SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	                  System.out.printf("Transcript : %s\n", alternative.getTranscript());
		              
	                  responses.add(response);
		              
	              }

	              public void onComplete() 
	              { 
	                for (StreamingRecognizeResponse response : responses) 
	                {
	                  StreamingRecognitionResult result = response.getResultsList().get(0);
	                  SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	                  System.out.printf("Transcript : %s\n", alternative.getTranscript());
	                }
	              }

	              public void onError(Throwable t)
	              {
	                System.out.println(t);
	              }
	            };

	        ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);

	        RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
	                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
	                .setLanguageCode("ko-KR")
	                .setSampleRateHertz(16000)
	                .build();

	        StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder()
	        		.setConfig(recognitionConfig)
	        		.build();

	        StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
	                .setStreamingConfig(streamingRecognitionConfig)
	                .build(); // The first request in a streaming call has to be a config
	        clientStream.send(request);

	        // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
	        // bigEndian: false
	        AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
	        DataLine.Info targetInfo = new Info
	        		(
		                TargetDataLine.class,
		                audioFormat
	                ); // Set the system information to read from the microphone audio stream

	        // AudioSystem 확인.
	        if (!AudioSystem.isLineSupported(targetInfo))
	        {
	          System.out.println("Microphone not supported");
	          System.exit(0);
	        }
	        
	        // Target data line captures the audio stream the microphone produces.
	        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
	        targetDataLine.open(audioFormat);
	        targetDataLine.start();
	        System.out.println("Start speaking");
	        long startTime = System.currentTimeMillis();
	        
	        // Audio Input Stream
	        AudioInputStream audio = new AudioInputStream(targetDataLine);
	        
	        // 스트리밍 루프. 정해진 시간이 지나면 break
	        while (true)
	        {
	          long estimatedTime = System.currentTimeMillis() - startTime;
	          byte[] data = new byte[6400];
	          audio.read(data);
	          if (estimatedTime > 290000) // ~5 minutes
	          { 
	            System.out.println("Stop speaking.");
	            targetDataLine.stop();
	            targetDataLine.close();
	            break;
	          }
	          request =
	              StreamingRecognizeRequest.newBuilder()
	                  .setAudioContent(ByteString.copyFrom(data))
	                  .build();
	          clientStream.send(request);
	        }
	      } catch (Exception e) {
	        System.out.println(e);
	      }
	      //responseObserver.onComplete();
	    }
}

