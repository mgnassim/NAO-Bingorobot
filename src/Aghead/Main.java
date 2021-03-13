package Aghead;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();
        nao.connect("padrick.robot.hva-robots.nl", 9559);
        nao.say("Hallo mijn vrienden \\pau=1000\\ hoe gaat het?");
        Thread.sleep(5000);
        nao.tell();
    }

}
