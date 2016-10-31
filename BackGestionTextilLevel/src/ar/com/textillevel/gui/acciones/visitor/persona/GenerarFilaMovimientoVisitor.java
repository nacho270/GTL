package ar.com.textillevel.gui.acciones.visitor.persona;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.DateUtil;
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
import ar.com.textillevel.gui.util.GenericUtils;

public class GenerarFilaMovimientoVisitor implements IFilaMovimientoVisitor {

	private double saldo;
	private FWJTable tabla;
	private Integer cantCols;
	private Map<Integer, Color> mapaColores;
	private final List<InfoSecondPass> rowsPagosSaldoAFavor;
	private BigDecimal transporte;

	public GenerarFilaMovimientoVisitor(FWJTable tabla, Integer cantCols, BigDecimal transporte) {
		this.tabla = tabla;
		this.cantCols = cantCols;
		this.transporte = transporte;
		this.saldo = transporte.doubleValue();
		this.mapaColores = new HashMap<Integer, Color>();
		this.rowsPagosSaldoAFavor = new ArrayList<InfoSecondPass>();
	}

	public void visit(MovimientoHaber movimiento) {
	
	}

	private void pintarCeldaSaldo() {
		double saldo = Double.valueOf(getSaldo());
		if(GenericUtils.esCero(saldo)){
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.BLACK);
		}else if (saldo > 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.GREEN.darker());
		} else if (saldo < 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.RED);
		}
	}

	public void visit(MovimientoDebe movimiento) {
		
	}

	public void visit(MovimientoInternoCuenta movimiento) {
	
	}

	public void visit(MovimientoHaberProveedor movimiento) {

	}
	
	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public FWJTable getTabla() {
		return tabla;
	}

	public void setTabla(FWJTable tabla) {
		this.tabla = tabla;
	}

	public Integer getCantCols() {
		return cantCols;
	}

	public void setCantCols(Integer cantCols) {
		this.cantCols = cantCols;
	}

	public Map<Integer, Color> getMapaColores() {
		return mapaColores;
	}

	public void setMapaColores(Map<Integer, Color> mapaColores) {
		this.mapaColores = mapaColores;
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

	public List<InfoSecondPass> getRowsPagosSaldoAFavor() {
		return rowsPagosSaldoAFavor;
	}

	
	public BigDecimal getTransporte() {
		return transporte;
	}

	
	public void setTransporte(BigDecimal transporte) {
		this.transporte = transporte;
	}

	public void visit(MovimientoDebeProveedor movimiento) {
		
	}

	public void visit(MovimientoHaberBanco movimiento) {
		
	}
	

	private Object[] getFilaMovimientoHaberPersona(MovimientoHaberPersona movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		String descripcionResumen = movimiento.getDescripcionResumen();
		row[1] = descripcionResumen;
		row[2] =  null;
		row[3] =getDecimalFormat().format(movimiento.getMonto().negate());
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue() ));
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[5] = movimiento;
		row[6] = movimiento.getOrdenDePago().getUsuarioVerificador()!=null;
		row[7] = movimiento.getOrdenDePago().getUsuarioVerificador();
		row[8] = movimiento.getOrdenDePago().getUsuarioCreador();
		row[9] = movimiento.getObservaciones();
		Boolean entregada = movimiento.getOrdenDePago().getEntregado();
		if (entregada == null) {
			row[10] = "NO";
		} else  if (entregada.equals(Boolean.TRUE)) {
			row[10] = DateUtil.dateToString(movimiento.getOrdenDePago().getFechaHoraEntregada(), DateUtil.SHORT_DATE_WITH_HOUR_SECONDS) + " - " + movimiento.getOrdenDePago().getTerminalEntrega(); 
		} else {
			row[10] = "R - " + DateUtil.dateToString(movimiento.getOrdenDePago().getFechaHoraEntregada(), DateUtil.SHORT_DATE_WITH_HOUR_SECONDS) + " - " + movimiento.getOrdenDePago().getTerminalEntrega();
		}
		return row;
	}
	
	private Object[] getFilaMovimientoDebePersona(MovimientoDebePersona movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		String descripcionResumen = movimiento.getDescripcionResumen();
		row[1] = descripcionResumen;
		row[2] =  getDecimalFormat().format(movimiento.getMonto());
		row[3] = null;
		setSaldo(getSaldo() - (movimiento.getMonto().doubleValue() ));
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[5] = movimiento;
		row[6] = movimiento.getFacturaPersona().getUsuarioVerificador()!=null;
		row[7] = movimiento.getFacturaPersona().getUsuarioVerificador();
		row[8] = movimiento.getFacturaPersona().getUsuarioCreador();
		row[9] = movimiento.getObservaciones();
		row[10] = "";
		return row;
	}

	public void visit(MovimientoDebeBanco movimiento) {
		
	}

	public void visit(MovimientoDebePersona movimientoDebePersona) {
		getTabla().addRow(getFilaMovimientoDebePersona(movimientoDebePersona,getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 2, Color.RED);
		pintarCeldaSaldo();
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		getTabla().addRow(getFilaMovimientoHaberPersona(movimientoHaberPersona,getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.GREEN.darker());
		pintarCeldaSaldo();
	}
}
