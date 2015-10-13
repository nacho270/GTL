package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.CategoriaValorPuesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ValorPuesto;
import ar.com.textillevel.modulos.personal.facade.api.remote.CategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogCargarCategoriaValorPuesto extends JDialog {

	private static final long serialVersionUID = -3645869306074747707L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private JComboBox cmbCategoria;
	
	private JTextField txtValorHoraDefault;
	private PanelTablaValorPuesto panTablaValorPuesto;

	private Sindicato sindicato;
	private CategoriaValorPuesto catValorPuesto;
	private CategoriaFacadeRemote categoriaFacade;

	private List<Categoria> categoriasUtilizadas;

	private boolean acepto;

	public JDialogCargarCategoriaValorPuesto(Frame owner, CategoriaValorPuesto catValorPuesto, Sindicato sindicato, List<Categoria> categoriasUtilizadas) {
		super(owner);
		this.owner = owner;
		this.catValorPuesto = catValorPuesto;
		this.categoriasUtilizadas = categoriasUtilizadas;
		this.sindicato = sindicato;
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		llenarComboCategoria();
		setDatos();
	}

	private void llenarComboCategoria() {
		List<Categoria> allByIdSindicato = getCategoriaFacade().getAllByIdSindicatoEager(sindicato.getId());
		allByIdSindicato.removeAll(categoriasUtilizadas);
		if(catValorPuesto.getCategoria() != null) {
			allByIdSindicato.add(catValorPuesto.getCategoria());
		}
		GuiUtil.llenarCombo(getCmbCategoria(), allByIdSindicato, true);
	}

	private void setDatos() {
		getCmbCategoria().setSelectedItem(catValorPuesto.getCategoria());
		getTxtValorHoraDefault().setText(GenericUtils.getDecimalFormat().format(catValorPuesto.getValorHoraDefault().doubleValue()).replaceAll("\\.", ","));
		getPanTablaValorPuesto().agregarElementos(catValorPuesto.getValoresPuesto());
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Cargar Valor de Hora por Categoría y Puesto");
		setResizable(false);
		setSize(new Dimension(400, 500));
		setModal(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			panelCentral.add(new JLabel("Categoria: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbCategoria(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Valor Hora Default: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtValorHoraDefault(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getPanTablaValorPuesto(),  GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 2, 1, 1, 1));
		}
		return panelCentral;
	}

	private JPanel getPanelBotones() {
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						BigDecimal valorHora = new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtValorHoraDefault()));
						getCatValorPuesto().setValorHoraDefault(valorHora);
						getCatValorPuesto().setCategoria((Categoria)getCmbCategoria().getSelectedItem());
						getCatValorPuesto().getValoresPuesto().clear();
						getCatValorPuesto().getValoresPuesto().addAll(getPanTablaValorPuesto().getElementos());
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	public CategoriaValorPuesto getCatValorPuesto() {
		return catValorPuesto;
	}
	
	private boolean validar() {
		if(getCmbCategoria().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(owner, "Debe seleccionar una categoría.", "Error");
			return false;
		}
		String valorHoraStr = getTxtValorHoraDefault().getText();
		if(StringUtil.isNullOrEmpty(valorHoraStr)) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar un valor por hora default.", "Error");
			getTxtValorHoraDefault().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(valorHoraStr)) {
			FWJOptionPane.showErrorMessage(owner, "El valor de la hora default debe ser numérico.", "Error");
			getTxtValorHoraDefault().requestFocus();
			return false;
		}
		double valorHora = GenericUtils.getDoubleValueInJTextField(getTxtValorHoraDefault());
		if(valorHora <= 0) {
			FWJOptionPane.showErrorMessage(owner, "El valor de la hora default debe ser mayor a cero.", "Error");
			getTxtValorHoraDefault().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
	
	private JTextField getTxtValorHoraDefault() {
		if(txtValorHoraDefault == null) {
			txtValorHoraDefault = new JTextField();
		}
		return txtValorHoraDefault;
	}

	private PanelTablaValorPuesto getPanTablaValorPuesto() {
		if(panTablaValorPuesto == null) {
			panTablaValorPuesto = new PanelTablaValorPuesto();
		}
		return panTablaValorPuesto;
	}

	private JComboBox getCmbCategoria() {
		if(cmbCategoria == null) {
			cmbCategoria = new JComboBox();
			cmbCategoria.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						getPanTablaValorPuesto().limpiar();
					}
				}

			});
		}
		return cmbCategoria;
	}

	public boolean isAcepto() {
		return acepto;
	}

	private CategoriaFacadeRemote getCategoriaFacade() {
		if(categoriaFacade == null) {
			categoriaFacade = GTLPersonalBeanFactory.getInstance().getBean2(CategoriaFacadeRemote.class);
		}
		return categoriaFacade;
	}

	private class PanelTablaValorPuesto extends PanelTabla<ValorPuesto> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 3;
		private static final int COL_PUESTO = 0;
		private static final int COL_VALOR_HORA = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaValorPuesto() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_PUESTO, "PUESTO", 200, 200, true);
			tabla.setStringColumn(COL_VALOR_HORA, "VALOR HORA", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);

			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if(e.getClickCount() == 2 && !isModoConsulta() && getTabla().getSelectedRow() != -1) {
						handleActionModificar(getTabla().getSelectedRow());
					}
				}

			});

			return tabla;
		}

		private void handleActionModificar(int filaSeleccionada) {
			ValorPuesto valorPuesto = getElemento(filaSeleccionada);
			List<Puesto> puestosUtilizados = getPuestosUtilizados();
			puestosUtilizados.remove(valorPuesto.getPuesto());
			JDialogIngresarValorPuesto dialogo = new JDialogIngresarValorPuesto(JDialogCargarCategoriaValorPuesto.this.owner, (Categoria)getCmbCategoria().getSelectedItem(), puestosUtilizados, valorPuesto);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				updateElemento(filaSeleccionada, valorPuesto);
			}
		}

		private void updateElemento(int selectedRow, ValorPuesto valorPuesto) {
			getTabla().setValueAt(valorPuesto, selectedRow, COL_OBJ);
			getTabla().setValueAt(valorPuesto.getPuesto(), selectedRow, COL_PUESTO);
			getTabla().setValueAt(GenericUtils.getDecimalFormat().format(valorPuesto.getValorHora().doubleValue()), selectedRow, COL_VALOR_HORA);
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			handleActionModificar(filaSeleccionada);
		}

		@Override
		protected void agregarElemento(ValorPuesto elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PUESTO] = elemento.getPuesto();
			row[COL_VALOR_HORA] = GenericUtils.getDecimalFormat().format(elemento.getValorHora().doubleValue());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ValorPuesto getElemento(int fila) {
			return (ValorPuesto)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			Categoria catSeleccionada = (Categoria)getCmbCategoria().getSelectedItem();
			if(catSeleccionada == null) {
				FWJOptionPane.showErrorMessage(JDialogCargarCategoriaValorPuesto.this.owner, "Debe seleccionar una categoría", "Atención");
			} else {
				JDialogIngresarValorPuesto dialogo = new JDialogIngresarValorPuesto(JDialogCargarCategoriaValorPuesto.this.owner, catSeleccionada, getPuestosUtilizados(), new ValorPuesto());
				dialogo.setVisible(true);
				if(dialogo.isAcepto()) {
					ValorPuesto valorPuesto = dialogo.getValorPuesto();
					agregarElemento(valorPuesto);
				}
			}
			return false;
		}

		private List<Puesto> getPuestosUtilizados() {
			List<Puesto> puestosUtilizados = new ArrayList<Puesto>();
			for(ValorPuesto vp : getElementos()) {
				puestosUtilizados.add(vp.getPuesto());
			}
			return puestosUtilizados;
		}
	}
}