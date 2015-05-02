package ar.com.textillevel.modulos.personal.entidades.configuracion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;

@Entity
@Table(name = "T_PERS_ALARMA_FIN_CONTRATO")
public class DatosAlarmaFinContrato implements Serializable {

	private static final long serialVersionUID = -4069930949234441619L;

	private Integer id;
	private Integer idTipoContrato;
	private Integer diasAntes;

	public DatosAlarmaFinContrato() {
		super();
	}
	
	public DatosAlarmaFinContrato(ETipoContrato tipoContrato) {
		super();
		setTipoContrato(tipoContrato);
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

	@Column(name="A_ID_TIPO_CONTRATO",nullable=false)
	private Integer getIdTipoContrato() {
		return idTipoContrato;
	}

	private void setIdTipoContrato(Integer idTipoContrato) {
		this.idTipoContrato = idTipoContrato;
	}

	@Transient
	public ETipoContrato getTipoContrato(){
		return ETipoContrato.getById(getIdTipoContrato());
	}
	
	public void setTipoContrato(ETipoContrato tipoContrato){
		setIdTipoContrato(tipoContrato.getId());
	}
	
	@Column(name="A_DIAS_AVISO",nullable=false)
	public Integer getDiasAntes() {
		return diasAntes;
	}

	public void setDiasAntes(Integer diasAntes) {
		this.diasAntes = diasAntes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diasAntes == null) ? 0 : diasAntes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idTipoContrato == null) ? 0 : idTipoContrato.hashCode());
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
		DatosAlarmaFinContrato other = (DatosAlarmaFinContrato) obj;
		if (diasAntes == null) {
			if (other.diasAntes != null)
				return false;
		} else if (!diasAntes.equals(other.diasAntes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idTipoContrato == null) {
			if (other.idTipoContrato != null)
				return false;
		} else if (!idTipoContrato.equals(other.idTipoContrato))
			return false;
		return true;
	}
}
