package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarPrecioMateriaPrima extends JDialog {

	private static final long serialVersionUID = 7364390484648139031L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private JComboBox cmbTipoMatPrima;
	private JComboBox cmbTipoAnilina;
	private CLCheckBoxList<PrecioMateriaPrima> checkBoxList;
	private JButton btnBuscar;
	private boolean acepto;
	private List<PrecioMateriaPrima> precioMateriaPrimaSelectedList;
	private List<PrecioMateriaPrima> allMatPrimaList;

	private JPanel pnlBotones;
	private JPanel pnlDatos;

	private TipoAnilinaFacadeRemote tipoAnilinaFacade;

	public JDialogSeleccionarPrecioMateriaPrima(Frame owner, List<PrecioMateriaPrima> allMatPrimaList) {
		super(owner);
		this.allMatPrimaList = allMatPrimaList;
		this.precioMateriaPrimaSelectedList = new ArrayList<PrecioMateriaPrima>();
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Seleccionar Materias Primas");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 550));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanelDatos(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
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

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			pnlDatos.add(new JLabel("TIPO DE MATERIA PRIMA: "), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbTipoMatPrima(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(new JLabel("TIPO DE ANILINA: "), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getCmbTipoAnilina(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.insets = new Insets(5,5,5,5);
			pnlDatos.add(getBtnBuscar(), gridBagConstraints);

			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			JScrollPane scrollPane = new JScrollPane(getClCheckBoxList());
			scrollPane.setBorder(BorderFactory.createTitledBorder("MATERIAS PRIMAS"));
			pnlDatos.add(scrollPane, gridBagConstraints);
		}
		return pnlDatos;
	}

	private JButton getBtnBuscar() {
		if(btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ETipoMateriaPrima etpmSelected = (ETipoMateriaPrima)getCmbTipoMatPrima().getSelectedItem();
					TipoAnilina taSelected = (TipoAnilina)getCmbTipoAnilina().getSelectedItem();
					List<PrecioMateriaPrima> mpMatchedList = new ArrayList<PrecioMateriaPrima>();
					getClCheckBoxList().setAllSelectedItems(false);
					for(PrecioMateriaPrima mp : allMatPrimaList) {
						boolean cumpleTipoMatPrima = etpmSelected == null || etpmSelected == mp.getMateriaPrima().getTipo();
						boolean cumpleTipoAnilina = true;
						if(cumpleTipoMatPrima && etpmSelected == ETipoMateriaPrima.ANILINA) {
							cumpleTipoAnilina = taSelected == null || ((Anilina)mp.getMateriaPrima()).getTipoAnilina().equals(taSelected);
						}
						if(cumpleTipoAnilina && cumpleTipoMatPrima) {
							mpMatchedList.add(mp);
						}
					}
					checkBoxList.setValues(mpMatchedList.toArray(new Object[mpMatchedList.size()]));
				}

			});
		}
		return btnBuscar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						dispose();
					}
				}

				private boolean validar() {
					if(getClCheckBoxList().getSelectedValues().length == 0){
						CLJOptionPane.showErrorMessage(JDialogSeleccionarPrecioMateriaPrima.this, "Debe seleccionar al menos un producto.", JDialogSeleccionarPrecioMateriaPrima.this.getTitle());
						return false;
					}
					return true;
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private CLCheckBoxList<PrecioMateriaPrima> getClCheckBoxList() {
		if(checkBoxList == null) {
			checkBoxList = new CLCheckBoxList<PrecioMateriaPrima>() {

				private static final long serialVersionUID = -8028977693425752374L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if(seleccionado) {
						PrecioMateriaPrima mp = (PrecioMateriaPrima)item;
						if(!precioMateriaPrimaSelectedList.contains(mp)) {
							precioMateriaPrimaSelectedList.add(mp);
						}
					} else {
						precioMateriaPrimaSelectedList.remove(item);
					}
				}

			};
			checkBoxList.setValues(allMatPrimaList.toArray(new Object[allMatPrimaList.size()]));
		}
		return checkBoxList;
	}

	private JComboBox getCmbTipoMatPrima() {
		if(cmbTipoMatPrima == null) {
			cmbTipoMatPrima = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoMatPrima, Arrays.asList(ETipoMateriaPrima.values()), false);
			cmbTipoMatPrima.setSelectedIndex(-1);
		}
		return cmbTipoMatPrima;
	}


	private JComboBox getCmbTipoAnilina() {
		if(cmbTipoAnilina == null) {
			cmbTipoAnilina = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoAnilina, getTipoAnilinaFacade().getAllOrderByName(), false);
			cmbTipoAnilina.setSelectedIndex(-1);
		}
		return cmbTipoAnilina;
	}

	public TipoAnilinaFacadeRemote getTipoAnilinaFacade() {
		if(tipoAnilinaFacade == null) {
			tipoAnilinaFacade = GTLBeanFactory.getInstance().getBean2(TipoAnilinaFacadeRemote.class);
		}
		return tipoAnilinaFacade;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public List<PrecioMateriaPrima> getPrecioMateriaPrimaSelectedList() {
		return precioMateriaPrimaSelectedList;
	}

}