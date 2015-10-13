package main.acciones.personal.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.swing.Action;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.modulos.personal.informes.vacaciones.planificacion.JDialogPlanificacionVacaciones;
import ar.com.textillevel.gui.util.dialogs.JDialogInputFechaDesdeHasta;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.facade.api.remote.VacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class PlanificacionVacacionesPersonalAction implements Action {

	private final Frame frame;

	public PlanificacionVacacionesPersonalAction(Frame frame) {
		this.frame = frame;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {

	}

	public Object getValue(String key) {
		return null;
	}

	public boolean isEnabled() {
		return false;
	}

	public void putValue(String key, Object value) {

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {

	}

	public void setEnabled(boolean b) {

	}

	public void actionPerformed(ActionEvent e) {
		JDialogInputFechaDesdeHasta dialog = new JDialogInputFechaDesdeHasta(frame,"Planificación de vacaciones");
		dialog.setVisible(true);
		if (dialog.isAcepto()) {
			Date fechaDesde = dialog.getFechaDesdeElegida();
			Date fechaHasta = dialog.getFechaHastaElegida();
			
			int mesDesde = DateUtil.getMes(fechaDesde);
			int mesHasta = DateUtil.getMes(fechaHasta);
			
			//se asume que la fecha hasta es mayor a la fecha desde, por ende, si el mes hasta es menor al desde, es otro año
			if(mesHasta<mesDesde){
				FWJOptionPane.showErrorMessage(frame, "Las fechas deben corresponder al mismo año", "Error");
				return;
			}
			fechaDesde = DateUtil.setDia(fechaDesde, 1);
			Calendar calendar = Calendar.getInstance();
			calendar.set(DateUtil.getAnio(fechaHasta), DateUtil.getMes(fechaHasta), 1);
			int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			fechaHasta = DateUtil.setDia(fechaHasta, ultimoDia);
			List<RegistroVacacionesLegajo> regs = GTLPersonalBeanFactory.getInstance().getBean2(VacacionesFacadeRemote.class).getAllRegistrosVacacionesByFecha(fechaDesde, fechaHasta);
			if(regs!=null && !regs.isEmpty()){
				new JDialogPlanificacionVacaciones(frame,regs,fechaDesde,fechaHasta).setVisible(true);
			}else{
				FWJOptionPane.showErrorMessage(frame, "No se han encontrado resultados", "Error");
			}
		}
	}
}
