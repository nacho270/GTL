package ar.com.textillevel.entidades.config;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.enums.ETipoFactura;

@Entity
@Table(name = "T_CONF_NROS_FACTURA")
public class ConfiguracionNumeracionFactura implements Serializable {

	private static final long serialVersionUID = 594138307844985237L;

	private Integer id;
	private Integer idTipoFactura;
	private List<NumeracionFactura> numeracion;

	private Integer diasAntesAviso;
	private Integer numerosAntesAviso;

	public ConfiguracionNumeracionFactura() {
		numeracion = new ArrayList<NumeracionFactura>();
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_ID_TIPO_FACTURA", nullable = false)
	private Integer getIdTipoFactura() {
		return idTipoFactura;
	}

	private void setIdTipoFactura(Integer idTipoFactura) {
		this.idTipoFactura = idTipoFactura;
	}

	@Transient
	public ETipoFactura getTipoFactura() {
		return ETipoFactura.getById(getIdTipoFactura());
	}

	public void setTipoFactura(ETipoFactura tipoFactura) {
		setIdTipoFactura(tipoFactura.getId());
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_CONF_NUMERACION_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<NumeracionFactura> getNumeracion() {
		return numeracion;
	}

	public void setNumeracion(List<NumeracionFactura> numeracion) {
		this.numeracion = numeracion;
	}

	@Transient
	public NumeracionFactura getNumeracionActual(Date fecha) {
		for (NumeracionFactura num : getNumeracion()) {
			if (DateUtil.isBetweenConExtremos(num.getFechaDesde(), num.getFechaHasta(), fecha)) {
				return num;
			}
		}
		return null;
	}

	@Column(name="A_DIAS_ANTES_AVISO",nullable=false)
	public Integer getDiasAntesAviso() {
		return diasAntesAviso;
	}

	public void setDiasAntesAviso(Integer diasAntesAviso) {
		this.diasAntesAviso = diasAntesAviso;
	}

	@Column(name="A_NUMEROS_ANTES_AVISO",nullable=false)
	public Integer getNumerosAntesAviso() {
		return numerosAntesAviso;
	}

	public void setNumerosAntesAviso(Integer numerosAntesAviso) {
		this.numerosAntesAviso = numerosAntesAviso;
	}
}
