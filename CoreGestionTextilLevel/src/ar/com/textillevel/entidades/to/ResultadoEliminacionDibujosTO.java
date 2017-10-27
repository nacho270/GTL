package ar.com.textillevel.entidades.to;

import java.util.List;
import com.google.common.collect.Lists;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;

public class ResultadoEliminacionDibujosTO {

	private List<RemitoEntradaDibujo> reDibujosInvolucrados;
	private double importePorDibujos;

	public ResultadoEliminacionDibujosTO(List<RemitoEntradaDibujo> reDibujosInvolucrados, double importePorDibujos) {
		this.reDibujosInvolucrados = reDibujosInvolucrados;
		this.importePorDibujos = importePorDibujos;
	}

	public List<RemitoEntradaDibujo> getReDibujosInvolucrados() {
		if(reDibujosInvolucrados == null) {
			return Lists.newArrayList();
		}
		return reDibujosInvolucrados;
	}

	public double getImportePorDibujos() {
		return importePorDibujos;
	}

	public RemitoEntradaDibujo findAny() {
		for(RemitoEntradaDibujo red : getReDibujosInvolucrados()) {
			if(red.getFactura() != null) {
				return red;
			}
		}
		return getReDibujosInvolucrados().isEmpty() ? null : getReDibujosInvolucrados().get(0);
	}

}