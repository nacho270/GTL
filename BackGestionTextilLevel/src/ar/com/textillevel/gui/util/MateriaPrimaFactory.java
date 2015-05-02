package ar.com.textillevel.gui.util;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Cabezal;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.IBC;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrimaGenerica;
import ar.com.textillevel.entidades.ventas.materiaprima.MaterialConstruccion;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;

public class MateriaPrimaFactory {
	public static MateriaPrima createMateriaPrima(ETipoMateriaPrima tipoMateriaPrima){
		if(tipoMateriaPrima == ETipoMateriaPrima.ANILINA) return new Anilina();
		if(tipoMateriaPrima == ETipoMateriaPrima.MATERIAL_CONSTRUCCION) return new MaterialConstruccion();
		if(tipoMateriaPrima == ETipoMateriaPrima.PIGMENTO)	return new Pigmento();
		if(tipoMateriaPrima == ETipoMateriaPrima.QUIMICO) return new Quimico();
		if(tipoMateriaPrima == ETipoMateriaPrima.TELA) return new Tela();
		if(tipoMateriaPrima == ETipoMateriaPrima.CILINDRO) return new Cilindro();
		if(tipoMateriaPrima == ETipoMateriaPrima.CABEZAL) return new Cabezal();
		if(tipoMateriaPrima == ETipoMateriaPrima.VARIOS) return new MateriaPrimaGenerica();
		if(tipoMateriaPrima == ETipoMateriaPrima.IBC) return new IBC();
		if(tipoMateriaPrima == ETipoMateriaPrima.REACTIVO) return new Reactivo();
		return null;
	}
}