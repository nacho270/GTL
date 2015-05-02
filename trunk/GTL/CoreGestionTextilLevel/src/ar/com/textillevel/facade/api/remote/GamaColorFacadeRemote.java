package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.articulos.GamaColor;

@Remote
public interface GamaColorFacadeRemote {
	public GamaColor save(GamaColor gama);
	public void remove(GamaColor gama);
	public List<GamaColor> getAllOrderByName();
}
