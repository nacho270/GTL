package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.columnas;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaInt;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;

public class ColumnaNumeroLegajo extends ColumnaInt<InfoReciboSueltoTO>{

	public ColumnaNumeroLegajo() {
		super("Nº de legajo");
		setAncho(100);
		setAlineacion(FWJTable.CENTER_ALIGN);
	}

	@Override
	public Integer getValor(InfoReciboSueltoTO item) {
		return item.getLegajo()!=null?item.getLegajo().getNroLegajo():null;
	}

}
