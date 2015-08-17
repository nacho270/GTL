package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

@Local
public interface GamaColorClienteDAOLocal extends DAOLocal<GamaColorCliente, Integer>{

	public List<GamaColorCliente> getByCliente(Integer idCliente);
	public GamaColorCliente getByGama(Integer idGamaOriginal);

}
