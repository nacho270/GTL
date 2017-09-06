package ar.com.textillevel.entidades.cheque.to;

import java.io.Serializable;
import java.sql.Timestamp;

import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.EEstadoCheque;

public class OperacionSobreChequeTO implements Serializable, Comparable<OperacionSobreChequeTO> {

	private static final long serialVersionUID = 4074293842773432470L;

	private Timestamp fechaOp;
	private EEstadoCheque pasoAEstado;
	private ETipoDocumento tipoDocumento;
	private Integer idDocumento;
	private String nroDocumento;
	private String usrOperacion;

	public OperacionSobreChequeTO(Timestamp fechaOp, EEstadoCheque pasoAEstado, ETipoDocumento tipoDocumento, Integer idDocumento, String nroDocumento, String usrOperacion) { 
		this.fechaOp = fechaOp;
		this.pasoAEstado = pasoAEstado;
		this.tipoDocumento = tipoDocumento;
		this.idDocumento = idDocumento;
		this.nroDocumento = nroDocumento;
		this.usrOperacion = usrOperacion;
	}

	public Timestamp getFechaOp() {
		return fechaOp;
	}

	public void setFechaOp(Timestamp fechaOp) {
		this.fechaOp = fechaOp;
	}

	public EEstadoCheque getPasoAEstado() {
		return pasoAEstado;
	}

	public void setPasoAEstado(EEstadoCheque pasoAEstado) {
		this.pasoAEstado = pasoAEstado;
	}

	public ETipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(ETipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getUsrOperacion() {
		return usrOperacion;
	}

	public void setUsrOperacion(String usrOperacion) {
		this.usrOperacion = usrOperacion;
	}

	@Override
	public int compareTo(OperacionSobreChequeTO o) {
		return getFechaOp().compareTo(o.getFechaOp());
	}

	public String getDescripcionDoc() {
		return "Se usó en " + getTipoDocumento().toString().replaceAll("_", " ") + " " + getNroDocumento();
	}

}