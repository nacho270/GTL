package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class BarcodeTester {
	public static void main(String[] args) throws IOException{
		Interleaved2Of5Bean bean = new Interleaved2Of5Bean();

	    bean.setHeight(10d);

	    bean.doQuietZone(false);

	    OutputStream out =
	        new java.io.FileOutputStream(new File("output.png"));

	    BitmapCanvasProvider provider =
	        new BitmapCanvasProvider(out, "image/x-png", 110,
	                                 BufferedImage.TYPE_BYTE_GRAY, false,
	                                 0);
	    bean.generateBarcode(provider, "1234567890123456789012345678901234567890");

	    provider.finish();
	    FileOutputStream fout = new FileOutputStream(new File("c:\\ooo.png"));
	    BufferedImage barcodeImage = provider.getBufferedImage();
	    ImageIO.write(barcodeImage, "png", fout);
	    fout.close();
	}
}
