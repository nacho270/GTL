package ar.com.textillevel.gui.acciones;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

@SuppressWarnings("unused")
public class JDialogSeleccionarRemitoEntrada extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JTextField txtRazSoc;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private CLJTable tablaODTs;
	private Cliente cliente;
	private Frame owner;
	private OrdenDeTrabajoFacadeRemote odtFacade;
	private List<OrdenDeTrabajo> odtSelectedList;
	
	public JDialogSeleccionarRemitoEntrada(Frame owner, Cliente cliente) {
		super(owner);
		this.owner = owner;
		this.cliente = cliente;
		this.odtSelectedList = new ArrayList<OrdenDeTrabajo>();
		this.odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		setModal(true);
		setSize(new Dimension(520, 550));
		setTitle("Seleccionar �rdenes de Trabajo");
		construct();
		llenarTablaODTs();
	}

	private void llenarTablaODTs() {
		/*
		List<OrdenDeTrabajo> odtList = odtFacade.getOdtNoAsociadasByClient(cliente.getId());
		getTablaOdts().setNumRows(0);
		int row = 0;
		for(OrdenDeTrabajo odt : odtList) {
			getTablaOdts().addRow();
			getTablaOdts().setValueAt(odt.toString(), row, 0);
			getTablaOdts().setValueAt(odt, row, 1);
			row ++;
		}
		*/

		List<DetallePiezaRemitoEntradaSinSalida> infoPiezas = odtFacade.getInfoPiezasEntradaSinSalidaByClient(cliente.getId());
		getTablaOdts().setNumRows(0);
		int row = 0;
		for(DetallePiezaRemitoEntradaSinSalida ip : infoPiezas) {
			getTablaOdts().addRow();
			getTablaOdts().setValueAt(ip.toString(), row, 0);
			getTablaOdts().setValueAt(ip.getIdODT(), row, 1);
			row ++;
		}
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel(" RAZON SOCIAL:"), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtRazSoc(), createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaOdts());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Ordenes de Trabajo"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 1));
		}
		return panDetalle;
	}

	private CLJTable getTablaOdts() {
		if(tablaODTs == null) {
			tablaODTs = new CLJTable(0, 2) {

				private static final long serialVersionUID = -2960448130069418277L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaODTs.setStringColumn(0, "ODT", 460, 460, true);
			tablaODTs.setStringColumn(1, "", 0, 0, true);
			tablaODTs.setAlignment(0, CLJTable.CENTER_ALIGN);
			tablaODTs.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionODT();
					}
				}

			});
		}
		return tablaODTs;
	}

	protected void llenarTabla(List<Cliente> clienteList) {
		getTablaOdts().setNumRows(0);
		int row = 0;
		for(Cliente c : clienteList) {
			getTablaOdts().addRow();
			getTablaOdts().setValueAt(c.getRazonSocial(), row, 0);
			getTablaOdts().setValueAt(c, row, 1);
			row ++;
		}
	}

	private JTextField getTxtRazSoc() {
		if(txtRazSoc == null) {
			txtRazSoc = new JTextField();
			txtRazSoc.setEditable(false);
			txtRazSoc.setText(cliente.getRazonSocial());
		}
		return txtRazSoc;
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
					odtSelectedList.clear();
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
					int[] selectedRows = getTablaOdts().getSelectedRows();
					for(int selectedRow : selectedRows) {
						Integer  idODT = (Integer)getTablaOdts().getValueAt(selectedRow, 1);
						odtSelectedList.add(odtFacade.getByIdEager(idODT));
					}
					dispose();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionODT() {
//		int selectedRow = getTablaOdts().getSelectedRow();
//		RemitoEntrada remitoEntrada = (RemitoEntrada)getTablaOdts().getValueAt(selectedRow, 1);
//		remitoEntrada = remitoEntradaFacade.getByIdEager(remitoEntrada.getId());
//		JDialogAgregarRemitoEntrada dialogAgregarRemitoEntrada = new JDialogAgregarRemitoEntrada(owner, remitoEntrada, true);
//		GuiUtil.centrar(dialogAgregarRemitoEntrada);
//		dialogAgregarRemitoEntrada.setVisible(true);
		//TODO: Hacer dialogo con la vista de la ODT
	}

	public List<OrdenDeTrabajo> getOdtList() {
		return odtSelectedList;
	}

}