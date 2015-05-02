package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Local
public interface DibujoEstampadoDAOLocal extends DAOLocal<DibujoEstampado, Integer>{

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado);

	public boolean existsNroDibujo(Integer idDibujo, Integer nro);

}
