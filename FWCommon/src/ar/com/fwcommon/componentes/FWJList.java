package ar.com.fwcommon.componentes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * Componente para la selección de items contenidos en una lista (igual a al componente
 * JList, aunque simplifica el uso de los métodos).
 */

public class FWJList extends JList {

	private static final long serialVersionUID = 6950288726258643624L;

	private DefaultListModel listModel;

	/** Método constructor */
	public FWJList() {
		super();
		listModel = new DefaultListModel();
		setModel(listModel);
	}

	/**
	 * Agrega un item a la lista.
	 * @param item El item a agregar a la lista.
	 */
	public void addItem(Object item) {
		listModel.addElement(item);
	}

	/**
	 * Elimina el item seleccionado de la lista.
	 */
	public void removeSelectedItem() {
		int i = getSelectedIndex();
		if(i != -1)
			listModel.removeElementAt(i);
	}

	/** Elimina todos los items de la lista */
	public void clear() {
		boolean enabled = isEnabled() ;
		if (enabled) {
			setEnabled(false);
		}
		listModel.clear();
		if (enabled) {
			setEnabled(true);
		}
	}

	/**
	 * Devuelve la cantidad de items de la lista.
	 * @return La cantidad de items de la lista.
	 */
	public int getItemCount() {
		return listModel.size();
	}

	/**
	 * Devuelve un item en la posición <b>index</b>.
	 * @return El item en la posición especificada.
	 */
	public Object getItem(int index) {
		if(index != -1) {
			return listModel.getElementAt(index);
		}
		return null;
	}

	/**
	 * Devuelve una lista (java.util.List) de los ítems de la lista.
	 * @return items
	 */
	public List getItemList() {
		List<Object> items = new ArrayList<Object>();
		for(int i = 0; i < getItemCount(); i++) {
            items.add(getItem(i));
        }
		return items;
	}

	/**
	 * Retorna <b>true</b> si existe el <b>item</b> en la lista.
	 * @param item
	 */	
	public boolean itemExist(Object item) {
		for(int i = 0; i < getItemCount(); i++)
			if(getItem(i).equals(item))
				return true;
		return false;
	}

	/**
	 * Cambia la tipografía a estilo <b>negrita</b> si <b>toBold</b> es <b>true</b>.
	 * @param toBold
	 */
	public void setFontBold(boolean toBold) {
		if(toBold) {
            setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
        } else {
            setFont(new Font(getFont().getName(), Font.PLAIN, getFont().getSize()));
        }
	}

	public void setSelectedValueWithText(String text) {
		ListModel listModel = getModel();
		if(text != null && text.trim().length() > 0) {
			for(int i = 0; i < listModel.getSize(); i++) {
				String valueStr = listModel.getElementAt(i).toString();
				if(valueStr.toLowerCase().indexOf(text.toLowerCase()) != -1) {
					setSelectedIndex(i);
					ensureIndexIsVisible(i);
					repaint();
					return;
				}
			}
		}
	}

}