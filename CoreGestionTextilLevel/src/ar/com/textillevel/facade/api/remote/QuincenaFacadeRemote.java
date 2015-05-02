package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.Quincena;

@Remote
public interface QuincenaFacadeRemote {
	
	public List<Quincena> getAllOrderByName();
	public Quincena getById(Integer idQuincena);
	
}
