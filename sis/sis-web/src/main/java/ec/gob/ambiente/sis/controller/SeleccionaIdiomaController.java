package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
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
	
	private String localeCode;    
	private static Map<String, Object> countries;
	
	@PostConstruct
	public void init(){
		setLenguaje("es_ES");
	}
	/**
	 * Permite cambiar el idioma
	 */
	public void cambiarIdioma(){
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
	    viewRoot.setLocale(new Locale(this.lenguaje));
	}
	
	 static {
	        countries = new LinkedHashMap<String, Object>();
	        countries.put("Español", new Locale("es", "ES")); 
	        countries.put("English", new Locale("en", "US"));
	    }
	 
	 public Map<String, Object> getCountriesInMap() {
	        return countries;
	    }

	    public String getLocaleCode() {
	        return localeCode;
	    }

	    public void setLocaleCode(String localeCode) {
	        this.localeCode = localeCode;
	    }
	    
	    public void countryLocaleCodeChanged(ValueChangeEvent e) {
	        String newLocaleValue = e.getNewValue().toString();	        
	        setLenguaje(newLocaleValue);
	        //loop country map to compare the locale code
	        for (Map.Entry<String, Object> entry : countries.entrySet()) {
	            if (entry.getValue().toString().equals(newLocaleValue)) {
	                FacesContext.getCurrentInstance()
	                        .getViewRoot().setLocale((Locale) entry.getValue());
	                
	                
	            }
	        }
	    }
}
