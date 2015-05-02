package ar.com.textillevel.gui.modulos.personal.modulos.legajos.cabecera;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.modulo.cabecera.Cabecera;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class CabeceraLegajos extends Cabecera<ModeloCabeceraLegajos>{

	private static final long serialVersionUID = 8594740077787010404L;

	private CLJNumericTextField txtNroLegajo;
	private CLJTextField txtNombreOApellido;
	private JComboBox cmbSindicatos;
	private JButton btnBuscar;
	private JComboBox cmbTipoBusqueda;
	private JComboBox cmbTipoContrato;
	
	private ModeloCabeceraLegajos modelo;
	
	public CabeceraLegajos() {
		setUpComponentes();
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		add(new JLabel("Nº Legajo: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getTxtNroLegajo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(new JLabel("Nombre o apellido: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getTxtNombreOApellido(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(new JLabel("Sindicato: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getCmbSindicatos(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(new JLabel("Tipo de empleado: "), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 0, 0, 0));
		add(getCmbTipoBusqueda(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 0, 0, 0));
		add(new JLabel("Tipo de contrato: "), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 0, 0, 0));
		add(getCmbTipoContrato(), GenericUtils.createGridBagConstraints(4,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 0, 0, 0));
		add(getBtnBuscar(), GenericUtils.createGridBagConstraints(5, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
	}

	@Override
	public ModeloCabeceraLegajos getModel() {
		if(modelo == null){
			modelo = new ModeloCabeceraLegajos();
		}
		modelo.setModoBusqueda((ETipoBusquedaEmpleados)getCmbTipoBusqueda().getSelectedItem());
		modelo.setNroLegajo(getTxtNroLegajo().getValueWithNull());
		modelo.setSindicato(getCmbSindicatos().getSelectedItem().equals("TODOS")?null:(Sindicato)getCmbSindicatos().getSelectedItem());
		modelo.setNombreOApellido(!StringUtil.isNullOrEmptyString(getTxtNombreOApellido().getText())?getTxtNombreOApellido().getText():null);
		modelo.setTipoContrato(getCmbTipoContrato().getSelectedItem().equals("TODOS")?null:(ETipoContrato)getCmbTipoContrato().getSelectedItem());
		return modelo;
	}

	private CLJNumericTextField getTxtNroLegajo() {
		if(txtNroLegajo == null){
			txtNroLegajo = new CLJNumericTextField();
			txtNroLegajo.setColumns(15);
		}
		return txtNroLegajo;
	}
	
	private JButton getBtnBuscar() {
		if(btnBuscar == null){
			btnBuscar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar_moderno.png","ar/com/textillevel/imagenes/b_buscar_moderno.png");
			btnBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					notificar();
				}
			});
		}
		return btnBuscar;
	}
	
	public JComboBox getCmbSindicatos() {
		if(cmbSindicatos == null){
			cmbSindicatos = new JComboBox();
			List<Sindicato> sindicatos = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class).getAllOrderByName();
			cmbSindicatos.addItem("TODOS");
			for(Sindicato s : sindicatos){
				cmbSindicatos.addItem(s);
			}
		}
		return cmbSindicatos;
	}
	
	public JComboBox getCmbTipoBusqueda() {
		if(cmbTipoBusqueda == null){
			cmbTipoBusqueda = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoBusqueda, Arrays.asList(ETipoBusquedaEmpleados.values()), true);
		}
		return cmbTipoBusqueda;
	}
	
	public CLJTextField getTxtNombreOApellido() {
		if(txtNombreOApellido == null){
			txtNombreOApellido = new CLJTextField();
			txtNombreOApellido.setPreferredSize(new Dimension(120, 20));
		}
		return txtNombreOApellido;
	}

	public JComboBox getCmbTipoContrato() {
		if(cmbTipoContrato == null){
			cmbTipoContrato = new JComboBox();
			cmbTipoContrato.addItem("TODOS");
			for(ETipoContrato tc : ETipoContrato.values()){
				cmbTipoContrato.addItem(tc);
			}
		}
		return cmbTipoContrato;
	}
}
