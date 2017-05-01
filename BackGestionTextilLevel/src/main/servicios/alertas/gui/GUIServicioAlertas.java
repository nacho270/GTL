package main.servicios.alertas.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.alertas.entidades.Alerta;
import ar.com.textillevel.modulos.alertas.entidades.AlertaFaltaStock;
import ar.com.textillevel.modulos.alertas.entidades.IVisitorAlerta;
import ar.com.textillevel.modulos.alertas.enums.EPosposicionAlerta;
import ar.com.textillevel.modulos.alertas.facade.api.remote.AlertaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

// ESTO DEBERIA MORIR
@Deprecated
public class GUIServicioAlertas extends JFrame {

	private static final long serialVersionUID = -66958124736449031L;

	private static final int CANT_COLS = 3;
	private static final int COL_ALERTA = 0;
	private static final int COL_ACCION_POSPOSICION = 1;
	private static final int COL_OBJ = 2;
	
	private static GUIServicioAlertas instance;
	
	private JButton btnAceptar;
	private FWJTable tablaAlertas;

	public static GUIServicioAlertas getInstance() {
		return getInstance(false);
	}

	public static GUIServicioAlertas getInstance(boolean create) {
		if (!create) {
			return instance;
		}
		instance = new GUIServicioAlertas();
		return instance;
	}

	private GUIServicioAlertas() {
		setUpScreen();
		setUpCompnentes();
	}

	private void setUpCompnentes() {
		JScrollPane jsp = new JScrollPane(getTablaAlertas());
		add(jsp,BorderLayout.CENTER);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		p.add(getBtnAceptar());
		add(p,BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Alertas");
		setSize(new Dimension(700,400));
		setResizable(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}

	public void addAlertas(List<Alerta> alertas) {
		getTablaAlertas().removeAllRows();
		CreadorTextoAlertaVisitor ctav = new CreadorTextoAlertaVisitor();
		for(Alerta alerta : alertas){
			alerta.accept(ctav);
			
			getTablaAlertas().addRow(new Object[]{ctav.getTextoAlerta(),EPosposicionAlerta.NADA,alerta});
		}
	}

	private class CreadorTextoAlertaVisitor implements IVisitorAlerta{

		private String textoAlerta;
		
		public void visit(AlertaFaltaStock afs) {
			textoAlerta = "Falta stock " + afs.getPrecioMateriaPrima().getAlias() + ". Stock Actual: " + GenericUtils.getDecimalFormat3().format(afs.getPrecioMateriaPrima().getStockActual().doubleValue()) + " " + afs.getPrecioMateriaPrima().getMateriaPrima().getUnidad().getDescripcion()+".";
		}
		
		public String getTextoAlerta() {
			return textoAlerta;
		}
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GUIServicioAlertas.this.setVisible(false);
				}
			});
		}
		return btnAceptar;
	}

	public FWJTable getTablaAlertas() {
		if(tablaAlertas == null){
			JComboBox cmbPosposicion = new JComboBox();
			GuiUtil.llenarCombo(cmbPosposicion, Arrays.asList(EPosposicionAlerta.values()), false);
			
			tablaAlertas = new FWJTable(0, CANT_COLS){
				private static final long serialVersionUID = 6326454265490690691L;
				
				@Override
				public void cellEdited(int column, int row) {
					if (column > -1) {
						if(column == COL_ACCION_POSPOSICION){
							Alerta alerta  = getAlerta(row);
							alerta.setFechaMinimaParaMostrar(getPosposicion(row).getFechaPosposicion(alerta.getFechaMinimaParaMostrar()));
							GTLBeanFactory.getInstance().getBean2(AlertaFacadeRemote.class).save(alerta);
						}
					}
				};
				
				public Alerta getAlerta(int row){
					return (Alerta) getValueAt(row, COL_OBJ);
				}
				
				public EPosposicionAlerta getPosposicion(int row){
					return (EPosposicionAlerta) getValueAt(row, COL_ACCION_POSPOSICION);
				}
			};
			tablaAlertas.setStringColumn(COL_ALERTA, "Alerta",500,500,true);
			tablaAlertas.setComboColumn(COL_ACCION_POSPOSICION, "Accion", cmbPosposicion, 150, false);
			tablaAlertas.setStringColumn(COL_OBJ, "", 0);
			tablaAlertas.setAllowHidingColumns(false);
			tablaAlertas.setAllowSorting(false);
			tablaAlertas.setReorderingAllowed(false);
			tablaAlertas.setHeaderAlignment(COL_ALERTA, FWJTable.CENTER_ALIGN);
			tablaAlertas.setHeaderAlignment(COL_ACCION_POSPOSICION, FWJTable.CENTER_ALIGN);
		}
		return tablaAlertas;
	}
}