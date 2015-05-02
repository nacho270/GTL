package ar.com.textillevel.entidades.to.remitosalida;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class PiezaRemitoSalidaTO implements Serializable {

	private static final long serialVersionUID = 8139167227217784587L;

	private PiezaRemito piezaRemitoEntrada;
	private OrdenDeTrabajo odt;
	private Integer nroPieza;
	private String observaciones;
	private Map<PrecioMateriaPrima, BigDecimal> pmpStockConsumido;
	private Articulo articulo;
	private EnumTipoPiezaRE tipoPiezaRE;
	private PrecioMateriaPrima precioMateriaPrimaRE; //Precio Materia Prima del Remito de Entrada por Compra de Tela (se usa para las piezas crudas (i.e sin ODT))

	public PiezaRemitoSalidaTO() {
		this.pmpStockConsumido = new HashMap<PrecioMateriaPrima, BigDecimal>();
	}

	public PiezaRemito getPiezaRemitoEntrada() {
		return piezaRemitoEntrada;
	}

	public void setPiezaRemitoEntrada(PiezaRemito piezaRemitoEntrada) {
		this.piezaRemitoEntrada = piezaRemitoEntrada;
	}

	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	public Integer getNroPieza() {
		return nroPieza;
	}

	public void setNroPieza(Integer nroPieza) {
		this.nroPieza = nroPieza;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public void addPmpStockConsumido(PrecioMateriaPrima pmp, BigDecimal stock) {
		pmpStockConsumido.put(pmp, stock);
		if(articulo == null) {
			this.articulo = ((Tela)pmp.getMateriaPrima()).getArticulo(); 
		}
	}

	public BigDecimal getTotalMetrosStockConsumido() {
		BigDecimal t = new BigDecimal(0);
		for(PrecioMateriaPrima pmp : pmpStockConsumido.keySet()) {
			t = t.add(pmpStockConsumido.get(pmp));
		}
		return t;
	}

	public Map<PrecioMateriaPrima, BigDecimal> getPmpStockConsumido() {
		return pmpStockConsumido;
	}

	public boolean isPiezaStockInicial() {
		return !pmpStockConsumido.isEmpty();
	}
	
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public void setPrecioMateriaPrimaRE(PrecioMateriaPrima precioMateriaPrimaRE) {
		this.precioMateriaPrimaRE = precioMateriaPrimaRE;
	}

	public PrecioMateriaPrima getPrecioMateriaPrimaRE() {
		return precioMateriaPrimaRE;
	}

	public EnumTipoPiezaRE getTipoPiezaRE() {
		return tipoPiezaRE;
	}

	public void setTipoPiezaRE(EnumTipoPiezaRE tipoPiezaRE) {
		this.tipoPiezaRE = tipoPiezaRE;
	}

	public enum EnumTipoPiezaRE {
		ENTRADA_01,
		COMPRA_DE_TELA,
		PIEZA_STOCK_INICIAL;
	}
	
	

}
