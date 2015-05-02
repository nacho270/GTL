package ar.com.textillevel.gui.acciones.proveedor;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Dialog;
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

import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.ImpuestoItemProveedorFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogCargaImpuestoItemProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarCrearImpuesto extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private CLCheckBoxList<ImpuestoItemProveedor> checkBoxList;
	private JButton btnAgregarImpuesto;
	private final List<ImpuestoItemProveedor> allImpuestos;
	private final List<ImpuestoItemProveedor> impuestosSelectedResult = new ArrayList<ImpuestoItemProveedor>();
	private boolean acepto;
	private Frame owner;

	private ImpuestoItemProveedorFacadeRemote impuestoFacadeRemote;

	public JDialogSeleccionarCrearImpuesto(Frame owner, Proveedor proveedor, List<ImpuestoItemProveedor> impuestosSelected) {
		super(owner);
		setModal(true);
		setSize(new Dimension(350, 400));
		setTitle("Seleccionar/Crear Impuesto(s)");
		construct();
		this.allImpuestos = filterIIBByProvincia(getImpuestoFacadeRemote().getAllOrderByName(), proveedor);
		getCheckBoxList().setValues(allImpuestos.toArray(new ImpuestoItemProveedor[allImpuestos.size()]));
		for(ImpuestoItemProveedor impuesto : impuestosSelected) {
			getCheckBoxList().setSelectedValue(impuesto, false);
		}
	}
	
	private List<ImpuestoItemProveedor> filterIIBByProvincia(List<ImpuestoItemProveedor> allOrderByName, Proveedor proveedor) {
		List<ImpuestoItemProveedor> result = new ArrayList<ImpuestoItemProveedor>();
		for(ImpuestoItemProveedor iip : allOrderByName) {
			if(iip.getTipoImpuesto() != ETipoImpuesto.INGRESOS_BRUTOS || iip.getProvincia().getId().equals(proveedor.getProvincia().getId())) {
				result.add(iip);
			}
		}
		return result;
	}

	public JDialogSeleccionarCrearImpuesto(Dialog owner, List<ImpuestoItemProveedor> impuestosSelected) {
		super(owner);
		setModal(true);
		setSize(new Dimension(350, 400));
		setTitle("Seleccionar/Crear Impuesto(s)");
		construct();
		this.allImpuestos = getImpuestoFacadeRemote().getAllOrderByName();
		getCheckBoxList().setValues(allImpuestos.toArray(new ImpuestoItemProveedor[allImpuestos.size()]));
		for(ImpuestoItemProveedor impuesto : impuestosSelected) {
			getCheckBoxList().setSelectedValue(impuesto, false);
		}
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		GuiUtil.centrar(this);
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			JScrollPane scrollPane = new JScrollPane(getCheckBoxList());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Impuestos"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
			panDetalle.add(getBtnAgregarImpuesto(), createGridBagConstraints(1, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
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
					handleImpuestosSeleccionados();
				}

			});
		}
		return btnAceptar;
	}

	private void handleImpuestosSeleccionados() {
		acepto = true;
		int[] selectedIndices = getCheckBoxList().getSelectedIndices();
		for(int index : selectedIndices) {
			impuestosSelectedResult.add((ImpuestoItemProveedor)getCheckBoxList().getItemAt(index));
		}
		dispose();
	}

	private CLCheckBoxList<ImpuestoItemProveedor> getCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new CLCheckBoxList<ImpuestoItemProveedor>();
		}
		return checkBoxList;
	}

	private ImpuestoItemProveedorFacadeRemote getImpuestoFacadeRemote() {
		if(impuestoFacadeRemote == null) {
			impuestoFacadeRemote = GTLBeanFactory.getInstance().getBean2(ImpuestoItemProveedorFacadeRemote.class);
		}
		return impuestoFacadeRemote;
	}

	public List<ImpuestoItemProveedor> getImpuestosSelectedResult() {
		return impuestosSelectedResult;
	}

	public boolean isAcepto() {
		return acepto;
	}

	private JButton getBtnAgregarImpuesto() {
		if(btnAgregarImpuesto == null) {
			btnAgregarImpuesto = BossEstilos.createButton("ar/clarin/fwjava/imagenes/b_agregar_fila.png", "ar/clarin/fwjava/imagenes/b_agregar_fila_des.png");
			btnAgregarImpuesto.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JDialogCargaImpuestoItemProveedor dialog = new JDialogCargaImpuestoItemProveedor(owner);
					GuiUtil.centrar(dialog);
					dialog.setVisible(true);
					ImpuestoItemProveedor iip = dialog.getImpuestoItemProveedor();
					if(iip != null) {
						allImpuestos.add(iip);
						List<ImpuestoItemProveedor> tmpImpuestosSelected = new ArrayList<ImpuestoItemProveedor>();
						int[] selectedIndices = getCheckBoxList().getSelectedIndices();
						for(int i : selectedIndices) {
							tmpImpuestosSelected.add((ImpuestoItemProveedor)getCheckBoxList().getItemAt(i));
						}
						getCheckBoxList().setValues(allImpuestos.toArray(new ImpuestoItemProveedor[allImpuestos.size()]));
						for(ImpuestoItemProveedor impuesto : tmpImpuestosSelected) {
							getCheckBoxList().setSelectedValue(impuesto, false);
						}
					}
				}

			});
		}
		return btnAgregarImpuesto;
	}

}