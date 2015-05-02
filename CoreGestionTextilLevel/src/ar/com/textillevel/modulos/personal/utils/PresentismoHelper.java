package ar.com.textillevel.modulos.personal.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismo;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoPorAusencia;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoRangoMinutos;
import ar.com.textillevel.modulos.personal.entidades.presentismo.visitor.IDescuentoPresentismoVisitor;
import ar.com.textillevel.modulos.personal.facade.impl.FichadaLegajoFacade.ContadorFaltas;

public class PresentismoHelper {

	private ConfiguracionPresentismo configuracion;

	private final List<DescuentoPresentismoPorAusencia> descuentosPorAusencia;
	private final List<DescuentoPresentismoRangoMinutos> descuentosPorRangoMinutos;

	public PresentismoHelper(ConfiguracionPresentismo conf) {
		setConfiguracion(conf);
		descuentosPorAusencia = new ArrayList<DescuentoPresentismoPorAusencia>();
		descuentosPorRangoMinutos = new ArrayList<DescuentoPresentismoRangoMinutos>();
		splipDescuentos();
	}

	private void splipDescuentos() {
		SplitDescuentosVisitor visitor = new SplitDescuentosVisitor();
		if(getConfiguracion() != null) {
			for (DescuentoPresentismo desc : getConfiguracion().getDescuentos()) {
				desc.visit(visitor);
			}
		}
	}

	private ConfiguracionPresentismo getConfiguracion() {
		return configuracion;
	}

	private void setConfiguracion(ConfiguracionPresentismo configuracion) {
		this.configuracion = configuracion;
	}

	private class SplitDescuentosVisitor implements IDescuentoPresentismoVisitor {

		public void visit(DescuentoPresentismoRangoMinutos descuento) {
			getDescuentosPorRangoMinutos().add(descuento);
		}

		public void visit(DescuentoPresentismoPorAusencia descuento) {
			getDescuentosPorAusencia().add(descuento);
		}
	}

	private List<DescuentoPresentismoPorAusencia> getDescuentosPorAusencia() {
		return descuentosPorAusencia;
	}

	private List<DescuentoPresentismoRangoMinutos> getDescuentosPorRangoMinutos() {
		return descuentosPorRangoMinutos;
	}
	
	public BigDecimal getDescuentoPorMinutosTarde(Integer minutos, ContadorFaltas contador){
		if(minutos>0){
			for(DescuentoPresentismoRangoMinutos desc : getDescuentosPorRangoMinutos()){
				if(desc.perteneceAlRango(minutos)){
					return desc.getPorcentajeDescuento();
				}
			}
			contador.aumentar(1);
			return getDescuentoPorCantidadDeFaltas(contador.getContador());
		}
		return new BigDecimal(0);
	}
	
	public BigDecimal getDescuentoPorCantidadDeFaltas(Integer cantidadDeFaltas){
		if(cantidadDeFaltas>0){
			for(DescuentoPresentismoPorAusencia desc : getDescuentosPorAusencia()){
				if(desc.getCantidadFaltas().equals(cantidadDeFaltas)){
					return desc.getPorcentajeDescuento();
				}
			}
			if(getConfiguracion() == null) {
				return new BigDecimal(0);
			} else {
				return getConfiguracion().getPorcentajeTotal();
			}
		}
		return new BigDecimal(0);
	}
}
