package ar.com.textillevel.gui.acciones.visitor.persona;

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
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.facade.api.remote.OrdenDePagoPersonaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogAgregarFacturaPersona;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDePagoAPersona;
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosPersona;
import ar.com.textillevel.util.GTLBeanFactory;

public class EditarDocumentoVisitor implements IFilaMovimientoVisitor{

	private final JFrameVerMovimientosPersona frameMovimientos;

	public EditarDocumentoVisitor(JFrameVerMovimientosPersona jFrameVerMovimientos) {
		this.frameMovimientos = jFrameVerMovimientos;
	}
	
	public void visit(MovimientoHaber movimiento) {
		
	}

	public void visit(MovimientoDebe movimiento) {
		
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
		new JDialogAgregarFacturaPersona(frameMovimientos, movimientoDebePersona.getFacturaPersona(), false).setVisible(true);
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		OrdenDePagoAPersona ordenDePago = GTLBeanFactory.getInstance().getBean2(OrdenDePagoPersonaFacadeRemote.class).getOrdenByNro(movimientoHaberPersona.getOrdenDePago().getNroOrden());
		new JDialogCargaOrdenDePagoAPersona(frameMovimientos, ordenDePago, false).setVisible(true);
	}
}
