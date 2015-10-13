package ar.com.fwcommon.componentes;

import java.util.Vector;
import javax.swing.JPanel;

/**
 * This class represents an observable object, or "data" in the model-view
 * paradigm. It can be subclassed to represent an object that the application
 * wants to have observed.
 * <p>
 * An observable object can have one or more observers. An observer may be any
 * object that implements interface <tt>Observer</tt>. After an observable
 * instance changes, an application calling the <code>Observable</code>'s
 * <code>notifyObservers</code> method causes all of its observers to be
 * notified of the change by a call to their <code>update</code> method.
 * <p>
 * The order in which notifications will be delivered is unspecified. The
 * default implementation provided in the Observerable class will notify
 * Observers in the order in which they registered interest, but subclasses may
 * change this order, use no guaranteed order, deliver notifications on separate
 * threaads, or may guarantee that their subclass follows this order, as they
 * choose.
 * <p>
 * Note that this notification mechanism is has nothing to do with threads and
 * is completely separate from the <tt>wait</tt> and <tt>notify</tt>
 * mechanism of class <tt>Object</tt>.
 * <p>
 * When an observable object is newly created, its set of observers is empty.
 * Two observers are considered the same if and only if the <tt>equals</tt>
 * method returns true for them.
 * 
 * @version 1.31, 02/02/00
 * java.util.Observable#notifyObservers()
 * java.util.Observable#notifyObservers(java.lang.Object)
 * java.util.Observer
 * java.util.Observer#update(java.util.Observable, java.lang.Object)
 * @since JDK1.0
 */
public class GuiPanelObservable extends JPanel {

    private boolean changed = false;
    private Vector<GuiPanelObserver> obs;

    /** Construct an Observable with zero Observers. */
    public GuiPanelObservable() {
        obs = new Vector<GuiPanelObserver>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided that
     * it is not the same as some observer already in the set. The order in
     * which notifications will be delivered to multiple observers is not
     * specified. See the class comment.
     * @param o an observer to be added.
     */
    public synchronized void addObserver(GuiPanelObserver o) {
        if(!obs.contains(o)) {
            obs.addElement(o);
        }
        notifyAll();
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * @param o the observer to be deleted.
     */
    public synchronized void deleteObserver(GuiPanelObserver o) {
        obs.removeElement(o);
    }

    /**
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to: <blockquote><tt>
     * notifyObservers(null)</tt>
     * </blockquote>
     * java.util.Observable#clearChanged()
     * java.util.Observable#hasChanged()
     * java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers() {
        notifyObservers(this);
    }

    /**
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     * @param arg any object.
     * java.util.Observable#clearChanged()
     * java.util.Observable#hasChanged()
     * java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers(GuiPanelObservable observable) {
        /*
         * a temporary array buffer, used as a snapshot of the state of current
         * Observers.
         */
        Object[] arrLocal;
        synchronized(this) {
            /*
             * We don't want the Observer doing callbacks into arbitrary code
             * while holding its own Monitor. The code where we extract each
             * Observable from the Vector and store the state of the Observer
             * needs synchronization, but notifying observers does not (should
             * not). The worst result of any potential race-condition here is
             * that: 1) a newly-added Observer will miss a notification in
             * progress 2) a recently unregistered Observer will be wrongly
             * notified when it doesn't care
             */
            if(!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }
        for(int i = arrLocal.length - 1; i >= 0; i--)
            ((GuiPanelObserver)arrLocal[i]).update(observable);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has already
     * notified all of its observers of its most recent change, so that the
     * <tt>hasChanged</tt> method will now return <tt>false</tt>. This
     * method is called automatically by the <code>notifyObservers</code>
     * methods.
     * java.util.Observable#notifyObservers()
     * java.util.Observable#notifyObservers(java.lang.Object)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     * @return <code>true</code> if and only if the <code>setChanged</code>
     *         method has been called more recently than the
     *         <code>clearChanged</code> method on this object;
     *         <code>false</code> otherwise.
     * java.util.Observable#clearChanged()
     * java.util.Observable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }

}