package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
			cargaSectoresInteres();
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
//			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
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
					getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
					
					cargaSalvaguardasProyecto();
					cargarAvanceEjecucionSalvaguarda();
					cargaValoresRespuestas();
					getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
					break;
				}
			}				
						
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void sectoresInteresProyecto(){
		
		getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
		for(AdvanceSectors as:getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdvanceSectorsList()){
			
			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().add(as.getSectors());
		}
	}
	
	public void cargaSectoresInteres(){
		try{
			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(new ArrayList<Sectors>());
			getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(getSectorsFacade().buscarTodosLosSectores());						
		}catch(Exception e){
			e.printStackTrace();
		}
	}

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
				avanceEjecucion.setAdexId(0);
				avanceEjecucion.setAdexIsReported(false);
				getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(avanceEjecucion);
				
			}else{
				sectoresInteresProyecto();
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

			List<Integer> lista=new ArrayList<>();
			for (ProjectsSafeguards salvaguarda : getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()) {				
				lista.add(salvaguarda.getSafeguards().getSafeId());
			}
			lista= lista.stream().sorted((sv1,sv2)->sv1.compareTo(sv2)).collect(Collectors.toList());
			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestas(getQuestionsAnswersFacade().buscarPorSalvaguarda(lista));

			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()>0){
				getSeguimientoSalvaguardaBean().setListaValoresRespuestas(getValueAnswersFacade().buscarPorAvanceEjecucion(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasTabla(getTableResponsesFacade().findByAdvanceExecution(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				////codigo prueba
				
//				for (ValueAnswers va : getSeguimientoSalvaguardaBean().getListaValoresRespuestas()) {
//					System.out.println(va.getQuestionsAnswers().getQuestions().getSafeguards().getSafeLevel());
//				}
				
			}
			if(getSeguimientoSalvaguardaBean().getListaValoresRespuestas()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestas().isEmpty() || getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla().isEmpty()){				
//				List<Integer> lista=new ArrayList<>();
//				for (ProjectsSafeguards salvaguarda : getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto()) {				
//					lista.add(salvaguarda.getSafeguards().getSafeId());
//				}
//				lista= lista.stream().sorted((sv1,sv2)->sv1.compareTo(sv2)).collect(Collectors.toList());
//				getSeguimientoSalvaguardaBean().setListaPreguntasRespuestas(getQuestionsAnswersFacade().findBySafegaurd(lista));
			
				//AQUI CARGAR POR SALVAGUARDA LAS PREGUNTAS
				filtrarPreguntaPorSalvaguarda(lista);
			/*
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
			*/
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void filtrarPreguntaPorSalvaguarda(List<Integer> salvaguardas){
		try{
			for(int contador=0;contador<salvaguardas.size();contador++){
				int codigosalvaguarda=salvaguardas.get(contador);
				getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().set(contador, getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==codigosalvaguarda).collect(Collectors.toList()));
				if(codigosalvaguarda==1)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasA(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
				else if(codigosalvaguarda==2)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasB(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
				else if(codigosalvaguarda==3)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasC(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
				else if(codigosalvaguarda==4)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasD(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
				else if(codigosalvaguarda==5)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasE(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
				else if(codigosalvaguarda==6)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasF(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
				else if(codigosalvaguarda==7)
					getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasG(getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador));
			}
			
			for(int contador=0;contador<salvaguardas.size();contador++){
				for(QuestionsAnswers preguntasRespuestas: getSeguimientoSalvaguardaBean().getListaDeListasPreguntasRespuestas().get(contador)){
					if(!preguntasRespuestas.getAnswers().getAnswResponseTypeFormat().equals(TipoRespuestaEnum.TABLA.getCodigo())){
						
						ValueAnswers valoresRespuestas=new ValueAnswers();
						valoresRespuestas.setQuestionsAnswers(preguntasRespuestas);
						valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
						valoresRespuestas.setVaanRemoveState(true);
						valoresRespuestas.setVaanNumericAnswerValue(0);
						valoresRespuestas.setVaanTextAnswerValue("");
						valoresRespuestas.setVaanYesnoAnswerValue(false);
//						getSeguimientoSalvaguardaBean().getListaValoresRespuestas().add(valoresRespuestas);
						if(contador==0){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().add(valoresRespuestas);
						}else if(contador==1){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(valoresRespuestas);
						}else if(contador==2){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(valoresRespuestas);
						}else if(contador==3){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().add(valoresRespuestas);
						}else if(contador==4){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().add(valoresRespuestas);
						}else if(contador==5){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().add(valoresRespuestas);
						}else if(contador==6){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().add(valoresRespuestas);
						}
					}
				}
//				getSeguimientoSalvaguardaBean().getListaValoresRespuestas().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList());
				
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));

			}
			
			
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasA(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==1).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasB(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==2).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasC(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==3).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasD(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==4).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasE(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==5).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasF(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==6).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestasG(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestas().stream().filter(pr->pr.getQuestions().getSafeguards().getSafeLevel()==7).collect(Collectors.toList()));
//			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargarRespuestasPrimeraVez(){
		for(QuestionsAnswers preguntasRespuestas: getSeguimientoSalvaguardaBean().getListaPreguntasRespuestasA()){
			if(!preguntasRespuestas.getAnswers().getAnswResponseTypeFormat().equals(TipoRespuestaEnum.TABLA.getCodigo())){
				ValueAnswers valoresRespuestas=new ValueAnswers();
				valoresRespuestas.setQuestionsAnswers(preguntasRespuestas);
				valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				valoresRespuestas.setVaanRemoveState(true);
				valoresRespuestas.setVaanNumericAnswerValue(0);
				valoresRespuestas.setVaanTextAnswerValue("");
				valoresRespuestas.setVaanYesnoAnswerValue(false);
				getSeguimientoSalvaguardaBean().getListaValoresRespuestas().add(valoresRespuestas);
			}
		}
		getSeguimientoSalvaguardaBean().getListaValoresRespuestas().stream().sorted((vr1,vr2)->vr1.getQuestionsAnswers().getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestionsAnswers().getQuestions().getQuesQuestionOrder())).collect(Collectors.toList());


	}
	
	/**
	 * Muestra el cuadro de dialogo para grabar el avnace de ejecucion
	 */
	public void mostrarDialogoGrabar(){
		Mensaje.verDialogo("dlgGrabaReporte");
	}
	/**
	 * Agrega una fila a primera tabla de la salvaguarda A
	 */
	public void agregaFilaTablaSalvaguardaA(){
		TableResponses respuestaTabla=new TableResponses();
		respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
		respuestaTabla.setQuestionsAnswers(getSeguimientoSalvaguardaBean().getListaPreguntasRespuestasA().get(4));
		respuestaTabla.setTareColumnOne("");
		respuestaTabla.setTareColumnTwo("");
		getSeguimientoSalvaguardaBean().getListaPreguntasRespuestasA().get(4).getTableResponsesList().add(respuestaTabla);
	}
	/**
	 * Graba el avance de ejecucion de las salvaguardas del proyecto
	 */
	public void grabarAvanceEjecucion(){
		try{
			List<AdvanceSectors> listaAvanceSectores=new ArrayList<>();
//			getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().forEach(s->{
//				AdvanceSectors avanceSectores=new AdvanceSectors();
//				avanceSectores.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//				avanceSectores.setSectors(s);
//				listaAvanceSectores.add(avanceSectores);
//			});
//			for(int j=0;j<getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().size();j++){
//				Sectors sector=new Sectors();				
//				System.out.println(getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().get(j).getSectName());
//			}
			for (Sectors sectores : getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados()) {
				AdvanceSectors avanceSectores=new AdvanceSectors();
				avanceSectores.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				avanceSectores.setSectors(sectores);
				listaAvanceSectores.add(avanceSectores);
			}
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdvanceSectorsList(listaAvanceSectores);
//			getAdvanceExecutionSafeguardsFacade().saveAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
//			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
}
