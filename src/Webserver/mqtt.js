var mqtt;
var host = "mqtt.hva-robots.nl";
var port = 443;
var username = "bilalma";
var password = "lo7ooKsNuabwdwvL2exq";

/* MQTTconnect() is used to connect to the Java application. Using MQTT to send messages which are later on used in the
Java application. For instance: MQTTConnect2() connects to Bingocard. When succesfully connected to Bingocard, onConnect2()
is used to send to the topic/destinationname the value put in at the website.

MQTTConnect() is triggered in the html when a button is being clicked. When the button is clicked a message is sent to the Main.java
This triggers the game to be started.
 */


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

    mqtt.connect(options);
}

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

    mqtt.connect(options);
}
