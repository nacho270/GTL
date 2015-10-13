package ar.com.textillevel.entidades.documentos.remito.proveedor.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.documentos.remito.proveedor.PiezaRemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;

public class RemitoEntradaProvMobileTO implements Serializable {

	private static final long serialVersionUID = -7991475422966276816L;

	private CuentaOwnerTO owner;
	private String nroRemito;
	private String fecha;
	private List<PiezaRemitoProvMobileTO> piezas;

	public RemitoEntradaProvMobileTO(RemitoEntradaProveedor remito) {
		this.owner = new CuentaOwnerTO(remito.getProveedor());
		this.nroRemito = remito.getNroRemito();
		this.fecha = DateUtil.dateToString(remito.getFechaEmision()); 
		cargarPiezas(remito);
	}

	private void cargarPiezas(RemitoEntradaProveedor remito) {
		this.piezas = new ArrayList<PiezaRemitoProvMobileTO>();
		for(PiezaRemitoEntradaProveedor p : remito.getPiezas()){
			PiezaRemitoProvMobileTO pm = new PiezaRemitoProvMobileTO();
			pm.setCantidadMasUnidad(p.getCantidad() + " " + p.getPrecioMateriaPrima().getMateriaPrima().getUnidad().getDescripcion());
			pm.setDescripcion(p.getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + " - " + p.getPrecioMateriaPrima().getAlias());
			pm.setContenedor(p.getRelContenedorPrecioMatPrima() == null ? null : p.getRelContenedorPrecioMatPrima().getContenedor().toString());
			pm.setCantContenedor(p.getCantContenedor() == null ? "0" : p.getCantContenedor().toString());
			this.piezas.add(pm);
		}
	}

	public CuentaOwnerTO getOwner() {
		return owner;
	}

	public void setOwner(CuentaOwnerTO owner) {
		this.owner = owner;
	}

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public List<PiezaRemitoProvMobileTO> getPiezas() {
		return piezas;
	}

}