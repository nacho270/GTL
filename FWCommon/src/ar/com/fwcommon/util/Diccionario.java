package ar.com.fwcommon.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Diccionario<C, V> implements Serializable {

	private static final long serialVersionUID = -4532162015355363078L;
	
	private Hashtable<C, List<V>> hashtable;

    public Diccionario() {
        hashtable = new Hashtable<C, List<V>>();
    }

    public void agregarValor(C clave, V valor) {
        if(hashtable.containsKey(clave)) {
            getValores(clave).add(valor);
        } else {
            List<V> valores = new ArrayList<V>();
            valores.add(valor);
            hashtable.put(clave, valores);
        }
    }

    public void agregarValores(C clave, List<V> valores) {
        if(hashtable.containsKey(clave)) {
            getValores(clave).addAll(valores);
        } else {
            hashtable.put(clave, valores);
        }
    }

    public void agregarValores(Diccionario<C, V> diccionario) {
        for(C clave : diccionario.getClaves()) {
            agregarValores(clave, diccionario.getValores(clave));
        }
    }

    public void eliminarClave(C clave) {
        hashtable.remove(clave);
    }

    public List<V> getValores(C clave) {
        return hashtable.get(clave);
    }

    public List<V> getValores() {
        List<V> valores = new ArrayList<V>();
        for(C clave : hashtable.keySet()) {
            valores.addAll(getValores(clave));
        }
        return valores;
    }

    public List<List<V>> getValoresComoLista() {
        return new ArrayList<List<V>>(hashtable.values());
    }

    public List<C> getClaves() {
        return new ArrayList<C>(hashtable.keySet());
    }

    public int getCantidadValores(C clave) {
        return getValores(clave).size();
    }

    public int getCantidadClaves() {
        return hashtable.keySet().size();
    }

    public boolean existeClave(C clave) {
        return hashtable.containsKey(clave);
    }

    public boolean existeValor(V value) {
        return getValores().contains(value);
    }

    public boolean isVacio() {
        return hashtable.isEmpty();
    }

    public void vaciar() {
        hashtable.clear();
    }

}