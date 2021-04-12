package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsSafeguardsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.ExecutiveSummariesFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoConformacionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoInstitucionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoParticipanteEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRespuestaEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoSalvaguardaEnum;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import ec.gob.ambiente.suia.model.Users;
import ec.gob.ambiente.suia.service.GeographicalLocationsFacade;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class SeguimientoSalvaguardaController  implements Serializable{


	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SeguimientoSalvaguardaController.class);
	
    @Getter
    @Setter
    @Inject
    private AplicacionController aplicacionController;
	
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
	
	private int numeroSalvaguardas;
	

	
	
	private Users usuario;
	
	@PostConstruct
	public void init(){
		try{
			numeroSalvaguardas=0;
			getSeguimientoSalvaguardaBean().setTabActual(0);
			vaciarValores();			
			cargarProyectos();
			cargaSalvaguardasPlanAccion();
			cargaSectoresInteres();
			usuario=new Users();
			usuario.setUserName("Christian Báez");
			usuario.setUserId(0);
			getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
			getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
			cargaProvincias();
			cargaTodosLosCantones();
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
 
	}
	/**
	 * Carga los proyectos, programas o pdi
	 */
	public void cargarProyectos(){
		try{
			
			getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<Projects>());
			getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().buscarTodosLosProyectos());
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));			
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
					getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
					cargaSalvaguardasProyecto();
					cargarAvanceEjecucionSalvaguarda();
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
	/**
	 * Carga los sectores de interes del proyecto seleccionado
	 */
	public void sectoresInteresProyecto(){
		
		getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
		for(AdvanceSectors as:getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdvanceSectorsList()){			
//			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().add(String.valueOf(as.getSectors().getSectId()));
			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().add(as.getSectors().getSectId());
		}		
	}
	/**
	 * Carga los sectores de interes
	 */
	public void cargaSectoresInteres(){
		try{
			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(new ArrayList<Sectors>());
			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(getSectorsFacade().buscarTodosLosSectores());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
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
				
				ps.setPrsaId(Integer.valueOf(objects[0].toString()));
				safeguard.setSafeId(Integer.valueOf(objects[1].toString()));
				safeguard.setSafeOrder(Integer.valueOf(objects[2].toString()));
				safeguard.setSafeLevel(Integer.valueOf(objects[3].toString()));
				safeguard.setSafeCode(objects[4].toString());
				ps.setProjectsCobenefits(null);
				ps.setProjectsRisks(null);
				ps.setSafeguards(safeguard);
				getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto().add(ps);
				
			}
			
			
//			getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(new ArrayList<ProjectsSafeguards>());
//			getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(getProjectsSafeguardsFacade().buscarProyectoPorCobeneficio(getSeguimientoSalvaguardaBean().getCodigoProyecto()));
//			if (getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null || getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto().isEmpty() ){
//				getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(getProjectsSafeguardsFacade().buscarProyectosPorRiesgo(getSeguimientoSalvaguardaBean().getCodigoProyecto()));
//			}
			if(getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion()==null || getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
			}else{
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
	public void cargarAvanceEjecucionSalvaguarda(){
		try{
			
			getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(getSeguimientoSalvaguardaBean().getCodigoProyecto()));	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()==null){
				AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
				avanceEjecucion.setAdexIsReported(false);
				avanceEjecucion.setAdexRegisterDate(new Date());
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(avanceEjecucion);
				getSeguimientoSalvaguardaBean().setResumenEjecutivo(new ExecutiveSummaries());
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuCreationDate(new Date());
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuStatus(true);
				getSeguimientoSalvaguardaBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
			}else{
				sectoresInteresProyecto();
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
		for (ProjectsSafeguards salvaguarda : getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()) {				
			lista.add(salvaguarda.getSafeguards().getSafeId());
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
					getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.LEYES.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}						
					}
					preparaInformacionSalvaguardaA();
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
					int salvaguardaB=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.INFORMEEJECUCION.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoInformacionComunica(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.INFORMECOMUNICACION.getCodigo()));
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}						
					}
					preparaInformacionSalvaguardaB();
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
					int salvaguardaC=salvaguarda.getSafeId();
//					cargaTodosLosCantones();
					
					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}else{
							agregaNuevosValoresRespuestas(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().filter((p)->p.getCatalogs().getCataId()==1 || p.getCatalogs().getCataId()==2 || p.getCatalogs().getCataId()==3).collect(Collectors.toList()),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}						
					}
					preparaInformacionSalvaguardaC();
					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
					int salvaguardaD=salvaguarda.getSafeId();
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasD().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasD().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}						
					}
					preparaInformacionSalvaguardaD();
					//SALVAGUARDA E
				}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
					int salvaguardaE=salvaguarda.getSafeId();
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaE).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntasE().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaE).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntasE().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}						
					}
					preparaInformacionSalvaguardaE();
					//SALVAGUARDA F
				}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
					int salvaguardaF=salvaguarda.getSafeId();
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaCatalogoMedidaTomada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MEDIDATOMADA.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaF).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntasF().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaF).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntasF().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}						
					}
					preparaInformacionSalvaguardaF();
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
					int salvaguardaG=salvaguarda.getSafeId();
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoRemoto(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOREMOTO.getCodigo()));
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoInSitu(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
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
							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
						}						
					}
					preparaInformacionSalvaguardaG();
				}
			}
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.respuestasPreguntas"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaValoresRespuestaPorSalvaguarda " + ": ").append(e.getMessage()));
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
					grabarSalvaguardaA();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");			
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB()){
					grabarSalvaguardaB();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");			
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC()){				
					grabarSalvaguardaC();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				
			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD()){				
					grabarSalvaguardaD();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE()){				
					grabarSalvaguardaE();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF()){				
					grabarSalvaguardaF();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG()){				
					grabarSalvaguardaG();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}
		}else{
		
			if(codigoSalvaguarda==1){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda))				
						Mensaje.verDialogo("dlgGrabaReporteA");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB())
					Mensaje.verDialogo("dlgGrabaReporteB");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC())				
					Mensaje.verDialogo("dlgGrabaReporteC");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				
			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD())				
					Mensaje.verDialogo("dlgGrabaReporteD");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE())				
					Mensaje.verDialogo("dlgGrabaReporteE");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF())				
					Mensaje.verDialogo("dlgGrabaReporteF");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG())				
					Mensaje.verDialogo("dlgGrabaReporteG");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}
		}
	}
	
	
	/**
	 * Agrega una fila a primera tabla de la salvaguarda A
	 */
	public void agregaFilaTablaSalvaguardaA(){
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareId()==null){
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));	
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(buscaCatalogoPlanGobierno(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnOne())));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareGenericoNumerico()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareStatus(true);
			getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(buscaCatalogoPlanGobierno(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnOne())));
			getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
		}
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasA(new TableResponses());
		getSeguimientoSalvaguardaBean().setNuevoRegistroTablaA1(false);

	}
	/**
	 * Graba la salvaguarda A
	 */
	public void grabarSalvaguardaA(){
		try{
			
				datosParaGrabarAvanceEjecucion();
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(new ArrayList<>());
				List<TableResponses> respuestasTabla= new ArrayList<>();
				List<TableResponses> datosRespuestasTabla=Stream.concat(preparaGrabarLeyesSalvaguardaA().stream(), preparaGrabarPoliticasSalvaguardaA().stream()).collect(Collectors.toList());
				respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA().stream(), datosRespuestasTabla.stream()).collect(Collectors.toList());
				
				
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
				
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().stream().forEach(datos->{
					datos.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));
					datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().add(datos);
				});
				List<TableResponses> datosTablaConcatenados= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().stream()).collect(Collectors.toList());
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(datosTablaConcatenados);
				getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),ubicaSalvaguarda(TipoSalvaguardaEnum.SALVAGUARDAA.getCodigo()));
				recargaPreguntasRespuestasSalvaguardas();
				preparaInformacionSalvaguardaA();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
				if(validaGrabarTabs(1)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
					getSeguimientoSalvaguardaBean().getMapaTabs().put(1,1);
//					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}
		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaA " + ": ").append(e.getMessage()));
			e.printStackTrace();
		}
	}
	/**
	 * Configura los datos de avanceEjecucion y avanceSectores para grabar/actualizar
	 */
	public void datosParaGrabarAvanceEjecucion(){
		Projects proyecto=new Projects();
		proyecto.setProjId(getSeguimientoSalvaguardaBean().getCodigoProyecto());
		Users usuario=new Users();
		usuario.setUserId(0);
		usuario.setUserName("Christian Baez");
		List<AdvanceSectors> listaAvanceSectores=new ArrayList<>();
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjects(proyecto);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setUsers(usuario);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreatorUser(usuario.getUserName());
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreationDate(new Date());
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexRegisterDate(new Date());
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexStatus(true);

		Iterator itera=getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().iterator();
		while (itera.hasNext()){

//			System.out.println(itera.next());
			Sectors sector=new Sectors();
//			int codigoSector = Integer.parseInt(sectores.toString());
//			sector.setSectId(new Integer(codigoSector)); //CAMBIO OJO
			sector.setSectId(Integer.valueOf(itera.next().toString()));
			AdvanceSectors avanceSectores=new AdvanceSectors();
			avanceSectores.setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			avanceSectores.setSectors(sector);
			avanceSectores.setAdseSelectedSector(true);
			listaAvanceSectores.add(avanceSectores);
		}

//		for (Integer sectores : getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados()) {
//			Sectors sector=new Sectors();
////			int codigoSector = Integer.parseInt(sectores.toString());
////			sector.setSectId(new Integer(codigoSector)); //CAMBIO OJO
//			sector.setSectId(sectores);
//			AdvanceSectors avanceSectores=new AdvanceSectors();
//			avanceSectores.setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			avanceSectores.setSectors(sector);
//			avanceSectores.setAdseSelectedSector(true);
//			listaAvanceSectores.add(avanceSectores);
//		}
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdvanceSectorsList(listaAvanceSectores);
	}

	public List<TableResponses> preparaGrabarLeyesSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera =getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(itera.next().toString());
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		
//		for(Integer catalogo:getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado()){
//			TableResponses respuesta=new TableResponses();
//			respuesta.setTareColumnOne(Integer.toString(catalogo));
//			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
//			respuesta.setTareStatus(true);
//			respuestasTabla.add(respuesta);
//		}
		return respuestasTabla;
	}
	public List<TableResponses> preparaGrabarPoliticasSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(String.valueOf(itera.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
//		for(Integer catalogo:getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado()){
//			TableResponses respuesta=new TableResponses();
//			respuesta.setTareColumnOne(String.valueOf(catalogo));
//			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3));
//			respuesta.setTareStatus(true);
//			respuestasTabla.add(respuesta);
//		}
		return respuestasTabla;
	}
	public List<TableResponses> preparaGrabarComunicaBeneficiariosSalvaguardaB(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(String.valueOf(itera.next()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(25));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
//		for(Integer catalogo:getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado()){
//			TableResponses respuesta=new TableResponses();
//			respuesta.setTareColumnOne(String.valueOf(catalogo));
//			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(25));
//			respuesta.setTareStatus(true);
//			respuestasTabla.add(respuesta);
//		}
		return respuestasTabla;
	}
	
	public List<TableResponses> preparaGrabarEjecucionProyectoSalvaguardaB(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(String.valueOf(itera.next()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(26));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
//		for(Integer catalogo:getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado()){
//			TableResponses respuesta=new TableResponses();
//			respuesta.setTareColumnOne(String.valueOf(catalogo));
//			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(26));
//			respuesta.setTareStatus(true);
//			respuestasTabla.add(respuesta);
//		}
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
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaA(new ArrayList<>());
		cargaCatalogoPlanGobierno();
		getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
		listaLeyes= getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1).getTableResponsesList().stream().filter((leyes)->leyes.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaPoliticas= getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3).getTableResponsesList().stream().filter((politicas)->politicas.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaLeyes.stream().forEach(res->{						
//			getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(String.valueOf(res.getTareColumnOne()));
			getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
		});
		listaPoliticas.stream().forEach(res->{			
			getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
		});
		List<TableResponses> tablaAux=new ArrayList<>();
		tablaAux=getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		tablaAux.stream().forEach(res->{
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(res);
		});	
				
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().forEach(res->{
			res.setTareCatPlanGobierno(buscaCatalogoPlanGobierno(Integer.parseInt(res.getTareColumnOne())));			
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPA(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPA(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
	}
	
	public void nuevaLeySA(){
		getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());		
		getSeguimientoSalvaguardaBean().setNuevaLey(true);
	}
	public void noGrabarNuevaLeySA(){
		getSeguimientoSalvaguardaBean().setNuevaLey(false);
		getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
	}
	public void noGrabarNuevaPoliticaSA(){
		getSeguimientoSalvaguardaBean().setNuevaPolitica(false);
		getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
	}
	public void nuevaPoliticaSA(){
		getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
		getSeguimientoSalvaguardaBean().setNuevaPolitica(true);
	}
	public void grabarNuevaLeyPoliticaSA(int categoria){
		CatalogsType tipo =new CatalogsType();
		
		Catalogs catalogo=new Catalogs();
		catalogo.setCataCreatorUser(usuario.getUserName());
		
		catalogo.setCataStatus(true);
		catalogo.setCataCreationDate(new Date());
		if (categoria==1){
			if(getSeguimientoSalvaguardaBean().getNuevaLeySA().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosLey"));
			}else{
				tipo.setCatyId(TipoCatalogoEnum.LEYES.getCodigo());
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
		}else{
			if(getSeguimientoSalvaguardaBean().getNuevaPoliticaSA().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosPolitica"));
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
			getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PLANPROYECTO.getCodigo()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean validaDatosAvanceEjecucion(){
		if(getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().size()==0){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosAvanceEjecucion"));			
			return false;
		}else{
			return true;
		}
	}
	
	public boolean validaCamposTablasSalvaguardas(int salvaguarda){
		if (salvaguarda==1){
//			for (TableResponses respuesta : getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()) {
//				if(respuesta.getTareColumnOne()==null)
//					return false;
//				else{
//					if(respuesta.getTareColumnOne().trim().equals("") || respuesta.getTareColumnTwo().trim().equals("")){
//						return false;						
//					}
//				}
//			}
			if(getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().size()==0 || getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().size()==0)
				return false;
		}
		return true;
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
	

	

	
	////////SALVAGUARDA B
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
	
//	public void filtraCanton(){
//		try{
//			
//			List<Object[]> cantones=getGeographicalLocationsFacade().listarCantonesPorProvincia(getSeguimientoSalvaguardaBean().getCodigoProvincia());
//			
//			getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
//			getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
//			for (Object[] registro : cantones) {
//				GeographicalLocations catalogo=new GeographicalLocations();
//				catalogo.setGeloName(registro[0].toString());
//				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
//				catalogo.setGeloCodificationInec(registro[2].toString());
//				getSeguimientoSalvaguardaBean().getListaCantones().add(catalogo);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	public void filtraParroquia(){
//		try{
//			List<Object[]> parroquia=getGeographicalLocationsFacade().listarParroquiasPorCanton(getSeguimientoSalvaguardaBean().getCodigoCanton());
//			getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
//			for (Object[] registro : parroquia) {
//				GeographicalLocations catalogo=new GeographicalLocations();
//				catalogo.setGeloName(registro[0].toString());
//				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
//				catalogo.setGeloCodificationInec(registro[2].toString());
//				getSeguimientoSalvaguardaBean().getListaParroquias().add(catalogo);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	public void agregarFilasTablaSalvaguardaB41(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaB();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTwo()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTree()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()), 3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTwo()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTree()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()), 3));			
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB41(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB41(false); 
//				getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
//				getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB51(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB51(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB71(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setTareStatus(true);
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB71(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB71(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB81(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());

			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTree()), 3));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81(new TableResponses());
			vaciarDatosProvinciaCantonParroquia();
//			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
//			getSeguimientoSalvaguardaBean().setCodigoCanton("");
//			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB81(false);
//			getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
//			getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB81 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB102(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());

			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
//			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
//			getSeguimientoSalvaguardaBean().setCodigoCanton("");
//			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB102(false);
//			getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
//			getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB102 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB103(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(15));		
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB103(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB103(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB103 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void inicializaNuevoRegistroTablas(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
//		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
//		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
//		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
	}
	public void inicializaNuevoRegistroTablasD321(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
//		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
//		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
//		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
	}

	public void agregarFilasTablaSalvaguardaB121(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaB();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareStatus(true);
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(ubicaConformacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFour())));
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());				
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(ubicaConformacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFour())));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB121(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB121 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB131(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23));		
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB131(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB131(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB131 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC201(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaB();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTree())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTree())));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC201(false);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC201 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC211(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC211(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC211(false);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			vaciarDatosProvinciaCantonParroquia();
//			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
//			getSeguimientoSalvaguardaBean().setCodigoCanton("");
//			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC211 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaC241(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC241(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC241(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC241 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaC26(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC26(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC26(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC26 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC271(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTwo())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareStatus(true);

				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTwo())));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC271(false);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC271 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC28(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareStatus(true);

				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne())));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC28(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC28(false);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC28 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC291(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC291(false);
			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC291 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC293(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17));			
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodCanton()));					
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodCanton()));					
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC293(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC293(false);

//			getSeguimientoSalvaguardaBean().setCodigoCanton("");
			getSeguimientoSalvaguardaBean().setCodCanton(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC293 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC301(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
			}else{
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodigoCanton()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC301(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC301(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaC311(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
			}else{			
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));

				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC311(false);
//			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
//			getSeguimientoSalvaguardaBean().setCodigoCanton("");
//			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
			vaciarDatosProvinciaCantonParroquia();
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaD321(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareId()==null){
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnOne()), 1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTwo()), 2));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTree()), 3));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnSeven())));
			
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareStatus(true);
			getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());

			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnSeven())));

				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD321(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD321 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPA(){
		try{
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPA " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPB(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(27));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPB().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPB(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPB(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPB " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPC(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(22));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPC().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPC(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPC(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPC " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaOPD(){
		try{
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPD " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaOPE(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPE().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPE());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPE(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPE(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaOPF(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(11));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPF().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPF(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPF(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaOPG(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(36));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPG().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPG(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaOPG(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	/**
	 * Permite agregar nueva fila a la tabla B102
	 */
	public void nuevaFilaTablaSalvaguardaB102(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
		
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnSix("0");

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	
	/**
	 * Permite agregar nueva fila a la tabla B81
	 */
	public void nuevaFilaTablaSalvaguardaB81(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81(new TableResponses());

		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnEight("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnSix("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnSeven("0");

		vaciarDatosProvinciaCantonParroquia();
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
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnSix("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFive("0");
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(0);
	}
	
	/**
	 * Permite agregar nueva fila a la tabla B41
	 */
	public void nuevaFilaTablaSalvaguardaB41(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB41(new TableResponses());

		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSix("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSeven("0");

		vaciarDatosProvinciaCantonParroquia();
	}
	
	public void nuevaFilaTablaSalvaguardaC211(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC211(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	
	public void nuevaFilaTablaSalvaguardaC201(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());

		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
	}
	public void nuevaFilaTablaSalvaguardaC241(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC241(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaC26(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC26(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaC271(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		
	}
	public void nuevaFilaTablaSalvaguardaC291(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);		
	}
	public void nuevaFilaTablaSalvaguardaC293(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC293(new TableResponses());		
		getSeguimientoSalvaguardaBean().setCodCanton(0);				
	}
	public void nuevaFilaTablaSalvaguardaC301(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC301(new TableResponses());		
		
	}
	
	public void nuevaFilaTablaSalvaguardaC3111(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());
		
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);		
	}
	
	/**
	 * Permite agregar nueva fila a la tabla D331
	 */
	public void nuevaFilaTablaSalvaguardaD321(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestas().setTareColumnEight("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestas().setTareColumnTen("0");
	}
	public void nuevaFilaTablaSalvaguardaE341(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE341(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
	}
	public void nuevaFilaTablaSalvaguardaE351(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodigoHerramienta(0);
		
	}
	
	public void nuevaFilaTablaSalvaguardaE361(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
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
	}
	
	public void nuevaFilaTablaSalvaguardaF421(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF421(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(0);
	}
	public void nuevaFilaTablaSalvaguardaF431(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoSistemas(0);
		
	}
	public void nuevaFilaTablaSalvaguardaF441(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
	}
	public void nuevaFilaTablaSalvaguardaF452(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF452(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaG461(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
		
	}
	public void nuevaFilaTablaSalvaguardaG471(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();	
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
	}
	public void nuevaFilaTablaSalvaguardaG472(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG472(new TableResponses());		
	}
	public void nuevaFilaTablaSalvaguardaG481(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG481(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoInstitucion(0);
		getSeguimientoSalvaguardaBean().setCodigoAccion(0);
	}
	public void nuevaFilaTablaSalvaguardaG491(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(0);
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
	}
	
	/**
	 * Agrega filas a la tabla D331
	 */
	public void agregarFilasTablaSalvaguardaD331(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3));			
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
			}else{
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD331(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD331(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaE341(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareId()==null){
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne()), 1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo()), 2));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree()), 3));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareStatus(true);
			getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());

			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE341(new TableResponses());

			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE341(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE341 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaE351(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3));		
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenerico(buscaNivelOrganizacion(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoHerramienta()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenericoDos(ubicaHerramienta(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnTwo())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareStatus(true);
				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoHerramienta()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenericoDos(ubicaHerramienta(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnTwo())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenerico(buscaNivelOrganizacion(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE351(false);
			getSeguimientoSalvaguardaBean().setCodigoHerramienta(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE351 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaE361(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareStatus(true);
				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive())));
					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE361(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE361 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaE371(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMetodo()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPublico()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenerico(ubicaMetodo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSix())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenericoDos(ubicaPublico(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSeven())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareStatus(true);
				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMetodo()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPublico()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenerico(ubicaMetodo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSix())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenericoDos(ubicaPublico(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSeven())));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE371(false);
				getSeguimientoSalvaguardaBean().setCodigoMetodo(0);
				getSeguimientoSalvaguardaBean().setCodigoPublico(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE371 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaE381(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(9));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoServicio()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(ubicaServicio(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnFive())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareStatus(true);
				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoServicio()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(ubicaServicio(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnFive())));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE381(false);
				getSeguimientoSalvaguardaBean().setCodigoServicio(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE391(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(11));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRecursos()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPeriodicidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenerico(ubicaRecurso(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnFive())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenericoDos(ubicaPeriodicidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnSix())));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRecursos()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPeriodicidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenerico(ubicaRecurso(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnFive())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenericoDos(ubicaPeriodicidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnSix())));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE391(false);
				getSeguimientoSalvaguardaBean().setCodigoRecursos(0);
				getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	

	public void agregarFilasTablaSalvaguardaF411(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTree()), 3));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF411(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF411(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF411 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaF421(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareId()==null){
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTree()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF421(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF421(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF421 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaF431(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoSistemas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareGenerico(ubicaSistema(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnOne())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
			}else
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoSistemas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareGenerico(ubicaSistema(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnOne())));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());			
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF431(false);
			getSeguimientoSalvaguardaBean().setCodigoSistemas(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF431 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaF441(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()), 3));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF441(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF441 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaF452(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(10));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());
			}else
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());			
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF452(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF452(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF452 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaG461(){		
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
				}else
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());			
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG461(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG461 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaG471(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSix())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()), 3));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSix())));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG471(false);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF441 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaG472(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(4));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472());
				}else
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG472());			
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG472(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG472(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG472 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaG481(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoInstitucion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(ubicaInstitucion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaAccion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnSix())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()), 3));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoInstitucion()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccion()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(ubicaInstitucion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne())));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaAccion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnSix())));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG481(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG481(false);
					getSeguimientoSalvaguardaBean().setCodigoAccion(0);
					getSeguimientoSalvaguardaBean().setCodigoTipoInstitucion(0);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}	
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG481 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaG491(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()), 3));						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix())));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG491(false);
					getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(0);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG491 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaG501(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().getTareId()==null){
								
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setTareStatus(true);
					
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
					}else{
						
						
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG501(new TableResponses());
	
					
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG501(false);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG501 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaG512(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaG();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareId()==null){
								
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(26));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFour()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnSix()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFour()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnSix()), 3));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));					
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne())));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());
	
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG512(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG512 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
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

	public void vaciarDatosProvinciaCantonParroquia(){
//		getSeguimientoSalvaguardaBean().setCodigoProvincia("");
//		getSeguimientoSalvaguardaBean().setCodigoCanton("");
//		getSeguimientoSalvaguardaBean().setCodigoParroquia("");
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
	}
	

	public void cargaTodosLosCantones(){
		try{
//			List<Object[]> cantones=getGeographicalLocationsFacade().listarCantones();
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
			datosParaGrabarAvanceEjecucion();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
			List<TableResponses> datosRespuestasTablasCheck=Stream.concat(preparaGrabarComunicaBeneficiariosSalvaguardaB().stream(), preparaGrabarEjecucionProyectoSalvaguardaB().stream()).collect(Collectors.toList());
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().stream(), datosRespuestasTablasCheck.stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().stream()).collect(Collectors.toList());
			
			
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAB.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaB();
			
			if(validaGrabarTabs(2)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(2,1);
//				getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaB " + ": ").append(e.getMessage()));
			e.printStackTrace();
		}
	}
	/**
	 * Grabar informacion salvaguarda C
	 */
	public void grabarSalvaguardaC(){
		try{						
			datosParaGrabarAvanceEjecucion();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
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
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAC.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaC();
		
			if(validaGrabarTabs(3)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(3,1);
//				getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaC " + ": ").append(e.getMessage()));
		}
		
	}
	
	public void preparaInformacionSalvaguardaB(){
		
		
		List<TableResponses> listaComunica=new ArrayList<>();
		List<TableResponses> listaEjecucion=new ArrayList<>();
		getSeguimientoSalvaguardaBean().setCatalogoInformacionComunicaSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucionSeleccionado(new ArrayList<>());
		
		
		getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
		listaComunica= getSeguimientoSalvaguardaBean().getListaPreguntasB().get(25).getTableResponsesList().stream().filter((leyes)->leyes.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId() && leyes.isTareStatus()).collect(Collectors.toList());
		listaEjecucion= getSeguimientoSalvaguardaBean().getListaPreguntasB().get(26).getTableResponsesList().stream().filter((politicas)->politicas.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId() && politicas.isTareStatus()).collect(Collectors.toList());
		listaComunica.stream().forEach(res->{						
			getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
		});
		listaEjecucion.stream().forEach(res->{			
			getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
		});
		
		
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnFour()), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
		});
		
//		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
//		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
//		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().forEach(res->{
			res.setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(res.getTareColumnTree())));
			res.setTareGenericoDos(ubicaConformacion(Integer.parseInt(res.getTareColumnFour())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));;
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(15).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPB(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(27).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPB(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPB().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
	}
	
	/**
	 * prepara informacion salvaguarda C
	 */
	public void preparaInformacionSalvaguardaC(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFive()), 1));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.valueOf(res.getTareColumnTree())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.valueOf(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().forEach(res->{
			
			res.setTareCanton(buscarCanton(Integer.parseInt(res.getTareColumnOne())));
			
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));

		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().forEach(res->{
			
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnTwo())));
			
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().forEach(res->{			
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnOne())));			
		});
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21).getTableResponsesList());
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPC(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(22).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPC(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPC().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
	}
	
	/**
	 * Graba salvaguarda D
	 */
	public void grabarSalvaguardaD(){
		try{
			datosParaGrabarAvanceEjecucion();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331() .stream()).collect(Collectors.toList());
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAD.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaD();
			
			if(validaGrabarTabs(4)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(4,1);
//				getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaC " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Graba la salvaguarda E
	 */
	public void grabarSalvaguardaE(){
		try{
			datosParaGrabarAvanceEjecucion();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391() .stream()).collect(Collectors.toList());
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAE.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaE();
			
			if(validaGrabarTabs(5)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(5,1);
//				getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaE " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Grabar informacion salvaguarda F
	 */
	public void grabarSalvaguardaF(){
		try{
			datosParaGrabarAvanceEjecucion();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441() .stream()).collect(Collectors.toList());
			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452() .stream()).collect(Collectors.toList());
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAF.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaF();
			
			if(validaGrabarTabs(6)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(6,1);
//				getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaF " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * Grabar informacion salvaguarda G
	 */
	public void grabarSalvaguardaG(){
		try{
			datosParaGrabarAvanceEjecucion();
			List<TableResponses> respuestasTabla=new ArrayList<>();			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());			
			
			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452() .stream()).collect(Collectors.toList());
//			respuestasTabla = getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461();
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAG.getCodigo());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaG();
			
			if(validaGrabarTabs(7)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(7,1);
//				getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaG " + ": ").append(e.getMessage()));
		}
		
	}
	/**
	 * prepara informacion salvaguarda D
	 */
	public void preparaInformacionSalvaguardaD(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnSeven())));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().forEach(res->{
			res.setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(res.getTareColumnTwo())));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPD(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPD(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPD().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		vaciarDatosProvinciaCantonParroquia();
	}
	
	/**
	 * prepara informacion salvaguarda E
	 */
	public void preparaInformacionSalvaguardaE(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().forEach(res->{
			res.setTareGenerico(buscaNivelOrganizacion(res.getTareColumnOne()));
			res.setTareGenericoDos(ubicaHerramienta(Integer.parseInt(res.getTareColumnTwo())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaMetodo(Integer.valueOf(res.getTareColumnSix())));
			res.setTareGenericoDos(ubicaPublico(Integer.valueOf(res.getTareColumnSeven())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(9).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaServicio(Integer.parseInt(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(11).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaRecurso(Integer.parseInt(res.getTareColumnFive())));
			res.setTareGenericoDos(ubicaPeriodicidad(Integer.parseInt(res.getTareColumnSix())));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPE(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPE(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPE().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
//		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
//		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		

	}
	/**
	 * Prepara informacion salvaguardaF
	 */
	public void preparaInformacionSalvaguardaF(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 1));
			res.setTareGenerico(buscaCatalogoMedidaTomada(Integer.parseInt(res.getTareColumnTwo())));
		});		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().forEach(res->{
			res.setTareGenerico(ubicaSistema(Integer.parseInt(res.getTareColumnOne())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPF(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(11).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPF(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPF().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		vaciarDatosProvinciaCantonParroquia();
	}
	/**
	 * Prepara informacion salvaguarda G
	 */
	public void preparaInformacionSalvaguardaG(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));

		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnFour()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnSix())));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFour()), 3));
			res.setTareGenerico(ubicaInstitucion(Integer.valueOf(res.getTareColumnOne())));
			res.setTareGenericoDos(ubicaAccion(Integer.valueOf(res.getTareColumnSix())));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(res.getTareColumnSix())));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(26).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList())); 
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFour()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFive()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnSix()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.valueOf(res.getTareColumnOne())));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().forEach(res->{
//			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 1));
//			res.setTareGenerico(buscaCatalogoMedidaTomada(Integer.parseInt(res.getTareColumnTwo())));
//		});		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5).getTableResponsesList());
//		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().forEach(res->{
//			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
//			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
//			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
//		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPG(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(36).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPG(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPG().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
		vaciarDatosProvinciaCantonParroquia();
	}
	/**
	 * Metodo que recarga la informacion de la salvaguarda luego de grabar.
	 */
	public void recargaPreguntasRespuestasSalvaguardas(){
		List<Integer> lista=new ArrayList<>();
		for (ProjectsSafeguards salvaguarda : getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()) {
				lista.add(salvaguarda.getSafeguards().getSafeId());
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
					getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.LEYES.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
						}						
					}
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
					int salvaguardaB=salvaguarda.getSafeId();
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
						}						
					}
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
					int salvaguardaC=salvaguarda.getSafeId();


					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}						
					}

					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
					int salvaguardaD=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasD().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasD().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
						}						
					}
				}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
					int salvaguardaE=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaE).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntasE().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaE).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaPreguntasE().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
						}						
					}
				}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
					int salvaguardaF=salvaguarda.getSafeId();
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setListaCatalogoMedidaTomada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MEDIDATOMADA.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaF).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntasF().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaF).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaPreguntasF().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
						}						
					}
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
					int salvaguardaG=salvaguarda.getSafeId();
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoRemoto(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOREMOTO.getCodigo()));
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoInSitu(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
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
							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
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
	 * 
	 * @param codigo del catalogo
	 * @param tipo 1 provincia, 2 canton , 3 parroquia
	 * @return
	 */
//	public String buscarProvinciaCantonParroquia(String codigo,int tipo){
//		GeographicalLocations catalogo;
//		String respuesta="";
//		try{			
//			if (tipo==1){
//				catalogo = getSeguimientoSalvaguardaBean().getListaProvincias().stream().filter(prov->prov.getGeloCodificationInec().equals(codigo)).findFirst().get();
//				getSeguimientoSalvaguardaBean().setCodigoProvincia(catalogo.getGeloCodificationInec());
//				filtraCanton();
//				respuesta=catalogo.getGeloName();
//			}else if(tipo==2){
//				catalogo = getSeguimientoSalvaguardaBean().getListaCantones().stream().filter(can->can.getGeloCodificationInec().equals(codigo)).findFirst().get();
//				getSeguimientoSalvaguardaBean().setCodigoCanton(catalogo.getGeloCodificationInec());
//				filtraParroquia();
//				respuesta = catalogo.getGeloName();
//			}else if(tipo==3){
//				catalogo = getSeguimientoSalvaguardaBean().getListaParroquias().stream().filter(parr->parr.getGeloCodificationInec().equals(codigo)).findFirst().get();
//				respuesta = catalogo.getGeloName();
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return respuesta;
//	}
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

		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTwo()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTree()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()));
	}
	
	public void editarTablaB81(){

		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne()));
		
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTree()));
	}
	
	public void editarTablaB102(){

		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive()));
	}
	public void editarTablaB121(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFour()));
	}
	
	public void editarTablaC201(){		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFive()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTree()));
	}
	public void editarTablaC211(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFive()));
	}
	public void editarTablaC271(){				
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTwo()));
	}
	public void editarTablaC28(){				
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne()));
	}

	public void editarTablaC291(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive()));
	}
	
	public void editarTablaC293(){
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().getTareColumnOne()));		
	}
	
	public void editarTablaC311(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnTree()));
	}
	public void editarTablaD321(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnSeven()));
	}
	
	public void editarTablaD331(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo()));
	}
	
	public void editarTablaE341(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree()));
	}
	
	public void editarTablaE351(){
		getSeguimientoSalvaguardaBean().setCodigoNivelProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnOne());	
		getSeguimientoSalvaguardaBean().setCodigoHerramienta(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnTwo()));
	}
	public void editarTablaE361(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive()));
	}
	public void editarTablaE371(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoMetodo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSix()));
		getSeguimientoSalvaguardaBean().setCodigoPublico(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnSeven()));
	}
	public void editarTablaE381(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoServicio(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnFive()));
	}

	public void editarTablaE391(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoRecursos(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnFive()));
		getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnSix()));
	}
	public void editarTablaF411(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTree()));
	}
	public void editarTablaF421(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTree()));				
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTwo()));		
	}
	public void editarTablaF431(){
		getSeguimientoSalvaguardaBean().setCodigoSistemas(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnOne()));
	}
	public void editarTablaF441(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()));
	}
		
	public void editarTablaG471(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSix()));
	}
	public void editarTablaG481(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()));
		getSeguimientoSalvaguardaBean().setCodigoTipoInstitucion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne()));
		getSeguimientoSalvaguardaBean().setCodigoAccion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnSix()));
	}
	public void editarTablaG491(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix()));
		
	}
	
	public void editarTablaG512(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFour()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()));
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne()));
	}
	
	public void siguienteTab(int salvaguarda){
		if(!(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual()==1)){
			
			if(salvaguarda==1 ){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(1)){
					grabarSalvaguardaA();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}else if(salvaguarda==2 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB()){
					grabarSalvaguardaB();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}else if(salvaguarda==3 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC()){
					grabarSalvaguardaC();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}else if(salvaguarda==4 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD()){
					grabarSalvaguardaD();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}else if(salvaguarda==5 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE()){
					grabarSalvaguardaE();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}else if(salvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF()){
					grabarSalvaguardaF();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}else if(salvaguarda==7 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG()){
					grabarSalvaguardaG();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}
		}
//		boolean encontrado=false;
//		Iterator it = getSeguimientoSalvaguardaBean().getMapaTabs().keySet().iterator();
//		while(it.hasNext()){
//		  Integer key = (Integer) it.next();		  
//		  if(key==salvaguarda){
//			  encontrado=true;
//			  break;
//		  }
//		}
//		if(encontrado==false)
//			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"));
//		else
//			getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
		
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
	//////Nuevo codigo
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
			if (getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(24).isVaanYesnoAnswerValue()==false &&
					(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(25).getVaanTextAnswerValue().length()>0 )){
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
					System.out.println(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().size());
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
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(24).setVaanYesnoAnswerValue(true);
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
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B71")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B81")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81()) && getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
			if ((validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102()) || validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103())) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(12).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(13).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
			try{
				getSeguimientoSalvaguardaBean().setListaTempPregunta141(getTableResponsesFacade().buscaPreguntas14_1_2(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				if (getSeguimientoSalvaguardaBean().getListaTempPregunta141().size()>0 
						&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(16).isVaanYesnoAnswerValue()==false){
					Mensaje.verDialogo("dlgEliminaDatosTabla");
				}else{
					getSeguimientoSalvaguardaBean().setCatalogoInformacionComunicaSeleccionado(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucionSeleccionado(new ArrayList<>());
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
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue()==false
					|| getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).getVaanTextAnswerValue().length()>0){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C26")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C271")){			
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C291")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(8).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C293")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(9).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C301")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(10).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C311")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(11).isVaanYesnoAnswerValue()==false){
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
					|| getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(5).getVaanTextAnswerValue().length()>0){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471()) || validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472()) 
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
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB41");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B51")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB51");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B71")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB71");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B81")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB81");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102());
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB102");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB103");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB121");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB131");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getListaTempPregunta141());				
				getSeguimientoSalvaguardaBean().setCatalogoInformacionComunicaSeleccionado(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucionSeleccionado(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelInformacionComunica");
				Mensaje.actualizarComponente(":form:salvaguardas:panelInformaEjecucion");	
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C201")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla201C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C211")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				eliminaInformacionPregunta21123();
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla211C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C241")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				eliminaInformacionPregunta241();
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla241C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C26")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla26C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C271")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla271C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C291")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla291C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C293")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla293C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C301")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla301C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C311")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla311C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D321")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla321D");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("D331")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla331D");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E341")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla341E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E351")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla351E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E361")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla361E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E371")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla371E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E381")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla381E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E391")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla391E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F411")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla411F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F421")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla421F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F431")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla431F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F441")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla451F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F452")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452());	
				eliminaInformacionPreguntaF451();
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla461G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla461G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471());				
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla471G");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla472G");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G481")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481());								
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla481G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G491")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491());								
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla491G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G501")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501());								
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla501G");				
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
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB7"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB102");
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB103");
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(12).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB9"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB121");
			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(13).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB10"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB131");
			
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(16).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSB11");
			Mensaje.actualizarComponente(":form:salvaguardas:panelInformaEjecucion"); 
			Mensaje.actualizarComponente(":form:salvaguardas:panelInformacionComunica");
			
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
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC3"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC241");
			Mensaje.actualizarComponente(":form:salvaguardas:txtC241");
									
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C26")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC4"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC26");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C271")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC5"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC271");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C291")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(8).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC6"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC291");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C293")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(9).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC7"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC293");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C301")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(10).setVaanYesnoAnswerValue(true);
			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC8"); 
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC301");												
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C311")){
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(11).setVaanYesnoAnswerValue(true);
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
			Mensaje.actualizarComponente(":form:salvaguardas:txtF451");
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
			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG472");
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
		}
	}
	
	public void eliminaInformacionPregunta21123(){
		try{
			ValueAnswers nuevaFila1=new ValueAnswers();
			ValueAnswers nuevaFila2=new ValueAnswers();
			nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			nuevaFila1.setVaanStatus(true);
			nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(4));				
			nuevaFila2.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			nuevaFila2.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(5));
			nuevaFila2.setVaanStatus(true);
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).setVaanStatus(false);
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).setVaanStatus(false);
			getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
			getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
			getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1));
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
			Mensaje.actualizarComponente(":form:salvaguardas:txtC212");
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
			Mensaje.actualizarComponente(":form:salvaguardas:txtC213");
			getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
			getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila2);
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(nuevaFila1);
			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(nuevaFila2);				
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC2");
			
		}catch(Exception e){
			e.printStackTrace();
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
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaHerramientas().stream().filter((p)->p.getCataId().equals(codigoHerramienta)).findFirst();
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
	
	public String ubicaServicio(int codigo){
		String servicio="";
		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaServicio().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			servicio=catalogo.getCataText1();
		}
		return servicio;
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
		}else{
			tipo = TipoInstitucionEnum.EJERCITO.getEtiqueta();
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
	
	public boolean validaDatosOpcionSiSalvaguardaB(){
		boolean informacionCompleta=true;
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue() && (
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).getVaanTextAnswerValue().trim().length()==0 ||
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).getVaanTextAnswerValue().trim().length()==0)){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().size()==0 && getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue() && (
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(11).getVaanTextAnswerValue().trim().length()==0 ||
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(12).getVaanTextAnswerValue().trim().length()==0||
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(13).getVaanTextAnswerValue().trim().length()==0)){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(14).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(15).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(16).isVaanYesnoAnswerValue() && (
					getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().size()==0 || getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado().size()==0 )){
				informacionCompleta=false;
			}
		
		return informacionCompleta;
	}
	public boolean validaDatosOpcionSiSalvaguardaC(){
		boolean informacionCompleta=true;
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().size()==0 && (
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).getVaanTextAnswerValue().trim().length()==0 ||
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).getVaanTextAnswerValue().trim().length()==0)){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().size()==0 && (
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).getVaanTextAnswerValue().trim().length()==0 )){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(8).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(9).isVaanYesnoAnswerValue() && 
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(10).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(11).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().size()==0 ){
				informacionCompleta=false;
			}
		
		return informacionCompleta;
	}
	
	public boolean validaDatosOpcionSiSalvaguardaD(){
		boolean informacionCompleta=true;
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().size()==0 ){
				informacionCompleta=false;
			}		
		return informacionCompleta;
	}
	public boolean validaDatosOpcionSiSalvaguardaE(){
		boolean informacionCompleta=true;
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().size()==0 ){
				informacionCompleta=false;
			}		
		return informacionCompleta;
	}
	
	public boolean validaDatosOpcionSiSalvaguardaF(){
		boolean informacionCompleta=true;
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452().size()==0 &&
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(5).getVaanTextAnswerValue().trim().length()==0){
				informacionCompleta=false;
			}	
		return informacionCompleta;
	}
	
	public boolean validaDatosOpcionSiSalvaguardaG(){
		boolean informacionCompleta=true;
		
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().size()==0 &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472().size()==0){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().size()==0 ){
				informacionCompleta=false;
			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(24).isVaanYesnoAnswerValue() &&
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().size()==0 &&
					getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(25).getVaanTextAnswerValue().trim().length()==0){
				informacionCompleta=false;
			}	
		return informacionCompleta;
	}
	
	public void grabarResumenEjecutivo(){
		try{
			getSeguimientoSalvaguardaBean().getResumenEjecutivo().setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getExecutiveSummarieFacade().grabarResumenEjecutivo(getSeguimientoSalvaguardaBean().getResumenEjecutivo());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void prueba(){
		
//		Iterator itera=getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().iterator();
//		while (itera.hasNext()){
//			System.out.println(itera.next());
//		}
		try{
			getSeguimientoSalvaguardaBean().setListaTempPregunta141(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaTempPregunta141(getTableResponsesFacade().buscaPreguntas14_1_2(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
			if(getSeguimientoSalvaguardaBean().getListaTempPregunta141().size()>0){
				System.out.println("Mostrar mensaje error");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
