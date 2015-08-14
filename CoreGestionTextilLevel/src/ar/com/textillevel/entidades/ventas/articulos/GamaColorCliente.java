package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_GAMA_CLIENTE")
public class GamaColorCliente implements Serializable {

	private static final long serialVersionUID = 2467232835529504756L;
	
	private Integer id;
	private GamaColor gamaOriginal;
	private List<Color> colores;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "F_GAMA_CLIENTE_P_ID", nullable = false)
	public GamaColor getGamaOriginal() {
		return gamaOriginal;
	}

	public void setGamaOriginal(GamaColor gamaOriginal) {
		this.gamaOriginal = gamaOriginal;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_GAMA_CLIENTE_COLOR_ASOC",
		joinColumns={
			@JoinColumn(name = "F_GAMA_CLIENTE_P_ID")
		},
		inverseJoinColumns = {
			@JoinColumn(name = "F_COLOR_P_ID")
	})
	public List<Color> getColores() {
		return colores;
	}

	public void setColores(List<Color> colores) {
		this.colores = colores;
	}
}
