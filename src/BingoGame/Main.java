package BingoGame;

import org.eclipse.paho.client.mqttv3.*;

public class Main {

    // Host of the MQTT broker
    public static String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    // Username from hva-robots.nl
    public static String MQTT_USERNAME = "bilalma";
    // Password from hva-robots.nl
    public static String MQTT_PASSWORD = "lo7ooKsNuabwdwvL2exq";

    // both to use for the flow of the game.
    public static boolean bingo = false;
    public static boolean start = false;

    public static void main(String[] args) throws Exception {
        NAO nao = new NAO(); // Creating an object of NAO
        nao.connect("padrick.robot.hva-robots.nl", 9559); // connecting to the robot

        MqttClient client = new MqttClient(MQTT_HOST, MqttClient.generateClientId()); // Every successful connection to the Robot a client id is generated.
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(MQTT_USERNAME);
        connectOptions.setPassword(MQTT_PASSWORD.toCharArray());
        client.connect(connectOptions); // connecting with the MQTT broker.

        nao.standUp(); // to let the robot stand before starting the game.
        nao.barcodeReader(); // On the background waits for a qr code to read.

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {}

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                nao.configurationListenToStart(); // So the robot listens to 'start' on the background.
                nao.say("als u wilt beginnen, zeg dan Start"); // announces the game is starting
                while (!Main.start) {
                    nao.listenToWord(); // waits for the word start to continue.
                }

                nao.configurationListenToBingo(); // So the robot listens to 'Bingo' on the background.
                while (!Main.bingo) { // continues while Bingo hasn't been received.
                    nao.sayNumbers(); // says a random number.
                    nao.listenToWord(); // listens to the word Bingo everytime after a number has been spoken.
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
        });
        client.subscribe("bilalma/robot/bingo"); // to connect with the start function/website.
    }
}
