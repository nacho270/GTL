package ar.com.textillevel.modulos.odt.entidades.maquinas;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MSH")
public class MaquinaSectorHumedo extends Maquina {

	private static final long serialVersionUID = -4941776920073904102L;

	private Boolean altaTemperatura;
	private Float capacidadCargaMin;
	private Float capacidadCargaMax;
	private Float diamTejidoMax;
	private Float cantLitrosMin;
	private Float cantLitrosMax;
	private Float cantLitrosRegular;
	private Float velocidadMin;
	private Float velocidadMax;
	private Float velocidadProm;
	private Float diamCilindroMax;

	public MaquinaSectorHumedo() {
		super();
	}

	@Column(name="A_ALTA_TEMPERATURA")
	public Boolean getAltaTemperatura() {
		return altaTemperatura;
	}

	public void setAltaTemperatura(Boolean altaTemperatura) {
		this.altaTemperatura = altaTemperatura;
	}

	@Column(name="A_CAP_CARGA_MIN")
	public Float getCapacidadCargaMin() {
		return capacidadCargaMin;
	}

	public void setCapacidadCargaMin(Float capacidadCargaMin) {
		this.capacidadCargaMin = capacidadCargaMin;
	}

	@Column(name="A_CAP_CARGA_MAX")
	public Float getCapacidadCargaMax() {
		return capacidadCargaMax;
	}

	public void setCapacidadCargaMax(Float capacidadCargaMax) {
		this.capacidadCargaMax = capacidadCargaMax;
	}

	@Column(name="A_DIAM_TEJIDO_MAX")
	public Float getDiamTejidoMax() {
		return diamTejidoMax;
	}

	public void setDiamTejidoMax(Float diamTejidoMax) {
		this.diamTejidoMax = diamTejidoMax;
	}

	@Column(name="A_CANT_LITROS_MIN")
	public Float getCantLitrosMin() {
		return cantLitrosMin;
	}

	public void setCantLitrosMin(Float cantLitrosMin) {
		this.cantLitrosMin = cantLitrosMin;
	}

	@Column(name="A_CANT_LITROS_MAX")
	public Float getCantLitrosMax() {
		return cantLitrosMax;
	}

	public void setCantLitrosMax(Float cantLitrosMax) {
		this.cantLitrosMax = cantLitrosMax;
	}

	@Column(name="A_CANT_LITROS_PROM")
	public Float getCantLitrosRegular() {
		return cantLitrosRegular;
	}

	public void setCantLitrosRegular(Float cantLitrosRegular) {
		this.cantLitrosRegular = cantLitrosRegular;
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

	@Column(name="A_DIAM_CILINDRO_MAX")
	public Float getDiamCilindroMax() {
		return diamCilindroMax;
	}

	public void setDiamCilindroMax(Float diamCilindroMax) {
		this.diamCilindroMax = diamCilindroMax;
	}

}