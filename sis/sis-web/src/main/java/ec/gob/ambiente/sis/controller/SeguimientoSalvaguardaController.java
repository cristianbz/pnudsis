package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;

@Named
@SessionScoped
public class SeguimientoSalvaguardaController  implements Serializable{


	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SeguimientoSalvaguardaController.class);
	
    
	
    @Getter
    @Setter
    @Inject
	private SeguimientoSalvaguardaBean seguimientoSalvaguardaBean;
    
    @Getter
    @Setter
    @Inject
    private MensajesController mensajesController;
	
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@EJB
	@Getter
	private PartnersFacade partnersFacade;
	
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
			
			getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<Projects>());
			getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().findAll());
//			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProyectos " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Busca el socio implementador del proyecto
	 */
	public void buscaSocioProyecto(){
		System.out.println(getSeguimientoSalvaguardaBean().getCodigoProyecto());
//		System.out.println(getSeguimientoSalvaguardaBean().getListaProyectos().size());
//		System.out.println(getSeguimientoSalvaguardaBean().getListaProyectos().get(0).getProjId());
//		if(getSeguimientoSalvaguardaBean().getListaProyectos()!=null){
//			Projects objetoProyecto=new Projects();
//			
//			for (Projects proyecto : getSeguimientoSalvaguardaBean().getListaProyectos()) {
//				if(proyecto.getProjId()==getSeguimientoSalvaguardaBean().getCodigoProyecto())
//					objetoProyecto=proyecto;
//			}				
//			getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().findByCode(objetoProyecto.getPartners().getPartId()) );
//			
//		}
	}
	public void inicializacion(){
		
	}
}
