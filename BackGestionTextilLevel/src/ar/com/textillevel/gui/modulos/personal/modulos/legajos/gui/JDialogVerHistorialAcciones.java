package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.RemoteFileUtil;
import ar.com.textillevel.gui.util.dialogs.JFileChooserImage;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.CalculatePathDocumentoVisitor;

import com.sun.media.imageioimpl.plugins.bmp.BMPImageReader;

public class JDialogVerHistorialAcciones extends JDialog {

	private static final long serialVersionUID = 1L;

	private Frame padre;
	private JButton btnAceptar;
	private JPanel panelBotones;
	private JPanel panelGeneral;
	private Empleado empleado;

	private PanTablaAccionHistorica panTablaAccionHistorica;
	private final EventListenerList listeners = new EventListenerList();

	public JDialogVerHistorialAcciones(Frame padre, Empleado empleado,List<AccionHistorica> acciones) {
		super(padre);
		this.empleado = empleado;
		setPadre(padre);
		setUpComponentes();
		setUpScreen();
		getPanAccionHistorica().agregarElementos(acciones);
	}

	private void setUpScreen() {
		setSize(new Dimension(580, 330));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Historial de Acciones");
		GuiUtil.centrar(this);
		setResizable(true);
		setModal(true);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		add(getPanelGeneral(), BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	public void addAccionHistoricaEventListener(AccionHistoricaEventListener listener) {
		listeners.add(AccionHistoricaEventListener.class, listener);
	}

	protected void fireAccionHistoricaEvent(AccionHistorica ah) {
		final AccionHistoricaEventListener[] l = listeners.getListeners(AccionHistoricaEventListener.class);
		final AccionHistoricaEvent evt = new AccionHistoricaEvent(ah);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].accionHistoricaPersisted(evt);
				}
			}
		});
	}

	
	private JPanel getPanelBotones(){
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
		}
		return panelBotones;
	}
	
	private JPanel getPanelGeneral(){
		if(panelGeneral == null){
			panelGeneral = new JPanel();
			panelGeneral.setLayout(new GridBagLayout());
			panelGeneral.add(getPanAccionHistorica(),  GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		}
		return panelGeneral;
	}

	private PanTablaAccionHistorica getPanAccionHistorica() {
		if(panTablaAccionHistorica == null) {
			panTablaAccionHistorica = new PanTablaAccionHistorica();
		}
		return panTablaAccionHistorica;
	}

	public Frame getPadre() {
		return padre;
	}

	public void setPadre(Frame padre) {
		this.padre = padre;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private void salir() {
		dispose();
	}

	private class PanTablaAccionHistorica extends PanelTabla<AccionHistorica> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 5;
		private static final int COL_FECHA_HORA = 0;
		private static final int COL_RESUMEN = 1;
		private static final int COL_USUARIO = 2;
		private static final int COL_TIENE_IMAGEN = 3;
		private static final int COL_OBJ = 4;

		private String pathAnterior;
		private JButton btnUpload;

		public PanTablaAccionHistorica() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
			BossEstilos.decorateButton(getBtnUploadFile(), "ar/clarin/fwjava/imagenes/b_flecha_subir.png", "ar/clarin/fwjava/imagenes/b_des_flecha_subir.png");
			agregarBoton(getBtnUploadFile());
			getBtnUploadFile().setEnabled(false);
		}

		private JButton getBtnUploadFile() {
			if(btnUpload == null) {
				btnUpload = new JButton();
				btnUpload.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						String nameFileTransferido = transferFile();
						AccionHistorica elemento = getElemento(getTabla().getSelectedRow());
						elemento.setFullNameDocCargado(nameFileTransferido);
						fireAccionHistoricaEvent(elemento);
						getTabla().setValueAt(true, getTabla().getSelectedRow(), COL_TIENE_IMAGEN);
					}

				});
			}
			return btnUpload;
		}

		private String transferFile() {
			String fileNameToTransfer = null;
			JFileChooserImage jFC = new JFileChooserImage();
			jFC.addFilter("Solo imagenes (gif, jpg, png)", new String[] { "gif", "jpg", "png" });
			try {
				File file = new File(getPathAnterior() == null ? "" : getPathAnterior());
				jFC.setCurrentDirectory(file.getParentFile());
				if (file.exists()) {
					jFC.setSelectedFile(file);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			int returnVal = jFC.showOpenDialog(JDialogVerHistorialAcciones.this);
			if (returnVal == JFileChooserImage.APPROVE_OPTION) {
				File file = jFC.getSelectedFile();
				if (valida(file)) {
					setPathAnterior(file.getAbsolutePath());
					try {
						AccionHistorica elemento = getElemento(getTabla().getSelectedRow());
						CalculatePathDocumentoVisitor visitor = new CalculatePathDocumentoVisitor(empleado, file);
						elemento.accept(visitor);
						fileNameToTransfer = visitor.getPath();
						RemoteFileUtil.getInstance().uploadFileCreatingFolder(fileNameToTransfer, file);
					} catch (Exception ex) {
						fileNameToTransfer = null;
						ex.printStackTrace();
						CLJOptionPane.showErrorMessage(JDialogVerHistorialAcciones.this, StringW.wordWrap(ex.getMessage()), "Error al transferir el archivo");
					}
				} else {
					CLJOptionPane.showErrorMessage(JDialogVerHistorialAcciones.this, "La imagen no es de un formato válido", "Tama\u00F1o inválido");
				}
			}
			return fileNameToTransfer;
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_FECHA_HORA, "Fecha", 100, 100, true);
			tabla.setMultilineColumn(COL_RESUMEN, "Resumen", 180,  true);
			tabla.setStringColumn(COL_USUARIO, "Usuario", 100, 100, true);
			tabla.setCheckColumn(COL_TIENE_IMAGEN, "Con Documento", 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(AccionHistorica elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA_HORA] = DateUtil.dateToString(elemento.getFechaHora(), DateUtil.SHORT_DATE_WITH_HOUR);
			row[COL_RESUMEN] = elemento.getDescrResumen();
			row[COL_USUARIO] = elemento.getUsuario();
			row[COL_TIENE_IMAGEN] = !StringUtil.isNullOrEmpty(elemento.getFullNameDocCargado());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected AccionHistorica getElemento(int fila) {
			return (AccionHistorica)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void filaTablaSeleccionada() {
			getBtnUploadFile().setEnabled(getTabla().getSelectedRow() != -1);
		}

		@Override
		protected void dobleClickTabla(int filaSeleccionada) {
			if(filaSeleccionada != -1) {
				AccionHistorica elemento = getElemento(filaSeleccionada);
				if(StringUtil.isNullOrEmpty(elemento.getFullNameDocCargado())) {
					CLJOptionPane.showInformationMessage(JDialogVerHistorialAcciones.this, "La acción no contiene un documento asociado.", "Información");
					return;
				} else {
					JDialogVisualizarDocumento dialogo = new JDialogVisualizarDocumento(padre, elemento);
					dialogo.setVisible(true);
				}
			}
		}

		private String getPathAnterior() {
			return pathAnterior;
		}

		private void setPathAnterior(String pathAnterior) {
			this.pathAnterior = pathAnterior;
		}

		private boolean valida(File file) {
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(file);
				Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
				if (readers.hasNext()) {
					ImageReader reader = readers.next();
					reader.setInput(iis, true);
					if (reader instanceof BMPImageReader) {
						return false;
					}
					return true;
				}
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

	}

}