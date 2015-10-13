package ar.com.textillevel.gui.modulos.personal.modulos.vales;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.builders.BuilderAccionesValesAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.vales.cabecera.ModeloCabeceraValesAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class ModuloValesAnticipoModel extends ModuloModel<ValeAnticipo, ModeloCabeceraValesAnticipo> {

	public ModuloValesAnticipoModel() {
		super();
	}

	public ModuloValesAnticipoModel(Integer id) throws FWException {
		super(id, BuilderAccionesValesAnticipo.getInstance(), BuilderAccionesValesAnticipo.getInstance(),BuilderAccionesValesAnticipo.getInstance());
		setTitulo("Administrar vales");
	}
	
	@Override
	public List<ValeAnticipo> buscarItems(ModeloCabeceraValesAnticipo modeloCabecera) {
		return GTLPersonalBeanFactory.getInstance().getBean2(ValeAnticipoFacadeRemote.class).buscarVales(modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta(), modeloCabecera.getApellidoEmpleado(), modeloCabecera.getEstadoVale());
	}
}
