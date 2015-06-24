package ar.com.textillevel.modulos.fe.facade.api.remote;

import javax.ejb.Remote;

@Remote
public interface ConfiguracionFacturaElectronicaFacadeRemote {

	public boolean isFacturaElectronicaHabilitado();

}
