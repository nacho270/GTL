package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;

@Local
public interface FormulaTenidoClienteDAOLocal extends DAOLocal<FormulaCliente, Integer> {

	public List<FormulaCliente> getAllDefaultOrByCliente(Cliente cliente);

	public boolean existeFormulaByClienteOrDefault(FormulaCliente formula);

	public List<FormulaTenidoCliente> getAllByIdClienteOrDefaultAndTipoArticulo(Integer idCliente, TipoArticulo tipoArticulo, Color color);

	public <T extends FormulaCliente> Integer getUltimoNroFormula(Class<T> clazz);

}
