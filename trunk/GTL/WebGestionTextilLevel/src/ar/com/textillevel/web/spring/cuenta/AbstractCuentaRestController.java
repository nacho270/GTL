package ar.com.textillevel.web.spring.cuenta;

import java.util.Collections;
import java.util.List;

import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.entidades.cuenta.to.ContactoTO;
import ar.com.textillevel.entidades.cuenta.to.CuentaTO;
import ar.com.textillevel.entidades.gente.IAgendable;

public abstract class AbstractCuentaRestController<I extends IAgendable, F, CF> {

	public CuentaTO getCuenta(String cuenta, Integer cantidadMovimientos) {
		CuentaTO cuentaTO = null;
		I entidad = null;
		cuenta = cuenta.replaceAll("\\n", "");
		List<I> entidadesQueMatchean = Collections.emptyList();
		if (NumUtil.esNumerico(cuenta)) {
			if (busquedaByIdEnabled()) {
				entidad = getEntidadById(Integer.valueOf(cuenta));
			} else {
				entidad = getEntidadByIdentificadorInterno(cuenta.trim());
			}
		} else {
			entidadesQueMatchean = getByNombre(cuenta.trim());
			if (entidadesQueMatchean.size() == 1) {
				entidad = entidadesQueMatchean.get(0);
			}
		}
		if (entidad == null && entidadesQueMatchean.isEmpty()) {// No se encontró ninguna entidad que matchea con la búsqueda
			cuentaTO = new CuentaTO();
		} else if (entidad == null && !entidadesQueMatchean.isEmpty()) {// se encontró más de una entidad
			cuentaTO = new CuentaTO();
			for (I ent : entidadesQueMatchean) {
				cuentaTO.getResultadoBusquedaContacto().add(new ContactoTO(busquedaByIdEnabled() ? String.valueOf(ent.getId()) : ent.getIdentificadorInterno(), ent.getRazonSocial()));
			}
		} else {
			cuentaTO = getCuentaTO(entidad, cantidadMovimientos);
		}
		return cuentaTO;
	}

	protected abstract I getEntidadById(Integer id);

	public abstract CF getCuentaFacade();

	public abstract F getFacade();

	protected abstract CuentaTO getCuentaTO(I entidad, Integer cantidadMovimientos);

	protected abstract List<I> getByNombre(String nombre);

	protected abstract I getEntidadByIdentificadorInterno(String identificador);

	protected abstract boolean busquedaByIdEnabled();
}
