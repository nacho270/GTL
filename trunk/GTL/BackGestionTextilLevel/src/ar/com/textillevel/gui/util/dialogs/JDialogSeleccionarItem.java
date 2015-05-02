package ar.com.textillevel.gui.util.dialogs;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;

public class JDialogSeleccionarItem<T> extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private CLCheckBoxList<T> checkBoxList;
	private final List<T> itemsList = new ArrayList<T>();
	
	private boolean acepto;

	public JDialogSeleccionarItem(Frame owner, String title, List<T> itemsList) {
		//TODO: Hacer que en el dialogo sea configurable la validación de los items!!! 
		super(owner);
		setModal(true);
		setSize(new Dimension(455, 550));
		setTitle(title);
		construct();
		llenarList(itemsList);
	}

	private void llenarList(List<T> itemsList) {
		getCheckBoxList().setValues(itemsList.toArray());
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		GuiUtil.centrar(this);
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());

			JScrollPane scrollPane = new JScrollPane(getCheckBoxList());

			scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}
	
	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					acepto = false;
					itemsList.clear();
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					acepto = true;
					handleItemSeleccionado();
				}

			});
		}
		return btnAceptar;
	}

	@SuppressWarnings("unchecked")
	private void handleItemSeleccionado() {
		int[] selectedIndices = getCheckBoxList().getSelectedIndices();
		if(selectedIndices.length == 0) {
			CLJOptionPane.showErrorMessage(JDialogSeleccionarItem.this, "Debe seleccionar al menos un item.", getTitle());
			return;
		}
		for(int index : selectedIndices) {
			itemsList.add((T)getCheckBoxList().getItemAt(index));
		}
		dispose();
	}

	public List<T> getItemsList() {
		return itemsList;
	}

	private CLCheckBoxList<T> getCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new CLCheckBoxList<T>();
		}
		return checkBoxList;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setSelectedItems(List<T> itemsList) {
		for(T item : itemsList) {
			checkBoxList.setSelectedValue(item, false);
		}
	}

}