package ar.com.textillevel.mobile.modules.cheque.to;

import java.io.Serializable;

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
