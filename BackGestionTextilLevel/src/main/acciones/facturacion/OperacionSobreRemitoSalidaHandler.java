package main.acciones.facturacion;

import java.awt.Frame;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.remitosalida.JDialogAgregarRemitoSalida;
import ar.com.textillevel.gui.acciones.remitosalida.JDialogAgregarRemitoSalidaDibujo;
import ar.com.textillevel.gui.acciones.remitosalidaventatela.JDialogAgregarRemitoSalidaVentaTela;
import ar.com.textillevel.util.GTLBeanFactory;

public class OperacionSobreRemitoSalidaHandler {

	private Frame owner;
	private RemitoSalida remitoSalida;
	private RemitoSalidaFacadeRemote remitoSalidaFacade;
	private boolean modoConsulta; 

	public OperacionSobreRemitoSalidaHandler(Frame owner, RemitoSalida remitoSalida, boolean modoConsulta) {
		this.owner = owner;
		this.remitoSalida = remitoSalida;
		this.modoConsulta = modoConsulta;
	}

	public void showRemitoEntradaDialog() {
		if(remitoSalida.getDibujoEstampados() != null && !remitoSalida.getDibujoEstampados().isEmpty()) {
			handleRemitoSalidaDibujo();
		}else if(remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA) {
			handleConsultaRemitoSalidaVentaTela();
		} else if(remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.CLIENTE_SALIDA_01) {
			handleConsultaRemitoSalida01();
		} else {
			handleConsultaRemitoSalidaNormal();
		}
	}

	private void handleRemitoSalidaDibujo() {
		remitoSalida = getRemitoSalidaFacade().getByIdConDibujo(remitoSalida.getId());
		JDialogAgregarRemitoSalidaDibujo dialogoRS = new JDialogAgregarRemitoSalidaDibujo(owner, remitoSalida, modoConsulta);
		GuiUtil.centrar(dialogoRS);
		dialogoRS.setVisible(true);		
	}

	private void handleConsultaRemitoSalidaVentaTela() {
		remitoSalida = getRemitoSalidaFacade().getByIdConPiezasYProductos(remitoSalida.getId());
		JDialogAgregarRemitoSalidaVentaTela dialogoRS = new JDialogAgregarRemitoSalidaVentaTela(owner, remitoSalida, true, modoConsulta);
		GuiUtil.centrar(dialogoRS);
		dialogoRS.setVisible(true);
	}

	private void handleConsultaRemitoSalida01() {
		remitoSalida = getRemitoSalidaFacade().getByIdConPiezasYProductos(remitoSalida.getId());
		JDialogAgregarRemitoSalidaVentaTela dialogoRS = new JDialogAgregarRemitoSalidaVentaTela(owner, remitoSalida, false, modoConsulta);
		GuiUtil.centrar(dialogoRS);
		dialogoRS.setVisible(true);
	}
	
	private void handleConsultaRemitoSalidaNormal() {
		remitoSalida = getRemitoSalidaFacade().getByIdConPiezasYProductos(remitoSalida.getId());
		JDialogAgregarRemitoSalida dialogoRS = new JDialogAgregarRemitoSalida(owner, remitoSalida, modoConsulta);
		GuiUtil.centrar(dialogoRS);
		dialogoRS.setVisible(true);
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

}