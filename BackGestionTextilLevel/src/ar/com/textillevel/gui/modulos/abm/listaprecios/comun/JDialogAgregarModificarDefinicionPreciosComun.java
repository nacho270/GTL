package ar.com.textillevel.gui.modulos.abm.listaprecios.comun;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
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
	}

	public JDialogAgregarModificarDefinicionPreciosComun(Frame padre, Cliente cliente, ETipoProducto tipoProducto, DefinicionPrecio definicionAModificar) {
		super(padre, cliente, tipoProducto, definicionAModificar);
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
		if(elemSiendoEditado == null) {
			precioTipoArticulo = rango.getPrecioArticulo(ta);
		}
		if(precioTipoArticulo == null) {
			precioTipoArticulo = new PrecioTipoArticulo();
			precioTipoArticulo.setRangoAncho(rango);
			rango.getPrecios().add(precioTipoArticulo);
		}
		precioTipoArticulo.setPrecio(getPrecio());
		precioTipoArticulo.setTipoArticulo(getTipoArticulo());

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
		if(validarDatosComunes()) {
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
		getTxtAnchoInicial().setText(rangoAnchoComun.getAnchoMinimo() == null ? "" : rangoAnchoComun.getAnchoMinimo().toString());
		getTxtAnchoFinal().setText(rangoAnchoComun.getAnchoMaximo() == null ? "" :rangoAnchoComun.getAnchoMaximo().toString());
		if(rangoAnchoComun.getAnchoExacto() != null) {
			getTxtAnchoExacto().setText(rangoAnchoComun.getAnchoExacto().toString());
			getChkAnchoExacto().setSelected(true);
		} else {
			getTxtAnchoExacto().setText(null);
			getChkAnchoExacto().setSelected(false);
		}
		getCmbTipoArticulo().setSelectedItem(elemHoja.getTipoArticulo());
		getTxtPrecio().setText(elemHoja.getPrecio().toString());
	}

	@Override
	public RangoAnchoComun getRangoAnchoFromElemSiendoEditado() {
		if(elemSiendoEditado != null) {
			return elemSiendoEditado.getRangoAncho();
		}
		return null;
	}

}