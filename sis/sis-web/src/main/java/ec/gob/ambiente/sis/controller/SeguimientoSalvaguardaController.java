package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;
import ec.gob.ambiente.sis.services.ProjectsFacade;
import ec.gob.ambiente.utils.Mensaje;

@ManagedBean
@ViewScoped
public class SeguimientoSalvaguardaController  {


	
	private static final Logger log = Logger.getLogger(SeguimientoSalvaguardaController.class);
	
    @Getter
    @Setter
    @ManagedProperty(value = "#{seguimientoSalvaguardaBean}")
	private SeguimientoSalvaguardaBean seguimientoSalvaguardaBean;
    
    @Getter
    @Setter
    @ManagedProperty(value = "#{mensajesController}")
    private MensajesController mensajesController;
	
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@PostConstruct
	public void init(){
		try{
			cargarProyectos();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Carga los proyectos, programas o pdi
	 */
	public void cargarProyectos(){
		try{
			
			getSeguimientoSalvaguardaBean().listaProyectos=new ArrayList<>();
			getSeguimientoSalvaguardaBean().listaProyectos=getProjectsFacade().findAll();
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProyectos " + ": ").append(e.getMessage()));
		}
	}
}
