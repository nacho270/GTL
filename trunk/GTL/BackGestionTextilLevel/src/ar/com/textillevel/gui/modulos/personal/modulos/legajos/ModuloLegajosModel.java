package ar.com.textillevel.gui.modulos.personal.modulos.legajos;

import java.util.ArrayList;
import java.util.List;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.builders.BuilderAccionesLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.cabecera.ModeloCabeceraLegajos;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class ModuloLegajosModel extends ModuloModel<Empleado, ModeloCabeceraLegajos>{

	public ModuloLegajosModel() {
		super();
	}

	public ModuloLegajosModel(Integer id) throws CLException {
		super(id, BuilderAccionesLegajo.getInstance(), BuilderAccionesLegajo.getInstance(),BuilderAccionesLegajo.getInstance(), BuilderAccionesLegajo.getInstance());
		setTitulo("Administrar legajos");
	}
	
	@Override
	public List<Empleado> buscarItems(ModeloCabeceraLegajos modeloCabecera) {
		List<Empleado> empleados = GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class).buscarEmpleados(modeloCabecera.getNroLegajo(), modeloCabecera.getModoBusqueda(), modeloCabecera.getSindicato(),modeloCabecera.getNombreOApellido(), GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin(),modeloCabecera.getTipoContrato());
		return empleados==null?new ArrayList<Empleado>():empleados;
	}
}
