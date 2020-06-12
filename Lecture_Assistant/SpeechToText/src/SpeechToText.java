package src;
/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;

// [START speech_transcribe_infinite_streaming]

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JOptionPane;
import javax.sound.sampled.TargetDataLine;

public class SpeechToText implements Runnable{
	
  private static final int STREAMING_LIMIT = 290000*12;
  //private static final int STREAMING_LIMIT = 290000*12; // ~5 minutes


  // Creating shared object
  private static volatile BlockingQueue<byte[]> sharedQueue = new LinkedBlockingQueue();
  private static TargetDataLine targetDataLine;
  private static int BYTES_PER_BUFFER = 6400; // buffer size in bytes

  private static int restartCounter = 0;
  private static ArrayList<ByteString> audioInput = new ArrayList<ByteString>();
  private static ArrayList<ByteString> lastAudioInput = new ArrayList<ByteString>();
  private static int resultEndTimeInMS = 0;
  private static int isFinalEndTime = 0;
  private static int finalRequestEndTime = 0;
  private static boolean newStream = true;
  private static double bridgingOffset = 0;
  private static boolean lastTranscriptWasFinal = false;
  private static StreamController referenceToStreamController;
  private static ByteString tempByteString;
  
  private boolean exit;
  private String languageCode;
  private SpeechClient client;
  private static ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();
  private ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
  private ClientStream<StreamingRecognizeRequest> clientStream;
  RecognitionConfig recognitionConfig;
  StreamingRecognitionConfig streamingRecognitionConfig;
  StreamingRecognizeRequest request;
  private String langCode;
  
  public void init(String... args) {
    exit = false;
	InfiniteStreamRecognizeOptions.langCode = langCode;
    InfiniteStreamRecognizeOptions options = InfiniteStreamRecognizeOptions.fromFlags(args);
    if (options == null) {
      // Could not parse.
      System.out.println("Failed to parse options.");
      System.exit(1);
    }
    languageCode = options.langCode;
    this.setting();
  }

  //Make milliSeconds to Real-Time Second
  public static String convertMillisToDate(double milliSeconds) {
    long millis = (long) milliSeconds;
    DecimalFormat format = new DecimalFormat();
    format.setMinimumIntegerDigits(2);
    return String.format(
        "%s:%s /",
        format.format(TimeUnit.MILLISECONDS.toMinutes(millis)),
        format.format(
            TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
  }

  //Authentiation to Google Speech 
  public void Authentiation(String filePath,String langCode) throws IOException { 
    CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream(filePath)));
	SpeechSettings settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
	this.client = SpeechClient.create(settings); 
	this.langCode = langCode;
  }
	
  public void exit() {
	this.exit = true;
  }
	
  //Split result string into 1 second increments
  public ArrayList<String> getString(){
    ArrayList<String> origin = new ArrayList<>();
	ArrayList<String> changedString = new ArrayList<>(); 
	StreamingRecognitionResult result;
    SpeechRecognitionAlternative alternative ;
    int k = 0;
    int i;
    for(i = 0; i<responses.size(); i++) {
      result = responses.get(i).getResultsList().get(0);
      alternative = result.getAlternativesList().get(0);
      origin.add(alternative.getTranscript());
    }
    if(origin.size() == 0) {
      return null;
    }
    System.out.println(origin);
    int j=0;
    for(i=0; i<origin.size()-1; i++) {
      int subindex = 0;
      if(origin.get(i).length()-10 > origin.get(i+1).length()) {
        String final_string = origin.get(i);
        for(; j<i+1; j++) {
          if(subindex < origin.get(j).length()) {
            changedString.add(final_string.substring(subindex, origin.get(j).length()));
        	subindex =  origin.get(j).length();
          }
        }
      } else if(i+1 == origin.size()-1) {
          String final_string = origin.get(i+1);
          for(; j<=i+1; j++) {
            if(subindex < origin.get(j).length()) {
              changedString.add(final_string.substring(subindex, origin.get(j).length()));
        	  subindex =  origin.get(j).length();
            }
          }
      }
    }
    return changedString;
  }
	
  public void setting() {
    responseObserver =
	  new ResponseObserver<StreamingRecognizeResponse>() {
	    String curTime = "";
	            
	    public void onStart(StreamController controller) {
	      referenceToStreamController = controller;
	    }
	    
	    //Add Result Method
	    public void onResponse(StreamingRecognizeResponse response) {
	      StreamingRecognitionResult result = response.getResultsList().get(0);
	      Duration resultEndTime = result.getResultEndTime();
	      resultEndTimeInMS =
	        (int)
	          ((resultEndTime.getSeconds() * 1000) + (resultEndTime.getNanos() / 1000000));
	        double correctedTime =
	          resultEndTimeInMS - bridgingOffset + (STREAMING_LIMIT * restartCounter);
	        String resulttime = convertMillisToDate(correctedTime);
	        if (!result.getIsFinal()) {
	          if(!curTime.equals(resulttime)) {
	            curTime = resulttime;
	            SpeechToText.responses.add(response);
	            lastTranscriptWasFinal = false;
	          }
	        } else {
	          isFinalEndTime = resultEndTimeInMS;
	          lastTranscriptWasFinal = true;
	        }
	    }

	    public void onComplete() {}

	    public void onError(Throwable t) {}
	  };
	  clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
	  recognitionConfig =
	  RecognitionConfig.newBuilder()
	    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
	    .setLanguageCode(languageCode)
	    .setSampleRateHertz(16000)
	    .build();

	  streamingRecognitionConfig =
	    StreamingRecognitionConfig.newBuilder()
	      .setConfig(recognitionConfig)
	      .setInterimResults(true)
	      .build();

	  request =
	    StreamingRecognizeRequest.newBuilder()
	      .setStreamingConfig(streamingRecognitionConfig)
	      .build(); // The first request in a streaming call has to be a config

	  clientStream.send(request);
  }
  public void run(){
    // Microphone Input buffering
    class MicBuffer implements Runnable {

      @Override
      public void run() {
        targetDataLine.start();
        byte[] data = new byte[BYTES_PER_BUFFER];
        while (targetDataLine.isOpen()) {
          try {
            int numBytesRead = targetDataLine.read(data, 0, data.length);
            if ((numBytesRead <= 0) && (targetDataLine.isOpen())) {
              continue;
            }
          sharedQueue.put(data.clone());
          } catch (InterruptedException e) {
        	JOptionPane.showMessageDialog(null, "Microphone input buffering interrupted");
          }
        }
      }
    }

    // Creating microphone input buffer thread
    MicBuffer micrunnable = new MicBuffer();
    Thread micThread = new Thread(micrunnable);
    micThread.setPriority(10);

    try {
      // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
      // bigEndian: false
      AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
      DataLine.Info targetInfo =
        new Info(
          TargetDataLine.class,
          audioFormat); // Set the system information to read from the microphone audio
        // stream

    if (!AudioSystem.isLineSupported(targetInfo)) {
      throw new InterruptedException();
    }
    // Target data line captures the audio stream the microphone produces.
    targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
    targetDataLine.open(audioFormat);
    micThread.start();

    long startTime = System.currentTimeMillis();

    while (true) {
      long estimatedTime = System.currentTimeMillis() - startTime;

      if (estimatedTime >= STREAMING_LIMIT || exit) {
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
          break;
        }

        newStream = true;

        clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);

        request =
          StreamingRecognizeRequest.newBuilder()
            .setStreamingConfig(streamingRecognitionConfig)
            .build();
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
  }
}
// [END speech_transcribe_infinite_streaming]