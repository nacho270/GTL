package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.ISancionVisitor;

@Entity
@DiscriminatorValue(value="CD")
public class CartaDocumento extends Sancion {

	private static final long serialVersionUID = -7388615374821238606L;

	private Integer cantDiasSuspencion;
	private Integer cantDiasPlazoJustif;
	private Date fechaIncorporacion;
	private String nroCartaDoc;
	private Timestamp fechaHoraRecepcion;
	private Integer idEstadoCD;
	private Integer idTipoCD;
	private List<Sancion> sancionesAsociadas;

	public CartaDocumento() {
		setEstadoCD(EEStadoCartaDocumento.CREADA);
		this.sancionesAsociadas = new ArrayList<Sancion>();
	}

	@Column(name="A_CANT_DIAS_SUSP")
	public Integer getCantDiasSuspencion() {
		return cantDiasSuspencion;
	}

	public void setCantDiasSuspencion(Integer cantDiasSuspencion) {
		this.cantDiasSuspencion = cantDiasSuspencion;
	}

	@Column(name="A_CANT_DIAS_PLAZO_JUSTIF")
	public Integer getCantDiasPlazoJustif() {
		return cantDiasPlazoJustif;
	}

	public void setCantDiasPlazoJustif(Integer cantDiasPlazoJustif) {
		this.cantDiasPlazoJustif = cantDiasPlazoJustif;
	}

	@Column(name="A_FECHA_INCORP")
	public Date getFechaIncorporacion() {
		return fechaIncorporacion;
	}

	public void setFechaIncorporacion(Date fechaIncorporacion) {
		this.fechaIncorporacion = fechaIncorporacion;
	}

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

	@Column(name="A_ID_TIPO_CD")
	private Integer getIdTipoCD() {
		return idTipoCD;
	}

	private void setIdTipoCD(Integer idTipoCD) {
		this.idTipoCD = idTipoCD;
	}

	@Transient
	public ETipoCartaDocumento getTipoCD() {
		if (getIdTipoCD() == null) {
			return null;
		}
		return ETipoCartaDocumento.getById(getIdTipoCD());
	}

	public void setTipoCD(ETipoCartaDocumento tipoCD) {
		if (tipoCD == null) {
			this.setIdTipoCD(null);
		}
		setIdTipoCD(tipoCD.getId());
	}

	@Column(name="A_FECHA_HORA_RECEPCION")
	public Timestamp getFechaHoraRecepcion() {
		return fechaHoraRecepcion;
	}

	public void setFechaHoraRecepcion(Timestamp fechaHoraRecepcion) {
		this.fechaHoraRecepcion = fechaHoraRecepcion;
	}

	@Column(name="A_NRO_CARTA_DOC")
	public String getNroCartaDoc() {
		return nroCartaDoc;
	}

	public void setNroCartaDoc(String nroCartaDoc) {
		this.nroCartaDoc = nroCartaDoc;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_CARTA_DOC_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<Sancion> getSancionesAsociadas() {
		return sancionesAsociadas;
	}

	public void setSancionesAsociadas(List<Sancion> sancionesAsociadas) {
		this.sancionesAsociadas = sancionesAsociadas;
	}

	@Transient
	public String getResumen() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("Tipo: " + getTipoCD().getDescripcion());
		sb = sb.append("\n").append("Texto: " + getMotivo());
		if(getTipoCD() == ETipoCartaDocumento.SANCION_POR_NO_JUSTIF) {
			if(getFechaIncorporacion() != null) {
				sb = sb.append("\n").append("Fecha de reincorporación: " + DateUtil.dateToString(getFechaIncorporacion()));
			}
			if(getEstadoCD() == EEStadoCartaDocumento.RECIBIDA) {
				if(getFechaHoraRecepcion() != null) {
					sb = sb.append("\n").append("Fecha de Recepción: " + DateUtil.dateToString(getFechaHoraRecepcion(), DateUtil.SHORT_DATE_WITH_HOUR) + "");
				}
			}
		} else {
			if(getEstadoCD().getId() < EEStadoCartaDocumento.RECIBIDA.getId()) {
				if(getCantDiasPlazoJustif() != null) {
					sb = sb.append("\n").append("Plazo para justificar faltas: " + getCantDiasPlazoJustif().toString() + " día(s) después de F. de Recepción.");
				}
			} else if(getEstadoCD() == EEStadoCartaDocumento.RECIBIDA ) {
				if(getFechaHoraRecepcion() != null) {
					if(getCantDiasPlazoJustif() != null) {
						Timestamp deadlineJustif = new Timestamp(getFechaHoraRecepcion().getTime() + DateUtil.ONE_DAY*getCantDiasPlazoJustif());
						sb = sb.append("\n").append("Debe justificar antes del: " + DateUtil.dateToString(deadlineJustif, DateUtil.SHORT_DATE_WITH_HOUR) + "");
					} else {
						sb = sb.append("\n").append("Fecha de Recepción: " + DateUtil.dateToString(getFechaHoraRecepcion(), DateUtil.SHORT_DATE_WITH_HOUR) + "");
					}
				}
			} else {
				
			}
		}
		//Marco como no justificada si corresponde
		if(getEstadoCD() == EEStadoCartaDocumento.RECIBIDA && getCantDiasPlazoJustif() != null && getFechaHoraRecepcion() != null) {
			Timestamp deadlineJustif = new Timestamp(getFechaHoraRecepcion().getTime() + DateUtil.ONE_DAY*getCantDiasPlazoJustif());
			if(deadlineJustif.before(DateUtil.getAhora())) {
				sb = sb.append("\n").append("Estado: " + EEStadoCartaDocumento.NO_JUSTIFICADA.toString());
			} else {
				sb = sb.append("\n").append("Estado: " + EEStadoCartaDocumento.RECIBIDA.toString());
			}
		} else {
			sb = sb.append("\n").append("Estado: " + getEstadoCD().toString());
		}
		if(!StringUtil.isNullOrEmpty(getNroCartaDoc())) {
			sb = sb.append("\n").append("Nro. de Carta Documento: " + getNroCartaDoc());
		}
		if(!StringUtil.isNullOrEmpty(getObservaciones())) {
			sb = sb.append("\n").append("Observaciones: " + getObservaciones());
		}
		if(!getSancionesAsociadas().isEmpty()) {
			sb = sb.append("\n").append("Sanciones relacionadas: " + StringUtil.getCadena(getSancionesAsociadas(), " - "));
		}
		return sb.toString();
	}

	@Override
	@Transient
	public ETipoSancion getTipoSancion() {
		return ETipoSancion.CARTA_DOCUMENTO;
	}

	@Override
	public void accept(ISancionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Carta Documento " + DateUtil.dateToString(getFechaSancion()) + " - Tipo: " + getTipoCD().toString();
	}

}