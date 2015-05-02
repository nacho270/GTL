package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Local
public interface CuentaArticuloDAOLocal extends DAOLocal<CuentaArticulo, Integer> {

	public CuentaArticulo getCuentaArticulo(Cliente cliente, Articulo articulo, EUnidad tipoUnidad);

}
