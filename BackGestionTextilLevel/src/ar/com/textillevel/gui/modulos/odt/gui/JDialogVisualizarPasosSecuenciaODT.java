package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.procedimientos.InstruccionProcedimientoRenderer;
import ar.com.textillevel.gui.modulos.odt.gui.procedimientos.JDialogSeleccionarInstruccion;
import ar.com.textillevel.gui.modulos.odt.util.ODTDatosMostradoHelper;
import ar.com.textillevel.gui.util.panels.PanelTablaAgregarQuitarSubirBajarModificar;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.IInstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTipoProductoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;

public class JDialogVisualizarPasosSecuenciaODT  extends JDialog {

	private static final long serialVersionUID = 575689358780865650L;

	private PanCabeceraDatosODT panelCabecera;
	private PanelTablaPasosSecuencia panelTabla;
	private JButton btnAceptar;

	private boolean acepto;
	private OrdenDeTrabajo odt;

	private final Frame padre;

	private List<PasoSecuenciaODT> pasos;
	private boolean modoConsulta;
	
	public JDialogVisualizarPasosSecuenciaODT(Frame padre, OrdenDeTrabajo odt, List<PasoSecuenciaODT> pasos, boolean modoConsulta) {
		super(padre);
		this.padre = padre;
		this.modoConsulta = modoConsulta;
		setPasos(pasos);
		setOdt(odt);
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpScreen() {
		setTitle("Asignar secuencia de Trabajo");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		//setSize(GenericUtils.getDimensionPantalla());
		setSize(new Dimension(1024, 768));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCabecera(), BorderLayout.NORTH);

//		JScrollPane jsp = new JScrollPane(getPanelTabla(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		jsp.setPreferredSize(new Dimension(1024, 768));
		add(getPanelTabla(), BorderLayout.CENTER);

		JPanel panSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panSur.add(getBtnAceptar());
		add(panSur, BorderLayout.SOUTH);
	}

	private class PanelTablaPasosSecuencia extends PanelTablaAgregarQuitarSubirBajarModificar<PasoSecuenciaODT> {

		private static final long serialVersionUID = -4014976876047274988L;

		private static final int CANT_COLS = 8;
		private static final int COL_SECTOR = 0;
		private static final int COL_PROCESO = 1;
		private static final int COL_PASO = 2;
		private static final int COL_OBS = 3;
		private static final int COL_OBJ = 4;
		private static final int COL_OBJ_INST = 5;
		private static final int COL_ORDEN_PASO = 6;
		private static final int COL_ORDEN_INSTRUCCION_PASO = 7;

		private JButton btnAsignarFormula;
		private JButton btnObservaciones;
		
		public PanelTablaPasosSecuencia(){
			agregarBoton(getBtnAsignarFormula());
			agregarBoton(getBtnObservaciones());
			getBotonQuitar().setEnabled(false);
			getBotonModificar().setEnabled(false);
		}
		
		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS){

				private static final long serialVersionUID = -7020923238057433347L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
					int rowSelected = getTabla().getSelectedRow();
					habilitarBotones(rowSelected);
				}
			};
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int rowSelected = getTabla().getSelectedRow();
					getBotonSubir().setEnabled(rowSelected>0 && getElemento(rowSelected - 1) != null &&  getElemento(rowSelected) != null);
					getBotonBajar().setEnabled(rowSelected<getTabla().getRowCount()-2 && getElemento(rowSelected + 1) != null &&  getElemento(rowSelected) != null);
					habilitarBotones(rowSelected);
					boolean enable = getInstruccion(rowSelected)!=null && getInstruccion(rowSelected) instanceof InstruccionProcedimientoTipoProductoODT;
					if(rowSelected!=-1 && e.getClickCount() == 2 && enable){
						getBtnAsignarFormula().doClick();
					}
				}
			});
			tabla.setStringColumn(COL_SECTOR, "Sector", 90, 90, true);
			tabla.setStringColumn(COL_PROCESO, "Proc./Subproc.", 180, 180, true);
			tabla.setMultilineColumn(COL_PASO, "Instrucción", 400, true,true);
			tabla.setStringColumn(COL_OBS, "Observaciones", 270, 270, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setStringColumn(COL_OBJ_INST, "", 0);
			tabla.setStringColumn(COL_ORDEN_PASO, "", 0);
			tabla.setStringColumn(COL_ORDEN_INSTRUCCION_PASO, "", 0);
			tabla.setHeaderAlignment(COL_SECTOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROCESO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PASO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_OBS, FWJTable.CENTER_ALIGN);
			tabla.setSelectionMode(FWJTable.SINGLE_SELECTION);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			
			return tabla;
		}
		
		private void habilitarBotones(int rowSelected) {
			getBotonSubir().setEnabled(!modoConsulta && rowSelected>0 && getElemento(rowSelected - 1) != null &&  getElemento(rowSelected) != null);
			getBotonBajar().setEnabled(!modoConsulta && rowSelected<getTabla().getRowCount()-2 && getElemento(rowSelected + 1) != null &&  getElemento(rowSelected) != null);
			boolean enable = !modoConsulta && getInstruccion(rowSelected)!=null && getInstruccion(rowSelected) instanceof InstruccionProcedimientoTipoProductoODT;
			getBtnAsignarFormula().setEnabled(enable);
			getBotonAgregar().setEnabled(!modoConsulta && getInstruccion(rowSelected)!=null);
			getBotonQuitar().setEnabled(!modoConsulta && getInstruccion(rowSelected)!=null);
			getBotonModificar().setEnabled(!modoConsulta && getInstruccion(rowSelected)!=null);
			getBtnObservaciones().setEnabled(!modoConsulta && getInstruccion(rowSelected)!=null);
		};
		
		@Override
		protected void agregarElemento(PasoSecuenciaODT elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_SECTOR] = "<html><b>" + elemento.getSector().getNombre().toUpperCase().replace("SECTOR ", "")+ "</b></html>";
			row[COL_PROCESO] = elemento.getProceso().getNombre() + " / " + elemento.getSubProceso().getNombre();
			Iterator<InstruccionProcedimientoODT> iteratorInstrucciones = elemento.getSubProceso().getPasos().iterator();
			if(iteratorInstrucciones.hasNext()){
				int startIndex = getTabla().getRowCount() + 1;
				int ordenInstruccion = 0;
				do {
					InstruccionProcedimientoODT instruccion = iteratorInstrucciones.next();
					row[COL_PASO] = InstruccionProcedimientoRenderer.renderInstruccionASHTML(instruccion,true);
					row[COL_OBS] = instruccion.getObservaciones();
					row[COL_OBJ] = elemento;
					row[COL_OBJ_INST] = instruccion;
					row[COL_ORDEN_PASO] = String.valueOf(getPasos().indexOf(elemento));
					row[COL_ORDEN_INSTRUCCION_PASO] = String.valueOf(ordenInstruccion);
					ordenInstruccion++;
					getTabla().addRow(row);
					row = new Object[CANT_COLS];
				} while (iteratorInstrucciones.hasNext());
				int endIndex = getTabla().getRowCount();
				
				if(elemento.getProceso().getRequiereMuestra() != null && elemento.getProceso().getRequiereMuestra().equals(Boolean.TRUE)){
					getTabla().agruparCeldas(startIndex, COL_SECTOR, endIndex-startIndex, COL_OBS-COL_PROCESO);
				}
				
				for(int i = startIndex;i<endIndex;i++){
					getTabla().setBackgroundCell(i, COL_SECTOR, Color.LIGHT_GRAY);
					getTabla().setBackgroundCell(i, COL_PROCESO, Color.LIGHT_GRAY);
				}
				
				getTabla().addRow(new Object[]{" "," "," "," ","<html><br>  <br></html>",null,null,null,null});
				getTabla().setBackgroundRow(getTabla().getRowCount()-1, Color.CYAN);
			}
		}

		@Override
		protected PasoSecuenciaODT getElemento(int fila) {
			if(fila != -1){
				if(getTabla().getValueAt(fila, COL_OBJ)!=null && !(getTabla().getValueAt(fila, COL_OBJ) instanceof String)){
					return (PasoSecuenciaODT) getTabla().getValueAt(fila, COL_OBJ);
				}
			}
			return null;
		}
		
		protected InstruccionProcedimientoODT getInstruccion(int fila){
			if(fila!=-1){
				if(getTabla().getValueAt(fila, COL_OBJ_INST)!=null){
					return (InstruccionProcedimientoODT)getTabla().getValueAt(fila, COL_OBJ_INST);
				}
			}
			return null;
		}
		
		protected int getOrdenPaso(int fila){
			if(getTabla().getValueAt(fila, COL_ORDEN_PASO)!=null){
				return Integer.valueOf((String)getTabla().getValueAt(fila, COL_ORDEN_PASO)).intValue();
			}
			return -1;
		}
		
		protected int getOrdenInstruccion(int fila){
			if(getTabla().getValueAt(fila, COL_ORDEN_INSTRUCCION_PASO)!=null){
				return Integer.valueOf((String)getTabla().getValueAt(fila, COL_ORDEN_INSTRUCCION_PASO)).intValue();
			}
			return -1;
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			int selectedRow = getTabla().getSelectedRow();
			PasoSecuenciaODT paso = null;
			if(selectedRow == -1){
				paso = getElemento(getTabla().getRowCount()-2);
			}else{
				paso = getElemento(selectedRow);
			}
			JDialogSeleccionarInstruccion dialog = new JDialogSeleccionarInstruccion(JDialogVisualizarPasosSecuenciaODT.this, getOdt().getProductoArticulo().getArticulo().getTipoArticulo(),paso.getSector().getSectorMaquina(), paso.getProceso());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				InstruccionProcedimiento instruccion = dialog.getInstruccionFinal(); 
				ExplotadorInstrucciones explotador = new ExplotadorInstrucciones(getOdt());
				instruccion.accept(explotador);
				PasoSecuenciaODT pasoAAgregarleInstruccion = null;
				if(selectedRow == -1){
					pasoAAgregarleInstruccion = getPasos().get(getOrdenPaso(getTabla().getRowCount()-2));
				}else{
					pasoAAgregarleInstruccion = getPasos().get(getOrdenPaso(getTabla().getSelectedRow()));
					
				}
				if(selectedRow == -1){
					pasoAAgregarleInstruccion.getSubProceso().getPasos().add(explotador.getInstruccionExplotada());
				}else{
					pasoAAgregarleInstruccion.getSubProceso().getPasos().add(getOrdenInstruccion(selectedRow)+1, explotador.getInstruccionExplotada());
				}
				refreshTable();
				if(selectedRow != -1 && getElemento(selectedRow +1) != null){
					getTabla().setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
				}
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow>-1){
				PasoSecuenciaODT pasoAQuitarleInstruccion = getPasos().get(getOrdenPaso(selectedRow));
				pasoAQuitarleInstruccion.getSubProceso().getPasos().remove(getOrdenInstruccion(selectedRow));
				getPasos().set(getOrdenPaso(selectedRow), pasoAQuitarleInstruccion);
				refreshTable();
				if(selectedRow - 1 < 0){
					if(getTabla().getRowCount()>0){
						getTabla().setRowSelectionInterval(0, 0);
					}
				}else{
					getTabla().setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
				}
			}
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			PasoSecuenciaODT paso = getElemento(getTabla().getSelectedRow());
			InstruccionProcedimientoODT instruccionODT = getInstruccion(filaSeleccionada);
			InstruccionProcedimiento instruccionProcedimiento = instruccionODT.toInstruccionProcedimiento();
			JDialogSeleccionarInstruccion dialog = new JDialogSeleccionarInstruccion(JDialogVisualizarPasosSecuenciaODT.this, getOdt().getProductoArticulo().getArticulo().getTipoArticulo(),instruccionProcedimiento, paso.getSector().getSectorMaquina(), paso.getProceso());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				IInstruccionProcedimiento instruccion = dialog.getInstruccionFinal(); 
				ExplotadorInstrucciones explotador = new ExplotadorInstrucciones(getOdt());
				((InstruccionProcedimiento)instruccion).accept(explotador);
				PasoSecuenciaODT pasoAAgregarleInstruccion = getPasos().get(getOrdenPaso(getTabla().getSelectedRow()));
				pasoAAgregarleInstruccion.getSubProceso().getPasos().set(getOrdenInstruccion(filaSeleccionada),explotador.getInstruccionExplotada());
				refreshTable();
			}
		}

		@Override
		protected void botonSubirPresionado() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow > 0 && selectedRow<getTabla().getRowCount()){
				PasoSecuenciaODT pasoAAgregarleInstruccion = getPasos().get(getOrdenPaso(selectedRow));
				InstruccionProcedimientoODT instruccionABajar = pasoAAgregarleInstruccion.getSubProceso().getPasos().get(selectedRow-1);
				InstruccionProcedimientoODT instruccionASubir = pasoAAgregarleInstruccion.getSubProceso().getPasos().get(selectedRow);
				pasoAAgregarleInstruccion.getSubProceso().getPasos().set(getOrdenInstruccion(selectedRow),instruccionABajar);
				pasoAAgregarleInstruccion.getSubProceso().getPasos().set(getOrdenInstruccion(selectedRow)-1, instruccionASubir);
				refreshTable();
				getTabla().setRowSelectionInterval(selectedRow - 1 , selectedRow - 1);
				getBotonSubir().setEnabled(selectedRow - 1 >0);
				getBotonBajar().setEnabled(true);
			}
		}
		
		@Override
		protected void botonBajarPresionado() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow<getTabla().getRowCount()){
				PasoSecuenciaODT pasoAAgregarleInstruccion = getPasos().get(getOrdenPaso(selectedRow));
				InstruccionProcedimientoODT instruccionABajar = pasoAAgregarleInstruccion.getSubProceso().getPasos().get(selectedRow);
				InstruccionProcedimientoODT instruccionASubir = pasoAAgregarleInstruccion.getSubProceso().getPasos().get(selectedRow+1);
				pasoAAgregarleInstruccion.getSubProceso().getPasos().set(getOrdenInstruccion(selectedRow),instruccionASubir);
				pasoAAgregarleInstruccion.getSubProceso().getPasos().set(getOrdenInstruccion(selectedRow)+1, instruccionABajar);
				refreshTable();
				getTabla().setRowSelectionInterval(selectedRow + 1 , selectedRow + 1);
				getBotonBajar().setEnabled(getElemento(selectedRow + 2 ) != null);
				getBotonSubir().setEnabled(true);
			}
		}
		
		public JButton getBtnAsignarFormula() {
			if(btnAsignarFormula == null){
				btnAsignarFormula =  BossEstilos.createButton("ar/com/textillevel/imagenes/b_formula.png", "ar/com/textillevel/imagenes/b_formula_des.png");
				btnAsignarFormula.setEnabled(false);
				btnAsignarFormula.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InstruccionProcedimientoTipoProductoODT instruccionProcedimientoTipoProductoODT = (InstruccionProcedimientoTipoProductoODT)getInstruccion(getTabla().getSelectedRow());
						TipoArticulo tipoArticulo = instruccionProcedimientoTipoProductoODT.getTipoArticulo();
						ODTDatosMostradoHelper odtHelper = new ODTDatosMostradoHelper(odt);
						JDialogSeleccionarFormula d = new JDialogSeleccionarFormula(JDialogVisualizarPasosSecuenciaODT.this.padre, getOdt().getRemito().getCliente(), odtHelper.getColor(), tipoArticulo);
						d.setVisible(true);
						if(d.isAcepto()){
							//seteo la formula en la instruccion seleccionada
							instruccionProcedimientoTipoProductoODT.explotarFormula(getOdt(), d.getFormulaElegida());
							PasoSecuenciaODT pasoAModificar = getPasos().get(getOrdenPaso(getTabla().getSelectedRow()));
							InstruccionProcedimientoTipoProductoODT instruccionProcedimientoODT = (InstruccionProcedimientoTipoProductoODT)pasoAModificar.getSubProceso().getPasos().get(getOrdenInstruccion(getTabla().getSelectedRow()));
							instruccionProcedimientoODT.setFormula(instruccionProcedimientoTipoProductoODT.getFormula());
							pasoAModificar.getSubProceso().getPasos().set(getOrdenInstruccion(getTabla().getSelectedRow()), instruccionProcedimientoODT);
							refreshTable();
						}
					}
				});
			}
			return btnAsignarFormula;
		}

		public JButton getBtnObservaciones() {
			if(btnObservaciones == null){
				btnObservaciones = BossEstilos.createButton("ar/com/textillevel/imagenes/b_nota.png", "ar/com/textillevel/imagenes/b_nota_des.png");
				btnObservaciones.setEnabled(false);
				btnObservaciones.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InstruccionProcedimientoODT instruccion = getInstruccion(getTabla().getSelectedRow());
						String observaciones = JOptionPane.showInputDialog(JDialogVisualizarPasosSecuenciaODT.this, "Observaciones", instruccion.getObservaciones());
						if(observaciones!=null){
							instruccion.setObservaciones(observaciones);
							getTabla().setValueAt(observaciones, getTabla().getSelectedRow(), COL_OBS);
						}
					}
				});
			}
			return btnObservaciones;
		}

	}

	public boolean isAcepto() {
		return acepto;
	}

	private void refreshTable() {
		getPanelTabla().limpiar();
		getPanelTabla().getTabla().desagruparCeldas();
		getPanelTabla().agregarElementos(getPasos());
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public PanCabeceraDatosODT getPanelCabecera() {
		if (panelCabecera == null) {
			panelCabecera = new PanCabeceraDatosODT(getOdt());
		}
		return panelCabecera;
	}

	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	public PanelTablaPasosSecuencia getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaPasosSecuencia();
			panelTabla.setModoConsulta(modoConsulta);
			panelTabla.agregarElementos(getPasos());
			panelTabla.getBtnAsignarFormula().setEnabled(false);
		}
		return panelTabla;
	}
	
	public List<PasoSecuenciaODT> getPasos() {
		return pasos;
	}

	public void setPasos(List<PasoSecuenciaODT> pasos) {
		this.pasos = pasos;
	}
}
