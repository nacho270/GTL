package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.dao.api.local.FormulaTenidoClienteDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.DoEagerFormulaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.FormulaCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;

@Stateless
@SuppressWarnings("unchecked")
public class FormulaTenidoClienteDAO extends GenericDAO<FormulaCliente, Integer> implements FormulaTenidoClienteDAOLocal {

	public List<FormulaCliente> getAllDefaultOrByCliente(Cliente cliente) {
		String hql = "SELECT f FROM FormulaCliente f " + (cliente == null ? "WHERE f.cliente IS NULL" : "WHERE f.cliente.id = :idCliente");
		Query q = getEntityManager().createQuery(hql);
		if(cliente != null) {
			q.setParameter("idCliente", cliente.getId());
		}
		List<FormulaCliente> formulas = q.getResultList();
		DoEagerFormulaVisitor visitor = new DoEagerFormulaVisitor(); 
		for(FormulaCliente fc : formulas) {
			fc.accept(visitor);
		}
		return formulas;
	}

	public boolean existeFormulaByClienteOrDefault(FormulaCliente formula) {
		String hql = "FROM FormulaCliente f where (:id IS NULL OR f.id != :id) AND f.nombre=:nombre " + (formula.getCliente() == null ? " AND f.cliente IS NULL " :  " AND f.cliente.id = :idCliente");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", formula.getId());
		q.setParameter("nombre", formula.getNombre());
		if(formula.getCliente() != null) {
			q.setParameter("idCliente", formula.getCliente().getId());
		}
		return !q.getResultList().isEmpty();
	}

	public List<FormulaTenidoCliente> getAllByIdClienteOrDefaultAndTipoArticulo(Integer idCliente, TipoArticulo tipoArticulo, Color color) {
		List<FormulaTenidoCliente> formulas = getEntityManager().createQuery("SELECT f FROM FormulaTenidoCliente f WHERE (f.cliente.id = :idCliente OR f.cliente IS NULL) AND f.tipoArticulo.id = :idTipoArticulo AND f.color.id=:idColor").setParameter("idCliente", idCliente).setParameter("idTipoArticulo", tipoArticulo.getId()).setParameter("idColor", color.getId()).getResultList();
		DoEagerFormulaVisitor visitor = new DoEagerFormulaVisitor(); 
		for(FormulaCliente fc : formulas) {
			fc.accept(visitor);
		}
		return formulas;
	}

	public <T extends FormulaCliente> Integer getUltimoNroFormula(Class<T> clazz) {
		Query query = getEntityManager().createQuery("SELECT MAX(f.nroFormula) FROM " + clazz.getName() + " f");
		Number lastNro = (Number)query.getSingleResult();
		return lastNro == null ? 0 : lastNro.intValue();
	}

}