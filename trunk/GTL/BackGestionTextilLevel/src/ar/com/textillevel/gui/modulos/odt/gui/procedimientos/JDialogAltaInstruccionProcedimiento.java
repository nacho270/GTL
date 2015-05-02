package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanComboConElementoOtro;
import ar.com.textillevel.gui.util.panels.PanSelectedFloatFromComboOrTextField;
import ar.com.textillevel.gui.util.panels.PanSelectedIntegerFromComboOrTextField;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTexto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.facade.api.remote.AccionProcedimientoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAltaInstruccionProcedimiento extends JDialog {

	private static final long serialVersionUID = -2335567081797221849L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private PanelInstruccion<?> panelInstruccion;

	private boolean acepto;
	private final ESectorMaquina sectorMaquina;
	private ETipoInstruccionProcedimiento tipoInstruccion;
	private TipoArticulo tipoArticulo;

	public JDialogAltaInstruccionProcedimiento(Dialog padre, ESectorMaquina sectorMaquina, ETipoInstruccionProcedimiento tipoInstruccion, TipoArticulo tipoArticulo) {
		super(padre);
		this.sectorMaquina = sectorMaquina;
		this.tipoInstruccion = tipoInstruccion;
		this.tipoArticulo = tipoArticulo;
		setUpScreen();
		setUpComponentes();
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		add(getPanelInstruccion(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setTitle("Alta de procedimientos");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		switch(tipoInstruccion){
			case PASADA:{
				setSize(new Dimension(450, 350));
				break;
			}
			case TIPO_PRODUCTO:{
				setSize(new Dimension(400, 170));
				break;
			}
			case TEXTO:{
				setSize(new Dimension(400, 130));
				break;
			}
		}
		GuiUtil.centrar(this);
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}


	public boolean isAcepto() {
		return acepto;
	}

	private void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()){
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void capturarDatos() {
		getPanelInstruccion().capturarDatos();
	}

	private boolean validar() {
		return getPanelInstruccion().validar();
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					acepto = false;
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private PanelInstruccion<?> getPanelInstruccion() {
		if(panelInstruccion == null) {
			if(tipoInstruccion == ETipoInstruccionProcedimiento.PASADA) {
				panelInstruccion = new PanelInstruccionPasada();
			} else if(tipoInstruccion == ETipoInstruccionProcedimiento.TEXTO) {
				panelInstruccion = new PanelInstruccionTexto();
			} else {
				panelInstruccion = new PanelInstruccionTipoProducto(sectorMaquina);
			}
		}
		return panelInstruccion;
	}

	public InstruccionProcedimiento getInstruccion() {
		return getPanelInstruccion().getInstruccion();
	}

	private abstract class PanelInstruccion<T extends InstruccionProcedimiento> extends JPanel {

		private static final long serialVersionUID = 4627326514992501258L;

		private final T instruccion;

		public T getInstruccion() {
			return instruccion;
		}

		public abstract void capturarDatos();

		public abstract boolean validar();

		public abstract void loadData();

		protected PanelInstruccion(T instruccion) {
			this.instruccion = instruccion;
		}
	}


	private class PanelInstruccionPasada extends PanelInstruccion<InstruccionProcedimientoPasadas> {

		private static final long serialVersionUID = -1811956351653800626L;

		private PanSelectedIntegerFromComboOrTextField panSelectedVueltas;
		private PanSelectedFloatFromComboOrTextField panSelectedVelocidades;
		private PanSelectedFloatFromComboOrTextField panSelectedTemperaturas;
		private PanelTablaQuimicoCantidad panelTabla;
		private PanComboAcccionProcedimiento panComboAccion;
		private Map<ESectorMaquina, List<Integer>> mapVueltasPorSector;
		private Map<ESectorMaquina, List<Float>> mapVelocidadesPorSector;
		private Map<ESectorMaquina, List<Float>> mapTemperaturasPorSector;


		protected PanelInstruccionPasada() {
			super(new InstruccionProcedimientoPasadas());
			initializeMaps();
			setUpComponentes();
		}

		protected PanelInstruccionPasada(InstruccionProcedimientoPasadas instruccion) {
			super(instruccion);
			initializeMaps();
			setUpComponentes();
		}

		private void initializeMaps() {
			//Vueltas
			mapVueltasPorSector = new HashMap<ESectorMaquina, List<Integer>>();
			Integer[] vueltas = {2,4,6};
			mapVueltasPorSector.put(ESectorMaquina.SECTOR_HUMEDO, Arrays.asList(vueltas));
			Integer[] vueltasSeco = {1};
			mapVueltasPorSector.put(ESectorMaquina.SECTOR_SECO, Arrays.asList(vueltasSeco));
			//Velocidades
			mapVelocidadesPorSector = new HashMap<ESectorMaquina, List<Float>>();
			Float[] velocidades = {80f, 100f, 120f};
			mapVelocidadesPorSector.put(ESectorMaquina.SECTOR_HUMEDO, Arrays.asList(velocidades));
			Float[] velocidadesSeco = {20f, 30f, 40f, 50f};
			mapVelocidadesPorSector.put(ESectorMaquina.SECTOR_SECO, Arrays.asList(velocidadesSeco));
			//Temperaturas
			mapTemperaturasPorSector = new HashMap<ESectorMaquina, List<Float>>();
			Float[] temps = {40f, 60f, 80f, 100f, 120f, 130f};
			mapTemperaturasPorSector.put(ESectorMaquina.SECTOR_HUMEDO, Arrays.asList(temps));
			Float[] tempsSeco = {120f, 150f, 180f, 190f};
			mapTemperaturasPorSector.put(ESectorMaquina.SECTOR_SECO, Arrays.asList(tempsSeco));
		}

		private void setUpComponentes() {
			setLayout(new BorderLayout());
			JPanel panelNorte = new JPanel(new GridBagLayout());
			panelNorte.add(getPanComboAccion(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
			panelNorte.add(getPanSelectedVueltas(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			panelNorte.add(getPanSelectedTemperaturas(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			panelNorte.add(getPanSelectedVelocidades(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			add(panelNorte, BorderLayout.NORTH);
			add(getPanelTabla(), BorderLayout.CENTER);
		}

		@Override
		public void capturarDatos() {
			getInstruccion().setAccion(getPanComboAccion().getSelectedItem());
			getInstruccion().setCantidadPasadas(getPanSelectedVueltas().getItemIngresado());
			Float temp = getPanSelectedTemperaturas().getItemIngresado();
			if(sectorMaquina == ESectorMaquina.SECTOR_HUMEDO) {
				if (temp > 130) {
					CLJOptionPane.showWarningMessage(this, "La temperatura máxima permitida es de 130 ºC.\nSe ha ajustado el valor automáticamente", "Advertencia");
					temp = 130f;
				}
			} else if(sectorMaquina == ESectorMaquina.SECTOR_SECO) {
				if (temp < 120) {
					CLJOptionPane.showWarningMessage(this, "La temperatura mínimia permitida es de 120 ºC.\nSe ha ajustado el valor automáticamente", "Advertencia");
					temp = 120f;
				}
				if (temp > 210) {
					CLJOptionPane.showWarningMessage(this, "La temperatura máxima permitida es de 210 ºC.\nSe ha ajustado el valor automáticamente", "Advertencia");
					temp = 210f;
				}
			} else if(sectorMaquina == ESectorMaquina.SECTOR_ESTAMPERIA) {
				if (temp < 120) {
					CLJOptionPane.showWarningMessage(this, "La temperatura mínimia permitida es de 120 ºC.\nSe ha ajustado el valor automáticamente", "Advertencia");
					temp = 120f;
				}
				if (temp > 150) {
					CLJOptionPane.showWarningMessage(this, "La temperatura máxima permitida es de 150 ºC.\nSe ha ajustado el valor automáticamente", "Advertencia");
					temp = 150f;
				}
			}
			getInstruccion().setTemperatura(temp);
			getInstruccion().setVelocidad(getPanSelectedVelocidades().getItemIngresado());
			getInstruccion().setSectorMaquina(sectorMaquina);
		}

		@Override
		public boolean validar() {
			Integer vueltas = getPanSelectedVueltas().getItemIngresado();
			if (vueltas == null) {				
				CLJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad de vueltas/pasadas.", "Error");
				return false;
			}
			if(vueltas == 0) {
				CLJOptionPane.showErrorMessage(this, "La cantidad de vueltas/pasadas debe ser mayor a 0.", "Error");
				return false;
			}
			if (getPanSelectedTemperaturas().getItemIngresado() == null) {
				CLJOptionPane.showErrorMessage(this, "Debe ingresar la temperatura", "Error");
				return false;
			}
			try {
				Float cantidad = getPanSelectedTemperaturas().getItemIngresado();
				if (cantidad <= 0) {
					CLJOptionPane.showErrorMessage(this, "La temperatura debe ser mayor a 0", "Error");
					return false;
				}
			} catch (NumberFormatException nfe) {
				CLJOptionPane.showErrorMessage(this, "Error en el formato de la temperatura", "Error");
				return false;
			}
			if(getPanSelectedVelocidades().getItemIngresado() == null) {
				CLJOptionPane.showErrorMessage(this, "Debe ingresar la velocidad", "Error");
				return false;
			}
			if(getPanSelectedVelocidades().getItemIngresado() <= 0f) {
				CLJOptionPane.showErrorMessage(this, "La velocidad debe ser mayor a 0", "Error");
				return false;
			}
			if (getInstruccion().getQuimicos().size() == 0) {
				if (CLJOptionPane.showQuestionMessage(this, "No ha ingresado quimicos. Desea continuar?", "Pregunta") == CLJOptionPane.NO_OPTION) {
					return false;
				}
			}
			return true;
		}

		private class PanComboAcccionProcedimiento extends PanComboConElementoOtro<AccionProcedimiento> {

			private static final long serialVersionUID = -2092538917036871227L;

			public PanComboAcccionProcedimiento(String lblCombo, List<AccionProcedimiento> items, AccionProcedimiento itemOtro) {
				super(lblCombo, items, itemOtro);
			}

			@Override
			public AccionProcedimiento itemOtroSelected() {
				JDialogAltaAccionProcedimiento dialogAltaTarima = new JDialogAltaAccionProcedimiento(JDialogAltaInstruccionProcedimiento.this, sectorMaquina);
				GuiUtil.centrar(dialogAltaTarima);
				dialogAltaTarima.setVisible(true);
				return dialogAltaTarima.getAccion();
			}
		}

		public PanComboAcccionProcedimiento getPanComboAccion() {
			if (panComboAccion == null) {
				AccionProcedimiento itemOtro = new AccionProcedimiento();
				itemOtro.setId(-1);
				itemOtro.setNombre("OTRO");
				panComboAccion = new PanComboAcccionProcedimiento("Acción: ", GTLBeanFactory.getInstance().getBean2(AccionProcedimientoFacadeRemote.class).getAllSortedBySector(sectorMaquina), itemOtro);
				if (getInstruccion() != null && getInstruccion().getAccion() != null) {
					this.panComboAccion.setSelectedItem(getInstruccion().getAccion());
				}
			}
			return panComboAccion;
		}

		private class PanelTablaQuimicoCantidad extends PanelTabla<QuimicoCantidad> {

			private static final long serialVersionUID = -6495780340286054397L;

			private static final int CANT_COLS = 4;
			private static final int COL_QUIMICO = 0;
			private static final int COL_CANTIDAD = 1;
			private static final int COL_UNIDAD = 2;
			private static final int COL_OBJ = 3;

			public PanelTablaQuimicoCantidad() {
				agregarBotonModificar();
			}

			@Override
			protected void agregarElemento(QuimicoCantidad elemento) {
				getTabla().addRow(new Object[] { elemento.getMateriaPrima().getDescripcion(), GenericUtils.getDecimalFormat2().format(elemento.getCantidad()), elemento.getUnidad(), elemento });
			}

			@Override
			protected void dobleClickTabla(int filaSeleccionada) {
				if (filaSeleccionada > -1) {
					botonModificarPresionado(filaSeleccionada);
				}
			}

			@Override
			protected CLJTable construirTabla() {
				CLJTable tabla = new CLJTable(0, CANT_COLS);
				tabla.setStringColumn(COL_QUIMICO, "Químico", 200, 200, true);
				tabla.setStringColumn(COL_CANTIDAD, "Proporción (%)", 100, 100, true);
				tabla.setStringColumn(COL_UNIDAD, "Unidad", 70, 70, true);
				tabla.setStringColumn(COL_OBJ, "", 0);
				tabla.setReorderingAllowed(false);
				tabla.setAllowHidingColumns(false);
				tabla.setAllowSorting(false);
				tabla.setHeaderAlignment(COL_QUIMICO, CLJTable.CENTER_ALIGN);
				tabla.setHeaderAlignment(COL_CANTIDAD, CLJTable.CENTER_ALIGN);
				tabla.setHeaderAlignment(COL_UNIDAD, CLJTable.CENTER_ALIGN);
				return tabla;
			}

			@Override
			protected QuimicoCantidad getElemento(int fila) {
				return (QuimicoCantidad) getTabla().getValueAt(fila, COL_OBJ);
			}

			@Override
			protected String validarElemento(int fila) {
				return null;
			}

			private List<Quimico> quimicosAgregados() {
				List<Quimico> quimicos = new ArrayList<Quimico>();
				for (QuimicoCantidad qc : getInstruccion().getQuimicos()) {
					quimicos.add(qc.getMateriaPrima());
				}
				return quimicos;
			}

			@Override
			public boolean validarAgregar() {
				JDialogAgregarModificarQuimicoCantidad dialog = new JDialogAgregarModificarQuimicoCantidad(JDialogAltaInstruccionProcedimiento.this, quimicosAgregados());
				dialog.setVisible(true);
				if (dialog.isAcepto()) {
					getInstruccion().getQuimicos().add(dialog.getQuimicoCantidadActual());
					limpiar();
					agregarElementos(getInstruccion().getQuimicos());
				}
				return false;
			}

			@Override
			public boolean validarQuitar() {
				getInstruccion().getQuimicos().remove(getTabla().getSelectedRow());
				limpiar();
				agregarElementos(getInstruccion().getQuimicos());
				return false;
			}

			@Override
			protected void botonModificarPresionado(int filaSeleccionada) {
				QuimicoCantidad qc = getElemento(filaSeleccionada);
				JDialogAgregarModificarQuimicoCantidad dialog = new JDialogAgregarModificarQuimicoCantidad(JDialogAltaInstruccionProcedimiento.this, quimicosAgregados(), qc);
				dialog.setVisible(true);
				if (dialog.isAcepto()) {
					getInstruccion().getQuimicos().set(filaSeleccionada, dialog.getQuimicoCantidadActual());
					limpiar();
					agregarElementos(getInstruccion().getQuimicos());
				}
			}
		}

		public PanelTablaQuimicoCantidad getPanelTabla() {
			if (panelTabla == null) {
				panelTabla = new PanelTablaQuimicoCantidad();
			}
			return panelTabla;
		}

		@Override
		public void loadData() {
			getPanSelectedVueltas().setSelectedItem(getInstruccion().getCantidadPasadas());
			getPanSelectedTemperaturas().setSelectedItem(getInstruccion().getTemperatura());
			getPanelTabla().agregarElementos(getInstruccion().getQuimicos());
			getPanSelectedVelocidades().setSelectedItem(getInstruccion().getVelocidad());
		}

		private PanSelectedIntegerFromComboOrTextField getPanSelectedVueltas() {
			if(panSelectedVueltas == null) {
				List<Integer> vueltas = mapVueltasPorSector.get(sectorMaquina);
				if(vueltas == null) {
					vueltas = Collections.emptyList();
				}
				panSelectedVueltas = new PanSelectedIntegerFromComboOrTextField("Vueltas/pasadas: ", vueltas);
			}
			return panSelectedVueltas;
		}

		private PanSelectedFloatFromComboOrTextField getPanSelectedVelocidades() {
			if(panSelectedVelocidades == null) {
				List<Float> velocidades = mapVelocidadesPorSector.get(sectorMaquina);
				if(velocidades == null) {
					velocidades = Collections.emptyList();
				}
				panSelectedVelocidades = new PanSelectedFloatFromComboOrTextField("Velocidad (MTS/MIN): ", velocidades);
			}
			return panSelectedVelocidades;
		}

		private PanSelectedFloatFromComboOrTextField getPanSelectedTemperaturas() {
			if(panSelectedTemperaturas == null) {
				List<Float> temperaturas = mapTemperaturasPorSector.get(sectorMaquina);
				if(temperaturas == null) {
					temperaturas = Collections.emptyList();
				}
				panSelectedTemperaturas = new PanSelectedFloatFromComboOrTextField("Temperatura (ºC): ", temperaturas);
			}
			return panSelectedTemperaturas;
		}
	
	}

	private class PanelInstruccionTipoProducto extends PanelInstruccion<InstruccionProcedimientoTipoProducto> {

		private static final long serialVersionUID = -4793452672325220926L;

		private JComboBox cmbTipoArticulo;
		private JComboBox cmbTipoProducto;
		private final ESectorMaquina sector;

		protected PanelInstruccionTipoProducto(ESectorMaquina sector) {
			super(new InstruccionProcedimientoTipoProducto());
			this.sector = sector;
			setUpComponentes();
		}

		protected PanelInstruccionTipoProducto(InstruccionProcedimientoTipoProducto instruccion, ESectorMaquina sector) {
			super(instruccion);
			this.sector = sector;
			setUpComponentes();
		}

		private void setUpComponentes() {
			setLayout(new BorderLayout());
			JPanel panelNorte = new JPanel(new GridBagLayout());
			panelNorte.add(new JLabel("Tipo de artículo: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getCmbTipoArticulo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelNorte.add(new JLabel("Tipo de proceso: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getCmbTipoProducto(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			add(panelNorte,BorderLayout.NORTH);
		}

		@Override
		public void capturarDatos() {
			getInstruccion().setTipoArticulo((TipoArticulo) getCmbTipoArticulo().getSelectedItem());
			getInstruccion().setTipoProducto((ETipoProducto)getCmbTipoProducto().getSelectedItem());
			getInstruccion().setSectorMaquina(sectorMaquina);
//			getInstruccion().setProceso(proceso);
		}

		@Override
		public boolean validar() {
			return true;
		}

		@Override
		public void loadData() {
			getCmbTipoArticulo().setSelectedItem(getInstruccion().getTipoArticulo());
			getCmbTipoProducto().setSelectedItem(getInstruccion().getTipoProducto());
		}

		public JComboBox getCmbTipoArticulo() {
			if (cmbTipoArticulo == null) {
				cmbTipoArticulo = new JComboBox();
				if (tipoArticulo.getTiposArticuloComponentes().isEmpty()) {
					cmbTipoArticulo.addItem(tipoArticulo);
				} else {
					GuiUtil.llenarCombo(cmbTipoArticulo, tipoArticulo.getTiposArticuloComponentes(), true);
				}
			}
			return cmbTipoArticulo;
		}

		public JComboBox getCmbTipoProducto() {
			if (cmbTipoProducto == null) {
				cmbTipoProducto = new JComboBox();
				GuiUtil.llenarCombo(cmbTipoProducto, ETipoProducto.getTipoProductosBySector(sector), true);
			}
			return cmbTipoProducto;
		}

	}


	private class PanelInstruccionTexto extends PanelInstruccion<InstruccionProcedimientoTexto> {

		private static final long serialVersionUID = -3242876826507087879L;

		private CLJTextField txtDescripcion;

		protected PanelInstruccionTexto() {
			super(new InstruccionProcedimientoTexto());
			setUpComponentes();
		}

		protected PanelInstruccionTexto(InstruccionProcedimientoTexto instruccion) {
			super(instruccion);
			setUpComponentes();
		}

		private void setUpComponentes() {
			setLayout(new BorderLayout());
			JPanel panelNorte = new JPanel(new GridBagLayout());
			panelNorte.add(new JLabel("Especificación: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getTxtDescripcion(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			add(panelNorte,BorderLayout.NORTH);
		}

		@Override
		public void capturarDatos() {
			getInstruccion().setEspecificacion(getTxtDescripcion().getText().trim().toUpperCase());
			getInstruccion().setSectorMaquina(sectorMaquina);
//			getInstruccion().setProceso(proceso);
		}

		@Override
		public boolean validar() {
			if (StringUtil.isNullOrEmpty(getTxtDescripcion().getText())) {
				CLJOptionPane.showErrorMessage(JDialogAltaInstruccionProcedimiento.this, "Debe ingresar la especificación", "Error");
				getTxtDescripcion().requestFocus();
				return false;
			}
			return true;
		}

		@Override
		public void loadData() {
//			getTxtDescripcion().setText(InstruccionProcedimientoRenderer.getDescripcionInstruccion(getInstruccionActual()));
		}

		public CLJTextField getTxtDescripcion() {
			if (txtDescripcion == null) {
				txtDescripcion = new CLJTextField(200);
			}
			return txtDescripcion;
		}
	}

	public static void main(String[] args) {
		JDialogAltaInstruccionProcedimiento d = new JDialogAltaInstruccionProcedimiento(new JDialog(), ESectorMaquina.SECTOR_HUMEDO, ETipoInstruccionProcedimiento.TIPO_PRODUCTO, null);
		d.setVisible(true);
		
	}

}