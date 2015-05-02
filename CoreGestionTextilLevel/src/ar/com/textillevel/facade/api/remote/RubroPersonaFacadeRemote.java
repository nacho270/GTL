package ar.com.textillevel.facade.api.remote;

import java.util.List;
import javax.ejb.Remote;

import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;

@Remote
public interface RubroPersonaFacadeRemote {

	public List<Rubro> getAllRubroByTipo(ETipoRubro tipo);
	public List<Rubro> getAll();
	public Rubro save(Rubro rubro);
	public void remove(Rubro rubro);

}
