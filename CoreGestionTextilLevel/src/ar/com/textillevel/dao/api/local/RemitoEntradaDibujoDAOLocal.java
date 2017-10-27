package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;
import javax.ejb.Local;
import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Local
public interface RemitoEntradaDibujoDAOLocal extends DAOLocal<RemitoEntradaDibujo, Integer> {

	public List<RemitoEntradaDibujo> getRemitosEntradaDibujo(Date fechaDesde, Date fechaHasta, Integer idCliente);

	public RemitoEntradaDibujo getByIdEager(Integer id);

	public RemitoEntradaDibujo getREByDibujo(DibujoEstampado dibujo);

	public void borrarREDibujoRelacionadoFC(Factura factura);

	public RemitoEntradaDibujo getByFCRelacionada(Factura factura);

}