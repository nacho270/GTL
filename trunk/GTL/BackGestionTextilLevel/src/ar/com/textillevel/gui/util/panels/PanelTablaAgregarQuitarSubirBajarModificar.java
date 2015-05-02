package ar.com.textillevel.gui.util.panels;

import java.awt.Component;
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;

public abstract class PanelTablaAgregarQuitarSubirBajarModificar<T> extends JPanel {

	private static final long serialVersionUID = 9211193882251973131L;

	private CLJTable tabla;
	private PanelBotonesAgregarQuitarSubirBajarModificar botonesTabla;
	private JPanel panBotonesExtra;
	protected boolean modoConsulta;

	protected PanelTablaAgregarQuitarSubirBajarModificar() {
		super(new GridBagLayout());
		rebuildTable(construirTabla());
	}

	protected void rebuildTable(CLJTable nuevaTabla) {
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
		// Botones tabla
		botonesTabla = new PanelBotonesAgregarQuitarSubirBajarModificar(tabla) {

			private static final long serialVersionUID = -1561673290098585554L;

			@Override
			public boolean validarAgregar() {
				return PanelTablaAgregarQuitarSubirBajarModificar.this.validarAgregar();
			};

			@Override
			public boolean validarQuitar() {
				return PanelTablaAgregarQuitarSubirBajarModificar.this.validarQuitar();
			};

			@Override
			public boolean validarSubir() {
				return PanelTablaAgregarQuitarSubirBajarModificar.this.validarSubir();
			}

			@Override
			public void botonSubirPresionado() {
				PanelTablaAgregarQuitarSubirBajarModificar.this.botonSubirPresionado();
			}

			@Override
			public boolean validarBajar() {
				return PanelTablaAgregarQuitarSubirBajarModificar.this.validarBajar();
			}

			@Override
			public void botonBajarPresionado() {
				PanelTablaAgregarQuitarSubirBajarModificar.this.botonBajarPresionado();
			}

			@Override
			public boolean validarModificar() {
				return PanelTablaAgregarQuitarSubirBajarModificar.this.validarModificar();
			}

			@Override
			public void botonModificarPresionado(int filaSeleccionada) {
				PanelTablaAgregarQuitarSubirBajarModificar.this.botonModificarPresionado(filaSeleccionada);
			}

		};
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = 0;
		constraints.gridx = 1;
		add(botonesTabla, constraints);
		// Panel botones extra
		panBotonesExtra = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 20));
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		// constraints.insets = new Insets(0, 5, 10, 5);
		constraints.insets = new Insets(0, 5, 20, 10);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = 1;
		constraints.gridx = 1;
		add(panBotonesExtra, constraints);
	}

	protected abstract CLJTable construirTabla();

	protected abstract T getElemento(int fila);

	protected abstract String validarElemento(int fila);

	protected boolean validarAgregar() {
		return true;
	}

	public boolean validarQuitar() {
		return true;
	}

	public boolean validarSubir() {
		return true;
	}

	public boolean validarBajar() {
		return true;
	}

	public boolean validarModificar() {
		return true;
	}

	public CLJTable getTabla() {
		return tabla;
	}

	public String validarElementos() {
		for (int fila = 0; fila < getTabla().getRowCount(); fila++) {
			String msg = validarElemento(fila);
			if (msg != null) {
				return msg;
			}
		}
		return null;
	}

	public List<T> getElementos() {
		List<T> elementos = new ArrayList<T>();
		for (int fila = 0; fila < getTabla().getRowCount(); fila++) {
			elementos.add(getElemento(fila));
		}
		return elementos;
	}

	protected abstract void agregarElemento(T elemento);

	protected void botonSubirPresionado() {

	}

	protected void botonBajarPresionado() {

	}

	protected void botonModificarPresionado() {
	}

	protected void botonModificarPresionado(int filaSeleccionada) {

	}

	protected void filaTablaSeleccionada() {
		
	}

	protected void dobleClickTabla(int filaSeleccionada) {
		
	}

	public void limpiar() {
		getTabla().removeAllRows();
	}

	public boolean isModoConsulta() {
		return modoConsulta;
	}

	public void setModoConsulta(boolean modoConsulta) {
		this.modoConsulta = modoConsulta;
		botonesTabla.setModoEdicion(!modoConsulta);
		for (JButton b : getBotonesExtra()) {
			b.setEnabled(!modoConsulta);
		}
	}

	public void agregarBoton(JButton boton) {
		panBotonesExtra.add(boton);
	}

	public void removerBotones() {
		botonesTabla.setVisible(false);
		panBotonesExtra.setVisible(false);
	}

	public JButton getBotonAgregar() {
		return botonesTabla.getBotonAgregar();
	}
	
	public JButton getBotonQuitar() {
		return botonesTabla.getBotonQuitar();
	}
	
	public JButton getBotonBajar() {
		return botonesTabla.getBotonBajar();
	}

	public JButton getBotonSubir() {
		return botonesTabla.getBotonSubir();
	}

	public JButton getBotonModificar() {
		return botonesTabla.getBotonModificar();
	}

	public PanelBotonesAgregarQuitarSubirBajarModificar getBotonesTabla() {
		return botonesTabla;
	}

	public Vector<JButton> getBotonesExtra() {
		Vector<JButton> btnsExtra = new Vector<JButton>();
		for (Component c : panBotonesExtra.getComponents()) {
			if (c instanceof JButton) {
				btnsExtra.add((JButton) c);
			}
		}
		return btnsExtra;
	}

	public void habilitarBotonesExtra(boolean habilitar) {
		for (JButton b : getBotonesExtra()) {
			b.setEnabled(habilitar);
		}
	}

	public void habilitarBotonSubir(boolean habilitar) {
		getBotonesTabla().getBotonSubir().setEnabled(habilitar);
	}

	public void habilitarBotonBajar(boolean habilitar) {
		getBotonesTabla().getBotonBajar().setEnabled(habilitar);
	}

	public void agregarBorde(String titulo) {
		setBorder(BorderFactory.createTitledBorder(titulo));
	}

	public void seleccionarFilaPorClave(int colClave, Object clave) {
		int fila = getTabla().getFirstRowWithValue(colClave, clave);
		if (fila != -1) {
			tabla.selectAndScroll(fila, fila);
		}
	}

	public void agregarElementos(Collection<T> elementos) {
		for (T elemento : elementos) {
			agregarElemento(elemento);
		}
	}

	public void agregarElementos(Collection<T> elementos, Comparator<T> comparator) {
		List<T> l = new ArrayList<T>(elementos);
		Collections.sort(l, comparator);
		agregarElementos(l);
	}

	public void seleccionarCelda(int fila, int col) {
		getTabla().selectCell(fila, col);
		getTabla().requestFocus();
	}

	class TablaSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent evt) {
			filaTablaSeleccionada();
		}
	}

	class TablaMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent evt) {
			if (evt.getClickCount() == 2) {
				dobleClickTabla(tabla.getSelectedRow());
			}
		}
	}

	/**
	 * 
	 * Retorna una lista con los contenidos de una columna
	 * @param colObjeto columna que se desea recuperar.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getDatosColumna(int colObjeto) {
		List<T> lista = new ArrayList<T>();
		for (int row = 0; row < tabla.getRowCount(); row++) {
			T elemento = (T) tabla.getValueAt(row, colObjeto);
			lista.add(elemento);

		}
		return lista;
	}
}