package RiddleGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;
import java.util.ArrayList;
import java.util.List;

public class RiddleNAO {

    private Application application;
    final String[] bingoLetters = {"B", "I", "N", "G", "O"};
    ArrayList<String> checkArrayOfGoed = new ArrayList<>();
    List<String> keywords = new ArrayList<>();

    public void connect(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
        // Create a new application
        this.application = new Application(new String[]{}, robotUrl);
        // Start your application
        application.start();
    }

    public void say(String tekst) throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        // Make your robot say something
        tts.say(tekst);
    }

    public void sayNummers() throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());

        keywords.add("BingoGame");

        for (int i = 0; i < 100; i++) {

            int randomLetter = (int) (Math.random() * 5);
            int randomNummer = (int) (Math.random() * 75) + 1;

            checkArrayOfGoed.add(bingoLetters[randomLetter] + " " + randomNummer);
            tts.say(bingoLetters[randomLetter] + " " + randomNummer);

            Thread.sleep(2000);

        }

    }

    public void listenToKeyword() throws Exception {

        keywords.add("BingoGame");
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("Dutch");
        speechrec.setVocabulary(keywords, false);

        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                List<Object> data = (List<Object>) o;
                String value = (String) data.get(0);
                float confidence = (float) data.get(1);

                if(!value.equals("")) {

                    if(value.contains(keywords.get(0))) {
                        try {
                            say("Scan je QR code om te zien of je gewonnen hebt!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

    }

}