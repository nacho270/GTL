package ar.com.textillevel.webservices.odt.api.remote;

import javax.ejb.Remote;

@Remote
public interface RegistrarAvaceODTServiceRemote {
	public void recibir(String codigoBarras);
}
