package ar.com.textillevel.entidades.to.informeproduccion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ItemInformeProduccionTO implements Serializable {

	private static final long serialVersionUID = -204956867643321230L;

	private String dia;
	private List<ClienteCantidadTO> clienteCantidadList;

	public ItemInformeProduccionTO(){
		clienteCantidadList = new ArrayList<ClienteCantidadTO>();
	}
	
	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public List<ClienteCantidadTO> getClienteCantidadList() {
		return clienteCantidadList;
	}

	public void setClienteCantidadList(List<ClienteCantidadTO> clienteCantidadList) {
		this.clienteCantidadList = clienteCantidadList;
	}
	
	public JRDataSource getClientesDS(){
		return new JRBeanCollectionDataSource(clienteCantidadList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clienteCantidadList == null) ? 0 : clienteCantidadList.hashCode());
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemInformeProduccionTO other = (ItemInformeProduccionTO) obj;
		if (clienteCantidadList == null) {
			if (other.clienteCantidadList != null)
				return false;
		} else if (!clienteCantidadList.equals(other.clienteCantidadList))
			return false;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		return true;
	}
}
