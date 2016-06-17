package ar.com.textillevel.gui.acciones.odt.componentes;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTablaSubirBajarModificar;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.modulos.odt.gui.JDialogVisualizarPasosSecuenciaODT;
import ar.com.textillevel.gui.modulos.odt.gui.JDialogVisualizarTransicionesODT;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public abstract class PanelTablaODT extends PanelTablaSubirBajarModificar<ODTTO> {

	private static final long serialVersionUID = -2468829266744601825L;

	private static final int CANT_COLS = 2;
	private static final int COL_ODT = 0;
	private static final int COL_OBJ = 1;

	private String encabezado;
	private EAvanceODT tipoAvance;
	
	private final Frame padre;

	public PanelTablaODT(Frame padre, String header) {
		this.padre = padre;
		setEncabezado(header);
		FWJTable nuevaTabla = createNewTabla();
		rebuildTable(nuevaTabla);
	}
	
	public PanelTablaODT(Frame padre, EAvanceODT tipoAvance, boolean ultima) {
		this.padre = padre;
		if(ultima && tipoAvance == EAvanceODT.FINALIZADO){
			setEncabezado("Oficina");
		}else{
			setEncabezado(tipoAvance.getDescripcion());
		}
		this.tipoAvance = tipoAvance;
		FWJTable nuevaTabla = createNewTabla();
		rebuildTable(nuevaTabla);
		setSize(new Dimension(500, 500));
		setPreferredSize(new Dimension(500, 500));
	}

	private FWJTable createNewTabla() {
		FWJTable nuevaTabla = new FWJTable(0, CANT_COLS); 
		nuevaTabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				habilitarBotones(getTabla().getSelectedRow(),getTipoAvance());
				//doble click => muestra dialogo de header e historia de la ODT o el de la secuencia de trabajo explotada
				if (e.getClickCount() == 2) {
					ODTTO odtto = getElemento(getTabla().getSelectedRow());
					if(odtto.getMaquinaActual() == null) {//Se muestra directamente la secuencia de trabajo de la ODT
						OrdenDeTrabajo odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odtto.getId());
						JDialogVisualizarPasosSecuenciaODT d = new JDialogVisualizarPasosSecuenciaODT(padre, odt, odt.getSecuenciaDeTrabajo().getPasos(), true);
						d.setVisible(true);
					} else {
						JDialogVisualizarTransicionesODT dialogo = new JDialogVisualizarTransicionesODT(padre, odtto.getId());
						GuiUtil.centrarEnFramePadre(dialogo);
						dialogo.setVisible(true);
					}
				}
			}
		});
		nuevaTabla.setMultilineColumn(COL_ODT, getEncabezado(), 280, true,true);
		nuevaTabla.setStringColumn(COL_OBJ, "", 0);
		nuevaTabla.setAllowHidingColumns(false);
		nuevaTabla.setReorderingAllowed(false);
		nuevaTabla.setAllowSorting(false);
		nuevaTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nuevaTabla.setHeaderAlignment(COL_ODT, FWJTable.CENTER_ALIGN);
		nuevaTabla.setAlignment(COL_ODT, FWJTable.CENTER_ALIGN);
		return nuevaTabla;
	}

	@Override
	protected FWJTable construirTabla() {
		return new FWJTable(0, CANT_COLS);
	}

	@Override
	protected void agregarElemento(ODTTO elemento) {
		getTabla().addRow(new Object[] { "<html><b>"+ODTCodigoHelper.getInstance().formatCodigo(elemento.getCodigo()) + "</b>" +
													" - CL:" + elemento.getNroCliente() + " - " + GenericUtils.getDecimalFormat().format(elemento.getTotalKilos()) + " KG - " +
											 		  GenericUtils.getDecimalFormat().format(elemento.getTotalMetros()) + " MTS" +
										 		  "<br>" + elemento.getProducto() + 
										 "</html>", 
										 elemento });
	}

	@Override
	protected ODTTO getElemento(int fila) {
		return (ODTTO) getTabla().getValueAt(fila, COL_OBJ);
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public EAvanceODT getTipoAvance() {
		return tipoAvance;
	}

	
	public void setEncabezado(String header) {
		this.encabezado = header;
	}
	
	protected abstract void habilitarBotones(int rowSelected,EAvanceODT tipoAvance);
}