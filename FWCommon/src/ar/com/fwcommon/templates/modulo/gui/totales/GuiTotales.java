package ar.com.fwcommon.templates.modulo.gui.totales;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.gui.meta.GuiSet;
import ar.com.fwcommon.templates.modulo.model.totales.Total;
import ar.com.fwcommon.templates.modulo.model.totales.Totales;

/**
 * GUI que agrupa a todos los totales
 *  
 * 
 */
@SuppressWarnings("serial")
public final class GuiTotales<T> extends GuiSet<T, Total<T>> implements IGuiTotales<T> {

    public GuiTotales(ModuloTemplate<T, ?> owner, Totales<T> totales) {    	
        super(owner);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setModel(totales);
    }
    
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.totales.IGuiTotales#getModel()
	 */
	public Totales<T> getModel() {
		return (Totales<T>)super.getModel();
	}

	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.totales.IGuiTotales#setModel(ar.com.fwcommon.templates.modulo.model.totales.Totales)
	 */
	public void setModel(Totales<T> totales) {
		if (this.model != totales) {
			removeAll();
			if (this.model != null) this.model.removeListChangeListener(getElementListChangeListener());
			this.model = totales;
			
			if (totales != null) {
				final int cantidad = totales.getElementCount();
				for (int i=0; i<cantidad; i++) {
					addElement(totales.getElement(i));
				}
				this.model.addListChangeListener(getElementListChangeListener());
			}
			notificar();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void addGroupElements(String name, List<Total<T>> elements) {
		for (Total<T> total : elements) {
			addSingleElement(total);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addSingleElement(java.lang.Object)
	 */
	@Override
	protected void addSingleElement(Total<T> total) {
    	GuiTotal<T> guiTotal = new GuiTotal<T>(getOwner(), total);
        this.add(guiTotal);
        this.validate();
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void removeGroupElements(String name, List<Total<T>> elements) {
		for (Total<T> total : elements) {
			removeSingleElement(total);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeSingleElement(java.lang.Object)
	 */
	@Override
	protected void removeSingleElement(Total<T> element) {
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof GuiTotal) {
				if (element.equals(((GuiTotal<?>)components[i]).getTotal())) {
					remove(components[i]);
					this.validate();
					return;
				}
			}
		}
	}
}