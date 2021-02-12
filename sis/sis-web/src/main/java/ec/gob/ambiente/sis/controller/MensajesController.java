package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.ObtenerPropiedades;
import lombok.Getter;

@Named
@ApplicationScoped
public class MensajesController implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private static String filePath = "";
	private Properties properties;
	@Getter
	private ObtenerPropiedades retrieveProperties;

	public MensajesController() {
		retrieveProperties = new ObtenerPropiedades();
	}
	@PostConstruct
	public void init() {
		
		try{
			filePath = "../resources/errores.properties";
			properties = retrieveProperties.retrievePropertiesFromClasspath(filePath);


		}catch(Exception e){
			e.printStackTrace();
		}
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
