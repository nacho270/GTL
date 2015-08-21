package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCantidadColores;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Stateless
public class ListaDePreciosDAO extends GenericDAO<ListaDePrecios, Integer> implements ListaDePreciosDAOLocal {

	@SuppressWarnings("unchecked")
	public ListaDePrecios getListaByIdCliente(Integer idCliente) {
		Query query = getEntityManager().createQuery("SELECT lc " +
													 "FROM ListaDePrecios AS lc " +
													 "WHERE lc.cliente.id = :idCliente ");
		query.setParameter("idCliente", idCliente);
		List<ListaDePrecios> resultList = query.getResultList();
		if(resultList.size()> 1) {
			throw new RuntimeException("Existe más de una lista de precios para el cliente con ID " + idCliente);
		} else if(resultList.size() == 1) {
			ListaDePrecios listaDePrecios = resultList.get(0);
			listaDePrecios.getVersiones().size();
			for (VersionListaDePrecios v : listaDePrecios.getVersiones()) {
				v.getPrecios().size();
				for(DefinicionPrecio dp : v.getPrecios()) {
					dp.getRangos().size();
					for (RangoAncho ra : dp.getRangos()) {
						if (ra instanceof RangoAnchoArticuloTenido) {
							RangoAnchoArticuloTenido raat = (RangoAnchoArticuloTenido) ra;
							raat.getGruposGama().size();
							for (GrupoTipoArticuloGama gtag : raat.getGruposGama()) {
								gtag.getPrecios().size();
							}
						} else if (ra instanceof RangoAnchoArticuloEstampado) {
							RangoAnchoArticuloEstampado raae = (RangoAnchoArticuloEstampado) ra;
							raae.getGruposBase().size();
							for (GrupoTipoArticuloBaseEstampado gtabe : raae.getGruposBase()) {
								gtabe.getPrecios().size();
								for (PrecioBaseEstampado pbe : gtabe.getPrecios()) {
									pbe.getRangosDeColores().size();
									for (RangoCantidadColores rcc : pbe.getRangosDeColores()) {
										rcc.getRangos().size();
									}
								}
							}
						} else {
							RangoAnchoComun rac = (RangoAnchoComun) ra;
							rac.getPrecios().size();
						}
					}
				}
			}
			return listaDePrecios;
		}
		return null;
	}

}
