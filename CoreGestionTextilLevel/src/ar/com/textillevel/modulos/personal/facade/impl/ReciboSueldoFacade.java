package ar.com.textillevel.modulos.personal.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.EmpleadoDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.ReciboSueldoDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.ValeAnticipoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.BusquedaReciboSueldoParams;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.to.InfoReciboSueltoTO;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.ReciboSueldoHelper;

@Stateless
public class ReciboSueldoFacade implements ReciboSueldoFacadeRemote {

	@EJB
	private EmpleadoDAOLocal empleadoDAO;

	@EJB
	private ReciboSueldoDAOLocal reciboSueldoDAO;

	@EJB
	private ValeAnticipoDAOLocal valeDAO;

	
	public List<InfoReciboSueltoTO> getInfoReciboSueldoTOList(BusquedaReciboSueldoParams params) {
		List<InfoReciboSueltoTO> resultList = new ArrayList<InfoReciboSueltoTO>();
		List<Empleado> empleadoList = empleadoDAO.getAllActivosBySindicato(params.getSindicato(), params.getNroLegajo(), params.getNombreOrApellido());
		for(Empleado e : empleadoList) {
			InfoReciboSueltoTO infoReciboSueltoTO = new InfoReciboSueltoTO();
			infoReciboSueltoTO.setLegajo(e.getLegajo());
			ReciboSueldo rs = reciboSueldoDAO.getReciboSueldo(e.getLegajo(), params.getAnio(), params.getMes(), params.getQuincena());
			infoReciboSueltoTO.setReciboSueldo(rs);
			resultList.add(infoReciboSueltoTO);
		}
		return resultList;
	}

	public ReciboSueldo guardarRecibo(ReciboSueldo reciboSueldo) {
		//actualizo el valor de la hora del legajo
		Empleado e = empleadoDAO.getById(reciboSueldo.getLegajo().getEmpleado().getId());
		e.getLegajo().setValorHora(reciboSueldo.getValorHora());
		empleadoDAO.save(e);
		return reciboSueldoDAO.save(reciboSueldo);
	}

	public ReciboSueldo getByIdEager(Integer idReciboSueldo) {
		return reciboSueldoDAO.getByIdEager(idReciboSueldo);
	}

	public void eliminarReciboSueldo(ReciboSueldo reciboSueldo) {
		reciboSueldo = reciboSueldoDAO.getById(reciboSueldo.getId());
		//Hago undo de los vales de anticipo
		for(ItemReciboSueldo item : reciboSueldo.getItems()) {
			if(item instanceof ItemReciboSueldoDeduccion) {
				ItemReciboSueldoDeduccion itemDed = (ItemReciboSueldoDeduccion)item;
				for(ValeAnticipo vale : itemDed.getVales()) {
					vale.setEstadoValeAnticipo(EEstadoValeAnticipo.A_DESCONTAR);
					valeDAO.save(vale);
				}
			}
		}
		reciboSueldoDAO.removeById(reciboSueldo.getId());
	}

	public ReciboSueldo getReciboSueldoAnterior(ReciboSueldo reciboSueldo) {
		String textoOrdenAnt = ReciboSueldoHelper.getInstance().getTextoOrdenAnterior(reciboSueldo);
		return reciboSueldoDAO.getReciboSueldoByTextoOrden(reciboSueldo.getLegajo().getId(), textoOrdenAnt);
	}

	public ReciboSueldo getReciboSueldoSiguiente(ReciboSueldo reciboSueldo) {
		String textoOrdenSig = ReciboSueldoHelper.getInstance().getTextoOrdenSiguiente(reciboSueldo);
		return reciboSueldoDAO.getReciboSueldoByTextoOrden(reciboSueldo.getLegajo().getId(), textoOrdenSig);
	}

}