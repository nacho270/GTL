package main.acciones.informes;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemInformeStockTO;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogInformeStock;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class InformeStockAction implements Action{

	private final Frame frame;
	
	public InformeStockAction(Frame frame){
		this.frame = frame;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public Object getValue(String key) {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public void putValue(String key, Object value) {
		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		
	}

	public void setEnabled(boolean b) {
		
	}

	public void actionPerformed(ActionEvent e) {
		String[] correcs= new String[ETipoMateriaPrima.values().length+1];
		correcs[0]= "TODAS";
		for(int i = 1 ; i<ETipoMateriaPrima.values().length;i++){
			correcs[i] = ETipoMateriaPrima.values()[i-1].getDescripcion();
		}
		Object opcion = JOptionPane.showInputDialog(null, "Seleccione el tipo de materia prima:", "Lista de materias primas", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
		if(opcion !=null){
			ETipoMateriaPrima tipo = ETipoMateriaPrima.getByDescripcion((String)opcion);
			List<ItemInformeStockTO> informeStock = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getInformeStock(tipo);
			if(informeStock!=null && !informeStock.isEmpty()){
				List<ItemSimpleInformeStockTO> lista = new ArrayList<ItemSimpleInformeStockTO>();
				for(ItemInformeStockTO it : informeStock){
					lista.add(new ItemSimpleInformeStockTO(it.getNombreMateriaPrima(), GenericUtils.getDecimalFormat().format(it.getStock())+ " " + it.getMateriaPrima().getUnidad().getDescripcion()));
				}
				new JDialogInformeStock(frame, lista).setVisible(true);
			}
		}
	}
	
	public class ItemSimpleInformeStockTO implements Serializable{
		
		private static final long serialVersionUID = -1607516225718373609L;

		private String nombre;
		private String stock;
		
		public ItemSimpleInformeStockTO(String nombre, String stock) {
			this.nombre = nombre;
			this.stock = stock;
		}

		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public String getStock() {
			return stock;
		}
		
		public void setStock(String stock) {
			this.stock = stock;
		}
	}
}
