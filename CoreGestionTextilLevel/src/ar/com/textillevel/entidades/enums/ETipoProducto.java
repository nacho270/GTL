package ar.com.textillevel.entidades.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.textillevel.entidades.ventas.productos.ProductoAprestado;
import ar.com.textillevel.entidades.ventas.productos.ProductoCalandrado;
import ar.com.textillevel.entidades.ventas.productos.ProductoDesmanchado;
import ar.com.textillevel.entidades.ventas.productos.ProductoDesmanchadoYLavado;
import ar.com.textillevel.entidades.ventas.productos.ProductoDevolucion;
import ar.com.textillevel.entidades.ventas.productos.ProductoDoblado;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampado;
import ar.com.textillevel.entidades.ventas.productos.ProductoEstampadoSobreCrudo;
import ar.com.textillevel.entidades.ventas.productos.ProductoFraccionado;
import ar.com.textillevel.entidades.ventas.productos.ProductoImpermeabilizado;
import ar.com.textillevel.entidades.ventas.productos.ProductoLavado;
import ar.com.textillevel.entidades.ventas.productos.ProductoLavadoEspecialYTermofijado;
import ar.com.textillevel.entidades.ventas.productos.ProductoLavadoYTermofijado;
import ar.com.textillevel.entidades.ventas.productos.ProductoPlanchado;
import ar.com.textillevel.entidades.ventas.productos.ProductoReprocesoConCargo;
import ar.com.textillevel.entidades.ventas.productos.ProductoReprocesoSinCargo;
import ar.com.textillevel.entidades.ventas.productos.ProductoRevisado;
import ar.com.textillevel.entidades.ventas.productos.ProductoSandforizado;
import ar.com.textillevel.entidades.ventas.productos.ProductoSuavizado;
import ar.com.textillevel.entidades.ventas.productos.ProductoTenido;
import ar.com.textillevel.entidades.ventas.productos.ProductoTermofijado;
import ar.com.textillevel.entidades.ventas.productos.ProductoVentaTelaCruda;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

public enum ETipoProducto {

	TENIDO(1, "Te�ido", EUnidad.KILOS, ProductoTenido.class.getName() ,ESectorMaquina.SECTOR_HUMEDO), 
	ESTAMPADO(2, "Estampado",EUnidad.METROS, ProductoEstampado.class.getName(), ESectorMaquina.SECTOR_ESTAMPERIA), 
	TERMOFIJADO(3, "Termofijado", EUnidad.METROS, ProductoTermofijado.class.getName() ,ESectorMaquina.SECTOR_SECO), 
	IMPERMEABILIZADO(4, "Impermeabilizado", EUnidad.METROS,ProductoImpermeabilizado.class.getName() ,ESectorMaquina.SECTOR_SECO), 
	PLANCHADO(5, "Planchado", EUnidad.METROS, ProductoPlanchado.class.getName() ,ESectorMaquina.SECTOR_SECO), 
	DOBLADO(6, "Doblado", EUnidad.METROS, ProductoDoblado.class.getName() ,ESectorMaquina.SECTOR_TERMINADO), 
	SUAVIZADO(7, "Suavizado", EUnidad.METROS, ProductoSuavizado.class.getName() ,ESectorMaquina.SECTOR_SECO), 
	CALANDRADO(17, "Calandrado",	EUnidad.METROS, ProductoCalandrado.class.getName(), ESectorMaquina.SECTOR_TERMINADO),
	SANDFORIZADO(18, "Sandforizado", EUnidad.METROS, ProductoSandforizado.class.getName() ,ESectorMaquina.SECTOR_TERMINADO),
	REPROCESO_SIN_CARGO(8, "Reproceso sin cargo", EUnidad.UNIDAD, ProductoReprocesoSinCargo.class.getName(), null),
	DEVOLUCION(9, "Devolucion", EUnidad.UNIDAD, ProductoDevolucion.class.getName() ,null),
	REPROCESO_CON_CARGO(10, "Reproceso con cargo", EUnidad.UNIDAD, ProductoReprocesoConCargo.class.getName() ,null),
	DESMANCHADO_Y_LAVADO(11, "Desmanchado y lavado", EUnidad.KILOS, ProductoDesmanchadoYLavado.class.getName(), ESectorMaquina.SECTOR_HUMEDO),
	DESMANCHADO(12, "Desmanchado", EUnidad.KILOS, ProductoDesmanchado.class.getName(), ESectorMaquina.SECTOR_HUMEDO),
	LAVADO(13, "Lavado", EUnidad.KILOS, ProductoLavado.class.getName(),ESectorMaquina.SECTOR_HUMEDO),
	REVISADO(14, "Revisado", EUnidad.METROS, ProductoRevisado.class.getName(), ESectorMaquina.SECTOR_TERMINADO),
	APRESTADO(15, "Aprestado", EUnidad.METROS, ProductoAprestado.class.getName(), ESectorMaquina.SECTOR_SECO),
	DESCRUDE(16, "Descrude", EUnidad.METROS, ProductoAprestado.class.getName(), ESectorMaquina.SECTOR_HUMEDO),
	ESTAMPADO_SOBRE_CRUDO(17, "Estampado sobre crudo", EUnidad.METROS, ProductoEstampadoSobreCrudo.class.getName(), ESectorMaquina.SECTOR_ESTAMPERIA),
	LAVADO_Y_TERMOFIJADO(18, "Lavado y termofijado", EUnidad.KILOS, ProductoLavadoYTermofijado.class.getName(), ESectorMaquina.SECTOR_HUMEDO),
	LAVADO_ESPECIAL_Y_TERMOFIJADO(19, "Lavado y termofijado (Sacando cascarillas)", EUnidad.KILOS, ProductoLavadoEspecialYTermofijado.class.getName(), ESectorMaquina.SECTOR_HUMEDO),
	VENTA_TELA_CRUDA(20, "Venta tela cruda", EUnidad.METROS, ProductoVentaTelaCruda.class.getName(), null),
	FRACCIONADO(21, "Fraccionado", EUnidad.METROS, ProductoFraccionado.class.getName(), ESectorMaquina.SECTOR_TERMINADO);

	private Integer id;
	private String descripcion;
	private EUnidad unidad;
	private String clase;
	private ESectorMaquina sector;

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	private ETipoProducto(Integer id, String descripcion, EUnidad unidad, String clase, ESectorMaquina sector) {
		this.id = id;
		this.descripcion = descripcion;
		this.unidad = unidad;
		this.clase = clase;
		this.sector = sector;
	}

	public EUnidad getUnidad() {
		return unidad;
	}

	public void setUnidad(EUnidad unidad) {
		this.unidad = unidad;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString(){
		return descripcion.toUpperCase();
	}
	
	public static ETipoProducto getById(Integer id) {
		if (id == null) return null;
		return getKeyMap().get(id);
	}
	
	private static Map<Integer, ETipoProducto> keyMap;
	
	private static Map<Integer, ETipoProducto> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Integer, ETipoProducto>();
			ETipoProducto values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getId(), values[i]);
			}
		}
		return keyMap;
	}

	
	public ESectorMaquina getSector() {
		return sector;
	}

	
	public void setSector(ESectorMaquina sector) {
		this.sector = sector;
	}

	public static List<ETipoProducto> getTipoProductosBySector(ESectorMaquina sector) {
		List<ETipoProducto> tiposProductos = new ArrayList<ETipoProducto>();
		for(ETipoProducto tp : values()) {
			if(tp.getSector() != null && tp.getSector() == sector) {
				tiposProductos.add(tp);
			}
		}
		return tiposProductos;
		
	}

	public static ETipoProducto getByDescripcion(String descripcion) {
		if (descripcion == null) return null;
		return getDescripcionMap().get(descripcion);
	}
	
	private static Map<String, ETipoProducto> descripcionMap;
	
	private static Map<String, ETipoProducto> getDescripcionMap() {
		if (descripcionMap == null) {
			descripcionMap = new HashMap<String, ETipoProducto>();
			ETipoProducto values[] = values();
			for (int i = 0; i < values.length; i++) {
				descripcionMap.put(values[i].getDescripcion(), values[i]);
			}
		}
		return descripcionMap;
	}

	public static ETipoProducto[] valuesSinReprocesoSinTipo(ETipoProducto... tipos) {
		ETipoProducto[] values = values();
		if(tipos.length > values.length) {
			throw new IllegalArgumentException("Mas productos excluidos de los que hay");
		}
		ETipoProducto[] tiposRet = new ETipoProducto[values.length - tipos.length];
		int index = 0;
		for(ETipoProducto tp : values) {
			boolean found = false;
			for(ETipoProducto tpExcluir : tipos) {
				if(tpExcluir == tp) {
					found = true;
					break;
				}
			}
			if(!found) {
				tiposRet[index++] = tp;
			}
		}
		return tiposRet;
	}

	public static boolean dependienteDeArticulo(ETipoProducto tipoProducto) {
		return tipoProducto != REPROCESO_SIN_CARGO; 
	}


	public static List<ETipoProducto> getValuesAsSortedList() {
		List<ETipoProducto> tiposProductos = new ArrayList<ETipoProducto>();
		for(ETipoProducto etp : ETipoProducto.values()) {
			tiposProductos.add(etp);
		}
		
		Collections.sort(tiposProductos, new Comparator<ETipoProducto>() {

			@Override
			public int compare(ETipoProducto o1, ETipoProducto o2) {
				return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());
			}

			
		});
		return tiposProductos;
	}

}