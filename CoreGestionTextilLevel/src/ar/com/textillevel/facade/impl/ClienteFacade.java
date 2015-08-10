package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.ClienteDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.ClienteFacadeLocal;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;

@Stateless
public class ClienteFacade implements ClienteFacadeLocal, ClienteFacadeRemote {

	@EJB
	private ClienteDAOLocal clienteDAOLocal;

	public List<Cliente> getAllOrderByName() {
		return clienteDAOLocal.getAllOrderByName();
	}

	public Cliente save(Cliente clienteActual) throws ValidacionException {
		checkDatosCliente(clienteActual);
		if(clienteActual.getNroCliente() == null || clienteActual.getNroCliente() == 0) {
			Integer ultNroCliente = clienteDAOLocal.getMaxNroCliente();
			if(ultNroCliente == null) {
				ultNroCliente = 0;
			}
			clienteActual.setNroCliente(ultNroCliente + 1);
		}
		return clienteDAOLocal.save(clienteActual);
	}

	private void checkDatosCliente(Cliente cliente) throws ValidacionException{
		// Chequeo unicidad de CUIT
		List<Cliente> clientes = clienteDAOLocal.getClienteByCUIT(cliente.getCuit(), cliente.getId() == null ? 0 : cliente.getId());
		if(!clientes.isEmpty()) {
			throw new ValidacionException(EValidacionException.CLIENTE_YA_EXISTE_CUIT.getInfoValidacion());
		}
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

	public Cliente getById(Integer idCliente) {
		return clienteDAOLocal.getById(idCliente);
	}

	public Set<String> getCuits() {
		return clienteDAOLocal.getCuits();
	}

}