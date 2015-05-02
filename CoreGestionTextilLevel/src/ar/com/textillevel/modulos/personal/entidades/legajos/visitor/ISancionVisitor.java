package ar.com.textillevel.modulos.personal.entidades.legajos.visitor;

import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Apercibimiento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;

public interface ISancionVisitor {

	public void visit(Apercibimiento apercibimiento);
	public void visit(CartaDocumento cd);

}
