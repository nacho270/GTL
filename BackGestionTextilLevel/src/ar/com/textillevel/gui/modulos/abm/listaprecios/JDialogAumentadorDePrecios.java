package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLCursor;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.DatosAumentoTO;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.GenericUtils.SiNoResponse;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAumentadorDePrecios extends JDialog {

	private static final long serialVersionUID = -1902150010141904549L;

	private static final int CANT_COLS = 2;
	private static final int COL_CLIENTE = 0;
	private static final int COL_RESULTADO = 1;
	
	private List<Cliente> allClientesConListaDePrecios;
	private List<Cliente> clientesSeleccionados;
	
	private ListaDePreciosFacadeRemote listaDePreciosFacade;
	
	private CLCheckBoxList<Cliente> chkListClientes;
	private JButton btnSeleccionarTodos;
	private JButton btnSeleccionarNinguno;
	private PanelTablaAumentosTipoProducto panelTablaAumentos;
	private JButton btnAumentar;
	private JButton btnAceptar;
	private JProgressBar progreso;
	private JLabel lblEstado;
	private JLabel lblWorking;
	private CLJTable tablaAvance;
	private JScrollPane jsp;
	
	public JDialogAumentadorDePrecios(Frame padre) {
		super(padre);
		this.allClientesConListaDePrecios = getListaDePreciosFacade().getClientesConListaDePrecios();
		this.clientesSeleccionados = new ArrayList<Cliente>(this.allClientesConListaDePrecios);
		getChkListClientes().setValues(allClientesConListaDePrecios.toArray());
		getChkListClientes().setAllSelectedItems(true);
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpComponentes() {
		JPanel panelNorte = new JPanel(new GridBagLayout());
		panelNorte.add(getPanelTablaAumentos(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 1));
		
		JScrollPane jsp = new JScrollPane(getChkListClientes(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(300, 100));
		
		panelNorte.add(jsp, GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 2, 3, 1, 0));
		panelNorte.add(getBtnSeleccionarTodos(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getBtnSeleccionarNinguno(), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getBtnAumentar(), GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelNorte.add(new JLabel("Estado: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getLblEstado(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 4, 1, 0, 0));
		panelNorte.add(getLblWorking(), GenericUtils.createGridBagConstraints(5, 4, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelNorte.add(new JLabel("Avance: "), GenericUtils.createGridBagConstraints(0, 5, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getProgreso(), GenericUtils.createGridBagConstraints(1, 5, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 0, 0));

		
		JPanel panelCentro = new JPanel(new GridBagLayout());
		panelCentro.add(getJsp(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 1));
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelSur.add(getBtnAceptar());
		
		add(panelNorte, BorderLayout.NORTH);
		add(panelCentro, BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setTitle("Aumentador de precios");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(500, 650);
		GuiUtil.centrar(this);
	}
	
	private JButton getBtnSeleccionarTodos() {
		if(btnSeleccionarTodos == null) {
			btnSeleccionarTodos = new JButton("Seleccionar todos");
			btnSeleccionarTodos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getChkListClientes().setAllSelectedItems(true);
					JDialogAumentadorDePrecios.this.clientesSeleccionados = new ArrayList<Cliente>(getAllClientesConListaDePrecios());
				}
			});
		}
		return btnSeleccionarTodos;
	}
	
	private JButton getBtnSeleccionarNinguno() {
		if(btnSeleccionarNinguno == null) {
			btnSeleccionarNinguno = new JButton("Quitar todos");
			btnSeleccionarNinguno.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getChkListClientes().setAllSelectedItems(false);
					JDialogAumentadorDePrecios.this.clientesSeleccionados = null;
				}
			});
		}
		return btnSeleccionarNinguno;
	}
	
	private CLCheckBoxList<Cliente> getChkListClientes() {
		if(chkListClientes == null) {
			chkListClientes = new CLCheckBoxList<Cliente>(){

				private static final long serialVersionUID = 5492341746189605276L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						Cliente cliente = (Cliente) item;
						if (!getClientesSeleccionados().contains(cliente)) {
							getClientesSeleccionados().add(cliente);
						}
					} else {
						getClientesSeleccionados().remove(item);
						if(getClientesSeleccionados().isEmpty()) {
							clientesSeleccionados = null;
						}
					}
				}
			};
		}
		return chkListClientes;
	}

	private JButton getBtnAumentar() {
		if(btnAumentar ==  null) {
			btnAumentar = new JButton("Aumentar", ImageUtil.loadIcon("ar/com/textillevel/imagenes/b_dinero.png"));
			btnAumentar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getPanelTablaAumentos().getTabla().getRowCount() == 0) {
						CLJOptionPane.showErrorMessage(JDialogAumentadorDePrecios.this, "Debe elegir un producto a aumentar", "Error");
						return;
					}
					if(getClientesSeleccionados() == null){
						CLJOptionPane.showErrorMessage(JDialogAumentadorDePrecios.this, "Debe elegir al menos un cliente", "Error");
						return;
					}
					for(int i = 0; i<getPanelTablaAumentos().getTabla().getRowCount(); i++) {
						if(getPanelTablaAumentos().getElemento(i).getPorcentajeAumento() == 0f){
							CLJOptionPane.showErrorMessage(JDialogAumentadorDePrecios.this, "El valor a aumentar es incorrecto", "Error");
							getPanelTablaAumentos().getTabla().setRowSelectionInterval(i, i);
							return;
						}
					}
					new ThreadAumentador(getClientesSeleccionados()).start();
				}
			});
		}
		return btnAumentar;
	}
	
	public CLJTable getTablaAvance() {
		if (tablaAvance == null) {
			tablaAvance = new CLJTable(0, CANT_COLS);
			tablaAvance.setStringColumn(COL_CLIENTE, "CLIENTE", 300, 300, true);
			tablaAvance.setStringColumn(COL_RESULTADO, "RESULTADO", 100, 100, true);
			tablaAvance.setHeaderAlignment(COL_CLIENTE, CLJTable.CENTER_ALIGN);
			tablaAvance.setHeaderAlignment(COL_RESULTADO, CLJTable.CENTER_ALIGN);
			tablaAvance.setReorderingAllowed(false);
			tablaAvance.setAllowHidingColumns(false);
			tablaAvance.setAllowSorting(false);
			tablaAvance.getColumnModel().getColumn(COL_RESULTADO).setCellRenderer(new EstadoCorreccionCuentaClienteCellRenderer());
		}
		return tablaAvance;
	}

	private class EstadoCorreccionCuentaClienteCellRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 7682578769114185942L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			EEstadoAumentoPrecioCliente estado = (EEstadoAumentoPrecioCliente) value;
			setText(String.valueOf(estado));
			if (estado == EEstadoAumentoPrecioCliente.ERROR) {
				setForeground(Color.RED);
			} else if (estado == EEstadoAumentoPrecioCliente.OK) {
				setForeground(Color.GREEN.darker());
			} else {
				setForeground(Color.BLACK);
			}
			return this;
		}
	}
	
	private enum EEstadoAumentoPrecioCliente {
		OK, PROCESANDO_LISTA, ENVIANDO_EMAIL, ERROR;
	}
	
	private JLabel getLblWorking() {
		if (lblWorking == null) {
			lblWorking = new JLabel(ImageUtil.loadIcon("ar/com/textillevel/imagenes/loading-chiquito.gif"));
			lblWorking.setVisible(false);
		}
		return lblWorking;
	}
	
	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel();
		}
		return lblEstado;
	}

	private JProgressBar getProgreso() {
		if (progreso == null) {
			progreso = new JProgressBar(0, 100);
			progreso.setStringPainted(true);
		}
		return progreso;
	}
	
	public JScrollPane getJsp() {
		if (jsp == null) {
			jsp = new JScrollPane(getTablaAvance(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(400, 200));
		}
		return jsp;
	}
	
	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	
	private PanelTablaAumentosTipoProducto getPanelTablaAumentos() {
		if (panelTablaAumentos == null) {
			panelTablaAumentos = new PanelTablaAumentosTipoProducto();
			panelTablaAumentos.setSize(300, 200);
			panelTablaAumentos.setPreferredSize(new Dimension(300, 200));
		}
		return panelTablaAumentos;
	}
	
	private class PanelTablaAumentosTipoProducto extends PanelTabla<DatosAumentoTO> {

		private static final long serialVersionUID = -8242619349477884825L;

		private static final int CANT_COLS = 3;
		private static final int COL_TIPO_PRODUCTO = 0;
		private static final int COL_AUMENTO = 1;
		private static final int COL_OBJ = 2;
		
		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			ETipoProducto[] productosFiltrados = ETipoProducto.valuesSinReprocesoSinTipo(ETipoProducto.REPROCESO_SIN_CARGO);
			tabla.setComboColumn(COL_TIPO_PRODUCTO, "PRODUCTO", new JComboBox(productosFiltrados), 270, false);
			ComboBoxTableCellEditor editor = new ComboBoxTableCellEditor(Arrays.asList(productosFiltrados));
			tabla.getColumnModel().getColumn(COL_TIPO_PRODUCTO).setCellEditor(editor);
			tabla.setFloatColumn(COL_AUMENTO, "AUMENTO (%)", 0f, 999999f, 90, false);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_TIPO_PRODUCTO, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_TIPO_PRODUCTO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_AUMENTO, CLJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_AUMENTO, CLJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			tabla.setSelectionMode(CLJTable.SINGLE_SELECTION);
			return tabla;
		}

		@Override
		protected DatosAumentoTO getElemento(int fila) {
			DatosAumentoTO datos = new DatosAumentoTO();
			datos.setTipoProducto((ETipoProducto)getTabla().getValueAt(fila, COL_TIPO_PRODUCTO));
			Object value = getTabla().getValueAt(fila, COL_AUMENTO);
			if(value == null) {
				datos.setPorcentajeAumento(0f);
			} else {
				datos.setPorcentajeAumento(Float.valueOf((String)value));
			}
			return datos;
		}

		public List<DatosAumentoTO> getDatosAumento() {
			List<DatosAumentoTO> datos = new ArrayList<DatosAumentoTO>();
			for(int i = 0; i<getTabla().getRowCount();i++) {
				datos.add(getElemento(i));
			}
			return datos;
		}

		// NO SE USAN
		@Override
		protected void agregarElemento(DatosAumentoTO elemento) { }

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	private class ComboBoxTableCellEditor extends AbstractCellEditor implements TableCellEditor {

		private static final long serialVersionUID = 8106765921055496460L;

		private JComboBox editor;
		private List<ETipoProducto> tiposProducto;

		public ComboBoxTableCellEditor(List<ETipoProducto> tiposProducto) {
			this.editor = new JComboBox();
			this.tiposProducto = tiposProducto;
		}

		public Object getCellEditorValue() {
			return editor.getSelectedItem();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(tiposProducto.toArray(new ETipoProducto[tiposProducto.size()]));
			for (int index = 0; index < table.getRowCount(); index++) {
				if (index != row) {
					ETipoProducto cellValue = (ETipoProducto) table.getValueAt(index, 0);
					model.removeElement(cellValue);
				}
			}
			editor.setModel(model);
			editor.setSelectedItem(value);
			return editor;
		}
	}

	public ListaDePreciosFacadeRemote getListaDePreciosFacade() {
		if(listaDePreciosFacade == null) {
			listaDePreciosFacade = GTLBeanFactory.getInstance().getBean2(ListaDePreciosFacadeRemote.class);
		}
		return listaDePreciosFacade;
	}

	public List<Cliente> getAllClientesConListaDePrecios() {
		return allClientesConListaDePrecios;
	}

	public List<Cliente> getClientesSeleccionados() {
		return clientesSeleccionados;
	}

	private class ThreadAumentador extends Thread {

		private List<Cliente> listaClientes;

		public ThreadAumentador(List<Cliente> clientes) {
			setClientes(clientes);
		}

		@Override
		public void run() {
			bloquearComponentes();
			CLCursor.startWait(JDialogAumentadorDePrecios.this);
			double avance = 100f / getListaClientes().size();
			double avanceAcumulado = 0;
			int contador = 1;
			boolean volverAPreguntarCotizacion = true;
			boolean volverAPreguntarEnviarEmail = true;
			boolean actualizarCotizacion = false;
			boolean enviarEmail = false;

			List<DatosAumentoTO> datosAumento = getPanelTablaAumentos().getDatosAumento();

			for (Cliente c : getListaClientes()) {
				try {
					agregarFila(c.getRazonSocial());
					getLblEstado().setText((contador++) + " de " + getListaClientes().size() + " - " + "aumentando a " + c.getRazonSocial());
					Cotizacion cotizacionActual = getListaDePreciosFacade().getCotizacionVigente(c);
					if (volverAPreguntarCotizacion){
						if (cotizacionActual != null){
							SiNoResponse preguntaCotizacion = GenericUtils.realizarPregunta(JDialogAumentadorDePrecios.this, "El cliente dispone de una cotizacion vigente. Desea actualizarla?", "Pregunta");
							if(preguntaCotizacion.getRespose() == CLJOptionPane.YES_OPTION){
								actualizarCotizacion = true;
							}
							if(preguntaCotizacion.isNoVolverAPreguntar()) {
								volverAPreguntarCotizacion = false;
							}
						}
					}

					getListaDePreciosFacade().aumentar(datosAumento, actualizarCotizacion);

					if (actualizarCotizacion) {
						// si pedi actualizar cotizacion, veo si mando el mail o no
						if (cotizacionActual != null) {
							if (volverAPreguntarEnviarEmail) {
								SiNoResponse preguntaCotizacion = GenericUtils.realizarPregunta(JDialogAumentadorDePrecios.this, "Desea enviar la cotizacion actualizada por email?", "Pregunta");
								if (preguntaCotizacion.getRespose() == CLJOptionPane.YES_OPTION) {
									enviarEmail = true;
								}
								if (preguntaCotizacion.isNoVolverAPreguntar()) {
									volverAPreguntarEnviarEmail = false;
								}
							}
							if (enviarEmail) {
								// actualizo la configuracion actual que ahora deberia ser nueva
								cotizacionActual = getListaDePreciosFacade().getCotizacionVigente(c);
								actualizarUltimaFila(EEstadoAumentoPrecioCliente.ENVIANDO_EMAIL);
								try {
									GenericUtils.enviarCotizacionPorEmail(c, new ImprimirListaDePreciosHandler(c, cotizacionActual.getVersionListaPrecio())
										.createJasperPrint(cotizacionActual.getValidez() + "", cotizacionActual.getNumero()));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
					actualizarUltimaFila(EEstadoAumentoPrecioCliente.OK);
					avanceAcumulado += avance;
					getProgreso().setValue((int) avanceAcumulado);
				} catch (Exception ex) {
					ex.printStackTrace();
					actualizarUltimaFila(EEstadoAumentoPrecioCliente.ERROR);
					continue;
				}
			}
			desBloquearComponentes();
			CLCursor.endWait(JDialogAumentadorDePrecios.this);
		}

		private void actualizarUltimaFila(EEstadoAumentoPrecioCliente estado) {
			getTablaAvance().setValueAt(estado, getTablaAvance().getRowCount() - 1, COL_RESULTADO);
		}

		private void agregarFila(String razonSocial) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CLIENTE] = razonSocial;
			row[COL_RESULTADO] = EEstadoAumentoPrecioCliente.PROCESANDO_LISTA;
			getTablaAvance().addRow(row);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getTablaAvance().scrollRectToVisible(getTablaAvance().getCellRect(getTablaAvance().getRowCount() - 1, getTablaAvance().getColumnCount(), true));
					getJsp().scrollRectToVisible(getTablaAvance().getCellRect(getTablaAvance().getRowCount() - 1, getTablaAvance().getColumnCount(), true));
					getJsp().invalidate();
					getJsp().repaint();
				}
			});
		}

		private void bloquearComponentes() {
			getTablaAvance().removeAllRows();
			getBtnSeleccionarTodos().setEnabled(false);
			getBtnSeleccionarNinguno().setEnabled(false);
			getChkListClientes().setEnabled(false);
			getLblWorking().setVisible(true);
			getBtnAceptar().setEnabled(false);
			getBtnAumentar().setEnabled(false);
			getProgreso().setValue(0);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}

		private void desBloquearComponentes() {
			getLblWorking().setVisible(false);
			getChkListClientes().setEnabled(true);
			getBtnSeleccionarTodos().setEnabled(true);
			getBtnSeleccionarNinguno().setEnabled(true);
			getBtnAceptar().setEnabled(true);
			getBtnAumentar().setEnabled(true);
			getLblEstado().setText("FIN");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

		public List<Cliente> getListaClientes() {
			return listaClientes;
		}

		public void setClientes(List<Cliente> listaCientes) {
			this.listaClientes = listaCientes;
		}

	}

}
