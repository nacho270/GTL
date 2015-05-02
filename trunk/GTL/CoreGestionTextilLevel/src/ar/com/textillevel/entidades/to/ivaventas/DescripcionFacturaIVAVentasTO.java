package ar.com.textillevel.entidades.to.ivaventas;

import java.io.Serializable;

public class DescripcionFacturaIVAVentasTO implements Serializable{

	private static final long serialVersionUID = 6999836996870426101L;
	
	private String fecha;
	private String tipoCte;
	private String nroCte;
	private String razonSocial;
	private String cuit;
	private String netoGravado;
	private String montoIVA21;
	private String percepcion;
	private String exento;
	private String noGravado;
	private String totalComp;
	private String posIVA;
	private Integer nroComprobanteSort;
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getTipoCte() {
		return tipoCte;
	}
	
	public void setTipoCte(String tipoCte) {
		this.tipoCte = tipoCte;
	}
	
	public String getNroCte() {
		return nroCte;
	}
	
	public void setNroCte(String nroCte) {
		this.nroCte = nroCte;
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
	
	public String getMontoIVA21() {
		return montoIVA21;
	}
	
	public void setMontoIVA21(String montoIVA21) {
		this.montoIVA21 = montoIVA21;
	}
	
	public String getPercepcion() {
		return percepcion;
	}
	
	public void setPercepcion(String percepcion) {
		this.percepcion = percepcion;
	}
	
	public String getExento() {
		return exento;
	}
	
	public void setExento(String exento) {
		this.exento = exento;
	}
	
	public String getNoGravado() {
		return noGravado;
	}
	
	public void setNoGravado(String noGravado) {
		this.noGravado = noGravado;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuit == null) ? 0 : cuit.hashCode());
		result = prime * result + ((exento == null) ? 0 : exento.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((montoIVA21 == null) ? 0 : montoIVA21.hashCode());
		result = prime * result + ((netoGravado == null) ? 0 : netoGravado.hashCode());
		result = prime * result + ((noGravado == null) ? 0 : noGravado.hashCode());
		result = prime * result + ((nroCte == null) ? 0 : nroCte.hashCode());
		result = prime * result + ((percepcion == null) ? 0 : percepcion.hashCode());
		result = prime * result + ((posIVA == null) ? 0 : posIVA.hashCode());
		result = prime * result + ((razonSocial == null) ? 0 : razonSocial.hashCode());
		result = prime * result + ((tipoCte == null) ? 0 : tipoCte.hashCode());
		result = prime * result + ((totalComp == null) ? 0 : totalComp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DescripcionFacturaIVAVentasTO other = (DescripcionFacturaIVAVentasTO) obj;
		if (cuit == null) {
			if (other.cuit != null)
				return false;
		} else if (!cuit.equals(other.cuit))
			return false;
		if (exento == null) {
			if (other.exento != null)
				return false;
		} else if (!exento.equals(other.exento))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (montoIVA21 == null) {
			if (other.montoIVA21 != null)
				return false;
		} else if (!montoIVA21.equals(other.montoIVA21))
			return false;
		if (netoGravado == null) {
			if (other.netoGravado != null)
				return false;
		} else if (!netoGravado.equals(other.netoGravado))
			return false;
		if (noGravado == null) {
			if (other.noGravado != null)
				return false;
		} else if (!noGravado.equals(other.noGravado))
			return false;
		if (nroCte == null) {
			if (other.nroCte != null)
				return false;
		} else if (!nroCte.equals(other.nroCte))
			return false;
		if (percepcion == null) {
			if (other.percepcion != null)
				return false;
		} else if (!percepcion.equals(other.percepcion))
			return false;
		if (posIVA == null) {
			if (other.posIVA != null)
				return false;
		} else if (!posIVA.equals(other.posIVA))
			return false;
		if (razonSocial == null) {
			if (other.razonSocial != null)
				return false;
		} else if (!razonSocial.equals(other.razonSocial))
			return false;
		if (tipoCte == null) {
			if (other.tipoCte != null)
				return false;
		} else if (!tipoCte.equals(other.tipoCte))
			return false;
		if (totalComp == null) {
			if (other.totalComp != null)
				return false;
		} else if (!totalComp.equals(other.totalComp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DescripcionFacturaIVAVentasTO [cuit=" + cuit + ", exento=" + exento + ", fecha=" + fecha + ", montoIVA21=" + montoIVA21 + ", netoGravado=" + netoGravado + ", noGravado=" + noGravado
				+ ", nroCte=" + nroCte + ", percepcion=" + percepcion + ", posIVA=" + posIVA + ", razonSocial=" + razonSocial + ", tipoCte=" + tipoCte + ", totalComp=" + totalComp + "]";
	}

	
	public Integer getNroComprobanteSort() {
		return nroComprobanteSort;
	}

	
	public void setNroComprobanteSort(Integer nroComprobanteSort) {
		this.nroComprobanteSort = nroComprobanteSort;
	}
}
