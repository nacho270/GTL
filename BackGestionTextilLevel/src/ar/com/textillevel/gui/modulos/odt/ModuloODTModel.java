package ar.com.textillevel.gui.modulos.odt;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.ModuloModel;
import ar.com.textillevel.gui.acciones.RemitoEntradaBusinessDelegate;
import ar.com.textillevel.gui.modulos.odt.builder.BuilderAccionesODT;
import ar.com.textillevel.gui.modulos.odt.cabecera.ModeloCabeceraODT;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import edu.emory.mathcs.backport.java.util.Collections;

public class ModuloODTModel extends ModuloModel<OrdenDeTrabajo, ModeloCabeceraODT> {
	
	private RemitoEntradaBusinessDelegate delegate;

	private RemitoEntradaBusinessDelegate getDelegate() {
		if(delegate == null) {
			this.delegate = new RemitoEntradaBusinessDelegate();
		}
		return delegate;
	}

	public ModuloODTModel() {
		super();
	}

	public ModuloODTModel(Integer id) throws FWException {
		super(id, BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance(), BuilderAccionesODT.getInstance());
		setTitulo("Administrar Ordenes de trabajo");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrdenDeTrabajo> buscarItems(ModeloCabeceraODT modeloCabecera) {
		try {
			return getDelegate().getOrdenesDeTrabajos(modeloCabecera.getEstadoODT(), modeloCabecera.getFechaDesde(), modeloCabecera.getFechaHasta());
		} catch (RemoteException e) {
			e.printStackTrace();
			FWJOptionPane.showErrorMessage(null, StringW.wordWrap(e.getMessage()), "Error");
			return Collections.emptyList();
		}
	}

}
