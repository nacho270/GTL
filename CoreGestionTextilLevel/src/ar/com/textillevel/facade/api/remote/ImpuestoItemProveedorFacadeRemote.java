package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;

@Remote
public interface ImpuestoItemProveedorFacadeRemote {

	public ImpuestoItemProveedor save(ImpuestoItemProveedor impuesto);
	public List<ImpuestoItemProveedor> getAllOrderByName();
	public void remove(ImpuestoItemProveedor impuesto);
	public boolean existsOtroImpuestoWithParams(Integer idImpuesto, double porcDescuento, ETipoImpuesto tipoImpuesto, Provincia provincia);

}
