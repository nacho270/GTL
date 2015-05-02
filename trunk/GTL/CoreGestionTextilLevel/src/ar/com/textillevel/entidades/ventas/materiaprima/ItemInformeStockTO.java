package ar.com.textillevel.entidades.ventas.materiaprima;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ItemInformeStockTO implements Serializable {

	private static final long serialVersionUID = 3110238161240416725L;

	private MateriaPrima materiaPrima;
	private BigDecimal stock;
	private String nombreMateriaPrima;
	private List<Integer> idsMateriasPrimas;

	public ItemInformeStockTO() {
		idsMateriasPrimas = new ArrayList<Integer>();
		stock = new BigDecimal(0d);
	}

	public MateriaPrima getMateriaPrima() {
		return materiaPrima;
	}

	public void setMateriaPrima(MateriaPrima materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public List<Integer> getIdsMateriasPrimas() {
		return idsMateriasPrimas;
	}

	public void setIdsMateriasPrimas(List<Integer> idsMateriasPrimas) {
		this.idsMateriasPrimas = idsMateriasPrimas;
	}

	
	public String getNombreMateriaPrima() {
		return nombreMateriaPrima;
	}

	
	public void setNombreMateriaPrima(String nombreMateriaPrima) {
		this.nombreMateriaPrima = nombreMateriaPrima;
	}

}
