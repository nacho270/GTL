package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import ar.com.fwcommon.util.GuiUtil;

public class FotoVarianteTest {

	private JLabel lblImagen = new JLabel();
	private JDialog d = new JDialog();
	private static final int ANCHO = 700;
	private static final int ALTO = 500;
	
	public FotoVarianteTest() {
		doAll();
	}
	
	private void doAll() {
		
		
		lblImagen.setIcon(new ImageIcon(readInGrayScale()));
		lblImagen.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 1) {
					System.out.println("Click en <" + e.getX() + ", " + e.getY() + ">");

					BufferedImage img = toBufferedImageHeadless(((ImageIcon)lblImagen.getIcon()).getImage());

					Graphics2D g2 = img.createGraphics();
					g2.setColor(Color.RED);
					String fontFamily = getFont("Times New Roman","FreeSerif");
					g2.setFont(new Font(fontFamily, Font.PLAIN, 40));
					g2.drawString("1", e.getX() - lblImagen.getX(), e.getY()+lblImagen.getY()+d.getY());

					lblImagen.setIcon(new ImageIcon(img));
				}
			}

		});

		d.setSize(new Dimension(ANCHO, ALTO));
		d.setLayout(new GridBagLayout());
		d.add(lblImagen, createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
		d.setVisible(true);
		GuiUtil.centrar(d);
	}

	public static void main(String[] args) {
		new FotoVarianteTest();
	}
	
	public static GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}

    public static BufferedImage toBufferedImageHeadless(Image image) {
        if (image instanceof BufferedImage)
        return (BufferedImage)image;

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels
        boolean hasAlpha = hasAlpha2(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;

            if (hasAlpha == true)
                transparency = Transparency.BITMASK;

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();

            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) { } //No screen

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;

            if (hasAlpha == true) {type = BufferedImage.TYPE_INT_ARGB;}
                bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }	

	public static boolean hasAlpha2(Image img) {
		if(img instanceof BufferedImage) {
			BufferedImage bimg = (BufferedImage)img;
			return bimg.getColorModel().hasAlpha();
		} else {
			PixelGrabber pg = new PixelGrabber(img, 0, 0, 1, 1, false);
			try {
				pg.grabPixels();
			} catch(InterruptedException e) {}
			ColorModel cm = pg.getColorModel();
			return cm.hasAlpha();
		}
	}

    
	private static Set<String> fontsDisponibles = Collections.synchronizedSet(new HashSet<String>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())));
	/**
	 * Es el tipico algoritmo que usa CSS
	 * Para elegir tipografias me baso en:
	 * http://mondaybynoon.com/20070402/linux-font-equivalents-to-popular-web-typefaces/ 
	 */
	private static String getFont(String... families){
		for (int i=0;i<families.length;i++){
			if (fontsDisponibles.contains(families[i])){
				return families[i];
			}
		}
		return null;
	}

	private BufferedImage readInGrayScale() {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File("C:/Users/Dromero/Desktop/desert.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int w = 0; w < bufferedImage.getWidth(); w++) {
			for (int h = 0; h < bufferedImage.getHeight(); h++) {
				Color color = new Color(bufferedImage.getRGB(w, h));
				int averageColor = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				Color avg = new Color(averageColor, averageColor, averageColor);
				bufferedImage.setRGB(w, h, avg.getRGB());
			}
		}
		return bufferedImage;
	}

}