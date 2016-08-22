package ar.com.textillevel.gui.acciones.remitosalidabajastock;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWCheckBoxListDialog;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

@SuppressWarnings("unused")
public class JDialogSelRemitoEntradaConPiezasEnStock extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private FWJTable tablaRemitosEntrada;
	private Frame owner;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private Map<RemitoEntrada, List<PiezaRemito>> resultMap;

	public JDialogSelRemitoEntradaConPiezasEnStock(Frame owner) {
		super(owner);
		this.resultMap = new HashMap<RemitoEntrada, List<PiezaRemito>>();
		this.owner = owner;
		this.remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		setModal(true);
		setSize(new Dimension(650, 550));
		setTitle("Seleccionar Piezas de Remitos No Asociadas");
		construct();
		llenarTablaRemitos();
	}

	private void llenarTablaRemitos() {
		List<RemitoEntrada> remitoEntradaList = remitoEntradaFacade.getRemitoEntradaConPiezasNoAsociadasList();
		getTablaRemitoEntrada().setNumRows(0);
		int row = 0;
		for(RemitoEntrada re : remitoEntradaList) {
			getTablaRemitoEntrada().addRow();
			getTablaRemitoEntrada().setValueAt("Nro.: " + re.getNroRemito() + " - "  + DateUtil.dateToString(re.getFechaEmision()), row, 0);
			getTablaRemitoEntrada().setValueAt(getInfoPiezas(re.getPiezas()), row, 1);
			getTablaRemitoEntrada().setValueAt(re, row, 3);
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
			tablaRemitosEntrada = new FWJTable(0, 4) {

				private static final long serialVersionUID = -2960448130069418277L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaRemitosEntrada.setStringColumn(0, "REMITO", 120, 120, true);

			tablaRemitosEntrada.setStringColumn(1, "PIEZAS DISPONIBLES", 250, 250, true);
			tablaRemitosEntrada.setStringColumn(2, "PIEZAS ELEGIDAS", 220, 220, true);

			tablaRemitosEntrada.setStringColumn(3, "", 0, 0, true);
			tablaRemitosEntrada.setAlignment(0, FWJTable.CENTER_ALIGN);
			tablaRemitosEntrada.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						int selectedRow = getTablaRemitoEntrada().getSelectedRow();
						if(selectedRow != -1) {
							RemitoEntrada re = (RemitoEntrada)getTablaRemitoEntrada().getValueAt(selectedRow, 3);
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
					resultMap = null;
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
					dispose();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	@SuppressWarnings("unchecked")
	private void handleSeleccionRemitoEntrada(RemitoEntrada re) {
		FWCheckBoxListDialog dialogo = new FWCheckBoxListDialog(owner);
		dialogo.setTitle("Seleccione las piezas");
		dialogo.setValores(re.getPiezas(), true);
		dialogo.setVisible(true);
		List<PiezaRemito> valoresSeleccionados = dialogo.getValoresSeleccionados();
		String piezasListStr = getInfoPiezas(valoresSeleccionados);
		getTablaRemitoEntrada().setValueAt(piezasListStr, getTablaRemitoEntrada().getSelectedRow(), 2);
		resultMap.put(re, valoresSeleccionados);
	}

	private String getInfoPiezas(List<PiezaRemito> valoresSeleccionados) {
		List<String> strList = new ArrayList<String>();
		for(PiezaRemito pr : valoresSeleccionados) {
			strList.add(pr.getMetros().toString());
		}
		String piezasListStr = StringUtil.getCadena(strList, " ");
		return piezasListStr;
	}

	public Map<RemitoEntrada, List<PiezaRemito>> getResult() {
		return resultMap;
	}

}