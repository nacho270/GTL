package ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums.ETipoConceptoReciboSueldo;

@Entity
@Table(name = "T_PERS_CONCEPTO_REC_SUE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "CONC")
public abstract class ConceptoReciboSueldo implements Serializable {

	private static final long serialVersionUID = -5393519706604068282L;

	private Integer id;
	private String nombre;
	private Sindicato sindicato;
	private List<ValorConceptoFecha> valoresPorFecha;

	public ConceptoReciboSueldo() {
		valoresPorFecha = new ArrayList<ValorConceptoFecha>();
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

	@Column(name = "A_NOMBRE", nullable = false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_CONC_REC_SUE_P_ID", nullable = false)
	public List<ValorConceptoFecha> getValoresPorFecha() {
		return valoresPorFecha;
	}

	public void setValoresPorFecha(List<ValorConceptoFecha> valoresPorFecha) {
		this.valoresPorFecha = valoresPorFecha;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "F_SINDICATO_P_ID", nullable = false)
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@Transient
	public abstract ETipoConceptoReciboSueldo getTipo();

	@Transient
	public ValorConceptoFecha getValorConceptoVigente(Date fecha) {
		List<ValorConceptoFecha> valores = new ArrayList<ValorConceptoFecha>(getValoresPorFecha());
		Collections.sort(valores, new Comparator<ValorConceptoFecha>() {

			public int compare(ValorConceptoFecha o1, ValorConceptoFecha o2) {
				return o1.getFechaDesde().compareTo(o2.getFechaDesde());
			}

		});

		for(int i = valores.size() - 1; i >= 0; i--) {
			ValorConceptoFecha valorConceptoFecha = valores.get(i);
			if(!valorConceptoFecha.getFechaDesde().after(fecha)) {
				return valorConceptoFecha;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ConceptoReciboSueldo other = (ConceptoReciboSueldo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	@Transient
	public String toString(){
		return nombre;
	}
	
}
