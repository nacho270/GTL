package ar.com.textillevel.web.spring.cuenta;

import java.util.List;

import ar.com.textillevel.entidades.cuenta.to.CuentaTO;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.ProveedorFacadeLocal;
import ar.com.textillevel.web.util.BeanHelper;

public class CuentaRestControllerProveedor extends AbstractCuentaRestController<Proveedor, ProveedorFacadeLocal, CuentaFacadeLocal> {

	@Override
	public ProveedorFacadeLocal getFacade() {
		return BeanHelper.getProveedorFacade();
	}

	@Override
	protected CuentaTO getCuentaTO(Proveedor entidad, Integer cantidadMovimientos) {
		return getCuentaFacade().getCuentaTO(entidad, cantidadMovimientos);
	}

	@Override
	protected List<Proveedor> getByNombre(String nombre) {
		return getFacade().getProveedorByRazonSocial(nombre.trim());
	}

	@Override
	protected Proveedor getEntidadByIdentificadorInterno(String identificador) {
		throw new RuntimeException("NO SE DEBE IMPLEMENTAR");
	}

	@Override
	public CuentaFacadeLocal getCuentaFacade() {
		return BeanHelper.getCuentaFacade();
	}

	@Override
	protected boolean busquedaByIdEnabled() {
		return true;
	}

	@Override
	protected Proveedor getEntidadById(Integer id) {
		return getFacade().getById(id);
	}
}
