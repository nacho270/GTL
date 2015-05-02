package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.CondicionDeVentaDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;

@Stateless
public class CondicionDeVentaDAO extends GenericDAO<CondicionDeVenta, Integer> implements CondicionDeVentaDAOLocal{

}
