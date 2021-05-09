package BingoGame;

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

    public static void main(String[] args) throws Exception {

        BingoNAO nao = new BingoNAO();
        nao.connect("naomi.robot.hva-robots.nl", 9559);

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

    }
}