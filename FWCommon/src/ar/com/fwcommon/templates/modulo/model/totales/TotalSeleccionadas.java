package ar.com.fwcommon.templates.modulo.model.totales;

import java.awt.Color;

public class TotalSeleccionadas<T> extends Total<T> {

	public TotalSeleccionadas() {
		super("Seleccionadas:");
	}

	public TotalSeleccionadas(String nombre, Color color) {
		super(nombre);
		setColor(color);
	}

	@Override
	public void totalizar(T objeto) {
	}

}