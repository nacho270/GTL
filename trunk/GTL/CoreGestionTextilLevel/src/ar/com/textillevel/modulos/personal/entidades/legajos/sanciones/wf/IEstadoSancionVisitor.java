package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf;

public interface IEstadoSancionVisitor {

	public void visit(SancionCreadaEstado sce);
	public void visit(SancionImpresaEstado sie);
	public void visit(SancionEnviadaEstado see);
	public void visit(SancionRecibidaEstado see);
	public void visit(SancionNoRecibidaEstado snre);
	public void visit(SancionJustificadaEstado sje);

}
