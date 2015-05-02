package ar.com.textillevel.modulos.alertas.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;

@Remote
public interface TipoAlertaFacadeRemote {
	public TipoAlerta getById(Integer id);
	public TipoAlerta getReferenceById(Integer id);
	public List<TipoAlerta> getAllOrderByName();
}
