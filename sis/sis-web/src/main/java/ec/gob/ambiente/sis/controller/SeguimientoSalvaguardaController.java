package ec.gob.ambiente.sis.controller;

import java.io.File;
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
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sigma.model.ProjectsSpecificObjectives;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsSafeguardsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.ProjectUsers;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.ExecutiveSummariesFacade;
import ec.gob.ambiente.sis.services.ProjectQuestionsFacade;
import ec.gob.ambiente.sis.services.ProjectUsersFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.OperacionesCatalogo;
import ec.gob.ambiente.sis.utils.ResumenPDF;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoAccesoRecursoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoAreaConsolidadaEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoConformacionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoEstadoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoInstitucionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoNivelInvolucramientoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoParticipanteEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRespuestaEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoResultadoAcuerdoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoSalvaguardaEnum;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import ec.gob.ambiente.suia.model.Users;
import ec.gob.ambiente.suia.service.GeographicalLocationsFacade;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class SeguimientoSalvaguardaController  implements Serializable{


	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SeguimientoSalvaguardaController.class);
	private static final int CODIGO_IDENTIFICACION_INDIGENA = 54;
	
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
	
    @Getter
    @Setter
    @Inject
	private SeguimientoSalvaguardaBean seguimientoSalvaguardaBean;
    
    @Getter
    @Setter
    @Inject
    private LoginBean loginBean;
    
    @Getter
    @Setter
    @Inject
    private MensajesController mensajesController;
	
    @EJB
	@Getter
	private QuestionsFacade questionsFacade;
    
    @EJB
   	@Getter
   	private GeographicalLocationsFacade geographicalLocationsFacade;
    
    @EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;
    
    @EJB
	@Getter
	private ExecutiveSummariesFacade executiveSummarieFacade;
    
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@EJB
	@Getter
	private PartnersFacade partnersFacade;
	
	@EJB
	@Getter
	private SectorsFacade sectorsFacade;
	
	@EJB
	@Getter
	private ProjectsSafeguardsFacade projectsSafeguardsFacade; 
	
	@EJB
	@Getter
	private AdvanceExecutionSafeguardsFacade advanceExecutionSafeguardsFacade;
	
	@EJB
	@Getter
	private ValueAnswersFacade valueAnswersFacade;
	
	@EJB
	@Getter
	private CatalogsFacade catalogsFacade;
	
	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;
	
	@EJB
	@Getter
	private ProjectUsersFacade projectUsersFacade;
	
	@EJB
	@Getter
	private ProjectQuestionsFacade projectQuestionsFacade;
	
	@Getter
	@Setter
	private int numeroSalvaguardas;
	
	private int nuevaActividad;
	private int nuevaModalidadActividad;
	private int nuevoRiesgo;
	@Getter
	@Setter
	private int codigoSalvaguardaActual;

	private String rutaPDF;
	
	
	private Users usuario;
	
	@PostConstruct
	public void init(){
		try{
			getComponenteBuscarProyectos().setEsReporteGenero(false);
			getComponenteBuscarProyectos().setEsReporteSalvaguardas(true);
			codigoSalvaguardaActual=0;
			numeroSalvaguardas=0;
			getSeguimientoSalvaguardaBean().setTabActual(0);
			vaciarValores();			
//			cargarProyectos();
			cargaSalvaguardasPlanAccion();
//			cargaSectoresInteres();
			usuario=getLoginBean().getUser();
//			usuario.setUserName("Christian Báez");
//			usuario.setUserId(0);
			getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
			getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
			cargaProvincias();
			cargaTodosLosCantones();
			cargaListadoTipoInvolucramiento();
			cargaListadoInstitucionAcompania();
			cargaSociosImplementadores();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Pone en estado false los tabs de las salvaguardas
	 */
	public void vaciarValores(){
		getSeguimientoSalvaguardaBean().setSalvaguardaA(false);
		getSeguimientoSalvaguardaBean().setSalvaguardaB(false);
		getSeguimientoSalvaguardaBean().setSalvaguardaC(false);
		getSeguimientoSalvaguardaBean().setSalvaguardaD(false);
		getSeguimientoSalvaguardaBean().setSalvaguardaE(false);
		getSeguimientoSalvaguardaBean().setSalvaguardaF(false);
		getSeguimientoSalvaguardaBean().setSalvaguardaG(false);
		getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(true);
		////cambio
		getSeguimientoSalvaguardaBean().setListaValoresRespuestas(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(new ArrayList<>());
	}
	/**
	 * Carga los proyectos, programas o pdi
	 */
	public void cargarProyectos(){
		try{
			
			getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<Projects>());
			getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().buscarTodosLosProyectos());
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.cargarProyectos"), "");			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProyectos " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Busca el socio implementador del proyecto
	 */
	public void seleccionaProyecto(){
		try{
		if(getSeguimientoSalvaguardaBean().getListaProyectos()!=null){			
			for (Projects proyecto : getSeguimientoSalvaguardaBean().getListaProyectos()) {
				if(proyecto.getProjId()==getSeguimientoSalvaguardaBean().getCodigoProyecto()){	
//					getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
					cargaSalvaguardasProyecto();
//					cargarAvanceEjecucionSalvaguarda();
					cargaValoresRespuestaPorSalvaguarda();
					getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
//					informacionFormularioPorSalvaguarda(getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto());
					getSeguimientoSalvaguardaBean().setTabActual(0);
					getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
					
					break;
				}
			}				
						
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaSalvaguardasDelProyectoSeleccionado(Projects proyecto){
		try{
			getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
			AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
			avanceEjecucion = getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(proyecto.getProjId());
			getSeguimientoSalvaguardaBean().setAnioReporte(null);
			getSeguimientoSalvaguardaBean().setPeriodoDesde(null);
			if((avanceEjecucion!=null && !avanceEjecucion.isAdexIsReported()) || avanceEjecucion==null){
				getSeguimientoSalvaguardaBean().setProyectoSeleccionado(proyecto);
				getSeguimientoSalvaguardaBean().setCodigoProyecto(proyecto.getProjId());
//				getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
				cargaSalvaguardasProyecto();
//				cargarAvanceEjecucionSalvaguarda();
				cargaValoresRespuestaPorSalvaguarda();
				getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
	//			informacionFormularioPorSalvaguarda(getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto());
				getSeguimientoSalvaguardaBean().setTabActual(0);
				getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.proyectoReportado"), "");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
//	/**
//	 * Carga los sectores de interes del proyecto seleccionado
//	 */
//	public void sectoresInteresProyecto(){
//		
//		getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
//		for(AdvanceSectors as:getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdvanceSectorsList()){			
////			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().add(String.valueOf(as.getSectors().getSectId()));
//			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().add(as.getSectors().getSectId());
//		}		
//	}
//	/**
//	 * Carga los sectores de interes
//	 */
//	public void cargaSectoresInteres(){
//		try{
//			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(new ArrayList<Sectors>());
//			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(getSectorsFacade().buscarTodosLosSectores());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	/**
	 * Carga las salvaguardas del proyecto
	 */
	public void cargaSalvaguardasProyecto(){
		try{
			vaciarValores();
			getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(new ArrayList<ProjectsSafeguards>());
			List<Object[]> listaTemporal= getProjectsSafeguardsFacade().buscaProyectoPorRiesgoCobeneficio(getSeguimientoSalvaguardaBean().getCodigoProyecto());
			for (Object[] objects : listaTemporal) {
				ProjectsSafeguards ps= new ProjectsSafeguards();
				Safeguards safeguard=new Safeguards();
				
				ps.setPrsaId(0);
				safeguard.setSafeId(Integer.valueOf(objects[0].toString()));
				safeguard.setSafeOrder(Integer.valueOf(objects[1].toString()));
				safeguard.setSafeLevel(Integer.valueOf(objects[2].toString()));
				safeguard.setSafeCode(objects[3].toString());
				ps.setProjectsCobenefits(null);
				ps.setProjectsRisks(null);
				ps.setSafeguards(safeguard);
				getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto().add(ps);
				
			}
			if(getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion()==null || getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error.cargarProyectos"), "");
				
			}else{
				getSeguimientoSalvaguardaBean().setDatosProyecto(true);
				numeroSalvaguardas=getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto().size();
				for(ProjectsSafeguards salvaguarda:getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()){
					if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaA(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==2 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaB(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==3 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaC(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==4 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaD(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==5 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaE(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==6 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaF(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==7 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaG(true);
					}
				}

			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Carga las salvaguardas del plan de accion
	 */
	public void cargaSalvaguardasPlanAccion(){
		try{
			getSeguimientoSalvaguardaBean().setListaSalvaguardasplanAccion(new ArrayList<Safeguards>());
			getSeguimientoSalvaguardaBean().setListaSalvaguardasplanAccion(getSafeguardsFacade().buscarTodosLosProyectos());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Carga el avance de ejecucion de las salvaguardas
	 */
	public void cargarAvanceEjecucionSalvaguarda(int codigoPartner,int codigoProyecto){
		try{
			
			getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());
			if(codigoPartner==0)
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(codigoProyecto));
			else
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getAdvanceExecutionSafeguardsFacade().buscarPorStrategicPartner(codigoPartner,codigoProyecto));
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()==null){
				AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
				avanceEjecucion.setAdexIsReported(false);
				avanceEjecucion.setAdexRegisterDate(new Date());
				avanceEjecucion.setAdexCreationDate(new Date());
				if(getSeguimientoSalvaguardaBean().getAnioReporte()!=null && getSeguimientoSalvaguardaBean().getPeriodoDesde()!=null){
					avanceEjecucion.setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
					avanceEjecucion.setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));
				}
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(avanceEjecucion);
				getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuCreationDate(new Date());
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuStatus(true);
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				
			}else{
				getSeguimientoSalvaguardaBean().setAnioReporte(Integer.valueOf(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexTermFrom().substring(0,4)));
				getSeguimientoSalvaguardaBean().setPeriodoDesde("01");
//				sectoresInteresProyecto();
				getSeguimientoSalvaguardaBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getSeguimientoSalvaguardaBean().getResumenEjecutivo()==null){
					getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuCreationDate(new Date());
					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuStatus(true);
					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				}
				
			}
			
			
		}catch(NoResultException e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		} catch (Exception e) {
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Ubica una salvaguarda del plan de accion por codigo de salvaguarda
	 * @param codigoSalvaguarda
	 * @return
	 */
	public Safeguards localizaSalvaguarda(int codigoSalvaguarda){
		Safeguards salvaguarda=new Safeguards();
		if(getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion()!=null && getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion().size()>0){
			for (Safeguards safeguard : getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion()) {
				if(safeguard.getSafeId()==codigoSalvaguarda){
					salvaguarda=safeguard;
					break;
				}
			}
		}
		return salvaguarda;
	}

	
	/**
	 * Carga valores respuestas por salvaguardas del proyecto
	 */
	public void cargaValoresRespuestaPorSalvaguarda(){
		List<Integer> lista=new ArrayList<>();

//		for (ProjectQuestions pq : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
//			lista.add(pq.getSafeguards().getSafeId());
//		}
		for(Safeguards salvaguarda: getComponenteBuscarProyectos().getBuscaProyectosBean().getListadoSalvaguardasAsignadasProyecto()){
			lista.add(salvaguarda.getSafeId());
		}
		
		lista= lista.stream().sorted((sv1,sv2)->sv1.compareTo(sv2)).collect(Collectors.toList());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestas(new ArrayList<>());
		List<ValueAnswers> listaTempValoresRespuesta= new ArrayList<>();
		Safeguards salvaguarda;
		try{
			getSeguimientoSalvaguardaBean().setListaPreguntas(new ArrayList<>());
			if(lista.size()>0){
//				getSeguimientoSalvaguardaBean().setListaPreguntas(getQuestionsFacade().buscarPreguntasPorSalvaguardas(lista));  //OJO REVISAR ESTA LINEA DE CODIGO
			
			for (Integer codigoSalvaguarda : lista) {
				salvaguarda = localizaSalvaguarda(codigoSalvaguarda);
				//salvaguarda A
				if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1){
					int salvaguardaA=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaA(salvaguardaA);
					getSeguimientoSalvaguardaBean().setListaPreguntasA(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaA));
					getSeguimientoSalvaguardaBean().setCatalogoPoliticas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.POLITICAS.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICOINTERNACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoMarcoJuridicoNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoNormativaSecundariaNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}						
					}
					preparaInformacionSalvaguardaA();
					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()==null  && getSeguimientoSalvaguardaBean().getTabActual()==0)
						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream()).collect(Collectors.toList());
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
					int salvaguardaB=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaB(salvaguardaB);
					getSeguimientoSalvaguardaBean().setListaPreguntasB(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaB));
					getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.INFORMEEJECUCION.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoInformacionComunica(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.INFORMECOMUNICACION.getCodigo()));
					cargaListadoInformaComunica();
					cargaListadoInformaEjecucion();
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}						
					}
					preparaInformacionSalvaguardaB();
					listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream()).collect(Collectors.toList());
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
					int salvaguardaC=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaC(salvaguardaC);
//					cargaTodosLosCantones();
					getSeguimientoSalvaguardaBean().setListaPreguntasC(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasC(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaC));
//					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}
//						else{
//							agregaNuevosValoresRespuestas(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().filter((p)->p.getCatalogs().getCataId()==1 || p.getCatalogs().getCataId()==2 || p.getCatalogs().getCataId()==3).collect(Collectors.toList()),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
//						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}						
					}
					preparaInformacionSalvaguardaC();
					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards() != null && getSeguimientoSalvaguardaBean().getTabActual()==0)
						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream()).collect(Collectors.toList());
					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
					int salvaguardaD=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaD(salvaguardaD);
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasD(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaD));
//					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasD().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasD().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}						
					}
					preparaInformacionSalvaguardaD();
					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards() != null && getSeguimientoSalvaguardaBean().getTabActual()==0)
						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream()).collect(Collectors.toList());
					//SALVAGUARDA E
				}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
					int salvaguardaE=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaE(salvaguardaE);
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasE(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasE(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaE));
//					getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaE).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntasE().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaE).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntasE().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}						
					}
					preparaInformacionSalvaguardaE();
					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!= null && getSeguimientoSalvaguardaBean().getTabActual()==0)
						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream()).collect(Collectors.toList());
					//SALVAGUARDA F
				}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
					int salvaguardaF=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaF(salvaguardaF);
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasF(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaF));
					getSeguimientoSalvaguardaBean().setListaCatalogoMedidaTomada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MEDIDATOMADA.getCodigo()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaF).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntasF().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaF).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntasF().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}						
					}
					preparaInformacionSalvaguardaF();
					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!= null && getSeguimientoSalvaguardaBean().getTabActual()==0)
						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream()).collect(Collectors.toList());
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
					int salvaguardaG=salvaguarda.getSafeId();
//					getSeguimientoSalvaguardaBean().setCodigoSalvaguardaG(salvaguardaG);
					getSeguimientoSalvaguardaBean().setListaPreguntasG(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaG));
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoRemoto(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOREMOTO.getCodigo()));
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoInSitu(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
						}
//						else{
//							agregaNuevosValoresRespuestas(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().filter((p)->p.getCatalogs().getCataId()==1 || p.getCatalogs().getCataId()==2 || p.getCatalogs().getCataId()==3).collect(Collectors.toList()),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
//						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
						}						
					}
					preparaInformacionSalvaguardaG();
					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!= null && getSeguimientoSalvaguardaBean().getTabActual()==0)
						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream()).collect(Collectors.toList());
				}
				
			}
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards() != null && getSeguimientoSalvaguardaBean().getTabActual()==0){				
				getSeguimientoSalvaguardaBean().setListaValoresRespuestas(listaTempValoresRespuesta);
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestas());
			}
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.proyectoSinSalvaguardas"), "");
				getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(true);
				getSeguimientoSalvaguardaBean().setDatosProyecto(false);
			}
		}catch(Exception e){
			e.printStackTrace();
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.respuestasPreguntas"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaValoresRespuestaPorSalvaguarda " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Inicializa respuestas por defecto a las preguntas
	 * @param listaPreguntas
	 * @param listaRespuestas
	 */
	public void valoresRespuestasPorDefecto(List<Questions> listaPreguntas, List<ValueAnswers> listaRespuestas){
		for (Questions  preguntas : listaPreguntas) {
			if(preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.CHECKBOX.getCodigo()) || preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.RADIOBUTTON.getCodigo())
					|| preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.TEXTO.getCodigo())){
				ValueAnswers valoresRespuestas=new ValueAnswers();
				valoresRespuestas.setQuestions(preguntas);
				valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				valoresRespuestas.setVaanStatus(true);
				valoresRespuestas.setVaanNumericAnswerValue(0);
				valoresRespuestas.setVaanTextAnswerValue("");
				valoresRespuestas.setVaanYesnoAnswerValue(false);
				listaRespuestas.add(valoresRespuestas);
			}
		}
	}
	
	public void agregaNuevosValoresRespuestas(List<Questions> listaPreguntas, List<ValueAnswers> listaRespuestas){
		try{

			boolean encontrado=false;
			for (Questions preguntas : listaPreguntas) {
				encontrado=false;
				for (ValueAnswers  respuesta: listaRespuestas) {
					if(respuesta.getQuestions().getQuesId().compareTo(preguntas.getQuesId())==0){
						encontrado=true;
						break;
					}
				}
				if (encontrado==false){

					ValueAnswers valoresRespuestas=new ValueAnswers();
					valoresRespuestas.setQuestions(preguntas);
					valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					valoresRespuestas.setVaanStatus(true);
					valoresRespuestas.setVaanNumericAnswerValue(0);
					valoresRespuestas.setVaanTextAnswerValue("");
					valoresRespuestas.setVaanYesnoAnswerValue(false);
					getValueAnswersFacade().create(valoresRespuestas);
				}
				
			}
		}catch(Exception e){
			
		}
	}

		
	/**
	 * Muestra el cuadro de dialogo para grabar el avnace de ejecucion
	 */
	public void mostrarDialogoGrabarSalvaguarda(int codigoSalvaguarda){
		
		if(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual()==1){			
			
			if(codigoSalvaguarda==1){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda)){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaA();
						grabarResumenEjecutivo();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.tablasSalvaguardaA"), "");
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB()){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaB();
						grabarResumenEjecutivo();
						for(int i=0;i<10;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, false);
						}
						renderizaMensajeRegistrosTablaB();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC()){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaC();
						grabarResumenEjecutivo();
						for(int i=0;i<8;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(0, false);
						}
						renderizaMensajeRegistrosTablaC();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				
			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD()){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaD();
						grabarResumenEjecutivo();
						for(int i=0;i<2;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(0, false);
						}
						renderizaMensajeRegistrosTablaD();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE()){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaE();
						grabarResumenEjecutivo();
						for(int i=0;i<6;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(0, false);
						}
						renderizaMensajeRegistrosTablaE();
					}else{
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
					}
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF()){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaF();
						grabarResumenEjecutivo();
						for(int i=0;i<5;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(0, false);
						}
						renderizaMensajeRegistrosTablaF();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG()){
					if (validaIngresoResumenEjecutivo()){
						grabarSalvaguardaG();
						grabarResumenEjecutivo();
						for(int i=0;i<6;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(0, false);
						}
						renderizaMensajeRegistrosTablaG();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),  "" );
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
			}
		}else{
		
			if(codigoSalvaguarda==1){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda))				
						Mensaje.verDialogo("dlgGrabaReporteA");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.tablasSalvaguardaA"), "");
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() ){
					if(validaDatosOpcionSiSalvaguardaB()){
						Mensaje.verDialogo("dlgGrabaReporteB");
						for(int i=0;i<10;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, false);
						}
						renderizaMensajeRegistrosTablaB();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				}
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaC()){
						Mensaje.verDialogo("dlgGrabaReporteC");
						for(int i=0;i<8;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(0, false);
						}
						renderizaMensajeRegistrosTablaC();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				}
			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion()){
					if (validaDatosOpcionSiSalvaguardaD()){
						Mensaje.verDialogo("dlgGrabaReporteD");
						for(int i=0;i<2;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(0, false);
						}
						renderizaMensajeRegistrosTablaD();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				}
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaE()){
						Mensaje.verDialogo("dlgGrabaReporteE");
						for(int i=0;i<6;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(0, false);
						}
						renderizaMensajeRegistrosTablaE();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				}
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaF()){
						Mensaje.verDialogo("dlgGrabaReporteF");
						for(int i=0;i<5;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(0, false);
						}
						renderizaMensajeRegistrosTablaF();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				}
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaG()){
						Mensaje.verDialogo("dlgGrabaReporteG");
						for(int i=0;i<6;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(0, false);
						}
						renderizaMensajeRegistrosTablaG();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
				}
			}
		}
	}
	
	
	/**
	 * Agrega una fila a primera tabla de la salvaguarda A
	 */
	public void agregaFilaTablaSalvaguardaA(){
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{

			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(buscaCatalogoPlanGobierno(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnNumberOne()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareColumnDecimalOne(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnDecimalOne());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(buscaCatalogoPlanGobierno(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnNumberOne()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasA(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaA1(false);
		}
	}
	/**
	 * Graba la salvaguarda A
	 */
	public void grabarSalvaguardaA(){
		try{
			
//				datosParaGrabarAvanceEjecucion();
				organizaSectoresSeleccionados();
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(new ArrayList<>());
				List<TableResponses> respuestasTabla= new ArrayList<>();
				List<TableResponses> datosRespuestasTabla=Stream.concat(preparaGrabarLeyesSalvaguardaA().stream(), preparaGrabarPoliticasSalvaguardaA().stream()).collect(Collectors.toList());
				respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA().stream(), datosRespuestasTabla.stream()).collect(Collectors.toList());
				
				for (ValueAnswers valores : getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()) {
					valores.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				}
				
				if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)					
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
				
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().stream().forEach(datos->{
					datos.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));
					datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().add(datos);
				});
				List<TableResponses> datosTablaConcatenados= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().stream()).collect(Collectors.toList());
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(datosTablaConcatenados);
				
				controlaCambiosFechaPeriodoReportar();
				getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),ubicaSalvaguarda(TipoSalvaguardaEnum.SALVAGUARDAA.getCodigo()));
				recargaPreguntasRespuestasSalvaguardas();
				preparaInformacionSalvaguardaA();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				if(validaGrabarTabs(1)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
					getSeguimientoSalvaguardaBean().getMapaTabs().put(1,1);
				}
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaA " + ": ").append(e.getMessage()));			
		}
	}
	
	public void controlaCambiosFechaPeriodoReportar(){
		String desde="";
		String hasta="";
		getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
		getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());

		desde = String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde());
		hasta = String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12");
		
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexTermFrom()!=null){
			if(!getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexTermFrom().equals(desde)){
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(desde);
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(hasta);
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(usuario.getUserName());
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
				
			}
		}else{
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(desde);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(hasta);
		}
	}
	
	/**
	 * Configura los datos de avanceEjecucion y avanceSectores para grabar/actualizar
	 */
	public void datosParaGrabarAvanceEjecucion(){
		Projects proyecto=new Projects();
		proyecto.setProjId(getSeguimientoSalvaguardaBean().getCodigoProyecto());
//		Users usuario=new Users();
//		usuario.setUserId(0);
//		usuario.setUserName("Christian Baez");
		List<AdvanceSectors> listaAvanceSectores=new ArrayList<>();
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjects(proyecto);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setUsers(usuario);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreatorUser(usuario.getUserName());
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()== null){
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreationDate(new Date());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexRegisterDate(new Date());
		}
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexStatus(true);

//		Iterator itera=getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().iterator();
//		while (itera.hasNext()){
//			Sectors sector=new Sectors();
//			sector.setSectId(Integer.valueOf(itera.next().toString()));
//			AdvanceSectors avanceSectores=new AdvanceSectors();
//			avanceSectores.setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			avanceSectores.setSectors(sector);
//			avanceSectores.setAdseSelectedSector(true);
//			listaAvanceSectores.add(avanceSectores);
//		}
//
//		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdvanceSectorsList(listaAvanceSectores);
	}

	public List<TableResponses> preparaGrabarLeyesSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera =getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnNumberOne(Integer.valueOf(itera.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		Iterator iteraMJN =getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().iterator();
		while(iteraMJN.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnNumberOne(Integer.valueOf(iteraMJN.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		Iterator iteraNSN =getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().iterator();
		while(iteraNSN.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnNumberOne(Integer.valueOf(iteraNSN.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		return respuestasTabla;
	}
	public List<TableResponses> preparaGrabarPoliticasSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnNumberOne(Integer.valueOf(itera.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}

		return respuestasTabla;
	}


	/**
	 * Prepara la informacion para mostrar en el formulario de acuerdo a las salvaguardas del proyecto
	 * @param salvaguardasProyecto
	 */
	public void informacionFormularioPorSalvaguarda(List<ProjectsSafeguards> salvaguardasProyecto){
		for (ProjectsSafeguards salvaguarda : salvaguardasProyecto) {
			if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaA();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==2 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaB();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==3 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaC();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==4 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaD();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==5 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaE();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==6 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaF();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==7 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaG();
			}
			
		}
	}
	
	/**
	 * Prepara la informacion a mostrar en el formulario de la salvaguarda A
	 */
	public void preparaInformacionSalvaguardaA(){
		List<TableResponses> listaLeyes=new ArrayList<>();
		List<TableResponses> listaPoliticas=new ArrayList<>();
		getSeguimientoSalvaguardaBean().setCatalogoPoliticasSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCatalogoLeyesSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCatalogoMarcoJuridicoNacionalSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCatalogoNormativaSecundariaNacionalSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaA(new ArrayList<>());
		try{
		cargaCatalogoPlanGobierno();
		
		List<TableResponses> listaSalvaguardaA=new ArrayList<>();		
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
			listaSalvaguardaA = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 1);
		}
		listaLeyes=listaSalvaguardaA.stream().filter((sc)->sc.getQuestions().getQuesId().equals(2)).collect(Collectors.toList());
		listaPoliticas=listaSalvaguardaA.stream().filter((sc)->sc.getQuestions().getQuesId().equals(4)).collect(Collectors.toList());
		listaLeyes.stream().forEach(res->{						
			if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoLeyes(), res.getTareColumnNumberOne()))
				getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(res.getTareColumnNumberOne());
			else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), res.getTareColumnNumberOne()))
				getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().add(res.getTareColumnNumberOne());
			else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), res.getTareColumnNumberOne()))
				getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().add(res.getTareColumnNumberOne());
			
		});
		
		listaPoliticas.stream().forEach(res->{			
			getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().add(res.getTareColumnNumberOne());			
		});
		List<TableResponses> tablaAux=new ArrayList<>();
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
			tablaAux=getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		}
		tablaAux.stream().forEach(res->{
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(res);
		});	
				
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().forEach(res->{
			res.setTareCatPlanGobierno(buscaCatalogoPlanGobierno(res.getTareColumnNumberOne()));			
		});
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPA(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		}
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPA(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void nuevoNSN(){
		getSeguimientoSalvaguardaBean().setNuevaNormativaSN(new Catalogs());		
		getSeguimientoSalvaguardaBean().setNuevoIngresoNSN(true);
	}
	
	public void nuevoMJN(){
		getSeguimientoSalvaguardaBean().setNuevoMJN(new Catalogs());		
		getSeguimientoSalvaguardaBean().setNuevoIngresoMJN(true);
	}
	
	public void nuevaLeySA(){
		getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());		
		getSeguimientoSalvaguardaBean().setNuevaLey(true);
	}
	public void noGrabarNuevaLeySA(){
		getSeguimientoSalvaguardaBean().setNuevaLey(false);
		getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
	}
	public void noGrabarNuevMJN(){
		getSeguimientoSalvaguardaBean().setNuevoIngresoMJN(false);
		getSeguimientoSalvaguardaBean().setNuevoMJN(new Catalogs());
	}
	
	public void noGrabarNuevNSN(){
		getSeguimientoSalvaguardaBean().setNuevoIngresoNSN(false);
		getSeguimientoSalvaguardaBean().setNuevaNormativaSN(new Catalogs());
	}
	
	public void noGrabarNuevaPoliticaSA(){
		getSeguimientoSalvaguardaBean().setNuevaPolitica(false);
		getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
	}
	public void nuevaPoliticaSA(){
		getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
		getSeguimientoSalvaguardaBean().setNuevaPolitica(true);
	}
	
	public void nuevaEjecucionSB(){
		getSeguimientoSalvaguardaBean().setNuevaEjecucionSB(new Catalogs());
	}
	public void nuevaComunicacionSB(){
		getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
	}
	public void grabarNuevaLeyPoliticaSA(int categoria){
		CatalogsType tipo =new CatalogsType();
		
		Catalogs catalogo=new Catalogs();
		catalogo.setCataCreatorUser(usuario.getUserName());
		
		catalogo.setCataStatus(true);
		catalogo.setCataCreationDate(new Date());
		if (categoria==1){
			if(getSeguimientoSalvaguardaBean().getNuevaLeySA().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosLey"),"" );
			}else{
				tipo.setCatyId(TipoCatalogoEnum.MARCOJURIDICOINTERNACIONAL.getCodigo());
				catalogo.setCatalogsType(tipo);
				getSeguimientoSalvaguardaBean().setCatalogoLeyes(getSeguimientoSalvaguardaBean().getCatalogoLeyes().stream().sorted((cl1,cl2)->cl1.getCataOrder().compareTo(cl2.getCataOrder())).collect(Collectors.toList()));
				if(getSeguimientoSalvaguardaBean().getCatalogoLeyes().size()==0)
					catalogo.setCataOrder(1);
				else
					catalogo.setCataOrder(getSeguimientoSalvaguardaBean().getCatalogoLeyes().get(getSeguimientoSalvaguardaBean().getCatalogoLeyes().size()-1).getCataOrder()+1);
				catalogo.setCataNumber(catalogo.getCataOrder());
				
				catalogo.setCataText1(getSeguimientoSalvaguardaBean().getNuevaLeySA().getCataText1());
	
				catalogo.setCataText2(getSeguimientoSalvaguardaBean().getNuevaLeySA().getCataText1());
				
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoLeyes().add(catalogo);
				getSeguimientoSalvaguardaBean().setNuevaLey(false);
			}
		}else if(categoria==2){
			if(getSeguimientoSalvaguardaBean().getNuevoMJN().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosLey"), "");
			}else{
				catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), getSeguimientoSalvaguardaBean().getNuevoMJN(), usuario.getUserName());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional().add(catalogo);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setNuevoMJN(new Catalogs());
				getSeguimientoSalvaguardaBean().setNuevoIngresoMJN(false);
			}
		}else if(categoria==3){
			if(getSeguimientoSalvaguardaBean().getNuevaNormativaSN().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosLey"), "");
			}else{
				catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), getSeguimientoSalvaguardaBean().getNuevaNormativaSN(), usuario.getUserName());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional().add(catalogo);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setNuevaNormativaSN(new Catalogs());
				getSeguimientoSalvaguardaBean().setNuevoIngresoNSN(false);
			}
		}else if(categoria==4){
			if(getSeguimientoSalvaguardaBean().getNuevaPoliticaSA().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosPolitica"), "");
			}else{
				tipo.setCatyId(TipoCatalogoEnum.POLITICAS.getCodigo());
				catalogo.setCatalogsType(tipo);
				getSeguimientoSalvaguardaBean().setCatalogoPoliticas(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().stream().sorted((cl1,cl2)->cl1.getCataOrder().compareTo(cl2.getCataOrder())).collect(Collectors.toList()));
				if(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().size()==0)
					catalogo.setCataOrder(1);
				else
					catalogo.setCataOrder(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().get(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().size()-1).getCataOrder()+1);
				
				catalogo.setCataNumber(catalogo.getCataOrder());
				
				catalogo.setCataText1(getSeguimientoSalvaguardaBean().getNuevaPoliticaSA().getCataText1());
				catalogo.setCataText2(getSeguimientoSalvaguardaBean().getNuevaPoliticaSA().getCataText1());
				
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoPoliticas().add(catalogo);
				getSeguimientoSalvaguardaBean().setNuevaPolitica(false);
			}
		}
	}
	
	public void cargaCatalogoPlanGobierno(){
		try{
			getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PLANPROYECTO.getCodigo()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cargaCatalogoModalidad(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoModalidad(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoModalidad(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MODALIDAD.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoModalidades(new ArrayList<>());
			for (Catalogs catalog : getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()) {
				getSeguimientoSalvaguardaBean().getListadoModalidades().add(catalog.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaCatalogoHerramientas(){
		try{
			getSeguimientoSalvaguardaBean().setListaHerramientas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.HERRAMIENTA.getCodigo()));
			getSeguimientoSalvaguardaBean().setListaHerramientas(getSeguimientoSalvaguardaBean().getListaHerramientas().stream().sorted((h1,h2)->h1.getCataText1().compareTo(h2.getCataText1())).collect(Collectors.toList()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaCatalogoActividad(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoActividad(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoActividad(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ACTIVIDAD.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoActividades(new ArrayList<>());
			for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getListaCatalogoActividad()) {
				getSeguimientoSalvaguardaBean().getListadoActividades().add(catalogo.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaCatalogoMonitoreoRemoto(){
		try{
			getSeguimientoSalvaguardaBean().setListadoMonitoreoRemoto(new ArrayList<>());
			for (Catalogs catalogo : getAplicacionBean().getListaMonitoreoRemoto()) {
				getSeguimientoSalvaguardaBean().getListadoMonitoreoRemoto().add(catalogo.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaCatalogoMonitoreoinSitu(){
		try{
			getSeguimientoSalvaguardaBean().setListadoMonitoreoInSitu(new ArrayList<>());
			for (Catalogs catalogo : getAplicacionBean().getListaMonitoreoInSitu()) {
				getSeguimientoSalvaguardaBean().getListadoMonitoreoInSitu().add(catalogo.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaCatalogoPeriodicidad(){
		try{
			getSeguimientoSalvaguardaBean().setListadoPeriodicidad(new ArrayList<>());
			for (Catalogs catalogo : getAplicacionBean().getListaPeriodicidad()) {
				getSeguimientoSalvaguardaBean().getListadoPeriodicidad().add(catalogo.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargarCatalogoServicio(){
		try{
			getSeguimientoSalvaguardaBean().setListaServicio(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaServicio(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.SERVICIO.getCodigo()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void cargaCatalogoTipoDeAcceso(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoTipoAcceso(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoTipoAcceso(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.TIPODEACCESO.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoActividades(new ArrayList<>());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/***
	 * Carga el nivel de involucramiento
	 */	
	public void cargaListadoTipoInvolucramiento(){
		getSeguimientoSalvaguardaBean().setListadoNivelInvolucramiento(new ArrayList<>());
		for (TipoNivelInvolucramientoEnum involucramiento : getSeguimientoSalvaguardaBean().getTipoNivelInvolucramientoEnum()) {
			getSeguimientoSalvaguardaBean().getListadoNivelInvolucramiento().add(involucramiento.getEtiqueta().toString());			
		}
	}
	/**
	 * Carga el listado de la institucion que acompaña
	 */
	public void cargaListadoInstitucionAcompania(){
		getSeguimientoSalvaguardaBean().setListadoinstitucionAcompania(new ArrayList<>());
		for (TipoInstitucionEnum institucion : getSeguimientoSalvaguardaBean().getTipoInstitucionEnum()) {
			getSeguimientoSalvaguardaBean().getListadoinstitucionAcompania().add(institucion.getEtiqueta().toString());
		}
	}
	
	public void cargaSociosImplementadores(){
		try{
			getSeguimientoSalvaguardaBean().setListaSociosImplementadores(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaSociosImplementadores(getPartnersFacade().listarSociosImplementadores());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaCatalogoAccionImplementada(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoAccionImplementada(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoAccionImplementada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ACCIONESIMPLEMENTADAS.getCodigo()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean validaDatosAvanceEjecucion(){
//		if(getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().size()==0){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosAvanceEjecucion"), "");			
//			return false;
//		}else{
//			return true;
//		}
		return true;
	}
	
	public boolean validaCamposTablasSalvaguardas(int salvaguarda){
		boolean retorno=true;
		if (salvaguarda==1){
			if(getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().size()==0 && getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().size()==0
					&& getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().size()==0 && getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().size()==0)
				retorno = false;
			else if((getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().size()==0 && 
					getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().size()==0 && 
					getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().size()==0) && getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().size()>0)
				retorno = false;
			else if((getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().size()==0 && 
					getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().size()==0 && 
					getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().size()==0) || getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().size()==0)
				retorno = false;
		}
		return retorno;
	}
	
	public void eliminaRegistroTablaSalvaguarda(int tablaRespuestas){
		List<TableResponses> tablaEvaluar=new ArrayList<>();
		switch(tablaRespuestas){
			case 1:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaA();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaA");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaA(tablaEvaluar);
				break;
			case 2:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB41");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(tablaEvaluar);
				break;	
			case 3:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB51");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(tablaEvaluar);
				break;	
			case 4:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB71");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(tablaEvaluar);
				break;	
			case 5:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB81");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(tablaEvaluar);
				break;	
			case 6:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB102");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(tablaEvaluar);
				break;	
			case 7:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB103");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(tablaEvaluar);
				break;	
			case 8:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB121");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(tablaEvaluar);
				break;	
			case 9:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaB131");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(tablaEvaluar);
				break;	
			case 10:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC201");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(tablaEvaluar);
				break;	
			case 11:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC211");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(tablaEvaluar);
				break;	
			case 12:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC241");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(tablaEvaluar);
				break;	
			case 13:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC26");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(tablaEvaluar);
				break;	
			case 14:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC271");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(tablaEvaluar);
				break;	
			case 15:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC28");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(tablaEvaluar);
				break;	
			case 16:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC291");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(tablaEvaluar);
				break;	
			case 17:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC293");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(tablaEvaluar);
				break;	
			case 18:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC301");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(tablaEvaluar);
				break;
			case 19:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaC311");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(tablaEvaluar);
				break;
			case 20:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaD321");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(tablaEvaluar);
				break;
			case 21:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaD331");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(tablaEvaluar);
				break;
			case 22:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaE341");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(tablaEvaluar);
				break;	
			case 23:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaE351");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(tablaEvaluar);
				break;	
			case 24:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaE361");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(tablaEvaluar);
				break;
			case 25:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaE371");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(tablaEvaluar);
				break;
			case 26:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaE381");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(tablaEvaluar);
				break;
			case 27:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaE391");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(tablaEvaluar);
				break;
			case 28:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaF411");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(tablaEvaluar);
				break;	
			case 29:
				tablaEvaluar = getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421();
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
					tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				}else{
					Mensaje.verDialogo("dlgEliminaItemTablaF421");
				}
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(tablaEvaluar);
				break;	
			case 30:				
				Mensaje.verDialogo("dlgEliminaItemTablaF431");				
				break;
			case 31:				
				Mensaje.verDialogo("dlgEliminaItemTablaF441");				
				break;
			case 32:				
				Mensaje.verDialogo("dlgEliminaItemTablaF452");				
				break;	
			case 33:				
				Mensaje.verDialogo("dlgEliminaItemTablaG461");				
				break;
			case 34:				
				Mensaje.verDialogo("dlgEliminaItemTablaG471");				
				break;	
			case 35:				
				Mensaje.verDialogo("dlgEliminaItemTablaG472");				
				break;	
			case 36:				
				Mensaje.verDialogo("dlgEliminaItemTablaG481");				
				break;
			case 37:				
				Mensaje.verDialogo("dlgEliminaItemTablaG491");				
				break;
			case 38:				
				Mensaje.verDialogo("dlgEliminaItemTablaG501");				
				break;	
			case 39:				
				Mensaje.verDialogo("dlgEliminaItemTablaG512");				
				break;	
			case 40:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPA");				
				break;	
			case 41:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPB");				
				break;
			case 42:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPC");				
				break;	
			case 43:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPD");				
				break;	
			case 44:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPE");				
				break;	
			case 45:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPF");				
				break;	
			case 46:				
				Mensaje.verDialogo("dlgEliminaItemTablaOPG");				
				break;
			case 47:
				Mensaje.verDialogo("dlgEliminaItemTablaB61");
				break;
			case 48:
				Mensaje.verDialogo("dlgEliminaItemTablaB62");	
				break;
			case 49:
				Mensaje.verDialogo("dlgEliminaItemTablaB9");
				break;
			case 50:
				Mensaje.verDialogo("dlgEliminaItemTablaB11");
			case 51:
				Mensaje.verDialogo("dlgEliminaItemTablaB143");
				break;
			case 52:
				Mensaje.verDialogo("dlgEliminaItemTablaC242");	
				break;
		}
	}
	
	public void eliminaRegistroTabla(int tablaRespuesta){
		try{
			switch(tablaRespuesta){
				case 1:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 2:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 3:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 4:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 5:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 6:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 7:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 8:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 9:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 10:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 11:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 12:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 13:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 14:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 15:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 16:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 17:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 18:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 19:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 20:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 21:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 22:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 23:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 24:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;	
				case 25:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 26:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 27:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 28:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 29:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;
				case 30:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 31:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 32:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 33:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 34:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 35:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 36:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 37:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 38:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 39:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 40:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 41:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPB().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 42:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPC().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 43:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPD().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 44:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPE().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 45:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPF().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 46:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPG().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 47:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 48:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB62().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 49:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;
				case 50:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;	
				case 51:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
					break;	
				case 52:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());					
					break;		
			}
			getTableResponsesFacade().eliminarRespuestasTabla(getSeguimientoSalvaguardaBean().getRegistroTabla());			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void vaciarPoliticasLeyes(int tipo){
		if(tipo==1){
			if(!getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().get(0).isVaanYesnoAnswerValue())
				getSeguimientoSalvaguardaBean().setCatalogoLeyesSeleccionado(new ArrayList<>());
		}else{
			if(!getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().get(1).isVaanYesnoAnswerValue())
				getSeguimientoSalvaguardaBean().setCatalogoPoliticasSeleccionado(new ArrayList<>());
		}
	}

	public void cargarProvincias(){
		try{
			List<Object[]> provincias=getGeographicalLocationsFacade().listarProvincias();
			getSeguimientoSalvaguardaBean().setListaProvincias(new ArrayList<>());
			for (Object[] registro : provincias) {
				GeographicalLocations catalogo=new GeographicalLocations();
				catalogo.setGeloName(registro[0].toString());
				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
				catalogo.setGeloCodificationInec(registro[2].toString());
				getSeguimientoSalvaguardaBean().getListaProvincias().add(catalogo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cancelaIngresoTablaB143(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB143(new TableResponses());
	}
	public void cancelaIngresoTablaB131(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB131(new TableResponses());
	}
	public void cancelaIngresoTablaB11(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB11(new TableResponses());
	}
	public void agregarFilasTablaSalvaguardaB41(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoModalidad());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberTwo(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberThree(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberOne(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive(), getAplicacionBean().getListaAutoIdentificacion()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberSix()));
					}
					
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareStatus(true);					
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0));
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberSix()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberSix(0);
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB41(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB41(false); 
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB41 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaB51(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB51(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB51 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
			
	public void agregarFilasTablaSalvaguardaB61(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(5));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB61(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB61(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB61 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}

	public void agregarFilasTablaSalvaguardaB71(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setTareStatus(true);
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8));
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB71(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB71(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaB81(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoActividad());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoTres(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFive()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());

				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoTres(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFive()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberFive(0);
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoTres("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB81(false);
				getSeguimientoSalvaguardaBean().setCodigoActividad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB81 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaB9(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(11));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFive()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9());
				}else{					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFive()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnNumberFive(0);
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB9(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB9(false);

				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB9 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}

	
	public void agregarFilasTablaSalvaguardaB102(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() , getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoEstado());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoTres(ubicaDatosEstado(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberSix()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareId()==null){
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					}
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(13));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());

				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					}				
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoTres(ubicaDatosEstado(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberSix()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB102(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB102 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaB11(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(16));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB11(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB11(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB11 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaB143(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnTwo(String.join(",", getSeguimientoSalvaguardaBean().getInformaComunicaSeleccionadas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnTree(String.join(",", getSeguimientoSalvaguardaBean().getInformaEjecucionSeleccionadas()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(22));	
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnFour(String.join(",", getSeguimientoSalvaguardaBean().getInformaComunicaSeleccionadas()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnFive(String.join(",", getSeguimientoSalvaguardaBean().getInformaEjecucionSeleccionadas()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB143(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB143(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB143 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void inicializaNuevoRegistroTablas(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
	}
	
	public void nuevoRegistroTablaA1(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasA(new TableResponses());
	}

	public void agregarFilasTablaSalvaguardaB121(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberOne()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(ubicaConformacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberThree(), getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(18));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareStatus(true);
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberTwo(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}
					
					
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());		
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberTwo(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberTwo(0);
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro("");
					}
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(ubicaConformacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFive())));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTwo()), getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB121(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(0);
				getSeguimientoSalvaguardaBean().setCodigoAccionImplementada(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB121 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaB131(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(20));		
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnOne(String.join(",", getSeguimientoSalvaguardaBean().getModalidadesSeleccionadas()));

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnOne(String.join(",", getSeguimientoSalvaguardaBean().getModalidadesSeleccionadas()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB131(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB131(false);
				getSeguimientoSalvaguardaBean().setCodigoActividad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB131 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaC201(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));					
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1));
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFive()));
					}
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberEight(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso());
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberEight(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso() ));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
//					
//					
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFive()));
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos("");
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberEight(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberEight(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso() ));
					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC201(false);
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC201 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaC211(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3));
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC211(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC211(false);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);

				vaciarDatosProvinciaCantonParroquia();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC211 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaC241(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(5));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC241(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC241(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC241 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaC242(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(6));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC242(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC242(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC242 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaC26(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoLineaAccion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareColumnNumberOne(), getAplicacionBean().getListaLineaAccion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(8));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoLineaAccion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareColumnOne()), getAplicacionBean().getListaLineaAccion()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC26(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC26(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC26 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaC271(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoLineaAccion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenerico( OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberTwo(),getAplicacionBean().getListaAutoIdentificacion()));				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoTres( OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberOne(),getAplicacionBean().getListaLineaAccion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberTwo() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberThree()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberTwo() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberThree()));
					}else{

						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC271(false);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC271 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaC28(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberOne(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(11));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberTwo()));
					}	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne()),getAplicacionBean().getListaAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberTwo()));
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnTwo("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC28(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC28(false);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC28 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaC291(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());

				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13));
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoTres(ubicaResultadoAcuerdo(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));


					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));
//
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos("");
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSix("");
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoTres(ubicaResultadoAcuerdo(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC291(false);
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoResultadoAcuerdo(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC291 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	
	public void agregarFilasTablaSalvaguardaC301(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				}else{					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC301(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC301(false);
				vaciarDatosProvinciaCantonParroquia();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaC311(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());

				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
				}else{			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenericoDos("");
						
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC311(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				vaciarDatosProvinciaCantonParroquia();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaD321(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareId()==null){
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD321(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD321 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPA(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(5));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPA());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPA(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPA(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPA " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPB(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPB().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPB(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPB(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPB " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPC(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(18));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPC().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPC(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPC(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPC " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPD(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(4));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPD().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPD());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPD(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPD(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPD " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	public void agregarFilasTablaSalvaguardaOPE(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(52));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPE().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPE(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPE(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	public void agregarFilasTablaSalvaguardaOPF(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(10));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPF().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPF(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPF(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	public void agregarFilasTablaSalvaguardaOPG(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(15));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPG().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPG(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPG(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	/**
	 * Permite agregar nueva fila a la tabla B102
	 */
	public void nuevaFilaTablaSalvaguardaB102(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
		
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnSix("0");

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoEstado(0);
	}
	
	/**
	 * Permite agregar nueva fila a la tabla B81
	 */
	public void nuevaFilaTablaSalvaguardaB81(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAccion(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaB9(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB9(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		
	}
	public void nuevaFilaTablaSalvaguardaB103(){
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnOne("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnTwo("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnTree("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnFour("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnFive("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnSix("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnSeven("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnEight("0");
	}
	public void nuevaFilaTablaSalvaguardaB121(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnSix("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnSeven("0");
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaB131(){
		getSeguimientoSalvaguardaBean().setModalidadesSeleccionadas(null);
	}
	/**
	 * Permite agregar nueva fila a la tabla B41
	 */
	public void nuevaFilaTablaSalvaguardaB41(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB41(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoModalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaB51(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51(new TableResponses());
	}
	public void nuevaFilaTablaSalvaguardaB143(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB143(new TableResponses());
		getSeguimientoSalvaguardaBean().setInformaComunicaSeleccionadas(null);
		getSeguimientoSalvaguardaBean().setInformaEjecucionSeleccionadas(null);
	}
	public void nuevaFilaTablaSalvaguardaB61(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB61(new TableResponses());
	}
	public void nuevaFilaTablaSalvaguardaB62(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB62(new TableResponses());
	}
	
	public void nuevaFilaTablaSalvaguardaC211(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC211(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	
	public void nuevaFilaTablaSalvaguardaC201(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());

		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaC241(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC241(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaC242(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC242(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaC26(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC26(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoLineaAccion(0);
		
	}
	public void nuevaFilaTablaSalvaguardaC271(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		
	}
	public void nuevaFilaTablaSalvaguardaC28(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC28(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);		
	}
	public void nuevaFilaTablaSalvaguardaC291(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());
		
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	public void nuevaFilaTablaSalvaguardaC293(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC293(new TableResponses());		
		getSeguimientoSalvaguardaBean().setCodCanton(0);				
	}
	public void nuevaFilaTablaSalvaguardaC301(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC301(new TableResponses());		
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);	
	}
	
	public void nuevaFilaTablaSalvaguardaC3111(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());
		
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	
	/**
	 * Permite agregar nueva fila a la tabla D331
	 */
	public void nuevaFilaTablaSalvaguardaD321(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	
	public void nuevaFilaTablaSalvaguardaD331(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD331(new TableResponses());
		
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoInvolucramiento(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setNivelInvolucramientoSeleccionados(null);
	}
	
	public void nuevaFilaTablaSalvaguardaE341(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE341(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(0);
	}
	public void nuevaFilaTablaSalvaguardaE351(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodigoHerramienta(0);
		
	}
	
	public void nuevaFilaTablaSalvaguardaE361(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	
	public void nuevaFilaTablaSalvaguardaE371(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoMetodo(0);
		getSeguimientoSalvaguardaBean().setCodigoPublico(0);

		vaciarDatosProvinciaCantonParroquia();
	}
	public void nuevaFilaTablaSalvaguardaE381(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoServicio(0);
	}
	public void nuevaFilaTablaSalvaguardaE391(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoRecursos(0);
		getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(0);
	}
	public void nuevaFilaTablaSalvaguardaF411(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF411(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	
	public void nuevaFilaTablaSalvaguardaF421(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF421(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaF431(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoSistemas(0);
		
	}
	public void nuevaFilaTablaSalvaguardaF441(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaF452(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF452(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaG461(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
		
	}
	public void nuevaFilaTablaSalvaguardaG471(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();	
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoActividad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaG472(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG472(new TableResponses());		
	}
	public void nuevaFilaTablaSalvaguardaG481(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG481(new TableResponses());	
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setInstitucionAcompaniaSeleccionados(null);
		getSeguimientoSalvaguardaBean().setCodigoAccion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaG491(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaG501(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG501(new TableResponses());
		
	}
	
	public void nuevaFilaTablaSalvaguardaG512(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setMonitoreoRemotoSeleccionados(null);
		getSeguimientoSalvaguardaBean().setMonitoreoInSituSeleccionados(null);
		getSeguimientoSalvaguardaBean().setPeriodicidadSeleccionados(null);
	}
	
	/**
	 * Agrega filas a la tabla D331
	 */
	public void agregarFilasTablaSalvaguardaD331(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberOne()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTree(String.join(",", getSeguimientoSalvaguardaBean().getNivelInvolucramientoSeleccionados()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3));			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberTwo(),getAplicacionBean().getListaPueblosNacionalidades() ));
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				}else{

//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberTwo(),getAplicacionBean().getListaPueblosNacionalidades() ));
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTree("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres("");
					}
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnSix(String.join(",", getSeguimientoSalvaguardaBean().getNivelInvolucramientoSeleccionados()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD331(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD331(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoInvolucramiento(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD331 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}

	}
	
	public void agregarFilasTablaSalvaguardaE341(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoTipoAreaConsolidada());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareGenerico(ubicaTipoAreaConsolidada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberFour()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoAreaConsolidada()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareGenerico(ubicaTipoAreaConsolidada(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnSix())));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE341(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE341(false);
				getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE341 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaE351(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenerico(buscaNivelOrganizacion(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoHerramienta());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenericoDos(ubicaHerramienta(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnNumberOne()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3));		
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareStatus(true);

//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoHerramienta()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenericoDos(ubicaHerramienta(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnTwo())));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr());
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenerico(buscaNivelOrganizacion(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE351(false);
				getSeguimientoSalvaguardaBean().setCodigoHerramienta(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE351 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaE361(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenerico(ubicaAutoidentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFour()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareId()==null){
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareStatus(true);

//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2));

					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));										
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive())));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnSix("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE361(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE361 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaE371(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoMetodo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPublico());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenerico(ubicaMetodo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberFour()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenericoDos(ubicaPublico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberFive()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareStatus(true);

//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMetodo()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPublico()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenerico(ubicaMetodo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSix())));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenericoDos(ubicaPublico(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSeven())));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE371(false);
				getSeguimientoSalvaguardaBean().setCodigoMetodo(0);
				getSeguimientoSalvaguardaBean().setCodigoPublico(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE371 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaE381(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoServicio());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberFour(),getSeguimientoSalvaguardaBean().getListaServicio()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareId()==null){
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(10));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareStatus(true);

//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoServicio()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnFive()),getSeguimientoSalvaguardaBean().getListaServicio()));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE381(false);
				getSeguimientoSalvaguardaBean().setCodigoServicio(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}

	public void agregarFilasTablaSalvaguardaE391(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoRecursos());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPeriodicidad());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenerico(ubicaRecurso(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberFour()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenericoDos(ubicaPeriodicidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberFive()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareId()==null){
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(12));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));					
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRecursos()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPeriodicidad()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenerico(ubicaRecurso(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnFive())));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenericoDos(ubicaPeriodicidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnSix())));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE391(false);
				getSeguimientoSalvaguardaBean().setCodigoRecursos(0);
				getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	

	public void agregarFilasTablaSalvaguardaF411(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareId()==null){
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1));
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF411(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF411(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF411 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaF421(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3));
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres("");
					}


					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF421(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF421(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF421 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaF431(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoSistemas());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareGenerico(ubicaSistema(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnNumberOne()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoSistemas()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareGenerico(ubicaSistema(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnOne())));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF431(false);
				getSeguimientoSalvaguardaBean().setCodigoSistemas(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF431 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaF441(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoRiesgo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberSix(), getAplicacionBean().getListaCatalogoRiesgo()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareId()==null){
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRiesgo()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()), 1));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()), 2));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnSeven()), getAplicacionBean().getListaCatalogoRiesgo()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF441(false);
				getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF441 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	public void agregarFilasTablaSalvaguardaF452(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(9));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());
				}else
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());			
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF452(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF452(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF452 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaG461(){		
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodigoRiesgo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberOne(), getAplicacionBean().getListaCatalogoRiesgo()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
				}else{
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRiesgo()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnTree()), getAplicacionBean().getListaCatalogoRiesgo()));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG461(false);
				getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG461 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaG471(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberSix(), getAplicacionBean().getListaControlVigilancia()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFive()));
					}										
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
						if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFive()));
						}else{
//							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFive("");
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos("");
						}
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSeven()), getAplicacionBean().getListaControlVigilancia()));
//
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnOne()), 1));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()), 2));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()), 3));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG471(false);
					getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
					getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
					getSeguimientoSalvaguardaBean().setCodigoControlVigilancia(0);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,   getMensajesController().getPropiedad("info.infoGrabada"),"");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF441 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}

	public void agregarFilasTablaSalvaguardaG481(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoAccion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoTres(ubicaAccion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberSix()));				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFive()));
					}
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.join(",", getSeguimientoSalvaguardaBean().getInstitucionAcompaniaSeleccionados()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareStatus(true);
					
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne()), 1));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()), 2));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()), 3));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccion()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnSeven(String.join(",", getSeguimientoSalvaguardaBean().getInstitucionAcompaniaSeleccionados()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoTres(ubicaAccion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnEight())));
//						
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
						if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFive()));
						}else{
//							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFive("");
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos("");
						}
						
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG481(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG481(false);
					getSeguimientoSalvaguardaBean().setCodigoAccion(0);
					getSeguimientoSalvaguardaBean().setCodigoTipoInstitucion(0);
					getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
					getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,   getMensajesController().getPropiedad("info.infoGrabada"),"");
			}	
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG481 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaG491(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberSix()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberSix(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8));
					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));						
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
						if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
							
						}else{
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres("");
//							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnEight("");
						}
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()), 1));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()), 2));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()), 3));						
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix())));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven()), getAplicacionBean().getListaAutoIdentificacion()));
						
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG491(false);
					getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(0);
					getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
					getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,   getMensajesController().getPropiedad("info.infoGrabada"),"");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG491 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaG501(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().getTareId()==null){
								
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setTareStatus(true);
					
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
					}else{
						
						
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG501(new TableResponses());
	
					
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG501(false);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG501 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public void agregarFilasTablaSalvaguardaG512(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());					
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwo(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoRemotoSeleccionados()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTree(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoInSituSeleccionados()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFour(String.join(",", getSeguimientoSalvaguardaBean().getPeriodicidadSeleccionados()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(12));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());					
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFive()));
					}
					
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareStatus(true);
//					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
					}else{
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne()), 1));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTwo()), 2));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()), 3));
//						
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));					
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
						if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());					
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFive()));
						}else{
//							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnSix("");					
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos("");

						}
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTen(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoRemotoSeleccionados()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnEleven(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoInSituSeleccionados()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwelve(String.join(",", getSeguimientoSalvaguardaBean().getPeriodicidadSeleccionados()));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());
	
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG512(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG512 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
		}
	}
	
	public String ubicaDatosParticipante(int codigo){
		String resultado="";
		for (TipoParticipanteEnum valor : TipoParticipanteEnum.values()) {
			if(valor.getCodigo()==codigo){
				resultado=valor.getEtiqueta();
				break;
			}			
		}
		return resultado;
	}
	
	public String ubicaDatosEstado(int codigo){
		String resultado="";
		for (TipoEstadoEnum valor : TipoEstadoEnum.values()) {
			if(valor.getCodigo()==codigo){
				resultado=valor.getEtiqueta();
				break;
			}			
		}
		return resultado;
	}

	public void vaciarDatosProvinciaCantonParroquia(){
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
	}
	

	public void cargaTodosLosCantones(){
		try{
			List<Object[]> cantones = getAplicacionController().getAplicacionBean().getListaTodosCantones();
			getSeguimientoSalvaguardaBean().setListaTodosCantones(new ArrayList<>());			
			for (Object[] registro : cantones) {
				GeographicalLocations catalogo=new GeographicalLocations();
				catalogo.setGeloName(registro[0].toString());
				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
				catalogo.setGeloCodificationInec(registro[2].toString());
				getSeguimientoSalvaguardaBean().getListaTodosCantones().add(catalogo);
			}
			getSeguimientoSalvaguardaBean().setListaTodosCantones(getSeguimientoSalvaguardaBean().getListaTodosCantones().stream().sorted((cant1,cant2)->cant1.getGeloName().compareTo(cant2.getGeloName())).collect(Collectors.toList()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void grabarSalvaguardaB(){
		try{
//			datosParaGrabarAvanceEjecucion();
			organizaSectoresSeleccionados();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
			List<TableResponses> datosRespuestasTablasCheck= new ArrayList<>();
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().stream(), datosRespuestasTablasCheck.stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().stream()).collect(Collectors.toList());
			
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAB.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaB();
			
			if(validaGrabarTabs(2)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(2,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");

		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaB " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Grabar informacion salvaguarda C
	 */
	public void grabarSalvaguardaC(){
		try{						
//			datosParaGrabarAvanceEjecucion();
			organizaSectoresSeleccionados();
			List<TableResponses> respuestasTabla=new ArrayList<>();	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());

			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAC.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaC();
		
			if(validaGrabarTabs(3)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(3,1);

			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaC " + ": ").append(e.getMessage()));
		}
		
	}
	
	public void preparaInformacionSalvaguardaB(){
		try{
		cargaCatalogoModalidad();
		cargaCatalogoActividad();
		cargaCatalogoAccionImplementada();

		List<TableResponses> listaSalvaguardaB=new ArrayList<>();
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
			listaSalvaguardaB = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 2);
		
		getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(7)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberFour(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberOne(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
			res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFive() == CODIGO_IDENTIFICACION_INDIGENA){
				res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getAplicacionBean().getListaPueblosNacionalidades()));
			}
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(16)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
			res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoTres(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB9(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(17)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(20)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
			
			res.setTareGenericoTres(ubicaDatosEstado(res.getTareColumnNumberSix()));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(27)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().forEach(res->{
			res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberOne()));
			res.setTareGenericoDos(ubicaConformacion(res.getTareColumnNumberFour()));
			res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberThree(),getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
			if(res.getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){				
				res.setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberTwo(), getAplicacionBean().getListaPueblosNacionalidades()));	
			}
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(23)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(9)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(11)).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB62(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(12)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(14)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(21)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(29)).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(31)).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPB(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(160)).collect(Collectors.toList()));
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * prepara informacion salvaguarda C
	 */
	public void preparaInformacionSalvaguardaC(){
		try{
			cargaCatalogoTipoDeAcceso();
		List<TableResponses> listaSalvaguardaC=new ArrayList<>();
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
			listaSalvaguardaC = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 9);
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(43)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
			res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberEight(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso()));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(45)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(57)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
			res.setTareGenericoTres(ubicaResultadoAcuerdo(res.getTareColumnNumberSix()));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(59)).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().forEach(res->{
//			
//			res.setTareCanton(buscarCanton(Integer.parseInt(res.getTareColumnOne())));
//			
//		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(63)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(49)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(50)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(52)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().forEach(res->{
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberOne(),getAplicacionBean().getListaLineaAccion()));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(54)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().forEach(res->{
			res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberOne(),getAplicacionBean().getListaLineaAccion()));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberTwo(), getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberTwo() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberThree()));
			
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(55)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().forEach(res->{			
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberOne(),getAplicacionBean().getListaAutoIdentificacion()));
			if(res.getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberTwo()));			
		});

		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(61)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
		});

		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPC(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(161)).collect(Collectors.toList()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Graba salvaguarda D
	 */
	public void grabarSalvaguardaD(){
		try{
//			datosParaGrabarAvanceEjecucion();
			organizaSectoresSeleccionados();
			List<TableResponses> respuestasTabla=new ArrayList<>();		
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAD.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaD();
			
			if(validaGrabarTabs(4)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(4,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaD " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Graba la salvaguarda E
	 */
	public void grabarSalvaguardaE(){
		try{
//			datosParaGrabarAvanceEjecucion();
			organizaSectoresSeleccionados();
			List<TableResponses> respuestasTabla=new ArrayList<>();		
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAE.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaE();
			
			if(validaGrabarTabs(5)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(5,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaE " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Grabar informacion salvaguarda F
	 */
	public void grabarSalvaguardaF(){
		try{
//			datosParaGrabarAvanceEjecucion();
			organizaSectoresSeleccionados();
			List<TableResponses> respuestasTabla=new ArrayList<>();	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAF.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaF();
			
			if(validaGrabarTabs(6)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(6,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaF " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Grabar informacion salvaguarda G
	 */
	public void grabarSalvaguardaG(){
		try{
//			datosParaGrabarAvanceEjecucion();
			organizaSectoresSeleccionados();
			List<TableResponses> respuestasTabla=new ArrayList<>();		
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAG.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaG();
			
			if(validaGrabarTabs(7)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(7,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarSalvaguarda"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaG " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Prepara informacion salvaguarda D
	 */
	public void preparaInformacionSalvaguardaD(){
		try{
			List<TableResponses> listaSalvaguardaD=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaD = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 16);

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(listaSalvaguardaD.stream().filter((sc)->sc.getQuestions().getQuesId().equals(65)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				else
					res.setTareGenericoDos("");
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(listaSalvaguardaD.stream().filter((sc)->sc.getQuestions().getQuesId().equals(67)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().forEach(res->{
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberOne()));
				if(res.getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberTwo(),getAplicacionBean().getListaPueblosNacionalidades()));
				else
					res.setTareGenericoTres("");
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPD(listaSalvaguardaD.stream().filter((sc)->sc.getQuestions().getQuesId().equals(162)).collect(Collectors.toList()));
			
			vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * prepara informacion salvaguarda E
	 */
	public void preparaInformacionSalvaguardaE(){
		try{
			cargaCatalogoHerramientas();
			cargarCatalogoServicio();
			List<TableResponses> listaSalvaguardaE=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaE = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 20);
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(69)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaTipoAreaConsolidada(res.getTareColumnNumberFour()));
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(71)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().forEach(res->{
				res.setTareGenerico(buscaNivelOrganizacion(res.getTareColumnOne()));
				res.setTareGenericoDos(ubicaHerramienta(res.getTareColumnNumberOne()));
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(73)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(75)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaMetodo(res.getTareColumnNumberFour()));
				res.setTareGenericoDos(ubicaPublico(res.getTareColumnNumberFive()));
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(78)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getSeguimientoSalvaguardaBean().getListaServicio()));
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(80)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaRecurso(res.getTareColumnNumberFour()));
				res.setTareGenericoDos(ubicaPeriodicidad(res.getTareColumnNumberFive()));
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_1(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(82)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_2(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(83)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_3(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(84)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_4(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(85)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_5(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(86)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_6(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(87)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_7(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(88)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_8(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(89)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_9(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(90)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_10(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(91)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_11(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(92)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_12(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(93)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_13(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(94)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_14(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(95)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_15(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(96)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_16(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(97)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_17(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(98)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_18(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(99)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_19(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(100)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_20(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(101)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_21(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(102)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_22(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(173)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_23(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(103)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_24(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(104)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_25(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(105)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_26(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(106)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_27(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(107)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_28(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(108)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_29(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(109)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_30(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(110)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_31(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(111)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_32(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(166)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_33(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(167)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_34(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(168)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_35(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(169)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_36(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(170)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_37(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(171)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_38(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(172)).collect(Collectors.toList()));
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().add(new TableResponses());
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPE(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(163)).collect(Collectors.toList()));


		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Prepara informacion salvaguardaF
	 */
	public void preparaInformacionSalvaguardaF(){
		try{
			List<TableResponses> listaSalvaguardaF=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaF = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 24);
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(113)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(115)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(buscaCatalogoMedidaTomada(res.getTareColumnNumberSix()));
				res.setTareGenericoDos(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});		
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(117)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().forEach(res->{
				res.setTareGenerico(ubicaSistema(res.getTareColumnNumberOne()));
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(119)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getAplicacionBean().getListaCatalogoRiesgo()));
				res.setTareGenericoDos(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(122)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPF(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(164)).collect(Collectors.toList()));
			vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Prepara informacion salvaguarda G
	 */
	public void preparaInformacionSalvaguardaG(){
		try{
			cargaCatalogoActividad();
			cargaCatalogoMonitoreoRemoto();
			cargaCatalogoMonitoreoinSitu();
			cargaCatalogoPeriodicidad();
			List<TableResponses> listaSalvaguardaG=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null) 
				listaSalvaguardaG = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 29);
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(124)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().forEach(res->{			
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberOne(),getAplicacionBean().getListaCatalogoRiesgo()));
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(126)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				}
				res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getAplicacionBean().getListaControlVigilancia()));
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(129)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				}	
				res.setTareGenericoTres(ubicaAccion(res.getTareColumnNumberSix()));
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(131)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAlternativaEconomica(res.getTareColumnNumberSix()));
				res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion() ));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades() ));
			});
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(133)).collect(Collectors.toList()));
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(136)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPG(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(165)).collect(Collectors.toList()));

			vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Metodo que recarga la informacion de la salvaguarda luego de grabar.
	 */
	public void recargaPreguntasRespuestasSalvaguardas(){
		List<Integer> lista=new ArrayList<>();

//		for (ProjectQuestions pq : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
//			lista.add(pq.getSafeguards().getSafeId());
//		}
		for (Safeguards salvaguarda : getComponenteBuscarProyectos().getBuscaProyectosBean().getListadoSalvaguardasAsignadasProyecto()) {
			lista.add(salvaguarda.getSafeId());
		}
		lista= lista.stream().sorted((sv1,sv2)->sv1.compareTo(sv2)).collect(Collectors.toList());
		Safeguards salvaguarda;
		try{
			getSeguimientoSalvaguardaBean().setListaPreguntas(getQuestionsFacade().buscarPreguntasPorSalvaguardas(lista));
			for (Integer codigoSalvaguarda : lista) {
				salvaguarda = localizaSalvaguarda(codigoSalvaguarda);
				//salvaguarda A
				if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1){
					int salvaguardaA=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setCatalogoPoliticas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.POLITICAS.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICOINTERNACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoMarcoJuridicoNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoNormativaSecundariaNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasA(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaA));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}						
					}
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
					int salvaguardaB=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasB(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaB));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}						
					}
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
					int salvaguardaC=salvaguarda.getSafeId();
					
					getSeguimientoSalvaguardaBean().setListaPreguntasC(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasC(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaC));

					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}						
					}

					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
					int salvaguardaD=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasD(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaD));
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}						
					}
				}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
					int salvaguardaE=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasE(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasE(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaE));
					
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}						
					}
				}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
					int salvaguardaF=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasF(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaF));
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}						
					}
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
					int salvaguardaG=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasG(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaG));
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
						}else{
							agregaNuevosValoresRespuestas(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().filter((p)->p.getCatalogs().getCataId()==1 || p.getCatalogs().getCataId()==2 || p.getCatalogs().getCataId()==3).collect(Collectors.toList()),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
						}						
					}
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * Busca el nombre del canton
	 * @param codigo
	 * @return
	 */
	public String buscarCanton(int codigo){
		GeographicalLocations catalogo;
		String respuesta="";
		try{			
			catalogo = getSeguimientoSalvaguardaBean().getListaTodosCantones().stream().filter(can->can.getGeloId().equals(codigo)).findFirst().get();
			respuesta = catalogo.getGeloName();
		}catch(Exception e){
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Localiza el catalogo del plan gobierno
	 * @param codigo
	 * @return
	 */
	public String buscaCatalogoPlanGobierno(int codigo){
		Catalogs catalogo;
		catalogo= getSeguimientoSalvaguardaBean().getCatalogoPlanGobierno().stream().filter(pg->pg.getCataId().equals(codigo)).findFirst().get();
		return catalogo.getCataText1();
	}
	/**
	 * Localiza el catalogo de medida tomada
	 * @param codigo
	 * @return
	 */
	public String buscaCatalogoMedidaTomada(int codigo){
		Catalogs catalogo;
		catalogo= getSeguimientoSalvaguardaBean().getListaCatalogoMedidaTomada().stream().filter(pg->pg.getCataId().equals(codigo)).findFirst().get();
		return catalogo.getCataText1();
	}
	public String buscaNivelOrganizacion(String codigo){
		if(codigo.equals("nivProv"))
			return "Provincial";
		else if(codigo.equals("nivCant"))
			return "Cantonal";
		else if(codigo.equals("nivParr"))
			return "Parroquial";
		else
			return "Organización indigena";
	}
	
	
	public void editarTablaB41(){

		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberTwo());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberThree());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setCodigoModalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive());

		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberSix());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}

		
//		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()) != CODIGO_IDENTIFICACION_INDIGENA){
//			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
//			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
//		}else{
//			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
//			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFive()));
//		}
	}
	
	public void editarTablaB81(){

		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberOne());
		
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoActividad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFour());
		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFour() != CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFive());
		}
	}
	public void editarTablaB9(){

		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberOne());
		
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFour());
		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFour() != CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnNumberFive());
		}
		
	}
	
	public void editarTablaB102(){

		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour());
		if (getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFive());
		getSeguimientoSalvaguardaBean().setCodigoTipoEstado(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberSix());
	}
	public void editarTablaB121(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberOne());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberTwo());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setCodigoAccionImplementada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberThree());
	}
	public void editarTablaB131(){
		getSeguimientoSalvaguardaBean().setModalidadesSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnOne().split(","));
		
	}
	public void editarTablaB143(){
		getSeguimientoSalvaguardaBean().setInformaComunicaSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnTwo().split(","));
		
		getSeguimientoSalvaguardaBean().setInformaEjecucionSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnTree().split(","));
		
	}
	
	public void editarTablaC201(){		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberOne());
		filtraCantones();	
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour());
		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour() != CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFive());
		}
		
		getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberEight());
	}
		
	public void editarTablaC211(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour() != CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFive());
		}
	}
	public void editarTablaC26(){
		getSeguimientoSalvaguardaBean().setCodigoLineaAccion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareColumnNumberOne());
	}
	public void editarTablaC271(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberTwo());
		getSeguimientoSalvaguardaBean().setCodigoLineaAccion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberOne());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberTwo() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberThree());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaC28(){				
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberOne());
		if (getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberOne()==CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberTwo());
	}

	public void editarTablaC291(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFive());
		getSeguimientoSalvaguardaBean().setCodigoTipoResultadoAcuerdo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberSix());
	}
	
	public void editarTablaC293(){
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().getTareColumnOne()));		
	}
	
	public void editarTablaC301(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnNumberThree());
	}
	
	public void editarTablaC311(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberThree());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaD321(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberThree());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	
	public void editarTablaD331(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setNivelInvolucramientoSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTree().split(","));
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberOne() == CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberTwo());
		
	}
	
	public void editarTablaE341(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberFour());
	}
	
	public void editarTablaE351(){
		getSeguimientoSalvaguardaBean().setCodigoNivelProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnOne());	
		getSeguimientoSalvaguardaBean().setCodigoHerramienta(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnNumberOne());
	}
	public void editarTablaE361(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFour());
		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaE371(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoMetodo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setCodigoPublico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberFive());
	}
	public void editarTablaE381(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoServicio(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberFour());
	}

	public void editarTablaE391(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoRecursos(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberFive());
	}
	public void editarTablaF411(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour());
		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaF421(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberOne());
		filtraCantones();	
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberThree());
		
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaF431(){
		getSeguimientoSalvaguardaBean().setCodigoSistemas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnNumberOne());
	}
	public void editarTablaF441(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberSix() );
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaG461(){
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberOne());
	}	
	
	public void editarTablaG471(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberThree());
		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setCodigoControlVigilancia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberSix());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}		
	}
	public void editarTablaG481(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setInstitucionAcompaniaSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo().split(","));
		getSeguimientoSalvaguardaBean().setCodigoAccion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberSix());
		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFour());		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
//			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaG491(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFive());			
			habilitaPuebloNacionalidad();
		}
	}
	
	public void editarTablaG512(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberThree());		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFive());
			habilitaPuebloNacionalidad();
		}
		getSeguimientoSalvaguardaBean().setMonitoreoRemotoSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTwo().split(","));
		getSeguimientoSalvaguardaBean().setMonitoreoInSituSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree().split(","));
		getSeguimientoSalvaguardaBean().setPeriodicidadSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFour().split(","));
	}
	
	public void siguienteTab(int salvaguarda){
		setCodigoSalvaguardaActual(salvaguarda);
		if(!(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual()==1)){			
			if(salvaguarda==1 ){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(1)){
					grabarSalvaguardaA();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.tablasSalvaguardaA"), "");					
				}
				
			}else if(salvaguarda==2 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaB()){
						grabarSalvaguardaB();				
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					}else{
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
					}
					
				}
			}else if(salvaguarda==3 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaC()){
						grabarSalvaguardaC();				
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
					
				}
			}else if(salvaguarda==4 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaD()){
						grabarSalvaguardaD();				
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
					
				}
			}else if(salvaguarda==5 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaE()){
						grabarSalvaguardaE();				

						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
					
				}
			}else if(salvaguarda==6 ){
				if(validaDatosAvanceEjecucion() ){
					if(validaDatosOpcionSiSalvaguardaF()){
						grabarSalvaguardaF();				
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
					
				}
			}else if(salvaguarda==7 ){
				
				if(validaDatosAvanceEjecucion() ){
					if(validaDatosOpcionSiSalvaguardaG()){
						grabarSalvaguardaG();				
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);

					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.seleccionSi"), "");
					
				}
			}
		}
		
	}
	public boolean validaSiguienteTab(int salvaguarda){
		boolean encontrado=false;
		Iterator it = getSeguimientoSalvaguardaBean().getMapaTabs().keySet().iterator();
		while(it.hasNext()){
		  Integer key = (Integer) it.next();		  
		  if(key==salvaguarda){
			  encontrado=true;
			  break;
		  }
		}
		return encontrado;
	}

	public boolean validaGrabarTabs(int salvaguarda){
		boolean encontrado=false;
		Iterator it = getSeguimientoSalvaguardaBean().getMapaTabs().keySet().iterator();
		while(it.hasNext()){
		  Integer key = (Integer) it.next();
		  
		  if(key==salvaguarda)
			  encontrado=true;
		}
		return encontrado;
	}
	public void atrasTabs(){
		if(getSeguimientoSalvaguardaBean().getTabActual()>0)
			getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()-1);
	}
	
	public void tabChange() {
		getSeguimientoSalvaguardaBean().setTabActual(0);
		
	}
	public void cargaProvincias(){
		getSeguimientoSalvaguardaBean().setListaProvincias(new ArrayList<>());
		
		for (Object[] objects : getAplicacionController().getAplicacionBean().getListaProvincias()) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			getSeguimientoSalvaguardaBean().getListaProvincias().add(geo);
		}
	}
	public void filtraCantones(){
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
		List<Object[]> listaTemporal = getAplicacionController().getAplicacionBean().getListaTodosCantones().stream().filter(canton->Integer.valueOf(canton[3].toString()).equals(getSeguimientoSalvaguardaBean().getCodProvincia())).collect(Collectors.toList());		
		for (Object[] objects : listaTemporal) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			getSeguimientoSalvaguardaBean().getListaCantones().add(geo);
		}
		Collections.sort(getSeguimientoSalvaguardaBean().getListaCantones(), new Comparator<GeographicalLocations>(){
            @Override
            public int compare(GeographicalLocations o1, GeographicalLocations o2) {
                return o1.getGeloName().compareToIgnoreCase(o2.getGeloName());
            }
        });
	}
	
	public void filtraParroquias(){
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
		List<Object[]> listaTemporal = getAplicacionController().getAplicacionBean().getListaTodasParroquias().stream().filter(canton->Integer.valueOf(canton[3].toString()).equals(getSeguimientoSalvaguardaBean().getCodCanton())).collect(Collectors.toList());		
		for (Object[] objects : listaTemporal) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			getSeguimientoSalvaguardaBean().getListaParroquias().add(geo);
		}
		Collections.sort(getSeguimientoSalvaguardaBean().getListaParroquias(), new Comparator<GeographicalLocations>(){
            @Override
            public int compare(GeographicalLocations o1, GeographicalLocations o2) {
                return o1.getGeloName().compareToIgnoreCase(o2.getGeloName());
            }
        });
	}
	public String buscaProvinciaCantonParroquia(int codigo,int tipo){
		GeographicalLocations catalogo;
		String respuesta="";
		try{			
			if (tipo==1){
				catalogo = getSeguimientoSalvaguardaBean().getListaProvincias().stream().filter(prov->prov.getGeloId().equals(codigo)).findFirst().get();
				getSeguimientoSalvaguardaBean().setCodProvincia(catalogo.getGeloId());
				filtraCantones();
				respuesta=catalogo.getGeloName();
			}else if(tipo==2){
				catalogo = getSeguimientoSalvaguardaBean().getListaCantones().stream().filter(can->can.getGeloId().equals(codigo)).findFirst().get();
				getSeguimientoSalvaguardaBean().setCodCanton(catalogo.getGeloId());
				filtraParroquias();
				respuesta = catalogo.getGeloName();
			}else if(tipo==3){
				catalogo = getSeguimientoSalvaguardaBean().getListaParroquias().stream().filter(parr->parr.getGeloId().equals(codigo)).findFirst().get();
				respuesta = catalogo.getGeloName();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return respuesta;
	}	
	
	public void mostrarDialogoEliminaValoresRespuesta(String codigo){
		getSeguimientoSalvaguardaBean().setCodigoValoresRespuestas(codigo);
		
		if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("B612")){
			if (getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue()==false &&
					(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).getVaanTextAnswerValue().length()>0 || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).getVaanTextAnswerValue().length()>0)){
				Mensaje.verDialogo("dlgEliminaValoresRespuestas");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("B11123")){
			if (getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue()==false &&
					(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).getVaanTextAnswerValue().length()>0 || 
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).getVaanTextAnswerValue().length()>0
					|| getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(11).getVaanTextAnswerValue().length()>0)){
				Mensaje.verDialogo("dlgEliminaValoresRespuestas");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("G515")){
			if (getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(6).isVaanYesnoAnswerValue()==false &&
					(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(7).getVaanTextAnswerValue().length()>0 )){
				Mensaje.verDialogo("dlgEliminaValoresRespuestas");
			}
		}
	}
	
	public void eliminaInformacionRespuestas(){
		try{
			if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("B612")){
				ValueAnswers nuevaFila1=new ValueAnswers();
				ValueAnswers nuevaFila2=new ValueAnswers();
				nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				nuevaFila1.setVaanStatus(true);
				nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(5));				
				nuevaFila2.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				nuevaFila2.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(6));
				nuevaFila2.setVaanStatus(true);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).setVaanStatus(false);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).setVaanStatus(false);
				getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3));
				getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4));
				getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2));
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3));
				Mensaje.actualizarComponente(":form:salvaguardas:txt1SB3");
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4));
				Mensaje.actualizarComponente(":form:salvaguardas:txt2SB3");
				getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
				getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila2);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(nuevaFila1);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(nuevaFila2);				
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				Mensaje.actualizarComponente(":form:salvaguardas:radiopSB3");
			}else if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("B11123")){
				ValueAnswers nuevaFila1=new ValueAnswers();
				ValueAnswers nuevaFila2=new ValueAnswers();
				ValueAnswers nuevaFila3=new ValueAnswers();
				nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				nuevaFila1.setVaanStatus(true);
				nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(17));				
				nuevaFila2.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				nuevaFila2.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(18));
				nuevaFila2.setVaanStatus(true);
				nuevaFila3.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				nuevaFila3.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(19));
				nuevaFila3.setVaanStatus(true);
				
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).setVaanStatus(false);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).setVaanStatus(false);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(11).setVaanStatus(false);
				
				getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9));
				getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10));
				getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(11));
				
				getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8));
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9));
				Mensaje.actualizarComponente(":form:salvaguardas:txt111");
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10));
				Mensaje.actualizarComponente(":form:salvaguardas:txt112");
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(11));
				Mensaje.actualizarComponente(":form:salvaguardas:txt113");
				getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
				getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila2);
				getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila3);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(nuevaFila1);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(nuevaFila2);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(nuevaFila3);
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				Mensaje.actualizarComponente(":form:salvaguardas:radiopSB8");
			}else if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("G515")){
				if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(25).getVaanTextAnswerValue().trim().length()>0){
					ValueAnswers nuevaFila1=new ValueAnswers();					
					nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					nuevaFila1.setVaanStatus(true);
					nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(35));				
					
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(24).setVaanStatus(false);
					
					getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(25));
					
					getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(24));
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(25));
					Mensaje.actualizarComponente(":form:salvaguardas:txtG515");
					
					getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
					
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().add(nuevaFila1);
									
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().add(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(24));
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));					
					Mensaje.actualizarComponente(":form:salvaguardas:radiopSG7");
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cancelaEliminaInformacionRespuestas(){
		if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("B612")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB3");
			Mensaje.actualizarComponente(":form:salvaguardas:txt1SB3");
			Mensaje.actualizarComponente(":form:salvaguardas:txt2SB3");
		}else if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("B11123")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB8");
			Mensaje.actualizarComponente(":form:salvaguardas:txt111");
			Mensaje.actualizarComponente(":form:salvaguardas:txt112");
			Mensaje.actualizarComponente(":form:salvaguardas:txt113");
		}else if(getSeguimientoSalvaguardaBean().getCodigoValoresRespuestas().equals("G515")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(6).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG7");
			Mensaje.actualizarComponente(":form:salvaguardas:txtG515");
			
		}
	}
	
	public void mostrarDialogoEliminaDatosTabla(String codigo){
		getSeguimientoSalvaguardaBean().setCodigoTablaDatos(codigo);
		if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B41")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B51")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B612")){
			if ((validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61()) || validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB62())) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B71")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B81")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B9")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue()==true){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
			if ((validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102())) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B11")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
			try{
				if(validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143())
						&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue()==false){
					Mensaje.verDialogo("dlgEliminaDatosTabla");
				}
			}catch(Exception e){
					e.printStackTrace();
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C201")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C211")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C241")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).isVaanYesnoAnswerValue()==false 
					&& validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242())){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C26")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C271")){			
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C291")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C301")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C311")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D321")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D331")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E341")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E351")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E361")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E371")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E381")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E391")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.1")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(8).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.2")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(9).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.3")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(10).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.4")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(11).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.5")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(12).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.6")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(13).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.7")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(14).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.8")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(15).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.9")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(16).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.10")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(17).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.11")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(18).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.12")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(19).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.13")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(20).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.14")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(21).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.15")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(22).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.16")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(23).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.17")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(24).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.18")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(25).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.19")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(26).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.20")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(27).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.21")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(28).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.22")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(29).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.23")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(30).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.24")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(31).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.25")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(32).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.26")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(33).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.27")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(34).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.28")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(35).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.29")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(36).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.30")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(37).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.31")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(38).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.32")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(39).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.33")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(40).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}		
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.34")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(41).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.35")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(42).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.36")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(43).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.37")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(44).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.38")){
			if (validaDatosTablasCobeneficio(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0)) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(45).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F411")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F421")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F431")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F441")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F452")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4).isVaanYesnoAnswerValue()==false
					){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471())  
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G481")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G491")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G501")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G511")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}
		
	}
	
	public boolean validaDatosTabla(List<TableResponses> tablaRespuestas){
		if(tablaRespuestas.size()>0)
			return true;
		else
			return false;
	}
	public void eliminaValoresTablaDatos(){
		try{
			if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B41")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB41");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B51")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB51");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B612")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61());
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB62(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB61");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B71")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB71");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B81")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB81");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B9")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB9(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB9");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB102");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B11")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla11B");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB121");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB131");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10));
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(new ArrayList<>());
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB143");
				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C201")){			
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla201C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C211")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla211C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C241")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242());
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla241C");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaC242");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C26")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla26C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C271")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla271C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C291")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla291C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C301")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla301C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C311")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla311C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D321")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla321D");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D331")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla331D");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E341")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla341E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E351")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla351E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E361")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361());
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla361E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E371")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371());	
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla371E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E381")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla381E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E391")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla391E");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.1")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(8));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_1(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio1");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.2")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(9));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_2(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio2");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.3")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(10));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_3(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio3");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.4")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(11));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_4(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio4");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.5")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(12));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_5(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio5");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.6")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(13));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_6(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio6");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.7")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(14));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_7(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio7");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.8")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(15));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_8(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio8");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.9")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(16));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_9(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio9");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.10")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(17));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_10(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio10");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.11")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(18));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_11(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio11");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.12")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(19));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_12(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio12");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.13")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(20));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_13(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio13");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.14")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(21));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_14(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio14");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.15")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(22));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_15(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio15");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.16")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(23));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_16(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio16");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.17")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(24));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_17(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio17");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.18")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(25));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_18(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio18");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.19")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(26));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_19(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio19");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.20")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(27));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_20(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio20");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.21")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(28));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_21(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio21");	
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.22")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(29));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_22(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio22");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.23")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(30));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_23(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio23");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.24")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(31));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_24(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio24");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.25")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(32));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_25(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio25");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.26")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(33));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_26(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio26");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.27")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(34));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_27(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio27");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.28")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(35));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_28(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio28");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.29")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(36));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_29(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio29");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.30")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(37));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_30(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio30");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.31")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(38));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_31(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio31");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.32")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(39));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_32(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio32");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.33")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(40));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_33(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio33");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.34")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(41));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_34(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio34");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.35")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(42));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_35(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio35");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.36")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(43));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_36(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio36");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.37")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(44));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_37(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio37");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.38")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(45));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_38(new ArrayList<>());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().add(new TableResponses());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio38");	
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F411")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411());
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla411F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F421")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421());
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla421F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F431")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla431F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F441")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441());
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla451F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F452")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452());	
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla452F");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla461G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla471G");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G481")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla481G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G491")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla491G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G501")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla501G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G511")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla512G");				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cancelaEliminaValoresTablaDatos(){
		if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B41")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB1");			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B51")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB2"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB51");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B612")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB3"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB61");
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB62");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B71")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB4"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btntablaB71");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B81")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB5"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB81");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B9")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5).setVaanYesnoAnswerValue(false);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB6"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB9");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB7"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB102");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B11")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB8"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB11");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB9"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB121");
			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB10"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB131");
			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB11");
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB143"); 						
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C201")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC1"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC201");
			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C211")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC2"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC211");
			Mensaje.actualizarComponente(":form:salvaguardas:txtC212");
			Mensaje.actualizarComponente(":form:salvaguardas:txtC213");						
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C241")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC3"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC241");
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC242");
									
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C26")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC4"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC26");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C271")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC5"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC271");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C291")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC6"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC291");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C301")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC8"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC301");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C311")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC9"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC311");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D321")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSD1"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaD321");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D331")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSD2"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaD331");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E341")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSE1"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaE341");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E351")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSE2"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaE351");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E361")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSE3"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaE361");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E371")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSE4"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaE371");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E381")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSE5"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaE381");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E391")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSE6"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaE391");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.1")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(8).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene1");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio1");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.2")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(9).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene2");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio2");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.3")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(10).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene3");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio3");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.4")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(11).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene4");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio4");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.5")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(12).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene5");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio5");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.6")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(13).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene6");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio6");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.7")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(14).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene7");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio7");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.8")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(15).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene8");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio8");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.9")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(16).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene9");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio9");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.10")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(17).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene10");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio10");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.11")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(18).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene11");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio11");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.12")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(19).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene12");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio12");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.13")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(20).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene13");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio13");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.14")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(21).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene14");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio14");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.15")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(22).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene15");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio15");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.16")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(23).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene16");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio16");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.17")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(24).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene17");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio17");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.18")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(25).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene18");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio18");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.19")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(26).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene19");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio19");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.20")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(27).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene20");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio20");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.21")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(28).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene21");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio21");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.22")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(29).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene22");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio22");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.23")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(30).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene23");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio23");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.24")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(31).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene24");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio24");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.25")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(32).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene25");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio25");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.26")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(33).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene26");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio26");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.27")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(34).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene27");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio27");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.28")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(35).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene28");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio28");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.29")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(36).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene29");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio29");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.30")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(37).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene30");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio30");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.31")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(38).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene31");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio31");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.32")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(39).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene32");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio32");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.33")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(40).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene33");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio33");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.34")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(41).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene34");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio34");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.35")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(42).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene41");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio41");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.36")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(43).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene36");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio36");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.37")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(44).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene37");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio37");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E40.38")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(45).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:chkcobene38");
			Mensaje.actualizarComponente(":form:salvaguardas:cobeneficio38");	
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F411")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSF1"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaF411");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F421")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSF2"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaF421");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F431")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSF3"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaF431");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F441")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSF4"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaF441");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F452")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSF5");
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaF452");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG1"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG461");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG2"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG471");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G481")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG3"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG481");			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G491")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG4"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG491");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G501")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG5"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG501");			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G511")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSG6"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG512");			
		}
	}
	
	public void eliminaInformacionPregunta241(){
		try{
			ValueAnswers nuevaFila1=new ValueAnswers();
			
			nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			nuevaFila1.setVaanStatus(true);
			nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(8));				
			
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).setVaanStatus(false);
			
			getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5));
			
			getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4));
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5));
			Mensaje.actualizarComponente(":form:salvaguardas:txtC241");
			
			getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
			
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(nuevaFila1);
							
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC3");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void eliminaInformacionPreguntaF451(){
		try{
			ValueAnswers nuevaFila1=new ValueAnswers();
			
			nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			nuevaFila1.setVaanStatus(true);
			nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(9));				
			
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(5).setVaanStatus(false);
			
			getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(5));
			
			getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4));
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(5));
			Mensaje.actualizarComponente(":form:salvaguardas:txtF451");
			
			getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
			
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().add(nuevaFila1);
							
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSF5");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String ubicaPuebloNacionalidad(int codigoPueblo){
		String pueblo="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaPueblosNacionalidades().stream().filter((p)->p.getCataId().equals(codigoPueblo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			pueblo=catalogo.getCataText1();
		}
		return pueblo;
	}
	public String ubicaHerramienta(int codigoHerramienta){
		String herramienta="";
		Optional<Catalogs> resultado= getSeguimientoSalvaguardaBean().getListaHerramientas().stream().filter((p)->p.getCataId().equals(codigoHerramienta)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			herramienta=catalogo.getCataText1();
		}
		return herramienta;
	}
	
	public String ubicaAutoidentificacion(int codigo){
		String identificacion="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaAutoIdentificacion().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			identificacion=catalogo.getCataText1();
		}
		return identificacion;
	}

	public String ubicaAlternativaEconomica(int codigo){
		String alternativa="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaAlternativaEconomica().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			alternativa=catalogo.getCataText1();
		}
		return alternativa;
	}

	public String ubicaConformacion(int codigo){
		String resultado="";
		for (TipoConformacionEnum valor : TipoConformacionEnum.values()) {
			if(valor.getCodigo()==codigo){
				resultado=valor.getEtiqueta();
				break;
			}			
		}
		return resultado;
	}
	public String ubicaMetodo(int codigo){
		String metodo="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaMetodo().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			metodo=catalogo.getCataText1();
		}
		return metodo;
	}

	public String ubicaPublico(int codigo){
		String publico="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaPublico().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			publico=catalogo.getCataText1();
		}
		return publico;
	}
		
	public String ubicaRecurso(int codigo){
		String recurso="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaRecursos().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			recurso=catalogo.getCataText1();
		}
		return recurso;
	}

	public String ubicaPeriodicidad(int codigo){
		String periodicidad="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaPeriodicidad().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			periodicidad = catalogo.getCataText1();
		}
		return periodicidad;
	}
	public String ubicaSistema(int codigo){
		String sistema="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaSistemas().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			sistema = catalogo.getCataText1();
		}
		return sistema;
	}
	public String ubicaInstitucion(int codigo){
		String tipo="";
		if(TipoInstitucionEnum.POLICIANACIONAL.getCodigo() == codigo){
			tipo = TipoInstitucionEnum.POLICIANACIONAL.getEtiqueta();
		}else if(TipoInstitucionEnum.EJERCITO.getCodigo() == codigo){
			tipo = TipoInstitucionEnum.EJERCITO.getEtiqueta();
		}else if(TipoInstitucionEnum.FISCALIA.getCodigo() == codigo){
			tipo = TipoInstitucionEnum.FISCALIA.getEtiqueta();
		}
		return tipo;
	}
	public String ubicaTipoAreaConsolidada(int codigo){
		String tipo="";
		if(TipoAreaConsolidadaEnum.CONSERVACION.getCodigo() == codigo){
			tipo = TipoAreaConsolidadaEnum.CONSERVACION.getEtiqueta();
		}else if(TipoAreaConsolidadaEnum.PRODUCCIONSOSTENIBLE.getCodigo() == codigo){
			tipo = TipoAreaConsolidadaEnum.PRODUCCIONSOSTENIBLE.getEtiqueta();
		}else if(TipoAreaConsolidadaEnum.RESTAURACION.getCodigo() == codigo){
			tipo = TipoAreaConsolidadaEnum.RESTAURACION.getEtiqueta();
		}
		return tipo;
	}
	
	public String ubicaInvolucramiento(int codigo){
		String tipo="";
		if(TipoNivelInvolucramientoEnum.EJECUCIONFINANCIERA.getCodigo() == codigo){
			tipo = TipoNivelInvolucramientoEnum.EJECUCIONFINANCIERA.getEtiqueta();
		}else{
			tipo = TipoNivelInvolucramientoEnum.EJECUCIONTECNICA.getEtiqueta();
		}
		return tipo;
	}
	
	public String ubicaResultadoAcuerdo(int codigo){
		String tipo="";
		if(TipoResultadoAcuerdoEnum.FIRMOACUERDO.getCodigo() == codigo){
			tipo = TipoResultadoAcuerdoEnum.FIRMOACUERDO.getEtiqueta();
		}else{
			tipo = TipoResultadoAcuerdoEnum.NOFIRMOACUERDO.getEtiqueta();
		}
		return tipo;
	}
	
	public String ubicaTipoAccesoRecurso(int codigo){
		String tipo="";
		if(TipoAccesoRecursoEnum.ACCESOPROPIEDADTIERRA.getCodigo() == codigo){
			tipo = TipoAccesoRecursoEnum.ACCESOPROPIEDADTIERRA.getEtiqueta();
		}else{
			tipo = TipoAccesoRecursoEnum.ACCESOARECURSOSNATURALES.getEtiqueta();
		}
		return tipo;
	}
	
	public String ubicaAccion(int codigo){
		String sistema="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaAccion().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			sistema = catalogo.getCataText1();
		}
		return sistema;
	}

	public int ubicaSalvaguarda(int codigo){
		int codigoSalvaguarda=0;
		List<Object[]> listaTemporal = getAplicacionController().getAplicacionBean().getListaSalvaguardas().stream().filter(nivel->Integer.valueOf(nivel[2].toString()).equals(codigo)).collect(Collectors.toList());		
		
		for (Object[] objects : listaTemporal) {
			codigoSalvaguarda = Integer.parseInt(objects[0].toString());			
		}
		return codigoSalvaguarda;
	}
	
	public void renderizaMensajeRegistrosTablaB(){
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida41");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida51B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida61B");		
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida71B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida81B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida9B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida102B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida11B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida121B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida131B");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida143B");
	}
	public void renderizaMensajeRegistrosTablaC(){
		
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida201C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida211C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida242C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida26C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida271C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida291C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida301C");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida311C");
	}
	public void renderizaMensajeRegistrosTablaD(){		
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida321D");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida331D");		
	}
	public void renderizaMensajeRegistrosTablaE(){		
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida341E");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida351E");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida361E");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida371E");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida381E");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida391E");
	}
	public void renderizaMensajeRegistrosTablaF(){		
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida411F");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida421F");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida431F");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida441F");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida452F");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida391E");
	}
	public void renderizaMensajeRegistrosTablaG(){		
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida461G");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida471G");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida481G");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida491G");
		Mensaje.actualizarComponente(":form:salvaguardas:msgTablaRequerida501G");
		
	}
	
	public boolean validaDatosOpcionSiSalvaguardaB(){
		boolean informacionCompleta=true;
			for (int i=0;i<11;i++)
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(i, false);
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().size()==0){							
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(1, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue() && (
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().size()==0)){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(2, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(3, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(4, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(5, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(6, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue() && 
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(7, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(8, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(9, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue() && (
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().size()==0)){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(10, true);
			}
			
		
		return informacionCompleta;
	}
	public boolean validaDatosOpcionSiSalvaguardaC(){
		boolean informacionCompleta=true;
		for (int i=0;i<8;i++)
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(i, false);
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(0, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(1, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(2, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(3, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(4, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(5, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(6, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(7, true);
			}
		
		return informacionCompleta;
	}
	
	public boolean validaDatosOpcionSiSalvaguardaD(){
		boolean informacionCompleta=true;
		for (int i=0;i<2;i++)
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(i, false);
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(0, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(1, true);
			}		
		return informacionCompleta;
	}
	public boolean validaDatosOpcionSiSalvaguardaE(){
		boolean informacionCompleta=true;
		for (int i=0;i<6;i++)
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(i, false);
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(0, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(1, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(2, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(3, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(4, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(5, true);
			}		
		return informacionCompleta;
	}
	
	public boolean validaDatosOpcionSiSalvaguardaF(){
		boolean informacionCompleta=true;
		for (int i=0;i<5;i++)
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(i, false);
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(0, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(1, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(2, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(3, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(4, true);
			}	
		return informacionCompleta;
	}
	
	public boolean validaDatosOpcionSiSalvaguardaG(){
		boolean informacionCompleta=true;
		for (int i=0;i<6;i++)
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(i, false);
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(0, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().size()==0 &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472().size()==0){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(1, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(2, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(3, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(4, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().size()==0 ){
				informacionCompleta=false;
				getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(5, true);
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(6).isVaanYesnoAnswerValue() &&					
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(7).getVaanTextAnswerValue().trim().length()==0){
				informacionCompleta=false;
			}	
		return informacionCompleta;
	}
	
	public void grabarResumenEjecutivo(){
		try{
//			getSeguimientoSalvaguardaBean().getResumenEjecutivo().setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getExecutiveSummarieFacade().grabarResumenEjecutivo(getSeguimientoSalvaguardaBean().getResumenEjecutivo());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void nuevoPlanGobierno(){
		getSeguimientoSalvaguardaBean().setPlanGobierno(new Catalogs());
		Mensaje.verDialogo("dlgNuevoPlanGobierno");
	}
	public void agregarNuevoPlanGobierno(){
		try{
			if(getSeguimientoSalvaguardaBean().getPlanGobierno().getCataText1()==null ){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosPlanGobierno"), "");
				
			}else{
				CatalogsType tipo=new CatalogsType();
				Catalogs catalogo=new Catalogs();
				tipo.setCatyId(TipoCatalogoEnum.PLANPROYECTO.getCodigo());
				catalogo.setCatalogsType(tipo);
				getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(getSeguimientoSalvaguardaBean().getCatalogoPlanGobierno().stream().sorted((cl1,cl2)->cl1.getCataOrder().compareTo(cl2.getCataOrder())).collect(Collectors.toList()));
				if(getSeguimientoSalvaguardaBean().getCatalogoPlanGobierno().size()==0)
					catalogo.setCataOrder(1);
				else
					catalogo.setCataOrder(getSeguimientoSalvaguardaBean().getCatalogoPlanGobierno().get(getSeguimientoSalvaguardaBean().getCatalogoPlanGobierno().size()-1).getCataOrder()+1);
				
				catalogo.setCataNumber(catalogo.getCataOrder());
				
				catalogo.setCataText1(getSeguimientoSalvaguardaBean().getPlanGobierno().getCataText1());
				catalogo.setCataText2(getSeguimientoSalvaguardaBean().getPlanGobierno().getCataText1());
				catalogo.setCataCreationDate(new Date());
				catalogo.setCataCreatorUser(usuario.getUserName());
				
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoPlanGobierno().add(catalogo);
				Mensaje.ocultarDialogo("dlgNuevoPlanGobierno");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setPlanGobierno(new Catalogs());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregaNuevaModalidad(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MODALIDAD.getCodigo(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad(), getSeguimientoSalvaguardaBean().getCatalogoModalidad(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getListaCatalogoModalidad().add(catalogo);
				getCatalogsFacade().create(catalogo);
				Mensaje.ocultarDialogo("dlgNuevaModalidadActividad");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setCatalogoModalidad(new Catalogs());
				if(nuevaModalidadActividad == 1){
					Mensaje.actualizarComponente(":form:salvaguardas:comboModalidadActividad");
				}else if(nuevaModalidadActividad == 2){
					getSeguimientoSalvaguardaBean().getListadoModalidades().add(catalogo.getCataText1());
					Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionB131");
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void nuevaModalidad(int contador){
		nuevaModalidadActividad = contador;
		getSeguimientoSalvaguardaBean().setCatalogoModalidad(new Catalogs());
		Mensaje.verDialogo("dlgNuevaModalidadActividad");
	}
	public void agregaNuevaActividad(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.ACTIVIDAD.getCodigo(), getSeguimientoSalvaguardaBean().getListaCatalogoActividad(), getSeguimientoSalvaguardaBean().getCatalogoActividad(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getListaCatalogoActividad().add(catalogo);
				getCatalogsFacade().create(catalogo);
				Mensaje.ocultarDialogo("dlgNuevaActividad");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
				getSeguimientoSalvaguardaBean().setCatalogoActividad(new Catalogs());
				if(nuevaActividad==1)
					Mensaje.actualizarComponente(":form:salvaguardas:comboActividad");
				else if(nuevaActividad==2){
					getSeguimientoSalvaguardaBean().getListadoActividades().add(catalogo.getCataText1());
					Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionB131");
				}else if(nuevaActividad==3){
						Mensaje.actualizarComponente(":form:salvaguardas:comboActividadB143");
				}else if(nuevaActividad==4){
					Mensaje.actualizarComponente(":form:salvaguardas:comboActividadG471");
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void nuevaActividad(int contador){
		nuevaActividad=contador;
		getSeguimientoSalvaguardaBean().setCatalogoActividad(new Catalogs());
		Mensaje.verDialogo("dlgNuevaActividad");
	}
	public void nuevoMonitoreoRemoto(){		
		getSeguimientoSalvaguardaBean().setNuevoMonitoreoRemoto(new Catalogs());
		Mensaje.verDialogo("dlgNuevoMonitoreoRemoto");
	}
	public void nuevoMonitoreoInSitu(){
		
		getSeguimientoSalvaguardaBean().setNuevoMonitoreoInSitu(new Catalogs());
		Mensaje.verDialogo("dlgNuevoMonitoreoInSitu");
	}
	public void nuevoTipoDeAcceso(){		
		getSeguimientoSalvaguardaBean().setNuevoTipoAcceso(new Catalogs());
		Mensaje.verDialogo("dlgNuevoTipoAcceso");
	}
	public void nuevoServicio(){		
		getSeguimientoSalvaguardaBean().setNuevoServicio(new Catalogs());
		Mensaje.verDialogo("dlgNuevoServicio");
	}
	public void nuevoRecurso(){		
		getSeguimientoSalvaguardaBean().setNuevoRecurso(new Catalogs());
		Mensaje.verDialogo("dlgNuevoRecurso");
	}
	public void nuevoRiesgo(){
		getSeguimientoSalvaguardaBean().setNuevoRiesgo(new Catalogs());
		Mensaje.verDialogo("dlgNuevoRiesgo");
	}
	public void nuevoIncentivo(){
		getSeguimientoSalvaguardaBean().setNuevoIncentivo(new Catalogs());
		Mensaje.verDialogo("dlgNuevoIncentivo");
	}
	public void nuevoControlVigilancia(){
		getSeguimientoSalvaguardaBean().setNuevoControlVigilancia(new Catalogs());
		Mensaje.verDialogo("dlgNuevoControlVigilancia");
	}
	
	public void agregaNuevoControlVigilancia(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.CONTROLVIGILANCIA.getCodigo(), getAplicacionBean().getListaControlVigilancia(), getSeguimientoSalvaguardaBean().getNuevoControlVigilancia(), usuario.getUserName());
				getAplicacionBean().getListaControlVigilancia().add(catalogo);
				
				getCatalogsFacade().create(catalogo);
				
				getSeguimientoSalvaguardaBean().setNuevoRecurso(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:comboControlVF471");
				Mensaje.ocultarDialogo("dlgNuevoControlVigilancia");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregaNuevoIncentivo(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.ALTERNATIVAECONOMICA.getCodigo(), getAplicacionBean().getListaAlternativaEconomica(), getSeguimientoSalvaguardaBean().getNuevoIncentivo(), usuario.getUserName());
				getAplicacionBean().getListaAlternativaEconomica().add(catalogo);
				
				getCatalogsFacade().create(catalogo);
				
				getSeguimientoSalvaguardaBean().setNuevoRecurso(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:alternativaG491");
				Mensaje.ocultarDialogo("dlgNuevoIncentivo");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void agregaNuevoRiesgo(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.RIESGO.getCodigo(), getAplicacionBean().getListaCatalogoRiesgo(), getSeguimientoSalvaguardaBean().getNuevoRiesgo(), usuario.getUserName());
				getAplicacionBean().getListaCatalogoRiesgo().add(catalogo);
				
				getCatalogsFacade().create(catalogo);
				
				getSeguimientoSalvaguardaBean().setNuevoRecurso(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:comboRiesgoF441");
				Mensaje.ocultarDialogo("dlgNuevoRiesgo");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void agregaNuevoRecurso(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.RECURSOS.getCodigo(), getAplicacionBean().getListaRecursos(), getSeguimientoSalvaguardaBean().getNuevoRecurso(), usuario.getUserName());
				getAplicacionBean().getListaRecursos().add(catalogo);
				
				getCatalogsFacade().create(catalogo);
				
				getSeguimientoSalvaguardaBean().setNuevoRecurso(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:recursoE391");
				Mensaje.ocultarDialogo("dlgNuevoRecurso");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregaNuevoServicio(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.SERVICIO.getCodigo(), getSeguimientoSalvaguardaBean().getListaServicio(), getSeguimientoSalvaguardaBean().getNuevoServicio(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getListaServicio().add(catalogo);				
				getCatalogsFacade().create(catalogo);
				
				getSeguimientoSalvaguardaBean().setNuevoServicio(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:servicioE381");
				Mensaje.ocultarDialogo("dlgNuevoServicio");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void agregaNuevoTipoAcceso(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.TIPODEACCESO.getCodigo(), getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso(), getSeguimientoSalvaguardaBean().getNuevoTipoAcceso(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso().add(catalogo);
				getCatalogsFacade().create(catalogo);
				Mensaje.ocultarDialogo("dlgNuevoTipoAcceso");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setNuevoTipoAcceso(new Catalogs());
				
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void agregaNuevaAccionImplementada(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.ACCIONESIMPLEMENTADAS.getCodigo(), getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada(), getSeguimientoSalvaguardaBean().getCatalogoAccionImplementada(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada().add(catalogo);
				getCatalogsFacade().create(catalogo);
				Mensaje.ocultarDialogo("dlgNuevaAccionImplementada");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setCatalogoAccionImplementada(new Catalogs());
				
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void nuevaAccionImplementada(){
		getSeguimientoSalvaguardaBean().setCatalogoAccionImplementada(new Catalogs());
		Mensaje.verDialogo("dlgNuevaAccionImplementada");
	}
	
	public void nuevaHerramienta(){
		getSeguimientoSalvaguardaBean().setNuevaHerramienta(new Catalogs());
		Mensaje.verDialogo("dlgNuevaHerramienta");
	}
	
	public void agregaNuevaHerramienta(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.HERRAMIENTA.getCodigo(), getSeguimientoSalvaguardaBean().getListaHerramientas(), getSeguimientoSalvaguardaBean().getNuevaHerramienta(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getListaHerramientas().add(catalogo);				
				getCatalogsFacade().create(catalogo);
				
				getSeguimientoSalvaguardaBean().setNuevaHerramienta(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:herramientaE351");
				Mensaje.ocultarDialogo("dlgNuevaHerramienta");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void nuevaFormaComunicar(){
		getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
		Mensaje.verDialogo("dlgNuevaFormaComunica");
	}
	public void nuevaFormaInformeEjecucion(){
		getSeguimientoSalvaguardaBean().setNuevaEjecucionSB(new Catalogs());
		Mensaje.verDialogo("dlgNuevaFormaInformeEjecucion");
	}
	
	public void agregaNuevoMonitoreoRemoto(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MONITOREOREMOTO.getCodigo(), getAplicacionBean().getListaMonitoreoRemoto(), getSeguimientoSalvaguardaBean().getNuevoMonitoreoRemoto(), usuario.getUserName());
				getAplicacionBean().getListaMonitoreoRemoto().add(catalogo);
				getSeguimientoSalvaguardaBean().getListadoMonitoreoRemoto().add(catalogo.getCataText1());
				getCatalogsFacade().create(catalogo);	
				getSeguimientoSalvaguardaBean().setNuevoMonitoreoRemoto(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionRemotoG512");
				Mensaje.ocultarDialogo("dlgNuevoMonitoreoRemoto");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregaNuevoMonitoreoInSitu(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo(), getAplicacionBean().getListaMonitoreoInSitu(), getSeguimientoSalvaguardaBean().getNuevoMonitoreoInSitu(), usuario.getUserName());
				getAplicacionBean().getListaMonitoreoInSitu().add(catalogo);
				getSeguimientoSalvaguardaBean().getListadoMonitoreoInSitu().add(catalogo.getCataText1());
				getCatalogsFacade().create(catalogo);	
				getSeguimientoSalvaguardaBean().setNuevoMonitoreoInSitu(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionInSituG512");
				Mensaje.ocultarDialogo("dlgNuevoMonitoreoInSitu");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"),"" );
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregaNuevaEjecucion(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.INFORMEEJECUCION.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion(), getSeguimientoSalvaguardaBean().getNuevaEjecucionSB(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion().add(catalogo);
				getSeguimientoSalvaguardaBean().getListadoInformaEjecucion().add(catalogo.getCataText1());
				getCatalogsFacade().create(catalogo);	
				getSeguimientoSalvaguardaBean().setNuevaEjecucionSB(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionEjecucionB141");
				Mensaje.ocultarDialogo("dlgNuevaFormaInformeEjecucion");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void agregaNuevaComunicacion(){
		try{
				Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.INFORMECOMUNICACION.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoInformacionComunica(), getSeguimientoSalvaguardaBean().getNuevaComunicacionSB(), usuario.getUserName());
				getSeguimientoSalvaguardaBean().getCatalogoInformacionComunica().add(catalogo);
				getSeguimientoSalvaguardaBean().getListadoInformaComunica().add(catalogo.getCataText1());
				getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionB141");
				Mensaje.ocultarDialogo("dlgNuevaFormaComunica");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada"), "");
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void habilitaPuebloNacionalidad(){
		
		if(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()==CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void controlAgregaEditaLeyes(ValueChangeEvent event){

		try{
			List<Integer> antiguos=new ArrayList<>();

			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
				List<Integer> listaAntiguos= (List<Integer>)event.getOldValue();
				List<Integer> listaNuevos= (List<Integer>)event.getNewValue();
				int nuevo=0;
				int antiguo=0;
				boolean encontrado=false;
				if(listaAntiguos!=null && listaAntiguos.size()>0){
					Iterator valoresAntiguos= listaAntiguos.iterator();
					while(valoresAntiguos.hasNext()){
						encontrado=false;
						antiguo = Integer.valueOf(valoresAntiguos.next().toString());
						Iterator valoresNuevos=listaNuevos.iterator();
						while(valoresNuevos.hasNext()){
							nuevo=Integer.valueOf(valoresNuevos.next().toString());
							if(antiguo== nuevo){
								encontrado = true;
								antiguos.add(antiguo);
							}
						}
						if(encontrado==false){
							TableResponses tablaRespuesta= getTableResponsesFacade().buscaLeyPolitica(antiguo, getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 2);
							tablaRespuesta.setTareStatus(false);
							getTableResponsesFacade().agregarEditarNuevaTableResponse(tablaRespuesta);
						}

					}

					int valorNuevo=0;
					int valorAntiguo=0;
					encontrado=false;

					Iterator iteraNuevo=listaNuevos.iterator();
					while (iteraNuevo.hasNext()){
						encontrado=false;
						valorNuevo=Integer.valueOf(iteraNuevo.next().toString());
						Iterator iteraAntiguo = antiguos.iterator();
						while(iteraAntiguo.hasNext()){
							valorAntiguo= Integer.valueOf(iteraAntiguo.next().toString());
							if(valorNuevo==valorAntiguo){
								encontrado=true;
								break;
							}
						}
						if(encontrado==false){
							TableResponses nuevoRegistroTableResponse=new TableResponses();
							Questions question=new Questions();
							question.setQuesId(2);
							nuevoRegistroTableResponse.setTareStatus(true);
							nuevoRegistroTableResponse.setQuestions(question);
							nuevoRegistroTableResponse.setTareColumnNumberOne(valorNuevo);
							nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
							getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
						}

					}
				}else{
					TableResponses nuevoRegistroTableResponse=new TableResponses();
					Questions question=new Questions();
					question.setQuesId(2);
					nuevoRegistroTableResponse.setTareStatus(true);
					nuevoRegistroTableResponse.setQuestions(question);
//					System.out.println( (Object)listaNuevos.get(0).getClass().getSimpleName());
					
					String numero= String.valueOf( listaNuevos.get(0));
//					System.out.println( (Object)numero.getClass().getSimpleName());
//					System.out.println(numero);
					int n= Integer.parseInt(numero);
//					System.out.println(n);
//					nuevoRegistroTableResponse.setTareColumnNumberOne(listaNuevos.get(0).intValue());
					nuevoRegistroTableResponse.setTareColumnNumberOne(n);
					nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void controlAgregaEditaPoliticas(ValueChangeEvent event){

		try{
			List<Integer> antiguos=new ArrayList<>();

			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
				List<Integer> listaAntiguos= (List<Integer>)event.getOldValue();
				List<Integer> listaNuevos= (List<Integer>)event.getNewValue();
				int nuevo=0;
				int antiguo=0;
				boolean encontrado=false;
				if(listaAntiguos!=null && listaAntiguos.size()>0){
					Iterator valoresAntiguos= listaAntiguos.iterator();
					while(valoresAntiguos.hasNext()){
						encontrado=false;
						antiguo = Integer.valueOf(valoresAntiguos.next().toString());
						Iterator valoresNuevos=listaNuevos.iterator();
						while(valoresNuevos.hasNext()){
							nuevo=Integer.valueOf(valoresNuevos.next().toString());
							if(antiguo== nuevo){
								encontrado = true;
								antiguos.add(antiguo);
							}
						}
						if(encontrado==false){
							TableResponses tablaRespuesta= getTableResponsesFacade().buscaLeyPolitica(antiguo, getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 4);
							tablaRespuesta.setTareStatus(false);
							getTableResponsesFacade().agregarEditarNuevaTableResponse(tablaRespuesta);
						}

					}

					int valorNuevo=0;
					int valorAntiguo=0;
					encontrado=false;

					Iterator iteraNuevo=listaNuevos.iterator();
					while (iteraNuevo.hasNext()){
						encontrado=false;
						valorNuevo=Integer.valueOf(iteraNuevo.next().toString());
						Iterator iteraAntiguo = antiguos.iterator();
						while(iteraAntiguo.hasNext()){
							valorAntiguo= Integer.valueOf(iteraAntiguo.next().toString());
							if(valorNuevo==valorAntiguo){
								encontrado=true;
								break;
							}
						}
						if(encontrado==false){
							TableResponses nuevoRegistroTableResponse=new TableResponses();
							Questions question=new Questions();
							question.setQuesId(2);
							nuevoRegistroTableResponse.setTareStatus(true);
							nuevoRegistroTableResponse.setQuestions(question);
							nuevoRegistroTableResponse.setTareColumnNumberOne(valorNuevo);
							nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
							getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
						}

					}
				}else{
					TableResponses nuevoRegistroTableResponse=new TableResponses();
					Questions question=new Questions();
					question.setQuesId(4);
					nuevoRegistroTableResponse.setTareStatus(true);
					nuevoRegistroTableResponse.setQuestions(question);
					String numero= String.valueOf( listaNuevos.get(0));
					int n = Integer.valueOf(numero);
					nuevoRegistroTableResponse.setTareColumnNumberOne(n);
					nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
public void cargaListadoInformaComunica(){
	try{
		getSeguimientoSalvaguardaBean().setListadoInformaComunica(new ArrayList<>());
		for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getCatalogoInformacionComunica()) {
			getSeguimientoSalvaguardaBean().getListadoInformaComunica().add(catalogo.getCataText1());
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
}

public void cargaListadoInformaEjecucion(){
	try{
		getSeguimientoSalvaguardaBean().setListadoInformaEjecucion(new ArrayList<>());
		for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion()) {
			getSeguimientoSalvaguardaBean().getListadoInformaEjecucion().add(catalogo.getCataText1());
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}	

//public void controlaSectores(ValueChangeEvent event){
//	try{
//		List<Integer> antiguos=new ArrayList<>();
//
//		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
//			List<Integer> listaAntiguos= (List<Integer>)event.getOldValue();
//			List<Integer> listaNuevos= (List<Integer>)event.getNewValue();
//			int nuevo=0;
//			int antiguo=0;
//			boolean encontrado=false;
//			if(listaAntiguos!=null && listaAntiguos.size()>0){
//				Iterator valoresAntiguos= listaAntiguos.iterator();
//				while(valoresAntiguos.hasNext()){
//					encontrado=false;
//					antiguo = Integer.valueOf(valoresAntiguos.next().toString());
//					Iterator valoresNuevos=listaNuevos.iterator();
//					while(valoresNuevos.hasNext()){
//						nuevo=Integer.valueOf(valoresNuevos.next().toString());
//						if(antiguo== nuevo){
//							encontrado = true;
//							antiguos.add(antiguo);
//						}
//					}
//					if(encontrado==false){
//						TableResponses tablaRespuesta= getTableResponsesFacade().buscaLeyPolitica(String.valueOf(antiguo), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 4);
//						tablaRespuesta.setTareStatus(false);
//						getTableResponsesFacade().agregarEditarNuevaTableResponse(tablaRespuesta);
//					}
//
//				}
//
//				int valorNuevo=0;
//				int valorAntiguo=0;
//				encontrado=false;
//
//				Iterator iteraNuevo=listaNuevos.iterator();
//				while (iteraNuevo.hasNext()){
//					encontrado=false;
//					valorNuevo=Integer.valueOf(iteraNuevo.next().toString());
//					Iterator iteraAntiguo = antiguos.iterator();
//					while(iteraAntiguo.hasNext()){
//						valorAntiguo= Integer.valueOf(iteraAntiguo.next().toString());
//						if(valorNuevo==valorAntiguo){
//							encontrado=true;
//							break;
//						}
//					}
//					if(encontrado==false){
//						TableResponses nuevoRegistroTableResponse=new TableResponses();
//						Questions question=new Questions();
//						question.setQuesId(2);
//						nuevoRegistroTableResponse.setTareStatus(true);
//						nuevoRegistroTableResponse.setQuestions(question);
//						nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(valorNuevo));
//						nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//						getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
//					}
//
//				}
//			}else{
//				TableResponses nuevoRegistroTableResponse=new TableResponses();
//				Questions question=new Questions();
//				question.setQuesId(4);
//				nuevoRegistroTableResponse.setTareStatus(true);
//				nuevoRegistroTableResponse.setQuestions(question);
//				nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(listaNuevos.get(0)));
//				nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//				getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
//			}
//		}
//	}catch(Exception e){
//		e.printStackTrace();
//	}
//	
//}

public void buscarProyectos(){
	try{
		getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<>());
		if(getSeguimientoSalvaguardaBean().getCodigoBusquedaProyecto()==1){			
			getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().listarProyectosPorIdSocioImpl(getSeguimientoSalvaguardaBean().getCodigoSocioImplementador()));
			if(getSeguimientoSalvaguardaBean().getListaProyectos().size()==0){
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.noProyectos"), "");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoBusquedaProyecto()==2){
			if (getSeguimientoSalvaguardaBean().getTituloProyecto().length()>5){
				getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().listarProyectosPorTextoTitulo(getSeguimientoSalvaguardaBean().getTituloProyecto()));
				if(getSeguimientoSalvaguardaBean().getListaProyectos().size()==0){
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.noProyectos"), "");
				}
			}else{
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
public void vaciaDatosBusqueda(){
	getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<>());
	getSeguimientoSalvaguardaBean().setCodigoProyecto(null);
	getSeguimientoSalvaguardaBean().setCodigoSocioImplementador(0);
	
	getSeguimientoSalvaguardaBean().setTabActual(0);
	vaciarValores();
	getSeguimientoSalvaguardaBean().setListaPreguntasRespuestas(new ArrayList<>());
//	getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
	getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());
	getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
	getSeguimientoSalvaguardaBean().setSocioImplementador(new Partners());
	getSeguimientoSalvaguardaBean().setTituloProyecto("");
}

public void imprimirResumenSalvaguardas(){
	ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	String directorioArchivoPDF = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append(1).append(".pdf").toString();
	rutaPDF=directorioArchivoPDF;
	ResumenPDF.reporteSalvaguardaConDatos(directorioArchivoPDF, getSeguimientoSalvaguardaBean());
}



public void finalizarReporteSalvaguardas(){
	try{
//		grabarResumenEjecutivo();

		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexIsReported(true);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexReportedStatus("F");
//		getAdvanceExecutionSafeguardsFacade().edit(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
		getAdvanceExecutionSafeguardsFacade().actualizaAvanceEjecucionGenero(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
		getSeguimientoSalvaguardaBean().setDatosProyecto(false);
		vaciaDatosBusqueda();
		getComponenteBuscarProyectos().getBuscaProyectosBean().setDatosProyecto(false);
	}catch(Exception e){
		e.printStackTrace();
	}
}

public void validaEdicionCobeneficios(int numeroCobeneficio){
	if(numeroCobeneficio == 1){
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().size() == 0)
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().add(new TableResponses());		
	}
}

public void agregarFilasTablaSalvaguardaE40_1(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(14));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(8));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(14));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_1 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}

public void agregarFilasTablaSalvaguardaE40_2(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(15));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(9));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(15));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_2 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}

public void agregarFilasTablaSalvaguardaE40_3(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(16));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(10));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(16));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_3 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_4(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(17));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(11));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(17));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_4 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_5(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(18));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(12));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(18));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_5 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_6(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(19));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(13));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(19));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_6 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_7(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(20));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(14));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(20));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_7 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_8(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(21));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(15));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(21));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_8 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_9(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(22));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(16));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(22));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_9 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_10(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(23));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(17));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(23));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_10 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_11(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(24));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(18));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(24));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_11 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_12(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(25));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(19));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(25));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_12 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_13(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(26));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(20));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(26));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_13 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_14(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(27));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(21));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(27));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_14 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_15(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(28));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(22));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(28));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_15 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_16(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(29));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(23));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(29));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_16 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("info.infoGrabada"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_17(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(30));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(24));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(30));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_17 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_18(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(31));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(25));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(31));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_18 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_19(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(32));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(26));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(32));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_19 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_20(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(33));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(27));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(33));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_20 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_21(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(34));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(28));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(34));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_21 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_22(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(35));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(29));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(35));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_22 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_23(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(36));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(30));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(36));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_23 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_24(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(37));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(31));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(37));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_24 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_25(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(38));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(32));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(38));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_38 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_26(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(39));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(33));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(39));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_26 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_27(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(40));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(34));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(40));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_27 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}

public void agregarFilasTablaSalvaguardaE40_28(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(41));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(35));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(41));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_28 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"),"" );
	}
}
public void agregarFilasTablaSalvaguardaE40_29(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(42));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(36));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(42));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"),"" );
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_29 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_30(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(43));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(37));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(43));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"),"" );
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_30 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_31(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(38));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_31 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_32(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(45));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(39));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(45));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_32 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_33(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(46));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(40));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(46));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_33 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_34(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(47));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(41));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(47));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_34 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_35(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(48));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(42));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(48));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_35 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"),"" );
	}
}
public void agregarFilasTablaSalvaguardaE40_36(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(49));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(43));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(49));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_36 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_37(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(50));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(44));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(50));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_37 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}
public void agregarFilasTablaSalvaguardaE40_38(){
	try{
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
		}else{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).getTareId()==null){			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(51));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().get(0));
				getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(45));
			}else{
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(51));			
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0).setTareStatus(true);			
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().get(0));
			}			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}
	}catch(Exception e){
		log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE40_38 " + ": ").append(e.getMessage()));
		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabar"), "");
	}
}

public boolean validaIngresoResumenEjecutivo(){
	boolean estado=false;
	if(getSeguimientoSalvaguardaBean().getResumenEjecutivo()!=null){
		if(getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuId() == null && getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuSummaryContent().isEmpty()){
			estado=false;
		}else if(getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuSummaryContent().isEmpty() || getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuSummaryContent().trim().length()<10){
			estado = false;
		}else{
			estado = true;
		}
	}
	return estado;
}
/**
 * Muestra el cuadro de dialogo para finalizar el reporte de ejecucion
 * @param salvaguarda
 */
public void mostrarDialogoFinalizarReporte(int salvaguarda){
	
//	if(getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuId() == null && getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuSummaryContent() == null ){
//		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),"");
//	}else if(getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuSummaryContent().isEmpty() || getSeguimientoSalvaguardaBean().getResumenEjecutivo().getExsuSummaryContent().trim().length()<10){
//		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.tamanioResumenEjecutivo"),"");
//	}else{
		if (salvaguarda == 1){
			if (validaCamposTablasSalvaguardas(salvaguarda)){
				grabarSalvaguardaA();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 2){
			if (validaDatosOpcionSiSalvaguardaB()){
				grabarSalvaguardaB();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 3){
			if (validaDatosOpcionSiSalvaguardaC()){		
				grabarSalvaguardaC();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 4){
			if (validaDatosOpcionSiSalvaguardaD()){
				grabarSalvaguardaD();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 5){
			if (validaDatosOpcionSiSalvaguardaE()){
				grabarSalvaguardaE();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 6){
			if (validaDatosOpcionSiSalvaguardaF()){
				grabarSalvaguardaF();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		} else if(salvaguarda == 7){
			if (validaDatosOpcionSiSalvaguardaG()){
				grabarSalvaguardaG();
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}
//	}
}
/**
 * Imprime el resumen de salvaguardas vacio
 */
public void resumenSalvaguardaVacio(){
	ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	String directorioArchivoPDF = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append(1).append(".pdf").toString();
	ResumenPDF.reporteSalvaguardaVacio(directorioArchivoPDF, getSeguimientoSalvaguardaBean());
}
public boolean validaDatosTablasCobeneficio(TableResponses tabla){
	boolean validacion=false;
	if(tabla.getTareId()!=null && ( tabla.getTareColumnNumberOne()>0 || tabla.getTareColumnNumberTwo()>0 || tabla.getTareColumnNumberThree()>0))
		validacion=true;
	return validacion;
}

public boolean datosProyecto(){		
	return getComponenteBuscarProyectos().datosProyecto();
}
public void volverBuscarProyectos(){
	getComponenteBuscarProyectos().volverABuscarProyectos();
	getComponenteBuscarProyectos().getBuscaProyectosBean().setAsignacionSalvaguardas(false);
	
	getSeguimientoSalvaguardaBean().setDatosProyecto(false);
	getSeguimientoSalvaguardaBean().setTabActual(0);
	vaciarValores();
	vaciaDatosBusqueda();
	getSeguimientoSalvaguardaBean().setMostrartabs(false);
	getComponenteBuscarProyectos().getBuscaProyectosBean().setNuevoSeguimiento(false);
}
///**
// * Carga la información de las salvaguardas del proyecto seleccionado.
// */
//public void cargaDatosProyectoSeleccionado(){
//	try{
//		if(!componenteBuscarProyectos.getBuscaProyectosBean().isAsignacionSalvaguardas()){
//			getSeguimientoSalvaguardaBean().setTabActual(0);
//			getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
//			getSeguimientoSalvaguardaBean().setProyectoSeleccionado(getComponenteBuscarProyectos().getBuscaProyectosBean().getProyectoSeleccionado());		
//			getSeguimientoSalvaguardaBean().setCodigoStrategicPartner(getComponenteBuscarProyectos().getCodigoPartner());		
//			AdvanceExecutionSafeguards avance =getAdvanceExecutionSafeguardsFacade().buscarAvanceSalvaguardaGeneroReportado(getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId(), getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()==null?0:getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(), 1, String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde()), String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-12"));
//			if(avance == null){
//				if(getLoginBean().getTipoRol()==3){
//					for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
//						if(pu.getProjects().getProjId()== getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId() && pu.getUsers().getUserId()== getLoginBean().getUser().getUserId()){
//							if(pu.getPspaId()== null){
//								obtenerSalvaguardasAsignadas(0,pu.getProjects().getProjId());
//							}else{
//								obtenerSalvaguardasAsignadas(pu.getPspaId(),0);
//							}
//
//							getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
//							getSeguimientoSalvaguardaBean().setTabActual(0);
//							getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
//							getSeguimientoSalvaguardaBean().setMostrartabs(true);
//							getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());
//							cargarSalvaguardas();
//							cargaValoresRespuestaPorSalvaguarda();
//							getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
//							getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
//							getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
//							if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null){
//								getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
//								getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));
//							}else{
//								getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());					
//							}
//
//							break;
//						}
//					}
//				}else{
//					if(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()== null){
//						obtenerSalvaguardasAsignadas(0,getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId());
//					}else{
//						obtenerSalvaguardasAsignadas(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(),0);
//					}
//
//					getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
//					getSeguimientoSalvaguardaBean().setTabActual(0);
//					getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
//					getSeguimientoSalvaguardaBean().setMostrartabs(true);
//					getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());
//
//					cargarSalvaguardas();
//					cargaValoresRespuestaPorSalvaguarda();
//					getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
//					getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
//					getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
//				}
//				if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null){
//					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
//					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));	
//					if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId() != null)
//						getSeguimientoSalvaguardaBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
//					if(getSeguimientoSalvaguardaBean().getResumenEjecutivo()==null){
//						getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
//						getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuCreationDate(new Date());
//						getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
//						getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuStatus(true);
//						getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
//					}
//
//				}else{
//					getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());
//					getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
//					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuCreationDate(new Date());
//					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
//					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuStatus(true);
//					getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
//				}
//			}else{
//				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.proyectoReportado"), "");	
//			}
//		}
//	}catch(Exception e){
//		e.printStackTrace();
//	}
//}
/**
 * Carga la información de las salvaguardas del proyecto seleccionado.
 */
public void cargaDatosProyectoSeleccionado(){
	try{
		ProjectsSpecificObjectives projectSpecificObjectives=new ProjectsSpecificObjectives();
		getSeguimientoSalvaguardaBean().setSocioImplementador(getComponenteBuscarProyectos().getBuscaProyectosBean().getSocioImplementador());
		getSeguimientoSalvaguardaBean().setTabActual(0);
		getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
		getSeguimientoSalvaguardaBean().setProyectoSeleccionado(getComponenteBuscarProyectos().getBuscaProyectosBean().getProyectoSeleccionado());		
		getSeguimientoSalvaguardaBean().setCodigoStrategicPartner(getComponenteBuscarProyectos().getBuscaProyectosBean().getCodigoStrategicPartner());
		getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresDisponibles());
		getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
//		System.out.println("CODIGO partner " + getSeguimientoSalvaguardaBean().getCodigoStrategicPartner());
		if(getComponenteBuscarProyectos().getBuscaProyectosBean().isNuevoSeguimiento()){
			AdvanceExecutionSafeguards avance =getAdvanceExecutionSafeguardsFacade().buscarAvanceSalvaguardaGeneroReportado(getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId(), getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()==null?0:getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(), 1, String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde()), String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-12"));
			if(avance == null){
				if(getLoginBean().getTipoRol()==3){
					for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
						if(pu.getProjects().getProjId()== getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId() && pu.getUsers().getUserId()== getLoginBean().getUser().getUserId()){
//							if(pu.getPspaId()== null){
//								obtenerSalvaguardasAsignadas(0,pu.getProjects().getProjId());-
//							}else{
//								obtenerSalvaguardasAsignadas(pu.getPspaId(),0);
//							}

							getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
							getSeguimientoSalvaguardaBean().setTabActual(0);
							getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
							getSeguimientoSalvaguardaBean().setMostrartabs(true);
							getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());
							cargarSalvaguardas();
							cargaValoresRespuestaPorSalvaguarda();
							getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
							getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
							getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
							getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
							getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));
							projectSpecificObjectives.setPsobId(getComponenteBuscarProyectos().getBuscaProyectosBean().getCodigoComponente());
							getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjectsSpecificObjectives(projectSpecificObjectives);
							break;
						}
					}
				}else{
//					if(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()== null){
//						obtenerSalvaguardasAsignadas(0,getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId());
//					}else{
//						obtenerSalvaguardasAsignadas(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(),0);
//					}
					getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
					getSeguimientoSalvaguardaBean().setTabActual(0);
					getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
					getSeguimientoSalvaguardaBean().setMostrartabs(true);
					getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());

					cargarSalvaguardas();
					cargaValoresRespuestaPorSalvaguarda();
					getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
					getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
					getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
					projectSpecificObjectives.setPsobId(getComponenteBuscarProyectos().getBuscaProyectosBean().getCodigoComponente());
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjectsSpecificObjectives(projectSpecificObjectives);
				}
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.proyectoReportado"), "");	
			}
		}else{
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getAdvanceExecution());
			getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
			getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));	

//			if(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()== null){
//				obtenerSalvaguardasAsignadas(0,getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId());
//			}else{
//				obtenerSalvaguardasAsignadas(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(),0);
//			}
			
			getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
			getSeguimientoSalvaguardaBean().setTabActual(0);
			getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
			getSeguimientoSalvaguardaBean().setMostrartabs(true);
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());
			projectSpecificObjectives.setPsobId(getComponenteBuscarProyectos().getBuscaProyectosBean().getCodigoComponente());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjectsSpecificObjectives(projectSpecificObjectives);

			cargarSalvaguardas();
			cargaValoresRespuestaPorSalvaguarda();
			getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
			getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
			getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
			
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}


public void obtenerSalvaguardasAsignadas(int codigoPartner,int codigoProyecto){
	
	try{
		Map<String,ProjectQuestions> mapaTemp=new HashMap<String,ProjectQuestions>();
		List<ProjectQuestions> listaTemporal=new ArrayList<>();
		getSeguimientoSalvaguardaBean().setListaSalvaguardasAsignadas(new ArrayList<>());
		if(codigoProyecto==0)
			listaTemporal = getProjectQuestionsFacade().listaPreguntasPartnerSeleccionadas(codigoPartner);
		else
			listaTemporal = getProjectQuestionsFacade().listaPreguntasProyectoSeleccionadas(codigoProyecto);
		for(ProjectQuestions sa:listaTemporal){
			mapaTemp.put(sa.getSafeguards().getSafeCode(), sa);
		}
		for(Entry<String,ProjectQuestions> sa:mapaTemp.entrySet()){
			getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas().add(sa.getValue());
		}	

	}catch(Exception e){
		e.printStackTrace();
	}
}

//public void cargarSalvaguardas(){
//	if(getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()==null)
//		Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error.cargarProyectos"), "");	
//	else{
//		getSeguimientoSalvaguardaBean().setDatosProyecto(true);
//		numeroSalvaguardas=getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas().size();
//		for (ProjectQuestions pq : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
//			if(pq.getSafeguards().getSafeOrder()==1 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaA(true);
//			}else if(pq.getSafeguards().getSafeOrder()==2 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaB(true);
//			}else if(pq.getSafeguards().getSafeOrder()==3 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaC(true);
//			}else if(pq.getSafeguards().getSafeOrder()==4 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaD(true);
//			}else if(pq.getSafeguards().getSafeOrder()==5 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaE(true);
//			}else if(pq.getSafeguards().getSafeOrder()==6 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaF(true);
//			}else if(pq.getSafeguards().getSafeOrder()==7 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
//				getSeguimientoSalvaguardaBean().setSalvaguardaG(true);
//			}
//		}
//
//	}
//
//}
public void cargarSalvaguardas(){
	if(getComponenteBuscarProyectos().getBuscaProyectosBean().getListadoSalvaguardasAsignadasProyecto()==null)
		Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error.cargarProyectos"), "");	
	else{
		getSeguimientoSalvaguardaBean().setDatosProyecto(true);
		vaciaMostrarSalvaguardas();
		numeroSalvaguardas=getComponenteBuscarProyectos().getBuscaProyectosBean().getListadoSalvaguardasAsignadasProyecto().size();
		for (Safeguards salvaguarda : getComponenteBuscarProyectos().getBuscaProyectosBean().getListadoSalvaguardasAsignadasProyecto()) {
			if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaA(true);
			}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaB(true);
			}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaC(true);
			}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaD(true);
			}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaE(true);
			}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaF(true);
			}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1 && salvaguarda.getSafeParentId()==null){
				getSeguimientoSalvaguardaBean().setSalvaguardaG(true);
			}
		}

	}

}
public void vaciaMostrarSalvaguardas(){
	getSeguimientoSalvaguardaBean().setSalvaguardaA(false);
	getSeguimientoSalvaguardaBean().setSalvaguardaB(false);
	getSeguimientoSalvaguardaBean().setSalvaguardaC(false);
	getSeguimientoSalvaguardaBean().setSalvaguardaD(false);
	getSeguimientoSalvaguardaBean().setSalvaguardaE(false);
	getSeguimientoSalvaguardaBean().setSalvaguardaF(false);
	getSeguimientoSalvaguardaBean().setSalvaguardaG(false);
	
}

public void organizaSectoresSeleccionados(){
	getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
	getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdvanceSectorsList(new ArrayList<>());
	int codigoSector=0;
	Iterator itera = getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().iterator();	
	while (itera.hasNext()){
		codigoSector = Integer.valueOf(itera.next().toString());
		Sectors sector=new Sectors();
		AdvanceSectors as = new AdvanceSectors();
		sector.setSectId(codigoSector);
		as.setSectors(sector);
		as.setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
		as.setAdseSelectedSector(true);
//		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdvanceSectorsList()==null)			
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdvanceSectorsList().add(as);
	}
	
}

}
