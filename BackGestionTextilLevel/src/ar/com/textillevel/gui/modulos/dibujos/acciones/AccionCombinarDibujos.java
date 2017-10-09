package ar.com.textillevel.gui.modulos.dibujos.acciones;

import java.util.ArrayList;
import java.util.List;
import org.apache.taglibs.string.util.StringW;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.modulos.dibujos.gui.JDialogAgregarModificarDibujoEstampado;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionCombinarDibujos extends Accion<DibujoEstampado> {

	public AccionCombinarDibujos(){
		setNombre("Combinar Dibujos");
		setDescripcion("Permite combinar dibujos de un mismo cliente"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_combinar_dibujo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_combinar_dibujo_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		List<DibujoEstampado> dibujosCombinados = new ArrayList<DibujoEstampado>();
		DibujoEstampado d1 = e.getSelectedElements().get(0);
		dibujosCombinados.add(d1);		
		DibujoEstampado d2 = e.getSelectedElements().get(1);
		dibujosCombinados.add(d2);		
		JDialogAgregarModificarDibujoEstampado dialog = new JDialogAgregarModificarDibujoEstampado(e.getSource().getFrame(), null, d1.getCantidadColores() + d2.getCantidadColores());
		dialog.setVisible(true);
		if (dialog.isAcepto()) {
			try {
				GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).combinarDibujos(dialog.getDibujoActual(), dibujosCombinados, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				return true;
			} catch (ValidacionException e1) {
				e1.printStackTrace();
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(e1.getMessage()), "Error");
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		if(e.getSelectedElements().size() == 2) {
			DibujoEstampado d1 = e.getSelectedElements().get(0);
			DibujoEstampado d2 = e.getSelectedElements().get(1);
			//mismo cliente y estado en_stock
			return d1.getCliente().getId() != null && d1.getCliente().getId() != null && d1.getCliente().getId().equals(d2.getCliente().getId()) && d1.getEstado() == EEstadoDibujo.EN_STOCK && d2.getEstado() == EEstadoDibujo.EN_STOCK;
		}
		return false;
	}

}