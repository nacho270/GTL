package ar.com.textillevel.modulos.odt.entidades.maquinas;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MSTC")
public class MaquinaSectorTerminadoCalandra extends MaquinaSectorTerminado {

	private static final long serialVersionUID = 3086613304534039815L;

	private Integer cantRodillos;
	private Integer cantPasadasMin;
	private Integer cantPasadasMax;
	private Float presionTrabajoMax;
	private Float velocidad;
	private Float temperaturaMin;
	private Float temperaturaMax;
	private Float temperaturaProm;

	@Column(name = "A_TEMP_MIN")
	public Float getTemperaturaMin() {
		return temperaturaMin;
	}

	public void setTemperaturaMin(Float temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}

	@Column(name = "A_TEMP_MAX")
	public Float getTemperaturaMax() {
		return temperaturaMax;
	}

	public void setTemperaturaMax(Float temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}

	@Column(name = "A_TEMP_PROM")
	public Float getTemperaturaProm() {
		return temperaturaProm;
	}

	public void setTemperaturaProm(Float temperaturaProm) {
		this.temperaturaProm = temperaturaProm;
	}

	@Column(name = "A_CANT_RODILLOS")
	public Integer getCantRodillos() {
		return cantRodillos;
	}

	public void setCantRodillos(Integer cantRodillos) {
		this.cantRodillos = cantRodillos;
	}

	@Column(name = "A_CANT_PASADAS_MIN")
	public Integer getCantPasadasMin() {
		return cantPasadasMin;
	}

	public void setCantPasadasMin(Integer cantPasadasMin) {
		this.cantPasadasMin = cantPasadasMin;
	}

	@Column(name = "A_CANT_PASADAS_MAX")
	public Integer getCantPasadasMax() {
		return cantPasadasMax;
	}

	public void setCantPasadasMax(Integer cantPasadasMax) {
		this.cantPasadasMax = cantPasadasMax;
	}

	@Column(name = "A_PRESION_TRABAJO_MAX")
	public Float getPresionTrabajoMax() {
		return presionTrabajoMax;
	}

	public void setPresionTrabajoMax(Float presionTrabajoMax) {
		this.presionTrabajoMax = presionTrabajoMax;
	}

	@Column(name="A_VEL_MIN")
	public Float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(Float velocidad) {
		this.velocidad = velocidad;
	}

}