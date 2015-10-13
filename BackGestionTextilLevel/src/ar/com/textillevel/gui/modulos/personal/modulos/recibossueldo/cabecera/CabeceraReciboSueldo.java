package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.entidades.Mes;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ETipoCobro;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;
import ar.com.textillevel.modulos.personal.facade.api.remote.MesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class CabeceraReciboSueldo extends Cabecera<ModeloCabeceraReciboSueldo> {

	private static final long serialVersionUID = -5201286907217207478L;

	private JLabel lblQuincena;
	private FWJNumericTextField txtAnio;
	private FWJNumericTextField txtNroLegajo;
	private FWJTextField txtNombreOApellido;
	private JComboBox cmbSindicatos;
	private JComboBox cmbMes;
	private JComboBox cmbQuincena;
	private JButton btnBuscar;
	
	private ModeloCabeceraReciboSueldo modelo;
	
	public CabeceraReciboSueldo() {
		setUpComponentes();
	}

	private void setUpComponentes() {
		setLayout(new GridBagLayout());
		add(new JLabel("Sindicato: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getCmbSindicatos(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(new JLabel("Año: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getTxtAnio(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(new JLabel("Mes: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getCmbMes(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getLblQuincena(), GenericUtils.createGridBagConstraints(6, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getCmbQuincena(), GenericUtils.createGridBagConstraints(7, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		add(new JLabel("Nº Legajo: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getTxtNroLegajo(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(new JLabel("Nombre o apellido: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 15), 1, 1, 0, 0));
		add(getTxtNombreOApellido(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getBtnBuscar(), GenericUtils.createGridBagConstraints(4, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
	}

	private JLabel getLblQuincena() {
		if(lblQuincena == null) {
			lblQuincena = new JLabel("Quincena: ");
		}
		return lblQuincena;
	}

	@Override
	public ModeloCabeceraReciboSueldo getModel() {
		if(modelo == null){
			modelo = new ModeloCabeceraReciboSueldo();
		}
		modelo.setNroLegajo(getTxtNroLegajo().getValueWithNull());
		modelo.setNombreOApellido(!StringUtil.isNullOrEmptyString(getTxtNombreOApellido().getText())?getTxtNombreOApellido().getText():null);
		modelo.setSindicato((Sindicato)getCmbSindicatos().getSelectedItem());
		modelo.setAnio(getTxtAnio().getValue());
		modelo.setMes((Mes)getCmbMes().getSelectedItem());
		modelo.setQuincena((EQuincena)getCmbQuincena().getSelectedItem());
		return modelo;
	}

	private FWJNumericTextField getTxtNroLegajo() {
		if(txtNroLegajo == null){
			txtNroLegajo = new FWJNumericTextField();
			txtNroLegajo.setColumns(15);
		}
		return txtNroLegajo;
	}

	private FWJNumericTextField getTxtAnio() {
		if(txtAnio == null){
			txtAnio = new FWJNumericTextField();
			txtAnio.setColumns(15);
			txtAnio.setValue(Long.valueOf(DateUtil.getAnio(DateUtil.getHoy())));
		}
		return txtAnio;
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
			for(Sindicato s : sindicatos){
				cmbSindicatos.addItem(s);
			}

			cmbSindicatos.setSelectedIndex(-1);
			
			cmbSindicatos.addItemListener(new ItemListener() {
				
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						Sindicato s = (Sindicato)getCmbSindicatos().getSelectedItem();
						boolean esSindicatoQuincenal = s.getTipoCobro() == ETipoCobro.QUINCENAL;
						if(esSindicatoQuincenal) {
							getCmbQuincena().setEnabled(true);
						} else {
							getCmbQuincena().setEnabled(false);
							getCmbQuincena().setSelectedIndex(-1);
						}
					}
				}

			});

		}
		return cmbSindicatos;
	}

	public JComboBox getCmbMes() {
		if(cmbMes == null){
			cmbMes = new JComboBox();
			List<Mes> meses = GTLPersonalBeanFactory.getInstance().getBean2(MesFacadeRemote.class).getAllMeses();
			for(Mes m : meses){
				cmbMes.addItem(m);
			}
		}
		return cmbMes;
	}

	public JComboBox getCmbQuincena() {
		if(cmbQuincena == null){
			cmbQuincena = new JComboBox();
			cmbQuincena.addItem(EQuincena.PRIMERA);
			cmbQuincena.addItem(EQuincena.SEGUNDA);
		}
		return cmbQuincena;
	}

	public FWJTextField getTxtNombreOApellido() {
		if(txtNombreOApellido == null){
			txtNombreOApellido = new FWJTextField();
			txtNombreOApellido.setPreferredSize(new Dimension(120, 20));
		}
		return txtNombreOApellido;
	}


	
}
