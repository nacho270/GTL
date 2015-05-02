package ar.com.textillevel.modulos.odt.entidades.maquinas;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="MSE")
public class MaquinaSectorEstampado extends Maquina {

	private static final long serialVersionUID = 1229055392742588889L;

	private Integer cantColores;
	private Integer cantCampos;
	private Float temperaturaMin;
	private Float temperaturaMax;
	private Float temperaturaProm;
	private Float velocidadMin;
	private Float velocidadMax;
	private Float velocidadProm;

	public MaquinaSectorEstampado() {
		super();
	}

	@Column(name="A_CANT_CAMPOS")
	public Integer getCantCampos() {
		return cantCampos;
	}

	public void setCantCampos(Integer cantCampos) {
		this.cantCampos = cantCampos;
	}

	@Column(name="A_CANT_COLORES")
	public Integer getCantColores() {
		return cantColores;
	}

	public void setCantColores(Integer cantColores) {
		this.cantColores = cantColores;
	}

	@Column(name="A_TEMP_MIN")
	public Float getTemperaturaMin() {
		return temperaturaMin;
	}

	public void setTemperaturaMin(Float temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}

	@Column(name="A_TEMP_MAX")
	public Float getTemperaturaMax() {
		return temperaturaMax;
	}

	public void setTemperaturaMax(Float temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}

	@Column(name="A_TEMP_PROM")
	public Float getTemperaturaProm() {
		return temperaturaProm;
	}

	public void setTemperaturaProm(Float temperaturaProm) {
		this.temperaturaProm = temperaturaProm;
	}

	@Column(name="A_VEL_MIN")
	public Float getVelocidadMin() {
		return velocidadMin;
	}

	public void setVelocidadMin(Float velocidadMin) {
		this.velocidadMin = velocidadMin;
	}

	@Column(name="A_VEL_MAX")
	public Float getVelocidadMax() {
		return velocidadMax;
	}

	public void setVelocidadMax(Float velocidadMax) {
		this.velocidadMax = velocidadMax;
	}

	@Column(name="A_VEL_PROM")
	public Float getVelocidadProm() {
		return velocidadProm;
	}

	public void setVelocidadProm(Float velocidadProm) {
		this.velocidadProm = velocidadProm;
	}

}