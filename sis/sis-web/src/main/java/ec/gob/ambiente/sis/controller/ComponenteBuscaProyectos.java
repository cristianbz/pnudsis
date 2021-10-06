/**
@autor proamazonia [Christian Báez]  26 may. 2021

 **/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.ComponentsFacade;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsSpecificObjectivesFacade;
import ec.gob.ambiente.sigma.services.ProjectsStrategicPartnersFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.BuscaProyectosBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.Indicators;
import ec.gob.ambiente.sis.model.ProjectGenderIndicator;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.ProjectUsers;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.CatalogsTypeFacade;
import ec.gob.ambiente.sis.services.ExecutiveSummariesFacade;
import ec.gob.ambiente.sis.services.IndicatorsFacade;
import ec.gob.ambiente.sis.services.ProjectGenderIndicatorFacade;
import ec.gob.ambiente.sis.services.ProjectQuestionsFacade;
import ec.gob.ambiente.sis.services.ProjectUsersFacade;
import ec.gob.ambiente.sis.services.ProjectsGenderInfoFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import lombok.Getter;
import lombok.Setter;

@Dependent
public class ComponenteBuscaProyectos implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ComponenteBuscaProyectos.class);

	@Inject
	@Getter
	private BuscaProyectosBean buscaProyectosBean;

	@Getter    
	@Inject
	private MensajesController mensajesController;

	@EJB
	@Getter
	private ExecutiveSummariesFacade executiveSummarieFacade;

	@EJB
	@Getter
	private PartnersFacade partnersFacade;
	
	
//	@EJB
//	@Getter
//	private GenderAdvancesFacade genderAdvancesFacade;

	@Getter
	@Setter
	@Inject
	private AplicacionController aplicacionController;

	@Getter
	@Setter
	@Inject
	private AplicacionBean aplicacionBean;

	@Getter
	@Setter
	@Inject
	private LoginBean loginBean;

	@EJB
	@Getter
	private AdvanceExecutionSafeguardsFacade advanceExecutionSafeguardsFacade;

	@EJB
	@Getter
	private ProjectsFacade projectsFacade;

	@EJB
	@Getter
	private ProjectUsersFacade projectUsersFacade;

    @EJB
   	@Getter
   	private ComponentsFacade componentsFacade;

	@EJB
	@Getter
	private CatalogsFacade catalogsFacade;
	
	@EJB
	@Getter
	private CatalogsTypeFacade catalogsTypeFacade;

	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;

	@EJB
	@Getter
	private ProjectQuestionsFacade projectQuestionsFacade;

	@EJB
	@Getter
	private SectorsFacade sectorsFacade;

	@EJB
	@Getter
	private ProjectsStrategicPartnersFacade projectsStrategicPartnersFacade;
	
	@EJB
	@Getter
	private ProjectsGenderInfoFacade projectsGenderInfoFacade;
	
	@EJB
	@Getter
	private ProjectGenderIndicatorFacade projectGenderIndicatorFacade;

	
	@EJB
	@Getter
	private ProjectsSpecificObjectivesFacade projectsSpecificObjectivesFacade;
	
	@EJB
	@Getter
	private IndicatorsFacade indicatorsFacade;
	

	@Getter
	@Setter
	private boolean esReporteGenero;

	@Getter
	@Setter
	private boolean esReporteSalvaguardas;
	
	@Getter
	@Setter
	private Integer codigoPartner;

	private boolean esProyecto;
	private boolean nuevoIngreso;

	private List<Safeguards> listadoSalvaguardas;

	@PostConstruct
	public void init(){
		try{
			getBuscaProyectosBean().setTipoSocio(1);
			cargaSociosImplementadores();
			getBuscaProyectosBean().setCodigoBusquedaProyecto(0);
			esProyecto=true;
			nuevoIngreso=true;
			getBuscaProyectosBean().setSinDatosProyectoPreguntas(false);
			indicaTipoRol();
			getBuscaProyectosBean().setListaLineasGenero(getCatalogsTypeFacade().listaLineasGenero());
			getBuscaProyectosBean().setLineaGeneroSel(new CatalogsType());
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Carga los socios implementadores
	 */
	public void cargaSociosImplementadores(){
		try{
			getBuscaProyectosBean().setListaSociosImplementadores(new ArrayList<>());
			getBuscaProyectosBean().setListaSociosImplementadores(getPartnersFacade().listarSociosImplementadores());
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSociosImplementadores " + ": ").append(e.getMessage()));
		}
	}

	/**
	 * Carga los sectores de interes
	 */
	public void cargaSectoresInteres(){
		try{
			getBuscaProyectosBean().setListaSectoresDisponibles(new ArrayList<Sectors>());
			getBuscaProyectosBean().setListaSectoresDisponibles(getSectorsFacade().buscarTodosLosSectores());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Carga los sectores de interes del proyecto seleccionado
	 */
	public void sectoresInteresProyecto(){

		getBuscaProyectosBean().setListaSectoresSeleccionados(new ArrayList<>());
		if(getBuscaProyectosBean().getAdvanceExecution()!=null && getBuscaProyectosBean().getAdvanceExecution().getAdvanceSectorsList()!=null){
			for(AdvanceSectors as:getBuscaProyectosBean().getAdvanceExecution().getAdvanceSectorsList())
				if(as.isAdseSelectedSector())
					getBuscaProyectosBean().getListaSectoresSeleccionados().add(as.getSectors().getSectId());					
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
			if(getBuscaProyectosBean().getTipoRol()==4 || getBuscaProyectosBean().getTipoRol()==1){
				if(getBuscaProyectosBean().getCodigoBusquedaProyecto()==1){			
					getBuscaProyectosBean().setListaProyectos(getProjectsFacade().listarProyectosPorIdSocioImpl(getBuscaProyectosBean().getCodigoSocioImplementador()));
					if(getBuscaProyectosBean().getListaProyectos().size()==0){
						Mensaje.actualizarComponente(":form:growl");				
						Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.noProyectos"));
					}

				}else if(getBuscaProyectosBean().getCodigoBusquedaProyecto()==2){
					if (getBuscaProyectosBean().getTituloProyecto().length()>5){
						getBuscaProyectosBean().setListaProyectos(getProjectsFacade().listarProyectosPorTextoTitulo(getBuscaProyectosBean().getTituloProyecto()));
						if(getBuscaProyectosBean().getListaProyectos().size()==0){
							Mensaje.actualizarComponente(":form:growl");				
							Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.noProyectos"));
						}
					}else{
						Mensaje.actualizarComponente(":form:growl");				
						Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.longitudMinima"));
					}

				}else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.seleccionBusqueda"));
				}
			}else{
				List<ProjectUsers> listaProyectousuarios=new ArrayList<>();
				getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
				if(getLoginBean().getListaProyectosDelUsuario().size()>0){	
					listaProyectousuarios=getLoginBean().getListaProyectosDelUsuario();
					for (ProjectUsers pu : listaProyectousuarios) {
						getBuscaProyectosBean().getListaProyectos().add(pu.getProjects());
					}
				}else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.proyectosUsuario"));
				}
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
			getBuscaProyectosBean().setPeriodoDesde(null);
			getBuscaProyectosBean().setAnioReporte(null);
			getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setAdvanceExecution(new AdvanceExecutionSafeguards());
			getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
			if(esReporteGenero){
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarAvanceGeneroPorProyecto(proyecto.getProjId()));								
				if (getBuscaProyectosBean().getAdvanceExecution()!=null && !getBuscaProyectosBean().getAdvanceExecution().isAdexIsReported()){
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0, 4)));
					getBuscaProyectosBean().setPeriodoDesde("01");
				}else{

					getBuscaProyectosBean().setDatosProyecto(true);
				}
			}else{
				if(getLoginBean().getTipoRol()==3){						
					cargaSectoresInteres();		
					cargaAvanceEjecucionSalvaguardas(getBuscaProyectosBean().getProyectoSeleccionado());
					getBuscaProyectosBean().setDatosProyecto(true);
					sectoresInteresProyecto();					
				}else{					
					getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));					
					Mensaje.verDialogo("dlgSeleccionSocios");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Busca el avance de ejecucion del proyecto reportado
	 * @param proyecto
	 */
	public void buscaAvanceEjecucionDelProyectoReportado(AdvanceExecutionSafeguards avanceEjecucion){
		try{
			if(avanceEjecucion.getProjects().getPartners()!= null)
				getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(avanceEjecucion.getProjects().getPartners().getPartId()) );
			if(avanceEjecucion.isAdexIsReported() || avanceEjecucion.getAdexReportedStatus().equals("F"))
				getBuscaProyectosBean().setProyectoReportado(true);
			else
				getBuscaProyectosBean().setProyectoReportado(false);
			if(esReporteGenero){
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);								
				if (getBuscaProyectosBean().getAdvanceExecution()!=null ){
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0, 4)));
					getBuscaProyectosBean().setPeriodoDesde("01");
				}else{
					getBuscaProyectosBean().setDatosProyecto(true);
				}
			}else{				
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
//				getBuscaProyectosBean().setCodigoComponente(avanceEjecucion.getProjectsSpecificObjectives().getPsobId());
				if(avanceEjecucion.getProjectsStrategicPartners()!=null){
//					getBuscaProyectosBean().setCodigoStrategicPartner(avanceEjecucion.getProjectsStrategicPartners().getPspaId());
//					System.out.println(getBuscaProyectosBean().getCodigoStrategicPartner());
					getBuscaProyectosBean().setNombreSocioEstrategico(getProjectsStrategicPartnersFacade().partnerEstrategico(getBuscaProyectosBean().getCodigoStrategicPartner()).getPartners().getPartName());
				}else{
					getBuscaProyectosBean().setNombreSocioEstrategico("");
				}
					cargaSectoresInteres();		
					getBuscaProyectosBean().setDatosProyecto(true);
//					sectoresInteresProyecto();
					getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0,4)));
					getBuscaProyectosBean().setPeriodoDesde("01");
					sectoresInteresProyecto();
//					organizaSalvaguardasEnComponentes(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(),getBuscaProyectosBean().getCodigoStrategicPartner());
//					salvaguardasAsignadasAlComponente();
					
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void seguimientoProyectoReportado(){
		////hacer el seguimiento al proyecto reportado
	}
	
//	public void nuevoSeguimientoProyecto(Projects proyecto){
//		try{
//			getBuscaProyectosBean().setListaSectoresSeleccionados(new ArrayList<>());
//			getBuscaProyectosBean().setPeriodoDesde(null);
//			getBuscaProyectosBean().setAnioReporte(null);
//			getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
//			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
////			getBuscaProyectosBean().setAdvanceExecution(new AdvanceExecutionSafeguards());
//			getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
//			AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
//			avanceEjecucion.setAdexIsReported(false);
//			avanceEjecucion.setAdexRegisterDate(new Date());
//			avanceEjecucion.setAdexCreationDate(new Date());				
//			avanceEjecucion.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
//
//			avanceEjecucion.setUsers(getLoginBean().getUser());
//			avanceEjecucion.setAdexStatus(true);
//
//			avanceEjecucion.setAdexCreatorUser(getLoginBean().getUser().getUserName());
//			avanceEjecucion.setAdexTermFrom(null);
//			avanceEjecucion.setAdexTermTo(null);
//
//			if(!esReporteGenero){
//				cargaSectoresInteres();
//				avanceEjecucion.setAdexIsGender(false);
//				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
//				getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
//				getBuscaProyectosBean().getResumenEjecutivo().setExsuCreationDate(new Date());
//				getBuscaProyectosBean().getResumenEjecutivo().setExsuUserCreator(getLoginBean().getUser().getUserName());
//				getBuscaProyectosBean().getResumenEjecutivo().setExsuStatus(true);
//				getBuscaProyectosBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
//			}else{
//				avanceEjecucion.setAdexIsGender(true);
//			}
//			getBuscaProyectosBean().setDatosProyecto(true);
//			getBuscaProyectosBean().setNuevoSeguimiento(true);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
	
	public void nuevoSeguimientoProyectoSocioEstrategico(Projects proyecto){
		try{
			getBuscaProyectosBean().setListaSectoresSeleccionados(new ArrayList<>());
			getBuscaProyectosBean().setPeriodoDesde(null);
			getBuscaProyectosBean().setAnioReporte(null);
			getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);

			getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
			AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
			avanceEjecucion.setAdexIsReported(false);
			avanceEjecucion.setAdexRegisterDate(new Date());
			avanceEjecucion.setAdexCreationDate(new Date());				
			avanceEjecucion.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());

			avanceEjecucion.setUsers(getLoginBean().getUser());
			avanceEjecucion.setAdexStatus(true);

			avanceEjecucion.setAdexCreatorUser(getLoginBean().getUser().getUserName());
			avanceEjecucion.setAdexTermFrom(null);
			avanceEjecucion.setAdexTermTo(null);
			avanceEjecucion.setAdexReportedStatus("I");
			
			ProjectsStrategicPartners psp=new ProjectsStrategicPartners();
			if (getLoginBean().getTipoRol()==3){
				for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
					if(pu.getProjects().getProjId() == proyecto.getProjId()){						
						getBuscaProyectosBean().setCodigoStrategicPartner(pu.getPspaId());
						psp.setPspaId(pu.getPspaId());
						avanceEjecucion.setProjectsStrategicPartners(psp);
						break;
					}
				}
			}
			
			if(!esReporteGenero){
				cargaSectoresInteres();
				avanceEjecucion.setAdexIsGender(false);
//				getBuscaProyectosBean().setCodigoComponente(0);
//				organizaSalvaguardasEnComponentes(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(),getBuscaProyectosBean().getCodigoStrategicPartner());
			}else{
				
				avanceEjecucion.setAdexIsGender(true);		

			}
			if(avanceEjecucion.getProjectsStrategicPartners()!=null)
				getBuscaProyectosBean().setNombreSocioEstrategico(getProjectsStrategicPartnersFacade().partnerEstrategico(avanceEjecucion.getProjectsStrategicPartners().getPspaId()).getPartners().getPartName());
			else
				getBuscaProyectosBean().setNombreSocioEstrategico("");
			getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
			
			
			getBuscaProyectosBean().setDatosProyecto(true);
			getBuscaProyectosBean().setNuevoSeguimiento(true);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void informacionAvanceEjecucion(){
		cargaSectoresInteres();	
		codigoPartner = null;
		if(getBuscaProyectosBean().getCodigoStrategicPartner()==null && getBuscaProyectosBean().isSalvaguardasSociosEstrategicos()){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.seleccionSocio"));
		}else{

			if(getBuscaProyectosBean().getCodigoStrategicPartner()== null){					
				cargarAvanceEjecucionSalvaguarda(0,getBuscaProyectosBean().getProyectoSeleccionado().getProjId());
			}else{			
				codigoPartner = getBuscaProyectosBean().getCodigoStrategicPartner();
				cargarAvanceEjecucionSalvaguarda(getBuscaProyectosBean().getCodigoStrategicPartner(),getBuscaProyectosBean().getProyectoSeleccionado().getProjId());
				getBuscaProyectosBean().setCodigoStrategicPartner(codigoPartner);
			}
			getBuscaProyectosBean().setDatosProyecto(true);
			sectoresInteresProyecto();
			getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			Mensaje.ocultarDialogo("dlgSeleccionSocios");
		}
	}

	public void buscaAsignacionSalvaguardas(Projects proyecto){
		try{
			getBuscaProyectosBean().setAsignacionSalvaguardas(true);

		}catch(Exception e){
			e.printStackTrace();
		}
	}	

	public AdvanceExecutionSafeguards getAdvanceExecution(){
		return getBuscaProyectosBean().getAdvanceExecution();
	}
	public void volverABuscarProyectos(){
		getBuscaProyectosBean().setDatosProyecto(false);
		getBuscaProyectosBean().setPosicionTab(0);
		getBuscaProyectosBean().setPosicionTabGenero(0);
		getBuscaProyectosBean().setListaSalvaguardasAsignadas(null);
		getBuscaProyectosBean().setPreguntasSelecionadas(null);
		getBuscaProyectosBean().setAsignacionGenero(false);
	}
	public boolean datosProyecto(){
		return getBuscaProyectosBean().isDatosProyecto();
	}
	public Projects proyectoSeleccionado(){
		return getBuscaProyectosBean().getProyectoSeleccionado();
	}
	/**
	 * Carga los partners del proyecto
	 */
	public void cargaPartnersProyecto(Projects proyecto){
		try{
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));			
			getBuscaProyectosBean().setDatosProyecto(true);
			getBuscaProyectosBean().setAsignacionSalvaguardas(true);
//			getBuscaProyectosBean().setListaObjetivosEspecificos(new ArrayList<>());
//			getBuscaProyectosBean().setListaObjetivosEspecificos(getProjectsSpecificObjectivesFacade().listaObjetivosProyecto(proyecto.getProjId()));
			cargaListadoSalvaguardas();
			getBuscaProyectosBean().setTipoSocio(1);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setNombreSocioEstrategico("");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void capturaNombreSocioEstrategico(){
		for (ProjectsStrategicPartners psp : getBuscaProyectosBean().getListaPartnersProyectos()) {
			if(getBuscaProyectosBean().getCodigoStrategicPartner() == psp.getPspaId()){
				getBuscaProyectosBean().setNombreSocioEstrategico(psp.getPartners().getPartName());
				break;
			}
		}
	}
	
	public boolean asignacionSalvaguardas(){
		return getBuscaProyectosBean().isAsignacionSalvaguardas();
	}
	public void tabChange() {
		getBuscaProyectosBean().setPosicionTab(0);
	}
	public void tabChangeGenero() {
		getBuscaProyectosBean().setPosicionTabGenero(0);
	}
	/**
	 * Avanza al tab de seleccion de preguntas para salvaguardas
	 */
	public void siguienteTabPreguntas(){

		if((getBuscaProyectosBean().getTipoSocio()==2 && getBuscaProyectosBean().getCodigoStrategicPartner()!=null) ||
				getBuscaProyectosBean().getTipoSocio()==1){
			getBuscaProyectosBean().setPosicionTab(1);
			preguntasAsignadas();
		}else{
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.seleccionSocio"));
		}
	}
	public void atrasTabSocios(){
		getBuscaProyectosBean().setPosicionTab(0);
	}
	public void atrasTabPreguntas(){
		getBuscaProyectosBean().setPosicionTab(1);
	}
	public void siguienteTabSalvaguardasSeleccionadas(){
		if(getBuscaProyectosBean().getPreguntasSelecionadas()!=null && getBuscaProyectosBean().getPreguntasSelecionadas().size()>0){
			getBuscaProyectosBean().setPosicionTab(2);
			asignarSalvaguardas();
		}else{
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.selecionPreguntas"));
		}
	}
	/**
	 * Lista de preguntas asignadas para asignacion salvaguardas
	 */
	public void preguntasAsignadas(){
		try{
			getBuscaProyectosBean().setPreguntasSelecionadas(new ArrayList<>());
			getBuscaProyectosBean().setListaPreguntasSalvaguardas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PREGUNTASASIGNACIONSALVAGUARDAS.getCodigo()));
			getBuscaProyectosBean().setListaPreguntasAsignadas(new ArrayList<>());
			if(getBuscaProyectosBean().getTipoSocio()==2)
				getBuscaProyectosBean().setListaPreguntasAsignadas(getProjectQuestionsFacade().listaPreguntasPartnerSeleccionadas(getBuscaProyectosBean().getCodigoStrategicPartner()));
			else
				getBuscaProyectosBean().setListaPreguntasAsignadas(getProjectQuestionsFacade().listaPreguntasProyectoSeleccionadas(getBuscaProyectosBean().getProyectoSeleccionado().getProjId()));			
			
			getBuscaProyectosBean().setListaPreguntasAsignadasAux(getBuscaProyectosBean().getListaPreguntasAsignadas());
			if(getBuscaProyectosBean().getListaPreguntasAsignadas().size()==0){	
				getBuscaProyectosBean().setSinDatosProyectoPreguntas(true);
				nuevoIngreso = true;
			}else{
				//Aqui consultar si ya respondio preguntas
				nuevoIngreso= false;
				for(ProjectQuestions preguntas:getBuscaProyectosBean().getListaPreguntasAsignadas()){
					getBuscaProyectosBean().getPreguntasSelecionadas().add(preguntas.getCatalogs().getCataId());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Seleccion de salvaguardas en base a las preguntas seleccionadas
	 */
	@SuppressWarnings("rawtypes")
	public void asignarSalvaguardas(){
		int pregunta=0;
		List<Catalogs> catalogoPreguntas=new ArrayList<>();
		getBuscaProyectosBean().setListaSalvaguardasAsignadas(new ArrayList<>());
		Map<String,ProjectQuestions> mapaTemp=new HashMap<String,ProjectQuestions>();
		List<ProjectQuestions> listaTemporal=new ArrayList<>();	

		Iterator itera = getBuscaProyectosBean().getPreguntasSelecionadas().iterator();
		while(itera.hasNext()){
			pregunta = Integer.valueOf(itera.next().toString());
			for(Catalogs cat:getBuscaProyectosBean().getListaPreguntasSalvaguardas()){
				if(pregunta == cat.getCataId()){
					catalogoPreguntas.add(cat);
				}
			}
		}

		for(Catalogs catalogo:catalogoPreguntas){
			ProjectQuestions salvaguardaAsignada=new ProjectQuestions();
			for(Safeguards salvaguarda:listadoSalvaguardas){
				if(catalogo.getCataText1().equals(salvaguarda.getSafeCode())){
					if(esProyecto)
						salvaguardaAsignada.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
					else{
						ProjectsStrategicPartners partner=new ProjectsStrategicPartners();
						partner.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
						salvaguardaAsignada.setProjectsStrategicPartners(partner);
					}
					salvaguardaAsignada.setSafeguards(salvaguarda);
					salvaguardaAsignada.setPrquStatus(true);
					salvaguardaAsignada.setCatalogs(catalogo);
					listaTemporal.add(salvaguardaAsignada);
				}
			}
		}
		getBuscaProyectosBean().setListaPreguntasAsignadas(listaTemporal);			
		for(ProjectQuestions sa:listaTemporal){
			mapaTemp.put(sa.getSafeguards().getSafeCode(), sa);
		}
		for(Entry<String,ProjectQuestions> sa:mapaTemp.entrySet()){
			getBuscaProyectosBean().getListaSalvaguardasAsignadas().add(sa.getValue());
		}
//		armaComponentesEnSalvaguardas();
		
		
	}
	
//	public void armaComponentesEnSalvaguardas(){
//		for(ProjectQuestions pq: getBuscaProyectosBean().getListaPreguntasAsignadasAux()){
//			for(ProjectQuestions pqAux: getBuscaProyectosBean().getListaSalvaguardasAsignadas()){
//				if(pq.getSafeguards().getSafeId() == pqAux.getSafeguards().getSafeId()){					
//					String[] vector = pq.getPrquComponents().split(" ");
//					pqAux.setPrquComponentsAux(new Integer[vector.length]);
//					for(int x=0;x<vector.length;x++){						
//						pqAux.getPrquComponentsAux()[x]=Integer.parseInt(vector[x]);						
//					}
//				}
//			}
//		}
//		
//	}
	

	public void cargaListadoSalvaguardas(){
		try{
			List<Object[]> listaTemp=getAplicacionBean().getListaSalvaguardas();
			listadoSalvaguardas=new ArrayList<>();
			for (Object[] objects : listaTemp) {
				Safeguards salvaguarda=new Safeguards();
				salvaguarda.setSafeId(Integer.parseInt(objects[0].toString()));				
				salvaguarda.setSafeCode(objects[3].toString());
				salvaguarda.setSafeTitle(objects[4].toString());
				listadoSalvaguardas.add(salvaguarda);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void quitaSeleccionSalvaguardaSocioEstrategico(){
//		if(!getBuscaProyectosBean().isSalvaguardasSociosEstrategicos()){
//			getBuscaProyectosBean().setCodigoStrategicPartner(null);
//			esProyecto = true;
//		}else
//			esProyecto = false;
//		getBuscaProyectosBean().setNombreSocioEstrategico("");
		if(getBuscaProyectosBean().getTipoSocio() == 1){
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			esProyecto = true;
		}else
			esProyecto = false;
		getBuscaProyectosBean().setNombreSocioEstrategico("");
	}
	/**
	 * Grabar la asignacion de preguntas y salvaguardas
	 */
	public void grabarAsignacionSalvaguardas(){
		try{
			int tipo=0;
			if(esProyecto)
				tipo=getBuscaProyectosBean().getProyectoSeleccionado().getProjId();
			else
				tipo = getBuscaProyectosBean().getCodigoStrategicPartner();
			
			
//			organizaComponentesSeleccionados();
			getProjectQuestionsFacade().agregaPreguntasProyecto(getBuscaProyectosBean().getListaPreguntasAsignadas(),esProyecto,tipo,nuevoIngreso);
			getBuscaProyectosBean().setAsignacionSalvaguardas(false);
			volverABuscarProyectos();
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarAsignacionSalvaguardas " + ": ").append(e.getMessage()));
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
//	public void organizaComponentesSeleccionados(){
//		for(ProjectQuestions pq:getBuscaProyectosBean().getListaSalvaguardasAsignadas()){
//			StringBuffer cadena= new StringBuffer();
//			for(int x=0;x<pq.getPrquComponentsAux().length;x++){
//				cadena = cadena.append(pq.getPrquComponentsAux()[x]).append(" ");
//			}
//			pq.setPrquComponents(cadena.toString());			
//		}
//		for(ProjectQuestions pq:getBuscaProyectosBean().getListaPreguntasAsignadas()){
//			for(ProjectQuestions pqsa:getBuscaProyectosBean().getListaSalvaguardasAsignadas()){
//				if(pq.getSafeguards().getSafeId()== pqsa.getSafeguards().getSafeId()){
//					pq.setPrquComponents(pqsa.getPrquComponents());
//				}
//			}
//		}
//		
//	}
	
	/**
	 * Setea el tipo de rol del usuario
	 */
	public void indicaTipoRol(){
		getBuscaProyectosBean().setTipoRol(getLoginBean().getTipoRol());
	}
	public void cargaAvanceEjecucionSalvaguardas(Projects proyecto){
		for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
			if(pu.getProjects().getProjId()== proyecto.getProjId() && pu.getUsers().getUserId()== getLoginBean().getUser().getUserId()){
				if(pu.getPspaId()== null){					
					cargarAvanceEjecucionSalvaguarda(0,pu.getProjects().getProjId());
					break;
				}else{					
					cargarAvanceEjecucionSalvaguarda(pu.getPspaId(),pu.getProjects().getProjId());
					break;
				}
			}
		}
	}
	public void cargarAvanceEjecucionSalvaguarda(int codigoPartner,int codigoProyecto){
		try{

			getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
			getBuscaProyectosBean().setAdvanceExecution(new AdvanceExecutionSafeguards());
			if(codigoPartner==0)
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(codigoProyecto));
			else
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarPorStrategicPartner(codigoPartner,codigoProyecto));
			if (getBuscaProyectosBean().getAdvanceExecution()==null){
				AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
				avanceEjecucion.setAdexIsReported(false);
				avanceEjecucion.setAdexRegisterDate(new Date());
				avanceEjecucion.setAdexCreationDate(new Date());				
				avanceEjecucion.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
				if(codigoPartner>0){
					ProjectsStrategicPartners psp=new ProjectsStrategicPartners();
					psp.setPspaId(codigoPartner);
					avanceEjecucion.setProjectsStrategicPartners(psp);
				}
				avanceEjecucion.setUsers(getLoginBean().getUser());
				avanceEjecucion.setAdexStatus(true);
				avanceEjecucion.setAdexIsGender(false);
				avanceEjecucion.setAdexCreatorUser(getLoginBean().getUser().getUserName());
				avanceEjecucion.setAdexTermFrom(null);
				avanceEjecucion.setAdexTermTo(null);
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
				getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
				getBuscaProyectosBean().getResumenEjecutivo().setExsuCreationDate(new Date());
				getBuscaProyectosBean().getResumenEjecutivo().setExsuUserCreator(getLoginBean().getUser().getUserName());
				getBuscaProyectosBean().getResumenEjecutivo().setExsuStatus(true);
				getBuscaProyectosBean().getResumenEjecutivo().setExsuRegisterDate(new Date());

			}else{
				getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0,4)));
				getBuscaProyectosBean().setPeriodoDesde("01");
				sectoresInteresProyecto();
				getBuscaProyectosBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getBuscaProyectosBean().getAdvanceExecution().getAdexId()));
				if(getBuscaProyectosBean().getResumenEjecutivo()==null){
					getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
					getBuscaProyectosBean().getResumenEjecutivo().setExsuCreationDate(new Date());
					getBuscaProyectosBean().getResumenEjecutivo().setExsuUserCreator(getLoginBean().getUser().getUserName());
					getBuscaProyectosBean().getResumenEjecutivo().setExsuStatus(true);
					getBuscaProyectosBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				}

			}


		}catch(NoResultException e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		} catch (Exception e) {
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Cancela la seleccion del socio estrategico
	 */
	public void cancelaSeleccionSocio(){
		getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
		getBuscaProyectosBean().setCodigoStrategicPartner(null);
	}
	
	public void cargaProyectosReportados(Projects proyecto){
		try{
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
				if(pu.getProjects().getProjId() == proyecto.getProjId()){						
					getBuscaProyectosBean().setCodigoStrategicPartner(pu.getPspaId());
					break;
				}
			}
			if(getLoginBean().getTipoRol() == 2)
				getBuscaProyectosBean().setListaProyectosReportados(getAdvanceExecutionSafeguardsFacade().listarProyectosReportados(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), 2));
			else if(getLoginBean().getTipoRol() == 3){
				getBuscaProyectosBean().setListaProyectosReportados(getAdvanceExecutionSafeguardsFacade().listarProyectosReportadosSocioEstrategico(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), 2,getBuscaProyectosBean().getCodigoStrategicPartner()));
			}
			if(getBuscaProyectosBean().getListaProyectosReportados().size()==0){
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "" ,getMensajesController().getPropiedad("info.noReportesGenerados") );
			}
			Collections.sort(getBuscaProyectosBean().getListaProyectosReportados(), new Comparator<AdvanceExecutionSafeguards>(){
				@Override
				public int compare(AdvanceExecutionSafeguards o1, AdvanceExecutionSafeguards o2) {
					return o1.getAdexTermFrom().compareToIgnoreCase(o2.getAdexTermFrom());
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Carga los proyectos en base a los criterios de busqueda seleccionados
	 */
	public void cargarProyectosCriterioBusqueda(){
		try{
			String periodoReporte = null;
			if(getBuscaProyectosBean().getTipoSocio()==2 && getBuscaProyectosBean().getCodigoStrategicPartner()==null){
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "" ,getMensajesController().getPropiedad("error.seleccionSocio") );
			}else{
				if (getLoginBean().getTipoRol()==3){
					for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
						if(pu.getProjects().getProjId() == getBuscaProyectosBean().getProyectoSeleccionado().getProjId()){						
							getBuscaProyectosBean().setCodigoStrategicPartner(pu.getPspaId());														
							break;
						}
					}
				}

				if(getBuscaProyectosBean().getAnioReporte() != null){
					periodoReporte = String.valueOf(getBuscaProyectosBean().getAnioReporte());
					periodoReporte = periodoReporte.concat("-").concat("01");
				}

				getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
				getBuscaProyectosBean().setListaProyectosReportados(getAdvanceExecutionSafeguardsFacade().listarProyReportadosConCriteriosBusqueda(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), getBuscaProyectosBean().getCodigoStrategicPartner(),  periodoReporte, getBuscaProyectosBean().getEstadoReporte()));
				if(getBuscaProyectosBean().getListaProyectosReportados().size()==0){
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "" ,getMensajesController().getPropiedad("info.noReportesGenerados") );
				}else{
					getBuscaProyectosBean().setMostrarOpcionesBusqueda(false);

					//				for (AdvanceExecutionSafeguards adex : getBuscaProyectosBean().getListaProyectosReportados()) {
					//					adex.setAdexComponente(adex.getProjectsSpecificObjectives().getGeloId().getGeloName().concat(":").concat(adex.getProjectsSpecificObjectives().getPsobDescription()));
					//				}
					//				Collections.sort(getBuscaProyectosBean().getListaProyectosReportados(), new Comparator<AdvanceExecutionSafeguards>(){
					//					@Override
					//					public int compare(AdvanceExecutionSafeguards o1, AdvanceExecutionSafeguards o2) {
					//						return o1.getAdexTermFrom().compareToIgnoreCase(o2.getAdexTermFrom());
					//					}
					//				});

					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
					periodoReporte="";
					//				getBuscaProyectosBean().setCodigoComponente(0);	
//					if(getBuscaProyectosBean().getTipoRol() != 3)
//						getBuscaProyectosBean().setCodigoStrategicPartner(null);
//					System.out.println(getBuscaProyectosBean().getCodigoStrategicPartner());
					setCodigoPartner(getBuscaProyectosBean().getCodigoStrategicPartner());
					getBuscaProyectosBean().setEstadoReporte("");
					getBuscaProyectosBean().setAnioReporte(0);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Muestra el panel para busqueda de proyectos reportados
	 * @param proyecto  Proyecto seleccionado
	 */
	public void mostrarPanelBusquedaProyectos(Projects proyecto){
		try{
			if(!esReporteGenero){
				getBuscaProyectosBean().setNuevoSeguimiento(false);
//				getBuscaProyectosBean().setCodigoComponente(0);
				getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
				getBuscaProyectosBean().setMostrarOpcionesBusqueda(true);
				getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
				getBuscaProyectosBean().setListaPartnersProyectos(new ArrayList<>());		
//				getBuscaProyectosBean().setListaComponentesParaBusqueda(new ArrayList<>());
				getBuscaProyectosBean().setCodigoStrategicPartner(null);
				getBuscaProyectosBean().setAnioReporte(null);
//				getBuscaProyectosBean().setListaComponentesParaBusqueda(getProjectsSpecificObjectivesFacade().listaComponentesProyecto(proyecto.getProjId()));
				getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));
//				Collections.sort(getBuscaProyectosBean().getListaComponentesParaBusqueda(), new Comparator<ProjectsSpecificObjectives>(){
//					@Override
//					public int compare(ProjectsSpecificObjectives o1, ProjectsSpecificObjectives o2) {
//						return o1.getGeloId().getGeloName().compareToIgnoreCase(o2.getGeloId().getGeloName());
//					}
//				});
			}else{
				cargaProyectosReportados(proyecto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Carga proyectos reportados de genero del socio estrategico 
	 * @param proyecto
	 */
	public void cargaProyectosSocioEstrategicoReportados(Projects proyecto){
		try{
			int codigoSocioEstrategico=0;
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
				if(pu.getProjects().getProjId() == proyecto.getProjId()){
					codigoSocioEstrategico=pu.getPspaId();
					getBuscaProyectosBean().setCodigoStrategicPartner(codigoSocioEstrategico);
					break;
				}
			}
			
			if(esReporteGenero){
				getBuscaProyectosBean().setListaProyectosReportados(getAdvanceExecutionSafeguardsFacade().listarProyectosReportadosSocioEstrategico(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), 2,codigoSocioEstrategico));				
			}else{
				getBuscaProyectosBean().setListaProyectosReportados(getAdvanceExecutionSafeguardsFacade().listarProyectosReportadosSocioEstrategico(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), 1,codigoSocioEstrategico));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Permite asignar las lineas de genero para que puedan ser reportadas
	 * @param proyecto
	 */
	public void asignacionLineasGenero(Projects proyecto){
		try{
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setAsignacionGenero(true);		
			getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));			
			getBuscaProyectosBean().setDatosProyecto(true);
			getBuscaProyectosBean().setProyectoTienePlanGenero(true);
			getBuscaProyectosBean().setListaIndicadores(getIndicatorsFacade().listaIndicadoresGenero());
//			getBuscaProyectosBean().setListaObjetivosEspecificos(new ArrayList<>());
//			getBuscaProyectosBean().setListaObjetivosEspecificos(getProjectsSpecificObjectivesFacade().listaObjetivosProyecto(proyecto.getProjId()));
			getBuscaProyectosBean().setTipoSocio(1);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setNombreSocioEstrategico("");
//			if(getAplicacionBean().getListaComponentes()==null){
//				getAplicacionBean().setListaComponentes(new ArrayList<>());
//				getAplicacionBean().setListaComponentes(getComponentsFacade().listaComponentesActivos(getBuscaProyectosBean().getProyectoSeleccionado().getProjId()));				
//			}
			getBuscaProyectosBean().setListadoComponentes(new ArrayList<>());
			getBuscaProyectosBean().setListadoComponentes(getComponentsFacade().listaComponentesActivos(getBuscaProyectosBean().getProyectoSeleccionado().getProjId()));
//			for (Components comp : getAplicacionBean().getListaComponentes()) {
//				getBuscaProyectosBean().getListadoComponentes().add(comp.getCompName());
//			}
			Components componente = new Components();
    		componente.setCompId(1000);
    		componente.setCompStatus(true);
    		componente.setCompCode("CEO5");
    		componente.setCompName("Componentes Operativos");
    		getBuscaProyectosBean().getListadoComponentes().add(componente);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Pasa al tab de seleccion de socios estrategicos para reportar genero
	 */
	public void siguienteTabGeneroSeleccionSocio(){
		if(getBuscaProyectosBean().isProyectoTienePlanGenero()){
			getBuscaProyectosBean().setPosicionTabGenero(1);	
			getBuscaProyectosBean().setLineasGeneroSociosEstrategicos(false);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
		}else{
//			Mensaje.actualizarComponente(":form:buscaProyectos:tabsGenero:msgPlanGenero");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,"" , getMensajesController().getPropiedad("info.sinGenero") );
		}		
	}
	public void quitaSeleccionLineasGeneroSocioEstrategico(){
//		if(!getBuscaProyectosBean().isLineasGeneroSociosEstrategicos()){
//			getBuscaProyectosBean().setCodigoStrategicPartner(null);
//			esProyecto = true;
//		}else
//			esProyecto = false;
//		getBuscaProyectosBean().setNombreSocioEstrategico("");
		if(getBuscaProyectosBean().getTipoSocio()==1){
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			esProyecto = true;
		}else
			esProyecto = false;
		getBuscaProyectosBean().setNombreSocioEstrategico("");
	}
	/**
	 * Avanza al tab de asignacion de lineas de genero
	 */
	public void siguienteTabAsignaLineasGenero(){
		cargarLineasGeneroAsignadas();
		getBuscaProyectosBean().setPosicionTabGenero(2);
	}
	
	public void cargarLineasGeneroAsignadas(){
		try{
			getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(new ArrayList<>());
			if(getBuscaProyectosBean().getCodigoStrategicPartner()==null)
				getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(getProjectsGenderInfoFacade().listaLineasGeneroProyectoPartner(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), 0));
			else
				getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(getProjectsGenderInfoFacade().listaLineasGeneroProyectoPartner(getBuscaProyectosBean().getProjectGenderInfoSeleccionado().getPginId(), getBuscaProyectosBean().getCodigoStrategicPartner()));
			if(getBuscaProyectosBean().getListaAccionesdeGeneroProyecto()!=null && getBuscaProyectosBean().getListaAccionesdeGeneroProyecto().size()>0){
				String [] codigos= null;
				for (ProjectsGenderInfo pgi : getBuscaProyectosBean().getListaAccionesdeGeneroProyecto()) {
					codigos = pgi.getPginComponents().split(",");
					pgi.setComponentesGenero(ubicaComponentesGenero(codigos));
				}
				for(ProjectsGenderInfo pgi: getBuscaProyectosBean().getListaAccionesdeGeneroProyecto()){
					pgi.setProjectGenderIndicatorList(getProjectGenderIndicatorFacade().listaPorProjectGender(pgi.getPginId()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String ubicaComponentesGenero(String[] codigos){
		StringBuilder componentes=new StringBuilder();
		for(int x=0;x<codigos.length;x++){
			for (Components c : getBuscaProyectosBean().getListadoComponentes()) {
				if(c.getCompId() == Integer.valueOf(codigos[x].trim())){
					componentes.append(c.getCompName()).append(",");
				}
			}
		}
		return componentes.toString();
	}
	
	
	public void atrasInicioGenero(){
		getBuscaProyectosBean().setPosicionTabGenero(0);
	}
	public void atrasSelecionSocioGenero(){
		getBuscaProyectosBean().setPosicionTabGenero(1);
	}
	
//	public void seleccionarLineaGeneroTema1(ProjectsGenderInfo pgi){
//		getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);
//		if(pgi.isSeleccionado()==false){
//			if(pgi.getCataId().getCataNumber()!=7 && (pgi.getPginResultsType()!=null || pgi.getPginAssociatedResults()!=null || pgi.getPginBudget()>0)){
//				Mensaje.verDialogo("dlgEliminaLineaGenero");
//			}
//		}
//	}
//	
//	public void seleccionarLineaGeneroTema2(ProjectsGenderInfo pgi){
//		getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);
//		if(pgi.isSeleccionado()==false){
//			if(pgi.getCataId().getCataNumber()!=4 && (pgi.getPginResultsType()!=null || pgi.getPginAssociatedResults()!=null || pgi.getPginBudget()>0)){
//				Mensaje.verDialogo("dlgEliminaLineaGenero");
//			}
//		}
//	}
//	
//	public void seleccionarLineaGeneroTema3(ProjectsGenderInfo pgi){
//		getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);
//		if(pgi.isSeleccionado()==false){
//			if(pgi.getCataId().getCataNumber()!=5 && (pgi.getPginResultsType()!=null || pgi.getPginAssociatedResults()!=null || pgi.getPginBudget()>0)){
//				Mensaje.verDialogo("dlgEliminaLineaGenero");
//			}
//		}
//	}
//	
//	public void cancelaEliminaLineaGeneroSeleccionada(){
//		getBuscaProyectosBean().getLineaGeneroSeleccionada().setSeleccionado(true);
//	}
	/**
	 * Permite eliminar la linea de genero seleccionada
	 */
	public void eliminaLineaGeneroSeleccionada(){
		try{
			List<ProjectGenderIndicator> listaIndicadores=new ArrayList<>();			
			listaIndicadores = getProjectGenderIndicatorFacade().listaPorProjectGender(getBuscaProyectosBean().getProjectGenderInfoSeleccionado().getPginId());
			for (ProjectGenderIndicator pgi : listaIndicadores) {
				pgi.setPgigStatus(false);
			}
			getBuscaProyectosBean().getProjectGenderInfoSeleccionado().setPginStatus(false);
			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(getBuscaProyectosBean().getProjectGenderInfoSeleccionado(), listaIndicadores);
			getBuscaProyectosBean().getListaAccionesdeGeneroProyecto().remove(getBuscaProyectosBean().getProjectGenderInfoSeleccionado());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.eliminar"));
			Mensaje.actualizarComponente(":form:growl");
		}catch(Exception e){
			e.printStackTrace();
		}
//		try{
//			
//			
//			List<GenderAdvances> listaTemp=getGenderAdvancesFacade().listaAvancesGeneroActivosPorProjectGender(getBuscaProyectosBean().getLineaGeneroSeleccionada().getPginId());
//			if(listaTemp!=null && listaTemp.size()>0){
//				getProjectsGenderInfoFacade().eliminaLineaDeGenero(listaTemp, getBuscaProyectosBean().getLineaGeneroSeleccionada(), getLoginBean().getUser().getUserName());
//				ProjectsGenderInfo projectGender = getBuscaProyectosBean().getLineaGeneroSeleccionada();
//				projectGender.setIndicators(null);
//				projectGender.setPginAnotherIndicator(null);
//				projectGender.setPginAssociatedResults(null);
//				projectGender.setPginBudget(0);
//				projectGender.setPginGoals(null);
//				projectGender.setPginGoalValue(null);
//				projectGender.setPginResultsType(null);
//				projectGender.setPginBaseLine(null);
//				projectGender.setPginOtherLine(null);
//				projectGender.setPginUpdateDate(new Date());
//				projectGender.setPginUpdateUser(getLoginBean().getUser().getUserName());				
//				projectGender.setIndicators(new Indicators());
//				getBuscaProyectosBean().setLineaGeneroSeleccionada(projectGender);
//			}else{
//				ProjectsGenderInfo projectGender = getBuscaProyectosBean().getLineaGeneroSeleccionada();
//				projectGender.setIndicators(null);
//				projectGender.setPginAnotherIndicator(null);
//				projectGender.setPginAssociatedResults(null);
//				projectGender.setPginBudget(0);
//				projectGender.setPginGoals(null);
//				projectGender.setPginGoalValue(null);
//				projectGender.setPginResultsType(null);
//				projectGender.setPginBaseLine(null);
//				projectGender.setPginOtherLine(null);
//				projectGender.setPginUpdateDate(new Date());
//				projectGender.setPginUpdateUser(getLoginBean().getUser().getUserName());
//				getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(projectGender);
//				projectGender.setIndicators(new Indicators());
//				getBuscaProyectosBean().setLineaGeneroSeleccionada(projectGender);
//			}
//				
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaLineaGeneroSeleccionada " + ": ").append(e.getMessage()));
//		}
	}
	
	public void eliminaIndicadorGenero(){
		try{
			getBuscaProyectosBean().getListadoProyectoGeneroIndicador().remove(getBuscaProyectosBean().getIndicadorSeleccionado());
			if(getBuscaProyectosBean().getIndicadorSeleccionado().getPgigId()>0){
				getBuscaProyectosBean().getIndicadorSeleccionado().setPgigStatus(false);
				getProjectGenderIndicatorFacade().agregarEditar(getBuscaProyectosBean().getIndicadorSeleccionado());
				cargarLineasGeneroAsignadas();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Arma las lineas de genero por tema
	 */
//	public void primerIngresoLineasDeGenero(int codigoProyecto, int codigoPartner){
//		try{
//			List<ProjectsGenderInfo> listaTemp=new ArrayList<>();
//			
//			List<Object[]> listaTempLineasGenero = getCatalogsFacade().buscaCatalogoLineasAccionGenero();
//			if(listaTempLineasGenero!=null && listaTempLineasGenero.size()>0){				
//				for(Object[] obj : listaTempLineasGenero) {
//					ProjectsGenderInfo pgi = new ProjectsGenderInfo();
//					Catalogs catalogo= new Catalogs();
//					catalogo.setCataId(Integer.parseInt(obj[0].toString()));
//					pgi.setCataId(catalogo);
//					pgi.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
//					if(codigoPartner>0)
//						pgi.setPspaId(codigoPartner);
//					pgi.setPginStatus(true);
//					pgi.setPginCreationDate(new Date());
//					pgi.setPginCreatorUser(getLoginBean().getUser().getUserName());
//					getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//				}
//			}
//			if(codigoPartner>0){
//				listaTemp = getProjectsGenderInfoFacade().listaLineasGeneroProyectoPartner(codigoProyecto, codigoPartner);
//				
//			}else{
//				listaTemp = getProjectsGenderInfoFacade().listaLineasGeneroProyectoPartner(codigoProyecto, 0);
//				
//			}
//			armarLineasGenero(listaTemp);
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "primerIngresoLineasDeGenero " + ": ").append(e.getMessage()));
//		}
//	}
	/**
	 * Arma las lineas de genero
	 * @param listaInfoGenero
	 */
	public void armarLineasGenero(List<ProjectsGenderInfo> listaInfoGenero){
//		getBuscaProyectosBean().setListaTemaGenero1(new ArrayList<>());
//		getBuscaProyectosBean().setListaTemaGenero2(new ArrayList<>());
//		getBuscaProyectosBean().setListaTemaGenero3(new ArrayList<>());
//		getBuscaProyectosBean().setListaTemaGeneroOtros(new ArrayList<>());
//		for (ProjectsGenderInfo pgi : listaInfoGenero) {			
//			if(pgi.getIndicators()== null)
//				pgi.setIndicators(new Indicators());
//			if(pgi.getCataId()!=null && pgi.getCataId().getCatalogsType().getCatyMnemonic().equals(TipoCatalogoEnum.LINEAGENERO1.getEtiqueta())){
//				if(pgi.getIndicators()!=null && pgi.getPginBudget()>0 && pgi.getPginAssociatedResults()!=null && pgi.getPginAssociatedResults().length()>0){
//					pgi.setSeleccionado(true);									
//					String[] listaTemp= pgi.getPginAssociatedResults().split(" ");
//					Integer[] listaNum = new Integer[listaTemp.length];
//					for(int x=0;x<listaTemp.length;x++){
//
//						listaNum[x]= Integer.valueOf(listaTemp[x]);						
//					}
//					pgi.setPginObjectivesSelectedAux(listaNum);
//				}
//				getBuscaProyectosBean().getListaTemaGenero1().add(pgi);
//			}else if(pgi.getCataId()!=null && pgi.getCataId().getCatalogsType().getCatyMnemonic().equals(TipoCatalogoEnum.LINEAGENERO2.getEtiqueta())){
//				if(pgi.getIndicators()!=null && pgi.getPginBudget()>0 && pgi.getPginAssociatedResults()!=null && pgi.getPginAssociatedResults().length()>0){
//					pgi.setSeleccionado(true);										
//					String[] listaTemp= pgi.getPginAssociatedResults().split(" ");
//					Integer[] listaNum = new Integer[listaTemp.length];
//					for(int x=0;x<listaTemp.length;x++)
//						listaNum[x]= Integer.valueOf(listaTemp[x]);											
//					pgi.setPginObjectivesSelectedAux(listaNum);
//
//				}
//				getBuscaProyectosBean().getListaTemaGenero2().add(pgi);
//			}else if(pgi.getCataId()!=null && pgi.getCataId().getCatalogsType().getCatyMnemonic().equals(TipoCatalogoEnum.LINEAGENERO3.getEtiqueta())){
//				if(pgi.getIndicators()!=null && pgi.getPginBudget()>0 && pgi.getPginAssociatedResults()!=null && pgi.getPginAssociatedResults().length()>0){
//					pgi.setSeleccionado(true);
//					String[] listaTemp= pgi.getPginAssociatedResults().split(" ");
//					Integer[] listaNum = new Integer[listaTemp.length];
//					for(int x=0;x<listaTemp.length;x++)
//						listaNum[x]= Integer.valueOf(listaTemp[x]);											
//					pgi.setPginObjectivesSelectedAux(listaNum);
//
//				}
//				getBuscaProyectosBean().getListaTemaGenero3().add(pgi);
//			}else if(pgi.getCataId()==null){ 
//				if(pgi.getPginAnotherIndicator()!=null && pgi.getPginAnotherIndicator().length()>0 && pgi.getPginBudget()>0 && pgi.getPginAssociatedResults()!=null && pgi.getPginAssociatedResults().length()>0){
//					pgi.setSeleccionado(true);
//					String[] listaTemp= pgi.getPginAssociatedResults().split(" ");
//					Integer[] listaNum = new Integer[listaTemp.length];
//					for(int x=0;x<listaTemp.length;x++)
//						listaNum[x]= Integer.valueOf(listaTemp[x]);											
//					pgi.setPginObjectivesSelectedAux(listaNum);
//
//				}
//				getBuscaProyectosBean().getListaTemaGeneroOtros().add(pgi);
//			}
//		}
	}
	/**
	 * Valida los datos seleccionados en las linea de genero
	 * @return
	 */
	public boolean validaSeleccionLineasAccion(){
		boolean respuesta=true;
//		for (ProjectsGenderInfo pgi : getBuscaProyectosBean().getListaTemaGenero1()) {			
//			if(pgi.isSeleccionado()){
//				StringBuffer objetivos=new StringBuffer();
//				for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//					String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//					objetivos.append(objeto).append(" ");
//				}
//				pgi.setPginAssociatedResults(objetivos.toString());
//				if(pgi.getCataId().getCataNumber()!=7 && (pgi.getIndicators().getIndiId() == null || pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || pgi.getPginAssociatedResults() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0))){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen1",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar").concat(pgi.getCataId().getCataText2()) );
//					respuesta=false;
//				}else if(pgi.getCataId().getCataNumber()==7 && (pgi.getPginOtherLine()==null || (pgi.getPginAnotherIndicator()!=null && pgi.getPginAnotherIndicator().length()==0) || pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0))){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen1",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar").concat(pgi.getCataId().getCataText2()) );
//					respuesta=false;					
//				}
//			}
//		}
//		for (ProjectsGenderInfo pgi : getBuscaProyectosBean().getListaTemaGenero2()) {
//			if(pgi.isSeleccionado()){
//				StringBuffer objetivos=new StringBuffer();
//				for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//					String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//					objetivos.append(objeto).append(" ");
//				}
//				pgi.setPginAssociatedResults(objetivos.toString());
//				if(pgi.getCataId().getCataNumber()!=4 && (pgi.getIndicators().getIndiId() == null || pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || pgi.getPginAssociatedResults() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0))){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen2",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar").concat(pgi.getCataId().getCataText2()) );
//					respuesta=false;
//				}else if(pgi.getCataId().getCataNumber()==4 && (pgi.getPginOtherLine()==null || (pgi.getPginAnotherIndicator()!=null && pgi.getPginAnotherIndicator().length()==0) || pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || pgi.getPginAssociatedResults() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0))){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen2",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar").concat(pgi.getCataId().getCataText2()) );
//					respuesta=false;					
//				}
//			}
//		}
//		for (ProjectsGenderInfo pgi : getBuscaProyectosBean().getListaTemaGenero3()) {
//			if(pgi.isSeleccionado()){
//				StringBuffer objetivos=new StringBuffer();
//				for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//					String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//					objetivos.append(objeto).append(" ");
//				}
//				pgi.setPginAssociatedResults(objetivos.toString());
//				if(pgi.getCataId().getCataNumber()!=5 && (pgi.getIndicators().getIndiId() == null || pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || pgi.getPginAssociatedResults() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0))){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen3",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar").concat(pgi.getCataId().getCataText2()) );
//					respuesta=false;
//				}else if(pgi.getCataId().getCataNumber()==5 && (pgi.getPginOtherLine()==null || (pgi.getPginAnotherIndicator()!=null && pgi.getPginAnotherIndicator().length()==0) || pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || pgi.getPginAssociatedResults() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0))){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen3",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar").concat(pgi.getCataId().getCataText2()) );
//					respuesta=false;					
//				}
//			}
//		}
//		for (ProjectsGenderInfo pgi : getBuscaProyectosBean().getListaTemaGeneroOtros()) {
//			if(pgi.isSeleccionado()){
//				StringBuffer objetivos=new StringBuffer();
//				for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//					String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//					objetivos.append(objeto).append(" ");
//				}
//				pgi.setPginAssociatedResults(objetivos.toString());
//				if(pgi.getPginBudget()==0 || pgi.getPginResultsType() == null || pgi.getPginAssociatedResults() == null || (pgi.getPginAssociatedResults() != null && pgi.getPginAssociatedResults().length()==0)){
//					Mensaje.verMensaje("form:buscaProyectos:tabsGenero:lstEnfGen4",FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.porFavorRevisar") );
//					respuesta=false;
//				}
//			}
//		}
		return respuesta;
	}
	/**
	 * Permite grabar las lineas de genero
	 */
	public void grabarLineasGenero(){
//		try{
//			if(validaSeleccionLineasAccion()){
//				for(ProjectsGenderInfo pgi: getBuscaProyectosBean().getListaTemaGenero1()){
//					if(pgi.isSeleccionado()){
//						StringBuilder objetivos=new StringBuilder();
//						if(pgi.getCataId().getCataNumber()==7)
//							pgi.setIndicators(null);
//						for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//							String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//							objetivos.append(objeto).append(" ");
//						}
//						pgi.setPginAssociatedResults(objetivos.toString());
//						pgi.setPginUpdateUser(getLoginBean().getUser().getUserName());
//						pgi.setPginUpdateDate(new Date());
//						getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//					}
//				}
//				for(ProjectsGenderInfo pgi: getBuscaProyectosBean().getListaTemaGenero2()){
//					if(pgi.isSeleccionado()){
//						StringBuilder objetivos=new StringBuilder();
//						if(pgi.getCataId().getCataNumber()==4)
//							pgi.setIndicators(null);
//						for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//							String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//							objetivos.append(objeto).append(" ");
//						}
//						pgi.setPginAssociatedResults(objetivos.toString());
//						
//						pgi.setPginUpdateUser(getLoginBean().getUser().getUserName());
//						pgi.setPginUpdateDate(new Date());
//						getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//					}
//				}
//				for(ProjectsGenderInfo pgi: getBuscaProyectosBean().getListaTemaGenero3()){
//					if(pgi.isSeleccionado()){
//						StringBuilder objetivos=new StringBuilder();
//						if(pgi.getCataId().getCataNumber()==5)
//							pgi.setIndicators(null);
//						for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//							String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//							objetivos.append(objeto).append(" ");
//						}
//						pgi.setPginAssociatedResults(objetivos.toString());
//
//						pgi.setPginUpdateUser(getLoginBean().getUser().getUserName());
//						pgi.setPginUpdateDate(new Date());
//						getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//					}
//				}
//				for(ProjectsGenderInfo pgi: getBuscaProyectosBean().getListaTemaGeneroOtros()){
//					if(pgi.isSeleccionado()){
//						StringBuilder objetivos=new StringBuilder();
//						for(int x=0;x<pgi.getPginObjectivesSelectedAux().length;x++){
//							String objeto=String.valueOf(pgi.getPginObjectivesSelectedAux()[x]);
//							objetivos.append(objeto).append(" ");
//						}
//						pgi.setPginAssociatedResults(objetivos.toString());
//
//						pgi.setPginUpdateUser(getLoginBean().getUser().getUserName());
//						pgi.setPginUpdateDate(new Date());
//						if(pgi.getCataId()== null )
//							pgi.setIndicators(null);
//						getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//					}
//				}
//				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.infoGrabada"));
//			}
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarLineasGenero " + ": ").append(e.getMessage()));
//		}
	}
	/**
	 * Permite eliminar otras lineas enfocadas en genero para la linea1
	 * @param pgi
	 */
	public void eliminaLineaEnfGenero1(ProjectsGenderInfo pgi){
//		try{
//			pgi.setIndicators(null);
//			pgi.setPginStatus(false);
//			getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);			
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGenero1().remove(pgi);
//			eliminaLineaGeneroSeleccionada();
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaLineaEnfGenero1 " + ": ").append(e.getMessage()));
//		}
	}
	/**
	 * Permite eliminar otras lineas enfocadas en genero para la linea1
	 * @param pgi
	 */
	public void eliminaLineaEnfGenero2(ProjectsGenderInfo pgi){
//		try{
//			pgi.setIndicators(null);
//			pgi.setPginStatus(false);
//			getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);
//			
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGenero2().remove(pgi);
//			eliminaLineaGeneroSeleccionada();
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaLineaEnfGenero2 " + ": ").append(e.getMessage()));
//		}
	}
	/**
	 * Permite eliminar otras lineas enfocadas en genero para la linea1
	 * @param pgi
	 */
	public void eliminaLineaEnfGenero3(ProjectsGenderInfo pgi){
//		try{
//			pgi.setIndicators(null);
//			pgi.setPginStatus(false);
//			getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);
//			
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGenero3().remove(pgi);
//			eliminaLineaGeneroSeleccionada();
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaLineaEnfGenero3 " + ": ").append(e.getMessage()));
//		}
	}
	/**
	 * Permite eliminar otras lineas enfocadas en genero para la linea1
	 * @param pgi
	 */
	public void eliminaLineaEnfGenero4(ProjectsGenderInfo pgi){
//		try{			
//			pgi.setIndicators(null);
//			pgi.setCataId(null);
//			pgi.setPginStatus(false);
//			getBuscaProyectosBean().setLineaGeneroSeleccionada(pgi);
//			
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGeneroOtros().remove(pgi);
//			eliminaLineaGeneroSeleccionada();
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaLineaEnfGenero4 " + ": ").append(e.getMessage()));
//		}
	}
	/**
	 * Agrega otra linea de genero al tema 1
	 */
//	public void agregaOtroLineaGenero1(){
//		try{
//			Catalogs catalogo;
//			ProjectsGenderInfo pgi=new ProjectsGenderInfo();
//			catalogo = getCatalogsFacade().catalogoOtrosDeLineaAccion(TipoCatalogoEnum.LINEAGENERO1.getEtiqueta(), 7);
//			pgi.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
//			pgi.setCataId(catalogo);
//			pgi.setPginStatus(true);
//			pgi.setPginCreationDate(new Date());
//			pgi.setPginCreatorUser(getLoginBean().getUser().getUserName());
//			pgi.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGenero1().add(pgi);
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaOtroLineaGenero1 " + ": ").append(e.getMessage()));
//		}
//	}
//	/**
//	 * Agrega otra linea de genero al tema 2
//	 */
//	public void agregaOtroLineaGenero2(){
//		try{
//			Catalogs catalogo;
//			ProjectsGenderInfo pgi=new ProjectsGenderInfo();
//			catalogo = getCatalogsFacade().catalogoOtrosDeLineaAccion(TipoCatalogoEnum.LINEAGENERO2.getEtiqueta(), 4);
//			pgi.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
//			pgi.setCataId(catalogo);
//			pgi.setPginStatus(true);
//			pgi.setPginCreationDate(new Date());
//			pgi.setPginCreatorUser(getLoginBean().getUser().getUserName());
//			pgi.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGenero2().add(pgi);
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaOtroLineaGenero2 " + ": ").append(e.getMessage()));
//		}
//	}
//	/**
//	 * Agrega otra linea de genero al tema 3
//	 */
//	public void agregaOtroLineaGenero3(){
//		try{
//			Catalogs catalogo;
//			ProjectsGenderInfo pgi=new ProjectsGenderInfo();
//			catalogo = getCatalogsFacade().catalogoOtrosDeLineaAccion(TipoCatalogoEnum.LINEAGENERO3.getEtiqueta(), 5);
//			pgi.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
//			pgi.setCataId(catalogo);
//			pgi.setPginStatus(true);
//			pgi.setPginCreationDate(new Date());
//			pgi.setPginCreatorUser(getLoginBean().getUser().getUserName());
//			pgi.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGenero3().add(pgi);
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaOtroLineaGenero3 " + ": ").append(e.getMessage()));
//		}
//	}
	/**
	 * Agrega otra linea de genero a otros temas
	 */
	public void agregaOtroLineaGenero4(){
//		try{			
//			ProjectsGenderInfo pgi=new ProjectsGenderInfo();			
//			pgi.setSeleccionado(true);
//			pgi.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
//			pgi.setCataId(null);
//			pgi.setIndicators(null);
//			pgi.setPginStatus(true);
//			pgi.setPginCreationDate(new Date());			
//			pgi.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
//			pgi.setPginCreatorUser(getLoginBean().getUser().getUserName());
//			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(pgi);
//			getBuscaProyectosBean().getListaTemaGeneroOtros().add(pgi);
//		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.grabar"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaOtroLineaGenero4 " + ": ").append(e.getMessage()));
//		}
	}
	
	/**
	 * Genera un nuevo seguimiento como socio implementador o administrador
	 * @param proyecto  Codigo del proyecto
	 */
	public void nuevoSeguimientoProyectoAdminSocioImpl(Projects proyecto){
		try{
			getBuscaProyectosBean().setAnioReporte(null);
			getBuscaProyectosBean().setPeriodoDesde(null);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setListaSectoresSeleccionados(null);
			getBuscaProyectosBean().setNuevoSeguimiento(true);
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));		
//			getBuscaProyectosBean().setCodigoComponente(0);
			getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
			
			Mensaje.verDialogo("dlgSeleccionSocios");			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//reemplaza a informacionAvanceEjecucion
	public void nuevaInformacionAvanceEjecucion(){
		try{
			codigoPartner = null;
			ProjectsStrategicPartners psp=new ProjectsStrategicPartners();
			AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
			avanceEjecucion.setAdexCreatorUser(getLoginBean().getUser().getUserName());
			avanceEjecucion.setAdexCreationDate(new Date());
			avanceEjecucion.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
			avanceEjecucion.setAdexIsReported(false);
			avanceEjecucion.setAdexRegisterDate(new Date());
			avanceEjecucion.setAdexReportedStatus("I");
			avanceEjecucion.setAdexStatus(true);
			avanceEjecucion.setUsers(getLoginBean().getUser());
			getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(getBuscaProyectosBean().getProyectoSeleccionado().getPartners().getPartId()) );
			if(getBuscaProyectosBean().getCodigoStrategicPartner()==null && getBuscaProyectosBean().getTipoSocio()==2){
				Mensaje.actualizarComponente(":form:growl");
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.seleccionSocio"));
			}else{
				if(esReporteSalvaguardas){
					cargaSectoresInteres();	

					avanceEjecucion.setAdexIsGender(false);
					if(getBuscaProyectosBean().getCodigoStrategicPartner()== null){					

						avanceEjecucion.setProjectsStrategicPartners(null);
					}else{			
						codigoPartner = getBuscaProyectosBean().getCodigoStrategicPartner();

						getBuscaProyectosBean().setCodigoStrategicPartner(codigoPartner);
						psp.setPspaId(codigoPartner);
						avanceEjecucion.setProjectsStrategicPartners(psp);
					}
					getBuscaProyectosBean().setDatosProyecto(true);

					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
					
//					organizaSalvaguardasEnComponentes(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(),getBuscaProyectosBean().getCodigoStrategicPartner());
					
					Mensaje.ocultarDialogo("dlgSeleccionSocios");
				}else{
					avanceEjecucion.setAdexIsGender(true);
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
//					getBuscaProyectosBean().setCodigoComponente(0);
					Mensaje.ocultarDialogo("dlgSeleccionSocios");
				}
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
				if(getBuscaProyectosBean().getAdvanceExecution()!=null && avanceEjecucion.getProjectsStrategicPartners()!=null)
					getBuscaProyectosBean().setNombreSocioEstrategico(getProjectsStrategicPartnersFacade().partnerEstrategico(avanceEjecucion.getProjectsStrategicPartners().getPspaId()).getPartners().getPartName());
				else
					getBuscaProyectosBean().setNombreSocioEstrategico("");
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void mostrarDialogoFinAsignaSalvaguardas(){
		Mensaje.verDialogo("dlgAsignaSalvaguardas");
	}
	/**
	 * Carga las lineas de accion por tema
	 */
	public void cargaLineasAccionPorTema(){
		try{
			if(getBuscaProyectosBean().getLineaGeneroSel()!=null)
				getBuscaProyectosBean().setListaLineasAccion(getCatalogsFacade().buscaCatalogosPorTipo(getBuscaProyectosBean().getLineaGeneroSel().getCatyId()));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Organiza las salvaguardas asignadas por componentes 
	 * @param codigoProyecto  Es el código del proyecto
	 * @param codigoSocioEstrategico  Es el código del socio estratégico
	 */
//	public void organizaSalvaguardasEnComponentes(Integer codigoProyecto,Integer codigoSocioEstrategico){
//		try{
//			Map<Integer,ProjectsSpecificObjectives> mapaTemp=new HashMap<Integer,ProjectsSpecificObjectives>();
//			List<ProjectsSpecificObjectives> listaComponentes=new ArrayList<>();
//			getBuscaProyectosBean().setListaComponentes(new ArrayList<>());
//			getBuscaProyectosBean().setListaComponentesSalvaguardas(new ArrayList<>());
//			List<Object[]> listaSalvaguardasComponentes = getProjectQuestionsFacade().listaSalvaguardasComponentes(codigoProyecto, codigoSocioEstrategico);			
//			for(Object object: listaSalvaguardasComponentes){
//				Object[] datos = (Object[]) object;
//				String[] vector = datos[1].toString().split(" ");	
//				int codigoSalvaguarda= Integer.parseInt(datos[0].toString());
//				for(int x=0;x<vector.length;x++){						
//					ProjectsSpecificObjectives pso=new ProjectsSpecificObjectives();					
//					pso.setPsobId(Integer.parseInt(vector[x]));
//					listaComponentes.add(pso);
//					getBuscaProyectosBean().getListaComponentesSalvaguardas().add(new DtoComponenteSalvaguarda(Integer.parseInt(vector[x]),codigoSalvaguarda));
//				}
//			}			
//			for(ProjectsSpecificObjectives pso:listaComponentes){
//				mapaTemp.put(pso.getPsobId(), pso);
//			}
//			for(Entry<Integer,ProjectsSpecificObjectives> pso:mapaTemp.entrySet()){
//				getBuscaProyectosBean().getListaComponentes().add(getProjectsSpecificObjectivesFacade().datosComponente(pso.getValue().getPsobId()));
//			}
//						
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	/**
	 * Carga las salvaguardas asignadas al componente seleccionado
	 */
//	public void salvaguardasAsignadasAlComponente(){
//		try{
//			getBuscaProyectosBean().setListadoSalvaguardasAsignadasProyecto(new ArrayList<>());
//			for(DtoComponenteSalvaguarda compsalv:getBuscaProyectosBean().getListaComponentesSalvaguardas()){
//				if(compsalv.getCodigoComponente()== getBuscaProyectosBean().getCodigoComponente()){					
//					getBuscaProyectosBean().getListadoSalvaguardasAsignadasProyecto().add(getSafeguardsFacade().obtieneSalvaguarda(compsalv.getCodigoSalvaguarda()));
//				}
//			}
//			getBuscaProyectosBean().setMostrarTabs(false);
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
	public void nuevaLineaAccion(){
		getBuscaProyectosBean().setListadoProyectoGeneroIndicador(new ArrayList<>());
		getBuscaProyectosBean().setProjectGenderInfoSeleccionado(new ProjectsGenderInfo());
		getBuscaProyectosBean().setLineaGeneroSel(new CatalogsType());
		getBuscaProyectosBean().setListaLineasAccion(new ArrayList<>());
		getBuscaProyectosBean().setListadoComponentesSeleccionados(new ArrayList<>());
		
	}
	public void agregaIndicadorGenero(){
		ProjectGenderIndicator pgi= new ProjectGenderIndicator();
		pgi.setProjectsGenderInfo(getBuscaProyectosBean().getProjectGenderInfoSeleccionado());
		getBuscaProyectosBean().getListadoProyectoGeneroIndicador().add(pgi);
		
	}

	public void grabarLineasAccion(){
		try{
			StringBuilder cmp = new StringBuilder();
			
			if(getBuscaProyectosBean().getListadoProyectoGeneroIndicador().size()>0){
				ProjectsGenderInfo projectGenderInfo = new ProjectsGenderInfo();
				projectGenderInfo = getBuscaProyectosBean().getProjectGenderInfoSeleccionado();
				projectGenderInfo.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
				projectGenderInfo.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
				Iterator it= getBuscaProyectosBean().getListadoComponentesSeleccionados().iterator();
				while(it.hasNext()){
					cmp.append(ubicaComponente(it.next().toString())).append(",");
				}
				for(ProjectGenderIndicator pgi:getBuscaProyectosBean().getListadoProyectoGeneroIndicador()){
					pgi.setPgigStatus(true);
					if(pgi.getIndicators().getIndiType().equals("B")){
						if(pgi.isPgigBooleanValue())
							pgi.setPgigGoalValueOne(1);
						else
							pgi.setPgigGoalValueOne(0);
					}
				}
				projectGenderInfo.setPginComponents(cmp.toString());
				projectGenderInfo.setCataId(getBuscaProyectosBean().getLineaAccionSel());
				projectGenderInfo.setPginStatus(true);
				if(projectGenderInfo.getPginId()==null){
					projectGenderInfo.setPginCreationDate(new Date());
					projectGenderInfo.setPginCreatorUser(getLoginBean().getUser().getUserName());				
				}else{
					projectGenderInfo.setPginUpdateDate(new Date());
					projectGenderInfo.setPginUpdateUser(getLoginBean().getUser().getUserName());
				}
				getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(projectGenderInfo, getBuscaProyectosBean().getListadoProyectoGeneroIndicador());
				cargarLineasGeneroAsignadas();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.infoGrabada"));
				Mensaje.ocultarDialogo("dlgAsignaLineaGenero");
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.indicador"));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void editarLineaAccion(ProjectsGenderInfo linea){
		try{			
			for (CatalogsType c : getBuscaProyectosBean().getListaLineasGenero()) {
				if(c.getCatyDescription().equals(linea.getCataId().getCatalogsType().getCatyDescription())){
					getBuscaProyectosBean().setLineaGeneroSel(c);
					break;
				}
			}
			cargaLineasAccionPorTema();
			for(Catalogs c:getBuscaProyectosBean().getListaLineasAccion()){
				if(c.getCataText2().equals(linea.getCataId().getCataText2())){
					getBuscaProyectosBean().setLineaAccionSel(c);
					break;
				}
			}
			getBuscaProyectosBean().setListadoComponentesSeleccionados(new ArrayList<>());
			String[] componentes = linea.getPginComponents().split(",");
			for(int i=0;i<componentes.length;i++){
				getBuscaProyectosBean().getListadoComponentesSeleccionados().add(ubicaComponentePorCodigo(Integer.valueOf(componentes[i])));
			}
			
			getBuscaProyectosBean().setListadoProyectoGeneroIndicador(linea.getProjectGenderIndicatorList());
			for (ProjectGenderIndicator pgin : getBuscaProyectosBean().getListadoProyectoGeneroIndicador()) {
				pgin.setIndicators(ubicaIndicador(pgin.getIndicators()));
				if(pgin.getIndicators().getIndiType().equals("B")){
					if(pgin.getPgigGoalValueOne().equals(1))
						pgin.setPgigBooleanValue(true);
					else
						pgin.setPgigBooleanValue(false);
				}
			}
			getBuscaProyectosBean().setProjectGenderInfoSeleccionado(linea);
			
			
			Mensaje.verDialogo("dlgAsignaLineaGenero");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public int ubicaComponente(String componente){
		int codigo=0;
		for (Components comp : getBuscaProyectosBean().getListadoComponentes()) {
			if(comp.getCompName().equals(componente)){
				codigo=comp.getCompId();
				break;
			}
		}
		return codigo;
	}
	public String ubicaComponentePorCodigo(int codigo){
		String componente="";
		for (Components comp : getBuscaProyectosBean().getListadoComponentes()) {
			if(comp.getCompId()== codigo){
				componente=comp.getCompName();
				break;
			}
		}
		return componente;
	}
	
	public Indicators ubicaIndicador(Indicators i){
		Indicators obj=null;
		for (Indicators indi : getBuscaProyectosBean().getListaIndicadores()) {
			if(indi.getIndiDescription().equals(i.getIndiDescription())){
				obj=indi;
				break;
			}
		}
		return obj;
	}
	public void mostrarDialogoEliminaLineaGenero(){
		
	}
	
}