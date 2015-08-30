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
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarDefinicionPreciosEstampado extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloEstampado, RangoCoberturaEstampado> {

	private static final long serialVersionUID = -6851805146971694269L;
	
	private JComboBox cmbBase;
	private DecimalNumericTextField txtCantColoresDesde;
	private DecimalNumericTextField txtCantColoresHasta;
	private DecimalNumericTextField txtCoberturaDesde;
	private DecimalNumericTextField txtCoberturaHasta;
	
	private List<GamaColor> bases;

	private GamaColorFacadeRemote gamaFacade;
	private JPanel panDatosPropios;

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		this(padre, cliente, tipoProducto, new DefinicionPrecio());
	}

	public JDialogAgregarModificarDefinicionPreciosEstampado(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
		bases = getGamaFacade().getAllOrderByName();
		GuiUtil.llenarCombo(cmbBase, bases, true);
		setModoEdicion(true);
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
			panDatosPropios.add(new JLabel("Cantidad de colores desde : "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCantColoresDesde(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Cantidad de colores Hasta : "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCantColoresHasta(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Cobertura desde (%): "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCoberturaDesde(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
			panDatosPropios.add(new JLabel("Cobertura Hasta (%): "), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 1, 1, 0, 0));
			panDatosPropios.add(getTxtCoberturaHasta(), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5,5,5,5), 1, 1, 1, 1));
		}
		return panDatosPropios;
	}

	@Override
	protected PanelTablaRango<RangoAnchoArticuloEstampado, RangoCoberturaEstampado> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloEstampado, RangoCoberturaEstampado> parent) {
		return new PanelTablaRangoEstampado(parent);
	}

	private JComboBox getCmbBase() {
		if (cmbBase == null) {
			cmbBase = new JComboBox();
		}
		return cmbBase;
	}

	private DecimalNumericTextField getTxtCantColoresDesde() {
		if (txtCantColoresDesde == null) {
			txtCantColoresDesde = new DecimalNumericTextField(new Integer(0) , new Integer(0));
			txtCantColoresDesde.setSize(100, 20);
		}
		return txtCantColoresDesde;
	}

	private DecimalNumericTextField getTxtCantColoresHasta() {
		if (txtCantColoresHasta == null) {
			txtCantColoresHasta = new DecimalNumericTextField(new Integer(0) , new Integer(0));
		}
		return txtCantColoresHasta;
	}
	
	private DecimalNumericTextField getTxtCoberturaDesde() {
		if (txtCoberturaDesde == null) {
			txtCoberturaDesde = new DecimalNumericTextField(new Integer(0) , new Integer(0));
			txtCoberturaDesde.setSize(100, 20);
			txtCoberturaDesde.setValue(1d);
			txtCoberturaDesde.setText("1");
		}
		return txtCoberturaDesde;
	}

	private DecimalNumericTextField getTxtCoberturaHasta() {
		if (txtCoberturaHasta == null) {
			txtCoberturaHasta = new DecimalNumericTextField(new Integer(0) , new Integer(0));
			txtCoberturaHasta.setValue(50d);
			txtCoberturaHasta.setText("50");
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

		if(elemSiendoEditado != null) {
			elemSiendoEditado.deepRemove();
		}

		//Definicion
		DefinicionPrecio definicion = getDefinicion();
		if(definicion == null) {
			setDefinicion(new DefinicionPrecio());
		}
		//Rango
		Float min = getAnchoInicial();
		Float max = getAnchoFinal();
		Float exacto = null;
		if(getChkAnchoExacto().isSelected()) {
			exacto = getAnchoExacto();
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
		TipoArticulo ta = getTipoArticulo();
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
		Float precio = getPrecio();
		
		RangoCoberturaEstampado rangoCobertura = null;
		if(elemSiendoEditado == null) {
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

		getDefinicion().deepOrderBy();
		
		List<RangoAnchoArticuloEstampado> rangosList = new ArrayList<RangoAnchoArticuloEstampado>();
		for(RangoAncho r : getDefinicion().getRangos()) {
			rangosList.add((RangoAnchoArticuloEstampado)r);
		}
		
		//agrego a la tabla
		getTablaRango().limpiar();
		getTablaRango().agregarElementos(rangosList);
		
		getTablaRango().selectElement(rangoCobertura);
		getTablaRango().setTextoBotonGuardar("Agregar");
	}

	@Override
	protected boolean validar() {
		if(validarDatosComunes()) {
			//Base
			if(getCmbBase().getSelectedItem() == null) {
				CLJOptionPane.showErrorMessage(this, "Debe seleccionar una 'Base'.", "Error");
				return false;
			}
			GamaColor base = (GamaColor)getCmbBase().getSelectedItem();
			//Rango Cantidad de colores
			boolean validarRangoCantColores = validarRango(getTxtCantColoresDesde(), "Color Desde", getTxtCantColoresHasta(), "Color Hasta", false);
			if(!validarRangoCantColores) {
				return false;
			}
			//Rango Cobertura
			boolean validarRangoCobertura = validarRango(getTxtCoberturaDesde(), "Cobertura Desde", getTxtCoberturaHasta(), "Cobertura Hasta", false);
			if(!validarRangoCobertura) {
				return false;
			}
			//Rango Ancho Articulo
			RangoAncho rangoAnchoArticuloExistente = getDefinicion().getRango(getAnchoInicial(), getAnchoFinal(), getAnchoExacto());

			//Rango Cantidad de Colores
			RangoCantidadColores rangoCantColoresSiendoEditado = null;
			Integer minCantColores = Integer.valueOf(getTxtCantColoresDesde().getText());
			Integer maxCantColores = Integer.valueOf(getTxtCantColoresHasta().getText());
			RangoCantidadColores rangoCantColoresExistente = null;
			if(elemSiendoEditado != null) {
				rangoCantColoresSiendoEditado = elemSiendoEditado.getRangoCantidadColores();
			}
			if(rangoAnchoArticuloExistente != null) {
				GrupoTipoArticuloBaseEstampado grupo = ((RangoAnchoArticuloEstampado)rangoAnchoArticuloExistente).getGrupo(getTipoArticulo());
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
					if(rangoCoberturaExistente != null && (elemSiendoEditado == null || elemSiendoEditado!=rangoCoberturaExistente)) {
						CLJOptionPane.showErrorMessage(this, "Rango Cobertura Existente", "Error");
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
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

	@Override
	public void setElemHojaSiendoEditado(RangoCoberturaEstampado rangoCoberturaEstampadoSiendoEditado, boolean modoEdicion) {
		this.elemSiendoEditado = rangoCoberturaEstampadoSiendoEditado;

		setModoEdicion(modoEdicion);

		getTxtPrecio().setValue(rangoCoberturaEstampadoSiendoEditado.getPrecio().doubleValue());
		getTxtCoberturaDesde().setValue(rangoCoberturaEstampadoSiendoEditado.getMinimo().doubleValue());
		getTxtCoberturaHasta().setValue(rangoCoberturaEstampadoSiendoEditado.getMaximo().doubleValue());

		RangoCantidadColores rangoCantidadColores = rangoCoberturaEstampadoSiendoEditado.getRangoCantidadColores();
		getTxtCantColoresDesde().setValue(rangoCantidadColores.getMinimo().doubleValue());
		getTxtCantColoresHasta().setValue(rangoCantidadColores.getMaximo().doubleValue());

		PrecioBaseEstampado precioBase = rangoCantidadColores.getPrecioBase();
		getCmbBase().setSelectedItem(precioBase.getGama());

		GrupoTipoArticuloBaseEstampado grupoTipoArticuloBase = precioBase.getGrupoTipoArticuloBase();
		getCmbTipoArticulo().setSelectedItem(grupoTipoArticuloBase.getTipoArticulo());

		RangoAnchoArticuloEstampado rangoAnchoArticulo = grupoTipoArticuloBase.getRangoAnchoArticulo();
		getTxtAnchoInicial().setValue(rangoAnchoArticulo.getAnchoMinimo() == null ? null : rangoAnchoArticulo.getAnchoMinimo().doubleValue());
		getTxtAnchoFinal().setValue(rangoAnchoArticulo.getAnchoMaximo() == null ? null :rangoAnchoArticulo.getAnchoMaximo().doubleValue());
		if(rangoAnchoArticulo.getAnchoExacto() != null) {
			getTxtAnchoExacto().setValue(rangoAnchoArticulo.getAnchoExacto().doubleValue());
			getChkAnchoExacto().setSelected(true);
		} else {
			getTxtAnchoExacto().setValue(null);
			getChkAnchoExacto().setSelected(false);
		}
	}

	@Override
	public RangoAnchoArticuloEstampado getRangoAnchoFromElemSiendoEditado() {
		if(elemSiendoEditado != null) {
			return elemSiendoEditado.getRangoCantidadColores().getPrecioBase().getGrupoTipoArticuloBase().getRangoAnchoArticulo();
		}
		return null;
	}

}