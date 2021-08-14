/**
@autor proamazonia [Christian Báez]  3 ago. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.ambiente.sis.bean.AplicacionBean;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class RegistroSalvaguardasController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	@Getter	
	private ComponenteBuscaProyectos componenteBuscarProyectos;
	
	@Getter
    @Setter
    @Inject
    private AplicacionController aplicacionController;
    
    @Getter
    @Setter
    @Inject
    private AplicacionBean aplicacionBean;
	
	@PostConstruct
	public void init(){
		getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroSalvaguardas(true);
		getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroGenero(false);
		getAplicacionController().cargarSalvaguardas();
	}
	
	public boolean datosProyecto(){		
		return getComponenteBuscarProyectos().datosProyecto();
	}
	
	public void volverBuscarProyectos(){
		getComponenteBuscarProyectos().volverABuscarProyectos();
		getComponenteBuscarProyectos().getBuscaProyectosBean().setAsignacionSalvaguardas(false);
	
	}
}

