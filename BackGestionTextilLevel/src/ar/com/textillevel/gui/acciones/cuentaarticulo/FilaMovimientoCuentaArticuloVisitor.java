package ar.com.textillevel.gui.acciones.cuentaarticulo;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoDebeCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoHaberCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.visitor.IMovimientoCuentaArticuloVisitor;

public class FilaMovimientoCuentaArticuloVisitor implements IMovimientoCuentaArticuloVisitor {

	private double saldo;
	private final FWJTable tabla;

	public FilaMovimientoCuentaArticuloVisitor(FWJTable tabla) {
		this.tabla = tabla;
	}

	public void visit(MovimientoDebeCuentaArticulo movimientoDebeCuentaArticulo) {
		getTabla().addRow(getFilaMovimientoDebe(movimientoDebeCuentaArticulo, 8));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 1, Color.RED);
		pintarCeldaSaldo();
	}

	private Object[] getFilaMovimientoDebe(MovimientoCuentaArticulo movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = movimiento.getDescripcionResumen();
		row[1] = getDecimalFormat().format(movimiento.getCantidad());
		row[2] = null;
		setSaldo(getSaldo() + movimiento.getCantidad().doubleValue());
		row[3] = Math.round(Double.valueOf(getSaldo()).floatValue())==0?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[4] = movimiento.getUsuarioConfirmacion() != null;
		row[5] = movimiento.getUsuarioConfirmacion() != null?movimiento.getUsuarioConfirmacion().getUsrName():"";
		row[6] = movimiento.getObservaciones();
		row[7] = movimiento;
		return row;
	}

	public double getSaldo() {
		return saldo;
	}

	private void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	private FWJTable getTabla() {
		return tabla;
	}

	private NumberFormat getDecimalFormat() {
		NumberFormat df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		return df;
	}

	public void visit(MovimientoHaberCuentaArticulo movimientoHaberCuentaArticulo) {
		getTabla().addRow(getFilaMovimientoHaber(movimientoHaberCuentaArticulo, 8));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 2, Color.GREEN);
		pintarCeldaSaldo();
	}

	private Object[] getFilaMovimientoHaber(MovimientoCuentaArticulo movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = movimiento.getDescripcionResumen();
		row[1] = null;
		row[2] = getDecimalFormat().format(movimiento.getCantidad());
		setSaldo(getSaldo() + movimiento.getCantidad().doubleValue());
		row[3] = Math.round(Double.valueOf(getSaldo()).floatValue())==0?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[4] = movimiento.getUsuarioConfirmacion() != null;
		row[5] = movimiento.getUsuarioConfirmacion() != null?movimiento.getUsuarioConfirmacion().getUsrName():"";
		row[6] = movimiento.getObservaciones();
		row[7] = movimiento;
		return row;
	}

	private void pintarCeldaSaldo() {
		if (getSaldo() > 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.GREEN.darker());
		} else if (getSaldo() < 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.RED);
		}
	}

}