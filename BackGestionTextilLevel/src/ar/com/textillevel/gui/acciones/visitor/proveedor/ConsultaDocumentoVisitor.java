package ar.com.textillevel.gui.acciones.visitor.proveedor;

import java.awt.Frame;

import main.acciones.compras.OperacionSobreFacturaProveedorHandler;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
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
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogCargaOrdenDePago;
import ar.com.textillevel.gui.acciones.JFrameVerMovimientosProveedor;
import ar.com.textillevel.gui.acciones.proveedor.JDialogCargarNotaDeCreditoProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class ConsultaDocumentoVisitor implements IFilaMovimientoVisitor{

	private final Frame padre;

	public ConsultaDocumentoVisitor(JFrameVerMovimientosProveedor jFrameVerMovimientos) {
		this.padre = jFrameVerMovimientos;
	}

	public void visit(MovimientoHaber movimiento) {
		
	}

	public void visit(MovimientoDebe movimiento) {
		
	}
	
	public void visit(MovimientoHaberProveedor movimiento) {
		if(movimiento.getOrdenDePago()!=null){
			OrdenDePago orden = GTLBeanFactory.getInstance().getBean2(OrdenDePagoFacadeRemote.class).getOrdenDePagoByNroOrdenEager(movimiento.getOrdenDePago().getNroOrden());
			JDialogCargaOrdenDePago dialog = new JDialogCargaOrdenDePago(padre, orden,true);
			dialog.setVisible(true);
		}
		if(movimiento.getNotaCredito() != null) {
			CorreccionFacturaProveedor cfp =  GTLBeanFactory.getInstance().getBean2(CorreccionFacturaProveedorFacadeRemote.class).getCorreccionFacturaByIdEager(movimiento.getNotaCredito().getId());
			JDialogCargarNotaDeCreditoProveedor dialogo = new JDialogCargarNotaDeCreditoProveedor(padre, ((CuentaProveedor)movimiento.getCuenta()).getProveedor(),  cfp, "Nota de Crédito", true);
			dialogo.setVisible(true);
		}
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		
	}
	
	public void visit(MovimientoDebeProveedor movimiento) {
		if(movimiento.getFacturaProveedor()!=null){
			OperacionSobreFacturaProveedorHandler operacionHandler = new OperacionSobreFacturaProveedorHandler(padre, movimiento.getFacturaProveedor(), true); 
			operacionHandler.showFacturaProveedorDialog();
		}
		if(movimiento.getNotaDebitoProveedor() != null) {
			CorreccionFacturaProveedor cfp =  GTLBeanFactory.getInstance().getBean2(CorreccionFacturaProveedorFacadeRemote.class).getCorreccionFacturaByIdEager(movimiento.getNotaDebitoProveedor().getId());
			JDialogCargarNotaDeCreditoProveedor dialogo = new JDialogCargarNotaDeCreditoProveedor(padre, ((CuentaProveedor)movimiento.getCuenta()).getProveedor(),  cfp, "Nota de Débito", true);
			dialogo.setVisible(true);
		}
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
