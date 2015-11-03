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
public class PiezaRemitoDAO extends GenericDAO<PiezaRemito, Integer> implements PiezaRemitoDAOLocal {

	@SuppressWarnings("unchecked")
	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) {
		List<DetallePiezaRemitoEntradaSinSalida> infoPiezas = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
		Query query = getEntityManager().createNativeQuery(
				"select r.a_nro_remito, odt.a_codigo, odt.p_id, prod.A_DESCR, count(*), sum(pr.a_metros) " +
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
			detalle.setMetrosTotales(((BigDecimal)row[5]).doubleValue());
			infoPiezas.add(detalle);
		}
		return infoPiezas;
	}

}