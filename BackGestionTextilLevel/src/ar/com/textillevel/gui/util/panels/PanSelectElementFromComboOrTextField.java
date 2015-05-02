package ar.com.textillevel.gui.util.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;

public abstract class PanSelectElementFromComboOrTextField<T> extends JPanel {

	private static final long serialVersionUID = -8135849230499235822L;
	private JComboBox cmbItems;
	private JTextField textField;
	private JCheckBox chkEscribirInTextfield;
	private List<T> items;
	
	public PanSelectElementFromComboOrTextField(String titleCombo, List<T> items) {
		this.items = items;
		construct(titleCombo);
	}

	private void construct(String titleCombo) {
		setLayout(new GridBagLayout());
		this.add(new JLabel(titleCombo), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		this.add(getCmbItems(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		this.add(getChkEscribirInTextfield(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		textField = createTextField(); 
		textField.setEditable(false);
		this.add(textField, GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
	}

	protected abstract JTextField createTextField();

	private JComboBox getCmbItems() {
		if(cmbItems == null) {
			cmbItems = new JComboBox();
			GuiUtil.llenarCombo(cmbItems, items, true);
		}
		return cmbItems;
	}

	private JCheckBox getChkEscribirInTextfield() {
		if(chkEscribirInTextfield == null) {
			chkEscribirInTextfield = new JCheckBox("Escribir valor");
			chkEscribirInTextfield.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent ie) {
					if(ie.getStateChange() == ItemEvent.SELECTED) {
						getCmbItems().setSelectedIndex(-1);
						getCmbItems().setEnabled(false);
						textField.setEditable(true);
					} else {
						getCmbItems().setEnabled(true);
						textField.setEditable(false);
						textField.setText(null);
					}
				}
			});
			
		}
		return chkEscribirInTextfield;
	} 

	@SuppressWarnings("unchecked")
	public T getItemIngresado() {
		if(getChkEscribirInTextfield().isSelected()) {
			return extractValueFromTextField();
		} else {
			return (T)getCmbItems().getSelectedItem();
		}
	}

	protected abstract T extractValueFromTextField();

	protected JTextField getTextField() {
		return textField;
	}

	public void setSelectedItem(T selectedItem) {
		if(items.contains(selectedItem)) {
			getCmbItems().setSelectedItem(selectedItem);
		} else {
			getCmbItems().setSelectedIndex(-1);
			getCmbItems().setEnabled(false);
			getTextField().setEditable(true);
			setValueInTextField(selectedItem);
		}
	}

	protected abstract void setValueInTextField(T selectedItem);

}