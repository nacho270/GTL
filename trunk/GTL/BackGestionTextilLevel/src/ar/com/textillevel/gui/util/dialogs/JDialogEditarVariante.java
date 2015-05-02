package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.ColorCilindro;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

import com.sun.media.imageioimpl.plugins.bmp.BMPImageReader;

public class JDialogEditarVariante extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;
	private static final int ANCHO_IMAGEN = 500;
	private static final int ALTO_IMAGEN = 500;
	
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private String pathAnterior;
	private JPanel panelImagen;
	private JLabel lblImagen;
	private JButton btnAgregarImagen;
	private JButton btnQuitarImagen;
	
	private CLJTextField txtNombreVariante;
	private JComboBox cmbColorParaFondo;
	private JCheckBox chkFondoSeEstampaEnCilindro;
	
	private VarianteEstampado varianteEstampado;

	private boolean acepto;
	private List<Color> colorSelectedList;
	private Integer cantColores;
	private PanelTablaColorCilindro panTablaColorCilindro;

	private JPanel pnlBotones;
	private JPanel pnlDatos;

	private static Set<String> fontsDisponibles = Collections.synchronizedSet(new HashSet<String>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())));
	
	public JDialogEditarVariante(Frame padre, VarianteEstampado varianteEstampado, Integer cantColores) {
		super(padre);
		this.cantColores = cantColores;
		this.varianteEstampado = varianteEstampado;
		this.colorSelectedList = new ArrayList<Color>();
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	private void setDatos() {
		getTxtNombreVariante().setText(varianteEstampado.getNombre());
		getChkFondoSeEstampaEnCilindro().setSelected(varianteEstampado.fondoSeEstampaEnCilindro());
		getCmbColorParaFondo().setSelectedItem(varianteEstampado.getColorFondo());
		if(varianteEstampado.getImagenEstampado() !=null){
			putImageInLabel(varianteEstampado.getImagenEstampado());
		}

		Collections.sort(varianteEstampado.getColores(), new Comparator<ColorCilindro>() {

			public int compare(ColorCilindro o1, ColorCilindro o2) {
				if(o1.getColor() == null && o2.getColor() == null) {
					return 0;
				} else if(o1.getColor() == null) {
					return 1;
				} else if(o2.getColor() == null) {
					return -1;
				} else if(o1.getNroCilindro() != null && o2.getNroCilindro() != null){
					return o1.getNroCilindro() - o2.getNroCilindro();
				} else {//No deberia pasar, es para que no tire un NPE con datos viejos.
					return 0;
				}
			}

		});
		
		getPanTablaColorCilindro().agregarElementos(varianteEstampado.getColores());
	}

	public void putImageInLabel(ImageIcon imageIcon) {
		getLblImagen().setIcon(imageIcon);
		getLblImagen().setVerticalTextPosition(JLabel.CENTER);
	}

	private void setUpScreen(){
		setTitle("Carga de Variante");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(700, 750));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanelDatos(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			pnlDatos.add(new JLabel("NOMBRE: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTxtNombreVariante(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(new JLabel("COLOR DE FONDO: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getCmbColorParaFondo(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getChkFondoSeEstampaEnCilindro(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
			pnlDatos.add(getPanelImagen(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
			pnlDatos.add(getPanTablaColorCilindro(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
		}
		return pnlDatos;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()){
						varianteEstampado.setNombre(getTxtNombreVariante().getText().toUpperCase());
						varianteEstampado.setColorFondo((Color)getCmbColorParaFondo().getSelectedItem());
						varianteEstampado.getColores().clear();
						varianteEstampado.getColores().addAll(getPanTablaColorCilindro().getElementos());
						varianteEstampado.setImagenEstampado((ImageIcon)getLblImagen().getIcon());
						acepto = true;
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getTxtNombreVariante().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(JDialogEditarVariante.this, "Debe ingresar el nombre.", JDialogEditarVariante.this.getTitle());
			getTxtNombreVariante().requestFocus();
			return false;
		}
		if(getCmbColorParaFondo().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(JDialogEditarVariante.this, "Debe seleccionar un color de fondo.", JDialogEditarVariante.this.getTitle());
			return false;
		}
		//Si el fondo se estampa por cilindro => valido que se haya seleccionado el cilindro
		if(getChkFondoSeEstampaEnCilindro().isSelected()) {
			List<ColorCilindro> coloresCilindros = getPanTablaColorCilindro().getElementos();
			boolean existeCilindroParaElFondo = false;
			for(ColorCilindro cc : coloresCilindros) {
				if(cc.getColor() == null) {//es el fondo
					existeCilindroParaElFondo = true;
					break;
				}
			}
			if(!existeCilindroParaElFondo) {
				CLJOptionPane.showErrorMessage(JDialogEditarVariante.this, "Debe asignar un cilindro para el fondo.", JDialogEditarVariante.this.getTitle());
				return false;
			}
		} else {
			List<ColorCilindro> coloresCilindros = getPanTablaColorCilindro().getElementos();
			boolean existeCilindroParaElFondo = false;
			for(ColorCilindro cc : coloresCilindros) {
				if(cc.getColor() == null) {//es el fondo
					existeCilindroParaElFondo = true;
					break;
				}
			}
			if(existeCilindroParaElFondo) {
				CLJOptionPane.showErrorMessage(JDialogEditarVariante.this, StringW.wordWrap("Debe quitar el cilindro para el fondo y definir un color para ese cilindro."), JDialogEditarVariante.this.getTitle());
				return false;
			}
		}
		return true;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					varianteEstampado = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private CLJTextField getTxtNombreVariante() {
		if(txtNombreVariante == null){
			txtNombreVariante = new CLJTextField();
		}
		return txtNombreVariante;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public List<Color> getColorSelectedList() {
		return colorSelectedList;
	}

	private PanelTablaColorCilindro getPanTablaColorCilindro() {
		if(panTablaColorCilindro == null) {
			panTablaColorCilindro = new PanelTablaColorCilindro(JDialogEditarVariante.this);
		}
		return panTablaColorCilindro;
	}

	private JPanel getPanelImagen() {
		if(panelImagen == null){
			panelImagen = new JPanel();
			panelImagen.setLayout(new GridBagLayout());
			panelImagen.add(getLblImagen(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
			panelImagen.add(getBtnAgregarImagen(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelImagen.add(getBtnQuitarImagen(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panelImagen;
	}
	
	private JLabel getLblImagen() {
		if(lblImagen == null) {
			lblImagen = new JLabel();
			lblImagen.setBorder(BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
			lblImagen.setVerticalTextPosition(JLabel.CENTER);
			lblImagen.setHorizontalAlignment(JLabel.CENTER);
			lblImagen.setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
			lblImagen.setSize(lblImagen.getPreferredSize());
			
			lblImagen.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent me) {
					List<String> cilindrosDisponiles = cilindrosDisponiles();
					boolean hayQueMostrarDialogoDeCilindros = me.getClickCount() == 1 && 
															  varianteEstampado.getImagenEstampadoOriginal() != null &&
															  (!cilindrosDisponiles.isEmpty() || (getChkFondoSeEstampaEnCilindro().isSelected() && faltaAgregarFondo()));
					if(hayQueMostrarDialogoDeCilindros) {
						JDialogSelColorCilindro dialogo = new JDialogSelColorCilindro(JDialogEditarVariante.this, cilindrosDisponiles, getChkFondoSeEstampaEnCilindro().isSelected());
						GuiUtil.centrar(dialogo);
						dialogo.setVisible(true);
						if(dialogo.isAcepto()) {
							ColorCilindro colorCilindro = dialogo.getColorCilindro();
							getPanTablaColorCilindro().agregarElemento(colorCilindro);

							int x = me.getX() - (getLblImagen().getWidth() - ((ImageIcon)getLblImagen().getIcon()).getIconWidth())/2;
							int y = me.getY() - (getLblImagen().getHeight() - ((ImageIcon)getLblImagen().getIcon()).getIconHeight())/2;

							drawImage(colorCilindro, x, y);

							//Guardo las coordenadas para poder poder re-dibujar en caso de edición de coordenadas
							colorCilindro.setxCoordDibujo(x);
							colorCilindro.setyCoordDibujo(y);
						}
					}
				}

			});
		}
		return lblImagen;
	}

	private boolean faltaAgregarFondo() {
		for(ColorCilindro cc : getPanTablaColorCilindro().getElementos()) {
			if(cc.getColor() == null) {
				return false;
			}
		}
		return true;
	}

	public void drawImage(ColorCilindro colorCilindro, int x, int y) {
		BufferedImage img = new BufferedImage(getLblImagen().getIcon().getIconWidth(), getLblImagen().getIcon().getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		getLblImagen().getIcon().paintIcon(null, g, 0,0);
		g.dispose();

		Graphics2D g2 = img.createGraphics();
		g2.setColor(java.awt.Color.RED);
		String fontFamily = getFont("Times New Roman","FreeSerif");
		g2.setFont(new Font(fontFamily, Font.PLAIN, 40));
		g2.drawString(colorCilindro.getNroCilindro() == null ? String.valueOf(PanelTablaColorCilindro.LABEL_FONDO.toCharArray()[0]) : colorCilindro.getNroCilindro().toString(), x, y);
		
		getLblImagen().setIcon(new ImageIcon(img));
		getLblImagen().revalidate();
	}

	private List<String> cilindrosDisponiles() {
		List<String> nrosCilindros = new ArrayList<String>();
		//chequeo que no existan los cilindros
		for(int i = 1; i <= cantColores; i++) {
			boolean noExiste = true;
			for(ColorCilindro cc : getPanTablaColorCilindro().getElementos()) {
				if(cc.getNroCilindro() != null && cc.getNroCilindro().equals(i)) {
					noExiste = false;
				}
			}
			if(noExiste) {
				nrosCilindros.add(String.valueOf(i));
			}
		}
		return nrosCilindros;
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
		if (varianteEstampado != null) {
			varianteEstampado.setImagenEstampado(null);
			varianteEstampado.setImagenEstampadoOriginal(null);
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
	
	private String getPathAnterior() {
		if(pathAnterior == null){
			pathAnterior = "";
		}
		return pathAnterior;
	}
	
	private void setPathAnterior(String pathAnterior) {
		this.pathAnterior = pathAnterior;
	}

	private JButton getBtnAgregarImagen() {
		if(btnAgregarImagen == null){
			btnAgregarImagen = new JButton("Agregar imagen");
			btnAgregarImagen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean yaExisteUnaFotoConCilindros = false;
					
					//Si ya hay una foto cargada con cilindros pregunto antes de pisar la foto
					if(varianteEstampado.getImagenEstampado() != null && !getPanTablaColorCilindro().getElementos().isEmpty()) {
						yaExisteUnaFotoConCilindros = CLJOptionPane.showQuestionMessage(JDialogEditarVariante.this, StringW.wordWrap("Al cambiar la foto se borrará toda la configuración de cilindros ¿Desea continuar?"), "Advertencia") == CLJOptionPane.YES_OPTION;
						if(!yaExisteUnaFotoConCilindros) {
							return;
						}
					}

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

					int returnVal = jFC.showOpenDialog(JDialogEditarVariante.this);
					if (returnVal == JFileChooserImage.APPROVE_OPTION) {
						File file = jFC.getSelectedFile();
						if (valida(file)) {
							if (isValidImageDimension(file.getAbsolutePath())) {
								setPathAnterior(file.getAbsolutePath());

								//Cada vez que se elige una foto nuevamente guardo la imagen original.
								loadImageFile(file.getAbsolutePath());
								ImageIcon icon = (ImageIcon)getLblImagen().getIcon();
								varianteEstampado.setImagenEstampadoOriginal(new ImageIcon(icon.getImage()));
								//borro los datos de cilindros si es que ya habia foto con cilindros cargados.
								if(yaExisteUnaFotoConCilindros) {
									getPanTablaColorCilindro().getTabla().removeAllRows();
									varianteEstampado.getColores().clear();
								}

								habilitarBotonesImagen(true, true);
							} else {
								CLJOptionPane.showErrorMessage(JDialogEditarVariante.this, "La imagen seleccionada supera las dimensiones esperadas.\n" + "El ancho máximo esperado debe ser menor o igual a: "
										+ ANCHO_IMAGEN + " pixeles.\n" + "El alto máximo esperado debe ser menor o igual a: " + ALTO_IMAGEN + " pixeles.\n", "Dimensiones inválidas");
							}
						} else{
							CLJOptionPane.showErrorMessage(null,"La imagen no es de un formato válido", "Tama\u00F1o inválido");
						}
					}
				}

				private void loadImageFile(String filename) {
					//leo la imagen en escala de grises
					BufferedImage bufferedImage = null;
					try {
						bufferedImage = ImageIO.read(new File(filename));
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (int w = 0; w < bufferedImage.getWidth(); w++) {
						for (int h = 0; h < bufferedImage.getHeight(); h++) {
							java.awt.Color color = new java.awt.Color(bufferedImage.getRGB(w, h));
							int averageColor = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);
							java.awt.Color avg = new java.awt.Color(averageColor, averageColor, averageColor);
							bufferedImage.setRGB(w, h, avg.getRGB());
						}
					}

					getLblImagen().setIcon(null);
					ImageIcon icon = new ImageIcon(bufferedImage);

					Image scaleImage = ImageUtil.scale(icon.getImage(), ANCHO_IMAGEN, ALTO_IMAGEN, false);
					getLblImagen().setIcon(new ImageIcon(scaleImage));

					getLblImagen().setText(null);
					getLblImagen().revalidate();
					
				}

				private boolean valida(File file) {
					try {
						ImageInputStream iis = ImageIO.createImageInputStream(file);
						Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
						if (readers.hasNext()) {
							ImageReader reader = readers.next();
							reader.setInput(iis, true);
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
				}
			});
		}
		return btnAgregarImagen;
	}

	private String getFont(String... families){
		for (int i=0;i<families.length;i++){
			if (fontsDisponibles.contains(families[i])){
				return families[i];
			}
		}
		return null;
	}

	private JCheckBox getChkFondoSeEstampaEnCilindro() {
		if(chkFondoSeEstampaEnCilindro == null) {
			chkFondoSeEstampaEnCilindro = new JCheckBox("FONDO SE ESTAMPA EN CILINDRO");
		}
		return chkFondoSeEstampaEnCilindro;
	}

	private JComboBox getCmbColorParaFondo() {
		if(cmbColorParaFondo == null) {
			cmbColorParaFondo = new JComboBox();
			GuiUtil.llenarCombo(cmbColorParaFondo, GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbColorParaFondo;
	}

	public static class PanelTablaColorCilindro extends PanelTabla<ColorCilindro> {

		public static final String LABEL_FONDO="FONDO";

		private static final long serialVersionUID = -2448440390638456345L;

		private static final int CANT_COLS = 5;
		private static final int COL_NRO_CILINDRO = 0;
		private static final int COL_COLOR = 1;
		private static final int COL_METROS_POR_COLOR = 2;
		private static final int COL_KILOS_POR_COLOR = 3;
		private static final int COL_OBJ = 4;
		
		private JDialogEditarVariante jDialogEditarVariante;

		public PanelTablaColorCilindro(JDialogEditarVariante jDialogEditarVariante) {
			this.jDialogEditarVariante = jDialogEditarVariante;
			getBotonAgregar().setVisible(false);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_NRO_CILINDRO, "CILINDRO", 70);
			tabla.setStringColumn(COL_COLOR, "COLOR", 130);
			tabla.setStringColumn(COL_METROS_POR_COLOR, "MTS. POR COLOR", 100);
			tabla.setStringColumn(COL_KILOS_POR_COLOR, "KG. POR COLOR", 100);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

		@Override
		protected void agregarElemento(ColorCilindro elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_CILINDRO] = elemento.getNroCilindro() == null ? "" : elemento.getNroCilindro().toString();
			row[COL_COLOR] = elemento.getColor() == null ? "[FONDO]" : elemento.getColor().toString();
			row[COL_METROS_POR_COLOR] = elemento.getMetrosPorColor();
			row[COL_KILOS_POR_COLOR] = elemento.getKilosPorColor();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ColorCilindro getElemento(int fila) {
			return (ColorCilindro)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			if(jDialogEditarVariante.varianteEstampado.getImagenEstampadoOriginal() != null) {
				jDialogEditarVariante.putImageInLabel(jDialogEditarVariante.varianteEstampado.getImagenEstampadoOriginal());
				for(int i = 0; i < getTabla().getRowCount(); i++) {
					if(i != selectedRow) {
						ColorCilindro cc = getElemento(i);
						if(cc.getxCoordDibujo() != null && cc.getyCoordDibujo() != null) {
							jDialogEditarVariante.drawImage(cc, cc.getxCoordDibujo(), cc.getyCoordDibujo());
						}
					}
				}
			}
			return true;
		}

	}

}