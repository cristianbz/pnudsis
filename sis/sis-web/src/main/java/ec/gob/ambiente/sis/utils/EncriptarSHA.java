/**
@autor proamazonia [Christian Báez]  16 jul. 2021

**/
package ec.gob.ambiente.sis.utils;
import org.apache.commons.codec.digest.DigestUtils;
public class EncriptarSHA {
	/**
	 * Metodo que encripta en sha 512.
	 * @param  cadena entrada
	 * @return resultado
	 */
	public static String encriptarSHA512(String entrada) {
		return DigestUtils.sha512Hex(entrada);
	}
	
	/**
	 * Metodo que encripta en sha 256.
	 * @param cadena entrada
	 * @return resultado
	 */
	public static String encriptarSHA256(String entrada) {
		return DigestUtils.sha256Hex(entrada);
	}
	
	/**
	 * Metodo que encripta en sha 1.
	 * @param cadena entrada
	 * @return resultado
	 */
	public static String encriptarSHA1(String entrada) {
		return DigestUtils.sha1Hex(entrada);
	}
}

