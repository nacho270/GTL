package ar.com.textillevel.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.dao.api.local.CotizacionDAOLocal;
import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.DatosAumentoTO;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAncho;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoCantidadColores;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.ListaDePreciosFacadeLocal;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;
import ar.com.textillevel.util.Utils;

@Stateless
public class ListaDePreciosFacade implements ListaDePreciosFacadeRemote, ListaDePreciosFacadeLocal {

	@EJB
	private ListaDePreciosDAOLocal listaDePreciosDAOLocal;

	@EJB
	private ProductoDAOLocal productoDAOLocal;
	
	@EJB
	private CotizacionDAOLocal cotizacionDAOLocal;
	
	@EJB
	private ListaDePreciosFacadeLocal self;
	
	public ListaDePrecios getListaByIdCliente(Integer idCliente) {
		ListaDePrecios listaByIdCliente = listaDePreciosDAOLocal.getListaByIdCliente(idCliente);
		if(listaByIdCliente != null) {
			for(VersionListaDePrecios v : listaByIdCliente.getVersiones()) {
				doEagerVersionListaDePrecios(v);
			}
		}
		return listaByIdCliente;
	}

	public ListaDePrecios save(ListaDePrecios listaDePreciosActual) {
		return listaDePreciosDAOLocal.save(listaDePreciosActual);
	}

	public void remove(ListaDePrecios listaDePreciosActual) {
		listaDePreciosDAOLocal.removeById(listaDePreciosActual.getId());
	}

	public VersionListaDePrecios getVersionCotizadaVigente(Cliente cliente) {
		Cotizacion cotizacion = cotizacionDAOLocal.getUltimaCotizacion(cliente);
		if(cotizacionVigente(cotizacion)) {
			doEagerVersionListaDePrecios(cotizacion.getVersionListaPrecio());
			return cotizacion.getVersionListaPrecio();
		}
		return null;
	}

	private boolean cotizacionVigente(Cotizacion cotizacion) {
		return cotizacion != null && !DateUtil.getHoy().after(DateUtil.sumarDias(cotizacion.getFechaInicio(), cotizacion.getValidez()));
	}

	public Float getPrecioProducto(ProductoArticulo productoArticulo, Cliente cliente) throws ValidacionException {
		DefinicionPrecio definicion = null;
		VersionListaDePrecios versionCotizadaVigente = getVersionCotizadaVigente(cliente);
		if(versionCotizadaVigente != null) {
			definicion = versionCotizadaVigente.getDefinicionPorTipoProducto(productoArticulo.getTipo());
			if(definicion != null) {
				return definicion.getPrecio(productoArticulo);
			}
		}
		VersionListaDePrecios versionActual = getVersionListaPrecioActual(cliente);
		definicion = versionActual.getDefinicionPorTipoProducto(productoArticulo.getTipo());
		if(definicion == null) {
			return null;
		}
		return definicion.getPrecio(productoArticulo);
	}

	public VersionListaDePrecios getVersionListaPrecioActual(Cliente cliente) throws ValidacionException {
		ListaDePrecios lista = getListaByIdCliente(cliente.getId());
		if (lista == null || lista.getVersionActual() == null) {
			throw new ValidacionException(EValidacionException.CLIENTE_SIN_LISTA_PRECIOS.getInfoValidacion());
		}
		return lista.getVersionActual();
	}

	private void doEagerVersionListaDePrecios(VersionListaDePrecios v) {
		v.getPrecios().size();
		for(DefinicionPrecio dp : v.getPrecios()) {
			dp.getRangos().size();
			for (RangoAncho ra : dp.getRangos()) {
				if (ra instanceof RangoAnchoArticuloTenido) {
					RangoAnchoArticuloTenido raat = (RangoAnchoArticuloTenido) ra;
					raat.getGruposGama().size();
					for (GrupoTipoArticuloGama gtag : raat.getGruposGama()) {
						gtag.getPrecios().size();
					}
				} else if (ra instanceof RangoAnchoArticuloEstampado) {
					RangoAnchoArticuloEstampado raae = (RangoAnchoArticuloEstampado) ra;
					raae.getGruposBase().size();
					for (GrupoTipoArticuloBaseEstampado gtabe : raae.getGruposBase()) {
						gtabe.getPrecios().size();
						for (PrecioBaseEstampado pbe : gtabe.getPrecios()) {
							pbe.getRangosDeColores().size();
							for (RangoCantidadColores rcc : pbe.getRangosDeColores()) {
								rcc.getRangos().size();
							}
						}
					}
				} else {
					RangoAnchoComun rac = (RangoAnchoComun) ra;
					rac.getPrecios().size();
				}
			}
		}
	}

	public Cotizacion generarCotizacion(Cliente cliente, VersionListaDePrecios versionListaDePrecios, Integer validez) throws ValidacionException {
		try {
			Cotizacion cotizacion = new Cotizacion();
			cotizacion.setCliente(cliente);
			cotizacion.setFechaInicio(DateUtil.getHoy());
			cotizacion.setValidez(validez);
			cotizacion.setVersionListaPrecio(versionListaDePrecios);
			cotizacion.setNumero(cotizacionDAOLocal.getUltimoNumeroCotizacion() + 1);
			return cotizacionDAOLocal.save(cotizacion);
		} catch(Exception e) {
			if(Utils.isConstraintViolationException(e)) {
				throw new ValidacionException(EValidacionException.COTIZACION_LISTA_DE_PRECIOS_NUMERO_REPETIDO.getInfoValidacion());
			} else {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	public Cotizacion getUltimaCotizacionVigente(VersionListaDePrecios version) {
		Cotizacion cotizacion = cotizacionDAOLocal.getUltimaCotizacionParaVersion(version);
		if(cotizacionVigente(cotizacion)) {
			doEagerVersionListaDePrecios(cotizacion.getVersionListaPrecio());
			return cotizacion;
		} else {
			return null;
		}
	}
	
	public Cotizacion getCotizacionVigente(Cliente cliente) {
		Cotizacion cotizacion = cotizacionDAOLocal.getUltimaCotizacion(cliente);
		if(cotizacionVigente(cotizacion)) {
			doEagerVersionListaDePrecios(cotizacion.getVersionListaPrecio());
			return cotizacion;
		}
		return null;
	}

	public VersionListaDePrecios getVersionActual(Cliente cliente) throws ValidacionException {
		VersionListaDePrecios versionActual = getVersionListaPrecioActual(cliente);
		if (versionActual != null) {
			doEagerVersionListaDePrecios(versionActual);
		}
		return versionActual;
	}

	public List<Cliente> getClientesConListaDePrecios() {
		return listaDePreciosDAOLocal.getClientesConListaDePrecios();
	}

	public void aumentarPrecios(Cliente cliente, Date inicioValidez, List<DatosAumentoTO> datosAumento, boolean actualizarCotizacion) throws ValidacionException {
		ListaDePrecios listaActual = getListaByIdCliente(cliente.getId());
		VersionListaDePrecios versionActual = getVersionListaPrecioActual(cliente);
		VersionListaDePrecios nuevaVersionListaDePrecios = versionActual.deepClone();
		nuevaVersionListaDePrecios.setInicioValidez(inicioValidez);
		for(DatosAumentoTO aumento : datosAumento) {
			DefinicionPrecio definicion = versionActual.getDefinicionPorTipoProducto(aumento.getTipoProducto());
			if (definicion != null) {
				DefinicionPrecio nuevaDefinicion = definicion.deepClone();
				nuevaDefinicion.aumentarPrecios(aumento.getPorcentajeAumento());
				nuevaVersionListaDePrecios.reemplazarDefinicion(nuevaDefinicion);
			}
		}
		listaActual.getVersiones().add(nuevaVersionListaDePrecios);
		listaActual = listaDePreciosDAOLocal.save(listaActual);
		
		if(actualizarCotizacion) {
			nuevaVersionListaDePrecios = listaActual.getVersiones().get(listaActual.getVersiones().size() - 1);
			Cotizacion cotizacion = cotizacionDAOLocal.getUltimaCotizacion(cliente);
			if(cotizacionVigente(cotizacion)) {
				cotizacion.setVersionListaPrecio(nuevaVersionListaDePrecios);
				cotizacionDAOLocal.save(cotizacion);
			}
//			self.actualizarVersionListaDePrecios(cliente, nuevaVersionListaDePrecios);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Cotizacion actualizarVersionListaDePrecios(Cliente cliente, VersionListaDePrecios nuevaVersionListaDePrecios) {
		Cotizacion cotizacion = cotizacionDAOLocal.getUltimaCotizacion(cliente);
		if(cotizacionVigente(cotizacion)) {
			cotizacion.setVersionListaPrecio(nuevaVersionListaDePrecios);
		}
		return cotizacionDAOLocal.save(cotizacion);
	}
}