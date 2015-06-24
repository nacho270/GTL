package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Remote
public interface DocumentoContableFacadeRemote {

	public Integer getProximoNroDocumentoContable(EPosicionIVA posIva) ;

}
