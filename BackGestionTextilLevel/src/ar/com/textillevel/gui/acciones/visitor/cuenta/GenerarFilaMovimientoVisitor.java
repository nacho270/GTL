package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
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
		Integer idRecibo = null;
		getTabla().addRow(getFilaMovimientoHaber(movimiento, getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.GREEN.darker());
		if (movimiento.getRecibo() != null) {
			idRecibo = movimiento.getRecibo().getId();
			mapaColores.put(idRecibo, GenericUtils.getRandomColor());
		}
		pintarCeldaSaldo();
		if (getSaldo() > 0 && idRecibo != null) {
			InfoSecondPass isp = new InfoSecondPass(idRecibo, getTabla().getRowCount() - 1, mapaColores.get(idRecibo), new BigDecimal(getSaldo()));
			rowsPagosSaldoAFavor.add(isp);
		}
	}

	private void pintarCeldaSaldo() {
		double saldo = Double.valueOf(getSaldo());
		if(GenericUtils.esCero(saldo)){
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.BLACK);
		}else if ( saldo > 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.GREEN.darker());
		} else if (saldo < 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.RED);
		}
	}

	public void visit(MovimientoDebe movimiento) {
		getTabla().addRow(getFilaMovimientoDebe(movimiento, getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 2, Color.RED);
		pintarCeldaSaldo();
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		getTabla().addRow(getFilaMovimientoDebe(movimiento, getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 2, Color.RED);
		pintarCeldaSaldo();
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}

	private Object[] getFilaMovimientoDebe(MovimientoCuenta movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		row[1] = movimiento.getDescripcionResumen();
		row[2] = getDecimalFormat().format(movimiento.getMonto());
		row[3] = null;
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue() * -1));
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[5] = movimiento;
		if(((MovimientoDebe) movimiento).getFactura() != null){
			row[6] = ((MovimientoDebe) movimiento).getFactura().getEstadoFactura() == EEstadoFactura.VERIFICADA;
			row[7] = (((MovimientoDebe) movimiento).getFactura().getEstadoFactura() == EEstadoFactura.VERIFICADA) ? ((MovimientoDebe) movimiento).getFactura().getUsuarioConfirmacion() : "";
		}else{
			Boolean verificada = ((MovimientoDebe) movimiento).getNotaDebito().getVerificada();
			if(verificada!=null && verificada == true){
				row[6] = verificada;
				row[7] = verificada ? ((MovimientoDebe) movimiento).getNotaDebito().getUsuarioVerificador() : "";
			}else{
				row[6] = false;
				row[7] = "";
			}
		}
		row[8] = movimiento.getObservaciones();
		return row;
	}

	private Object[] getFilaMovimientoHaber(MovimientoHaber movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		row[1] = movimiento.getDescripcionResumen();
		row[2] = null;
		row[3] = getDecimalFormat().format(movimiento.getMonto());
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue() * -1));
		row[4] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":getDecimalFormat().format( Double.valueOf(getSaldo()).floatValue());
		row[5] = movimiento;
		if((movimiento).getRecibo() != null){
			row[6] = (movimiento).getRecibo().getEstadoRecibo() == EEstadoRecibo.ACEPTADO;
			row[7] = (movimiento).getRecibo().getEstadoRecibo() == EEstadoRecibo.ACEPTADO ? (movimiento).getRecibo().getUsuarioConfirmacion() : "";
		}else{
			Boolean verificada = (movimiento).getNotaCredito().getVerificada();
			if(verificada!= null && verificada == true ){
				row[6] = verificada;
				row[7] = verificada ? (movimiento).getNotaCredito().getUsuarioVerificador() : "";
			}else{
				row[6] = false;
				row[7] = "";
			}
		}
		row[8] = movimiento.getObservaciones();
		return row;
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

	private NumberFormat getDecimalFormat() {
		NumberFormat df =DecimalFormat.getNumberInstance(new Locale("es_AR"));// new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		/*df.setGroupingUsed(true);
		df.setMinimumIntegerDigits(1);		*/
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

	public void visit(MovimientoDebeBanco movimiento) {
		
	}

	public void visit(MovimientoDebePersona movimientoDebePersona) {
		
	}

	public void visit(MovimientoHaberPersona movimientoHaberPersona) {
		
	}
}