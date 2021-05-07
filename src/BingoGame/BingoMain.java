package BingoGame;

<<<<<<< HEAD
import org.eclipse.paho.client.mqttv3.*;

public class BingoMain {

    // Host of the MQTT broker
    public static String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    // Client id, unique name for each client, prefix with your username
    public static String MQTT_CLIENT_ID = "bilalma_test";
    // Username from hva-robots.nl
    public static String MQTT_USERNAME = "bilalma";
    // Password from hva-robots.nl
    public static String MQTT_PASSWORD = "lo7ooKsNuabwdwvL2exq";
=======
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BingoMain {

    static String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    static String MQTT_CLIENT_ID = "mengatn_%";
    static String MQTT_USERNAME = "mengatn";
    static String MQTT_PASSWORD = "lhSc0gQN5dvFNrjGXq3F";
>>>>>>> 3e7e4c5c19f586b9a21904ed272c983022c7a7a4

    public static void main(String[] args) throws Exception {

        List<String> bingoKeyword = new ArrayList<>();
        bingoKeyword.add("bingo");
        bingoKeyword.add("BINGO");
        bingoKeyword.add("BINGOOOO!");

        BingoNAO nao = new BingoNAO();
        nao.connect("naomi.robot.hva-robots.nl", 9559);

<<<<<<< HEAD
        MqttClient client = new MqttClient(MQTT_HOST, MqttClient.generateClientId());
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(MQTT_USERNAME);
        connectOptions.setPassword(MQTT_PASSWORD.toCharArray());

        client.connect(connectOptions);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println("Bericht ontvangen");
                System.out.print("Topic: ");
                System.out.println(topic);
                System.out.print("Bericht: ");
                System.out.println(mqttMessage.toString());
                nao.say(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        client.subscribe("bilalma/robot/bingo");
/*
        nao.say("Ik kan nederlands spreken");

        while (true) Thread.sleep(1000);*/
=======
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
>>>>>>> 3e7e4c5c19f586b9a21904ed272c983022c7a7a4

    public static void call() throws DocumentException, IOException {
        Bingokaart.main(null);
    }
}