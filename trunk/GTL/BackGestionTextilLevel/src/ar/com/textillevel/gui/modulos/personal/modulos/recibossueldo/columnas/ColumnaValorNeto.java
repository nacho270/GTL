package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaFloat;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

public class ColumnaValorNeto extends ColumnaFloat<InfoReciboSueltoTO>{

	public ColumnaValorNeto() {
		super("Total Neto");
		setAncho(100);
		setAlineacion(CLJTable.CENTER_ALIGN);
	}

	@Override
	public Float getValor(InfoReciboSueltoTO item) {
		return item.getReciboSueldo()!=null?item.getReciboSueldo().getNeto().floatValue():null;
	}

}