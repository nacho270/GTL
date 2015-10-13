package ar.com.textillevel.dao.api.local;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.ETipoVentaStock;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Local
public interface PrecioMateriaPrimaDAOLocal extends DAOLocal<PrecioMateriaPrima, Integer>{

	public List<PrecioMateriaPrima> getAllByProveedorOrderByMateriaPrima(Proveedor proveedor);
	public abstract List<PrecioMateriaPrima> getPrecioMateriaPrimaByTipo(ETipoMateriaPrima tipo);
	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByIdsMateriasPrimas(List<Integer> idsMateriasPrimas);
	public List<PrecioMateriaPrima> getPreciosMateriaPrimaByTipoVentaStock(ETipoVentaStock tipoVentaStock);
	public BigDecimal getStockByPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima);
	public List<PrecioMateriaPrima> getAllWithStockByProveedorOrderByMateriaPrima(Integer idProveedor);
	public List<PrecioMateriaPrima> getPrecioMateriaConStockPrimaByTipo(ETipoMateriaPrima tipo);
	public List<PrecioMateriaPrima> getAllWithStockInicialDispByArticulo(Integer idArticulo);
	public BigDecimal getPrecioMasRecienteTela(Integer idArticulo);

}
