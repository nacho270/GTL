package ar.com.textillevel.gui.modulos.odt.gui.tenido;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaFormulasTenido;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaQuimicos;
import ar.com.textillevel.gui.modulos.odt.gui.PanTablaVisualizacionFormulaCliente;
import ar.com.textillevel.gui.modulos.odt.gui.PanelTablaFormula;
import ar.com.textillevel.gui.modulos.odt.gui.aprestado.PanTablaFormulasAprestado;
import ar.com.textillevel.gui.modulos.odt.gui.estampado.PanTablaFormulasEstampado;
import ar.com.textillevel.gui.modulos.odt.gui.estampado.PanTablaQuimicosPigmentosVisualizacion;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.IFormulaClienteVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.util.GTLBeanFactory;

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

			@Override
			public void sort() {
			}

			@Override
			protected JPanel getPanFiltros() {
				return null;
			}

			@Override
			protected void limpiarFiltros() {
			}

		};

		((PanTablaFormulasEstampado)panFormulaEstampado.getPanFormulas()).setPanVisualizacionQuimicosPigmentos((PanTablaQuimicosPigmentosVisualizacion)panFormulaEstampado.getPanMateriaPrima());

		this.panFormulaTenido = new PanContenedorFormula<FormulaTenidoCliente>(owner, modoConsulta) {
			
			

			private static final long serialVersionUID = 179866514948151333L;
			
			private JComboBox cmbTipoArticulo;
			private JPanel panFiltros;
			private List<FormulaTenidoCliente> formulasCopy = new ArrayList<FormulaTenidoCliente>(); //para filtrar en el combo 

			@Override
			protected PanTablaVisualizacionFormulaCliente createPanMateriaPrimaCantidad() {
				return new PanTablaQuimicos();
			}

			@Override
			protected PanelTablaFormula<FormulaTenidoCliente> createPanFormula() {
				return new PanTablaFormulasTenido(TabPaneFormulas.this.owner, persisterFormulaHandler);
			}

			@Override
			public void sort() {
				List<FormulaTenidoCliente> formulas = getPanFormulas().getElementos();
				Collections.sort(formulas, new Comparator<FormulaTenidoCliente>() {

					public int compare(FormulaTenidoCliente o1, FormulaTenidoCliente o2) {
						int compResult = o1.getColor().getNombre().compareTo(o2.getColor().getNombre());
						if(compResult == 0) {
							compResult = o1.getTipoArticulo().getNombre().compareTo(o2.getTipoArticulo().getNombre());
							if(compResult == 0) {
								return (o1.getNombre() == null ? "":o1.getNombre()).compareTo(o2.getNombre() == null ? "":o2.getNombre());
							}
						}
						return compResult;
					}

				});
				getPanFormulas().limpiar();
				getPanFormulas().agregarElementos(formulas);
				this.formulasCopy = new ArrayList<FormulaTenidoCliente>(formulas);
			}

			@Override
			protected JPanel getPanFiltros() {
				if(panFiltros == null) {
					panFiltros = new JPanel();
					panFiltros.setLayout(new GridBagLayout());
					panFiltros.add(new JLabel("Tipo de Artículo:"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0,0,0), 1, 1, 1, 0.5));
					panFiltros.add(getCmbTipoArticulo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,0), 1, 1, 1, 0.5));
				}
				return panFiltros;
			}

			private JComboBox getCmbTipoArticulo() {
				if(cmbTipoArticulo == null) {
					cmbTipoArticulo = new JComboBox();
					GuiUtil.llenarCombo(cmbTipoArticulo, GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class).getAllTipoArticulos(), true);
					cmbTipoArticulo.insertItemAt("TODOS", 0);
					cmbTipoArticulo.setSelectedIndex(0);

					cmbTipoArticulo.addItemListener(new ItemListener() {
						
						public void itemStateChanged(ItemEvent e) {
							if(e.getStateChange() == ItemEvent.SELECTED) {
								getPanFormulas().limpiar();
								if(cmbTipoArticulo.getSelectedIndex() == 0) {
									getPanFormulas().agregarElementos(formulasCopy);
								} else {
									TipoArticulo tp = (TipoArticulo)cmbTipoArticulo.getSelectedItem();
									List<FormulaTenidoCliente> formulasTipoArticulo = new ArrayList<FormulaTenidoCliente>();
									for(FormulaTenidoCliente f : formulasCopy) {
										if(f.getTipoArticulo().equals(tp)) {
											formulasTipoArticulo.add(f);
										}
									}
									getPanFormulas().agregarElementos(formulasTipoArticulo);
								}
							}
						}
					});
				}
				return cmbTipoArticulo;
			}

			@Override
			protected void limpiarFiltros() {
				cmbTipoArticulo.setSelectedIndex(0);
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

			@Override
			public void sort() {
			}

			@Override
			protected JPanel getPanFiltros() {
				return null;
			}

			@Override
			protected void limpiarFiltros() {
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
		panFormulaEstampado.sort();
		panFormulaAprestado.sort();
		panFormulaTenido.sort();
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