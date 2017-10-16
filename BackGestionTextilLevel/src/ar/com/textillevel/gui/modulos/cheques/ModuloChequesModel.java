package ar.com.textillevel.gui.modulos.cheques;

import java.sql.Date;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.modulos.cheques.builders.BuilderAccionesCheque;
import ar.com.textillevel.gui.modulos.cheques.cabecera.CabeceraCheques;
import ar.com.textillevel.gui.modulos.cheques.cabecera.ModeloCabeceraCheques;
import ar.com.textillevel.util.GTLBeanFactory;

public class ModuloChequesModel extends ModuloModel<Cheque, ModeloCabeceraCheques> {

	private ChequeFacadeRemote chequeFacade;
	
	public ModuloChequesModel() {
		super();
	}

	public ModuloChequesModel(Integer id) throws FWException {
		super(id, BuilderAccionesCheque.getInstance(), BuilderAccionesCheque.getInstance(), BuilderAccionesCheque.getInstance(), BuilderAccionesCheque.getInstance(), BuilderAccionesCheque.getInstance());
		setTitulo("Administrar cheques");
	}

	@Override
	public List<Cheque> buscarItems(ModeloCabeceraCheques modeloCabecera) {
		Date fechaDesde = modeloCabecera.getFechaDesde();
		Date fechaHasta = modeloCabecera.getFechaHasta();
		if(modeloCabecera.getNroCliente()!=null){
			return getChequeFacade().getChequesPorFechaYPaginado(modeloCabecera.getNroCliente(), modeloCabecera.getEstadoCheque(),(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null),(fechaHasta!=null? new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),
					modeloCabecera.getPaginaActual(), CabeceraCheques.MAX_ROWS, modeloCabecera.getTipoFecha(), modeloCabecera.getIdBanco(), modeloCabecera.getMontoDesde(), modeloCabecera.getMontoHasta());
		}else if(modeloCabecera.getNumeracionCheque()!=null){ //SI ES POR NUMERACION, NO FILTRO POR FECHA
			return getChequeFacade().getChequesPorFechaYPaginado(modeloCabecera.getNumeracionCheque(), modeloCabecera.getEstadoCheque(), null,null, modeloCabecera.getPaginaActual(), CabeceraCheques.MAX_ROWS, modeloCabecera.getTipoFecha(), modeloCabecera.getIdBanco(), modeloCabecera.getMontoDesde(), modeloCabecera.getMontoHasta());
		}else if(modeloCabecera.getNumeroCheque()!=null){
			return getChequeFacade().getChequesPorNumeroDeCheque(modeloCabecera.getNumeroCheque(), modeloCabecera.getEstadoCheque(), null, null, modeloCabecera.getPaginaActual(), CabeceraCheques.MAX_ROWS, modeloCabecera.getTipoFecha(), modeloCabecera.getIdBanco(), modeloCabecera.getMontoDesde(), modeloCabecera.getMontoHasta());
		}else{
			String nombre = modeloCabecera.getNombreProveedor()==null?modeloCabecera.getNombrePersona():modeloCabecera.getNombreProveedor();
			return getChequeFacade().getChequesPorFechaYPaginadoPorProveedor(nombre, modeloCabecera.getEstadoCheque(),(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null),(fechaHasta!=null? new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),
					modeloCabecera.getPaginaActual(), CabeceraCheques.MAX_ROWS, modeloCabecera.getTipoFecha(), modeloCabecera.getIdBanco(), modeloCabecera.getMontoDesde(), modeloCabecera.getMontoHasta());
		}
	}

	private ChequeFacadeRemote getChequeFacade() {
		if(chequeFacade == null){
			chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		}
		return chequeFacade;
	}
}
