package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.DestinatarioEmailDAOLocal;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;

@Stateless
public class DestinatarioEmailDAO extends GenericDAO<DestinatarioEmail, String> implements DestinatarioEmailDAOLocal {

}
