package ar.com.fwcommon.templates;

import java.awt.Color;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;
import ar.com.fwcommon.util.GuiUtil;

/**
 * Template para la creación de formularios MDI con la siguiente estructura básica:
 * Un combobox en la parte superior izquierda, una lista (JList) a la izquierda,
 * y a la derecha de la pantalla, un conjunto de tabs o solapas para mostrar/editar
 * los datos relacionados a los items de la lista, con botones para <b>Modificar</b>
 * y <b>Grabar</b> esos datos.
 * ar.com.fwcommon.templates.GuiABMArbolTemplate
 */
public abstract class GuiABMListaTemplate extends GuiABMTemplate {

    protected FWJList lista;
    protected JTabbedPane panTabs;

    /** Método constructor */
    public GuiABMListaTemplate() {
        super();
        construct();
    }

    public GuiABMListaTemplate(Modulo modulo) throws FWException {
		super(modulo);
		construct();
    }

    /** Construye graficamente la pantalla */
    private void construct() {
        //Lista
        lista = new FWJList();
        getPanSelector().setViewportView(lista);
        lista.addListSelectionListener(new ListaListener());
        setSelector(lista);
        //Tabs
        panTabs = new JTabbedPane();
        panTabs.setFont(BossEstilos.getDefaultFont());
        getContentPane().add(panTabs);
        panTabs.setBounds(330, 60, 650, 570);
    }

    /** Método abstracto de llenado de la lista */
    public abstract void cargarLista();

    /** Actualiza el contenido de la lista */
    public void refrescarSelector() {
        if(isHijoCreado()) {
            Object sel = null;
            List oldValues = lista.getItemList();
            if(lista.getModel().getSize() > 0) {
                sel = lista.getSelectedValue();
            }
            lista.clear();
            cargarLista();
            Object newObject = getNewObject(oldValues, lista.getItemList());
            if(newObject != null) {
                lista.setSelectedValue(newObject, true);
            } else if(sel != null) {
                lista.setSelectedValue(sel, true);
            }
        }
    }

    /**
     * Devuelve el elemento recién agregado. Este método debería encontrar un elemento nuevo
     * sólo en el caso de 'Agregar'. 
     * @param oldValues La lista de elementos de la lista antes de agregar uno nuevo
     * @param newValues La lista de elementos de la lista después de agregar uno nuevo
     * @return El elemento recién agregado si existe, <code>null</code> en caso contrario
     */
    private Object getNewObject(List oldValues, List newValues) {
        if(oldValues != null && !oldValues.isEmpty()) {
            for(Object obj : newValues) {
                if(!oldValues.contains(obj)) {
                    return obj;
                }
            }
        }
        return null;
    }

    /** Devuelve <b>true</b> si la lista no tiene ningún ítem seleccionado */
    protected boolean isSelectorSelectionEmpty() {
        return lista.isSelectionEmpty();
    }

    protected void setEstadoInicialTemplate() {
        super.setEstadoInicialTemplate();
        getBtnAgregar().setEnabled(true);
        if(isHijoCreado())
            habilitarTabSeleccionado(false);
    }

    /** Define el estado de los botones y del selector en el modo edición */
    public void setModoEdicionTemplate(boolean estado) {
        habilitarTabSeleccionado(estado);
        super.setModoEdicionTemplate(estado);
    }

    /**
     * Habilita/Deshabilita el tab seleccionado.
     * @param estado
     */
    public void habilitarTabSeleccionado(boolean estado) {
        JComponent c = (JComponent)panTabs.getSelectedComponent();
        if(c != null) {
            GuiUtil.setEstadoPanel(c, estado);
        }
    }

    /**
     * Habilita/Deshabilita todos los tabs.
     * @param estado
     */
    public void habilitarTabs(boolean estado) {
        for(int i = 0; i < panTabs.getTabCount(); i++) {
            GuiUtil.setEstadoPanel((JComponent)panTabs.getComponentAt(i), estado);
        }
    }

    /** Clase listener de eventos de selección de items de la lista */
    class ListaListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent evt) {
			if(!evt.getValueIsAdjusting()) { //Para que el evento no se dispare dos veces
				if(lista.isEnabled()) {
	                habilitarAgregarEliminar();
				}
                if(lista.getSelectedIndex() != -1) {
                    itemSelectorSeleccionado(lista.getSelectedIndex());
                } else {
                    limpiarDatos();
                }
			}
        }
    }

    /**
     * Selecciona el tab donde ocurrió la validación y muestra el mensaje
     * correspondiente.
     * @param msg El mensaje a mostrar que contiene el texto de validación.
     * @param title El título del mensaje a mostrar.
     * @param tab El tab donde ocurrió la validación.
     */
    protected void mostrarMensajeValidacion(String msg, String title, JPanel tab) {
        panTabs.setSelectedComponent(tab);
        FWJOptionPane.showWarningMessage(GuiABMListaTemplate.this, StringW.wordWrap(msg), title);
    }

    /**
     * Selecciona el tab donde ocurrió la validación y muestra el mensaje
     * correspondiente.
     * @param msg El mensaje a mostrar que contiene el texto de validación.
     * @param title El título del mensaje a mostrar.
     * @param tab El tab donde ocurrió la validación.
     * @param tf El TextField que obtiene el foco.
     */
    protected void mostrarMensajeValidacion(String msg, String title, JPanel tab, JTextField tf) {
        panTabs.setSelectedComponent(tab);
        FWJOptionPane.showWarningMessage(GuiABMListaTemplate.this, StringW.wordWrap(msg), title);
        tf.requestFocus();
        tf.selectAll();
    }

    /**
     * Selecciona el tab donde ocurrió la validación y muestra el mensaje
     * correspondiente.
     * @param msg El mensaje a mostrar que contiene el texto de validación.
     * @param title El título del mensaje a mostrar.
     * @param tab El tab donde ocurrió la validación.
     * @param tabla
     * @param filas
     * @param color
     */
    protected void mostrarMensajeValidacion(String msg, String title, JPanel tab, FWJTable tabla, Integer[] filas, Color color) {
        panTabs.setSelectedComponent(tab);
        FWJOptionPane.showWarningMessage(GuiABMListaTemplate.this, StringW.wordWrap(msg), title);
        tabla.clearSelection();
        for(int fila : filas) {
        	tabla.setBackgroundRow(fila, color);
        }
    }

}