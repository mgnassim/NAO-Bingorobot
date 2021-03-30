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

    public void listen(List<String> trueAnswers, List<String> falseAnswers)throws Exception{
        for (int i  = 0; i< falseAnswers.size(); i++){
            trueAnswers.add(falseAnswers.get(0));
        }
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("Dutch");
        speechrec.setVocabulary(trueAnswers,false);

        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                ArrayList<String> data = (ArrayList<String>) o;
                System.out.println(data.get(0));
                Listenstate state = Listenstate.standard;
                boolean isTrue = false;
                while (!isTrue) {


                    for (int i = 0; i < trueAnswers.size(); i++) {
                        if (data.get(0).contains(trueAnswers.get(i))) {
                            state = Listenstate.rightanswer;
                        } else if (data.get(0).contains(falseAnswers.get(i))) {
                            state = Listenstate.wronganswer;
                        } else
                            state = Listenstate.standard;
                    }

                    if (state == Listenstate.wronganswer) {
                        try {
                            say("dat is helaas fout probeer het nog eens");
                            isTrue = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (state == Listenstate.rightanswer) {
                        try {
                            say("correct");
                            isTrue = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            say("Ik heb u helaas niet verstaan");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        speechrec.subscribe("Test_asr");
        Thread.sleep(2000);
        speechrec.unsubscribe("Test_asr");

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

enum Listenstate{
    rightanswer,
    wronganswer,
    standard
}
