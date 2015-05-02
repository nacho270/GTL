package ar.com.textillevel.modulos.odt.facade.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Local
public interface OrdenDeTrabajoFacadeLocal {
	public void cambiarODTAFacturada(Integer idOdt, UsuarioSistema usuarioSistema);
	public void cambiarODTAOficina(Integer idOdt, UsuarioSistema usuarioSistema);
	public List<OrdenDeTrabajo> getOdtEagerByRemitoList(Integer idRE);
}
