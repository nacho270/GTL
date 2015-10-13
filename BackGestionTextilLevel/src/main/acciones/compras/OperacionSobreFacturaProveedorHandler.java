package main.acciones.compras;

import java.awt.Frame;
import java.util.ArrayList;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.enums.ETipoFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.facade.api.remote.FacturaProveedorFacadeRemote;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaProveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarFacturaServicioProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class OperacionSobreFacturaProveedorHandler {

	private Frame owner;
	private FacturaProveedor facturaProveedor;
	private FacturaProveedorFacadeRemote facturaProveedorFacade;
	private boolean modoConsulta; 

	public OperacionSobreFacturaProveedorHandler(Frame owner, FacturaProveedor facturaProveedor, boolean modoConsulta) {
		this.owner = owner;
		this.facturaProveedor = facturaProveedor;
		this.modoConsulta = modoConsulta;
	}

	public void showFacturaProveedorDialog() {
		if(facturaProveedor.getTipoFacturaProveedor() == null) {
			handleConsultaFacturaProveedorNormal();
		} else {
			if(facturaProveedor.getTipoFacturaProveedor() == ETipoFacturaProveedor.NORMAL) {
				handleConsultaFacturaProveedorNormal();
			} else if(facturaProveedor.getTipoFacturaProveedor() == ETipoFacturaProveedor.SIN_REMITO) {
				handleConsultaFacturaProveedorSinRemito();
			} else {
				handleConsultaFacturaProveedorServicio();
			}
		}
		
	}

	private void handleConsultaFacturaProveedorNormal() {
		facturaProveedor = getFacturaProveedorFacade().getByIdEager(facturaProveedor.getId());
		JDialogCargarFacturaProveedor dialogo = new JDialogCargarFacturaProveedor(owner, facturaProveedor, modoConsulta, new ArrayList<RemitoEntradaProveedor>());
		GuiUtil.centrar(dialogo);
		dialogo.setVisible(true);
	}

	private void handleConsultaFacturaProveedorSinRemito() {
		facturaProveedor = getFacturaProveedorFacade().getByIdEager(facturaProveedor.getId());
		JDialogCargarFacturaProveedor dialogo = new JDialogCargarFacturaProveedor(owner, facturaProveedor, modoConsulta, new ArrayList<RemitoEntradaProveedor>());
		GuiUtil.centrar(dialogo);
		dialogo.setVisible(true);
	}
	
	private void handleConsultaFacturaProveedorServicio() {
		facturaProveedor = getFacturaProveedorFacade().getByIdEager(facturaProveedor.getId());
		JDialogCargarFacturaServicioProveedor dialogoRS = new JDialogCargarFacturaServicioProveedor(owner, facturaProveedor, modoConsulta);
		GuiUtil.centrar(dialogoRS);
		dialogoRS.setVisible(true);
	}

	private FacturaProveedorFacadeRemote getFacturaProveedorFacade() {
		if(facturaProveedorFacade == null) {
			facturaProveedorFacade = GTLBeanFactory.getInstance().getBean2(FacturaProveedorFacadeRemote.class);
		}
		return facturaProveedorFacade;
	}

}