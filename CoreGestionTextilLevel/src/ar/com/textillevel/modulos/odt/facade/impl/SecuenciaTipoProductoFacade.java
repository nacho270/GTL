package ar.com.textillevel.modulos.odt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.dao.api.local.SecuenciaTipoProductoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.PasoSecuenciaAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.SecuenciaAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.SecuenciaTipoProductoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ABMSecuenciasModelTO;

@Stateless
public class SecuenciaTipoProductoFacade implements SecuenciaTipoProductoFacadeRemote{

	@EJB
	private SecuenciaTipoProductoDAOLocal secuenciaDao;
	
	public void persistModel(ABMSecuenciasModelTO model) {
		for(SecuenciaTipoProducto s : model.getSecuenciasABorrar()){
			delete(s);
		}
		for(SecuenciaTipoProducto s : model.getSecuenciasAPersistir()){
			save(s);
		}
	}
	
	public SecuenciaTipoProducto save(SecuenciaTipoProducto secuencia) {
		return secuenciaDao.save(secuencia);
	}

	public void delete(SecuenciaTipoProducto secuencia) {
		secuenciaDao.removeById(secuencia.getId());
	}

	public List<SecuenciaTipoProducto> getAllByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente){
		return getAllByTipoProductoYCliente(tipoProducto, cliente, false);
	}
	
	public List<SecuenciaTipoProducto> getAllByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente, Boolean incluirDefault){
		List<SecuenciaTipoProducto> seqs = secuenciaDao.getAllByTipoProductoYCliente(tipoProducto,cliente,incluirDefault,SecuenciaTipoProducto.class);
		if(seqs!=null && !seqs.isEmpty()){
			for(SecuenciaTipoProducto stp : seqs){
				doEager(stp);
			}
		}
		return seqs;
	}
	
	public List<SecuenciaTipoProducto> getAllAbstractByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente, Boolean incluirDefault){
		List<SecuenciaTipoProducto> seqs = getAllByTipoProductoYCliente(tipoProducto,cliente,incluirDefault);
		List<SecuenciaODT> seqs2 = secuenciaDao.getAllByTipoProductoYCliente(tipoProducto,cliente,incluirDefault,SecuenciaODT.class);
		List<SecuenciaTipoProducto> seqRet = new ArrayList<SecuenciaTipoProducto>();
		seqRet.addAll(seqs);
		seqRet.addAll(convert(seqs2));
		return seqRet;
	}
	
	private List<SecuenciaTipoProducto> convert(List<SecuenciaODT> seqs2) {
		List<SecuenciaTipoProducto> seqRet = new ArrayList<SecuenciaTipoProducto>();
		for(SecuenciaODT s : seqs2){
			SecuenciaTipoProducto stp = new SecuenciaTipoProducto();
			stp.setCliente(s.getCliente());
			stp.setNombre(s.getNombre());
			stp.setTipoProducto(s.getTipoProducto());
			for(PasoSecuenciaODT pso : s.getPasos()){
				stp.getPasos().add(pso.toPasoSecuencia());
			}
			seqRet.add(stp);
		}
		return seqRet;
	}

	public List<SecuenciaTipoProducto> getAllByTipoProducto(ETipoProducto tipoProducto) {
		List<SecuenciaTipoProducto> seqs = secuenciaDao.getByTipoProducto(tipoProducto);
		if(seqs!=null && !seqs.isEmpty()){
			for(SecuenciaTipoProducto stp : seqs){
				stp = getByIdEager(stp.getId());
			}
		}
		return seqs;
	}
	
	public SecuenciaTipoProducto getByIdEager(Integer idSecuencia){
		SecuenciaTipoProducto s = secuenciaDao.getById(idSecuencia);
		doEager(s);
		return s;
	}

	private <T extends PasoSecuenciaAbstract<?>> void doEager(SecuenciaAbstract<?,T> stp) {
		if(stp.getCliente()!=null){
			stp.getCliente().getContacto();
		}
		stp.getPasos().size();
		for(T ps : stp.getPasos()){
			ps.getSector().getProcesos().size();
			for(ProcesoTipoMaquina p : ps.getSector().getProcesos()){
				p.getProcedimientos().size();
				for(ProcedimientoTipoArticulo pro : p.getProcedimientos()){
					pro.getPasos().size();
					for(InstruccionProcedimiento i : pro.getPasos()){
						if(i instanceof InstruccionProcedimientoPasadas){
							((InstruccionProcedimientoPasadas)i).getQuimicos().size();
						}
					}
				}
			}
			ps.getProceso().getNombre();
			ps.getSector().getNombre();
			ps.getSubProceso().getNombre();
			ps.getSubProceso().getTipoArticulo().getNombre();
		}
	}
}
