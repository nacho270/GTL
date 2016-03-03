package ar.com.textillevel.gui.modulos.odt;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.odt.cabecera.CabeceraODT;
import ar.com.textillevel.gui.modulos.odt.cabecera.ModeloCabeceraODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ModuloODT extends ModuloTemplate<OrdenDeTrabajo, ModeloCabeceraODT> {

	private static final long serialVersionUID = 7002425448395339601L;

	public ModuloODT(Integer idModulo) throws FWException {
		super(idModulo);
		setTiempoRefresco(0);
		actualizar();
		pack();
	}

	@Override
	protected Cabecera<ModeloCabeceraODT> createCabecera() {
		return new CabeceraODT();
	}

	@Override
	protected List<ModuloModel<OrdenDeTrabajo, ModeloCabeceraODT>> createModulosModel() throws FWException {
		List<ModuloModel<OrdenDeTrabajo, ModeloCabeceraODT>> modulosModel = new ArrayList<ModuloModel<OrdenDeTrabajo, ModeloCabeceraODT>>();
		modulosModel.add(new ModuloODTModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected void setModuloModelActivo(ModuloModel<OrdenDeTrabajo, ModeloCabeceraODT> moduloModelActivo) {
		super.setModuloModelActivo(moduloModelActivo);
		FWJTable tabla = getGuiTabla().getJTable();
		tabla.getColumnModel().getColumn(3).setCellRenderer(tabla.createCheckRenderer());
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = OrdenDeTrabajo.class;
		return clases;
	}
}
