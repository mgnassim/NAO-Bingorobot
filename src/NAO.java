import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALSpeechRecognition;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALMemory;

import java.util.List;

public class NAO {
    private String naam;
    private Application application;

    public void verbind(String hostname, int port){
        String robotUrl = "tcp://" + hostname+ ":" + port;
        // Create a new application
        this.application = new Application(new String[]{}, robotUrl);
        // Start your application
        application.start();
    }

    public void staan() throws Exception {
        ALRobotPosture posture = new ALRobotPosture(this.application.session());
        posture.goToPosture("Stand", 0.75f);
    }

    public void zitten() throws Exception {
        ALRobotPosture posture = new ALRobotPosture(this.application.session());
        posture.goToPosture("Sit", 0.75f);
    }
    public void zeg(String tekst) throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        // Make your robot say something
        tts.say(tekst);
    }

    public void luisteren(List<String> woordenlijst)throws Exception{
        ALSpeechRecognition spraakherk = new ALSpeechRecognition(this.application.session());
        ALMemory geheugen = new ALMemory(this.application.session());
        spraakherk.setLanguage("Dutch");
        spraakherk.setVocabulary(woordenlijst,true);
        spraakherk.subscribe("Test_asr");
        Thread.sleep(5000);
        spraakherk.unsubscribe("Test_asr");
        String woord = (String) geheugen.getData("Test_asr");

        for(int i = 0; i <woordenlijst.size(); i++){
            if(woord.equals(woordenlijst.get(i))){
                zeg(woordenlijst.get(i));
            }
        }

    }



}
