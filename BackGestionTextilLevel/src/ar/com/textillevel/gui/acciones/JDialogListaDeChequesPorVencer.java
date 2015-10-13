package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogListaDeChequesPorVencer extends JDialog{

	private static final long serialVersionUID = -8902172799078457735L;
	private PanelTablaCheques panelTablaCheques;
	private List<Cheque> cheques;
	private Integer diasVencimiento;

	public JDialogListaDeChequesPorVencer(Frame padre, List<Cheque> chequesAMostrar){
		super(padre);
		setDiasVencimiento(GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getDiasVenceCheque());
		setCheques(chequesAMostrar);
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}
	
	private void setUpScreen() {
		setTitle("Cheques por vencer");
		setSize(new Dimension(450,300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
	}

	private void setUpComponentes() {
		this.add(getPanelTablaCheques(),BorderLayout.CENTER);
		this.add(getPanelSur(),BorderLayout.SOUTH);
	}

	private void llenarTabla(){
		for(Cheque c : getCheques()){
			getPanelTablaCheques().agregarElemento(c);
		}
	}
	
	private JPanel getPanelSur() {
		JButton btnSalir = new JButton("Aceptar");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(btnSalir);
		return panel;
	}

	private class PanelTablaCheques extends PanelTablaSinBotones<Cheque>{

		private static final long serialVersionUID = 4247457498635763788L;
		
		private static final int CANT_COLS_TBL_CHEQUES = 5;
		private static final int COL_NUMERACION = 0;
		private static final int COL_FECHA_DEPOSITO = 1;
		private static final int COL_FECHA_VENCIMIENTO = 2;
		private static final int COL_DIAS_FALTANTES = 3;
		private static final int COL_OBJ_CHEQUE = 4;

		@Override
		protected void agregarElemento(Cheque cheque) {
			Object[] row = new Object[CANT_COLS_TBL_CHEQUES];
			row[COL_NUMERACION] = cheque.getNumeracion();
			row[COL_FECHA_DEPOSITO] = DateUtil.dateToString(cheque.getFechaDeposito(), DateUtil.SHORT_DATE);
			int diasParaVencimiento = DateUtil.contarDias(DateUtil.getHoy(), DateUtil.sumarDias(cheque.getFechaDeposito(), getDiasVencimiento()));
			row[COL_DIAS_FALTANTES] = diasParaVencimiento==0?"HOY":diasParaVencimiento;
			row[COL_FECHA_VENCIMIENTO]=DateUtil.dateToString(DateUtil.sumarDias(cheque.getFechaDeposito(), getDiasVencimiento()), DateUtil.SHORT_DATE);
			row[COL_OBJ_CHEQUE]=cheque;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS_TBL_CHEQUES);
			tabla.setStringColumn(COL_NUMERACION, "Número", 100,100,true);
			tabla.setStringColumn(COL_FECHA_DEPOSITO, "Fecha de deposito", 100, 100, true);
			tabla.setStringColumn(COL_FECHA_VENCIMIENTO, "Fecha de vencimiento", 120, 120, true);
			tabla.setStringColumn(COL_DIAS_FALTANTES, "Días restantes", 80,80, true);
			tabla.setStringColumn(COL_OBJ_CHEQUE, "", 0, 0, true);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(true);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected Cheque getElemento(int fila) {
			return (Cheque) getTabla().getValueAt(fila, COL_OBJ_CHEQUE);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}

	private PanelTablaCheques getPanelTablaCheques() {
		if(panelTablaCheques == null){
			panelTablaCheques = new PanelTablaCheques();
		}
		return panelTablaCheques;
	}
	
	public static void main(String[] args){
		new JDialogListaDeChequesPorVencer(null, null);
	}

	private List<Cheque> getCheques() {
		return cheques;
	}

	private void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
	}

	
	public Integer getDiasVencimiento() {
		return diasVencimiento;
	}

	
	public void setDiasVencimiento(Integer diasVencimiento) {
		this.diasVencimiento = diasVencimiento;
	}
}
