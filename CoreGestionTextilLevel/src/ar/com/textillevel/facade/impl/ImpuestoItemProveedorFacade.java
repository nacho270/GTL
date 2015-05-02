package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ImpuestoItemDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;
import ar.com.textillevel.facade.api.remote.ImpuestoItemProveedorFacadeRemote;

@Stateless
public class ImpuestoItemProveedorFacade implements ImpuestoItemProveedorFacadeRemote {

	@EJB
	private ImpuestoItemDAOLocal impuestoItemDAO; 
	
	public List<ImpuestoItemProveedor> getAllOrderByName() {
		return impuestoItemDAO.getAllOrderBy("nombre");
	}

	public ImpuestoItemProveedor save(ImpuestoItemProveedor impuesto) {
		return impuestoItemDAO.save(impuesto);
	}

	public void remove(ImpuestoItemProveedor impuesto) {
		impuestoItemDAO.removeById(impuesto.getId());
	}

	public boolean existsOtroImpuestoWithParams(Integer idImpuesto, double porcDescuento, ETipoImpuesto tipoImpuesto, Provincia provincia) {
		return impuestoItemDAO.existsOtroImpuestoWithParams(idImpuesto, porcDescuento, tipoImpuesto,provincia);
	}

}
