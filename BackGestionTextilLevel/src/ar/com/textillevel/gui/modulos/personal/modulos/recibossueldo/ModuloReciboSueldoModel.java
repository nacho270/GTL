package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo;

import java.util.Collections;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.builders.BuilderAccionesReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.ModeloCabeceraReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.BusquedaReciboSueldoParams;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class ModuloReciboSueldoModel extends ModuloModel<InfoReciboSueltoTO, ModeloCabeceraReciboSueldo> {

	private ReciboSueldoFacadeRemote reciboSueldoFacade;
	
	public ModuloReciboSueldoModel() {
		super();
	}

	public ModuloReciboSueldoModel(Integer id) throws FWException {
		super(id, BuilderAccionesReciboSueldo.getInstance(), BuilderAccionesReciboSueldo.getInstance(), BuilderAccionesReciboSueldo.getInstance(), BuilderAccionesReciboSueldo.getInstance());
		setTitulo("Administrar Recibos de Sueldo");
	}

	@Override
	public List<InfoReciboSueltoTO> buscarItems(ModeloCabeceraReciboSueldo modeloCabecera) {
		BusquedaReciboSueldoParams params = new BusquedaReciboSueldoParams();
		params.setAnio(modeloCabecera.getAnio());
		params.setMes(modeloCabecera.getMes());
		params.setNombreOrApellido(modeloCabecera.getNombreOApellido());
		params.setNroLegajo(modeloCabecera.getNroLegajo());
		params.setQuincena(modeloCabecera.getQuincena());
		params.setSindicato(modeloCabecera.getSindicato());
		if(modeloCabecera.getSindicato() == null) {
			return Collections.emptyList();
		} else {
			return getReciboSueldoFacade().getInfoReciboSueldoTOList(params);
		}
	}

	private ReciboSueldoFacadeRemote getReciboSueldoFacade() {
		if(reciboSueldoFacade == null) {
			reciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ReciboSueldoFacadeRemote.class);
		}
		return reciboSueldoFacade;
	}

}