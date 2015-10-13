package ar.com.fwcommon.mail;

public class DestinatarioRequest {

    private int tipoDestinatario;
    private String direccionMail;
    public static final int PARA = 0;
    public static final int CC = 1;
    public static final int CCO = 2;

    public DestinatarioRequest() {
    }

    public DestinatarioRequest(int tipoDestinatario, String direccionMail) {
        this.tipoDestinatario = tipoDestinatario;
        this.direccionMail = direccionMail;
    }

    public int getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(int tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public String getDireccionMail() {
        return direccionMail;
    }

    public void setDireccionMail(String direccionMail) {
        this.direccionMail = direccionMail;
    }

}