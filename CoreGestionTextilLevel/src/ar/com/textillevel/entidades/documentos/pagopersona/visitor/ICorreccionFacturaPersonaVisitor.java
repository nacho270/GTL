package ar.com.textillevel.entidades.documentos.pagopersona.visitor;

import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;

public interface ICorreccionFacturaPersonaVisitor {

	public abstract void visit(NotaDebitoPersona ndp);

}
