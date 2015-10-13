package ar.com.textillevel.gui.modulos.odt.gui.secuencias;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.modulos.odt.gui.procedimientos.PanelTablaInstrucciones;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.PasoSecuencia;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCrearEditarPasoSecuencia extends JDialog {

	private static final long serialVersionUID = 7675171829247026014L;
	
	private static final int MAX_LONG_OBS = 100;

	private JComboBox cmbSector;
	private JComboBox cmbProceso;
	private JComboBox cmbSubproceso;
	private FWJTextField txtObservaciones;
	private PanelTablaInstruccionesSinAcciones tablaPasos;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private boolean acepto;

	private List<TipoMaquina> sectores;
	
	private PasoSecuencia pasoActual;
	private TipoMaquinaFacadeRemote tipoMaquinaFacade;

	public JDialogCrearEditarPasoSecuencia(Dialog padre) {
		super(padre);
		initCrear();
	}
	
	public JDialogCrearEditarPasoSecuencia(Frame padre) {
		super(padre);
		initCrear();
	}

	private void initCrear() {
		setPasoActual(new PasoSecuencia());
		setUpComponentes();
		setUpScreen();
	}

	public JDialogCrearEditarPasoSecuencia(Dialog padre, PasoSecuencia paso) {
		super(padre);
		initModificar(paso);
	}
	
	public JDialogCrearEditarPasoSecuencia(Frame padre, PasoSecuencia paso) {
		super(padre);
		initModificar(paso);
	}

	private void initModificar(PasoSecuencia paso) {
		setPasoActual(paso);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getCmbSector().setSelectedItem(getPasoActual().getSector());
		getCmbProceso().setSelectedItem(getPasoActual().getProceso());
		getCmbSubproceso().setSelectedItem(getPasoActual().getSubProceso());
		getTxtObservaciones().setText(getPasoActual().getObservaciones());
	}

	private void setUpScreen() {
		setTitle("Crear/Editar paso secuencia");
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(785, 250));
		GuiUtil.centrar(this);
		setResizable(false);
	}

	private void setUpComponentes() {
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentro(){
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.add(new JLabel("Sector: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbSector(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 1));
		
		panel.add(new JLabel("Proceso: "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbProceso(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 1));

		
		panel.add(new JLabel("Subproceso: "),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbSubproceso(),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 1));

		panel.add(new JLabel("Observaciones: "),GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtObservaciones(),GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.2, 1));

		panel.add(getTablaPasos(),GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 4, 1, 1));
		
		return panel;
	}

	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}
	
	public PasoSecuencia getPasoActual() {
		return pasoActual;
	}

	public void setPasoActual(PasoSecuencia pasoActual) {
		this.pasoActual = pasoActual;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public JComboBox getCmbSector() {
		if(cmbSector == null){
			cmbSector = new JComboBox();
			cmbSector.addItem(null);
			for(TipoMaquina tp : getSectores()){
				cmbSector.addItem(tp);
			}
//			GuiUtil.llenarCombo(cmbSector, getSectores(), true);
			cmbSector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED && getCmbSector().getSelectedItem()!=null){
						GuiUtil.llenarCombo(getCmbProceso(), ((TipoMaquina)getCmbSector().getSelectedItem()).getProcesos(), true);
					}else{
						getCmbProceso().removeAllItems();
						getCmbSubproceso().removeAllItems();
					}
				}
			});
		}
		return cmbSector;
	}

	public JComboBox getCmbProceso() {
		if(cmbProceso == null){
			cmbProceso = new JComboBox();
			cmbProceso.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						GuiUtil.llenarCombo(getCmbSubproceso(), ((ProcesoTipoMaquina)getCmbProceso().getSelectedItem()).getProcedimientos(), true);
					}
				}
			});
		}
		return cmbProceso;
	}

	public JComboBox getCmbSubproceso() {
		if(cmbSubproceso == null){
			cmbSubproceso = new JComboBox();
			cmbSubproceso.addItemListener(new ItemListener() {
				
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						ProcedimientoTipoArticulo pta = (ProcedimientoTipoArticulo)getCmbSubproceso().getSelectedItem();
						getTablaPasos().limpiar();
						getTablaPasos().agregarElementos(pta.getPasos());
					}
				}
			});
		}
		return cmbSubproceso;
	}

	public FWJTextField getTxtObservaciones() {
		if(txtObservaciones == null){
			txtObservaciones = new FWJTextField(MAX_LONG_OBS);
		}
		return txtObservaciones;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getCmbSector().getSelectedItem() == null){
						FWJOptionPane.showErrorMessage(JDialogCrearEditarPasoSecuencia.this, "Debe ingresar el sector", "Error");
						return;
					}
					if(getCmbProceso().getSelectedItem() == null){
						FWJOptionPane.showErrorMessage(JDialogCrearEditarPasoSecuencia.this, "Debe ingresar el proceso", "Error");
						return;
					}
					if(getCmbSubproceso().getSelectedItem() == null){
						FWJOptionPane.showErrorMessage(JDialogCrearEditarPasoSecuencia.this, "Debe ingresar el subproceso", "Error");
						return;
					}
					getPasoActual().setObservaciones(getTxtObservaciones().getText());
					getPasoActual().setSector((TipoMaquina)getCmbSector().getSelectedItem());
					getPasoActual().setProceso((ProcesoTipoMaquina)getCmbProceso().getSelectedItem());
					getPasoActual().setSubProceso((ProcedimientoTipoArticulo)getCmbSubproceso().getSelectedItem());
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	
	public TipoMaquinaFacadeRemote getTipoMaquinaFacade() {
		if(tipoMaquinaFacade == null){
			tipoMaquinaFacade = GTLBeanFactory.getInstance().getBean2(TipoMaquinaFacadeRemote.class);
		}
		return tipoMaquinaFacade;
	}

	
	public List<TipoMaquina> getSectores() {
		if(sectores == null){
			sectores = getTipoMaquinaFacade().getAllOrderByOrden(TipoMaquinaFacadeRemote.MASK_PROCESOS | TipoMaquinaFacadeRemote.MASK_SUBPROCESOS | TipoMaquinaFacadeRemote.MASK_INSTRUCCIONES);
		}
		return sectores;
	}
	
	private class PanelTablaInstruccionesSinAcciones extends PanelTablaInstrucciones{

		private static final long serialVersionUID = -9039437663399603383L;
		
		public PanelTablaInstruccionesSinAcciones(){
			getBotonAgregar().setVisible(false);
			getBotonQuitar().setVisible(false);
			getBotonModificar().setVisible(false);
			getBotonBajar().setVisible(false);
			getBotonSubir().setVisible(false);
		}
	}
	
	public PanelTablaInstruccionesSinAcciones getTablaPasos() {
		if(tablaPasos == null){
			tablaPasos = new PanelTablaInstruccionesSinAcciones();
		}
		return tablaPasos;
	}
}
