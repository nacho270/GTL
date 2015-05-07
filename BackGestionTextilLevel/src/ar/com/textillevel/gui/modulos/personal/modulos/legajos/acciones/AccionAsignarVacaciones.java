package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogAgregarModificarPeriodoVacaciones;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.PeriodoVacaciones;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.VacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;
import ar.com.textillevel.modulos.personal.utils.VacacionesHelper;

public class AccionAsignarVacaciones extends Accion<Empleado>{

	public AccionAsignarVacaciones(){
		setNombre("Asignar/modificar vacaciones");
		setDescripcion("Permite asignar o modifcar período de vacaciones actual para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_asignar_vacaciones.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_asignar_vacaciones_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws CLException {
		ConfiguracionVacaciones conf = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVacacionesFacadeRemote.class).getConfiguracionVacaciones(DateUtil.getHoy());
		if(conf == null){
			CLJOptionPane.showErrorMessage(e.getSource().getFrame(), "No existe una configuración de vacaciones vigente para la fecha actual", "Error");
			return false;
		}
		Empleado empleado = e.getSelectedElements().get(0);
		LegajoEmpleado legajo = empleado.getLegajo();
		List<VigenciaEmpleado> historialVigencias = legajo.getHistorialVigencias();
		VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
		PeriodoVacaciones periodoCorrespondiente = VacacionesHelper.getPeriodoCorrespondiente(conf, ultima.getFechaAlta());
		if(periodoCorrespondiente == null){
			CLJOptionPane.showWarningMessage(e.getSource().getFrame(), "El empleado tiene menos de " + conf.getMesesMinimosParaEntrar() + " meses de antigüedad.\nSe calcula 1 día de vacaciones por cada 20 trabajados.", "Advertencia");
			//TODO: CALCULAR 1 DIA POR CADA 20 TRABAJADOS
		}
		List<RegistroVacacionesLegajo> historialVacaciones = legajo.getHistorialVacaciones();
		JDialogAgregarModificarPeriodoVacaciones dialogo = new JDialogAgregarModificarPeriodoVacaciones(e.getSource().getFrame(), legajo,periodoCorrespondiente);
		dialogo.setVisible(true);
		if (dialogo.isAcepto()) {
			RegistroVacacionesLegajo registroActual = dialogo.getRegistroActual();
			historialVacaciones.add(registroActual);
			Collections.sort(historialVacaciones, new Comparator<RegistroVacacionesLegajo>() {
				public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
					return o1.getFechaDesde().compareTo(o2.getFechaDesde());
				}
			});
			empleado.setLegajo(legajo);
			GTLPersonalBeanFactory.getInstance().getBean2(VacacionesFacadeRemote.class).grabarVacaciones(empleado, registroActual);
		}
		return true;
	}
	
	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		if( e.getSelectedElements().size() == 1){
			LegajoEmpleado legajo = e.getSelectedElements().get(0).getLegajo();
			List<VigenciaEmpleado> historialVigencias = legajo.getHistorialVigencias();
			if(legajo!=null && !historialVigencias.isEmpty()){
				VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
				return legajo!=null && ultima.getFechaBaja()==null;
			}
		}
		return false;
	}

}
