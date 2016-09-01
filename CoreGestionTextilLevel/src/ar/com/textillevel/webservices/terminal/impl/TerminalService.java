package ar.com.textillevel.webservices.terminal.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

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
	public void marcarEntregado(String codigo) {
		ETipoDocumento etd = getTipoDocumento(codigo);
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
				throw new RuntimeException("Operacion no permitida");
			}
		}
	}

	@Override
	public void reingresar(String codigo) {
		ETipoDocumento etd = getTipoDocumento(codigo);
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
				throw new RuntimeException("Operacion no permitida");
			}
		}
	}

	private ETipoDocumento getTipoDocumento(String codigo) {
		ETipoDocumento etd = ETipoDocumento.getByPrefijo(codigo.subSequence(0, 4).toString());
		if (etd == null) {
			throw new RuntimeException("Documento no valido");
		}
		return etd;
	}
}
