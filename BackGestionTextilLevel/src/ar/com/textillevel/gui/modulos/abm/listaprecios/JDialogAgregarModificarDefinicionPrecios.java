package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogAgregarModificarDefinicionPrecios extends JDialog {

	private static final long serialVersionUID = 4298103239770904005L;

	private PanelTablaRangos tablaRangos;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLJTextField txtTipoProducto;

	private ETipoProducto tipoProducto;
	private boolean acepto;
	private DefinicionPrecio definicion;

	public JDialogAgregarModificarDefinicionPrecios(Frame padre, ETipoProducto tipoProducto) {
		super(padre);
		setDefinicion(new DefinicionPrecio());
		setTipoProducto(tipoProducto);
		setUpComponentes();
		setUpScreen();
	}
	
	public JDialogAgregarModificarDefinicionPrecios(Frame padre, ETipoProducto tipoProducto, DefinicionPrecio defincion) {
		this(padre, tipoProducto);
		setDefinicion(defincion);
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar definición de precios para - " + getTipoProducto().getDescripcion().toUpperCase());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(600, 600));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelNorte.add(new JLabel("Tipo de producto: "));
		panelNorte.add(getTxtTipoProducto());
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		
		add(panelNorte, BorderLayout.NORTH);
		add(getTablaRangos(), BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
	}

	private JDialogAgregarModificarRangoAncho getDialogoAgregarModificarRangoAncho(boolean isModificar) {
		int selectedRow = getTablaRangos().getTabla().getSelectedRow();
		RangoAncho rangoElegido = selectedRow == -1 ? null : getTablaRangos().getElemento(selectedRow);
		if (getTipoProducto() == ETipoProducto.TENIDO){
			if (isModificar) {
				return new JDialogAgregarModificarRangoAnchoTenido(JDialogAgregarModificarDefinicionPrecios.this, getDefinicion().getRangos(), rangoElegido);
			}
			return new JDialogAgregarModificarRangoAnchoTenido(JDialogAgregarModificarDefinicionPrecios.this, getDefinicion().getRangos());
		}
		if (getTipoProducto() == ETipoProducto.ESTAMPADO){
			if (isModificar) {
				return new JDialogAgregarModificarRangoAnchoEstampado(JDialogAgregarModificarDefinicionPrecios.this, getDefinicion().getRangos(), rangoElegido);
			}
			return new JDialogAgregarModificarRangoAnchoEstampado(JDialogAgregarModificarDefinicionPrecios.this, getDefinicion().getRangos());
		}
		if (isModificar) {
			return new JDialogAgregarModificarRangoAnchoComun(JDialogAgregarModificarDefinicionPrecios.this, getDefinicion().getRangos(), rangoElegido);
		}
		return new JDialogAgregarModificarRangoAnchoComun(JDialogAgregarModificarDefinicionPrecios.this, getDefinicion().getRangos());
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public DefinicionPrecio getDefinicion() {
		return definicion;
	}

	public void setDefinicion(DefinicionPrecio definicion) {
		this.definicion = definicion;
	}

	private class PanelTablaRangos extends PanelTabla<RangoAncho> {

		private static final long serialVersionUID = -8601564676068790417L;

		private static final int CANT_COLS = 3;
		private static final int COL_ANCHO_MINIMO = 0;
		private static final int COL_ANCHO_MAXIMO = 1;
		private static final int COL_OBJ = 2;
		
		public PanelTablaRangos() {
			agregarBotonModificar();
			setBorder(BorderFactory.createTitledBorder("Anchos"));
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_ANCHO_MINIMO, "ANCHO DESDE", 200, 200, true);
			tabla.setStringColumn(COL_ANCHO_MAXIMO, "ANCHO HASTA", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_ANCHO_MINIMO, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_ANCHO_MAXIMO, CLJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected void agregarElemento(RangoAncho elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ANCHO_MINIMO] = GenericUtils.getDecimalFormat().format(elemento.getAnchoMinimo());
			row[COL_ANCHO_MAXIMO] = GenericUtils.getDecimalFormat().format(elemento.getAnchoMaximo());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);		
		}

		@Override
		protected RangoAncho getElemento(int fila) {
			return (RangoAncho) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarRangoAncho dialog = getDialogoAgregarModificarRangoAncho(false);
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getDefinicion().getRangos().add(dialog.getRango());
				refrescarTabla();
			}
			return false;
		}
		
		private void refrescarTabla() {
			getTabla().removeAllRows();
			agregarElementos(getDefinicion().getRangos());
		}

		@Override
		public boolean validarQuitar() {
			return true;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogAgregarModificarRangoAncho dialog = getDialogoAgregarModificarRangoAncho(true);
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getDefinicion().getRangos().set(filaSeleccionada, dialog.getRango());
				refrescarTabla();
			}
		}
	}

	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Alta de cheque");
		if (ret == CLJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}

	public PanelTablaRangos getTablaRangos() {
		if (tablaRangos == null) {
			tablaRangos = new PanelTablaRangos();
		}
		return tablaRangos;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public CLJTextField getTxtTipoProducto() {
		if (txtTipoProducto == null) {
			txtTipoProducto = new CLJTextField(getTipoProducto().getDescripcion().toUpperCase());
			txtTipoProducto.setEditable(false);
			txtTipoProducto.setPreferredSize(new Dimension(300, 20));
			txtTipoProducto.setSize(new Dimension(300, 20));
		}
		return txtTipoProducto;
	}

	public ETipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(ETipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
}
