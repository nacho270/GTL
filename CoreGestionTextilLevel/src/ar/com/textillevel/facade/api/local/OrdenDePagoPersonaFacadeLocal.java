package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

@Local
public interface OrdenDePagoPersonaFacadeLocal {

	void reingresar(String numero, String nombreTerminal);
	void marcarEntregada(String numero, String nombreTerminal);
}
