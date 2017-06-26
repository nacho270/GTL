package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Frame;

import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;
import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebe;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebePersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaber;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberBanco;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberPersona;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoInternoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaFactura;
import ar.com.textillevel.gui.acciones.JDialogCargaRecibo;
import ar.com.textillevel.gui.acciones.JFrameVerMovimientos;
import ar.com.textillevel.util.GTLBeanFactory;

public class ConsultaDocumentoVisitor implements IFilaMovimientoVisitor{

	private final Frame padre;
	
	public ConsultaDocumentoVisitor(JFrameVerMovimientos jFrameVerMovimientos) {
		this.padre = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {
		if(movimiento.getRecibo() != null){
			ReciboFacadeRemote rfr;
			try {
				rfr = GTLBeanFactory.getInstance().getBean(ReciboFacadeRemote.class);
				Recibo recibo = rfr.getByIdEager(movimiento.getRecibo().getId());
				JDialogCargaRecibo dialogCargaRecibo = new JDialogCargaRecibo(padre,recibo , true);
				GuiUtil.centrar(dialogCargaRecibo);
				dialogCargaRecibo.setVisible(true);
			} catch (FWException e) {
				BossError.gestionarError(e);
			}
		}else if(movimiento.getNotaCredito() !=null){
			consultarCorreccion(movimiento.getNotaCredito());
		}
	}

	public void visit(MovimientoDebe movimiento) {
		if(movimiento.getFactura() != null){
			try {
				FacturaFacadeRemote ffr = GTLBeanFactory.getInstance().getBean(FacturaFacadeRemote.class);
				Factura f = ffr.getByIdEager(movimiento.getFactura().getId());
				JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(padre,f,true);
				dialogCargaFactura.setVisible(true);
			} catch (FWException e) {
				BossError.gestionarError(e);
			}
		}else if(movimiento.getNotaDebito() != null){
			consultarCorreccion(movimiento.getNotaDebito());
		} else {
			RemitoSalida rsEager = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class).getByIdConPiezasYProductos(movimiento.getRemitoSalida().getId());
			OperacionSobreRemitoSalidaHandler handler = new OperacionSobreRemitoSalidaHandler(padre, rsEager, true);
			handler.showRemitoEntradaDialog();
		}
	}
	
	private void consultarCorreccion(CorreccionFactura correccion) {
		try {
			CorreccionFacadeRemote cfr = GTLBeanFactory.getInstance().getBean(CorreccionFacadeRemote.class);
			correccion = cfr.getCorreccionById(correccion.getId());
			JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(padre,correccion, true);
			dialogCargaFactura.setVisible(true);
		} catch (FWException e) {
			BossError.gestionarError(e);
		}
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}

	public void visit(MovimientoDebeProveedor movimiento) {
	
	}
	
	public void visit(MovimientoHaberBanco movimiento) {
		
	}

	public void visit(MovimientoDebeBanco movimiento) {
		
	}

	public void visit(MovimientoDebePersona movimientoDebePersona) {
		
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		
	}
}
