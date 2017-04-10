package ar.com.textillevel.gui.modulos.remitoentrada.cabecera;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;

public class CabeceraRemitoEntrada extends Cabecera<ModeloCabeceraRemitoEntrada> {

	private static final long serialVersionUID = 4798710431798792144L;

	private ModeloCabeceraRemitoEntrada model;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	
	private JRadioButton rbtBuscarRemitoPorFiltros;
	private JRadioButton rbtBuscarRemitoPorNro;

	private JPanel panCliente;
	private JPanel panFiltros;
	
	private FWJTextField txtNroCliente;
	private LinkableLabel lblElegirCliente;
	private LinkableLabel lblBorrarCliente;
	
	private JPanel panProductos; 
	private JComboBox cmbProductos;
	private FWJNumericTextField txtNroRemito;

	private ProductoFacadeRemote productoFacade;
	
	
	private ProductoFacadeRemote getProductoFacade() {
		if(productoFacade == null) {
			productoFacade = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class);
		}
		return productoFacade;
	}

	public CabeceraRemitoEntrada() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Búsqueda"));
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 1, 0);
		add(getPanFiltros(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 1, 0);
		add(getPanBusquedaPorNro(), gc);
		ButtonGroup bgOpcionProceso = new ButtonGroup();
		bgOpcionProceso.add(getRbtBuscarRemitoPorFiltros());
		bgOpcionProceso.add(getRbtBuscarRemitoPorNro());
	}

	private JPanel getPanFiltros() {
		if(panFiltros == null) {
			panFiltros = new JPanel();
			panFiltros.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 1, 0);
			panFiltros.add(getRbtBuscarRemitoPorFiltros(), gc);
			gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 1, 0);
			panFiltros.add(getPanBusquedaFecha(), gc);
			gc = GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 1, 0);
			panFiltros.add(getPanelElegirCliente(), gc);
			gc = GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 1, 0);
			panFiltros.add(getPanProductos(), gc);
		}
		return panFiltros;
	}
	
	private JPanel getPanProductos() {
		if(panProductos == null) {
			panProductos = new JPanel();
			panProductos.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
			panProductos.add(new JLabel("PRODUCTO:"));
			panProductos.add(getCmbProductos());
		}
		return panProductos;
	}
	
	private JComboBox getCmbProductos() {
		if(cmbProductos == null) {
			cmbProductos = new JComboBox();
			GuiUtil.llenarCombo(cmbProductos, getProductoFacade().getAllOrderByName(), true);
			cmbProductos.insertItemAt("TODOS", 0);
			cmbProductos.setSelectedIndex(0);
			cmbProductos.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						notificar();
					}
				}
			});
			
		}
		return cmbProductos;
	}
	
	@Override
	public ModeloCabeceraRemitoEntrada getModel() {
		if (model == null) {
			model = new ModeloCabeceraRemitoEntrada();
		}
		Date fechaDesde = getPanelFechaDesde().getDate();
		Date fechaHasta = getPanelFechaHasta().getDate();
		model.setFechaDesde(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null);
		model.setFechaHasta(fechaHasta!=null?DateUtil.getManiana(new java.sql.Date(fechaHasta.getTime())):null);
		if(getCmbProductos().getSelectedIndex() == 0) {
			model.setProducto(null);
		} else {
			model.setProducto((Producto)getCmbProductos().getSelectedItem());
		}
		return model;
	}
	
	
	private PanelDatePicker getPanelFechaDesde() {
		if(panelFechaDesde == null){
			panelFechaDesde = new PanelDatePicker() {
				private static final long serialVersionUID = 1L;

				public void accionBotonCalendarioAdicional() {
					notificar();
				}
			};
			panelFechaDesde.setCaption("Fecha desde: ");
			panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 30));
		}
		return panelFechaDesde;
	}

	private PanelDatePicker getPanelFechaHasta() {
		if(panelFechaHasta == null){
			panelFechaHasta = new PanelDatePicker() {
				
				private static final long serialVersionUID = 1L;

				public void accionBotonCalendarioAdicional() {
					notificar();
				}
			};
			panelFechaHasta.setCaption("Fecha hasta: ");
			panelFechaHasta.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaHasta;
	}

	private JPanel getPanBusquedaFecha() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getPanelFechaDesde());
		pan.add(getPanelFechaHasta());
		return pan;
	}

	private JPanel getPanelElegirCliente(){
		if(panCliente == null) {
			panCliente = new JPanel();
			
			JPanel labelPanels = new JPanel();
			labelPanels.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5), 1, 1, 1, 0);
			labelPanels.add(getLblelegirCliente(), gc);
			gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,5,5,5), 1, 1, 1, 0);
			labelPanels.add(getLblBorrarCliente(), gc);

			panCliente.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
			panCliente.add(new JLabel("Cliente: "));
			panCliente.add(getTxtNroCliente());
			panCliente.add(labelPanels);
			
		}
		return panCliente;
	}
	
	private FWJTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new FWJTextField();
			txtNroCliente.setEditable(false);
			txtNroCliente.setPreferredSize(new Dimension(50, 20));
		}
		return txtNroCliente;
	}
	
	private LinkableLabel getLblelegirCliente() {
		if (lblElegirCliente == null) {
			lblElegirCliente = new LinkableLabel("Elegir cliente") {

				private static final long serialVersionUID = 580819185565135378L;

				@Override
				public void labelClickeada(MouseEvent e) {
					if (e.getClickCount() == 1) {
						JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_ID);
						GuiUtil.centrar(dialogSeleccionarCliente);
						dialogSeleccionarCliente.setVisible(true);
						Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
						if (clienteElegido != null) {
							model.setCliente(clienteElegido);
							getTxtNroCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
							notificar();
						}
					}
				}
			};
		}
		return lblElegirCliente;
	}

	private LinkableLabel getLblBorrarCliente() {
		if (lblBorrarCliente == null) {
			lblBorrarCliente = new LinkableLabel("Borrar Cliente") {

				private static final long serialVersionUID = 6524961686671880843L;

				@Override
				public void labelClickeada(MouseEvent e) {
						model.setCliente(null);
						getTxtNroCliente().setText(null);
						notificar();
				}
			};
		}
		return lblBorrarCliente;
	}
	
	private JRadioButton getRbtBuscarRemitoPorFiltros() {
		if(rbtBuscarRemitoPorFiltros == null) {
			rbtBuscarRemitoPorFiltros = new JRadioButton("Por Filtros");
			rbtBuscarRemitoPorFiltros.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setEnabledBusquedaPorFiltroComponents(true);
					setEnabledBusquedaPorNro(false);
					model.setBuscarPorFiltros(true);
					notificar();
				}

			});

			rbtBuscarRemitoPorFiltros.setSelected(true);
		}
		return rbtBuscarRemitoPorFiltros;
	}

	private JPanel getPanBusquedaPorNro() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getRbtBuscarRemitoPorNro());
		pan.add(getTxtNroRemito());
		return pan;
	}
	
	private JRadioButton getRbtBuscarRemitoPorNro() {
		if(rbtBuscarRemitoPorNro == null) {
			rbtBuscarRemitoPorNro = new JRadioButton("Por Número de Remito");
			rbtBuscarRemitoPorNro.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setEnabledBusquedaPorFiltroComponents(false);
					setEnabledBusquedaPorNro(true);
					model.setBuscarPorFiltros(false);
					getTxtNroCliente().setText(null);
					getTxtNroRemito().requestFocus();
					notificar();
				}

			});
			
		}
		return rbtBuscarRemitoPorNro;
	}

	private void setEnabledBusquedaPorNro(boolean enabled) {
		getTxtNroRemito().setEnabled(enabled);
	}

	private void setEnabledBusquedaPorFiltroComponents(boolean enabled) {
		getLblelegirCliente().setVisible(enabled);
		getLblBorrarCliente().setVisible(enabled);
		getPanelFechaDesde().setEnabled(enabled);
		getPanelFechaHasta().setEnabled(enabled);
		getCmbProductos().setEnabled(enabled);
	}

	private FWJNumericTextField getTxtNroRemito() {
		if(txtNroRemito == null) {
			txtNroRemito = new FWJNumericTextField(0, Long.MAX_VALUE);
				
			txtNroRemito.addKeyListener(new KeyListener() {
				
				public void keyTyped(KeyEvent e) {
				}
				
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						Integer value = getTxtNroRemito().getValue();
						model.setNroRemito(value);
						notificar();
					}
				}
				
				public void keyPressed(KeyEvent e) {
				}

			});
			
			txtNroRemito.setPreferredSize(new Dimension(70, 20));
			txtNroRemito.setEnabled(false);
		}
		return txtNroRemito;
	}

}