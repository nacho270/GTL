package ar.com.textillevel.modulos.alertas.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.modulos.alertas.entidades.Alerta;

@Remote
public interface AlertaFacadeRemote {
	public void crearAlerta(Alerta alerta);
	public void save(Alerta alerta);
	public List<Alerta> getAlertasVigentesByPerfil(Perfil perfil);
}
