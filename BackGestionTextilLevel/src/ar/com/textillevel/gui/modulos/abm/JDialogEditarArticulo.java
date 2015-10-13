package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogEditarArticulo extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private FWJTextField txtNombre;
	private FWJTextField txtDescripcion;
	private FWJTextField txtGramaje;
	private FWJTextField txtAncho;

	private Articulo articulo;
	private boolean acepto;

	public JDialogEditarArticulo(Frame owner, Articulo articulo) {
		super(owner);
		this.articulo = articulo;
		setTitle("Cargar Artículo");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 250));
		setResizable(false);
		setModal(true);
		construct();
		setDatos();
	}

	private void setDatos() {
		getTxtNombre().setText(articulo.getNombre());
		getTxtDescripcion().setText(articulo.getDescripcion());
		getTxtGramaje().setText(articulo.getGramaje() == null ? "" : articulo.getGramaje().toString());
		getTxtAncho().setText(articulo.getAncho() == null ? "" : articulo.getAncho().toString());
	}

	private void construct() {
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
			pnlDatos.add(new JLabel(" NOMBRE:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTxtNombre(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(new JLabel(" DESCRIPCION:"), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTxtDescripcion(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(new JLabel(" GRAMAJE:"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTxtGramaje(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(new JLabel(" ANCHO:"), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTxtAncho(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return pnlDatos;
	}		
	
	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						if(!StringUtil.isNullOrEmpty(getTxtAncho().getText())) {
							BigDecimal ancho = new BigDecimal(getTxtAncho().getText().trim().replace(',', '.'));
							articulo.setAncho(ancho);
						}
						if(!StringUtil.isNullOrEmpty(getTxtGramaje().getText())) {
							BigDecimal gramaje = new BigDecimal(getTxtGramaje().getText().trim().replace(',', '.'));
							articulo.setGramaje(gramaje);
						}
						articulo.setNombre(getTxtNombre().getText().trim().toUpperCase());
						articulo.setDescripcion(getTxtDescripcion().getText().trim().toUpperCase());
						acepto = true;
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		String anchoStr = getTxtAncho().getText();
		if(StringUtil.isNullOrEmpty(anchoStr) || !GenericUtils.esNumerico(anchoStr)) {
			FWJOptionPane.showErrorMessage(JDialogEditarArticulo.this, "Debe ingresar un ancho válido.", "Error");
			getTxtAncho().requestFocus();
			return false;
		}
		String gramajeStr = getTxtGramaje().getText();
		if(StringUtil.isNullOrEmpty(gramajeStr) || !GenericUtils.esNumerico(gramajeStr)) {
			FWJOptionPane.showErrorMessage(JDialogEditarArticulo.this, "Debe ingresar un gramaje válido.", "Error");
			getTxtGramaje().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					acepto = false;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private FWJTextField getTxtGramaje() {
		if(txtGramaje == null) {
			txtGramaje = new FWJTextField();
		}
		return txtGramaje;
	}

	private FWJTextField getTxtAncho() {
		if(txtAncho == null) {
			txtAncho = new FWJTextField();
		}
		return txtAncho;
	}

	private FWJTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new FWJTextField();
		}
		return txtNombre;
	}

	private FWJTextField getTxtDescripcion() {
		if(txtDescripcion == null) {
			txtDescripcion = new FWJTextField();
		}
		return txtDescripcion;
	}

	public boolean isAcepto() {
		return acepto;
	}

	
}
