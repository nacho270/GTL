package ar.com.fwcommon.templates.modulo.gui.status;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;
import ar.com.fwcommon.templates.modulo.gui.meta.GuiSet;
import ar.com.fwcommon.templates.modulo.model.status.Status;
import ar.com.fwcommon.templates.modulo.model.status.Statuses;

/**
 * Gui que muestra todos los status
 * 
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class GuiStatuses<T> extends GuiSet<T, Status<T>> implements IGuiStatuses<T> {

	public GuiStatuses(ModuloTemplate<T, ?> owner, Statuses<T> statuses) {
		super(owner);
		setLayout(new GridLayout(0,1,0,2));
		setModel(statuses);
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#getModel()
	 */
	@Override
	public Statuses<T> getModel() {
		return (Statuses<T>)super.getModel();
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.status.IGuiStatuses#setModel(ar.com.fwcommon.templates.modulo.model.status.Statuses)
	 */
	public void setModel(Statuses<T> statuses) {
		if (this.model != statuses) {
			removeAll();
			if (this.model != null) this.model.removeListChangeListener(getElementListChangeListener());
			this.model = statuses;
			
			if (statuses != null) {
				final int cantidad = statuses.getElementCount();
				for (int i=0; i<cantidad; i++) {
					addElement(statuses.getElement(i));
				}
				this.model.addListChangeListener(getElementListChangeListener());
			}
			notificar();
		}	
	}
	
	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void addGroupElements(String name, List<Status<T>> elements) {
		for (Status<T> status : elements) {
			addSingleElement(status);
		}
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#addSingleElement(java.lang.Object)
	 */
	@Override
	protected void addSingleElement(Status<T> element) {
    	GuiStatus<T> guiStatus = new GuiStatus<T>(getOwner(), element);
        this.add(guiStatus);
        ((GridLayout) this.getLayout()).setRows(this.getComponentCount());
        this.validate();
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeGroupElements(java.lang.String, java.util.List)
	 */
	@Override
	protected void removeGroupElements(String name, List<Status<T>> elements) {
		for (Status<T> status : elements) {
			removeSingleElement(status);
		}		
	}

	/* (non-Javadoc)
	 * ar.com.fwcommon.templates.modulo.gui.meta.GuiSet#removeSingleElement(java.lang.Object)
	 */
	@Override
	protected void removeSingleElement(Status<T> element) {
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof GuiStatus) {
				if (element.equals(((GuiStatus<?>)components[i]).getStatus())) {
					remove(components[i]);
					((GridLayout)this.getLayout()).setRows(this.getComponentCount());
					this.validate();
					return;
				}
			}
		}	
	}
}
