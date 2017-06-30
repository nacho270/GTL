package ar.com.textillevel.gui.util.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;

public abstract class PanSelectorEntity<T> extends JPanel {

	private static final long serialVersionUID = -6720201839721580037L;

	private String entityName;
	private LinkableLabel lblElegirEntity;
	private LinkableLabel lblBorrarEntitySelected;
	private FWJTextField txtDescrEntity;

	public PanSelectorEntity(String entityName) {
		this.entityName = entityName;
		construct();
	}

	private void construct() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		JPanel labelPanels = new JPanel();
		labelPanels.setLayout(new GridBagLayout());
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5), 1, 1, 1, 0);
		labelPanels.add(getLblelegirEntity(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,5,5,5), 1, 1, 1, 0);
		labelPanels.add(getLblBorrarCliente(), gc);

		add(new JLabel(entityName + ": "));
		add(getDescrEntity());
		add(labelPanels);
	}

	private LinkableLabel getLblelegirEntity() {
		if (lblElegirEntity == null) {
			lblElegirEntity = new LinkableLabel("Elegir " + entityName) {

				private static final long serialVersionUID = 580819185565135378L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (e.getClickCount() == 1) {
						T selectedEntity = handleHowToSelectEntity();
						if(selectedEntity == null) {
							getDescrEntity().setText(null);
						} else {
							getDescrEntity().setText(toStringEntity(selectedEntity));
						}
						entitySelected(selectedEntity);
					}
				}
			};
		}
		return lblElegirEntity;
	}

	private LinkableLabel getLblBorrarCliente() {
		if (lblBorrarEntitySelected == null) {
			lblBorrarEntitySelected = new LinkableLabel("Borrar " + entityName) {

				private static final long serialVersionUID = 6524961686671880843L;

				@Override
				public void labelClickeada(MouseEvent e) {
					getDescrEntity().setText(null);
					entitySelected(null);
				}

			};
		}
		return lblBorrarEntitySelected;
	}

	private FWJTextField getDescrEntity() {
		if (txtDescrEntity == null) {
			txtDescrEntity = new FWJTextField();
			txtDescrEntity.setEditable(false);
			txtDescrEntity.setPreferredSize(new Dimension(50, 20));
		}
		return txtDescrEntity;
	}

	public void setEnabledOperations(boolean enabled) {
		getLblBorrarCliente().setVisible(enabled);
		getLblelegirEntity().setVisible(enabled);
	}

	public void clear() {
		getDescrEntity().setText(null);
	}

	public abstract String toStringEntity(T entity);
	public abstract T handleHowToSelectEntity();
	public abstract void entitySelected(T entity);

}