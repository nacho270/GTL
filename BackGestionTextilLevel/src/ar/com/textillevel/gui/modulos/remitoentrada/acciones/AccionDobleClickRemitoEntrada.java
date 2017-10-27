package ar.com.textillevel.gui.modulos.remitoentrada.acciones;

import ar.com.fwcommon.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.facade.api.remote.RemitoEntradaDibujoFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO.ETipoREModulo;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;
import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;
import main.acciones.facturacion.gui.JDialogIngresarDibujosEstampado;

public class AccionDobleClickRemitoEntrada extends AccionAdicional<RemitoEntradaModuloTO> {

	private RemitoEntradaFacadeRemote reFacade;
	private RemitoEntradaDibujoFacadeRemote remitoEntradaDibujoFacade;

	@Override
	protected void update(AccionEvent<RemitoEntradaModuloTO> e) {
		if(e.getSelectedElements().isEmpty()) {
			return;
		}
		RemitoEntradaModuloTO reTO = e.getSelectedElements().get(0);
		if(reTO.getTipoRE() == ETipoREModulo.RE_CON_PIEZAS) {
			RemitoEntrada remitoEntrada = getRemitoEntradaFacade().getByIdEager(reTO.getId());
			OperacionSobreRemitoEntradaHandler handler = new OperacionSobreRemitoEntradaHandler(e.getSource().getFrame(), remitoEntrada, true);
			handler.showRemitoEntradaDialog();
		}
		if(reTO.getTipoRE() == ETipoREModulo.RE_CON_DIBUJOS) {
			RemitoEntradaDibujo red = getRemitoEntradaDibujoFacade().getByIdEager(reTO.getId());
			JDialogIngresarDibujosEstampado dialogo = new JDialogIngresarDibujosEstampado(e.getSource().getFrame(), red);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				getRemitoEntradaDibujoFacade().grabarREDibujo(red, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
			}
		}
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(reFacade == null) {
			reFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return reFacade;
	}

	private RemitoEntradaDibujoFacadeRemote getRemitoEntradaDibujoFacade() {
		if(remitoEntradaDibujoFacade == null) {
			remitoEntradaDibujoFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaDibujoFacadeRemote.class);
		}
		return remitoEntradaDibujoFacade;
	}

}
