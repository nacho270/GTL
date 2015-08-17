package ar.com.textillevel.gui.modulos.abm.listaprecios.estampado;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCantidadColores;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCoberturaEstampado;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarDefinicionPreciosEstampado extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloEstampado> {

	private static final long serialVersionUID = -6851805146971694269L;
	
	private JComboBox cmbBase;
	private CLJTextField txtCantColoresDesde;
	private CLJTextField txtCantColoresHasta;
	private CLJTextField txtCoberturaDesde;
	private CLJTextField txtCoberturaHasta;
	
	private List<GamaColor> bases;

	private GamaColorFacadeRemote gamaFacade;

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre, cliente, tipoProducto);
		bases = getGamaFacade().getAllOrderByName();
		GuiUtil.llenarCombo(cmbBase, bases, true);
	}

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
	}

	@Override
	protected JPanel createPanelDatosEspecificos() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Base: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getCmbBase(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		panel.add(new JLabel("Color desde : "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getTxtCantColoresDesde(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		panel.add(new JLabel("Color Hasta : "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getTxtCantColoresHasta(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		panel.add(new JLabel("Cobertura desde : "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getTxtCoberturaDesde(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		panel.add(new JLabel("Cobertura Hasta : "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
		panel.add(getTxtCoberturaHasta(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		return panel;
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloEstampado> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloEstampado> parent) {
		return new PanelTablaRangoEstampado(parent);
	}

	public JComboBox getCmbBase() {
		if (cmbBase == null) {
			cmbBase = new JComboBox();
		}
		return cmbBase;
	}

	private CLJTextField getTxtCantColoresDesde() {
		if (txtCantColoresDesde == null) {
			txtCantColoresDesde = new CLJTextField();
			txtCantColoresDesde.setSize(100, 20);
		}
		return txtCantColoresDesde;
	}

	private CLJTextField getTxtCantColoresHasta() {
		if (txtCantColoresHasta == null) {
			txtCantColoresHasta = new CLJTextField();
		}
		return txtCantColoresHasta;
	}
	
	private CLJTextField getTxtCoberturaDesde() {
		if (txtCoberturaDesde == null) {
			txtCoberturaDesde = new CLJTextField();
			txtCoberturaDesde.setSize(100, 20);
		}
		return txtCoberturaDesde;
	}

	private CLJTextField getTxtCoberturaHasta() {
		if (txtCoberturaHasta == null) {
			txtCoberturaHasta = new CLJTextField();
		}
		return txtCoberturaHasta;
	}
	
	private GamaColorFacadeRemote getGamaFacade() {
		if(gamaFacade == null) {
			gamaFacade = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class);
		}
		return gamaFacade;
	}

	@Override
	protected void botonAgregarPresionado() {
		//Definicion
		DefinicionPrecio definicion = getDefinicion();
		if(definicion == null) {
			setDefinicion(new DefinicionPrecio());
		}
		//Rango
		Float min = Float.valueOf(getTxtAnchoInicial().getText());
		Float max = Float.valueOf(getTxtAnchoFinal().getText());
		Float exacto = null;
		if(getChkAnchoExacto().isSelected()) {
			exacto = Float.valueOf(getTxtAnchoExacto().getText());
		}
		RangoAnchoArticuloEstampado rango = (RangoAnchoArticuloEstampado)definicion.getRango(min, max, exacto);
		if(rango == null) {
			rango = new RangoAnchoArticuloEstampado();
			rango.setAnchoExacto(exacto);
			rango.setAnchoMinimo(min);
			rango.setAnchoMaximo(max);
			getDefinicion().getRangos().add(rango);
		}
		//Grupo
		TipoArticulo ta = (TipoArticulo)getCmbTipoArticulo().getSelectedItem();
		GrupoTipoArticuloBaseEstampado grupo = rango.getGrupo(ta);
		if(grupo == null) {
			grupo = new GrupoTipoArticuloBaseEstampado();
			grupo.setTipoArticulo(ta);
			rango.getGruposBase().add(grupo);
		}
		//Precios Base (Gama)
		GamaColor base = (GamaColor)getCmbBase().getSelectedItem();  
		PrecioBaseEstampado precioBase = grupo.getPrecioBase(base);
		if(precioBase == null) {
			precioBase = new PrecioBaseEstampado();
			precioBase.setGama(base);
			grupo.getPrecios().add(precioBase);
		}
		//Rango Cant Colores
		Integer minCantColores = Integer.valueOf(getTxtCantColoresDesde().getText());
		Integer maxCantColores = Integer.valueOf(getTxtCantColoresHasta().getText());
		RangoCantidadColores rangoCantColores = precioBase.getRango(minCantColores, maxCantColores);
		if(rangoCantColores == null) {
			rangoCantColores = new RangoCantidadColores();
			rangoCantColores.setMinimo(minCantColores);
			rangoCantColores.setMaximo(maxCantColores);
			precioBase.getRangosDeColores().add(rangoCantColores);
		}
		//Rango Cobertura
		Integer minCobertura = Integer.valueOf(getTxtCoberturaDesde().getText());
		Integer maxCobertura = Integer.valueOf(getTxtCoberturaHasta().getText());
		Float precio = Float.valueOf(getTxtPrecio().getText());
		RangoCoberturaEstampado rangoCobertura = rangoCantColores.getRangoCobertura(minCobertura, maxCobertura);
		if(rangoCobertura == null) {
			rangoCobertura = new RangoCoberturaEstampado();
			rangoCobertura.setMinimo(minCobertura);
			rangoCobertura.setMaximo(maxCobertura);
			rangoCantColores.getRangos().add(rangoCobertura);
		}
		rangoCobertura.setPrecio(precio);

		List<RangoAnchoArticuloEstampado> rangosList = new ArrayList<RangoAnchoArticuloEstampado>();
		rangosList.add(rango);
		getTablaRango().agregarElementos(rangosList);
	}

}