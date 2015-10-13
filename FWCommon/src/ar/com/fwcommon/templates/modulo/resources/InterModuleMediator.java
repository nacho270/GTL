package ar.com.fwcommon.templates.modulo.resources;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.templates.modulo.ModuloTemplate;

/**
 * 
 *
 */
public class InterModuleMediator {
	private static final InterModuleMediator instance = new InterModuleMediator();
	private final EventListenerList listeners = new EventListenerList();
	
	private InterModuleMediator() {
		super();
	}
	
	public static InterModuleMediator getInstance() {
		return instance;
	}

	public synchronized void addInterModuleListener(InterModuleListener listener) {
		listeners.add(InterModuleListener.class, listener);
	}
	
	public synchronized void removeInterModuleListener(InterModuleListener listener) {
		listeners.remove(InterModuleListener.class, listener);
	}

	public synchronized void fireInterModuleEvent(final ModuloTemplate<?, ?> modulo, final Class<?> clazz) {
		final InterModuleListener[] l = listeners.getListeners(InterModuleListener.class);
		final ActionEvent e = new ActionEvent(modulo,ActionEvent.ACTION_PERFORMED,clazz.toString());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Thread() {
					@Override
					public void run() {
						for (int i=0; i<l.length; i++) {
							try {
								if (l[i].isAplicableFor(clazz)) {
									l[i].actionPerformed(e);
								}
							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();					
			}
		});
	}
}
