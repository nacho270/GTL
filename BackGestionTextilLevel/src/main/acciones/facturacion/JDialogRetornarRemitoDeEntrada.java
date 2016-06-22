package main.acciones.facturacion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWCursor;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.to.DetalleRemitoEntradaNoFacturado;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.acciones.RemitoEntradaBusinessDelegate;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogRetornarRemitoDeEntrada extends JDialog {

	private static final long serialVersionUID = 8209625959757826846L;

	private JButton btnAceptar;
	private PanelTablaRemitos panelTabla;
	
	private RemitoEntradaBusinessDelegate remitoBusinessDelegate = new RemitoEntradaBusinessDelegate();
	private RemitoEntradaFacadeRemote remitoFacade;
	
	public JDialogRetornarRemitoDeEntrada(Frame padre) {
		super(padre);
		setUpComponentes();
		setUpScreen();
		getPanelTabla().agregarElementos(getRemitoFacade().getRemitosEntradaSinFactura());
	}

	private void setUpScreen() {
		setTitle("Retornar Remito de Entrada");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(550, 550));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(new JScrollPane(getPanelTabla(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		p.add(getBtnAceptar());
		add(p, BorderLayout.SOUTH);
	}

	private class PanelTablaRemitos extends PanelTabla<DetalleRemitoEntradaNoFacturado> {

		private static final long serialVersionUID = -2734929179965024589L;

		private static final int CANT_COLS = 2;
		private static final int COL_REMITO = 0;
		private static final int COL_OBJ = 1;
		
		private JButton btnEnviar;

		public PanelTablaRemitos() {
			agregarBoton(getBtnEnviar());
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
		}

		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setMultilineColumn(COL_REMITO, "REMITO", 460, true, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_REMITO, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_REMITO, FWJTable.CENTER_ALIGN);
			tabla.setSelectionMode(FWJTable.SINGLE_SELECTION);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int selectedRow = getTabla().getSelectedRow();
					getBtnEnviar().setEnabled(selectedRow >= 0);
				}
			});
			return tabla;
		}

		@Override
		protected void agregarElemento(DetalleRemitoEntradaNoFacturado elemento) {
			getTabla().addRow(new Object[]{
					"<html><b>" + elemento.getNroRemito() + " - " + elemento.getFecha() + " - " + elemento.getCliente() + "</b><br/>" +
							elemento.getListaProductos() + "</html>",
					elemento
			});
		}

		@Override
		protected DetalleRemitoEntradaNoFacturado getElemento(int fila) {
			return (DetalleRemitoEntradaNoFacturado) getTabla().getValueAt(fila, COL_OBJ);
		}

		protected String validarElemento(int fila) {
			return null;
		}

		public JButton getBtnEnviar() {
			if (btnEnviar == null) {
				btnEnviar = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_next.png", "ar/com/textillevel/imagenes/btn_next_des.png");
				btnEnviar.setSize(20, 20);
				btnEnviar.setEnabled(false);
				btnEnviar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean ok = false;
						DetalleRemitoEntradaNoFacturado elemento = getPanelTabla().getElemento(getPanelTabla().getTabla().getSelectedRow());
						try {
							FWCursor.startWait(JDialogRetornarRemitoDeEntrada.this);
							ok = remitoBusinessDelegate.retornarRemito(elemento);
						} catch (RemoteException e1) {
							FWJOptionPane.showErrorMessage(JDialogRetornarRemitoDeEntrada.this, "No se pudo establecer comunicacion con " + System.getProperty("textillevel.ipintercambio"), "Error");
							e1.printStackTrace();
						} finally {
							FWCursor.endWait(JDialogRetornarRemitoDeEntrada.this);
						}
						if (ok) {
							remitoFacade.eliminarRemitoEntradaForzado(elemento.getId(), true);
						}
					}
				});
			}
			return btnEnviar;
		}
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public PanelTablaRemitos getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaRemitos();
		}
		return panelTabla;
	}

	public RemitoEntradaFacadeRemote getRemitoFacade() {
		if (remitoFacade == null) {
			remitoFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return remitoFacade;
	}
}
