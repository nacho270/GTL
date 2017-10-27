package ar.com.textillevel.gui.modulos.remitoentrada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaDibujoFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.modulos.remitoentrada.builder.BuilderAccionesRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.cabecera.ModeloCabeceraRemitoEntrada;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloRemitoEntradaModel extends ModuloModel<RemitoEntradaModuloTO, ModeloCabeceraRemitoEntrada> {

	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private RemitoEntradaDibujoFacadeRemote remitoEntradaDibujoFacade;

	public ModuloRemitoEntradaModel() {
		super();
	}

	public ModuloRemitoEntradaModel(Integer id) throws FWException {
		super(id, BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance(), BuilderAccionesRemitoEntrada.getInstance());
		setTitulo("Administrar Remitos");
	}

	@Override
	public List<RemitoEntradaModuloTO> buscarItems(ModeloCabeceraRemitoEntrada modeloCabecera) {
		if(modeloCabecera.isBuscarPorFiltros()) {
			Cliente cliente = modeloCabecera.getCliente();
			List<RemitoEntrada> rePiezas = getRemitoEntradaFacade().getRemitoEntradaByFechasAndCliente(modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta(), cliente == null ? null : cliente.getId(), modeloCabecera.getProducto(), modeloCabecera.getSituacionODT());
			List<RemitoEntradaModuloTO> reModulosTOs = fromREPiezastoReModulosTOs(rePiezas);
			reModulosTOs.addAll(fromREDibujotoReModulosTOs(getRemitoEntradaDibujoFacade().getRemitosEntradaDibujo(modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta(), cliente == null ? null : cliente.getId())));
			return reModulosTOs;
		} else {
			Integer nroRemito = modeloCabecera.getNroRemito();
			if(nroRemito == null || nroRemito == 0) {
				return Collections.emptyList();
			}
			return fromREPiezastoReModulosTOs(getRemitoEntradaFacade().getByNroRemito(nroRemito));
		}
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

	private RemitoEntradaDibujoFacadeRemote getRemitoEntradaDibujoFacade() {
		if(remitoEntradaDibujoFacade == null) {
			remitoEntradaDibujoFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaDibujoFacadeRemote.class);
		}
		return remitoEntradaDibujoFacade;
	}

	private List<RemitoEntradaModuloTO> fromREPiezastoReModulosTOs(List<RemitoEntrada> reList) {
		List<RemitoEntradaModuloTO> reModulosTOs = new ArrayList<RemitoEntradaModuloTO>(reList.size());
		for(RemitoEntrada re : reList) {
			reModulosTOs.add(new RemitoEntradaModuloTO(re));
		}
		return reModulosTOs;
	}

	private List<RemitoEntradaModuloTO> fromREDibujotoReModulosTOs(List<RemitoEntradaDibujo> reList) {
		List<RemitoEntradaModuloTO> reModulosTOs = new ArrayList<RemitoEntradaModuloTO>(reList.size());
		for(RemitoEntradaDibujo red : reList) {
			reModulosTOs.add(new RemitoEntradaModuloTO(red));
		}
		return reModulosTOs;
	}

}