package ar.com.textillevel.gui.modulos.odt.util;

import java.math.BigDecimal;

import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ODTDatosMostradoHelper {

	private final OrdenDeTrabajo odt;
	
	public ODTDatosMostradoHelper(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	public String getDescTarima() {
		String descrTarima = odt.getRemito().getTarima() != null ? odt.getRemito().getTarima().toString() : null;
		String descrEnPalet = odt.getRemito().getEnPalet() != null && odt.getRemito().getEnPalet() ? "EN PALET" : null;
		if(descrTarima != null && descrEnPalet != null) {
			return descrTarima + " / " + descrEnPalet;
		} else if(descrTarima != null && descrEnPalet == null) {
			return descrTarima;
		} else if(descrTarima == null && descrEnPalet != null) {
			return descrEnPalet;
		} else {
			return "";
		}
	}

	public String getDescColor() {
		if(getColor() != null) {
			return getColor().getNombre();
		} else {
			return "";
		}
	}

	public Color getColor() {
		if(odt.getProducto() instanceof ProductoTenido) {
			return ((ProductoTenido)odt.getProducto()).getColor();
		} else if(odt.getProducto() instanceof ProductoEstampado) {
			return ((ProductoEstampado)odt.getProducto()).getVariante().getColorFondo();
		} else {
			return null;
		}
	}

	public String getDescArticulo() {
		return odt.getProducto().getArticulo() != null ? odt.getProducto().getArticulo().getNombre() : odt.getProducto().getDescripcion();
	}

	public String getDescGramaje() {
		return GenericUtils.getDecimalFormat3().format(odt.getRemito().getTotalMetros().floatValue()/odt.getRemito().getPesoTotal().floatValue());
	}

	public String getDescAnchoFinal() {
		BigDecimal anchoFinal = odt.getRemito().getAnchoFinal();
		return anchoFinal == null ? "" : GenericUtils.getDecimalFormat().format(anchoFinal.doubleValue());
	}

	public String getDescAnchoCrudo() {
		BigDecimal anchoCrudo = odt.getRemito().getAnchoCrudo();
		return anchoCrudo == null ? "" : GenericUtils.getDecimalFormat().format(anchoCrudo.doubleValue());
	}

	public String getDescCliente() {
		return String.valueOf(odt.getRemito().getCliente().getNroCliente());
		//TODO: Ver caso 01
	}

}