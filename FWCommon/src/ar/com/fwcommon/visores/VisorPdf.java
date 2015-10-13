package ar.com.fwcommon.visores;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import ar.com.fwcommon.componentes.error.FWException;

public class VisorPdf extends Visor {

	private PdfDecoder pdf;
	private float factorOptimo = 1.0f;
	private float factorAnchoMaximo = 1.0f;
	private float anchoOriginal = 0f;
	private float altoOriginal = 0f;

	public VisorPdf(String nombreArchivo) {
		super(nombreArchivo);
		File file = new File(nombreArchivo);
		if(file.exists()) {
			try {
				getPdf().openPdfFile(nombreArchivo);
				setScale(1f);
				getPdf().setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
//				getPdf().decodePage(1);
//				getImagen().setName(nombreArchivo);
				anchoOriginal = getPdf().getPDFWidth();
				altoOriginal = getPdf().getPDFHeight();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return Returns the imagen.
	 */
//	public JPanel getImagen() {
//		try {
//			getPdf().decodePage(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return getPdf();
//	}

	private PdfDecoder getPdf() {
		if(pdf == null) {
			pdf = new PdfDecoder();
			pdf.setOpaque(true);
		}
		return pdf;
	}

	/**
	 * @return Returns the factorOptimo.
	 */
	public float getFactorOptimo() {
		return factorOptimo;
	}

	/**
	 * @param factorOptimo The factorOptimo to set.
	 */
	public void setFactorOptimo(int ancho, int alto) {
		float factorAncho = 0;
		float factorAlto = 0;
		factorAncho = (float)ancho / anchoOriginal;
		factorAlto = (float)alto / altoOriginal;
		factorOptimo = (factorAncho < factorAlto) ? factorAncho : factorAlto;
	}

	/**
	 * @return Returns the factorAnchoMaximo.
	 */
	public float getFactorAnchoMaximo() {
		return factorAnchoMaximo;
	}

	/**
	 * @param factorOptimo The factorAnchoMaximo to set.
	 */
	public void setFactorAnchoMaximo(int ancho) {
		factorAnchoMaximo = (float)ancho / anchoOriginal;
	}

	/**
	 * @return Returns the altoOriginal.
	 */
	public float getAltoOriginal() {
		return altoOriginal;
	}

	/**
	 * @return Returns the anchoOriginal.
	 */
	public float getAnchoOriginal() {
		return anchoOriginal;
	}

	public int getWidth() {
		return getPdf().getPDFWidth();
	}

	public int getHeight() {
		return getPdf().getPDFHeight();
	}

	public void setScale(float factor) {
		getPdf().setPageParameters(factor, 1);
	}

	public void setInset(int h, int v) {
		getPdf().setInset(h, v);
	}

	public void close() {
		getPdf().closePdfFile();
	}

	public BufferedImage getBufferedImage() throws FWException {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = getPdf().getPageAsImage(1);
			if(bufferedImage == null) {
				throw new FWException("No se pudo generar la vista previa del archivo " + getNombreArchivo());
			}
		} catch(PdfException e) {
			throw new FWException("No se pudo generar la vista previa del archivo " + getNombreArchivo(), e);
		}
		return bufferedImage;
	}

	public static void main(String[] args) {
		System.out.println("Iniciando ...");
		try {
			javax.swing.JDialog dialog = new javax.swing.JDialog();
			VisorPdf visorPdf = new VisorPdf("C:/Documents and Settings/nacho/Escritorio/JasperReports - cotizacion.pdf");
			edu.umd.cs.piccolo.PCanvas canvas = new edu.umd.cs.piccolo.PCanvas();
			canvas.setOpaque(true);
			canvas.setPreferredSize(new Dimension(640, 480));
			edu.umd.cs.piccolox.swing.PScrollPane scrollPane = new edu.umd.cs.piccolox.swing.PScrollPane(canvas);
			dialog.add(scrollPane);
			edu.umd.cs.piccolo.nodes.PImage image = new edu.umd.cs.piccolo.nodes.PImage(visorPdf.getBufferedImage());
			canvas.getLayer().addChild(image);
			dialog.pack();
			dialog.setVisible(true);
		} catch(FWException e) {
			e.printStackTrace();
			System.out.println("Excepcion ...");
		}
		System.out.println("Finalizando ...");
	}

}