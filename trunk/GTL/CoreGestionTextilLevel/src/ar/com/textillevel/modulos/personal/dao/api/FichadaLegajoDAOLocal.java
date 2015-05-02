package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;

@Local
public interface FichadaLegajoDAOLocal extends DAOLocal<FichadaLegajo, Integer>{
	public List<FichadaLegajo> getAllByLegajo(Integer idLegajo);
	public List<FichadaLegajo> getAllByLegajoYFecha(Integer idLegajo, Date fechaDesde, Date fechaHasta);
	public Timestamp getFechaHoraUltimaFichada();
}
