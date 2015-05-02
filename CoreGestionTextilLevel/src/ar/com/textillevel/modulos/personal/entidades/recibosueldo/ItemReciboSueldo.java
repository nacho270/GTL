package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.ETipoItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

@Entity
@Table(name = "T_PERS_ITEM_RECIBO_SUELDO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class ItemReciboSueldo implements Serializable {

	private static final long serialVersionUID = 4524100627713647634L;

	private Integer id;
	private BigDecimal monto;
	private BigDecimal porcentaje;
	private String codigo;
	private String descripcion;
	private Double unidades;
	private Integer idTipoItemReciboSueldo;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_MONTO", nullable=false)
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "A_CODIGO", nullable=true)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "A_UNIDADES", nullable=true)
	public Double getUnidades() {
		return unidades;
	}

	public void setUnidades(Double unidades) {
		this.unidades = unidades;
	}

	@Column(name = "A_DESCRIPCION", nullable=false)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "A_PORCENTAJE", nullable=true)
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	@Column(name = "A_ID_TIPO_ITEM", nullable=false)
	private Integer getIdTipoItemReciboSueldo() {
		return idTipoItemReciboSueldo;
	}

	private void setIdTipoItemReciboSueldo(Integer idTipoItemReciboSueldo) {
		this.idTipoItemReciboSueldo = idTipoItemReciboSueldo;
	}

	@Transient
	public ETipoItemReciboSueldo getTipoItemReciboSueldo(){
		if(getIdTipoItemReciboSueldo() == null){
			return null;
		}
		return ETipoItemReciboSueldo.getById(getIdTipoItemReciboSueldo());
	}

	public void setTipoItemReciboSueldo(ETipoItemReciboSueldo tipoItemRS){
		if(tipoItemRS == null){
			setIdTipoItemReciboSueldo(null);
		}
		setIdTipoItemReciboSueldo(tipoItemRS.getId());
	}
	
	public abstract void aceptarVisitor(IItemReciboSueldoVisitor visitor);

}