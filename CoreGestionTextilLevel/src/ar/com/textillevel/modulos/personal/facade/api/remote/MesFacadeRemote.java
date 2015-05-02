package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.entidades.Mes;

@Remote
public interface MesFacadeRemote {

	public List<Mes> getAllMeses();

}
