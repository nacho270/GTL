package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.com.fwcommon.util.DecorateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
/**
 * Componente que muestra un cuadro de diálogo con una lista de checkbox.
 * ar.com.fwcommon.componentes.CLCheckBoxList
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FWCheckBoxListDialog extends JDialog {

	private static final long serialVersionUID = 2516088591238560066L;
	
	public static Font fuente;
    private FWCheckBoxList checkBoxList;
    private JLabel mensajeLabel;
    private JButton btnAceptar;
    private JCheckBox chkTodos;
    private JCheckBox chkNinguno;
    private List valoresAnteriores;
    private List valoresOriginales;
    private JPanel panelTodosNinguno;
	private JButton btnFiltrar;
	private JTextField txtFiltroElementos;
	

    /** Método constructor */
    public FWCheckBoxListDialog() {
        super(new JDialog(), true);
        construct();
    }

    public FWCheckBoxListDialog(Frame owner) {
        super(owner, true);
        construct();
        setValores(valoresOriginales, true);
    }

    /**
     * Remueve los checkbox todos y ninguno.
     */
    public void removerCheckTodosNinguno() {
        panelTodosNinguno.remove(chkNinguno);
        panelTodosNinguno.remove(chkTodos);
    }

    /**
     * Método constructor.
     * @param owner La ventana dueña del cuadro de diálogo.
     * @param valores La lista de valores.
     */
    public FWCheckBoxListDialog(Dialog owner, List valores) {
        super(owner, true);
        construct();
        setValores(valores, false);
    }

    //Construye gráficamente el componente
    private void construct() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                getCheckBoxList().setAllSelectedItems(false);
                for(Iterator i = valoresAnteriores.iterator(); i.hasNext();) {
                    seleccionarValor(i.next());
                }
            }
        });
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        mensajeLabel = new JLabel();
        getContentPane().add(getPanNorte(), BorderLayout.NORTH);
        panelTodosNinguno = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTodosNinguno.add(getChkTodos());
        panelTodosNinguno.add(getChkNinguno());
        JPanel panel = new JPanel();
        panel.add(panelTodosNinguno);
        panel.add(getBtnAceptar());
        JScrollPane sp = new JScrollPane(getCheckBoxList());
        getContentPane().add(BorderLayout.CENTER, sp);
        getContentPane().add(BorderLayout.SOUTH, panel);
        pack();
        //Centra el componente en la pantalla
        GuiUtil.centrar(this);
    }

    private JPanel getPanNorte() {
    	JPanel panNorte = new JPanel();
    	panNorte.setLayout(new GridBagLayout());
    	panNorte.add(getTxtFiltroElementos(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
    	panNorte.add(getBtnFiltrar(), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
    	return panNorte;
    }

    private JTextField getTxtFiltroElementos() {
    	if(txtFiltroElementos == null) {
    		txtFiltroElementos = new JTextField();
    	}
		return txtFiltroElementos;
	}

	private JButton getBtnFiltrar() {
    	if(btnFiltrar == null) {
    		btnFiltrar = new JButton("Buscar");
    		btnFiltrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String textoFiltro = getTxtFiltroElementos().getText();
					if(StringUtil.isNullOrEmpty(textoFiltro)) {
						setValores(valoresOriginales, true);
					} else {
						List listaFiltro = new ArrayList();
						for(Object obj : valoresOriginales) {
							if(obj.toString().toLowerCase().indexOf(textoFiltro.trim().toLowerCase()) != -1) {
								listaFiltro.add(obj);
							}
						}
						setValores(listaFiltro, true);
					}
				}

    		});
    	}
		return btnFiltrar;
	}

	public String getMensaje() {
    	return mensajeLabel.getText();
    }

    public void setMensaje(String mensaje) {
    	mensajeLabel.setText(mensaje);
    	pack();
    }

    /**
     * Devuelve el componente CLCheckBoxList asociado.
     * @return checkBoxList El componente CLCheckBoxList asociado.
     */
	public FWCheckBoxList getCheckBoxList() {
        if(checkBoxList == null) {
            checkBoxList = new FWCheckBoxList() {

            	private static final long serialVersionUID = 6426896695529160785L;
				
            	@Override
            	public void itemListaSeleccionado(Object item, boolean seleccionado) {
            		FWCheckBoxListDialog.this.itemListaSeleccionado(item, seleccionado) ;
            	}
				@Override
				public void setAllSelectedItems(boolean selected) {
					super.setAllSelectedItems(selected);
					FWCheckBoxListDialog.this.setAllSelectedItems(selected);
				}
            };
            checkBoxList.addListSelectionListener(new ChksListSelectionListener());
        }
        return checkBoxList;
    } 

    /**
     * Sobreescribiendo este método se puede interceptar la selección/desselección de un ítem
     * @param item
     * @param seleccionado
     */
	public void itemListaSeleccionado(Object item, boolean seleccionado) {}
	
	/**
	 * Sobreescribiendo este método se puede interceptar la selección/desselección de todos los ítems
	 * @param selected
	 */
	public void setAllSelectedItems(boolean selected) {}

    class ChksListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent evt) {
            getChkNinguno().setSelected(false);
            getChkTodos().setSelected(false);
        }
    }

    /**
     * Setea los valores a la CLCheckBoxList.
     * @param valores La lista de valores de la CLCheckBoxList.
     * @param conservarSeleccion
     */
    public void setValores(List valores, boolean conservarSeleccion) {
        //Se guardan los seleccionados
        List seleccionados = new ArrayList();
        if(conservarSeleccion)
            seleccionados = getValoresSeleccionados();
        //Se cargan los nuevos valores
        if(valores != null) {
            getCheckBoxList().setValues(valores.toArray());
            //Se conservan las selecciones
            for(Iterator i = seleccionados.iterator(); i.hasNext();) {
                seleccionarValor(i.next());
            }
        }
        //Se respetan las selecciones anteriores
        if(getChkTodos().isSelected())
            getCheckBoxList().setAllSelectedItems(true);
        else if(getChkNinguno().isSelected())
            getCheckBoxList().setAllSelectedItems(false);
        pack();
        GuiUtil.centrar(this);
    }

    /**
     * Selecciona el valor en la CLCheckBoxList.
     * @param o El objeto que representa el valor.
     */
    public void seleccionarValor(Object o) {
        getCheckBoxList().setSelectedValue(o, true);
        getChkNinguno().setSelected(false);
        getChkTodos().setSelected(false);
    }

    /**
     * Devuelve una lista con los valores seleccionados en la CLCheckBoxList.
     * @return La lista de valores seleccionados en la CLCheckBoxList.
     */
	public List getValoresSeleccionados() {
        return getCheckBoxList().getListSelectedValues();
    }

    /**
     * Devuelve el botón <b>Aceptar</b> asociado.
     * @return btnAceptar
     */
    public JButton getBtnAceptar() {
        if(btnAceptar == null) {
            btnAceptar = new JButton("Aceptar");
            btnAceptar.setMnemonic(java.awt.event.KeyEvent.VK_A);
            btnAceptar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	btnAceptarPresionado();
                	setVisible(false);
                }
            });
            DecorateUtil.decorateButton(btnAceptar);
        }
        return btnAceptar;
    }
    
    protected void btnAceptarPresionado() {}

    /**
     * Devuelve el checkbox <b>Ninguno</b>.
     * @return chkNinguno
     */
    public JCheckBox getChkNinguno() {
        if(chkNinguno == null) {
            chkNinguno = new JCheckBox("Ninguno");
            DecorateUtil.decorateCheckBox(chkNinguno);
            chkNinguno.addItemListener(new ChkNingunoItemListener());
        }
        return chkNinguno;
    }

    /**
     * Devuelve el checkbox <b>Todos</b>.
     * @return chkTodos
     */
    public JCheckBox getChkTodos() {
        if(chkTodos == null) {
            chkTodos = new JCheckBox(StringUtil.TODOS);
            DecorateUtil.decorateCheckBox(chkTodos);
            chkTodos.addItemListener(new ChkTodosItemListener());
        }
        return chkTodos;
    }

    /**
     * Devuelve los valores seleccionados separados por ','.
     * @return Un String con los valores seleccionados separados por ','.
     */
     // mfazzito 24/06/09  se modifca por fix  7986 
    public String getValoresSeleccionadosAsString() {
    	//return (getCheckBoxList().allSelected() ? "" : StringUtil.getCadena(getValoresSeleccionados().toArray(), ", "));
    	return (StringUtil.getCadena(getValoresSeleccionados().toArray(), ", "));
    }

	/** Sobreescritura del método setVisible(true) de JDialog */
    public void setVisible(boolean b) {
        if(b)
            valoresAnteriores = getValoresSeleccionados();
        super.setVisible(b);
    }

    class ChkTodosItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent evt) {
            if(evt.getStateChange() == ItemEvent.SELECTED) {
                getChkNinguno().setSelected(false);
                getCheckBoxList().setAllSelectedItems(true);
            }
        }
    }

    class ChkNingunoItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent evt) {
            if(evt.getStateChange() == ItemEvent.SELECTED) {
                getChkTodos().setSelected(false);
                getCheckBoxList().setAllSelectedItems(false);
            }
        }
    }

}