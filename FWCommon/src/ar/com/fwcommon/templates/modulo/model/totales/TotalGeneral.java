package ar.com.fwcommon.templates.modulo.model.totales;

import java.awt.Color;

public class TotalGeneral<T> extends Total<T> {

	public TotalGeneral() {
		super("Total:");
	}

	public TotalGeneral(String nombre, Color color) {
		super(nombre);
		setColor(color);
	}

	public void totalizar(T objecto) {
		incrementarValor();
	}

}