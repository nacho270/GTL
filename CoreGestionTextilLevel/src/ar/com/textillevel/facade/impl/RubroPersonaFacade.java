package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.RubroPersonaDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;

@Stateless
public class RubroPersonaFacade implements RubroPersonaFacadeRemote {

	@EJB
	private RubroPersonaDAOLocal rubroDAOLocal;

	public Rubro save(Rubro rubroPersona) {
		return rubroDAOLocal.save(rubroPersona);
	}

	public List<Rubro> getAllRubroByTipo(ETipoRubro tipo) {
		return rubroDAOLocal.getAllRubroByTipo(tipo);
	}

	public List<Rubro> getAll() {
		return rubroDAOLocal.getAllOrderBy("nombre");
	}

	public void remove(Rubro rubro) {
		rubroDAOLocal.removeById(rubro.getId());
	}

}