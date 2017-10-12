package ar.com.textillevel.gui.modulos.dibujos;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.modulos.dibujos.builders.BuilderAccionesDibujo;
import ar.com.textillevel.gui.modulos.dibujos.cabecera.ModeloCabeceraDibjuos;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloDibujosModel extends ModuloModel<DibujoEstampado, ModeloCabeceraDibjuos> {

	private DibujoEstampadoFacadeRemote dibujoFacade;
	
	public ModuloDibujosModel() {
		super();
	}

	public ModuloDibujosModel(Integer id) throws FWException {
		super(id, BuilderAccionesDibujo.getInstance(), BuilderAccionesDibujo.getInstance(), BuilderAccionesDibujo.getInstance(), BuilderAccionesDibujo.getInstance());
		setTitulo("Administrar dibujos");
	}

	@Override
	public List<DibujoEstampado> buscarItems(ModeloCabeceraDibjuos modeloCabecera) {
		return getDibujoFacade().getByNroClienteYEstado(modeloCabecera.getNroCliente(), modeloCabecera.getEstadoDibujo(), modeloCabecera.isIncluir01());
	}

	private DibujoEstampadoFacadeRemote getDibujoFacade() {
		if(dibujoFacade == null){
			dibujoFacade = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		}
		return dibujoFacade;
	}
}
