package ar.com.textillevel.facade.api.local;

import java.math.BigDecimal;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;

@Local
public interface RelacionContenedorMatPrimaFacadeLocal {
	
	public RelacionContenedorPrecioMatPrima actualizarStockRelContPrecioMatPrima(BigDecimal cantidad, Integer idRelacionContenedorPrecioMatPrima);

}
