package ar.com.textillevel.modulos.personal.entidades.calenlaboral;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ar.clarin.fwjava.entidades.Dia;

@Entity
@Table(name = "T_PERS_TOTAL_HORAS_PAGO_DIA")
public class TotalHorasPagoDia implements Serializable {

	private static final long serialVersionUID = 3528069020326080751L;

	private Integer id;
	private boolean discriminaEnRS; // si se discrimina en el recibo de sueldo
	private Dia dia;
	private Integer totalHoras;
	
	public TotalHorasPagoDia() {
		setTotalHoras(0);
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

	@Column(name="A_DISCRIMINA_EN_RS",nullable=false)
	public boolean isDiscriminaEnRS() {
		return discriminaEnRS;
	}

	public void setDiscriminaEnRS(boolean discriminaEnRS) {
		this.discriminaEnRS = discriminaEnRS;
	}

	@ManyToOne
	@JoinColumn(name="F_DIA_P_ID",nullable=true)
	public Dia getDia() {
		return dia;
	}

	public void setDia(Dia dia) {
		this.dia = dia;
	}

	@Column(name="A_TOTAL_HORAS",nullable=false)
	public Integer getTotalHoras() {
		return totalHoras;
	}

	public void setTotalHoras(Integer totalHoras) {
		this.totalHoras = totalHoras;
	}

}