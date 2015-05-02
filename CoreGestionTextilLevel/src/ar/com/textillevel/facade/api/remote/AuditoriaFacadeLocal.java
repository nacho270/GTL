package ar.com.textillevel.facade.api.remote;

import javax.ejb.Local;

@Local
public interface AuditoriaFacadeLocal<C> {
	public void auditar(String usuario, String descripcion, Integer idTipoEvento,C clase);
}
