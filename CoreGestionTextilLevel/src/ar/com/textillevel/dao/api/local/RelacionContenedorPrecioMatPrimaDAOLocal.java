package ar.com.textillevel.dao.api.local;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;

@Local
public interface RelacionContenedorPrecioMatPrimaDAOLocal extends DAOLocal<RelacionContenedorPrecioMatPrima, Integer> {

	public Map<String, RelacionContenedorPrecioMatPrima> getAllRelacionContenedorByIdProveedor(Integer idProveedor);
	public List<RelacionContenedorPrecioMatPrima> getAllRelacionContenedorConStockByIdProveedor(Integer idProveedor, List<ContenedorMateriaPrima> contenedores);

}
