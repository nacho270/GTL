package ar.com.fwcommon.auditoria;

/**
 * Interface que debe implementar cualquier objeto que vaya a ser auditado.
 * 
 *
 */
public interface CloneableAuditoria extends Cloneable {

	/**
	 * Sólo es necesario clonar los atributos auditables.<br>
	 * <br>
	 *     public Object deepClone() {<br>
	 *        try {<br>
	 *            CloningExample copy = (CloningExample)super.clone();<br>
	 *            // Por cada atributo auditable no primitivo<br>
	 *            copy.atributoAutditable = this.atributoAuditable.deepClone();<br>
	 *            // Por cada atributo auditable coleccion<br>
	 *            // Clonar (deep copy) la coleccion;<br>
	 *            return copy;<br>
	 *        } catch (CloneNotSupportedException e) {<br>
	 *            throw new Error("This should not occur since we implement Cloneable");<br>
	 *        }<br>
	 *    }<br>
	 * 
	 * @return Una copia (deep copy) del objeto.
	 * @throws AuditoriaException 
	 */
	public Object deepClone() throws AuditoriaException ;

}
