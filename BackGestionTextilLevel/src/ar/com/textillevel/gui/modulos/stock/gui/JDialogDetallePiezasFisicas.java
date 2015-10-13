package ar.com.textillevel.gui.modulos.stock.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.acciones.facturacion.IngresoRemitoSalidaHandler;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.GrupoDetallePiezasFisicasTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogDetallePiezasFisicas extends JDialog {

	private static final long serialVersionUID = 7006951447026659542L;

	private PanelTablaDetallesPiezas panelTabla;

	private JButton btnAceptar;

	private FWJTextField txtNombreArticulo;
	private FWJTextField txtNombreTipoTela;
	private FWJTextField txtCantPiezasSeleccionadas;

	private ETipoTela tipoTelaElegida;
	private Articulo articuloElegido;
	private Frame padre;

	private PrecioMateriaPrimaFacadeRemote precioMateriaPrimaFacade;
	private Set<DetallePiezaFisicaTO> piezasSelected = new HashSet<DetallePiezaFisicaTO>();
	private Set<DetallePiezaFisicaTO> piezasEnMemoriaSet;

	public JDialogDetallePiezasFisicas(Frame padre, Articulo articulo, ETipoTela tipoTela, Set<DetallePiezaFisicaTO> piezasEnMemoriaSet) {
		super(padre);
		this.padre = padre;
		this.piezasEnMemoriaSet = piezasEnMemoriaSet;
		setArticuloElegido(articulo);
		setTipoTelaElegida(tipoTela);
		setUpComponentes();
		setUpScreen();
		getTxtNombreArticulo().setText(getArticuloElegido().getDescripcion());
		getTxtNombreTipoTela().setText(getTipoTelaElegida().getDescripcion());
		llenarTabla();
		getPanelTabla().actualizarCantidadPiezasSeleccionadas();
	}

	private void llenarTabla() {
		List<GrupoDetallePiezasFisicasTO> grupos = quitarPiezasEnMemoria(getPrecioMateriaPrimaFacade().getDetallePiezas(getArticuloElegido(), getTipoTelaElegida()));
		getPanelTabla().getTabla().removeAllRows();
		if (grupos != null) {
			for (GrupoDetallePiezasFisicasTO grupo : grupos) {
				getPanelTabla().agregarElemento(grupo);
			}
		}
	}

	private List<GrupoDetallePiezasFisicasTO> quitarPiezasEnMemoria(List<GrupoDetallePiezasFisicasTO> detallePiezas) {
		List<GrupoDetallePiezasFisicasTO> gruposRemove = new ArrayList<GrupoDetallePiezasFisicasTO>();
		for(GrupoDetallePiezasFisicasTO grupo : detallePiezas) {
			grupo.getPiezasTotales().removeAll(piezasEnMemoriaSet);
			if(grupo.getPiezasTotales().isEmpty()) {
				gruposRemove.add(grupo);
			}
		}
		detallePiezas.removeAll(gruposRemove);
		return detallePiezas;
	}

	private void setUpScreen() {
		setTitle("Detalle de piezas " + getTipoTelaElegida().getDescripcion());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		int ancho = getTipoTelaElegida() == ETipoTela.CRUDA ? 550 : 1024;
		setSize(new Dimension(ancho, 500));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelTabla(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Artículo: "));
		panel.add(getTxtNombreArticulo());
		panel.add(new JLabel("Tipo de tela: "));
		panel.add(getTxtNombreTipoTela());
		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Piezas elegidas: "));
		panel.add(getTxtCantPiezasSeleccionadas());
		panel.add(getBtnAceptar());
		return panel;
	}

	private class PanelTablaDetallesPiezas extends PanelTabla<GrupoDetallePiezasFisicasTO> {

		private static final long serialVersionUID = -7354757359956850532L;

		private static final int CANT_COLS = 5;
		private static final int COL_PROCESO = 0;
		private static final int COL_PROVEEDOR = 1;
		private static final int COL_CANT_PIEZAS_ELEGIDAS = 2;
		private static final int COL_CANT_PIEZAS = 3;
		private static final int COL_OBJ = 4;

		// private JButton btnConsultarRemito;
		private JButton btnSeleciconarPiezas;

		private JButton btnSalida01PiezasSeleccionadas;
		private JButton btnVentaPiezasSeleccionadas;

		public PanelTablaDetallesPiezas() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
			agregarBoton(getBtnSeleciconarPiezas());
			agregarBoton(getBtnSalida01PiezasSeleccionadas());
			agregarBoton(getBtnVentaPiezasSeleccionadas());
		}

		@Override
		protected void agregarElemento(GrupoDetallePiezasFisicasTO elemento) {
			Object[] fila = new Object[CANT_COLS];
			fila[COL_PROCESO] = getTipoTelaElegida() == ETipoTela.CRUDA ? null : elemento.getOdt();
			fila[COL_PROVEEDOR] = elemento.getProveedor();
			fila[COL_CANT_PIEZAS_ELEGIDAS] = elemento.getPiezasSeleccionadas().size();
			fila[COL_CANT_PIEZAS] = elemento.getPiezasTotales().size();
			fila[COL_OBJ] = elemento;
			getTabla().addRow(fila);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			int size = getTipoTelaElegida() == ETipoTela.CRUDA ? 0 : 500;
			tabla.setStringColumn(COL_PROCESO, "Proceso", size, size, true);
			tabla.setStringColumn(COL_PROVEEDOR, "Proveedor", 250, 250, true);
			tabla.setIntColumn(COL_CANT_PIEZAS_ELEGIDAS, "Piezas elegidas", 90, true);
			tabla.setIntColumn(COL_CANT_PIEZAS, "Cant. Piezas", 90, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setHeaderAlignment(COL_PROCESO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROVEEDOR, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CANT_PIEZAS_ELEGIDAS, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_CANT_PIEZAS, FWJTable.CENTER_ALIGN);
			tabla.setSelectionMode(FWJTable.SINGLE_SELECTION);
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (getTabla().getSelectedRow() > -1) {
						if (e.getClickCount() == 1) {
							getBtnSeleciconarPiezas().setEnabled(true);
						} else if (e.getClickCount() == 2) {
							getBtnSeleciconarPiezas().doClick();
						}
					} else {
						getBtnSeleciconarPiezas().setEnabled(false);
					}
				}
			});
			return tabla;
		}

		@Override
		protected GrupoDetallePiezasFisicasTO getElemento(int fila) {
			return (GrupoDetallePiezasFisicasTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public JButton getBtnSeleciconarPiezas() {
			if (btnSeleciconarPiezas == null) {
				btnSeleciconarPiezas = BossEstilos.createButton("ar/com/textillevel/imagenes/b_verificar_stock.png", "ar/com/textillevel/imagenes/b_verificar_stock_des.png");
				btnSeleciconarPiezas.setEnabled(false);
				btnSeleciconarPiezas.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						GrupoDetallePiezasFisicasTO grupo = getElemento(getTabla().getSelectedRow());
						JDialogSeleccionarPiezasGrupoDetalleStock jd = new JDialogSeleccionarPiezasGrupoDetalleStock(JDialogDetallePiezasFisicas.this, grupo,getTipoTelaElegida());
						jd.setVisible(true);
						getTabla().setValueAt(grupo.getPiezasSeleccionadas().size(), getTabla().getSelectedRow(), COL_CANT_PIEZAS_ELEGIDAS);
						actualizarCantidadPiezasSeleccionadas();
					}
				});
			}
			return btnSeleciconarPiezas;
		}

		private void actualizarCantidadPiezasSeleccionadas() {
			Integer suma = 0;
			for (int i = 0; i < getTabla().getRowCount(); i++) {
				suma += getElemento(i).getPiezasSeleccionadas().size();
			}
			getTxtCantPiezasSeleccionadas().setText(String.valueOf(suma));
			getBtnSalida01PiezasSeleccionadas().setEnabled(suma>0);
			getBtnVentaPiezasSeleccionadas().setEnabled(suma>0);
		}

		private JButton getBtnSalida01PiezasSeleccionadas() {
			if(btnSalida01PiezasSeleccionadas == null){
				btnSalida01PiezasSeleccionadas = BossEstilos.createButton("ar/com/textillevel/imagenes/b_salida.png", "ar/com/textillevel/imagenes/b_salida_des.png");
				btnSalida01PiezasSeleccionadas.setToolTipText("Salida 01");
				btnSalida01PiezasSeleccionadas.setEnabled(false);
				btnSalida01PiezasSeleccionadas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleIngresoRemitoSalida(ETipoRemitoSalida.CLIENTE_SALIDA_01); 
					}

				});
			}
			return btnSalida01PiezasSeleccionadas;
		}

		private JButton getBtnVentaPiezasSeleccionadas() {
			if(btnVentaPiezasSeleccionadas == null){
				btnVentaPiezasSeleccionadas = BossEstilos.createButton("ar/com/textillevel/imagenes/b_venta.png", "ar/com/textillevel/imagenes/b_venta_des.png");
				btnVentaPiezasSeleccionadas.setToolTipText("Salida a venta");
				btnVentaPiezasSeleccionadas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleIngresoRemitoSalida(ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA);
					}
				});
			}
			return btnVentaPiezasSeleccionadas;
		}

		private void handleIngresoRemitoSalida(ETipoRemitoSalida tipoRS) {
			List<GrupoDetallePiezasFisicasTO> elementos = getElementos();
			List<DetallePiezaFisicaTO> detallePiezasResult = new ArrayList<DetallePiezaFisicaTO>(); 
			for(GrupoDetallePiezasFisicasTO gr : elementos) {
				detallePiezasResult.addAll(gr.getPiezasSeleccionadas());
			}
			IngresoRemitoSalidaHandler rsHandler = new IngresoRemitoSalidaHandler(padre, tipoRS, false, detallePiezasResult);
			rsHandler.gestionarIngresoRemitoSalida();
			dispose();
		}
		
	}

	private PanelTablaDetallesPiezas getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaDetallesPiezas();
		}
		return panelTabla;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					for(GrupoDetallePiezasFisicasTO g : getPanelTabla().getElementos()) {
						piezasSelected.addAll(g.getPiezasSeleccionadas()); 
					}
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public Set<DetallePiezaFisicaTO> getPiezasSelected() {
		return piezasSelected;
	}

	private FWJTextField getTxtNombreArticulo() {
		if (txtNombreArticulo == null) {
			txtNombreArticulo = new FWJTextField();
			txtNombreArticulo.setPreferredSize(new Dimension(200, 20));
			txtNombreArticulo.setEditable(false);
		}
		return txtNombreArticulo;
	}

	public ETipoTela getTipoTelaElegida() {
		return tipoTelaElegida;
	}

	public void setTipoTelaElegida(ETipoTela tipoTelaElegida) {
		this.tipoTelaElegida = tipoTelaElegida;
	}

	public Articulo getArticuloElegido() {
		return articuloElegido;
	}

	public void setArticuloElegido(Articulo articuloElegido) {
		this.articuloElegido = articuloElegido;
	}

	private FWJTextField getTxtNombreTipoTela() {
		if (txtNombreTipoTela == null) {
			txtNombreTipoTela = new FWJTextField();
			txtNombreTipoTela.setPreferredSize(new Dimension(200, 20));
			txtNombreTipoTela.setEditable(false);
		}
		return txtNombreTipoTela;
	}

	private PrecioMateriaPrimaFacadeRemote getPrecioMateriaPrimaFacade() {
		if (precioMateriaPrimaFacade == null) {
			precioMateriaPrimaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		}
		return precioMateriaPrimaFacade;
	}

	private FWJTextField getTxtCantPiezasSeleccionadas() {
		if (txtCantPiezasSeleccionadas == null) {
			txtCantPiezasSeleccionadas = new FWJTextField();
			txtCantPiezasSeleccionadas.setPreferredSize(new Dimension(60, 20));
			txtCantPiezasSeleccionadas.setEditable(false);
		}
		return txtCantPiezasSeleccionadas;
	}

}
