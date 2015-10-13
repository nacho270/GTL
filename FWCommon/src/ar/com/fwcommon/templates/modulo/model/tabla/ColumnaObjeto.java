package ar.com.fwcommon.templates.modulo.model.tabla;

public class ColumnaObjeto<T> extends Columna<T> {

	public ColumnaObjeto() {
		super("", TIPO_STRING);
		setAncho(0);
	}

	@Override
	public T getValor(T objeto) {
		return objeto;
	}

}
