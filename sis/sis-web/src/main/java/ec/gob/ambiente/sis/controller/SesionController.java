/**
@autor proamazonia [Christian Báez]  6 may. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@Named
@SessionScoped
public class SesionController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SesionController.class);
    public void cerrarSesion() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.FALSE);
            session.invalidate();
            redirect("/sis-web/index.xhtml?faces-redirect=true&redirected=true");
        } catch (Exception e) {
        	log.error(new StringBuilder().append(this.getClass().getName() + "." + "cerrarSesion " + ": ").append(e.getMessage()));
        }
    }
    public void redirect(final String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    public void redireccionarAPagina(String carpeta, String pagina) {
        try {
            if (carpeta.equals("")) {
                redirect("/sis-web/pages/" + pagina + ".jsf");
            } else {
                redirect("/sis-web/pages/" + carpeta + "/" + pagina + ".jsf");
            }

        } catch (Exception e) {
        	log.error(new StringBuilder().append(this.getClass().getName() + "." + "redireccionarAPagina " + ": ").append(e.getMessage()));
        }
    }
    
}

