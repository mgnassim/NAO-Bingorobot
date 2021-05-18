var mqtt;
const reconnectTimeout = 2000;
var host = "mqtt.hva-robots.nl";
var port = 443;
var username = "bilalma";
var password = "lo7ooKsNuabwdwvL2exq";

function onConnect() {
    console.log("Connected");
    message = new Paho.MQTT.Message("Dit is een test voor de bingo");
    message.destinationName = "bilalma/robot/bingo";
    mqtt.send(message);
}

function MQTTconnect() {
    console.log("connecting to " + host + "" + port);
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

var sum = document.getElementById('searchTerm').value;

function onConnect2() {
    console.log("Connected");
    message2 = new Paho.MQTT.Message(document.getElementById('searchTerm').value);
    message2.destinationName = "bilalma/robot/bingo2";
    console.log(message2);
    mqtt.send(message2);
}

function MQTTconnect2() {
    console.log("connecting to " + host + "" + port);
    mqtt = new Paho.MQTT.Client(host, port, "clientjs");
    var options = {
        useSSL: true,
        timeout: 3,
        userName: username,
        password: password,
        onSuccess: onConnect2,
    };

    mqtt.connect(options); // connect
}
