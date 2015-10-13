package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDialog;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.visores.VisorJAI;
import ar.com.textillevel.gui.util.RemoteFileUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolox.swing.PScrollPane;

public class JDialogVisualizarDocumento extends JDialog {

	private static final long serialVersionUID = 1L;

	private AccionHistorica accionHistorica;
	private File documento;

	public JDialogVisualizarDocumento(Frame padre, AccionHistorica accionHistorica) {
		super(padre);
		this.accionHistorica = accionHistorica;
		setModal(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				    ((JDialog)e.getSource()).dispose();
				    documento.delete();
				}
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				documento.delete();
			}

		});

		downloadDocAndShowIt();
		pack();
	}

	private void downloadDocAndShowIt() {
		try {
			String[] paths = accionHistorica.getFullNameDocCargado().split("/");
			String nombreArchivo = paths[paths.length - 1];
			setDocumento(new File(nombreArchivo));
			RemoteFileUtil.getInstance().downloadFile(accionHistorica.getFullNameDocCargado(), documento);
			VisorJAI visor = new VisorJAI(nombreArchivo);
			edu.umd.cs.piccolo.PCanvas canvas = new edu.umd.cs.piccolo.PCanvas();
			canvas.setOpaque(true);
			canvas.setPreferredSize(new Dimension(visor.getBufferedImage().getWidth(), visor.getBufferedImage().getHeight()));
			PScrollPane scrollPane = new PScrollPane(canvas);
			add(scrollPane);
			PImage image = new PImage(visor.getBufferedImage());
			canvas.getLayer().addChild(image);
		} catch(Exception e) {
			e.printStackTrace();
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMessage()), "Error en la transferencia");
		}
	}

	private void setDocumento(File documento) {
		this.documento = documento;
	}
	
}
