package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;


public class Main {
    // Timer
    Timer timer;

    public static void main(String[] args) {
        // Scanner input question
        Scanner answer = new Scanner(System.in);
        // Generate random number for Question
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);

        // Start Game
        Game game = new Game(randomNum);
        // Start Timer
        new Main();
        //Send Question
        game.GameQuestions();
        // put answer in question.
        String AnswerInput = answer.nextLine();
        // Check Answer
        game.GameAnswer(AnswerInput.toLowerCase());
    }

    // To Start Timer
    public Main() {
        // Start Timer
        timer = new Timer();
        // Start Music
        playSound("src/Sound/Timer2.mid");
        // Schedule
        timer.schedule(new TimerTask(), 5*1000);
    }

    // Timer Task
    class TimerTask extends java.util.TimerTask {

        @Override
        public void run() {
            // print no time
            System.out.println("Sorry geen tijd meer!");
            // cancel timer
            timer.cancel();
            // Cancel program
            System.exit(69);
        }
    }

    // Play Sound
    void playSound(String soundFile) {
        try {
            // Search file
            File f = new File(soundFile);
            // Get Stream
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            // Get clip
            Clip clip = AudioSystem.getClip();
            // Open Clip
            clip.open(audioIn);
            // Start playing
            clip.start();
        } catch(Exception e) {
            // Throw exception
            System.out.println(e.getMessage());
        }
    }
}