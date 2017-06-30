package ar.com.textillevel.gui.modulos.odt.cabecera;

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
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.panels.PanSelectorEntityCliente;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.util.GTLBeanFactory;

public class CabeceraODT extends Cabecera<ModeloCabeceraODT> {

	private static final long serialVersionUID = -8134663669868110901L;

	private ModeloCabeceraODT model;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	
	private JRadioButton rbtBuscarODTPorFiltros;
	private JRadioButton rbtBuscarODTPorCodigo;

	private JPanel panFiltros;
	
	private PanSelectorEntityCliente panSelectorCliente;
	
	private JPanel panProductos; 
	private JComboBox cmbProductos;
	private JComboBox cmbEstadoODT;
	private JCheckBox chkConProductoParcial;
	
	private FWJTextField txtCodigo;

	private ProductoFacadeRemote productoFacade;
	
	private ProductoFacadeRemote getProductoFacade() {
		if(productoFacade == null) {
			productoFacade = GTLBeanFactory.getInstance().getBean2(ProductoFacadeRemote.class);
		}
		return productoFacade;
	}

	public CabeceraODT() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Búsqueda"));
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,0,5), 1, 1, 1, 0.5);
		add(getPanFiltros(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,5,5,5), 1, 1, 0, 0.5);
		add(getPanBusquedaPorNro(), gc);
		ButtonGroup bgOpcionProceso = new ButtonGroup();
		bgOpcionProceso.add(getRbtBuscarODTPorFiltros());
		bgOpcionProceso.add(getRbtBuscarODTPorCodigo());
	}

	private JPanel getPanFiltros() {
		if(panFiltros == null) {
			panFiltros = new JPanel();
			panFiltros.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 0, 0);
			panFiltros.add(getRbtBuscarODTPorFiltros(), gc);
			gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 0, 0);
			panFiltros.add(getPanBusquedaFecha(), gc);
			gc = GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1);
			panFiltros.add(getPanSelectorCliente(), gc);
			gc = GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0);
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
			panProductos.add(new JLabel("ESTADO:"));
			panProductos.add(getCmbEstadoODT());
			panProductos.add(getChkConProductoParcial());
		}
		return panProductos;
	}

	private JComboBox getCmbEstadoODT() {
		if(cmbEstadoODT == null){
			cmbEstadoODT = new JComboBox();
			cmbEstadoODT.addItem("TODOS");
			for(EEstadoODT estado : EEstadoODT.values()){
				if(estado == EEstadoODT.ANTERIOR){
					continue;
				}
				cmbEstadoODT.addItem(estado);
			}
			cmbEstadoODT.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						notificar();
					}
				}
			});
			
		}
		return cmbEstadoODT;
	}	

	private JCheckBox getChkConProductoParcial() {
		if(chkConProductoParcial == null) {
			chkConProductoParcial = new JCheckBox("Sin Color");
			chkConProductoParcial.setSelected(false);
			chkConProductoParcial.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					notificar();
				}

			});
		}
		return chkConProductoParcial;
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
	public ModeloCabeceraODT getModel() {
		if (model == null) {
			model = new ModeloCabeceraODT();
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
		if(getCmbEstadoODT().getSelectedIndex() == 0) {
			model.setEstadoODT(null);
		} else {
			model.setEstadoODT((EEstadoODT)getCmbEstadoODT().getSelectedItem());
		}
		model.setConProductoParcial(getChkConProductoParcial().isSelected());
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

	private PanSelectorEntityCliente getPanSelectorCliente() {
		if(panSelectorCliente == null) {
			
			panSelectorCliente = new PanSelectorEntityCliente() {

				private static final long serialVersionUID = 1L;

				@Override
				public void entitySelected(Cliente entity) {
					model.setCliente(entity);
					notificar();
				}
			
			};
		}
		return panSelectorCliente;
	}

	
	private JRadioButton getRbtBuscarODTPorFiltros() {
		if(rbtBuscarODTPorFiltros == null) {
			rbtBuscarODTPorFiltros = new JRadioButton("Por Filtros");
			rbtBuscarODTPorFiltros.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setEnabledBusquedaPorFiltroComponents(true);
					setEnabledBusquedaPorNro(false);
					model.setBuscarPorFiltros(true);
					notificar();
				}

			});

			rbtBuscarODTPorFiltros.setSelected(true);
		}
		return rbtBuscarODTPorFiltros;
	}

	private JPanel getPanBusquedaPorNro() {
		JPanel pan = new JPanel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		pan.add(getRbtBuscarODTPorCodigo());
		pan.add(getTxtCodigo());
		return pan;
	}
	
	private JRadioButton getRbtBuscarODTPorCodigo() {
		if(rbtBuscarODTPorCodigo == null) {
			rbtBuscarODTPorCodigo = new JRadioButton("Por Código:");
			rbtBuscarODTPorCodigo.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setEnabledBusquedaPorFiltroComponents(false);
					setEnabledBusquedaPorNro(true);
					model.setBuscarPorFiltros(false);
					getPanSelectorCliente().clear();
					getTxtCodigo().requestFocus();
					notificar();
				}

			});
			
		}
		return rbtBuscarODTPorCodigo;
	}

	private void setEnabledBusquedaPorNro(boolean enabled) {
		getTxtCodigo().setEnabled(enabled);
	}

	private void setEnabledBusquedaPorFiltroComponents(boolean enabled) {
		getPanSelectorCliente().setEnabledOperations(enabled);
		getPanelFechaDesde().setEnabled(enabled);
		getPanelFechaHasta().setEnabled(enabled);
		getCmbProductos().setEnabled(enabled);
		getCmbEstadoODT().setEnabled(enabled);
	}

	private FWJTextField getTxtCodigo() {
		if(txtCodigo == null) {
			txtCodigo = new FWJTextField();
				
			txtCodigo.addKeyListener(new KeyListener() {
				
				public void keyTyped(KeyEvent e) {
				}
				
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						String value = getTxtCodigo().getText();
						model.setCodigoODT(value);
						notificar();
					}
				}

				public void keyPressed(KeyEvent e) {
				}

			});
			
			txtCodigo.setPreferredSize(new Dimension(70, 20));
			txtCodigo.setEnabled(false);
		}
		return txtCodigo;
	}

}
