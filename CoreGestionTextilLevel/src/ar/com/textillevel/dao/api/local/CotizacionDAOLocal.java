package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Local
public interface CotizacionDAOLocal extends DAOLocal<Cotizacion, Integer> {

	public Cotizacion getUltimaCotizacion(Cliente cliente);

	public Integer getUltimoNumeroCotizacion();

	public Cotizacion getUltimaCotizacionParaVersion(VersionListaDePrecios version);

}
