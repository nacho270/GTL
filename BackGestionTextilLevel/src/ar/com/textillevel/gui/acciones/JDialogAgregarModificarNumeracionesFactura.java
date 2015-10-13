package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.config.ConfiguracionNumeracionFactura;
import ar.com.textillevel.entidades.config.NumeracionFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarNumeracionesFactura extends JDialog {

	private static final long serialVersionUID = -3557792841378986149L;

	private FWJTextField txtTipoFactura;
	private FWJNumericTextField txtDiasAntesAviso;
	private FWJNumericTextField txtNrosAntesAviso;
	private PanelTablaNumeracionFactura panelTabla;
	private JButton btnAceptar;
	private JButton btnSalir;
	
	private Integer ultimoNroFacturaImpreso;

	private ConfiguracionNumeracionFactura configuracionActual;
	private boolean acepto;

	public JDialogAgregarModificarNumeracionesFactura(Dialog padre, ConfiguracionNumeracionFactura configuracion) {
		super(padre);
		setConfiguracionActual(configuracion);
		setUpComponentes();
		setUpScreen();
		refreshTable();
	}

	public JDialogAgregarModificarNumeracionesFactura(JDialogParametrosGenerales padre, ETipoFactura tipoFactura) {
		super(padre);
		setConfiguracionActual(new ConfiguracionNumeracionFactura());
		getConfiguracionActual().setTipoFactura(tipoFactura);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Configuración de numeración: Factura " + getConfiguracionActual().getTipoFactura().getDescripcion());
		setSize(new Dimension(450, 320));
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
		
		JPanel panelNorte = new JPanel(new GridBagLayout());
		panelNorte.add(new JLabel("Tipo de factura: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtTipoFactura(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelNorte.add(new JLabel("Avisar: "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtDiasAntesAviso(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(new JLabel("días antes"),GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		
		panelNorte.add(new JLabel("Avisar: "),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtNrosAntesAviso(),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(new JLabel("números antes"),GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnSalir());
		
		add(panelNorte,BorderLayout.NORTH);
		add(getPanelTabla(),BorderLayout.CENTER);
		add(panelSur,BorderLayout.SOUTH);
	}

	public FWJTextField getTxtTipoFactura() {
		if (txtTipoFactura == null) {
			txtTipoFactura = new FWJTextField();
			txtTipoFactura.setEditable(false);
			txtTipoFactura.setPreferredSize(new Dimension(50, 20));
			txtTipoFactura.setHorizontalAlignment(FWJTextField.CENTER);
			txtTipoFactura.setText(getConfiguracionActual().getTipoFactura().getDescripcion());
		}
		return txtTipoFactura;
	}

	public PanelTablaNumeracionFactura getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaNumeracionFactura();
		}
		return panelTabla;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public ConfiguracionNumeracionFactura getConfiguracionActual() {
		return configuracionActual;
	}

	public void setConfiguracionActual(ConfiguracionNumeracionFactura configuracionActual) {
		this.configuracionActual = configuracionActual;
	}

	private void refreshTable() {
		getPanelTabla().limpiar();
		getPanelTabla().agregarElementos(getConfiguracionActual().getNumeracion());
	}

	private class PanelTablaNumeracionFactura extends PanelTabla<NumeracionFactura> {

		private static final long serialVersionUID = -7846225195312807736L;

		private static final int CANT_COLS = 5;
		private static final int COL_FECHA_DESDE = 0;
		private static final int COL_FECHA_HASTA = 1;
		private static final int COL_NRO_DESDE = 2;
		private static final int COL_NRO_HASTA = 3;
		private static final int COL_OBJ= 4;
		
		public PanelTablaNumeracionFactura() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setDateColumn(COL_FECHA_DESDE, "Fecha Desde", 90, true);
			tabla.setDateColumn(COL_FECHA_HASTA, "Fecha Hasta", 90, true);
			tabla.setIntColumn(COL_NRO_DESDE, "Nro. Desde", 90, true);
			tabla.setIntColumn(COL_NRO_HASTA, "Nro. Hasta", 90, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setHeaderAlignment(COL_FECHA_DESDE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_FECHA_HASTA, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NRO_DESDE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NRO_HASTA, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_FECHA_DESDE, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_FECHA_HASTA, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_NRO_DESDE, FWJTable.CENTER_ALIGN);
			tabla.setAlignment(COL_NRO_HASTA, FWJTable.CENTER_ALIGN);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(NumeracionFactura elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA_DESDE] = elemento.getFechaDesde();
			row[COL_FECHA_HASTA] = elemento.getFechaHasta();
			row[COL_NRO_DESDE] = elemento.getNroDesde();
			row[COL_NRO_HASTA] = elemento.getNroHasta();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row); 
		}

		@Override
		protected NumeracionFactura getElemento(int fila) {
			return (NumeracionFactura)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogInputNumeracionFactura d = null;
			if(getConfiguracionActual().getNumeracion().isEmpty()){
				d = new JDialogInputNumeracionFactura(JDialogAgregarModificarNumeracionesFactura.this,DateUtil.getHoy(),getUltimoNroFacturaImpreso());
			}else{
				NumeracionFactura ultimaNumeracion = getConfiguracionActual().getNumeracion().get(getConfiguracionActual().getNumeracion().size()-1);
				d = new JDialogInputNumeracionFactura(JDialogAgregarModificarNumeracionesFactura.this,DateUtil.getManiana(ultimaNumeracion.getFechaHasta()),getUltimoNroFacturaImpreso()==null?ultimaNumeracion.getNroHasta()+1:getUltimoNroFacturaImpreso()+1){

					private static final long serialVersionUID = -4333144358100581149L;

					@Override
					protected boolean validarAdicional(NumeracionFactura numeracionFactura) {
						if(getUltimoNroFacturaImpreso() != null){
							if( numeracionFactura.getNroDesde()<getUltimoNroFacturaImpreso()){
								FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "El número 'desde' debe ser mayor al último número impreso: " + getUltimoNroFacturaImpreso(), "Error");
								return false;
							}
							return true;
						}else{
							return validarSolapamientos(numeracionFactura);
						}
					}
				};
			}
			d.setVisible(true);
			if(d.isAcepto()){
				getConfiguracionActual().getNumeracion().add(d.getNumeracionActual());
				refreshTable();
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			int fila = getTabla().getSelectedRow();
			if(fila != -1){
				if(fila != getConfiguracionActual().getNumeracion().size()){
					getConfiguracionActual().getNumeracion().remove(fila);
					refreshTable();
				}else{
					FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "Solo puede borrar la ultima numeración", "Error");
				}
			}
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogInputNumeracionFactura d = new JDialogInputNumeracionFactura(JDialogAgregarModificarNumeracionesFactura.this,getElemento(filaSeleccionada)){

				private static final long serialVersionUID = 1627770940594336613L;

				@Override
				protected boolean validarAdicional(NumeracionFactura numeracionFactura) {
					if(getUltimoNroFacturaImpreso() != null){
						if( numeracionFactura.getNroDesde()<getUltimoNroFacturaImpreso()){
							FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "El número 'desde' debe ser mayor al último número impreso: " + getUltimoNroFacturaImpreso(), "Error");
							return false;
						}
						return true;
					}else{
						return validarSolapamientos(numeracionFactura) &&  validarConsecutividad(numeracionFactura);
					}
				}
			};
			d.setVisible(true);
			if(d.isAcepto()){
				getConfiguracionActual().getNumeracion().set(filaSeleccionada,d.getNumeracionActual());
				refreshTable();
			}
		}
	}
	
	private boolean validarConsecutividad(NumeracionFactura numeracionFactura){
		List<NumeracionFactura> numeracion = getConfiguracionActual().getNumeracion();
		if(numeracion.size()>1){
			for(int i = 0; i<numeracion.size()-1;i++){
				NumeracionFactura n1 = numeracion.get(i);
				NumeracionFactura n2 = numeracion.get(i+1);
				if( (n1.getNroHasta()+1)< n2.getNroDesde()){
					FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "Los números ingresados no son consecutivos con otras numeraciones", "Error");
					return false;
				}
				if(!DateUtil.getManiana(n1.getFechaHasta()).equals(n2.getFechaDesde())){
					FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "Las fechas ingresadas no son consecutivas con otras numeraciones", "Error");
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validarSolapamientoFecha(NumeracionFactura numeracionFactura){
		for(NumeracionFactura n : getConfiguracionActual().getNumeracion()){
			if(numeracionFactura.getId() == null || !n.equals(numeracionFactura)){
				if(n.seSolapaEnFecha(numeracionFactura)){
					FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "Las fechas ingresadas se solapan con las fechas de otra numeración", "");
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validarSolapamientoEnNumeros(NumeracionFactura numeracionFactura){
		for(NumeracionFactura n : getConfiguracionActual().getNumeracion()){
			if(numeracionFactura.getId() == null || !n.equals(numeracionFactura)){
				if(n.seSolapaEnNumero(numeracionFactura)){
					FWJOptionPane.showErrorMessage(JDialogAgregarModificarNumeracionesFactura.this, "Los números ingresados se solapan con los números de otra numeración", "");
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validarSolapamientos(NumeracionFactura numeracionFactura) {
		return validarSolapamientoEnNumeros(numeracionFactura) && validarSolapamientoFecha(numeracionFactura);
	};

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(this, "Va a salir sin guardar los cambios. Desea continuar?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			List<NumeracionFactura> aRemover = new ArrayList<NumeracionFactura>();
			for(NumeracionFactura n : getConfiguracionActual().getNumeracion()){
				if(n.getId()==null){
					aRemover.add(n);
				}
			}
			getConfiguracionActual().getNumeracion().removeAll(aRemover);
			setAcepto(false);
			dispose();
		}
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						if(!getConfiguracionActual().getNumeracion().isEmpty()){
							Collections.sort(getConfiguracionActual().getNumeracion(), new Comparator<NumeracionFactura>() {
								public int compare(NumeracionFactura o1, NumeracionFactura o2) {
									return o1.getFechaDesde().compareTo(o2.getFechaHasta());
								}
							});
						}else{
							getConfiguracionActual().setNumeracion(null);
						}
						getConfiguracionActual().setDiasAntesAviso(getTxtDiasAntesAviso().getValue());
						getConfiguracionActual().setNumerosAntesAviso(getTxtNrosAntesAviso().getValue());
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar(){
		if(getTxtDiasAntesAviso().getValueWithNull() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar cuantos días antes se debe notificar del vencimiento", "Error");
			getTxtDiasAntesAviso().requestFocus();
			return false;
		}
		if(getTxtNrosAntesAviso().getValueWithNull() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar cuantos números antes se debe notificar del vencimiento", "Error");
			getTxtNrosAntesAviso().requestFocus();
			return false;
		}
		return true;
	}
	
	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}
	
	public Integer getUltimoNroFacturaImpreso() {
		if(ultimoNroFacturaImpreso == null){
			ultimoNroFacturaImpreso = GTLBeanFactory.getInstance().getBean2(FacturaFacadeRemote.class).getUltimoNumeroFacturaImpreso(getConfiguracionActual().getTipoFactura());
		}
		return ultimoNroFacturaImpreso;
	}

	
	public FWJNumericTextField getTxtDiasAntesAviso() {
		if(txtDiasAntesAviso == null){
			txtDiasAntesAviso = new FWJNumericTextField(1l, 365l);
			if(getConfiguracionActual()!=null && getConfiguracionActual().getDiasAntesAviso() != null){
				txtDiasAntesAviso.setValue(getConfiguracionActual().getDiasAntesAviso().longValue());
			}
		}
		return txtDiasAntesAviso;
	}
	
	public FWJNumericTextField getTxtNrosAntesAviso() {
		if(txtNrosAntesAviso == null){
			txtNrosAntesAviso = new FWJNumericTextField(1l, 365l);
			if(getConfiguracionActual()!=null && getConfiguracionActual().getNumerosAntesAviso() != null){
				txtNrosAntesAviso.setValue(getConfiguracionActual().getNumerosAntesAviso().longValue());
			}

		}
		return txtNrosAntesAviso;
	}
}
