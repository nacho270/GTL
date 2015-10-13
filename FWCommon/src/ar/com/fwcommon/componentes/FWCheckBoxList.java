package ar.com.fwcommon.componentes;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import ar.com.fwcommon.util.StringUtil;

/**
 * Componente que representa una lista (tal como el componente JList) con checkbox como items.
 */
public class FWCheckBoxList<T> extends JList {

	private static final long serialVersionUID = -3998363152138514775L;

	private boolean checkBoxPressed;

	/** Método constructor */
	public FWCheckBoxList() {
		setCellRenderer(new CheckBoxListCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFixedCellHeight(18);
		CheckBoxListener listener = new CheckBoxListener(this);
		addMouseListener(listener);
		addKeyListener(listener);
	}

	/**
	 * Método constructor.
	 * @param listData Los items de la lista.
	 */
	public FWCheckBoxList(final Object[] listData) {
		setValues(listData);
		setCellRenderer(new CheckBoxListCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFixedCellHeight(18);
		CheckBoxListener listener = new CheckBoxListener(this);
		addMouseListener(listener);
		addKeyListener(listener);
	}

	/**
	 * Genera los items <code>CLCheckBoxListItem</code>.
	 * @param items
	 * @return checkBoxItems
	 */
	private FWCheckBoxListItem[] createItems(Object[] items) {
		FWCheckBoxListItem[] checkBoxItems = new FWCheckBoxListItem[items.length];
		for(int i = 0; i < items.length; i++)
			checkBoxItems[i] = new FWCheckBoxListItem(items[i]);
		return checkBoxItems;
	}

	/**
	 * Selecciona/deselecciona todos los items de la lista.
	 * @param selected
	 */
	public void setAllSelectedItems(boolean selected) {
		ListModel listModel = getModel();
		for(int i = 0; i < listModel.getSize(); i++) {
			((FWCheckBoxListItem)listModel.getElementAt(i)).setSelected(selected);
		}
		repaint();
	}

	/** Retorna la cantidad de checkbox seleccionados en la lista */
	public int getSelectedCount() {
		return getSelectedValues().length;
	}

	/**
	 * Retorna <b>true</b> si se encuentran todos los checkbox seleccionados en la lista.
	 * @return
	 */
	public boolean allSelected() {
		if(getItemCount() == getSelectedCount())
			return true;
		return false;
	}

	/**
	 * Devuelve si el checkbox en el índice <b>i</b> está seleccionado.
	 * @param i El índice del checkbox.
	 */
	public boolean isItemSelected(int i) {
		return (((FWCheckBoxListItem)getModel().getElementAt(i)).isSelected());
	}

	/** Returns the largest selected cell index */
	public int getMaxSelectionIndex() {
		ListModel listModel = getModel();
		for(int i = listModel.getSize() - 1; i >= 0; i--) {
			if(((FWCheckBoxListItem)listModel.getElementAt(i)).isSelected())
				return i;
		}
		return -1;
	}

	/** Returns the smallest selected cell index */
	public int getMinSelectionIndex() {
		ListModel listModel = getModel();
		for(int i = 0; i < listModel.getSize(); i++) {
			if(((FWCheckBoxListItem)listModel.getElementAt(i)).isSelected())
				return i;
		}
		return -1;
	}

	/**
	 * Devuelve el objeto de la lista en el índice <b>i</b>.
	 * @param i El índice en la lista.
	 */
	public Object getItemAt(int i) {
		FWCheckBoxListItem checkBoxItem = null;
		checkBoxItem = (FWCheckBoxListItem)getModel().getElementAt(i);
		return checkBoxItem.getItem();
	}

	/**
	 * Devuelve el CLCheckBoxListItem de la lista en el índice <b>i</b>.
	 * @param i El índice en la lista.
	 */
	public FWCheckBoxListItem getCheckBoxListItemAt(int i) {
		return(FWCheckBoxListItem)getModel().getElementAt(i);
	}

	/** Returns the first selected index; returns -1 if there is no selected item */
	public int getSelectedIndex() {
		int i = getSelectedIndexSuper();
		if(i != -1) {
			if(((FWCheckBoxListItem)getModel().getElementAt(i)).isSelected())
				return i;
		}
		return -1;
	}

	/** Returns an array of all of the selected indices in increasing order */
	public int[] getSelectedIndices() {
		FWCheckBoxListItem checkBoxItem;
		ListModel listModel = getModel();
		int[] itemsTmp = new int[listModel.getSize()];
		int n = 0;
		for(int i = 0; i < listModel.getSize(); i++) {
			checkBoxItem = (FWCheckBoxListItem)listModel.getElementAt(i);
			if(checkBoxItem.isSelected())
				itemsTmp[n++] = i;
		}
		int[] items = new int[n];
		System.arraycopy(itemsTmp, 0, items, 0, n);
		return items;
	}

	/** Returns the first selected value, or null if the selection is empty */
	public Object getSelectedValue() {
		int i = getSelectedIndexSuper();
		if(i != -1) {
			FWCheckBoxListItem checkBoxItem = (FWCheckBoxListItem)getModel().getElementAt(i);
			if(checkBoxItem.isSelected())
				return checkBoxItem;
		}
		return null;
	}

	/** Returns an array of the values for the selected cells */
	public Object[] getSelectedValues() {
		FWCheckBoxListItem checkBoxItem;
		ListModel listModel = getModel();
		Object[] itemsTmp = new Object[listModel.getSize()];
		int n = 0;
		for(int i = 0; i < listModel.getSize(); i++) {
			checkBoxItem = (FWCheckBoxListItem)listModel.getElementAt(i);
			if(checkBoxItem.isSelected())
				itemsTmp[n++] = checkBoxItem.getItem();
		}
		Object[] items = new Object[n];
		System.arraycopy(itemsTmp, 0, items, 0, n);
		return items;
	}

	/**
	 * Retorna una lista <b>ArrayList</b> de los ítems seleccionados. 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getListSelectedValues() {
		return (List<T>)Arrays.asList(getSelectedValues());
	}

	/** Returns <b>true</b> if the specified index is selected */
	public boolean isSelectedIndex(int index) {
		FWCheckBoxListItem checkBoxItem = (FWCheckBoxListItem)getModel().getElementAt(index);
		if(checkBoxItem.isSelected())
			return true;
		return false;
	}

	/** Returns true if nothing is selected */
	public boolean isSelectionEmpty() {
		if(getMaxSelectionIndex() == -1)
			return true;
		return false;
	}

	/** Selects a single cell */
	public void setSelectedIndex(int index) {
		setSelectedIndex(index, true);
	}

	/** Selects a single cell */
	public void setSelectedIndex(int index, boolean selected) {
		if(index != -1) {
			checkBoxPressed = true;
			setSelectionInterval(index, index, selected);
		}
	}

    /** Selects a set of cells */
	public void setSelectedIndices(int[] indices) {
		for(int i = 0; i < indices.length; i++) {
			if(indices[i] != -1) {
				checkBoxPressed = true;
				setSelectionInterval(indices[i], indices[i]);
			}
		}
	}

	/** Selects the specified object from the list */
	public void setSelectedValue(Object obj, boolean shouldScroll) {
		ListModel listModel = getModel();
		if(obj == null)
			setSelectedIndex(-1);
		else if(!obj.equals(getSelectedValue())) {
			int i;
			for(i = 0; i < listModel.getSize(); i++)
				if(obj.equals(((FWCheckBoxListItem)listModel.getElementAt(i)).getItem())) {
					setSelectedIndex(i);
					if(shouldScroll)
						ensureIndexIsVisible(i);
					repaint();
					return;
				}
			setSelectedIndex(-1);
		}
		repaint();
	}

    public void setSelectedValueNoCheck(String text) {
        ListModel listModel = getModel();
        ListSelectionModel listSelectionModel = getSelectionModel();
        int firstValueIndex = -1;
        if(text != null && text.trim().length() > 0) {
        	text = StringUtil.reemplazarAcentos(text.trim()); //Reemplazo las vocales acentuadas
        	int selectedIndex = listSelectionModel.getAnchorSelectionIndex();
            for(int i = 0; i < listModel.getSize(); i++) {
            	Object obj = ((FWCheckBoxListItem)listModel.getElementAt(i)).getItem();
            	String objText = StringUtil.reemplazarAcentos(obj.toString().trim()); //Reemplazo las vocales acentuadas
                if(objText.toLowerCase().indexOf(text.toLowerCase()) != -1) {
                	if(firstValueIndex == -1) {
                		firstValueIndex = i;
                	}
                	if(selectedIndex >= i) {
                		continue;
                	}
                	setSelectedIndexNoCheck(i);
                    return;
                }
            }
            setSelectedIndexNoCheck(firstValueIndex);
        }
    }

    public void setSelectedIndexNoCheck(int index) {
    	setSelectionInterval(index, index, true);
    	ensureIndexIsVisible(index);
//    	repaint();
    }

    /** Selects the specified object from the list */
	public void setSelectionInterval(int anchor, int lead, boolean selected) {
		if(checkBoxPressed) {
			checkBoxPressed = false;
			for(int i = anchor; i <= lead; i++) {
				FWCheckBoxListItem checkBoxItem = (FWCheckBoxListItem)getModel().getElementAt(i);
				checkBoxItem.setSelected(selected);
			}
			repaint();
		} else
			super.setSelectionInterval(anchor, lead);
	}

	/** Selects the specified interval */
	public void setSelectionInterval(int anchor, int lead) {
		setSelectionInterval(anchor, lead, true);
	}

	/**
	 * Sets whether or not this component is enabled.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.clearSelection();
		//FIXME: Modificar el skin para que se vea cuando está deshabilitado y recién ahí descomentar éste código.
//		for(int i = 0; i < getItemCount(); i++) {
//			((CLCheckBoxListItem)getModel().getElementAt(i)).setEnabled(enabled);
//		}
	}

	/** Ejecuta el método getMinSelectionIndex de JList */
	private int getSelectedIndexSuper() {
		return super.getMinSelectionIndex();
	}

	/**
	 * Setea los items de la lista.
	 * @param values Los items de la lista.
	 */
	public void setValues(Object[] values) {
		super.setListData(createItems(values));
	}

	/** Devuelve la <b>cantidad de items</b> de la lista */
	public int getItemCount() {
		return getModel().getSize();
	}

	/**
	 * Manejo del evento de selección de un <b>checkbox</b> de la lista.
	 * Implementado vacío para ser sobreescrito.
	 * @param item El ítem de la lista
	 * @param seleccionado
	 */
	public void itemListaSeleccionado(Object item, boolean seleccionado) {
	}

	/**
     * @return La lista de objetos que se muestran en la lista
     *         para seleccionar.   
	 */
    @SuppressWarnings("unchecked")
    public List getItemList() {
        List items = new ArrayList();  
        ListModel listModel = getModel();
        for(int i = 0; i < listModel.getSize(); i++) {
            FWCheckBoxListItem checkBoxItem = (FWCheckBoxListItem)listModel.getElementAt(i);
            items.add(checkBoxItem.getItem());
        }
        return items;
    }

    /** Clase que implementa el CellRenderer de la lista */
	private class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer {

		private static final long serialVersionUID = -6503310746059269632L;

		/** Método constructor */
		public CheckBoxListCellRenderer() {
			super();
			setOpaque(true);
		}

		/**
		 * Método que realiza el render de cada celda de la lista.
		 * @param lista
		 * @param value
		 * @param indice
		 * @param isSeleccionado
		 * @param cellHasFocus
		 */
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			setFont(list.getFont());
			if(isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			//Habilita/deshabilita los checkboxes de la lista de acuerdo al estado de la lista
			/*
			if(list.isEnabled())
				this.setEnabled(true);
			else
				this.setEnabled(false);
			*/

			FWCheckBoxListItem checkBoxItem = (FWCheckBoxListItem)value;
			setSelected(checkBoxItem.isSelected());
			setEnabled(checkBoxItem.isEnabled());
			setToolTipText(checkBoxItem.getToolTipText());
			return this;
		}
	}

	/**
	 * Clase listener de eventos de los CLCheckBoxListItems.
	 * Necesario para hacer el rapaint de la lista cuando cambia el estado seleccionado
	 * de un checkbox.
	 */
	class CheckBoxListener extends MouseAdapter implements KeyListener {
		private FWCheckBoxList<T> cbl;

		/**
		 * Método constructor.
		 * @param cbl
		 */
		public CheckBoxListener(FWCheckBoxList<T> cbl) {
			this.cbl = cbl;
		}

		/** Evento de click de mouse */
		public void mouseClicked(MouseEvent evt) {
			if(evt.getX() < 20 || evt.getClickCount() == 2)
				if(isEnabled())
					selectCheckBox();
		}

		/** Evento de tecla presionada */
		public void keyPressed(KeyEvent evt) {
			//El código que sigue es porque selecciona los checks de arriba o abajo
			//Esta solución es dependiente de look and feel
			if(evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
				evt.consume();
				return;
			}
			//Si la tecla presionada es la barra espaciadora cambiar estado seleccionado
			if(evt.getKeyCode() == KeyEvent.VK_SPACE) {
				if(isEnabled()) {
					selectCheckBox();
				}
			}
		}

		public void keyTyped(KeyEvent evt) {
		}

		public void keyReleased(KeyEvent evt) {
		}

		/** Cambia el estado seleccionado del checkbox y hace el repaint de la lista */
		private void selectCheckBox() {
			int i = cbl.getSelectedIndexSuper();
			FWCheckBoxListItem checkBoxItem = null;
			if(i != -1) {
				checkBoxItem = (FWCheckBoxListItem)cbl.getModel().getElementAt(i);
				if(checkBoxItem.isSelected()) {
					checkBoxItem.invertSelection();
					checkBoxPressed = false;
				} else {
					checkBoxPressed = true;
					setSelectionInterval(i, i);
				}
			}
			cbl.repaint();
			if (checkBoxItem != null){
				itemListaSeleccionado(checkBoxItem.getItem(), checkBoxItem.isSelected());
			}
		}
	}

	public static class FWCheckBoxListItem {
		Object item;
		boolean selected;
		boolean enabled = true;
		String toolTipText;

		/**
		 * Método constructor.
		 * @param item
		 */
		public FWCheckBoxListItem(Object item) {
			this.item = item;
		}

		/**
		 * Otro constructor.
		 * @param item
		 * @param selected
		 */
		public FWCheckBoxListItem(Object item, boolean selected) {
			this.item = item;
			this.selected = selected;
		}

		/** Retorna el objeto ítem del checkbox */
		public Object getItem() {
			return item;
		}

		/** Retorna true si el ítem está seleccionado */
		public boolean isSelected() {
			return selected;
		}

		/**
		 * Setea el estado seleccionado del ítem.
		 * @param selected
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		/** Retorna true si el ítem está habilitado */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Setea el estado del ítem.
		 * @param enabled
		 */
		public void setEnabled(boolean enabled) {
			this.enabled= enabled;
		}

		/** Invierte el estado seleccionado del ítem */
		public void invertSelection() {
			this.selected = !selected;
		}

		/** Método toString */
		public String toString() {
			return item.toString();
		}

		public void setToolTipText(String toolTipText) {
			this.toolTipText = toolTipText;
		}

		public String getToolTipText() {
			return toolTipText;
		}
	}

}