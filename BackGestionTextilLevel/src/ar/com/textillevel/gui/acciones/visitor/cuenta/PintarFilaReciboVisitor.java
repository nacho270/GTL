package ar.com.textillevel.gui.acciones.visitor.cuenta;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

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
import ar.com.textillevel.entidades.documentos.factura.to.InfoCuentaTO;

public class PintarFilaReciboVisitor implements IFilaMovimientoVisitor {

	private FWJTable tabla;
	private Map<Integer, Color> mapaColores;
	private Integer filaActual;
	private CellRenderer renderer;
	private InfoCuentaTO infoCuentaTO;
	private boolean segundaPasada;
	private static final String CHAR_MARKER_PAGO = "X";
	
	public PintarFilaReciboVisitor(FWJTable tabla, Map<Integer, Color> mapaColores, CellRenderer renderer) {
		this.tabla = tabla;
		this.mapaColores = mapaColores;
		this.renderer = renderer;
	}

	public PintarFilaReciboVisitor(FWJTable tabla, Map<Integer, Color> mapaColores, CellRenderer cellRenderer, InfoCuentaTO infoCuentaTO) {
		this(tabla, mapaColores, cellRenderer);
		this.infoCuentaTO = infoCuentaTO;
		this.segundaPasada = true;
	}

	public void visit(final MovimientoHaber movimiento) {
		if (movimiento.getRecibo() != null) {
			Integer idRecibo = movimiento.getRecibo().getId();
			Color colorFilaRecibo = mapaColores.get(idRecibo);
			getTabla().setBackgroundCell(getFilaActual(), 1, colorFilaRecibo);
		}
	}

	public void visit(MovimientoDebe movimiento) {
		if(!segundaPasada) {
			return;
		}
		Set<Integer> idReciboSet = null; 
		if(movimiento.getFactura() != null) {
			idReciboSet = infoCuentaTO.getInfoFacturaReciboSet(movimiento.getFactura().getId());
		}
		if(movimiento.getNotaDebito() != null) {
			idReciboSet = infoCuentaTO.getInfoNotDebReciboSet(movimiento.getNotaDebito().getId());
		}
		String texto = "";
		if(idReciboSet != null) {
			for(Integer idRecibo : idReciboSet) {
				Color color = mapaColores.get(idRecibo);
				if(color != null) {
					String hexaColor = Integer.toHexString(color.getRed())+ "" + Integer.toHexString(color.getGreen())+""+Integer.toHexString(color.getBlue());
					texto += "<span style='font-size:23; font-weight: bold; color: #" + hexaColor + ";'>" + CHAR_MARKER_PAGO + "</span>";
				}
			}
		}
		getRenderer().agregarTexto(getFilaActual(), texto);
	}

	public void visit(MovimientoInternoCuenta movimiento) {
	
	}
	
	public void visit(MovimientoHaberProveedor movimiento) {
		if (movimiento.getOrdenDePago() != null) {
			Integer idOrdenDePago = movimiento.getOrdenDePago().getId();
			Color colorFilaRecibo = mapaColores.get(idOrdenDePago);
			getTabla().setBackgroundCell(getFilaActual(), 1, colorFilaRecibo);
		}
	}

	public FWJTable getTabla() {
		return tabla;
	}

	public void setTabla(FWJTable tabla) {
		this.tabla = tabla;
	}

	public Map<Integer, Color> getMapaColores() {
		return mapaColores;
	}

	public void setMapaColores(Map<Integer, Color> mapaColores) {
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

	public void visit(MovimientoDebeProveedor movimiento) {
		if(!segundaPasada) {
			return;
		}
		Set<Integer> idOrdenDePagoSet = null; 
		if(movimiento.getFacturaProveedor() != null) {
			idOrdenDePagoSet = infoCuentaTO.getInfoFacturaReciboSet(movimiento.getFacturaProveedor().getId());
		}
		if(movimiento.getNotaDebitoProveedor() != null) {
			idOrdenDePagoSet = infoCuentaTO.getInfoNotDebReciboSet(movimiento.getNotaDebitoProveedor().getId());
		}
		String texto = "";
		if(idOrdenDePagoSet != null) {
			for(Integer idOrdenDePago : idOrdenDePagoSet) {
				Color color = mapaColores.get(idOrdenDePago);
				if(color != null) {
					String hexaColor = Integer.toHexString(color.getRed())+ "" + Integer.toHexString(color.getGreen())+""+Integer.toHexString(color.getBlue());
					texto += "<span style='font-size:23; font-weight: bold; color: #" + hexaColor + ";'>" + CHAR_MARKER_PAGO + "</span>";
				}
			}
		}
		getRenderer().agregarTexto(getFilaActual(), texto);
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
