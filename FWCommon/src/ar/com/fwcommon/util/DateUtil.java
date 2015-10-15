package ar.com.fwcommon.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
/**
 * Clase que contiene funciones útiles de Fecha.
 *  
 */
public class DateUtil {

	public static final long ONE_DAY = 86400000;
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final String DEFAULT_DATE = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_WITHOUT_SEPARATOR = "yyyyMMdd"; //Ej.: 20051004
	public static final String SHORT_DATE = "dd/MM/yyyy"; //Ej.: 11/05/2004
	public static final String SHORT_DATE_WITH_HOUR = "dd/MM/yyyy HH:mm"; //Ej.: 11/05/2004 18:30
	public static final String SHORT_DATE_WITH_HOUR_SECONDS_MILLIS = "dd/MM/yyyy HH:mm:ss:zz"; //Ej.: 11/05/2004 18:30:59:88
	public static final String SHORT_DATE_WITH_HOUR_SECONDS = "dd/MM/yyyy HH:mm:ss"; //Ej.: 11/05/2004 18:30:59
	public static final String MEDIUM_DATE = "E dd MMM yyyy"; //Ej.: Mar 11 May 2004
	public static final String MEDIUM_DATE_WITH_HOUR = "E dd MMM yyyy HH:mm"; //Ej.: Mar 11 May 2004 18:30
	public static final String LONG_DATE = "EEEE dd MMMM yyyy";	//Ej.: Martes 11 Mayo 2004
	public static final String LONG_DATE_WITH_HOUR = "EEEE dd MMMM yyyy HH:mm";	//Ej.: Martes 11 Mayo 2004 18:30
	public static final String WEEK_DAY_SHORT_DATE = "EEEE dd/MM/yyyy"; //Ej.: Martes 11/05/2004
	public static final String WEEK_DAY_SHORT_DATE_WITH_HOUR = "EEEE dd/MM/yyyy HH:mm"; //Ej.: Martes 11/05/2004 18:30
    public static final String SHORT_TIME = "mm:ss"; //10:20
    public static final String MEDIUM_TIME = "HH:mm:ss"; //14:10:20
	public static final String SHORT_HOUR_TIME = "HH:mm"; //14:10
	public static final String[] DIAS_SEMANA = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
	public static final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
	private static final long MILLISEGUNDOS_POR_DIA = 86400000;

    /**
     * Convierte un String a <b>java.sql.Date</b>.
     * La fecha se debe pasar en el formato <b>dd/MM/yyyy</b>
     * (usar <b>DateUtil.SHORT_DATE</b>).
     * @param fecha
     * @return La fecha convertida a java.sql.Date.
     */
    public static Date stringToDate(String fecha) {
        return stringToDate(fecha, SHORT_DATE);
    }

	/**
	 * Convierte un String a <b>java.sql.Date</b>.
	 * (Pueden utilizarse los formatos definidos en DateUtil como SHORT_DATE /
	 * MEDIUM_DATE / LONG_DATE / WEEK_DAY_SHORT_DATE / etc.)
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public static Date stringToDate(String fecha, String formato) {
		try {
			//DateFormat formatter = new SimpleDateFormat(formato);
			DateFormat formatter = getSimpleDateFormat(formato);
			return new Date(formatter.parse(fecha).getTime());
		} catch(ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * Formatea la fecha como String utilizando el formato SHORT_DATE (dd/MM/yyyy).
     * @param fecha La fecha a formatear.
     * @return La fecha formateada como String.
     */
    public static String dateToString(java.util.Date fecha) {
        if(fecha != null) {
            return dateToString(fecha, SHORT_DATE);
        } else {
            return "";
        }
    }

    /**
     * Formatea la fecha como String utilizando el formato especificado.
     * @since 19/08/2009: oarias: se agrega una validacion de que la fecha recibida y el string de fecha convertida
     * se corresponde.  						
     * @param fecha La fecha a formatear.
     * @param formato El formato especificado.
     * @return La fecha formateada como String. Si se produce un error retorna un string vacio.
     */
    public static String dateToString(java.util.Date fecha, String formato) {
    	SimpleDateFormat formatDate =  getSimpleDateFormat(formato);
    	String nuevaFecha = formatDate.format(fecha);
    	try {
			if(new Date(formatDate.parse(nuevaFecha).getTime())== fecha){
				new ParseException("Error de conversion de fechas a String para la fecha " + fecha.toString(),0).printStackTrace();
				return "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return nuevaFecha;
    }

    public static Timestamp toTimestamp (java.util.Date date) {
    	return date == null ? null : new Timestamp(date.getTime());
    }

    public static Date toDate(java.util.Date date) {
    	return date == null ? null : new Date(date.getTime());
    }

    /**
     * Convierte un String a <b>java.sql.Time</b>.
     * @param time
     * @return
     */
    public static Time stringToTime(String time) {
        try {
            //DateFormat formatter = new SimpleDateFormat(SHORT_TIME);
            DateFormat formatter = getSimpleDateFormat(SHORT_TIME);
            return new Time(formatter.parse(time).getTime());
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * Devuelve el <b>nro. de día</b> que se obtiene a partir de haber restado a
	 * diaInicio la cantidad de días cantidadDias.
	 * @param diaInicio
	 * @param cantDias
	 * @return res
	 */
	public static int restarDias(int diaInicio, int cantDias) {
		int res = ((diaInicio - (cantDias % 7)) % 7);
		if(res <= 0) {
			res += 7;
		}
		return res;
	}

	/**
	 * Toma una fecha y le resta <code>cant</code> días. Devuelve la fecha restada. 
	 * El parametro fecha queda intacto. 
	 * @param fecha. Fecha a la cual se le restará cant. No es afectado en el método.
	 * @param cant
	 * @return
	 */
	public static Date restarDias(java.util.Date fechaInicio, int cantDias) {
		GregorianCalendar fecha = getGregorianCalendar();
		fecha.setTime(fechaInicio);
		fecha.add(Calendar.DAY_OF_MONTH, -cantDias);
		return new Date(fecha.getTime().getTime()); 
	}
	
	/**
	 * Toma una fecha y le suma <code>cant</code> días. Devuelve la fecha suma. 
	 * El parametro fecha queda intacto. 
	 * @param fecha. Fecha a la cual se le sumará cant. No es afectado en el método.
	 * @param cant
	 * @return
	 */
	public static Date sumarDias(java.util.Date fechaInicio, int cantDias) {
		GregorianCalendar fecha = getGregorianCalendar();
		fecha.setTime(fechaInicio);
		fecha.add(Calendar.DAY_OF_MONTH, cantDias);
		return new Date(fecha.getTime().getTime()); 
	}
	
	/**
	 * similar a {{@link #restarDias(java.util.Date, int)}, pero con otro tipo y sumando minutos.
	 */
	public static java.sql.Timestamp addMinutos(java.sql.Timestamp fecha, int cantMinutos) {
		GregorianCalendar gc = getGregorianCalendar();
		gc.setTime(fecha);
		gc.add(Calendar.MINUTE, cantMinutos);
		return new java.sql.Timestamp(gc.getTime().getTime()); 
	}
	

	/**
	 * Toma una fecha y le resta <code>cant</code> meses. Devuelve la fecha restada. 
	 * El parametro fecha queda intacto. 
	 * @param fecha. Fecha a la cual se le restará cant. No es afectado en el método.
	 * @param cant
	 * @return
	 */
	public static Date restarMeses(Date fecha, int cant) {
		//GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
		cal.add(Calendar.MONTH, -cant);
		return new Date (cal.getTime().getTime());
	}
	
	
	/**
	 * Retorna <b>la cantidad de días</b> que hay entre las fechas Desde y Hasta.
	 * @param fechaDesde La fecha desde o inicial.
	 * @param fechaHasta La fecha hasta o final.
	 * @return cantidadDias La cantidad de días entre las dos fechas.
	 */
	public static int contarDias(Date fechaDesde, Date fechaHasta) {
		Date f = new Date(fechaDesde.getTime());
		int cantDias = 0;
		while(f.before(fechaHasta)) {
			cantDias++;
    		//Incremento de la fecha en un día
    		f.setTime(f.getTime() + ONE_DAY);
		}
		return cantDias;
	}

	/**
	 * Devuelve el <b>Día</b> de la fecha pasada por parámetro.
	 * Ej.: si la fecha es 25/01/2005 devuelve <b>25</b>. 
	 * @param fecha La fecha de la cual extraer el día.
	 * @return El día de la fecha.
	 */
	public static int getDia(Date fecha) {
		//GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Setea el <b>Día</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea el nuevo día.
	 * @param dia El nuevo día.
	 * @return La fecha con el nuevo día seteado.
	 */
	public static Date setDia(Date fecha, int dia) {
	    //GregorianCalendar cal = new GregorianCalendar();
	    GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.DAY_OF_MONTH, dia);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Setea el <b>Día</b> y el <b>Mes</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea el nuevo día.
	 * @param dia El nuevo día.
	 * @param mes El nuevo mes un valor de 0 a 11.
	 * @return La fecha con el nuevo día y mes setedo.
	 */
	public static Date setDiaMes(Date fecha, int dia, int mes) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.DAY_OF_MONTH, dia);
	    cal.set(Calendar.MONTH, mes);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Devuelve el <b>Mes</b> menos 1 de la fecha pasada por parámetro.
	 * Ej.: si la fecha es 25/01/2005 devuelve <b>0</b>.
	 * @param fecha la fecha de la cual extraer el mes.
	 * @return El mes de la fecha.
	 */
	public static int getMes(Date fecha) {
		//GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
		return cal.get(Calendar.MONTH);
	}

    /**
     * Devuelve el <b>Mes</b> como String de la fecha pasada por parámetro.
     * Ej.: si la fecha es 25/01/2005 devuelve <b>Enero</b>.
     * @param fecha La fecha de la cual extraer el día de semana.
     * @return El mes de la fecha como String.
     */
    public static String getMesAsString(Date fecha) {
        //DateFormat df = new SimpleDateFormat("MMMM");
    	DateFormat df = getSimpleDateFormat("MMMM");
        return df.format(fecha);
    }

	/**
	 * Setea el <b>Mes</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea el nuevo mes.
	 * @param mes El nuevo mes.
	 * @return La fecha con el nuevo mes seteado.
	 */
	public static Date setMes(Date fecha, int mes) {
	    //GregorianCalendar cal = new GregorianCalendar();
	    GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.MONTH, mes);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Devuelve el <b>Año</b> de la fecha pasada por parámetro.
	 * Ej.: si la fecha es 25/01/2005 devuelve <b>2005</b>.
	 * @param fecha La fecha de la cual extraer el año.
	 * @return El año de la fecha.
	 */
	public static int getAnio(Date fecha) {
		//GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Devuelve el <b>Año</b> de la fecha pasada por parámetro.
	 * La fecha es un <b>Timestamp</b>.
	 * Ej.: si la fecha es 25/01/2005 17:45:50 devuelve <b>2005</b>.
	 * @param fecha La fecha de la cual extraer el año.
	 * @return El año de la fecha.
	 */
	public static int getAnio(Timestamp fecha) {
		//GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Setea el <b>Año</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea el nuevo año.
	 * @param anio El nuevo año.
	 * @return La fecha con el nuevo año seteado.
	 */
	public static Date setAnio(Date fecha, int anio) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.YEAR, anio);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Devuelve el <b>Día</b> correspondiente a los valores pasados por parámetro.
	 * @param year El año.
	 * @param month El mes.
	 * @param dayOfMonth El día.
	 */
	public static Date getFecha(int year, int month, int dayOfMonth) {
		GregorianCalendar cal = getGregorianCalendar();
		cal.set(year, month - 1, dayOfMonth);
		return new Date(cal.getTimeInMillis());
		//return new Date(new GregorianCalendar(year, month - 1, dayOfMonth).getTimeInMillis());
	}

	/**
	 * Devuelve el <b>Día y hora</b> correspondiente a los valores pasados por parámetro.
	 * @param year El año.
	 * @param month El mes.
	 * @param dayOfMonth El día.
	 * @param hourOfDay La hora.
	 * @param minute Los minutos.
	 * @param second Los segundos.
	 */
	public static Timestamp getFecha(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
		GregorianCalendar cal = getGregorianCalendar();
		cal.set(year, month - 1, dayOfMonth, hourOfDay, minute, second);
		cal.set(GregorianCalendar.MILLISECOND, 0) ;
		return new Timestamp(cal.getTimeInMillis());
		//return new Date(new GregorianCalendar(year, month - 1, dayOfMonth).getTimeInMillis());
	}

	/**
	 * Devuelve el <b>Día de Semana</b> de la fecha pasada por parámetro.
	 * Ej.: si la fecha es 25/01/2005 devuelve <b>3</b> (Martes).
	 * @param fecha La fecha de la cual extraer el día de semana.
	 * @return El día de semana de la fecha.
	 */
	public static int getDiaSemana(Date fecha) {
		//GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Devuleve el siguiente dia habil despues de la fecha pasada como parámetro
	 * @param fecha
	 * @return el siguiente dia habil
	 */
	public static Date getSiguienteDiaHabil(Date fecha){
		int diaSemana = getDiaSemana(fecha);
		//si es sabado
		if(diaSemana == 7){
			return getManana(getManana(fecha)); 
		}
		//si es viernes
		if(diaSemana == 6){
			return  getManana(getManana(getManana(fecha))); 
		}
		return getManana(fecha);
	}

	/**
	 * Devuelve el <b>Día de Semana</b> como String de la fecha pasada por parámetro.
	 * Ej.: si la fecha es 25/01/2005 devuelve <b>Martes</b>.
	 * @param fecha La fecha de la cual extraer el día de semana.
	 * @return El día de semana de la fecha como String.
	 */
	public static String getDiaSemanaAsString(Date fecha) {
	    //DateFormat df = new SimpleDateFormat("EEEE");
		DateFormat df = getSimpleDateFormat("EEEE");
		return df.format(fecha);
	}

	/**
	 * Devuelve la <b>Hora</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha de la cual extraer la hora.
	 * @return La hora de la fecha.
	 */
	public static int getHoras(Date fecha) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Devuelve la <b>Hora</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha de la cual extraer la hora.
	 * @return La hora de la fecha.
	 */
	public static int getHoras(Timestamp t) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(t);
	    return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Devuelve la representación en milisegundos de las horas, minutos, segundos
	 * y milisegundos para la fecha <code>t</code>.
	 * @param t
	 * @return La representación en milisegundos de las horas, minutos, segundos
	 * 		y milisegundos para la fecha <code>t</code>
	 */
	public static long getHorasMs(java.util.Date t) {
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(t);
	    //Paso las horas a milisegundos
	    long horasMs = cal.get(Calendar.HOUR_OF_DAY) * 3600000;
	    //Paso los minutos a milisegundos
	    horasMs += cal.get(Calendar.MINUTE) * 60000;
	    //Paso los segundos a milisegundos
	    horasMs += cal.get(Calendar.SECOND) * 1000;
	    //Por último sumo los milisegundos
	    horasMs += cal.get(Calendar.MILLISECOND);
	    return horasMs;
	}

    /**
	 * Setea la <b>Hora</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea la nueva hora.
	 * @param horas La nueva hora de la fecha.
	 * @return La fecha con la nueva hora seteada.
	 */
	public static Date setHoras(Date fecha, int horas) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.HOUR_OF_DAY, horas);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Setea la <b>Hora</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea la nueva hora.
	 * @param horas La nueva hora de la fecha.
	 * @return La fecha con la nueva hora seteada.
	 */
	public static Timestamp setHoras(Timestamp fecha, int horas) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.HOUR_OF_DAY, horas);
	    return new Timestamp(cal.getTimeInMillis());
	}
	
	/**
	 * Devuelve los <b>Minutos</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha de la cual extraer los minutos.
	 * @return Los minutos de la fecha.
	 */
	public static int getMinutos(Date fecha) {
	    //GregorianCalendar cal = new GregorianCalendar();
	    GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    return cal.get(Calendar.MINUTE);
	}

	/**
	 * Devuelve los <b>Minutos</b> de la fecha pasada por parámetro.
	 * @param t La fecha de la cual extraer los minutos.
	 * @return Los minutos de la fecha.
	 */
	public static int getMinutos(Timestamp t) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(t);
	    return cal.get(Calendar.MINUTE);
	}

	/**
	 * Setea los <b>Minutos</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea los nuevos minutos.
	 * @param minutos Los nuevos minutos de la fecha.
	 * @return La fecha con los nuevos minutos seteados.
	 */
	public static Date setMinutos(Date fecha, int minutos) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.MINUTE, minutos);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Setea los <b>Minutos</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea los nuevos minutos.
	 * @param minutos Los nuevos minutos de la fecha.
	 * @return La fecha con los nuevos minutos seteados.
	 */
	public static Timestamp setMinutos(Timestamp fecha, int minutos) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.MINUTE, minutos);
	    return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * Devuelve los <b>Segundos</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha de la cual extraer los segundos.
	 * @return Los segundos de la fecha.
	 */
	public static int getSegundos(Date fecha) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    return cal.get(Calendar.SECOND);
	}

	/**
	 * Devuelve los <b>Segundos</b> de la fecha pasada por parámetro.
	 * @param t La fecha de la cual extraer los segundos.
	 * @return Los segundos de la fecha.
	 */
	public static int getSegundos(Timestamp t) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(t);
	    return cal.get(Calendar.SECOND);
	}

	/**
	 * Setea los <b>Segundos</b> de la fecha pasada por parámetro.
	 * @param fecha La fecha a la cual se le setea los nuevos segundos.
	 * @param segundos Los nuevos segundos de la fecha.
	 * @return La fecha con los nuevos segundos seteados.
	 */
	public static Date setSegundos(Date fecha, int segundos) {
	    //GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
	    cal.setTime(fecha);
	    cal.set(Calendar.SECOND, segundos);
	    return new Date(cal.getTimeInMillis());
	}

	/**
	 * Devuelve la mayor fecha dentro del mismo mes y año a partir 
     * de la que recibe como parámetro.
     * Ej: Si fecha es 13/02/2004 => devuelve 28/02/2004
	 * @param fecha
	 */
	public static Date getUltimoDiaMes(Date fecha) {
		//GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal = getGregorianCalendar();
		cal.setTime(fecha);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Date(cal.getTime().getTime());
	}

    /**
     * Devuelve la menor fecha dentro del mismo mes y año a partir 
     * de la que recibe como parámetro.
     * Ej: Si fecha es 13/02/2004 => devuelve 01/02/2004
     * @param fecha
     */
    public static Date getPrimerDiaMes(Date fecha) {
        //GregorianCalendar cal = new GregorianCalendar();
    	GregorianCalendar cal = getGregorianCalendar();
        cal.setTime(fecha);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new Date(cal.getTime().getTime());
    }

    /**
	 * Devuelve la fecha de <b>Hoy</b>.
	 * @return La fecha actual de la PC.
	 */
	public static Date getHoy() {
		return redondearFecha(new Date(System.currentTimeMillis()));
	}
	
	/**
	 * @return La fecha <b>Ayer</b>. 
	 */
	public static Date getAyer() {
		return redondearFecha(new Date(System.currentTimeMillis() - ONE_DAY));
	}

	/**
	 * Devuelve la fecha que representa un día anterior a <b>fecha</b>.
	 * @param fecha
	 * @return
	 */
	@Deprecated //  Usar el getAyerSinRedondear
	public static Date getAyer(Date fecha) {
	    return redondearFecha(new Date(fecha.getTime() - ONE_DAY));
	}
	public static Date getAyerSinRedondear(Date fecha) {
	    return new Date(fecha.getTime() - ONE_DAY);
	}
	
	/**
	 * Devuelve la fecha de <b>Mañana</b>.
	 * @return
	 */
	public static Date getManana() {
		return redondearFecha(new Date(System.currentTimeMillis() + ONE_DAY));
	}

	/**
	 * Devuelve la fecha de <b>Mañana</b>.
	 * @return
	 */
	@Deprecated //  Usar el getManiana(Date fecha)
	public static Date getManana(Date fecha) {
		return redondearFecha(new Date(fecha.getTime() + ONE_DAY));
	}
	
	public static Date getMananaSinRedondear(Date fecha) {
		return new Date(fecha.getTime() + ONE_DAY);
	}

	public static Date getManiana(Date fecha) {
		Calendar c = getGregorianCalendar() ;
		c.setTime(fecha) ;
		c.add(Calendar.DAY_OF_MONTH, 1) ;
		c.set(Calendar.HOUR_OF_DAY, 0) ;
		c.set(Calendar.MINUTE, 0) ;
		c.set(Calendar.SECOND, 0) ;
		c.set(Calendar.MILLISECOND, 0) ;
		return new Date (c.getTimeInMillis()) ;
	}

	public static Date getManianaSinRedondear(Date fecha) {
		Calendar c = getGregorianCalendar() ;
		c.setTime(fecha) ;
		c.add(Calendar.DAY_OF_MONTH, 1) ;
		return new Date (c.getTimeInMillis()) ;
	}

	/**
	 * Devuelve la fecha de <b>Mañana</b>.
	 * @return
	 */
	public static Date getManana(Timestamp fecha) {
		return redondearFecha(new Date(fecha.getTime() + ONE_DAY));
	}

	/**
	 * Devuelve un Timestamp con la fecha y la hora actual.
	 * @return
	 */
	public static Timestamp getAhora() {
	    return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return La hora actual como String.
	 */
	public static String getAhoraAsString() {
		return getAhoraAsString(SHORT_HOUR_TIME);
	}

	/**
	 * @param formato El formato que se utiliza para devolver la hora.
	 * @return La hora actual como String.
	 */
	public static String getAhoraAsString(String formato) {
		return dateToString(getAhora(), formato);
	}

	/**
	 * Redondea los minutos/segundos de una fecha.
	 * @param fecha La fecha a redondear.
	 * @return La fecha con los minutos y segundos redondeados.
	 */
	public static Date redondearFecha(java.util.Date fecha) {
	    long t = fecha.getTime();
	    //int diferenciaGMT = Calendar.getInstance().getTimeZone().getRawOffset();
	    int diferenciaGMT = getTimeZone().getRawOffset();
	    return new Date(t - (t + diferenciaGMT) % ONE_DAY);
	}
	
	public static java.util.Date getDateInDefaultTimeZone(Date currentDate) {
		Calendar mbCal = new GregorianCalendar(DateUtil.getTimeZone());
		mbCal.setTimeInMillis(currentDate.getTime());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));

		return cal.getTime();
	}

	/**
	 * Devuelve un String con la diferencia de tiempos entre f1 y f2.
	 * @param f1
	 * @param f2
	 * @return diferencia
	 */
    public static String getDifTiempos(Timestamp f1, Timestamp f2) {
        String formato = "";
        int m1 = 0, m2 = 0;
        long unDia = DateUtil.ONE_DAY;
    	long unaHora = unDia / 24;
    	long unMinuto = unaHora / 60;
    	long unSegundo = unMinuto / 60;
    	String diferencia = "";
    	int minutos = 0;
    	if(f1 != null && f2 != null) {
    	    long dif = Math.abs(f1.getTime() - f2.getTime());
    	    if(dif >= unDia) {
    	        diferencia += (dif / unDia) + " días ";
    	        dif = dif % unDia;
    	    }
    	    if(dif >= unaHora) {
    	        diferencia += (dif / unaHora) + " hs. ";
    	        dif = dif % unaHora;
    	    }
	        formato = "mm";
	        m1 = getCampo(f1, formato);
	        m2 = getCampo(f2, formato);
	        formato = "ss";
	        int s1 = getCampo(f1, formato);
    	    int s2 = getCampo(f2, formato);
	        if(dif >= unMinuto) {
    	        minutos = (int)(dif / unMinuto);
    	        dif = dif % unMinuto;
    	    }
    	    if((dif >= unSegundo) && (m1 != m2) && (s1 > s2))
    	        minutos++;
    	    diferencia += minutos + " min.";
    	}
    	return diferencia;
    }

    public static int getCampo(Timestamp t, String formato) {
    	//DateFormat formatter = new SimpleDateFormat(formato);
    	DateFormat formatter = getSimpleDateFormat(formato);
    	return Integer.valueOf(formatter.format(t)).intValue();
    }

    public static String getHorasMinutos(java.util.Date fecha) {
    	long unaHora = DateUtil.ONE_DAY / 24;
    	long unMinuto = unaHora / 60;
    	String diferencia = "";
    	int minutos = 0;
    	if(fecha != null) {
    	    long time = fecha.getTime();
    	    if(time >= unaHora) {
    	        diferencia += (time / unaHora) + " hs. ";
    	        time = time % unaHora;
    	    }
	        if(time >= unMinuto) {
    	        minutos = (int)(time / unMinuto);
    	        time = time % unMinuto;
    	    }
    	    diferencia += minutos + " min.";
    	}
    	return diferencia;
    }
    
    public static String getHorasMinutosTimestamp(Timestamp fecha) {
    	return getHoras(fecha) + " hs. " + getMinutos(fecha) + " min";
    }

    public static String getMesAnio(Date fecha) {
    	return (getMes(fecha)+1) + "/" + getAnio(fecha);
    }

    /**
     * Devuelve la fecha (java.sql.Date) a partir del Timestamp t.
     * @param t El Timestamp.
     * @return La fecha resultante.
     */
    public static Date getDate(Timestamp t) {
        return redondearFecha(new Date(t.getTime()));
    }

    public static Timestamp construirFecha(java.util.Date fechaIngresoSeleccionada, Time horaIngresoSeleccionada) {
        Timestamp t = new Timestamp(redondearFecha(fechaIngresoSeleccionada).getTime());
        return new Timestamp(t.getTime() + getHorasMs(horaIngresoSeleccionada));
    }

    /**
	 * Compara dos objetos java.sql.Time ignorando la componente
	 * fecha de ambos sólo tiene en cuenta la componente hh:mm:ss.  
	 * @param t1
	 * @param t2
	 * @return True si la hora de t1 es menor a la hora de t2.
	 */
	public static boolean esMenor(Time t1, Time t2) {
		//GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal1 = getGregorianCalendar();
		cal1.setTime(t1);
		cal1.set(1970, 1, 1);
		//GregorianCalendar cal2 = new GregorianCalendar();
		GregorianCalendar cal2 = getGregorianCalendar();
		cal2.setTime(t2);
		cal2.set(1970, 1, 1);
		return cal1.getTime().before(cal2.getTime());
	}

	/**
	 * Construye una fecha a partir de un timestamp.
	 * @param t
	 * @return
	 */
	public static Date construirFecha(Timestamp t) {
		return new Date(t.getTime());
	}

    /**
     * Construye y devuelve un objeto <b>java.util.Date</b> a partir de uno java.sql.Date.
     * @param fecha El objeto java.sql.Date a partir del cual se construye el java.util.Date.
     * @return
     */
    public static java.util.Date construirFecha(Date fecha) {
        try {
            //DateFormat formatter = new SimpleDateFormat(DEFAULT_DATE);
        	DateFormat formatter = getSimpleDateFormat(DEFAULT_DATE);
            return formatter.parse(fecha.toString());
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retorna <b>true</b> si la fecha respeta la máscara
     * @param fecha
     * @param mascara
     * @return
     */
	public static boolean validarFecha(String fecha, String mascara) {
		//SimpleDateFormat sdf = new SimpleDateFormat(mascara);	   
		SimpleDateFormat sdf = getSimpleDateFormat(mascara);
		java.util.Date testDate = null;
		try {
			testDate = sdf.parse(fecha);
		} catch(ParseException e) {
			return false;
		}
		return sdf.format(testDate).equalsIgnoreCase(fecha);
	}

	/**
	 * @param java.util.Date fechaNac
	 * @return Devuelve la edad a partir de la fecha de nacimiento pasada por parámetro.
	 */
	public static int calcularEdad(java.util.Date fechaNac) {
		if(fechaNac!=null){
			int dia = Integer.parseInt(new SimpleDateFormat("dd").format(fechaNac));
			int mes = Integer.parseInt(new SimpleDateFormat("MM").format(fechaNac));
			int anio = Integer.parseInt(new SimpleDateFormat("yyyy").format(fechaNac));
			return calcularEdad(dia, mes, anio);
		} else {
			throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
		}
	}
	
	/**
	 * @param dia
	 * @param mes
	 * @param anio
	 * @return Devuelve la edad a partir de la fecha de nacimiento pasada por parámetro.
	 */
	public static int calcularEdad(int dia, int mes, int anio) {
		//Calendar cal = new GregorianCalendar(anio, mes, dia);
		Calendar cal = getGregorianCalendar();
		cal.set(anio, mes, dia);
		//Calendar hoy = new GregorianCalendar();
		Calendar hoy = getGregorianCalendar();
		int edad = hoy.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if(edad == 0){
			return edad;
		}
		if((cal.get(Calendar.MONTH) > hoy.get(Calendar.MONTH)) || (cal.get(Calendar.MONTH) == hoy.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) > hoy.get(Calendar.DAY_OF_MONTH))) {
			edad--;
		}
		return edad;
	}

	/**
	 * @param diaNac
	 * @param mesNac
	 * @param anioNac
	 * @param diaHasta
	 * @param mesHasta
	 * @param anioHasta
	 * @return Devuelve la edad a partir de la fecha de nacimiento pasada por parámetro y la fecha final.
	 */
	public static int calcularEdadHasta(int diaNac, int mesNac, int anioNac, int diaHasta, int mesHasta, int anioHasta) {
		Calendar cal = getGregorianCalendar();
		cal.set(anioNac, mesNac, diaNac);
		Calendar hasta = getGregorianCalendar();
		hasta.set(anioHasta, mesHasta, diaHasta);
		int edad = hasta.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if((cal.get(Calendar.MONTH) > hasta.get(Calendar.MONTH)) || (cal.get(Calendar.MONTH) == hasta.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) > hasta.get(Calendar.DAY_OF_MONTH))) {
			edad--;
		}
		return edad;
	}

	public static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setTimeZone(getTimeZone());
		return simpleDateFormat;
	}

	private static TimeZone getTimeZone() {
		String tz = System.getProperty("cltimezone");
		if (!StringUtil.isNullOrEmpty(tz)){
			return TimeZone.getTimeZone(tz);
		}
		return getAmericaArgentinaBuenos_Aires();
	}

	public static TimeZone getUTC() {
		return TimeZone.getTimeZone("UTC");
	}

	public static TimeZone getGMT_3() {
		return TimeZone.getTimeZone("GMT-3");
	}

	private static TimeZone getAmericaArgentinaBuenos_Aires() {
		return TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
	}

	public static GregorianCalendar getGregorianCalendar() {
		return new GregorianCalendar(getTimeZone());
	}

	/**
	 * @param fechaInicio
	 * @param fechaFin
	 * @param fecha
	 * @return Devuelve <b>true</b> si una fecha esta entre dos fechas.
	 */
	public static boolean isBetween(Date fechaInicio, Date fechaFin, Date fecha) {
		return (fechaInicio.before(fecha) && fechaFin.after(fecha));
	}

	/**
	 * @param fechaInicio
	 * @param fechaFin
	 * @param fecha
	 * @return Devuelve <b>true</b> si una fecha esta entre dos fechas.
	 */
	public static boolean isBetween(Timestamp fechaInicio, Timestamp fechaFin, Timestamp fecha) {
		return isBetween(fechaInicio, fechaFin, fecha, false);
	}

	/**
	 * @param fechaInicio
	 * @param fechaFin
	 * @param fecha
	 * @param incluyeExtremos
	 * @return Devuelve <b>true</b> si una fecha esta entre dos fechas.
	 */
	public static boolean isBetween(Timestamp fechaInicio, Timestamp fechaFin, Timestamp fecha, boolean incluyeExtremos) {
		if (incluyeExtremos) {
			if (fechaInicio.equals(fecha) || fechaFin.equals(fecha)) {
				return true;
			} 
		}
		return (fechaInicio.before(fecha) && fechaFin.after(fecha));
	}

	/**
	 * Me dice cual es la cantidad de dias que existen entre la fecha fin y la de comienzo.</br>
	 * Compara por milisegundos.</br>
	 * Ej: Si una fecha es hoy a las 6 y la otra es mañana a las 5, hay 0 días entre ellas.
	 * 
	 *  Lcremonte
	 * 
	 * @param comienzo
	 * @param fin
	 * @return int cantidad de dias entre las dos fechas.
	 */
	public static int getDaysBetween(java.util.Date comienzo, java.util.Date fin) {
		if(comienzo == null) {
			throw new IllegalArgumentException("la fecha de comienzo es null");
		}
		if(fin == null) {
			throw new IllegalArgumentException("la fecha de fin es null");
		}
		return new Long((fin.getTime() - comienzo.getTime()) / MILLISEGUNDOS_POR_DIA).intValue();
	}

	/**
	 * Retorna true si las fechas pasadas como parámetro son iguales sin tener en cuenta la hora.
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean iguales(Date date1, Date date2) {
		if (date1 == null || date2 == null) return false ;
		return 
			DateUtil.getAnio(date1) == DateUtil.getAnio(date2) &&  
			DateUtil.getMes(date1) == DateUtil.getMes(date2) &&
			DateUtil.getDia(date1) == DateUtil.getDia(date2) ;
	}

	public static boolean isBetweenConExtremos(Date fechaInicio, Date fechaFin, Date fecha) {
		return  !fecha.before(fechaInicio) && !fecha.after(fechaFin);
	}

	public static Timestamp sumarDias(Timestamp fechaInicio, int cantDias) {
		GregorianCalendar fecha = getGregorianCalendar();
		fecha.setTime(fechaInicio);
		fecha.add(Calendar.DAY_OF_MONTH, cantDias);
		return new Timestamp(fecha.getTime().getTime());
	}

	public static Timestamp sumarMinutos(Timestamp fechaInicio, int cantMinutos) {
		GregorianCalendar fecha = getGregorianCalendar();
		fecha.setTime(fechaInicio);
		fecha.add(Calendar.MINUTE, cantMinutos);
		return new Timestamp(fecha.getTime().getTime());
	}

	public static Timestamp sumarHoras(Timestamp fechaInicio, int cantHoras) {
		GregorianCalendar fecha = getGregorianCalendar();
		fecha.setTime(fechaInicio);
		fecha.add(Calendar.HOUR_OF_DAY, cantHoras);
		return new Timestamp(fecha.getTime().getTime());
	}

}