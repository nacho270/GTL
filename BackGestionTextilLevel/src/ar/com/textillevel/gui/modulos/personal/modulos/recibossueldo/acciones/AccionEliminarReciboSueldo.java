package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.acciones;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionEliminarReciboSueldo extends Accion<InfoReciboSueltoTO> {

	private ReciboSueldoFacadeRemote reciboSueldoFacade;

	public AccionEliminarReciboSueldo(){
		setNombre("Eliminar Recibo de Sueldo");
		setDescripcion("Permite eliminar un recibo de sueldo"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar_recibo_sueldo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_recibo_sueldo_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<InfoReciboSueltoTO> e) throws FWException {
		if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), StringW.wordWrap("¿Está seguro que desea eliminar el recibo de sueldo seleccionado?"), "Confirmación") == FWJOptionPane.YES_OPTION) { 
			InfoReciboSueltoTO irsto = e.getSelectedElements().get(0);
			getReciboSueldoFacade().eliminarReciboSueldo(irsto.getReciboSueldo());
			FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "El recibo de sueldo se ha eliminado con éxito.", "Información");
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<InfoReciboSueltoTO> e) {
		if(e.getSelectedElements().isEmpty()) {
			return false;
		} else {
			ReciboSueldo reciboSueldo = e.getSelectedElements().get(0).getReciboSueldo();
			return reciboSueldo != null && reciboSueldo.getId() != null;
		}
	}

	private ReciboSueldoFacadeRemote getReciboSueldoFacade() {
		if(reciboSueldoFacade == null) {
			reciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ReciboSueldoFacadeRemote.class);
		}
		return reciboSueldoFacade;
	}

}