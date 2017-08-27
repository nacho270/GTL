package ar.com.textillevel.gui.modulos.dibujos.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.VarianteEstampado;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class ColumnaVariantesDibujo extends ColumnaString<DibujoEstampado> {

	public ColumnaVariantesDibujo() {
		super("Variantes");
		setAncho(300);
	}

	@Override
	public String getValor(DibujoEstampado item) {
		return item.getVariantes() == null || item.getVariantes().isEmpty() ? " - " : //
				StringUtil.getCadena(FluentIterable.from(item.getVariantes()) //
						.transform(new Function<VarianteEstampado, String>() {
							@Override
							public String apply(VarianteEstampado input) {
								return input.getNombre();
							}
						}).toList(), ", ");
	}
}
