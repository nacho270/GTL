package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class DatosEducacion implements Serializable {

	private static final long serialVersionUID = -866445617640950091L;

	private String profesionOficio;
	private Integer idEstudiosCursados;
	private String idiomasHablados;
	private String idiomasQueLee;
	private String idiomasQueEscribe;
	private String otrosConocimientos;
	private Boolean exhibeTitulos;

	@Column(name="A_PROFESION",nullable=true)
	public String getProfesionOficio() {
		return profesionOficio;
	}

	public void setProfesionOficio(String profesionOficio) {
		this.profesionOficio = profesionOficio;
	}

	@Column(name="A_EST_CURSADOS",nullable=true)
	private Integer getIdEstudiosCursados() {
		return idEstudiosCursados;
	}

	private void setIdEstudiosCursados(Integer idEstudiosCursados) {
		this.idEstudiosCursados = idEstudiosCursados;
	}

	@Column(name="A_IDIOMAS_HABLA",nullable=true)
	public String getIdiomasHablados() {
		return idiomasHablados;
	}

	public void setIdiomasHablados(String idiomasHablados) {
		this.idiomasHablados = idiomasHablados;
	}

	@Column(name="A_IDIOMAS_LEE",nullable=true)
	public String getIdiomasQueLee() {
		return idiomasQueLee;
	}

	public void setIdiomasQueLee(String idiomasQueLee) {
		this.idiomasQueLee = idiomasQueLee;
	}

	@Column(name="A_IDIOMAS_ESCRIBE",nullable=true)
	public String getIdiomasQueEscribe() {
		return idiomasQueEscribe;
	}

	public void setIdiomasQueEscribe(String idiomasQueEscribe) {
		this.idiomasQueEscribe = idiomasQueEscribe;
	}

	@Column(name="A_OTROS_CONOC",nullable=true)
	public String getOtrosConocimientos() {
		return otrosConocimientos;
	}

	public void setOtrosConocimientos(String otrosConocimientos) {
		this.otrosConocimientos = otrosConocimientos;
	}

	@Column(name="A_EXHIBE_TITULOS",nullable=true,columnDefinition="INTEGER UNSIGNED DEFAULT 0")
	public Boolean getExhibeTitulos() {
		return exhibeTitulos;
	}

	public void setExhibeTitulos(Boolean exhibeTitulos) {
		this.exhibeTitulos = exhibeTitulos;
	}
	
	@Transient
	public EEstudiosCursados getEstudiosCursados(){
		return EEstudiosCursados.getById(getIdEstudiosCursados());
	}
	
	public void setEstudiosCursados(EEstudiosCursados estudios){
		if(estudios == null){
			setIdEstudiosCursados(null);
			return;
		}
		setIdEstudiosCursados(estudios.getId());
	}
}
