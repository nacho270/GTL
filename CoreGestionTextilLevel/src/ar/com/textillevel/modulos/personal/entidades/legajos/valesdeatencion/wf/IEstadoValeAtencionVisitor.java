package ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf;

public interface IEstadoValeAtencionVisitor {

	public void visit(ValeAbiertoEstado vae);

	public void visit(ValeJustificadoControlEstado vjce);

	public void visit(ValeJustificadoAltaEstado vjae);

	public void visit(ValeNoJustificadoAltaEstado vnjae);

}
