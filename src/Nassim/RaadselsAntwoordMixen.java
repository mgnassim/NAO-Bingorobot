package Nassim;

import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.Application;
import java.util.HashMap;
import java.util.Random;


public class RaadselsAntwoordMixen extends Hint {

    private Application application;
    private Random r = new Random();

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

        for (String key : riddlesAndAnswer.keySet()) {
            tts.say(riddlesAndAnswer.get(r.nextInt(riddlesAndAnswer.size())));
            Thread.sleep(10000);


        }

    }

    public Random getRandomRiddleNumber() {
        return this.r;
    }
}
