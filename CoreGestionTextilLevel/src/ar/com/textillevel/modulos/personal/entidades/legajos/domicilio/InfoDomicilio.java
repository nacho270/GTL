package ar.com.textillevel.modulos.personal.entidades.legajos.domicilio;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_PERS_INFO_DOMICILIO")
public class InfoDomicilio implements Serializable {

	private static final long serialVersionUID = 3517509613149736198L;

	private Integer id;
	private Date fecha;
	private Domicilio domicilio;

	public InfoDomicilio() {
		super();
		domicilio = new Domicilio();
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

	@Column(name="A_FECHA_RESIDENCIA",nullable=false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="F_DOMICILIO_P_ID")
	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}
}
