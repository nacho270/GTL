package ar.com.fwcommon.templates.main.login;

import ar.com.fwcommon.componentes.error.FWException;

public interface ILoginManager {

	public boolean login(String usuario, String password) throws FWException;

}