import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();
        nao.verbind("Padrick.robot.hva-robots.nl", 9559);

        List <String> woordenlijst = new ArrayList<>();
        woordenlijst.add("12");
        woordenlijst.add("allemaal");
        woordenlijst.add("februari");
        woordenlijst.add("december");

        nao.zeg("hoeveel maanden hebben 28 dagen?");

        nao.luisteren(woordenlijst);

        Thread.sleep(5000);

        nao.scan();



    }
}
