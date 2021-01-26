package ec.gob.ambiente.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ObtenerPropiedades implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ObtenerPropiedades.class);
	private Properties properties = null;

	public Properties retrievePropertiesFromClasspath(String filePath) {
		try {
			if (properties == null) {
				ClassLoader cl = null;
				cl = Thread.currentThread().getContextClassLoader();
				if (cl == null) {
					cl = ObtenerPropiedades.class.getClassLoader();
				}
				InputStream in = cl.getResourceAsStream(filePath);
				properties = new Properties();
				properties.load(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return properties;
	}

	public String getProperty(Properties properties, String key) {
		if (properties != null) {
			String property = properties.getProperty(key);
			if (property == null) {
				logger.error("No existe la propiedad " + key + "en archivo de propiedades.");
			}
			return property;
		} else {
			return null;
		}
	}
}
