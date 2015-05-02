package ar.com.textillevel.entidades.documentos.recibo.to;

import java.io.Serializable;
import java.sql.Date;

public class ResumenReciboTO implements Serializable, Comparable<ResumenReciboTO> {

	private static final long serialVersionUID = -9082278834092527634L;
	
	private Integer idRecibo;
	private Integer nroRecibo;
	private Date fecha;
	private double totalEfectivo;
	private double totalIIBB;
	private double totalIVA;
	private double totalCheques;
	private double totalNC;
	private double totalTransfBancarias;
	private double total;

	public ResumenReciboTO() {
	}

	public Integer getIdRecibo() {
		return idRecibo;
	}

	public void setIdRecibo(Integer idRecibo) {
		this.idRecibo = idRecibo;
	}

	public Integer getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Integer nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public double getTotalEfectivo() {
		return totalEfectivo;
	}

	public void setTotalEfectivo(double totalEfectivo) {
		this.totalEfectivo = totalEfectivo;
	}

	public double getTotalIIBB() {
		return totalIIBB;
	}

	public void setTotalIIBB(double totalIIBB) {
		this.totalIIBB = totalIIBB;
	}

	public double getTotalIVA() {
		return totalIVA;
	}

	public void setTotalIVA(double totalIVA) {
		this.totalIVA = totalIVA;
	}

	public double getTotalCheques() {
		return totalCheques;
	}

	public void setTotalCheques(double totalCheques) {
		this.totalCheques = totalCheques;
	}

	public double getTotalNC() {
		return totalNC;
	}

	public void setTotalNC(double totalNC) {
		this.totalNC = totalNC;
	}

	public double getTotalTransfBancarias() {
		return totalTransfBancarias;
	}

	public void setTotalTransfBancarias(double totalTransfBancarias) {
		this.totalTransfBancarias = totalTransfBancarias;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int compareTo(ResumenReciboTO o) {
		int resultCompareTo = getFecha().compareTo(o.getFecha());
		if(resultCompareTo == 0) {
			return getNroRecibo().compareTo(o.getNroRecibo());
		}
		return resultCompareTo;
	}

}