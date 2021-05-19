package BingoGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class BingoNAO {

    public ArrayList<String> spokenNumbers = new ArrayList<>();
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
        tts.setVolume(0.4f);
        tts.setLanguage("Dutch");
        tts.say(tekst);
    }

    public void configurationListenToBingo() throws Exception {
        List<String> words = new ArrayList<>();
        words.add("Bingo");
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("English");
        try {
            speechrec.unsubscribe("Test_asr");
        } catch(Exception e) {

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

                    BingoMain.bingo = true;

                    if (confidence > 0.30f) {
                        try {
                            say("Scan de qr code van je bingokaart bij mijn hoofd om te zien of je gewonnen hebt.");
                            scan();
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
//                Thread.sleep(1500)

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
                ArrayList<String> qrCode = (ArrayList<String>) data.get(0);
                System.out.println(data.get(0));

            }
        });
        try {
            scanner.unsubscribe("QR-Code");
        } catch (Exception exception) {

        }
        scanner.subscribe("QR-Code");
    }


    public boolean scan() throws Exception {

        ALMemory memory = new ALMemory(this.application.session());

        try {


            say("geef me een barcode");



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
