package ar.com.textillevel.entidades.ventas.materiaprima;

import java.math.BigDecimal;

import ar.com.textillevel.entidades.ventas.articulos.Articulo;


public class ItemMateriaPrimaTelaTO extends ItemMateriaPrimaTO {

	private static final long serialVersionUID = 1319827644207825520L;

	private Articulo articulo;
	private BigDecimal stockFisico;
	private BigDecimal stockCrudo;
	private BigDecimal stockTerminado;

	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public BigDecimal getStockFisico() {
		return stockFisico;
	}

	public void setStockFisico(BigDecimal stockFisico) {
		this.stockFisico = stockFisico;
	}

	public BigDecimal getStockCrudo() {
		return stockCrudo;
	}

	public void setStockCrudo(BigDecimal stockCrudo) {
		this.stockCrudo = stockCrudo;
	}

	public BigDecimal getStockTerminado() {
		return stockTerminado;
	}

	public void setStockTerminado(BigDecimal stockTerminado) {
		this.stockTerminado = stockTerminado;
	}
	
}
