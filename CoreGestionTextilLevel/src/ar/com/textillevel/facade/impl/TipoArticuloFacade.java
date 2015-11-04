package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.TipoArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;

@Stateless
public class TipoArticuloFacade implements TipoArticuloFacadeRemote {

	@EJB
	private TipoArticuloDAOLocal tipoArticuloDAO;

	public List<TipoArticulo> getAllTipoArticulos() {
		List<TipoArticulo> tps = tipoArticuloDAO.getAllOrderBy("nombre");
		for(TipoArticulo tp : tps){
			tp.getTiposArticuloComponentes().size();
		}
		return tps;
	}

	public TipoArticulo save(TipoArticulo tipoArticulo) {
		return tipoArticuloDAO.save(tipoArticulo);
	}

	public TipoArticulo getByIdEager(Integer idTipoArticulo) {
		return tipoArticuloDAO.getByIdEager(idTipoArticulo);
	}

	public void remove(TipoArticulo tipoArticuloActual) throws ValidacionException {
		checkEliminacionTA(tipoArticuloActual);
		tipoArticuloDAO.removeById(tipoArticuloActual.getId());
	}

	private void checkEliminacionTA(TipoArticulo tipoArticuloActual) throws ValidacionException {
		if(tipoArticuloDAO.existeTipoArticuloComoComponente(tipoArticuloActual)) {
			throw new ValidacionException(EValidacionException.TIPO_ART_NO_SE_PUEDE_ELIMINAR_TIPO_ARTICULO.getInfoValidacion());
		}
	}

}