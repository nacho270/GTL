package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;

@Local
public interface GamaColorDAOLocal extends DAOLocal<GamaColor, Integer>{
}
