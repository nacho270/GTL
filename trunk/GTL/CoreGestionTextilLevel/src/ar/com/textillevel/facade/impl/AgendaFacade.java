package ar.com.textillevel.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.util.AlphabeticComparator;
import ar.com.textillevel.dao.api.local.ClienteDAOLocal;
import ar.com.textillevel.dao.api.local.PersonaDAOLocal;
import ar.com.textillevel.dao.api.local.ProveedorDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.AgendaFacadeRemote;

@Stateless
public class AgendaFacade implements AgendaFacadeRemote{

	@EJB
	private ClienteDAOLocal clienteDAO;
	
	@EJB
	private ProveedorDAOLocal proveedorDAO;
	
	@EJB
	private PersonaDAOLocal personaDAO;
	
	@SuppressWarnings("unchecked")
	public List<IAgendable> buscar(String criterio,ETipoBusquedaAgenda tipoBusqueda, Rubro rubroPersona) throws CLException{
		
		List<IAgendable> ret = new ArrayList<IAgendable>();
		
		if(criterio.trim().length() == 0){
			if(tipoBusqueda.equals(ETipoBusquedaAgenda.TODOS)){
				ret.addAll(clienteDAO.getAllOrderByName());
				ret.addAll(proveedorDAO.getAllOrderByName());
				ret.addAll(personaDAO.getAllOrderByName());
			}else if(tipoBusqueda.equals(ETipoBusquedaAgenda.CLIENTE)){
				ret.addAll(clienteDAO.getAllOrderByName());
			}else if(tipoBusqueda.equals(ETipoBusquedaAgenda.PERSONA)){
				ret.addAll(personaDAO.getAllByRubroOrderByName(rubroPersona));
			}else{
				ret.addAll(proveedorDAO.getAllByRubroOrderByName(rubroPersona));
			}
		}else{
			if(tipoBusqueda.equals(ETipoBusquedaAgenda.TODOS)){
				ret.addAll(clienteDAO.getAllByRazonSocial(criterio));
				ret.addAll(proveedorDAO.getAllByRazonSocial(criterio));
				ret.addAll(personaDAO.getByNombreOApellido(criterio));
			}else if(tipoBusqueda.equals(ETipoBusquedaAgenda.CLIENTE)){
				ret.addAll(clienteDAO.getAllByRazonSocial(criterio));
			}else if(tipoBusqueda.equals(ETipoBusquedaAgenda.PERSONA)){
				ret.addAll(personaDAO.getByNombreOApellidoYRubro(criterio,rubroPersona));
			}else{
				ret.addAll(proveedorDAO.getAllByRazonSocialAndRubro(criterio,rubroPersona));
			}
		}
		
		Collections.sort(ret, new AlphabeticComparator());
		
		return ret;
	}
}
