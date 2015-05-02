package ar.com.textillevel.modulos.personal.entidades.legajos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.ImageIcon;

import org.hibernate.annotations.Type;

import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.InfoDomicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.estadocivil.InfoEstadoCivil;
import ar.com.textillevel.modulos.personal.entidades.legajos.familia.Familiar;
import ar.com.textillevel.modulos.personal.entidades.legajos.legales.DatosArt;
import ar.com.textillevel.modulos.personal.entidades.legajos.legales.DatosSeguroEmpleado;

@Entity
@Table(name = "T_PERS_EMPLEADO")
public class Empleado implements Serializable {

	private static final long serialVersionUID = -5438709111053067559L;

	private Integer id;
	private String nombre;
	private String apellido;
	private Date fechaNacimiento;
	private Nacionalidad nacionalidad;
	private byte[] bytesFotos;
	private Integer idSexo;
	private List<InfoEstadoCivil> informacionEstadoCivil;
	private List<InfoDomicilio> domicilios;
	private Documentacion documentacion;
	private List<Familiar> datosFamilia;
	private List<DatosEmpleoAnterior> historialEmpleos;
	private DatosSeguroEmpleado seguro;
	private DatosArt artObligatoria;
	private DatosArt artConvenio;
	private DatosEducacion datosEducacion;
	private Boolean jubilado;
	private Boolean privado;

	private LegajoEmpleado legajo;
	private ContratoEmpleado contratoEmpleado;

	public Empleado() {
		informacionEstadoCivil = new ArrayList<InfoEstadoCivil>();
		domicilios = new ArrayList<InfoDomicilio>();
		datosFamilia = new ArrayList<Familiar>();
		historialEmpleos = new ArrayList<DatosEmpleoAnterior>();
		documentacion = new Documentacion();
		// seguro = new DatosSeguroEmpleado();
		// artConvenio = new DatosArt();
		// artObligatoria = new DatosArt();
		datosEducacion = new DatosEducacion();
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NOMBRE", nullable = false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "A_APELLIDO", nullable = false)
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	@Column(name = "A_FECHA_NAC", nullable = false)
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_NACIONALIDAD_P_ID")
	public Nacionalidad getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	
	@Column(name = "A_IMAGEN", nullable = true)
	@Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
	private byte[] getBytesFotos() {
		return bytesFotos;
	}

	private void setBytesFotos(byte[] bytesFotos) {
		this.bytesFotos = bytesFotos;
	}

	@Transient
	public ImageIcon getFoto() {
		if(getBytesFotos() == null){
			return null;
		}
		return new ImageIcon(getBytesFotos());
	}
	
	public void setFoto(ImageIcon imagen) {
		try{
			setBytesFotos(ImageUtil.toByteArray(imagen.getImage()));
		}catch (Exception e) {
			
		}
	}

	@Column(name = "A_ID_SEXO", nullable = false)
	private Integer getIdSexo() {
		return idSexo;
	}

	private void setIdSexo(Integer idSexo) {
		this.idSexo = idSexo;
	}

	@Transient
	public ESexo getSexo() {
		if (getIdSexo() == null) {
			return null;
		}
		return ESexo.getById(getIdSexo());
	}

	public void setSexo(ESexo sexo) {
		if (sexo == null) {
			setIdSexo(null);
			return;
		}
		setIdSexo(sexo.getId());
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "F_EMPLEADO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<InfoEstadoCivil> getInformacionEstadoCivil() {
		return informacionEstadoCivil;
	}

	public void setInformacionEstadoCivil(List<InfoEstadoCivil> informacionEstadoCivil) {
		this.informacionEstadoCivil = informacionEstadoCivil;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_EMPLEADO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<InfoDomicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<InfoDomicilio> domicilios) {
		this.domicilios = domicilios;
	}

	@Embedded
	public Documentacion getDocumentacion() {
		return documentacion;
	}

	public void setDocumentacion(Documentacion documentacion) {
		this.documentacion = documentacion;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_EMPLEADO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<Familiar> getDatosFamilia() {
		return datosFamilia;
	}

	public void setDatosFamilia(List<Familiar> datosFamilia) {
		this.datosFamilia = datosFamilia;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_EMPLEADO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<DatosEmpleoAnterior> getHistorialEmpleos() {
		return historialEmpleos;
	}

	public void setHistorialEmpleos(List<DatosEmpleoAnterior> historialEmpleos) {
		this.historialEmpleos = historialEmpleos;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_SEGURO_P_ID", nullable = true)
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public DatosSeguroEmpleado getSeguro() {
		return seguro;
	}

	public void setSeguro(DatosSeguroEmpleado seguro) {
		this.seguro = seguro;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_ART_OBL", nullable = true)
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public DatosArt getArtObligatoria() {
		return artObligatoria;
	}

	public void setArtObligatoria(DatosArt artObligatoria) {
		this.artObligatoria = artObligatoria;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "F_ART_CONVENIO", nullable = true)
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public DatosArt getArtConvenio() {
		return artConvenio;
	}

	public void setArtConvenio(DatosArt artConvenio) {
		this.artConvenio = artConvenio;
	}

	@Embedded
	public DatosEducacion getDatosEducacion() {
		return datosEducacion;
	}

	public void setDatosEducacion(DatosEducacion datosEducacion) {
		this.datosEducacion = datosEducacion;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable = true)
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "F_CONTR_EMP_P_ID", nullable = true)
	public ContratoEmpleado getContratoEmpleado() {
		return contratoEmpleado;
	}

	public void setContratoEmpleado(ContratoEmpleado contratoEmpleado) {
		this.contratoEmpleado = contratoEmpleado;
	}

	@Column(name = "A_JUBILADO", nullable = false, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getJubilado() {
		return jubilado;
	}

	public void setJubilado(Boolean jubilado) {
		this.jubilado = jubilado;
	}
	
	@Column(name = "A_PRIVADO", nullable = false, columnDefinition = "INTEGER UNSIGNED DEFAULT 0")
	public Boolean getPrivado() {
		return privado;
	}

	public void setPrivado(Boolean privado) {
		this.privado = privado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empleado other = (Empleado) obj;
		if (apellido == null) {
			if (other.apellido != null)
				return false;
		} else if (!apellido.equals(other.apellido))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre + " " + apellido;
	}

	@Transient
	public Domicilio getUltimoDomicilio() {
		if (getDomicilios().isEmpty()) {
			return null;
		} else {
			return getDomicilios().get(getDomicilios().size() - 1).getDomicilio();
		}
	}
	
	@Transient
	public String getNombreYApellido(){
		return getNombre() + " " + getApellido();
	}
}