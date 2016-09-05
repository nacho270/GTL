package ar.com.textillevel.webservices.terminal.impl;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.facade.api.local.OrdenDePagoFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoSalidaFacadeLocal;
import ar.com.textillevel.webservices.terminal.api.remote.TerminalServiceRemote;

@Stateless
@WebService
public class TerminalService implements TerminalServiceRemote {

	@EJB
	private OrdenDePagoFacadeLocal odpFacade;

	@EJB
	private RemitoSalidaFacadeLocal rsFacade;

	// URL: http://localhost:8080/GTL-gtlback-server/TerminalService?wsdl

	@Override
	public TerminalServiceResponse marcarEntregado(String codigo) {
		if (StringUtil.isNullOrEmpty(codigo)) {
			return new TerminalServiceResponse(TerminalServiceError.CODIGO_NO_INGRESADO);
		}
		ETipoDocumento etd = ETipoDocumento.getByPrefijo(codigo.subSequence(0, 4).toString());
		if (etd == null) {
			return new TerminalServiceResponse(TerminalServiceError.TIPO_DOC_RECONOCIDO);
		}
		try {
			switch (etd) {
				case ORDEN_PAGO: {
					odpFacade.marcarEntregada(codigo.substring(4));
					break;
				}
				case REMITO_SALIDA: {
					rsFacade.marcarEntregado(codigo.substring(4));
					break;
				}
				default: {
					return new TerminalServiceResponse(TerminalServiceError.OPERACION_NO_PERMITIDA);
				}
			}
		}catch(RuntimeException e) {
			if (etd == ETipoDocumento.REMITO_SALIDA) {
				return new TerminalServiceResponse(TerminalServiceError.RS_NO_ENCONTRADO);
			}
			return new TerminalServiceResponse(TerminalServiceError.ODP_NO_ENCONTRADA);
		}
		return new TerminalServiceResponse();
	}

	@Override
	public TerminalServiceResponse reingresar(String codigo) {
		if (StringUtil.isNullOrEmpty(codigo)) {
			return new TerminalServiceResponse(TerminalServiceError.CODIGO_NO_INGRESADO);
		}
		ETipoDocumento etd = ETipoDocumento.getByPrefijo(codigo.subSequence(0, 4).toString());
		if (etd == null) {
			return new TerminalServiceResponse(TerminalServiceError.TIPO_DOC_RECONOCIDO);
		}
		try {
			switch (etd) {
				case ORDEN_PAGO: {
					odpFacade.reingresar(codigo.substring(4));
					break;
				}
				case REMITO_SALIDA: {
					rsFacade.reingresar(codigo.substring(4));
					break;
				}
				default: {
					return new TerminalServiceResponse(TerminalServiceError.OPERACION_NO_PERMITIDA);
				}
			}
		} catch (RuntimeException e) {
			if (etd == ETipoDocumento.REMITO_SALIDA) {
				return new TerminalServiceResponse(TerminalServiceError.RS_NO_ENCONTRADO);
			}
			return new TerminalServiceResponse(TerminalServiceError.ODP_NO_ENCONTRADA);
		}
		return new TerminalServiceResponse();
	}

	public enum TerminalServiceError {
		OPERACION_NO_PERMITIDA(1, "Operacion no permitida"), //
		ODP_NO_ENCONTRADA(2, "Orden de pago no encontrada"), //
		RS_NO_ENCONTRADO(3, "Remito de salida no encontrado"), //
		TIPO_DOC_RECONOCIDO(4, "Tipo de documento no reconocido"), //
		CODIGO_NO_INGRESADO(5, "No se ha ingresado codigo")
		;

		private TerminalServiceError(int codigo, String mensaje) {
			this.codigo = codigo;
			this.mensaje = mensaje;
		}

		private int codigo;
		private String mensaje;

		public int getCodigo() {
			return codigo;
		}

		public String getMensaje() {
			return mensaje;
		}
	}

	public static class TerminalServiceResponse implements Serializable {

		private static final long serialVersionUID = -4072902962266559090L;

		private boolean error;
		private String mensajeError;
		private int codigoError;

		public TerminalServiceResponse() {
			this.error = false;
		}

		public TerminalServiceResponse(TerminalServiceError errorData) {
			this.error = true;
			this.mensajeError = errorData.getMensaje();
			this.codigoError = errorData.getCodigo();
		}

		public boolean isError() {
			return error;
		}

		public void setError(boolean error) {
			this.error = error;
		}

		public String getMensajeError() {
			return mensajeError;
		}

		public void setMensajeError(String mensajeError) {
			this.mensajeError = mensajeError;
		}

		public int getCodigoError() {
			return codigoError;
		}

		public void setCodigoError(int codigoError) {
			this.codigoError = codigoError;
		}
	}
}
