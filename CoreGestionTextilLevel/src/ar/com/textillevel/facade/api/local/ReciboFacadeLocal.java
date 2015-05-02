package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;

@Local
public interface ReciboFacadeLocal {

	public Recibo ingresarRecibo(Recibo recibo, String usuario) throws ValidacionException;
	public Recibo getByIdEager(Integer id);
	public Recibo getByNroReciboEager(Integer nroRecibo);

}
