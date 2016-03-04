package ar.com.textillevel.gui.modulos.odt.gui;

import java.util.HashMap;
import java.util.Map;

import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.CreadorFormulasVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.IFormulaClienteExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.IInstruccionProcedimientoVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTexto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoPasadasODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTextoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTipoProductoODT;

public class ExplotadorInstrucciones implements IInstruccionProcedimientoVisitor {

	private InstruccionProcedimientoODT instruccionExplotada;
	private final OrdenDeTrabajo odt;
	
	private final Map<MateriaPrima, Float> mapaStock;
	
	public ExplotadorInstrucciones(OrdenDeTrabajo odt) {
		this.odt = odt;
		mapaStock = new HashMap<MateriaPrima, Float>();
	}

	public void visit(InstruccionProcedimientoPasadas instruccion) {
		InstruccionProcedimientoPasadasODT nuevo = new InstruccionProcedimientoPasadasODT();
		nuevo.setAccion(instruccion.getAccion());
		nuevo.setCantidadPasadas(instruccion.getCantidadPasadas());
		nuevo.setTemperatura(instruccion.getTemperatura());
		nuevo.setVelocidad(instruccion.getVelocidad());
		nuevo.setSectorMaquina(instruccion.getSectorMaquina());
		for (QuimicoCantidad qc : instruccion.getQuimicos()) {
			float cantidad = qc.getCantidad() * odt.getRemito().getPesoTotal().floatValue();
			updateStockInfo(qc.getMateriaPrima(), cantidad);
			MateriaPrimaCantidadExplotada<Quimico> mpce = new MateriaPrimaCantidadExplotada<Quimico>();
			mpce.setCantidadExplotada(cantidad);
			mpce.setMateriaPrimaCantidadDesencadenante(qc);
			nuevo.getQuimicosExplotados().add(mpce);
		}
		setInstruccionExplotada(nuevo);
	}

	public void visit(InstruccionProcedimientoTexto instruccion) {
		InstruccionProcedimientoTextoODT ipt = new InstruccionProcedimientoTextoODT();
		ipt.setEspecificacion(instruccion.getEspecificacion());
		ipt.setSectorMaquina(instruccion.getSectorMaquina());
		setInstruccionExplotada(ipt);
	}

	public void visit(InstruccionProcedimientoTipoProducto instruccion) {
		InstruccionProcedimientoTipoProductoODT itpExplotada = new InstruccionProcedimientoTipoProductoODT();
		itpExplotada.setTipoArticulo(instruccion.getTipoArticulo());
		itpExplotada.setTipoProducto(instruccion.getTipoProducto());
		itpExplotada.setSectorMaquina(instruccion.getSectorMaquina());
		FormulaCliente formula = instruccion.getFormulaTransient();
		if (formula == null) {
			setInstruccionExplotada(itpExplotada);
			return;
		}
		CreadorFormulasVisitor creadorFormula = new CreadorFormulasVisitor(odt);
		formula.accept(creadorFormula);
		FormulaClienteExplotada formulaExplotada = creadorFormula.getFormulaExplotada();
		itpExplotada.setFormula(formulaExplotada);
		setInstruccionExplotada(itpExplotada);
		StockCheckerFormulaClienteVisitorVisitor stockChecker = new StockCheckerFormulaClienteVisitorVisitor();
		formulaExplotada.accept(stockChecker);
	}

	public InstruccionProcedimientoODT getInstruccionExplotada() {
		return instruccionExplotada;
	}

	public void setInstruccionExplotada(InstruccionProcedimientoODT instruccionExplotada) {
		this.instruccionExplotada = instruccionExplotada;
	}
	
	private void updateStockInfo(MateriaPrima mp, Float cantidad){
		if(mapaStock.get(mp)==null){
			mapaStock.put(mp, 0f);
		}
		mapaStock.put(mp, mapaStock.get(mp) + cantidad);
	}
	
	private class StockCheckerFormulaClienteVisitorVisitor implements IFormulaClienteExplotadaVisitor{

		public void visit(FormulaEstampadoClienteExplotada fece) {
			for(MateriaPrimaCantidadExplotada<Pigmento> mpe : fece.getPigmentos()){
				updateStockInfo(mpe.getMateriaPrimaCantidadDesencadenante().getMateriaPrima(), mpe.getCantidadExplotada());
			}
			for(MateriaPrimaCantidadExplotada<Quimico> mpe : fece.getQuimicos()){
				updateStockInfo(mpe.getMateriaPrimaCantidadDesencadenante().getMateriaPrima(), mpe.getCantidadExplotada());
			}
		}

		public void visit(FormulaTenidoClienteExplotada ftce) {
			for(MateriaPrimaCantidadExplotada<Anilina> mpe : ftce.getMateriasPrimas()){
				updateStockInfo(mpe.getMateriaPrimaCantidadDesencadenante().getMateriaPrima(), mpe.getCantidadExplotada());
			}
		}
	}

	
	public Map<MateriaPrima, Float> getMapaStock() {
		return mapaStock;
	}
}
