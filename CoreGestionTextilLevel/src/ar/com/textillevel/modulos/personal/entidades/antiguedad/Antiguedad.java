package ar.com.textillevel.modulos.personal.entidades.antiguedad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;

@Entity
@Table(name = "T_PERS_ANTIG")
public class Antiguedad implements Serializable {

	private static final long serialVersionUID = -4896972583897971961L;

	private Integer id;
	private Puesto puesto;
	private List<ValorAntiguedad> valoresAntiguedad;

	public Antiguedad() {
		valoresAntiguedad = new ArrayList<ValorAntiguedad>();
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "F_PUESTO_P_ID", nullable = true)
	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_ANTIGUEDAD_P_ID", nullable = false)
	public List<ValorAntiguedad> getValoresAntiguedad() {
		return valoresAntiguedad;
	}

	public void setValoresAntiguedad(List<ValorAntiguedad> valoresAntiguedad) {
		this.valoresAntiguedad = valoresAntiguedad;
	}

	@Transient
	public BigDecimal getValorPorAntiguedad(Integer antiguedadAnios) {
		List<ValorAntiguedad> valores = new ArrayList<ValorAntiguedad>(getValoresAntiguedad());
		Collections.sort(valoresAntiguedad, new Comparator<ValorAntiguedad>() {

			public int compare(ValorAntiguedad o1, ValorAntiguedad o2) {
				return o1.getAntiguedad().compareTo(o2.getAntiguedad());
			}

		});

		ValorAntiguedad vaPorAntiguedad = null;
		int indexActual = 0;
		for(ValorAntiguedad va : valores) {
			if(va.getAntiguedad().equals(antiguedadAnios)) {
				vaPorAntiguedad = va;
				break;
			}
			if(va.getAntiguedad() > antiguedadAnios.intValue()) {
				if(indexActual > 0) {
					vaPorAntiguedad = valores.get(indexActual - 1);
					break;
				} else {
					break;
				}
			}
			indexActual++;
		}
		return vaPorAntiguedad == null ? null : vaPorAntiguedad.getValorDefault();
	}

}
