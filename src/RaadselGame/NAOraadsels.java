<<<<<<< HEAD:src/RaadselGame/NAOraadsels.java
package RaadselGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALSpeechRecognition;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALAudioPlayer;

import java.util.*;

public class NAOraadsels {
    private Application application;
    protected String[] raadsels = new String[4];

    public void verbind(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
        // Create a new application
        this.application = new Application(new String[]{}, robotUrl);
        // Start your application
        application.start();
    }

    public void zeg(String tekst) throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        // Make your robot say something
        tts.say(tekst);
    }

    public void speechRecognition() throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALSpeechRecognition robotRecognition = new ALSpeechRecognition(this.application.session());
        // A List of string's
        List<String> stringsList = new ArrayList<>();
        stringsList.add("Het zijn jaartallen van vòòr Christus");
        stringsList.add("Een koper");
        stringsList.add("Een van de vaders is ook een opa. Daardoor is de andere vader zowel een zoon als een vader");
        stringsList.add("Een om ruzie te maken en een om het weer goed te maken");
        // Make your robot recognize words
        robotRecognition.setVocabulary(stringsList, false);

    }

    public void audioAfspelen() throws Exception {
        ALAudioPlayer audioPlayer = new ALAudioPlayer(this.application.session());
        audioPlayer.playFile("../src/SONGS/BingoBackground.wav");
    }

    public void tell() throws Exception {
        // Add riddles to riddles array and Make your robot say something
        raadsels[0] = "Een man werd dodelijk gewond tijdens een veldslag in 892. Hij overleed en werd begraven in 891. Rara, hoe kan dat?"; // Het zijn jaartallen van vòòr Christus!
        raadsels[1] = "Hoe noem je een metalen klant?"; // Een koper!
        raadsels[2] = "2 vaders en 2 zoons aten tijdens een ontbijt samen 3 eieren, iedereen had één ei. Hoe kan dit?"; // Een van de vaders is ook een opa. Daardoor is de andere vader zowel een zoon als een vader.
        raadsels[3] = "Waarom heeft een vrouw twee paar lippen?"; // Een om ruzie te maken en een om het weer goed te maken!
        // Create an ALTextToSpeech object and link it to your current session
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        // arrayLoop
        for (int i = 0; i < raadsels.length; i++) {
            String raadsel = raadsels[i];
            tts.say(raadsel);
            Thread.sleep(15000);
            speechRecognition();
        }
    }

    public void sayRandomRiddles() throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());

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
            if(luisteren(help)) {
                tts.say(hints.get(index));
            }

        }

    }

    public boolean luisteren(List<String> woordenlijst) throws Exception {
        ALSpeechRecognition spraakherk = new ALSpeechRecognition(this.application.session());
        ALMemory geheugen = new ALMemory(this.application.session());
        spraakherk.setLanguage("English");
        spraakherk.setVocabulary(woordenlijst, true);


        geheugen.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                ArrayList<String> data = (ArrayList<String>) o;
                System.out.println(data.get(0));
            }
        });

        spraakherk.subscribe("Test_asr");
        Thread.sleep(5000);
        spraakherk.unsubscribe("Test_asr");
        //ArrayList<> woord = geheugen.getData("WordRecognized");


        /*for(int i = 0; i <woordenlijst.size(); i++){
            if(woord.equals(woordenlijst.get(i))){
                zeg(woordenlijst.get(i));
            }
        }*/


        return false;
    }


}



=======
package Aghead;
import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALSpeechRecognition;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.ArrayList;
import java.util.List;

public class NAO {
    protected String [] raadsels = new String[4];
    private String naam;
    private Application application;

    public void connect(String hostname, int port){
        String robotUrl = "tcp://"+ hostname + ":" + port;
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

    public void speechRecognition(List<String> stringsList) throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALSpeechRecognition robotRecognition = new ALSpeechRecognition(this.application.session());
        ALMemory geheugen = new ALMemory(this.application.session());
        robotRecognition.setLanguage("Dutch");
        // Make your robot recognize words
        robotRecognition.setVocabulary(stringsList, false);

        geheugen.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                ArrayList<String> data = (ArrayList<String>) o;
                System.out.println(data.get(0));
            }
        });

        robotRecognition.subscribe("Test_ASR");
        Thread.sleep(5000);
        robotRecognition.unsubscribe("Test_ASR");
    }

    public void tell() throws Exception {
        // Add riddles to riddles array and Make your robot say something
        raadsels[0] = "Een man werd dodelijk gewond tijdens een veldslag in 892. Hij overleed en werd begraven in 891. Rara, hoe kan dat?"; // Het zijn jaartallen van vòòr Christus!
        raadsels[1] = "Hoe noem je een metalen klant?"; // Een koper!
        raadsels[2] = "2 vaders en 2 zoons aten tijdens een ontbijt samen 3 eieren, iedereen had één ei. Hoe kan dit?"; // Een van de vaders is ook een opa. Daardoor is de andere vader zowel een zoon als een vader.
        raadsels[3] = "Waarom heeft een vrouw twee paar lippen?"; // Een om ruzie te maken en een om het weer goed te maken!
        // Create an ALTextToSpeech object and link it to your current session
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        // arrayLoop
        for (int i = 0; i < raadsels.length; i++) {
            String raadsel = raadsels[i];
            tts.say(raadsel);
            Thread.sleep(15000);
            // A List of string's
            List<String> stringsList = new ArrayList<>();
            stringsList.add("Het zijn jaartallen van vòòr Christus");
            stringsList.add("Een koper");
            stringsList.add("Een van de vaders is ook een opa. Daardoor is de andere vader zowel een zoon als een vader");
            stringsList.add("Een om ruzie te maken en een om het weer goed te maken");
            speechRecognition(stringsList);

        }
    }


}
>>>>>>> 9cd455333a3bf84d5958dc777e78dbbd3097815f:src/Aghead/NAO.java
