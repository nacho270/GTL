package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;

@Local
public interface RemitoEntradaDibujoFacadeLocal {

	public RemitoEntradaDibujo grabarREDibujo(RemitoEntradaDibujo red, String usrName);

	public void borrarREDibujoRelacionadoFC(Factura factura);

}
