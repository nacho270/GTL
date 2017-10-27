package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;
import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;

@Remote
public interface RemitoEntradaDibujoFacadeRemote {

	public List<RemitoEntradaDibujo> getRemitosEntradaDibujo(Date fechaDesde, Date fechaHasta, Integer idCliente);

	public RemitoEntradaDibujo grabarREDibujo(RemitoEntradaDibujo red, String usrName);

	public RemitoEntradaDibujo getByIdEager(Integer id);

	public RemitoEntradaDibujo getByFCRelacionada(Factura factura);

}
