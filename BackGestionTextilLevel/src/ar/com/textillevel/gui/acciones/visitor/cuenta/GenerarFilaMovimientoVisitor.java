package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
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
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;
import ar.com.textillevel.gui.acciones.InfoSecondPass;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.ODTCodigoHelper;

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
		Integer idRecibo = null;
		getTabla().addRow(getFilaMovimientoHaber(movimiento, getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 4, Color.GREEN.darker());
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
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 5, Color.BLACK);
		}else if ( saldo > 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 5, Color.GREEN.darker());
		} else if (saldo < 0) {
			getTabla().setForegroundCell(getTabla().getRowCount() - 1, 5, Color.RED);
		}
	}

	public void visit(MovimientoDebe movimiento) {
		getTabla().addRow(getFilaMovimientoDebe(movimiento, getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.RED);
		pintarCeldaSaldo();
	}

	public void visit(MovimientoInternoCuenta movimiento) {
		getTabla().addRow(getFilaMovimientoDebe(movimiento, getCantCols()));
		getTabla().setForegroundCell(getTabla().getRowCount() - 1, 3, Color.RED);
		pintarCeldaSaldo();
	}

	public void visit(MovimientoHaberProveedor movimiento) {
		
	}

	private Object[] getFilaMovimientoDebe(MovimientoCuenta movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		String descripcion = movimiento.getDescripcionResumen();
		if (GenericUtils.isSistemaTest()) {
			int indexOfFC = descripcion.indexOf(" - FC ");
			if(indexOfFC != -1) {
				descripcion = descripcion.substring(0, indexOfFC);
			}
		}
		if(((MovimientoDebe) movimiento).getFactura() != null){
			Set<String> odts = new HashSet<String>();
			for(RemitoSalida rs : ((MovimientoDebe) movimiento).getFactura().getRemitos()) {
				for (OrdenDeTrabajo odt : rs.getOdts()) {
					odts.add(ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
				}
			}
			row[1] = StringUtil.getCadena(odts, " - ");
		} else if (((MovimientoDebe) movimiento).getRemitoSalida() != null) {
			Set<String> odts = new HashSet<String>();
			for (OrdenDeTrabajo odt : ((MovimientoDebe) movimiento).getRemitoSalida().getOdts()) {
				odts.add(ODTCodigoHelper.getInstance().formatCodigo(odt.getCodigo()));
			}
			row[1] = StringUtil.getCadena(odts, " - ");
		} else {
			row[1] = "";
		}
		row[2] = descripcion;
		row[3] = GenericUtils.getDecimalFormatTablaMovimientos().format(movimiento.getMonto());
		row[4] = null;
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue() * -1));
		row[5] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":GenericUtils.getDecimalFormatTablaMovimientos().format( Double.valueOf(getSaldo()).floatValue());
		row[6] = movimiento;
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		if(((MovimientoDebe) movimiento).getFactura() != null){
			row[7] = ((MovimientoDebe) movimiento).getFactura().getEstadoFactura() == EEstadoFactura.VERIFICADA;
			row[8] = (((MovimientoDebe) movimiento).getFactura().getEstadoFactura() == EEstadoFactura.VERIFICADA) ? ((MovimientoDebe) movimiento).getFactura().getUsuarioConfirmacion() : "";
			Factura f = ((MovimientoDebe) movimiento).getFactura();
			for (RemitoSalida r : f.getRemitos()) {
				sb.append("<div style=\"padding:2px 0px;\">");
				Boolean entregado = r.getEntregado();
				sb.append(r.getNroRemito() + " - ");
				if (entregado == null) {
					sb.append("NO");
				} else if (entregado.equals(Boolean.TRUE)) {
					sb.append(DateUtil.dateToString(r.getFechaHoraEntregado(), DateUtil.SHORT_DATE_WITH_HOUR_SECONDS) + " - " + r.getTerminalEntrega());
				} else {
					sb.append("R - " + DateUtil.dateToString(r.getFechaHoraEntregado(), DateUtil.SHORT_DATE_WITH_HOUR_SECONDS) + " - " + r.getTerminalEntrega());
				}
				sb.append("</div>");
			}
//			for (int i = 0; i<3; i++) {
//				sb.append("<div style=\"padding:2px 0px;\">" +i + " - " + "SI - 08/09/2016 21:30:39 - Terminal local</div>");
//			}
		}else if (((MovimientoDebe) movimiento).getNotaDebito() != null){
			Boolean verificada = ((MovimientoDebe) movimiento).getNotaDebito().getVerificada();
			if(verificada!=null && verificada == true){
				row[7] = verificada;
				row[8] = verificada ? ((MovimientoDebe) movimiento).getNotaDebito().getUsuarioVerificador() : "";
			}else{
				row[7] = false;
				row[8] = "";
			}
			sb.append("<div style=\"padding:2px 0px;\">&nbsp;</div>");
		} else {
			row[7] = false;
			row[8] = "";
		}
		row[9] = movimiento.getObservaciones();
		sb.append("</html>");
		row[10] = sb.toString();
		return row;
	}

	private Object[] getFilaMovimientoHaber(MovimientoHaber movimiento, Integer size) {
		Object[] row = new Object[size];
		row[0] = "";
		row[1] = "";
		row[2] = movimiento.getDescripcionResumen();
		row[3] = null;
		row[4] = GenericUtils.getDecimalFormatTablaMovimientos().format(movimiento.getMonto());
		setSaldo(getSaldo() + (movimiento.getMonto().doubleValue() * -1));
		row[5] = GenericUtils.esCero(Double.valueOf(getSaldo()))?"0.00":GenericUtils.getDecimalFormatTablaMovimientos().format( Double.valueOf(getSaldo()).floatValue());
		row[6] = movimiento;
		if((movimiento).getRecibo() != null){
			row[7] = (movimiento).getRecibo().getEstadoRecibo() == EEstadoRecibo.ACEPTADO;
			row[8] = (movimiento).getRecibo().getEstadoRecibo() == EEstadoRecibo.ACEPTADO ? (movimiento).getRecibo().getUsuarioConfirmacion() : "";
		}else{
			Boolean verificada = (movimiento).getNotaCredito().getVerificada();
			if(verificada!= null && verificada == true ){
				row[7] = verificada;
				row[8] = verificada ? (movimiento).getNotaCredito().getUsuarioVerificador() : "";
			}else{
				row[7] = false;
				row[8] = "";
			}
		}
		row[9] = movimiento.getObservaciones();
		row[10] = "<html><div style=\"padding:2px 0px;\">&nbsp;</div></html>";
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