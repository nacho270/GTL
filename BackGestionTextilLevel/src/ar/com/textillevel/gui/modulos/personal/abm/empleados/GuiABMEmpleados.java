package ar.com.textillevel.gui.modulos.personal.abm.empleados;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.MaskFormatter;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJLetterTextField;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.ImageUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.UppercaseDocumentFilterSoloLetras;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JFileChooserImage;
import ar.com.textillevel.modulos.personal.entidades.commons.AFJP;
import ar.com.textillevel.modulos.personal.entidades.legajos.DatosEmpleoAnterior;
import ar.com.textillevel.modulos.personal.entidades.legajos.EEstudiosCursados;
import ar.com.textillevel.modulos.personal.entidades.legajos.ESexo;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.Nacionalidad;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.InfoDomicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.estadocivil.EEstadoCivil;
import ar.com.textillevel.modulos.personal.entidades.legajos.estadocivil.InfoEstadoCivil;
import ar.com.textillevel.modulos.personal.entidades.legajos.familia.EParentesco;
import ar.com.textillevel.modulos.personal.entidades.legajos.familia.Familiar;
import ar.com.textillevel.modulos.personal.facade.api.remote.AFJPFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.NacionalidadFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

import com.sun.media.imageioimpl.plugins.bmp.BMPImageReader;

public class GuiABMEmpleados extends GuiABMListaTemplate {

	private static final long serialVersionUID = -7365772210973323819L;

	private static final int EDAD_MINIMA = 18;
	private static final int EDAD_MAXIMA = 65;
	
	private JPanel panelTabResumen;
	private JPanel panelTabDatosBasicos;
	private JPanel panelTabDatosFamiliares;
	private JPanel panelTabDatosEmpleosAnteriores;

	// CONTROLES PANEL DATOS BASICOS

	private FWJTextField txtNombre;
	private FWJTextField txtApellido;
	private JLabel lblFotoEmpleado;
	private LinkableLabel linkLabelBuscarFoto;
	private PanelDatePicker panelFechaNacimiento;
	private PanelTablaInfoEstadoCivil panelTablaEstadoCivil;
	private JComboBox cmbNacionalidad;
	private JComboBox cmbSexo;
	private PanelTablaInfoDomicilio panelTablaDomicilio;
	private JFormattedTextField txtCuil;
	private FWJNumericTextField txtDni;
	private FWJNumericTextField txtCedula;
	private FWJNumericTextField txtNroImpuestoGanancias;
	private JComboBox cmbAFJP;
	private FWJNumericTextField txtNroAFJP;
	private JComboBox cmbEstudiosCursados;
	private FWJLetterTextField txtProfesion;
	private JCheckBox chkExhibeTitulos;
	private FWJLetterTextField txtIdiomasQueHabla;
	private FWJLetterTextField txtIdiomasQueLee;
	private FWJLetterTextField txtIdiomasQueEscribe;
	private FWJLetterTextField txtOtrosConocimienos;
	private JCheckBox chkJubilado;
	private JCheckBox chkPrivado;
	
	private JPanel panelDatosPersonales;
	private JPanel panelDocumentacion;
	private JPanel panelEstudios;
	
	private JScrollPane jsp;
	
	private static final int ANCHO_IMAGEN = 120;
	private static final int ALTO_IMAGEN = 120;

	private String pathAnterior;

	// FIN CONTROLES PANEL DATOS BASICOS

	// PANEL CONTROLES DATOS EMPLEOS ANTERIORES

	private PanelTablaTrabajos panelTablaEmpleosAnteriores;

	// FIN PANEL CONTROLES DATOS EMPLEOS ANTERIORES

	// PANEL CONTROLES DATOS FAMILIARES

	private PanelTablaDatosFamiliares panelTablaDatosFamiliares;

	// FIN PANEL CONTROLES DATOS

	private Empleado empleadoActual;
	private EmpleadoFacadeRemote empleadoFacade;

	public GuiABMEmpleados(Integer idModulo) throws FWException {
		super();
		setHijoCreado(true);
		setTitle("Administrar empleados");
		constructGui();
		setEstadoInicial();
	}

	public JScrollPane getJsp() {
		if(jsp == null){
			jsp = new JScrollPane(getPanelTabDatosBasicos(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jsp;
	}
	
	private void constructGui() {
		// panTabs.addTab("Resumen", getPanelTabResumen());
		panTabs.addTab("Datos básicos", getJsp());
		panTabs.addTab("Datos familiares", getPanelTabDatosFamiliares());
		panTabs.addTab("Historial de empleos", getPanelTabDatosEmpleosAnteriores());
		// setSize(GenericUtils.getDimensionPantalla());
		// panTabs.setSize((int)GenericUtils.getDimensionPantalla().getWidth()-lista.getWidth()-400,(int)GenericUtils.getDimensionPantalla().getHeight()-200);
		// getPanModificarGrabar().setBounds((int)panTabs.getBounds().getX(),
		// (int)panTabs.getBounds().getY() + panTabs.getHeight() + 20,
		// getPanModificarGrabar().getWidth(),
		// getPanModificarGrabar().getHeight());
		// setMaximizable(false);
	}

	@Override
	public void cargarLista() {
		List<Empleado> empleados = getEmpleadoFacade().getAllOrderByName(GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin());
		lista.removeAll();
		for (Empleado e : empleados) {
			lista.addItem(e);
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicionTemplate(false);
		habilitarTabSeleccionado(true);
		setModoEdicion(false);
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setEmpleadoActual(new Empleado());
		getTxtApellido().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar al empleado seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				if(getEmpleadoActual().getLegajo()==null){
					getEmpleadoFacade().remove(getEmpleadoActual());
					itemSelectorSeleccionado(-1);
				}else{
					FWJOptionPane.showErrorMessage(this,"No se puede eliminar al empleado debido a que ya tiene cargado un legajo", "Error");
				}
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtApellido().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un empleado", "Error");
			return false;
		}
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		limpiarDatos();
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarDatos();
			Empleado emp = getEmpleadoFacade().save(getEmpleadoActual());
			FWJOptionPane.showInformationMessage(this, "Los datos del empleado se han guardado con éxito", "Administrar empleados");
			lista.setSelectedValue(emp, true);
			return true;
		}
		return false;
	}

	private void capturarDatos() {
		capturarDatosBasicos();
		capturarDatosDocumentacion();
		capurarDatosEstudios();
	}

	private void capurarDatosEstudios() {
		getEmpleadoActual().getDatosEducacion().setEstudiosCursados((EEstudiosCursados)getCmbEstudiosCursados().getSelectedItem());
		getEmpleadoActual().getDatosEducacion().setExhibeTitulos(getChkExhibeTitulos().isSelected());
		getEmpleadoActual().getDatosEducacion().setIdiomasHablados(getTxtIdiomasQueHabla().getText().trim().toUpperCase());
		getEmpleadoActual().getDatosEducacion().setIdiomasQueEscribe(getTxtIdiomasQueEscribe().getText().trim().toUpperCase());
		getEmpleadoActual().getDatosEducacion().setIdiomasQueLee(getTxtIdiomasQueLee().getText().trim().toUpperCase());
		getEmpleadoActual().getDatosEducacion().setOtrosConocimientos(getTxtOtrosConocimienos().getText().trim().toUpperCase());
		getEmpleadoActual().getDatosEducacion().setProfesionOficio(getTxtProfesion().getText().trim().toUpperCase());
	}

	private void capturarDatosBasicos() {
		getEmpleadoActual().setApellido(getTxtApellido().getText().trim().toUpperCase());
		getEmpleadoActual().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getEmpleadoActual().setFechaNacimiento(new java.sql.Date(getPanelFechaNacimiento().getDate().getTime()));
		getEmpleadoActual().setNacionalidad((Nacionalidad)getCmbNacionalidad().getSelectedItem());
		getEmpleadoActual().setFoto((ImageIcon) getLblFotoEmpleado().getIcon());
		getEmpleadoActual().setSexo((ESexo)getCmbSexo().getSelectedItem());
		getEmpleadoActual().setJubilado(getChkJubilado().isSelected());
		getEmpleadoActual().setPrivado(getChkPrivado().isSelected());
	}

	private void capturarDatosDocumentacion() {
		getEmpleadoActual().getDocumentacion().setCuit(getTxtCuil().getText());
		getEmpleadoActual().getDocumentacion().setAfjp((AFJP)getCmbAFJP().getSelectedItem());
		getEmpleadoActual().getDocumentacion().setNroAfiliacionAFJP(getTxtNroAFJP().getValueWithNull());
		getEmpleadoActual().getDocumentacion().setNroCedula(getTxtCedula().getValueWithNull());
		getEmpleadoActual().getDocumentacion().setNroDocumento(getTxtDni().getValueWithNull());
		getEmpleadoActual().getDocumentacion().setNroImpuestoALasGanancias(getTxtNroImpuestoGanancias().getValueWithNull());
	}

	private boolean validar() {
		if(getTxtApellido().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el apellido del empleado", "Error");
			getTxtApellido().requestFocus();
			return false;
		}
		if(getTxtNombre().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del empleado", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getPanelFechaNacimiento().getDate()==null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar una fecha de nacimiento válida", "Error");
			getPanelFechaNacimiento().requestFocus();
			return false;
		}
 		if(getCmbNacionalidad().getSelectedIndex()<0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar una nacionalidad válida", "Error");
			getCmbNacionalidad().requestFocus();
			return false;
		}
		if(getEmpleadoActual().getInformacionEstadoCivil().size()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un estado civil", "Error");
			return false;
		}
		if(getEmpleadoActual().getInformacionEstadoCivil().size()>0){
			boolean ok = true;
			for(InfoEstadoCivil e: getEmpleadoActual().getInformacionEstadoCivil()){
				if(e.getEstadoCivil()==null){
					ok = false;
				}
			}
			if(!ok){
				FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un estado civil válido", "Error");
				return false;
			}
		}
		if(getEmpleadoActual().getDomicilios().size()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un domicilio", "Error");
			return false;
		}
		if(getTxtCuil().getText().trim().length() < 13) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el C.U.I.L de empleado.","Advertencia");
			getTxtCuil().requestFocus();
			return false;
		}
		if(getTxtDni().getValueWithNull() == null && getTxtCedula().getValueWithNull()==null){
			FWJOptionPane.showErrorMessage(this, "Debe ingesar al menos un número de documento (DNI Y/O Cédula)", "Error");
			getTxtDni().requestFocus();
			return false;
		}
		if(getCmbAFJP().getSelectedIndex()<0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la AFJP del empleado", "Error");
			getCmbAFJP().requestFocus();
			return false;
		}
//		if(getTxtNroAFJP().getValueWithNull()==null){
//			CLJOptionPane.showErrorMessage(this, "Debe ingresar el número de afilizado a la AFJP del empleado", "Error");
//			getTxtNroAFJP().requestFocus();
//			return false;
//		}
		int edad = DateUtil.calcularEdad(getPanelFechaNacimiento().getDate());
		if(edad < EDAD_MINIMA || edad > EDAD_MAXIMA){
			FWJOptionPane.showErrorMessage(this, "El empleado tiene " + edad + " años. La edad estar entre " + EDAD_MINIMA + " y " + EDAD_MAXIMA + ".", "Error");
			getPanelFechaNacimiento().requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void setModoEdicion(boolean estado) {
		setEstadoPanelDatosBasicos(estado);
		GuiUtil.setEstadoPanel(getPanelTabResumen(), estado);
		GuiUtil.setEstadoPanel(getPanelTabDatosFamiliares(), estado);
		getPanelTablaEmpleosAnteriores().setModoConsulta(!estado);
	}

	private void setEstadoPanelDatosBasicos(boolean estado) {
		getTxtApellido().setEnabled(estado);
		getTxtNombre().setEnabled(estado);
		getCmbNacionalidad().setEnabled(estado);
		getPanelFechaNacimiento().setEnabled(estado);
		getCmbSexo().setEnabled(estado);
		getLinkLabelBuscarFoto().setEnabled(estado);
		getPanelTablaDomicilio().setModoConsulta(!estado);
		getPanelTablaEstadoCivil().setModoConsulta(!estado);
		getTxtCuil().setEnabled(estado);
		getTxtDni().setEnabled(estado);
		getTxtCedula().setEnabled(estado);
		getTxtNroImpuestoGanancias().setEnabled(estado);
		getTxtNroAFJP().setEnabled(estado);
		getCmbAFJP().setEnabled(estado);
		getTxtProfesion().setEnabled(estado);
		getCmbEstudiosCursados().setEnabled(estado);
		getChkExhibeTitulos().setEnabled(estado);
		getTxtIdiomasQueEscribe().setEnabled(estado);
		getTxtIdiomasQueHabla().setEnabled(estado);
		getTxtIdiomasQueLee().setEnabled(estado);
		getTxtOtrosConocimienos().setEnabled(estado);
		getChkJubilado().setEnabled(estado);
		getChkPrivado().setEnabled(estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtApellido().setText("");
		getTxtNombre().setText("");
		getLblFotoEmpleado().setIcon(null);
		getCmbNacionalidad().setSelectedIndex(-1);
		getCmbAFJP().setSelectedIndex(-1);
		getTxtNroAFJP().setText("");
		getTxtCedula().setText("");
		getTxtDni().setText("");
		getTxtProfesion().setText("");
		getTxtIdiomasQueEscribe().setText("");
		getTxtIdiomasQueHabla().setText("");
		getTxtIdiomasQueLee().setText("");
		getChkExhibeTitulos().setSelected(false);
		getTxtOtrosConocimienos().setText("");
		getCmbEstudiosCursados().setSelectedIndex(0);
		getTxtNroImpuestoGanancias().setText("");
		getTxtCuil().setText("");
		getPanelTablaDomicilio().getTabla().removeAllRows();
		getPanelTablaEstadoCivil().getTabla().removeAllRows();
		getPanelTablaEstadoCivil().getTabla().addRow(new Object[PanelTablaInfoDomicilio.CANT_COLS]);
		getPanelTablaDatosFamiliares().getTabla().removeAllRows();
		getPanelTablaEmpleosAnteriores().getTabla().removeAllRows();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		Empleado selectedValue = (Empleado) lista.getSelectedValue();
		if (selectedValue != null) {
			setEmpleadoActual(getEmpleadoFacade().getByIdTotalmenteEager(selectedValue.getId()));
			limpiarDatos();
			mostrarDatos();
		}
	}

	private void mostrarDatos() {
		getTxtApellido().setText(getEmpleadoActual().getApellido());
		getTxtNombre().setText(getEmpleadoActual().getNombre());
		getCmbNacionalidad().setSelectedItem(getEmpleadoActual().getNacionalidad());
		getLblFotoEmpleado().setIcon(getEmpleadoActual().getFoto());
		getPanelFechaNacimiento().setSelectedDate(getEmpleadoActual().getFechaNacimiento());
		getCmbSexo().setSelectedItem(getEmpleadoActual().getSexo());
		getChkJubilado().setSelected(getEmpleadoActual().getJubilado());
		getChkPrivado().setSelected(getEmpleadoActual().getPrivado());
		
		getTxtCuil().setText(getEmpleadoActual().getDocumentacion().getCuit());
		getCmbAFJP().setSelectedItem(getEmpleadoActual().getDocumentacion().getAfjp());
		getTxtCedula().setValue(getEmpleadoActual().getDocumentacion().getNroCedula()!=null?getEmpleadoActual().getDocumentacion().getNroCedula().longValue():null);
		getTxtNroAFJP().setValue(getEmpleadoActual().getDocumentacion().getNroAfiliacionAFJP()!=null?getEmpleadoActual().getDocumentacion().getNroAfiliacionAFJP().longValue():null);
		getTxtNroImpuestoGanancias().setValue(getEmpleadoActual().getDocumentacion().getNroImpuestoALasGanancias()!=null?getEmpleadoActual().getDocumentacion().getNroImpuestoALasGanancias().longValue():null);
		getTxtDni().setValue(getEmpleadoActual().getDocumentacion().getNroDocumento()!=null?getEmpleadoActual().getDocumentacion().getNroDocumento().longValue():null);
		
		getTxtIdiomasQueEscribe().setText(getEmpleadoActual().getDatosEducacion().getIdiomasQueEscribe());
		getTxtIdiomasQueHabla().setText(getEmpleadoActual().getDatosEducacion().getIdiomasHablados());
		getTxtIdiomasQueLee().setText(getEmpleadoActual().getDatosEducacion().getIdiomasQueLee());
		getTxtOtrosConocimienos().setText(getEmpleadoActual().getDatosEducacion().getOtrosConocimientos());
		getTxtProfesion().setText(getEmpleadoActual().getDatosEducacion().getProfesionOficio());
		getCmbEstudiosCursados().setSelectedItem(getEmpleadoActual().getDatosEducacion().getEstudiosCursados());
		getChkExhibeTitulos().setSelected(getEmpleadoActual().getDatosEducacion().getExhibeTitulos());
		
		getPanelTablaDatosFamiliares().refreshTable();
		getPanelTablaDomicilio().refreshTabla();
		getPanelTablaEmpleosAnteriores().refreshTable();
		getPanelTablaEstadoCivil().getTabla().removeAllRows();
		for(InfoEstadoCivil info : getEmpleadoActual().getInformacionEstadoCivil()){
			getPanelTablaEstadoCivil().agregarElemento(info);
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {

	}

	public JPanel getPanelTabResumen() {
		if (panelTabResumen == null) {
			panelTabResumen = new JPanel();
		}
		return panelTabResumen;
	}

	public JPanel getPanelTabDatosBasicos() {
		if (panelTabDatosBasicos == null) {
			panelTabDatosBasicos = new JPanel(new GridBagLayout());
			panelTabDatosBasicos.setPreferredSize(new Dimension(500, 760));
			panelTabDatosBasicos.add(getPanelDatosPersonales(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 6, 1, 1, 1));
			panelTabDatosBasicos.add(getPanelDocumentacion(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 6, 1, 1, 1));
			panelTabDatosBasicos.add(getPanelEstudios(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 6, 1, 1, 1));
		}
		return panelTabDatosBasicos;
	}

	private JPanel getPanelEstudios() {
		if(panelEstudios == null){
			panelEstudios = new JPanel(new GridBagLayout());
			panelEstudios.setBorder(BorderFactory.createTitledBorder("Conocimientos y Estudios"));

			panelEstudios.add(new JLabel("Profesión u oficio: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(15, 5, 5, 5), 1, 1, 0, 0));
			panelEstudios.add(getTxtProfesion(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(15, 5, 5, 5), 5, 1, 1, 0));
			panelEstudios.add(new JLabel("Estudios cursados: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelEstudios.add(getCmbEstudiosCursados(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelEstudios.add(getChkExhibeTitulos(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));

			panelEstudios.add(new JLabel("Idiomas que habla: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelEstudios.add(getTxtIdiomasQueHabla(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelEstudios.add(new JLabel("Lee: "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelEstudios.add(getTxtIdiomasQueLee(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelEstudios.add(new JLabel("Escribe: "), GenericUtils.createGridBagConstraints(4, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelEstudios.add(getTxtIdiomasQueEscribe(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelEstudios.add(new JLabel("Otros conocimientos: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 20, 5), 1, 1, 0, 0));
			panelEstudios.add(getTxtOtrosConocimienos(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 20, 5), 5, 1, 1, 0));
		}
		return panelEstudios;
	}

	private JPanel getPanelDatosPersonales() {
		if(panelDatosPersonales == null){
			panelDatosPersonales = new JPanel(new GridBagLayout());
			panelDatosPersonales.setBorder(BorderFactory.createTitledBorder("Datos personales"));
			panelDatosPersonales.add(new JLabel("Apellido: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getTxtApellido(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelDatosPersonales.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getTxtNombre(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelDatosPersonales.add(new JLabel("Fecha de nacimiento: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getPanelFechaNacimiento(), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			panelDatosPersonales.add(new JLabel("Nacionalidad: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getCmbNacionalidad(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getChkJubilado(), GenericUtils.createGridBagConstraints(4, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getChkPrivado(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			
			panelDatosPersonales.add(new JLabel("Sexo: "), GenericUtils.createGridBagConstraints(4, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 10, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getCmbSexo(), GenericUtils.createGridBagConstraints(5, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 10, 5), 1, 1, 0.5, 0));
			
			panelDatosPersonales.add(getLinkLabelBuscarFoto(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
	
			panelDatosPersonales.add(new JLabel("Foto: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDatosPersonales.add(getLblFotoEmpleado(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 2, 2, 0, 0));
	
			panelDatosPersonales.add(getPanelTablaEstadoCivil(), GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 6, 2, 1, 0));
	
			panelDatosPersonales.add(getPanelTablaDomicilio(), GenericUtils.createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 15, 5), 8, 1, 1, 0));
		}
		return panelDatosPersonales;
	}

	private JPanel getPanelDocumentacion() {
		if(panelDocumentacion == null){
			panelDocumentacion = new JPanel(new GridBagLayout());
			panelDocumentacion.setBorder(BorderFactory.createTitledBorder("Documentación"));

			panelDocumentacion.add(new JLabel("C.U.I.L"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDocumentacion.add(new JLabel("D.N.I/L.E/L.C"), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDocumentacion.add(new JLabel("Cédula de identidad"), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

			panelDocumentacion.add(getTxtCuil(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelDocumentacion.add(getTxtDni(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelDocumentacion.add(getTxtCedula(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelDocumentacion.add(new JLabel("Nro. impuesto a las ganancias"), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDocumentacion.add(new JLabel("Afiliacion a AFJP"), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelDocumentacion.add(new JLabel("Nro. AFJP"), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

			panelDocumentacion.add(getTxtNroImpuestoGanancias(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 20, 5), 1, 1, 1, 0));
			panelDocumentacion.add(getCmbAFJP(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 20, 5), 1, 1, 1, 0));
			panelDocumentacion.add(getTxtNroAFJP(), GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 20, 5), 1, 1, 1, 0));
		}
		return panelDocumentacion;
	}

	public JPanel getPanelTabDatosFamiliares() {
		if (panelTabDatosFamiliares == null) {
			panelTabDatosFamiliares = new JPanel();
			panelTabDatosFamiliares.add(getPanelTablaDatosFamiliares());
		}
		return panelTabDatosFamiliares;
	}

	public JPanel getPanelTabDatosEmpleosAnteriores() {
		if (panelTabDatosEmpleosAnteriores == null) {
			panelTabDatosEmpleosAnteriores = new JPanel(new GridBagLayout());
			panelTabDatosEmpleosAnteriores.add(getPanelTablaEmpleosAnteriores(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		}
		return panelTabDatosEmpleosAnteriores;
	}

	private class PanelTablaInfoEstadoCivil extends PanelTabla<InfoEstadoCivil> {

		private static final long serialVersionUID = -234670984720945366L;

		private static final int CANT_COLS = 3;
		private static final int COL_ANIO = 0;
		private static final int COL_ESTADO = 1;
		private static final int COL_OBJ = 2;

		@Override
		protected FWJTable construirTabla() {
			JComboBox cmbEstadoCivil = new JComboBox();
			GuiUtil.llenarCombo(cmbEstadoCivil, Arrays.asList(EEstadoCivil.values()), false);
			FWJTable tabla = new FWJTable(0, CANT_COLS){

				private static final long serialVersionUID = 45605064330426374L;

				@Override
				public void cellEdited(int column, int row) {
					try{
						if (column > -1) {
							InfoEstadoCivil info = new InfoEstadoCivil();
							Object valueAt = getValueAt(row, COL_ANIO);
							if(valueAt != null){
								Integer anio = Integer.valueOf((String) valueAt);
								if(anio>1900){
									info.setAnio(anio);
									EEstadoCivil estado = (EEstadoCivil) getValueAt(row, COL_ESTADO);
									if(estado != null){
										info.setEstadoCivil(estado);
										if(getEmpleadoActual().getInformacionEstadoCivil().isEmpty() || getEmpleadoActual().getInformacionEstadoCivil().size() <= row){
											getEmpleadoActual().getInformacionEstadoCivil().add(info);
										} else {
											getEmpleadoActual().getInformacionEstadoCivil().set(row, info);
										}
									}
								}else{
									Object valueAt2 = getValueAt(row, COL_ESTADO);
									if(valueAt2 != null){
										EEstadoCivil estado = (EEstadoCivil) getValueAt(row, COL_ESTADO);
										info.setEstadoCivil(estado);
										if(getEmpleadoActual().getInformacionEstadoCivil().isEmpty() || getEmpleadoActual().getInformacionEstadoCivil().size() <= row){
											getEmpleadoActual().getInformacionEstadoCivil().add(info);
										} else {
											getEmpleadoActual().getInformacionEstadoCivil().set(row, info);
										}
									}
								}
							}else {
								Object valueAt2 = getValueAt(row, COL_ESTADO);
								if(valueAt2 != null){
									EEstadoCivil estado = (EEstadoCivil) getValueAt(row, COL_ESTADO);
									info.setEstadoCivil(estado);
									if(getEmpleadoActual().getInformacionEstadoCivil().isEmpty() || getEmpleadoActual().getInformacionEstadoCivil().size() <= row){
										getEmpleadoActual().getInformacionEstadoCivil().add(info);
									} else {
										getEmpleadoActual().getInformacionEstadoCivil().set(row, info);
									}
								}
							}
						}
					}catch(IndexOutOfBoundsException e){
						
					}
				}
			};
			tabla.addRow(new Object[CANT_COLS]);
			tabla.setIntColumn(COL_ANIO, "Año", 100, false);
			tabla.setComboColumn(COL_ESTADO, "Estado", cmbEstadoCivil, 100, false);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(InfoEstadoCivil elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ANIO] = elemento.getAnio();
			row[COL_ESTADO] = elemento.getEstadoCivil();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected InfoEstadoCivil getElemento(int fila) {
			return (InfoEstadoCivil)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
	}

	private class PanelTablaInfoDomicilio extends PanelTabla<InfoDomicilio> {

		private static final long serialVersionUID = 384133255131555728L;

		private static final int CANT_COLS = 9;
		private static final int COL_FECHA = 0;
		private static final int COL_CALLE = 1;
		private static final int COL_NRO = 2;
		private static final int COL_PISO = 3;
		private static final int COL_DTO = 4;
		private static final int COL_TELEFONO = 5;
		private static final int COL_LOCALIDAD = 6;
		private static final int COL_COD_POSTAL = 7;
		private static final int COL_OBJ = 8;

		public PanelTablaInfoDomicilio() {
			super();
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS) {

				private static final long serialVersionUID = 3029440665368037548L;

				@Override
				public void cellEdited(int column, int row) {
					try{
						InfoDomicilio info = (InfoDomicilio) getValueAt(row, COL_OBJ);
						if (info == null) {
							info = new InfoDomicilio();
						}
						switch (column) {
							case COL_FECHA: {
								Date fecha = (Date) getValueAt(row, column);
								info.setFecha(fecha);
								break;
							}
							case COL_CALLE: {
								String calle = ((String) getValueAt(row, column)).trim();
								info.getDomicilio().setCalle(calle.toUpperCase());
								break;
							}
							case COL_NRO: {
								Integer numero = Integer.valueOf(getValueAt(row, column).toString());
								info.getDomicilio().setNumero(numero);
								break;
							}
							case COL_PISO: {
								String piso = (String) getValueAt(row, column);
								info.getDomicilio().setPiso(piso.toUpperCase());
								break;
							}
							case COL_DTO: {
								String dto = ((String) getValueAt(row, column)).trim();
								info.getDomicilio().setDepartamento(dto.toUpperCase());
								break;
							}
							case COL_TELEFONO: {
								String tel = ((String) getValueAt(row, column)).trim();
								info.getDomicilio().setTelefono(tel);
								break;
							}
							default: {
								break;
							}
						}
						getEmpleadoActual().getDomicilios().set(row, info);
					}catch(IndexOutOfBoundsException iobe){
						
					}
				};
			};
			tabla.setDateColumn(COL_FECHA, "Fecha", 100, false);
			tabla.setStringColumn(COL_CALLE, "Calle", 150, 150, false);
			tabla.setIntColumn(COL_NRO, "Número", 80, false);
			tabla.setStringColumn(COL_PISO, "Piso", 40,40, false);
			tabla.setStringColumn(COL_DTO, "Dto", 50, 50, false);
			tabla.setStringColumn(COL_TELEFONO, "Teléfono", 100, 100, true);
			tabla.setStringColumn(COL_LOCALIDAD, "Localidad", 150, 150, true);
			tabla.setIntColumn(COL_COD_POSTAL, "Código postal", 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

		@Override
		protected void agregarElemento(InfoDomicilio elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] = elemento.getFecha();
			row[COL_CALLE] = elemento.getDomicilio().getCalle();
			row[COL_NRO] = elemento.getDomicilio().getNumero();
			row[COL_PISO] = elemento.getDomicilio().getPiso();
			row[COL_DTO] = elemento.getDomicilio().getDepartamento();
			if(elemento.getDomicilio().getTelefono()!=null){
				row[COL_TELEFONO] = "("+elemento.getDomicilio().getInfoLocalidad().getCodigoArea()+")"+elemento.getDomicilio().getTelefono();
			}else{
				row[COL_TELEFONO] = null;
			}
			row[COL_LOCALIDAD] = elemento.getDomicilio().getInfoLocalidad().getNombreLocalidad();
			row[COL_COD_POSTAL] = elemento.getDomicilio().getInfoLocalidad().getCodigoPostal();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected InfoDomicilio getElemento(int fila) {
			return (InfoDomicilio)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			getEmpleadoActual().getDomicilios().remove(filaSeleccionada);
			refreshTabla();
			return true;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarDomicilio dialog = new JDialogAgregarModificarDomicilio(getFrame());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getEmpleadoActual().getDomicilios().add(dialog.getInfoDomicilio());
				refreshTabla();
			}
			return false;
		}
		
		public void refreshTabla(){
			getTabla().removeAllRows();
			for(InfoDomicilio info : getEmpleadoActual().getDomicilios()){
				agregarElemento(info);
			}
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if(filaSeleccionada > -1){
				InfoDomicilio info = getElemento(filaSeleccionada);
				JDialogAgregarModificarDomicilio dialog = new JDialogAgregarModificarDomicilio(getFrame(),info);
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					info = dialog.getInfoDomicilio();
					getEmpleadoActual().getDomicilios().set(filaSeleccionada,info);
					refreshTabla();
				}
			}
		}
	}

	public FWJTextField getTxtNombre() {
		if (txtNombre == null) {
			txtNombre = new FWJTextField();
			txtNombre.setPreferredSize(new Dimension(120, 20));
			((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtNombre;
	}

	public FWJTextField getTxtApellido() {
		if (txtApellido == null) {
			txtApellido = new FWJTextField();
			txtApellido.setPreferredSize(new Dimension(120, 20));
			((AbstractDocument) txtApellido.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtApellido;
	}

	public JLabel getLblFotoEmpleado() {
		if (lblFotoEmpleado == null) {
			lblFotoEmpleado = new JLabel();
			lblFotoEmpleado.setBorder(BorderFactory.createLineBorder(Color.RED.darker().darker()));
		}
		return lblFotoEmpleado;
	}

	public LinkableLabel getLinkLabelBuscarFoto() {
		if (linkLabelBuscarFoto == null) {
			linkLabelBuscarFoto = new LinkableLabel("Examinar") {

				private static final long serialVersionUID = 5716838708809338327L;

				@Override
				public void labelClickeada(MouseEvent e) {
					JFileChooserImage jFC = new JFileChooserImage();
					jFC.addFilter("Solo imagenes (gif, jpg, png)", new String[] { "gif", "jpg", "png" });
					try {
						File file = new File(getPathAnterior());
						jFC.setCurrentDirectory(file.getParentFile());
						if (file.exists()) {
							jFC.setSelectedFile(file);
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					int returnVal = jFC.showOpenDialog(GuiABMEmpleados.this);
					if (returnVal == JFileChooserImage.APPROVE_OPTION) {
						File file = jFC.getSelectedFile();
						if (valida(file)) {
							setPathAnterior(file.getAbsolutePath());
							loadImageFile(file.getAbsolutePath());
						} else {
							FWJOptionPane.showErrorMessage(null, "La imagen no es de un formato válido", "Tama\u00F1o inválido");
						}
					}
				}

				private void loadImageFile(String filename) {
					getLblFotoEmpleado().setText("");
					ImageIcon icon = new ImageIcon(filename);
					Image scaleImage = ImageUtil.scale(icon.getImage(), ANCHO_IMAGEN, ALTO_IMAGEN, true);
					getLblFotoEmpleado().setIcon(new ImageIcon(scaleImage));
					getLblFotoEmpleado().setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
					getLblFotoEmpleado().setSize(getLblFotoEmpleado().getPreferredSize());
					getLblFotoEmpleado().setVerticalTextPosition(JLabel.CENTER);
					getLblFotoEmpleado().setHorizontalTextPosition(JLabel.CENTER);
					getLblFotoEmpleado().repaint();
				}

				private boolean valida(File file) {
					try {
						ImageInputStream iis = ImageIO.createImageInputStream(file);
						Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
						if (readers.hasNext()) {
							ImageReader reader = readers.next();
							reader.setInput(iis, true);
							// com.sun.media.imageioimpl.plugins.bmp.
							if (reader instanceof BMPImageReader) {
								return false;
							}
							return true;
						}
						return false;
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			};
		}
		return linkLabelBuscarFoto;
	}

	public PanelDatePicker getPanelFechaNacimiento() {
		if (panelFechaNacimiento == null) {
			panelFechaNacimiento = new PanelDatePicker();
		}
		return panelFechaNacimiento;
	}

	public PanelTablaInfoEstadoCivil getPanelTablaEstadoCivil() {
		if (panelTablaEstadoCivil == null) {
			panelTablaEstadoCivil = new PanelTablaInfoEstadoCivil();
			panelTablaEstadoCivil.setPreferredSize(new Dimension(300, 150));
			panelTablaEstadoCivil.setBorder(BorderFactory.createTitledBorder("Estado civil"));
		}
		return panelTablaEstadoCivil;
	}

	public PanelTablaInfoDomicilio getPanelTablaDomicilio() {
		if (panelTablaDomicilio == null) {
			panelTablaDomicilio = new PanelTablaInfoDomicilio();
			panelTablaDomicilio.setBorder(BorderFactory.createTitledBorder("Domicilio"));
		}
		return panelTablaDomicilio;
	}

	public JFormattedTextField getTxtCuil() {
		if (txtCuil == null) {
			try {
				txtCuil = new JFormattedTextField(new MaskFormatter("##-########-#"));
				txtCuil.addFocusListener(new FocusAdapter() {

					@Override
					public void focusLost(FocusEvent e) {
						try {
							txtCuil.commitEdit();
						} catch (ParseException e1) {
							// e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtCuil;
	}

	public FWJNumericTextField getTxtDni() {
		if (txtDni == null) {
			txtDni = new FWJNumericTextField();
		}
		return txtDni;
	}

	public FWJNumericTextField getTxtCedula() {
		if (txtCedula == null) {
			txtCedula = new FWJNumericTextField();
		}
		return txtCedula;
	}

	public FWJNumericTextField getTxtNroImpuestoGanancias() {
		if (txtNroImpuestoGanancias == null) {
			txtNroImpuestoGanancias = new FWJNumericTextField();
		}
		return txtNroImpuestoGanancias;
	}

	public JComboBox getCmbAFJP() {
		if(cmbAFJP == null){
			cmbAFJP = new JComboBox();
			GuiUtil.llenarCombo(cmbAFJP, GTLPersonalBeanFactory.getInstance().getBean2(AFJPFacadeRemote.class).getAllOrderByName(), true);
			cmbAFJP.setPreferredSize(new Dimension(80, 20));
			cmbAFJP.addItem("OTRA");
			cmbAFJP.setSelectedIndex(-1);
			cmbAFJP.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						if(getCmbAFJP().getSelectedItem().equals("OTRA")){
							String texto = JOptionPane.showInputDialog(GuiABMEmpleados.this, "Ingrese la AFJP", "Alta de AFJP",JOptionPane.PLAIN_MESSAGE);
							if(texto !=null){
								AFJP afjp = new AFJP();
								afjp.setNombre(texto.toUpperCase());
								afjp = GTLPersonalBeanFactory.getInstance().getBean2(AFJPFacadeRemote.class).save(afjp);
								getCmbAFJP().removeAllItems();
								GuiUtil.llenarCombo(getCmbAFJP(), GTLPersonalBeanFactory.getInstance().getBean2(AFJPFacadeRemote.class).getAllOrderByName(), true);
								getCmbAFJP().addItem("OTRA");
								getCmbAFJP().setSelectedItem(afjp);
							}else{
								getCmbAFJP().setSelectedIndex(-1);
							}
						}
					}
				}
			});
		}
		return cmbAFJP;
	}

	public FWJNumericTextField getTxtNroAFJP() {
		if (txtNroAFJP == null) {
			txtNroAFJP = new FWJNumericTextField();
		}
		return txtNroAFJP;
	}

	public JComboBox getCmbEstudiosCursados() {
		if (cmbEstudiosCursados == null) {
			cmbEstudiosCursados = new JComboBox();
			GuiUtil.llenarCombo(cmbEstudiosCursados, Arrays.asList(EEstudiosCursados.values()), true);
		}
		return cmbEstudiosCursados;
	}

	public FWJLetterTextField getTxtProfesion() {
		if (txtProfesion == null) {
			txtProfesion = new FWJLetterTextField();
			((AbstractDocument) txtProfesion.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtProfesion;
	}

	public JCheckBox getChkExhibeTitulos() {
		if (chkExhibeTitulos == null) {
			chkExhibeTitulos = new JCheckBox("Exhibe títulos");
		}
		return chkExhibeTitulos;
	}

	public FWJLetterTextField getTxtIdiomasQueHabla() {
		if (txtIdiomasQueHabla == null) {
			txtIdiomasQueHabla = new FWJLetterTextField();
			((AbstractDocument) txtIdiomasQueHabla.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtIdiomasQueHabla;
	}

	public FWJLetterTextField getTxtIdiomasQueLee() {
		if (txtIdiomasQueLee == null) {
			txtIdiomasQueLee = new FWJLetterTextField();
			((AbstractDocument) txtIdiomasQueLee.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtIdiomasQueLee;
	}

	public FWJLetterTextField getTxtIdiomasQueEscribe() {
		if (txtIdiomasQueEscribe == null) {
			txtIdiomasQueEscribe = new FWJLetterTextField();
			((AbstractDocument) txtIdiomasQueEscribe.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtIdiomasQueEscribe;
	}

	public FWJLetterTextField getTxtOtrosConocimienos() {
		if (txtOtrosConocimienos == null) {
			txtOtrosConocimienos = new FWJLetterTextField();
			((AbstractDocument) txtOtrosConocimienos.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtOtrosConocimienos;
	}

	private class PanelTablaTrabajos extends PanelTabla<DatosEmpleoAnterior> {

		private static final long serialVersionUID = -3419369882431083098L;

		private static final int CANT_COLS = 9;
		private static final int COL_EMPLEADOR = 0;
		private static final int COL_DOMICILIO = 1;
		private static final int COL_REFERENCIA = 2;
		private static final int COL_TELEFONO = 3;
		private static final int COL_DESDE = 4;
		private static final int COL_HASTA = 5;
		private static final int COL_TAREAS = 6;
		private static final int COL_CERTIFICADO = 7;
		private static final int COL_OBJ = 8;

		public PanelTablaTrabajos() {
			super();
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_EMPLEADOR, "Empleador", 150, 150, true);
			tabla.setStringColumn(COL_DOMICILIO, "Domicilio", 150, 150, true);
			tabla.setStringColumn(COL_REFERENCIA, "Referencia", 150, 150, true);
			tabla.setStringColumn(COL_TELEFONO, "Teléfono", 100, 100, true);
			tabla.setDateColumn(COL_DESDE, "Desde", 80, true);
			tabla.setDateColumn(COL_HASTA, "Hasta", 80, true);
			tabla.setStringColumn(COL_TAREAS, "Tareas", 90, 90, true);
			tabla.setCheckColumn(COL_CERTIFICADO, "Certificado", 90, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(DatosEmpleoAnterior elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_EMPLEADOR] = elemento.getNombreEmpleador();
			row[COL_DOMICILIO] = elemento.getDomicilioEmpleador().getCalle()+" "+elemento.getDomicilioEmpleador().getNumero();
			row[COL_REFERENCIA] = elemento.getReferencia();
			row[COL_TELEFONO] = elemento.getDomicilioEmpleador().getTelefono();
			row[COL_DESDE] = elemento.getFechaDesde();
			row[COL_HASTA] = elemento.getFechaHasta();
			row[COL_TAREAS] = elemento.getTareas();
			row[COL_CERTIFICADO] = elemento.getCertificado();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected DatosEmpleoAnterior getElemento(int fila) {
			return (DatosEmpleoAnterior)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarEmpleoAnterior dialog = new JDialogAgregarModificarEmpleoAnterior(getFrame());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getEmpleadoActual().getHistorialEmpleos().add(dialog.getEmpleo());
				refreshTable();
			}
			return false;
		}
		
		public void refreshTable(){
			getTabla().removeAllRows();
			for(DatosEmpleoAnterior empleo : getEmpleadoActual().getHistorialEmpleos()){
				agregarElemento(empleo);
			}
		}
		
		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			getEmpleadoActual().getHistorialEmpleos().remove(filaSeleccionada);
			refreshTable();
			return true;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if(filaSeleccionada > -1){
				DatosEmpleoAnterior empleo = getElemento(filaSeleccionada);
				JDialogAgregarModificarEmpleoAnterior dialog = new JDialogAgregarModificarEmpleoAnterior(getFrame(),empleo);
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					empleo = dialog.getEmpleo();
					getEmpleadoActual().getHistorialEmpleos().set(filaSeleccionada,empleo);
					refreshTable();
				}
			}
		}
	}

	public PanelTablaTrabajos getPanelTablaEmpleosAnteriores() {
		if (panelTablaEmpleosAnteriores == null) {
			panelTablaEmpleosAnteriores = new PanelTablaTrabajos();
			panelTablaEmpleosAnteriores.setPreferredSize(new Dimension(600, 500));
			panelTablaEmpleosAnteriores.setBorder(BorderFactory.createTitledBorder("Empleos anteriores"));
		}
		return panelTablaEmpleosAnteriores;
	}

	private class PanelTablaDatosFamiliares extends PanelTabla<Familiar> {

		private static final long serialVersionUID = -8669621361348182725L;

		private static final int CANT_COLS = 6;
		private static final int COL_PARENTESCO = 0;
		private static final int COL_NOMBRE_Y_APELLIDO = 1;
		private static final int COL_FECHA_NAC = 2;
		private static final int COL_DOMICILIO = 3;
		private static final int COL_DOCUMENTO = 4;
		private static final int COL_OBJ = 5;
		
		public PanelTablaDatosFamiliares() {
			super();
			agregarBotonModificar();
		}
		@Override
		protected FWJTable construirTabla() {
			JComboBox cmbParentescos = new JComboBox();
			GuiUtil.llenarCombo(cmbParentescos, Arrays.asList(EParentesco.values()), true);
			cmbParentescos.addItem("PADRE");
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setComboColumn(COL_PARENTESCO, "Parentesco", cmbParentescos, 100, true);
			tabla.setStringColumn(COL_NOMBRE_Y_APELLIDO, "Nombre y apellido", 150, 150, true);
			tabla.setDateColumn(COL_FECHA_NAC, "Fecha nac.", 80, true);
			tabla.setStringColumn(COL_DOMICILIO, "Domicilio", 100, 100, true);
			tabla.setIntColumn(COL_DOCUMENTO, "Documento", 80, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(Familiar elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PARENTESCO] = elemento.getParentesco();
			row[COL_NOMBRE_Y_APELLIDO] = elemento.getNombre() + " " + elemento.getApellido();
			row[COL_FECHA_NAC] = elemento.getFechaNacimiento();
			row[COL_DOMICILIO] = elemento.getDomicilio().getCalle() + " " + elemento.getDomicilio().getNumero();
			row[COL_DOCUMENTO] = elemento.getNroDocumento();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected Familiar getElemento(int fila) {
			return (Familiar)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarFamiliar dialog = new JDialogAgregarModificarFamiliar(getFrame(),getEmpleadoActual().getDatosFamilia());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getEmpleadoActual().getDatosFamilia().add(dialog.getFamiliar());
				refreshTable();
			}
			return false;
		}
		
		public void refreshTable(){
			getTabla().removeAllRows();
			for(Familiar familiar : getEmpleadoActual().getDatosFamilia()){
				agregarElemento(familiar);
			}
		}
		
		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			getEmpleadoActual().getDatosFamilia().remove(filaSeleccionada);
			refreshTable();
			return true;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if(filaSeleccionada > -1){
				Familiar familiar = getElemento(filaSeleccionada);
				JDialogAgregarModificarFamiliar dialog = new JDialogAgregarModificarFamiliar(getFrame(),getEmpleadoActual().getDatosFamilia(),familiar);
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					familiar = dialog.getFamiliar();
					getEmpleadoActual().getDatosFamilia().set(filaSeleccionada,familiar);
					refreshTable();
				}
			}
		}
	}

	public PanelTablaDatosFamiliares getPanelTablaDatosFamiliares() {
		if (panelTablaDatosFamiliares == null) {
			panelTablaDatosFamiliares = new PanelTablaDatosFamiliares();
			panelTablaDatosFamiliares.setPreferredSize(new Dimension(600, 500));
		}
		return panelTablaDatosFamiliares;
	}

	private String getPathAnterior() {
		if (pathAnterior == null) {
			pathAnterior = "";
		}
		return pathAnterior;
	}

	private void setPathAnterior(String pathAnterior) {
		this.pathAnterior = pathAnterior;
	}
	
	public Empleado getEmpleadoActual() {
		return empleadoActual;
	}

	public void setEmpleadoActual(Empleado empleadoActual) {
		this.empleadoActual = empleadoActual;
	}

	public EmpleadoFacadeRemote getEmpleadoFacade() {
		if (empleadoFacade == null) {
			empleadoFacade = GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class);
		}
		return empleadoFacade;
	}
	
	public JComboBox getCmbNacionalidad() {
		if(cmbNacionalidad == null){
			cmbNacionalidad = new JComboBox();
			GuiUtil.llenarCombo(cmbNacionalidad, GTLPersonalBeanFactory.getInstance().getBean2(NacionalidadFacadeRemote.class).getAllOrderByName(), true);
			cmbNacionalidad.setPreferredSize(new Dimension(80, 20));
			cmbNacionalidad.addItem("OTRA");
			cmbNacionalidad.setSelectedIndex(-1);
			cmbNacionalidad.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						if(getCmbNacionalidad().getSelectedItem().equals("OTRA")){
							String nacionalidad = JOptionPane.showInputDialog(GuiABMEmpleados.this, "Ingrese la nacionalidad", "Alta de nacionalidad",JOptionPane.PLAIN_MESSAGE);
							if(nacionalidad !=null){
								Nacionalidad n = new Nacionalidad();
								n.setNombre(nacionalidad.toUpperCase());
								n = GTLPersonalBeanFactory.getInstance().getBean2(NacionalidadFacadeRemote.class).save(n);
								getCmbNacionalidad().removeAllItems();
								GuiUtil.llenarCombo(getCmbNacionalidad(), GTLPersonalBeanFactory.getInstance().getBean2(NacionalidadFacadeRemote.class).getAllOrderByName(), true);
								getCmbNacionalidad().addItem("OTRA");
								getCmbNacionalidad().setSelectedItem(n);
							}else{
								getCmbNacionalidad().setSelectedIndex(-1);
							}
						}
					}
				}
			});
		}
		return cmbNacionalidad;
	}

	public JComboBox getCmbSexo() {
		if(cmbSexo == null){
			cmbSexo = new JComboBox();
			GuiUtil.llenarCombo(cmbSexo, Arrays.asList(ESexo.values()), true);
		}
		return cmbSexo;
	}

	public JCheckBox getChkJubilado() {
		if(chkJubilado == null){
			chkJubilado = new JCheckBox("Jubilado");
		}
		return chkJubilado;
	}

	public JCheckBox getChkPrivado() {
		if(chkPrivado == null){
			chkPrivado = new JCheckBox("Privado");
			chkPrivado.setVisible(GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin());
		}
		return chkPrivado;
	}
}
