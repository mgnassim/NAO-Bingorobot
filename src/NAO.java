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
        ALSpeechRecognition spraakherk = new ALSpeechRecognition(this.application.session());
        ALMemory geheugen = new ALMemory(this.application.session());
        spraakherk.setLanguage("Dutch");
        spraakherk.setVocabulary(woordenlijst,false);


        geheugen.subscribeToEvent("WordRecognized", new EventCallback() {
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

        spraakherk.subscribe("Test_asr");
        Thread.sleep(2000);
        spraakherk.unsubscribe("Test_asr");

    }

    public boolean test(){
        return true;
    }
    public void sayRandomRiddles() throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        ALSpeechRecognition speechRecognition = new ALSpeechRecognition(this.application.session());
        speechRecognition.setLanguage("English");
        ArrayList<String> help = new ArrayList<>();
        help.add("help");
        help.add("hint");

        HashMap<String, String> riddlesAndAnswer = new HashMap<>();
        riddlesAndAnswer.put("Hoe noem je een metalen klant?", "Een koper");
        riddlesAndAnswer.put("Een man werd dodelijk gewond tijdens een veldslag in 892. Hij overleed en werd begraven in 891. Rara, hoe kan dat?", "Het zijn jaartallen van vòòr Christus");
        riddlesAndAnswer.put("2 vaders en 2 zoons aten tijdens een ontbijt samen 3 eieren, iedereen had één ei. Hoe kan dit?", "Een van de vaders is ook een opa. Daardoor is de andere vader zowel een zoon als een vader");
        riddlesAndAnswer.put("Waarom heeft een vrouw twee paar lippen?", "Een om ruzie te maken en een om het weer goed te maken");
        int index = 0; // om te weten welke raadsel je bent.

        ArrayList<String> hints = new ArrayList<>();
        hints.add("Jaartallen");
        hints.add("Het is een van de meest beroemdste metalen.");
        hints.add("Eentje is heel oud!");
        hints.add("Ze hebben andere functies.");

        Iterator<Map.Entry<String, String>> iterator = riddlesAndAnswer.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, String> entry = iterator.next();
            tts.say(entry.getKey());
            index++; // om te weten welke raadsel koppelen aan goede hint.
            Thread.sleep(2000);
            if(test()) {
                tts.say(hints.get(index));
            }

        }

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
