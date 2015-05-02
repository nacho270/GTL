package main;

import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

public class GTLGlobalCache {
	
	private UsuarioSistema usuarioSistema;
	private Articulo articuloDefault;
	
	private static GTLGlobalCache instance = new GTLGlobalCache();

	public UsuarioSistema getUsuarioSistema() {
		return usuarioSistema;
	}

	public void setUsuarioSistema(UsuarioSistema usuarioSistema) {
		this.usuarioSistema = usuarioSistema;
	}
	
	public static GTLGlobalCache getInstance(){
		return instance;
	}
	
	public List<Modulo> getModulosTriggerUsuarioLogueado(){
		List<Modulo> modulosTrigger = new ArrayList<Modulo>();
		for(Modulo m : getUsuarioSistema().getPerfil().getModulos()){
			if(m.getTrigger()){
				modulosTrigger.add(m);
			}
		}
		return modulosTrigger;
	}

	
	public Articulo getArticuloDefault() {
		return articuloDefault;
	}

	
	public void setArticuloDefault(Articulo articuloDefault) {
		this.articuloDefault = articuloDefault;
	}

	public List<Modulo> getServiciosUsuarioLogueado() {
		List<Modulo> modulosServicios = new ArrayList<Modulo>();
		for(Modulo m : getUsuarioSistema().getPerfil().getModulos()){
			if(m.getServicio()){
				modulosServicios.add(m);
			}
		}
		return modulosServicios;
	}
}
