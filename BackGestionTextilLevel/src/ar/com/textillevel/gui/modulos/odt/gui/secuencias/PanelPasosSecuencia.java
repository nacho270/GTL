package ar.com.textillevel.gui.modulos.odt.gui.secuencias;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.DobleClickPasoSecuenciaEventData;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.DobleClickPasoSecuenciaEventListener;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanelTablaAgregarQuitarSubirBajarModificar;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.PasoSecuencia;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

public class PanelPasosSecuencia extends JPanel{

	private static final long serialVersionUID = -3223999406812802125L;

	private final EventListenerList listeners = new EventListenerList();
	
	private static final int MAX_LONGITUD_NOMBRE = 50;
	private FWJTextField txtNombre;
	private PanelTablaPasosSecuencia panelTabla;
	
	private boolean edicion;
	
	private Frame framePadre;
	private Dialog dialogPadre;
	
	private SecuenciaTipoProducto secuenciaActual;

	public PanelPasosSecuencia(Frame padre,SecuenciaTipoProducto secuenciaTipoProducto, boolean modoEdicion){
		this.framePadre = padre;
		init(modoEdicion,secuenciaTipoProducto);
	}

	private void init(boolean modoEdicion,SecuenciaTipoProducto secuenciaTipoProducto) {
		this.edicion = modoEdicion;
		this.secuenciaActual = secuenciaTipoProducto;
		setBorder(BorderFactory.createTitledBorder("Pasos"));
		setLayout(new GridBagLayout());
		add(new JLabel("Nombre secuencia: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getTxtNombre(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		add(getPanelTabla(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 2, 1, 1, 1));
		loadData();
	}
	
	public void loadData() {
		if(getSecuenciaActual()!=null){
			getTxtNombre().setText(getSecuenciaActual().getNombre());
			refreshTable();
		}
	}
	
	public void addDobleClickSecuenciaEventListener(DobleClickPasoSecuenciaEventListener listener){
		listeners.add(DobleClickPasoSecuenciaEventListener.class, listener);
	}

	public PanelPasosSecuencia(Dialog padre, SecuenciaTipoProducto secuenciaTipoProducto, boolean modoEdicion){
		this.dialogPadre = padre;
		init(modoEdicion,secuenciaTipoProducto);
	}
	
	private void dobleClickPasoSecuencia(PasoSecuencia paso){
		final DobleClickPasoSecuenciaEventData evtData = new DobleClickPasoSecuenciaEventData(paso);
		final DobleClickPasoSecuenciaEventListener[] list = listeners.getListeners(DobleClickPasoSecuenciaEventListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < list.length; i++) {
					list[i].dobleClickPasoSecuencia(evtData);
				}
			}
		});
	}
	
	private class PanelTablaPasosSecuencia extends PanelTablaAgregarQuitarSubirBajarModificar<PasoSecuencia>{

		private static final long serialVersionUID = 191377883182551250L;
		
		private static final int CANT_COLS = 5;
		private static final int COL_SECTOR = 0;
		private static final int COL_PROCESO = 1;
		private static final int COL_SUBPROCESO = 2;
		private static final int COL_OBS = 3;
		private static final int COL_OBJ = 4;

		public PanelTablaPasosSecuencia(boolean edicion) {
			getBotonBajar().setVisible(edicion);
			getBotonSubir().setVisible(edicion);
			getBotonModificar().setVisible(edicion);
			getBotonAgregar().setVisible(edicion);
			getBotonQuitar().setVisible(edicion);
		}
		
		@Override
		protected void dobleClickTabla(int filaSeleccionada) {
			dobleClickPasoSecuencia(getElemento(filaSeleccionada));
		}
		
		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int rowSelected = getTabla().getSelectedRow();
					getBotonSubir().setEnabled(rowSelected>0);
					getBotonBajar().setEnabled(rowSelected<getTabla().getRowCount()-1);
				}
			});
			tabla.setStringColumn(COL_SECTOR, "Sector", 120, 120, true);
			tabla.setStringColumn(COL_PROCESO,"Proceso",100, 100, true);
			tabla.setStringColumn(COL_SUBPROCESO,"Subproceso",150, 150, true);
			tabla.setStringColumn(COL_OBS,"Observaciones",170, 170, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setHeaderAlignment(COL_SECTOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROCESO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_SUBPROCESO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBS, FWJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected PasoSecuencia getElemento(int fila) {
			return (PasoSecuencia)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void agregarElemento(PasoSecuencia elemento) {
			getTabla().addRow(new Object[]{elemento.getSector().getNombre(),elemento.getProceso().getNombre(),elemento.getSubProceso().getNombre(),elemento.getObservaciones(),elemento});
		}
		
		@Override
		protected boolean validarAgregar() {
			Frame fp = PanelPasosSecuencia.this.framePadre;
			Dialog dp = PanelPasosSecuencia.this.dialogPadre;
			JDialogCrearEditarPasoSecuencia dialog = null;
			if(fp == null) {
				dialog = new JDialogCrearEditarPasoSecuencia(dp);
			}else{
				dialog = new JDialogCrearEditarPasoSecuencia(fp);
			}
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getSecuenciaActual().getPasos().add(dialog.getPasoActual());
				refreshTable();
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			getSecuenciaActual().getPasos().remove(filaSeleccionada);
			refreshTable();
			return false;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			Frame fp = PanelPasosSecuencia.this.framePadre;
			Dialog dp = PanelPasosSecuencia.this.dialogPadre;
			JDialogCrearEditarPasoSecuencia dialog = null;
			if(fp == null) {
				dialog = new JDialogCrearEditarPasoSecuencia(dp,getElemento(filaSeleccionada));
			}else{
				dialog = new JDialogCrearEditarPasoSecuencia(fp,getElemento(filaSeleccionada));
			}
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getSecuenciaActual().getPasos().set(filaSeleccionada,dialog.getPasoActual());
				refreshTable();
			}
		}
		
		@Override
		protected void botonSubirPresionado() {
			int filaSeleccionada = getTabla().getSelectedRow();
			if(filaSeleccionada>0){
				PasoSecuencia pasoASubir = getElemento(filaSeleccionada);
				PasoSecuencia pasoABajar = getElemento(filaSeleccionada-1);
				getSecuenciaActual().getPasos().set(filaSeleccionada, pasoABajar);
				getSecuenciaActual().getPasos().set(filaSeleccionada-1, pasoASubir);
				refreshTable();
			}
		}
		
		@Override
		protected void botonBajarPresionado() {
			int filaSeleccionada = getTabla().getSelectedRow();
			if(filaSeleccionada<getTabla().getRowCount()){
				PasoSecuencia pasoABajar = getElemento(filaSeleccionada);
				PasoSecuencia pasoASubir = getElemento(filaSeleccionada+1);
				getSecuenciaActual().getPasos().set(filaSeleccionada,pasoASubir);
				getSecuenciaActual().getPasos().set(filaSeleccionada+1, pasoABajar);
				refreshTable();
			}
		}
	}

	public FWJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
			txtNombre.setEditable(edicion);
			txtNombre.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(getSecuenciaActual()!=null){
						getSecuenciaActual().setNombre(getTxtNombre().getText().trim());
					}
				}
			});
		}
		return txtNombre;
	}
	
	public void refreshTable() {
		getPanelTabla().limpiar();
		getPanelTabla().agregarElementos(getSecuenciaActual().getPasos());
	}

	public PanelTablaPasosSecuencia getPanelTabla() {
		if(panelTabla == null){
			panelTabla = new PanelTablaPasosSecuencia(edicion);
			panelTabla.setPreferredSize(new Dimension(200, 200));
			panelTabla.setSize(new Dimension(200,200));
		}
		return panelTabla;
	}

	public void limpiar() {
		getPanelTabla().limpiar();
		getTxtNombre().setText("");
	}
	
	public SecuenciaTipoProducto getSecuenciaActual() {
		return secuenciaActual;
	}
	
	public boolean validar(){
		if(StringUtil.isNullOrEmpty(getTxtNombre().getText())){
			FWJOptionPane.showErrorMessage(dialogPadre==null?framePadre:dialogPadre,"Debe ingresar el nombre de la secuencia", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getSecuenciaActual().getPasos().size()==0){
			FWJOptionPane.showErrorMessage(dialogPadre==null?framePadre:dialogPadre,"Debe ingresar al menos un paso para la secuencia", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		return true;
	}

	public void setSecuenciaActual(SecuenciaTipoProducto secuenciaActual) {
		this.secuenciaActual = secuenciaActual;
	}
}
