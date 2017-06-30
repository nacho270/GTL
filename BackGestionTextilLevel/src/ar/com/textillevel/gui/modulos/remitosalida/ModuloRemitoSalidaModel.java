package ar.com.textillevel.gui.modulos.remitosalida;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.modulos.remitosalida.builder.BuilderAccionesRemitoSalida;
import ar.com.textillevel.gui.modulos.remitosalida.cabecera.ModeloCabeceraRemitoSalida;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloRemitoSalidaModel extends ModuloModel<RemitoSalida, ModeloCabeceraRemitoSalida> {

	private RemitoSalidaFacadeRemote rsFacade;

	public ModuloRemitoSalidaModel() {
		super();
	}

	public ModuloRemitoSalidaModel(Integer id) throws FWException {
		super(id, BuilderAccionesRemitoSalida.getInstance(), BuilderAccionesRemitoSalida.getInstance(), BuilderAccionesRemitoSalida.getInstance(), BuilderAccionesRemitoSalida.getInstance(), BuilderAccionesRemitoSalida.getInstance());
		setTitulo("Consultar Remitos de Salida");
	}

	@Override
	public List<RemitoSalida> buscarItems(ModeloCabeceraRemitoSalida model) {
		if(model.isBuscarPorFiltros()) {
			return getRSFacade().getRemitoSalidaByParams(model.getFechaDesde(), DateUtil.getManiana(model.getFechaHasta()), model.getCliente() == null ? null : model.getCliente().getId(), model.getProveedor() == null ? null : model.getProveedor().getId());
		} else {
			return getRSFacade().getRemitosByNroRemitoConPiezasYProductos(model.getNroRemito());
		}
	}

	private RemitoSalidaFacadeRemote getRSFacade() {
		if(rsFacade == null) {
			rsFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return rsFacade;
	}

}