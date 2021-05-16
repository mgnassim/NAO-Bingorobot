package BingoGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class BingoNAO {
    private Application application;
    protected Bingokaart bka = new Bingokaart();

    public void connect(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
        this.application = new Application(new String[]{}, robotUrl);
        application.start();
    }

    public void say(String tekst) throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        tts.setParameter("speed", 75f);
        tts.say(tekst);
    }

    public boolean listenToBingo(ArrayList<String> spokenNumbers) throws Exception {
        List<String> words = new ArrayList<>();
        words.add("Bingo");
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("English");
        speechrec.setVocabulary(words, false);
        final boolean[] bingoTrue = {false};


        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                List<Object> data = (List<Object>) o;
                String value = (String) data.get(0);
                float confidence = (float) data.get(1);

                if (!value.equals("")) {

                    System.out.println(confidence);
                    System.out.println(value);
                    if (confidence > 0.55f) {
                        try {
                            say("Scan de qr code van je bingokaart bij mijn hoofd om te zien of je gewonnen hebt.");
                            System.out.println("het werkt");
                            bingoTrue[0] = scan(spokenNumbers);
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

        return bingoTrue[0];

    }

    public void sayNumbers(ArrayList<String> spokenNumbers) throws Exception {


        int randomNumber = (int) (Math.random() * 75) + 1;

        if (spokenNumbers.contains(String.valueOf(randomNumber))) {
            while (!spokenNumbers.contains(String.valueOf(randomNumber)))
                randomNumber = (int) (Math.random() * 75) + 1;
        }

        spokenNumbers.add(String.valueOf(randomNumber));

        try {
            say("Nummer " + String.valueOf(randomNumber));
            Thread.sleep(1000);
//                say("Ik herhaal het laatst genoemde nummer was" + randomNummer);
//                Thread.sleep(1500);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(spokenNumbers);


    }


    public boolean scan(ArrayList<String> spokenNumbers) throws Exception {
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
        ArrayList<String> data = (ArrayList<String>) memory.getData("QR-Code");
        String value = data.get(0);
        System.out.println(value);
        String[] numbers = value.split(" ");

        ArrayList<String> cardNumbers = new ArrayList<>(Arrays.asList(numbers));

        return spokenNumbers.contains(cardNumbers);


    }
}
