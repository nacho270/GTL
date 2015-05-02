package ar.com.textillevel.gui.modulos.personal.informes.vacaciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.GTLGlobalCache;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogParametrosInformeVacaciones extends JDialog {

	private static final long serialVersionUID = 5842880315308424997L;

	private JRadioButton radioButtonAnioCorrespondiente;
	private JRadioButton radioButtonFechaDesdeHasta;
	private ButtonGroup grupoRadios;
	private JPanel panelAnioCorrespondiente;
	private JPanel panelFechaDesdeHasta;
	private PanelDatePicker panelFechaHasta;
	private PanelDatePicker panelFechaDesde;
	private JComboBox cmbEmpleadosActivos;
	private JComboBox cmbAnios;
	private JButton btnVerReporte;
	private JButton btnSalir;

	private EmpleadoFacadeRemote empleadosFacade;
	
	public JDialogParametrosInformeVacaciones(Frame frame) {
		super(frame);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Reporte de vacaciones");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setSize(new Dimension(550, 250));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentro(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Empleado: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbEmpleadosActivos(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		panel.add(getPanelRadios(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
		
		return panel;
	}
	
	private JPanel getPanelRadios(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Filtro de fechas"));
		panel.add(getRadioButtonAnioCorrespondiente(),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getPanelAnioCorrespondiente(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		panel.add(getRadioButtonFechaDesdeHasta(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getPanelFechaDesdeHasta(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		crearGrupoRadios();
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnVerReporte());
		panel.add(getBtnSalir());
		return panel;
	}

	public JRadioButton getRadioButtonAnioCorrespondiente() {
		if(radioButtonAnioCorrespondiente == null){
			radioButtonAnioCorrespondiente = new JRadioButton("Por año correspondiente",true);
			GuiUtil.setEstadoPanel(getPanelAnioCorrespondiente(), true);
			radioButtonAnioCorrespondiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GuiUtil.setEstadoPanel(getPanelFechaDesdeHasta(), !getRadioButtonAnioCorrespondiente().isSelected());
					GuiUtil.setEstadoPanel(getPanelAnioCorrespondiente(), getRadioButtonAnioCorrespondiente().isSelected());
				}
			});
		}
		return radioButtonAnioCorrespondiente;
	}

	public JRadioButton getRadioButtonFechaDesdeHasta() {
		if(radioButtonFechaDesdeHasta == null){
			radioButtonFechaDesdeHasta = new JRadioButton("Por fecha",false);
			GuiUtil.setEstadoPanel(getPanelFechaDesdeHasta(), false);
			radioButtonFechaDesdeHasta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GuiUtil.setEstadoPanel(getPanelFechaDesdeHasta(), getRadioButtonFechaDesdeHasta().isSelected());
					GuiUtil.setEstadoPanel(getPanelAnioCorrespondiente(), !getRadioButtonFechaDesdeHasta().isSelected());
				}
			});
		}
		return radioButtonFechaDesdeHasta;
	}

	public ButtonGroup crearGrupoRadios() {
		if (grupoRadios == null) {
			grupoRadios = new ButtonGroup();
			grupoRadios.add(getRadioButtonAnioCorrespondiente());
			grupoRadios.add(getRadioButtonFechaDesdeHasta());
		}
		return grupoRadios;
	}

	public JPanel getPanelAnioCorrespondiente() {
		if(panelAnioCorrespondiente == null){
			panelAnioCorrespondiente = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelAnioCorrespondiente.add(getCmbAnios());
		}
		return panelAnioCorrespondiente;
	}

	public JPanel getPanelFechaDesdeHasta() {
		if(panelFechaDesdeHasta == null){
			panelFechaDesdeHasta = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelFechaDesdeHasta.add(getPanelFechaDesde());
			panelFechaDesdeHasta.add(getPanelFechaHasta());
		}
		return panelFechaDesdeHasta;
	}
	
	public JButton getBtnVerReporte() {
		if(btnVerReporte == null){
			btnVerReporte = new JButton("Ver reporte");
			btnVerReporte.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						new JDialogVerReporteVacaciones(JDialogParametrosInformeVacaciones.this,crearParametrosInformeVacaciones()).setVisible(true);
					}
				}
			});
		}
		return btnVerReporte;
	}

	private ParametrosInformeVacaciones crearParametrosInformeVacaciones(){
		return new ParametrosInformeVacaciones();
	}
	
	private boolean validar(){
		return true;
	}
	
	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public JComboBox getCmbAnios() {
		if(cmbAnios == null){
			cmbAnios = new JComboBox();
			for(int anio = Math.min(2012, DateUtil.getAnio(DateUtil.getHoy())-10);anio< DateUtil.getAnio(DateUtil.getHoy())+2;anio++){
				cmbAnios.addItem(anio);
			}
		}
		return cmbAnios;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if(panelFechaHasta == null){
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Hasta: ");
		}
		return panelFechaHasta;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if(panelFechaDesde == null){
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Desde: ");
		}
		return panelFechaDesde;
	}

	public JComboBox getCmbEmpleadosActivos() {
		if(cmbEmpleadosActivos == null){
			cmbEmpleadosActivos = new JComboBox();
			boolean isAdmin = GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin();
			List<Empleado> empleadosActivos = getEmpleadosFacade().buscarEmpleados(null, ETipoBusquedaEmpleados.ACTIVOS, null, null, isAdmin, null);
			cmbEmpleadosActivos.addItem(null);
			if(empleadosActivos!=null){
				for(Empleado e : empleadosActivos){
					cmbEmpleadosActivos.addItem(e);
				}
			}
		}
		return cmbEmpleadosActivos;
	}

	public EmpleadoFacadeRemote getEmpleadosFacade() {
		if(empleadosFacade == null){
			empleadosFacade = GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class);
		}
		return empleadosFacade;
	}
}
