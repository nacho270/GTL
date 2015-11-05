package ar.com.textillevel.gui.modulos.abm.listaprecios.comun;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class JDialogAgregarModificarDefinicionPreciosComun extends JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun, PrecioTipoArticulo> {

	private static final long serialVersionUID = -6851805146971694269L;

	public JDialogAgregarModificarDefinicionPreciosComun(Frame padre, Cliente cliente, ETipoProducto tipoProducto) {
		super(padre, cliente, tipoProducto);
		setModoEdicion(true);
	}

	public JDialogAgregarModificarDefinicionPreciosComun(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
		setModoEdicion(true);
	}

	@Override
	protected JPanel createPanelDatosEspecificos() {
		return new JPanel();
	}

	@Override
	protected PanelTablaRango<RangoAnchoComun, PrecioTipoArticulo> createPanelTabla(JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun, PrecioTipoArticulo> parent) {
		return new PanelTablaRangoComun(parent);
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
		RangoAnchoComun rango = (RangoAnchoComun)definicion.getRango(min, max, exacto);
		if(rango == null) {
			rango = new RangoAnchoComun();
			rango.setAnchoExacto(exacto);
			rango.setAnchoMinimo(min);
			rango.setAnchoMaximo(max);
			getDefinicion().getRangos().add(rango);
			rango.setDefinicionPrecio(definicion);
		}
		//Precio Tipo Articulo
		PrecioTipoArticulo precioTipoArticulo = null;
		TipoArticulo ta = getTipoArticulo();
		Articulo art = getArticulo();
		if(elemSiendoEditado == null) {
			precioTipoArticulo = rango.getPrecioArticulo(ta, art, PrecioTipoArticulo.class);
		}
		if(precioTipoArticulo == null || (precioTipoArticulo.getArticulo() == null && art != null)) {
			precioTipoArticulo = new PrecioTipoArticulo();
			precioTipoArticulo.setRangoAncho(rango);
			precioTipoArticulo.setArticulo(art);
			precioTipoArticulo.setPrecio(getPrecio());
			precioTipoArticulo.setTipoArticulo(getTipoArticulo());
			rango.getPrecios().add(precioTipoArticulo);
		}

		getDefinicion().deepOrderBy();

		List<RangoAnchoComun> rangosList = new ArrayList<RangoAnchoComun>();
		for(RangoAncho r : getDefinicion().getRangos()) {
			rangosList.add((RangoAnchoComun)r);
		}

		//agrego a la tabla
		getTablaRango().limpiar();
		getTablaRango().agregarElementos(rangosList);
		getTablaRango().selectElement(precioTipoArticulo);
		getTablaRango().setTextoBotonGuardar("Agregar");
	}

	@Override
	protected boolean validar() {
		if(validarDatosComunes(true)) {
			RangoAnchoComun rangoAnchoComun = (RangoAnchoComun)getDefinicion().getRango(getAnchoInicial(), getAnchoFinal(), getAnchoExacto());
			if(rangoAnchoComun != null) {
				Articulo art = getArticulo();
				PrecioTipoArticulo pta = rangoAnchoComun.getPrecioArticulo(getTipoArticulo(),art, PrecioTipoArticulo.class);
				if(pta != null && ( (pta.getArticulo() == null && art == null) || (pta.getArticulo() != null && art != null)) 
						&& (elemSiendoEditado == null  || !elemSiendoEditado.equals(pta))) {
					FWJOptionPane.showErrorMessage(this, "Ya existe un precio para ese tipo de artículo.", "Error");
					getTxtPrecio().requestFocus();
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setModoEdicionExtended(boolean modoEdicion) {
	}

	@Override
	protected void limpiarDatosExtended() {
	}

	@Override
	public void setElemHojaSiendoEditado(PrecioTipoArticulo elemHoja, boolean modoEdicion) {
		this.elemSiendoEditado = elemHoja;
		setModoEdicion(modoEdicion);
		RangoAnchoComun rangoAnchoComun = elemHoja.getRangoAncho();
		getTxtAnchoInicial().setValue(rangoAnchoComun.getAnchoMinimo() == null ? null : rangoAnchoComun.getAnchoMinimo().doubleValue());
		getTxtAnchoFinal().setValue(rangoAnchoComun.getAnchoMaximo() == null ? null :rangoAnchoComun.getAnchoMaximo().doubleValue());
		if(rangoAnchoComun.getAnchoExacto() != null) {
			getTxtAnchoExacto().setValue(rangoAnchoComun.getAnchoExacto().doubleValue());
			getChkAnchoExacto().setSelected(true);
		} else {
			getTxtAnchoExacto().setValue(null);
			getChkAnchoExacto().setSelected(false);
		}
		getCmbArticulo().setSelectedItem(elemHoja.getArticulo());
		getCmbTipoArticulo().setSelectedItem(elemHoja.getTipoArticulo());
		getTxtPrecio().setValue(elemHoja.getPrecio().doubleValue());
	}

	@Override
	public RangoAnchoComun getRangoAnchoFromElemSiendoEditado() {
		if(elemSiendoEditado != null) {
			return elemSiendoEditado.getRangoAncho();
		}
		return null;
	}

}