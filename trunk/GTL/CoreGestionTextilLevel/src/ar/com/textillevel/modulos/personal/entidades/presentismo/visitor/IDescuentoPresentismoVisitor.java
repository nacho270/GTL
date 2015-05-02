package ar.com.textillevel.modulos.personal.entidades.presentismo.visitor;

import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoPorAusencia;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoRangoMinutos;

public interface IDescuentoPresentismoVisitor {
	public void visit(DescuentoPresentismoRangoMinutos descuento);
	public void visit(DescuentoPresentismoPorAusencia descuento);
}
