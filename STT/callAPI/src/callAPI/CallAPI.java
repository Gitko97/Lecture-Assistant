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
import com.google.protobuf.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;

public class CallAPI implements Runnable
{
	
	static final int STREAMING_LIMIT = 290000;
	
	static SpeechClient client = null;
	static boolean exit;
	static int BYTES_PER_BUFFER = 6400;
	
	 private static volatile BlockingQueue<byte[]> sharedQueue = new LinkedBlockingQueue();
	
	private static int restartCounter = 0;
	static ArrayList<ByteString> audioInput = new ArrayList<ByteString>();
	static ArrayList<ByteString> lastAudioInput = new ArrayList<ByteString>();
	static int resultEndTimeInMS = 0;
	static int isFinalEndTime = 0;
	static int finalRequestEndTime = 0;
	static boolean newStream = true;
	static double bridgingOffset = 0;
	static boolean lastTranscriptWasFinal = false;
	static StreamController referenceToStreamController;
	static ByteString tempByteString;
	
	
	public CallAPI(String keyPath) {
		try {
			authExplicit(keyPath);
			exit = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Exit() {
		exit = true;
	}
	
	static String convertMillisToDate(double milliSeconds)
	{
		long millis = (long) milliSeconds;
	    DecimalFormat format = new DecimalFormat();
	    format.setMinimumIntegerDigits(2);
	    return String.format(
		        "%s:%s /",
		        format.format(TimeUnit.MILLISECONDS.toMinutes(millis)),
		        format.format(
		        		TimeUnit.MILLISECONDS.toSeconds(millis)
		        		- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
		        		)
		        );
	}

	
	static void authExplicit(String jsonPath) throws IOException  // key/key.json 占쏙옙占쏙옙 占싱울옙占쏙옙  Credential 占쏙옙체 占쏙옙占쏙옙 占쏙옙 占싱몌옙 占싱울옙占쏙옙 SpeechClient 占쏙옙체占쏙옙 占쏙옙占쏙옙占싹울옙 占쏙옙占쏙옙틱 client占쏙옙 占쌀댐옙
	{	
		CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream(jsonPath)));
		SpeechSettings settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
		client = SpeechClient.create(settings);
	}
	
	public void run() {
		ResponseObserver<StreamingRecognizeResponse> responseObserver = null;

		try
		{
			responseObserver = new ResponseObserver<StreamingRecognizeResponse>()
			{
				ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();
				public void onStart(StreamController controller) 
				{  
                }

                public void onResponse(StreamingRecognizeResponse response) // onResponse占쏙옙 response占쏙옙 占쏙옙 占쏙옙 占쏙옙占쏙옙 占쌥뱄옙풔占� 占쌉쇽옙. 占쌩곤옙占쌩곤옙 占쏙옙占쏙옙占쏙옙占� 占쏙옙占쏙옙求占� 占쌘듸옙
	            {
	             responses.add(response);      	  
	             StreamingRecognitionResult result = response.getResultsList().get(0);
	             Duration resultEndTime = result.getResultEndTime();
	             resultEndTimeInMS =
	                 (int) 	((resultEndTime.getSeconds() * 1000) + (resultEndTime.getNanos() / 1000000));
	              double correctedTime =
	                  resultEndTimeInMS - bridgingOffset + (STREAMING_LIMIT * restartCounter);

	              SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	              if (result.getIsFinal()) {
	                System.out.print("\033[2K\r");
	                System.out.printf(
	                    "%s: %s [confidence: %.2f]\n",
	                    convertMillisToDate(correctedTime),
	                    alternative.getTranscript(),
	                    alternative.getConfidence());
	                isFinalEndTime = resultEndTimeInMS;
	                lastTranscriptWasFinal = true;
	              } else {
	                System.out.print("\033[2K\r");
	                System.out.printf(
	                    "%s: %s", convertMillisToDate(correctedTime), alternative.getTranscript());
	                lastTranscriptWasFinal = false;
	              }
	            }

	            public void onComplete() 
	            { 
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

	        // AudioSystem 확占쏙옙.
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
	        
	        long startTime = System.currentTimeMillis();  // �떆媛� 痢≪젙 硫붿냼�뱶 �씪�떒 二쇱꽍
	        
	        // Audio Input Stream
	        AudioInputStream audio = new AudioInputStream(targetDataLine);
	        
	        // 占쏙옙트占쏙옙占쏙옙 占쏙옙占쏙옙. 占쏙옙占쏙옙占쏙옙 占시곤옙占쏙옙 占쏙옙占쏙옙占쏙옙 break
	        while (true)
	        {
	        	long estimatedTime = System.currentTimeMillis() - startTime;

	            if (estimatedTime >= STREAMING_LIMIT) {

	              clientStream.closeSend();
	              referenceToStreamController.cancel(); // remove Observer

	              if (resultEndTimeInMS > 0) {
	                finalRequestEndTime = isFinalEndTime;
	              }
	              resultEndTimeInMS = 0;

	              lastAudioInput = null;
	              lastAudioInput = audioInput;
	              audioInput = new ArrayList<ByteString>();

	              restartCounter++;

	              if (!lastTranscriptWasFinal) {
	                System.out.print('\n');
	              }

	              newStream = true;

	              clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);

	              request =
	                  StreamingRecognizeRequest.newBuilder()
	                      .setStreamingConfig(streamingRecognitionConfig)
	                      .build();

	              System.out.printf("%d: RESTARTING REQUEST\n", restartCounter * STREAMING_LIMIT);

	              startTime = System.currentTimeMillis();

	            } else {

	              if ((newStream) && (lastAudioInput.size() > 0)) {
	                // if this is the first audio from a new request
	                // calculate amount of unfinalized audio from last request
	                // resend the audio to the speech client before incoming audio
	                double chunkTime = STREAMING_LIMIT / lastAudioInput.size();
	                // ms length of each chunk in previous request audio arrayList
	                if (chunkTime != 0) {
	                  if (bridgingOffset < 0) {
	                    // bridging Offset accounts for time of resent audio
	                    // calculated from last request
	                    bridgingOffset = 0;
	                  }
	                  if (bridgingOffset > finalRequestEndTime) {
	                    bridgingOffset = finalRequestEndTime;
	                  }
	                  int chunksFromMS =
	                      (int) Math.floor((finalRequestEndTime - bridgingOffset) / chunkTime);
	                  // chunks from MS is number of chunks to resend
	                  bridgingOffset =
	                      (int) Math.floor((lastAudioInput.size() - chunksFromMS) * chunkTime);
	                  // set bridging offset for next request
	                  for (int i = chunksFromMS; i < lastAudioInput.size(); i++) {
	                    request =
	                        StreamingRecognizeRequest.newBuilder()
	                            .setAudioContent(lastAudioInput.get(i))
	                            .build();
	                    clientStream.send(request);
	                  }
	                }
	                newStream = false;
	              }

	              tempByteString = ByteString.copyFrom(sharedQueue.take());

	              request =
	                  StreamingRecognizeRequest.newBuilder().setAudioContent(tempByteString).build();

	              audioInput.add(tempByteString);
	            }

	            clientStream.send(request);
	        }
	      } catch (Exception e) {
	        System.out.println(e);
	      }
	      //responseObserver.onComplete(); �쓳�떟 arrayList 二쇱꽍泥섎━
	    }
}

