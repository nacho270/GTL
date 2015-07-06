package ar.com.textillevel.entidades.documentos.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.to.ItemFacturaTO;

public class FacturaMobTO implements Serializable {

	private static final long serialVersionUID = 8911443081882101624L;

	private String nroFactura;
	private String fecha;
	private String tipoDocumento;
	private String tipoFactura;
	private String condicionVenta;
	private CuentaOwnerTO cliente;
	private String porcIvaInsc;
	private String subTotal;
	private String totalIvaInscr;
	private String totalFactura;
	private List<ItemFacturaTO> items;
	private List<DocumentoRelMobTO> remitos;

	public FacturaMobTO(Factura f, ParametrosGenerales parametrosGenerales) {
		this.nroFactura = StringUtil.fillLeftWithZeros(String.valueOf(f.getNroSucursal()), 4) + "-" + StringUtil.fillLeftWithZeros(String.valueOf(f.getNroFactura()), 8);
		this.fecha = DateUtil.dateToString(f.getFechaEmision());
		this.tipoFactura = f.getTipoFactura().getDescripcion();
		this.condicionVenta = f.getCondicionDeVenta().getNombre();
		this.porcIvaInsc = f.getPorcentajeIVAInscripto().toString();
		this.subTotal = f.getMontoSubtotal().toString();
		this.totalIvaInscr = String.valueOf(f.getPorcentajeIVAInscripto().doubleValue() * f.getMontoSubtotal().doubleValue()/ 100);
		this.totalFactura = f.getMontoTotal().toString();
		this.cliente = new CuentaOwnerTO(f.getCliente());
		this.items = getItems(f);
		this.remitos = getRemitos(f);
	}

	private List<ItemFacturaTO> getItems(Factura f) {
		List<ItemFacturaTO> items  = new ArrayList<ItemFacturaTO>();
		for(ItemFactura itemFactura : f.getItems()) {
			ItemFacturaTO ito = new ItemFacturaTO();
			ito.setCantidad(itemFactura.getCantidad().toString());
			ito.setDescripcion(itemFactura.getDescripcion());
			ito.setImporte(itemFactura.getImporte().toString());
			ito.setPrecioUnitario(itemFactura.getPrecioUnitario() == null ? "" : itemFactura.getPrecioUnitario().toString());
			ito.setUnidad(itemFactura.getUnidad().toString());
			items.add(ito);
		}
		return items;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(String tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public String getCondicionVenta() {
		return condicionVenta;
	}

	public void setCondicionVenta(String condicionVenta) {
		this.condicionVenta = condicionVenta;
	}

	public CuentaOwnerTO getCliente() {
		return cliente;
	}

	public void setCliente(CuentaOwnerTO cliente) {
		this.cliente = cliente;
	}

	public String getPorcIvaInsc() {
		return porcIvaInsc;
	}

	public void setPorcIvaInsc(String porcIvaInsc) {
		this.porcIvaInsc = porcIvaInsc;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getTotalIvaInscr() {
		return totalIvaInscr;
	}

	public void setTotalIvaInscr(String totalIvaInscr) {
		this.totalIvaInscr = totalIvaInscr;
	}

	public String getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(String totalFactura) {
		this.totalFactura = totalFactura;
	}

	public List<ItemFacturaTO> getItems() {
		return items;
	}

	public List<DocumentoRelMobTO> getRemitos() {
		return remitos;
	}

	private List<DocumentoRelMobTO> getRemitos(Factura f) {
		List<DocumentoRelMobTO> result = new ArrayList<DocumentoRelMobTO>();
		for(RemitoSalida r : f.getRemitos()) {
			result.add(new DocumentoRelMobTO(r.getId(), ETipoDocumento.REMITO_SALIDA.getId(), r.getNroRemito().toString()));
		}
		return result;
	}

}
