package ar.com.textillevel.gui.acciones.impresionremito;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class JDialogAvisoImpresionRemitoSalida extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton btnAceptar;
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private RemitoSalida remitoSalida;
	private int nroCopia;
	private int totalCopias;

	public JDialogAvisoImpresionRemitoSalida(JDialog owner, RemitoSalida remitoSalida, int nroCopia, int totalCopias) {
		super(owner);
		this.nroCopia = nroCopia;
		this.totalCopias = totalCopias;
		this.remitoSalida = remitoSalida;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Información");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 150));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanelCarga(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCarga(){
		JPanel pnl = new JPanel();
		pnl.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
		pnl.add(getPanelDatos());
		return pnl;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			gc.fill = GridBagConstraints.HORIZONTAL;
			pnlDatos.add(createLabelLeyenda(), gc);
			gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 1;
			gc.weightx = 1;
			gc.weighty = 1;
			gc.anchor = GridBagConstraints.CENTER;
			pnlDatos.add(createLabelNroRemito(), gc);
		}
		return pnlDatos;
	}

	private JLabel createLabelLeyenda() {
		JLabel lblStatusMerma = new JLabel("Se imprimirá el remito: ");
		lblStatusMerma.setFont(new Font("Dialog", Font.BOLD, 30));
		lblStatusMerma.setForeground(Color.GREEN);
		return lblStatusMerma;
	}

	private JLabel createLabelNroRemito() {
		JLabel lblStatusMerma = new JLabel(remitoSalida.getNroRemito().toString() + " - Copia " + nroCopia  + " de " + totalCopias);
		lblStatusMerma.setFont(new Font("Dialog", Font.BOLD, 30));
		lblStatusMerma.setForeground(Color.BLACK);
		return lblStatusMerma;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Continuar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						dispose();
				}
			});
		}
		return btnAceptar;
	}

	public static void main(String[] args) {
		RemitoSalida remitoSalida2 = new RemitoSalida();
		remitoSalida2.setNroRemito(100234);
		JDialogAvisoImpresionRemitoSalida dialogResultadoAltaRemitoSalida = new JDialogAvisoImpresionRemitoSalida(new JDialog(), remitoSalida2, 2, 5);
		dialogResultadoAltaRemitoSalida.setVisible(true);
	}

}