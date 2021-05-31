#IT102A-1(Bingobot)

##About this project
For this project we were tasked to program a NAO robot to have
a helping function at the 'Amstelhuis' in Amsterdam. We decided
to create a bingo game with the robot, so the elderly can have some
classic fun together. We achieved this by using the various
APIs documented on the developer guide from Softbank Robotics themselves(https://developer.softbankrobotics.com/nao6/naoqi-developer-guide).

##Before starting
Make sure you understand the NAO6 API documentation before modifying this repository. It is also recommended you know how to
write valid Java code.

##Installation
For the use of this code the following external libraries are needed:
- java-naoqi-sdk-2.5.6.5-win32-vs2013.jar (for the robot)
- itextpdf-5.5.13.2.jar (for the bingocard)
- org.eclipse.paho.client.mqttv3-1.2.5.jar (for the website)

all of these files should be included in the 'External Files' folder.
After you check if you have all the necessary files, navigate to the
'project structure' under the 'File' tab and press the plus icon. now you
can select the files, and you should be able to run the program.

##How to play
If you want play the game, simply run the main method and go to the
website wich you can find at 'index' in the 'webserver' package.
From there you can download bingocards and when you're ready start
the game.

The robot will call out numbers from 1 to 75. Once a number on your card
is called out, you can cross it off. When all your numbers are crossed off, shout
"Bingo" and show the QR-Code on your card to the robot. If the robot determines
that the bingo is valid you win, and the game is ended.

##Contact information
Joris Brouwer:  joris.brouwer2@hva.nl

Aghead Bilal:   aghead.bilal@hva.nl

Nassim Mengat:  nassim.mengat@hva.nl
