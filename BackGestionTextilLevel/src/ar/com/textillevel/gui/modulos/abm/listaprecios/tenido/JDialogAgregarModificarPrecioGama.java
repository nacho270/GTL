package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioGama;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarPrecioGama extends JDialog {

	private static final long serialVersionUID = -104927715670261857L;

	private JComboBox cmbTipoArticulo;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private PanelTablaPrecioGama tablaPrecios;

	private TipoArticuloFacadeRemote tipoArticuloFacade;

	public JDialogAgregarModificarPrecioGama(Dialog owner) {
		super(owner);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar precios por gama");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(600, 600));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelNorte.add(new JLabel("Tipo de artículo: "));
		panelNorte.add(getCmbTipoArticulo());
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		
		add(panelNorte, BorderLayout.NORTH);
		add(getTablaPrecios(), BorderLayout.CENTER);
		add(panelSur, BorderLayout.SOUTH);
		
	}

	public JComboBox getCmbTipoArticulo() {
		if (cmbTipoArticulo == null) {
			cmbTipoArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoArticulo, getTipoArticuloFacade().getAllTipoArticulos(), true);
		}
		return cmbTipoArticulo;
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

	private void salir() {
		int ret = CLJOptionPane.showQuestionMessage(this, "Va a salir sin grabar, desea continuar?", "Alta de cheque");
		if (ret == CLJOptionPane.YES_OPTION) {
			dispose();
		}
	}
	
	public TipoArticuloFacadeRemote getTipoArticuloFacade() {
		if (tipoArticuloFacade == null) {
			tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
		}
		return tipoArticuloFacade;
	}
	
	private class PanelTablaPrecioGama extends PanelTabla<PrecioGama> {

		private static final long serialVersionUID = -6751878646615280690L;

		private static final int CANT_COLS = 3;
		private static final int COL_GAMA = 0;
		private static final int COL_PRECIO = 1;
		private static final int COL_OBJ = 2;
		
		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_GAMA, "GAMA", 200, 200, true);
			tabla.setFloatColumn(COL_PRECIO, "PRECIO", 120, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setHeaderAlignment(COL_GAMA, CLJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_PRECIO, CLJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected void agregarElemento(PrecioGama elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_GAMA] = elemento.getGamaCliente().getNombre();
			row[COL_PRECIO] = elemento.getPrecio();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);	
		}

		@Override
		protected PrecioGama getElemento(int fila) {
			return (PrecioGama) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
	}

	public PanelTablaPrecioGama getTablaPrecios() {
		if (tablaPrecios == null) {
			tablaPrecios = new PanelTablaPrecioGama();
		}
		return tablaPrecios;
	}
}
