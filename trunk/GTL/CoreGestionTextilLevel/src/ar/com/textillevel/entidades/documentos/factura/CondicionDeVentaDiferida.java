package ar.com.textillevel.entidades.documentos.factura;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CDVD")
public class CondicionDeVentaDiferida extends CondicionDeVenta {

	private static final long serialVersionUID = 4697011026147379787L;

	private Integer diasIniciales;
	private Integer diasFinales;

	public CondicionDeVentaDiferida() {
		diasIniciales = 0;
	}

	@Column(name="A_DIAS_INICIALES", nullable = false, columnDefinition="INTEGER DEFAULT 0")
	public Integer getDiasIniciales() {
		return diasIniciales;
	}

	public void setDiasIniciales(Integer diasIniciales) {
		this.diasIniciales = diasIniciales;
	}

	@Column(name="A_DIAS_FINALES", nullable = false, columnDefinition="INTEGER DEFAULT 0")
	public Integer getDiasFinales() {
		return diasFinales;
	}

	public void setDiasFinales(Integer diasFinales) {
		this.diasFinales = diasFinales;
	}
}
