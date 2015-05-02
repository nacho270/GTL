package ar.com.textillevel.entidades.documentos.recibo.to;

import java.io.Serializable;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.util.Utils;

public class ChequeFullTO implements Serializable {

	private static final long serialVersionUID = 263079843030628600L;

	private String cuit;
	private String banco;
	private String importe;
	private String numero;
	private String numeroInterno;
	private String fechaEntrada;
	private String fechaDeposito;
	private String fechaSalida;
	private String owner;
	private String capOInt;
	private String estado;

	public ChequeFullTO(Cheque cheque) {
		this.cuit = cheque.getCuit();
		this.banco = cheque.getBanco().getNombre();
		this.importe = Utils.getDecimalFormat().format(cheque.getImporte().doubleValue());
		this.numero = cheque.getNumero();
		this.numeroInterno = cheque.getNumeracion().toString();
		this.fechaEntrada = DateUtil.dateToString(cheque.getFechaEntrada());
		this.fechaDeposito = DateUtil.dateToString(cheque.getFechaDeposito());
		this.fechaSalida = DateUtil.dateToString(cheque.getFechaSalida());
		this.owner = cheque.getCliente().getNroCliente() + " - " + cheque.getCliente().getRazonSocial();
		this.capOInt = (Character.toLowerCase(cheque.getCapitalOInterior()) == 'c' ? "Capital" : "Interior");
		setEstadoCheque(cheque);
	}

	private void setEstadoCheque(Cheque cheque) {
		if (cheque.getEstadoCheque() == EEstadoCheque.SALIDA_PROVEEDOR || cheque.getEstadoCheque() == EEstadoCheque.SALIDA_CLIENTE || 
			cheque.getEstadoCheque() == EEstadoCheque.SALIDA_PERSONA || cheque.getEstadoCheque() == EEstadoCheque.SALIDA_BANCO || 
			cheque.getEstadoCheque() == EEstadoCheque.RECHAZADO) {
			String nombreSalida = "";
			if (cheque.getProveedorSalida() != null) {
				nombreSalida = cheque.getProveedorSalida().getRazonSocial();
			} else if (cheque.getClienteSalida() != null) {
				nombreSalida = cheque.getClienteSalida().getRazonSocial();
			} else if (cheque.getPersonaSalida() != null) {
				nombreSalida = cheque.getPersonaSalida().getRazonSocial();
			}
			if (cheque.getBancoSalida() != null) {
				nombreSalida = cheque.getBancoSalida().getNombre();
			}
			this.estado = cheque.getEstadoCheque().getDescripcion() + " / " + nombreSalida;
		} else {
			this.estado = cheque.getEstadoCheque().getDescripcion();
		}
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNumeroInterno() {
		return numeroInterno;
	}

	public void setNumeroInterno(String numeroInterno) {
		this.numeroInterno = numeroInterno;
	}

	public String getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(String fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public String getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(String fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public String getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCapOInt() {
		return capOInt;
	}

	public void setCapOInt(String capOInt) {
		this.capOInt = capOInt;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
