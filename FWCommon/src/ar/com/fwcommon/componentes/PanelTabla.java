package ar.com.fwcommon.componentes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import ar.com.fwcommon.boss.BossEstilos;

@SuppressWarnings("serial")
public abstract class PanelTabla<T> extends JPanel {

	private FWJTable tabla;
	private FWBotonesTablaLight botonesTabla;
	private JPanel panBotonesExtra;
	private JButton btnModificar;
    protected boolean modoConsulta;
	private static final String ICONO_BOTON_MODIF = "ar/com/fwcommon/imagenes/b_modificar_fila.png";
	private static final String ICONO_BOTON_MODIF_DES = "ar/com/fwcommon/imagenes/b_modificar_fila_des.png";

	protected PanelTabla() {
		super(new GridBagLayout());
		//Tabla
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
		//Botones tabla
		botonesTabla = new FWBotonesTablaLight(tabla) {
		    @Override
			public boolean validarAgregar() {
                return PanelTabla.this.validarAgregar();
            }

            @Override
			public void botonAgregarPresionado() {
				PanelTabla.this.botonAgregarPresionado();                
			}

            @Override
			public boolean validarQuitar() {
                return PanelTabla.this.validarQuitar();
            }

            @Override
			public void botonQuitarPresionado() {
				PanelTabla.this.botonQuitarPresionado();
			}
		};
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = 0;
		constraints.gridx = 1;
		add(botonesTabla, constraints);
		//Panel botones extra
		panBotonesExtra = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 20));
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
//		constraints.insets = new Insets(0, 5, 10, 5);
		constraints.insets = new Insets(0, 5, 20, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = 1;
		constraints.gridx = 1;
		add(panBotonesExtra, constraints);
	}

    protected abstract FWJTable construirTabla();

    protected abstract void agregarElemento(T elemento);

    protected abstract T getElemento(int fila);

    protected abstract String validarElemento(int fila);

	public boolean validarQuitar() {
        return true;
    }

    public boolean validarAgregar() {
        return true;
    }

    public FWJTable getTabla() {
		return tabla;
	}

	public void agregarElementos(Collection<T> elementos){
		for(T elemento : elementos) {
			agregarElemento(elemento);
		}
	}

	public void agregarElementos(Collection<T> elementos, Comparator<T> comparator){
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

    protected void botonAgregarPresionado() {
	}

    protected void botonQuitarPresionado() {
	}

    protected void botonModificarPresionado(int filaSeleccionada) {
	}

	protected void filaTablaSeleccionada() {
	}

    protected void dobleClickTabla(int filaSeleccionada) {
    }

	protected void dobleClickTabla(int[] selectedRows) {
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
		for(JButton b : getBotonesExtra()) {
			b.setEnabled(!modoConsulta);
		}
		if(btnModificar != null && !isModoConsulta()) {
			btnModificar.setEnabled(tabla.getSelectedRow() != -1);
		}
	}

	public void agregarBoton(JButton boton) {
		panBotonesExtra.add(boton);
	}
	
	public JPanel getPanelBotonesExtra(){
		return panBotonesExtra;
	}

	public void agregarBotonModificar() {
		agregarBoton(crearBotonModificar());
	}

	public void removerBotones() {
		botonesTabla.setVisible(false);
		panBotonesExtra.setVisible(false);
	}

	public JButton getBotonAgregar() {
		return botonesTabla.getBotonAgregar();
	}

	public JButton getBotonEliminar() {
		return botonesTabla.getBotonEliminar();
	}

	public JButton getBotonModificar() {
		return btnModificar;
	}

	public FWBotonesTablaLight getBotonesTabla() {
		return botonesTabla;
	}

	public Vector<JButton> getBotonesExtra() {
		Vector<JButton> btnsExtra = new Vector<JButton>();
		for(Component c : panBotonesExtra.getComponents()) {
			if(c instanceof JButton) {
				btnsExtra.add((JButton)c);
			}
		}
		return btnsExtra;
	}

	public void habilitarBotonesExtra(boolean habilitar) {
		for(JButton b : getBotonesExtra()) {
			b.setEnabled(habilitar);
		}
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

	private JButton crearBotonModificar() {
		if(btnModificar == null) {
			btnModificar = BossEstilos.createButton(ICONO_BOTON_MODIF, ICONO_BOTON_MODIF_DES);
			btnModificar.setToolTipText("Modificar fila");
			btnModificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					int filaSeleccionada = getTabla().getSelectedRow();
					botonModificarPresionado(filaSeleccionada);
				}
			});
			btnModificar.setEnabled(false);
		}
		return btnModificar;
	}

   class TablaSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent evt) {
			if(btnModificar != null && !isModoConsulta()) {
				btnModificar.setEnabled((tabla.getSelectedRow() != -1));
			}
			if(!evt.getValueIsAdjusting()){
				filaTablaSeleccionada();				
			}
		}
	}

    class TablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent evt) {
			if(evt.getClickCount() == 2) {
				dobleClickTabla(tabla.getSelectedRow());
				dobleClickTabla(tabla.getSelectedRows());
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
		for(int row = 0; row < tabla.getRowCount(); row++){
			T elemento = (T) tabla.getValueAt(row,colObjeto);
			lista.add(elemento);
			
		}
		return lista;
	}

}