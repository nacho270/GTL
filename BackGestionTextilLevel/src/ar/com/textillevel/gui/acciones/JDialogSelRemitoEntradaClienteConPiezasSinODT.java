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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSelRemitoEntradaClienteConPiezasSinODT extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private FWJTable tablaRemitosEntrada;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private Cliente cliente;
	private RemitoEntrada remitoEntrada;
	private Frame owner;

	public JDialogSelRemitoEntradaClienteConPiezasSinODT(Frame owner, Cliente cliente) {
		super(owner);
		this.owner = owner;
		this.cliente = cliente;
		this.remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		setModal(true);
		setSize(new Dimension(560, 550));
		setTitle("Seleccionar Remito Con Piezas Sin ODT");
		construct();
		llenarTablaRemitos();
	}

	private void llenarTablaRemitos() {
		List<RemitoEntrada> remitoEntradaList = remitoEntradaFacade.getRemitoEntradaConPiezasSinODTByCliente(cliente.getId());
		getTablaRemitoEntrada().setNumRows(0);
		int row = 0;
		for(RemitoEntrada re : remitoEntradaList) {
			getTablaRemitoEntrada().addRow();
			getTablaRemitoEntrada().setValueAt("Nro.: " + re.getNroRemito() + " - "  + DateUtil.dateToString(re.getFechaEmision()), row, 0);
			getTablaRemitoEntrada().setValueAt(getInfoPiezas(re.getPiezas()), row, 1);
			getTablaRemitoEntrada().setValueAt(re, row, 2);
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
			JScrollPane scrollPane = new JScrollPane(getTablaRemitoEntrada());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Remitos"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private FWJTable getTablaRemitoEntrada() {
		if(tablaRemitosEntrada == null) {
			tablaRemitosEntrada = new FWJTable(0, 3) {

				private static final long serialVersionUID = -2960448130069418277L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaRemitosEntrada.setStringColumn(0, "REMITO", 160, 160, true);

			tablaRemitosEntrada.setStringColumn(1, "PIEZAS SIN ODT", 250, 250, true);

			tablaRemitosEntrada.setStringColumn(2, "", 0, 0, true);
			tablaRemitosEntrada.setAlignment(0, FWJTable.CENTER_ALIGN);
			tablaRemitosEntrada.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						int selectedRow = getTablaRemitoEntrada().getSelectedRow();
						if(selectedRow != -1) {
							RemitoEntrada re = (RemitoEntrada)getTablaRemitoEntrada().getValueAt(selectedRow, 2);
							handleSeleccionRemitoEntrada(re);
						}
					}
				}

			});
		}
		return tablaRemitosEntrada;
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
					remitoEntrada = null;
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
					int selectedRow = getTablaRemitoEntrada().getSelectedRow();
					if(selectedRow == -1) {
						FWJOptionPane.showErrorMessage(owner, "Debe seleccionar un remito", "Error");
					} else {
						remitoEntrada = (RemitoEntrada)getTablaRemitoEntrada().getValueAt(selectedRow, 2);
					}
					dispose();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	private void handleSeleccionRemitoEntrada(RemitoEntrada re) {
		remitoEntrada = re;
		dispose();
	}

	private String getInfoPiezas(List<PiezaRemito> valoresSeleccionados) {
		List<String> strList = new ArrayList<String>();
		for(PiezaRemito pr : valoresSeleccionados) {
			strList.add(pr.getMetros().toString());
		}
		String piezasListStr = StringUtil.getCadena(strList, " ");
		return piezasListStr;
	}

	public RemitoEntrada getRemitoEntrada() {
		return remitoEntrada;
	}

}