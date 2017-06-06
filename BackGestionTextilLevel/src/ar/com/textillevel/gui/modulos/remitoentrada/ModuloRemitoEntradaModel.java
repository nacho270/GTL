package ar.com.textillevel.gui.modulos.remitoentrada;

import java.util.Collections;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.modulos.remitoentrada.builder.BuilderAccionesRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.cabecera.ModeloCabeceraRemitoEntrada;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloRemitoEntradaModel extends ModuloModel<RemitoEntrada, ModeloCabeceraRemitoEntrada> {

	private RemitoEntradaFacadeRemote remitoEntradaFacade;

	public ModuloRemitoEntradaModel() {
		super();
	}

	public ModuloRemitoEntradaModel(Integer id) throws FWException {
		super(id, BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance());
		setTitulo("Administrar Remitos");
	}

	@Override
	public List<RemitoEntrada> buscarItems(ModeloCabeceraRemitoEntrada modeloCabecera) {
		if(modeloCabecera.isBuscarPorFiltros()) {
			Cliente cliente = modeloCabecera.getCliente();
			return getRemitoEntradaFacade().getRemitoEntradaByFechasAndCliente(modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta(), cliente == null ? null : cliente.getId(), modeloCabecera.getProducto(), modeloCabecera.getSituacionODT());
		} else {
			Integer nroRemito = modeloCabecera.getNroRemito();
			if(nroRemito == null || nroRemito == 0) {
				return Collections.emptyList();
			}
			return getRemitoEntradaFacade().getByNroRemito(nroRemito);
		}
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

}