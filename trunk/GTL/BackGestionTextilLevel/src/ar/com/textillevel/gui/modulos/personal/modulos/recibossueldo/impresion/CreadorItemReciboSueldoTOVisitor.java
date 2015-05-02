package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.impresion;

import java.util.List;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoNoRemunerativo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

public class CreadorItemReciboSueldoTOVisitor implements IItemReciboSueldoVisitor {

	private List<ItemReciboSueldoTO> items;

	public CreadorItemReciboSueldoTOVisitor(List<ItemReciboSueldoTO> items) {
		this.items = items;
	}

	public void visit(ItemReciboSueldoHaber irsh) {
		ItemReciboSueldoTO item = new ItemReciboSueldoTO();
		item.setConcepto(irsh.getDescripcion());
		item.setCodigo(irsh.getCodigo() == null ? "" : irsh.getCodigo());
		item.setHaberes(irsh.getMonto().toString());
		item.setUnidades(irsh.getUnidades() == null ? "" : irsh.getUnidades().toString());
		items.add(item);
	}

	public void visit(ItemReciboSueldoNoRemunerativo irsnr) {
		ItemReciboSueldoTO item = new ItemReciboSueldoTO();
		item.setConcepto(irsnr.getDescripcion());
		item.setCodigo(irsnr.getCodigo() == null ? "" : irsnr.getCodigo());
		item.setNoRemun(irsnr.getMonto().toString());
		item.setUnidades(irsnr.getUnidades() == null ? "" : irsnr.getUnidades().toString());
		items.add(item);
	}

	public void visit(ItemReciboSueldoRetencion irsr) {
		ItemReciboSueldoTO item = new ItemReciboSueldoTO();
		item.setConcepto(irsr.getDescripcion());
		item.setCodigo(irsr.getCodigo() == null ? "" : irsr.getCodigo());
		item.setRetenciones(irsr.getMonto().toString());
		item.setUnidades(irsr.getUnidades() == null ? "" : item.getUnidades().toString());
		items.add(item);
	}

	public void visit(ItemReciboSueldoDeduccion irsd) {
		ItemReciboSueldoTO item = new ItemReciboSueldoTO();
		item.setConcepto(irsd.getDescripcion());
		item.setCodigo(irsd.getCodigo() == null ? "" : irsd.getCodigo());
		item.setDeducciones(irsd.getMonto().toString());
		item.setUnidades(irsd.getUnidades() == null ? "" : item.getUnidades().toString());
		items.add(item);
	}

}