package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.contratos.Contrato;

@Remote
public interface ContratoFacadeRemote {
	public List<Contrato> getAll();
}
