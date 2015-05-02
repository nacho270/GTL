package ar.com.textillevel.gui.modulos.abm.gente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.validator.EmailValidator;
import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextArea;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.error.CLRuntimeException;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.facade.api.remote.PersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogCargarRubro;
import ar.com.textillevel.gui.util.panels.PanDatosDireccion;
import ar.com.textillevel.gui.util.panels.PanDatosTelefono;
import ar.com.textillevel.gui.util.panels.PanDireccionEvent;
import ar.com.textillevel.gui.util.panels.PanDireccionEventListener;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMPersona extends GuiABMListaTemplate {

	private static final long serialVersionUID = -7808476440361699270L;
	
	private static final int MAX_LONGITUD_NOMBRE_Y_APELLIDO = 100;
	private static final int MAX_LONGITUD_OBSERVACIONES = 1000;
	private static final int MAX_LONGITUD_EMAIL = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private PanDatosTelefono panTelefonoFijo;
	private PanDatosTelefono panCelular;
	private CLJTextField txtApellido;
	private CLJTextField txtNombres;
	
	private JComboBox cmbRubro;
	private CLJTextField txtEmail;
	private PanDatosDireccion panDatosDireccion;
	private CLJTextArea txtObservaciones;

	private Persona personaActual;
	private List<InfoLocalidad> infoLocalidadList;
	private PersonaFacadeRemote personaFacadeRemote;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;
	private RubroPersonaFacadeRemote rubroFacadeRemote;

	public GuiABMPersona(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Personas");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos de la persona", getTabDetalle());		
	}

	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.CENTER);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("NOMBRES:"), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 45, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombres(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("APELLIDO:"), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtApellido(), createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanDatosDireccion(), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
			panDetalle.add(getPanTelefonos(), createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(new JLabel("RUBRO:"), createGridBagConstraints(0, 4,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbRubro(), createGridBagConstraints(1, 4,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("EMAIL:"), createGridBagConstraints(0, 5,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtEmail(), createGridBagConstraints(1, 5,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("OBSERVACIONES:"), createGridBagConstraints(0, 6,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtObservaciones(), createGridBagConstraints(1, 6,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 4, 1, 1, 0.8));
		}
		return panDetalle;
	}

	private CLJTextField getTxtNombres() {
		if(txtNombres == null) {
			txtNombres = new CLJTextField(MAX_LONGITUD_NOMBRE_Y_APELLIDO);
		}
		return txtNombres;
	}

	@Override
	public void cargarLista() {
		List<Persona> clienteList = getPersonaFacade().getAllOrderByName();
		lista.removeAll();
		for(Persona c : clienteList) {
			lista.addItem(c);
		}
	}

	private PersonaFacadeRemote getPersonaFacade() {
		if(personaFacadeRemote == null) {
			personaFacadeRemote = GTLBeanFactory.getInstance().getBean2(PersonaFacadeRemote.class);
		}
		return personaFacadeRemote;
	}

	@Override
	public void botonAgregarPresionado(int arg0) {
		limpiarDatos();
		setPersonaActual(new Persona());
		getTxtNombres().requestFocus();
		getPanDatosDireccion().setDireccion(getPersonaActual().getInfoDireccion(), getInfoLocalidadList());
	}

	@Override
	public void botonCancelarPresionado(int arg0) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int arg0) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMPersona.this, "¿Está seguro que desea eliminar a la persona seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				try{
					getPersonaFacade().remove(getPersonaActual());
					lista.setSelectedIndex(-1);
				}catch(CLRuntimeException ex){
					BossError.gestionarError(ex);
				}
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int arg0) {
		if(validar()) {
			capturarSetearDatos();
			Persona personaRefresh = getPersonaFacade().save(getPersonaActual());
			lista.setSelectedValue(personaRefresh, true);
			CLJOptionPane.showInformationMessage(this, "Los datos de la persona se han guardado con éxito", "Administrar Clientes");
			return true;
		}
		return false;
	}

	private void capturarSetearDatos() {
		getPersonaActual().setEmail(getTxtEmail().getText().trim());
		getPersonaActual().setObservaciones(getTxtObservaciones().getText().trim().toUpperCase());
		getPersonaActual().setTelefono(getPanTelefonoFijo().getDatos());
		getPersonaActual().setCelular(getPanCelular().getDatos());
		getPersonaActual().setInfoDireccion(getPanDatosDireccion().getInfoDireccion());
		getPersonaActual().setApellido(getTxtApellido().getText().trim().toUpperCase());
		getPersonaActual().setNombres(getTxtNombres().getText().trim().toUpperCase());
		getPersonaActual().setRubroPersona((Rubro)getCmbRubro().getSelectedItem());
	}

	private boolean validar() {
		String textoNombres = getTxtNombres().getText().trim();
		if(StringUtil.isNullOrEmpty(textoNombres)) {
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, "Falta completar el campo 'NOMBRES'", "Advertencia");
			getTxtNombres().requestFocus();
			return false;
		}
		String textoRazonSocial = getTxtApellido().getText().trim();
		if(StringUtil.isNullOrEmpty(textoRazonSocial)) {
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, "Falta completar el campo 'APELLIDO'", "Advertencia");
			getTxtApellido().requestFocus();
			return false;
		}
		String textoErrorTelefono = getPanTelefonoFijo().validar();
		if(textoErrorTelefono != null) {
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, StringW.wordWrap(textoErrorTelefono), "Advertencia");
			return false;
		}
		String textoErrorCelular = getPanCelular().validar();
		if(textoErrorCelular != null) {
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, StringW.wordWrap(textoErrorCelular), "Advertencia");
			return false;
		}
		String strMail = getTxtEmail().getText();
		if(!StringUtil.isNullOrEmpty(strMail) && !EmailValidator.getInstance().isValid(strMail.trim())) {
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, "El 'EMAIL' ingresado no es válido", "Advertencia");
			getTxtEmail().requestFocus();
			return false;
		}
		Rubro rubro = (Rubro)getCmbRubro().getSelectedItem();
		if(rubro == null) {
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, "Falta seleccionar el 'RUBRO'", "Advertencia");
			return false;
		}
		if(StringUtil.isNullOrEmpty(getPanCelular().getDatos()) &&  StringUtil.isNullOrEmpty(getPanTelefonoFijo().getDatos())){
			CLJOptionPane.showErrorMessage(GuiABMPersona.this, "Debe ingresar al menos un teléfono", "Advertencia");
			return false;
		}
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombres().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un cliente", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int arg0) {
		setPersonaActual((Persona)lista.getSelectedValue());
		limpiarDatos();
		if(getPersonaActual() != null) {
			getTxtApellido().setText(getPersonaActual().getApellido());
			getTxtNombres().setText(getPersonaActual().getNombres());
			getPanDatosDireccion().setDireccion(getPersonaActual().getInfoDireccion(), getInfoLocalidadList());
			getTxtObservaciones().setText(getPersonaActual().getObservaciones());
			getTxtEmail().setText(getPersonaActual().getEmail());
			getPanCelular().setFullDatos(getPersonaActual().getCelular());
			getPanTelefonoFijo().setFullDatos(getPersonaActual().getTelefono());
			getCmbRubro().setSelectedItem(getPersonaActual().getRubroPersona());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombres().setText("");
		getTxtApellido().setText("");
		getPanCelular().limpiarDatos();
		getPanTelefonoFijo().limpiarDatos();
		getTxtObservaciones().setText("");
		getTxtEmail().setText("");
		getPanDatosDireccion().limpiarDatos();
		getCmbRubro().setSelectedIndex(-1);
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		llenarComboRubro();
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	private void llenarComboRubro() {
		List<Rubro> allRubroPersona = getRubroFacadeRemote().getAllRubroByTipo(ETipoRubro.PERSONA);
		Rubro rubroPersona = new Rubro();
		rubroPersona.setId(-1);
		rubroPersona.setNombre("OTRO");
		allRubroPersona.add(rubroPersona);
		GuiUtil.llenarCombo(getCmbRubro(), allRubroPersona, true);
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	private GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}

	private JComboBox getCmbRubro() {
		if(cmbRubro == null) {
			cmbRubro = new JComboBox();
			cmbRubro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getCmbRubro().isEnabled()) {
						Rubro rubroSelected = ((Rubro)getCmbRubro().getSelectedItem());
						if(getCmbRubro().getSelectedItem() != null && rubroSelected.getId() == -1) {
							JDialogCargarRubro dialogCargaRubro = new JDialogCargarRubro(GuiABMPersona.this.getFrame(), ETipoRubro.PERSONA);
							GuiUtil.centrarEnFramePadre(dialogCargaRubro);
							dialogCargaRubro.setVisible(true);
							Rubro rubroNuevo = dialogCargaRubro.getRubro();
							if(rubroNuevo != null) {
								llenarComboRubro();
								getCmbRubro().setSelectedItem(rubroNuevo);
							} else {
								getCmbRubro().setSelectedItem(getPersonaActual().getRubroPersona());
							}
						}
					}
				}
			});

		}
		return cmbRubro;
	}

	private CLJTextField getTxtApellido() {
		if(txtApellido == null) {
			txtApellido = new CLJTextField(MAX_LONGITUD_NOMBRE_Y_APELLIDO);
		}
		return txtApellido;
	}

	private JPanel getPanTelefonos() {
		JPanel panTelefonos = new JPanel();
		panTelefonos.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panTelefonos.add(getPanTelefonoFijo(), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panTelefonos.add(getPanCelular(), gbc);
		return panTelefonos;
	}

	private PanDatosTelefono getPanTelefonoFijo() {
		if(panTelefonoFijo == null) {
			panTelefonoFijo = new PanDatosTelefono("TELEFONO", "-");
		}
		return panTelefonoFijo;
	}

	private PanDatosTelefono getPanCelular() {
		if(panCelular == null) {
			panCelular = new PanDatosTelefono("CELULAR", "-");
		}
		return panCelular;
	}

	private CLJTextArea getTxtObservaciones() {
		if(txtObservaciones == null) {
			txtObservaciones = new CLJTextArea(MAX_LONGITUD_OBSERVACIONES);
			txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		}
		return txtObservaciones;
	}

	
	private Persona getPersonaActual() {
		return personaActual;
	}

	private void setPersonaActual(Persona personaActual) {
		this.personaActual = personaActual;
	}

	public void setModoEdicionTemplate(boolean estado) {
		super.setModoEdicionTemplate(estado);
		if(!estado && lista.getSelectedIndex() < 0) {
			getBtnEliminar().setEnabled(false);
			getBtnModificar().setEnabled(false);
		}
	}

	private PanDatosDireccion getPanDatosDireccion() {
		if(panDatosDireccion == null) {
			panDatosDireccion = new PanDatosDireccion(GuiABMPersona.this.getFrame(), "DIRECCION");
			panDatosDireccion.addPanDireccionListener(new PanDireccionEventListener() {

				public void newInfoLocalidadAdded(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					infoLocalidadList.clear();
					infoLocalidadList.addAll(getInfoLocalidadRemote().getAllInfoLocalidad());
					InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
					infoLocalidadOTRO.setId(PanDatosDireccion.OTRO);
					infoLocalidadOTRO.setNombreLocalidad("OTRO");
					infoLocalidadList.add(infoLocalidadOTRO);
					getPanDatosDireccion().seleccionarLocalidad(infoLocalidadSelected);
				}

				public void newInfoLocalidadSelected(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					getPanTelefonoFijo().setCodArea(infoLocalidadSelected.getCodigoArea());
				}

			});

		}
		return panDatosDireccion;
	}

	private InfoLocalidadFacadeRemote getInfoLocalidadRemote() {
		if(infoLocalidadRemote == null) {
			infoLocalidadRemote = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		}
		return infoLocalidadRemote;
	}

	private List<InfoLocalidad> getInfoLocalidadList() {
		if(infoLocalidadList == null) {
			infoLocalidadList = getInfoLocalidadRemote().getAllInfoLocalidad();
			InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
			infoLocalidadOTRO.setId(-1);
			infoLocalidadOTRO.setNombreLocalidad("OTRO");
			infoLocalidadList.add(infoLocalidadOTRO);
		}
		return infoLocalidadList;
	}

	private CLJTextField getTxtEmail() {
		if(txtEmail == null) {
			txtEmail = new CLJTextField(MAX_LONGITUD_EMAIL);
		}
		return txtEmail;
	}

	public RubroPersonaFacadeRemote getRubroFacadeRemote() {
		if(rubroFacadeRemote == null) {
			rubroFacadeRemote = GTLBeanFactory.getInstance().getBean2(RubroPersonaFacadeRemote.class);
		}
		return rubroFacadeRemote;
	}

}