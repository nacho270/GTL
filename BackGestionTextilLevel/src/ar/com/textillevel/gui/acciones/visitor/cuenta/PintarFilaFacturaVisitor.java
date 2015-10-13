package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Color;
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
import ar.com.textillevel.entidades.documentos.recibo.Recibo;

public class PintarFilaFacturaVisitor implements IFilaMovimientoVisitor {

	private FWJTable tabla;
	private Map<Recibo, Color> mapaColores;
	private Integer filaActual;
	private CellRenderer renderer;
	private Map<Integer, List<Integer>> mapaRecibosYPagosRecibos;
	
	public PintarFilaFacturaVisitor(FWJTable tabla, Map<Recibo, Color> mapaColores, CellRenderer renderer, Map<Integer, List<Integer>> mapaRecibosYPagosRecibos) {
		this.tabla = tabla;
		this.mapaColores = mapaColores;
		this.renderer = renderer;
		this.mapaRecibosYPagosRecibos = mapaRecibosYPagosRecibos;
	}

	public void visit(MovimientoHaber movimiento) {

	}

	public void visit(MovimientoDebe movimiento) {
		
	}

	public void visit(MovimientoInternoCuenta movimiento) {

	}
	
	public void visit(MovimientoHaberProveedor movimiento) {
		
	}

	public FWJTable getTabla() {
		return tabla;
	}

	public void setTabla(FWJTable tabla) {
		this.tabla = tabla;
	}

	public Map<Recibo, Color> getMapaColores() {
		return mapaColores;
	}

	public void setMapaColores(Map<Recibo, Color> mapaColores) {
		this.mapaColores = mapaColores;
	}

	public Integer getFilaActual() {
		return filaActual;
	}

	public void setFilaActual(Integer filaActual) {
		this.filaActual = filaActual;
	}

	public CellRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(CellRenderer renderer) {
		this.renderer = renderer;
	}

	public Map<Integer, List<Integer>> getMapaRecibosYPagosRecibos() {
		return mapaRecibosYPagosRecibos;
	}

	public void setMapaRecibosYPagosRecibos(
			Map<Integer, List<Integer>> mapaRecibosYPagosRecibos) {
		this.mapaRecibosYPagosRecibos = mapaRecibosYPagosRecibos;
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
