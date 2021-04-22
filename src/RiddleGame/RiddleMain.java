package RiddleGame;

import java.util.List;

public class RiddleMain {
    public RiddleNAO nao = new RiddleNAO();

    public void sayRiddle(String riddle, List<String> correctAwnsers, List<String> falseAwnsers) throws Exception {
        nao.say(riddle);


    }

}
