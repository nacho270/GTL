package ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoNoRemunerativo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoRetencion;

public interface IItemReciboSueldoVisitor {

	public void visit(ItemReciboSueldoHaber irsh);
	public void visit(ItemReciboSueldoNoRemunerativo irsnr);
	public void visit(ItemReciboSueldoRetencion irsr);
	public void visit(ItemReciboSueldoDeduccion irsd);

}

