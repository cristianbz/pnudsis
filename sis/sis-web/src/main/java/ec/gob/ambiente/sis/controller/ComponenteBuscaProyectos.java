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
import ec.gob.ambiente.sis.model.AdvanceExecutionProjectGender;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.Indicators;
import ec.gob.ambiente.sis.model.ProjectGenderIndicator;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.services.AdvanceExecutionProjectGenderFacade;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.CatalogsTypeFacade;
import ec.gob.ambiente.sis.services.IndicatorsFacade;
import ec.gob.ambiente.sis.services.ProjectGenderIndicatorFacade;
import ec.gob.ambiente.sis.services.ProjectQuestionsFacade;
import ec.gob.ambiente.sis.services.ProjectsGenderInfoFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import lombok.Getter;
import lombok.Setter;

@Dependent
public class ComponenteBuscaProyectos implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ComponenteBuscaProyectos.class);

	@Inject
	@Getter
	private BuscaProyectosBean buscaProyectosBean;

	@Getter    
	@Inject
	private MensajesController mensajesController;

	@EJB
	@Getter
	private PartnersFacade partnersFacade;
	
	
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
	private AdvanceExecutionProjectGenderFacade advanceExecutionProjectGenderFacade;

	@EJB
	@Getter
	private ProjectsFacade projectsFacade;


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
	
	@Setter
	@Getter
	private boolean esSeguimientoSalvaguardas;
	
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
			getBuscaProyectosBean().setLineaAccionSel(new Catalogs());
			getBuscaProyectosBean().setNuevaLineaAccion(false);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSociosImplementadores " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSectoresInteres " + ": ").append(e.getMessage()));
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
		getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
		getBuscaProyectosBean().setMostrarOpcionesBusqueda(false);
		getBuscaProyectosBean().setMostrarOpcionesBusquedaGenero(false);
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
			}else if(getBuscaProyectosBean().getTipoRol()==2){
				List<Projects> listaProyectousuarios=new ArrayList<>();
				getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
				if(getLoginBean().getListaProyectosDelSocioImplementador().size()>0){	
					listaProyectousuarios=getLoginBean().getListaProyectosDelSocioImplementador();
					for (Projects pu : listaProyectousuarios) {
						getBuscaProyectosBean().getListaProyectos().add(pu);
					}
				}else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.proyectosUsuario"));
				}
			}else if(getBuscaProyectosBean().getTipoRol()==3){
				List<ProjectsStrategicPartners> listaProyectousuarios=new ArrayList<>();
				getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
				if(getLoginBean().getListaProyectosDelSocioEstrategico().size()>0){	
					listaProyectousuarios=getLoginBean().getListaProyectosDelSocioEstrategico();
					for (ProjectsStrategicPartners pu : listaProyectousuarios) {
						getBuscaProyectosBean().getListaProyectos().add(pu.getProjects());
					}
				}else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.proyectosUsuario"));
				}
			}

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "buscarProyectos " + ": ").append(e.getMessage()));
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
				getBuscaProyectosBean().setDatosProyecto(true);
				if(avanceEjecucion.getProjectsStrategicPartners()!=null){
					getBuscaProyectosBean().setCodigoStrategicPartner(avanceEjecucion.getProjectsStrategicPartners().getPspaId());
					getBuscaProyectosBean().setNombreSocioEstrategico(getProjectsStrategicPartnersFacade().partnerEstrategico(getBuscaProyectosBean().getCodigoStrategicPartner()).getPartners().getPartName());
				}else{
					getBuscaProyectosBean().setCodigoStrategicPartner(null);
					getBuscaProyectosBean().setNombreSocioEstrategico("");
				}
				if (getBuscaProyectosBean().getAdvanceExecution()!=null ){					
					getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0, 4)));
					getBuscaProyectosBean().setPeriodoDesde("01");
				}
			}else{				
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);

				if(avanceEjecucion.getProjectsStrategicPartners()!=null){
					getBuscaProyectosBean().setNombreSocioEstrategico(getProjectsStrategicPartnersFacade().partnerEstrategico(getBuscaProyectosBean().getCodigoStrategicPartner()).getPartners().getPartName());
				}else{
					getBuscaProyectosBean().setNombreSocioEstrategico("");
				}
					cargaSectoresInteres();		
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0,4)));
					getBuscaProyectosBean().setPeriodoDesde("01");
					sectoresInteresProyecto();
					
			}
			getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "buscaAvanceEjecucionDelProyectoReportado " + ": ").append(e.getMessage()));
		}
	}

		
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
				for(ProjectsStrategicPartners prosp:getLoginBean().getListaProyectosDelSocioEstrategico()){
					if(proyecto.getProjId() == prosp.getProjects().getProjId()){
						getBuscaProyectosBean().setCodigoStrategicPartner(prosp.getPspaId());
						avanceEjecucion.setProjectsStrategicPartners(prosp);
						break;
					}
				}
				
			}
			
			if(!esReporteGenero){
				cargaSectoresInteres();
				avanceEjecucion.setAdexIsGender(false);
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "nuevoSeguimientoProyectoSocioEstrategico " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "buscaAsignacionSalvaguardas " + ": ").append(e.getMessage()));
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
			cargaListadoSalvaguardas();
			getBuscaProyectosBean().setTipoSocio(1);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setNombreSocioEstrategico("");
			esProyecto=true;
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaPartnersProyecto " + ": ").append(e.getMessage()));
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

				nuevoIngreso= false;
				for(ProjectQuestions preguntas:getBuscaProyectosBean().getListaPreguntasAsignadas()){
					getBuscaProyectosBean().getPreguntasSelecionadas().add(preguntas.getCatalogs().getCataId());
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preguntasAsignadas " + ": ").append(e.getMessage()));
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
		
	}
	
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaListadoSalvaguardas " + ": ").append(e.getMessage()));
		}
	}
	public void quitaSeleccionSalvaguardaSocioEstrategico(){
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
			
			getProjectQuestionsFacade().agregaPreguntasProyecto(getBuscaProyectosBean().getListaPreguntasAsignadas(),esProyecto,tipo,nuevoIngreso);
			getBuscaProyectosBean().setAsignacionSalvaguardas(false);
			volverABuscarProyectos();
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarAsignacionSalvaguardas " + ": ").append(e.getMessage()));			
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.grabar"));
		}
	}
		
	/**
	 * Setea el tipo de rol del usuario
	 */
	public void indicaTipoRol(){
		getBuscaProyectosBean().setTipoRol(getLoginBean().getTipoRol());
	}

	public void cargarAvanceEjecucionSalvaguarda(int codigoPartner,int codigoProyecto){
		try{

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

			}else{
				getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0,4)));
				getBuscaProyectosBean().setPeriodoDesde("01");
				sectoresInteresProyecto();

			}


		}catch(NoResultException e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		} catch (Exception e) {
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Cancela la seleccion del socio estrategico
	 */
	public void cancelaSeleccionSocio(){
		getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
		getBuscaProyectosBean().setCodigoStrategicPartner(null);
	}
	/**
	 * Carga proyectos reportados como socio estrategico
	 * @param proyecto
	 */
	public void cargaProyectosReportados(Projects proyecto){
		try{
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			if(getLoginBean().getTipoRol() == 3){
				for(ProjectsStrategicPartners psp:getLoginBean().getListaProyectosDelSocioEstrategico()){
					if(psp.getProjects().getProjId() == proyecto.getProjId()){
						getBuscaProyectosBean().setCodigoStrategicPartner(psp.getPspaId());
						break;
					}
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaProyectosReportados " + ": ").append(e.getMessage()));
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
					for(ProjectsStrategicPartners psp:getLoginBean().getListaProyectosDelSocioEstrategico()){
						if(psp.getProjects().getProjId() == getBuscaProyectosBean().getProyectoSeleccionado().getProjId()){
							getBuscaProyectosBean().setCodigoStrategicPartner(psp.getPspaId());														
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

					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
					periodoReporte="";
					setCodigoPartner(getBuscaProyectosBean().getCodigoStrategicPartner());
					getBuscaProyectosBean().setEstadoReporte("");
					getBuscaProyectosBean().setAnioReporte(0);
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProyectosCriterioBusqueda " + ": ").append(e.getMessage()));
		}
	}
	
	public void cargarProyectosGeneroCriterioBusqueda(){
		try{
			String periodoReporte = null;
			if(getBuscaProyectosBean().getTipoSocio()==2 && getBuscaProyectosBean().getCodigoStrategicPartner()==null){
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "" ,getMensajesController().getPropiedad("error.seleccionSocio") );
			}else{
				if (getLoginBean().getTipoRol()==3){
					for(ProjectsStrategicPartners psp:getLoginBean().getListaProyectosDelSocioEstrategico()){
						if(psp.getProjects().getProjId() == getBuscaProyectosBean().getProyectoSeleccionado().getProjId()){
							getBuscaProyectosBean().setCodigoStrategicPartner(psp.getPspaId());														
							break;
						}
					}
				}

				if(getBuscaProyectosBean().getAnioReporte() != null){
					periodoReporte = String.valueOf(getBuscaProyectosBean().getAnioReporte());
					periodoReporte = periodoReporte.concat("-").concat("01");
				}

				getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
				getBuscaProyectosBean().setListaProyectosReportados(getAdvanceExecutionSafeguardsFacade().listarProyGeneroReportadosConCriteriosBusqueda(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), getBuscaProyectosBean().getCodigoStrategicPartner(),  periodoReporte, getBuscaProyectosBean().getEstadoReporte()));
				if(getBuscaProyectosBean().getListaProyectosReportados().size()==0){
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "" ,getMensajesController().getPropiedad("info.noReportesGenerados") );
				}else{
					getBuscaProyectosBean().setMostrarOpcionesBusquedaGenero(false);

					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
					periodoReporte="";
					setCodigoPartner(getBuscaProyectosBean().getCodigoStrategicPartner());
					getBuscaProyectosBean().setEstadoReporte("");
					getBuscaProyectosBean().setAnioReporte(0);
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProyectosCriterioBusqueda " + ": ").append(e.getMessage()));
		}
	}
	
	/**
	 * Muestra el panel para busqueda de proyectos reportados
	 * @param proyecto  Proyecto seleccionado
	 */
	public void mostrarPanelBusquedaProyectos(Projects proyecto){
		try{
			if(!esReporteGenero){
				getBuscaProyectosBean().setTipoSocio(1);
				getBuscaProyectosBean().setNuevoSeguimiento(false);
				getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
				getBuscaProyectosBean().setMostrarOpcionesBusqueda(true);
				getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
				getBuscaProyectosBean().setListaPartnersProyectos(new ArrayList<>());		
				getBuscaProyectosBean().setCodigoStrategicPartner(null);
				getBuscaProyectosBean().setAnioReporte(null);
				getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));
			}else{
				if(getLoginBean().getTipoRol() == 3)
					cargaProyectosReportados(proyecto);
				else{
					getBuscaProyectosBean().setTipoSocio(1);
					getBuscaProyectosBean().setNuevoSeguimiento(false);
					getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
					getBuscaProyectosBean().setMostrarOpcionesBusquedaGenero(true);
					getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
					getBuscaProyectosBean().setListaPartnersProyectos(new ArrayList<>());		
					getBuscaProyectosBean().setCodigoStrategicPartner(null);
					getBuscaProyectosBean().setAnioReporte(null);
					getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "mostrarPanelBusquedaProyectos " + ": ").append(e.getMessage()));
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
			getBuscaProyectosBean().setTipoSocio(1);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setNombreSocioEstrategico("");
			getBuscaProyectosBean().setListadoComponentes(new ArrayList<>());
			getBuscaProyectosBean().setListadoComponentes(getComponentsFacade().listaComponentesActivos(getBuscaProyectosBean().getProyectoSeleccionado().getProjId()));
			Components componente = new Components();
    		componente.setCompId(1000);
    		componente.setCompStatus(true);
    		componente.setCompCode("CEO5");
    		componente.setCompName("Componentes Operativos");
    		getBuscaProyectosBean().getListadoComponentes().add(componente);
    		esProyecto=true;
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "asignacionLineasGenero " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,"" , getMensajesController().getPropiedad("info.sinGenero") );
		}		
	}
	public void quitaSeleccionLineasGeneroSocioEstrategico(){

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
		if(getBuscaProyectosBean().getTipoSocio()==2 && getBuscaProyectosBean().getCodigoStrategicPartner()!=null || getBuscaProyectosBean().getTipoSocio()==1){
			cargarLineasGeneroAsignadas();		
			getBuscaProyectosBean().setPosicionTabGenero(2);
		}else{
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "" ,getMensajesController().getPropiedad("error.seleccionSocio") );
		}
	}
	
	public void cargarLineasGeneroAsignadas(){
		try{
			getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(new ArrayList<>());
			if(getBuscaProyectosBean().getCodigoStrategicPartner()==null)
				getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(getProjectsGenderInfoFacade().listaLineasGeneroProyectoPartner(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), 0));
			else
				getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(getProjectsGenderInfoFacade().listaLineasGeneroProyectoPartner(getBuscaProyectosBean().getProyectoSeleccionado().getProjId(), getBuscaProyectosBean().getCodigoStrategicPartner()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarLineasGeneroAsignadas " + ": ").append(e.getMessage()));
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
	
	/**
	 * Permite eliminar la linea de genero seleccionada
	 */
	public void eliminaLineaGeneroSeleccionada(){
		try{
			
			List<AdvanceExecutionProjectGender> listaTemp= new ArrayList<>();
			List<ProjectGenderIndicator> listaIndicadores=new ArrayList<>();			
			listaIndicadores = getProjectGenderIndicatorFacade().listaPorProjectGender(getBuscaProyectosBean().getProjectGenderInfoSeleccionado().getPginId());
			for (ProjectGenderIndicator pgi : listaIndicadores) {
				pgi.setPgigStatus(false);
				listaTemp .addAll(getAdvanceExecutionProjectGenderFacade().ubicaIndicadoresActivosNoFinalizados(pgi.getPgigId()));
			}
			getBuscaProyectosBean().getProjectGenderInfoSeleccionado().setPginStatus(false);
			getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(getBuscaProyectosBean().getProjectGenderInfoSeleccionado(), listaIndicadores,listaTemp);
			getBuscaProyectosBean().getListaAccionesdeGeneroProyecto().remove(getBuscaProyectosBean().getProjectGenderInfoSeleccionado());
			
			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.eliminar"));
			Mensaje.actualizarComponente(":form:growl");
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaLineaGeneroSeleccionada " + ": ").append(e.getMessage()));
		}

	}
	
	public void eliminaIndicadorGenero(){
		try{
			getBuscaProyectosBean().getListadoProyectoGeneroIndicador().remove(getBuscaProyectosBean().getIndicadorSeleccionado());
			if(getBuscaProyectosBean().getIndicadorSeleccionado().getPgigId() != null){
				getBuscaProyectosBean().getIndicadorSeleccionado().setPgigStatus(false);
				getProjectGenderIndicatorFacade().agregarEditar(getBuscaProyectosBean().getIndicadorSeleccionado());
				cargarLineasGeneroAsignadas();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
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
			getBuscaProyectosBean().setListaProyectosReportados(new ArrayList<>());
			getBuscaProyectosBean().setCodigoBusquedaProyecto(1);
			getBuscaProyectosBean().setTipoSocio(1);
			
			Mensaje.verDialogo("dlgSeleccionSocios");			
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "nuevoSeguimientoProyectoAdminSocioImpl " + ": ").append(e.getMessage()));
		}
	}
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
//					if(getBuscaProyectosBean().getCodigoStrategicPartner()== null){					
//
//						avanceEjecucion.setProjectsStrategicPartners(null);
//					}else{			
//						codigoPartner = getBuscaProyectosBean().getCodigoStrategicPartner();
//
//						getBuscaProyectosBean().setCodigoStrategicPartner(codigoPartner);
//						psp.setPspaId(codigoPartner);
//						avanceEjecucion.setProjectsStrategicPartners(psp);
//					}
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
					Mensaje.ocultarDialogo("dlgSeleccionSocios");
				}else{
					avanceEjecucion.setAdexIsGender(true);
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
					Mensaje.ocultarDialogo("dlgSeleccionSocios");
				}
				
				if(getBuscaProyectosBean().getCodigoStrategicPartner()== null){					
					avanceEjecucion.setProjectsStrategicPartners(null);
				}else{			
					codigoPartner = getBuscaProyectosBean().getCodigoStrategicPartner();
					getBuscaProyectosBean().setCodigoStrategicPartner(codigoPartner);
					psp.setPspaId(codigoPartner);
					avanceEjecucion.setProjectsStrategicPartners(psp);
				}

				
				
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
				if(getBuscaProyectosBean().getAdvanceExecution()!=null && avanceEjecucion.getProjectsStrategicPartners()!=null)
					getBuscaProyectosBean().setNombreSocioEstrategico(getProjectsStrategicPartnersFacade().partnerEstrategico(avanceEjecucion.getProjectsStrategicPartners().getPspaId()).getPartners().getPartName());
				else
					getBuscaProyectosBean().setNombreSocioEstrategico("");
			}

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "nuevaInformacionAvanceEjecucion " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaLineasAccionPorTema " + ": ").append(e.getMessage()));
		}
	}

	public void nuevaLineaAccion(){
		getBuscaProyectosBean().setListadoProyectoGeneroIndicador(new ArrayList<>());
		getBuscaProyectosBean().setProjectGenderInfoSeleccionado(new ProjectsGenderInfo());
		getBuscaProyectosBean().setLineaGeneroSel(new CatalogsType());
		getBuscaProyectosBean().setListaLineasAccion(new ArrayList<>());
		getBuscaProyectosBean().setListadoComponentesSeleccionados(new ArrayList<>());
		getBuscaProyectosBean().setNuevaLineaAccion(true);
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
					getProjectsGenderInfoFacade().agregarEditarProjectGenerInfo(projectGenderInfo, getBuscaProyectosBean().getListadoProyectoGeneroIndicador(),null);
					cargarLineasGeneroAsignadas();
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.infoGrabada"));
					Mensaje.ocultarDialogo("dlgAsignaLineaGenero");
					getBuscaProyectosBean().setNuevaLineaAccion(false);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.indicador"));
					
				}
		
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarLineasAccion " + ": ").append(e.getMessage()));
		}	
	}
	
	public void validatablaIndicadores(){
//		cargarLineasGeneroAsignadas();
		if(getBuscaProyectosBean().getProjectGenderInfoSeleccionado().getProjectGenderIndicatorList() !=null){
			Iterator itera= getBuscaProyectosBean().getProjectGenderInfoSeleccionado().getProjectGenderIndicatorList().iterator();
			while (itera.hasNext()){
				ProjectGenderIndicator pgi= (ProjectGenderIndicator)itera.next();
				if(pgi.getIndicators()==null)
					itera.remove();
			}
		}
		Mensaje.ocultarDialogo("dlgAsignaLineaGenero");
	}
	
	public void editarLineaAccion(ProjectsGenderInfo linea){
		try{
			getBuscaProyectosBean().setNuevaLineaAccion(true);
			getBuscaProyectosBean().setProjectGenderInfoSeleccionado(linea);
			for (CatalogsType c : getBuscaProyectosBean().getListaLineasGenero()) {
				if(c.getCatyDescription().equals(linea.getCataId().getCatalogsType().getCatyDescription())){
					getBuscaProyectosBean().setLineaGeneroSel(c);
//					System.out.println(c.getCatyDescription());
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
			Mensaje.verDialogo("dlgAsignaLineaGenero");
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "editarLineaAccion " + ": ").append(e.getMessage()));
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
		try{
			
			for (Indicators indi : getBuscaProyectosBean().getListaIndicadores()) {
				if(indi.getIndiDescription().equals(i.getIndiDescription())){
					obj=indi;
					break;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	public void finAsignacionGenero(){
		getBuscaProyectosBean().setListadoComponentesSeleccionados(new ArrayList<>());
		getBuscaProyectosBean().setProjectGenderInfoSeleccionado(new ProjectsGenderInfo());
		getBuscaProyectosBean().setListadoProyectoGeneroIndicador(new ArrayList<>());
		getBuscaProyectosBean().setListaAccionesdeGeneroProyecto(new ArrayList<>());
		getBuscaProyectosBean().setAsignacionGenero(false);
		getBuscaProyectosBean().setDatosProyecto(false);
		getBuscaProyectosBean().setPosicionTabGenero(0);
	}
	
	public void mostrarDialogoActivarReporte(){
		Mensaje.verDialogo("dlgVolverActivarReporte");
	}
	public void volverActivarReporte(){
		try{
			getBuscaProyectosBean().getAdvanceExecution().setAdexIsReported(false);
			getBuscaProyectosBean().getAdvanceExecution().setAdexReportedStatus("I");
			getBuscaProyectosBean().getAdvanceExecution().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getBuscaProyectosBean().getAdvanceExecution().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().edit(getBuscaProyectosBean().getAdvanceExecution());
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "volverActivarReporte " + ": ").append(e.getMessage()));
		}
	}
}