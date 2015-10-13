package ar.com.textillevel.gui.modulos.personal.abm.antiguedad;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.Antiguedad;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ConfiguracionAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ValorAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.AntiguedadFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMConfiguracionAntiguedades extends GuiABMListaTemplate {

	private static final long serialVersionUID = 8611398246288418370L;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private PanelDatePicker panelFechaDesde;
	private PanelTablaAntiguedad panelTabla;
	private JCheckBox chkValoresPorHora;

	private ConfiguracionAntiguedad configuracionAntiguedadActual;
	private AntiguedadFacadeRemote antiguedadFacade;

	public GuiABMConfiguracionAntiguedades(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Configuración de Antigüedades");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();

			JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelNorte.add(getPanelFechaDesde());
			panelNorte.add(getChkValoresPorHora());

			panDetalle.add(panelNorte, BorderLayout.NORTH);
			panDetalle.add(getPanelTabla(), BorderLayout.CENTER);
		}
		return panDetalle;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSeleccionado != null) {
			List<ConfiguracionAntiguedad> confs = getAntiguedadFacade().getAllConfiguracionesBySindicato(sindicatoSeleccionado);
			lista.clear();
			if (confs != null) {
				for (ConfiguracionAntiguedad c : confs) {
					lista.addItem(c);
				}
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		setContenidoComboMaestro(GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class).getAllOrderByName(), "Sindicato: ");
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		ConfiguracionAntiguedad conf = new ConfiguracionAntiguedad();
		conf.setSindicato(sindicato);
		setConfiguracionAntiguedadActual(conf);
//		getPanelTabla().getTabla().addRow(new Object[]{1,"<html><table border='1'><tr><td>nacho</td><td>capo</td></tr><tr><td>nacho</td><td>capo</td></tr><tr><td>nacho</td><td>capo</td></tr><tr><td>nacho</td><td>capo</td></tr></table></html>"});
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la configuración para el sindicato seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getAntiguedadFacade().remove(getConfiguracionAntiguedadActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getPanelFechaDesde().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un sindicato", "Error");
			return false;
		}
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarDatos();
			ConfiguracionAntiguedad conf = getAntiguedadFacade().save(getConfiguracionAntiguedadActual());
			FWJOptionPane.showInformationMessage(getFrame(), "La configuración se ha grabado con éxito", "Información");
			lista.setSelectedValue(conf, true);
			return true;
		}
		return false;
	}

	private void capturarDatos() {
		getConfiguracionAntiguedadActual().setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
		getConfiguracionAntiguedadActual().setValoresPorHora(getChkValoresPorHora().isSelected());
	}

	private boolean validar() {
		if (getPanelFechaDesde().getDate() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe elegir una 'Fecha desde'", "Error");
			getPanelFechaDesde().requestFocus();
			return false;
		}
		if(getConfiguracionAntiguedadActual().getAntiguedades().size()==0){
			FWJOptionPane.showErrorMessage(this, "Debe agregar al menos una entrada de antigüedad.", "Error");
			getPanelFechaDesde().requestFocus();
			return false;
		}
		
		if(getConfiguracionAntiguedadActual().getId()!=null && !getConfiguracionAntiguedadActual().getFechaDesde().equals(new java.sql.Date(getPanelFechaDesde().getDate().getTime())) ){
			Sindicato sindicato = getConfiguracionAntiguedadActual().getSindicato();
			java.sql.Date fecha = new java.sql.Date(getPanelFechaDesde().getDate().getTime());
			Date configuracionParaFechaAnterior = getAntiguedadFacade().getConfiguracionParaFechaAnterior(sindicato, fecha);
			if (configuracionParaFechaAnterior != null) {
				FWJOptionPane.showErrorMessage(this, "Ya existe una configuración de antigüedad anterior para el sindicato elegido. Elija una fecha posterior a " + DateUtil.dateToString(fecha), "Error");
				getPanelFechaDesde().requestFocus();
				return false;
			}
		}
		return true;
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getPanelTabla().limpiar();
		getPanelFechaDesde().setSelectedDate(DateUtil.getHoy());
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setConfiguracionAntiguedadActual((ConfiguracionAntiguedad) lista.getSelectedValue());
		limpiarDatos();
		if (getConfiguracionAntiguedadActual() != null) {
			llenarDatos();
		}
	}

	private void llenarDatos() {
		getPanelFechaDesde().setSelectedDate(getConfiguracionAntiguedadActual().getFechaDesde());
		getChkValoresPorHora().setSelected(getConfiguracionAntiguedadActual().getValoresPorHora());
		getPanelTabla().refreshTable();
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	private class PanelTablaAntiguedad extends PanelTabla<Antiguedad> {

		private static final long serialVersionUID = 2638535870733477999L;

		private static final int CANT_COLS = 3;
		private static final int COL_PUESTO = 0;
		private static final int COL_VALORES = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaAntiguedad() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_PUESTO, "Puesto", 100, 100, true);
			tabla.setMultilineColumn(COL_VALORES, "Valores", 200, true);
//			tabla.setStringColumn(COL_VALORES, "Valores", 100,100,true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(Antiguedad elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PUESTO] = elemento.getPuesto()==null?"CUALQUIERA":elemento.getPuesto();
			row[COL_VALORES] = getStringValoresPuesto(elemento.getValoresAntiguedad());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		private String getStringValoresPuesto(List<ValorAntiguedad> valoresAntiguedad) {
			List<String> lista = new ArrayList<String>();
			for(ValorAntiguedad va : valoresAntiguedad){
				String str = va.getAntiguedad() + " años - $ " + GenericUtils.getDecimalFormat().format(va.getValorDefault().doubleValue());
				lista.add(str);
			}
			return StringUtil.getCadena(lista, "\n");
		}

		@Override
		protected Antiguedad getElemento(int fila) {
			return (Antiguedad)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarAntiguedadPuesto dialog;
			try {
				dialog = new JDialogAgregarModificarAntiguedadPuesto(GuiABMConfiguracionAntiguedades.this.getFrame(), getConfiguracionAntiguedadActual().getSindicato(), getPuestosUtilizados(false));
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					getConfiguracionAntiguedadActual().getAntiguedades().add(dialog.getAntiguedadActual());
					refreshTable();
				}
			} catch (YaHayUnPuestoCualquieraException e) {
				FWJOptionPane.showErrorMessage(GuiABMConfiguracionAntiguedades.this.getFrame(), StringW.wordWrap("No se pueden agregar puestos debido a que ya hay una entrada con valor 'CUALQUIERA'. Modifique esta entrada asignandole puesto para poder ingresar mas."), "Error");
			} catch (NoSePuedenAgregarMasPuestosException e) {
				FWJOptionPane.showErrorMessage(GuiABMConfiguracionAntiguedades.this.getFrame(), StringW.wordWrap("No se pueden agregar puestos debido a que ya se han utilizado todos los disponibles."), "Error");
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			getConfiguracionAntiguedadActual().getAntiguedades().remove(getTabla().getSelectedRow());
			refreshTable();
			return false;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogAgregarModificarAntiguedadPuesto dialog;
			try {
				dialog = new JDialogAgregarModificarAntiguedadPuesto(GuiABMConfiguracionAntiguedades.this.getFrame(), getConfiguracionAntiguedadActual().getSindicato(), getPuestosUtilizados(true),getElemento(filaSeleccionada));
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					getConfiguracionAntiguedadActual().getAntiguedades().set(filaSeleccionada,dialog.getAntiguedadActual());
					refreshTable();
				}
			} catch (YaHayUnPuestoCualquieraException e) {}
			  catch (NoSePuedenAgregarMasPuestosException e) {}
		}
		
		private void refreshTable(){
			limpiar();
			for(Antiguedad a : getConfiguracionAntiguedadActual().getAntiguedades()){
				agregarElemento(a);
			}
		}
		
		private List<Puesto> getPuestosUtilizados(boolean modificar) throws YaHayUnPuestoCualquieraException, NoSePuedenAgregarMasPuestosException {
			List<Puesto> puestos = new ArrayList<Puesto>();
			for(Antiguedad a : getElementos()){
				if(a.getPuesto()!=null){
					puestos.add(a.getPuesto());
				}else if(!modificar){
					throw new YaHayUnPuestoCualquieraException();
				}
			}
			
			if(!modificar && !puestos.isEmpty()){
				List<Puesto> allPuestos = GTLPersonalBeanFactory.getInstance().getBean2(PuestoFacadeRemote.class).getAllByIdSindicato(getConfiguracionAntiguedadActual().getSindicato().getId());
				if(puestos.size()==allPuestos.size()){
					throw new NoSePuedenAgregarMasPuestosException();
				}
			}
			
			return puestos;
		}
	}
	
	private class YaHayUnPuestoCualquieraException extends Exception{

		private static final long serialVersionUID = -6221288077008522195L;
		
	}
	
	private class NoSePuedenAgregarMasPuestosException extends Exception{

		private static final long serialVersionUID = -6221288077008522195L;
		
	}

	public AntiguedadFacadeRemote getAntiguedadFacade() {
		if (antiguedadFacade == null) {
			antiguedadFacade = GTLPersonalBeanFactory.getInstance().getBean2(AntiguedadFacadeRemote.class);
		}
		return antiguedadFacade;
	}

	public ConfiguracionAntiguedad getConfiguracionAntiguedadActual() {
		return configuracionAntiguedadActual;
	}

	public void setConfiguracionAntiguedadActual(ConfiguracionAntiguedad configuracionAntiguedadActual) {
		this.configuracionAntiguedadActual = configuracionAntiguedadActual;
	}


	public PanelTablaAntiguedad getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaAntiguedad();
		}
		return panelTabla;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Desde: ");
			panelFechaDesde.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaDesde;
	}

	public JCheckBox getChkValoresPorHora() {
		if(chkValoresPorHora == null){
			chkValoresPorHora = new JCheckBox("Valores por hora");
		}
		return chkValoresPorHora;
	}
}
