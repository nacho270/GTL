package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import main.GTLGlobalCache;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.modulos.odt.gui.tenido.PersisterFormulaHandler;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.facade.api.remote.FormulaClienteFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public abstract class PanelTablaFormula<T extends FormulaCliente> extends PanelTabla<T> {

	private static final long serialVersionUID = 7178532055286935695L;

	protected PersisterFormulaHandler persisterFormulaHandler;
	protected ETipoProducto tipoProducto;
	private JButton btnCopiar;
	private JButton btnImprimir;
	private JButton btnVerificar;
	protected Frame owner;

	public PanelTablaFormula(Frame owner, ETipoProducto tipoProducto, PersisterFormulaHandler persisterFormulaHandler) {
		this.tipoProducto = tipoProducto;
		this.persisterFormulaHandler = persisterFormulaHandler;
		this.owner = owner;
		agregarBotonModificar();
		agregarBoton(getBtnCopiar());
		agregarBoton(getBtnImprimir());
		agregarBoton(getBtnVericar());
		getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = getTabla().getSelectedRow();
				getBtnCopiar().setEnabled(selectedRow != -1 && !modoConsulta);
				getBtnImprimir().setEnabled(selectedRow != -1);
				getBtnVericar().setEnabled(selectedRow != -1 && !modoConsulta);
			}
		});
	}

	public abstract void ocultarBotones();

	public abstract void agregarFormula(T formula);

	public abstract T getFormulaElegida();
	
	public abstract void imprimirFormula(T formula) throws IOException;

	@Override
	public boolean validarQuitar() {
		int selectedRow = getTabla().getSelectedRow();
		if(selectedRow != -1) {
			T formula = getElemento(selectedRow);
			if(formula.getId() != null && formula.getId() > 0) {
				persisterFormulaHandler.addIdFormulaEliminar(formula.getId());
			}
			persisterFormulaHandler.deleteFormulaParaGrabar(tipoProducto, formula);
		}
		return true;
	}

	public JButton getBtnCopiar() {
		if (btnCopiar == null) {
			btnCopiar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_copiar_moderno.png", "ar/com/textillevel/imagenes/b_copiar_moderno_des.png");
			btnCopiar.setEnabled(false);
			btnCopiar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_ID);
					GuiUtil.centrar(dialogSeleccionarCliente);
					dialogSeleccionarCliente.setVisible(true);
					Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
					if (clienteElegido != null) {
						List<FormulaCliente> formulasSeleccionadas = new ArrayList<FormulaCliente>();
						List<String> codigosFormulas = new ArrayList<String>();
						for(int i : getTabla().getSelectedRows()) {
							FormulaCliente elemento = getElemento(i);
							formulasSeleccionadas.add(elemento);
							codigosFormulas.add(elemento.getCodigoFormula());
						}
						if(FWJOptionPane.showQuestionMessage(owner, "Va copiar las siguientes formulas al cliente " + clienteElegido.getRazonSocial() + ":\n\n" + StringUtil.getCadena(codigosFormulas, "\n") + "\n\nDesea continuar?", "Pregunta") == FWJOptionPane.YES_OPTION) {
							try {
								GTLBeanFactory.getInstance().getBean2(FormulaClienteFacadeRemote.class).copiarFormulas(formulasSeleccionadas, clienteElegido, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
								FWJOptionPane.showInformationMessage(owner, "Las formulas de copiaron con exito", "Informacion");
							} catch (ValidacionException e1) {
								FWJOptionPane.showErrorMessage(owner, e1.getMensajeError(), "Error");
							}
						}
					}
				}
			});
		}
		return btnCopiar;
	}

	public JButton getBtnImprimir() {
		if(btnImprimir == null) {
			btnImprimir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imprimir_moderno.png", "ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
			btnImprimir.setEnabled(false);
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getTabla().getSelectedRows() == null || getTabla().getSelectedRows().length == 0) {
						return;
					}
					List<T> formulasSeleccionadas = new ArrayList<T>();
					for(int i : getTabla().getSelectedRows()) {
						T elemento = getElemento(i);
						formulasSeleccionadas.add(elemento);
					}
					try {
						imprimirFormula(formulasSeleccionadas.get(0));
					} catch (IOException e1) {
						FWJOptionPane.showErrorMessage(owner, e1.getMessage(), "Error");						
					}
				}

			});
		}
		return btnImprimir;
	}
	
	public JButton getBtnVericar() {
		if(btnVerificar == null) {
			btnVerificar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_verificar_stock.png", "ar/com/textillevel/imagenes/b_verificar_stock_des.png");
			btnVerificar.setEnabled(false);
			btnVerificar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int[] selectedRows = getTabla().getSelectedRows();
					for(int i : selectedRows) {
						T elemento = getElemento(i);
						boolean value = !calcVerificada(elemento.getVerificada());
						elemento.setVerificada(value);
						getTabla().setValueAt(value, i, getColVerificada());
						persisterFormulaHandler.addFormulaParaGrabar(tipoProducto, elemento);
					}
				}

			});
		}
		return btnVerificar;
	}

	protected abstract int getColVerificada();

	protected boolean calcVerificada(Boolean verificada) {
		return verificada == null ? false : verificada;
	}

}