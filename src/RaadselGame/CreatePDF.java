package Aghead;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;

/*This class is to create a Pdf file for our Bingo card*/

public class CreatePDF {
    public static void main(String[] args){
        try {
            Document document = new Document(new Rectangle(360, 852)); // Create Document instance
            PdfWriter.getInstance(document, new FileOutputStream("FirstFile.pdf")); // Give the instance a name
            document.open();
            // add content
            document.add(new Paragraph("Pdf Bestand voor een Bingo-kaart"));
            BarcodeQRCode my_code = new BarcodeQRCode("Example QR Code Creation in Itext", 1, 1, null);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Pdf is gemaakt");
    }
}