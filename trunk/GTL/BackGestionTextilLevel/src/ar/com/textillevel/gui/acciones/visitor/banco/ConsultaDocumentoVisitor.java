package ar.com.textillevel.gui.acciones.visitor.banco;

import java.awt.Frame;

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
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.facade.api.remote.OrdenDeDepositoFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDeposito;
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosBanco;
import ar.com.textillevel.util.GTLBeanFactory;

public class ConsultaDocumentoVisitor implements IFilaMovimientoVisitor{

	private final Frame padre;

	public ConsultaDocumentoVisitor(JFrameVerMovimientosBanco jFrameVerMovimientos) {
		this.padre = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {
		
	}

	public void visit(MovimientoDebe movimiento) {
		
	}
	
	public void visit(MovimientoHaberProveedor movimiento) {

	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}
	
	public void visit(MovimientoDebeProveedor movimiento) {
		
	}
	
	public void visit(MovimientoHaberBanco movimiento) {
		if(movimiento.getOrdenDeDeposito()!=null){
			OrdenDeDeposito ordenDeDeposito = movimiento.getOrdenDeDeposito();
			ordenDeDeposito = GTLBeanFactory.getInstance().getBean2(OrdenDeDepositoFacadeRemote.class).getOrdenByNro(ordenDeDeposito.getNroOrden());
			new JDialogCargaOrdenDeposito(padre, ordenDeDeposito).setVisible(true);
		}
	}

	public void visit(MovimientoDebeBanco movimiento) {
		
	}

	public void visit(MovimientoDebePersona movimientoDebePersona) {
		
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		
	}
}
