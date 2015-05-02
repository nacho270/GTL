package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.entidades.Dia;

@Remote
public interface DiaFacadeRemote {
	public List<Dia> getAllDias();
}
