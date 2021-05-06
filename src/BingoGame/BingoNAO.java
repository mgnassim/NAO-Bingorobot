package BingoGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class BingoNAO {
    private Application application;
    protected List<String> gezegdeCijfers = new ArrayList<>();
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

    public void listen(List<String> keyword) throws Exception {

        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("Dutch");
        speechrec.setVocabulary(keyword, false);

        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                ArrayList<String> data = (ArrayList<String>) o;
                System.out.println(data.get(0));
                Listenstate state = Listenstate.standard;
                boolean isTrue = false;
                while (!isTrue) {

                    for (String s : keyword) {
                        if (data.get(0).contains(s)) {
                            state = Listenstate.keyword;
                        } else
                            state = Listenstate.standard;
                    }

                    if (state == Listenstate.keyword) {
                        try {
                            say("Scan de QR code van je bingokaart om te zien of je echt gewonnen hebt.");

                            scan();

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

    public void sayNummers() throws Exception {

        int a = 0;
        while (true) {

            int randomNummer = (int) (Math.random() * 75) + 1;

            if (this.gezegdeCijfers.contains(String.valueOf(randomNummer))) {
                while (!this.gezegdeCijfers.contains(String.valueOf(randomNummer)))
                    randomNummer = (int) (Math.random() * 75) + 1;
            }

            this.gezegdeCijfers.add(String.valueOf(randomNummer));

            try {
                say("Nummer " + String.valueOf(randomNummer));
                Thread.sleep(1000);
                say("Ik herhaal het laatst genoemde nummer was" + randomNummer);
                Thread.sleep(1500);

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(this.gezegdeCijfers);

            a++;
            if (a == 4)
                return; // voor test 4x

        }

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

                    String[] robotCijfers = new String[gezegdeCijfers.size()];
                    gezegdeCijfers.toArray(robotCijfers);

                    String[] spelerCijferz = bka.getSpelerCijfersz();

                    if (bka.checkPlayersCard(robotCijfers, spelerCijferz)) {
                        try {
                            say("We hebben een winnaar! De bingo is nu klaar.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    } else {
                        try {
                            say("Wie wil jij nou bedriegen hier! Het spelletje wordt nu weer voortgezet.");
                            Thread.sleep(2000);
                            sayNummers();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

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

enum Listenstate {
    standard,
    keyword
}
