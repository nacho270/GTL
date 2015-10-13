package ar.com.textillevel.facade.impl;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.dao.api.local.ListaDePreciosProveedorDAOLocal;
import ar.com.textillevel.entidades.ventas.materiaprima.ListaDePreciosProveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.local.ListaDePreciosProveedorFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.ListaDePreciosProveedorFacadeRemote;

@Stateless
public class ListaDePreciosProveedorFacade implements ListaDePreciosProveedorFacadeRemote, ListaDePreciosProveedorFacadeLocal {

	@EJB
	private ListaDePreciosProveedorDAOLocal listaDePreciosProveedorDAO;

	@EJB
	private AuditoriaFacadeLocal<ListaDePreciosProveedor> auditoriaFacade;

	public ListaDePreciosProveedor getListaByIdProveedor(Integer idProveedor) {
		return listaDePreciosProveedorDAO.getListaByIdProveedor(idProveedor);
	}

	public void remove(ListaDePreciosProveedor listaDePrecios) {
		listaDePreciosProveedorDAO.removeById(listaDePrecios.getId());
	} 

	public ListaDePreciosProveedor save(ListaDePreciosProveedor listaDePrecios, String usuario) {
		Timestamp ahora = DateUtil.getAhora();
		for(PrecioMateriaPrima pmp : listaDePrecios.getPrecios()) {
			if(pmp.getFechaUltModif() == null || pmp.isActualizarHorario()) {
				pmp.setFechaUltModif(ahora);
			}
			pmp.setPreciosProveedor(listaDePrecios);
		}
		auditar(listaDePrecios, usuario);
		return listaDePreciosProveedorDAO.save(listaDePrecios);
	}

	private void auditar(ListaDePreciosProveedor listaDePrecios, String usuario) {
		Integer tipoEvento = null;
		String descripcion = null;
		boolean alta = (listaDePrecios.getId() == null);
		if(alta) {
			tipoEvento = EnumTipoEvento.ALTA;
			descripcion = "Alta de la lista de precios del proveedor '" + listaDePrecios.getProveedor().getRazonSocial() + "'."; 
		} else {
			descripcion = "Modificación de la lista de precios del proveedor '" + listaDePrecios.getProveedor().getRazonSocial() + "'.";
			tipoEvento = EnumTipoEvento.MODIFICACION;
		}
		tipoEvento = alta ? EnumTipoEvento.ALTA : EnumTipoEvento.MODIFICACION;
		auditoriaFacade.auditar(usuario, descripcion, tipoEvento, listaDePrecios);
	}

}