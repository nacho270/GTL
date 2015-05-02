package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

public interface IInstruccionProcedimientoVisitor {
	public void visit(InstruccionProcedimientoPasadas instruccion);
	public void visit(InstruccionProcedimientoTexto instruccion);
	public void visit(InstruccionProcedimientoTipoProducto instruccion);
}
