package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;

@Local
public interface ImpuestoItemDAOLocal extends DAOLocal<ImpuestoItemProveedor , Integer> {

	boolean existsOtroImpuestoWithParams(Integer idImpuesto, double porcDescuento, ETipoImpuesto tipoImpuesto, Provincia provincia);

}
