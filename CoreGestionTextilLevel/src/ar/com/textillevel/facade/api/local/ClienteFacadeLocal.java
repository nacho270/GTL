package ar.com.textillevel.facade.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.entidades.gente.Cliente;

@Local
public interface ClienteFacadeLocal {

	public abstract List<Cliente> getAllOrderByName();
	public abstract Cliente getClienteByNumero(Integer nroCliente);
	public abstract List<Cliente> getAllByRazonSocial(String razSoc);

}
