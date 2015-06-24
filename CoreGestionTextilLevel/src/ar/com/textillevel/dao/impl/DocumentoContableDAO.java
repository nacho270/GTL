package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.DocumentoContableDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;

@Stateless
public class DocumentoContableDAO extends GenericDAO<DocumentoContableCliente, Integer> implements DocumentoContableDAOLocal {

}
