package ar.com.textillevel.gui.modulos.dibujos.cabecera;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;

public class CabeceraDibujos extends Cabecera<ModeloCabeceraDibjuos> {

	private static final long serialVersionUID = -6178911074486418969L;

	private FWJNumericTextField txtBusquedaCliente;
	private JComboBox cmbTipoBusquedaCliente;
	private JComboBox cmbEstadoDibujo;
	private ModeloCabeceraDibjuos modeloCabeceraDibujos;

	@Override
	public ModeloCabeceraDibjuos getModel() {
		if (modeloCabeceraDibujos == null) {
			modeloCabeceraDibujos = new ModeloCabeceraDibjuos();
		}
		Integer nroCliente = getTxtBusquedaCliente().getValueWithNull();
		modeloCabeceraDibujos.setNroCliente(nroCliente.equals(0) ? null : nroCliente);
		modeloCabeceraDibujos.setEstadoDibujo(getCmbEstadoDibujo().getSelectedItem().equals("TODOS") ?null:(EEstadoDibujo)getCmbEstadoDibujo().getSelectedItem());
		return modeloCabeceraDibujos;
	}

	public CabeceraDibujos() {
		setLayout(new GridBagLayout());
		JPanel panelCliente = new JPanel();
		panelCliente.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		panelCliente.setBorder(BorderFactory.createEtchedBorder());
		panelCliente.add(new JLabel("Cliente: "));
		panelCliente.add(getCmbTipoBusquedaCliente());
		panelCliente.add(getTxtBusquedaCliente());
		panelCliente.add(new JLabel("Estado dibujo: "));
		panelCliente.add(getCmbEstadoDibujo());
		add(panelCliente, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.LINE_END, new Insets(15, 5, 0, 5), 0, 0));
	}

	private void refrescar() {
		try {
			ModeloCabeceraDibjuos model = getModel();
			model.setNroCliente(getTxtBusquedaCliente().getValueWithNull());
			model.setEstadoDibujo(getCmbEstadoDibujo().getSelectedItem().equals("TODOS") ?null:(EEstadoDibujo)getCmbEstadoDibujo().getSelectedItem());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private FWJNumericTextField getTxtBusquedaCliente() {
		if (txtBusquedaCliente == null) {
			txtBusquedaCliente = new FWJNumericTextField(0, 9999l);
			txtBusquedaCliente.setPreferredSize(new Dimension(100, 20));
			txtBusquedaCliente.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					refrescar();
					notificar();
				}
			});
		}
		return txtBusquedaCliente;
	}

	private JComboBox getCmbTipoBusquedaCliente() {
		if (cmbTipoBusquedaCliente == null) {
			cmbTipoBusquedaCliente = new JComboBox();
			cmbTipoBusquedaCliente.addItem("ID");
			cmbTipoBusquedaCliente.addItem("NOMBRE");
			cmbTipoBusquedaCliente.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (cmbTipoBusquedaCliente.getSelectedItem().equals("NOMBRE")) {
							JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_NOMBRE);
							GuiUtil.centrar(dialogSeleccionarCliente);
							dialogSeleccionarCliente.setVisible(true);
							Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
							if (clienteElegido != null) {
								getTxtBusquedaCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
								refrescar();
								notificar();
							}
							getCmbTipoBusquedaCliente().setSelectedIndex(0);
						}
					}
				}
			});
		}
		return cmbTipoBusquedaCliente;
	}

	private JComboBox getCmbEstadoDibujo() {
		if (cmbEstadoDibujo == null) {
			cmbEstadoDibujo = new JComboBox();
			cmbEstadoDibujo.addItem("TODOS");
			for (EEstadoDibujo e : EEstadoDibujo.values()) {
				cmbEstadoDibujo.addItem(e);
			}
			cmbEstadoDibujo.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						refrescar();
						notificar();
					}
				}
			});
		}
		return cmbEstadoDibujo;
	}
}