package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

@Local
public interface DibujoEstampadoDAOLocal extends DAOLocal<DibujoEstampado, Integer> {

	public DibujoEstampado getByIdEager(Integer idDibujoEstampado);

	public boolean existsNroDibujo(Integer idDibujo, Integer nro);

	public List<DibujoEstampado> getByNroClienteYEstado(Integer nroCliente, EEstadoDibujo estadoDibujo);

	public List<DibujoEstampado> getAllByClienteAndClienteDefault(Cliente cliente);

	public void fixHuecosNroDibujo(Integer nroDibujo);

	public void remove2(DibujoEstampado dibujoEstampado);

}
