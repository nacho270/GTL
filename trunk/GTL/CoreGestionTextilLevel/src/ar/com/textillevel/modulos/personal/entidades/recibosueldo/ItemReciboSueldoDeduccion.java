package ar.com.textillevel.modulos.personal.entidades.recibosueldo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;

@Entity
@DiscriminatorValue(value="DED")
public class ItemReciboSueldoDeduccion extends ItemReciboSueldo {

	private static final long serialVersionUID = -7934749140120071408L;

	private List<ValeAnticipo> vales;

	public ItemReciboSueldoDeduccion() {
		this.vales = new ArrayList<ValeAnticipo>();
	}

	@OneToMany(cascade=CascadeType.MERGE)
	@JoinColumn(name="F_ITEM_REC_SUELDO_P_ID")
	public List<ValeAnticipo> getVales() {
		return vales;
	}

	public void setVales(List<ValeAnticipo> vales) {
		this.vales = vales;
	}

	@Override
	public void aceptarVisitor(IItemReciboSueldoVisitor visitor) {
		visitor.visit(this);
	}

}