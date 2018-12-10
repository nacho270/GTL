package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.VersionListaDePreciosDAOLocal;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Stateless
public class VersionListaDePreciosDAO extends GenericDAO<VersionListaDePrecios, Integer> implements VersionListaDePreciosDAOLocal{

}
