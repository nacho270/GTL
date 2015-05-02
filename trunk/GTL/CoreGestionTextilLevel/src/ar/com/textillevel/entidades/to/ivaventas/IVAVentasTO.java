package ar.com.textillevel.entidades.to.ivaventas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IVAVentasTO implements Serializable {

	private static final long serialVersionUID = -4739104529775524211L;

	private List<DescripcionFacturaIVAVentasTO> facturas;
	private TotalesIVAVentasTO totalResponsableInscripto;
	private TotalesIVAVentasTO totalGeneral;

	public IVAVentasTO() {
		facturas = new ArrayList<DescripcionFacturaIVAVentasTO>();
	}

	public List<DescripcionFacturaIVAVentasTO> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<DescripcionFacturaIVAVentasTO> facturas) {
		this.facturas = facturas;
	}

	public TotalesIVAVentasTO getTotalResponsableInscripto() {
		return totalResponsableInscripto;
	}

	public void setTotalResponsableInscripto(TotalesIVAVentasTO totalResponsableInscripto) {
		this.totalResponsableInscripto = totalResponsableInscripto;
	}

	public TotalesIVAVentasTO getTotalGeneral() {
		return totalGeneral;
	}

	public void setTotalGeneral(TotalesIVAVentasTO totalGeneral) {
		this.totalGeneral = totalGeneral;
	}
}
