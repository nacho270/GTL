package ar.com.textillevel.modulos.personal.utils;

import java.sql.Date;
import java.sql.Timestamp;

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaParcial;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaVigencia;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.EMotivoAntifichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

public class AntiFichadaFactory {
	
	public static AntiFichada createAntiFichadaVigencia(EMotivoAntifichada tipo, Date fechaDesde, Date fechaHasta, LegajoEmpleado legajo, Boolean justificada){
		AntiFichadaVigencia afv = new AntiFichadaVigencia();
		afv.setMotivoAntifichada(tipo);
		afv.setFechaDesde(fechaDesde);
		afv.setFechaHasta(fechaHasta);
		afv.setJustificada(justificada);
		afv.setLegajo(legajo);
		return afv;
	}
	
	public static AntiFichada createAntiFichadaParcial(EMotivoAntifichada tipo, Timestamp fechaHora, LegajoEmpleado legajo, Boolean justificada, Boolean entrada){
		AntiFichadaParcial afp = new AntiFichadaParcial();
		afp.setMotivoAntifichada(tipo);
		afp.setEntrada(entrada);
		afp.setFechaHora(fechaHora);
		afp.setJustificada(justificada);
		afp.setLegajo(legajo);
		return afp;
	}
}
