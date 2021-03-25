package QRCode;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;

public class QR {
    Rectangle rectangle = new Rectangle(360, 852);
    BarcodeQRCode my_code = new BarcodeQRCode("Example QR Code Creation in Itext", 1, 1, null);
    Image qr_image = my_code.getImage();


    QR() throws BadElementException {
    }
}
