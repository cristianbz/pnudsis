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
import ec.gob.ambiente.suia.model.Users;
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
	
	
	private boolean tienePreguntasRespuestas;
	
	@PostConstruct
	public void init(){
		try{
			vaciarValores();			
			cargarProyectos();
			cargaSalvaguardasPlanAccion();
			cargaSectoresInteres();
			tienePreguntasRespuestas=false;
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
					cargaValoresRespuestas();
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
	
	public void organizarRespuestasPorSalvaguardayPregunta(List<Integer> salvaguardas){
		getSeguimientoSalvaguardaBean().getListaValoresRespuestas().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList());
		getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList());
		
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(new ArrayList<>());
		
		for (ValueAnswers valoresRespuestas : getSeguimientoSalvaguardaBean().getListaValoresRespuestas()) {
			Safeguards salvaguarda=localizaSalvaguarda(valoresRespuestas.getQuestions().getSafeguards().getSafeId());
			if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().add(valoresRespuestas);
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==2)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(valoresRespuestas);
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==3)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(valoresRespuestas);
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==4)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().add(valoresRespuestas);
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==5)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().add(valoresRespuestas);
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==6)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().add(valoresRespuestas);
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==7)
				getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().add(valoresRespuestas);
		}
		ordenaListasValoresRespuestas();
		organizaListaDePreguntasRespuestas(salvaguardas);
		
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
			
			for (Integer elemento : lista) {
				Safeguards salvaguarda=localizaSalvaguarda(elemento.intValue());
				if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1){
					try {
						getSeguimientoSalvaguardaBean().setCatalogoPoliticas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.POLITICAS.getCodigo()));
						getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.LEYES.getCodigo()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}			
			getSeguimientoSalvaguardaBean().setListaPreguntas(getQuestionsFacade().buscarPreguntasPorSalvaguardas(lista));
//			getSeguimientoSalvaguardaBean().setListaPreguntasRespuestas(getQuestionsAnswersFacade().buscarPorSalvaguarda(lista));
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
				getSeguimientoSalvaguardaBean().setListaValoresRespuestas(getValueAnswersFacade().buscarPorAvanceEjecucion(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				getSeguimientoSalvaguardaBean().setListaValoresRespuestasTabla(getTableResponsesFacade().findByAdvanceExecution(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
				tienePreguntasRespuestas=true;
				organizarRespuestasPorSalvaguardayPregunta(lista);				
			}else{
				if(getSeguimientoSalvaguardaBean().getListaValoresRespuestas()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestas().isEmpty() || getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasTabla().isEmpty()){				
				//AQUI CARGAR POR SALVAGUARDA LAS PREGUNTAS
					filtrarPreguntaPorSalvaguarda(lista);				
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Organiza las listas de preguntasRespuestas de acuerdo al codigo de salvaguarda
	 * @param salvaguardas
	 */
	public void organizaListaDePreguntasRespuestas(List<Integer> salvaguardas){
		for(int contador=0;contador<salvaguardas.size();contador++){
			int codigosalvaguarda=salvaguardas.get(contador);
			getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().set(contador, getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter(pr->pr.getSafeguards().getSafeLevel()==codigosalvaguarda).collect(Collectors.toList()));
			Safeguards salvaguarda=localizaSalvaguarda(codigosalvaguarda);
			if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1)
				getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));				
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==2)
				getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==3)
				getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==4)
				getSeguimientoSalvaguardaBean().setListaPreguntasD(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==5)
				getSeguimientoSalvaguardaBean().setListaPreguntasE(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==6)
				getSeguimientoSalvaguardaBean().setListaPreguntasF(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));
			else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==7)
				getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador));
		}
	}
	
	public void filtrarPreguntaPorSalvaguarda(List<Integer> salvaguardas){
		try{
			organizaListaDePreguntasRespuestas(salvaguardas);
			for(int contador=0;contador<salvaguardas.size();contador++){
				Safeguards salvaguarda=localizaSalvaguarda(salvaguardas.get(contador));
				for(Questions preguntas: getSeguimientoSalvaguardaBean().getListaDeListasPreguntas().get(contador)){
					if(preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.CHECKBOX.getCodigo()) || preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.RADIOBUTTON.getCodigo())
							|| preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.TEXTO.getCodigo())){
						ValueAnswers valoresRespuestas=new ValueAnswers();
						valoresRespuestas.setQuestions(preguntas);
						valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
						valoresRespuestas.setVaanRemoveState(true);
						valoresRespuestas.setVaanNumericAnswerValue(0);
						valoresRespuestas.setVaanTextAnswerValue("");
						valoresRespuestas.setVaanYesnoAnswerValue(false);

						if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1){	
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().add(valoresRespuestas);
						}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==2){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().add(valoresRespuestas);
						}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==3){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(valoresRespuestas);
						}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==4){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().add(valoresRespuestas);
						}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==5){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().add(valoresRespuestas);
						}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==6){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().add(valoresRespuestas);
						}else if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==7){
							getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().add(valoresRespuestas);
						}
					}
				}
				ordenaListasValoresRespuestas();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Ordena las listasValoresRespuesta de acuerdo al orden de la pregunta
	 */
	public void ordenaListasValoresRespuestas(){
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
	}
		
	/**
	 * Muestra el cuadro de dialogo para grabar el avnace de ejecucion
	 */
	public void mostrarDialogoGrabar(int codigoSalvaguarda){
		if (codigoSalvaguarda==1)
			Mensaje.verDialogo("dlgGrabaReporteA");
	}
	/**
	 * Agrega una fila a primera tabla de la salvaguarda A
	 */
	public void agregaFilaTablaSalvaguardaA(){
		TableResponses respuestaTabla=new TableResponses();
		respuestaTabla.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
		respuestaTabla.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4));
		respuestaTabla.setTareColumnOne("");
		respuestaTabla.setTareColumnTwo("");
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(respuestaTabla);
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
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
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
			}
		}
	}
	
	/**
	 * Prepara la informacion a mostrar en el formulario de la salvaguarda A
	 */
	public void preparaInformacionSalvaguardaA(){
		getSeguimientoSalvaguardaBean().setCatalogoPoliticasSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCatalogoLeyesSeleccionado(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaA(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1).getTableResponsesList().stream().forEach(res->{		
			
			getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().add(String.valueOf(res.getTareColumnOne()));
		});
		getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3).getTableResponsesList().stream().forEach(res->{
			
			getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(String.valueOf(res.getTareColumnOne()));
		});
		List<TableResponses> tablaAux=new ArrayList<>();
		tablaAux=getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		tablaAux.stream().forEach(res->{
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(res);
		});
		
	}
}
