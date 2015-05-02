package ar.com.textillevel.gui.modulos.odt.acciones;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.odt.gui.JDialogSeleccionarCrearSecuenciaDeTrabajo;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.JDialogEditarSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.stock.InfoBajaStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionCargarSecuenciaDeTrabajoODT extends Accion<OrdenDeTrabajo>{

	public AccionCargarSecuenciaDeTrabajoODT(){
		setNombre("Asignar Secuencia de Trabajo");
		setDescripcion("Permite especificar el proceso necesario para realizar la ODT"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_fabrica2.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_fabrica2_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<OrdenDeTrabajo> e) throws CLException {
		return ejecutarCargaSecuencia(e);
	}

	public static boolean ejecutarCargaSecuencia(AccionEvent<OrdenDeTrabajo> e) {
		OrdenDeTrabajo odt = e.getSelectedElements().get(0);
		odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odt.getId());
		if(odt.getSecuenciaDeTrabajo() == null){
			JDialogSeleccionarCrearSecuenciaDeTrabajo dS = new JDialogSeleccionarCrearSecuenciaDeTrabajo(e.getSource().getFrame(),odt);
			dS.setVisible(true);
			if(dS.isAcepto()){
				Collection<Set<InfoBajaStock>> values = dS.getMapaWarnings().values();
				Set<InfoBajaStock> set = new HashSet<InfoBajaStock>();
				for(Set<InfoBajaStock> s :values){
					set.addAll(s);
				}
				OrdenDeTrabajo odtGrabada = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).grabarODTYDescontarStock(dS.getOdt(),set,GTLGlobalCache.getInstance().getUsuarioSistema());
				//CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "La orden de trabajo se ha guardado con éxito", "Información");
				odtGrabada = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odtGrabada.getId());
				JDialogEditarSecuenciaODT dialog = new JDialogEditarSecuenciaODT(e.getSource().getFrame(), odtGrabada);
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).grabarODT(dialog.getOdt(),GTLGlobalCache.getInstance().getUsuarioSistema());
					CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "La orden de trabajo se ha guardado con éxito", "Información");
				}
				return true;
			}
		}else{
			JDialogEditarSecuenciaODT dialog = new JDialogEditarSecuenciaODT(e.getSource().getFrame(), odt);
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).grabarODT(dialog.getOdt(),GTLGlobalCache.getInstance().getUsuarioSistema());
				CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "La orden de trabajo se ha guardado con éxito", "Información");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<OrdenDeTrabajo> e) {
		return e.getSelectedElements().size()==1;// && e.getSelectedElements().get(0).getEstado()==EEstadoODT.PENDIENTE;
	}
}
