package ar.com.textillevel.modulos.personal.entidades.fichadas.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EEstadoDiaFichada;

public class FichadaLegajoTO implements Serializable {

	private static final long serialVersionUID = -878648114683372195L;

	private Date dia;
	private String strDia;
	private List<GrupoHoraEntradaSalidaTO> gruposEntradaSalida;
	private Double horasNormales;
	private Double horasExtrasAl50;
	private Double horasExtrasAl100;
	private EEstadoDiaFichada estadoDiaFichada;
	private List<FichadaLegajo> fichadasComprendidas;
	private Boolean justificada;
	private String causa;
	
	private Integer minutosParaPresentismo;
	private BigDecimal porcentajeDescuentoAusentismo;

	public FichadaLegajoTO() {
		setEstadoDiaFichada(EEstadoDiaFichada.NORMAL);
		fichadasComprendidas = new ArrayList<FichadaLegajo>();
		gruposEntradaSalida = new ArrayList<GrupoHoraEntradaSalidaTO>();
		justificada = false;
		horasNormales = 0d;
		horasExtrasAl100 = 0d;
		horasExtrasAl50 = 0d;
		minutosParaPresentismo = 0;
		porcentajeDescuentoAusentismo = new BigDecimal(0);
	}

	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}

	public Double getHorasNormales() {
		return horasNormales;
	}

	public void setHorasNormales(Double horasNormales) {
		this.horasNormales = horasNormales;
	}

	public Double getHorasExtrasAl50() {
		return horasExtrasAl50;
	}

	public void setHorasExtrasAl50(Double horasExtrasAl50) {
		this.horasExtrasAl50 = horasExtrasAl50;
	}

	public Double getHorasExtrasAl100() {
		return horasExtrasAl100;
	}

	public void setHorasExtrasAl100(Double horasExtrasAl100) {
		this.horasExtrasAl100 = horasExtrasAl100;
	}

	public EEstadoDiaFichada getEstadoDiaFichada() {
		return estadoDiaFichada;
	}

	public void setEstadoDiaFichada(EEstadoDiaFichada estadoDiaFichada) {
		this.estadoDiaFichada = estadoDiaFichada;
	}

	public String getStrDia() {
		return strDia;
	}

	public void setStrDia(String strDia) {
		this.strDia = strDia;
	}

	public List<FichadaLegajo> getFichadasComprendidas() {
		return fichadasComprendidas;
	}

	public void setFichadasComprendidas(List<FichadaLegajo> fichadasComprendidas) {
		this.fichadasComprendidas = fichadasComprendidas;
	}

	public boolean esConsistente() {
		return getGruposEntradaSalida() != null && !getGruposEntradaSalida().isEmpty() && gruposConsistentes();
	}

	private boolean gruposConsistentes() {
		for (GrupoHoraEntradaSalidaTO g : getGruposEntradaSalida()) {
			if (!g.estaCompleto()) {
				return false;
			}
		}
		return true;
	}

	public Boolean getJustificada() {
		return justificada;
	}

	public void setJustificada(Boolean justificada) {
		this.justificada = justificada;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public List<GrupoHoraEntradaSalidaTO> getGruposEntradaSalida() {
		return gruposEntradaSalida;
	}

	public void setGruposEntradaSalida(List<GrupoHoraEntradaSalidaTO> gruposEntradaSalida) {
		this.gruposEntradaSalida = gruposEntradaSalida;
	}

	public int getHoraEntrada() {
		return DateUtil.getHoras(getPrimerGrupo().getHoraEntrada());
	}

	public GrupoHoraEntradaSalidaTO getPrimerGrupo() {
		return getGruposEntradaSalida().get(0);
	}

	public int getHoraSalida() {
		return DateUtil.getHoras(getUltimoGrupo().getHoraSalida());
	}

	public GrupoHoraEntradaSalidaTO getUltimoGrupo() {
		return getGruposEntradaSalida().get(getGruposEntradaSalida().size() - 1);
	}

	public int getMinutosEntrada() {
		return DateUtil.getMinutos(getGruposEntradaSalida().get(0).getHoraEntrada());
	}

	public int getMinutosSalida() {
		return DateUtil.getMinutos(getGruposEntradaSalida().get(getGruposEntradaSalida().size() - 1).getHoraSalida());
	}

	public BigDecimal getPorcentajeDescuentoAusentismo() {
		return porcentajeDescuentoAusentismo;
	}

	public void setPorcentajeDescuentoAusentismo(BigDecimal porcentajeDescuentoAusentismo) {
		this.porcentajeDescuentoAusentismo = porcentajeDescuentoAusentismo;
	}

	
	public Integer getMinutosParaPresentismo() {
		return minutosParaPresentismo;
	}

	
	public void setMinutosParaPresentismo(Integer minutosParaPresentismo) {
		this.minutosParaPresentismo = minutosParaPresentismo;
	}
}
