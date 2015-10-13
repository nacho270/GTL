package ar.com.textillevel.gui.modulos.personal.abm.antiguedad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.Antiguedad;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ValorAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogAgregarModificarAntiguedadPuesto extends JDialog {

	private static final long serialVersionUID = 2323964050368544773L;

	private Antiguedad antiguedadActual;
	private List<Puesto> puestosUsados;
	private Sindicato sindicato;

	private boolean acepto;

	private JComboBox cmbPuestos;
	private PanelTablaValorAntiguedad panelTabla;
	private JButton btnAceptar;
	private JButton btnCancelar;

	public JDialogAgregarModificarAntiguedadPuesto(Frame padre, Sindicato sindicato, List<Puesto> puestosUsados) {
		super(padre);
		setAntiguedadActual(new Antiguedad());
		setPuestosUsados(puestosUsados);
		setSindicato(sindicato);
		setUpComponentes();
		setUpScreen();
		setAcepto(false);
	}

	public JDialogAgregarModificarAntiguedadPuesto(Frame padre, Sindicato sindicato, List<Puesto> puestosUsados, Antiguedad antiguedad) {
		super(padre);
		setAntiguedadActual(antiguedad);
		setPuestosUsados(puestosUsados);
		setSindicato(sindicato);
		setAcepto(false);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getPanelTabla().refreshTable();
		if(getAntiguedadActual().getPuesto()!=null){
			getCmbPuestos().addItem(getAntiguedadActual().getPuesto());
			getCmbPuestos().setSelectedItem(getAntiguedadActual().getPuesto());
		}
	}

	private void setUpScreen() {
		setTitle("Agregar antigüedad");
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(350, 400));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelTabla(), BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Puesto: "));
		panel.add(getCmbPuestos());
		return panel;
	}
	
	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public Antiguedad getAntiguedadActual() {
		return antiguedadActual;
	}

	public void setAntiguedadActual(Antiguedad antiguedadActual) {
		this.antiguedadActual = antiguedadActual;
	}

	public JComboBox getCmbPuestos() {
		if (cmbPuestos == null) {
			cmbPuestos = new JComboBox();
			List<Puesto> puestos = GTLPersonalBeanFactory.getInstance().getBean2(PuestoFacadeRemote.class).getAllByIdSindicato(getSindicato().getId());
			puestos.removeAll(getPuestosUsados());
			cmbPuestos.addItem(null);
			for (Puesto p : puestos) {
				cmbPuestos.addItem(p);
			}
			cmbPuestos.setSelectedIndex(-1);
		}
		return cmbPuestos;
	}

	private class PanelTablaValorAntiguedad extends PanelTabla<ValorAntiguedad> {

		private static final long serialVersionUID = 2557293649895742968L;

		private static final int CANT_COLS = 3;
		private static final int COL_ANIOS = 0;
		private static final int COL_VALOR = 1;
		private static final int COL_OBJ = 2;
		
		public PanelTablaValorAntiguedad() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0,CANT_COLS);
			tabla.setIntColumn(COL_ANIOS, "Años", 100, true);
			tabla.setFloatColumn(COL_VALOR, "Valor", 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(ValorAntiguedad elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ANIOS] = elemento.getAntiguedad();
			row[COL_VALOR] = elemento.getValorDefault().doubleValue();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ValorAntiguedad getElemento(int fila) {
			return (ValorAntiguedad)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarValorAntiguedad dialog = new JDialogAgregarModificarValorAntiguedad(JDialogAgregarModificarAntiguedadPuesto.this,getAntiguedadActual().getValoresAntiguedad());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getAntiguedadActual().getValoresAntiguedad().add(dialog.getValorAntiguedadActual());
				refreshTable();
			}
			return false;
		}
		
		private void refreshTable(){
			limpiar();
			for(ValorAntiguedad va : getAntiguedadActual().getValoresAntiguedad()){
				agregarElemento(va);
			}
		}

		@Override
		public boolean validarQuitar() {
			ValorAntiguedad va = getElemento(getTabla().getSelectedRow());
			getAntiguedadActual().getValoresAntiguedad().remove(va);
			refreshTable();
			return false;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogAgregarModificarValorAntiguedad dialog = new JDialogAgregarModificarValorAntiguedad(JDialogAgregarModificarAntiguedadPuesto.this,getElemento(filaSeleccionada),getAntiguedadActual().getValoresAntiguedad());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getAntiguedadActual().getValoresAntiguedad().set(filaSeleccionada,dialog.getValorAntiguedadActual());
				refreshTable();
			}
		}
	}

	public List<Puesto> getPuestosUsados() {
		return puestosUsados;
	}

	public void setPuestosUsados(List<Puesto> puestosUsados) {
		this.puestosUsados = puestosUsados;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public PanelTablaValorAntiguedad getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaValorAntiguedad();
		}
		return panelTabla;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						Puesto puestoElegido =(Puesto) getCmbPuestos().getSelectedItem();
						getAntiguedadActual().setPuesto(puestoElegido);
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getAntiguedadActual().getValoresAntiguedad().isEmpty()){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un valor para una antigüedad", "Error");
			return false;
		}
		if(getPuestosUsados().size()-1>0 && getCmbPuestos().getSelectedItem()==null){
			FWJOptionPane.showErrorMessage(this, "Debe elegir un puesto debido a que ya se han elegido otros.", "Error");
			return false;
		}
		return true;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}
}
