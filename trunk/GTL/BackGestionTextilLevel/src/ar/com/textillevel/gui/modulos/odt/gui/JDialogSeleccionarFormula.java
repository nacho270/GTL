package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PanContenedorFormula;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PersisterFormulaHandler;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.facade.api.remote.FormulaTenidoClienteFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarFormula extends JDialog {

	private static final long serialVersionUID = 1137347910405474515L;

	private PanContenedorFormula<FormulaTenidoCliente> panelFormulas;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private boolean acepto;
	private FormulaTenidoCliente formulaElegida;

	private FormulaTenidoClienteFacadeRemote formulaFacade;

	private final Frame padre;
	private Cliente cliente;
	private TipoArticulo tipoArticulo;
	private Color color;
	
	private PersisterFormulaHandler persisterFormulaHandler;

	public JDialogSeleccionarFormula(Frame padre, Cliente cliente, Color color, TipoArticulo tipoArticulo) {
		super(padre);
		this.padre = padre;
		this.color = color;
		this.persisterFormulaHandler = new PersisterFormulaHandler();
		setCliente(cliente);
		setTipoArticulo(tipoArticulo);
		setAcepto(false);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Seleccionar f�rmula");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(670, 500));
		GuiUtil.centrar(this);
		setModal(true);
		setResizable(false);
	}

	private void setUpComponentes() {
		add(getPanelFormulas(), BorderLayout.CENTER);
		JPanel panSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panSur.add(getBtnAceptar());
		panSur.add(getBtnCancelar());
		add(panSur, BorderLayout.SOUTH);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public FormulaTenidoCliente getFormulaElegida() {
		return formulaElegida;
	}

	public void setFormulaElegida(FormulaTenidoCliente formulaElegida) {
		this.formulaElegida = formulaElegida;
	}

	public PanContenedorFormula<FormulaTenidoCliente> getPanelFormulas() {
		if (panelFormulas == null) {
			panelFormulas = new PanContenedorFormula<FormulaTenidoCliente>(padre, false) {

				private static final long serialVersionUID = 5136337104268619725L;

				@Override
				protected PanelTablaFormula<FormulaTenidoCliente> createPanFormula() {
					PanTablaFormulasTenido panTablaFormulasTenido = new PanTablaFormulasTenido(padre, persisterFormulaHandler);
					panTablaFormulasTenido.setColorFixed(color);
					panTablaFormulasTenido.setTipoArticuloFixed(tipoArticulo);
					return panTablaFormulasTenido;
				}

				@Override
				protected PanTablaVisualizacionFormulaCliente createPanMateriaPrimaCantidad() {
					return new PanTablaQuimicos();
				}

			};

			((PanTablaFormulasTenido)panelFormulas.getPanFormulas()).setPanQuimicos((PanTablaQuimicos)panelFormulas.getPanMateriaPrima());

			panelFormulas.setFormulas(getFormulaFacade().getAllByIdClienteOrDefaultAndTipoArticulo(getCliente().getId(), getTipoArticulo(), color));
			panelFormulas.ocultarBotonesTabla();
		}
		return panelFormulas;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					FormulaTenidoCliente formulaSeleccionada = getPanelFormulas().getFormulaElegida();
					if (formulaSeleccionada == null) {
						CLJOptionPane.showErrorMessage(JDialogSeleccionarFormula.this, "Debe elegir una formula.", "Error");
						return;
					}

					//recolecto las formulas
					boolean existeUnaTransient = false;
					List<FormulaCliente> formulas = persisterFormulaHandler.getFormulasToSave();
					for(FormulaCliente ftc : formulas) {
						if(ftc.getId() == null) {
							existeUnaTransient = true;
						}
						ftc.setCliente(cliente);
					}

					if(persisterFormulaHandler.getFormulasToSave().isEmpty()) {
						setFormulaElegida(formulaSeleccionada);
					} else {
						//si se crearon f�rmulas entonces las grabo
						List<FormulaCliente> formulasGrabadas = null;
						boolean grabarTambienComoDefault = existeUnaTransient && CLJOptionPane.showQuestionMessage(JDialogSeleccionarFormula.this, StringW.wordWrap("�Desea que la(s) f�rmula(s) agregada(s) tambi�n se asocie(n) al cliente default?"), "Confirmaci�n") == CLJOptionPane.YES_OPTION;
						try {
							formulasGrabadas = getFormulaFacade().saveFormulas(persisterFormulaHandler.getFormulasToSave(), persisterFormulaHandler.getIdsFormulasToBorrar(), grabarTambienComoDefault, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						} catch (ValidacionException e1) {
							CLJOptionPane.showErrorMessage(JDialogSeleccionarFormula.this, StringW.wordWrap(e1.getMensajeError()), "Error");
							return;
						}
						
						FormulaTenidoCliente formulaElegida = null;
						for(FormulaCliente fc : formulasGrabadas) {
							if(fc.getNombre().compareToIgnoreCase(formulaSeleccionada.getNombre()) == 0 && fc.getCliente() != null) {
								formulaElegida = (FormulaTenidoCliente)fc;
							}
						}
						setFormulaElegida(formulaElegida);
					}
					
					persisterFormulaHandler.clear();
					
					setAcepto(true);
					dispose();
					
					
					
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					persisterFormulaHandler.clear();
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public FormulaTenidoClienteFacadeRemote getFormulaFacade() {
		if (formulaFacade == null) {
			formulaFacade = GTLBeanFactory.getInstance().getBean2(FormulaTenidoClienteFacadeRemote.class);
		}
		return formulaFacade;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

}
