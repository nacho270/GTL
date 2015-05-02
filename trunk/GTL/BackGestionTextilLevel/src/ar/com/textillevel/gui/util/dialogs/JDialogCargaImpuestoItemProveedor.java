package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;
import ar.com.textillevel.facade.api.remote.ImpuestoItemProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProvinciaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaImpuestoItemProveedor extends JDialog{

	private static final long serialVersionUID = 1386015398909377968L;

	private final ImpuestoItemProveedorFacadeRemote impuestoItemProveedorFacade;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	
	private CLJTextField txtNombreImpuesto;
	private CLJTextField txtPorcentaje;
	private JComboBox cmbTipoImpuesto;
	private JCheckBox chkAplicaEnProvincia;
	private JComboBox cmbProvincia;
	
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	
	private ImpuestoItemProveedor impuestoItemProveedor;


	
	public JDialogCargaImpuestoItemProveedor(Frame padre){
		super(padre);
		impuestoItemProveedorFacade = GTLBeanFactory.getInstance().getBean2(ImpuestoItemProveedorFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Carga de Impuesto");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 150));
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
			pnlDatos.setLayout(new GridLayout(4, 3, 1, 1));
			pnlDatos.add(new JLabel("Nombre: "));
			pnlDatos.add(getTxtNombreImpuesto());
			pnlDatos.add(new JLabel("Porcentaje: "));
			pnlDatos.add(getTxtPorcentaje());
			pnlDatos.add(new JLabel("Tipo de Impuesto:"));
			pnlDatos.add(getCmbTipoImpuesto());
			pnlDatos.add(new JLabel("Provincia"));
			pnlDatos.add(getCmbProvincia());
		}
		return pnlDatos;
	}

	private JCheckBox getChkAplicaEnProvincia() {
		if(chkAplicaEnProvincia == null) {
			chkAplicaEnProvincia = new JCheckBox("Aplica en Provincia");
		}
		return chkAplicaEnProvincia;
	}
	
	private JComboBox getCmbTipoImpuesto() {
		if(cmbTipoImpuesto == null) {
			cmbTipoImpuesto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoImpuesto, Arrays.asList(ETipoImpuesto.values()), true);
			cmbTipoImpuesto.setSelectedIndex(-1);
		}
		return cmbTipoImpuesto;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(validar()){
							ImpuestoItemProveedor impuesto = new ImpuestoItemProveedor();
							impuesto.setNombre(getTxtNombreImpuesto().getText().toUpperCase().trim());
							impuesto.setPorcDescuento(Double.valueOf(getTxtPorcentaje().getText().trim().replace(',', '.')));
							impuesto.setTipoImpuesto((ETipoImpuesto)getCmbTipoImpuesto().getSelectedItem());
							impuesto.setAplicaEnProvincia(getChkAplicaEnProvincia().isSelected());
							if(impuesto.getTipoImpuesto() == ETipoImpuesto.INGRESOS_BRUTOS) {
								impuesto.setProvincia((Provincia)getCmbProvincia().getSelectedItem());
							}
							impuestoItemProveedor = impuestoItemProveedorFacade.save(impuesto);
							CLJOptionPane.showInformationMessage(JDialogCargaImpuestoItemProveedor.this, "Los datos se han guardado correctamente", JDialogCargaImpuestoItemProveedor.this.getTitle());
							dispose();
						}
					}catch(RuntimeException re){
						BossError.gestionarError(re);
					}
				}

				private boolean validar() {
					if(getTxtNombreImpuesto().getText().trim().length() == 0){
						CLJOptionPane.showErrorMessage(JDialogCargaImpuestoItemProveedor.this, "Debe ingresar el nombre del impuesto", JDialogCargaImpuestoItemProveedor.this.getTitle());
						getTxtNombreImpuesto().requestFocus();
						return false;
					}
					String porc = getTxtPorcentaje().getText().trim();
					if(porc.length() == 0){
						getTxtPorcentaje().requestFocus();
						CLJOptionPane.showErrorMessage(JDialogCargaImpuestoItemProveedor.this, "Debe ingresar el porcentaje del impuesto", JDialogCargaImpuestoItemProveedor.this.getTitle());
						return false;
					}
					if(!GenericUtils.esNumerico(porc)) {
						getTxtPorcentaje().requestFocus();
						CLJOptionPane.showErrorMessage(JDialogCargaImpuestoItemProveedor.this, "El porcentaje debe ser numérico", JDialogCargaImpuestoItemProveedor.this.getTitle());
						return false;
					}
					if(getCmbTipoImpuesto().getSelectedIndex() == -1) {
						CLJOptionPane.showErrorMessage(JDialogCargaImpuestoItemProveedor.this, "Debe seleccionar el tipo de impuesto.", JDialogCargaImpuestoItemProveedor.this.getTitle());
						return false;
					}
					if(((ETipoImpuesto)getCmbTipoImpuesto().getSelectedItem()) == ETipoImpuesto.INGRESOS_BRUTOS && getCmbProvincia().getSelectedItem() == null) {
						CLJOptionPane.showErrorMessage(JDialogCargaImpuestoItemProveedor.this, "Debe seleccionar la provincia.", JDialogCargaImpuestoItemProveedor.this.getTitle());
						return false;
					}
					Double porcDescuento = Double.valueOf(getTxtPorcentaje().getText().trim().replace(',', '.'));
					Provincia selectedItemProvincia = (Provincia)getCmbProvincia().getSelectedItem();
					if(impuestoItemProveedorFacade.existsOtroImpuestoWithParams(null, porcDescuento, (ETipoImpuesto)getCmbTipoImpuesto().getSelectedItem(),selectedItemProvincia)) {
						CLJOptionPane.showErrorMessage(JDialogCargaImpuestoItemProveedor.this, StringW.wordWrap("Ya existe otro impuesto con el mismo porcentaje de descuento"+(selectedItemProvincia!=null?", tipo de impuesto y provincia.":" y tipo de impuesto.")), JDialogCargaImpuestoItemProveedor.this.getTitle());
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

	private CLJTextField getTxtNombreImpuesto() {
		if(txtNombreImpuesto == null){
			txtNombreImpuesto = new CLJTextField();
		}
		return txtNombreImpuesto;
	}

	private CLJTextField getTxtPorcentaje() {
		if(txtPorcentaje == null){
			txtPorcentaje = new CLJTextField();
		}
		return txtPorcentaje;
	}

	public ImpuestoItemProveedor getImpuestoItemProveedor() {
		return impuestoItemProveedor;
	}

	private JComboBox getCmbProvincia() {
		if(cmbProvincia == null){
			cmbProvincia = new JComboBox();
			GuiUtil.llenarCombo(cmbProvincia, GTLBeanFactory.getInstance().getBean2(ProvinciaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbProvincia;
	}

}
