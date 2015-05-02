package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;

@Remote
public interface ContenedorMateriaPrimaFacadeRemote {

	public abstract List<ContenedorMateriaPrima> getAllOrderByName();

	public abstract void remove(ContenedorMateriaPrima contenedorActual);

	public abstract ContenedorMateriaPrima guardarContenedor(ContenedorMateriaPrima contenedorActual);

}
