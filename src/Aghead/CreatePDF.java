package Aghead;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/*This class is to create a Pdf file for our Bingo card*/

public class CreatePDF {
    public static void main(String[] args){
        try {
            Document document = new Document(); // Create Document instance
            PdfWriter.getInstance(document, new FileOutputStream("FirstFile.pdf")); // Give the instance a name
            document.open();
            // add content
            document.add(new Paragraph("Pdf Bestand voor een Bingo-kaart"));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Pdf is gemaakt");
    }
}