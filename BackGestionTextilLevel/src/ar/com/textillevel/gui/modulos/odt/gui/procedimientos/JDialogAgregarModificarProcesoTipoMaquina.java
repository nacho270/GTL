package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

import java.awt.BorderLayout;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarProcesoTipoMaquina extends JDialog {

	private static final long serialVersionUID = 4231146835126750800L;

	private List<ProcedimientoTipoArticulo> procedimientosActuales;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private FWJTextField txtNombreProceso;
	private JCheckBox chkRequiereMuestra;
	private PanelTablaProcedimientos panelTabla;
	private JComboBox cmbTipoArticulo;

	private ProcesoTipoMaquina procesoActual;
	private ESectorMaquina sectorMaquina;
	private boolean acepto;

	public JDialogAgregarModificarProcesoTipoMaquina(Frame padre, ESectorMaquina sectorMaquina) {
		super(padre);
		this.sectorMaquina = sectorMaquina;
		setProcesoActual(new ProcesoTipoMaquina());
		setProcedimientosActuales(new ArrayList<ProcedimientoTipoArticulo>());
		setUpComponentes();
		setUpScreen();
	}

	public JDialogAgregarModificarProcesoTipoMaquina(Frame padre, List<ProcedimientoTipoArticulo> procedimientosActuales, ProcesoTipoMaquina procesoTipoMaquina, ESectorMaquina sectorMaquina) {
		super(padre);
		this.sectorMaquina = sectorMaquina;
		setProcesoActual(procesoTipoMaquina);
		setProcedimientosActuales(procedimientosActuales);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getTxtNombreProceso().setText(getProcesoActual().getNombre());
		getChkRequiereMuestra().setSelected(getProcesoActual().getRequiereMuestra());
		getPanelTabla().agregarElementos(getProcesoActual().getProcedimientos());
	}

	private void setUpScreen() {
		setTitle("Alta/modificación de proceso");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(400, 550));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getPanelTabla(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Nombre: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNombreProceso(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		panel.add(getChkRequiereMuestra(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));

		panel.add(new JLabel("Tipo: "),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbTipoArticulo(),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public ProcesoTipoMaquina getProcesoActual() {
		return procesoActual;
	}

	public void setProcesoActual(ProcesoTipoMaquina procesoActual) {
		this.procesoActual = procesoActual;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	private class PanelTablaProcedimientos extends PanelTabla<ProcedimientoTipoArticulo> {

		private static final long serialVersionUID = 3856748337766664259L;

		private static final int CANT_COLS = 3;
		private static final int COL_TIPO_ART = 0;
		private static final int COL_NOMBRE = 1;
		private static final int COL_OBJ = 2;
		
		private ESectorMaquina sectorMaquina;

		public PanelTablaProcedimientos(ESectorMaquina sectorMaquina) {
			this.sectorMaquina = sectorMaquina;
			agregarBotonModificar();
		}

		@Override
		protected void agregarElemento(ProcedimientoTipoArticulo elemento) {
			getTabla().addRow(new Object[]{elemento.getTipoArticulo().getNombre(),elemento.getNombre(),elemento});
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_TIPO_ART, "Tipo Artículo", 100, 100, true);
			tabla.setStringColumn(COL_NOMBRE, "Nombre", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			return tabla;
		}

		@Override
		protected ProcedimientoTipoArticulo getElemento(int fila) {
			return (ProcedimientoTipoArticulo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarProcedimientos dialog = new JDialogAgregarModificarProcedimientos(JDialogAgregarModificarProcesoTipoMaquina.this, (TipoArticulo)getCmbTipoArticulo().getSelectedItem(), sectorMaquina, procesoActual);
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getProcesoActual().getProcedimientos().add(dialog.getProcedimientoActual());
				limpiar();
				agregarElementos(getProcesoActual().getProcedimientos());
				setProcedimientosActuales(getProcesoActual().getProcedimientos());
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			getProcesoActual().getProcedimientos().remove(getTabla().getSelectedRow());
			limpiar();
			agregarElementos(getProcesoActual().getProcedimientos());
			setProcedimientosActuales(getProcesoActual().getProcedimientos());
			return false;
		}
		
		@Override
		protected void dobleClickTabla(int filaSeleccionada) {
			if(filaSeleccionada>-1){
				botonModificarPresionado(filaSeleccionada);
			}
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogAgregarModificarProcedimientos dialog = new JDialogAgregarModificarProcedimientos(JDialogAgregarModificarProcesoTipoMaquina.this,getElemento(filaSeleccionada), sectorMaquina, procesoActual);
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getProcesoActual().getProcedimientos().set(filaSeleccionada,dialog.getProcedimientoActual());
				limpiar();
				agregarElementos(getProcesoActual().getProcedimientos());
				setProcedimientosActuales(getProcesoActual().getProcedimientos());
			}
		}
	}

	private List<ProcedimientoTipoArticulo> filtrarProcedimientos(final TipoArticulo tipoArticulo) {
		List<ProcedimientoTipoArticulo> procs = new ArrayList<ProcedimientoTipoArticulo>(getProcedimientosActuales());
		CollectionUtils.filter(procs, new Predicate() {

			public boolean evaluate(Object arg0) {
				if (arg0 == null) {
					return true;
				}
				ProcedimientoTipoArticulo procedimiento = (ProcedimientoTipoArticulo) arg0;
				return procedimiento.getTipoArticulo().getId().equals(tipoArticulo.getId());
			}
		});
		return procs;
	}

	public List<ProcedimientoTipoArticulo> getProcedimientosActuales() {
		return procedimientosActuales;
	}

	public void setProcedimientosActuales(List<ProcedimientoTipoArticulo> procedimientosActuales) {
		this.procedimientosActuales = procedimientosActuales;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void capturarDatos() {
		getProcesoActual().setNombre(getTxtNombreProceso().getText().trim().toUpperCase());
		getProcesoActual().setRequiereMuestra(getChkRequiereMuestra().isSelected());
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombreProceso().getText())){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del proceso", "Error");
			getTxtNombreProceso().requestFocus();
			return false;
		}
		return true;
	}
	
	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public FWJTextField getTxtNombreProceso() {
		if(txtNombreProceso == null){
			txtNombreProceso = new FWJTextField(50);
		}
		return txtNombreProceso;
	}

	private JCheckBox getChkRequiereMuestra() {
		if(chkRequiereMuestra == null) {
			chkRequiereMuestra = new JCheckBox("Requiere muestra");
		}
		return chkRequiereMuestra;
	}

	public PanelTablaProcedimientos getPanelTabla() {
		if(panelTabla == null){
			panelTabla = new PanelTablaProcedimientos(sectorMaquina);
		}
		return panelTabla;
	}

	public JComboBox getCmbTipoArticulo() {
		if(cmbTipoArticulo == null){
			cmbTipoArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoArticulo, GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class).getAllTipoArticulos(), true);
			cmbTipoArticulo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						List<ProcedimientoTipoArticulo> filtrarProcedimientos = filtrarProcedimientos((TipoArticulo)getCmbTipoArticulo().getSelectedItem());
						getPanelTabla().limpiar();
						getPanelTabla().agregarElementos(filtrarProcedimientos);
					}
				}
			});
		}
		return cmbTipoArticulo;
	}
	
	private void salir(){
		if(FWJOptionPane.showQuestionMessage(JDialogAgregarModificarProcesoTipoMaquina.this, "Va a salir sin grabar los cambios. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION){
			setAcepto(false);
			dispose();
		}
	}
}
