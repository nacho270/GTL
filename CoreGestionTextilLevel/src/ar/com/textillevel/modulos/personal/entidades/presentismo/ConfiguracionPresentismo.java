package ar.com.textillevel.modulos.personal.entidades.presentismo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
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

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Entity
@Table(name = "T_PERS_CONF_PRESENTISMO")
public class ConfiguracionPresentismo implements Serializable {

	private static final long serialVersionUID = -2089455592721007919L;

	private Integer id;
	private Date fecha;
	private Sindicato sindicato;
	private BigDecimal porcentajeTotal; //seria el porcentaje si no falta nunca y nunca llega tarde...20%
	private List<DescuentoPresentismo> descuentos;

	public ConfiguracionPresentismo() {
		descuentos = new ArrayList<DescuentoPresentismo>();
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

	@Column(name = "A_FECHA", nullable = false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@ManyToOne
	@JoinColumn(name = "F_SINDICADO_P_ID", nullable = false)
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@Column(name = "A_PORCENTAJE_TOTAL", nullable = false)
	public BigDecimal getPorcentajeTotal() {
		return porcentajeTotal;
	}

	public void setPorcentajeTotal(BigDecimal porcentajeTotal) {
		this.porcentajeTotal = porcentajeTotal;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_CONF_PRESET_P_ID", nullable = false)
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<DescuentoPresentismo> getDescuentos() {
		return descuentos;
	}

	public void setDescuentos(List<DescuentoPresentismo> descuentos) {
		this.descuentos = descuentos;
	}

	@Override
	public String toString() {
		return sindicato.getNombre() + " - " + DateUtil.dateToString(fecha);
	}
}
