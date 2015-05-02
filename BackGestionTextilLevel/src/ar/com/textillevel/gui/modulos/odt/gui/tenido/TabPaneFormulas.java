package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.awt.Frame;
import java.util.List;

import javax.swing.JTabbedPane;

import ar.com.textillevel.gui.modulos.odt.gui.PanTablaFormulasTenido;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaQuimicos;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaVisualizacionFormulaCliente;
import ar.com.textillevel.gui.modulos.odt.gui.PanelTablaFormula;
import ar.com.textillevel.gui.modulos.odt.gui.aprestado.PanTablaFormulasAprestado;
import ar.com.textillevel.gui.modulos.odt.gui.estampado.PanTablaFormulasEstampado;
import ar.com.textillevel.gui.modulos.odt.gui.estampado.PanTablaQuimicosPigmentosVisualizacion;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.IFormulaClienteVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;

public class TabPaneFormulas extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private PanContenedorFormula<FormulaTenidoCliente> panFormulaTenido;
	private PanContenedorFormula<FormulaEstampadoCliente> panFormulaEstampado;
	private PanContenedorFormula<FormulaAprestadoCliente> panFormulaAprestado;
	private FormulaPorPanelDivisorVisitor formulaDivisorVisitor;
	private PersisterFormulaHandler persisterFormulaHandler;
	private Frame owner;

	public TabPaneFormulas(Frame owner, boolean modoConsulta) {
		this.owner = owner;
		this.persisterFormulaHandler = new PersisterFormulaHandler();
		this.panFormulaEstampado = new PanContenedorFormula<FormulaEstampadoCliente>(owner, modoConsulta)  {

			private static final long serialVersionUID = 69248108623035223L;

			@Override
			protected PanTablaVisualizacionFormulaCliente createPanMateriaPrimaCantidad() {
				return new PanTablaQuimicosPigmentosVisualizacion();
			}

			@Override
			protected PanelTablaFormula<FormulaEstampadoCliente> createPanFormula() {
				return new PanTablaFormulasEstampado(TabPaneFormulas.this.owner, persisterFormulaHandler);
			}

		};

		((PanTablaFormulasEstampado)panFormulaEstampado.getPanFormulas()).setPanVisualizacionQuimicosPigmentos((PanTablaQuimicosPigmentosVisualizacion)panFormulaEstampado.getPanMateriaPrima());

		this.panFormulaTenido = new PanContenedorFormula<FormulaTenidoCliente>(owner, modoConsulta) {

			private static final long serialVersionUID = 179866514948151333L;

			@Override
			protected PanTablaVisualizacionFormulaCliente createPanMateriaPrimaCantidad() {
				return new PanTablaQuimicos();
			}

			@Override
			protected PanelTablaFormula<FormulaTenidoCliente> createPanFormula() {
				return new PanTablaFormulasTenido(TabPaneFormulas.this.owner, persisterFormulaHandler);
			}

		};

		((PanTablaFormulasTenido)panFormulaTenido.getPanFormulas()).setPanQuimicos((PanTablaQuimicos)panFormulaTenido.getPanMateriaPrima());

		this.panFormulaAprestado = new PanContenedorFormula<FormulaAprestadoCliente>(owner, modoConsulta) {

			private static final long serialVersionUID = -8709329590562647195L;

			@Override
			protected PanelTablaFormula<FormulaAprestadoCliente> createPanFormula() {
				return new PanTablaFormulasAprestado(TabPaneFormulas.this.owner, persisterFormulaHandler);
			}

			@Override
			protected PanTablaVisualizacionFormulaCliente createPanMateriaPrimaCantidad() {
				return new PanTablaQuimicosPigmentosVisualizacion();
			}

		};
		((PanTablaFormulasAprestado)panFormulaAprestado.getPanFormulas()).setPanVisualizacionQuimicosPigmentos((PanTablaQuimicosPigmentosVisualizacion)panFormulaAprestado.getPanMateriaPrima());

		this.formulaDivisorVisitor = new FormulaPorPanelDivisorVisitor();
		construct();
	}

	private void construct() {
		add("TEÑIDO", panFormulaTenido);
		formulaDivisorVisitor.setPanContenedorTenido(panFormulaTenido);
		add("ESTAMPADO", panFormulaEstampado);
		formulaDivisorVisitor.setPanContenedorEstampado(panFormulaEstampado);
		add("APRESTADO", panFormulaAprestado);
		formulaDivisorVisitor.setPanContenedorAprestado(panFormulaAprestado);
	}

	public void llenarTabs(List<FormulaCliente> formulas) {
		panFormulaEstampado.limpiar();
		panFormulaTenido.limpiar();
		panFormulaAprestado.limpiar();
		for(FormulaCliente fc : formulas) {
			fc.accept(formulaDivisorVisitor);
		}
	}

	public void setModoConsulta(boolean modoConsulta) {
		panFormulaEstampado.setModoConsulta(modoConsulta);
		panFormulaTenido.setModoConsulta(modoConsulta);
		panFormulaAprestado.setModoConsulta(modoConsulta);
	}

	public List<FormulaCliente> getFormulas() {
		return getPersisterFormulaHandler().getFormulasToSave();
	}

	public PersisterFormulaHandler getPersisterFormulaHandler() {
		return persisterFormulaHandler;
	}

	private class FormulaPorPanelDivisorVisitor implements IFormulaClienteVisitor {

		private PanContenedorFormula<FormulaTenidoCliente> panContenedorTenido;
		private PanContenedorFormula<FormulaEstampadoCliente> panContenedorEstampado;
		private PanContenedorFormula<FormulaAprestadoCliente> panContenedorAprestado;

		public void setPanContenedorTenido(PanContenedorFormula<FormulaTenidoCliente> panContenedorTenido) {
			this.panContenedorTenido = panContenedorTenido;
		}

		public void setPanContenedorAprestado(PanContenedorFormula<FormulaAprestadoCliente> panFormulaAprestado) {
			this.panContenedorAprestado = panFormulaAprestado;
		}

		public void setPanContenedorEstampado(PanContenedorFormula<FormulaEstampadoCliente> panContenedorEstampado) {
			this.panContenedorEstampado = panContenedorEstampado;
		}

		public void visit(FormulaTenidoCliente formulaTenido) {
			panContenedorTenido.agregarFormula(formulaTenido);
		}

		public void visit(FormulaEstampadoCliente formulaEstampado) {
			panContenedorEstampado.agregarFormula(formulaEstampado);
		}

		public void visit(FormulaAprestadoCliente formulaAprestado) {
			panContenedorAprestado.agregarFormula(formulaAprestado);
		}

	}

}