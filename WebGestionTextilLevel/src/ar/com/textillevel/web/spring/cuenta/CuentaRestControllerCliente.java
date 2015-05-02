package ar.com.textillevel.web.spring.cuenta;

import java.util.List;

import ar.com.textillevel.entidades.cuenta.to.CuentaTO;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.local.ClienteFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.web.util.BeanHelper;

public class CuentaRestControllerCliente extends AbstractCuentaRestController<Cliente, ClienteFacadeLocal, CuentaFacadeLocal> {

	@Override
	public ClienteFacadeLocal getFacade() {
		return BeanHelper.getClienteFacade();
	}

	@Override
	protected CuentaTO getCuentaTO(Cliente entidad, Integer cantidadMovimientos) {
		return getCuentaFacade().getCuentaTO(entidad, cantidadMovimientos);
	}

	@Override
	protected List<Cliente> getByNombre(String nombre) {
		return getFacade().getAllByRazonSocial(nombre.trim());
	}

	@Override
	protected Cliente getEntidadByIdentificadorInterno(String identificador) {
		return getFacade().getClienteByNumero(Integer.valueOf(identificador));
	}

	@Override
	public CuentaFacadeLocal getCuentaFacade() {
		return BeanHelper.getCuentaFacade();
	}

	@Override
	protected boolean busquedaByIdEnabled() {
		return false;
	}

	@Override
	protected Cliente getEntidadById(Integer id) {
		throw new RuntimeException("NO SE DEBE IMPLEMENTAR");
	}
}
