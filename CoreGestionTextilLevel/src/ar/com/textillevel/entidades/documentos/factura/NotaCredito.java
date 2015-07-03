package ar.com.textillevel.entidades.documentos.factura;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;

@Entity
@DiscriminatorValue(value = "NC")
public class NotaCredito extends CorreccionFactura {

	private static final long serialVersionUID = 2602232678527908608L;

	private List<Factura> facturasRelacionadas;

	private BigDecimal montoSobrante;

	public NotaCredito() {
		facturasRelacionadas = new ArrayList<Factura>();
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "F_NOTA_CREDITO_P_ID")
	public List<Factura> getFacturasRelacionadas() {
		return facturasRelacionadas;
	}

	public void setFacturasRelacionadas(List<Factura> facturasRelacionadas) {
		this.facturasRelacionadas = facturasRelacionadas;
	}

	@Column(name="A_MONTO_SOBRANTE",nullable=true)
	public BigDecimal getMontoSobrante() {
		return montoSobrante;
	}

	public void setMontoSobrante(BigDecimal montoSobrante) {
		this.montoSobrante = montoSobrante;
	}

	
	@Override
	@Transient
	public ETipoCorreccionFactura getTipo() {
		return ETipoCorreccionFactura.NOTA_CREDITO;
	}
	
	@Override
	@Transient
	public ETipoDocumento getTipoDocumento() {
		return ETipoDocumento.NOTA_CREDITO;
	}

	@Override
	@Transient
	public List<DocumentoContableCliente> getDocsContableRelacionados() {
		return new ArrayList<DocumentoContableCliente>(getFacturasRelacionadas());
	}

	@Override
	@Transient
	public double getTotalIVA() {
		double ivaInsc = 0;
		if (getPorcentajeIVAInscripto() != null) {
			double valIVAInsc = getPorcentajeIVAInscripto().doubleValue() / 100;
			ivaInsc = getMontoSubtotal().doubleValue() * valIVAInsc;
		}
		return ivaInsc;
	}

}
