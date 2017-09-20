package ar.com.textillevel.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.GrupoArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;

@Stateless
@SuppressWarnings("unchecked")
public class GrupoArticuloDAO extends GenericDAO<GrupoTipoArticulo, Integer> implements GrupoArticuloDAOLocal {

	@Override
	public void deleteGruposArtEstampadoByDibujo(DibujoEstampado dibujoEstampado) {
		Query query = getEntityManager().createQuery("FROM GrupoTipoArticuloBaseEstampado GE WHERE EXISTS (SELECT 1 FROM GE.precios PR WHERE PR.dibujo = :dibujo)");
		query.setParameter("dibujo", dibujoEstampado);
		List<GrupoTipoArticuloBaseEstampado> resultList = query.getResultList();
		Set<PrecioBaseEstampado> preciosParaBorrar = new HashSet<PrecioBaseEstampado>(); 
		for(GrupoTipoArticuloBaseEstampado ga : resultList) {
			for(PrecioBaseEstampado pbe : ga.getPrecios()) {
				if(pbe.getDibujo() != null && pbe.getDibujo().equals(dibujoEstampado)) {
					preciosParaBorrar.add(pbe);
				}
			}
			ga.getPrecios().removeAll(preciosParaBorrar);
			if(ga.getPrecios().isEmpty()) {//si era la única para ese dibujo => borro el grupo tipo artículo
				try {
					remove(ga);
				} catch (FWException e) {//sino sólo hago un update
					e.printStackTrace();
				}
			} else {
				save(ga);
			}
		}
	}

}