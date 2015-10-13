package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.SecuenciaAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

@Local
public interface SecuenciaTipoProductoDAOLocal extends DAOLocal<SecuenciaTipoProducto, Integer>{

	List<SecuenciaTipoProducto> getByTipoProducto(ETipoProducto tipoProducto);
	public <T extends SecuenciaAbstract<?, ?>> List<T> getAllByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente, Boolean incluirDefault, Class<T> clase);

}
