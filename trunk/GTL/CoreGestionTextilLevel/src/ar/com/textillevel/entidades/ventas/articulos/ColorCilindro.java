package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_COLOR_CILINDRO")
public class ColorCilindro implements Serializable {

	private static final long serialVersionUID = -2620030588685716590L;

	private Integer id;
	private Integer nroCilindro; 
	private Color color; //Si es null se trata del color de fondo
	private Float metrosPorColor;
	private Float kilosPorColor;
	private Integer xCoordDibujo;
	private Integer yCoordDibujo;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NRO_CILINDRO", nullable=true)
	public Integer getNroCilindro() {
		return nroCilindro;
	}

	public void setNroCilindro(Integer nroCilindro) {
		this.nroCilindro = nroCilindro;
	}

	@ManyToOne
	@JoinColumn(name="F_COLOR_P_ID")
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Column(name="A_METROS_POR_COLOR")
	public Float getMetrosPorColor() {
		return metrosPorColor;
	}

	public void setMetrosPorColor(Float metrosPorColor) {
		this.metrosPorColor = metrosPorColor;
	}

	@Column(name="A_KILOS_POR_COLOR")
	public Float getKilosPorColor() {
		return kilosPorColor;
	}

	public void setKilosPorColor(Float kilosPorColor) {
		this.kilosPorColor = kilosPorColor;
	}

	@Column(name="A_X_COORD_DIBUJO")
	public Integer getxCoordDibujo() {
		return xCoordDibujo;
	}

	public void setxCoordDibujo(Integer xCoordDibujo) {
		this.xCoordDibujo = xCoordDibujo;
	}

	@Column(name="A_Y_COORD_DIBUJO")
	public Integer getyCoordDibujo() {
		return yCoordDibujo;
	}

	public void setyCoordDibujo(Integer yCoordDibujo) {
		this.yCoordDibujo = yCoordDibujo;
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
		ColorCilindro other = (ColorCilindro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(getColor() == null) {
			return "CILINDRO N°" + getNroCilindro() +  " -> [FONDO]"  ;
		} else {
			return "CILINDRO N°" + getNroCilindro() +  " -> " + getColor().getNombre();
		}
	}


}