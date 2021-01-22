package ec.gob.ambiente.sis.controller;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import ec.gob.ambiente.utils.Mensaje;
import lombok.Getter;

@ManagedBean
@ApplicationScoped
public class MensajesController {
	private static String filePath = "";
	private Properties properties;
	@Getter
	private ObtenerPropiedades retrieveProperties;

	public MensajesController() {
		retrieveProperties = new ObtenerPropiedades();
	}
	@PostConstruct
	public void init() {
		filePath = "../ec/gob/ambiente/sis/resources/errores_es.properties";
		properties = retrieveProperties.retrievePropertiesFromClasspath(filePath);
	}

	public String getPropiedad(String name) {
		String prop = null;
		try {
			if (properties == null) {
				init();
			}
			prop = getRetrieveProperties().getProperty(properties, name);
			if (null == prop) {
				throw new Exception("No existe la propiedad " + name + " en archivo " + filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage());
		}
		return prop;
	}
}
