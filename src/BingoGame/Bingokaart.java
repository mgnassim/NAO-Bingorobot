package BingoGame;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Bingokaart {

    static final String pathFontLemon = "BingoFonts\\LemonMilk.otf";
    static final String pathFontHardy = "BingoFonts\\Hardy Mind.ttf";

    public static void main(String[] args) throws DocumentException, IOException {
        String[][] bingoNummers = new String[5][5];
        String[] bingoLetters = {"B", "I", "N", "G", "O"};
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("voorgenoemde cijfers hierin doen", 1000, 1000, null);
        final float[] kolomBreedtes = {2f, 2f, 2f, 2f, 2f};
        BaseColor color = new BaseColor(107, 217, 57);

        BaseFont bf = BaseFont.createFont(pathFontLemon, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font1 = new Font(bf, 25); // voor bingo letters
        Font font2 = new Font(bf, 12); // voor introductie
        Font font3 = new Font(bf, 8); // voor textje daarbeneden

        Document document = new Document(PageSize.A6);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Bingokaart.pdf"));
            document.open();
            document.add(new Paragraph("Welkom bij de naoBingo!", font2));
            document.add(new Paragraph("Wanneer je klaar bent roep heel hard BINGOOO! en scan de QR code bij de robot om te zien of je wint.", font3));

            Image codeQrImage = barcodeQRCode.getImage();
            Image mask = barcodeQRCode.getImage();
            mask.makeMask();
            codeQrImage.setImageMask(mask);
            codeQrImage.scaleToFit(50f, 50f);

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
                letter.setPaddingLeft(15f); letter.setPaddingTop(10f);
                bingotabel.addCell(letter);
            }

            randomNummersOpKaart(bingoNummers);
            for (int i = 0; i < bingoNummers.length; i++) {
                for (int j = 0; j < bingoNummers[i].length; j++) {
                    PdfPCell a = new PdfPCell(new Paragraph(bingoNummers[i][j]));
                    a.setBackgroundColor(color);
                    a.setFixedHeight(50f);
                    a.setPadding(5f);
                    a.setPaddingLeft(18f); a.setPaddingTop(15f);
                    bingotabel.addCell(a);

                    if(i == 2 && j == 1) {
                        PdfPCell b = new PdfPCell(codeQrImage);
                        bingotabel.addCell(b);
                    }

                }
            }

            document.add(bingotabel);

            document.close();
            writer.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void randomNummersOpKaart(String[][] Array) {

        int[][] bingoNummersv2 = new int[5][5];

        for (int i = 0; i < Array.length; i++) {
            for (int j = 0; j < Array[i].length; j++) {
                bingoNummersv2[i][j] = (int) (Math.random() * 50);
                int temp = bingoNummersv2[i][j];
                Array[i][j] = String.valueOf(temp);
            }
        }
    }

}
