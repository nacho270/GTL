package ar.com.textillevel.entidades.documentos.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.to.ItemFacturaTO;

public class CorreccionFacturaMobTO implements Serializable {

	private static final long serialVersionUID = 8911443081882101624L;

	private String nroCorreccion;
	private String fecha;
	private String tipoDocumento;
	private CuentaOwnerTO cliente;
	private String porcIvaInsc;
	private String subTotal;
	private String totalIvaInscr;
	private String totalFactura;
	private List<ItemFacturaTO> items;
	private List<DocumentoRelMobTO> facturas;

	public CorreccionFacturaMobTO(CorreccionFactura cf) {
		this.nroCorreccion = StringUtil.fillLeftWithZeros(String.valueOf(cf.getNroSucursal()), 4) + "-" + StringUtil.fillLeftWithZeros(String.valueOf(cf.getNroFactura()), 8);
		this.fecha = DateUtil.dateToString(cf.getFechaEmision());
		this.porcIvaInsc = cf.getPorcentajeIVAInscripto().toString();
		BigDecimal subtotalCF = cf instanceof NotaCredito ? (cf.getMontoSubtotal().multiply(new BigDecimal(-1))) : cf.getMontoSubtotal();
		BigDecimal totalCF = cf instanceof NotaCredito ? (cf.getMontoTotal().multiply(new BigDecimal(-1))) : cf.getMontoTotal();
		this.subTotal = subtotalCF.toString();
		this.totalIvaInscr = String.valueOf(cf.getPorcentajeIVAInscripto().doubleValue() * subtotalCF.doubleValue()/ 100);
		this.totalFactura = totalCF.toString();
		this.cliente = new CuentaOwnerTO(cf.getCliente());
		this.items = getItems(cf);
		this.tipoDocumento = "" + (cf instanceof NotaCredito ? ETipoDocumento.NOTA_CREDITO.getId() : ETipoDocumento.NOTA_DEBITO.getId()); 
		this.facturas = calcularFacturasRelacionadas(cf);
	}

	private List<DocumentoRelMobTO> calcularFacturasRelacionadas(CorreccionFactura cf) {
		List<DocumentoRelMobTO> result = new ArrayList<DocumentoRelMobTO>();
		if(cf instanceof NotaCredito) {
			List<Factura> facturas = ((NotaCredito)cf).getFacturasRelacionadas();
			for(Factura f : facturas){
				result.add(new DocumentoRelMobTO(f.getId(), ETipoDocumento.FACTURA.getId(), StringUtil.fillLeftWithZeros(String.valueOf(cf.getNroSucursal()), 4) + "-" + StringUtil.fillLeftWithZeros(String.valueOf(cf.getNroFactura()), 8)));
			}
		}
		return result;
	}	

	private List<ItemFacturaTO> getItems(CorreccionFactura cf) {
		List<ItemFacturaTO> items  = new ArrayList<ItemFacturaTO>();
		ItemFacturaTO unicoItem = new ItemFacturaTO();
		unicoItem.setCantidad("1");
		unicoItem.setUnidad("");
		unicoItem.setDescripcion(cf.getDescripcion());
		
		BigDecimal montoSubtotal = cf.getMontoSubtotal();
		if(montoSubtotal!=null) {
			montoSubtotal = montoSubtotal.multiply(new BigDecimal(cf instanceof NotaCredito?-1:1));
			unicoItem.setPrecioUnitario(montoSubtotal.toString());
			unicoItem.setImporte(montoSubtotal != null ? getDecimalFormat().format(montoSubtotal.doubleValue()) : null);
		} else {
			BigDecimal monto = cf.getMontoTotal();
			if (monto!=null){
				monto = monto.multiply(new BigDecimal(cf instanceof NotaCredito?-1:1));
				unicoItem.setPrecioUnitario(monto.toString());
				unicoItem.setImporte(getDecimalFormat().format(monto));
			}
		}
		items.add(unicoItem);
		return items;
	}

	public String getNroCorreccion() {
		return nroCorreccion;
	}

	public void setNroFactura(String nroCorreccion) {
		this.nroCorreccion = nroCorreccion;
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

	public void setItems(List<ItemFacturaTO> items) {
		this.items = items;
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

	public List<DocumentoRelMobTO> getFacturas() {
		return facturas;
	}

}
