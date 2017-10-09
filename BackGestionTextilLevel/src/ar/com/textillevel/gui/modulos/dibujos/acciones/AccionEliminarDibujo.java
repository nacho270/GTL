package ar.com.textillevel.gui.modulos.dibujos.acciones;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionEliminarDibujo extends Accion<DibujoEstampado> {

	public AccionEliminarDibujo() {
		setNombre("Eliminar dibujo");
		setDescripcion("Permite eliminar un dibujo");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		DibujoEstampadoFacadeRemote dibujoFacade = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		DibujoEstampado dibujoEstampado = e.getSelectedElements().get(0);
		try {
			if (FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Está seguro que desea eliminar el dibujo seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				try {
					dibujoFacade.remove(dibujoEstampado, false, true, usuario);
				} catch (ValidacionExceptionSinRollback e1) {
					FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(e1.getMensajeError()), "Error");
					return false;
				}
			}
		} catch (ValidacionException e2) {
			if(e2.getCodigoError() == EValidacionException.DIBUJO_USADO_EN_LISTAS_DE_PRECIOS.getCodigo()) {
				if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), StringW.wordWrap(e2.getMensajeError()), "Confirmación") == FWJOptionPane.YES_OPTION) {
					try {
						try {
							dibujoFacade.remove(dibujoEstampado, true, true, usuario);
						} catch (ValidacionExceptionSinRollback e1) {
							FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(e1.getMensajeError()), "Error");
							return false;
						}
					} catch (ValidacionException e1) {
						FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(e1.getMensajeError()), "Error");
						return false;
					}
				} else {
					return false;
				}
			} else {
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(e2.getMensajeError()), "Error");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1;
	}
}
