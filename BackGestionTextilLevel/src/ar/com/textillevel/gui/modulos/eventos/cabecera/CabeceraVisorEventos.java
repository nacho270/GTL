package ar.com.textillevel.gui.modulos.eventos.cabecera;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.EventoFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.controles.PanelPaginador;
import ar.com.textillevel.util.GTLBeanFactory;

public class CabeceraVisorEventos extends Cabecera<ModeloCabeceraVisorEventos> {

	private static final long serialVersionUID = -4900245160474583386L;

	public static final Integer MAX_ROWS = 30;

	private PanelPaginador paginador;
	private PanelDatePicker txtFechaDesde;
	private PanelDatePicker txtFechaHasta;
	private JComboBox cmbUsuarios;
	private final JButton btnBuscar;
	private ModeloCabeceraVisorEventos modeloCabecera;

	@Override
	public ModeloCabeceraVisorEventos getModel() {
		if (modeloCabecera == null) {
			modeloCabecera = new ModeloCabeceraVisorEventos();
		}
		modeloCabecera.setPaginaActual(getPaginador().getPageIndex());
		modeloCabecera.setFechaDesde(DateUtil.redondearFecha(getTxtFechaDesde().getDate() != null ? getTxtFechaDesde().getDate() : DateUtil.getHoy()));
		modeloCabecera.setFechaHasta(DateUtil.redondearFecha(getTxtFechaHasta().getDate() != null ? getTxtFechaHasta().getDate() : DateUtil.getHoy()));
		modeloCabecera.setNombreUsuario(getCmbUsuarios().getSelectedItem().equals("TODOS") ?null:(String)getCmbUsuarios().getSelectedItem());
		return modeloCabecera;
	}

	public CabeceraVisorEventos() {
		super();
		setLayout(new GridBagLayout());
		JPanel panel2 = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER,5,2));
		
		JPanel panelArriba = new JPanel();
		panelArriba.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		panel2.setBorder(BorderFactory.createEtchedBorder());
		panelArriba.add(new JLabel("Usuario: "));
		panelArriba.add(getCmbUsuarios());
		panelArriba.add(getTxtFechaDesde());
		panelArriba.add(getTxtFechaHasta());
		btnBuscar = new JButton("Buscar");
		panelArriba.add(btnBuscar);
		
		panel2.add(panelArriba);
		
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				getPaginador().setPageIndex(1);
				refrescar();
				notificar();
			}
		});
		add(panel2, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.LINE_END, new Insets(15, 5, 0, 5), 0, 0));
		add(getPaginador(), new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.LINE_END, new Insets(15, 5, 0, 5), 0, 0));
	}
	
	private PanelPaginador getPaginador() {
		if (paginador == null) {
			paginador = new PanelPaginador();
			paginador.setRowsPageSize(MAX_ROWS);
			paginador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					refrescar();
					notificar();
				}
			});
		}
		return paginador;
	}

	private void refrescar() {
		try {
			ModeloCabeceraVisorEventos model = getModel();
			model.setPaginaActual(getPaginador().getPageIndex());
			Timestamp fechaDesde = null;
			Timestamp fechaHasta = null;
			if (model.getFechaDesde() != null)
				fechaDesde = new Timestamp(model.getFechaDesde().getTime());
			if (model.getFechaHasta() != null)
				fechaHasta = new Timestamp(DateUtil.getManana(new Timestamp(model.getFechaHasta().getTime())).getTime());
			if ((model.getFechaDesde() != null) && (model.getFechaHasta() != null)) {
				if (fechaHasta.before(fechaDesde)) {
					FWJOptionPane.showErrorMessage(this, "La fecha 'Hasta' debe ser mayor o igual que la fecha 'Desde'", "Validación de fechas");
					return;
				}
			}
			Integer rows = this.getCantidadDeRegistros();
			getPaginador().setRowsCount(rows);
			getPaginador().setRowsPageSize(MAX_ROWS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected Integer getCantidadDeRegistros() {
		return GTLBeanFactory.getInstance().getBean2(EventoFacadeRemote.class).getCantidadDeRegistros(getModel().getNombreUsuario(), getModel().getFechaDesde()	,getModel().getFechaHasta());
	}

	private PanelDatePicker getTxtFechaDesde() {
		if (txtFechaDesde == null) {
			txtFechaDesde = new PanelDatePicker();
			txtFechaDesde.setCaption("Fecha desde:");
			txtFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
		}
		return txtFechaDesde;
	}

	private PanelDatePicker getTxtFechaHasta() {
		if (txtFechaHasta == null) {
			txtFechaHasta = new PanelDatePicker();
			txtFechaHasta.setCaption("Fecha hasta:");
		}
		return txtFechaHasta;
	}
	
	public JComboBox getCmbUsuarios() {
		if(cmbUsuarios == null){
			cmbUsuarios = new JComboBox();
			cmbUsuarios.addItem("TODOS");
			for(UsuarioSistema u : GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).getAllOrderByName()){
				cmbUsuarios.addItem(u.getUsrName());
			}
		}
		return cmbUsuarios;
	}
}
