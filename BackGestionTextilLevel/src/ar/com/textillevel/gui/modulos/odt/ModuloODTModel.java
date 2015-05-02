package ar.com.textillevel.gui.modulos.odt;

import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.odt.builder.BuilderAccionesODT;
import ar.com.textillevel.gui.modulos.odt.cabecera.ModeloCabeceraODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloODTModel extends ModuloModel<OrdenDeTrabajo, ModeloCabeceraODT> {

	public ModuloODTModel() {
		super();
	}

	public ModuloODTModel(Integer id) throws CLException {
		super(id, BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance());
		setTitulo("Administrar Ordenes de trabajo");
	}

	@Override
	public List<OrdenDeTrabajo> buscarItems(ModeloCabeceraODT modeloCabecera) {
		return GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getOrdenesDeTrabajo(modeloCabecera.getEstadoODT(), modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta());
	}
}
