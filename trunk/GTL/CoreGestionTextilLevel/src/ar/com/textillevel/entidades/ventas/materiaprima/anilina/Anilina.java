package ar.com.textillevel.entidades.ventas.materiaprima.anilina;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;

@Entity
@DiscriminatorValue(value = "MPA")
public class Anilina extends MateriaPrima implements Formulable {

	private static final long serialVersionUID = -6990490932050311121L;

	private TipoAnilina tipoAnilina;
	private Integer colorIndex;
	private String hexaDecimalColor;

	@ManyToOne
	@JoinColumn(name="F_TIPO_ANILINA_P_ID")
	public TipoAnilina getTipoAnilina() {
		return tipoAnilina;
	}

	public void setTipoAnilina(TipoAnilina tipoAnilina) {
		this.tipoAnilina = tipoAnilina;
	}

	@Column(name="A_COLOR_INDEX")
	public Integer getColorIndex() {
		return colorIndex;
	}

	public void setColorIndex(Integer colorIndex) {
		this.colorIndex = colorIndex;
	}

	@Column(name="A_HEXA_COLOR")
	public String getHexaDecimalColor() {
		return hexaDecimalColor;
	}
	
	public void setHexaDecimalColor(String hexaDecimalColor) {
		this.hexaDecimalColor = hexaDecimalColor;
	}
	
	@Override
	public void accept(IMateriaPrimaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public ETipoMateriaPrima getTipo() {
		return ETipoMateriaPrima.ANILINA;
	}
}
