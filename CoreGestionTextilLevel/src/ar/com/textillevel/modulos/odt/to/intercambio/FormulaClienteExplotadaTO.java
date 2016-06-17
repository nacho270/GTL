package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

public class FormulaClienteExplotadaTO implements Serializable {

	private static final long serialVersionUID = -7948582719654339869L;

	private String tipo;
	private Integer idFormulaDesencadenante; // está federada

	// tenido
	private List<MateriaPrimaCantidadExplotadaTO> anilinas;

	// estampado
	private List<MateriaPrimaCantidadExplotadaTO> pigmentos;
	private List<MateriaPrimaCantidadExplotadaTO> quimicos;

	// Necesario para el WS
	public FormulaClienteExplotadaTO() {

	}

	public FormulaClienteExplotadaTO(FormulaClienteExplotada formula) {
		if (formula instanceof FormulaTenidoClienteExplotada) {
			tipo = "TEN";
			FormulaTenidoClienteExplotada formulaT = (FormulaTenidoClienteExplotada)formula;
			setAnilinas(new ArrayList<MateriaPrimaCantidadExplotadaTO>());
			llenarListaConLista(getAnilinas(), formulaT.getMateriasPrimas());
		} else {
			tipo = "ESTAMP";
			FormulaEstampadoClienteExplotada formulaE = (FormulaEstampadoClienteExplotada)formula;
			setPigmentos(new ArrayList<MateriaPrimaCantidadExplotadaTO>());
			llenarListaConLista(getPigmentos(), formulaE.getPigmentos());
			setQuimicos(new ArrayList<MateriaPrimaCantidadExplotadaTO>());
			llenarListaConLista(getQuimicos(), formulaE.getQuimicos());
		}

	}

	private <T extends Formulable> void llenarListaConLista(List<MateriaPrimaCantidadExplotadaTO> listaParaLlenar, List<MateriaPrimaCantidadExplotada<T>> lista) {
		for(MateriaPrimaCantidadExplotada<T> mpce : lista) {
			listaParaLlenar.add(new MateriaPrimaCantidadExplotadaTO(mpce));
		}
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getIdFormulaDesencadenante() {
		return idFormulaDesencadenante;
	}

	public void setIdFormulaDesencadenante(Integer idFormulaDesencadenante) {
		this.idFormulaDesencadenante = idFormulaDesencadenante;
	}

	public List<MateriaPrimaCantidadExplotadaTO> getAnilinas() {
		return anilinas;
	}

	public void setAnilinas(List<MateriaPrimaCantidadExplotadaTO> anilinas) {
		this.anilinas = anilinas;
	}

	public List<MateriaPrimaCantidadExplotadaTO> getPigmentos() {
		return pigmentos;
	}

	public void setPigmentos(List<MateriaPrimaCantidadExplotadaTO> pigmentos) {
		this.pigmentos = pigmentos;
	}

	public List<MateriaPrimaCantidadExplotadaTO> getQuimicos() {
		return quimicos;
	}

	public void setQuimicos(List<MateriaPrimaCantidadExplotadaTO> quimicos) {
		this.quimicos = quimicos;
	}

}