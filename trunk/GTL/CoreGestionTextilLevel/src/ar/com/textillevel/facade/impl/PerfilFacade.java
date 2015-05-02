package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.PerfilDAOLocal;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.facade.api.remote.PerfilFacadeRemote;

@Stateless
public class PerfilFacade implements PerfilFacadeRemote{

	@EJB
	private PerfilDAOLocal perfilDao;
	
	public List<Perfil> getAllOrderByName() {
		List<Perfil> perfiles = perfilDao.getAllOrderBy("nombre");
		if(perfiles!=null && !perfiles.isEmpty()){
			for(Perfil p : perfiles){
				if(p.getTiposDeAlertas()!=null){
					p.getTiposDeAlertas().size();
				}
			}
		}
		return perfiles;
	}

	public void remove(Perfil perfil) {
		perfilDao.removeById(perfil.getId());
	}

	public Perfil save(Perfil perfil) throws ValidacionException {
//		if(perfil.getIsAdmin()!=null && perfil.getIsAdmin()==true && perfilDao.yaHayPerfilAdministrador()){
//			throw new ValidacionException(EValidacionException.YA_HAY_PERFIL_ADMIN.getInfoValidacion());
//		}
		return perfilDao.save(perfil);
	}
}

