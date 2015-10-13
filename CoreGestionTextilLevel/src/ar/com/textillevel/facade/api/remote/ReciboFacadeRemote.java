package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.to.ResumenReciboTO;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;

@Remote
public interface ReciboFacadeRemote {

	public Recibo ingresarRecibo(Recibo recibo, String usuario) throws ValidacionException;

	public Integer getLastNroRecibo();

	public Recibo getByNroReciboEager(Integer nroRecibo);
	
	public Recibo getByIdEager(Integer id);
	
	public Recibo saveRecibo(Recibo recibo);
	
	public void anularRecibo(Recibo recibo, String usuario) throws FWException;
	
	public void borrarRecibo(Recibo recibo, String usuario) throws ValidacionException;

	public void cambiarEstadoRecibo(Recibo r, EEstadoRecibo estadoNuevo, String usrName);

	public Date getUltimaFechaReciboGrabado();

	public List<ResumenReciboTO> getResumenReciboList(Integer idCliente, Date fechaDesde, Date fechaHasta);
	
	public void checkEliminacionRecibo(Integer id) throws ValidacionException;
	
	public void checkEdicionRecibo(Integer idRecibo) throws ValidacionException;

}
