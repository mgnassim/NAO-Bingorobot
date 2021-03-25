import java.awt.*;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;

/*This class is to create a Pdf file for our Bingo card*/

    public class Bingo {
        public static void main(String[] args) {
            try {
                Document document = new Document(new Rectangle(360, 852)); // Create Document instance
                PdfWriter.getInstance(document, new FileOutputStream("FirstFile.pdf")); // Give the instance a name
                document.open();
                // add content
                document.add(new Paragraph("Pdf Bestand voor een Bingo-kaart"));
                BarcodeQRCode my_code = new BarcodeQRCode("BLAH BLAH", 200, 200, null);
                Image qr_image = my_code.getImage();
                document.add(qr_image);
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Pdf is gemaakt");
        }
    }
