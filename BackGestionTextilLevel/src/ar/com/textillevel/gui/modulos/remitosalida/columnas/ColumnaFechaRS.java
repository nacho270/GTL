package ar.com.textillevel.gui.modulos.remitosalida.columnas;

import java.sql.Date;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class ColumnaFechaRS extends ColumnaDate<RemitoSalida> {

	public ColumnaFechaRS() {
		super("FECHA");
	}

	@Override
	public Date getValor(RemitoSalida item) {
		return item.getFechaEmision();
	}

}
