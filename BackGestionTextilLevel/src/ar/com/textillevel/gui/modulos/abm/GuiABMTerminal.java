package ar.com.textillevel.gui.modulos.abm;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.terminal.entidades.ModuloTerminal;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;
import ar.com.textillevel.modulos.terminal.facade.api.remote.ModuloTerminalFacadeRemote;
import ar.com.textillevel.modulos.terminal.facade.api.remote.TerminalFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMTerminal extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1840795762852110775L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final int MAX_LONGITUD_CODIGO = 5;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombre;
	private FWJTextField txtIp;
	private FWJTextField txtCodigo;
	private JComboBox cmbModulosTerminal;

	private TerminalFacadeRemote terminalFacade;
	private ModuloTerminalFacadeRemote moduloTerminalFacade;
	private Terminal terminal;

	public GuiABMTerminal(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Terminales");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la terminal", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre:"), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Código:"), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCodigo(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("IP:"), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtIp(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("Módulo:"), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbModulosTerminal(), createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

		}
		return panDetalle;
	}

	private FWJTextField getTxtCodigo() {
		if (txtCodigo == null) {
			txtCodigo = new FWJTextField(MAX_LONGITUD_CODIGO);
		}
		return txtCodigo;
	}

	private FWJTextField getTxtNombre() {
		if (txtNombre == null) {
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	private FWJTextField getTxtIp() {
		if (txtIp == null) {
			txtIp = new FWJTextField();
		}
		return txtIp;
	}

	public JComboBox getCmbModulosTerminal() {
		if (cmbModulosTerminal == null) {
			cmbModulosTerminal = new JComboBox();
			GuiUtil.llenarCombo(cmbModulosTerminal, getModuloTerminalFacade().getAll(), true);
		}
		return cmbModulosTerminal;
	}

	@Override
	public void cargarLista() {
		List<Terminal> terminalList = getTerminalFacade().getAll();
		lista.removeAll();
		for (Terminal terminal : terminalList) {
			lista.addItem(terminal);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {

	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarSetearDatos();
			Terminal terminalRefresh = getTerminalFacade().save(getTerminalActual());
			lista.setSelectedValue(terminalRefresh, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if (getTxtNombre().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del sector.", this.getTitle());
			getTxtNombre().requestFocus();
			return false;
		}
		if (getTxtCodigo().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el Código.", this.getTitle());
			getTxtCodigo().requestFocus();
			return false;
		}

		if (getTxtIp().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el IP.", this.getTitle());
			getTxtIp().requestFocus();
			return false;
		}

		if (getCmbModulosTerminal().getSelectedIndex() < 0) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un módulo.", this.getTitle());
			return false;
		}

		if (getTerminalFacade().existeNombre(getTerminalActual().getId(), getTxtNombre().getText())) {
			FWJOptionPane.showErrorMessage(this, "Ya existe una terminal con el nombre ingresado.", this.getTitle());
			getTxtNombre().requestFocus();
			return false;
		}
		
		if (getTerminalFacade().existeCodigo(getTerminalActual().getId(), getTxtCodigo().getText())) {
			FWJOptionPane.showErrorMessage(this, "Ya existe una terminal con el código ingresado.", this.getTitle());
			getTxtCodigo().requestFocus();
			return false;
		}

		if (getTerminalFacade().existeIp(getTerminalActual().getId(), getTxtIp().getText())) {
			FWJOptionPane.showErrorMessage(this, "Ya existe una terminal con el IP ingresado.", this.getTitle());
			getTxtIp().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getTerminalActual().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getTerminalActual().setCodigo(getTxtCodigo().getText().trim().toUpperCase());
		getTerminalActual().setIp(getTxtIp().getText());
		getTerminalActual().setModuloPorDefecto((ModuloTerminal) getCmbModulosTerminal().getSelectedItem());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			setTerminalActual(getTerminalFacade().getById(getTerminalActual().getId()));
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una terminal", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		Terminal terminalActual = (Terminal) lista.getSelectedValue();
		terminalActual = getTerminalFacade().getById(terminalActual.getId());
		setTerminalActual(terminalActual);
		limpiarDatos();
		if (getTerminalActual() != null) {
			getTxtNombre().setText(getTerminalActual().getNombre());
			getTxtIp().setText(getTerminalActual().getIp());
			getTxtCodigo().setText(getTerminalActual().getCodigo());
			getCmbModulosTerminal().setSelectedItem(getTerminalActual().getModuloPorDefecto());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtIp().setText("");
		getTxtNombre().setText("");
		getTxtCodigo().setText("");
		getCmbModulosTerminal().setSelectedIndex(0);
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	public TerminalFacadeRemote getTerminalFacade() {
		if (terminalFacade == null) {
			terminalFacade = GTLBeanFactory.getInstance().getBean2(TerminalFacadeRemote.class);
		}
		return terminalFacade;
	}

	public ModuloTerminalFacadeRemote getModuloTerminalFacade() {
		if (moduloTerminalFacade == null) {
			moduloTerminalFacade = GTLBeanFactory.getInstance().getBean2(ModuloTerminalFacadeRemote.class);
		}
		return moduloTerminalFacade;
	}

	public Terminal getTerminalActual() {
		return terminal;
	}

	public void setTerminalActual(Terminal tipoMaquina) {
		this.terminal = tipoMaquina;
	}
}