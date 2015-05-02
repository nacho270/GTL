package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.strategy;

import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;

public class CreacionContratoStrategyFactory {
	public static StrategyCreacionContrato crearStrategy(ETipoContrato tipoContrato){
		switch (tipoContrato) {
			case A_PRUEBA:{
				return new StrategyCreacionContratoAPrueba();
			}
			case PLAZO_FIJO:{
				return new StrategyCreacionContratoAPlazoFijo();
			}
			default:{
				break;
			}
		}
		return null;
	}
}
