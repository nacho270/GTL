package ar.com.textillevel.modulos.fe.facade.impl;

import javax.ejb.Stateless;
import ar.com.textillevel.modulos.fe.ConfiguracionAFIPHolder;
import ar.com.textillevel.modulos.fe.facade.api.remote.ConfiguracionFacturaElectronicaFacadeRemote;

@Stateless
public class ConfiguracionFacturaElectronicaFacade implements ConfiguracionFacturaElectronicaFacadeRemote {

	public boolean isFacturaElectronicaHabilitado() {
		return ConfiguracionAFIPHolder.getInstance().isHabilitado();
	}

}