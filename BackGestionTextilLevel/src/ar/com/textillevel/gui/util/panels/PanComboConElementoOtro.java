package ar.com.textillevel.gui.util.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;

public abstract class PanComboConElementoOtro<T> extends JPanel {

	private static final long serialVersionUID = -5227003402308737323L;

	private JButton btnModificar;
	private JComboBox cmbItems;
	private String lblCombo;
	private List<T> items;
	private T itemOtro;
	private PanComboActionListener panComboActionListener = new PanComboActionListener();
	
	private final EventListenerList listeners = new EventListenerList();

	public PanComboConElementoOtro(String lblCombo, List<T> items, T itemOtro) {
		this(lblCombo, items, itemOtro, false);
	}

	public PanComboConElementoOtro(String lblCombo, List<T> items, T itemOtro, boolean mostrarBotonModificar) {
		this.lblCombo = lblCombo;
		this.items = new ArrayList<T>(items);
		this.itemOtro = itemOtro;
		construct(mostrarBotonModificar);
		llenarCombo();
		getCmbItems().setSelectedIndex(-1);
	}

	private void llenarCombo() {
		items.remove(itemOtro);
		GuiUtil.llenarCombo(getCmbItems(), this.items, false);
		getCmbItems().addItem(itemOtro);
		getCmbItems().addActionListener(panComboActionListener);
	}

	public void addBotonModificarEventListener(BotonModificarEventListener<T> listener) {
		listeners.add(BotonModificarEventListener.class, listener);
	}
	
	public interface BotonModificarEventListener<T> extends EventListener {
		public void botonModificarPresionado(BotonModificarData<T> data);
	}
	
	public static class BotonModificarData<T> {
		private T itemAModificar;
		private int comboIndex;

		public BotonModificarData(T item, int comboIndex) {
			this.itemAModificar = item;
			this.comboIndex = comboIndex;
		}
		
		public T getItemAModificar() {
			return itemAModificar;
		}

		public int getComboIndex() {
			return comboIndex;
		}
	}

	private class PanComboActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			T itemSelected = ((T)getCmbItems().getSelectedItem());
			if(itemSelected!=null && itemSelected.equals(itemOtro)) {
				T newItem = itemOtroSelected();
				if(newItem == null) {
					getCmbItems().setSelectedItem(null);
				} else {
					items.add(newItem);
					llenarCombo();
					getCmbItems().setSelectedItem(newItem);
					itemDistintoOtroSelected(newItem);
				}
			} else if(itemSelected != null && !itemSelected.equals(itemOtro)) {
				itemDistintoOtroSelected(itemSelected);
			}
		}

	}
	
	private void construct(boolean mostrarBotonModificar) {
		setLayout(new GridBagLayout());
		this.add(new JLabel(lblCombo), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		if (mostrarBotonModificar) {
			this.add(getCmbItems(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			this.add(getBtnModificarInstruccion(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		} else {
			this.add(getCmbItems(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		}
	}

	protected void itemDistintoOtroSelected(T itemSelected) {
	}

	private JComboBox getCmbItems() {
		if(cmbItems == null) {
			cmbItems = new JComboBox();
		}
		return cmbItems;
	}

	public void setSelectedItem(T item) {
		getCmbItems().setSelectedItem(item);
	}

	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T)getCmbItems().getSelectedItem();
	}

	public abstract T itemOtroSelected();

	public String getLblCombo() {
		return lblCombo;
	}

	public void setItems(List<T> items) {
		this.items = new ArrayList<T>(items);
		getCmbItems().removeActionListener(panComboActionListener);
		GuiUtil.llenarCombo(getCmbItems(), items, true);
		getCmbItems().addActionListener(panComboActionListener);
	}

	public JButton getBtnModificarInstruccion() {
		if (btnModificar == null) {
			btnModificar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_modificar_fila.png", "ar/com/textillevel/imagenes/b_modificar_fila_des.png");
			btnModificar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					T selectedItem = getSelectedItem();
					if (selectedItem == null) {
						return;
					}
					@SuppressWarnings("unchecked")
					final BotonModificarEventListener<T>[] l = listeners.getListeners(BotonModificarEventListener.class);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].botonModificarPresionado(new BotonModificarData<T>(selectedItem, getCmbItems().getSelectedIndex()));
							}
						}
					});
				}
			});
		}
		return btnModificar;
	}
}