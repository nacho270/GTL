package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;

@Local
public interface CondicionDeVentaDAOLocal extends DAOLocal<CondicionDeVenta, Integer>{

}
