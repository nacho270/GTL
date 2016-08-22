package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.CuentaArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.PrecioMateriaPrimaDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaDAOLocal;
import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.enums.ETipoVentaStock;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.GrupoDetallePiezasFisicasTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Cabezal;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.IBC;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemInformeStockTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrimaGenerica;
import ar.com.textillevel.entidades.ventas.materiaprima.MaterialConstruccion;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.visitor.IMateriaPrimaVisitor;
import ar.com.textillevel.facade.api.local.ListaDePreciosProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;

@Stateless
public class PrecioMateriaPrimaFacade implements PrecioMateriaPrimaFacadeRemote,PrecioMateriaPrimaFacadeLocal {

	@EJB
	private PrecioMateriaPrimaDAOLocal precioMPDao;
	
	@EJB
	private ListaDePreciosProveedorFacadeLocal listaPreciosFacade;
	
	@EJB
	private RemitoEntradaDAOLocal remitoDao;

	@EJB
	private CuentaArticuloDAOLocal cuentaArticuloDao;
	
	public List<PrecioMateriaPrima> getAllOrderByName(Proveedor proveedor) {
		return precioMPDao.getAllByProveedorOrderByMateriaPrima(proveedor);
	}

	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByTipo(ETipoMateriaPrima tipo) {
		return precioMPDao.getPrecioMateriaPrimaByTipo(tipo);
	}
	
	public List<ItemInformeStockTO> getInformeStock(ETipoMateriaPrima tipo){
		List<ItemInformeStockTO> ret = new ArrayList<ItemInformeStockTO>();
		List<PrecioMateriaPrima> preciosMatPrima = precioMPDao.getPrecioMateriaConStockPrimaByTipo(tipo);
		if (preciosMatPrima != null && !preciosMatPrima.isEmpty()) {
			Map<PrecioMateriaPrima, BigDecimal> mapaPrecioMateriaPrimaStock = new HashMap<PrecioMateriaPrima, BigDecimal>();
			for (PrecioMateriaPrima pmp : preciosMatPrima) {
				if (mapaPrecioMateriaPrimaStock.get(pmp) == null) {
					mapaPrecioMateriaPrimaStock.put(pmp, pmp.getStockActual());
				} else {
					BigDecimal stock = mapaPrecioMateriaPrimaStock.get(pmp.getMateriaPrima());
					stock = stock.add(pmp.getStockActual());
					mapaPrecioMateriaPrimaStock.put(pmp, stock);
				}
			}
			
			for (PrecioMateriaPrima mp : mapaPrecioMateriaPrimaStock.keySet()) {
				ItemInformeStockTO to = getToInformeStock(ret, mp.getMateriaPrima());
				if (to != null) {
					to.setStock(to.getStock().add(mapaPrecioMateriaPrimaStock.get(mp)));
					to.getIdsMateriasPrimas().add(mp.getId());
					to.setNombreMateriaPrima(to.getNombreMateriaPrima() + " / " + mp.getMateriaPrima() + " " + mp.getAlias() + " " + 
												(mp.getMateriaPrima() instanceof Anilina?((Anilina)mp.getMateriaPrima()).getColorIndex():""));
				} else {
					ItemInformeStockTO item = new ItemInformeStockTO();
					item.setMateriaPrima(mp.getMateriaPrima());
					item.setNombreMateriaPrima(mp.getMateriaPrima() + " " + mp.getAlias() + " " + 
												(mp.getMateriaPrima() instanceof Anilina?((Anilina)mp.getMateriaPrima()).getColorIndex():""));
					item.setStock(mapaPrecioMateriaPrimaStock.get(mp));
					item.getIdsMateriasPrimas().add(mp.getId());
					ret.add(item);
				}
			}
		}
		return ret;
	}
	

	public List<ItemMateriaPrimaTO> getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima tipo) {
		List<ItemMateriaPrimaTO> ret = new ArrayList<ItemMateriaPrimaTO>();
		List<PrecioMateriaPrima> preciosMatPrima = getPrecioMateriaPrimaByTipo(tipo);
		if (preciosMatPrima != null && !preciosMatPrima.isEmpty()) {
			Map<MateriaPrima, BigDecimal> mapaPrecioMateriaPrimaStock = new HashMap<MateriaPrima, BigDecimal>();
			for (PrecioMateriaPrima pmp : preciosMatPrima) {
				if (mapaPrecioMateriaPrimaStock.get(pmp.getMateriaPrima()) == null) {
					mapaPrecioMateriaPrimaStock.put(pmp.getMateriaPrima(), pmp.getStockActual());
				} else {
					BigDecimal stock = mapaPrecioMateriaPrimaStock.get(pmp.getMateriaPrima());
					stock = stock.add(pmp.getStockActual());
					mapaPrecioMateriaPrimaStock.put(pmp.getMateriaPrima(), stock);
				}
			}

			for (MateriaPrima mp : mapaPrecioMateriaPrimaStock.keySet()) {
				ItemMateriaPrimaTO to = getTo(ret, mp);
				if (to != null) {
					to.setStock(to.getStock().add(mapaPrecioMateriaPrimaStock.get(mp)));
					to.getIdsMateriasPrimas().add(mp.getId());
				} else {
					ItemMateriaPrimaTO item = new ItemMateriaPrimaTO();
					item.setMateriaPrima(mp);
					item.setStock(mapaPrecioMateriaPrimaStock.get(mp));
					item.getIdsMateriasPrimas().add(mp.getId());
					ret.add(item);
				}
			}
		}
		return ret;
	}
	
	private ItemInformeStockTO getToInformeStock(List<ItemInformeStockTO> ret, MateriaPrima mp) {
		if(ret == null || ret.isEmpty()){
			return null;
		}
		ComparacionMateriaPrimaVisitor cmpv = new ComparacionMateriaPrimaVisitor(mp);
		for (ItemInformeStockTO it : ret) {
			it.getMateriaPrima().accept(cmpv);
			if (cmpv.isCoincide()) {
				return it;
			}
		}
		return null;
	}

	private ItemMateriaPrimaTO getTo(List<ItemMateriaPrimaTO> ret, MateriaPrima mp) {
		if(ret == null || ret.isEmpty()){
			return null;
		}
		ComparacionMateriaPrimaVisitor cmpv = new ComparacionMateriaPrimaVisitor(mp);
		for (ItemMateriaPrimaTO it : ret) {
			it.getMateriaPrima().accept(cmpv);
			if (cmpv.isCoincide()) {
				return it;
			}
		}
		return null;
	}

	private class ComparacionMateriaPrimaVisitor implements IMateriaPrimaVisitor {

		private boolean coincide;
		private MateriaPrima param;

		public ComparacionMateriaPrimaVisitor(MateriaPrima param) {
			setParam(param);
		}

		public void visit(MaterialConstruccion materialConstruccion) {
			setCoincide(false);
		}

		public void visit(Anilina anilina) {
			if (getParam().getTipo() == ETipoMateriaPrima.ANILINA) {
				Anilina ani = (Anilina) param;
				if (ani.getColorIndex() != null && anilina.getColorIndex() != null && ani.getColorIndex().equals(anilina.getColorIndex()) && 
					ani.getConcentracion()!=null && anilina.getConcentracion() != null && ani.getConcentracion().equals(anilina.getConcentracion()) && 
					ani.getTipoAnilina() != null && anilina.getTipoAnilina() != null && ani.getTipoAnilina().equals(anilina.getTipoAnilina())) {
					
					setCoincide(true);
				} else {
					setCoincide(false);
				}
			} else {
				setCoincide(false);
			}
		}

		public void visit(Quimico quimico) {
			setCoincide(false);
		}

		public void visit(Pigmento pigmento) {
			setCoincide(false);
		}

		public void visit(Tela materiaPrimaTela) {
			setCoincide(false);
		}
		
		public void visit(Cilindro cilindro) {
			setCoincide(false);
		}

		public void visit(IBC ibc) {
			setCoincide(false);
		}

		public void visit(Cabezal cabezal) {
			if (getParam().getTipo() == ETipoMateriaPrima.CABEZAL) {
				Cabezal cabe = (Cabezal) param;
				if (cabe.getDiametroCabezal().equals(cabezal.getDiametroCabezal())) {
					setCoincide(true);
				} else {
					setCoincide(false);
				}
			} else {
				setCoincide(false);
			}
		}

		public boolean isCoincide() {
			return coincide;
		}

		public void setCoincide(boolean coincide) {
			this.coincide = coincide;
		}

		public MateriaPrima getParam() {
			return param;
		}

		public void setParam(MateriaPrima param) {
			this.param = param;
		}

		public void visit(MateriaPrimaGenerica varios) {
			setCoincide(false);			
		}

		public void visit(Reactivo reactivo) {
			setCoincide(false);
		}

	}

	public PrecioMateriaPrima actualizarStockPrecioMateriaPrima(BigDecimal cantidad, Integer idPrecioMateriaPrima) {
		PrecioMateriaPrima pm = precioMPDao.getById(idPrecioMateriaPrima);
		if (pm.getStockActual() == null) {
			pm.setStockActual(new BigDecimal(0));
		}
		pm.setStockActual(pm.getStockActual().add(cantidad));
		return precioMPDao.save(pm);
	}

	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByIdsMateriasPrimas(List<Integer> idsMateriasPrimas) {
		if(idsMateriasPrimas!=null && !idsMateriasPrimas.isEmpty()){
			return precioMPDao.getPrecioMateriaPrimaByIdsMateriasPrimas(idsMateriasPrimas);
		}
		return null;
	}

	public List<PrecioMateriaPrima> getPreciosMateriaPrimaByTipoVentaStock(ETipoVentaStock tipoVentaStock) {
		return precioMPDao.getPreciosMateriaPrimaByTipoVentaStock(tipoVentaStock);
	}

	public BigDecimal getStockByPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		return precioMPDao.getStockByPrecioMateriaPrima(precioMateriaPrima);
	}

	public PrecioMateriaPrima grabarNuevoPrecioMateriaPrimaYAgregarloAListaDePreciosProveedor(PrecioMateriaPrima precioMateriaPrima, Proveedor proveedor, String usuario) {
		ListaDePreciosProveedor listaPrecios = listaPreciosFacade.getListaByIdProveedor(proveedor.getId());
		if (listaPrecios == null) {
			listaPrecios = new ListaDePreciosProveedor();
			listaPrecios.setProveedor(proveedor);
		}
		precioMateriaPrima.setPreciosProveedor(listaPrecios);
		listaPrecios.getPrecios().add(precioMateriaPrima);
		listaPrecios = listaPreciosFacade.save(listaPrecios, usuario);
		return listaPrecios.getPrecios().get(listaPrecios.getPrecios().size()-1);
	}

	public List<PrecioMateriaPrima> getAllWithStockByProveedorOrderByMateriaPrima(Integer idProveedor) {
		return precioMPDao.getAllWithStockByProveedorOrderByMateriaPrima(idProveedor);
	}

	public List<ItemMateriaPrimaTO> getStockTelasFisicas() {
		List<CuentaArticulo> cuentas = cuentaArticuloDao.getAll();
		Map<Articulo, BigDecimal> mapaCuentas = new HashMap<Articulo, BigDecimal>();
		for(CuentaArticulo c : cuentas){
			Articulo art = c.getArticulo();
			if(mapaCuentas.get(art)==null){
				mapaCuentas.put(art, c.getCantidad());
			}else{
				mapaCuentas.put(art, mapaCuentas.get(art).add(c.getCantidad()));
			}
		}
	
		List<ItemMateriaPrimaTO> lista = getItemsMateriaPrimaByTipoMateriaPrima(ETipoMateriaPrima.TELA);
		Map<Articulo, BigDecimal> mapaStockReal = new HashMap<Articulo, BigDecimal>();
		for(ItemMateriaPrimaTO it : lista){
			Articulo art = ((Tela)it.getMateriaPrima()).getArticulo();
			if(mapaStockReal.get(art) == null){
				mapaStockReal.put(art, it.getStock());
			}else{
				mapaStockReal.put(art, mapaStockReal.get(art).add(it.getStock()));
			}
		}
		Set<Articulo> setArticulos = new HashSet<Articulo>();
		setArticulos.addAll(mapaCuentas.keySet());
		setArticulos.addAll(mapaStockReal.keySet());
		
		List<ItemMateriaPrimaTO> listaRet = new ArrayList<ItemMateriaPrimaTO>();
		for(Articulo art : setArticulos){
			ItemMateriaPrimaTelaTO item = new ItemMateriaPrimaTelaTO();
			BigDecimal stockCuenta = mapaCuentas.get(art)==null?new BigDecimal(0d):mapaCuentas.get(art);
			BigDecimal stockReal = mapaStockReal.get(art)==null?new BigDecimal(0d):mapaStockReal.get(art);
			BigDecimal sumaStock = new BigDecimal(stockCuenta.doubleValue() + stockReal.doubleValue());
			BigDecimal stockCrudo = remitoDao.getStockFisico(art,ETipoTela.CRUDA);
			BigDecimal stockTerminado = remitoDao.getStockFisico(art,ETipoTela.TERMINADA);	
			item.setArticulo(art);
			item.setStockFisico(sumaStock);
			item.setStockCrudo(stockCrudo);
			item.setStockTerminado(stockTerminado);
			listaRet.add(item);
		}
		
		return listaRet;
	}

	public List<PrecioMateriaPrima> getAllWithStockInicialDispByArticulo(Integer idArticulo) {
		return precioMPDao.getAllWithStockInicialDispByArticulo(idArticulo);
	}

	public PrecioMateriaPrima save(PrecioMateriaPrima pmpActualizada) {
		return precioMPDao.save(pmpActualizada);
	}

	public List<GrupoDetallePiezasFisicasTO> getDetallePiezas(Articulo articuloElegido, ETipoTela tipoTelaElegida) {
		List<DetallePiezaFisicaTO> detallePiezas = remitoDao.getDetallePiezas(articuloElegido,tipoTelaElegida);
		List<GrupoDetallePiezasFisicasTO> listaGrupos = new ArrayList<GrupoDetallePiezasFisicasTO>();
		Map<String, List<DetallePiezaFisicaTO>> mapa = new HashMap<String, List<DetallePiezaFisicaTO>>();
		if(tipoTelaElegida == ETipoTela.TERMINADA){
			armarGruposTelaTerminada(detallePiezas, listaGrupos, mapa);
		}else{
			armarGruposTelaCruda(detallePiezas, listaGrupos, mapa);
		}
		return listaGrupos;
	}

	private void armarGruposTelaTerminada(List<DetallePiezaFisicaTO> detallePiezas, List<GrupoDetallePiezasFisicasTO> listaGrupos, Map<String, List<DetallePiezaFisicaTO>> mapa) {
		Collections.sort(detallePiezas, new Comparator<DetallePiezaFisicaTO>() {
			public int compare(DetallePiezaFisicaTO o1, DetallePiezaFisicaTO o2) {
				if(o1.getOdt().equals(o2.getOdt())){
					return o1.getProveedor().compareTo(o2.getProveedor());
				}
				return o1.getOdt().compareTo(o2.getOdt());
			}
		});
		for(DetallePiezaFisicaTO det : detallePiezas){
			String concat = det.getOdt() + "=" +det.getProveedor();
			if(mapa.get(concat)==null){
				mapa.put(concat, new ArrayList<DetallePiezaFisicaTO>());
			}
			mapa.get(concat).add(det);
		}
		for(String concat : mapa.keySet()){
			String [] split = concat.split("=");
			GrupoDetallePiezasFisicasTO grupo = new GrupoDetallePiezasFisicasTO();
			grupo.setOdt(split[0]);
			grupo.setProveedor(split[1]);
			grupo.setPiezasSeleccionadas(new ArrayList<DetallePiezaFisicaTO>());
			grupo.setPiezasTotales(mapa.get(concat));
			listaGrupos.add(grupo);
		}
	}
	
	private void armarGruposTelaCruda(List<DetallePiezaFisicaTO> detallePiezas, List<GrupoDetallePiezasFisicasTO> listaGrupos, Map<String, List<DetallePiezaFisicaTO>> mapa) {
		Collections.sort(detallePiezas, new Comparator<DetallePiezaFisicaTO>() {
			public int compare(DetallePiezaFisicaTO o1, DetallePiezaFisicaTO o2) {
				return o1.getProveedor().compareTo(o2.getProveedor());
			}
		});
		for(DetallePiezaFisicaTO det : detallePiezas){
			String proveedor = det.getProveedor();
			if(mapa.get(proveedor)==null){
				mapa.put(proveedor, new ArrayList<DetallePiezaFisicaTO>());
			}
			mapa.get(proveedor).add(det);
		}
		for(String prov : mapa.keySet()){
			GrupoDetallePiezasFisicasTO grupo = new GrupoDetallePiezasFisicasTO();
			grupo.setProveedor(prov);
			grupo.setPiezasSeleccionadas(new ArrayList<DetallePiezaFisicaTO>());
			grupo.setPiezasTotales(mapa.get(prov));
			listaGrupos.add(grupo);
		}
	}

	public BigDecimal getPrecioMasRecienteTela(Integer idArticulo) {
		return precioMPDao.getPrecioMasRecienteTela(idArticulo);
	}

	public PrecioMateriaPrima getById(Integer id) {
		return precioMPDao.getById(id);
	}

}
