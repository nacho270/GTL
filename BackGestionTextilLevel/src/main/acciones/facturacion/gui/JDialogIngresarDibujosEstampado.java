package main.acciones.facturacion.gui;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.BorderLayout;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.ItemDibujoRemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.gui.acciones.FacturaLinkableLabel;
import ar.com.textillevel.gui.modulos.dibujos.gui.JDialogAgregarModificarDibujoEstampado;
import ar.com.textillevel.gui.util.NroDibujoEstampadoTracker;

public class JDialogIngresarDibujosEstampado extends JDialog {

	private static final long serialVersionUID = -6422206579879882702L;

	private JPanel panelBotones;
	private JPanel panDetalle;
	private FWJNumericTextField txtCantCilindros;
	private PanelTablaDibujo panelTablaDibujo;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private boolean acepto = false;
	private boolean consulta;
	private Frame padre;
	private NroDibujoEstampadoTracker nroDibujoTracker;
	private Integer cantCilindrosActual;
	private RemitoEntradaDibujo remitoEntradaDibujo;
	
	private JPanel panREAndFactura;
	private FacturaLinkableLabel facturaLinkeableLabel;

	public JDialogIngresarDibujosEstampado(Frame padre, RemitoEntradaDibujo remitoEntradaDibujo, Integer cantCilindrosInicial) {
		super(padre, true);
		this.padre = padre;
		this.remitoEntradaDibujo = remitoEntradaDibujo;
		if(cantCilindrosInicial != null) {
			this.cantCilindrosActual = cantCilindrosInicial;
			getTxtCantCilindros().setText(cantCilindrosInicial.toString());
		}
		setUpComponentes();
		setUpScreen();
		cargarDatos();
	}

	//para modo consulta (no hay edición)
	public JDialogIngresarDibujosEstampado(Frame frame, RemitoEntradaDibujo red) {
		this(frame, red, red.calcCantCilindros());
		this.consulta = true;
		getPanelTablaDibujo().setModoConsulta(true);
		getBtnAceptar().setVisible(false);
		setTitle("Remito de Entrada de Dibujos");
		getPanREAndFactura().setVisible(true);
	}

	private void setUpScreen() {
		setSize(new Dimension(545, 460));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Ingresar Dibujos");
		GuiUtil.centrar(this);
		setResizable(true);
		setModal(true);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				salir();
			}
		});

		add(getPanDetalle(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private void cargarDatos() {
		getPanelTablaDibujo().agregarElementos(remitoEntradaDibujo.getDibujos());
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("CANTIDAD DE CILINDROS:"), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCantCilindros(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			if(remitoEntradaDibujo.getFactura() == null) {
				panDetalle.add(getPanelTablaDibujo(), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 2, 1, 1, 0.5));
			} else {
				panDetalle.add(getPanREAndFactura(), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
				panDetalle.add(getPanelTablaDibujo(), createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 2, 1, 1, 0.5));
			}
		}
		return panDetalle;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnCancelar());
		}
		return panelBotones;
	}

	private void salir() {
		if (!isConsulta()) {
			int ret = FWJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Alta de dibujo");
			if (ret == FWJOptionPane.YES_OPTION) {
				setAcepto(false);
				dispose();
			}
		} else {
			dispose();
		}
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private PanelTablaDibujo getPanelTablaDibujo() {
		if (panelTablaDibujo == null) {
			panelTablaDibujo = new PanelTablaDibujo();
			panelTablaDibujo.setBorder(BorderFactory.createTitledBorder("DIBUJOS"));
		}
		return panelTablaDibujo;
	}

	private boolean validar() {
		if (getPanelTablaDibujo().getTabla().getRowCount() == 0) {
			FWJOptionPane.showErrorMessage(JDialogIngresarDibujosEstampado.this, "Debe ingresar al menos un dibujo.", "Advertencia");
			return false;
		}
		Integer cantCilindros = getTxtCantCilindros().getValue();
		if(!cantCilindros.equals(remitoEntradaDibujo.calcCantCilindros())) {
			FWJOptionPane.showErrorMessage(JDialogIngresarDibujosEstampado.this, "Debe ingresar todos los dibujos. La cantidad de cilindros ingresada es de " + cantCilindros, "Advertencia");
			return false;
		}
		return true;
	}

	private FWJNumericTextField getTxtCantCilindros() {
		if (txtCantCilindros == null) {
			txtCantCilindros = new FWJNumericTextField(1, 10);
			txtCantCilindros.setEditable(false);
		}
		return txtCantCilindros;
	}

	private class PanelTablaDibujo extends PanelTabla<DibujoEstampado> {

		private static final long serialVersionUID = -5350962120083656924L;

		private static final int CANT_COLS = 3;
		private static final int COL_DIBUJO = 0;
		private static final int COL_CANT_CILINDROS = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaDibujo() {
		}

		@Override
		protected void agregarElemento(DibujoEstampado elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DIBUJO] = elemento.toString();
			row[COL_CANT_CILINDROS] = elemento.getCantidadColores();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaVariante = new FWJTable(0, CANT_COLS);
			tablaVariante.setStringColumn(COL_DIBUJO, "Dibujo", 350, 350, true);
			tablaVariante.setMultilineColumn(COL_CANT_CILINDROS, "Cilindro/Colores", 100, true);
			tablaVariante.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaVariante.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						botonModificarPresionado(getTabla().getSelectedRow());
					}
				}

			});
			return tablaVariante;
		}

		@Override
		protected DibujoEstampado getElemento(int fila) {
			return (DibujoEstampado) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			DibujoEstampado de = null;
			JDialogAgregarModificarDibujoEstampado dialog = new JDialogAgregarModificarDibujoEstampado(JDialogIngresarDibujosEstampado.this.padre, getCantCilindrosActual(), null, getNroDibujoTracker());
			dialog.seleccionDibujoExistente(remitoEntradaDibujo.getCliente(), remitoEntradaDibujo.getDibujosPersited());
			if(!dialog.isAcepto()) {
				dialog.setVisible(true);
				if (dialog.isAcepto()) {
					de = dialog.getDibujoActual();
				}
			} else {
				de = dialog.getDibujoActual();
			}
			if(de != null) {
				ItemDibujoRemitoEntrada item = new ItemDibujoRemitoEntrada();
				item.setDibujo(de);
				remitoEntradaDibujo.getItems().add(item);
				decCantClindrosActual(de.getCantidadColores());
				getNroDibujoTracker().putNro(de.getNroDibujo());
				refresh();
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			DibujoEstampado dibujoEliminar = remitoEntradaDibujo.getItems().get(selectedRow).getDibujo();
			incCantClindrosActual(dibujoEliminar.getCantidadColores());
			getNroDibujoTracker().removeNro(dibujoEliminar.getNroDibujo());
			remitoEntradaDibujo.getItems().remove(selectedRow);
			refresh();
			return true;
		}

		private void refresh() {
			getTabla().setNumRows(0);
			agregarElementos(remitoEntradaDibujo.getDibujos());
		}

	}

	public boolean isAcepto() {
		return acepto;
	}

	public boolean isConsulta() {
		return consulta;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	private Integer getCantCilindrosActual() {
		if(cantCilindrosActual == null) {
			cantCilindrosActual = getTxtCantCilindros().getValue();
		}
		return cantCilindrosActual;
	}
	
	private void decCantClindrosActual(Integer cant) {
		cantCilindrosActual = getCantCilindrosActual() - cant;
	}

	private void incCantClindrosActual(Integer cant) {
		cantCilindrosActual = getCantCilindrosActual() + cant;
	}
	
	private NroDibujoEstampadoTracker getNroDibujoTracker() {
		if(this.nroDibujoTracker == null) {
			this.nroDibujoTracker = new NroDibujoEstampadoTracker();
		}
		return nroDibujoTracker;
	}

	private FacturaLinkableLabel getFacturaLinkeableLabel() {
		if(facturaLinkeableLabel == null) {
			facturaLinkeableLabel = new FacturaLinkableLabel();
			if(this.remitoEntradaDibujo.getFactura() != null) {
				facturaLinkeableLabel.setFactura(this.remitoEntradaDibujo.getFactura());
			}
		}
		return facturaLinkeableLabel;
	}

	private JPanel getPanREAndFactura() {
		if(panREAndFactura == null) {
			panREAndFactura = new JPanel(new FlowLayout());
			panREAndFactura.add(new JLabel("Factura: "));
			panREAndFactura.add(getFacturaLinkeableLabel());
			panREAndFactura.setVisible(false);
		}
		return panREAndFactura;
	}

}