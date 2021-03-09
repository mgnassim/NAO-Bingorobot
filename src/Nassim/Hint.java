package Nassim;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALSpeechRecognition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hint {

    private Application application;
    String[] hintje = {"Hint"};

    public boolean hintRecognize() throws Exception {
        // Create an ALTextToSpeech object and link it to your current session
        ALSpeechRecognition robotRecognition = new ALSpeechRecognition(this.application.session());
        robotRecognition.setVocabulary(Arrays.asList(hintje), false);

        return false;
    }


}
