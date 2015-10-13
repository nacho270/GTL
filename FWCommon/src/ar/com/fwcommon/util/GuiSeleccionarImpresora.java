package ar.com.fwcommon.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.standard.PrinterResolution;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;

@SuppressWarnings("serial")
public class GuiSeleccionarImpresora extends JDialog {

	private PrintService[] impresoras;
	private JComboBox cmbImpresoras;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JCheckBox chkImpresoraMatrizPunto;
	private String impresoraSeleccionada;
	private final ListPrinterMode printMode;	
	private static final int MIN_CROSSFEED_RESOLUTION_DPI = 200;
	private static final int MIN_FEED_RESOLUTION_DPI = 200;
	
	
	/**
	 * Se marcó como <code>@Deprecated</code> , debido a que ahora es necesario implementar la interfaz
	 * <code>ListPrinterMode</code> para setear el modo de impresora a utilizar.
	 * Constructor.
	 * @param owner
	 * @param esImpresoraNormal
	 * @throws java.awt.HeadlessException
	 * GuiSeleccionarImpresora(Frame owner, ListPrinterMode printMode)
	 */
	@Deprecated
	public GuiSeleccionarImpresora(Frame owner, boolean esImpresoraNormal) throws HeadlessException {
		this(owner, (esImpresoraNormal)?
				new DefaultModePrinter():
					new DefaultModePrinter(DefaultModePrinter.PRINTER_PAPELETA));
	}

	/**
	 * Crea la impresora a utilizar. Definida en <code>DefaultModePrinter</code>
	 * @param owner
	 * @param printMode el modo de impresora actual.
	 * @throws java.awt.HeadlessException
	 */
	public GuiSeleccionarImpresora(Frame owner, ListPrinterMode printMode) throws HeadlessException {
		super(owner, true);
		if(printMode == null) {
			throw new IllegalArgumentException("El modo de impresora a utilizar no puede ser nulo o no es válido");
		}				
		this.printMode = printMode;
		construirDialogo();
		impresoras = PrinterJob.lookupPrintServices();
		for(int i = 0; i < impresoras.length; i ++) {
			cmbImpresoras.addItem(impresoras[i].getName());
			if(i == getPropiedadId()) {
				cmbImpresoras.setSelectedItem(impresoras[i].getName());	
			}			
		}
		chkImpresoraMatrizPunto.setSelected(isMatrizPuntos());
	}

	private void construirDialogo() {
		setSize(new Dimension(350, 130));
		setTitle(printMode.getTituloDialogo());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());
		//Impresoras
		cmbImpresoras = new JComboBox();
		cmbImpresoras.setMaximumRowCount(2);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 2, 10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		getContentPane().add(cmbImpresoras, constraints);
		chkImpresoraMatrizPunto = BossEstilos.createCheckBox("Es impresora de matriz de punto", false);
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, 10, 5, 0);
		constraints.gridy = 1;
		constraints.gridx = 0;
		getContentPane().add(chkImpresoraMatrizPunto, constraints);
		//Botones
		JPanel panBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 0, 0, 0);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 2;
		constraints.gridx = 0;
		getContentPane().add(panBotones, constraints);
		//Aceptar
		btnAceptar = BossEstilos.createButton("Aceptar");
		btnAceptar.addActionListener(new BtnAceptarActionListener());
		panBotones.add(btnAceptar);
		//Cancelar
		btnCancelar = BossEstilos.createButton("Cancelar");
		btnCancelar.addActionListener(new BtnCancelarActionListener());
		panBotones.add(btnCancelar);
		//Centrar el diálogo
		GuiUtil.centrar(this);
	}

	/**
	 * Devuelve la descripción de la impresora seleccionada.
	 * @return impresoraSeleccionada
	 */
	public String getImpresoraSeleccionada() {
	    return impresoraSeleccionada;
	}

	/**
	 * Se marcó como deprecated debido a que se encapsulo en <code>DefaultModePrinter</code>
     * Devuelve el Id de la Impresora Normal.
     * @return idImpresoraNormal
     */
	@Deprecated
    public static int getIdImpresoraNormal() {
    	int idImpresoraNormal;
        if(System.getProperty("idImpresoraNormal") != null) {
            idImpresoraNormal = Integer.valueOf(System.getProperty("idImpresoraNormal")).intValue();
        } else {
            idImpresoraNormal = 0;
        }
        return idImpresoraNormal;
    }

    /**
     * Setea el Id de la Impresora Normal.
     * @param idImpresoraNormal
     */
	 @Deprecated
    public static void setIdImpresoraNormal(int idImpresoraNormal) {
        System.setProperty("idImpresoraNormal", String.valueOf(idImpresoraNormal));
    }

    /**
     * Se marcó como deprecated debido a que se encapsulo en <code>DefaultModePrinter</code>
     * Devuelve el Id de la Impresora de la Papeleta.
     * @return idImpresoraPapeleta
     */
    @Deprecated
    public static int getIdImpresoraPapeleta() {
    	int idImpresoraPapeleta;
        if(System.getProperty("idImpresoraPapeleta") != null) {
            idImpresoraPapeleta = Integer.valueOf(System.getProperty("idImpresoraPapeleta")).intValue();
        } else {
            idImpresoraPapeleta = 0;
        }
        return idImpresoraPapeleta;
    }

    /**
     * Setea el Id de la Impresora de la Papeleta.
     * @param idImpresoraPapeleta
     */
    @Deprecated
    public static void setIdImpresoraPapeleta(int idImpresoraPapeleta) {
        System.setProperty("idImpresoraPapeleta", String.valueOf(idImpresoraPapeleta));
    }

    /**
     * Devuelve el Nombre de la Impresora Normal.
     * @return nombreImpresoraNormal
     */
    @Deprecated
    public static String getNombreImpresoraNormal() {
    	String nombreImpresoraNormal;
        if(System.getProperty("NombreImpresoraNormal") != null) {
            nombreImpresoraNormal = System.getProperty("NombreImpresoraNormal");
        } else {
            nombreImpresoraNormal = "No Asignada";
        }
        return nombreImpresoraNormal;
    }

    /**
     * Setea el Nombre de la Impresora Normal.
     * @param nombreImpresoraNormal
     */
    @Deprecated
    public static void setNombreImpresoraNormal(String nombreImpresoraNormal) {
        System.setProperty("NombreImpresoraNormal", nombreImpresoraNormal);
    }

    /**
     * Devuelve el Nombre de la Impresora de la Papeleta.
     * @return nombreImpresoraPapeleta
     */
    @Deprecated
    public static String getNombreImpresoraPapeleta() {
    	String nombreImpresoraPapeleta;
        if(System.getProperty("NombreImpresoraPapeleta") != null) {
            nombreImpresoraPapeleta = System.getProperty("NombreImpresoraPapeleta");
        } else {
            nombreImpresoraPapeleta = "No Asignada";
        }
        return nombreImpresoraPapeleta;
    }

    /**
     * Setea el Nombre de la Impresora de la Papeleta.
     * @param nombreImpresoraPapeleta
     */
    @Deprecated
    public static void setNombreImpresoraPapeleta(String nombreImpresoraPapeleta) {
        System.setProperty("NombreImpresoraPapeleta", nombreImpresoraPapeleta);
    }

    /**
     * Setea si la impresora es de matriz de punto.
     * @param impresoraMatrizPunto
     */
    @Deprecated
    public static void setImpresoraMatrizPunto(boolean impresoraMatrizPunto) {
    	System.setProperty("ImpresoraMatrizPunto", String.valueOf(impresoraMatrizPunto));
    }
    
    private int getPropiedadId() {
    	String valor = System.getProperty(this.printMode.getPropiedadId());
    	if(valor == null) return -1;
		return Integer.valueOf(valor);
    }
	/**
	 * Setea el valor id para el modo de la impresora actual. 
	 * @param valor
	 */
	private void setPropiedadId(int valor) {
		System.setProperty(this.printMode.getPropiedadId(), String.valueOf(valor));

	}	

	/**
	 * Setea el valor <code>true</code> si es matriz de puntos <code>false</code> caso contrario.
	 * @param valor 
	 */
	private void setPropiedadMatrizPuntos(boolean valor) {
		System.setProperty(this.printMode.getPropiedadMatrizPuntos(), String.valueOf(valor));
	}

	/**
	 * Setea el valor de la propiedad nombre.
	 * @param valor
	 */
	private void setPropiedadNombre(String valor) {
		if(valor == null) {
			System.setProperty(this.printMode.getPropiedadNombre(), String.valueOf("Sin Asignar"));
		} else {
			System.setProperty(this.printMode.getPropiedadNombre(), String.valueOf(valor));
		}
	}		

    /**
     * Devuelve <b>true</b> si la impresora esta marcada como matriz de punto.
     * @return
     */
	private boolean isMatrizPuntos() {
		String impresoraMatrizPunto = System.getProperty(this.printMode.getPropiedadMatrizPuntos());
		if(impresoraMatrizPunto != null) {
			return Boolean.valueOf(impresoraMatrizPunto);
		}
		return false;
	}

	private boolean validarResolucion() {
		PrintService impresora = impresoras[cmbImpresoras.getSelectedIndex()];
		PrinterResolution pr = (PrinterResolution)impresora.getDefaultAttributeValue(PrinterResolution.class);
		int resolution[] = pr.getResolution(PrinterResolution.DPI);
		boolean continuar = true;
		if(chkImpresoraMatrizPunto.isSelected()) {
			if(resolution[0] > MIN_CROSSFEED_RESOLUTION_DPI || resolution[1] > MIN_FEED_RESOLUTION_DPI) {
				if(FWJOptionPane.showQuestionMessage(getOwner(), "La impresora seleccionada podría no ser de matriz de punto. ¿Desea continuar?", "Advertencia") == FWJOptionPane.NO_OPTION) {
					chkImpresoraMatrizPunto.setSelected(false);
					continuar = false;
				}
			}
		} else {
			if(resolution[0] < MIN_CROSSFEED_RESOLUTION_DPI || resolution[1] < MIN_FEED_RESOLUTION_DPI) {
				if(FWJOptionPane.showQuestionMessage(getOwner(), "La impresora seleccionada aparenta ser de matriz de punto. ¿Desea continuar?", "Advertencia") == FWJOptionPane.YES_OPTION) {
					chkImpresoraMatrizPunto.setSelected(true);
				} else {
					continuar = false;
				}
			}
		}
		return continuar;
	}

	class BtnAceptarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			impresoraSeleccionada = (String)cmbImpresoras.getSelectedItem();
			setPropiedadId(cmbImpresoras.getSelectedIndex());
			setPropiedadNombre(impresoraSeleccionada);
			setPropiedadMatrizPuntos(chkImpresoraMatrizPunto.isSelected());			
			if(validarResolucion()) {
				dispose();
			}
		}
	}

	class BtnCancelarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			impresoraSeleccionada = null;
			dispose();
		}
	}

}