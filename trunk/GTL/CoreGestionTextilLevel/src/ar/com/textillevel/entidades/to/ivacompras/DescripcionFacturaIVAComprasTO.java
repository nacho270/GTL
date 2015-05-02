package ar.com.textillevel.entidades.to.ivacompras;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DescripcionFacturaIVAComprasTO implements Serializable {

	private static final long serialVersionUID = -5850122275226163854L;

	private Date fecha;
	private String tipoComprobante;
	private String nroComprobante;
	private String razonSocial;
	private String cuit;
	private String netoGravado;
	private String exento;
	private String totalComp;
	private String netoNoGravado;
	private String posIVA;
	private String percIVA;
	private Integer nroComprobanteSort;
	private List<DetalleImpuestoFacturaIVAComprasTO> detalleImpuestoList;

	public DescripcionFacturaIVAComprasTO() {
		this.detalleImpuestoList = new ArrayList<DetalleImpuestoFacturaIVAComprasTO>();
	}

	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(ETipoDocumentoProveedor tipoDocumentoProv) {
		this.tipoComprobante = tipoDocumentoProv.toString();
	}

	public String getNroComprobante() {
		return nroComprobante;
	}
	
	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public String getCuit() {
		return cuit;
	}
	
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	public String getNetoGravado() {
		return netoGravado;
	}
	
	public void setNetoGravado(String netoGravado) {
		this.netoGravado = netoGravado;
	}

	public String getExento() {
		return exento;
	}
	
	public void setExento(String exento) {
		this.exento = exento;
	}
	
	public String getTotalComp() {
		return totalComp;
	}
	
	public void setTotalComp(String totalComp) {
		this.totalComp = totalComp;
	}

	
	public String getPosIVA() {
		return posIVA;
	}
	
	public void setPosIVA(String posIVA) {
		this.posIVA = posIVA;
	}

	public String getNetoNoGravado() {
		return netoNoGravado;
	}

	public void setNetoNoGravado(String netoNoGravado) {
		this.netoNoGravado = netoNoGravado;
	}

	public List<DetalleImpuestoFacturaIVAComprasTO> getDetalleImpuestoList() {
		return detalleImpuestoList;
	}

	public void setDetalleImpuestoList(List<DetalleImpuestoFacturaIVAComprasTO> detalleImpuestoList) {
		this.detalleImpuestoList = detalleImpuestoList;
	}

	public String getPercIVA() {
		return percIVA;
	}

	public void setPercIVA(String percIVA) {
		this.percIVA = percIVA;
	}

	@Override
	public String toString() {
		return "DescripcionFacturaIVAVentasTO [cuit=" + cuit + ", exento=" + exento + ", fecha=" + fecha + ", netoGravado=" + netoGravado + ", noGravado=" + netoNoGravado
				+ ", nroCte=" + nroComprobante + ", posIVA=" + posIVA + ", razonSocial=" + razonSocial + ", tipoCte=" + tipoComprobante + ", percIVA=" + percIVA + ", totalComp=" + totalComp + "]";
	}
	
	public Integer getNroComprobanteSort() {
		return nroComprobanteSort;
	}

	
	public void setNroComprobanteSort(Integer nroComprobanteSort) {
		this.nroComprobanteSort = nroComprobanteSort;
	}

	public DetalleImpuestoFacturaIVAComprasTO getDetalleImpuesto(Integer idImpuesto) {
		for(DetalleImpuestoFacturaIVAComprasTO d : getDetalleImpuestoList()) {
			if(d.getIdImpuesto().equals(idImpuesto)) {
				return d;
			}
		}
		return null;
	}

	
}
