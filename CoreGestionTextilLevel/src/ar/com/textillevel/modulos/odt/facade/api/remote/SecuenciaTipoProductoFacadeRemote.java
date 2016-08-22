package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;
import ar.com.textillevel.modulos.odt.to.ABMSecuenciasModelTO;

@Remote
public interface SecuenciaTipoProductoFacadeRemote {
	public SecuenciaTipoProducto save(SecuenciaTipoProducto secuencia);
	public void delete (SecuenciaTipoProducto secuencia);
	public List<SecuenciaTipoProducto> getAllByTipoProducto(ETipoProducto tipoProducto);
	public List<SecuenciaTipoProducto> getAllByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente);
	public List<SecuenciaTipoProducto> getAllByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente, Boolean incluirDefault);
	public void persistModel(ABMSecuenciasModelTO model);
	public SecuenciaTipoProducto getByIdEager(Integer idSecuencia);
	public List<SecuenciaTipoProducto> getAllAbstractByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente, Boolean incluirDefault);
}
