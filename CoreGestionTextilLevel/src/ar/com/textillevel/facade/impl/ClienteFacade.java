package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ClienteDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;
import ar.com.textillevel.facade.api.local.ClienteFacadeLocal;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;

@Stateless
public class ClienteFacade implements ClienteFacadeLocal, ClienteFacadeRemote {

	@EJB
	private ClienteDAOLocal clienteDAOLocal;

	public List<Cliente> getAllOrderByName() {
		return clienteDAOLocal.getAllOrderByName();
	}

	public Cliente save(Cliente clienteActual) {
		if(clienteActual.getNroCliente() == null || clienteActual.getNroCliente() == 0) {
			Integer ultNroCliente = clienteDAOLocal.getMaxNroCliente();
			if(ultNroCliente == null) {
				ultNroCliente = 0;
			}
			clienteActual.setNroCliente(ultNroCliente + 1);
		}
		return clienteDAOLocal.save(clienteActual);
	}

	public void remove(Cliente clienteActual) {
		clienteDAOLocal.removeById(clienteActual.getId());
	}

	public boolean existeNroCliente(Integer idCliente, Integer nroCliente) {
		return clienteDAOLocal.existeNroCliente(idCliente, nroCliente);
	}

	public Cliente getClienteByNumero(Integer nroCliente) {
		return clienteDAOLocal.getClienteByNumero(nroCliente);
	}

	public List<Cliente> getAllByRazonSocial(String razSoc) {
		return clienteDAOLocal.getAllByRazonSocial(razSoc);
	}

	public List<ClienteDeudaTO> getClientesConDeudaMayorA(BigDecimal monto) {
		return clienteDAOLocal.getClientesConDeudaMayorA(monto);
	}

	public List<ClienteDeudaTO> getClientesDeudores() {
		return clienteDAOLocal.getClientesConDeudaMayorA(new BigDecimal(0d));
	}

}