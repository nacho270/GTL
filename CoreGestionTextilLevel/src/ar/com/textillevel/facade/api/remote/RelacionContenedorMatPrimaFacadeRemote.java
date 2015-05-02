package ar.com.textillevel.facade.api.remote;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;

@Remote
public interface RelacionContenedorMatPrimaFacadeRemote {

	public Map<String, RelacionContenedorPrecioMatPrima> getAllRelacionContenedorByIdProveedor(Integer idProveedor);
	public List<RelacionContenedorPrecioMatPrima> getAllRelacionContenedorConStockByIdProveedor(Integer idProveedor, List<ContenedorMateriaPrima> contenedores);

}
