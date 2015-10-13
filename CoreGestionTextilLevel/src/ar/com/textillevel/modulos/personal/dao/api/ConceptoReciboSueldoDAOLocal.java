package ar.com.textillevel.modulos.personal.dao.api;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;

@Local
public interface ConceptoReciboSueldoDAOLocal extends DAOLocal<ConceptoReciboSueldo, Integer>{

	public List<ConceptoReciboSueldo> getAllBySindicato(Sindicato sindicato);

}
