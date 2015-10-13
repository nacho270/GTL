package ar.com.textillevel.dao.api.local;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;

@Local
public interface ClienteDAOLocal extends DAOLocal<Cliente, Integer> {

	public abstract List<Cliente> getAllOrderByName();
	public abstract List<Cliente> getAllByRazonSocial(String razonSocial);
	public abstract boolean existeNroCliente(Integer idCliente, Integer nroCliente);
	public abstract Integer getMaxNroCliente();
	public abstract Cliente getClienteByNumero(Integer nroCliente);
	public abstract List<ClienteDeudaTO> getClientesConDeudaMayorA(BigDecimal monto);
	public abstract Set<String> getCuits();
	public abstract List<Cliente> getClienteByCUIT(String cuit, Integer idCliente);

}
