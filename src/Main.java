import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();
        nao.connect("Padrick.robot.hva-robots.nl", 9559);

        List <String> wordlist = new ArrayList<>();
        wordlist.add("12");
        wordlist.add("allemaal");
        wordlist.add("februari");
        wordlist.add("december");

        nao.say("hoeveel maanden hebben 28 dagen?");

        nao.listen(wordlist);

        Thread.sleep(5000);

        nao.scan();



    }
}
