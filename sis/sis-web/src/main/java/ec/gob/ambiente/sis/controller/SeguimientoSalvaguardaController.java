package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	
	
//	private boolean tienePreguntasRespuestas;
	private Users usuario;
	
	@PostConstruct
	public void init(){
		try{
			vaciarValores();			
			cargarProyectos();
			cargaSalvaguardasPlanAccion();
			cargaSectoresInteres();
//			tienePreguntasRespuestas=false;
			usuario=new Users();
			usuario.setUserName("Christian Báez");
			usuario.setUserId(1);
			getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
			getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
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
					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
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
					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
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
				}
			}
		}catch(Exception e){
			e.printStackTrace();
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
				valoresRespuestas.setVaanRemoveState(true);
				valoresRespuestas.setVaanNumericAnswerValue(0);
				valoresRespuestas.setVaanTextAnswerValue("");
				valoresRespuestas.setVaanYesnoAnswerValue(false);
				listaRespuestas.add(valoresRespuestas);
			}
		}
	}

		
	/**
	 * Muestra el cuadro de dialogo para grabar el avnace de ejecucion
	 */
	public void mostrarDialogoGrabarSalvaguardaA(int codigoSalvaguarda){
		if(codigoSalvaguarda==1){
			if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda))				
					Mensaje.verDialogo("dlgGrabaReporteA");			
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
		}
	}
	
	
	/**
	 * Agrega una fila a primera tabla de la salvaguarda A
	 */
	public void agregaFilaTablaSalvaguardaA(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(respuestaTabla);
			}
		}
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
					datos.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntas().get(4));
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

	/**
	 * Prepara la informacion para mostrar en el formulario de acuerdo a las salvaguardas del proyecto
	 * @param salvaguardasProyecto
	 */
	public void informacionFormularioPorSalvaguarda(List<ProjectsSafeguards> salvaguardasProyecto){
		for (ProjectsSafeguards salvaguarda : salvaguardasProyecto) {
			if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==1 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaA();
			}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==3 && salvaguarda.getSafeguards().getSafeParentId()==null){
				preparaInformacionSalvaguardaC();
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
		}
//		int contador=0;
//		List<Integer> listaPosiciones=new ArrayList<>();
//		if(getSeguimientoSalvaguardaBean().getRegistroTabla()==null){
//			for (TableResponses tabla : tablaEvaluar) {
//				if(tabla.getTareId()==null && (tabla.getTareColumnOne()==null ||  tabla.getTareColumnTwo()==null)){			
//					listaPosiciones.add(contador);
//				}
//				contador++;
//			}
//			for (Integer posicion : listaPosiciones) 			
//				tablaEvaluar.remove(posicion.intValue());
//		}else 
		if(getSeguimientoSalvaguardaBean().getRegistroTabla().getTareId()==null){
			tablaEvaluar.remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
		}else{
			Mensaje.verDialogo("dlgEliminaItemTablaA");
		}
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaA(tablaEvaluar);
	}
	
	public void eliminaRegistroTabla(int tablaRespuesta){
		try{
			switch(tablaRespuesta){
				case 1:
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
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
	
	public void eliminaItemTablaA(){
		
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
	
	public void filtraCanton(String codigoProvincia){
		try{
			
			List<Object[]> cantones=getGeographicalLocationsFacade().listarCantonesPorProvincia(codigoProvincia);
			
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
	public void filtraParroquia(String codigoCanton){
		try{
			List<Object[]> parroquia=getGeographicalLocationsFacade().listarParroquiasPorCanton(codigoCanton);
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
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));	
			respuestaTabla.setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoProvincia());
			respuestaTabla.setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoCanton());
			respuestaTabla.setTareColumnFour(getSeguimientoSalvaguardaBean().getCodigoParroquia());
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().add(respuestaTabla);

		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTree()==null || fila.getTareColumnNine()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));	
				respuestaTabla.setTareColumnTwo(getSeguimientoSalvaguardaBean().getCodigoProvincia());
				respuestaTabla.setTareColumnTree(getSeguimientoSalvaguardaBean().getCodigoCanton());
				respuestaTabla.setTareColumnFour(getSeguimientoSalvaguardaBean().getCodigoParroquia());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().add(respuestaTabla);

			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaB51(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	public void agregarFilasTablaSalvaguardaB71(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaB81(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaB102(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaB103(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(15));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(15));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}

	public void agregarFilasTablaSalvaguardaB121(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaB131(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC201(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnOne().isEmpty() || fila.getTareColumnTwo()==null || fila.getTareColumnTwo().isEmpty()){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC211(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC241(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC26(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC271(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC28(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC291(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC293(){
		boolean existenulo=false;
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC301(){
		boolean existenulo=false;		
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
	}
	
	public void agregarFilasTablaSalvaguardaC311(){
		boolean existenulo=false;		
		if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().size()==0){
			TableResponses respuestaTabla=new TableResponses();
			respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21));		
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().add(respuestaTabla);
		}else{
			for (TableResponses fila : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311()) {
				if(fila.getTareColumnOne()==null || fila.getTareColumnTwo()==null){
					existenulo=true;
					break;
				}
			}
			if (existenulo==false){
				TableResponses respuestaTabla=new TableResponses();
				respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21));		
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().add(respuestaTabla);
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.camposTabla"));
			}
		}
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
	/**
	 * prepara informacion salvaguarda C
	 */
	public void preparaInformacionSalvaguardaC(){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19).getTableResponsesList());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21).getTableResponsesList());
	}

	public void recargaPreguntasRespuestasSalvaguardas(){
		List<Integer> lista=new ArrayList<>();
		for (ProjectsSafeguards salvaguarda : getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()) {
//			if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==3 && salvaguarda.getSafeguards().getSafeParentId()==null)
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
					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaB).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
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
//					cargaTodosLosCantones();

					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}						
					}//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==4){
					int salvaguardaD=salvaguarda.getSafeId();
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaD).collect(Collectors.toList()));
					getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
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
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	public void prueba(){
		for (TableResponses tabla : getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()) {
			System.out.println(tabla.getTareColumnOne());
		}
	}
}
