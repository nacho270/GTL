package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.ventas.productos.PrecioProducto;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogEditarPrecioProducto extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private CLJTextField txtProducto;

	private JPanel pnlBotones;
	private JPanel pnlDatos;

	private CLJTextField txtPrecioProducto;
	private PrecioProducto precioProducto;
	private boolean acepto;

	public JDialogEditarPrecioProducto(Frame padre, PrecioProducto precioProducto) {
		super(padre);
		this.precioProducto = precioProducto;
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	private void setDatos() {
		getTxtProducto().setText(precioProducto.getProducto().getDescripcion());
		getTxtPrecio().setText(precioProducto.getPrecio().toString());
	}

	private void setUpScreen(){
		setTitle("Editar Precio");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 150));
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			pnlDatos.add(new JLabel("Producto: "), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(0, 0, 10, 0);
			pnlDatos.add(getTxtProducto(), gridBagConstraints);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			pnlDatos.add(new JLabel("Precio: "), gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			pnlDatos.add(getTxtPrecio(), gridBagConstraints);
		}
		return pnlDatos;
	}

	private CLJTextField getTxtPrecio() {
		if(txtPrecioProducto == null){
			txtPrecioProducto = new CLJTextField();
			txtPrecioProducto.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnAceptar().doClick();
					}
				}

			});
			
		}
		return txtPrecioProducto;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(validar()){
							BigDecimal precio = new BigDecimal(getTxtPrecio().getText().replace(',', '.'));
							precioProducto.setPrecio(precio);
							precioProducto.setFechaUltModif(DateUtil.getAhora());
							acepto = true;
							dispose();
						}
					}catch(RuntimeException re){
						BossError.gestionarError(re);
					}
				}

				private boolean validar() {
					if(getTxtProducto().getText().trim().length() == 0){
						CLJOptionPane.showErrorMessage(JDialogEditarPrecioProducto.this, "Debe ingresar el precio.", JDialogEditarPrecioProducto.this.getTitle());
						return false;
					}
					if(!GenericUtils.esNumerico(getTxtPrecio().getText().trim())) {
						CLJOptionPane.showErrorMessage(JDialogEditarPrecioProducto.this, "El precio debe ser numérico.", JDialogEditarPrecioProducto.this.getTitle());
						return false;
					}
					return true;
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

	private CLJTextField getTxtProducto() {
		if(txtProducto == null){
			txtProducto = new CLJTextField();
			txtProducto.setEditable(false);
		}
		return txtProducto;
	}

	public boolean isAcepto() {
		return acepto;
	}


}