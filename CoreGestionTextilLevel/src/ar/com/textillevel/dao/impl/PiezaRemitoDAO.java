package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.PiezaRemitoDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;

@Stateless
@SuppressWarnings("unchecked")
public class PiezaRemitoDAO extends GenericDAO<PiezaRemito, Integer> implements PiezaRemitoDAOLocal {

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) {
		List<DetallePiezaRemitoEntradaSinSalida> infoPiezas = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
		Query query = getEntityManager().createNativeQuery(
				"select r.a_nro_remito, odt.a_codigo, odt.p_id, prod.A_DESCR, count(*), sum(podt.a_metros) " +
				"from t_pieza_remito pr " + 
				"inner join T_PIEZA_ODT podt on podt.F_PIEZA_REMITO_P_ID = pr.p_id " + 
				"inner join T_ORDEN_DE_TRABAJO odt on odt.p_id = podt.f_odt_p_id " +
				"inner join T_PRODUCTO_ARTICULO pa on pa.p_id = odt.f_producto_articulo_p_id " +
				"inner join T_PRODUCTO prod on prod.p_id = pa.f_producto_p_id " +				
				"inner join t_remito r on r.p_id = pr.f_remito_p_id " +
				"where r.f_cliente_p_id=:idCliente AND  r.tipo='ENT' AND " +
				"      NOT EXISTS( " + 
				"           SELECT 1 " + 
				"           FROM T_PIEZA_REMITO PRS " + 
				"           INNER JOIN T_REMITO R2 on R2.P_ID=PRS.F_REMITO_P_ID " +
				"           WHERE PRS.F_PIEZA_PADRE_P_ID = pr.P_ID AND R2.TIPO='SAL' " +
				"                 AND r.f_cliente_p_id = R2.F_CLIENTE_P_ID) " +
				"GROUP BY r.a_nro_remito, odt.a_codigo, odt.p_id, prod.A_DESCR " + 
				"order by odt.a_codigo DESC "
		);
		query.setParameter("idCliente", idCliente);
		List<Object[]> resultList = query.getResultList();
		for(Object[] row : resultList) {
			DetallePiezaRemitoEntradaSinSalida detalle = new DetallePiezaRemitoEntradaSinSalida();
			detalle.setNroRemito((Integer)row[0]);
			detalle.setCodigoODT((String)row[1]);
			detalle.setIdODT((Integer)row[2]);
			detalle.setProducto((String)row[3]);
			detalle.setCantPiezas(NumUtil.toInteger(row[4]));
			detalle.setMetrosTotales(row[5] == null ? 0d : ((BigDecimal)row[5]).doubleValue());
			infoPiezas.add(detalle);
		}
		return infoPiezas;
	}

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaCompletoSinSalidaByClient(Integer idCliente) {
		List<DetallePiezaRemitoEntradaSinSalida> infoPiezas = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
		Query query = getEntityManager().createNativeQuery(
				"SELECT R.A_NRO_REMITO, ODT.A_CODIGO, ODT.P_ID, PROD.A_DESCR, COUNT(*), SUM(PODT.A_METROS) "+
				"FROM T_PIEZA_REMITO PR "+
				"INNER JOIN T_PIEZA_ODT PODT ON PODT.F_PIEZA_REMITO_P_ID = PR.P_ID " +
				"INNER JOIN T_ORDEN_DE_TRABAJO ODT ON ODT.P_ID = PODT.F_ODT_P_ID " +
				"INNER JOIN T_PRODUCTO_ARTICULO PA ON PA.P_ID = ODT.F_PRODUCTO_ARTICULO_P_ID " +
				"INNER JOIN T_PRODUCTO PROD ON PROD.P_ID = PA.F_PRODUCTO_P_ID " +
				"INNER JOIN T_REMITO R ON R.P_ID = PR.F_REMITO_P_ID " +
				"WHERE R.F_CLIENTE_P_ID=:idCliente AND  R.TIPO='ENT' AND " +
				"NOT EXISTS( " +
				"	SELECT 1 " +
				"      FROM T_PIEZA_REMITO PR2 " +
				"      INNER JOIN T_PIEZA_REMITO PRS ON PRS.F_PIEZA_PADRE_P_ID = PR2.P_ID " +
				"      INNER JOIN T_REMITO R2 ON R2.P_ID=PRS.F_REMITO_P_ID " +
				"      WHERE PR2.F_REMITO_P_ID = R.P_ID AND " +
				"            PRS.F_PIEZA_PADRE_P_ID = PR2.P_ID AND R2.TIPO='SAL' AND " +
				"            R.F_CLIENTE_P_ID = R2.F_CLIENTE_P_ID " +
				") AND " +
				// SI TIENE EL REMITO DE ENTRADA TIENE REMITO ENTRADA PROVEEDOR, NO SE PUEDE PASAR A LA B
				" NOT EXISTS (SELECT 1 FROM T_REMITO_ENTRADA_PROV rep WHERE R.p_id = rep.f_rem_ent_cliente_p_id) " +
				"GROUP BY R.A_NRO_REMITO, ODT.A_CODIGO, ODT.P_ID, PROD.A_DESCR " + 
				"ORDER BY ODT.A_CODIGO DESC"
		);
		query.setParameter("idCliente", idCliente);
		List<Object[]> resultList = query.getResultList();
		for(Object[] row : resultList) {
			DetallePiezaRemitoEntradaSinSalida detalle = new DetallePiezaRemitoEntradaSinSalida();
			detalle.setNroRemito((Integer)row[0]);
			detalle.setCodigoODT((String)row[1]);
			detalle.setIdODT((Integer)row[2]);
			detalle.setProducto((String)row[3]);
			detalle.setCantPiezas(NumUtil.toInteger(row[4]));
			detalle.setMetrosTotales(row[5] == null ? 0d : ((BigDecimal)row[5]).doubleValue());
			infoPiezas.add(detalle);
		}
		return infoPiezas;
	}

}