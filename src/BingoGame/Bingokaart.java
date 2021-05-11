package BingoGame;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Bingokaart {

    static final String pathFontLemon = "BingoFonts\\LemonMilk.otf";
    static final String pathFontHardy = "BingoFonts\\Hardy Mind.ttf";
    static final String pathFontItim = "BingoFonts\\Itim-Regular.ttf";
    private String[] spelerCijfersz;

    public static void main(String[] args) throws DocumentException, IOException {
        final String[] bingoLetters = {"B", "I", "N", "G", "O"};
        String[][] bingoNummers = new String[5][5];
        String spelercijferrz = "";
        final float[] kolomBreedtes = {2f, 2f, 2f, 2f, 2f};
        BaseColor color = new BaseColor(107, 217, 57);
        BingoNAO naoo = new BingoNAO();

        BaseFont bf = BaseFont.createFont(pathFontLemon, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bf2 = BaseFont.createFont(pathFontItim, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font1 = new Font(bf, 25); // voor bingo letters
        Font font2 = new Font(bf, 12); // voor introductie
        Font font3 = new Font(bf2, 10); // voor textje daarbeneden
        Font font4 = new Font(bf, 15);

        Document document = new Document(PageSize.A5);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Bingokaart.pdf"));
            document.open();
            font2.setColor(BaseColor.BLUE);
            document.add(new Paragraph("Welkom bij de naoBingo!", font2));
            document.add(new Paragraph("Wanneer je klaar bent roep heel hard BINGOOO! en scan de QR code bij de robot om te zien of je wint!", font3));

            PdfPTable bingotabel = new PdfPTable(5);
            bingotabel.setWidthPercentage(105);
            bingotabel.setSpacingBefore(5f);
            bingotabel.setWidths(kolomBreedtes);

            PdfPTable bingoletters = new PdfPTable(5);
            bingoletters.setWidthPercentage(105);
            bingoletters.setSpacingBefore(20f);
            bingoletters.setWidths(kolomBreedtes);

            for (int i = 0; i < bingoLetters.length; i ++) {
                PdfPCell letter = new PdfPCell(new Paragraph(bingoLetters[i], font1));
                letter.setPadding(10f);
                letter.setBackgroundColor(BaseColor.YELLOW);
                letter.setBorderWidth(1f);
                letter.setFixedHeight(50f);
                letter.setPaddingLeft(25f);
                letter.setPaddingTop(10f);
                if(i == 1)
                    letter.setPaddingLeft(33f);
                bingotabel.addCell(letter);
            }

            randomNummersOpKaart(bingoNummers);
            for (int i = 0; i < bingoNummers.length; i++) {
                for (int j = 0; j < bingoNummers[i].length; j++) {

                    font4.setColor(BaseColor.WHITE.brighter());
                    PdfPCell a = new PdfPCell(new Paragraph(bingoNummers[i][j], font4));

                    a.setBackgroundColor(BaseColor.BLUE);
                    a.setFixedHeight(50f);
                    a.setPaddingLeft(30f);
                    a.setPaddingTop(20f);

                    spelercijferrz = spelercijferrz.concat(bingoNummers[i][j] + " ");

                    bingotabel.addCell(a);

                }
            }

            String[] spelerCijfersz = spelercijferrz.split(" ");

            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(spelercijferrz, 1000, 1000, null);
            Image codeQrImage = barcodeQRCode.getImage();
            codeQrImage.scaleToFit(170, 170);

            Image img = Image.getInstance("naoQI.jpg");
            img.scaleToFit(150, 150);
            img.setAbsolutePosition(230f, 40f);

            document.add(bingotabel);
            document.add(codeQrImage);
            document.add(img);
            document.close();
            writer.close();

            System.out.println(naoo.gezegdeCijfers);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void randomNummersOpKaart(String[][] array) {

        int[][] bingoNummersv2 = new int[5][5];
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
        return new HashSet<String>(Arrays.asList(robotCijfers)).containsAll(Arrays.asList(spelerCijfers));
    }

    public String[] getSpelerCijfersz() {
        return getSpelerCijfersz().clone();
    }

    public String toJSON(String[][] spelerCombis) {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String[][] bingoNummers = new String[5][5];

        try (FileWriter file = new FileWriter("bingoKaartNummersVanSpeler.json")) {

            for (int i = 0; i < bingoNummers.length; i++) {
                for (int j = 0; j < bingoNummers[i].length; j++) {
                    jsonArray.add(bingoNummers[i][j]);
                }
                jsonObject.put("speler nummers", jsonArray);
            }
            file.write(jsonObject.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(jsonArray);

    }

}


