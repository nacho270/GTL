package ar.com.textillevel.gui.acciones.visitor.proveedor;

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
import ar.com.textillevel.entidades.enums.EEstadoOrdenDePago;
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosProveedor;

public class HabilitarBotonesCuentaVisitor implements IFilaMovimientoVisitor {

	private JFrameVerMovimientosProveedor frameMovimientos;

	public HabilitarBotonesCuentaVisitor(JFrameVerMovimientosProveedor jFrameVerMovimientos) {
		this.frameMovimientos = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {

	}

	public void visit(MovimientoDebe movimiento) {

	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}
	
	public JFrameVerMovimientosProveedor getFrameMovimientos() {
		return frameMovimientos;
	}

	public void setFrameMovimientos(JFrameVerMovimientosProveedor frameMovimientos) {
		this.frameMovimientos = frameMovimientos;
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		if(movimiento.getFacturaProveedor()!=null){
			if(movimiento.getFacturaProveedor().getVerificada()!=null && 
				movimiento.getFacturaProveedor().getVerificada()){
				frameMovimientos.getBtnConfirmar().setEnabled(false);
				frameMovimientos.getBtnEliminarFactura().setEnabled(false);
			}else{
				frameMovimientos.getBtnConfirmar().setEnabled(true);
				frameMovimientos.getBtnEliminarFactura().setEnabled(true);
			}
			frameMovimientos.getBtnCompletarDatosNotaDebitoCredito().setEnabled(false);
			frameMovimientos.getBtnEditar().setEnabled(true);
		}else{
			if(movimiento.getNotaDebitoProveedor()!=null && 
				movimiento.getNotaDebitoProveedor().getVerificada()!=null && 
				movimiento.getNotaDebitoProveedor().getVerificada().booleanValue()==true){
				frameMovimientos.getBtnConfirmar().setEnabled(false);
				frameMovimientos.getBtnEliminarFactura().setEnabled(false);
			}else{
				frameMovimientos.getBtnConfirmar().setEnabled(true);
				frameMovimientos.getBtnEliminarFactura().setEnabled(true);
			}
			frameMovimientos.getBtnCompletarDatosNotaDebitoCredito().setEnabled(true);
			frameMovimientos.getBtnEditar().setEnabled(false);
		}
		frameMovimientos.getBtnAgregarNroReciboOrdenDePago().setEnabled(false);
		frameMovimientos.getBtnAgregarObservaciones().setEnabled(true);
	}
	
	public void visit(MovimientoHaberProveedor movimiento) {
		if(movimiento.getOrdenDePago()!=null){
			frameMovimientos.getBtnEditar().setEnabled(true);
			if(movimiento.getOrdenDePago().getEstadoOrden() == EEstadoOrdenDePago.PREPARADO){
				frameMovimientos.getBtnConfirmar().setEnabled(true);
				frameMovimientos.getBtnEliminarFactura().setEnabled(true);
			}else{
				frameMovimientos.getBtnConfirmar().setEnabled(false);
				frameMovimientos.getBtnEliminarFactura().setEnabled(false);
			}
			if(movimiento.getOrdenDePago().getNroReciboProveedor()==null){
				frameMovimientos.getBtnAgregarNroReciboOrdenDePago().setEnabled(true);
			}else{
				frameMovimientos.getBtnAgregarNroReciboOrdenDePago().setEnabled(false);
			}
			frameMovimientos.getBtnCompletarDatosNotaDebitoCredito().setEnabled(false);
		}else{
			if(movimiento.getNotaCredito().getVerificada() !=null && movimiento.getNotaCredito().getVerificada()){
				frameMovimientos.getBtnConfirmar().setEnabled(false);
				frameMovimientos.getBtnEliminarFactura().setEnabled(false);
			}else{
				frameMovimientos.getBtnConfirmar().setEnabled(true);
				frameMovimientos.getBtnEliminarFactura().setEnabled(true);
			}
			frameMovimientos.getBtnCompletarDatosNotaDebitoCredito().setEnabled(true);
		}
		frameMovimientos.getBtnAgregarObservaciones().setEnabled(true);
//		frameMovimientos.getBtnEditar().setEnabled(false);
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
