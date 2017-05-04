package ar.com.textillevel.gui.modulos.odt.acciones;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.odt.gui.JDialogSeleccionarCrearSecuenciaDeTrabajo;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.JDialogEditarSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.modulos.odt.to.stock.InfoBajaStock;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionCargarSecuenciaDeTrabajoODT extends Accion<ODTTO> {

	public AccionCargarSecuenciaDeTrabajoODT(){
		setNombre("Asignar Secuencia de Trabajo");
		setDescripcion("Permite especificar el proceso necesario para realizar la ODT"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_fabrica2.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_fabrica2_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<ODTTO> e) throws FWException {
		return ejecutarCargaSecuencia(e);
	}

	public static boolean ejecutarCargaSecuencia(AccionEvent<ODTTO> e) {
		ODTTO odtto = e.getSelectedElements().get(0);
		OrdenDeTrabajo odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odtto.getId());
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
					FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "La orden de trabajo se ha guardado con éxito", "Información");
				}
				return true;
			}
		}else{
			JDialogEditarSecuenciaODT dialog = new JDialogEditarSecuenciaODT(e.getSource().getFrame(), odt);
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).grabarODT(dialog.getOdt(),GTLGlobalCache.getInstance().getUsuarioSistema());
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "La orden de trabajo se ha guardado con éxito", "Información");
				return true;
			}
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<ODTTO> e) {
		return e.getSelectedElements().size()==1;
	}

}
