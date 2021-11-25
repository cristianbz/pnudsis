/**
@autor proamazonia [Christian Báez]  4 ago. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class RegistroDeGeneroController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(RegistroDeGeneroController.class);

	

	@Getter
	@Setter
	@Inject
	private MensajesController mensajesController;

	@Inject
	@Getter
	private ComponenteBuscaProyectos componenteBuscarProyectos;

	@PostConstruct
	private void init(){
		try{
			getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroSalvaguardas(false);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroGenero(true);
			getComponenteBuscarProyectos().setEsSeguimientoSalvaguardas(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Regresa a la busqueda de proyectos
	 */
	public void volverBuscarProyectos(){
		getComponenteBuscarProyectos().volverABuscarProyectos();		
	}
	public boolean datosProyecto(){		
		return getComponenteBuscarProyectos().datosProyecto();
	}
}

