package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

public interface IInstruccionProcedimientoODTVisitor {
	public void visit(InstruccionProcedimientoPasadasODT instruccion);
	public void visit(InstruccionProcedimientoTextoODT instruccion);
	public void visit(InstruccionProcedimientoTipoProductoODT instruccion);
}
