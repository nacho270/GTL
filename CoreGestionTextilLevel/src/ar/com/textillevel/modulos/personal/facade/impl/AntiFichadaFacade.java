package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.AntiFichadaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaParcial;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaVigencia;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.EMotivoAntifichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.facade.api.local.AntiFichadaFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.AntiFichadaFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.AntiFichadaFactory;

@Stateless
public class AntiFichadaFacade implements AntiFichadaFacadeLocal, AntiFichadaFacadeRemote{
	
	@EJB
	private AntiFichadaDAOLocal antiFichadasDao;

	public List<AntiFichada> getAntifichadasParaFecha(Date fecha, LegajoEmpleado legajo) {
		List<AntiFichadaVigencia> afvs = antiFichadasDao.getAntiFichadasDeVigenciaPorFechaYEmpleado(fecha, legajo);
		List<AntiFichadaParcial> afps = antiFichadasDao.getAntiFichadasParcialesPorFechaYEmpleado(fecha, legajo);
		List<AntiFichada> listaRet = new ArrayList<AntiFichada>();
		if(afvs!=null){
			listaRet.addAll(afvs);
		}
		if(afps!=null){
			listaRet.addAll(afps);
		}
		return listaRet;
	}

	public void crearAntiFichadas(EMotivoAntifichada tipo, Date fechaDesde, Date fechaHasta, LegajoEmpleado legajo, Boolean justificada) {
		antiFichadasDao.save(AntiFichadaFactory.createAntiFichadaVigencia(tipo, fechaDesde, fechaHasta, legajo, justificada));
	} 
}
