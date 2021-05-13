/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.sis.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fechas {
	/**
	 * Permite cambiar un string a formato fecha
	 * 
	 * @param fecha fecha
	 * @return fecja modificada
	 */
	public static Date cambiarStringADate(String fecha){
		try {
			DateFormat formatoFecha = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		    return formatoFecha.parse(fecha);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * Permite sumar/restar dias, meses o años a una fecha
	 * @param fecha
	 * @param sumaresta
	 * @param opcion
	 * @return
	 */
	public static Date sumaRestarFecha(Date fecha, int sumaresta, String opcion){
        LocalDate date = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        TemporalUnit unidadTemporal=null;
        switch(opcion){
            case "DAYS":
                unidadTemporal = ChronoUnit.DAYS;
                break;
            case "MONTHS":
                unidadTemporal = ChronoUnit.MONTHS;
                break;
            case "YEARS":
                unidadTemporal = ChronoUnit.YEARS;
                break;
            default:
                unidadTemporal=null;
        }
        LocalDate dateResultado = date.minus(sumaresta, unidadTemporal);
        return Date.from(dateResultado.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
	
	/**
	 * Permite cambiar un string a formato fecha
	 * 
	 * @param fecha
	 * @return
	 */
	public static Date cambiarStringaDate(String fecha, String formato){
		try {
			DateFormat formatoFecha = new SimpleDateFormat (formato);
		    return formatoFecha.parse(fecha);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * Suma o resta dias a una fecha si es resta dias <0
	 * @param fecha
	 * @param dias
	 * @return
	 */
	 public static Date sumarRestarDiasFecha(Date fecha, int dias){
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias); 
	      return calendar.getTime(); 
	 }

	/**
	 * 	/**
	 * Cambia la fecha de formato considerar el uso de:
	 * 
	 *  yyyyy-MM-dd hh:mm:ss
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public static String cambiarFormato(Date fecha, String formato) {
		SimpleDateFormat dt1 = new SimpleDateFormat(formato,new Locale("es","ES"));
		return dt1.format(fecha);
	}
}

