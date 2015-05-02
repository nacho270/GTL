package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ContenedorMateriaPrimaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;

@Stateless
public class ContenedorMateriaPrimaDAO extends GenericDAO<ContenedorMateriaPrima, Integer> implements ContenedorMateriaPrimaDAOLocal {

}
