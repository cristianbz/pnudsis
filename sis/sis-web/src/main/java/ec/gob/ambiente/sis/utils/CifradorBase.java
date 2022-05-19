/**
@autor proamazonia [Christian Báez]  16 jul. 2021

**/
package ec.gob.ambiente.sis.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CifradorBase {
	/**
	 * Metodo que cifrar base 64.
	 * @param a cadena a cifrar
	 * @return codificacion
	 */
	public static String cifrarBase64(String a){
        Base64.Encoder encoder = Base64.getEncoder();
        String b = encoder.encodeToString(a.getBytes(StandardCharsets.UTF_8) );        
        return b;
    }
 
	/**
	 * Metodo que decifra en base 64.
	 * @param a cadena a descifrar
	 * @return cadena
	 */
    public static String descifrarBase64(String a){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(a);
 
        String b = new String(decodedByteArray);        
        return b;
    }
}

