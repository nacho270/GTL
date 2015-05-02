package ar.com.textillevel.modulos.alertas.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.modulos.alertas.entidades.Alerta;

@Local
public interface AlertaDAOLocal extends DAOLocal<Alerta, Integer>{

	public boolean existeAlerta(Alerta alerta);

	public List<Alerta> getAlertasVigentesByPerfil(Perfil perfil);

}
