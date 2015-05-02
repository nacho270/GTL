package ar.com.textillevel.modulos.personal.entidades.configuracion;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "T_PERS_PARAM_GENERALES")
public class ParametrosGeneralesPersonal implements Serializable {

	private static final long serialVersionUID = 9064427701354235245L;

	private Integer id;
	private Integer maximoPeriodoFueraDeLaEmpresa;	//meses
	private Integer toleranciaParaHorasExtra;		//minutos
	private List<DatosAlarmaFinContrato> alarmasFinContrato;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_MAX_PERIODO_FUERA_EMPRESA",nullable=false)
	public Integer getMaximoPeriodoFueraDeLaEmpresa() {
		return maximoPeriodoFueraDeLaEmpresa;
	}

	public void setMaximoPeriodoFueraDeLaEmpresa(Integer maximoPeriodoFueraDeLaEmpresa) {
		this.maximoPeriodoFueraDeLaEmpresa = maximoPeriodoFueraDeLaEmpresa;
	}

	@Column(name="A_TOLERANCIA_HORAS_EXTRA",nullable=false)
	public Integer getToleranciaParaHorasExtra() {
		return toleranciaParaHorasExtra;
	}

	public void setToleranciaParaHorasExtra(Integer toleranciaParaHorasExtra) {
		this.toleranciaParaHorasExtra = toleranciaParaHorasExtra;
	}
	
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_PARAM_GRALES_P_ID",nullable=false)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<DatosAlarmaFinContrato> getAlarmasFinContrato() {
		return alarmasFinContrato;
	}
	
	public void setAlarmasFinContrato(List<DatosAlarmaFinContrato> alarmasFinContrato) {
		this.alarmasFinContrato = alarmasFinContrato;
	}
	
	@Transient
	public Boolean hayAlgunParametroVacio() {
		return toleranciaParaHorasExtra == null || alarmasFinContrato == null || alarmasFinContrato.isEmpty() || maximoPeriodoFueraDeLaEmpresa == null;
	}
}
