package ar.com.textillevel.gui.modulos.odt.gui.secuencias;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.GTLGlobalCache;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.modulos.odt.gui.JDialogVisualizarPasosSecuenciaODT;
import ar.com.textillevel.gui.modulos.odt.gui.PanCabeceraDatosODT;
import ar.com.textillevel.gui.modulos.odt.impresion.ImprimirODTHandler;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogEditarSecuenciaODT extends JDialog {

	private static final long serialVersionUID = 6656537615505953421L;

	private PanelTablaPasosSecuencia panelTablaPasos;
	private JButton btnGuardar;
	private JButton btnImprimir;
	private JButton btnVerPasos;
	private JButton btnVerTodosPasos;
	private JButton btnSalir;
	private PanCabeceraDatosODT cabecera;
	private final Frame padre;
	
	private boolean acepto;
	private OrdenDeTrabajo odt;

	public JDialogEditarSecuenciaODT(Frame owner, OrdenDeTrabajo odt) {
		super(owner);
		this.padre = owner;
		setOdt(odt);
		setUpComponentes();
		setUpScreen();
		getPanelTablaPasos().agregarElementos(odt.getSecuenciaDeTrabajo().getPasos());
	}

	private void setUpScreen() {
		setTitle("Editar Secuencia de Trabajo");
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(800, 500));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getCabecera(),BorderLayout.NORTH);
		add(getPanelTablaPasos(),BorderLayout.CENTER);
		add(crearPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel crearPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(getBtnVerPasos());
		panel.add(getBtnVerTodosPasos());
		panel.add(getBtnImprimir());
		panel.add(getBtnGuardar());
		panel.add(getBtnSalir());
		return panel;
	}
	
	private void abrirDialogoVisualizador(List<PasoSecuenciaODT> pasos){
		JDialogVisualizarPasosSecuenciaODT d = new JDialogVisualizarPasosSecuenciaODT(padre, odt, pasos, false);
		d.setVisible(true);
	}

	private class PanelTablaPasosSecuencia extends PanelTabla<PasoSecuenciaODT> {

		private static final long serialVersionUID = 191377883182551250L;

		private static final int CANT_COLS = 5;
		private static final int COL_SECTOR = 0;
		private static final int COL_PROCESO = 1;
		private static final int COL_SUBPROCESO = 2;
		private static final int COL_OBS = 3;
		private static final int COL_OBJ = 4;

		public PanelTablaPasosSecuencia() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int rowSelected = getTabla().getSelectedRow();
					getBtnVerPasos().setEnabled(rowSelected != -1);
				}
			});
			tabla.setStringColumn(COL_SECTOR, "Sector", 120, 120, true);
			tabla.setStringColumn(COL_PROCESO, "Proceso", 100, 100, true);
			tabla.setStringColumn(COL_SUBPROCESO, "Subproceso", 150, 150, true);
			tabla.setStringColumn(COL_OBS, "Observaciones", 170, 170, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setHeaderAlignment(COL_SECTOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROCESO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_SUBPROCESO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBS, FWJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			tabla.setSelectionMode(FWJTable.MULTIPLE_INTERVAL_SELECTION);
			return tabla;
		}

		@Override
		protected PasoSecuenciaODT getElemento(int fila) {
			return (PasoSecuenciaODT) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void agregarElemento(PasoSecuenciaODT elemento) {
			getTabla().addRow(new Object[] { elemento.getSector().getNombre(), elemento.getProceso().getNombre(), elemento.getSubProceso().getNombre(), elemento.getObservaciones(), elemento });
		}

		@Override
		protected void dobleClickTabla(int[] selectedRows) {
			List<PasoSecuenciaODT> pasosSeleccionados = new ArrayList<PasoSecuenciaODT>();
			for(int i : selectedRows){
				pasosSeleccionados.add(getElemento(i));
			}
			abrirDialogoVisualizador(pasosSeleccionados);
		}
	}

	public PanelTablaPasosSecuencia getPanelTablaPasos() {
		if(panelTablaPasos == null){
			panelTablaPasos = new PanelTablaPasosSecuencia();
		}
		return panelTablaPasos;
	}

	public JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_guardar.png", "ar/com/textillevel/imagenes/b_guardar_des.png");
			btnGuardar.setToolTipText("Guardar");
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnGuardar;
	}

	public JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imprimir_moderno.png", "ar/com/textillevel/imagenes/b_imprimir_moderno.png");
			btnImprimir.setToolTipText("Imprimir");
			btnImprimir.setEnabled(true);
			btnImprimir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).grabarODT(odt,GTLGlobalCache.getInstance().getUsuarioSistema());
					odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odt.getId());
					ImprimirODTHandler handler = new ImprimirODTHandler(odt,JDialogEditarSecuenciaODT.this);
					handler.imprimir();	
				}
			});
		}
		return btnImprimir;
	}

	public JButton getBtnVerPasos() {
		if (btnVerPasos == null) {
			btnVerPasos = BossEstilos.createButton("ar/com/textillevel/imagenes/b_abrir.png", "ar/com/textillevel/imagenes/b_abrir_des.png");
			btnVerPasos.setEnabled(false);
			btnVerPasos.setToolTipText("Ver instrucciones seleccionadas");
			btnVerPasos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<PasoSecuenciaODT> pasosSeleccionados = new ArrayList<PasoSecuenciaODT>();
					for(int i : getPanelTablaPasos().getTabla().getSelectedRows()){
						pasosSeleccionados.add(getPanelTablaPasos().getElemento(i));
					}
					abrirDialogoVisualizador(pasosSeleccionados);	
				}
			});
		}
		return btnVerPasos;
	}


	public JButton getBtnVerTodosPasos() {
		if(btnVerTodosPasos == null){
			btnVerTodosPasos = BossEstilos.createButton("ar/com/textillevel/imagenes/b_verificar_stock.png", "ar/com/textillevel/imagenes/b_verificar_stock_des.png");
			btnVerTodosPasos.setToolTipText("Ver todas las instrucciones");
			btnVerTodosPasos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirDialogoVisualizador(odt.getSecuenciaDeTrabajo().getPasos());
				}
			});
		}
		return btnVerTodosPasos;
	}
	
	public JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_exit.png", "ar/com/textillevel/imagenes/b_exit.png");
			btnSalir.setMnemonic(KeyEvent.VK_S);
			btnSalir.setToolTipText("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(this, "Va a salir sin guardar, esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}

	public PanCabeceraDatosODT getCabecera() {
		if (cabecera == null) {
			cabecera = new PanCabeceraDatosODT(getOdt());
		}
		return cabecera;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
	
	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}
}
