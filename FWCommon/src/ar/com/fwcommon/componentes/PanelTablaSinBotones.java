package ar.com.fwcommon.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public abstract class PanelTablaSinBotones<T> extends JPanel {

	private FWJTable tabla;
	private JButton btnModificar;
	protected boolean modoConsulta;

	protected PanelTablaSinBotones() {
		super(new GridBagLayout());
		// Tabla
		tabla = construirTabla();
		tabla.getSelectionModel().addListSelectionListener(new TablaSelectionListener());
		tabla.addMouseListener(new TablaMouseListener());
		JScrollPane sp = new JScrollPane(tabla);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		add(sp, constraints);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = 0;
		constraints.gridx = 1;
	}

	protected void rebuildTable(FWJTable nuevaTabla){
		removeAll();
		tabla = nuevaTabla;
		tabla.getSelectionModel().addListSelectionListener(new TablaSelectionListener());
		tabla.addMouseListener(new TablaMouseListener());
		JScrollPane sp = new JScrollPane(tabla);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		add(sp, constraints);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = 0;
		constraints.gridx = 1;
		invalidate();
		updateUI();
	}
	
	protected abstract FWJTable construirTabla();

	protected abstract void agregarElemento(T elemento);

	protected abstract T getElemento(int fila);

	protected abstract String validarElemento(int fila);

	public FWJTable getTabla() {
		return tabla;
	}

	public void agregarElementos(Collection<T> elementos) {
		for(T elemento : elementos) {
			agregarElemento(elemento);
		}
	}

	public void agregarElementos(Collection<T> elementos, Comparator<T> comparator) {
		List<T> l = new ArrayList<T>(elementos);
		Collections.sort(l, comparator);
		agregarElementos(l);
	}

	public String validarElementos() {
		for(int fila = 0; fila < getTabla().getRowCount(); fila++) {
			String msg = validarElemento(fila);
			if(msg != null) {
				return msg;
			}
		}
		return null;
	}

	public List<T> getElementos() {
		List<T> elementos = new ArrayList<T>();
		for(int fila = 0; fila < getTabla().getRowCount(); fila++) {
			elementos.add(getElemento(fila));
		}
		return elementos;
	}

	protected void filaTablaSeleccionada() {
	}

	protected void dobleClickTabla(int filaSeleccionada) {
	}

	public void limpiar() {
		getTabla().removeAllRows();
	}

	public void agregarBorde(String titulo) {
		setBorder(BorderFactory.createTitledBorder(titulo));
	}

	public void seleccionarFilaPorClave(int colClave, Object clave) {
		int fila = getTabla().getFirstRowWithValue(colClave, clave);
		if(fila != -1) {
			tabla.selectAndScroll(fila, fila);
		}
	}

	public void seleccionarCelda(int fila, int col) {
		getTabla().selectCell(fila, col);
		getTabla().requestFocus();
	}


	class TablaSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent evt) {
			if(btnModificar != null) {
				btnModificar.setEnabled((tabla.getSelectedRow() != -1));
			}
			filaTablaSeleccionada();
		}
	}

	class TablaMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent evt) {
			if(evt.getClickCount() == 2) {
				dobleClickTabla(tabla.getSelectedRow());
			}
		}
	}
}