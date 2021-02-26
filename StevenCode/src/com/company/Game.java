package com.company;

public class Game {
    // Random number
    private int number;

    // Constructor
    public Game(int number) {
        this.number = number;
    }

    // Game questions
    public void GameQuestions() {
        // Switch to choose between the Questions
        switch (number) {
            // If number = 1, show Question 1
            case 1:
                System.out.println("Een man werd dodelijk gewond tijdens een veldslag in 892. Hij overleed en werd " +
                        "begraven in 891. Rara, hoe kan dat");
                break;
            // If number = 2, show Question 2
            case 2:
                System.out.println("Hoe noem je een metalen klant?");
                break;
            // If number = 3, show Question 3
            case 3:
                System.out.println("2 vaders en 2 zoons aten tijdens een ontbijt samen 3 eieren, iedereen had één ei." +
                        " Hoe kan dit?");
                break;
            // If number = 4, show Question 4
            case 4:
                System.out.println("Waarom heeft een vrouw twee paar lippen?");
                break;
            // If something went wrong go default and send message
            default:
                System.out.println("Error 404");
                break;
        }
    }

    // Game Answer
    public void GameAnswer(String answer) {
        // Switch to check answer's of Questions
        switch (number) {
            // If number = 1, show Question 1
            case 1:
                // See if answer contains critical keywords
                if (answer.contains("jaartallen") && answer.contains("voor") && answer.contains("christus")) {
                    // answer is Good
                    System.out.println("Dit Antwoord is goed!");
                } else {
                    // answer is Wrong
                    System.out.println("Sorry Dat is fout");
                }
                break;
            // If number = 2, show Question 2
            case 2:
                if (answer.contains("koper")) {
                    // answer is Good
                    System.out.println("Dit Antwoord is goed!");
                } else {
                    // answer is Wrong
                    System.out.println("Sorry Dat is fout");
                }
                break;
            // If number = 3, show Question 3
            case 3:
                if (answer.contains("vader") && answer.contains("opa") && answer.contains("zoon")) {
                    // answer is Good
                    System.out.println("Dit Antwoord is goed!");
                } else {
                    // answer is Wrong
                    System.out.println("Sorry Dat is fout");
                }
                break;
            // If number = 4, show Question 4
            case 4:
                if (answer.contains("ruzie") && answer.contains("maken") && answer.contains("goed")) {
                    // answer is Good
                    System.out.println("Dit Antwoord is goed!");
                } else {
                    // answer is Wrong
                    System.out.println("Sorry Dat is fout");
                }
                break;
            // If something went wrong go default and send message
            default:
                System.out.println("Error: 404");
                break;
        }
    }
}
