package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Local
public interface RemitoSalidaFacadeLocal {

	public void anularRemitoSalida(RemitoSalida remitoSalida);
	
	public Integer getUltimoNumeroFactura(EPosicionIVA posIva);

	public RemitoSalida guardarRemito(RemitoSalida remito);
	
	public RemitoSalida getByIdConPiezasYProductos(Integer id);

	public void marcarEntregado(String numero, String nombreTerminal);

	public void reingresar(String numero, String nombreTerminal);

}
