package ar.com.textillevel.entidades.ventas.materiaprima.visitor;

import ar.com.textillevel.entidades.ventas.materiaprima.Cabezal;
import ar.com.textillevel.entidades.ventas.materiaprima.Cilindro;
import ar.com.textillevel.entidades.ventas.materiaprima.IBC;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrimaGenerica;
import ar.com.textillevel.entidades.ventas.materiaprima.MaterialConstruccion;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;

public interface IMateriaPrimaVisitor {
	
	public void visit(MaterialConstruccion materialConstruccion);
	public void visit(Anilina anilina);
	public void visit(Quimico quimico);
	public void visit(Pigmento pigmento);
	public void visit(Tela materiaPrimaTela);
	public void visit(Cilindro cilindro);
	public void visit(Cabezal cabezal);
	public void visit(MateriaPrimaGenerica varios);
	public void visit(IBC ibc);
	public void visit(Reactivo reactivo);
	
}
