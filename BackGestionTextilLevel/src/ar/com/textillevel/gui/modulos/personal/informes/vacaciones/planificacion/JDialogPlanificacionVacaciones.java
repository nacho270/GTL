package ar.com.textillevel.gui.modulos.personal.informes.vacaciones.planificacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.calendario.EventoCalendario;
import ar.com.textillevel.gui.util.controles.calendario.PanelCalendario;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;

public class JDialogPlanificacionVacaciones extends JDialog {

	private static final long serialVersionUID = 3500830740235106771L;

	private PanelCalendario pc;
	private JButton btnAceptar;

	private List<RegistroVacacionesLegajo> allRegs;
	private List<EventoCalendario> allEvts;
	private Date fechaDesde;
	private Date fechaHasta;

	public JDialogPlanificacionVacaciones(Frame frame, List<RegistroVacacionesLegajo> registros, Date fechaDesde, Date fechaHasta) {
		super(frame);
		setAllRegs(registros);
		setAllEvts(convertirRegistrosDeVacacionesAEventos(registros));
		setFechaDesde(fechaDesde);
		setFechaHasta(fechaHasta);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Planificación de vacaciones de " + PanelCalendario.MONTHS[DateUtil.getMes(getFechaDesde())] + " a " +  PanelCalendario.MONTHS[DateUtil.getMes(getFechaHasta())]);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(GenericUtils.getDimensionPantalla());
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(new JScrollPane(getPc(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		p.add(getBtnAceptar());
		add(p,BorderLayout.SOUTH);
	}

	private List<EventoCalendario> convertirRegistrosDeVacacionesAEventos(List<RegistroVacacionesLegajo> regs) {
		List<EventoCalendario> lista = new ArrayList<EventoCalendario>();
		for (RegistroVacacionesLegajo r : regs) {
			Date fechaInicio = r.getFechaDesde();
			Date fechaFin = r.getFechaHasta();
			while (fechaInicio.compareTo(fechaFin) < 1) {
				lista.add(new EventoCalendario("Vacaciones: " + r.getLegajo().getEmpleado().getNombreYApellido(), fechaInicio));
				fechaInicio = DateUtil.sumarDias(fechaInicio, 1);
			}
		}
		return lista;
	}

	public PanelCalendario getPc() {
		if (pc == null) {
			pc = new PanelCalendario(getFechaDesde(), getFechaHasta(), getAllEvts());
		}
		return pc;
	}

	public List<RegistroVacacionesLegajo> getAllRegs() {
		return allRegs;
	}

	public void setAllRegs(List<RegistroVacacionesLegajo> allRegs) {
		this.allRegs = allRegs;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public List<EventoCalendario> getAllEvts() {
		return allEvts;
	}

	public void setAllEvts(List<EventoCalendario> allEvts) {
		this.allEvts = allEvts;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}
}
