package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogEditarVariante;
import ar.com.textillevel.gui.util.dialogs.JFileChooserImage;
import ar.com.textillevel.util.GTLBeanFactory;

import com.sun.media.imageioimpl.plugins.bmp.BMPImageReader;

public class GuiABMDibujoEstampado extends GuiABMListaTemplate {

	private static final long serialVersionUID = 5817318509804786243L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final int COTA_MINIMA_NRO_DIBUJO = 1000;
	private static final int ANCHO_IMAGEN = 120;
	private static final int ALTO_IMAGEN = 120;

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private JPanel panelImagen;
	private CLJTextField txtNombre;
	private PanelTablaVariante panelTablaVariante;
	private JLabel lblImagen;
	private JButton btnAgregarImagen;
	private JButton btnQuitarImagen;
	private String pathAnterior;
	private CLJNumericTextField txtNroDibujo;
	private JTextField txtAnchoCilindro;
	
	private DibujoEstampado dibujo;
	private DibujoEstampadoFacadeRemote dibujoEstampadoFacadeRemote;


	public GuiABMDibujoEstampado(Integer idModulo) {
		setHijoCreado(true);
		setTitle("Administrar Dibujos de Estampado");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos del Dibujo", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.CENTER);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("NOMBRE:"), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("N�MERO: "), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNroDibujo(), createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("ANCHO DEL CILINDRO: "), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtAnchoCilindro(), createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanelTablaVariante(), createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 2, 1, 1, 0.5));
			panDetalle.add(getPanelImagen(), createGridBagConstraints(0, 4,GridBagConstraints.SOUTH, GridBagConstraints.VERTICAL, new Insets(10, 5, 5, 5), 2, 1, 0, 0.5));
		}
		return panDetalle;
	}

	private JTextField getTxtAnchoCilindro() {
		if(txtAnchoCilindro == null) {
			txtAnchoCilindro = new JTextField();
		}
		return txtAnchoCilindro;
	}

	private JPanel getPanelImagen() {
		if(panelImagen == null){
			panelImagen = new JPanel();
			panelImagen.setLayout(new GridBagLayout());
			panelImagen.setBorder(BorderFactory.createTitledBorder("IMAGEN"));
			panelImagen.add(getLblImagen(), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
			panelImagen.add(getBtnAgregarImagen(), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelImagen.add(getBtnQuitarImagen(), createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelImagen;
	}

	private CLJTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	@Override
	public void cargarLista() {
		List<DibujoEstampado> dibujoEstampadoList = getDibujoEstampadoFacadeRemote().getAllOrderByNombre();
		lista.clear();
		for(DibujoEstampado mp : dibujoEstampadoList) {
			lista.addItem(mp);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setDibujoActual(new DibujoEstampado());
		getPanelTablaVariante().setDibujo(getDibujoActual());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMDibujoEstampado.this, "�Est� seguro que desea eliminar el dibujo seleccionado?", "Confirmaci�n") == CLJOptionPane.YES_OPTION) {
				getDibujoEstampadoFacadeRemote().remove(getDibujoActual());
				lista.setSelectedIndex(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			DibujoEstampado dibujoRefresh = getDibujoEstampadoFacadeRemote().save(getDibujoActual());
			lista.setSelectedValue(dibujoRefresh, true);
			return true;
		}
		return false;
		
	}

	private void capturarSetearDatos() {
		getDibujoActual().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getDibujoActual().setImagenEstampado((ImageIcon)getLblImagen().getIcon());
		getDibujoActual().setNroDibujo(getTxtNroDibujo().getValue());
		getDibujoActual().setAnchoCilindro(new BigDecimal(getTxtAnchoCilindro().getText().replace(',', '.')));
		getPanelTablaVariante().capturarSetearVariantes();
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombre().getText())) {
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "Falta Completar el campo 'Nombre'", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		Integer nroDibujo = getTxtNroDibujo().getValue();
		if(nroDibujo==null){
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "Falta Completar el campo 'N�mero'", "Advertencia");
			getTxtNroDibujo().requestFocus();
			return false;
		}
		if(nroDibujo < COTA_MINIMA_NRO_DIBUJO) {
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "El N�mero de Dibujo no puede ser menor a " + COTA_MINIMA_NRO_DIBUJO, "Advertencia");
			getTxtNroDibujo().requestFocus();
			return false;
		}
		if(getDibujoEstampadoFacadeRemote().existsNroDibujo(dibujo.getId(), nroDibujo)) {
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "El N�mero " + nroDibujo + " ya est� siendo usado para otro dibujo.", "Advertencia");
			getTxtNroDibujo().requestFocus();
			return false;
		}
		String text = getTxtAnchoCilindro().getText();
		if(!GenericUtils.esNumerico(text)) {
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "Debe ingresar un ancho de cilindro num�rico.", "Advertencia");
			getTxtAnchoCilindro().requestFocus();
			return false;
		}
		if(getPanelTablaVariante().getTabla().getRowCount() == 0) {
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "Debe definir al menos una variante.", "Advertencia");
			return false;
		}
		String mensaje = getPanelTablaVariante().validar();
		if(mensaje != null) {
			CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, StringW.wordWrap(mensaje), "Advertencia");
			return false;
		}
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un dibujo", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		DibujoEstampado selectedValue = (DibujoEstampado)lista.getSelectedValue();
		if(selectedValue == null) {
			limpiarDatos();
			return;
		}
		selectedValue = getDibujoEstampadoFacadeRemote().getByIdEager(selectedValue.getId());
		if(selectedValue != null) {
			setDibujoActual(selectedValue);
			getPanelTablaVariante().setDibujo(selectedValue);
		}
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
		} else {
			limpiarDatos();
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText(null);
		getLblImagen().setIcon(null);
		getTxtNroDibujo().setText("");
		getLblImagen().setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
		getPanelTablaVariante().limpiar();
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		getPanelTablaVariante().setModoConsulta(!estado);
		habilitarBotonesImagen(estado, getDibujoActual()!= null && getDibujoActual().getImagenEstampado() != null);
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

	private DibujoEstampado getDibujoActual() {
		return dibujo;
	}

	private void setDibujoActual(DibujoEstampado rubro) {
		this.dibujo = rubro;
	}

	private DibujoEstampadoFacadeRemote getDibujoEstampadoFacadeRemote() {
		if(dibujoEstampadoFacadeRemote == null) {
			dibujoEstampadoFacadeRemote = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		}
		return dibujoEstampadoFacadeRemote;
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
			if(filaSeleccionada != -1) {
				if(getTxtNroDibujo().getValue() == null) {
					CLJOptionPane.showInformationMessage(GuiABMDibujoEstampado.this, "Por favor, ingrese el n�mero de dibujo", "Error");
					return;
				}

				VarianteEstampado elemento = getElemento(filaSeleccionada);
				Integer cantCilindros = Integer.valueOf(String.valueOf(getTxtNroDibujo().getValue().toString().charAt(0)));
				JDialogEditarVariante dialogSeleccionarColores = new JDialogEditarVariante(GuiABMDibujoEstampado.this.getFrame(), elemento, cantCilindros);
				GuiUtil.centrarEnPadre(dialogSeleccionarColores);
				dialogSeleccionarColores.setVisible(true);
				getTabla().setNumRows(0);
				agregarElementos(dibujo.getVariantes());
			}
		}

		public String validar() {
			if(getTabla().getFirstEmptyRow(COL_VARIANTE) != -1) {
				return "Falta completar el nombre de la variante en al menos una fila.";
			}
			if(getTabla().getFirstEmptyRow(COL_COLOR) != -1) {
				return "Falta seleccionar los colores de la variante en al menos una fila.";
			}
			if(existsVariantesRepetidas()) {
				return "Hay nombres de variantes repetidas.";
			}
			return null;
		}

		private boolean existsVariantesRepetidas() {
			List<String> variantes = new ArrayList<String>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				variantes.add(((String)getTabla().getValueAt(i, COL_VARIANTE)).trim().toUpperCase());
			}
			return new HashSet<String>(variantes).size() != getTabla().getRowCount(); 
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tablaVariante = new CLJTable(0, CANT_COLS);
			tablaVariante.setStringColumn(COL_VARIANTE, "Nombre", 200, 200, true);
			tablaVariante.setMultilineColumn(COL_COLOR, "Cilindro/Colores", 350, true);
			tablaVariante.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaVariante.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						botonModificarPresionado(getTabla().getSelectedRow());
					}
				}

			});
			return tablaVariante;
		}

		@Override
		protected VarianteEstampado getElemento(int fila) {
			return (VarianteEstampado)getTabla().getValueAt(fila, COL_OBJ);
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
			if(getTxtNroDibujo().getValue() == null) {
				CLJOptionPane.showInformationMessage(GuiABMDibujoEstampado.this, "Por favor, ingrese el n�mero de dibujo", "Error");
				return false;
			}
			
			VarianteEstampado variante = new VarianteEstampado();
			JDialogEditarVariante dialogSeleccionarColores = new JDialogEditarVariante(GuiABMDibujoEstampado.this.getFrame(), variante, Integer.valueOf(getTxtNroDibujo().getValue().toString().toCharArray()[0]));
			GuiUtil.centrarEnPadre(dialogSeleccionarColores);
			dialogSeleccionarColores.setVisible(true);
			if(dialogSeleccionarColores.isAcepto()) {
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
			for(int sr : selectedRows) {
				varianteRemovedList.add(getElemento(sr));
			}
			dibujo.getVariantes().removeAll(varianteRemovedList);
			return true;
		}

		public void capturarSetearVariantes() {
			//Saco los que estaban antes y ahora no
			List<VarianteEstampado> varianteBorrarList = new ArrayList<VarianteEstampado>();
			for(VarianteEstampado variante : dibujo.getVariantes()) {
				//Si el elemento no existe mas en la tabla => lo saco de la lista de objetos
				if(getTabla().getFirstRowWithValue(COL_OBJ, variante) == -1) {
					varianteBorrarList.add(variante);
				}
			}
			dibujo.getVariantes().removeAll(varianteBorrarList);
		}

	}

	private PanelTablaVariante getPanelTablaVariante() {
		if(panelTablaVariante == null) {
			panelTablaVariante = new PanelTablaVariante();
			panelTablaVariante.setBorder(BorderFactory.createTitledBorder("VARIANTES"));
		}
		return panelTablaVariante;
	}

	private JLabel getLblImagen() {
		if(lblImagen == null){
			lblImagen = new JLabel();
			lblImagen.setBorder(BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
			lblImagen.setVerticalTextPosition(JLabel.CENTER);
			lblImagen.setHorizontalAlignment(JLabel.CENTER);
		}
		return lblImagen;
	}

	private JButton getBtnAgregarImagen() {
		if(btnAgregarImagen == null){
			btnAgregarImagen = new JButton("Agregar imagen");
			btnAgregarImagen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooserImage jFC = new JFileChooserImage();
					jFC.addFilter("Solo imagenes", new String[] { "gif", "jpg", "png" });
					try {
						File file = new File(getPathAnterior());
						jFC.setCurrentDirectory(file.getParentFile());
						if (file.exists()){
							jFC.setSelectedFile(file);
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					int returnVal = jFC.showOpenDialog(GuiABMDibujoEstampado.this);
					if (returnVal == JFileChooserImage.APPROVE_OPTION) {
						File file = jFC.getSelectedFile();
						if (valida(file)) {
							if (isValidImageDimension(file.getAbsolutePath())) {
								setPathAnterior(file.getAbsolutePath());
								loadImageFile(file.getAbsolutePath());
								habilitarBotonesImagen(true, true);
							} else {
								CLJOptionPane.showErrorMessage(GuiABMDibujoEstampado.this, "La imagen seleccionada supera las dimensiones esperadas.\n" + "El ancho m�ximo esperado debe ser menor o igual a: "
										+ ANCHO_IMAGEN + " pixeles.\n" + "El alto m�ximo esperado debe ser menor o igual a: " + ALTO_IMAGEN + " pixeles.\n", "Dimensiones inv�lidas");
							}
						} else{
							CLJOptionPane.showErrorMessage(null,"La imagen no es de un formato v�lido", "Tama\u00F1o inv�lido");
						}
					}
				}
			});
		}
		return btnAgregarImagen;
	}
	
	private String getPathAnterior() {
		if(pathAnterior == null){
			pathAnterior = "";
		}
		return pathAnterior;
	}
	
	private void setPathAnterior(String pathAnterior) {
		this.pathAnterior = pathAnterior;
	}

	private JButton getBtnQuitarImagen() {
		if(btnQuitarImagen == null){
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
		if(modoEdicion) {
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
				if (reader instanceof BMPImageReader){
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
//		ImageIcon imageIcon = new ImageIcon(filename);
//		int iconHeight = imageIcon.getIconHeight();
//		int iconWidth = imageIcon.getIconWidth();
//		if (iconHeight > ALTO_IMAGEN || iconWidth > ANCHO_IMAGEN)
//			return false;
//		return true;
	}

	private CLJNumericTextField getTxtNroDibujo() {
		if(txtNroDibujo == null){
			txtNroDibujo = new CLJNumericTextField();
			txtNroDibujo.setMaxLength(4);
		}
		return txtNroDibujo;
	}

}