package ec.gob.ambiente.sis.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

public class Mensaje {
	public static void verMensaje(FacesMessage.Severity s, String message, String detail) {
		FacesMessage facesMessage = new FacesMessage(s, message, detail);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
	/**
	 * Visualiza un mensaje con detalles
	 * @param clienteId
	 * @param s
	 * @param message
	 * @param detail
	 */
	public static void verMensaje(String clienteId, FacesMessage.Severity s, String message, String detail) {
		FacesMessage facesMessage = new FacesMessage(s, message, detail);
		FacesContext.getCurrentInstance().addMessage(clienteId, facesMessage);
	}
	/**
	 * Visualiza un cuadro de dialogo
	 * @param widget
	 */
	public static void verDialogo(String widget) {
		PrimeFaces.current().executeScript("PF('"+widget+"').show()");
	}
	/**
	 * Oculta un cuadro de dialogo
	 * @param widget
	 */
	public static void ocultarDialogo(String widget) {
		
		PrimeFaces.current().executeScript("PF('"+widget+"').hide()");
	}
	/**
	 * Actualiza un componente
	 * @param id
	 */
	public static void actualizarComponente(String id) {
		PrimeFaces.current().ajax().update(id);
		
	}

}
