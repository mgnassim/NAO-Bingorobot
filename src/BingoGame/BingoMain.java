package BingoGame;

public class BingoMain {

    public static void main(String[] args) throws Exception {

        BingoNAO nao = new BingoNAO();
        nao.connect("Padrick.robot.hva-robots.nl", 9559);
        nao.say("aaaaaaaaaaaaaa");

        while (true) Thread.sleep(1000);

    }
}