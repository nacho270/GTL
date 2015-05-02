package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value="ACCCD")
public class AccionCartaDocumento extends AccionSancion<CartaDocumento> {

	private static final long serialVersionUID = 2309365891602593508L;

	private Integer idEstadoCD;

	@Column(name="A_ID_ESTADO_CD")
	private Integer getIdEstadoCD() {
		return idEstadoCD;
	}

	private void setIdEstadoCD(Integer idEstadoCD) {
		this.idEstadoCD = idEstadoCD;
	}

	@Transient
	public EEStadoCartaDocumento getEstadoCD() {
		if (getIdEstadoCD() == null) {
			return null;
		}
		return EEStadoCartaDocumento.getById(getIdEstadoCD());
	}

	public void setEstadoCD(EEStadoCartaDocumento estadoCD) {
		if (estadoCD == null) {
			this.setIdEstadoCD(null);
		}
		setIdEstadoCD(estadoCD.getId());
	}

	@Override
	@Transient
	public String getDescrResumen() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("Estado de la carta documento: " + getEstadoCD().toString());
		return sb.toString();
	}

	@Override
	public String calculateNombreDocumento() {
		return "CD-" + getId();
	}

}
