package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
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
	
	
	private Users usuario;
	
	@PostConstruct
	public void init(){
		try{
			vaciarValores();			
			cargarProyectos();
			cargaSalvaguardasPlanAccion();
			cargaSectoresInteres();
			usuario=new Users();
			usuario.setUserName("Christian Báez");
			usuario.setUserId(1);
			getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
			getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
			cargaProvincias();
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
					informacionFormularioPorSalvaguarda(getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto());
					
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
			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().add(as.getSectors().getSectId().toString());
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
			getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(getProjectsSafeguardsFacade().buscarProyectoPorCobeneficio(getSeguimientoSalvaguardaBean().getCodigoProyecto()));
			if (getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null){
				getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(getProjectsSafeguardsFacade().buscarProyectosPorRiesgo(getSeguimientoSalvaguardaBean().getCodigoProyecto()));
			}
			if(getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion()==null || getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
			}else{
				for(ProjectsSafeguards salvaguarda:getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()){
					if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaA(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==2 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaB(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==3 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaC(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==4 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaD(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==5 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaE(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==6 && salvaguarda.getSafeguards().getSafeParentId()==null){
						getSeguimientoSalvaguardaBean().setSalvaguardaF(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==7 && salvaguarda.getSafeguards().getSafeParentId()==null){
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
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(getSeguimientoSalvaguardaBean().getCodigoProyecto()));	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()==null){
				AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
				avanceEjecucion.setAdexIsReported(false);
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(avanceEjecucion);
				
			}else{
				sectoresInteresProyecto();
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
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==2){
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
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==3){
					int salvaguardaC=salvaguarda.getSafeId();
					cargaTodosLosCantones();
					
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
					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==4){
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
					//SALVAGUARDA E
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==5){
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

					//SALVAGUARDA F
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==6){
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
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==7){
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
		if(codigoSalvaguarda==1){
			if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda))				
					Mensaje.verDialogo("dlgGrabaReporteA");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
		}else if(codigoSalvaguarda==2){	
			if(validaDatosAvanceEjecucion())
				Mensaje.verDialogo("dlgGrabaReporteB");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
		}else if(codigoSalvaguarda==3){
			if(validaDatosAvanceEjecucion())				
				Mensaje.verDialogo("dlgGrabaReporteC");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			
		}else if(codigoSalvaguarda==4){
			if(validaDatosAvanceEjecucion())				
				Mensaje.verDialogo("dlgGrabaReporteD");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
		}else if(codigoSalvaguarda==5){
			if(validaDatosAvanceEjecucion())				
				Mensaje.verDialogo("dlgGrabaReporteE");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
		}else if(codigoSalvaguarda==6){
			if(validaDatosAvanceEjecucion())				
				Mensaje.verDialogo("dlgGrabaReporteF");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
		}else if(codigoSalvaguarda==7){
			if(validaDatosAvanceEjecucion())				
				Mensaje.verDialogo("dlgGrabaReporteG");			
			else
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
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
				List<TableResponses> datosRespuestasTabla=Stream.concat(preparaGrabarLeyesSalvaguardaA().stream(), preparaGrabarPoliticasSalvaguardaA().stream()).collect(Collectors.toList());
				
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
				
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().stream().forEach(datos->{
					datos.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));
					datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().add(datos);
				});
				List<TableResponses> datosTablaConcatenados= Stream.concat(datosRespuestasTabla.stream(), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().stream()).collect(Collectors.toList());
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(datosTablaConcatenados);
				getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(),TipoSalvaguardaEnum.SALVAGUARDAA.getCodigo());
				recargaPreguntasRespuestasSalvaguardas();
				preparaInformacionSalvaguardaA();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
			
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaA " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Configura los datos de avanceEjecucion y avanceSectores para grabar/actualizar
	 */
	public void datosParaGrabarAvanceEjecucion(){
		Projects proyecto=new Projects();
		proyecto.setProjId(getSeguimientoSalvaguardaBean().getCodigoProyecto());
		Users usuario=new Users();
		usuario.setUserId(1);
		usuario.setUserName("Christian Baez");
		List<AdvanceSectors> listaAvanceSectores=new ArrayList<>();
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjects(proyecto);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setUsers(usuario);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreatorUser(usuario.getUserName());
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreationDate(new Date());
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexDropState(true);

		for (String sectores : getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados()) {
			Sectors sector=new Sectors();
			sector.setSectId(Integer.parseInt(sectores));
			AdvanceSectors avanceSectores=new AdvanceSectors();
			avanceSectores.setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			avanceSectores.setSectors(sector);
			listaAvanceSectores.add(avanceSectores);
		}
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdvanceSectorsList(listaAvanceSectores);
	}

	public List<TableResponses> preparaGrabarLeyesSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		for(String catalogo:getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(catalogo);
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuestasTabla.add(respuesta);
		}
		return respuestasTabla;
	}
	public List<TableResponses> preparaGrabarPoliticasSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		for(String catalogo:getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(catalogo);
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3));
			respuestasTabla.add(respuesta);
		}
		return respuestasTabla;
	}
	public List<TableResponses> preparaGrabarComunicaBeneficiariosSalvaguardaB(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		
		for(String catalogo:getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(catalogo);
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(25));
			respuestasTabla.add(respuesta);
		}
		return respuestasTabla;
	}
	
	public List<TableResponses> preparaGrabarEjecucionProyectoSalvaguardaB(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		for(String catalogo:getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(catalogo);
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(26));
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
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaA(new ArrayList<>());
		cargaCatalogoPlanGobierno();
		getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
		listaLeyes= getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1).getTableResponsesList().stream().filter((leyes)->leyes.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaPoliticas= getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3).getTableResponsesList().stream().filter((politicas)->politicas.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaLeyes.stream().forEach(res->{						
			getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(String.valueOf(res.getTareColumnOne()));
		});
		listaPoliticas.stream().forEach(res->{			
			getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().add(String.valueOf(res.getTareColumnOne()));
		});
		List<TableResponses> tablaAux=new ArrayList<>();
		tablaAux=getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		tablaAux.stream().forEach(res->{
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(res);
		});	
				
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().forEach(res->{
			res.setTareCatPlanGobierno(buscaCatalogoPlanGobierno(Integer.parseInt(res.getTareColumnOne())));			
		});
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
	
	public void cargaCatalogoPlanGobierno(){
		try{
			getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PLANPROYECTO.getCodigo()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean validaDatosAvanceEjecucion(){
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexRegisterDate()==null || getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().size()==0){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosAvanceEjecucion"));			
			return false;
		}else{
			return true;
		}
	}
	
	public boolean validaCamposTablasSalvaguardas(int salvaguarda){
		if (salvaguarda==1){
			for (TableResponses respuesta : getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()) {
				if(respuesta.getTareColumnOne()==null)
					return false;
				else{
					if(respuesta.getTareColumnOne().trim().equals("") || respuesta.getTareColumnTwo().trim().equals("")){
						return false;						
					}
				}
			}
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
	
	public void filtraCanton(){
		try{
			
			List<Object[]> cantones=getGeographicalLocationsFacade().listarCantonesPorProvincia(getSeguimientoSalvaguardaBean().getCodigoProvincia());
			
			getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
			for (Object[] registro : cantones) {
				GeographicalLocations catalogo=new GeographicalLocations();
				catalogo.setGeloName(registro[0].toString());
				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
				catalogo.setGeloCodificationInec(registro[2].toString());
				getSeguimientoSalvaguardaBean().getListaCantones().add(catalogo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void filtraParroquia(){
		try{
			List<Object[]> parroquia=getGeographicalLocationsFacade().listarParroquiasPorCanton(getSeguimientoSalvaguardaBean().getCodigoCanton());
			getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
			for (Object[] registro : parroquia) {
				GeographicalLocations catalogo=new GeographicalLocations();
				catalogo.setGeloName(registro[0].toString());
				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
				catalogo.setGeloCodificationInec(registro[2].toString());
				getSeguimientoSalvaguardaBean().getListaParroquias().add(catalogo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
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
		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
	}
	public void inicializaNuevoRegistroTablasD321(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaParroquias(new ArrayList<>());
	}

	public void agregarFilasTablaSalvaguardaB121(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());				
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB121(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
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
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC201(false);
			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC211(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC211(false);
			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
			getSeguimientoSalvaguardaBean().setCodigoCanton("");
			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
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

				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC271(false);
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

				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC28(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC28(false);
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC291(false);
			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
			getSeguimientoSalvaguardaBean().setCodigoCanton("");
			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodigoCanton()));					
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoCanton());

				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodigoCanton()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC293(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC293(false);

			getSeguimientoSalvaguardaBean().setCodigoCanton("");
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
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodigoCanton()));
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
			}else{			
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC311(false);
			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
			getSeguimientoSalvaguardaBean().setCodigoCanton("");
			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaD321(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareId()==null){
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnOne(), 1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTwo(), 2));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTree(), 3));
			getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());

			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
			getSeguimientoSalvaguardaBean().setCodigoProvincia("");
			getSeguimientoSalvaguardaBean().setCodigoCanton("");
			getSeguimientoSalvaguardaBean().setCodigoParroquia("");
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD321(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD321 " + ": ").append(e.getMessage()));
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
	}
	
	/**
	 * Permite agregar nueva fila a la tabla D331
	 */
	public void nuevaFilaTablaSalvaguardaD321(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD321(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestas().setTareColumnEight("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestas().setTareColumnTen("0");
	}
	
	public void nuevaFilaTablaSalvaguardaE351(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());		
	}
	
	public void nuevaFilaTablaSalvaguardaE361(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
	}
	
	public void nuevaFilaTablaSalvaguardaE371(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
	}
	public void nuevaFilaTablaSalvaguardaE381(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
	}
	public void nuevaFilaTablaSalvaguardaE391(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());

		vaciarDatosProvinciaCantonParroquia();
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
	}
	public void nuevaFilaTablaSalvaguardaG472(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG472(new TableResponses());		
	}
	public void nuevaFilaTablaSalvaguardaG481(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG481(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
	}
	public void nuevaFilaTablaSalvaguardaG491(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
	}
	public void nuevaFilaTablaSalvaguardaG501(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG501(new TableResponses());
		
	}
	
	public void nuevaFilaTablaSalvaguardaG512(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoParticipante(0);
	}
	
	/**
	 * Agrega filas a la tabla D331
	 */
	public void agregarFilasTablaSalvaguardaD331(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3));			
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodigoCanton()));
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
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne(), 1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo(), 2));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree(), 3));
			getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());

			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
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
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenerico(buscaNivelOrganizacion(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE351(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE351 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaE361(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTree(), 3));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTree(), 3));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE371(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE371 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaE381(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(9));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree(), 3));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE381(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE391(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(11));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTree(), 3));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE391(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	

	public void agregarFilasTablaSalvaguardaF411(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTree(), 3));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTree(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoProvincia());
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
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
			}else
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());			
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF431(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF431 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaF441(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree(), 3));
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoParroquia());
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(getSeguimientoSalvaguardaBean().getCodigoParroquia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour(), 3));
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoProvincia(), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoCanton(), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscarProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodigoParroquia(), 3));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoProvincia());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoCanton());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(getSeguimientoSalvaguardaBean().getCodigoParroquia());
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
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()), 3));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG481(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG481(false);
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()), 1));
					
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()), 1));
						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG491(false);
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
								
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(ubicaDatosParticipante(getSeguimientoSalvaguardaBean().getCodigoTipoParticipante()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(26));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFour()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoParticipante()));
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoParticipante()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(ubicaDatosParticipante(getSeguimientoSalvaguardaBean().getCodigoTipoParticipante()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()), 3));
						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());
	
				vaciarDatosProvinciaCantonParroquia();
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
			List<Object[]> cantones=getGeographicalLocationsFacade().listarCantones();			
			getSeguimientoSalvaguardaBean().setListaTodosCantones(new ArrayList<>());			
			for (Object[] registro : cantones) {
				GeographicalLocations catalogo=new GeographicalLocations();
				catalogo.setGeloName(registro[0].toString());
				catalogo.setGeloId(Integer.valueOf(registro[1].toString()));
				catalogo.setGeloCodificationInec(registro[2].toString());
				getSeguimientoSalvaguardaBean().getListaTodosCantones().add(catalogo);
			}
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaC " + ": ").append(e.getMessage()));			
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
		listaComunica= getSeguimientoSalvaguardaBean().getListaPreguntasB().get(25).getTableResponsesList().stream().filter((leyes)->leyes.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaEjecucion= getSeguimientoSalvaguardaBean().getListaPreguntasB().get(26).getTableResponsesList().stream().filter((politicas)->politicas.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaComunica.stream().forEach(res->{						
			getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().add(String.valueOf(res.getTareColumnOne()));
		});
		listaEjecucion.stream().forEach(res->{			
			getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado().add(String.valueOf(res.getTareColumnOne()));
		});
		
		
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnFour()), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(15).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23).getTableResponsesList());
	}
	
	/**
	 * prepara informacion salvaguarda C
	 */
	public void preparaInformacionSalvaguardaC(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnFive(), 1));			
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().forEach(res->{
			
			res.setTareCanton(buscarCanton(res.getTareColumnOne()));
			
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19).getTableResponsesList());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21).getTableResponsesList());
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
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);

	}
	
	/**
	 * prepara informacion salvaguarda E
	 */
	public void preparaInformacionSalvaguardaE(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().forEach(res->{
			res.setTareGenerico(buscaNivelOrganizacion(res.getTareColumnOne()));			
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(9).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(11).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(null);
		getSeguimientoSalvaguardaBean().setCodigoCanton(null);
		getSeguimientoSalvaguardaBean().setCodigoParroquia(null);

	}
	/**
	 * Prepara informacion salvaguardaF
	 */
	public void preparaInformacionSalvaguardaF(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 1));
			res.setTareGenerico(buscaCatalogoMedidaTomada(Integer.parseInt(res.getTareColumnTwo())));
		});		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5).getTableResponsesList());
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnOne(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(10).getTableResponsesList());
		
		vaciarDatosProvinciaCantonParroquia();
	}
	/**
	 * Prepara informacion salvaguarda G
	 */
	public void preparaInformacionSalvaguardaG(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1).getTableResponsesList());

		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().forEach(res->{
			res.setTareProvincia(buscarProvinciaCantonParroquia(res.getTareColumnTwo(), 1));
			res.setTareCanton(buscarProvinciaCantonParroquia(res.getTareColumnTree(), 2));
			res.setTareParroquia(buscarProvinciaCantonParroquia(res.getTareColumnFour(), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(4).getTableResponsesList());
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFour()), 3));
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8).getTableResponsesList());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			
		});
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10).getTableResponsesList());
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(26).getTableResponsesList()); 
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFour()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnFive()), 3));
			res.setTareGenerico(ubicaDatosParticipante(Integer.valueOf(res.getTareColumnOne())));
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
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==2){
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
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==3){
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
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==4){
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
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==5){
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
	public String buscarProvinciaCantonParroquia(String codigo,int tipo){
		GeographicalLocations catalogo;
		String respuesta="";
		try{			
			if (tipo==1){
				catalogo = getSeguimientoSalvaguardaBean().getListaProvincias().stream().filter(prov->prov.getGeloCodificationInec().equals(codigo)).findFirst().get();
				getSeguimientoSalvaguardaBean().setCodigoProvincia(catalogo.getGeloCodificationInec());
				filtraCanton();
				respuesta=catalogo.getGeloName();
			}else if(tipo==2){
				catalogo = getSeguimientoSalvaguardaBean().getListaCantones().stream().filter(can->can.getGeloCodificationInec().equals(codigo)).findFirst().get();
				getSeguimientoSalvaguardaBean().setCodigoCanton(catalogo.getGeloCodificationInec());
				filtraParroquia();
				respuesta = catalogo.getGeloName();
			}else if(tipo==3){
				catalogo = getSeguimientoSalvaguardaBean().getListaParroquias().stream().filter(parr->parr.getGeloCodificationInec().equals(codigo)).findFirst().get();
				respuesta = catalogo.getGeloName();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return respuesta;
	}
	/**
	 * Busca el nombre del canton
	 * @param codigo
	 * @return
	 */
	public String buscarCanton(String codigo){
		GeographicalLocations catalogo;
		String respuesta="";
		try{			
			
				catalogo = getSeguimientoSalvaguardaBean().getListaTodosCantones().stream().filter(can->can.getGeloCodificationInec().equals(codigo)).findFirst().get();
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
		System.out.println(getSeguimientoSalvaguardaBean().getCodProvincia());
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
	
	public void editarTablaC201(){		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFive());		
	}
	public void editarTablaC211(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnTree());
	}
	public void editarTablaC291(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnTree());
	}
	
	public void editarTablaC293(){
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().getTareColumnOne());		
	}
	
	public void editarTablaC311(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnTree());
	}
	public void editarTablaD321(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTree());
	}
	public void editarTablaE341(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree());
	}
	
	public void editarTablaE351(){
		getSeguimientoSalvaguardaBean().setCodigoNivelProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnOne());			
	}
	public void editarTablaE361(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnTree());
	}
	public void editarTablaE371(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnTree());
	}
	public void editarTablaE381(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree());
	}

	public void editarTablaE391(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnTree());
	}
	public void editarTablaF411(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnTree());
	}
	public void editarTablaF421(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTree());				
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTwo()));		
	}
	public void editarTablaF441(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree());
	}
		
	public void editarTablaG471(){
		
		getSeguimientoSalvaguardaBean().setCodigoProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo());
		filtraCanton();		
		getSeguimientoSalvaguardaBean().setCodigoCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree());
		filtraParroquia();
		getSeguimientoSalvaguardaBean().setCodigoParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour());
	}
	public void editarTablaG481(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()));
	}
	public void editarTablaG491(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()));
		
	}
	
	public void editarTablaG512(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFour()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()));
		getSeguimientoSalvaguardaBean().setCodigoTipoParticipante(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne()));
	}
	
	public void siguienteTab(){
		
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
				getSeguimientoSalvaguardaBean().setCodigoProvincia(catalogo.getGeloCodificationInec());
				filtraCanton();
				respuesta=catalogo.getGeloName();
			}else if(tipo==2){
				catalogo = getSeguimientoSalvaguardaBean().getListaCantones().stream().filter(can->can.getGeloId().equals(codigo)).findFirst().get();
				getSeguimientoSalvaguardaBean().setCodigoCanton(catalogo.getGeloCodificationInec());
				filtraParroquia();
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
	
}
