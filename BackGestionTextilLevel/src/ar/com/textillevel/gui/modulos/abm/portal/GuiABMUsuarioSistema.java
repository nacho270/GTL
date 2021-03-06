package ar.com.textillevel.gui.modulos.abm.portal;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.PerfilFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.passwordanalyzer.EPasswordStrength;
import ar.com.textillevel.gui.util.passwordanalyzer.PasswordAnalyzer;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMUsuarioSistema extends GuiABMListaTemplate{

	private static final long serialVersionUID = -4715334346688072145L;
	
	private static final int MAX_LONGITUD_NOMBRE = 20;
	
	private static final String TIPS_PASSWORD = "<html>Tips para crear el password:<br><ul><li>Debe tener mas de 8 caracteres</li>" +
			"<li>Debe tener combinacion de mayusculas y minusculas</li><li>Debe contener al menos uno de los siguientes caracteres: ! @ # $ % ^ & * ? _ ~</li></ul></html>";
		
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private FWJTextField txtUserName;
	private FWJNumericTextField txtCodigoUsuario;
	private JPasswordField txtPassword;
	private JComboBox cmbPerfiles;
	private JLabel lblPassStrength;
	private JCheckBox chkCambiarPass;
	
	private UsuarioSistema usuarioActual;
	
	private UsuarioSistemaFacadeRemote usuarioFacade;
	private PerfilFacadeRemote perfilFacade;

	private boolean editando;
	
	public GuiABMUsuarioSistema(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar usuarios");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Informaci�n del usuario", getTabDetalle());
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
			int indiceY = 0;
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, indiceY, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panDetalle.add(getTxtUserName(), GenericUtils.createGridBagConstraints(1, indiceY++, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 2, 1, 1, 0));
			panDetalle.add(new JLabel("Password: "), GenericUtils.createGridBagConstraints(0, indiceY, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panDetalle.add(getTxtPassword(), GenericUtils.createGridBagConstraints(1, indiceY, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1,1 , 1, 0));
			panDetalle.add(getLblPassStrength(), GenericUtils.createGridBagConstraints(2, indiceY++, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Cambiar password?"), GenericUtils.createGridBagConstraints(0, indiceY, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panDetalle.add(getChkCambiarPass(), GenericUtils.createGridBagConstraints(1, indiceY++, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1,1 , 1, 0));
			panDetalle.add(new JLabel("Codigo: "), GenericUtils.createGridBagConstraints(0, indiceY, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panDetalle.add(getTxtCodigoUsuario(), GenericUtils.createGridBagConstraints(1, indiceY++, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1,1 , 1, 0));
			panDetalle.add(new JLabel("Perfil: "), GenericUtils.createGridBagConstraints(0, indiceY, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panDetalle.add(getCmbPerfiles(), GenericUtils.createGridBagConstraints(1, indiceY++, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel(TIPS_PASSWORD), GenericUtils.createGridBagConstraints(0, indiceY, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 2, 1, 1, 0));
		}
		return panDetalle;
	}

	@Override
	public void cargarLista() {
		List<UsuarioSistema> UsuarioSistema = getUsuarioFacade().getAllOrderByName();
		lista.removeAll();
		for (UsuarioSistema u : UsuarioSistema) {
			lista.addItem(u);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		getTxtCodigoUsuario().setValue(Long.valueOf(getUsuarioFacade().getProximoCodigoUsuario()));
		setUsuarioActual(new UsuarioSistema());
		getTxtUserName().requestFocus();
		setEditando(false);
		getChkCambiarPass().setEnabled(false);
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		getLblPassStrength().setText("");
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this,"�Est� seguro que desea eliminar el usuario seleccionado?","Confirmaci�n") == FWJOptionPane.YES_OPTION){
				getUsuarioFacade().remove(getUsuarioActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			if(!isEditando() && existeUsuario(getTxtUserName().getText())){
				FWJOptionPane.showErrorMessage(this, "El nombre usuario elegido ya existe. Por favor, elija otro.", "Error");
				return false;
			}
			capturarSetearDatos();
			UsuarioSistema usuario;
			try {
				usuario = getUsuarioFacade().save(getUsuarioActual(), !isEditando() || getChkCambiarPass().isSelected());
				lista.setSelectedValue(usuario, true);
				cargarLista();
				return true;
			} catch (ValidacionException e) {
				FWJOptionPane.showErrorMessage(this, e.getMensajeError(), "Error");
				return false;
			}
		}
		return false;
	}

	private boolean existeUsuario(String usuario) {
		return GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).existeUsuario(usuario);
	}

	private void capturarSetearDatos() {
		getUsuarioActual().setUsrName(getTxtUserName().getText());
		if (!isEditando() || getChkCambiarPass().isSelected()) {
			getUsuarioActual().setPassword(new String(getTxtPassword().getPassword()));
		}
		getUsuarioActual().setCodigoUsuario(Integer.valueOf(getTxtCodigoUsuario().getValue()));
		getUsuarioActual().setPerfil((Perfil)getCmbPerfiles().getSelectedItem());
	}

	private boolean validar() {
		if(getTxtUserName().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre de usuario.", "Advertencia");
			getTxtUserName().requestFocus();
			return false;
		}
		
		if(getTxtPassword().getPassword().length==0){
			FWJOptionPane.showErrorMessage(this, "Debe completar la contrase�a.", "Advertencia");
			getTxtPassword().requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			getTxtUserName().requestFocus();
			setEditando(true);
			setModoEdicion(true);
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un perfil", "Error");
			setEditando(false);
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setUsuarioActual((UsuarioSistema)lista.getSelectedValue());
		limpiarDatos();
		if(getUsuarioActual() != null) {
			getTxtUserName().setText(getUsuarioActual().getUsrName());
			getTxtPassword().setText(getUsuarioActual().getPassword());
			getTxtCodigoUsuario().setValue(Long.valueOf(getUsuarioActual().getCodigoUsuario()));
			getCmbPerfiles().setSelectedItem(getUsuarioActual().getPerfil());
			getLblPassStrength().setText("");
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtPassword().setText("");
		getTxtUserName().setText("");
		getTxtCodigoUsuario().setValue(null);
		getChkCambiarPass().setSelected(false);
		if(getCmbPerfiles().getItemCount()>0){
			getCmbPerfiles().setSelectedIndex(0);
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		getChkCambiarPass().setEnabled(isEditando());
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	private FWJTextField getTxtUserName() {
		if(txtUserName == null){
			txtUserName = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtUserName;
	}

	private FWJNumericTextField getTxtCodigoUsuario() {
		if (txtCodigoUsuario == null) {
			txtCodigoUsuario = new FWJNumericTextField(100, Integer.MAX_VALUE);
			txtCodigoUsuario.setEditable(false);
		}
		return txtCodigoUsuario;
	}
	
	private JPasswordField getTxtPassword() {
		if(txtPassword == null){
			txtPassword = new JPasswordField();
			txtPassword.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					analizarPassword(new String(getTxtPassword().getPassword()));
				}
			});
		}
		return txtPassword;
	}

	private JComboBox getCmbPerfiles() {
		if(cmbPerfiles == null){
			cmbPerfiles = new JComboBox();
			GuiUtil.llenarCombo(cmbPerfiles, getPerfilFacade().getAllOrderByName(), true);
		}
		return cmbPerfiles;
	}

	public UsuarioSistema getUsuarioActual() {
		return usuarioActual;
	}

	private UsuarioSistemaFacadeRemote getUsuarioFacade() {
		if(usuarioFacade == null){
			usuarioFacade = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class);
		}
		return usuarioFacade;
	}

	private PerfilFacadeRemote getPerfilFacade() {
		if(perfilFacade == null){
			perfilFacade = GTLBeanFactory.getInstance().getBean2(PerfilFacadeRemote.class);
		}
		return perfilFacade;
	}

	private JLabel getLblPassStrength() {
		if(lblPassStrength == null){
			lblPassStrength = new JLabel();
		}
		return lblPassStrength;
	}

	public void setUsuarioActual(UsuarioSistema usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	private void analizarPassword(String password) {
		EPasswordStrength strength = PasswordAnalyzer.getInstance().analyze(password);
		getLblPassStrength().setText(strength.getDescripcion());
		getLblPassStrength().setForeground(strength.getColor());
	}

	
	public boolean isEditando() {
		return editando;
	}

	
	public void setEditando(boolean editando) {
		this.editando = editando;
	}

	public JCheckBox getChkCambiarPass() {
		if (chkCambiarPass == null) {
			chkCambiarPass = new JCheckBox();
			chkCambiarPass.setEnabled(false);
		}
		return chkCambiarPass;
	}
}
