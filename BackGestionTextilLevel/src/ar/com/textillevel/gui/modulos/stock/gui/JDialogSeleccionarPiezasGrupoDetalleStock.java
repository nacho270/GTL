package ar.com.textillevel.gui.modulos.stock.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;
import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.GrupoDetallePiezasFisicasTO;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarPiezasGrupoDetalleStock extends JDialog {

	private static final long serialVersionUID = 6330386046553424292L;

	private ETipoTela tipoTela;

	private PanelTablaDetallesPiezas panelTabla;
	private JButton btnAceptar;
	private JButton btnSeleccionarTodos;
	private JButton btnSeleccionarNinguno;
	private JPanel panelSur;

	private GrupoDetallePiezasFisicasTO grupo;
	private List<DetallePiezaFisicaTO> piezasElegidas;

	private boolean acepto;

	public JDialogSeleccionarPiezasGrupoDetalleStock(Dialog padre, GrupoDetallePiezasFisicasTO grupo, ETipoTela tipoTela) {
		super(padre);
		setTipoTela(tipoTela);
		setGrupo(grupo);
		setPiezasElegidas(new ArrayList<DetallePiezaFisicaTO>());
		getPiezasElegidas().addAll(grupo.getPiezasSeleccionadas());
		setUpComponentes();
		setUpScreen();
		llenarTabla();
	}

	private void llenarTabla() {
		getPanelTabla().getTabla().removeAllRows();
		for (DetallePiezaFisicaTO det : getGrupo().getPiezasTotales()) {
			getPanelTabla().agregarElemento(det);
		}
	}

	private void setUpScreen() {
		setTitle("Seleccione las piezas");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		int ancho = getTipoTela() == ETipoTela.CRUDA ? 600 : 1024;
		setSize(new Dimension(ancho, 500));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelTabla(), BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private class PanelTablaDetallesPiezas extends PanelTabla<DetallePiezaFisicaTO> {

		private static final long serialVersionUID = -7354757359956850532L;

		private static final int CANT_COLS = 7;
		private static final int COL_NRO_PIEZA = 1;
		private static final int COL_METROS_PIEZA = 2;
		private static final int COL_PROCESO = 3;
		private static final int COL_PROVEEDOR = 4;
		private static final int COL_NRO_REMITO = 5;
		private static final int COL_CHECK = 0;
		private static final int COL_OBJ = 6;

		private JButton btnConsultarRemito;

		public PanelTablaDetallesPiezas() {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
			agregarBoton(getBtnConsultarRemito());
		}

		@Override
		protected void agregarElemento(DetallePiezaFisicaTO elemento) {
			Object[] fila = new Object[CANT_COLS];
			fila[COL_NRO_PIEZA] = elemento.getNroPieza();
			fila[COL_METROS_PIEZA] = elemento.getMetros();
			fila[COL_PROCESO] = getTipoTela() == ETipoTela.CRUDA ? null : elemento.getOdt();
			fila[COL_PROVEEDOR] = elemento.getProveedor();
			fila[COL_NRO_REMITO] = elemento.getNroRemito();
			fila[COL_OBJ] = elemento;
			if(getGrupo().getPiezasSeleccionadas().contains(elemento)){
				fila[COL_CHECK]=true;
			}
			getTabla().addRow(fila);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setCheckColumn(COL_CHECK, "");
			tabla.setIntColumn(COL_NRO_PIEZA, "Nº pieza", 50, true);
			tabla.setFloatColumn(COL_METROS_PIEZA, "Metros", 55, true);
			int size = getTipoTela() == ETipoTela.CRUDA ? 0 : 400;
			tabla.setStringColumn(COL_PROCESO, "Proceso", size, size, true);
			tabla.setStringColumn(COL_PROVEEDOR, "Proveedor", 250, 250, true);
			tabla.setIntColumn(COL_NRO_REMITO, "Remito", 90, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setHeaderAlignment(COL_NRO_PIEZA, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_METROS_PIEZA, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROCESO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PROVEEDOR, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NRO_REMITO, CLJTable.CENTER_ALIGN);
			tabla.setSelectionMode(CLJTable.SINGLE_SELECTION);
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					int fila = getTabla().getSelectedRow();
					if (fila > -1) {
						if (e.getClickCount() == 1) {
							getBtnConsultarRemito().setEnabled(true);
							if (getTabla().getSelectedColumn() == COL_CHECK) {
								Object valor = getTabla().getValueAt(fila, COL_CHECK);
								if (valor == null || ((Boolean) valor) == false) {
//									getTabla().setValueAt(true, fila, COL_CHECK);
//									getPiezasElegidas().add(getElemento(fila));
									seleccionarPieza(true, fila);
								} else if (((Boolean) valor) == true) {
//									getTabla().setValueAt(false, fila, COL_CHECK);
//									getPiezasElegidas().remove(getElemento(fila));
									seleccionarPieza(false, fila);
								}
							}
						} else if (e.getClickCount() == 2) {
							getBtnConsultarRemito().doClick();
						}
					} else {
						getBtnConsultarRemito().setEnabled(false);
					}
				}
			});
			return tabla;
		}
		
		public void seleccionarPieza(boolean valor, int fila){
			getTabla().setValueAt(valor, fila, COL_CHECK);
			if(valor){
				getPiezasElegidas().add(getElemento(fila));
			}else{
				getPiezasElegidas().remove(getElemento(fila));
			}
		}

		@Override
		protected DetallePiezaFisicaTO getElemento(int fila) {
			return (DetallePiezaFisicaTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public JButton getBtnConsultarRemito() {
			if (btnConsultarRemito == null) {
				btnConsultarRemito = BossEstilos.createButton("ar/com/textillevel/imagenes/b_rechazar_cheque.png", "ar/com/textillevel/imagenes/b_rechazar_cheque_des.png");
				btnConsultarRemito.setToolTipText("Consultar remito");
				btnConsultarRemito.setEnabled(false);
				btnConsultarRemito.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						DetallePiezaFisicaTO elemento = getElemento(getTabla().getSelectedRow());
						RemitoEntrada remitoEntrada = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class).getByIdEager(elemento.getIdRemito());
						OperacionSobreRemitoEntradaHandler op = new OperacionSobreRemitoEntradaHandler(null, remitoEntrada, true);
						op.showRemitoEntradaDialog();
					}
				});
			}
			return btnConsultarRemito;
		}
	}

	public PanelTablaDetallesPiezas getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaDetallesPiezas();
		}
		return panelTabla;
	}

	public ETipoTela getTipoTela() {
		return tipoTela;
	}

	public void setTipoTela(ETipoTela tipoTela) {
		this.tipoTela = tipoTela;
	}

	public GrupoDetallePiezasFisicasTO getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoDetallePiezasFisicasTO grupo) {
		this.grupo = grupo;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getGrupo().getPiezasSeleccionadas().clear();
					getGrupo().getPiezasSeleccionadas().addAll(getPiezasElegidas());
					//FIXME:
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public List<DetallePiezaFisicaTO> getPiezasElegidas() {
		return piezasElegidas;
	}

	public void setPiezasElegidas(List<DetallePiezaFisicaTO> piezasElegidas) {
		this.piezasElegidas = piezasElegidas;
	}

	public JPanel getPanelSur() {
		if(panelSur == null){
			panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSeleccionarTodos());
			panelSur.add(getBtnSeleccionarNinguno());
		}
		return panelSur;
	}

	public JButton getBtnSeleccionarTodos() {
		if(btnSeleccionarTodos == null){
			btnSeleccionarTodos = new JButton("Seleccionar todos");
			btnSeleccionarTodos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(int i = 0; i<getPanelTabla().getTabla().getRowCount();i++){
						getPanelTabla().seleccionarPieza(true, i);
					}
				}
			});
		}
		return btnSeleccionarTodos;
	}
	
	public JButton getBtnSeleccionarNinguno() {
		if(btnSeleccionarNinguno == null){
			btnSeleccionarNinguno = new JButton("Deseleccionar todos");
			btnSeleccionarNinguno.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(int i = 0; i<getPanelTabla().getTabla().getRowCount();i++){
						getPanelTabla().seleccionarPieza(false, i);
					}
				}
			});
		}
		return btnSeleccionarNinguno;
	}
}
