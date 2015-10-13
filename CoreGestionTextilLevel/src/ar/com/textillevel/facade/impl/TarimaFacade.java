package ar.com.textillevel.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.TarimaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.Tarima;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.remote.TarimaFacadeRemote;

@Stateless
public class TarimaFacade implements TarimaFacadeRemote {

	@EJB
	private TarimaDAOLocal tarimaDAO;
	
	public List<Tarima> getAllSorted() {
		return tarimaDAO.getAllSorted();
	}

	public Tarima save(Tarima tarima) throws ValidacionException {
		if(tarimaDAO.existsTarima(tarima)) {
			List<String> strList = new ArrayList<String>();
			strList.add(tarima.getNumero().toString());
			throw new ValidacionException(EValidacionException.REMITO_ENTRADA_TARIMA_EXISTENTE.getInfoValidacion(), strList);
		}
		return tarimaDAO.save(tarima);
	}

}