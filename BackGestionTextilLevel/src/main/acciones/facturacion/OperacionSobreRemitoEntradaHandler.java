package main.acciones.facturacion;

import java.awt.Frame;
import java.util.List;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntrada;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaCompraTela;
import ar.com.textillevel.gui.acciones.JDialogAgregarRemitoEntradaStock;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class OperacionSobreRemitoEntradaHandler {

	private Frame owner;
	private RemitoEntrada remitoEntrada;
	private OrdenDeTrabajoFacadeRemote odtFacade;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private boolean modoConsulta; 

	public OperacionSobreRemitoEntradaHandler(Frame owner, RemitoEntrada remitoEntrada, boolean modoConsulta) {
		this.owner = owner;
		this.remitoEntrada = getRemitoEntradaFacade().getByIdEager(remitoEntrada.getId());
		this.modoConsulta = modoConsulta;
	}

	public void showRemitoEntradaDialog() {
		if(remitoEntrada.getArticuloStock() == null) {
			handleConsultaRemitoEntradaNormal();
		} else if(remitoEntrada.getCliente() == null) {
			handleConsultaRemitoEntradaCompraTela();
		} else {
			handleConsultaRemitoEntrada01();
		}
	}

	private void handleConsultaRemitoEntradaCompraTela() {
		List<OrdenDeTrabajo> odtList = getOdtFacade().getOdtEagerByRemitoList(remitoEntrada.getId());
		JDialogAgregarRemitoEntradaCompraTela dialogoRemitoEntrada = new JDialogAgregarRemitoEntradaCompraTela(owner, remitoEntrada, odtList, modoConsulta);
		GuiUtil.centrar(dialogoRemitoEntrada);
		dialogoRemitoEntrada.setVisible(true);
	}

	private void handleConsultaRemitoEntradaNormal() {
		List<OrdenDeTrabajo> odtList = getOdtFacade().getOdtEagerByRemitoList(remitoEntrada.getId());
		JDialogAgregarRemitoEntrada dialogoRemitoEntrada = new JDialogAgregarRemitoEntrada(owner, remitoEntrada, odtList, modoConsulta);
		GuiUtil.centrar(dialogoRemitoEntrada);
		dialogoRemitoEntrada.setVisible(true);
	}

	private void handleConsultaRemitoEntrada01() {
		List<OrdenDeTrabajo> odtList = getOdtFacade().getOdtEagerByRemitoList(remitoEntrada.getId());
		JDialogAgregarRemitoEntradaStock dialogo = new JDialogAgregarRemitoEntradaStock(owner, remitoEntrada, odtList, modoConsulta);
		GuiUtil.centrar(dialogo);
		dialogo.setVisible(true);
	}

	private OrdenDeTrabajoFacadeRemote getOdtFacade() {
		if(odtFacade == null) {
			odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		}
		return odtFacade;
	}
	
	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(remitoEntradaFacade == null) {
			remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoEntradaFacade;
	}

}