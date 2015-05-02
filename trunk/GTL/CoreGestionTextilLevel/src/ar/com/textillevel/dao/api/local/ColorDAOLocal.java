package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Color;

@Local
public interface ColorDAOLocal extends DAOLocal<Color, Integer>{
	List<Color> getAllOrderByNameGamaEager();
}
