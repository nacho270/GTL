package ar.com.textillevel.modulos.personal.entidades.antiguedad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Entity
@Table(name = "T_PERS_CONF_ANTIG")
public class ConfiguracionAntiguedad implements Serializable {

	private static final long serialVersionUID = -8068092165505776054L;

	private Integer id;
	private Date fechaDesde;
	private Sindicato sindicato;
	private Boolean valoresPorHora;
	private List<Antiguedad> antiguedades;

	public ConfiguracionAntiguedad() {
		antiguedades = new ArrayList<Antiguedad>();
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

	@Column(name = "A_FECHA_DESDE", nullable = false)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@ManyToOne
	@JoinColumn(name = "F_SINDICATO_P_ID", nullable = false)
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "F_CONFIG_ANTIG_P_ID")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<Antiguedad> getAntiguedades() {
		return antiguedades;
	}

	public void setAntiguedades(List<Antiguedad> antiguedades) {
		this.antiguedades = antiguedades;
	}

	@Override
	public String toString() {
		return getSindicato().getNombre() + " - AL " + DateUtil.dateToString(fechaDesde);
	}

	@Column(name="A_VALORES_X_HORA",columnDefinition="INTEGER UNSIGNED DEFAULT 0")
	public Boolean getValoresPorHora() {
		return valoresPorHora;
	}

	public void setValoresPorHora(Boolean valoresPorHora) {
		this.valoresPorHora = valoresPorHora;
	}
	
	@Transient
	public BigDecimal getImportePorPuestoYAnios(Integer antiguedadAnios, Puesto puesto) {
		Antiguedad antiguedadPorPuesto = null;
		Antiguedad antiguedadDefault = null;
		for(Antiguedad ant : getAntiguedades()) {
			if(ant.getPuesto() != null && ant.getPuesto().equals(puesto)) {
				antiguedadPorPuesto = ant;
				break;
			}
			if(ant.getPuesto() == null) {
				antiguedadDefault = ant;
			}
		}
		antiguedadPorPuesto = antiguedadPorPuesto == null ? antiguedadDefault : antiguedadPorPuesto;
		if(antiguedadPorPuesto != null) {
			BigDecimal valorPorAntiguedad = antiguedadPorPuesto.getValorPorAntiguedad(antiguedadAnios);
			if(valorPorAntiguedad != null) {
				if(getValoresPorHora()) {
					//si es un sindicato cuya antiguedad es por valor de hora => multiplico por 8 (hrs de 1 dia) y por 30 (1 mes)  
					return new BigDecimal(valorPorAntiguedad.doubleValue()*30*8);  
				}
			}
			return valorPorAntiguedad;
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
		ConfiguracionAntiguedad other = (ConfiguracionAntiguedad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}