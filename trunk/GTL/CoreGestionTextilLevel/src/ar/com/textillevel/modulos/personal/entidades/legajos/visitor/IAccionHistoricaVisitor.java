package ar.com.textillevel.modulos.personal.entidades.legajos.visitor;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;

@SuppressWarnings("rawtypes")
public interface IAccionHistoricaVisitor {

	public void visit(AccionValeAtencion accva);
	public void visit(AccionSancion accs);

}
