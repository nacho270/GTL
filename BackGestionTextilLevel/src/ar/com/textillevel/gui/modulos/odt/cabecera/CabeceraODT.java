package ar.com.textillevel.gui.modulos.odt.cabecera;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;

public class CabeceraODT extends Cabecera<ModeloCabeceraODT> {

	private static final long serialVersionUID = 4798710431798792144L;

	private ModeloCabeceraODT model;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private JComboBox cmbEstadoODT;

	private JButton btnBuscar;
	
	public CabeceraODT() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Filtros"));
		panel.add(new JLabel("Estado: "));
		panel.add(getCmbEstadoODT());
		panel.add(getPanelFechaDesde());
		panel.add(getPanelFechaHasta());
		panel.add(getBtnBuscar());
		add(panel,BorderLayout.CENTER);
	}

	@Override
	public ModeloCabeceraODT getModel() {
		if (model == null) {
			model = new ModeloCabeceraODT();
		}
		Date fechaDesde = getPanelFechaDesde().getDate();
		Date fechaHasta = getPanelFechaHasta().getDate();
		model.setFechaDesde(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null);
		model.setFechaHasta(fechaHasta!=null?DateUtil.getManiana(new java.sql.Date(fechaHasta.getTime())):null);
		Object selectedItem = getCmbEstadoODT().getSelectedItem();
		model.setEstadoODT(selectedItem.equals("TODOS")?null:(EEstadoODT)selectedItem);
		return model;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if(panelFechaDesde == null){
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Fecha desde: ");
			panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 5));
		}
		return panelFechaDesde;
	}

	private PanelDatePicker getPanelFechaHasta() {
		if(panelFechaHasta == null){
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Fecha hasta: ");
			panelFechaHasta.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaHasta;
	}

	private JComboBox getCmbEstadoODT() {
		if(cmbEstadoODT == null){
			cmbEstadoODT = new JComboBox();
			for(EEstadoODT estado : EEstadoODT.values()){
				if(estado == EEstadoODT.ANTERIOR){
					continue;
				}
				cmbEstadoODT.addItem(estado);
			}
		}
		return cmbEstadoODT;
	}

	private JButton getBtnBuscar() {
		if(btnBuscar == null){
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					notificar();
				}
			});
		}
		return btnBuscar;
	}
}
