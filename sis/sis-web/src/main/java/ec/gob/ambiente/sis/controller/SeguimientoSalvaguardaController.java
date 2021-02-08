package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

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
import ec.gob.ambiente.sis.model.QuestionsAnswers;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.QuestionsAnswersFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRespuestaEnum;

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
	private TableResponsesFacade tableResponsesFacade;
	
    @EJB
	@Getter
	private QuestionsAnswersFacade questionsAnswersFacade; 
	
	
	
	@PostConstruct
	public void init(){
		try{
			vaciarValores();			
			cargarProyectos();
			cargaSalvaguardasPlanAccion();
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
	public void seleccionaProyecto(){
		if(getSeguimientoSalvaguardaBean().getListaProyectos()!=null){
			for (Projects proyecto : getSeguimientoSalvaguardaBean().getListaProyectos()) {
				if(proyecto.getProjId()==getSeguimientoSalvaguardaBean().getCodigoProyecto()){					
					getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().findByCode(proyecto.getPartners().getPartId()) );
					cargaSectoresInteres();
					cargaSalvaguardasProyecto();
					cargarAvanceEjecucionSalvaguarda();
					cargaValoresRespuestas();
					getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
					break;
				}
			}				
						
		}
	}
	
	public void cargaSectoresInteres(){
		try{
			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(new ArrayList<Sectors>());
			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(getSectorsFacade().findAll());						
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void cargaSalvaguardasProyecto(){
		try{
			vaciarValores();
			getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(new ArrayList<ProjectsSafeguards>());
			getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(getProjectsSafeguardsFacade().findByProjectsCobenefits(getSeguimientoSalvaguardaBean().getCodigoProyecto()));
			if (getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null){
				getSeguimientoSalvaguardaBean().setListaSalvaguardasProyecto(getProjectsSafeguardsFacade().findByProjectsRisks(getSeguimientoSalvaguardaBean().getCodigoProyecto()));
			}
			if(getSeguimientoSalvaguardaBean().getListaSalvaguardasplanAccion()==null || getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
			}else{
				for(ProjectsSafeguards salvaguarda:getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()){
					if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==1){
						getSeguimientoSalvaguardaBean().setSalvaguardaA(true);
					}else if(salvaguarda.getSafeguards().getSafeOrder()==1 && salvaguarda.getSafeguards().getSafeLevel()==2){
						getSeguimientoSalvaguardaBean().setSalvaguardaB(true);
					}
				}

			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaSalvaguardasPlanAccion(){
		try{
			getSeguimientoSalvaguardaBean().setListaSalvaguardasplanAccion(new ArrayList<Safeguards>());
			getSeguimientoSalvaguardaBean().setListaSalvaguardasplanAccion(getSafeguardsFacade().findAll());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Carga el avance de ejecucion de las salvaguardas
	 */
	public void cargarAvanceEjecucionSalvaguarda(){
		try{
			getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getAdvanceExecutionSafeguardsFacade().findByProject(getSeguimientoSalvaguardaBean().getCodigoProyecto()));	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()==null){
				AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
				avanceEjecucion.setAdexId(0);
				avanceEjecucion.setAdexIsReported(false);
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(avanceEjecucion);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Carga los valores de respuestas del avance de ejecucion
	 */
	public void cargaValoresRespuestas(){
		try{
			getSeguimientoSalvaguardaBean().setListaValoresRespuestas(new ArrayList<ValueAnswers>());
			getSeguimientoSalvaguardaBean().setListaValoresRespuestasTabla(new ArrayList<TableResponses>());
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()>0){
				getSeguimientoSalvaguardaBean().setListaValoresRespuestas(getValueAnswersFacade().findByAdvanceExecution(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasTabla(getTableResponsesFacade().findByAdvanceExecution(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
			}
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestas()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestas().isEmpty()){				
				List<Integer> lista=new ArrayList<>();
				getSeguimientoSalvaguardaBean().setListaValoresRespuestas(new ArrayList<ValueAnswers>());
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasTabla(new ArrayList<TableResponses>());
				for (ProjectsSafeguards salvaguarda : getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()) {				
					lista.add(salvaguarda.getSafeguards().getSafeId());
				}
				getSeguimientoSalvaguardaBean().setListaPreguntasRespuestas(getQuestionsAnswersFacade().findBySafegaurd(lista));
				for(QuestionsAnswers preguntasRespuestas: getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas()){
					if(preguntasRespuestas.getAnswers().getAnswResponseTypeFormat().equals(TipoRespuestaEnum.TABLA.getCodigo())){
						TableResponses respuestasTabla=new TableResponses();
						respuestasTabla.setQuestionsAnswers(preguntasRespuestas);
						respuestasTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
						respuestasTabla.setTareColumnOne("");
						respuestasTabla.setTareColumnTwo("");
						respuestasTabla.setTareColumnTree("");
						respuestasTabla.setTareColumnFour("");
						respuestasTabla.setTareColumnFive("");
						respuestasTabla.setTareColumnSix("");
						respuestasTabla.setTareColumnSeven("");
						getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla().add(respuestasTabla);
					}else{
						ValueAnswers valoresRespuestas=new ValueAnswers();
						valoresRespuestas.setQuestionsAnswers(preguntasRespuestas);
						valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
						valoresRespuestas.setVaanRemoveState(true);
						valoresRespuestas.setVaanNumericAnswerValue(0);
						valoresRespuestas.setVaanTextAnswerValue("");
						valoresRespuestas.setVaanYesnoAnswer(false);
						getSeguimientoSalvaguardaBean().getListaValoresRespuestas().add(valoresRespuestas);
					}
				}
				getSeguimientoSalvaguardaBean().getListaValoresRespuestas().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList());
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla().stream().sorted((vrt1,vrt2)->vrt1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vrt2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Muestra el cuadro de dialogo para grabar el avnace de ejecucion
	 */
	public void mostrarDialogoGrabar(){
		Mensaje.verDialogo("dlgGrabaReporte");
	}
	
	public void grabarAvanceEjecucion(){
		System.out.println("Valores respuestas");
		for (ValueAnswers respuestas : getSeguimientoSalvaguardaBean().getListaValoresRespuestas()) {
			System.out.println(respuestas.getQuestionsAnswers().getQuestions().getQuesId());
		}
		System.out.println("Valores tablas");
		for (TableResponses  respuestas : getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla()) {
			System.out.println(respuestas.getTareColumnOne());
		}
	}
	
}
