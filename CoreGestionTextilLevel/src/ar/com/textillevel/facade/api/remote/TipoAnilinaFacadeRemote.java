package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

@Remote
public interface TipoAnilinaFacadeRemote {
	public TipoAnilina save(TipoAnilina tipoAnilina);
	public void remove(TipoAnilina tipoAnilina);
	public List<TipoAnilina> getAllOrderByName();
	public TipoAnilina getByIdEager(Integer id);
}
