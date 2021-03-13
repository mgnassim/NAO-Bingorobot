package RaadselGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALSpeechRecognition;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALAudioPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NAOraadsels {
    private String naam;
    private Application application;
    protected String[] raadsels = new String[4];
    private int number;

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

    public void luisteren(List<String> woordenlijst) throws Exception {
        ALSpeechRecognition spraakherk = new ALSpeechRecognition(this.application.session());
        ALMemory geheugen = new ALMemory(this.application.session());
        spraakherk.setLanguage("Dutch");
        spraakherk.setVocabulary(woordenlijst, true);
        spraakherk.subscribe("Test_asr");
        Thread.sleep(5000);
        spraakherk.unsubscribe("Test_asr");
        String woord = (String) geheugen.getData("Test_asr");

        for (int i = 0; i < woordenlijst.size(); i++) {
            if (woord.equals(woordenlijst.get(i))) {
                zeg(woordenlijst.get(i));
            }
        }
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
        HashMap<String, String> riddlesAndAnswer = new HashMap<>();
        riddlesAndAnswer.put("Een man werd dodelijk gewond tijdens een veldslag in 892. Hij overleed en werd begraven in 891. Rara, hoe kan dat?",
                "Het zijn jaartallen van vòòr Christus");

        riddlesAndAnswer.put("Hoe noem je een metalen klant?",
                "Een koper");

        riddlesAndAnswer.put("2 vaders en 2 zoons aten tijdens een ontbijt samen 3 eieren, iedereen had één ei. Hoe kan dit?",
                "Een van de vaders is ook een opa. Daardoor is de andere vader zowel een zoon als een vader");

        riddlesAndAnswer.put("Waarom heeft een vrouw twee paar lippen?",
                "Een om ruzie te maken en een om het weer goed te maken");


    }

}



