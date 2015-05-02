package ar.com.textillevel.modulos.personal.entidades.vacaciones;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.clarin.fwjava.util.DateUtil;

/*
 * Hay que tener cuidado, cuando la antiguedad es menor a 6 meses, no debe entrar en ninguno de los periodos
 */

@Entity
@Table(name="T_PERS_CONF_VACACIONES")
public class ConfiguracionVacaciones implements Serializable {

	private static final long serialVersionUID = 6151069197175071822L;

	private Integer id;
	private Date fechaVigencia;
	private List<PeriodoVacaciones> periodos;
	private Integer mesesMinimosParaEntrar;
	
	public ConfiguracionVacaciones() {
		periodos = new ArrayList<PeriodoVacaciones>();
	}

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_CONF_VAC_P_ID",nullable=false)
	public List<PeriodoVacaciones> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<PeriodoVacaciones> periodos) {
		this.periodos = periodos;
	}

	@Column(name="A_MESES_MINIMOS",nullable=false)
	public Integer getMesesMinimosParaEntrar() {
		return mesesMinimosParaEntrar;
	}
	
	public void setMesesMinimosParaEntrar(Integer mesesMinimosParaEntrar) {
		this.mesesMinimosParaEntrar = mesesMinimosParaEntrar;
	}
	
	@Column(name="A_FECHA_VIGENCIA",nullable=false)
	public Date getFechaVigencia() {
		return fechaVigencia;
	}
	
	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	
	@Override
	public String toString(){
		return  "A PARTIR DE - " + DateUtil.dateToString(fechaVigencia);
	}
}
