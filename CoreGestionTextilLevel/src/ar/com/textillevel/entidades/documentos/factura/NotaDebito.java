package ar.com.textillevel.entidades.documentos.factura;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;

@Entity
@DiscriminatorValue(value = "ND")
public class NotaDebito extends CorreccionFactura {

	private static final long serialVersionUID = 2113471876503679142L;

	private BigDecimal montoFaltantePorPagar;
	private Cheque chequeRechazado;
	private Integer idEstadoAnteriorCheque;
	private Boolean isParaRechazarCheque;
	private BigDecimal gastos;

	public NotaDebito(){
		isParaRechazarCheque = false;
	}

	@Column(name = "A_MONTO_FALTANTE", nullable = true)
	public BigDecimal getMontoFaltantePorPagar() {
		return montoFaltantePorPagar;
	}

	public void setMontoFaltantePorPagar(BigDecimal montoFaltantePorPagar) {
		this.montoFaltantePorPagar = montoFaltantePorPagar;
	}

	@Override
	@Transient
	public ETipoCorreccionFactura getTipo() {
		return ETipoCorreccionFactura.NOTA_DEBITO;
	}

	@Column(name = "A_GASTOS", nullable = true)
	public BigDecimal getGastos() {
		return gastos;
	}

	public void setGastos(BigDecimal gastos) {
		this.gastos = gastos;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_CHEQUE_P_ID",nullable=true)
	public Cheque getChequeRechazado() {
		return chequeRechazado;
	}

	public void setChequeRechazado(Cheque chequeRechazado) {
		this.chequeRechazado = chequeRechazado;
	}

	@Transient
	public EEstadoCheque getEstadoAnteriorCheque() {
		return EEstadoCheque.getById(getIdEstadoAnteriorCheque());
	}

	public void setEstadoAnteriorCheque(EEstadoCheque estadoAnteriorCheque) {
		setIdEstadoAnteriorCheque(estadoAnteriorCheque.getId());
	}

	@Column(name="A_ID_EST_ANT_CHEQUE",nullable=true)
	public Integer getIdEstadoAnteriorCheque() {
		return idEstadoAnteriorCheque;
	}

	public void setIdEstadoAnteriorCheque(Integer idEstadoAnteriorCheque) {
		this.idEstadoAnteriorCheque = idEstadoAnteriorCheque;
	}
	
	@Column(name="A_ES_RECHAZA_CHEQUE",nullable=true)
	public Boolean getIsParaRechazarCheque() {
		return isParaRechazarCheque;
	}

	public void setIsParaRechazarCheque(Boolean isParaRechazarCheque) {
		this.isParaRechazarCheque = isParaRechazarCheque;
	}
	
	@Override
	@Transient
	public ETipoDocumento getTipoDocumento() {
		return ETipoDocumento.NOTA_DEBITO;
	}

	@Override
	@Transient
	public List<DocumentoContableCliente> getDocsContableRelacionados() {
		return Collections.emptyList();
	}

	@Override
	@Transient
	public double getTotalIVA() {
		double ivaInsc = 0;
		if (getPorcentajeIVAInscripto() != null) {
			double valIVAInsc = getPorcentajeIVAInscripto().doubleValue() / 100;
			if((getChequeRechazado()!=null || ((getIsParaRechazarCheque()!=null) && (getIsParaRechazarCheque()==true)) ) && getGastos()!=null) {
				ivaInsc = valIVAInsc * getGastos().doubleValue();
			}else{
				ivaInsc =  getMontoSubtotal().doubleValue() * valIVAInsc;
			}
		}
		return ivaInsc;
	}

}