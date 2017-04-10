package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import java.sql.Date;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

public class ColumnaFechaRE extends ColumnaDate<RemitoEntrada> {

	public ColumnaFechaRE() {
		super("FECHA");
	}

	@Override
	public Date getValor(RemitoEntrada item) {
		return item.getFechaEmision();
	}

}
