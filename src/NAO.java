import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class NAO {
    private String naam;
    private Application application;

    public void connect(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
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

    public void listen(List<String> trueAnswers, List<String> falseAnswers) throws Exception {
        List<String> words = new ArrayList<>();
        words.addAll(falseAnswers);
        words.addAll(trueAnswers);
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("English");
        speechrec.setVocabulary(words, false);

        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                List<Object> data = (List<Object>) o;
                String value = (String) data.get(0);
                float confidence = (float) data.get(1);

                if (!value.equals("")) {

                    System.out.println(confidence);
                    System.out.println(value);
                    if (confidence > 0.35f) {


                        if (falseAnswers.contains(value)) {
                            try {
                                say("dat is helaas fout probeer het nog eens");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (trueAnswers.contains(value)) {
                            try {
                                say("correct");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                say("ik ben boos");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        try {
                            say("i don't know, fuck you");
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


    public void scan() throws Exception {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}