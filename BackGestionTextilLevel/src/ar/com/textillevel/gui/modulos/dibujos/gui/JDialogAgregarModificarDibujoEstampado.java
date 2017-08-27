package ar.com.textillevel.gui.modulos.dibujos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogEditarVariante;
import ar.com.textillevel.gui.util.dialogs.JFileChooserImage;
import ar.com.textillevel.util.GTLBeanFactory;

import com.sun.media.imageioimpl.plugins.bmp.BMPImageReader;

public class JDialogAgregarModificarDibujoEstampado extends JDialog {

	private static final long serialVersionUID = 3086797199842716508L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final int COTA_MINIMA_NRO_DIBUJO = 1000;
	private static final int ANCHO_IMAGEN = 120;
	private static final int ALTO_IMAGEN = 120;

	private JPanel panelBotones;
	private JPanel panDetalle;
	private JPanel panelImagen;
	private FWJTextField txtNombre;
	private PanelTablaVariante panelTablaVariante;
	private JLabel lblImagen;
	private JButton btnAgregarImagen;
	private JButton btnQuitarImagen;
	private String pathAnterior;
	private FWJNumericTextField txtNroDibujo;
	private JTextField txtAnchoCilindro;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private boolean acepto = false;
	private boolean consulta;
	private DibujoEstampado dibujoActual;
	private DibujoEstampadoFacadeRemote dibujoEstampadoFacadeRemote;
	private Frame padre;

	public JDialogAgregarModificarDibujoEstampado(Frame padre) {
		this(padre, new DibujoEstampado(), false);
	}

	public JDialogAgregarModificarDibujoEstampado(Frame padre, DibujoEstampado dibujo, boolean consulta) {
		super(padre);
		this.padre = padre;
		this.dibujoActual = dibujo;
		this.consulta = consulta;
		setUpComponentes();
		setUpScreen();
		cargarDatos();
		if(isConsulta()) {
			GuiUtil.setEstadoPanel(getPanDetalle(), false);
			getBtnAceptar().setEnabled(false);
		}
	}

	private void setUpScreen() {
		setSize(new Dimension(640, 460));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Alta/Modificacion de dibujo");
		GuiUtil.centrar(this);
		setResizable(true);
		setModal(true);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		add(getPanDetalle(), BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}
	
	private void cargarDatos() {
		getPanelTablaVariante().setDibujo(getDibujoActual());
		if (getDibujoActual().getId() == null) {
			return;
		}
		dibujoActual = getDibujoEstampadoFacadeRemote().getByIdEager(getDibujoActual().getId());
		if(getDibujoActual() != null) {
			getTxtNombre().setText(getDibujoActual().getNombre());
			getTxtNroDibujo().setValue(getDibujoActual().getNroDibujo().longValue());
			getTxtAnchoCilindro().setText(getDibujoActual().getAnchoCilindro().toString());
			if(getDibujoActual().getImagenEstampado()!=null){
				getLblImagen().setIcon(getDibujoActual().getImagenEstampado());
				getLblImagen().setVerticalTextPosition(JLabel.CENTER);
			}else{
				getLblImagen().setIcon(null);
				getLblImagen().setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
				getLblImagen().setVerticalTextPosition(JLabel.CENTER);
			}
			getPanelTablaVariante().setDibujo(getDibujoActual());
		}
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("NOMBRE:"), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("NÚMERO: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNroDibujo(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("ANCHO DEL CILINDRO: "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtAnchoCilindro(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanelTablaVariante(), createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 2, 1, 1, 0.5));
			panDetalle.add(getPanelImagen(), createGridBagConstraints(0, 4, GridBagConstraints.SOUTH, GridBagConstraints.VERTICAL, new Insets(10, 5, 5, 5), 2, 1, 0, 0.5));
		}
		return panDetalle;
	}

	private JPanel getPanelBotones(){
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnCancelar());
		}
		return panelBotones;
	}
	
	private void salir() {
		if (!isConsulta()) {
			int ret = FWJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Alta de cheque");
			if (ret == FWJOptionPane.YES_OPTION) {
				setAcepto(false);
				dispose();
			}
		} else {
			dispose();
		}
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						capturarSetearDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void capturarSetearDatos() {
		getDibujoActual().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getDibujoActual().setImagenEstampado((ImageIcon)getLblImagen().getIcon());
		getDibujoActual().setNroDibujo(getTxtNroDibujo().getValue());
		getDibujoActual().setAnchoCilindro(new BigDecimal(getTxtAnchoCilindro().getText().replace(',', '.')));
		getPanelTablaVariante().capturarSetearVariantes();
	}
	
	private PanelTablaVariante getPanelTablaVariante() {
		if (panelTablaVariante == null) {
			panelTablaVariante = new PanelTablaVariante();
			panelTablaVariante.setBorder(BorderFactory.createTitledBorder("VARIANTES"));
		}
		return panelTablaVariante;
	}

	private JPanel getPanelImagen() {
		if (panelImagen == null) {
			panelImagen = new JPanel();
			panelImagen.setLayout(new GridBagLayout());
			panelImagen.setBorder(BorderFactory.createTitledBorder("IMAGEN"));
			panelImagen.add(getLblImagen(), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
			panelImagen.add(getBtnAgregarImagen(), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelImagen.add(getBtnQuitarImagen(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelImagen;
	}

	private JLabel getLblImagen() {
		if (lblImagen == null) {
			lblImagen = new JLabel();
			lblImagen.setBorder(BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
			lblImagen.setVerticalTextPosition(JLabel.CENTER);
			lblImagen.setHorizontalAlignment(JLabel.CENTER);
		}
		return lblImagen;
	}

	private JButton getBtnAgregarImagen() {
		if (btnAgregarImagen == null) {
			btnAgregarImagen = new JButton("Agregar imagen");
			btnAgregarImagen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooserImage jFC = new JFileChooserImage();
					jFC.addFilter("Solo imagenes", new String[] { "gif", "jpg", "png" });
					try {
						File file = new File(getPathAnterior());
						jFC.setCurrentDirectory(file.getParentFile());
						if (file.exists()) {
							jFC.setSelectedFile(file);
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					int returnVal = jFC.showOpenDialog(JDialogAgregarModificarDibujoEstampado.this);
					if (returnVal == JFileChooserImage.APPROVE_OPTION) {
						File file = jFC.getSelectedFile();
						if (valida(file)) {
							if (isValidImageDimension(file.getAbsolutePath())) {
								setPathAnterior(file.getAbsolutePath());
								loadImageFile(file.getAbsolutePath());
								habilitarBotonesImagen(true, true);
							} else {
								FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "La imagen seleccionada supera las dimensiones esperadas.\n" + "El ancho máximo esperado debe ser menor o igual a: " + ANCHO_IMAGEN + " pixeles.\n" + "El alto máximo esperado debe ser menor o igual a: " + ALTO_IMAGEN + " pixeles.\n", "Dimensiones inválidas");
							}
						} else {
							FWJOptionPane.showErrorMessage(null, "La imagen no es de un formato válido", "Tama\u00F1o inválido");
						}
					}
				}
			});
		}
		return btnAgregarImagen;
	}

	private String getPathAnterior() {
		if (pathAnterior == null) {
			pathAnterior = "";
		}
		return pathAnterior;
	}

	private void setPathAnterior(String pathAnterior) {
		this.pathAnterior = pathAnterior;
	}

	private JButton getBtnQuitarImagen() {
		if (btnQuitarImagen == null) {
			btnQuitarImagen = new JButton("Quitar imagen");
			btnQuitarImagen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelarImagenSeleccionada();
					habilitarBotonesImagen(true, false);
				}
			});
		}
		return btnQuitarImagen;
	}

	private void cancelarImagenSeleccionada() {
		if (getDibujoActual() != null) {
			getDibujoActual().setImagenEstampado(null);
		}
		lblImagen.setIcon(null);
		setPathAnterior("");
		lblImagen.setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
		lblImagen.setSize(lblImagen.getPreferredSize());
	}

	private void habilitarBotonesImagen(boolean modoEdicion, boolean tieneImagen) {
		if (modoEdicion) {
			getBtnAgregarImagen().setEnabled(true);
			getBtnQuitarImagen().setEnabled(tieneImagen);
		}
	}

	private void loadImageFile(String filename) {
		lblImagen.setText("");
		ImageIcon icon = new ImageIcon(filename);
		Image scaleImage = ImageUtil.scale(icon.getImage(), ANCHO_IMAGEN, ALTO_IMAGEN, true);
		lblImagen.setIcon(new ImageIcon(scaleImage));
		lblImagen.setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
		lblImagen.setSize(lblImagen.getPreferredSize());
		lblImagen.setVerticalTextPosition(JLabel.CENTER);
		lblImagen.setBorder(BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
		lblImagen.repaint();
	}

	private boolean valida(File file) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				reader.setInput(iis, true);
				// com.sun.media.imageioimpl.plugins.bmp.
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

	private boolean isValidImageDimension(String filename) {
		return true;
		// ImageIcon imageIcon = new ImageIcon(filename);
		// int iconHeight = imageIcon.getIconHeight();
		// int iconWidth = imageIcon.getIconWidth();
		// if (iconHeight > ALTO_IMAGEN || iconWidth > ANCHO_IMAGEN)
		// return false;
		// return true;
	}

	private FWJNumericTextField getTxtNroDibujo() {
		if (txtNroDibujo == null) {
			txtNroDibujo = new FWJNumericTextField();
			txtNroDibujo.setMaxLength(4);
		}
		return txtNroDibujo;
	}

	private DibujoEstampadoFacadeRemote getDibujoEstampadoFacadeRemote() {
		if (dibujoEstampadoFacadeRemote == null) {
			dibujoEstampadoFacadeRemote = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		}
		return dibujoEstampadoFacadeRemote;
	}

	private boolean validar() {
		if (StringUtil.isNullOrEmpty(getTxtNombre().getText())) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "Falta Completar el campo 'Nombre'", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		Integer nroDibujo = getTxtNroDibujo().getValue();
		if (nroDibujo == null) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "Falta Completar el campo 'Número'", "Advertencia");
			getTxtNroDibujo().requestFocus();
			return false;
		}
		if (nroDibujo < COTA_MINIMA_NRO_DIBUJO) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "El Número de Dibujo no puede ser menor a " + COTA_MINIMA_NRO_DIBUJO, "Advertencia");
			getTxtNroDibujo().requestFocus();
			return false;
		}
		if (getDibujoEstampadoFacadeRemote().existsNroDibujo(dibujoActual.getId(), nroDibujo)) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "El Número " + nroDibujo + " ya está siendo usado para otro dibujo.", "Advertencia");
			getTxtNroDibujo().requestFocus();
			return false;
		}
		String text = getTxtAnchoCilindro().getText();
		if (!GenericUtils.esNumerico(text)) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "Debe ingresar un ancho de cilindro numérico.", "Advertencia");
			getTxtAnchoCilindro().requestFocus();
			return false;
		}
		if (getPanelTablaVariante().getTabla().getRowCount() == 0) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, "Debe definir al menos una variante.", "Advertencia");
			return false;
		}
		String mensaje = getPanelTablaVariante().validar();
		if (mensaje != null) {
			FWJOptionPane.showErrorMessage(JDialogAgregarModificarDibujoEstampado.this, StringW.wordWrap(mensaje), "Advertencia");
			return false;
		}
		return true;
	}

	private FWJTextField getTxtNombre() {
		if (txtNombre == null) {
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	private JTextField getTxtAnchoCilindro() {
		if (txtAnchoCilindro == null) {
			txtAnchoCilindro = new JTextField();
		}
		return txtAnchoCilindro;
	}

	private class PanelTablaVariante extends PanelTabla<VarianteEstampado> {

		private static final long serialVersionUID = -5350962120083656924L;

		private static final int CANT_COLS = 3;
		private static final int COL_VARIANTE = 0;
		private static final int COL_COLOR = 1;
		private static final int COL_OBJ = 2;

		private DibujoEstampado dibujo;

		public PanelTablaVariante() {
			agregarBotonModificar();
		}

		@Override
		protected void agregarElemento(VarianteEstampado elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_VARIANTE] = elemento.getNombre();
			row[COL_COLOR] = StringUtil.getCadena(elemento.getColores(), "\n");
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if (filaSeleccionada != -1) {
				if (getTxtNroDibujo().getValue() == null) {
					FWJOptionPane.showInformationMessage(JDialogAgregarModificarDibujoEstampado.this, "Por favor, ingrese el número de dibujo", "Error");
					return;
				}

				VarianteEstampado elemento = getElemento(filaSeleccionada);
				Integer cantCilindros = Integer.valueOf(String.valueOf(getTxtNroDibujo().getValue().toString().charAt(0)));
				JDialogEditarVariante dialogSeleccionarColores = new JDialogEditarVariante(padre, elemento, cantCilindros);
				GuiUtil.centrarEnPadre(dialogSeleccionarColores);
				dialogSeleccionarColores.setVisible(true);
				getTabla().setNumRows(0);
				agregarElementos(dibujo.getVariantes());
			}
		}

		public String validar() {
			if (getTabla().getFirstEmptyRow(COL_VARIANTE) != -1) {
				return "Falta completar el nombre de la variante en al menos una fila.";
			}
			if (getTabla().getFirstEmptyRow(COL_COLOR) != -1) {
				return "Falta seleccionar los colores de la variante en al menos una fila.";
			}
			if (existsVariantesRepetidas()) {
				return "Hay nombres de variantes repetidas.";
			}
			return null;
		}

		private boolean existsVariantesRepetidas() {
			List<String> variantes = new ArrayList<String>();
			for (int i = 0; i < getTabla().getRowCount(); i++) {
				variantes.add(((String) getTabla().getValueAt(i, COL_VARIANTE)).trim().toUpperCase());
			}
			return new HashSet<String>(variantes).size() != getTabla().getRowCount();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaVariante = new FWJTable(0, CANT_COLS);
			tablaVariante.setStringColumn(COL_VARIANTE, "Nombre", 200, 200, true);
			tablaVariante.setMultilineColumn(COL_COLOR, "Cilindro/Colores", 350, true);
			tablaVariante.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaVariante.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						botonModificarPresionado(getTabla().getSelectedRow());
					}
				}

			});
			return tablaVariante;
		}

		@Override
		protected VarianteEstampado getElemento(int fila) {
			return (VarianteEstampado) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public void setDibujo(DibujoEstampado dibujoEstampado) {
			this.dibujo = dibujoEstampado;
			limpiar();
			agregarElementos(dibujo.getVariantes());
		}

		@Override
		public boolean validarAgregar() {
			if (getTxtNroDibujo().getValue() == null) {
				FWJOptionPane.showInformationMessage(JDialogAgregarModificarDibujoEstampado.this, "Por favor, ingrese el número de dibujo", "Error");
				return false;
			}

			VarianteEstampado variante = new VarianteEstampado();
			JDialogEditarVariante dialogSeleccionarColores = new JDialogEditarVariante(padre, variante, Integer.valueOf(getTxtNroDibujo().getValue().toString().toCharArray()[0]));
			GuiUtil.centrarEnPadre(dialogSeleccionarColores);
			dialogSeleccionarColores.setVisible(true);
			if (dialogSeleccionarColores.isAcepto()) {
				dibujo.getVariantes().add(variante);
				getTabla().setNumRows(0);
				agregarElementos(dibujo.getVariantes());
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			List<VarianteEstampado> varianteRemovedList = new ArrayList<VarianteEstampado>();
			int[] selectedRows = getTabla().getSelectedRows();
			for (int sr : selectedRows) {
				varianteRemovedList.add(getElemento(sr));
			}
			dibujo.getVariantes().removeAll(varianteRemovedList);
			return true;
		}

		public void capturarSetearVariantes() {
			// Saco los que estaban antes y ahora no
			List<VarianteEstampado> varianteBorrarList = new ArrayList<VarianteEstampado>();
			for (VarianteEstampado variante : dibujo.getVariantes()) {
				// Si el elemento no existe mas en la tabla => lo saco de la
				// lista de objetos
				if (getTabla().getFirstRowWithValue(COL_OBJ, variante) == -1) {
					varianteBorrarList.add(variante);
				}
			}
			dibujo.getVariantes().removeAll(varianteBorrarList);
		}
	}

	public DibujoEstampado getDibujoActual() {
		return dibujoActual;
	}

	public void setDibujoActual(DibujoEstampado dibujoActual) {
		this.dibujoActual = dibujoActual;
	}

	private GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
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

	public boolean isAcepto() {
		return acepto;
	}

	public boolean isConsulta() {
		return consulta;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}
