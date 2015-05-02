package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.enums.ETipoVentaStock;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.GrupoDetallePiezasFisicasTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemInformeStockTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Remote
public interface PrecioMateriaPrimaFacadeRemote {
	public List<PrecioMateriaPrima> getAllOrderByName(Proveedor proveedor);
	public List<PrecioMateriaPrima> getAllWithStockByProveedorOrderByMateriaPrima(Integer idProveedor);
	public List<PrecioMateriaPrima> getAllWithStockInicialDispByArticulo(Integer idArticulo);
	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByTipo(ETipoMateriaPrima tipo);
	public PrecioMateriaPrima actualizarStockPrecioMateriaPrima(BigDecimal cantidad, Integer idPrecioMateriaPrima);
	public List<ItemMateriaPrimaTO> getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima tipo);
	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByIdsMateriasPrimas(List<Integer> idsMateriasPrimas);
	public List<PrecioMateriaPrima> getPreciosMateriaPrimaByTipoVentaStock(ETipoVentaStock tipoVentaStock);
	public BigDecimal getStockByPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima);
	public PrecioMateriaPrima grabarNuevoPrecioMateriaPrimaYAgregarloAListaDePreciosProveedor(PrecioMateriaPrima precioMateriaPrima, Proveedor proveedor, String usuario);
	public List<ItemInformeStockTO> getInformeStock(ETipoMateriaPrima tipo);
	public List<ItemMateriaPrimaTO> getStockTelasFisicas();
	public List<GrupoDetallePiezasFisicasTO> getDetallePiezas(Articulo articuloElegido, ETipoTela tipoTelaElegida);
	public BigDecimal getPrecioMasRecienteTela(Integer idArticulo);
	
}
