package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import java.math.BigDecimal;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;

public class ODTTO implements Serializable {

	private static final long serialVersionUID = -8293434233580197226L;

	private Integer id;
	private String codigo;
	private Integer idRemito;
	private String nombreCliente;
	private Integer nroCliente;
	private String producto;
	private Short ordenEnMaquina;
	private Integer maquinaActual;
	private BigDecimal totalMetros;
	private BigDecimal totalKilos;
	private EAvanceODT avance;
	private ETipoProducto tipoProducto;
	private TipoMaquina tipoMaquina;
	private EnumSituacionMaquina situacionMaquina;
	
	public ODTTO() {

	}

	public ODTTO(Integer id, String codigo, Integer idRemito, Cliente cliente, ProductoArticulo productoArticulo, Short ordenEnMaquina, Maquina maquinaActual, BigDecimal totalKilos, Byte avance, BigDecimal totalMetros) {
		this.id = id;
		this.codigo = codigo;
		this.idRemito = idRemito;
		this.nombreCliente = cliente.getDescripcionResumida();
		this.nroCliente = cliente.getNroCliente();
		this.producto = productoArticulo.toString();
		this.ordenEnMaquina = ordenEnMaquina;
		this.maquinaActual = maquinaActual == null ? null : maquinaActual.getId();
		this.totalMetros = totalMetros;
		this.totalKilos = totalKilos;
		this.avance = avance == null ? null : EAvanceODT.getById(avance.byteValue());
		this.tipoProducto = productoArticulo.getTipo();
		this.tipoMaquina = maquinaActual == null ? null : maquinaActual.getTipoMaquina();
		calcularSituacionMaquina();
	}

	public Integer getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public Integer getIdRemito() {
		return idRemito;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getProducto() {
		return producto;
	}

	public Short getOrdenEnMaquina() {
		return ordenEnMaquina;
	}

	public Integer getMaquinaActual() {
		return maquinaActual;
	}

	public Integer getNroCliente() {
		return nroCliente;
	}

	public BigDecimal getTotalMetros() {
		return totalMetros;
	}

	public BigDecimal getTotalKilos() {
		return totalKilos;
	}

	public EAvanceODT getAvance() {
		return avance;
	}

	public ETipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public TipoMaquina getTipoMaquina() {
		return tipoMaquina;
	}

	public EnumSituacionMaquina getSituacionMaquina() {
		return situacionMaquina;
	}

	public void setSituacionMaquina(EnumSituacionMaquina situacionMaquina) {
		this.situacionMaquina = situacionMaquina;
	}

	private void calcularSituacionMaquina() {
		if(getMaquinaActual() == null) {
			setSituacionMaquina(EnumSituacionMaquina.SIN_MAQUINA_ASIGNADA);
		} else if(getAvance() == EAvanceODT.POR_COMENZAR) {
			setSituacionMaquina(EnumSituacionMaquina.CON_MAQUINA_EN_PROCESO);
		} else if(getAvance() == EAvanceODT.FINALIZADO) {
			setSituacionMaquina(EnumSituacionMaquina.CON_MAQUINA_TERMINADA);
		}
	}

	public static enum EnumSituacionMaquina {

		SIN_MAQUINA_ASIGNADA,
		CON_MAQUINA_EN_PROCESO,
		CON_MAQUINA_TERMINADA,
		CON_MAQUINA_FUERA_DE_SECUENCIA;

	}

}