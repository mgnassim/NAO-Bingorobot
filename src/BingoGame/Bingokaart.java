package BingoGame;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.eclipse.paho.client.mqttv3.*;
import java.io.*;
import java.util.ArrayList;

public class Bingokaart {

    public static String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    public static String MQTT_USERNAME = "bilalma";
    public static String MQTT_PASSWORD = "lo7ooKsNuabwdwvL2exq";
    static final String pathFontLemon = "BingoFonts\\LemonMilk.otf";
    static final String pathFontItim = "BingoFonts\\Itim-Regular.ttf";

    public static void main(String[] args) throws MqttException {
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
                final String[] bingoLetters = {"B", "I", "N", "G", "O"};
                final String[][] bingoNumbers = new String[5][5];
                final float[] colomnWidth = {2f, 2f, 2f, 2f, 2f};
                final BaseFont bf = BaseFont.createFont(pathFontLemon, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                final BaseFont bf2 = BaseFont.createFont(pathFontItim, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                final Font font1 = new Font(bf, 25); // for bingo characters
                final Font font2 = new Font(bf, 12); // for bigger intro text
                final Font font3 = new Font(bf2, 10); // for intro text
                final Font font4 = new Font(bf, 15); // for bingo numbers
                final Document document = new Document(PageSize.A5);
                final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Bingokaart.pdf"));
                final BaseColor GREEN = new BaseColor(27, 209, 85);
                String playerNumbers = "";
                ArrayList<PdfPTable> bingoTables = new ArrayList<>();
                bingoTables.add(new PdfPTable(bingoNumbers.length));
                ArrayList<BarcodeQRCode> qrCodes = new ArrayList<>();
                int index = 0;

                for (int cards = 0; cards < Integer.parseInt(mqttMessage.toString()); cards++) {
                    try {
                        document.open();
                        font2.setColor(BaseColor.BLUE);
                        font1.setColor(BaseColor.WHITE);
                        document.add(new Paragraph("Welkom bij de naoBingo!", font2));
                        document.add(new Paragraph("Wanneer je klaar bent roep heel hard BINGOOO! en scan de QR code bij de robot om te zien of je wint!", font3));

                        bingoTables.get(index).setWidthPercentage(105);
                        bingoTables.get(index).setSpacingBefore(5f);
                        bingoTables.get(index).setWidths(colomnWidth); // lay-out of the pdfTables

                        for (int i = 0; i < bingoLetters.length; i++) {
                            PdfPCell letter = new PdfPCell(new Paragraph(bingoLetters[i], font1)); // printing the letters of B.I.N.G.O

                            letter.setBackgroundColor(BaseColor.YELLOW); // for the background color of the numbers.

                            letter.setPadding(10f);
                            letter.setBorderWidth(1f);
                            letter.setFixedHeight(50f);
                            letter.setPaddingLeft(25f); // for the lay-out.
                            letter.setPaddingTop(10f);

                            if (i == 1)
                                letter.setPaddingLeft(33f); // the letter I had less width so needed to put it more in the middle.

                            bingoTables.get(index).addCell(letter); // adds the cell to the document/pdf.
                        }

                        randomNumbersOnBingoCard(bingoNumbers); // random numbers for the bingocard
                        for (int i = 0; i < bingoNumbers.length; i++) {
                            for (int j = 0; j < bingoNumbers[i].length; j++) {

                                font4.setColor(BaseColor.WHITE);
                                PdfPCell a = new PdfPCell(new Paragraph(bingoNumbers[i][j], font4));

                                a.setBackgroundColor(GREEN);
                                a.setFixedHeight(50f);
                                a.setPaddingLeft(30f); // lay out of numbers
                                a.setPaddingTop(20f);

                                playerNumbers = playerNumbers.concat(bingoNumbers[i][j] + " "); // Data to put on qr code
                                bingoTables.get(index).addCell(a); // Adding numbers to document per cell.
                            }
                        }

                        qrCodes.add(new BarcodeQRCode(playerNumbers, 1000, 1000, null)); // make qr code and add to arraylist
                        Image codeQrImage = qrCodes.get(index).getImage();
                        codeQrImage.scaleToFit(180, 180); // lay out and size of qr code.
                        codeQrImage.setAbsolutePosition(20, 30);

                        Image img = Image.getInstance("naoQI.jpg");
                        img.scaleToFit(150, 150); // lay out of nao picture
                        img.setAbsolutePosition(230f, 40f);

                        document.add(bingoTables.get(index));
                        document.add(img);
                        document.add(codeQrImage); // adding everything to document.
                        document.newPage(); // making a new page for second.. third.. etc.. document/bingocard.

                        bingoTables.add(new PdfPTable(bingoNumbers.length)); // making tables for next bingocard
                        playerNumbers = ""; // resetting contents of qr code for next card.
                        index++; // for the next card.

                    } catch (DocumentException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                document.close();
                writer.close();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        client.subscribe("bilalma/robot/bingo2"); // for MQTT connection
    }

    private static void randomNumbersOnBingoCard(String[][] array) {

        ArrayList<String> cardNumbers = new ArrayList<>();

        // cardNumbers is being used to check if a number generated is unique or not.
        // within each for loop a maximum and minimum number is generated for each vertical column of the bingocard.

        for (int i = 0; i < array.length; i++) {
            String number = String.valueOf((int) (Math.random() * ((15 - 1) + 1)) + 1);

            while (cardNumbers.contains(number)) {
                number = String.valueOf((int) (Math.random() * ((15 - 1) + 1)) + 1);
            }
            array[i][0] = number;
            cardNumbers.add(number);
        }
        for (int i = 0; i < array.length; i++) {
            String number = String.valueOf((int) (Math.random() * ((30 - 16) + 1)) + 16);

            while (cardNumbers.contains(number)) {
                number = String.valueOf((int) (Math.random() * ((30 - 16) + 1)) + 16);
            }
            array[i][1] = number;
            cardNumbers.add(number);
        }
        for (int i = 0; i < array.length; i++) {
            String number = String.valueOf((int) (Math.random() * ((45 - 31) + 1)) + 31);

            while (cardNumbers.contains(number)) {
                number = String.valueOf((int) (Math.random() * ((45 - 31) + 1)) + 31);
            }
            array[i][2] = number;
            cardNumbers.add(number);
        }
        for (int i = 0; i < array.length; i++) {
            String number = String.valueOf((int) (Math.random() * ((60 - 46) + 1)) + 46);

            while (cardNumbers.contains(number)) {
                number = String.valueOf((int) (Math.random() * ((60 - 46) + 1)) + 46);
            }
            array[i][3] = number;
            cardNumbers.add(number);
        }
        for (int i = 0; i < array.length; i++) {
            String number = String.valueOf((int) (Math.random() * ((75 - 61) + 1)) + 61);

            while (cardNumbers.contains(number)) {
                number = String.valueOf((int) (Math.random() * ((75 - 61) + 1)) + 61);
            }
            array[i][4] = number;
            cardNumbers.add(number);
        }
    }
}
