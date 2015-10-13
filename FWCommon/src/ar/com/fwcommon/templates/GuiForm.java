package ar.com.fwcommon.templates;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.templates.main.AbstractMainTemplate;

/**
 * Template básico para la creación de formularios MDI.
 * ar.com.fwcommon.templates.GuiABMTemplate
 */
public abstract class GuiForm extends JInternalFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnSalir;
    protected static final int ALTO_DEFAULT = 728;
    protected static final int ANCHO_DEFAULT = 1014;
    protected static final int ALTO_PIE = 83;
    private Modulo modulo;

	public abstract boolean botonSalirPresionado();

    /** Método constructor */
    protected GuiForm() {
        this(ANCHO_DEFAULT, ALTO_DEFAULT);
    }

    protected GuiForm(Modulo modulo) throws FWException {
        this(ANCHO_DEFAULT, ALTO_DEFAULT, modulo);
    }

    /**
     * Método constructor.
     * @param ancho El ancho del formulario.
     * @param alto El alto del formulario.
     */
    protected GuiForm(int ancho, int alto) {
        super();
        construct(ancho, alto);
    }

    public GuiForm(int ancho, int alto, Modulo modulo) throws FWException {
    	super();
    	construct(ancho, alto);
    	this.modulo = modulo;
    	this.setTitle(modulo.getNombre());
    }

    private void construct(int ancho, int alto) {
        setPreferredSize(new Dimension(ancho, alto));
        setIconifiable(true);
        setResizable(true);
        //Botón 'Salir'
        btnSalir = BossEstilos.createButton("Salir >>");
        btnSalir.setToolTipText("Cerrar el módulo");
        btnSalir.setMnemonic(java.awt.event.KeyEvent.VK_S);
        getContentPane().add(btnSalir);
        btnSalir.setBounds(ancho - 123, alto - 70, 90, 20);
		btnSalir.setPreferredSize(new Dimension(90, 20));
        btnSalir.addActionListener(new BotonSalirListener());
    }

    /** Devuelve el estado activo/inacivo del formulario */
    public boolean isActiva() {
        return super.isVisible();
    }

    /** Setea el estado activo/inactivo del formulario */
    public void setActiva(boolean activa) {
        super.setVisible(activa);
    }

    /**
     * Devuelve el botón <b>Salir</b>.
     * @return btnSalir
     */
    public JButton getBtnSalir() {
        return btnSalir;
    }

    /** Devuelve el frame padre */
    public Frame getFrame() {
    	return AbstractMainTemplate.getFrameInstance();
    }

    /** Clase para el manejo del evento click del botón 'Salir' */
    public class BotonSalirListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if(botonSalirPresionado()) {
                dispose();
            }
        }
    }

    public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
}