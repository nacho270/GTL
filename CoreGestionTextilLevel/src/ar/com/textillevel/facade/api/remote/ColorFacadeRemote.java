package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.articulos.Color;

@Remote
public interface ColorFacadeRemote {
	public Color save(Color color);
	public void remove(Color color);
	public List<Color> getAllOrderByName();
	public List<Color> getAllOrderByNameGamaEager();
}
