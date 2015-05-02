package ar.com.textillevel.gui.util;

import java.util.Comparator;

public final class ToStringComparator<T> implements Comparator<T> {
	public int compare(T o1, T o2) {
		return o1.toString().compareToIgnoreCase(o2.toString());
	}
}
