package ar.com.textillevel.facade.impl;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.DibujoEstampadoDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaDibujoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntradaDibujo;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.facade.api.local.RemitoEntradaDibujoFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.RemitoEntradaDibujoFacadeRemote;

@Stateless
public class RemitoEntradaDibujoFacade implements RemitoEntradaDibujoFacadeLocal, RemitoEntradaDibujoFacadeRemote {	

	@EJB
	private RemitoEntradaDibujoDAOLocal reDibujoDAO;

	@EJB
	private AuditoriaFacadeLocal<RemitoEntradaDibujo> auditoriaFacade;
	
	@EJB
	private DibujoEstampadoDAOLocal dibujoDAO;

	@Override
	public List<RemitoEntradaDibujo> getRemitosEntradaDibujo(Date fechaDesde, Date fechaHasta, Integer idCliente) {
		return reDibujoDAO.getRemitosEntradaDibujo(fechaDesde, fechaHasta, idCliente);
	}

	@Override
	public RemitoEntradaDibujo grabarREDibujo(RemitoEntradaDibujo red, String usrName) {
		//check precondiciones
		checkREDibujo(red);
		//seteo los datos
		for(DibujoEstampado d : red.getDibujos()) {
			d.setEstado(EEstadoDibujo.EN_STOCK);
			d.setCliente(red.getCliente());
		}
		boolean isAlta = red.getId() == null;
		if(isAlta) {
			red.setFechaIngreso(DateUtil.getAhora());
		}
		//persist + auditoria
		RemitoEntradaDibujo redSaved = reDibujoDAO.save(red);
		if(isAlta) {
			auditoriaFacade.auditar(usrName, "ALTA RE DIB CLI: " + red.getCliente().getNroCliente() + (red.getFactura() == null ? "":" FC: " + red.getFactura()) +  "| ITEMS: " + StringUtil.getCadena(redSaved.getItems(), "||"), EnumTipoEvento.ALTA, red);
		} else {
			auditoriaFacade.auditar(usrName, "MODIF RE DIB CLI: " + red.getCliente().getNroCliente() + (red.getFactura() == null ? "":" FC: " + red.getFactura()) +  "| ITEMS: " + StringUtil.getCadena(redSaved.getItems(), "||"), EnumTipoEvento.MODIFICACION, red);
		}
		return redSaved;
	}

	private void checkREDibujo(RemitoEntradaDibujo red) {
		Set<Cliente> clienteSet = new HashSet<Cliente>();
		for(DibujoEstampado d : red.getDibujos()) {
			if(d.getCliente() != null && !d.getCliente().equals(red.getCliente())) {
				throw new IllegalArgumentException("El dibujo " + d + " no pertenece al cliente " + red.getCliente());
			}
			if(d.getId() != null && d.getEstado() != EEstadoDibujo.DEVUELTO) {
				throw new IllegalArgumentException("El dibujo " + d + " no está en estado " + EEstadoDibujo.DEVUELTO);
			}
			clienteSet.add(d.getCliente());
		}
		if(clienteSet.size() != 1) {
			throw new IllegalArgumentException("Los dibujos no son todos del mismo cliente");
		}
	}

	@Override
	public RemitoEntradaDibujo getByIdEager(Integer id) {
		return reDibujoDAO.getByIdEager(id);
	}

	@Override
	public void borrarREDibujoRelacionadoFC(Factura factura) {
		reDibujoDAO.borrarREDibujoRelacionadoFC(factura);
	}

	@Override
	public RemitoEntradaDibujo getByFCRelacionada(Factura factura) {
		return reDibujoDAO.getByFCRelacionada(factura);
	}

}