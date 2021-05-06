package BingoGame;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class Bingokaart {

    static final String pathFontLemon = "BingoFonts\\LemonMilk.otf";
    //static final String pathFontHardy = "BingoFonts\\Hardy Mind.ttf";
    //static String imageFile = "src\\naoAfbeelding.png";
    private String[] spelerCijfersz;

    public static void main(String[] args) throws DocumentException, IOException {
        final String[] bingoLetters = {"B", "I", "N", "G", "O"};
        String[][] bingoNummers = new String[5][5];
        String spelercijferrz = "";
        final float[] kolomBreedtes = {2f, 2f, 2f, 2f, 2f};
        BaseColor color = new BaseColor(107, 217, 57);
        BingoNAO naoo = new BingoNAO();

        BaseFont bf = BaseFont.createFont(pathFontLemon, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font1 = new Font(bf, 25); // voor bingo letters
        Font font2 = new Font(bf, 12); // voor introductie
        Font font3 = new Font(bf, 8); // voor textje daarbeneden

        Document document = new Document(PageSize.A5);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Bingokaart.pdf"));
            document.open();
            document.add(new Paragraph("Welkom bij de naoBingo!", font2));
            document.add(new Paragraph("Wanneer je klaar bent roep heel hard BINGOOO! en scan de QR code bij de robot om te zien of je wint.", font3));

            PdfPTable bingotabel = new PdfPTable(5);
            bingotabel.setWidthPercentage(105);
            bingotabel.setSpacingBefore(5f);
            bingotabel.setWidths(kolomBreedtes);

            PdfPTable bingoletters = new PdfPTable(5);
            bingoletters.setWidthPercentage(105);
            bingoletters.setSpacingBefore(20f);
            bingoletters.setWidths(kolomBreedtes);

            for (String bingoLetter : bingoLetters) {
                PdfPCell letter = new PdfPCell(new Paragraph(bingoLetter, font1));
                letter.setBackgroundColor(BaseColor.YELLOW);
                letter.setFixedHeight(50f);
                letter.setPaddingLeft(15f);
                letter.setPaddingTop(10f);
                bingotabel.addCell(letter);
            }

            randomNummersOpKaart(bingoNummers);
            for (int i = 0; i < bingoNummers.length; i++) {
                for (int j = 0; j < bingoNummers[i].length; j++) {
                    PdfPCell a = new PdfPCell(new Paragraph(bingoNummers[i][j]));
                    a.setBackgroundColor(color);
                    a.setFixedHeight(50f);
                    a.setPadding(5f);
                    a.setPaddingLeft(18f);
                    a.setPaddingTop(15f);

                    spelercijferrz = spelercijferrz.concat(bingoNummers[i][j] + " ");

                    bingotabel.addCell(a);

                }
            }

            String[] spelerCijfersz = spelercijferrz.split(" ");

            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(spelercijferrz, 1000, 1000, null);
            Image codeQrImage = barcodeQRCode.getImage();
            Image mask = barcodeQRCode.getImage();
            mask.makeMask();
            codeQrImage.setImageMask(mask);
            codeQrImage.scaleToFit(150, 150);

            document.add(bingotabel);
            document.add(codeQrImage);
            document.close();
            writer.close();

            System.out.println(naoo.gezegdeCijfers);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void randomNummersOpKaart(String[][] Array) {

        int[][] bingoNummersv2 = new int[5][5];

        for (int i = 0; i < Array.length; i++) {
            for (int j = 0; j < Array[i].length; j++) {

                switch (j) {
                    case 0:
                        bingoNummersv2[i][j] = (int) (Math.random() * ((15 - 1) + 1)) + 1;
                        Array[i][j] = String.valueOf(bingoNummersv2[i][j]);
                        break;
                    case 1:
                        bingoNummersv2[i][j] = (int) (Math.random() * ((30 - 16) + 1)) + 16;
                        Array[i][j] = String.valueOf(bingoNummersv2[i][j]);
                        break;
                    case 2:
                        bingoNummersv2[i][j] = (int) (Math.random() * ((45 - 31) + 1)) + 31;
                        Array[i][j] = String.valueOf(bingoNummersv2[i][j]);
                        break;
                    case 3:
                        bingoNummersv2[i][j] = (int) (Math.random() * ((60 - 46) + 1)) + 46;
                        Array[i][j] = String.valueOf(bingoNummersv2[i][j]);
                        break;
                    case 4:
                        bingoNummersv2[i][j] = (int) (Math.random() * ((75 - 61) + 1)) + 61;
                        Array[i][j] = String.valueOf(bingoNummersv2[i][j]);
                        break;
                }

            }
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
