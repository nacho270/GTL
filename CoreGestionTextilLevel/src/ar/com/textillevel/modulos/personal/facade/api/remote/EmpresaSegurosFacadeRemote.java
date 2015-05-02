package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.commons.EmpresaSeguros;

@Remote
public interface EmpresaSegurosFacadeRemote {
	public EmpresaSeguros save(EmpresaSeguros empresaSeguros);
	public void remove(EmpresaSeguros empresaSeguros);
	public List<EmpresaSeguros> getAllOrderByName();
}
