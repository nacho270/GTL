package ar.com.fwcommon.mail;

import java.sql.Timestamp;
import java.util.List;

import ar.com.fwcommon.entidades.enumeradores.EnumTipoContenidoMail;

public class MailRequest {

    private String aplicacion;
    private String usuario;
    private String origen;
    private String asunto;
    private String cuerpo;
    
    
    private Integer idTipoContenido;
    private Integer reintentos;
    private Timestamp ultimoIntento;
    private Integer idAutomata;
    
    
    private String direccionRespuesta;
    private Timestamp fechaAlta;
    private Timestamp fechaEnvio;
    private int estado;
    private List<DestinatarioRequest> destinatarios;

    public MailRequest() {
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public List<DestinatarioRequest> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<DestinatarioRequest> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getDireccionRespuesta() {
        return direccionRespuesta;
    }

    public void setDireccionRespuesta(String direccionRespuesta) {
        this.direccionRespuesta = direccionRespuesta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Timestamp getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Timestamp fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

	
	public Integer getIdTipoContenido() {
		return idTipoContenido;
	}

	
	public void setTipoContenido (EnumTipoContenidoMail contentType){
		this.idTipoContenido = contentType.getId();
	}
	
	private void setIdTipoContenido(Integer idTipoMensaje) {
		this.idTipoContenido = idTipoMensaje;
	}

	
	public Integer getReintentos() {
		return reintentos;
	}

	
	public void setReintentos(Integer reintentos) {
		this.reintentos = reintentos;
	}

	
	public Timestamp getUltimoIntento() {
		return ultimoIntento;
	}

	
	public void setUltimoIntento(Timestamp ultimoIntento) {
		this.ultimoIntento = ultimoIntento;
	}

	
	public Integer getIdAutomata() {
		return idAutomata;
	}

	
	public void setIdAutomata(Integer idAplicacion) {
		this.idAutomata = idAplicacion;
	}

}