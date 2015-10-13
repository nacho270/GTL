package ar.com.textillevel.gui.modulos.agenda.cabecera;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class CabeceraAgenda extends Cabecera<ModeloCabeceraAgenda> {

	private static final long serialVersionUID = 3945789175664432323L;

	private JComboBox cmbCriterio;
	private FWJTextField txtBusqueda;
	private JButton btnBuscar;
	private ModeloCabeceraAgenda modeloCabecera;
	private JComboBox cmbRubroPersona;
	private JPanel pnlRubros;
	private RubroPersonaFacadeRemote rubroFacade;
	
	public CabeceraAgenda() {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(getTxtBusqueda());
		add(getCmbCriterio());
		JPanel pnlRubroPersona = getPnlRubroPersona();
		GuiUtil.setEstadoPanel(pnlRubroPersona, false);
		getModel().setRubroPersona(null);
		add(pnlRubroPersona);
		add(getBtnBuscar());
		//add(getPanelShortCuts());
	}

//	private JPanel getPanelShortCuts() {
//		JPanel panel = new JPanel();
//		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
//		JButton btnAbmCliente = new JButton("Agregar cliente");
//		btnAbmCliente.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new GuiABMCliente(-1);
//			}
//		});
//		panel.add(btnAbmCliente);
//		
//		JButton btnAbmProveedor = new JButton("Agregar proveedor");
//		btnAbmProveedor.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new GuiABMProveedor(-2).setVisible(true);
//			}
//		});
//		panel.add(btnAbmProveedor);
//		
//		JButton btnAbmPersona = new JButton("Agregar persona");
//		btnAbmPersona.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new GuiABMPersona(-3).setVisible(true);
//			}
//		});
//		panel.add(btnAbmPersona);
//		return panel;
//	}

	private JPanel getPnlRubroPersona() {
		if(pnlRubros == null){
			pnlRubros = new JPanel();
			pnlRubros.setLayout(new FlowLayout(FlowLayout.LEFT));
			pnlRubros.add(new JLabel("Rubro persona: "));
			pnlRubros.add(getCmbRubroPersona());
		}
		return pnlRubros;
	}

	public JComboBox getCmbCriterio() {
		if (cmbCriterio == null) {
			cmbCriterio = new JComboBox();
			GuiUtil.llenarCombo(cmbCriterio, Arrays.asList(ETipoBusquedaAgenda.values()), true);
			cmbCriterio.setPreferredSize(new Dimension(150, 20));
			cmbCriterio.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						if(((ETipoBusquedaAgenda)cmbCriterio.getSelectedItem()).equals(ETipoBusquedaAgenda.PERSONA)){
							List<Rubro> rubrosPersona = getRubroFacade().getAllRubroByTipo(ETipoRubro.PERSONA);
							getCmbRubroPersona().removeAllItems();
							getCmbRubroPersona().addItem("-TODOS-");
							for(Rubro r : rubrosPersona){
								getCmbRubroPersona().addItem(r);
							}
							//GuiUtil.llenarCombo(getCmbRubroPersona(), rubrosPersona, true);
							GuiUtil.setEstadoPanel(getPnlRubroPersona(), true);
						}else if(((ETipoBusquedaAgenda)cmbCriterio.getSelectedItem()).equals(ETipoBusquedaAgenda.PROVEEDOR)){
							List<Rubro> rubrosProveedor = getRubroFacade().getAllRubroByTipo(ETipoRubro.PROVEEDOR);
							getCmbRubroPersona().removeAllItems();
							getCmbRubroPersona().addItem("-TODOS-");
							for(Rubro r : rubrosProveedor){
								getCmbRubroPersona().addItem(r);
							}
						//	GuiUtil.llenarCombo(getCmbRubroPersona(), getRubroFacade().getAllRubroByTipo(ETipoRubro.PROVEEDOR), true);
							GuiUtil.setEstadoPanel(getPnlRubroPersona(), true);
						}else{
							GuiUtil.setEstadoPanel(getPnlRubroPersona(), false);
							getModel().setRubroPersona(null);
						}
					}
				}
			});
		}
		return cmbCriterio;
	}

	public FWJTextField getTxtBusqueda() {
		if(txtBusqueda == null){
			txtBusqueda = new FWJTextField();
			txtBusqueda.setPreferredSize(new Dimension(150, 20));
			txtBusqueda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!getCmbRubroPersona().isEnabled()){
						getModel().setRubroPersona(null);
					}
					notificar();
				}
			});
		}
		return txtBusqueda;
	}

	public JButton getBtnBuscar() {
		if(btnBuscar == null){
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!getCmbRubroPersona().isEnabled()){
						getModel().setRubroPersona(null);
					}
					notificar();
				}
			});
		}
		return btnBuscar;
	}

	@Override
	public ModeloCabeceraAgenda getModel() {
		if (modeloCabecera == null) {
			modeloCabecera = new ModeloCabeceraAgenda();
		}
		modeloCabecera.setCriterioBusqueda(getTxtBusqueda().getText());
		modeloCabecera.setTipoBusqueda((ETipoBusquedaAgenda) getCmbCriterio().getSelectedItem());
		if(!getCmbRubroPersona().isEnabled() || getCmbRubroPersona().getSelectedItem().equals("-TODOS-")){
			modeloCabecera.setRubroPersona(null);
		}else{
			modeloCabecera.setRubroPersona((Rubro)getCmbRubroPersona().getSelectedItem());
		}
		return modeloCabecera;
	}

	public JComboBox getCmbRubroPersona() {
		if(cmbRubroPersona == null){
			cmbRubroPersona = new JComboBox();
		}
		return cmbRubroPersona;
	}

	public RubroPersonaFacadeRemote getRubroFacade() {
		if(rubroFacade == null){
			rubroFacade = GTLBeanFactory.getInstance().getBean2(RubroPersonaFacadeRemote.class);
		}
		return rubroFacade;
	}
}
