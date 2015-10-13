package ar.com.fwcommon.entidades;

import java.io.Serializable;

public class Destinatario implements Serializable{

	private static final long serialVersionUID = 1L;
	private int idDestinatario;
    private int tipoDestinatario;
    private String direccionMail;

    /** Método constructor */
    public Destinatario() {
    }

    /**
     * 
     * @param tipoDest DestinatarioRequest.PARA,DestinatarioRequest.CC... 
     * @param email string, ej asdf@mail.com
     */
    public Destinatario(int tipoDest, String email) {
		this.direccionMail = email;
		this.tipoDestinatario = tipoDest;
	}

	/**
     * Devuelve el <b>id</b> del destinatario.
     * @return idDestinatario
     */
    public int getIdDestinatario() {
        return idDestinatario;
    }

    /**
     * Setea el <b>id</b> del destinatario.
     * @param idDestinatario
     */
    public void setIdDestinatario(int idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    /**
     * Devuelve el <b>tipo</b> de destinatario. Puede ser TO = 0 / CC = 1 / CCO = 2.
     * @return tipoDestinatario
     */
    public int getTipoDestinatario() {
        return tipoDestinatario;
    }

    /**
     * Setea el <b>tipo</b> de destinatario. Puede ser PARA = 0 / CC = 1 / CCO = 2.
     * @param tipoDestinatario
     */
    public void setTipoDestinatario(int tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    /**
     * Devuelve la <b>dirección</b> de Mail.
     * @return direccionMail
     */
    public String getDireccionMail() {
        return direccionMail;
    }

    /**
     * Setea la <b>dirección</b> de Mail.
     * @param direccionMail
     */
    public void setDireccionMail(String direccionMail) {
        this.direccionMail = direccionMail;
    }

    public String toString () {
    	return getDireccionMail() ; 
    }
}