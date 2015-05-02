package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.PigmentoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.AnilinaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;

public class CreadorFormulasVisitor implements IFormulaClienteVisitor {

	private FormulaClienteExplotada formulaExplotada;
	private final OrdenDeTrabajo odt;
	
	public CreadorFormulasVisitor(OrdenDeTrabajo odt){
		this.odt = odt;
	}

	public void visit(FormulaTenidoCliente formula) {
		FormulaTenidoClienteExplotada ftcExplotada = new FormulaTenidoClienteExplotada();
		ftcExplotada.setFormulaDesencadenante(formula);
		for (TenidoTipoArticulo tta : formula.getTenidosComponentes()) {
			for (AnilinaCantidad ac : tta.getAnilinasCantidad()) {
				MateriaPrimaCantidadExplotada<Anilina> mpce = new MateriaPrimaCantidadExplotada<Anilina>();
				mpce.setCantidadExplotada(ac.getCantidad() * odt.getRemito().getPesoTotal().floatValue() / 100);
				mpce.setMateriaPrimaCantidadDesencadenante(ac);
				mpce.setTipoArticulo(tta.getTipoArticulo());
				ftcExplotada.getMateriasPrimas().add(mpce);
			}
		}
		setFormulaExplotada(ftcExplotada);
	}

	public void visit(FormulaEstampadoCliente formula) {
		FormulaEstampadoClienteExplotada ftcExplotada = new FormulaEstampadoClienteExplotada();
		ftcExplotada.setFormulaDesencadenante(formula);
		for (PigmentoCantidad pc : formula.getPigmentos()) {
			MateriaPrimaCantidadExplotada<Pigmento> mpcp = new MateriaPrimaCantidadExplotada<Pigmento>();
			mpcp.setCantidadExplotada(pc.getCantidad() * odt.getRemito().getPesoTotal().floatValue() / 100);
			mpcp.setMateriaPrimaCantidadDesencadenante(pc);
			//					mpce.setTipoArticulo(formula.get);
			ftcExplotada.getPigmentos().add(mpcp);
		}

		for (QuimicoCantidad qc : formula.getQuimicos()) {
			MateriaPrimaCantidadExplotada<Quimico> mpcq = new MateriaPrimaCantidadExplotada<Quimico>();
			mpcq.setCantidadExplotada(qc.getCantidad() * odt.getRemito().getPesoTotal().floatValue() / 100);
			mpcq.setMateriaPrimaCantidadDesencadenante(qc);
			//					mpce.setTipoArticulo(formula.get);
			ftcExplotada.getQuimicos().add(mpcq);
		}
		setFormulaExplotada(ftcExplotada);
	}

	public FormulaClienteExplotada getFormulaExplotada() {
		return formulaExplotada;
	}

	public void setFormulaExplotada(FormulaClienteExplotada formulaExplotada) {
		this.formulaExplotada = formulaExplotada;
	}

	public void visit(FormulaAprestadoCliente formulaAprestado) {
		// TODO Auto-generated method stub
	}

}
