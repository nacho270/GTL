package ar.com.textillevel.modulos.odt.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;

@Remote
public interface FormulaTenidoClienteFacadeRemote {

	public List<FormulaCliente> getAllDefaultOrByCliente(Cliente cliente);

	public List<FormulaTenidoCliente> getAllByIdClienteOrDefaultAndTipoArticulo(Integer idCliente, TipoArticulo tipoArticulo, Color color);

	public List<FormulaCliente> saveFormulas(List<FormulaCliente> formulasParaGrabar, List<Integer> idsFormulasParaBorrar, boolean grabarTambienComoDefault , String usuario) throws ValidacionException;

}
