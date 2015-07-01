package main.acciones.facturacion;

import java.awt.Frame;
import java.util.Collections;
import java.util.List;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.acciones.JDialogQuestionNumberInput;
import ar.com.textillevel.gui.acciones.remitosalidaventatela.JDialogAgregarRemitoSalidaVentaTela;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.util.GTLBeanFactory;

public class IngresoRemitoSalidaHandler {

	private final Frame owner;
	private final ETipoRemitoSalida tipoRemitoSalida;
	private final List<DetallePiezaFisicaTO> detallePiezas;
	private final boolean modoConsulta;

	public IngresoRemitoSalidaHandler(Frame owner, ETipoRemitoSalida tipoRemitoSalida, boolean modoConsulta, List<DetallePiezaFisicaTO> detallePiezas) {
		this.owner = owner;
		this.tipoRemitoSalida = tipoRemitoSalida;
		this.modoConsulta = modoConsulta;
		this.detallePiezas = detallePiezas;
	}

	public RemitoSalida gestionarIngresoRemitoSalida() {
		ParametrosGeneralesFacadeRemote paramGeneralesFacadeRemote = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		ParametrosGenerales parametrosGenerales = paramGeneralesFacadeRemote.getParametrosGenerales();
		JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(owner);
		GuiUtil.centrar(dialogSeleccionarCliente);
		dialogSeleccionarCliente.setVisible(true);
		Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
		if (clienteElegido == null) {
			return null;
		} else {
			RemitoSalida remitoSalida = agregarRemitoCliente(parametrosGenerales, clienteElegido);
			return remitoSalida;
		}
		
	}

	public RemitoSalida agregarRemitoCliente(ParametrosGenerales parametrosGenerales, Cliente clienteElegido) {
		RemitoSalidaFacadeRemote remitoSalidaFacadeRemote = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		DocumentoContableFacadeRemote docContableFacadeRemote = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class);
		Integer lastNroRemito = getProximoNroRemitoSalida(parametrosGenerales, remitoSalidaFacadeRemote);
		RemitoSalida remitoSalida = new RemitoSalida();
		remitoSalida.setCliente(clienteElegido);
		remitoSalida.setNroRemito(lastNroRemito);
		remitoSalida.setNroOrden(0);
		remitoSalida.setNroFactura(docContableFacadeRemote.getProximoNroDocumentoContable(clienteElegido.getPosicionIva(), ETipoDocumento.FACTURA));
		remitoSalida.setTipoRemitoSalida(tipoRemitoSalida);
		JDialogAgregarRemitoSalidaVentaTela dialogAgregarRemitoSalida = createDialog(remitoSalida);
		GuiUtil.centrar(dialogAgregarRemitoSalida);
		dialogAgregarRemitoSalida.setVisible(true);
		RemitoSalida remitoSalidaSaved = dialogAgregarRemitoSalida.getRemitoSalida();
		if(remitoSalidaSaved == null) {
			return null;
		}
		remitoSalidaSaved = remitoSalidaFacadeRemote.getByIdConPiezasYProductos(remitoSalidaSaved.getId());
		if (remitoSalidaSaved != null) {
			JDialogQuestionNumberInput dialogQuestionNumberInput = new JDialogQuestionNumberInput(owner, "Confirmación", "¿Desea Cargar una factura?", "Cantidad de tubos:", remitoSalidaSaved.getCantidadPiezas());
			GuiUtil.centrar(dialogQuestionNumberInput);
			dialogQuestionNumberInput.setVisible(true);
			if(dialogQuestionNumberInput.isAcepto()){
				Integer cantTubosIngresada = dialogQuestionNumberInput.getNumberInput();
				if (cantTubosIngresada == null) {
					cantTubosIngresada=0;
				}
				JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(owner, Collections.singletonList(remitoSalidaSaved), remitoSalidaSaved.getNroFactura(), cantTubosIngresada, false,remitoSalidaSaved.getCliente());
				dialogCargaFactura.setVisible(true);
			}
		}
		return remitoSalida;
	}

	private JDialogAgregarRemitoSalidaVentaTela createDialog(RemitoSalida remitoSalida) {
		JDialogAgregarRemitoSalidaVentaTela dialogo = null;
		if(detallePiezas == null) {
			dialogo = new JDialogAgregarRemitoSalidaVentaTela(owner, remitoSalida, isVentaTela(), modoConsulta);
		} else {
			dialogo = new JDialogAgregarRemitoSalidaVentaTela(owner, remitoSalida, isVentaTela(), detallePiezas);
		}
		return dialogo;
	}

	private Integer getProximoNroRemitoSalida(ParametrosGenerales parametrosGenerales, RemitoSalidaFacadeRemote remitoSalidaFacadeRemote) {
		Integer lastNroRemito = remitoSalidaFacadeRemote.getLastNroRemito();
		if (lastNroRemito == null) {
			if (parametrosGenerales.getNroComienzoRemito() == null) {
				throw new RuntimeException("Falta configurar el número de comienzo de remito en los parámetros generales.");
			}
			lastNroRemito = parametrosGenerales.getNroComienzoRemito();
		} else {
			lastNroRemito++;
		}
		return lastNroRemito;
	}

	private boolean isVentaTela() {
		return tipoRemitoSalida == ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA;
	}

}