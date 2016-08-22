package ar.com.textillevel.gui.acciones.visitor.proveedor;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fwcommon.componentes.FWJTable;
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
		Integer idOrdenPago = null;
		getTabla().addRow(getFilaMovimientoHaberProveedor(movimiento,getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.GREEN.darker());
		if (movimiento.getOrdenDePago() != null) {
			idOrdenPago = movimiento.getOrdenDePago().getId();
			mapaColores.put(idOrdenPago, GenericUtils.getRandomColor());
		}
		pintarCeldaSaldo();
		if (getSaldo() > 0 && idOrdenPago != null) {
			InfoSecondPass isp = new InfoSecondPass(idOrdenPago, getTabla().getRowCount() - 1, mapaColores.get(idOrdenPago), new BigDecimal(getSaldo()));
			rowsPagosSaldoAFavor.add(isp);
		}
	}
	
	private Object[] getFilaMovimientoHaberProveedor(MovimientoHaberProveedor movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		String descripcionResumen = movimiento.getDescripcionResumen();
		if(movimiento.getOrdenDePago()!=null && movimiento.getOrdenDePago().getNroReciboProveedor()!=null){
			descripcionResumen +=" (Recibo Nº: " +movimiento.getOrdenDePago().getNroReciboProveedor()+")";
		}
		row[1] = descripcionResumen;
		row[2] =  null;
		row[3] =getDecimalFormat().format(movimiento.getMonto().doubleValue() > 0?movimiento.getMonto().negate():movimiento.getMonto());
		double valorMovimiento = movimiento.getMonto().doubleValue();
		setSaldo(getSaldo() + valorMovimiento);
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[5] = movimiento;
		row[6] = false;
		row[7] = "";
		row[8] = "";
		
		if((movimiento).getOrdenDePago() != null){
			row[6] = movimiento.getOrdenDePago().getEstadoOrden() == EEstadoOrdenDePago.VERIFICADO;
			row[7] = movimiento.getOrdenDePago().getEstadoOrden() == EEstadoOrdenDePago.VERIFICADO ? movimiento.getOrdenDePago().getUsuarioConfirmacion() : "";
			row[8] = movimiento.getOrdenDePago().getUsuarioCreador()!=null?movimiento.getOrdenDePago().getUsuarioCreador():"";
		}else if(movimiento.getNotaCredito() != null){
			Boolean verificada = movimiento.getNotaCredito().getVerificada();
			if(verificada!= null && verificada == true ){
				row[6] = verificada;
				row[7] = verificada ? movimiento.getNotaCredito().getUsuarioConfirmacion() : "";
			}else{
				row[6] = false;
				row[7] = "";
			}
		}
		row[9] = movimiento.getObservaciones();
		return row;
	}
	
	private Object[] getFilaMovimientoDebeProveedor(MovimientoDebeProveedor movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		row[1] = movimiento.getDescripcionResumen();
		row[2] =  getDecimalFormat().format(movimiento.getMonto().negate());
		row[3] = null;
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue()));
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[5] = movimiento;
		row[6] = false;
		row[7] = "";
		row[8] = movimiento.getFacturaProveedor()!= null ? movimiento.getFacturaProveedor().getUsuarioCreador() : "";
		
		if(movimiento.getFacturaProveedor() != null){
			Boolean verificada = movimiento.getFacturaProveedor().getVerificada();
			if(verificada!= null && verificada == true ){
				row[6] = verificada==true;
				row[7] =  verificada ? movimiento.getFacturaProveedor().getUsuarioConfirmacion() : "";
			}else{
				row[6] = false;
				row[7] = "";
			}
		}else{
			Boolean verificada = movimiento.getNotaDebitoProveedor().getVerificada();
			if(verificada!= null && verificada == true ){
				row[6] = verificada;
				row[7] = verificada ? movimiento.getNotaDebitoProveedor().getUsuarioConfirmacion() : "";
			}else{
				row[6] = false;
				row[7] = "";
			}
		}
		row[9] = movimiento.getObservaciones();
		return row;
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
		getTabla().addRow(getFilaMovimientoDebeProveedor(movimiento,getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 2, Color.RED);
		pintarCeldaSaldo();
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