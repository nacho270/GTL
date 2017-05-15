package main.acciones.facturacion;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.DocumentoContableFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.acciones.JDialogQuestionNumberInput;
import ar.com.textillevel.gui.acciones.remitosalida.JDialogAgregarRemitoSalida;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.GTLBeanFactory;

public class IngresoRemitoSalidaNormalHandler {

	private final Frame owner;
	private final List<OrdenDeTrabajo> odtList;
	private Cliente clienteElegido;
	

	public IngresoRemitoSalidaNormalHandler(Frame owner, Cliente clienteElegido, List<OrdenDeTrabajo> odts) {
		this.owner = owner;
		this.clienteElegido = clienteElegido;
		this.odtList = odts;
	}

	public void gestionarIngresoRemitoSalida() {
		if(!checkODTs()) {
			return;
		}
		ParametrosGeneralesFacadeRemote paramGeneralesFacadeRemote = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class);
		RemitoSalidaFacadeRemote remitoSalidaFacadeRemote = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		DocumentoContableFacadeRemote docContableFacadeRemote = GTLBeanFactory.getInstance().getBean2(DocumentoContableFacadeRemote.class);
		ParametrosGenerales parametrosGenerales = paramGeneralesFacadeRemote.getParametrosGenerales();
		Integer lastNroRemito = getProximoNroRemitoSalida(parametrosGenerales, remitoSalidaFacadeRemote);
		RemitoSalida remitoSalida = new RemitoSalida();
		remitoSalida.setNroOrden(0);
		remitoSalida.setTipoRemitoSalida(ETipoRemitoSalida.CLIENTE);
		remitoSalida.setCliente(clienteElegido);
		remitoSalida.setNroRemito(lastNroRemito);
		boolean esReproceso = false;
		if(odtList.size()==1 && (odtList.get(0).getProductoArticulo().getTipo()==ETipoProducto.REPROCESO_SIN_CARGO || odtList.get(0).getProductoArticulo().getTipo()==ETipoProducto.DEVOLUCION)) {
			esReproceso = true;
		}else{
			remitoSalida.setNroFactura(docContableFacadeRemote.getProximoNroDocumentoContable(clienteElegido.getPosicionIva(), ETipoDocumento.FACTURA));
			remitoSalida.setNroSucursal(parametrosGenerales.getNroSucursal());
		}
		remitoSalida.getOdts().addAll(odtList);
		JDialogAgregarRemitoSalida dialogAgregarRemitoSalida = new JDialogAgregarRemitoSalida(owner, remitoSalida, false);
		GuiUtil.centrar(dialogAgregarRemitoSalida);
		dialogAgregarRemitoSalida.setVisible(true);
		RemitoSalida remitoSalidaSaved = dialogAgregarRemitoSalida.getRemitoSalida();
		//logica de remito simple
		if(remitoSalidaSaved != null) {
			remitoSalidaSaved = remitoSalidaFacadeRemote.getByIdConPiezasYProductos(remitoSalidaSaved.getId());
			if (remitoSalidaSaved != null && !esReproceso) {
				JDialogQuestionNumberInput dialogQuestionNumberInput = new JDialogQuestionNumberInput(owner, "Confirmación", "¿Desea Cargar una factura?", "Cantidad de tubos:", remitoSalida.getCantidadPiezas());
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
			}  //lógica de remitos múltiples
		} else if(dialogAgregarRemitoSalida.getRemitosSalida() != null && !esReproceso) {
			List<RemitoSalida> remitosSalida = dialogAgregarRemitoSalida.getRemitosSalida();
			remitosSalida = remitoSalidaFacadeRemote.getByIdsConPiezasYProductos(extractIds(remitosSalida));
			Integer cantPiezas = 0;
			for(RemitoSalida rs : remitosSalida) {
				cantPiezas += rs.getCantidadPiezas();
			}
			JDialogQuestionNumberInput dialogQuestionNumberInput = new JDialogQuestionNumberInput(owner, "Confirmación", "¿Desea Cargar una factura?", "Cantidad de tubos:", cantPiezas);
			GuiUtil.centrar(dialogQuestionNumberInput);
			dialogQuestionNumberInput.setVisible(true);
			if(dialogQuestionNumberInput.isAcepto()){
				Integer cantTubosIngresada = dialogQuestionNumberInput.getNumberInput();
				if (cantTubosIngresada == null) {
					cantTubosIngresada=0;
				}
				RemitoSalida remitoEjemplo = remitosSalida.get(0);
				JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(owner, remitosSalida, remitoEjemplo.getNroFactura(), cantTubosIngresada, false, remitoEjemplo.getCliente());
				dialogCargaFactura.setVisible(true);
			}
		} else { //se canceló o es un reproceso
			return;
		}
	}

	private boolean checkODTs() {
		for(OrdenDeTrabajo odt : odtList) {
			if(odt.yaTuvoSalidaCompletamente()) {
				FWJOptionPane.showErrorMessage(owner, "La ODT " + odt.getCodigo() + " ya tuvo salida.", "Error");
				return false;
			}
		}
		return true;
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

	private List<Integer> extractIds(List<RemitoSalida> remitosSalida) {
		List<Integer> ids = new ArrayList<Integer>();
		for(RemitoSalida rs : remitosSalida) {
			ids.add(rs.getId());
		}
		return ids;
	}

}