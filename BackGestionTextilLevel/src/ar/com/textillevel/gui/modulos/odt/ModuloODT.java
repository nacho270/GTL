package ar.com.textillevel.gui.modulos.odt;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.odt.cabecera.CabeceraODT;
import ar.com.textillevel.gui.modulos.odt.cabecera.ModeloCabeceraODT;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class ModuloODT extends ModuloTemplate<ODTTO, ModeloCabeceraODT> {

	private static final long serialVersionUID = 7002425448395339601L;

	public ModuloODT(Integer idModulo) throws FWException {
		super(idModulo);
		setTiempoRefresco(0);
		actualizar();
		pack();
		Dimension d = GenericUtils.getDimensionPantalla();
		setSize(new Dimension((int)d.getWidth()-10, (int)d.getHeight()-70));
	}

	@Override
	protected Cabecera<ModeloCabeceraODT> createCabecera() {
		return new CabeceraODT();
	}

	@Override
	protected List<ModuloModel<ODTTO, ModeloCabeceraODT>> createModulosModel() throws FWException {
		List<ModuloModel<ODTTO, ModeloCabeceraODT>> modulosModel = new ArrayList<ModuloModel<ODTTO, ModeloCabeceraODT>>();
		modulosModel.add(new ModuloODTModel(getIdModulo()));
		return modulosModel;	
	}
	
	@Override
	protected void setModuloModelActivo(ModuloModel<ODTTO, ModeloCabeceraODT> moduloModelActivo) {
		super.setModuloModelActivo(moduloModelActivo);
		FWJTable tabla = getGuiTabla().getJTable();
		tabla.getColumnModel().getColumn(4).setCellRenderer(tabla.createCheckRenderer());
	}
	
	@Override
	protected Class<?> [] listenUpdateFor(){
		Class<?> [] clases = new Class<?>[2];
		clases[0] = OrdenDeTrabajo.class;
		return clases;
	}
}
