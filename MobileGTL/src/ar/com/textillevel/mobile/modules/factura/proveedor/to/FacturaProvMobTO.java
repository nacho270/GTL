package ar.com.textillevel.mobile.modules.factura.proveedor.to;

import java.io.Serializable;
import java.util.List;

import ar.com.textillevel.mobile.modules.common.DocumentoRelMobTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;

public class FacturaProvMobTO implements Serializable {

	private static final long serialVersionUID = -6668104132547612310L;

	private String nroFactura;
	private String fecha;
	private CuentaOwnerTO proveedor;
	private List<DocumentoRelMobTO> docsRelacionados;
	private List<ItemDocumentoProvMobTO> items;
	private String subtotal;
	private String subtotalConFactor;
	private String descuento;
	private String impVarios;
	private String percepIVA;
	private String totalFactura;
	private String totalFacturaConFactor;
	private ImpuestoFacturaProvMobTO impuestoTO;
	private String tipoDocumento;

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

	public List<ItemDocumentoProvMobTO> getItems() {
		return items;
	}

	public List<DocumentoRelMobTO> getDocsRelacionados() {
		return docsRelacionados;
	}

	public CuentaOwnerTO getProveedor() {
		return proveedor;
	}

	public void setProveedor(CuentaOwnerTO proveedor) {
		this.proveedor = proveedor;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getSubtotalConFactor() {
		return subtotalConFactor;
	}

	public void setSubtotalConFactor(String subtotalConFactor) {
		this.subtotalConFactor = subtotalConFactor;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getImpVarios() {
		return impVarios;
	}

	public void setImpVarios(String impVarios) {
		this.impVarios = impVarios;
	}

	public String getPercepIVA() {
		return percepIVA;
	}

	public void setPercepIVA(String percepIVA) {
		this.percepIVA = percepIVA;
	}

	public String getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(String totalFactura) {
		this.totalFactura = totalFactura;
	}

	public String getTotalFacturaConFactor() {
		return totalFacturaConFactor;
	}

	public void setTotalFacturaConFactor(String totalFacturaConFactor) {
		this.totalFacturaConFactor = totalFacturaConFactor;
	}

	public ImpuestoFacturaProvMobTO getImpuestoTO() {
		return impuestoTO;
	}

	public void setDocsRelacionados(List<DocumentoRelMobTO> docsRelacionados) {
		this.docsRelacionados = docsRelacionados;
	}

	public void setItems(List<ItemDocumentoProvMobTO> items) {
		this.items = items;
	}

	public void setImpuestoTO(ImpuestoFacturaProvMobTO impuestoTO) {
		this.impuestoTO = impuestoTO;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}