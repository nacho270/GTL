package ar.com.textillevel.gui.acciones.remitosalida;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class JDialogResultadoAltaRemitoSalida extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton btnAceptar;
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private BigDecimal porcMermaConf;
	private List<RemitoSalida> remitosSalida;

	public JDialogResultadoAltaRemitoSalida(JDialog owner, List<RemitoSalida> remitosSalida, BigDecimal porcMermaConf) {
		super(owner);
		this.remitosSalida = remitosSalida;
		this.porcMermaConf = porcMermaConf; 
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Información");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(420, remitosSalida.size() == 1 ? 150 : remitosSalida.size() * 80));
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
			JLabel lbl = new JLabel(remitosSalida.size() == 1 ? "El Remito se ha grabado con éxito." : "Los remitos se han grabado con éxito.");
			pnlDatos.add(lbl, gc);
			//Creo los labels con el status de las mermas de los remitos
			int i = 1;
			for(RemitoSalida rs : remitosSalida) {
				gc = new GridBagConstraints();
				gc.gridx = 0;
				gc.gridy = i;
				gc.fill = GridBagConstraints.BOTH;
				gc.weightx = 1;
				gc.weighty = 1;
				pnlDatos.add(createLabelStatusMerma(rs), gc);
				i++;
			}
		}
		return pnlDatos;
	}

	private JLabel createLabelStatusMerma(RemitoSalida rs) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		BigDecimal porcMerma = rs.getPorcentajeMerma();
		JLabel lblStatusMerma = new JLabel("Remito " + rs.getNroRemito()  + " -> Merma: " + df.format(porcMerma) + "%");
		lblStatusMerma.setFont(new Font("Dialog", Font.BOLD, 30));
		if(porcMerma.compareTo(BigDecimal.ZERO) < 0 && porcMerma.abs().compareTo(porcMermaConf) > 0) {
			lblStatusMerma.setForeground(Color.RED);
		} else {
			lblStatusMerma.setForeground(Color.GREEN);
		}
		return lblStatusMerma;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						dispose();
				}
			});
		}
		return btnAceptar;
	}

	public static void main(String[] args) {
		List<RemitoSalida> remitos = new ArrayList<RemitoSalida>();
		RemitoSalida rs = new RemitoSalida();
		rs.setNroRemito(100);
		rs.setPorcentajeMerma(new BigDecimal(20));
		remitos.add(rs);
		rs = new RemitoSalida();
		rs.setNroRemito(102);
		rs.setPorcentajeMerma(new BigDecimal(-40));
		remitos.add(rs);
		rs = new RemitoSalida();
		rs.setNroRemito(103);
		rs.setPorcentajeMerma(new BigDecimal(40));
		remitos.add(rs);
		rs = new RemitoSalida();
		rs.setNroRemito(104);
		rs.setPorcentajeMerma(new BigDecimal(60));
		remitos.add(rs);
		JDialogResultadoAltaRemitoSalida dialogResultadoAltaRemitoSalida = new JDialogResultadoAltaRemitoSalida(new JDialog(), remitos, new BigDecimal(3));
		dialogResultadoAltaRemitoSalida.setVisible(true);
	}

}