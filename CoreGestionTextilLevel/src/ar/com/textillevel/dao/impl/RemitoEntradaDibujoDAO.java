package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.RemitoEntradaDibujoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Stateless
@SuppressWarnings("unchecked")
public class RemitoEntradaDibujoDAO extends GenericDAO<RemitoEntradaDibujo, Integer> implements RemitoEntradaDibujoDAOLocal {

	public List<RemitoEntradaDibujo> getRemitosEntradaDibujo(Date fechaDesde, Date fechaHasta, Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT re " + 
				"FROM RemitoEntradaDibujo re "
				+ "WHERE 1=1 "
				+ "AND re.fechaIngreso between :fechaDesde AND :fechaHasta "
				+ (idCliente == null ? "" : " AND re.cliente.id = :idCliente ")
				+ "ORDER BY re.fechaIngreso DESC");
		
		if(idCliente != null) {
			query.setParameter("idCliente", idCliente);
		}
		query.setParameter("fechaDesde", fechaDesde);
		query.setParameter("fechaHasta", fechaHasta);
		List<RemitoEntradaDibujo> lista = query.getResultList();
		for (RemitoEntradaDibujo re : lista) {
			re.getItems().size();
		}
		return lista;
	}

	public RemitoEntradaDibujo getByIdEager(Integer id) {
		RemitoEntradaDibujo reDibujo = getById(id);
		reDibujo.getItems().size();
		return reDibujo;
	}

	public RemitoEntradaDibujo getREByDibujo(DibujoEstampado dibujo) {
		Query query = getEntityManager().createQuery("SELECT red FROM RemitoEntradaDibujo red WHERE EXISTS (SELECT 1 FROM red.items ITS WHERE ITS.dibujo = :dibujo)");
		query.setParameter("dibujo", dibujo);
		List<RemitoEntradaDibujo> lista = query.getResultList();
		if(lista.isEmpty()) {
			return null;
		} else {
			for (RemitoEntradaDibujo re : lista) {
				re.getItems().size();
			}
			return lista.get(0);
		}
	}

	@Override
	public void borrarREDibujoRelacionadoFC(Factura factura) {
		Query query = getEntityManager().createQuery("DELETE FROM RemitoEntradaDibujo red WHERE red.factura = :factura");
		query.setParameter("factura", factura);
		query.executeUpdate();
	}

	@Override
	public RemitoEntradaDibujo getByFCRelacionada(Factura factura) {
		Query query = getEntityManager().createQuery("SELECT red FROM RemitoEntradaDibujo red WHERE red.factura = :factura");
		query.setParameter("factura", factura);
		List<RemitoEntradaDibujo> lista = query.getResultList();
		if(lista.isEmpty()) {
			return null;
		} else {
			for (RemitoEntradaDibujo re : lista) {
				re.getItems().size();
			}
			return lista.get(0);
		}
	}

}