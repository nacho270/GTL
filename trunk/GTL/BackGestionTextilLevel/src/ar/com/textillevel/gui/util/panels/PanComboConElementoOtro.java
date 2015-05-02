package ar.com.textillevel.gui.util.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;

public abstract class PanComboConElementoOtro<T> extends JPanel {

	private static final long serialVersionUID = -5227003402308737323L;

	private JComboBox cmbItems;
	private String lblCombo;
	private List<T> items;
	private T itemOtro;
	private PanComboActionListener panComboActionListener = new PanComboActionListener();

	public PanComboConElementoOtro(String lblCombo, List<T> items, T itemOtro) {
		this.lblCombo = lblCombo;
		this.items = new ArrayList<T>(items);
		this.itemOtro = itemOtro;
		construct();
		llenarCombo();
		getCmbItems().setSelectedIndex(-1);
	}

	private void llenarCombo() {
		items.remove(itemOtro);
		GuiUtil.llenarCombo(getCmbItems(), this.items, false);
		getCmbItems().addItem(itemOtro);
		getCmbItems().addActionListener(panComboActionListener);
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
	
	private void construct() {
		setLayout(new GridBagLayout());
		this.add(new JLabel(lblCombo), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		this.add(getCmbItems(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
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

}