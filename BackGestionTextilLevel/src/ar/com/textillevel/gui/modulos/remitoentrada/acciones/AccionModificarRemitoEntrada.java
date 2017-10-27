package ar.com.textillevel.gui.modulos.remitoentrada.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO.ETipoREModulo;
import ar.com.textillevel.util.GTLBeanFactory;
import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;

public class AccionModificarRemitoEntrada extends Accion<RemitoEntradaModuloTO> {

	private RemitoEntradaFacadeRemote reFacade;

	public AccionModificarRemitoEntrada(){
		setNombre("Modificar Remito de Entrada");
		setDescripcion("Permite modificart un remito de entrada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_modificar_fila.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_modificar_fila_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<RemitoEntradaModuloTO> e) throws FWException {
		RemitoEntradaModuloTO reTO = e.getSelectedElements().get(0);
		if(reTO.getTipoRE() == ETipoREModulo.RE_CON_PIEZAS) {
			RemitoEntrada remitoEntrada = getRemitoEntradaFacade().getByIdEager(reTO.getId());
			OperacionSobreRemitoEntradaHandler consultaREHandler = new OperacionSobreRemitoEntradaHandler(e.getSource().getFrame(), remitoEntrada, false);
			consultaREHandler.showRemitoEntradaDialog();
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<RemitoEntradaModuloTO> e) {
		return e.getSelectedElements().size() == 1;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(reFacade == null) {
			reFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return reFacade;
	}

}