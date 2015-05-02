package ar.com.textillevel.gui.acciones.visitor.banco;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.clarin.fwjava.componentes.CLJTable;
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
	private CLJTable tabla;
	private Integer cantCols;
	private Map<Integer, Color> mapaColores;
	private final List<InfoSecondPass> rowsPagosSaldoAFavor;
	private BigDecimal transporte;

	public GenerarFilaMovimientoVisitor(CLJTable tabla, Integer cantCols, BigDecimal transporte) {
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

	public CLJTable getTabla() {
		return tabla;
	}

	public void setTabla(CLJTable tabla) {
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
		getTabla().addRow(getFilaMovimientoHaberBanco(movimiento,getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.GREEN.darker());
		pintarCeldaSaldo();
	}
	

	private Object[] getFilaMovimientoHaberBanco(MovimientoHaberBanco movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		String descripcionResumen = movimiento.getDescripcionResumen();
		row[1] = descripcionResumen;
		row[2] =  null;
		row[3] =getDecimalFormat().format(movimiento.getMonto().negate());
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue() ));
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue()); 
				//getSaldo()==0?"0.00":getDecimalFormat().format(getSaldo());
		row[5] = movimiento;
		row[6] = false;
		row[7] = "";
		row[8] = "";
		if(movimiento.getOrdenDeDeposito() != null){
//			row[6] = movimiento.getOrdenDeDeposito().getEstadoOrden() == EEstadoOrdenDePago.VERIFICADO;
//			row[7] = movimiento.getOrdenDeDeposito().getEstadoOrden() == EEstadoOrdenDePago.VERIFICADO ? movimiento.getOrdenDePago().getUsuarioConfirmacion() : "";
//			row[8] = movimiento.getOrdenDeDeposito().getUsuarioCreador()!=null?movimiento.getOrdenDePago().getUsuarioCreador():"";
		}else {
			row[6] = false;
			row[7] = "";
			row[8] = "";
		}
		row[9] = movimiento.getObservaciones();
		return row;
	}

	public void visit(MovimientoDebeBanco movimiento) {
		
	}

	public void visit(MovimientoDebePersona movimientoDebePersona) {
		
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		
	}
}
