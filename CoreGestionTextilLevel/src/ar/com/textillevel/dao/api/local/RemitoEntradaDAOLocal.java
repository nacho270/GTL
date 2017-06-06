package ar.com.textillevel.dao.api.local;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;
import ar.com.textillevel.entidades.documentos.remito.to.DetalleRemitoEntradaNoFacturado;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Local
public interface RemitoEntradaDAOLocal extends DAOLocal<RemitoEntrada, Integer> {

	public boolean existsNroRemitoByCliente(Integer idCliente, Integer nroRemito);

	public List<RemitoEntrada> getRemitoEntradaByClienteList(Integer idCliente);

	public RemitoEntrada getByIdEager(Integer idRemito);

	public RemitoEntrada getByNroRemitoEager(Integer nroCliente, Integer nroRemito);

	/**
	 * Devuelve el remito de entrada por id de cliente y nro de remito
	 * @param idCliente
	 * @param idProveedor 
	 * @param nroRemitoEntrada
	 * @return El remito de entrada por id de cliente y nro de remito
	 */
	public RemitoEntrada getByIdClienteAndNro(Integer idCliente, Integer idProveedor, Integer nroRemitoEntrada);

	public List<RemitoEntrada> getRemitoEntradaByFechasAndCliente(Date fechaDesde, Date fechaHasta, Integer idCliente, Producto producto, ESituacionODTRE eSituacionODT);

	public List<RemitoEntrada> getRemitoEntradaConPiezasNoAsociadasList();

	public List<RemitoEntrada> getRemitoEntradaConPiezasSinODTByCliente(Integer idCliente);

	public BigDecimal getStockFisico(Articulo articulo, ETipoTela tipoTela);

	public List<RemitoEntrada> getByNroRemito(Integer nroRemito);

	public List<RemitoEntrada> getRemitoEntradaConPiezasParaVender();

	public List<DetallePiezaFisicaTO> getDetallePiezas(Articulo articuloElegido, ETipoTela tipoTelaElegida);

	public Integer getArticuloByPiezaSalidaCruda(Integer idPiezaRemitoSalidaCruda);

	public RemitoEntrada getByIdPiezaRemitoEntradaEager(Integer idPiezaRemito);

	public List<DetalleRemitoEntradaNoFacturado> getRemitosEntradaSinFactura();

}