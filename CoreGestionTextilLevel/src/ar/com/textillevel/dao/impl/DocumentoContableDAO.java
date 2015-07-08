package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.DocumentoContableDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;

@Stateless
public class DocumentoContableDAO extends GenericDAO<DocumentoContableCliente, Integer> implements DocumentoContableDAOLocal {

	@SuppressWarnings("unchecked")
	public List<DocumentoContableCliente> getAllSinCAE(Integer nroSucursal) {
		return getEntityManager().createQuery(
				  " SELECT d FROM DocumentoContableCliente d "
				  + "	JOIN FETCH d.cliente "
				+ "	WHERE d.caeAFIP IS NULL AND d.nroSucursal = :nroSucursal "
				+ " ORDER BY d.nroFactura ASC ")
				.setParameter("nroSucursal", nroSucursal).getResultList();
	}
}
