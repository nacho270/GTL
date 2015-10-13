package ar.com.textillevel.gui.modulos.cheques.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;

public class ColumnaEstadoCheque extends ColumnaString<Cheque>{

	public ColumnaEstadoCheque() {
		super("Estado");
		setAncho(180);
	}

	@Override
	public String getValor(Cheque item) {
		if(item.getEstadoCheque() == EEstadoCheque.SALIDA_PROVEEDOR || 
			item.getEstadoCheque() == EEstadoCheque.SALIDA_CLIENTE ||
			item.getEstadoCheque() == EEstadoCheque.SALIDA_PERSONA ||
			item.getEstadoCheque() == EEstadoCheque.SALIDA_BANCO ||
			item.getEstadoCheque() == EEstadoCheque.RECHAZADO){
			String nombreSalida = "";
			if(item.getProveedorSalida()!=null){
				nombreSalida = item.getProveedorSalida().getRazonSocial();
			}else if(item.getClienteSalida() != null){
				nombreSalida = item.getClienteSalida().getRazonSocial();
			}else if(item.getPersonaSalida()!=null){
				nombreSalida = item.getPersonaSalida().getRazonSocial();
			}if(item.getBancoSalida()!=null){
				nombreSalida = item.getBancoSalida().getNombre();
			}
			return item.getEstadoCheque().getDescripcion() + " / " + nombreSalida;
		}else{
			return item.getEstadoCheque().getDescripcion();
		}
	}
}
