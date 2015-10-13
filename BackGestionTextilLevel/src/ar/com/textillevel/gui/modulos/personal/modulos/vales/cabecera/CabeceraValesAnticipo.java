package ar.com.textillevel.gui.modulos.personal.modulos.vales.cabecera;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;

public class CabeceraValesAnticipo extends Cabecera<ModeloCabeceraValesAnticipo> {

	private static final long serialVersionUID = 3006861598962219548L;

	private PanelDatePicker txtFechaDesde;
	private PanelDatePicker txtFechaHasta;
	private JComboBox cmbEstadoVale;
	private FWJTextField txtApellidoEmpleado;
	private JButton btnBuscar;

	private ModeloCabeceraValesAnticipo modelo;

	public CabeceraValesAnticipo() {
		super();
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(new JLabel("Apellido empleado: "));
		add(getTxtApellidoEmpleado());
		add(getTxtFechaDesde());
		add(getTxtFechaHasta());
		add(new JLabel("Estado: "));
		add(getCmbEstadoVale());
		add(getBtnBuscar());
	}

	@Override
	public ModeloCabeceraValesAnticipo getModel() {
		if (modelo == null) {
			modelo = new ModeloCabeceraValesAnticipo();
		}
		modelo.setApellidoEmpleado(getTxtApellidoEmpleado().getText().trim().length() == 0 ? null : getTxtApellidoEmpleado().getText().trim());
		modelo.setFechaDesde(getTxtFechaDesde().getDate() != null ? new java.sql.Date(getTxtFechaDesde().getDate().getTime()) : null);
		modelo.setFechaHasta(getTxtFechaHasta().getDate() != null ? new java.sql.Date(getTxtFechaHasta().getDate().getTime()) : null);
		modelo.setEstadoVale(getCmbEstadoVale().getSelectedItem().equals("TODOS")?null:(EEstadoValeAnticipo)getCmbEstadoVale().getSelectedItem());
		return modelo;
	}

	public PanelDatePicker getTxtFechaDesde() {
		if (txtFechaDesde == null) {
			txtFechaDesde = new PanelDatePicker();
			txtFechaDesde.setCaption("Fecha desde:");
			txtFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
		}
		return txtFechaDesde;
	}

	public PanelDatePicker getTxtFechaHasta() {
		if (txtFechaHasta == null) {
			txtFechaHasta = new PanelDatePicker();
			txtFechaHasta.setCaption("Fecha hasta:");
		}
		return txtFechaHasta;
	}

	public FWJTextField getTxtApellidoEmpleado() {
		if (txtApellidoEmpleado == null) {
			txtApellidoEmpleado = new FWJTextField();
			txtApellidoEmpleado.setPreferredSize(new Dimension(150, 20));
		}
		return txtApellidoEmpleado;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar_moderno.png", "ar/com/textillevel/imagenes/b_buscar_moderno.png");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					notificar();
				}
			});
		}
		return btnBuscar;
	}

	public JComboBox getCmbEstadoVale() {
		if(cmbEstadoVale == null){
			cmbEstadoVale = new JComboBox();
			cmbEstadoVale.addItem("TODOS");
			for(EEstadoValeAnticipo e : EEstadoValeAnticipo.values()){
				cmbEstadoVale.addItem(e);
			}
		}
		return cmbEstadoVale;
	}
}
