package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Frame;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.util.GuiUtil;
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
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ReciboFacadeRemote;
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
			} catch (CLException e) {
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
			} catch (CLException e) {
				BossError.gestionarError(e);
			}
		}else if(movimiento.getNotaDebito() != null){
			consultarCorreccion(movimiento.getNotaDebito());
		}
	}
	
	private void consultarCorreccion(CorreccionFactura correccion) {
		try {
			CorreccionFacadeRemote cfr = GTLBeanFactory.getInstance().getBean(CorreccionFacadeRemote.class);
			correccion = cfr.getCorreccionByNumero(correccion.getNroFactura());
			JDialogCargaFactura dialogCargaFactura = new JDialogCargaFactura(padre,correccion, true);
			dialogCargaFactura.setVisible(true);
		} catch (CLException e) {
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
