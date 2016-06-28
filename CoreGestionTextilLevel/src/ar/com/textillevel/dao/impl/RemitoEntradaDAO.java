package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.RemitoEntradaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.to.DetalleRemitoEntradaNoFacturado;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@Stateless
@SuppressWarnings("unchecked")
public class RemitoEntradaDAO extends GenericDAO<RemitoEntrada, Integer> implements RemitoEntradaDAOLocal {

	public boolean existsNroRemitoByCliente(Integer idCliente, Integer nroRemito) {
		Query query = getEntityManager().createQuery("SELECT re.id " +
													 "FROM RemitoEntrada re " +
													 "WHERE re.cliente.id = :idCliente AND re.nroRemito = :nroRemito");
		query.setParameter("idCliente", idCliente);
		query.setParameter("nroRemito", nroRemito);
		return !query.getResultList().isEmpty();
	}

	public RemitoEntrada getByIdEager(Integer idRemito) {
		RemitoEntrada remito = getById(idRemito);
		doEager(remito, true);
		return remito;
	}

	private void doEager(RemitoEntrada remito, boolean piezas) {
		remito.getProductoArticuloList().size();
		remito.getPiezas().size();
		remito.getCliente().getCelular();
		if (piezas && remito.getPiezas() != null) {
			for(PiezaRemito pr : remito.getPiezas()) {
				if (pr.getPiezasPadreODT() != null) {
					pr.getPiezasPadreODT().size();
					for (PiezaODT po : pr.getPiezasPadreODT()) {
						if (po.getOdt() != null) {
							po.getOdt().getCodigo();
						}
					}
				}
			}
		}
	}

	public List<RemitoEntrada> getRemitoEntradaByClienteList(Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT re " +
													 "FROM RemitoEntrada re " +
													 "WHERE re.cliente.id = :idCliente AND " +
													 "NOT EXISTS (SELECT 1 " +
													 "			  FROM OrdenDeTrabajo odt " +
													 "			  WHERE odt.remito.id = re.id " +
													 ") " +
													 "ORDER BY re.fechaEmision ");
		query.setParameter("idCliente", idCliente);
		List<RemitoEntrada> lista = query.getResultList();
		for(RemitoEntrada re : lista){
			re.getProductoArticuloList().size();
		}
		return lista;
	}

	public RemitoEntrada getByNroRemitoEager(Integer nroCliente, Integer nroRemito) {
		Query query = getEntityManager().createQuery("SELECT re " +
				 									 "FROM RemitoEntrada re " +
				 									 "WHERE re.cliente.nroCliente = :nroCliente AND re.nroRemito = :nroRemito");
		query.setParameter("nroCliente", nroCliente);
		query.setParameter("nroRemito", nroRemito);
		List<RemitoEntrada> remitoEntList = query.getResultList();
		if(remitoEntList.size() == 1) {
			RemitoEntrada remitoEntrada = remitoEntList.get(0);
			doEager(remitoEntrada, true);
			return remitoEntrada;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ar.com.textillevel.dao.api.local.RemitoEntradaDAOLocal#getByIdClienteAndNro(java.lang.Integer, java.lang.Integer)
	 */
	public RemitoEntrada getByIdClienteAndNro(Integer idCliente, Integer idProveedor, Integer nroRemito) {
		Query query = getEntityManager().createQuery("SELECT re " +
													 "FROM RemitoEntrada re " +
													 (idCliente != null ? "WHERE re.cliente.id = :idCliente " : "WHERE re.proveedor.id = :idProveedor ") +
													 "AND re.nroRemito = :nroRemito");
		if(idCliente != null) {
			query.setParameter("idCliente", idCliente);
		}
		if(idProveedor != null) {
			query.setParameter("idProveedor", idProveedor);
		}
		query.setParameter("nroRemito", nroRemito);
		List<RemitoEntrada> remitoEntList = query.getResultList();
		if (remitoEntList.size() == 1) {
			RemitoEntrada remitoEntrada = remitoEntList.get(0);
			doEager(remitoEntrada, true);
			if(remitoEntrada.getProveedor() != null) {
				remitoEntrada.getProveedor().getNombreCorto();
			}
			return remitoEntrada;
		} else {
			throw new RuntimeException("Existe mas de un remito con el mismo número para un mismo cliente. Nro Rem: " + nroRemito + ", id cliente: " + idCliente);
		}
	}

	public List<RemitoEntrada> getRemitoEntradaByFechasAndCliente(Date fechaDesde, Date fechaHasta, Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT re " + 
													"FROM RemitoEntrada re "
													+ "WHERE re.cliente.id = :idCliente AND "
													+ "re.fechaEmision between :fechaDesde AND :fechaHasta "
													+ "ORDER BY re.fechaEmision ");
		query.setParameter("idCliente", idCliente);
		query.setParameter("fechaDesde", fechaDesde);
		query.setParameter("fechaHasta", fechaHasta);
		List<RemitoEntrada> lista = query.getResultList();
		for (RemitoEntrada re : lista) {
			re.getProductoArticuloList().size();
		}
		return lista;
	}

	public List<RemitoEntrada> getRemitoEntradaConPiezasNoAsociadasList() {
		Query query = getEntityManager().createQuery("SELECT DISTINCT re " + 
				"FROM RemitoEntrada re " +
				"JOIN FETCH re.articuloStock articulo " + //solo los que alteran el stock o entraron por compra de tela
				"JOIN FETCH re.piezas piezas " +
				"WHERE (piezas.enSalida IS NULL OR piezas.enSalida = 0) AND re.precioMatPrima IS NULL " + //Solo las piezas que aun no tuvieron salida
				"ORDER BY re.fechaEmision ");
		List<RemitoEntrada> resultList = query.getResultList();
		for(RemitoEntrada re : resultList) {
			getEntityManager().refresh(re);//asi me trae el remito con todas sus piezas. Sin esto me traia sólo las del join
			re.getProductoArticuloList().size();
		}
		return resultList;
	}

	public List<RemitoEntrada> getRemitoEntradaConPiezasParaVender() {
		Query query = getEntityManager().createQuery("SELECT DISTINCT re " + 
				"FROM RemitoEntrada re " +
				"JOIN FETCH re.precioMatPrima " + //solo los que son para venta de tela (porque descuentan stock de lo comprado a un proveedor)
				"JOIN FETCH re.piezas piezas " +
				"WHERE (piezas.enSalida IS NULL OR piezas.enSalida = 0) " + //Solo las piezas que aun no tuvieron salida
				"ORDER BY re.fechaEmision ");
		List<RemitoEntrada> resultList = query.getResultList();
		for(RemitoEntrada re : resultList) {
			getEntityManager().refresh(re);//asi me trae el remito con todas sus piezas. Sin esto me traia sólo las del join
			re.getProductoArticuloList().size();
		}
		return resultList;
	}

	public List<RemitoEntrada> getRemitoEntradaConPiezasSinODTByCliente(Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT DISTINCT re " + 
				"FROM RemitoEntrada re " +
				"JOIN FETCH re.piezas piezas " +
				"WHERE piezas.piezaSinODT = 1" +
				"ORDER BY re.fechaEmision ");
		List<RemitoEntrada> resultList = query.getResultList();
		for(RemitoEntrada re : resultList) {
			re.getProductoArticuloList().size();
		}
		return resultList;
	}

	public BigDecimal getStockFisico(Articulo articulo, ETipoTela tipoTela) {
		String consulta = " SELECT SUM(A_METROS) FROM T_PIEZA_REMITO pm " +
						  " INNER JOIN T_REMITO r ON r.P_ID = pm.F_REMITO_P_ID" +
						  " WHERE r.F_ARTICULO_STOCK_P_ID = :idArticulo " +
						  (tipoTela == ETipoTela.CRUDA?" AND  pm.A_PIEZA_SIN_ODT = 1 " :
							  						   " AND (pm.A_PIEZA_SIN_ODT IS NULL OR pm.A_PIEZA_SIN_ODT = 0 ) ") +
					      " AND (pm.A_EN_SALIDA is null OR pm.A_EN_SALIDA = 0 ) ";
		Query q = getEntityManager().createNativeQuery(consulta);
		q.setParameter("idArticulo", articulo.getId());
		BigDecimal stock = NumUtil.toBigDecimal(q.getSingleResult());
		stock = stock == null?new BigDecimal(0d):stock;
		if(tipoTela == ETipoTela.CRUDA){
			consulta = " SELECT SUM(pm.A_STOCK_INICIAL_DISP) FROM T_PRECIO_MATERIA_PRIMA pm " +
					   " INNER JOIN T_MATERIA_PRIMA mp ON mp.P_ID = pm.F_MATERIA_PRIMA_P_ID " +
					   " WHERE mp.F_ARTICULO_P_ID = :idArticulo ";
			q = getEntityManager().createNativeQuery(consulta);
			q.setParameter("idArticulo", articulo.getId());
			BigDecimal sumaStockInicial = NumUtil.toBigDecimal(q.getSingleResult());
			sumaStockInicial = sumaStockInicial==null?new BigDecimal(0d):sumaStockInicial;
			stock = stock.add(sumaStockInicial);
		}
		return stock;
	}

	public List<RemitoEntrada> getByNroRemito(Integer nroRemito) {
		Query query = getEntityManager().createQuery("SELECT re " +
						 							 "FROM RemitoEntrada re " +
						 							 "WHERE re.nroRemito = :nroRemito");
		query.setParameter("nroRemito", nroRemito);
		List<RemitoEntrada> remitoEntList = query.getResultList();
		for(RemitoEntrada re : remitoEntList) {
			re.getProductoArticuloList().size();
		}
		return remitoEntList;
	}

	public List<DetallePiezaFisicaTO> getDetallePiezas(Articulo articuloElegido, ETipoTela tipoTelaElegida) {
		String sql = "";
		if(tipoTelaElegida == ETipoTela.TERMINADA){
			sql = " SELECT pr.A_ORDEN_PIEZA, pr.A_METROS, prov.A_NOMBRE_CORTO, cl.A_RAZON_SOCIAL, rem.A_NRO_REMITO, pr.F_REMITO_P_ID, pr.P_ID, CONCAT(odt.A_CODIGO,' - ',prod.A_DESCR, ' - ', art.A_NOMBRE) " +
				  " FROM T_PIEZA_REMITO pr INNER JOIN T_REMITO rem ON pr.F_REMITO_P_ID = rem.P_ID " +
				  " 				  	   LEFT JOIN T_PROVEEDORES prov ON prov.P_ID = rem.F_PROVEEDOR_P_ID " +
				  " 				  	   LEFT JOIN T_CLIENTE cl ON cl.P_ID = rem.F_CLIENTE_P_ID " +
				  "						   INNER JOIN T_PIEZA_ODT podt ON pr.P_ID = podt.F_PIEZA_REMITO_P_ID " +
				  "						   INNER JOIN T_ORDEN_DE_TRABAJO odt ON odt.P_ID = podt.F_ODT_P_ID " +
				  "						   INNER JOIN T_PRODUCTO_ARTICULO prodart ON prodart.P_ID = odt.F_PRODUCTO_ARTICULO_P_ID " +				  
				  "						   INNER JOIN T_PRODUCTO prod ON prod.P_ID = prodart.F_PRODUCTO_P_ID " +
				  "						   LEFT JOIN T_ARTICULO art ON art.P_ID = prodart.F_ARTICULO_P_ID " +				  
				  " WHERE rem.F_ARTICULO_STOCK_P_ID = :idArticulo AND pr.A_PIEZA_SIN_ODT = 0 " +
				  "		  AND (pr.A_EN_SALIDA is null OR pr.A_EN_SALIDA = 0) AND rem.TIPO = 'ENT'";
		}else{
			sql = " SELECT pr.A_ORDEN_PIEZA, pr.A_METROS, prov.A_NOMBRE_CORTO, cl.A_RAZON_SOCIAL, rem.A_NRO_REMITO, pr.F_REMITO_P_ID, pr.P_ID " +
				  " FROM T_PIEZA_REMITO pr INNER JOIN T_REMITO rem ON pr.F_REMITO_P_ID = rem.P_ID " +
				  " 					   LEFT JOIN T_PROVEEDORES prov ON prov.P_ID = rem.F_PROVEEDOR_P_ID " +
				  " 				       LEFT JOIN T_CLIENTE cl ON cl.P_ID = rem.F_CLIENTE_P_ID  " +
				  " WHERE rem.F_ARTICULO_STOCK_P_ID = :idArticulo AND pr.A_PIEZA_SIN_ODT = 1 " +
				  "		  AND (pr.A_EN_SALIDA is null OR pr.A_EN_SALIDA = 0) AND rem.TIPO = 'ENT'";
		}
		Query q = getEntityManager().createNativeQuery(sql);
		q.setParameter("idArticulo", articuloElegido.getId());
		List<Object[]> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			List<DetallePiezaFisicaTO> listaRet = new ArrayList<DetallePiezaFisicaTO>();
			for(Object[] tupla : lista){
				DetallePiezaFisicaTO detalle = new DetallePiezaFisicaTO();
				detalle.setNroPieza(NumUtil.toInteger(tupla[0]));
				detalle.setMetros(NumUtil.toBigDecimal(tupla[1]));
				String proveedor = (String)(tupla[2]==null?tupla[3]:tupla[2]);
				detalle.setProveedor(proveedor);
				detalle.setNroRemito(NumUtil.toInteger(tupla[4]));
				detalle.setIdRemito(NumUtil.toInteger(tupla[5]));
				detalle.setIdPiezaRemito(NumUtil.toInteger(tupla[6]));
				if(tipoTelaElegida == ETipoTela.TERMINADA){
					detalle.setOdt((String)tupla[7]);
				}
				listaRet.add(detalle);
			}
			return listaRet;
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("rawtypes")
	public Integer getArticuloByPiezaSalidaCruda(Integer idPiezaRemitoSalidaCruda) {
		Query query = getEntityManager().createNativeQuery("SELECT R.F_ARTICULO_STOCK_P_ID " + 
													 	   "FROM T_PIEZA_REMITO PRS " +
													 	   "INNER JOIN T_PIEZA_REMITO PRE ON PRE.P_ID = PRS.F_PIEZA_PADRE_P_ID " +
													 	   "INNER JOIN T_REMITO R ON R.P_ID = PRE.F_REMITO_P_ID " +
													 	   "WHERE PRS.P_ID = :idPiezaRemitoSalida ");
		query.setParameter("idPiezaRemitoSalida", idPiezaRemitoSalidaCruda);
		List resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			Number idArticulo = (Number)resultList.get(0); 
			return idArticulo == null ? null : idArticulo.intValue();
		}
	}

	public RemitoEntrada getByIdPiezaRemitoEntradaEager(Integer idPiezaRemito) {
		Query query = getEntityManager().createNativeQuery("SELECT PRS.F_REMITO_P_ID " + 
													 	   "FROM T_PIEZA_REMITO PRS " +
													 	   "INNER JOIN T_REMITO RE ON RE.P_ID = PRS.F_REMITO_P_ID " +
													 	   "WHERE PRS.P_ID = :idPiezaRemito AND RE.TIPO = 'ENT'");
		query.setParameter("idPiezaRemito", idPiezaRemito);
		Number idRemitoEntrada = (Number)query.getSingleResult();
		if(idRemitoEntrada == null) {
			return null;
		} else {
			return getByIdEager(idRemitoEntrada.intValue());
		}
	}

	public List<DetalleRemitoEntradaNoFacturado> getRemitosEntradaSinFactura() {
		List<DetalleRemitoEntradaNoFacturado> lista = new ArrayList<DetalleRemitoEntradaNoFacturado>();
		Query query = getEntityManager().createNativeQuery(" SELECT DISTINCT re.p_id FROM T_ORDEN_DE_TRABAJO odt " +
														   " 	INNER JOIN T_REMITO re on odt.F_REMITO_P_ID=re.P_ID AND re.TIPO='ENT' " +
														   " 	INNER JOIN T_REMITO_SALIDA_ODT rsodt on rsodt.F_ODT_P_ID = odt.P_ID " +
														   " 	INNER JOIN T_REMITO rs on rs.p_id = rsodt.F_REMITO_SALIDA_P_ID AND rs.F_FACTURA_P_ID IS NULL " +
														   // SI TIENE REMITO ENTRADA PROVEEDOR, NO SE PUEDE PASAR A LA A
														   " WHERE NOT EXISTS (SELECT 1 FROM T_REMITO_ENTRADA_PROV rep WHERE re.p_id = rep.f_rem_ent_cliente_p_id) " +
														   " ORDER BY re.A_FECHA_EMISION DESC ");
		List<Object[]> ids = query.getResultList();
		if (ids == null || ids.isEmpty()) {
			return lista;
		}
		for(Object arr : ids) {
			RemitoEntrada remito = getById(NumUtil.toInteger(arr));
			doEager(remito, false);
			lista.add(new DetalleRemitoEntradaNoFacturado(remito));
		}
		return lista;
	}
}