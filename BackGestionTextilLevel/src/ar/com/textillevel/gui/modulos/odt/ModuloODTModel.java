package ar.com.textillevel.gui.modulos.odt;

import java.util.Collections;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.modulos.odt.builder.BuilderAccionesODT;
import ar.com.textillevel.gui.modulos.odt.cabecera.ModeloCabeceraODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloODTModel extends ModuloModel<ODTTO, ModeloCabeceraODT> {
	
	private OrdenDeTrabajoFacadeRemote delegate;

	private OrdenDeTrabajoFacadeRemote getODTFacade() {
		if(delegate == null) {
			this.delegate = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		}
		return delegate;
	}

	public ModuloODTModel() {
		super();
	}

	public ModuloODTModel(Integer id) throws FWException {
		super(id, BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance());
		setTitulo("Administrar Órdenes de trabajo");
	}

	@Override
	public List<ODTTO> buscarItems(ModeloCabeceraODT modeloCabecera) {
		if(modeloCabecera.isBuscarPorFiltros()) {
			return getODTFacade().getAllODTTOByParams(modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta(), modeloCabecera.getCliente(), null, modeloCabecera.getProducto() == null ? null : modeloCabecera.getProducto().getId(), modeloCabecera.isConProductoParcial(), modeloCabecera.getEstadoODT());
		} else {
			String codigo = modeloCabecera.getCodigoODT();
			if(StringUtil.isNullOrEmpty(codigo)) {
				return Collections.emptyList();
			}
			OrdenDeTrabajo byCodigoEager = getODTFacade().getByCodigoEager(codigo);
			if(byCodigoEager == null) {
				return Collections.emptyList();
			}
			return Collections.singletonList(new ODTTO(byCodigoEager));
		}
	}


}