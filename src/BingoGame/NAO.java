package BingoGame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.*;

public class NAO {

    private final List<String> words = new ArrayList<>(); // vocabulary of the robot.
    private final ArrayList<String> spokenNumbers = new ArrayList<>(); // numbers said during the bingo.
    private String value; // The numbers of the bingo in a string.
    private String[] numbers; // The numbers of the bingo in a array.
    private Application application; // To connect with the robot

    public void connect(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
        this.application = new Application(new String[]{}, robotUrl);
        application.start();
    }

    public void standUp() throws Exception {
        // Create an ALRobotPosture object and link it to the session
        ALRobotPosture alRobotPosture = new ALRobotPosture(this.application.session());
        // Make the robot do something
        alRobotPosture.goToPosture("Stand", 0.75f);
    }


    public void say(String tekst) throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        tts.setParameter("speed", 75f); // slows the speed of speaking (max = 100f)
        tts.setVolume(1.0f); // sets the volume to the maximum
        tts.setLanguage("Dutch"); // sets the language to Dutch
        tts.say(tekst); // says the text put in the parameter of the method.
    }

    public void animation(String path) throws Exception {
        // Create an ALAnimationPlayer object and link it to the session
        ALAnimationPlayer alAnimationPlayer = new ALAnimationPlayer(this.application.session());
        // Make the robot do somethings
        alAnimationPlayer.run(path);
    }

    public void animatedSpeech(String text) throws Exception {
        // Create an ALAnimationSpeech object and link it to the session
        ALAnimatedSpeech alAnimatedSpeech = new ALAnimatedSpeech(this.application.session());
        // Make the robot do something
        alAnimatedSpeech.say(text);

    }

    public void configurationListenToStart() throws Exception{
        words.add("Start"); // adds a word to the list of words known to the robot.
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        try {
            speechrec.unsubscribe("Test_asr"); // safety measure to unsubscribe to the speechrec on the beginning
        } catch(Exception e) {
            e.printStackTrace();
        }

        speechrec.setVocabulary(words, true); /* sets the Vocabulary of the robot to the words given above in the list 'words'.
        It only recognizes the word 'Start' at the moment.
        The reason enabledWordSpotting is true, is that it indicates everything except the word Start as garbage results.*/

        memory.subscribeToEvent("WordRecognized", new EventCallback() { // Subscribes to the event WordRecognized.
            // Wordrecognized is raised when one of the words has been recognized from setVocabulary.
            @Override
            public void onEvent(Object o) {
                List<Object> data = (List<Object>) o; // Casting the data of 'o' to a list of <Object>s.
                String value = (String) data.get(0); // Casting the first index of 'data' to a String to check later on.
                float confidence = (float) data.get(1); // Casting the second index of 'data' to a float to check later on the trustworthiness.

                if (!value.equals("")) { // Here is checked if value is not empty.

                    if (confidence > 0.30f) { // This is used to check if it really was the word 'Start' said.
                        Main.start = true; // used to stop the loop in Main for checking the word start..
                        try {
                            say("Oké, we kunnen beginnen");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void listenToWord() throws Exception {
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        speechrec.subscribe("Test_asr"); // Writes information to the event 'Wordrecognized'.
        Thread.sleep(3000); // Time given to say the word needed to recognize.
        speechrec.unsubscribe("Test_asr"); // Stops writing information to the event 'Wordrecognized'.
    }

    public void configurationListenToBingo() throws Exception {
        words.clear(); // clears the vocabulary of the robot.
        words.add("Bingo");
        ALSpeechRecognition speechrec = new ALSpeechRecognition(this.application.session());
        ALMemory memory = new ALMemory(this.application.session());
        speechrec.setLanguage("English"); // sets language to English
        try {
            speechrec.unsubscribe("Test_asr"); // safety measure to unsubscribe to the speechrec on the beginning
        } catch (Exception e) {
            e.printStackTrace();
        }

        speechrec.setVocabulary(words, true); // same as configurationListenToStart();

        memory.subscribeToEvent("WordRecognized", new EventCallback() {
            @Override
            public void onEvent(Object o) throws InterruptedException, CallError {
                List<Object> data = (List<Object>) o;
                String value = (String) data.get(0);
                float confidence = (float) data.get(1);

                if (!value.equals("")) {

                    if (confidence > 0.40f) { // for the word Bingo the confidence level is a bit higher to let the player scream!
                        try {
                            // lets the player know to scan the bingocard.
                            say("Scan de qr code van je bingokaart bij mijn hoofd om te zien of je gewonnen hebt.");
                            if (scan()) { // checks the card
                                say("Gefeliciteerd je hebt gewonnen"); // says the player won
                                Main.bingo = true; // stops the loop in main
                                animation("wd/ad"); // robot dances
                            }
                            else{
                                say("U heeft helaas niet gewonnen"); // says the player didnt win
                                Main.bingo = false; // continues with the game
                            }
                        } catch (Exception e) {
                            System.out.println("Geen barcode ontvangen");
                        }
                    }
                }
            }
        });
    }

    public void sayNumbers() throws Exception {
        ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
        tts.setParameter("speed", 50f);
        tts.setVolume(1.0f);
        tts.setLanguage("Dutch");

        int randomNumber = (int) (Math.random() * 75) + 1; // generates a random number between 1 and 75.

        if (spokenNumbers.contains(String.valueOf(randomNumber))) { // checks whether the new number made already exists in the list spokenNumbers.
            while (spokenNumbers.contains(String.valueOf(randomNumber))) // generates a new number the whole time
                randomNumber = (int) (Math.random() * 75) + 1; // and stops generating when a new unique number is made that didnt exist before.
        }
        spokenNumbers.add(String.valueOf(randomNumber)); // adds the number to the list

        try {
            // says the generated number with a little animation
            animatedSpeech("Het volgende nummer is ^start(animations/Stand/Gestures/shortrange)" + randomNumber + "^wait(animations/Stand/Gestures/shortrange)");
            Thread.sleep(1000); // pauses a little bit after saying the number.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void barcodeReader() throws Exception {
        ALMemory memory = new ALMemory(this.application.session());
        ALBarcodeReader scanner = new ALBarcodeReader(this.application.session());
        memory.subscribeToEvent("BarcodeReader/BarcodeDetected", new EventCallback() { // subscribes to the event of reading a qrCode/Barcode.
            @Override
            public void onEvent(Object o) {
                List<Object> data = (List<Object>) o; // Casting the data received of the event to a list of <Object>s.
                ArrayList<Object> qrCode = (ArrayList<Object>) data.get(0); // Casting the first index of 'data'.
                value = (String) qrCode.get(0); // Casting the numbers to a String.
                numbers = value.split("\\s+"); // Splitting the numbers to fix it as an Array to use later.
            }
        });
        try {
            scanner.unsubscribe("QR-Code"); // to Stop writing qr code infortmation to 'QR-code' of the event.
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.subscribe("QR-Code"); // Writing qr code information to 'QR-Code'.
    }

    public boolean scan(){
        ArrayList<String> cardNumbers = new ArrayList<>(Arrays.asList(numbers)); // converting the numbers array to an arraylist.
        return new HashSet<>(spokenNumbers).containsAll(cardNumbers); // checking the spokenNumbers with the numbers of the player.
    }
}
