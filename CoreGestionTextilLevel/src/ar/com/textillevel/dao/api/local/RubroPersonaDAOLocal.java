package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;

@Local
public interface RubroPersonaDAOLocal extends DAOLocal<Rubro, Integer> {

	public List<Rubro> getAllRubroByTipo(ETipoRubro tipo);
}
