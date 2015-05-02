package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_PERS_HORARIO_DIA")
public class HorarioDia implements Serializable {

	private static final long serialVersionUID = 1747908790702697607L;

	private Integer id;
	private RangoDias rangoDias;
	private RangoHorario rangoHorario;
	
	public HorarioDia() {
		rangoDias = new RangoDias();
		rangoHorario = new RangoHorario();
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

	@Embedded
	public RangoDias getRangoDias() {
		return rangoDias;
	}

	@Embedded
	public void setRangoDias(RangoDias rangoDias) {
		this.rangoDias = rangoDias;
	}

	public RangoHorario getRangoHorario() {
		return rangoHorario;
	}

	public void setRangoHorario(RangoHorario rangoHorario) {
		this.rangoHorario = rangoHorario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((rangoDias == null) ? 0 : rangoDias.hashCode());
		result = prime * result + ((rangoHorario == null) ? 0 : rangoHorario.hashCode());
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
		HorarioDia other = (HorarioDia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (rangoDias == null) {
			if (other.rangoDias != null)
				return false;
		} else if (!rangoDias.equals(other.rangoDias))
			return false;
		if (rangoHorario == null) {
			if (other.rangoHorario != null)
				return false;
		} else if (!rangoHorario.equals(other.rangoHorario))
			return false;
		return true;
	}
	
	@Transient
	public Double getTotalDeHoras(){
		return (getRangoDias().getDiaHasta().getNroDia()-getRangoDias().getDiaDesde().getNroDia()+1) * getRangoHorario().getCantidadDeHoras();
	}
	
	@Transient
	public boolean seSolapa(HorarioDia horarioDia){
		if(this.getRangoDias().seSolapaSinExtremos(horarioDia.getRangoDias())){
			return true;
		}
		if(this.getRangoDias().getDiaDesde().getNroDia()==horarioDia.getRangoDias().getDiaDesde().getNroDia() ||
		   this.getRangoDias().getDiaHasta().getNroDia()==horarioDia.getRangoDias().getDiaHasta().getNroDia()	){
			boolean seSolapaSinExtremo = this.getRangoHorario().seSolapaSinExtremos(horarioDia.getRangoHorario());
			if(seSolapaSinExtremo){
				return true;
			}else{
				if(this.getRangoHorario().getHoraDesde().intValue() == horarioDia.getRangoHorario().getHoraDesde().intValue() ||
				   this.getRangoHorario().getHoraHasta().intValue() == horarioDia.getRangoHorario().getHoraHasta().intValue() ){
					return (horarioDia.getRangoHorario().getMinutosDesde() > this.getRangoHorario().getMinutosDesde() && horarioDia.getRangoHorario().getMinutosHasta() < this.getRangoHorario().getMinutosHasta() || 
							horarioDia.getRangoHorario().getMinutosHasta() > this.getRangoHorario().getMinutosDesde() && horarioDia.getRangoHorario().getMinutosHasta() < this.getRangoHorario().getMinutosHasta() ||
							horarioDia.getRangoHorario().getMinutosDesde() > this.getRangoHorario().getMinutosDesde() && horarioDia.getRangoHorario().getMinutosDesde() < this.getRangoHorario().getMinutosHasta());
				}
				return false;
			}
		}
		return false;
	}

	@Transient
	public boolean contieneFecha(Date fecha) {
		return rangoDias.contieneFecha(fecha);
	}
}
