package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo;

import java.math.BigDecimal;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoNoRemunerativo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

public class CalculadorTotalesVisitor implements IItemReciboSueldoVisitor {

	private ReciboSueldo reciboSueldo;

	public CalculadorTotalesVisitor(ReciboSueldo reciboSueldo) {
		this.reciboSueldo = reciboSueldo;
		reciboSueldo.setBruto(BigDecimal.ZERO);
		reciboSueldo.setTotalNoRemunerativo(BigDecimal.ZERO);
		reciboSueldo.setNeto(BigDecimal.ZERO);
	}

	public void visit(ItemReciboSueldoHaber irsh) {
		reciboSueldo.setBruto(reciboSueldo.getBruto().add(irsh.getMonto()));
		reciboSueldo.setNeto(reciboSueldo.getNeto().add(irsh.getMonto()));
	}

	public void visit(ItemReciboSueldoNoRemunerativo irsnr) {
		reciboSueldo.setTotalNoRemunerativo(reciboSueldo.getTotalNoRemunerativo().add(irsnr.getMonto()));
		reciboSueldo.setNeto(reciboSueldo.getNeto().add(irsnr.getMonto()));
	}

	public void visit(ItemReciboSueldoRetencion irsr) {
		reciboSueldo.setTotalRetenciones(reciboSueldo.getTotalRetenciones().add(irsr.getMonto()));
		reciboSueldo.setNeto(reciboSueldo.getNeto().subtract(irsr.getMonto()));
	}

	public void visit(ItemReciboSueldoDeduccion irsd) {
		reciboSueldo.setTotalDeducciones(reciboSueldo.getTotalDeducciones().add(irsd.getMonto()));
		reciboSueldo.setNeto(reciboSueldo.getNeto().subtract(irsd.getMonto()));
	}

}