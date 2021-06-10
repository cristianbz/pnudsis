/**
@autor proamazonia [Christian Báez]  26 may. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sis.bean.BuscaProyectosBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import lombok.Getter;
import lombok.Setter;

@Dependent
public class ComponenteBuscaProyectos implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	@Getter
	private BuscaProyectosBean buscaProyectosBean;
	
	@Getter    
    @Inject
    private MensajesController mensajesController;
	
	@EJB
	@Getter
	private PartnersFacade partnersFacade;
	
	@EJB
	@Getter
	private AdvanceExecutionSafeguardsFacade advanceExecutionSafeguardsFacade;
	
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@Getter
	@Setter
	private boolean esReporteGenero;
	
	@PostConstruct
	public void init(){
		cargaSociosImplementadores();
		getBuscaProyectosBean().setCodigoBusquedaProyecto(0);
	}
	/**
	 * Carga los socios implementadores
	 */
	public void cargaSociosImplementadores(){
		try{
			getBuscaProyectosBean().setListaSociosImplementadores(new ArrayList<>());
			getBuscaProyectosBean().setListaSociosImplementadores(getPartnersFacade().listarSociosImplementadores());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void vaciaDatosBusqueda(){
		getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
		getBuscaProyectosBean().setCodigoProyecto(0);
		getBuscaProyectosBean().setCodigoSocioImplementador(0);
						
		getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
		getBuscaProyectosBean().setSocioImplementador(new Partners());
		getBuscaProyectosBean().setTituloProyecto("");
	}
	/**
	 * Busca proyectos
	 */
	public void buscarProyectos(){
		try{
			getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
			if(getBuscaProyectosBean().getCodigoBusquedaProyecto()==1)			
				getBuscaProyectosBean().setListaProyectos(getProjectsFacade().listarProyectosPorIdSocioImpl(getBuscaProyectosBean().getCodigoSocioImplementador()));
			else if(getBuscaProyectosBean().getCodigoBusquedaProyecto()==2){
				if (getBuscaProyectosBean().getTituloProyecto().length()>5)
					getBuscaProyectosBean().setListaProyectos(getProjectsFacade().listarProyectosPorTextoTitulo(getBuscaProyectosBean().getTituloProyecto()));
				else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.longitudMinima"), "");
				}
					
			}else{
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionBusqueda") ,"" );
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Busca el avance de ejecucion por genero o salvaguarda del proyecto
	 * @param proyecto
	 */	
	public void buscaAvanceEjecucionDelProyecto(Projects proyecto){
		try{
			getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setAdvanceExecution(new AdvanceExecutionSafeguards());
			getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
			if(esReporteGenero){
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarAvanceGeneroPorProyecto(proyecto.getProjId()));
				getBuscaProyectosBean().setDatosProyecto(true);
			}else{
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(proyecto.getProjId()));
				getBuscaProyectosBean().setDatosProyecto(true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public AdvanceExecutionSafeguards getAdvanceExecution(){
		return getBuscaProyectosBean().getAdvanceExecution();
	}
	public void volverABuscarProyectos(){
		getBuscaProyectosBean().setDatosProyecto(false);
		
	}
	public boolean datosProyecto(){
		return getBuscaProyectosBean().isDatosProyecto();
	}
	public Projects proyectoSeleccionado(){
		return getBuscaProyectosBean().getProyectoSeleccionado();
	}
}

