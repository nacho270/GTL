package ar.com.fwcommon.templates.main.config;

import ar.com.fwcommon.componentes.error.FWException;

public interface IConfigClienteManager {

	public abstract void cargarConfiguracionCliente() throws FWException;

	public abstract void guardarConfiguracionCliente() throws FWException;

}