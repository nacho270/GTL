package ar.com.textillevel.gui.util.panels;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ar.clarin.fwjava.componentes.CLCheckBoxListDialog;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;

public class PanelSeleccionarElementos<T> extends JPanel {

	private static final long serialVersionUID = 1922853475038097587L;

	private final Frame owner;
	private JTextField txtSelectedElements;
	private JButton btnSelectElements;
	private List<T> elements;
	private List<T> selectedElements;
	private final String titleLabel;
	private String separator = ", ";

	public PanelSeleccionarElementos(Frame owner, List<T> elements, String titleLabel) {
		super();
		this.owner = owner;
		this.titleLabel = titleLabel;
		construct(titleLabel);
		this.elements = elements;
		this.selectedElements = new ArrayList<T>();
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}

	public void setElementsAndSelectedElements(List<T> elements, List<T> selectedElements) {
		this.elements = elements;
		this.selectedElements = selectedElements;
		getTxtSelectedElements().setText(StringUtil.getCadena(selectedElements, separator));
	}

	private void construct(String titleLabel) {
		setLayout(new GridBagLayout());
		add(getBtnSelectElements(titleLabel), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getTxtSelectedElements(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
	}

	private JTextField getTxtSelectedElements() {
		if(txtSelectedElements == null) {
			txtSelectedElements = new JTextField();
			txtSelectedElements.setEditable(false);
		}
		return txtSelectedElements;
	}

	private JButton getBtnSelectElements(String titleLabel) {
		if(btnSelectElements == null) {
			btnSelectElements = new JButton(titleLabel);
			btnSelectElements.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					CLCheckBoxListDialog clCheckBoxListDialog = new CLCheckBoxListDialog(owner);
					clCheckBoxListDialog.setTitle(PanelSeleccionarElementos.this.titleLabel);
					clCheckBoxListDialog.setValores(elements, true);
					
					for(T element : selectedElements) {
						clCheckBoxListDialog.seleccionarValor(element);
					}
					clCheckBoxListDialog.setVisible(true);
					List<T> valoresSeleccionados = clCheckBoxListDialog.getValoresSeleccionados();
					selectedElements.clear();
					selectedElements.addAll(valoresSeleccionados);
					
					getTxtSelectedElements().setText(StringUtil.getCadena(selectedElements, separator));
					
					fireChangeItemFacturaEvent(selectedElements);
				}

			});
		}
		return btnSelectElements;
	}

	public List<T> getSelectedElements() {
		return selectedElements;
	}

	public void addPanelSeleccionarElementosListener(PanelSeleccionarElementoEventListener<T> l) {
		listenerList.add(PanelSeleccionarElementoEventListener.class, l);
	}

	@SuppressWarnings("unchecked")
	protected final void fireChangeItemFacturaEvent(List<T> selectedElements) {
		final PanelSeleccionarElementoEvent<T> e = new PanelSeleccionarElementoEvent<T>(this, selectedElements);
		@SuppressWarnings("rawtypes")
		final PanelSeleccionarElementoEventListener listeners[] = listenerList.getListeners(PanelSeleccionarElementoEventListener.class);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < listeners.length; i++) {
					listeners[i].elementsSelected(e);
				}
			}
		});
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void clear() {
		getTxtSelectedElements().setText(null);
	}

	public String getTitleLabel() {
		return titleLabel;
	}

	public void limpiar(){
		this.selectedElements = new ArrayList<T>();
		clear();
	}
}
