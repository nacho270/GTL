package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfvaleatencion;

import java.awt.Frame;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaParcial;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaVigencia;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAccidente;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValePreocupacional;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ETipoVale;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.Horario;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAccidente;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValePreocupacional;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.IEstadoValeAtencionVisitor;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.ValeAbiertoEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.ValeJustificadoAltaEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.ValeJustificadoControlEstado;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.ValeNoJustificadoAltaEstado;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAtencionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

@SuppressWarnings({"rawtypes"})
public class WorkflowEstadoValeAtencionVisitor implements IEstadoValeAtencionVisitor {

	private Frame padre;
	private ValeAtencion valeAtencion;
	private String usuario;
	private ValeAtencionFacadeRemote valeAtencionFacade;
	
	public WorkflowEstadoValeAtencionVisitor(Frame padre, ValeAtencion valeAtencion, String usuario) {
		this.padre = padre;
		this.valeAtencion = valeAtencion;
		this.usuario = usuario;
		this.valeAtencionFacade = GTLPersonalBeanFactory.getInstance().getBean2(ValeAtencionFacadeRemote.class);
	}

	public void visit(ValeAbiertoEstado vae) {
		throw new UnsupportedOperationException();
	}

	public void visit(ValeJustificadoControlEstado vjce) {
		JDialogInputFecha dialogo = new JDialogInputFecha(padre, "Fecha de Control");
		dialogo.setVisible(true);
		Date fechaControl = dialogo.getFecha();
		if(fechaControl != null) {
			List<AntiFichada> antifichadas = new ArrayList<AntiFichada>();
			Date fechaControlOriginal = valeAtencion.getFechaControl();
			if(fechaControlOriginal == null) {
				antifichadas.addAll(generarAntifichadasParcialAndVigencia(fechaControl));
			} else {
				Date fechaDesde = DateUtil.getManiana(fechaControlOriginal);
				antifichadas.add(generarAntifichadaVigencia(fechaDesde, fechaDesde, fechaControl));
			}
			valeAtencion.setEstadoValeAtencion(vjce.getEnumEstado());
			valeAtencion.setFechaControl(fechaControl);
			AccionValeAtencion acd = createAccion(valeAtencion);
			acd.setEstadoValeAtencion(vjce.getEnumEstado());
			acd.setUsuario(usuario);
			acd.setFechaControl(fechaControl);
			valeAtencionFacade.updateValeAtencionAndSaveAccionHistorica(valeAtencion, acd, antifichadas);
		}
	}

	public void visit(ValeJustificadoAltaEstado vjae) {
		if(valeAtencion.getTipoVale() == ETipoVale.ENFERMEDAD) {
			JDialogInputJustifAltaValEnfermedad dialogo = new JDialogInputJustifAltaValEnfermedad(padre, valeAtencion);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				valeAtencion.setEstadoValeAtencion(vjae.getEnumEstado());
				AccionValeAtencion acd = createAccion(valeAtencion);
				acd.setEstadoValeAtencion(vjae.getEnumEstado());
				acd.setUsuario(usuario);
				List<AntiFichada> antifichadas = generarAntifichadasParcialAndVigencia(DateUtil.getAyerSinRedondear(valeAtencion.getFechaAlta()));
				valeAtencionFacade.updateValeAtencionAndSaveAccionHistorica(valeAtencion, acd, antifichadas);
			}
		} else {
			JDialogInputFecha dialogo = new JDialogInputFecha(padre, "Fecha de Alta");
			dialogo.setVisible(true);
			Date fechaAlta = dialogo.getFecha();
			if(fechaAlta != null) {
				valeAtencion.setEstadoValeAtencion(vjae.getEnumEstado());
				valeAtencion.setFechaAlta(fechaAlta);
				AccionValeAtencion acd = createAccion(valeAtencion);
				acd.setEstadoValeAtencion(vjae.getEnumEstado());
				acd.setUsuario(usuario);
				List<AntiFichada> antifichadas = generarAntifichadasParcialAndVigencia(DateUtil.getAyerSinRedondear(valeAtencion.getFechaAlta()));
				valeAtencionFacade.updateValeAtencionAndSaveAccionHistorica(valeAtencion, acd, antifichadas);
			}
		}
	}

	public void visit(ValeNoJustificadoAltaEstado vnjae) {
		AntiFichada antifichada = null;
		if(valeAtencion.getAsistioAlTrabajo()) {
			AntiFichadaParcial antifichadaParcial = generarAntifichadaParcial(false, false);
			antifichada = antifichadaParcial;
		} else {
			AntiFichadaVigencia antiFichadaVigencia = new AntiFichadaVigencia();
			antiFichadaVigencia.setLegajo(valeAtencion.getLegajo());
			antiFichadaVigencia.setJustificada(false);
			antiFichadaVigencia.setValeAtencion(valeAtencion);
			antiFichadaVigencia.setFechaDesde(valeAtencion.getFechaVale());
			antiFichadaVigencia.setFechaHasta(valeAtencion.getFechaVale());
			antifichada = antiFichadaVigencia;
		}
		
		valeAtencion.setEstadoValeAtencion(vnjae.getEnumEstado());
		AccionValeAtencion acd = createAccion(valeAtencion);
		acd.setEstadoValeAtencion(vnjae.getEnumEstado());
		acd.setUsuario(usuario);
		valeAtencionFacade.updateValeAtencionAndSaveAccionHistorica(valeAtencion, acd, Collections.singletonList(antifichada));
	}

	/**
	 * Genera antifichadas desde el inicio del Vale hasta una fecha hasta. 
	 * Analiza si es necesario crear una parcial, una parcial y vigencia o solamente una vigencia. 
	 * @param fechaHasta
	 * @return la antifichada
	 */
	private List<AntiFichada> generarAntifichadasParcialAndVigencia(Date fechaHasta) {
		List<AntiFichada> antifichadas = new ArrayList<AntiFichada>();
		if(valeAtencion.getAsistioAlTrabajo()) {
			if(fechaHasta.compareTo(valeAtencion.getFechaVale()) <= 0) {
				AntiFichadaParcial antifichadaParcial = generarAntifichadaParcial(false, true);
				antifichadas.add(antifichadaParcial);
			} else {
				AntiFichadaParcial antifichadaParcial = generarAntifichadaParcial(false, true);
				Date fechaDesde = DateUtil.getManiana(valeAtencion.getFechaVale());
				antifichadas.add(antifichadaParcial);
				AntiFichadaVigencia antifichadaVigencia = generarAntifichadaVigencia(fechaDesde, fechaDesde, fechaHasta);
				antifichadas.add(antifichadaVigencia);
			}
		} else {
			AntiFichadaVigencia antifichadaVigencia = generarAntifichadaVigencia(valeAtencion.getFechaVale(), valeAtencion.getFechaVale(), fechaHasta);
			antifichadas.add(antifichadaVigencia);
		}
		return antifichadas;
	}

	/**
	 * Genera una antifichada parcial hasta el horario de salida
	 * @param entrada
	 * @param justificada
	 * @return la antifichada
	 */
	private AntiFichadaParcial generarAntifichadaParcial(Boolean entrada, Boolean justificada) {
		AntiFichadaParcial antifichadaParcial = new AntiFichadaParcial();
		antifichadaParcial.setLegajo(valeAtencion.getLegajo());
		antifichadaParcial.setEntrada(entrada);
		antifichadaParcial.setJustificada(justificada);
		antifichadaParcial.setValeAtencion(valeAtencion);
		Horario horarioSalida = valeAtencion.getHorarioSalida();
		long diaMS = DateUtil.redondearFecha(valeAtencion.getFechaVale()).getTime();
		antifichadaParcial.setFechaHora(new Timestamp(diaMS + DateUtil.ONE_HOUR*horarioSalida.getHoras() + DateUtil.ONE_MINUTE*horarioSalida.getMinutos()));
		return antifichadaParcial;
	}

	/**
	 * Genera una antifichada vigencia [fechaDesdePrioritaria, fechaHasta] o [fechaDesdeAlternativa, fechaHasta]
	 * cuando fechaDesdePrioritaria es <code>null</code>
	 * @param entrada
	 * @param justificada
	 * @return la antifichada
	 */
	private AntiFichadaVigencia generarAntifichadaVigencia(Date fechaDesdePrioritaria, Date fechaDesdeAlternativa, Date fechaHasta) {
		AntiFichadaVigencia antifichadaVigencia = new AntiFichadaVigencia();
		antifichadaVigencia.setLegajo(valeAtencion.getLegajo());
		antifichadaVigencia.setValeAtencion(valeAtencion);
		antifichadaVigencia.setJustificada(true);
		if(fechaDesdePrioritaria == null) {
			antifichadaVigencia.setFechaDesde(fechaDesdeAlternativa);
		} else {
			antifichadaVigencia.setFechaDesde(fechaDesdePrioritaria);
		}
		antifichadaVigencia.setFechaHasta(fechaHasta);
		return antifichadaVigencia;
	}

	private AccionValeAtencion createAccion(ValeAtencion va) {
		if(va instanceof ValeEnfermedad) {
			AccionValeEnfermedad ave = new AccionValeEnfermedad();
			ave.setValeAtencion((ValeEnfermedad)va);
			return ave;
		} else if(va instanceof ValeAccidente) {
			AccionValeAccidente ave = new AccionValeAccidente();
			ave.setValeAtencion((ValeAccidente)va);
			return ave;
		} else {
			AccionValePreocupacional ave = new AccionValePreocupacional();
			ave.setValeAtencion((ValePreocupacional)va);
			return ave;
		}
	}

}