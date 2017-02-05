package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.TerminalServiceError;
import ar.com.textillevel.entidades.to.TerminalServiceResponse;
import ar.com.textillevel.facade.api.local.EntregaReingresoDocumentosFacadeLocal;
import ar.com.textillevel.facade.api.local.OrdenDePagoFacadeLocal;
import ar.com.textillevel.facade.api.local.OrdenDePagoPersonaFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoSalidaFacadeLocal;
import ar.com.textillevel.facade.api.remote.EntregaReingresoDocumentosFacadeRemote;
import ar.com.textillevel.util.GestorTerminalBarcode;

@Stateless
public class EntregaReingresoDocumentosFacade implements EntregaReingresoDocumentosFacadeRemote, EntregaReingresoDocumentosFacadeLocal {

	@EJB
	private OrdenDePagoFacadeLocal odpFacade;

	@EJB
	private RemitoSalidaFacadeLocal rsFacade;

	@EJB
	private OrdenDePagoPersonaFacadeLocal odpPersonaFacade;

	// URL: http://localhost:8080/GTL-gtlback-server/TerminalService?wsdl

	public TerminalServiceResponse marcarEntregado(String codigo, String nombreTerminal) {
		if (StringUtil.isNullOrEmpty(codigo)) {
			return new TerminalServiceResponse(TerminalServiceError.CODIGO_NO_INGRESADO);
		}
		ETipoDocumento etd = GestorTerminalBarcode.getTipoDocumento(codigo);
		if (etd == null) {
			return new TerminalServiceResponse(TerminalServiceError.TIPO_DOC_RECONOCIDO);
		}
		try {
			switch (etd) {
				case ORDEN_PAGO: {
					odpFacade.marcarEntregada(GestorTerminalBarcode.extraer(codigo), nombreTerminal);
					break;
				}
				case REMITO_SALIDA: {
					rsFacade.marcarEntregado(GestorTerminalBarcode.extraer(codigo), nombreTerminal);
					break;
				}
				case ORDEN_PAGO_PERSONA: {
					odpPersonaFacade.marcarEntregada(GestorTerminalBarcode.extraer(codigo), nombreTerminal);
				}
				default: {
					return new TerminalServiceResponse(TerminalServiceError.OPERACION_NO_PERMITIDA);
				}
			}
		} catch (RuntimeException e) {
			if (etd == ETipoDocumento.REMITO_SALIDA) {
				return new TerminalServiceResponse(TerminalServiceError.RS_NO_ENCONTRADO);
			}
			// ODP comun y persona
			return new TerminalServiceResponse(TerminalServiceError.ODP_NO_ENCONTRADA);
		}
		return new TerminalServiceResponse();
	}

	public TerminalServiceResponse reingresar(String codigo, String nombreTerminal) {
		if (StringUtil.isNullOrEmpty(codigo)) {
			return new TerminalServiceResponse(TerminalServiceError.CODIGO_NO_INGRESADO);
		}
		ETipoDocumento etd = GestorTerminalBarcode.getTipoDocumento(codigo);
		if (etd == null) {
			return new TerminalServiceResponse(TerminalServiceError.TIPO_DOC_RECONOCIDO);
		}
		try {
			switch (etd) {
				case ORDEN_PAGO: {
					odpFacade.reingresar(GestorTerminalBarcode.extraer(codigo), nombreTerminal);
					break;
				}
				case REMITO_SALIDA: {
					rsFacade.reingresar(GestorTerminalBarcode.extraer(codigo), nombreTerminal);
					break;
				}
				case ORDEN_PAGO_PERSONA: {
					odpPersonaFacade.reingresar(GestorTerminalBarcode.extraer(codigo), nombreTerminal);
				}
				default: {
					return new TerminalServiceResponse(TerminalServiceError.OPERACION_NO_PERMITIDA);
				}
			}
		} catch (RuntimeException e) {
			if (etd == ETipoDocumento.REMITO_SALIDA) {
				return new TerminalServiceResponse(TerminalServiceError.RS_NO_ENCONTRADO);
			}
			// ODP comun y persona
			return new TerminalServiceResponse(TerminalServiceError.ODP_NO_ENCONTRADA);
		}
		return new TerminalServiceResponse();
	}
}
