package ar.com.textillevel.modulos.personal.facade.api.local;

import java.sql.Timestamp;

import javax.ejb.Local;

import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;

@Local
public interface FichadaLegajoFacadeLocal {
	public FichadaLegajo save(FichadaLegajo fichada);

	public Timestamp getFechaHoraUltimaFichada();
}
