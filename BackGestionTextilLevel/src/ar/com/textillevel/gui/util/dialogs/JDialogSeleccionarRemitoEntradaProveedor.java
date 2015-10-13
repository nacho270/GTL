package ar.com.textillevel.gui.util.dialogs;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

public class JDialogSeleccionarRemitoEntradaProveedor extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private FWCheckBoxList<RemitoEntradaProveedor> checkBoxList;
	private Proveedor proveedor;
	private List<RemitoEntradaProveedor> remitoEntradaList = new ArrayList<RemitoEntradaProveedor>();
	
	private boolean acepto;

	public JDialogSeleccionarRemitoEntradaProveedor(Frame owner, Proveedor proveedor, List<RemitoEntradaProveedor> remitoEntradaList) {
		super(owner);
		setModal(true);
		this.proveedor = proveedor;
		setSize(new Dimension(455, 550));
		setTitle("Seleccionar remito de entrada");
		construct();
		llenarList(remitoEntradaList);
	}

	private void llenarList(List<RemitoEntradaProveedor> remitoEntradaList) {
		getCheckBoxList().setValues(remitoEntradaList.toArray(new RemitoEntradaProveedor[remitoEntradaList.size()]));
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		GuiUtil.centrar(this);
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());

			JScrollPane scrollPane = new JScrollPane(getCheckBoxList());

			scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}
	
	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					acepto = false;
					remitoEntradaList.clear();
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					acepto = true;
					handleRemitoSeleccionado();
				}

			});
		}
		return btnAceptar;
	}

	private void handleRemitoSeleccionado() {
		int[] selectedIndices = getCheckBoxList().getSelectedIndices();
		if(selectedIndices.length == 0) {
			FWJOptionPane.showErrorMessage(JDialogSeleccionarRemitoEntradaProveedor.this, "Debe seleccionar al menos un remito.", getTitle());
			return;
		}
		for(int index : selectedIndices) {
			remitoEntradaList.add((RemitoEntradaProveedor)getCheckBoxList().getItemAt(index));
		}
		dispose();
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public List<RemitoEntradaProveedor> getRemitoEntradaList() {
		return remitoEntradaList;
	}

	private FWCheckBoxList<RemitoEntradaProveedor> getCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new FWCheckBoxList<RemitoEntradaProveedor>();
		}
		return checkBoxList;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setSelectedRemitos(List<RemitoEntradaProveedor> remitoList) {
		for(RemitoEntradaProveedor rep : remitoList) {
			checkBoxList.setSelectedValue(rep, false);
		}
	}

}