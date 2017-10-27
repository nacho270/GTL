package ar.com.textillevel.gui.modulos.remitoentrada.columnas;

import java.sql.Date;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.gui.modulos.remitoentrada.to.RemitoEntradaModuloTO;

public class ColumnaFechaRE extends ColumnaDate<RemitoEntradaModuloTO> {

	public ColumnaFechaRE() {
		super("FECHA");
	}

	@Override
	public Date getValor(RemitoEntradaModuloTO item) {
		return item.getFechaIngreso();
	}

}
