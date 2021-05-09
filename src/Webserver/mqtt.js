var mqtt;
var reconnectTimeout = 2000;
var host = "mqtt.hva-robots.nl"; // change this
var port = 443;
var username = "bilalma";
var password = "lo7ooKsNuabwdwvL2exq";

function onConnect() {
    // Once a connection has been made, make a sub and send a message.
    console.log("Connected");
    message = new Paho.MQTT.Message("Dit is een test voor de bingo"); // bericht
    message.destinationName = "bilalma/robot/bingo"; // topic
    mqtt.send(message);
}

function MQTTconnect() {
    console.log("connecting to "+ host + "" + port);
    mqtt = new Paho.MQTT.Client(host, port, "clientjs");
    var options = {
      useSSL: true,
      timeout: 3,
        userName: username,
        password: password,
      onSuccess: onConnect,
    };

    mqtt.connect(options); // connect
}