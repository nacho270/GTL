package ar.com.textillevel.gui.acciones;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Color;
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.util.GTLBeanFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class JDialogSeleccionarRemitoEntrada extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JTextField txtRazSoc;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private FWJTable tablaODTs;
	private FWJTable tablaDibujos;
	private JTabbedPane tabbedPane;
	private JPanel panReferenciaRemExternos;
	private Cliente cliente;
	private List<OrdenDeTrabajo> odtSelectedList;
	private List<DibujoEstampado> dibujoEstampadoElegidos;
	private RemitoEntradaBusinessDelegate remitoBusinessDelegate = new RemitoEntradaBusinessDelegate();
	private Multimap<Integer, Integer> mapaRemitoFilas = TreeMultimap.create();
	private Multimap<Integer, Integer> mapaFilasRemito = TreeMultimap.create();

	public JDialogSeleccionarRemitoEntrada(Frame owner, Cliente cliente) {
		super(owner);
		this.cliente = cliente;
		this.odtSelectedList = new ArrayList<OrdenDeTrabajo>();
		setModal(true);
		setSize(new Dimension(550, 550));
		setTitle("Seleccionar Órdenes de Trabajo");
		construct();
		llenarTablaODTs();
		llenarTablaDibujos();
		if(!GenericUtils.isSistemaTest()) {
			getPanReferenciaRemitosExternos().setVisible(false);
		}
	}

	private void llenarTablaDibujos() {
		List<DibujoEstampado> dibujos = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).getAllByEstadoYCliente(EEstadoDibujo.EN_STOCK, cliente);
		getTablaDibujos().setNumRows(0);
		if(dibujos == null) {
			return;
		}
		int row = 0;
		for(DibujoEstampado d : dibujos) {
			getTablaDibujos().addRow();
			getTablaDibujos().setValueAt(d.toString(), row, 0);
			getTablaDibujos().setValueAt(d, row, 1);
			row ++;
		}
	}

	private void llenarTablaODTs() {
		List<DetallePiezaRemitoEntradaSinSalida> infoPiezas;
		try {
			infoPiezas = remitoBusinessDelegate.getInfoPiezasEntradaSinSalidaByClient(cliente.getId());
			getTablaOdts().setNumRows(0);
			int row = 0;

			for(DetallePiezaRemitoEntradaSinSalida ip : infoPiezas) {
				mapaRemitoFilas.put(ip.getNroRemito(), row);
				mapaFilasRemito.put(row, ip.getNroRemito());
				getTablaOdts().addRow();
				getTablaOdts().setValueAt(ip.toString(), row, 0);
				getTablaOdts().setValueAt(ip, row, 1);
				
				if(ip.isNoLocales()) {
					getTablaOdts().setBackgroundCell(row, 0, Color.GREEN.darker());
				}

				row ++;
			}
		} catch (RemoteException e) {
			FWJOptionPane.showErrorMessage(JDialogSeleccionarRemitoEntrada.this, "No se pudo establecer comunicacion con " + System.getProperty("textillevel.ipintercambio"), "Error");
			e.printStackTrace();
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
			JPanel pnlOdt = new JPanel(new GridBagLayout());
			JScrollPane scrollPane = new JScrollPane(getTablaOdts());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Ordenes de Trabajo"));
			pnlOdt.add(scrollPane, createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
			pnlOdt.add(getPanReferenciaRemitosExternos(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
			getTabbedPane().addTab("ODT", pnlOdt);
			
			JPanel pnlDibujos = new JPanel();
			JScrollPane scrollPaneDibujos = new JScrollPane(getTablaDibujos());
			scrollPaneDibujos.setBorder(BorderFactory.createTitledBorder("Dibujos"));
			pnlDibujos.add(scrollPaneDibujos, createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));

			getTabbedPane().addTab("Dibujos", pnlDibujos);
			panDetalle.add(getTabbedPane(),  createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private JPanel getPanReferenciaRemitosExternos() {
		if(panReferenciaRemExternos == null) {
			panReferenciaRemExternos = new JPanel();
			panReferenciaRemExternos.setLayout(new FlowLayout());
			panReferenciaRemExternos.add(new JLabel("Remitos Externos"));
			panReferenciaRemExternos.setBackground(Color.GREEN.darker());
		}
		return panReferenciaRemExternos;
	}

	private FWJTable getTablaDibujos() {
		if(tablaDibujos == null) {
			tablaDibujos = new FWJTable(0, 2);
			tablaDibujos.setStringColumn(0, "Dibujo", 460, 460, true);
			tablaDibujos.setStringColumn(1, "", 0, 0, true);
			tablaDibujos.setAlignment(0, FWJTable.CENTER_ALIGN);
		}
		return tablaDibujos;
	}

	private FWJTable getTablaOdts() {
		if(tablaODTs == null) {
			tablaODTs = new FWJTable(0, 2);
			tablaODTs.setStringColumn(0, "ODT", 460, 460, true);
			tablaODTs.setStringColumn(1, "", 0, 0, true);
			tablaODTs.setAlignment(0, FWJTable.CENTER_ALIGN);
			tablaODTs.addMouseListener(new MouseAdapter () {

				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						handleSeleccionODT();
					}
				}
			});
		}
		return tablaODTs;
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
					if(getTabbedPane().getSelectedIndex() == 0) {
						int[] selectedRows = getTablaOdts().getSelectedRows();
						if(selectedRows.length == 0){
							return;
						}
						List<Integer> selectedRowsList = new ArrayList<Integer>();
						for(int r : selectedRows) {
							selectedRowsList.add(r);
						}
						List<DetallePiezaRemitoEntradaSinSalida> ids = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
						/*
						if(GenericUtils.isSistemaTest()) {
							for(int row : selectedRows) {
								Collection<Integer> idsRemitos = mapaFilasRemito.get(row);
								for(int idR : idsRemitos) {
									Collection<Integer> filasRemito = mapaRemitoFilas.get(idR);
									if(GenericUtils.restaConjuntosOrdenada(filasRemito, selectedRowsList).size() > 0) {
										FWJOptionPane.showErrorMessage(JDialogSeleccionarRemitoEntrada.this, "Solo puede dar salida a remitos completos."
												+ "\nEl remito de la fila " + (row + 1) + " no ha sido seleccionado en su totalidad.", "Error");
										return;
									}
								}
							}
						}
						*/
						for(int selectedRow : selectedRows) {
							ids.add((DetallePiezaRemitoEntradaSinSalida)getTablaOdts().getValueAt(selectedRow, 1));
						}
						try {
							odtSelectedList.addAll(remitoBusinessDelegate.getODTByIdsEager(ids));
						} catch (RemoteException e1) {
							FWJOptionPane.showErrorMessage(JDialogSeleccionarRemitoEntrada.this, "No se pudo establecer comunicacion con " + System.getProperty("textillevel.ipintercambio"), "Error");
							e1.printStackTrace();
						}
						dispose();
					} else {
						int[] selectedRows = getTablaDibujos().getSelectedRows();
						if(selectedRows.length == 0){
							return;
						}
						dibujoEstampadoElegidos = Lists.newArrayList();
						for(int i =0; i<selectedRows.length; i++) {
							dibujoEstampadoElegidos.add((DibujoEstampado) getTablaDibujos().getValueAt(selectedRows[i], 1));
						}
						dispose();
					}
				}
			});
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

	public JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
		}
		return tabbedPane;
	}

	public List<DibujoEstampado> getDibujoEstampadoElegidos() {
		return dibujoEstampadoElegidos;
	}
}