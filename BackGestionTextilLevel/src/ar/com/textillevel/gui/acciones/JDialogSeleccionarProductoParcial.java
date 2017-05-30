package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.textillevel.entidades.ventas.ProductoArticuloParcial;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.gui.util.panels.PanSeleccionProductoArticuloParcial;

public class JDialogSeleccionarProductoParcial extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private boolean acepto;
	private List<ProductoArticuloParcial> productoSelectedList;
	private List<Articulo> articuloFilterList;
	private JPanel pnlBotones;

	private PanSeleccionProductoArticuloParcial panPA;

	public JDialogSeleccionarProductoParcial(JDialog owner, List<ProductoArticuloParcial> productoSelectedList, List<Articulo> articuloFilterList) {
		super(owner);
		this.productoSelectedList = new ArrayList<ProductoArticuloParcial>(productoSelectedList);
		this.articuloFilterList = articuloFilterList;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Selección de Productos Parciales");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 550));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanPA(),BorderLayout.CENTER);
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

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acepto = true;
					dispose();
				}

			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public boolean isAcepto() {
		return acepto;
	}

	private PanSeleccionProductoArticuloParcial getPanPA() {
		if(panPA == null) {
			panPA = new PanSeleccionProductoArticuloParcial(this, productoSelectedList, articuloFilterList);
		}
		return panPA;
	}

	public List<ProductoArticuloParcial> getProductoSelectedList() {
		return getPanPA().getProductoSelectedList();
	}

}