# IT102A-1(Bingobot)

## About this project
For this project we were tasked to program a NAO robot to have
a helping function at the 'Amstelhuis' in Amsterdam. We decided
to create a bingo game with the robot, so the elderly can have some
classic fun together. We achieved this by using the various
APIs documented on the developer guide from Softbank Robotics themselves(https://developer.softbankrobotics.com/nao6/naoqi-developer-guide).

## Before starting
Make sure you understand the NAO6 API documentation before modifying this repository. It is also recommended you know how to
write valid Java code.

## Installation
For the use of this code the following external libraries are needed:
- java-naoqi-sdk-2.5.6.5-win32-vs2013.jar (for the robot)
- itextpdf-5.5.13.2.jar (for the bingocard)
- org.eclipse.paho.client.mqttv3-1.2.5.jar (for the website)

Paho mqtt client download:

https://jar-download.com/artifacts/org.eclipse.paho/org.eclipse.paho.client.mqttv3

iTextPDF library download:

https://jar-download.com/artifacts/com.itextpdf/itextpdf/5.5.13.2/source-code

Java NAOQI SDK download:

http://doc.aldebaran.com/2-1/dev/java/index_java.html

When having downloaded all these files onto your computer, you have to add them to your project.
To add the files to your project do the following:

    Navigate to the File Tab top left of your screen
    Scroll down to 'Project Structure'
    Navigate to Modules
    Go to dependencies
    Click the '+' icon beneath Module SDK (depending on your intellij version)
    Click JARs or directories
    Navigate to the file you downloaded and click Add.
    Do this for all your files.


SDK Installation:

This program runs on Java 8 and *32-bits!* so be sure to download Java 8 onto your computer.
You are able to do this from the official Oracle site, here is a link:
https://www.oracle.com/nl/java/technologies/javase/javase-jdk8-downloads.html


## MQTT

For this project MQTT is being used for two things. The activation for the start of the game remotely from the website.
And for the bingo card generator used in the website. To be able to use these functions on the website, you need to run the MQTT file on the background in intellij (Java).

Paho is the main implementation used for this MQTT server.

Basics being used for MQTT communication between robot and website:

An HTML file containing:

    an input or a button to send values to Javascript
    an <script src=javascript file location tag> to communicate with Javascript

A Javascript file containing:

    A function connecting to the host (subscribing) which contains an onConnect: (method to put in here that sends the message when connecting)
    A function containing the destination name of the subscriber which also sends the message received from the HTML file.

## How to play
First of all what is needed to play the bingo game, are bingo cards.
You are able to generate these cards from the website from a pdf file for a set amount of players.
If you do not know how to generate these cards we recommend reading the MQTT section again and the comments left in the Java class making these cards.
You are not able to play the Bingo with the robot unless you have these cards because they contain a very special QR-Code in which contains the numbers of the players personal card. These are needed later on to verify your win or loss.

When finally having these special cards, you are able to start the game remotely from the website. Again, read the MQTT section if you do not know how to start the game.

When having started the game with the button on the homepage, the robot shall ask you for confirmation to start the game.
To activate the game you just need to say the keyword: START!. We recommend when saying keywords like BINGO! or START! that you should be near enough to the robot since the speechrecognition of the robot is not perfect.

If the robot confirms that you said START! it assumes all the players are ready and the game is being started.
The robot will start to call out numbers from 1 to 75 (Like a traditional bingo game). Once a number on your card is called out, you can cross it off with a pencil. When all your numbers are crossed off, shout Bingo! very hard and the robot should acknowledge you said the keyword. If the robot did acknowledge it, the robot will ask you to show the QR-Code on your card you generated to the robot. Be informed that the camera of the robot is not with its eyes! It is located right in the middle of its forehead. To learn more about the positions of the microphones and camera of the robot I recommend reading the documentation on the official Softbank Robotics website with the following link:

https://developer.softbankrobotics.com/nao6/nao-documentation/nao-developer-guide/technical-overview

The robot compares the values given in the QR-code and the values of the numbers the robot said out loud during the game.
When this comparison is true, the robot grants you the win and should say: Congratulations, you won the game. At the end there is also a short dance performed by the robot when there is a valid winner. The winner and everyone else can ofcourse dance with the robot.

## Important Information

    ~The START keyword should only be said after hearing a second beep. 
    This means that the robot is ready to hear you out (subscribe/unsubscribe).
    The advice above also applies to the BINGO keyword.

    ~The robot makes a little movement while saying a number but there is still a chance the robot can fall over.
    That is why it is recommended to have someone close to the robot if that happens.

    ~Only scan the QR-Code on the card when the robot is done with talking and you hear a second beep.
    This is the most safe way to recognize a valid winner and overload of data to the robot. 

## Contact information
Joris Brouwer: joris.brouwer2@hva.nl

Aghead Bilal: aghead.bilal@hva.nl

Nassim Mengat: nassim.mengat@hva.nl
