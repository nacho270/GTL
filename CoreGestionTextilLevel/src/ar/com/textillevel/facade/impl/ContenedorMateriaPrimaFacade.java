package ar.com.textillevel.facade.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.dao.api.local.ContenedorMateriaPrimaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.facade.api.remote.ContenedorMateriaPrimaFacadeRemote;

@Stateless
public class ContenedorMateriaPrimaFacade implements ContenedorMateriaPrimaFacadeRemote {

	@EJB
	private ContenedorMateriaPrimaDAOLocal contenedorMateriaPrimaDAO;

	public List<ContenedorMateriaPrima> getAllOrderByName() {
		return contenedorMateriaPrimaDAO.getAllOrderBy("nombre");
	}

	public void remove(ContenedorMateriaPrima contenedorActual) {
		try {
			contenedorMateriaPrimaDAO.remove(contenedorActual);
		} catch (CLException e) {
			e.printStackTrace();
		}
	}

	public ContenedorMateriaPrima guardarContenedor(ContenedorMateriaPrima contenedorActual) {
		return contenedorMateriaPrimaDAO.save(contenedorActual);
	}

}