package ar.com.fwcommon.componentes.error.notificadores;

import ar.com.fwcommon.componentes.error.FWException;

public interface INotificadorErrores {

	public abstract void notificarError(FWException e);

}