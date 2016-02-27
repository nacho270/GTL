package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.FormulaTenidoClienteDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.IFormulaClienteVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.ReactivoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.aprestado.FormulaAprestadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.PigmentoCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.AnilinaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;
import ar.com.textillevel.modulos.odt.facade.api.remote.FormulaTenidoClienteFacadeRemote;

@Stateless
public class FormulaTenidoClienteFacade implements FormulaTenidoClienteFacadeRemote {

	@EJB
	private FormulaTenidoClienteDAOLocal formulaTenidoClienteDAO;

	@EJB
	private AuditoriaFacadeLocal<FormulaCliente> auditoriaFacade;

	public List<FormulaCliente> getAllDefaultOrByCliente(Cliente cliente) {
		return formulaTenidoClienteDAO.getAllDefaultOrByCliente(cliente);
	}

	public List<FormulaTenidoCliente> getAllByIdClienteOrDefaultAndTipoArticulo(Integer idCliente, TipoArticulo tipoArticulo, Color color) {
		return formulaTenidoClienteDAO.getAllByIdClienteOrDefaultAndTipoArticulo(idCliente,tipoArticulo, color);
	}

	public List<FormulaCliente> saveFormulas(List<FormulaCliente> formulasParaGrabar, List<Integer> idsFormulasParaBorrar, boolean grabarTambienComoDefault, String usuario) throws ValidacionException {
		List<FormulaCliente> result = new ArrayList<FormulaCliente>();
		Cliente cliente = null;
		List<String> nombresFormulasAud = new ArrayList<String>();
		List<String> nombresFormulasDefault = new ArrayList<String>();
		FormulaCliente formulaSave = null;
		
		FormulaCliente formulaEliminar = null;
		//borro las que son para eliminar 
		for(Integer idFormula : idsFormulasParaBorrar) {
			formulaEliminar = formulaTenidoClienteDAO.getById(idFormula);
			cliente = formulaEliminar.getCliente();
			nombresFormulasAud.add(formulaEliminar.getNombre());
			formulaTenidoClienteDAO.removeById(idFormula);
		}
		if(!idsFormulasParaBorrar.isEmpty()) {
			auditoriaFacade.auditar(usuario, "Borrado de Fórmulas [Cliente Nro.  " +  (cliente == null ? "01" : cliente.getNroCliente()) + "]: " + StringUtil.getCadena(nombresFormulasAud, ", "), EnumTipoEvento.BAJA, formulaEliminar);
		}
		nombresFormulasAud.clear();
		
		for(FormulaCliente fc : formulasParaGrabar) {
			cliente = fc.getCliente();
			formulaSave = fc;
			if(formulaTenidoClienteDAO.existeFormulaByClienteOrDefault(fc)) {
				throw new ValidacionException(-1, "Existen nombres de fórmulas repetidas.");
			} else if(fc.getId() == null) {
				//seteo el código
				CalculadorCodigoAndNroFormulaVisitor visitorFormula = new CalculadorCodigoAndNroFormulaVisitor();
				fc.accept(visitorFormula);
				fc.setCodigoFormula(visitorFormula.getCodigoFormula());
				fc.setNroFormula(visitorFormula.getNroFormula());

				result.add(formulaTenidoClienteDAO.save(fc));
				if(grabarTambienComoDefault) {
					CloneFormulaVisitor visitor = new CloneFormulaVisitor();
					fc.accept(visitor);
					FormulaCliente formulaDefault = visitor.getFormulaClonada();
					if(formulaTenidoClienteDAO.existeFormulaByClienteOrDefault(formulaDefault)) {
						throw new ValidacionException(-1, "Existen nombres de fórmulas (cliente 01) repetidas: " + formulaDefault.getNombre());
					} else {
						formulaDefault.accept(visitorFormula);
						formulaDefault.setCodigoFormula(visitorFormula.getCodigoFormula());
						formulaDefault.setNroFormula(visitorFormula.getNroFormula());
						formulaTenidoClienteDAO.save(formulaDefault);
					}
					nombresFormulasDefault.add(formulaDefault.getNombre());
				}
			} else {
				result.add(formulaTenidoClienteDAO.save(fc));
				nombresFormulasAud.add(fc.getNombre());
			}
		}
		if(!nombresFormulasAud.isEmpty()) {
			auditoriaFacade.auditar(usuario, "Grabado de fórmulas [Cliente Nro.  " +  (cliente == null ? "01" : cliente.getNroCliente()) + "]: " + StringUtil.getCadena(nombresFormulasAud, ", "), EnumTipoEvento.ALTA, formulaSave);
		}
		if(!nombresFormulasDefault.isEmpty()) {
			auditoriaFacade.auditar(usuario, "Grabado de fórmulas [Cliente Nro. 01 ]: " + StringUtil.getCadena(nombresFormulasDefault, ", "), EnumTipoEvento.ALTA, formulaSave);
		}
		return result;
	}

	private class CalculadorCodigoAndNroFormulaVisitor implements IFormulaClienteVisitor {

		private String codigoFormula;
		private Integer nroFormula;

		public void visit(FormulaTenidoCliente formulaTenido) {
			nroFormula = formulaTenidoClienteDAO.getUltimoNroFormula(FormulaTenidoCliente.class) + 1;
			codigoFormula = "T" + StringUtil.fillLeftWithZeros(nroFormula.toString(), 5);
		}

		public void visit(FormulaEstampadoCliente formulaEstampado) {
			nroFormula = formulaTenidoClienteDAO.getUltimoNroFormula(FormulaEstampadoCliente.class) + 1;
			codigoFormula = "E" + StringUtil.fillLeftWithZeros(nroFormula.toString(), 5);
		}

		public void visit(FormulaAprestadoCliente formulaAprestado) {
			nroFormula = formulaTenidoClienteDAO.getUltimoNroFormula(FormulaAprestadoCliente.class) + 1;
			codigoFormula = "A" + StringUtil.fillLeftWithZeros(nroFormula.toString(), 5);
		}

		public String getCodigoFormula() {
			return codigoFormula;
		}

		public Integer getNroFormula() {
			return nroFormula;
		}

	}
	
	private class CloneFormulaVisitor implements IFormulaClienteVisitor {
		
		private FormulaCliente formulaClonada;

		public void visit(FormulaTenidoCliente formulaTenido) {
			FormulaTenidoCliente fcInternalClonada = new FormulaTenidoCliente();
			fcInternalClonada.setColor(formulaTenido.getColor());
			fcInternalClonada.setNombre(formulaTenido.getNombre());
			fcInternalClonada.setTipoArticulo(formulaTenido.getTipoArticulo());
			for(TenidoTipoArticulo tta : formulaTenido.getTenidosComponentes()) {
				TenidoTipoArticulo ttaClone = new TenidoTipoArticulo();
				ttaClone.setTipoArticulo(tta.getTipoArticulo());
				for(AnilinaCantidad ac : tta.getAnilinasCantidad()) {
					AnilinaCantidad acClone = new AnilinaCantidad();
					acClone.setCantidad(ac.getCantidad());
					acClone.setMateriaPrima(ac.getMateriaPrima());
					acClone.setUnidad(ac.getUnidad());
					ttaClone.getAnilinasCantidad().add(acClone);
				}
				for(ReactivoCantidad rc : tta.getReactivosCantidad()) {
					ReactivoCantidad rcClone = new ReactivoCantidad();
					rcClone.setCantidad(rc.getCantidad());
					rcClone.setMateriaPrima(rc.getMateriaPrima());
					rcClone.setUnidad(rc.getUnidad());
					ttaClone.getReactivosCantidad().add(rcClone);
				}
				fcInternalClonada.getTenidosComponentes().add(ttaClone);
			}
			formulaClonada = fcInternalClonada;
		}

		public void visit(FormulaEstampadoCliente formulaEstampado) {
			FormulaEstampadoCliente fcInternalClonada = new FormulaEstampadoCliente();
			fcInternalClonada.setNombre(formulaEstampado.getNombre());
			fcInternalClonada.setColor(formulaEstampado.getColor());
			for(QuimicoCantidad ac : formulaEstampado.getQuimicos()) {
				QuimicoCantidad qcClone = new QuimicoCantidad();
				qcClone.setCantidad(ac.getCantidad());
				qcClone.setMateriaPrima(ac.getMateriaPrima());
				qcClone.setUnidad(ac.getUnidad());
				fcInternalClonada.getQuimicos().add(qcClone);
			}
			for(ReactivoCantidad rc : formulaEstampado.getReactivos()) {
				ReactivoCantidad rcClone = new ReactivoCantidad();
				rcClone.setCantidad(rc.getCantidad());
				rcClone.setMateriaPrima(rc.getMateriaPrima());
				rcClone.setUnidad(rc.getUnidad());
				fcInternalClonada.getReactivos().add(rcClone);
			}
			for(PigmentoCantidad rc : formulaEstampado.getPigmentos()) {
				PigmentoCantidad pcClone = new PigmentoCantidad();
				pcClone.setCantidad(rc.getCantidad());
				pcClone.setMateriaPrima(rc.getMateriaPrima());
				pcClone.setUnidad(rc.getUnidad());
				fcInternalClonada.getPigmentos().add(pcClone);
			}
			formulaClonada = fcInternalClonada;
		}

		public void visit(FormulaAprestadoCliente formulaAprestado) {
			FormulaAprestadoCliente fcInternalClonada = new FormulaAprestadoCliente();
			fcInternalClonada.setNombre(formulaAprestado.getNombre());
			for(QuimicoCantidad ac : formulaAprestado.getQuimicos()) {
				QuimicoCantidad qcClone = new QuimicoCantidad();
				qcClone.setCantidad(ac.getCantidad());
				qcClone.setMateriaPrima(ac.getMateriaPrima());
				qcClone.setUnidad(ac.getUnidad());
				fcInternalClonada.getQuimicos().add(qcClone);
			}
			formulaClonada = fcInternalClonada;
		}

		public FormulaCliente getFormulaClonada() {
			return formulaClonada;
		}

	}

}