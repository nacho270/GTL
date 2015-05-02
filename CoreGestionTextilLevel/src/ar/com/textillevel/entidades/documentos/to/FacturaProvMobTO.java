package ar.com.textillevel.entidades.documentos.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.util.DocumentoProveedorHelper;
import ar.com.textillevel.util.Utils;

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
	private String tipoDocumento;
	private ImpuestoFacturaProvMobTO impuestoTO;

	public FacturaProvMobTO(FacturaProveedor f) {
		this.nroFactura = f.getNroFactura();
		this.fecha = DateUtil.dateToString(f.getFechaIngreso());
		this.proveedor = new CuentaOwnerTO(f.getProveedor());
		this.docsRelacionados = getRemitos(f);
		this.items = getItems(f);
		Float factorMoneda = DocumentoProveedorHelper.getInstance().getFactorMoneda(f.getItems());
		this.subtotal = Utils.getDecimalFormat().format(f.getMontoSubtotal());
		this.subtotalConFactor = String.valueOf(f.getMontoSubtotal().doubleValue() / factorMoneda);
		this.descuento = f.getDescuento() == null ? null : Utils.getDecimalFormat().format(f.getDescuento().doubleValue());
		this.impVarios = Utils.getDecimalFormat().format(f.getImpVarios().doubleValue());
		this.percepIVA = Utils.getDecimalFormat().format(f.getPercepIVA().doubleValue());
		this.totalFactura = Utils.getDecimalFormat().format(f.getMontoTotal().doubleValue());
		this.totalFacturaConFactor = String.valueOf(f.getMontoTotal().doubleValue() / factorMoneda);
		this.impuestoTO = new ImpuestoFacturaProvMobTO(factorMoneda, DocumentoProveedorHelper.getInstance().calcularMapImpuestos(f.getDescuento(), f.getItems()));
		this.tipoDocumento = ETipoDocumento.FACTURA_PROV.getId() + "";
	}

	public FacturaProvMobTO(CorreccionFacturaProveedor cf) {
		this.nroFactura = cf.getNroCorreccion();
		this.fecha = DateUtil.dateToString(cf.getFechaIngreso());
		this.proveedor = new CuentaOwnerTO(cf.getProveedor());
		this.docsRelacionados = getFacturas(cf);
		this.items = getItemsCF(cf);
		Float factorMoneda = DocumentoProveedorHelper.getInstance().getFactorMonedaCorreccFact(cf.getItemsCorreccion());
		this.impVarios = Utils.getDecimalFormat().format(cf.getImpVarios());
		this.percepIVA = Utils.getDecimalFormat().format(cf.getPercepIVA());
		this.totalFactura = Utils.getDecimalFormat().format(cf.getMontoTotal().abs());
		this.totalFacturaConFactor = Utils.getDecimalFormat().format(cf.getMontoTotal().doubleValue() / factorMoneda);
		this.impuestoTO = new ImpuestoFacturaProvMobTO(factorMoneda, DocumentoProveedorHelper.getInstance().calcularMapImpuestosCorreccFact(cf.getItemsCorreccion()));
		this.tipoDocumento = ((cf instanceof NotaCreditoProveedor) ? ETipoDocumento.NOTA_CREDITO_PROV.getId():ETipoDocumento.NOTA_DEBITO_PROV.getId()) + "";
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

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	private List<DocumentoRelMobTO> getRemitos(FacturaProveedor f) {
		List<DocumentoRelMobTO> result = new ArrayList<DocumentoRelMobTO>();
		for(RemitoEntradaProveedor r : f.getRemitoList()) {
			result.add(new DocumentoRelMobTO(r.getId(), ETipoDocumento.REMITO_ENTRADA_PROV.getId(), r.getNroRemito().toString()));
		}
		return result;
	}

	private List<ItemDocumentoProvMobTO> getItems(FacturaProveedor f) {
		List<ItemDocumentoProvMobTO> items  = new ArrayList<ItemDocumentoProvMobTO>();
		for(ItemFacturaProveedor ifp : f.getItems()) {
			ItemDocumentoProvMobTO ifpmto = new ItemDocumentoProvMobTO();
			PrecioMateriaPrima precioMateriaPrima = ((ItemFacturaMateriaPrima)ifp).getPrecioMateriaPrima();
			precioMateriaPrima.getMateriaPrima().getUnidad().toString();
			ifpmto.setCantidadMasUnidad(Utils.getDecimalFormat().format(ifp.getCantidad().doubleValue()) + " " + precioMateriaPrima.getMateriaPrima().getUnidad().toString());
			ifpmto.setDescripcion(ifp.getDescripcion());
			ifpmto.setFactor(ifp.getFactorConversionMoneda().toString());
			ifpmto.setImporte(Utils.getDecimalFormat().format(ifp.getImporte()));
			ifpmto.setImpuestos(StringUtil.getCadena(ifp.getImpuestos(), ", "));
			ifpmto.setPrecioUnitario(Utils.getDecimalFormat().format(ifp.getPrecioUnitario()));
			ifpmto.setDescuento(ifp.getPorcDescuento() == null ? null : ifp.getPorcDescuento().toString());
			items.add(ifpmto);
		}
		return items;
	}

	private List<DocumentoRelMobTO> getFacturas(CorreccionFacturaProveedor cf) {
		List<DocumentoRelMobTO> result = new ArrayList<DocumentoRelMobTO>();
		for(FacturaProveedor fp : cf.getFacturas()) {
			result.add(new DocumentoRelMobTO(fp.getId(), ETipoDocumento.FACTURA_PROV.getId(), fp.getNroFactura().toString()));
		}
		return result;
	}	

	private List<ItemDocumentoProvMobTO> getItemsCF(CorreccionFacturaProveedor cf) {
		List<ItemDocumentoProvMobTO> items  = new ArrayList<ItemDocumentoProvMobTO>();
		for(ItemCorreccionFacturaProveedor icfp : cf.getItemsCorreccion()) {
			ItemDocumentoProvMobTO ifpmto = new ItemDocumentoProvMobTO();
			PrecioMateriaPrima precioMateriaPrima = null;
			if(icfp instanceof ItemCorreccionMateriaPrima) {
				precioMateriaPrima = ((ItemCorreccionMateriaPrima)icfp).getPrecioMateriaPrima();
				precioMateriaPrima.getMateriaPrima().getUnidad().toString();
			}
			ifpmto.setCantidadMasUnidad(icfp.getCantidad().toString() + " " + (precioMateriaPrima == null ? "" : precioMateriaPrima.getMateriaPrima().getUnidad().toString()));
			ifpmto.setDescripcion(icfp.getDescripcion());
			ifpmto.setFactor(icfp.getFactorConversionMoneda().toString());
			ifpmto.setImporte(Utils.getDecimalFormat().format(icfp.getImporte()));
			ifpmto.setImpuestos(StringUtil.getCadena(icfp.getImpuestos(), ", "));
			ifpmto.setPrecioUnitario(Utils.getDecimalFormat().format(icfp.getPrecioUnitario()));
			ifpmto.setDescuento(icfp.getPorcDescuento() == null ? null : icfp.getPorcDescuento().toString());
			items.add(ifpmto);
		}
		return items;
	}

}