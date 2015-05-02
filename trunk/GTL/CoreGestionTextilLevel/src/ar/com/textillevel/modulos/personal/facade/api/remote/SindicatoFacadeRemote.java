package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.util.List;
import javax.ejb.Remote;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Remote
public interface SindicatoFacadeRemote {
	
	public Sindicato save(Sindicato sindicato);
	public void remove(Sindicato sindicato);
	public List<Sindicato> getAllOrderByName();
}
