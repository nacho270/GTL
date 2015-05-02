
package ar.com.textillevel.gui.acciones.visitor.cuenta;

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
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;
import ar.com.textillevel.gui.acciones.JFrameVerMovimientos;

public class HabilitarBotonesCuentaVisitor implements IFilaMovimientoVisitor{

	private JFrameVerMovimientos frameMovimientos;
	
	public HabilitarBotonesCuentaVisitor(JFrameVerMovimientos jFrameVerMovimientos) {
		this.frameMovimientos = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {
		if(movimiento.getRecibo()!=null){
			if(movimiento.getRecibo().getEstadoRecibo() == EEstadoRecibo.PENDIENTE){
				getFrameMovimientos().getBtnAnular().setEnabled(true);
				getFrameMovimientos().getBtnConfirmar().setEnabled(true);
				getFrameMovimientos().getBtnEliminarFactura().setEnabled(true);
				getFrameMovimientos().getBtnEditar().setEnabled(true);
			}else if(movimiento.getRecibo().getEstadoRecibo() == EEstadoRecibo.RECHAZADO){
				getFrameMovimientos().getBtnAnular().setEnabled(false);
				getFrameMovimientos().getBtnConfirmar().setEnabled(false);
				getFrameMovimientos().getBtnEliminarFactura().setEnabled(false);
				getFrameMovimientos().getBtnEditar().setEnabled(false);
			}else{
				getFrameMovimientos().getBtnAnular().setEnabled(true);
				getFrameMovimientos().getBtnConfirmar().setEnabled(false);
				getFrameMovimientos().getBtnEliminarFactura().setEnabled(true);
				getFrameMovimientos().getBtnEditar().setEnabled(true);
			}
		}else{
			getFrameMovimientos().getBtnAnular().setEnabled(movimiento.getNotaCredito().getAnulada()==false);
			getFrameMovimientos().getBtnConfirmar().setEnabled(movimiento.getNotaCredito().getAnulada()==false && !(movimiento.getNotaCredito().getVerificada()!=null && movimiento.getNotaCredito().getVerificada() == true));
			getFrameMovimientos().getBtnEliminarFactura().setEnabled(true);
			getFrameMovimientos().getBtnEditar().setEnabled(true);
		}
//		getFrameMovimientos().getBtnEliminarFactura().setEnabled(false);
		getFrameMovimientos().getBtnAgregarObservaciones().setEnabled(true);
	}

	public void visit(MovimientoDebe movimiento) {
		if(movimiento.getFactura()!=null){
			if(movimiento.getFactura().getEstadoFactura() == EEstadoFactura.IMPAGA || movimiento.getFactura().getEstadoFactura() == EEstadoFactura.PAGADA){
				getFrameMovimientos().getBtnAnular().setEnabled(true);
				getFrameMovimientos().getBtnConfirmar().setEnabled(true);
				getFrameMovimientos().getBtnEditar().setEnabled(true);
			}else if(movimiento.getFactura().getEstadoFactura() == EEstadoFactura.ANULADA){
				getFrameMovimientos().getBtnAnular().setEnabled(false);
				getFrameMovimientos().getBtnConfirmar().setEnabled(false);
				getFrameMovimientos().getBtnEditar().setEnabled(false);
			}else{
				getFrameMovimientos().getBtnAnular().setEnabled(true);
				getFrameMovimientos().getBtnConfirmar().setEnabled(false);
				getFrameMovimientos().getBtnEditar().setEnabled(false);
			}
			getFrameMovimientos().getBtnEliminarFactura().setEnabled(true);
		}else{
			getFrameMovimientos().getBtnAnular().setEnabled(movimiento.getNotaDebito().getAnulada()==false);
			getFrameMovimientos().getBtnConfirmar().setEnabled(movimiento.getNotaDebito().getAnulada()==false && !(movimiento.getNotaDebito().getVerificada()!=null && movimiento.getNotaDebito().getVerificada() == true));
			getFrameMovimientos().getBtnEliminarFactura().setEnabled(true);
			getFrameMovimientos().getBtnEditar().setEnabled(true);
		}
		getFrameMovimientos().getBtnAgregarObservaciones().setEnabled(true);
//		getFrameMovimientos().getBtnEliminarRecibo().setEnabled(false);
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		getFrameMovimientos().getBtnAnular().setEnabled(false);
		getFrameMovimientos().getBtnConfirmar().setEnabled(false);
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}
	
	public JFrameVerMovimientos getFrameMovimientos() {
		return frameMovimientos;
	}

	
	public void setFrameMovimientos(JFrameVerMovimientos frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
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
