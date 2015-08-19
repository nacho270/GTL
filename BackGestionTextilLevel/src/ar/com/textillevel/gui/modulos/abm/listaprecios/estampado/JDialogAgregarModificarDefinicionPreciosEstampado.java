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

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
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
	private JPanel panDatosPropios;
	private RangoCoberturaEstampado rangoCoberturaEstampadoSiendoEditado;

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre, cliente, tipoProducto);
		bases = getGamaFacade().getAllOrderByName();
		GuiUtil.llenarCombo(cmbBase, bases, true);
		setModoEdicion(false);
	}

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
	}

	@Override
	protected JPanel createPanelDatosEspecificos() {
		return getPanDatosPropios();
	}

	private JPanel getPanDatosPropios() {
		if(panDatosPropios == null) {
			panDatosPropios = new JPanel(new GridBagLayout());
			panDatosPropios.add(new JLabel("Base: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getCmbBase(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Color desde : "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCantColoresDesde(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Color Hasta : "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCantColoresHasta(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Cobertura desde : "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCoberturaDesde(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Cobertura Hasta : "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCoberturaHasta(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		}
		return panDatosPropios;
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloEstampado> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloEstampado> parent) {
		return new PanelTablaRangoEstampado(parent);
	}

	private JComboBox getCmbBase() {
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

		if(rangoCoberturaEstampadoSiendoEditado != null) {
			rangoCoberturaEstampadoSiendoEditado.deepRemove();
		}

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
			rango.setDefinicionPrecio(definicion);
		}
		
		//Grupo
		TipoArticulo ta = (TipoArticulo)getCmbTipoArticulo().getSelectedItem();
		GrupoTipoArticuloBaseEstampado grupo = rango.getGrupo(ta);
		if(grupo == null) {
			grupo = new GrupoTipoArticuloBaseEstampado();
			grupo.setTipoArticulo(ta);
			rango.getGruposBase().add(grupo);
			grupo.setRangoAnchoArticulo(rango);
		}
		
		//Precios Base (Gama)
		GamaColor base = (GamaColor)getCmbBase().getSelectedItem();  
		PrecioBaseEstampado precioBase = grupo.getPrecioBase(base);
		if(precioBase == null) {
			precioBase = new PrecioBaseEstampado();
			precioBase.setGama(base);
			grupo.getPrecios().add(precioBase);
			precioBase.setGrupoTipoArticuloBase(grupo);
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
			rangoCantColores.setPrecioBase(precioBase);
		}
		//Rango Cobertura
		Integer minCobertura = Integer.valueOf(getTxtCoberturaDesde().getText());
		Integer maxCobertura = Integer.valueOf(getTxtCoberturaHasta().getText());
		Float precio = Float.valueOf(getTxtPrecio().getText());
		
		RangoCoberturaEstampado rangoCobertura = null;
		if(rangoCoberturaEstampadoSiendoEditado == null) {
			List<RangoCoberturaEstampado> rangosCobertura = rangoCantColores.getRangoCobertura(minCobertura, maxCobertura);
			if(!rangosCobertura.isEmpty()) {
				rangoCobertura = rangosCobertura.get(0);//Seguro hay uno solo 
			}
		}
		if(rangoCobertura == null) {
			rangoCobertura = new RangoCoberturaEstampado();
		}
		rangoCobertura.setMinimo(minCobertura);
		rangoCobertura.setMaximo(maxCobertura);
		rangoCobertura.setPrecio(precio);
		rangoCantColores.getRangos().add(rangoCobertura);
		rangoCobertura.setRangoCantidadColores(rangoCantColores);

		List<RangoAnchoArticuloEstampado> rangosList = new ArrayList<RangoAnchoArticuloEstampado>();
		for(RangoAncho r : getDefinicion().getRangos()) {
			rangosList.add((RangoAnchoArticuloEstampado)r);
		}
		//agrego a la tabla
		getTablaRango().limpiar();
		getTablaRango().agregarElementos(rangosList);
	}

	@Override
	protected boolean validar() {
		//TODO: Validar completitud y correctitud de tipos de datos para post conversión
		/*
		if(StringUtil.isNullOrEmpty(getTxtAnchoInicial().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar 'Ancho Inicial'", "Error");
			getTxtAnchoInicial().requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtAnchoFinal().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar 'Ancho Final'", "Error");
			getTxtAnchoFinal().requestFocus();
			return false;
		}
		*/
		
		TipoArticulo ta = (TipoArticulo)getCmbTipoArticulo().getSelectedItem();
		GamaColor base = (GamaColor)getCmbBase().getSelectedItem();
		
		Float precio = Float.valueOf(getTxtPrecio().getText());

		//Rango Ancho Articulo
		RangoAnchoArticuloEstampado rangoAnchoArticuloSiendoEditado=null;
		Float min = Float.valueOf(getTxtAnchoInicial().getText());
		Float max = Float.valueOf(getTxtAnchoFinal().getText());
		Float anchoExacto = null;
		if(getChkAnchoExacto().isSelected()) {
			anchoExacto = Float.valueOf(getTxtAnchoExacto().getText());
		}
		RangoAncho rangoAnchoArticuloExistente = null;
		if(rangoCoberturaEstampadoSiendoEditado != null) {
			rangoAnchoArticuloSiendoEditado = rangoCoberturaEstampadoSiendoEditado.getRangoCantidadColores().getPrecioBase().getGrupoTipoArticuloBase().getRangoAnchoArticulo();
		}
		if(getDefinicion() != null) {
			rangoAnchoArticuloExistente = getDefinicion().getRangoSolapadoCon(min, max, anchoExacto);
			if(rangoAnchoArticuloExistente != null && (rangoAnchoArticuloSiendoEditado != null && !rangoAnchoArticuloExistente.equals(rangoAnchoArticuloSiendoEditado))) {
				CLJOptionPane.showErrorMessage(this, "Rango Ancho Articulo Existente", "Error");
				return false;
			}
			rangoAnchoArticuloExistente = getDefinicion().getRango(min, max, anchoExacto);			
		}

		//Rango Cantidad de Colores
		RangoCantidadColores rangoCantColoresSiendoEditado = null;
		Integer minCantColores = Integer.valueOf(getTxtCantColoresDesde().getText());
		Integer maxCantColores = Integer.valueOf(getTxtCantColoresHasta().getText());
		RangoCantidadColores rangoCantColoresExistente = null;
		if(rangoCoberturaEstampadoSiendoEditado != null) {
			rangoCantColoresSiendoEditado = rangoCoberturaEstampadoSiendoEditado.getRangoCantidadColores();
		}
		if(rangoAnchoArticuloExistente != null) {
			GrupoTipoArticuloBaseEstampado grupo = ((RangoAnchoArticuloEstampado)rangoAnchoArticuloExistente).getGrupo(ta);
			if(grupo != null) {
				PrecioBaseEstampado precioBase = grupo.getPrecioBase(base);
				if(precioBase != null) {
					rangoCantColoresExistente = precioBase.getRangoSolapadoCon(minCantColores, maxCantColores);
					if(rangoCantColoresExistente != null && (rangoCantColoresSiendoEditado != null && !rangoCantColoresSiendoEditado.equals(rangoCantColoresExistente))) {
						CLJOptionPane.showErrorMessage(this, "Rango Cantidad de Colores Existente", "Error");
						return false;
					}
					rangoCantColoresExistente = precioBase.getRango(minCantColores, maxCantColores);
				}
			}
		}

		//Rango Cobertura
		RangoCoberturaEstampado rangoCoberturaExistente = null;
		Integer minCobertura = Integer.valueOf(getTxtCoberturaDesde().getText());
		Integer maxCobertura = Integer.valueOf(getTxtCoberturaHasta().getText());
		if(rangoCantColoresExistente != null) {
			List<RangoCoberturaEstampado> rangosCobertura = rangoCantColoresExistente.getRangoCobertura(minCobertura, maxCobertura);
			if(!rangosCobertura.isEmpty()) {
				if(rangosCobertura.size()>1) {
					CLJOptionPane.showErrorMessage(this, "Rango Cobertura Existente", "Error");
					return false;
				} else {
					rangoCoberturaExistente = rangosCobertura.get(0);
				}
				if(rangoCoberturaExistente != null && (rangoCoberturaEstampadoSiendoEditado == null || rangoCoberturaEstampadoSiendoEditado!=rangoCoberturaExistente)) {
					CLJOptionPane.showErrorMessage(this, "Rango Cobertura Existente", "Error");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void setModoEdicionExtended(boolean modoEdicion) {
		GuiUtil.setEstadoPanel(getPanDatosPropios(), modoEdicion);
	}

	@Override
	protected void limpiarDatosExtended() {
		getTxtCantColoresDesde().setText(null);
		getTxtCantColoresHasta().setText(null);
		getTxtCoberturaDesde().setText(null);
		getTxtCoberturaHasta().setText(null);
		getCmbBase().setSelectedIndex(-1);
	}

	public void setRangoCoberturaEstampadoSiendoEditado(RangoCoberturaEstampado rangoCoberturaEstampadoSiendoEditado, boolean modoEdicion) {
		this.rangoCoberturaEstampadoSiendoEditado = rangoCoberturaEstampadoSiendoEditado;

		setModoEdicion(modoEdicion);

		getTxtPrecio().setText(rangoCoberturaEstampadoSiendoEditado.getPrecio().toString());
		getTxtCoberturaDesde().setText(rangoCoberturaEstampadoSiendoEditado.getMinimo().toString());
		getTxtCoberturaHasta().setText(rangoCoberturaEstampadoSiendoEditado.getMaximo().toString());

		RangoCantidadColores rangoCantidadColores = rangoCoberturaEstampadoSiendoEditado.getRangoCantidadColores();
		getTxtCantColoresDesde().setText(rangoCantidadColores.getMinimo().toString());
		getTxtCantColoresHasta().setText(rangoCantidadColores.getMaximo().toString());

		PrecioBaseEstampado precioBase = rangoCantidadColores.getPrecioBase();
		getCmbBase().setSelectedItem(precioBase.getGama());

		GrupoTipoArticuloBaseEstampado grupoTipoArticuloBase = precioBase.getGrupoTipoArticuloBase();
		getCmbTipoArticulo().setSelectedItem(grupoTipoArticuloBase.getTipoArticulo());

		RangoAnchoArticuloEstampado rangoAnchoArticulo = grupoTipoArticuloBase.getRangoAnchoArticulo();
		getTxtAnchoInicial().setText(rangoAnchoArticulo.getAnchoMinimo().toString());
		getTxtAnchoFinal().setText(rangoAnchoArticulo.getAnchoMaximo().toString());
	}

	@Override
	protected void botonAgregarOrCancelarPresionado() {
		this.rangoCoberturaEstampadoSiendoEditado = null;
	}

}