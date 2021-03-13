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