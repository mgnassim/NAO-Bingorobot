package BingoGame;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Bingokaart {

    public static String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    // Client id, unique name for each client, prefix with your username
    public static String MQTT_CLIENT_ID = "bilalma_test";
    // Username from hva-robots.nl
    public static String MQTT_USERNAME = "bilalma";
    // Password from hva-robots.nl
    public static String MQTT_PASSWORD = "lo7ooKsNuabwdwvL2exq";

    static final String pathFontLemon = "BingoFonts\\LemonMilk.otf";
    static final String pathFontItim = "BingoFonts\\Itim-Regular.ttf";

    public void call() throws MqttException, DocumentException, IOException {
        Bingokaart.main(null);
    }

    public static void main(String[] args) throws DocumentException, IOException, MqttException {
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
                final String[][] bingoNummers = new String[5][5];
                final float[] kolomBreedtes = {2f, 2f, 2f, 2f, 2f};
                final BaseFont bf = BaseFont.createFont(pathFontLemon, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                final BaseFont bf2 = BaseFont.createFont(pathFontItim, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                final Font font1 = new Font(bf, 25); // for bingo characters
                final Font font2 = new Font(bf, 12); // for bigger intro text
                final Font font3 = new Font(bf2, 10); // for intro text
                final Font font4 = new Font(bf, 15); // for bingo numbers
                final Document document = new Document(PageSize.A5);
                final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Bingokaart.pdf"));
                String spelercijfers = "";
                ArrayList<PdfPTable> bingoTables = new ArrayList<>();
                bingoTables.add(new PdfPTable(bingoNummers.length));
                ArrayList<BarcodeQRCode> qrCodes = new ArrayList<>();
                int index = 0;
                System.out.println("Bericht ontvangen");
                System.out.print("Topic: ");
                System.out.println(topic);
                System.out.print("Bericht: ");
                System.out.println(mqttMessage.toString());

                for (int cards = 0; cards < Integer.parseInt(mqttMessage.toString()); cards++) {
                    try {
                        document.open();
                        font2.setColor(BaseColor.BLUE);
                        font1.setColor(BaseColor.WHITE);
                        document.add(new Paragraph("Welkom bij de naoBingo!", font2));
                        document.add(new Paragraph("Wanneer je klaar bent roep heel hard BINGOOO! en scan de QR code bij de robot om te zien of je wint!", font3));

                        bingoTables.get(index).setWidthPercentage(105);
                        bingoTables.get(index).setSpacingBefore(5f);
                        bingoTables.get(index).setWidths(kolomBreedtes);

                        BaseColor ORANGEE = new BaseColor(255,165,0);

                        for (int i = 0; i < 5; i++) {
                            PdfPCell letter = new PdfPCell(new Paragraph(bingoLetters[i], font1));
                            letter.setPadding(10f);
                            letter.setBackgroundColor(BaseColor.YELLOW);
                            letter.setBorderWidth(1f);
                            letter.setFixedHeight(50f);
                            letter.setPaddingLeft(25f);
                            letter.setPaddingTop(10f);
                            if (i == 1)
                                letter.setPaddingLeft(33f);
                            bingoTables.get(index).addCell(letter);
                        }

                        BaseColor GREENN = new BaseColor(27, 209, 85);

                        randomNummersOpKaart(bingoNummers);
                        for (int i = 0; i < bingoNummers.length; i++) {
                            for (int j = 0; j < bingoNummers[i].length; j++) {

                                font4.setColor(BaseColor.WHITE);
                                PdfPCell a = new PdfPCell(new Paragraph(bingoNummers[i][j], font4));

                                a.setBackgroundColor(GREENN);
                                a.setFixedHeight(50f);
                                a.setPaddingLeft(30f);
                                a.setPaddingTop(20f);

                                spelercijfers = spelercijfers.concat(bingoNummers[i][j] + " ");
                                bingoTables.get(index).addCell(a);
                            }
                        }

                        qrCodes.add(new BarcodeQRCode(spelercijfers, 1000, 1000, null));
                        Image codeQrImage = qrCodes.get(index).getImage();
                        codeQrImage.scaleToFit(180, 180);
                        codeQrImage.setAbsolutePosition(20, 30);

                        Image img = Image.getInstance("naoQI.jpg");
                        img.scaleToFit(150, 150);
                        img.setAbsolutePosition(230f, 40f);

                        document.add(bingoTables.get(index));
                        document.add(img);
                        document.add(codeQrImage);
                        document.newPage();

                        System.out.println("\nBingokaart " + (index + 1) + " is gemaakt.");
                        System.out.println("Met cijfers: " + spelercijfers);

                        bingoTables.add(new PdfPTable(5));
                        spelercijfers = "";
                        index++;

                    } catch (DocumentException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                document.close();
                writer.close();
                Desktop.getDesktop().open(new File("Bingokaart.pdf"));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        client.subscribe("bilalma/robot/bingo2");
    }

    private static void randomNummersOpKaart(String[][] array) {

        ArrayList<String> cardNumbers = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            String nummer = String.valueOf((int) (Math.random() * ((15 - 1) + 1)) + 1);

            while (cardNumbers.contains(nummer)) {
                nummer = String.valueOf((int) (Math.random() * ((15 - 1) + 1)) + 1);
            }
            array[i][0] = nummer;
            cardNumbers.add(nummer);
        }
        for (int i = 0; i < array.length; i++) {
            String nummer = String.valueOf((int) (Math.random() * ((30 - 16) + 1)) + 16);

            while (cardNumbers.contains(nummer)) {
                nummer = String.valueOf((int) (Math.random() * ((30 - 16) + 1)) + 16);
            }
            array[i][1] = nummer;
            cardNumbers.add(nummer);
        }
        for (int i = 0; i < array.length; i++) {
            String nummer = String.valueOf((int) (Math.random() * ((45 - 31) + 1)) + 31);

            while (cardNumbers.contains(nummer)) {
                nummer = String.valueOf((int) (Math.random() * ((45 - 31) + 1)) + 31);
            }
            array[i][2] = nummer;
            cardNumbers.add(nummer);
        }
        for (int i = 0; i < array.length; i++) {
            String nummer = String.valueOf((int) (Math.random() * ((60 - 46) + 1)) + 46);

            while (cardNumbers.contains(nummer)) {
                nummer = String.valueOf((int) (Math.random() * ((60 - 46) + 1)) + 46);
            }
            array[i][3] = nummer;
            cardNumbers.add(nummer);
        }
        for (int i = 0; i < array.length; i++) {
            String nummer = String.valueOf((int) (Math.random() * ((75 - 61) + 1)) + 61);

            while (cardNumbers.contains(nummer)) {
                nummer = String.valueOf((int) (Math.random() * ((75 - 61) + 1)) + 61);
            }
            array[i][4] = nummer;
            cardNumbers.add(nummer);
        }
    }

    public boolean checkPlayersCard(String[] robotCijfers, String[] spelerCijfers) {
        return new HashSet<>(Arrays.asList(robotCijfers)).containsAll(Arrays.asList(spelerCijfers));
    }
}