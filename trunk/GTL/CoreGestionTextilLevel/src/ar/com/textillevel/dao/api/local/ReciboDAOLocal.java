package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.to.ResumenReciboTO;

@Local
public interface ReciboDAOLocal extends DAOLocal<Recibo, Integer> {

	public Integer getLastNroRecibo();

	public Recibo getByNroReciboEager(Integer nroRecibo);

	public Recibo getByIdEager(Integer id);
	
	public Map<Integer, List<Integer>> getMapaRecibosYPagosRecibos();

	public InfoCuentaTO getInfoReciboYPagosRecibidos(Integer nroCliente);

	public void rollBackPagosFacturaYNotasDeDebito(Recibo recibo) throws CLException ;

	public Date getUltimaFechaReciboGrabado();
	
	public List<ResumenReciboTO> getResumenReciboList(Integer idCliente, Date fechaDesde, Date fechaHasta);

	public Integer getLastNroReciboByCliente(Integer id);

	public boolean existsNroRecibo(Integer idRecibo, Integer nroRecibo);
	
	public List<Recibo> getAllNoAnuladosByIdCliente(Integer idCliente);

}
