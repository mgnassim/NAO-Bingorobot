package BingoGame;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BingoMain {

    static String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    static String MQTT_CLIENT_ID = "mengatn_%";
    static String MQTT_USERNAME = "mengatn";
    static String MQTT_PASSWORD = "lhSc0gQN5dvFNrjGXq3F";

    public static void main(String[] args) throws Exception {

        List<String> bingoKeyword = new ArrayList<>();
        bingoKeyword.add("bingo");
        bingoKeyword.add("BINGO");
        bingoKeyword.add("BINGOOOO!");

        BingoNAO nao = new BingoNAO();
        nao.connect("naomi.robot.hva-robots.nl", 9559);

        nao.sayNummers();
        nao.listenToBingo(bingoKeyword);

//        MqttClient client = new MqttClient(MQTT_HOST, MQTT_CLIENT_ID);
//
//        MqttConnectOptions connectOptions = new MqttConnectOptions();
//        connectOptions.setUserName(MQTT_USERNAME);
//        connectOptions.setUserName(MQTT_PASSWORD);
//        connectOptions.setUserName(MQTT_PASSWORD);
//
//        client.connect(connectOptions);
//
//        System.out.println("Verbonden? " + client.isConnected());

        while (true)
            Thread.sleep(1000);

    }

    public static void call() throws DocumentException, IOException {
        Bingokaart.main(null);
    }
}