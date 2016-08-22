package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Color;
import java.math.BigDecimal;

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
import ar.com.textillevel.gui.acciones.InfoSecondPass;

public class PintarRecibosSecondPassVisitor implements IFilaMovimientoVisitor {
	
	private static final String CHAR_MARKER_PAGO = "X";
	private final InfoSecondPass isp;
	private final int fila;
	private final CellRenderer cellRenderer;
	
	public PintarRecibosSecondPassVisitor(InfoSecondPass isp, int fila, CellRenderer cellRenderer) {
		this.isp = isp;
		this.fila = fila;
		this.cellRenderer = cellRenderer;
	}

	public void visit(MovimientoHaber movimiento) {
	}

	public void visit(MovimientoDebe movimiento) {
		if(isp.getMonto().compareTo(new BigDecimal(0)) > 0) {
			isp.setMonto(isp.getMonto().subtract(movimiento.getMonto().abs()));
			Color color = isp.getColor();
			String hexaColor = Integer.toHexString(color.getRed())+ "" + Integer.toHexString(color.getGreen())+""+Integer.toHexString(color.getBlue());
			String texto = "<span style='font-size:23; font-weight: bold; color: #" + hexaColor + ";'>" + CHAR_MARKER_PAGO + "</span>";
			cellRenderer.agregarTexto(fila, texto);
		}
	}

	public void visit(MovimientoInternoCuenta movimiento) {
	
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		if(isp.getMonto().compareTo(new BigDecimal(0)) > 0) {
			isp.setMonto(isp.getMonto().subtract(movimiento.getMonto().abs()));
			Color color = isp.getColor();
			String hexaColor = Integer.toHexString(color.getRed())+ "" + Integer.toHexString(color.getGreen())+""+Integer.toHexString(color.getBlue());
			String texto = "<span style='font-size:23; font-weight: bold; color: #" + hexaColor + ";'>" + CHAR_MARKER_PAGO + "</span>";
			cellRenderer.agregarTexto(fila, texto);
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