package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class ColumnaClienteODT extends ColumnaString<ODTTO>{

	public ColumnaClienteODT() {
		super("Cliente");
	}

	@Override
	public String getValor(ODTTO item) {
		return getDescripcionResumida(item);
	}

	private String getDescripcionResumida(ODTTO item) {
		String iniciales = "";
		String[] split = item.getNombreCliente().split(" ");
		for(String s : split){
			iniciales += s.toUpperCase().charAt(0);
		}
		return item.getNroCliente() + " " + iniciales;
	}

}
