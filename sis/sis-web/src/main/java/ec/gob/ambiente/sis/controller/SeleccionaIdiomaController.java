package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@SessionScoped
@Named
public class SeleccionaIdiomaController implements Serializable {


	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String lenguaje;
	
	/**
	 * Permite cambiar el idioma
	 */
	public void cambiarIdioma(){
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
	    viewRoot.setLocale(new Locale(this.lenguaje));
	}
}
