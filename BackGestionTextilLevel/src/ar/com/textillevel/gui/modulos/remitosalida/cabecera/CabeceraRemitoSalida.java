package ar.com.textillevel.gui.modulos.remitosalida.cabecera;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.panels.PanSelectorEntityCliente;
import ar.com.textillevel.gui.util.panels.PanSelectorEntityProveedor;

public class CabeceraRemitoSalida extends Cabecera<ModeloCabeceraRemitoSalida> {

	private static final long serialVersionUID = 4798710431798792144L;

	private ModeloCabeceraRemitoSalida model;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	
	private JRadioButton rbtBuscarRemitoPorFiltros;
	private JRadioButton rbtBuscarRemitoPorNro;

	private JPanel panFiltros;
	
	private PanSelectorEntityCliente panSelectorCliente;
	private PanSelectorEntityProveedor panSelectorProveedor;

	private FWJNumericTextField txtNroRemito;
	
	public CabeceraRemitoSalida() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Búsqueda"));
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,0,5), 1, 1, 1, 0.5);
		add(getPanFiltros(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,5,5,5), 1, 1, 0, 0.5);
		add(getPanBusquedaPorNro(), gc);
		ButtonGroup bgOpcionProceso = new ButtonGroup();
		bgOpcionProceso.add(getRbtBuscarRemitoPorFiltros());
		bgOpcionProceso.add(getRbtBuscarRemitoPorNro());
	}

	private JPanel getPanFiltros() {
		if(panFiltros == null) {
			panFiltros = new JPanel();
			panFiltros.setLayout(new GridBagLayout());
			GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 0, 0);
			panFiltros.add(getRbtBuscarRemitoPorFiltros(), gc);
			gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 1, 1, 0, 0);
			panFiltros.add(getPanBusquedaFecha(), gc);
			gc = GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1);
			panFiltros.add(getPanSelectorCliente(), gc);
			gc = GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1);
			panFiltros.add(getPanSelectorProveedor(), gc);
		}
		return panFiltros;
	}


	@Override
	public ModeloCabeceraRemitoSalida getModel() {
		if (model == null) {
			model = new ModeloCabeceraRemitoSalida();
		}
		Date fechaDesde = getPanelFechaDesde().getDate();
		Date fechaHasta = getPanelFechaHasta().getDate();
		model.setFechaDesde(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null);
		model.setFechaHasta(fechaHasta!=null?DateUtil.getManiana(new java.sql.Date(fechaHasta.getTime())):null);
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
					model.setProveedor(null);
					getPanSelectorProveedor().clear();
					notificar();
				}
			
			};
		}
		return panSelectorCliente;
	}

	private PanSelectorEntityProveedor getPanSelectorProveedor() {
		if(panSelectorProveedor == null) {
			
			panSelectorProveedor = new PanSelectorEntityProveedor() {

				private static final long serialVersionUID = 1L;

				@Override
				public void entitySelected(Proveedor entity) {
					model.setProveedor(entity);
					model.setCliente(null);
					getPanSelectorCliente().clear();
					notificar();
				}
			
			};
		}
		return panSelectorProveedor;
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
					getPanSelectorCliente().clear();
					model.setCliente(null);
					getPanSelectorProveedor().clear();
					model.setProveedor(null);
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
		getPanSelectorCliente().setEnabledOperations(enabled);
		getPanSelectorProveedor().setEnabledOperations(enabled);
		getPanelFechaDesde().setEnabled(enabled);
		getPanelFechaHasta().setEnabled(enabled);
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