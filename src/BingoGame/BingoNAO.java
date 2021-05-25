package BingoGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class BingoNAO {

    public ArrayList<String> spokenNumbers = new ArrayList<>();
    protected Bingokaart bka = new Bingokaart();
    List<Object> data;
    String value;
    ArrayList<String> code;
    private Application application;

    public void connect(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
        this.application = new Application(new String[]{}, robotUrl);
        application.start();
    }

    public void say(String tekst) throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        tts.setParameter("speed", 75f);
        tts.setVolume(0.4f);
        tts.setLanguage("Dutch");
        tts.say(tekst);
    }

    public void animation(String path) throws Exception {
        // Create an ALAniamationPlayer object and link it to the session
        ALAnimationPlayer alAnimationPlayer = new ALAnimationPlayer(this.application.session());
        // Make the robot do something
        alAnimationPlayer.run(path);
    }

    public void animatedSpeech(String text) throws Exception {
        // Create an ALAniamationSpeech object and link it to the session
        ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(this.application.session());
        // Make the robot do something
        alAnimatedSpeech.say(text);

    }

    public void configurationListenToBingo() throws Exception {
        List<String> words = new ArrayList<>();
        words.add("Bingo");
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("English");
        try {
            speechrec.unsubscribe("Test_asr");
        } catch (Exception e) {

        }

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

                    if (confidence > 0.40f) {
                        try {
                            say("Scan de qr code van je bingokaart bij mijn hoofd om te zien of je gewonnen hebt.");
                            if (scan()) {
                                System.out.println("We hebben een winnaar!!!");
                                BingoMain.bingo = true;
                            }
                            else{
                                System.out.println("geen winnaar helaas");
                                BingoMain.bingo = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    public void listenToBingo() throws Exception {
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());

        speechrec.subscribe("Test_asr");
        Thread.sleep(2000);
        speechrec.unsubscribe("Test_asr");
    }


    public void sayNumbers() throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        tts.setParameter("speed", 60f);
        tts.setVolume(1.0f);
        tts.setLanguage("Dutch");

        int randomNumber = (int) (Math.random() * 75) + 1;

        if (spokenNumbers.contains(String.valueOf(randomNumber))) {
            while (spokenNumbers.contains(String.valueOf(randomNumber)))
                randomNumber = (int) (Math.random() * 75) + 1;
        }

        spokenNumbers.add(String.valueOf(randomNumber));

        try {
            animatedSpeech("Het volgende nummer is ^start(animations/Stand/Gestures/shortrange)" + String.valueOf(randomNumber) + "^wait(animations/Stand/Gestures/shortrange)");
            Thread.sleep(1000);
//            animatedSpeech("Ik herhaal het laatst genoemde nummer was" + randomNumber);
//            Thread.sleep(1500);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(spokenNumbers);
    }

    public void barcodeReader() throws Exception {
        ALMemory memory = new ALMemory(this.application.session());
        ALBarcodeReader scanner = new ALBarcodeReader(this.application.session());
        memory.subscribeToEvent("BarcodeReader/BarcodeDetected", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                List<Object> data = (List<Object>) o;

                ArrayList<Object> qrCode = (ArrayList<Object>) data.get(0);
                value = (String) qrCode.get(0);
                System.out.println((String) qrCode.get(0));
            }
        });
        try {
            scanner.unsubscribe("QR-Code");
        } catch (Exception exception) {

        }
        scanner.subscribe("QR-Code");
    }

    public boolean scan() throws Exception {

        try {
            say("geef me een barcode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] numbers = value.split("\\s+");

        ArrayList<String> cardNumbers = new ArrayList<>(Arrays.asList(numbers));

        return spokenNumbers.contains(cardNumbers);
    }
}
