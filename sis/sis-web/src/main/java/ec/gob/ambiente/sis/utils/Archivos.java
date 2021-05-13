/**
@autor proamazonia [Christian Báez]  4 may. 2021

**/
package ec.gob.ambiente.sis.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Archivos {
	public static byte[] getBytesFromFile(File file) throws IOException {
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large!");
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }
        if (offset < bytes.length) {
            throw new IOException("No se pudo leer completamente el archivo " + file.getName());
        }
        return bytes;
    }
}

