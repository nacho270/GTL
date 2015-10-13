package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfsanciones;

import java.awt.Frame;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionCartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.IEstadoSancionVisitor;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionCreadaEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionEnviadaEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionImpresaEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionJustificadaEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionNoRecibidaEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionRecibidaEstado;
import ar.com.textillevel.modulos.personal.facade.api.remote.SancionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class WorkflowEstadoSancionVisitor implements IEstadoSancionVisitor {

	private Frame padre;
	private CartaDocumento cartaDocumento;
	private String usuario;
	private SancionFacadeRemote sancionFacade;
	
	public WorkflowEstadoSancionVisitor(Frame padre, CartaDocumento cartaDocumento, String usuario) {
		this.padre = padre;
		this.cartaDocumento = cartaDocumento;
		this.usuario = usuario;
		sancionFacade = GTLPersonalBeanFactory.getInstance().getBean2(SancionFacadeRemote.class);
	}

	public void visit(SancionCreadaEstado sce) {
		throw new UnsupportedOperationException();
	}

	public void visit(SancionImpresaEstado sie) {
		cartaDocumento.setEstadoCD(sie.getEnumEstado());
		AccionCartaDocumento acd = new AccionCartaDocumento();
		acd.setEstadoCD(sie.getEnumEstado());
		acd.setUsuario(usuario);
		acd.setSancion(cartaDocumento);
		sancionFacade.updateSancionAndSaveAccionHistorica(cartaDocumento, acd);
	}

	public void visit(SancionEnviadaEstado see) {
		String nroCartaDoc = JOptionPane.showInputDialog(padre, "Ingrese el Número de Carta Documento:");
		if(!StringUtil.isNullOrEmpty(nroCartaDoc)) {
			cartaDocumento.setNroCartaDoc(nroCartaDoc);
			cartaDocumento.setEstadoCD(see.getEnumEstado());
			AccionCartaDocumento acd = new AccionCartaDocumento();
			acd.setEstadoCD(see.getEnumEstado());
			acd.setUsuario(usuario);
			acd.setSancion(cartaDocumento);
			sancionFacade.updateSancionAndSaveAccionHistorica(cartaDocumento, acd);
		}
	}

	public void visit(SancionRecibidaEstado see) {
		JDialogInputFechaHoraRecepcionCD dialogo = new JDialogInputFechaHoraRecepcionCD(padre);
		dialogo.setVisible(true);
		Timestamp fechaHora = dialogo.getFechaHora();
		if(fechaHora != null) {
			if(!DateUtil.getAhora().after(fechaHora)) {
				FWJOptionPane.showErrorMessage(padre, "La fecha y hora de recepción debe ser menor a la fecha y hora actual.", "Error");
				return;
			}
			cartaDocumento.setEstadoCD(see.getEnumEstado());
			cartaDocumento.setFechaHoraRecepcion(fechaHora);
			AccionCartaDocumento acd = new AccionCartaDocumento();
			acd.setEstadoCD(see.getEnumEstado());
			acd.setUsuario(usuario);
			acd.setSancion(cartaDocumento);
			sancionFacade.updateSancionAndSaveAccionHistorica(cartaDocumento, acd);
		}
	}

	public void visit(SancionJustificadaEstado sje) {
		cartaDocumento.setEstadoCD(sje.getEnumEstado());
		AccionCartaDocumento acd = new AccionCartaDocumento();
		acd.setEstadoCD(sje.getEnumEstado());
		acd.setUsuario(usuario);
		acd.setSancion(cartaDocumento);
		sancionFacade.updateSancionAndSaveAccionHistorica(cartaDocumento, acd);
	}

	public void visit(SancionNoRecibidaEstado snre) {
		cartaDocumento.setEstadoCD(snre.getEnumEstado());
		AccionCartaDocumento acd = new AccionCartaDocumento();
		acd.setEstadoCD(snre.getEnumEstado());
		acd.setUsuario(usuario);
		acd.setSancion(cartaDocumento);
		sancionFacade.updateSancionAndSaveAccionHistorica(cartaDocumento, acd);
	}

}