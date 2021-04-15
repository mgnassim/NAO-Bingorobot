import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class NAO {
    private String naam;
    private Application application;

    public void connect(String hostname, int port){
        String robotUrl = "tcp://" + hostname+ ":" + port;
        // Create a new application
        this.application = new Application(new String[]{}, robotUrl);
        // Start your application
        application.start();
    }

    public void stand() throws Exception {
        ALRobotPosture posture = new ALRobotPosture(this.application.session());
        posture.goToPosture("Stand", 0.75f);
    }

    public void sit() throws Exception {
        ALRobotPosture posture = new ALRobotPosture(this.application.session());
        posture.goToPosture("Sit", 0.75f);
    }
    public void say(String tekst) throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        // Make your robot say something
        tts.say(tekst);
    }

    public void listen(List<String> woordenlijst)throws Exception{
        ALSpeechRecognition speakreg = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speakreg.setLanguage("Dutch");
        speakreg.setVocabulary(woordenlijst,false);

        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                ArrayList<String> data = (ArrayList<String>) o;
                System.out.println(data.get(0));

                if(data.get(0).contains("februari") || data.get(0).contains("december")){
                    try {
                        say("nee, dat is niet het antwoord waar ik naar zoek");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(data.get(0).contains("12") || data.get(0).contains("allemaal")){
                    try{
                        say("correct");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
        speakreg.subscribe("Test_asr");
        Thread.sleep(2000);
        speakreg.unsubscribe("Test_asr");
    }

    public void scan()throws Exception{
        ALBarcodeReader scanner = new ALBarcodeReader(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        try {
            say("geef me een barcode");
            memory.subscribeToEvent("BarcodeReader/BarcodeDetected", new EventCallback() {
                @Override
                public void onEvent(Object o) throws InterruptedException, CallError {
                    ArrayList<String> data = (ArrayList<String>) o;
                    System.out.println(data.get(0));

                }
            });
            scanner.subscribe("QR-Code");
            Thread.sleep(5000);
            scanner.unsubscribe("QR-Code");
            say("dank u wel");
            System.out.println();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
