/**
@autor proamazonia [Christian Báez]  8 jun. 2022

**/
package ec.gob.ambiente.sis.utils;

public class UtilsCadenas {
	
	public static String romperCadena(String cadena, int tamanio){
		String returnString="";
		StringBuffer auxCadena=new StringBuffer();
		int posicion=0;
		while(cadena!=null && cadena.length()>0 &&  posicion <= tamanio){
			if(tamanio - posicion > 20){
				auxCadena = auxCadena.append(cadena.substring(posicion,posicion+20).toString()).append("\n");
				posicion+=20;
			}else{
				auxCadena = auxCadena.append(cadena.substring(posicion,tamanio).toString());
				break;
			}
		}
		if (posicion == 0)
			returnString = cadena;
		else
			returnString = auxCadena.toString();
	    return returnString;
	}
}

