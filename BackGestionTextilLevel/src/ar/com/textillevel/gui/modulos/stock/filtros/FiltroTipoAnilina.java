package ar.com.textillevel.gui.modulos.stock.filtros;

import java.awt.Dimension;

import ar.clarin.fwjava.templates.modulo.model.filtros.FiltroListaOpciones;
import ar.clarin.fwjava.templates.modulo.model.filtros.FiltroRenderingInformation;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

public class FiltroTipoAnilina extends FiltroListaOpciones<ItemMateriaPrimaTO, TipoAnilina>{

	private FiltroRenderingInformation renderingInformation;
	
	public FiltroTipoAnilina() {
		super("Tipo de anilina");
		setTodosOption(true);
	}

	@Override
	public boolean filtrar(ItemMateriaPrimaTO item) {
		if(isTodosSelected()) {
			return true;
		}
		return item.getMateriaPrima() instanceof Anilina &&  getValue().equals(((Anilina)item.getMateriaPrima()).getTipoAnilina());
	}

	@Override
	public FiltroRenderingInformation getRenderingInformation() {
		if (renderingInformation == null) {
			renderingInformation = new FiltroRenderingInformation();
			renderingInformation.setMinimumSize(new Dimension(100, 20));
			renderingInformation.setAjustable(false);
		}
		return renderingInformation;
	}
}
