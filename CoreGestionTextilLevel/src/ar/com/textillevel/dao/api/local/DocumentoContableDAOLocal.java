package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;

@Local
public interface DocumentoContableDAOLocal extends DAOLocal<DocumentoContableCliente, Integer> {

	List<DocumentoContableCliente> getAllSinCAE();

}