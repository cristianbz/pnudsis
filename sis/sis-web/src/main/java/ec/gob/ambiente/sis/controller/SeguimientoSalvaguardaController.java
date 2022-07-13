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

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.ComponentsFacade;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsSafeguardsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.BuscaProyectosBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.AdvanceSummary;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.AdvanceSummaryFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.ProjectQuestionsFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.OperacionesCatalogo;
import ec.gob.ambiente.sis.utils.OperacionesListas;
import ec.gob.ambiente.sis.utils.ResumenPDF;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoAccesoRecursoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoAreaConsolidadaEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoConformacionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoEstadoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoEstadoReporteEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoInstitucionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoNivelInvolucramientoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoParticipanteEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRespuestaEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoResultadoAcuerdoEnum;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import ec.gob.ambiente.suia.model.Users;
import ec.gob.ambiente.suia.service.GeographicalLocationsFacade;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class SeguimientoSalvaguardaController  implements Serializable{


	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(SeguimientoSalvaguardaController.class);
//	private static final int CODIGO_IDENTIFICACION_INDIGENA = 54;
	int CODIGO_IDENTIFICACION_INDIGENA = 0;

	@Inject
	@Getter	
	private ComponenteBuscaProyectos componenteBuscarProyectos;
	
	@Inject
	@Getter	
	private BuscaProyectosBean buscaProyectosBean;

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
	private AdvanceSummaryFacade advanceSummaryFacade; 
	
    @EJB
   	@Getter
   	private ComponentsFacade componentsFacade;

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
			getComponenteBuscarProyectos().getBuscaProyectosBean().setMenuAdministrador(false);
			getComponenteBuscarProyectos().setEsReporteGenero(false);
			getComponenteBuscarProyectos().setEsReporteSalvaguardas(true);
			getComponenteBuscarProyectos().setEsSeguimientoSalvaguardas(true);
			codigoSalvaguardaActual=0;
			numeroSalvaguardas=0;
			getSeguimientoSalvaguardaBean().setTabActual(0);
			vaciarValores();			
			cargaSalvaguardasPlanAccion();
			usuario=getLoginBean().getUser();
			getSeguimientoSalvaguardaBean().setNuevaLeySA(new Catalogs());
			getSeguimientoSalvaguardaBean().setNuevaPoliticaSA(new Catalogs());
			cargaProvincias();
			cargaTodosLosCantones();
			cargaListadoTipoInvolucramiento();
			cargaListadoInstitucionAcompania();
			cargaSociosImplementadores();

			CODIGO_IDENTIFICACION_INDIGENA =getAplicacionBean().getCodigoIndigena();
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.cargarProyectos"));			
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProyectos " + ": ").append(e.getMessage()));
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

				getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
				cargaSalvaguardasProyecto();

				cargaValoresRespuestaPorSalvaguarda();
				getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);

				getSeguimientoSalvaguardaBean().setTabActual(0);
				getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.proyectoReportado"));
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSalvaguardasDelProyectoSeleccionado " + ": ").append(e.getMessage()));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("error.cargarProyectos"));

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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSalvaguardasProyecto " + ": ").append(e.getMessage()));		
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSalvaguardasPlanAccion " + ": ").append(e.getMessage()));
		}

	}
	/**
	 * Carga el avance de ejecucion de las salvaguardas
	 */
	public void cargarAvanceEjecucionSalvaguarda(int codigoPartner,int codigoProyecto){
		try{


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

			}else{
				getSeguimientoSalvaguardaBean().setAnioReporte(Integer.valueOf(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexTermFrom().substring(0,4)));
				getSeguimientoSalvaguardaBean().setPeriodoDesde("01");


			}


		}catch(NoResultException e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		} catch (Exception e) {
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
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

		for (ProjectQuestions pq : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
			lista.add(pq.getSafeguards().getSafeId());
		}


		Collections.sort(lista, new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		getSeguimientoSalvaguardaBean().setListaValoresRespuestas(new ArrayList<>());
		List<ValueAnswers> listaTempValoresRespuesta= new ArrayList<>();
		Safeguards salvaguarda;
		try{
			getSeguimientoSalvaguardaBean().setListaPreguntas(new ArrayList<>());
			if(lista.size()>0){

				for (Integer codigoSalvaguarda : lista) {
					salvaguarda = localizaSalvaguarda(codigoSalvaguarda);
					//salvaguarda A
					if(salvaguarda.getSafeOrder()==1 && salvaguarda.getSafeLevel()==1){
						int salvaguardaA=salvaguarda.getSafeId();

						getSeguimientoSalvaguardaBean().setListaPreguntasA(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasA(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaA));
						getSeguimientoSalvaguardaBean().setCatalogoPoliticas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.POLITICAS.getCodigo()));
						getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICOINTERNACIONAL.getCodigo()));
						getSeguimientoSalvaguardaBean().setCatalogoMarcoJuridicoNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo()));
						getSeguimientoSalvaguardaBean().setCatalogoNormativaSecundariaNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
							getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasA(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
							}						
						}
						preparaInformacionSalvaguardaA();
						if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()==null  && getSeguimientoSalvaguardaBean().getTabActual()==0){
							listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
							listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
//							listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasA().stream()).collect(Collectors.toList());
						}
						//salvaguarda B
					}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
						int salvaguardaB=salvaguarda.getSafeId();

						getSeguimientoSalvaguardaBean().setListaPreguntasB(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasB(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaB));
						getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.INFORMEEJECUCION.getCodigo()));
						getSeguimientoSalvaguardaBean().setCatalogoInformacionComunica(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.INFORMECOMUNICACION.getCodigo()));
						cargaListadoInformaComunica();
						cargaListadoInformaEjecucion();
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

							getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasB(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
							}						
						}
						preparaInformacionSalvaguardaB();
						listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
						listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());
//						listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().stream()).collect(Collectors.toList());
						//SALVAGUARDA C
					}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
						int salvaguardaC=salvaguarda.getSafeId();
						getSeguimientoSalvaguardaBean().setListaPreguntasC(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasC(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaC));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

							getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
							}						
						}
						preparaInformacionSalvaguardaC();
						if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards() != null && getSeguimientoSalvaguardaBean().getTabActual()==0){
							listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
							listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
//							listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream()).collect(Collectors.toList());
						}
						//SALVAGUARDA D
					}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
						int salvaguardaD=salvaguarda.getSafeId();
						getSeguimientoSalvaguardaBean().setListaPreguntasD(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasD(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaD));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

							getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasD(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
							}						
						}
						preparaInformacionSalvaguardaD();
						if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards() != null && getSeguimientoSalvaguardaBean().getTabActual()==0){
							listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
							listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());
//							listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().stream()).collect(Collectors.toList());
						}
						//SALVAGUARDA E
					}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
						int salvaguardaE=salvaguarda.getSafeId();
						getSeguimientoSalvaguardaBean().setListaPreguntasE(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasE(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaE));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

							getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasE(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
							}						
						}
						preparaInformacionSalvaguardaE();
						if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!= null && getSeguimientoSalvaguardaBean().getTabActual()==0){
							listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
							listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());
//							listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().stream()).collect(Collectors.toList());
						}
						//SALVAGUARDA F
					}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
						int salvaguardaF=salvaguarda.getSafeId();
						getSeguimientoSalvaguardaBean().setListaPreguntasF(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasF(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaF));
						getSeguimientoSalvaguardaBean().setListaCatalogoMedidaTomada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MEDIDATOMADA.getCodigo()));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

							getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasF(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
							}						
						}
						preparaInformacionSalvaguardaF();
						if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!= null && getSeguimientoSalvaguardaBean().getTabActual()==0){
							listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
							listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());
//							listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().stream()).collect(Collectors.toList());
						}
						//SALVAGUARDA G
					}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
						int salvaguardaG=salvaguarda.getSafeId();
						getSeguimientoSalvaguardaBean().setListaPreguntasG(new ArrayList<>());
						getSeguimientoSalvaguardaBean().setListaPreguntasG(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaG));
						getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(new ArrayList<>());
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
							getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
							Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG(), new Comparator<ValueAnswers>(){
								@Override
								public int compare(ValueAnswers o1, ValueAnswers o2) {
									return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
								}
							});
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
							}
						}else{						
							if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().isEmpty()){
								valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasG(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
							}						
						}
						preparaInformacionSalvaguardaG();
						if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!= null && getSeguimientoSalvaguardaBean().getTabActual()==0){
							listaTempValoresRespuesta = new ArrayList<ValueAnswers>(listaTempValoresRespuesta);
							listaTempValoresRespuesta.addAll(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());
//							listaTempValoresRespuesta = Stream.concat(listaTempValoresRespuesta.stream(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().stream()).collect(Collectors.toList());
						}
					}

				}
				if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards() != null && getSeguimientoSalvaguardaBean().getTabActual()==0){				
					getSeguimientoSalvaguardaBean().setListaValoresRespuestas(listaTempValoresRespuesta);
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestas());
				}
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.proyectoSinSalvaguardas"));
				getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(true);
				getSeguimientoSalvaguardaBean().setDatosProyecto(false);
			}
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "", getMensajesController().getPropiedad("error.respuestasPreguntas"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaValoresRespuestaPorSalvaguarda " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Inicializa respuestas por defecto a las preguntas
	 * @param listaPreguntas
	 * @param listaRespuestas
	 */
	public void valoresRespuestasPorDefecto(List<Questions> listaPreguntas, List<ValueAnswers> listaRespuestas){
		try{
			for (Questions  preguntas : listaPreguntas) {
				if(preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.CHECKBOX.getCodigo()) || preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.RADIOBUTTON.getCodigo())
						|| preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.TEXTO.getCodigo())){
					ValueAnswers valoresRespuestas=new ValueAnswers();
					valoresRespuestas.setQuestions(preguntas);
					valoresRespuestas.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					valoresRespuestas.setVaanStatus(true);
					valoresRespuestas.setVaanNumericAnswerValue(0);
					valoresRespuestas.setVaanTextAnswerValue("");
					if(valoresRespuestas.getQuestions().getQuesId()==17)
						valoresRespuestas.setVaanYesnoAnswerValue(true);
					else
						valoresRespuestas.setVaanYesnoAnswerValue(false);
					listaRespuestas.add(valoresRespuestas);
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
						getValueAnswersFacade().agregarNuevaInformacionRespuesta(valoresRespuestas);
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "valoresRespuestasPorDefecto " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevosValoresRespuestas " + ": ").append(e.getMessage()));
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
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.tablasSalvaguardaA"));
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB()){

					grabarSalvaguardaB();

					for(int i=0;i<10;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, false);
					}
					renderizaMensajeRegistrosTablaB();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC()){

					grabarSalvaguardaC();

					for(int i=0;i<8;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(0, false);
					}
					renderizaMensajeRegistrosTablaC();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));

			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD()){

					grabarSalvaguardaD();

					for(int i=0;i<2;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(0, false);
					}
					renderizaMensajeRegistrosTablaD();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE()){

					grabarSalvaguardaE();

					for(int i=0;i<6;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(0, false);
					}
					renderizaMensajeRegistrosTablaE();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF()){

					grabarSalvaguardaF();

					for(int i=0;i<5;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(0, false);
					}
					renderizaMensajeRegistrosTablaF();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG()){

					grabarSalvaguardaG();

					for(int i=0;i<6;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(0, false);
					}
					renderizaMensajeRegistrosTablaG();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
			}
		}else{
			if(codigoSalvaguarda==1){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda))				
					Mensaje.verDialogo("dlgGrabaReporteA");			
				else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.tablasSalvaguardaA"));
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() ){
					if(validaDatosOpcionSiSalvaguardaB()){
						Mensaje.verDialogo("dlgGrabaReporteB");
						for(int i=0;i<10;i++){
							getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, false);
						}
						renderizaMensajeRegistrosTablaB();
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
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
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
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
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
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
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
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
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
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
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
				}
			}
		}
	}


	/**
	 * Agrega una fila a primera tabla de la salvaguarda A
	 */
	public void agregaFilaTablaSalvaguardaA(){

			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoPlanGobierno());
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnNumberSix()>0)
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(buscaCatalogoPlanGobierno(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnNumberSix()));
			else
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareAnotherCatalog());
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(2));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareColumnDecimalOne(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnDecimalOne());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareCatPlanGobierno(buscaCatalogoPlanGobierno(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnNumberSix()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasA(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaA1(false);
			getSeguimientoSalvaguardaBean().setCodigoPlanGobierno(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

	}
	/**
	 * Graba la salvaguarda A
	 */
	public void grabarSalvaguardaA(){
		try{
			organizaSectoresSeleccionados();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(new ArrayList<>());
			List<TableResponses> respuestasTabla= new ArrayList<>();
			List<TableResponses> datosRespuestasTabla = new ArrayList<TableResponses>(preparaGrabarLeyesSalvaguardaA());
			datosRespuestasTabla.addAll(preparaGrabarPoliticasSalvaguardaA());
			respuestasTabla = new ArrayList<TableResponses>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA());
			respuestasTabla.addAll(datosRespuestasTabla);

			for (ValueAnswers valores : getSeguimientoSalvaguardaBean().getListaValoresRespuestasA()) {
				valores.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}

			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)					
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA());
			
			for (TableResponses tr : getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()) {
				tr.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(2));
				tr.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList().add(tr);
			}
			List<TableResponses> datosTablaConcatenados = new ArrayList<TableResponses>(respuestasTabla);
			datosTablaConcatenados.addAll(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getTableResponsesList());
			
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(datosTablaConcatenados);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());

			controlaCambiosFechaPeriodoReportar();
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			
			getSeguimientoSalvaguardaBean().getAvanceResumenA().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenA().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenA());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaA();
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
			if(validaGrabarTabs(1)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(1,1);
			}
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaA " + ": ").append(e.getMessage()));			
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
		List<AdvanceSectors> listaAvanceSectores=new ArrayList<>();
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setProjects(proyecto);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setUsers(usuario);
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreatorUser(usuario.getUserName());
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()== null){
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexCreationDate(new Date());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexRegisterDate(new Date());
		}
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexStatus(true);

	}

	public List<TableResponses> preparaGrabarLeyesSalvaguardaA(){
		List<TableResponses> respuestasTabla=new ArrayList<>();
		Iterator itera =getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().iterator();
		while(itera.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareLawPolitical(Integer.valueOf(itera.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		Iterator iteraMJN =getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().iterator();
		while(iteraMJN.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareLawPolitical(Integer.valueOf(iteraMJN.next().toString()));
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		Iterator iteraNSN =getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().iterator();
		while(iteraNSN.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareLawPolitical(Integer.valueOf(iteraNSN.next().toString()));
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
			respuesta.setTareLawPolitical(Integer.valueOf(itera.next().toString()));
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
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPA(new ArrayList<>());
		try{
			cargaCatalogoPlanGobierno();

			List<TableResponses> listaSalvaguardaA=new ArrayList<>();		
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
				listaSalvaguardaA = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 1);
			}
			listaLeyes = OperacionesListas.filtrar(listaSalvaguardaA, 2);
			listaPoliticas = OperacionesListas.filtrar(listaSalvaguardaA, 4);
//			listaLeyes.stream().forEach(res->{						
//				if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoLeyes(), res.getTareLawPolitical()))
//					getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(res.getTareLawPolitical());
//				else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), res.getTareLawPolitical()))
//					getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().add(res.getTareLawPolitical());
//				else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), res.getTareLawPolitical()))
//					getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().add(res.getTareLawPolitical());
//
//			});
			
			for (TableResponses res : listaLeyes) {
				if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoLeyes(), res.getTareLawPolitical()))
					getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(res.getTareLawPolitical());
				else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), res.getTareLawPolitical()))
					getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().add(res.getTareLawPolitical());
				else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), res.getTareLawPolitical()))
					getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().add(res.getTareLawPolitical());
			}

//			listaPoliticas.stream().forEach(res->{			
//				getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().add(res.getTareLawPolitical());			
//			});
			
			for (TableResponses res : listaPoliticas) {
				getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().add(res.getTareLawPolitical());
			}
			
			List<TableResponses> tablaAux=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
				tablaAux = OperacionesListas.filtrar(listaSalvaguardaA, 5);
			}
//			tablaAux.stream().forEach(res->{				
//				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(res);
//			});	
			
			for (TableResponses res : tablaAux) {
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().add(res);
			}

//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaA().forEach(res->{
//				if(res.getTareColumnNumberSix()>0)
//					res.setTareCatPlanGobierno(buscaCatalogoPlanGobierno(res.getTareColumnNumberSix()));
//				else
//					res.setTareCatPlanGobierno(res.getTareAnotherCatalog());
//							
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()) {
				if(res.getTareColumnNumberSix()>0)
					res.setTareCatPlanGobierno(buscaCatalogoPlanGobierno(res.getTareColumnNumberSix()));
				else
					res.setTareCatPlanGobierno(res.getTareAnotherCatalog());
			}
			
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPA(OperacionesListas.filtrar(listaSalvaguardaA, 159));
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaA " + ": ").append(e.getMessage()));
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
			if(getSeguimientoSalvaguardaBean().getNuevaLeySA().getCataText2().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,"",  getMensajesController().getPropiedad("error.datosLey") );
			}else{
				tipo.setCatyId(TipoCatalogoEnum.MARCOJURIDICOINTERNACIONAL.getCodigo());
				catalogo.setCatalogsType(tipo);
				Collections.sort(getSeguimientoSalvaguardaBean().getCatalogoLeyes(), new Comparator<Catalogs>(){
					@Override
					public int compare(Catalogs o1, Catalogs o2) {
						return o1.getCataOrder().compareTo(o2.getCataOrder());
					}
				});
				if(getSeguimientoSalvaguardaBean().getCatalogoLeyes().size()==0)
					catalogo.setCataOrder(1);
				else
					catalogo.setCataOrder(getSeguimientoSalvaguardaBean().getCatalogoLeyes().get(getSeguimientoSalvaguardaBean().getCatalogoLeyes().size()-1).getCataOrder()+1);
				catalogo.setCataNumber(catalogo.getCataOrder());

				catalogo.setCataText1(".");

				catalogo.setCataText2(getSeguimientoSalvaguardaBean().getNuevaLeySA().getCataText2());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoLeyes().add(catalogo);
				getSeguimientoSalvaguardaBean().setNuevaLey(false);
			}
		}else if(categoria==2){
			if(getSeguimientoSalvaguardaBean().getNuevoMJN().getCataText2().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.datosLey"));
			}else{
				catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), getSeguimientoSalvaguardaBean().getNuevoMJN(), usuario.getUserName());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional().add(catalogo);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				getSeguimientoSalvaguardaBean().setNuevoMJN(new Catalogs());
				getSeguimientoSalvaguardaBean().setNuevoIngresoMJN(false);
			}
		}else if(categoria==3){
			if(getSeguimientoSalvaguardaBean().getNuevaNormativaSN().getCataText2().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.datosLey"), "");
			}else{
				catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), getSeguimientoSalvaguardaBean().getNuevaNormativaSN(), usuario.getUserName());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional().add(catalogo);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
				getSeguimientoSalvaguardaBean().setNuevaNormativaSN(new Catalogs());
				getSeguimientoSalvaguardaBean().setNuevoIngresoNSN(false);
			}
		}else if(categoria==4){
			if(getSeguimientoSalvaguardaBean().getNuevaPoliticaSA().getCataText2().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.datosPolitica"));
			}else{
				tipo.setCatyId(TipoCatalogoEnum.POLITICAS.getCodigo());
				catalogo.setCatalogsType(tipo);
				Collections.sort(getSeguimientoSalvaguardaBean().getCatalogoPoliticas(), new Comparator<Catalogs>(){
					@Override
					public int compare(Catalogs o1, Catalogs o2) {
						return o1.getCataOrder().compareTo(o2.getCataOrder());
					}
				});
				if(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().size()==0)
					catalogo.setCataOrder(1);
				else
					catalogo.setCataOrder(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().get(getSeguimientoSalvaguardaBean().getCatalogoPoliticas().size()-1).getCataOrder()+1);

				catalogo.setCataNumber(catalogo.getCataOrder());

				catalogo.setCataText1(".");
				catalogo.setCataText2(getSeguimientoSalvaguardaBean().getNuevaPoliticaSA().getCataText2());

				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoPoliticas().add(catalogo);
				getSeguimientoSalvaguardaBean().setNuevaPolitica(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
			}
		}
	}

	public void cargaCatalogoPlanGobierno(){
		try{
			getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setCatalogoPlanGobierno(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PLANPROYECTO.getCodigo()));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoPlanGobierno " + ": ").append(e.getMessage()));
		}
	}
	public void cargaCatalogoModalidad(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoModalidad(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoModalidad(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MODALIDAD.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoModalidades(new ArrayList<>());
			for (Catalogs catalog : getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()) {
				getSeguimientoSalvaguardaBean().getListadoModalidades().add(catalog.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoModalidad " + ": ").append(e.getMessage()));
		}
	}

	public void cargaCatalogoHerramientas(){
		try{
			getSeguimientoSalvaguardaBean().setListaHerramientas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.HERRAMIENTA.getCodigo()));
			Collections.sort(getSeguimientoSalvaguardaBean().getListaHerramientas(), new Comparator<Catalogs>(){
				@Override
				public int compare(Catalogs o1, Catalogs o2) {
					return o1.getCataText1().compareTo(o2.getCataText2());
				}
			});
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoHerramientas " + ": ").append(e.getMessage()));
		}
	}

	public void cargaCatalogoActividad(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoActividad(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoActividad(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ACTIVIDAD.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoActividades(new ArrayList<>());
			for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getListaCatalogoActividad()) {
				getSeguimientoSalvaguardaBean().getListadoActividades().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoActividad " + ": ").append(e.getMessage()));
		}
	}
	public void cargaCatalogoEstado(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoEstado(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoEstado(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ESTADO.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoEstados(new ArrayList<>());
			for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getListaCatalogoEstado()) {
				getSeguimientoSalvaguardaBean().getListadoEstados().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoEstado " + ": ").append(e.getMessage()));
		}
	}
	public void cargaCatalogoConformacion(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoConformacion(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoConformacion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.CONFORMACION.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoConformacion(new ArrayList<>());
			for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getListaCatalogoConformacion()) {
				getSeguimientoSalvaguardaBean().getListadoConformacion().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoConformacion " + ": ").append(e.getMessage()));
		}
	}

	public void cargaCatalogoMonitoreoRemoto(){
		try{
			getSeguimientoSalvaguardaBean().setListadoMonitoreoRemoto(new ArrayList<>());
			for (Catalogs catalogo : getAplicacionBean().getListaMonitoreoRemoto()) {
				getSeguimientoSalvaguardaBean().getListadoMonitoreoRemoto().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoMonitoreoRemoto " + ": ").append(e.getMessage()));
		}
	}
	public void cargaCatalogoInstitucionAcompania(){		
		try{
			getSeguimientoSalvaguardaBean().setListadoinstitucionAcompania(new ArrayList<>());
			for(Catalogs cat: getAplicacionBean().getListaInstitucionAcompania()){
				getSeguimientoSalvaguardaBean().getListadoinstitucionAcompania().add(cat.getCataText2());
			}
			Collections.sort(getSeguimientoSalvaguardaBean().getListadoinstitucionAcompania(), new Comparator<String>(){
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoInstitucionAcompania " + ": ").append(e.getMessage()));
		}
	}

	public void cargaCatalogoMonitoreoinSitu(){
		try{
			getSeguimientoSalvaguardaBean().setListadoMonitoreoInSitu(new ArrayList<>());
			for (Catalogs catalogo : getAplicacionBean().getListaMonitoreoInSitu()) {
				getSeguimientoSalvaguardaBean().getListadoMonitoreoInSitu().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoMonitoreoinSitu " + ": ").append(e.getMessage()));
		}
	}

	public void cargaCatalogoPeriodicidad(){
		try{
			getSeguimientoSalvaguardaBean().setListadoPeriodicidad(new ArrayList<>());
			for (Catalogs catalogo : getAplicacionBean().getListaPeriodicidad()) {
				getSeguimientoSalvaguardaBean().getListadoPeriodicidad().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoPeriodicidad " + ": ").append(e.getMessage()));
		}
	}

	public void cargarCatalogoServicio(){
		try{
			getSeguimientoSalvaguardaBean().setListaServicio(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaServicio(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.SERVICIO.getCodigo()));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarCatalogoServicio " + ": ").append(e.getMessage()));
		}
	}


	public void cargaCatalogoTipoDeAcceso(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoTipoAcceso(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoTipoAcceso(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.TIPODEACCESO.getCodigo()));
			getSeguimientoSalvaguardaBean().setListadoActividades(new ArrayList<>());

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoTipoDeAcceso " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaSociosImplementadores " + ": ").append(e.getMessage()));
		}
	}

	public void cargaCatalogoAccionImplementada(){
		try{
			getSeguimientoSalvaguardaBean().setListaCatalogoAccionImplementada(new ArrayList<>());
			getSeguimientoSalvaguardaBean().setListaCatalogoAccionImplementada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ACCIONESIMPLEMENTADAS.getCodigo()));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaCatalogoAccionImplementada " + ": ").append(e.getMessage()));
		}
	}
	public boolean validaDatosAvanceEjecucion(){
		if(getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().size()==0){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.datosAvanceEjecucion"));			
			return false;
		}else{
			return true;
		}
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
			Mensaje.verDialogo("dlgEliminaItemTablaA");
			break;
		case 2:
			Mensaje.verDialogo("dlgEliminaItemTablaB41");
			break;	
		case 3:
				Mensaje.verDialogo("dlgEliminaItemTablaB51");
			break;	
		case 4:
			Mensaje.verDialogo("dlgEliminaItemTablaB71");
			break;	
		case 5:
				Mensaje.verDialogo("dlgEliminaItemTablaB81");
			break;	
		case 6:
			Mensaje.verDialogo("dlgEliminaItemTablaB102");
			break;	
		case 7:
			Mensaje.verDialogo("dlgEliminaItemTablaB103");
			break;	
		case 8:
			Mensaje.verDialogo("dlgEliminaItemTablaB121");
			break;	
		case 9:
			Mensaje.verDialogo("dlgEliminaItemTablaB131");
			break;	
		case 10:
			Mensaje.verDialogo("dlgEliminaItemTablaC201");
			break;	
		case 11:
			Mensaje.verDialogo("dlgEliminaItemTablaC211");
			break;	
		case 12:
			Mensaje.verDialogo("dlgEliminaItemTablaC241");
			break;	
		case 13:
			Mensaje.verDialogo("dlgEliminaItemTablaC26");
			break;	
		case 14:
			Mensaje.verDialogo("dlgEliminaItemTablaC271");
			break;	
		case 15:
			Mensaje.verDialogo("dlgEliminaItemTablaC28");
			break;	
		case 16:
			Mensaje.verDialogo("dlgEliminaItemTablaC291");
			break;	
		case 17:
			Mensaje.verDialogo("dlgEliminaItemTablaC293");
			break;	
		case 18:
			Mensaje.verDialogo("dlgEliminaItemTablaC301");
			break;
		case 19:
			Mensaje.verDialogo("dlgEliminaItemTablaC311");
			break;
		case 20:
			Mensaje.verDialogo("dlgEliminaItemTablaD321");
			break;
		case 21:
			Mensaje.verDialogo("dlgEliminaItemTablaD331");
			break;
		case 22:
			Mensaje.verDialogo("dlgEliminaItemTablaE341");
			break;	
		case 23:
			Mensaje.verDialogo("dlgEliminaItemTablaE351");
			break;	
		case 24:
			Mensaje.verDialogo("dlgEliminaItemTablaE361");
			break;
		case 25:
			Mensaje.verDialogo("dlgEliminaItemTablaE371");
			break;
		case 26:
			Mensaje.verDialogo("dlgEliminaItemTablaE381");
			break;
		case 27:
			Mensaje.verDialogo("dlgEliminaItemTablaE391");
			break;
		case 28:
			Mensaje.verDialogo("dlgEliminaItemTablaF411");
			break;	
		case 29:
			Mensaje.verDialogo("dlgEliminaItemTablaF421");
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
				List<TableResponses> listaTemp=new ArrayList<>();
				listaTemp = getTableResponsesFacade().buscarPorAvanceEjecucionPreguntaProCanParr(getSeguimientoSalvaguardaBean().getRegistroTabla().getAdvanceExecutionSaveguards().getAdexId(), getSeguimientoSalvaguardaBean().getRegistroTabla().getQuestions().getQuesId(), getSeguimientoSalvaguardaBean().getRegistroTabla().getTareColumnNumberOne(), getSeguimientoSalvaguardaBean().getRegistroTabla().getTareColumnNumberTwo(), getSeguimientoSalvaguardaBean().getRegistroTabla().getTareColumnNumberThree());
				getTableResponsesFacade().eliminarDatosTabla(listaTemp);
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51Aux(new ArrayList<>());
				break;
			case 4:
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				break;
			case 5:
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				List<TableResponses> listaTemp8=new ArrayList<>();
				listaTemp8 = getTableResponsesFacade().buscarPorAvanceEjecucionPreguntaProCanParr(getSeguimientoSalvaguardaBean().getRegistroTabla().getAdvanceExecutionSaveguards().getAdexId(), getSeguimientoSalvaguardaBean().getRegistroTabla().getQuestions().getQuesId(), getSeguimientoSalvaguardaBean().getRegistroTabla().getTareColumnNumberOne(), getSeguimientoSalvaguardaBean().getRegistroTabla().getTareColumnNumberTwo(), getSeguimientoSalvaguardaBean().getRegistroTabla().getTareColumnNumberThree());
				getTableResponsesFacade().eliminarDatosTabla(listaTemp8);
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81Aux(new ArrayList<>());
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaRegistroTabla " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarProvincias " + ": ").append(e.getMessage()));
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoModalidad());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberThree(), 3));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareAnotherCatalog());

				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive()));
					}

					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareStatus(true);					
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0));
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnNumberFive(0);
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB41(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB41(false); 
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB41 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB51(){
		try{
			
//				if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51Aux().size()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberOne(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberTwo(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberThree(), 3));
					
//					for(TableResponses tr:getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51Aux()){
//						tr.setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
//						tr.setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
//						tr.setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
//						tr.setTareColumnOne(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnOne());
//						tr.setTareColumnTwo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnTwo());
//						tr.setTareColumnTree(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnTree());
//						tr.setTareCodeComponent(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareCodeComponent());
//						tr.setTareProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareProvincia());
//						tr.setTareCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareCanton());
//						tr.setTareParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareParroquia());
//						tr.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//						tr.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3));
//						tr.setTareStatus(true);
//						if(tr.getTareId()==null){
//							getTableResponsesFacade().agregaRespuestaTabla(tr, getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1));
//						}else
//							getTableResponsesFacade().agregarEditarNuevaTableResponse(tr);							
//						
//					}
//					if(getSeguimientoSalvaguardaBean().isEsNuevoRegistroTbl51())
//						getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51Aux().get(0));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareId()==null){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().setTareStatus(true);
						getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1));
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());	
					}else
						getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51());
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setCodigoComponente(0);
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51(new TableResponses());
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB51(false);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
//				}else{
//					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.beneficiarios"));
//				}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB51 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void preparaDatosB51(List<TableResponses> lista){
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(new ArrayList<>());
//		int provincia=0;
//		int canton=0;
//		int parroquia=0;
//		if(lista.size()>0){
//			provincia=lista.get(0).getTareColumnNumberOne();
//			canton=lista.get(0).getTareColumnNumberTwo();
//			parroquia=lista.get(0).getTareColumnNumberThree();
//			lista.get(0).setTareProvincia(buscaProvinciaCantonParroquia(lista.get(0).getTareColumnNumberOne(), 1));
//			lista.get(0).setTareCanton(buscaProvinciaCantonParroquia(lista.get(0).getTareColumnNumberTwo(), 2));
//			lista.get(0).setTareParroquia(buscaProvinciaCantonParroquia(lista.get(0).getTareColumnNumberThree(), 3));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(lista.get(0));
//			for (TableResponses tr : lista) {
//				if(tr.getTareColumnNumberOne()!=provincia || tr.getTareColumnNumberTwo()!=canton || tr.getTareColumnNumberThree()!=parroquia){
//					provincia=tr.getTareColumnNumberOne();
//					canton=tr.getTareColumnNumberTwo();
//					parroquia=tr.getTareColumnNumberThree();
//					tr.setTareProvincia(buscaProvinciaCantonParroquia(tr.getTareColumnNumberOne(), 1));
//					tr.setTareCanton(buscaProvinciaCantonParroquia(tr.getTareColumnNumberTwo(), 2));
//					tr.setTareParroquia(buscaProvinciaCantonParroquia(tr.getTareColumnNumberThree(), 3));
//					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().add(tr);
//				}
//			}
//		}
	}

	public void preparaDatosB81(List<TableResponses> lista){
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(new ArrayList<>());
		int provincia=0;
		int canton=0;
		int parroquia=0;
		if(lista.size()>0){
			provincia=lista.get(0).getTareColumnNumberOne();
			canton=lista.get(0).getTareColumnNumberTwo();
			parroquia=lista.get(0).getTareColumnNumberThree();
			lista.get(0).setTareProvincia(buscaProvinciaCantonParroquia(lista.get(0).getTareColumnNumberOne(), 1));
			lista.get(0).setTareCanton(buscaProvinciaCantonParroquia(lista.get(0).getTareColumnNumberTwo(), 2));
			lista.get(0).setTareParroquia(buscaProvinciaCantonParroquia(lista.get(0).getTareColumnNumberThree(), 3));
			if(lista.get(0).getTareColumnNumberSix()>0)
				lista.get(0).setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(lista.get(0).getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
			else
				lista.get(0).setTareGenerico(lista.get(0).getTareAnotherCatalog());
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(lista.get(0));
			for (TableResponses tr : lista) {
				if(tr.getTareColumnNumberOne()!=provincia || tr.getTareColumnNumberTwo()!=canton || tr.getTareColumnNumberThree()!=parroquia){
					provincia=tr.getTareColumnNumberOne();
					canton=tr.getTareColumnNumberTwo();
					parroquia=tr.getTareColumnNumberThree();
					tr.setTareProvincia(buscaProvinciaCantonParroquia(tr.getTareColumnNumberOne(), 1));
					tr.setTareCanton(buscaProvinciaCantonParroquia(tr.getTareColumnNumberTwo(), 2));
					tr.setTareParroquia(buscaProvinciaCantonParroquia(tr.getTareColumnNumberThree(), 3));
					if(tr.getTareColumnNumberSix()>0)
						tr.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(tr.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
					else
						tr.setTareGenerico(tr.getTareAnotherCatalog());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(tr);
				}
			}
		}
	}

	
	public void agregarFilasTablaSalvaguardaB61(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareColumnNumberTwo(), 2));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(5));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB61(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB61(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
				vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB61 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB71(){
		try{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setTareStatus(true);
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8));
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB71());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB71(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB71(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "", getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB81(){
		try{
				if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81Aux().size()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoActividad());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberOne(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberTwo(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberThree(), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
					for(TableResponses tr:getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81Aux()){
						tr.setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
						tr.setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
						tr.setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
						tr.setTareColumnOne(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne());
						tr.setTareColumnNine(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNine());
						tr.setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoActividad());
						tr.setTareColumnDecimalOne(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnDecimalOne());
						tr.setTareProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareProvincia());
						tr.setTareCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareCanton());
						tr.setTareParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareParroquia());
						
						tr.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
						tr.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10));
						tr.setTareStatus(true);
						if(tr.getTareColumnNumberSix()>0){					
							tr.setTareAnotherCatalog("");
							tr.setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareGenerico());
						}else{
							tr.setTareAnotherCatalog(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareAnotherCatalog());
							tr.setTareGenerico(tr.getTareAnotherCatalog());
						}
						if(tr.getTareId()==null){
							getTableResponsesFacade().agregaRespuestaTabla(tr, getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4));
							tr.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(tr.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
							if(tr.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
								tr.setTareGenericoTres(ubicaPuebloNacionalidad(tr.getTareColumnNumberFive()));
						}else
							getTableResponsesFacade().agregarEditarNuevaTableResponse(tr);							
					}
					if(getSeguimientoSalvaguardaBean().isEsNuevoRegistroTbl81())
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81Aux().get(0));
					
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB81(false);
					getSeguimientoSalvaguardaBean().setCodigoActividad(0);
					getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
					getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.beneficiarios"));
				}
		
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB81 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregaFilaTabla81Aux(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81Aux(new TableResponses());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81Aux().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81Aux());
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);	
	}
	
	public void agregaFilaTabla51Aux(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51Aux(new TableResponses());
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51Aux().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51Aux());		
	}

	public void agregarFilasTablaSalvaguardaB9(){
		try{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB9 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}


	public void agregarFilasTablaSalvaguardaB102(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() , getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoEstado());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaCatalogoEstado()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareId()==null){
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(13));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					}				
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaCatalogoEstado()));
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoEstado(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB102(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB102 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB11(){
		try{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(16));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB11(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB11(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB11 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB143(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnTwo(String.join(",", getSeguimientoSalvaguardaBean().getInformaComunicaSeleccionadas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnTree(String.join(",", getSeguimientoSalvaguardaBean().getInformaEjecucionSeleccionadas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(20));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB143(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB143(false);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB143 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
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
		getSeguimientoSalvaguardaBean().setCodigoPlanGobierno(null);
		getSeguimientoSalvaguardaBean().setOtroCatalogoA1(true);
	}

	public void agregarFilasTablaSalvaguardaB121(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberSeven(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoConformacion()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberSeven()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberSeven(), getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoTres(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(16));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareStatus(true);

					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnNumberFive(0);
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB121(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(0);
				getSeguimientoSalvaguardaBean().setCodigoAccionImplementada(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB121 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB131(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoModalidad());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareAnotherCatalog());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(18));		
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB131(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB131(false);
				getSeguimientoSalvaguardaBean().setCodigoActividad(0);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				getSeguimientoSalvaguardaBean().setCodigoModalidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB131 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaC201(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());

				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));					

				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso() ));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1));

					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFive()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberFive()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos("");
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso() ));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC201(false);
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "", getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC201 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaC211(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3));

					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareStatus(true);

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
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				vaciarDatosProvinciaCantonParroquia();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC211 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaC241(){
		try{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(5));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC241(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC241(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC241 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaC242(){
		try{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(6));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC242(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC242(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC242 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaC26(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(8));

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC26(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC26(false);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC26 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaC271(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenerico( OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));								
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFive()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFive()));
					}else{

						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC271(false);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC271 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaC28(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(11));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFive()));
					}	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFive()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC28(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC28(false);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC28 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaC291(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());

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
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos("");
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
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC291 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}


	public void agregarFilasTablaSalvaguardaC301(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15));	
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				}else{					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC301(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC301(false);
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaC311(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
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
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				vaciarDatosProvinciaCantonParroquia();
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC301 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaD321(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareStatus(true);

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
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD321 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPA " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaOPB(){
		try{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPB " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaOPC(){
		try{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPC " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPD " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaOPE(){
		try{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaOPF(){
		try{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaOPG(){
		try{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaOPE " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	/**
	 * Permite agregar nueva fila a la tabla B102
	 */
	public void nuevaFilaTablaSalvaguardaB102(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(null);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(null);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoTipoEstado(0);
	}

	/**
	 * Permite agregar nueva fila a la tabla B81
	 */
	public void nuevaFilaTablaSalvaguardaB81(){
		getSeguimientoSalvaguardaBean().setEsNuevoRegistroTbl81(true);
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAccion(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoActividad(null);
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81Aux(new ArrayList<>());
	}
	public void nuevaFilaTablaSalvaguardaB9(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB9(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
//	public void nuevaFilaTablaSalvaguardaB103(){
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnOne("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnTwo("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnTree("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnFour("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnFive("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnSix("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnSeven("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareColumnEight("0");
//	}
	public void nuevaFilaTablaSalvaguardaB121(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(null);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setOtroCatalogo121(true);
	}
	public void nuevaFilaTablaSalvaguardaB131(){
		getSeguimientoSalvaguardaBean().setCodigoModalidad(null);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoB131(true);
	}
	/**
	 * Permite agregar nueva fila a la tabla B41
	 */
	public void nuevaFilaTablaSalvaguardaB41(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB41(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoModalidad(null);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setOtroCatalogoB41(true);
	}
	public void nuevaFilaTablaSalvaguardaB51(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51Aux(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setEsNuevoRegistroTbl51(true);
	}
	public void nuevaFilaTablaSalvaguardaB143(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB143(new TableResponses());
		getSeguimientoSalvaguardaBean().setInformaComunicaSeleccionadas(null);
		getSeguimientoSalvaguardaBean().setInformaEjecucionSeleccionadas(null);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
	}
	public void nuevaFilaTablaSalvaguardaB61(){
		vaciarDatosProvinciaCantonParroquia();
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
	}

	public void nuevaFilaTablaSalvaguardaC201(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(null);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoC201(true);
	}
	public void nuevaFilaTablaSalvaguardaC241(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC241(new TableResponses());

	}
	public void nuevaFilaTablaSalvaguardaC242(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC242(new TableResponses());

	}
	public void nuevaFilaTablaSalvaguardaC26(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC26(new TableResponses());

		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);

	}
	public void nuevaFilaTablaSalvaguardaC271(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaC28(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC28(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);	
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaC291(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());

		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
	}

	public void nuevaFilaTablaSalvaguardaC3111(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());

		getSeguimientoSalvaguardaBean().setCodCanton(0);
		getSeguimientoSalvaguardaBean().setCodParroquia(0);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}

	public void nuevaFilaTablaSalvaguardaD331(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD331(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoTipoInvolucramiento(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setNivelInvolucramientoSeleccionados(null);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}

	public void nuevaFilaTablaSalvaguardaE341(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE341(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
	}
	public void nuevaFilaTablaSalvaguardaE351(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());	
		getSeguimientoSalvaguardaBean().setCodigoHerramienta(null);		
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoE351(true);
	}

	public void nuevaFilaTablaSalvaguardaE361(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}

	public void nuevaFilaTablaSalvaguardaE371(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoMetodo(0);
		getSeguimientoSalvaguardaBean().setCodigoPublico(null);
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoE371(true);
	}
	public void nuevaFilaTablaSalvaguardaE381(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoServicio(null);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoE381(true);
	}
	public void nuevaFilaTablaSalvaguardaE391(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoRecursos(null);
		getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoE391(true);
	}
	public void nuevaFilaTablaSalvaguardaF411(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF411(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
	}

	public void nuevaFilaTablaSalvaguardaF421(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF421(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(null);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoF421(true);
	}
	public void nuevaFilaTablaSalvaguardaF431(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoSistemas(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
	}
	public void nuevaFilaTablaSalvaguardaF441(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(null);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoF441(true);
	}
	public void nuevaFilaTablaSalvaguardaF452(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF452(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
	}
	public void nuevaFilaTablaSalvaguardaG461(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(null);
		getSeguimientoSalvaguardaBean().setCodProvincia(0);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoG461(true);
	}
	public void nuevaFilaTablaSalvaguardaG471(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());
		vaciarDatosProvinciaCantonParroquia();	
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoActividad(null);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoG471(true);
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
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(null);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setOtroCatalogoG491(true);
	}
	public void nuevaFilaTablaSalvaguardaG501(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG501(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}

	/**
	 * Agrega filas a la tabla D331
	 */
	public void agregarFilasTablaSalvaguardaD331(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFour()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTree(String.join(",", getSeguimientoSalvaguardaBean().getNivelInvolucramientoSeleccionados()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3));			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades() ));
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades() ));
					}else{
						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD331(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD331(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoInvolucramiento(0);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD331 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaE341(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoTipoAreaConsolidada());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareGenerico(ubicaTipoAreaConsolidada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberSix()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE341(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE341(false);
				getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE341 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE351(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnOne(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenerico(buscaNivelOrganizacion(getSeguimientoSalvaguardaBean().getCodigoNivelProCanParr()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoHerramienta());
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenericoDos(ubicaHerramienta(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnNumberSix()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareGenericoDos(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3));		

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE351(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE351(false);
				getSeguimientoSalvaguardaBean().setCodigoHerramienta(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE351 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE361(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
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

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2));

					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE361(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE361(false);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE361 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE371(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoMetodo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareColumnNumberSeven(getSeguimientoSalvaguardaBean().getCodigoPublico());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenerico(ubicaMetodo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberSix()));
				

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberSeven()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenericoDos(ubicaPublico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberSeven()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareGenericoDos(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE371(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE371(false);
				getSeguimientoSalvaguardaBean().setCodigoMetodo(0);
				getSeguimientoSalvaguardaBean().setCodigoPublico(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE371 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE381(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoServicio());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaServicio()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareAnotherCatalog());

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(10));

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE381(new TableResponses());
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE381(false);
				getSeguimientoSalvaguardaBean().setCodigoServicio(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaE391(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoRecursos());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareColumnNumberSeven(getSeguimientoSalvaguardaBean().getCodigoPeriodicidad());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenerico(ubicaRecurso(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberSix()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareAnotherCatalog());
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareGenericoDos(ubicaPeriodicidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberSeven()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(12));

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasE391(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaE391(false);
				getSeguimientoSalvaguardaBean().setCodigoRecursos(0);
				getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaE381 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}


	public void agregarFilasTablaSalvaguardaF411(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1));

					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF411(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF411(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF411 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaF421(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3));

					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}

					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF421(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF421(false);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF421 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaF431(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoSistemas());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareGenerico(ubicaSistema(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnNumberSix()));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5));

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF431(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF431(false);
				getSeguimientoSalvaguardaBean().setCodigoSistemas(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF431 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaF441(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoRiesgo());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberThree(), 3));
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberSix(), getAplicacionBean().getListaCatalogoRiesgo()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareAnotherCatalog());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareId()==null){

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}

					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF441(false);
				getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF441 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}
	public void agregarFilasTablaSalvaguardaF452(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().getTareColumnNumberOne(), 1));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(9));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());
				}else
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452());			
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF452(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF452(false);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF452 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaG461(){		
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoRiesgo());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberSix(), getAplicacionBean().getListaCatalogoRiesgo()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareStatus(true);
					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG461(false);
				getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
				getSeguimientoSalvaguardaBean().setCodProvincia(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG461 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaG471(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberThree(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberSix(), getAplicacionBean().getListaControlVigilancia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoTres(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareAnotherCatalog());

				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFive()));
					}										
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnNumberFive()));
					}else{

						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos("");
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG471(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoControlVigilancia(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,"",   getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF441 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaG481(){
		try{
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

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.join(",", getSeguimientoSalvaguardaBean().getInstitucionAcompaniaSeleccionados()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnNumberFive()));
					}else{
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,"",   getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG481 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaG491(){
		try{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberThree(getSeguimientoSalvaguardaBean().getCodParroquia());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberSix(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberFour(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion());				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberOne(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberTwo(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberThree(), 3));
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour()!=null)
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberSix()>0){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberSix()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareAnotherCatalog("");
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareAnotherCatalog());
				
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour()!=null){
						if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA ){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));						
						}
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour()!=null){
						if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA ){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
	
						}else{
							
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres("");
						}
					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG491(false);
				getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,"",   getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG491 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaG501(){
		try{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
				}else{
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG501(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG501(false);
				getSeguimientoSalvaguardaBean().setCodigoComponente(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG501 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void agregarFilasTablaSalvaguardaG512(){
		try{
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareComponente(ubicaComponente(getSeguimientoSalvaguardaBean().getCodigoComponente()));

				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareId()==null){					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(12));					
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());					
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFive()));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareStatus(true);

					getTableResponsesFacade().agregaRespuestaTabla(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5));
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
				}else{
					if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());					
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnNumberFive()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos("");

					}
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG512(false);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaG512 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
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
			Collections.sort(getSeguimientoSalvaguardaBean().getListaTodosCantones(), new Comparator<GeographicalLocations>(){
				@Override
				public int compare(GeographicalLocations o1, GeographicalLocations o2) {
					return o1.getGeloName().compareTo(o2.getGeloName());
				}
			});
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaTodosLosCantones " + ": ").append(e.getMessage()));
		}
	}
	public void grabarSalvaguardaB(){
		try{
			organizaSectoresSeleccionados();
//			List<TableResponses> respuestasTabla=new ArrayList<>();			
//			List<TableResponses> datosRespuestasTablasCheck= new ArrayList<>();
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB());

			List<TableResponses> respuestasTabla=new ArrayList<>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131());

			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getAvanceResumenB().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenB().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenB());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaB();

			if(validaGrabarTabs(2)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(2,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaB " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Grabar informacion salvaguarda C
	 */
	public void grabarSalvaguardaC(){
		try{						

			organizaSectoresSeleccionados();
//			List<TableResponses> respuestasTabla=new ArrayList<>();	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());

			List<TableResponses> respuestasTabla=new ArrayList<>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311());
//			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getAvanceResumenC().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenC().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenC());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaC();

			if(validaGrabarTabs(3)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(3,1);

			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaC " + ": ").append(e.getMessage()));
		}

	}

	public void preparaInformacionSalvaguardaB(){
		try{
			cargaCatalogoModalidad();
			cargaCatalogoActividad();
			cargaCatalogoAccionImplementada();
			cargaCatalogoEstado();
			cargaCatalogoConformacion();

			List<TableResponses> listaSalvaguardaB=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaB = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 2);
			Collections.sort(getSeguimientoSalvaguardaBean().getListaPreguntasB(), new Comparator<Questions>(){
				@Override
				public int compare(Questions o1, Questions o2) {
					return o1.getQuesQuestionOrder().compareTo(o2.getQuesQuestionOrder());
				}
			});

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(OperacionesListas.filtrar(listaSalvaguardaB, 7));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
//				if(res.getTareColumnNumberSix()>0)
//					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(res.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
//				else
//					res.setTareGenerico(res.getTareAnotherCatalog());
//				
//				res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
//					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
//				}
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(res.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				
				res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
				}
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(OperacionesListas.filtrar(listaSalvaguardaB, 9));
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//				res.setTareCodeComponent(getSeguimientoSalvaguardaBean().getCodigoComponente());
				
//				res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
//					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));
//				}
			}
			
			

			List<TableResponses> tabla81 = new ArrayList<>();
			tabla81 = OperacionesListas.filtrar(listaSalvaguardaB, 16);
			Collections.sort(tabla81, new Comparator<TableResponses>(){
				@Override
				public int compare(TableResponses o1, TableResponses o2) {
					return o1.getTareColumnNumberThree().compareTo(o2.getTareColumnNumberThree());
				}
			});
			
			preparaDatosB81(tabla81);
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB9(OperacionesListas.filtrar(listaSalvaguardaB, 17));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(OperacionesListas.filtrar(listaSalvaguardaB, 20));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//
//				res.setTareGenericoTres(ubicaDatosEstado(res.getTareColumnNumberSix()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));

//				res.setTareGenericoTres(ubicaDatosEstado(res.getTareColumnNumberSix()));				
				res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoEstado()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(OperacionesListas.filtrar(listaSalvaguardaB, 27));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().forEach(res->{
//				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
//				res.setTareGenericoDos(ubicaConformacion(res.getTareColumnNumberSix()));
//				if(res.getTareColumnNumberSeven()>0)
//					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSeven(),getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
//				else
//					res.setTareGenericoTres(res.getTareAnotherCatalog());
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){				
//					res.setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
//				}
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121()) {
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaCatalogoConformacion()));
				if(res.getTareColumnNumberSeven()>0)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSeven(),getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
				else
					res.setTareGenericoTres(res.getTareAnotherCatalog());
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){				
					res.setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(), getAplicacionBean().getListaPueblosNacionalidades()));	
				}
			}
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(OperacionesListas.filtrar(listaSalvaguardaB, 23));
//			List<TableResponses> tabla = new ArrayList<>();
//			tabla = OperacionesListas.filtrar(listaSalvaguardaB, 9);
//			Collections.sort(tabla, new Comparator<TableResponses>(){
//				@Override
//				public int compare(TableResponses o1, TableResponses o2) {
//					return o1.getTareColumnNumberThree().compareTo(o2.getTareColumnNumberThree());
//				}
//			});
			
//			preparaDatosB51(tabla);
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(OperacionesListas.filtrar(listaSalvaguardaB, 11));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(OperacionesListas.filtrar(listaSalvaguardaB, 14));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(OperacionesListas.filtrar(listaSalvaguardaB, 21));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(OperacionesListas.filtrar(listaSalvaguardaB, 29));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//				if(res.getTareColumnNumberSix()>0)
//					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
//				else
//					res.setTareGenerico(res.getTareAnotherCatalog());
//				
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
				if(res.getTareColumnNumberSix()>0){
					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogoText2(res.getTareColumnNumberSix(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
					
				}else
					res.setTareGenerico(res.getTareAnotherCatalog());
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(OperacionesListas.filtrar(listaSalvaguardaB, 31));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));				
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));				
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPB(OperacionesListas.filtrar(listaSalvaguardaB, 160));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaB " + ": ").append(e.getMessage()));
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
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(OperacionesListas.filtrar(listaSalvaguardaC, 43));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//				
//				if(res.getTareColumnNumberEight()>0)
//					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberEight(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso()));
//				else
//					res.setTareGenericoTres(res.getTareAnotherCatalog());
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));	
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso()));
				else
					res.setTareGenericoTres(res.getTareAnotherCatalog());
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));	
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(OperacionesListas.filtrar(listaSalvaguardaC, 45));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));	
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));	
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(OperacionesListas.filtrar(listaSalvaguardaC, 57));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//				res.setTareGenericoTres(ubicaResultadoAcuerdo(res.getTareColumnNumberSix()));
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				res.setTareGenericoTres(ubicaResultadoAcuerdo(res.getTareColumnNumberSix()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(OperacionesListas.filtrar(listaSalvaguardaC, 63));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(OperacionesListas.filtrar(listaSalvaguardaC, 49));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(OperacionesListas.filtrar(listaSalvaguardaC, 50));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(OperacionesListas.filtrar(listaSalvaguardaC, 52));			
			
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(OperacionesListas.filtrar(listaSalvaguardaC, 54));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().forEach(res->{
//				
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271()) {
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(OperacionesListas.filtrar(listaSalvaguardaC, 55));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().forEach(res->{			
//				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28()) {
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(OperacionesListas.filtrar(listaSalvaguardaC, 61));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPC(OperacionesListas.filtrar(listaSalvaguardaC, 161));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaC " + ": ").append(e.getMessage()));
		}
	}

	/**
	 * Graba salvaguarda D
	 */
	public void grabarSalvaguardaD(){
		try{

			organizaSectoresSeleccionados();
//			List<TableResponses> respuestasTabla=new ArrayList<>();		
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD());			

			List<TableResponses> respuestasTabla=new ArrayList<>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331());
//			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getAvanceResumenD().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenD().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenD());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaD();

			if(validaGrabarTabs(4)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(4,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){

			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaD " + ": ").append(e.getMessage()));
		}

	}
	/**
	 * Graba la salvaguarda E
	 */
	public void grabarSalvaguardaE(){
		try{

			organizaSectoresSeleccionados();
//			List<TableResponses> respuestasTabla=new ArrayList<>();		
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE());			

			List<TableResponses> respuestasTabla=new ArrayList<>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391());
//			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getAvanceResumenE().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenE().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenE());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaE();

			if(validaGrabarTabs(5)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(5,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaE " + ": ").append(e.getMessage()));
		}

	}
	/**
	 * Grabar informacion salvaguarda F
	 */
	public void grabarSalvaguardaF(){
		try{

			organizaSectoresSeleccionados();
//			List<TableResponses> respuestasTabla=new ArrayList<>();	
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF());			

			List<TableResponses> respuestasTabla=new ArrayList<>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452());
//			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441() .stream()).collect(Collectors.toList());
//			respuestasTabla= Stream.concat(respuestasTabla.stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getAvanceResumenF().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenF().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenF());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaF();

			if(validaGrabarTabs(6)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(6,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaF " + ": ").append(e.getMessage()));
		}

	}
	/**
	 * Grabar informacion salvaguarda G
	 */
	public void grabarSalvaguardaG(){
		try{

			organizaSectoresSeleccionados();
//			List<TableResponses> respuestasTabla=new ArrayList<>();		
			if (getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG());			

			List<TableResponses> respuestasTabla=new ArrayList<>(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461());
			respuestasTabla.addAll(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471());
//			respuestasTabla= Stream.concat(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().stream(), getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471() .stream()).collect(Collectors.toList());
			for (TableResponses datos : respuestasTabla) {
				datos.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			}
			controlaCambiosFechaPeriodoReportar();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setTableResponsesList(respuestasTabla);
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(getLoginBean().getUser().getUserName());
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getAvanceResumenG().setAdsuUpdateDate(new Date());
			getSeguimientoSalvaguardaBean().getAvanceResumenG().setAdsuUpdateUser(getLoginBean().getUser().getUserName());
			getAdvanceSummaryFacade().edit(getSeguimientoSalvaguardaBean().getAvanceResumenG());
			recargaPreguntasRespuestasSalvaguardas();
			preparaInformacionSalvaguardaG();

			if(validaGrabarTabs(7)==false && getSeguimientoSalvaguardaBean().getTabActual()<numeroSalvaguardas){
				getSeguimientoSalvaguardaBean().getMapaTabs().put(7,1);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaG " + ": ").append(e.getMessage()));
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
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(OperacionesListas.filtrar(listaSalvaguardaD, 65));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
//				else
//					res.setTareGenericoDos("");
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				else
					res.setTareGenericoDos("");
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(OperacionesListas.filtrar(listaSalvaguardaD, 67));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().forEach(res->{
//				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
//					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
//				else
//					res.setTareGenericoTres("");
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331()) {
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				else
					res.setTareGenericoTres("");
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPD(OperacionesListas.filtrar(listaSalvaguardaD, 162));

			vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaD " + ": ").append(e.getMessage()));
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

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(OperacionesListas.filtrar(listaSalvaguardaE, 69));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(ubicaTipoAreaConsolidada(res.getTareColumnNumberSix()));
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaTipoAreaConsolidada(res.getTareColumnNumberSix()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(OperacionesListas.filtrar(listaSalvaguardaE, 71));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().forEach(res->{
//				res.setTareGenerico(buscaNivelOrganizacion(res.getTareColumnOne()));
//				
//				if(res.getTareColumnNumberSix()>0)
//					res.setTareGenericoDos(ubicaHerramienta(res.getTareColumnNumberSix()));
//				else
//					res.setTareGenericoDos(res.getTareAnotherCatalog());
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351()) {
				res.setTareGenerico(buscaNivelOrganizacion(res.getTareColumnOne()));
				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenericoDos(ubicaHerramienta(res.getTareColumnNumberSix()));
				else
					res.setTareGenericoDos(res.getTareAnotherCatalog());
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(OperacionesListas.filtrar(listaSalvaguardaE, 73));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
//				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
//					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
//				}
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(OperacionesListas.filtrar(listaSalvaguardaE, 75));
//			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().forEach(res->{
//				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
//				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
//				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
//				res.setTareGenerico(ubicaMetodo(res.getTareColumnNumberSix()));
//				if(res.getTareColumnNumberSeven()>0)
//					res.setTareGenericoDos(ubicaPublico(res.getTareColumnNumberSeven()));
//				else
//					res.setTareGenericoDos(res.getTareAnotherCatalog());
//				
//				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
//			});
			
			for (TableResponses res : getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371()) {
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaMetodo(res.getTareColumnNumberSix()));
				if(res.getTareColumnNumberSeven()>0)
					res.setTareGenericoDos(ubicaPublico(res.getTareColumnNumberSeven()));
				else
					res.setTareGenericoDos(res.getTareAnotherCatalog());
				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(OperacionesListas.filtrar(listaSalvaguardaE, 78));
			for(TableResponses res: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(),getSeguimientoSalvaguardaBean().getListaServicio()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			
			
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(OperacionesListas.filtrar(listaSalvaguardaE, 80));
			for(TableResponses res: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(ubicaRecurso(res.getTareColumnNumberSix()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				res.setTareGenericoDos(ubicaPeriodicidad(res.getTareColumnNumberSeven()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_1(OperacionesListas.filtrar(listaSalvaguardaE, 82));
			for(TableResponses res :getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareGenerico(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_2(OperacionesListas.filtrar(listaSalvaguardaE, 83));
			for(TableResponses res: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_3(OperacionesListas.filtrar(listaSalvaguardaE, 84));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_4(OperacionesListas.filtrar(listaSalvaguardaE, 85));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_5(OperacionesListas.filtrar(listaSalvaguardaE, 86));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_6(OperacionesListas.filtrar(listaSalvaguardaE, 87));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_7(OperacionesListas.filtrar(listaSalvaguardaE, 88));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_8(OperacionesListas.filtrar(listaSalvaguardaE, 89));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_9(OperacionesListas.filtrar(listaSalvaguardaE, 90));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_10(OperacionesListas.filtrar(listaSalvaguardaE, 91));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_11(OperacionesListas.filtrar(listaSalvaguardaE, 92));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_12(OperacionesListas.filtrar(listaSalvaguardaE, 93));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_13(OperacionesListas.filtrar(listaSalvaguardaE, 94));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_14(OperacionesListas.filtrar(listaSalvaguardaE, 95));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_15(OperacionesListas.filtrar(listaSalvaguardaE, 96));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_16(OperacionesListas.filtrar(listaSalvaguardaE, 97));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_17(OperacionesListas.filtrar(listaSalvaguardaE, 98));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_18(OperacionesListas.filtrar(listaSalvaguardaE, 99));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_19(OperacionesListas.filtrar(listaSalvaguardaE, 100));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_20(OperacionesListas.filtrar(listaSalvaguardaE, 101));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_21(OperacionesListas.filtrar(listaSalvaguardaE, 102));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_22(OperacionesListas.filtrar(listaSalvaguardaE, 173));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_23(OperacionesListas.filtrar(listaSalvaguardaE, 103));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_24(OperacionesListas.filtrar(listaSalvaguardaE, 104));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_25(OperacionesListas.filtrar(listaSalvaguardaE, 105));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_26(OperacionesListas.filtrar(listaSalvaguardaE, 106));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_27(OperacionesListas.filtrar(listaSalvaguardaE, 107));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_28(OperacionesListas.filtrar(listaSalvaguardaE, 108));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_29(OperacionesListas.filtrar(listaSalvaguardaE, 109));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_30(OperacionesListas.filtrar(listaSalvaguardaE, 110));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_31(OperacionesListas.filtrar(listaSalvaguardaE, 111));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_32(OperacionesListas.filtrar(listaSalvaguardaE, 166));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_33(OperacionesListas.filtrar(listaSalvaguardaE, 167));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_34(OperacionesListas.filtrar(listaSalvaguardaE, 168));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_35(OperacionesListas.filtrar(listaSalvaguardaE, 169));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_36(OperacionesListas.filtrar(listaSalvaguardaE, 170));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_37(OperacionesListas.filtrar(listaSalvaguardaE, 171));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE40_38(OperacionesListas.filtrar(listaSalvaguardaE, 172));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPE(OperacionesListas.filtrar(listaSalvaguardaE, 163));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaE " + ": ").append(e.getMessage()));
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

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(OperacionesListas.filtrar(listaSalvaguardaF, 113));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(OperacionesListas.filtrar(listaSalvaguardaF, 115));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(buscaCatalogoMedidaTomada(res.getTareColumnNumberSix()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				
				res.setTareGenericoDos(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(OperacionesListas.filtrar(listaSalvaguardaF, 117));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431()){
				res.setTareGenerico(ubicaSistema(res.getTareColumnNumberSix()));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}


			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(OperacionesListas.filtrar(listaSalvaguardaF, 119));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getAplicacionBean().getListaCatalogoRiesgo()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				res.setTareGenericoDos(ubicaAutoidentificacion(res.getTareColumnNumberFour()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
				}
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(OperacionesListas.filtrar(listaSalvaguardaF, 122));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPF(OperacionesListas.filtrar(listaSalvaguardaF, 164));
			vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaF " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Prepara informacion salvaguarda G
	 */
	public void preparaInformacionSalvaguardaG(){
		try{
			cargaCatalogoActividad();
			cargaCatalogoInstitucionAcompania();
			cargaCatalogoMonitoreoRemoto();
			cargaCatalogoMonitoreoinSitu();
			cargaCatalogoPeriodicidad();
			List<TableResponses> listaSalvaguardaG=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null) 
				listaSalvaguardaG = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 29);
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(OperacionesListas.filtrar(listaSalvaguardaG, 124));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461()){
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(),getAplicacionBean().getListaCatalogoRiesgo()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(OperacionesListas.filtrar(listaSalvaguardaG, 126));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberSix()>0){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberSix(), getAplicacionBean().getListaControlVigilancia()));
					res.setTareAnotherCatalog("");
				}else
					res.setTareGenericoTres(res.getTareAnotherCatalog());

				
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				}
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(OperacionesListas.filtrar(listaSalvaguardaG, 129));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(), getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));
				}	
				res.setTareGenericoTres(ubicaAccion(res.getTareColumnNumberSix()));
			}

			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(OperacionesListas.filtrar(listaSalvaguardaG, 131));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));	
				
				if(res.getTareColumnNumberSix()>0)
					res.setTareGenerico(ubicaAlternativaEconomica(res.getTareColumnNumberSix()));
				else
					res.setTareGenerico(res.getTareAnotherCatalog());
				
				if(res.getTareColumnNumberFour()!=null){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion() ));
					if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
						res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades() ));
				}
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(OperacionesListas.filtrar(listaSalvaguardaG, 133));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501()){
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(OperacionesListas.filtrar(listaSalvaguardaG, 136));
			for(TableResponses res:getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512()){
				res.setTareProvincia(buscaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareComponente(ubicaComponente(res.getTareCodeComponent()));
				res.setTareParroquia(buscaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(res.getTareColumnNumberFive()));

			}
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPG(OperacionesListas.filtrar(listaSalvaguardaG, 165));
			vaciarDatosProvinciaCantonParroquia();
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "preparaInformacionSalvaguardaG " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Metodo que recarga la informacion de la salvaguarda luego de grabar.
	 */
	public void recargaPreguntasRespuestasSalvaguardas(){
		List<Integer> lista=new ArrayList<>();
		for (ProjectQuestions pq : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
			lista.add(pq.getSafeguards().getSafeId());
		}
		Collections.sort(lista, new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
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
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasA(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasA(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasB(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));						
						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));
						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasD(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));

						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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

					getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasE(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));

						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasF(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));

						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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
					getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(new ArrayList<>());
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){

						getSeguimientoSalvaguardaBean().setListaValoresRespuestasG(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), salvaguarda.getSafeId()));

						Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG(), new Comparator<ValueAnswers>(){
							@Override
							public int compare(ValueAnswers o1, ValueAnswers o2) {
								return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
							}
						});
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "recargaPreguntasRespuestasSalvaguardas " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "buscarCanton " + ": ").append(e.getMessage()));
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
		return catalogo.getCataText2();
	}
	/**
	 * Localiza el catalogo de medida tomada
	 * @param codigo
	 * @return
	 */
	public String buscaCatalogoMedidaTomada(int codigo){
		Catalogs catalogo;
		catalogo= getSeguimientoSalvaguardaBean().getListaCatalogoMedidaTomada().stream().filter(pg->pg.getCataId().equals(codigo)).findFirst().get();
		return catalogo.getCataText2();
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

		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoModalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour());

		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
		if(getSeguimientoSalvaguardaBean().getCodigoModalidad()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoB41(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoB41(true);
	}
	
	public void editarTablaB51(){
		getSeguimientoSalvaguardaBean().setEsNuevoRegistroTbl51(false);
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberThree());		
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareCodeComponent());
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51Aux(new ArrayList<>());
//		try{
//			List<TableResponses> listaTemp=new ArrayList<>();
//			listaTemp = getTableResponsesFacade().buscarPorAvanceEjecucionPreguntaProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getAdvanceExecutionSaveguards().getAdexId(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getQuestions().getQuesId(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberOne(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberTwo(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB51().getTareColumnNumberThree());
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51Aux(listaTemp);
//		}catch(Exception e){
//			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "editarTablaB51 " + ": ").append(e.getMessage()));
//		}
	}

	public void editarTablaB61(){
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareColumnNumberTwo());
	}

	public void editarTablaB81(){
		getSeguimientoSalvaguardaBean().setEsNuevoRegistroTbl81(false);
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoActividad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81Aux(new ArrayList<>());
		try{
			List<TableResponses> listaTemp=new ArrayList<>();
			listaTemp = getTableResponsesFacade().buscarPorAvanceEjecucionPreguntaProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getAdvanceExecutionSaveguards().getAdexId(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getQuestions().getQuesId(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberOne(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberTwo(), getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnNumberThree());
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81Aux(listaTemp);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "editarTablaB81 " + ": ").append(e.getMessage()));
		}

		if(getSeguimientoSalvaguardaBean().getCodigoActividad()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoB81(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoB81(true);
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
		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAccionImplementada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnNumberSeven());
		if(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogo121(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogo121(true);
	}
	public void editarTablaB131(){
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodigoModalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnNumberSix());
		if(getSeguimientoSalvaguardaBean().getCodigoModalidad()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoB131(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoB131(true);
	}
	public void editarTablaB143(){
		getSeguimientoSalvaguardaBean().setInformaComunicaSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnTwo().split(","));
		getSeguimientoSalvaguardaBean().setInformaEjecucionSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnTree().split(","));
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareCodeComponent());
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareCodeComponent());
		if(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoC201(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoC201(true);
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareCodeComponent());
	}
	public void editarTablaC26(){
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareCodeComponent());
	}
	public void editarTablaC271(){
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFour());
		
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnNumberFive());
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaC28(){				
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFour());
		if (getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFour()==CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnNumberFive());
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareCodeComponent());
	}

	public void editarTablaC291(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareCodeComponent());
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnNumberThree());
	}

	public void editarTablaC311(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareCodeComponent());
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnNumberFour());
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareCodeComponent());		
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberOne());
		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFour());
		getSeguimientoSalvaguardaBean().setNivelInvolucramientoSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTree().split(","));
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnNumberFive());
	}

	public void editarTablaE341(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnNumberSix());
	}

	public void editarTablaE351(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodigoNivelProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnOne());	
		getSeguimientoSalvaguardaBean().setCodigoHerramienta(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().getTareColumnNumberSix());
		if(getSeguimientoSalvaguardaBean().getCodigoHerramienta()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE351(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoE351(true);
	}
	public void editarTablaE361(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodigoNivelProCanParr(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnOne());
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoMetodo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoPublico(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().getTareColumnNumberSeven());
		if(getSeguimientoSalvaguardaBean().getCodigoPublico()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE371(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoE371(true);
	}
	public void editarTablaE381(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoServicio(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnNumberSix());
		if(getSeguimientoSalvaguardaBean().getCodigoServicio()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE381(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoE381(true);
	}

	public void editarTablaE391(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoRecursos(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoPeriodicidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().getTareColumnNumberSeven());
		if(getSeguimientoSalvaguardaBean().getCodigoRecursos()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE391(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoE391(true);
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareCodeComponent());
	}
	public void editarTablaF421(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareCodeComponent());
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
		if(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoF421(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoF421(true);
	}
	
	public void editarTablaA1(){
		getSeguimientoSalvaguardaBean().setCodigoPlanGobierno(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnNumberSix());
		if(getSeguimientoSalvaguardaBean().getCodigoPlanGobierno()==0)			
			getSeguimientoSalvaguardaBean().setOtroCatalogoA1(false);
		else
			getSeguimientoSalvaguardaBean().setOtroCatalogoA1(true);
	}
	public void editarTablaF431(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodigoSistemas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnNumberSix());
	}
	public void editarTablaF441(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareCodeComponent());
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
		if(getSeguimientoSalvaguardaBean().getCodigoRiesgo()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoF441(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoF441(true);
	}
	public void editarTablaF452(){
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().getTareColumnNumberOne());
	}
	public void editarTablaG461(){
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnNumberOne());
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareCodeComponent());
		if(getSeguimientoSalvaguardaBean().getCodigoRiesgo()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoG461(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoG461(true);
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareCodeComponent());
		if(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia() == 0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoG471(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoG471(true);
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
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaG491(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareCodeComponent());
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberOne());
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberTwo());
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberThree());
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberSix());
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour()!=null){
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnNumberFive());			
				habilitaPuebloNacionalidad();
			}
		}
		if(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoG491(false);
		}else
			getSeguimientoSalvaguardaBean().setOtroCatalogoG491(true);
	}
	
	public void editarTablaG501(){
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG501().getTareCodeComponent());
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
		getSeguimientoSalvaguardaBean().setCodigoComponente(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareCodeComponent());
	}

	public void siguienteTab(int salvaguarda){
		setCodigoSalvaguardaActual(salvaguarda);
		if(!(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual()==1)){			
			if(salvaguarda==1 ){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(1)){
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					else{
						grabarSalvaguardaA();				
						getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
					}
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.tablasSalvaguardaA"));					
				}

			}else if(salvaguarda==2 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaB()){
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						else{
							grabarSalvaguardaB();				
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						}
					}else{
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));
					}

				}
			}else if(salvaguarda==3 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaC()){
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						else{
							grabarSalvaguardaC();				
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						}

					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));

				}
			}else if(salvaguarda==4 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaD()){
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						else{
							grabarSalvaguardaD();				
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						}

					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));

				}
			}else if(salvaguarda==5 ){
				if(validaDatosAvanceEjecucion()){
					if(validaDatosOpcionSiSalvaguardaE()){
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						else{
							grabarSalvaguardaE();				
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						}

					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));

				}
			}else if(salvaguarda==6 ){
				if(validaDatosAvanceEjecucion() ){
					if(validaDatosOpcionSiSalvaguardaF()){
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						else{
							grabarSalvaguardaF();				
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						}
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));

				}
			}else if(salvaguarda==7 ){

				if(validaDatosAvanceEjecucion() ){
					if(validaDatosOpcionSiSalvaguardaG()){
						if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexReportedStatus().equals("F"))
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						else{
							grabarSalvaguardaG();				
							getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
						}
					}else
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.seleccionSi"));

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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "buscaProvinciaCantonParroquia " + ": ").append(e.getMessage()));
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
				Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB(), new Comparator<ValueAnswers>(){
					@Override
					public int compare(ValueAnswers o1, ValueAnswers o2) {
						return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
					}
				});
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
				Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB(), new Comparator<ValueAnswers>(){
					@Override
					public int compare(ValueAnswers o1, ValueAnswers o2) {
						return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
					}
				});
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
					Collections.sort(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG(), new Comparator<ValueAnswers>(){
						@Override
						public int compare(ValueAnswers o1, ValueAnswers o2) {
							return o1.getQuestions().getQuesQuestionOrder().compareTo(o2.getQuestions().getQuesQuestionOrder());
						}
					});
					Mensaje.actualizarComponente(":form:salvaguardas:radiopSG7");
				}
			}

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaInformacionRespuestas " + ": ").append(e.getMessage()));
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
//		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B11")){
//			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11()) 
//					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue()==false){
//				Mensaje.verDialogo("dlgEliminaDatosTabla");
//			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){
			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131()) 
					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
			try{
				if(validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143())
						&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue()==false){
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
					|| validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242())){
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
				List<TableResponses> tablaTemp=getTableResponsesFacade().listaAvanceEjecucionPregunta(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().get(0).getAdvanceExecutionSaveguards().getAdexId(),getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().get(0).getQuestions().getQuesId());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(tablaTemp,getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1));
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
				List<TableResponses> tablaTemp=getTableResponsesFacade().listaAvanceEjecucionPregunta(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().get(0).getAdvanceExecutionSaveguards().getAdexId(),getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().get(0).getQuestions().getQuesId());				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(tablaTemp,getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4));
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
//			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B11")){
//				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7));
//				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(new ArrayList<>());
//				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
//				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla11B");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B121")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB121");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B131")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB131");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B141")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9));
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
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla341E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E351")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla351E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E361")){
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
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla381E");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("E391")){				
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
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla411F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F421")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla421F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F431")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla431F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F441")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla451F");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("F452")){	
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla452F");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){				
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla461G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){			
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla471G");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G481")){			
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla481G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G491")){			
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla491G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G501")){			
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla501G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G511")){		
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512(),getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5));
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla512G");				
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaValoresTablaDatos " + ": ").append(e.getMessage()));
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
			herramienta=catalogo.getCataText2();
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
		}else if(TipoAreaConsolidadaEnum.CONECTIVIDADDEAREAS.getCodigo() == codigo){
			tipo = TipoAreaConsolidadaEnum.CONECTIVIDADDEAREAS.getEtiqueta();
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
		}else if(!getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue() &&
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().size()==0){
			informacionCompleta=false;
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(5, true);
		}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue() &&
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().size()==0 ){
			informacionCompleta=false;
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(6, true);
//		}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue() && 
//				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().size()==0){
//			informacionCompleta=false;
//			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(7, true);
		}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue() &&
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().size()==0 ){
			informacionCompleta=false;
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(7, true);
		}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue() &&
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().size()==0 ){
			informacionCompleta=false;
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(8, true);
		}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue() && (
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().size()==0)){
			informacionCompleta=false;
			getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(9, true);
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
	public void nuevoPlanGobierno(){
//		getSeguimientoSalvaguardaBean().setPlanGobierno(new Catalogs());
//		Mensaje.verDialogo("dlgNuevoPlanGobierno");
	}

	public void agregaNuevaModalidad(){
		try{
			Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MODALIDAD.getCodigo(), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad(), getSeguimientoSalvaguardaBean().getCatalogoModalidad(), usuario.getUserName());
			getSeguimientoSalvaguardaBean().getListaCatalogoModalidad().add(catalogo);
			getCatalogsFacade().create(catalogo);
			Mensaje.ocultarDialogo("dlgNuevaModalidadActividad");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
			getSeguimientoSalvaguardaBean().setCatalogoModalidad(new Catalogs());
			if(nuevaModalidadActividad == 1){
				Mensaje.actualizarComponente(":form:salvaguardas:comboModalidadActividad");
			}else if(nuevaModalidadActividad == 2){
				getSeguimientoSalvaguardaBean().getListadoModalidades().add(catalogo.getCataText1());
				Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionB131");
			}

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevaModalidad " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "", getMensajesController().getPropiedad("info.infoGrabada"));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevaActividad " + ": ").append(e.getMessage()));
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

	public void agregaNuevoControlVigilancia(){
		try{
			Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.CONTROLVIGILANCIA.getCodigo(), getAplicacionBean().getListaControlVigilancia(), getSeguimientoSalvaguardaBean().getNuevoControlVigilancia(), usuario.getUserName());
			getAplicacionBean().getListaControlVigilancia().add(catalogo);
			getCatalogsFacade().create(catalogo);
			getSeguimientoSalvaguardaBean().setNuevoRecurso(new Catalogs());
			Mensaje.actualizarComponente(":form:salvaguardas:comboControlVF471");
			Mensaje.ocultarDialogo("dlgNuevoControlVigilancia");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoControlVigilancia " + ": ").append(e.getMessage()));
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
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoIncentivo " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoRiesgo " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoRecurso " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoServicio " + ": ").append(e.getMessage()));
		}
	}
	public void agregaNuevoTipoAcceso(){
		try{
			Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.TIPODEACCESO.getCodigo(), getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso(), getSeguimientoSalvaguardaBean().getNuevoTipoAcceso(), usuario.getUserName());
			getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso().add(catalogo);
			getCatalogsFacade().create(catalogo);
			Mensaje.ocultarDialogo("dlgNuevoTipoAcceso");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
			getSeguimientoSalvaguardaBean().setNuevoTipoAcceso(new Catalogs());
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoTipoAcceso " + ": ").append(e.getMessage()));
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevaHerramienta " + ": ").append(e.getMessage()));
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
			getSeguimientoSalvaguardaBean().getListadoMonitoreoRemoto().add(catalogo.getCataText2());
			getCatalogsFacade().create(catalogo);	
			getSeguimientoSalvaguardaBean().setNuevoMonitoreoRemoto(new Catalogs());
			Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionRemotoG512");
			Mensaje.ocultarDialogo("dlgNuevoMonitoreoRemoto");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoMonitoreoRemoto " + ": ").append(e.getMessage()));
		}
	}

	public void agregaNuevoMonitoreoInSitu(){
		try{
			Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo(), getAplicacionBean().getListaMonitoreoInSitu(), getSeguimientoSalvaguardaBean().getNuevoMonitoreoInSitu(), usuario.getUserName());
			getAplicacionBean().getListaMonitoreoInSitu().add(catalogo);
			getSeguimientoSalvaguardaBean().getListadoMonitoreoInSitu().add(catalogo.getCataText2());
			getCatalogsFacade().create(catalogo);	
			getSeguimientoSalvaguardaBean().setNuevoMonitoreoInSitu(new Catalogs());
			Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionInSituG512");
			Mensaje.ocultarDialogo("dlgNuevoMonitoreoInSitu");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,"",  getMensajesController().getPropiedad("info.infoGrabada") );

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevoMonitoreoInSitu " + ": ").append(e.getMessage()));
		}
	}

	public void agregaNuevaEjecucion(){
		try{
			Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.INFORMEEJECUCION.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion(), getSeguimientoSalvaguardaBean().getNuevaEjecucionSB(), usuario.getUserName());
			getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion().add(catalogo);
			getSeguimientoSalvaguardaBean().getListadoInformaEjecucion().add(catalogo.getCataText2());
			getCatalogsFacade().create(catalogo);	
			getSeguimientoSalvaguardaBean().setNuevaEjecucionSB(new Catalogs());
			Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionEjecucionB141");
			Mensaje.ocultarDialogo("dlgNuevaFormaInformeEjecucion");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevaEjecucion " + ": ").append(e.getMessage()));
		}
	}

	public void agregaNuevaComunicacion(){
		try{
			Catalogs catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.INFORMECOMUNICACION.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoInformacionComunica(), getSeguimientoSalvaguardaBean().getNuevaComunicacionSB(), usuario.getUserName());
			getSeguimientoSalvaguardaBean().getCatalogoInformacionComunica().add(catalogo);
			getSeguimientoSalvaguardaBean().getListadoInformaComunica().add(catalogo.getCataText2());
			getCatalogsFacade().create(catalogo);
			getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
			Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionB141");
			Mensaje.ocultarDialogo("dlgNuevaFormaComunica");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "", getMensajesController().getPropiedad("info.infoGrabada"));

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaNuevaComunicacion " + ": ").append(e.getMessage()));
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
							nuevoRegistroTableResponse.setTareLawPolitical(valorNuevo);
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
					String numero= String.valueOf( listaNuevos.get(0));
					int n= Integer.parseInt(numero);
					nuevoRegistroTableResponse.setTareLawPolitical(n);
					nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "controlAgregaEditaLeyes " + ": ").append(e.getMessage()));
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
							question.setQuesId(4);
							nuevoRegistroTableResponse.setTareStatus(true);
							nuevoRegistroTableResponse.setQuestions(question);
							nuevoRegistroTableResponse.setTareLawPolitical(valorNuevo);
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
					nuevoRegistroTableResponse.setTareLawPolitical(n);
					nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "controlAgregaEditaPoliticas " + ": ").append(e.getMessage()));
		}
	}


	public void cargaListadoInformaComunica(){
		try{
			getSeguimientoSalvaguardaBean().setListadoInformaComunica(new ArrayList<>());
			for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getCatalogoInformacionComunica()) {
				getSeguimientoSalvaguardaBean().getListadoInformaComunica().add(catalogo.getCataText2());
			}

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaListadoInformaComunica " + ": ").append(e.getMessage()));
		}
	}

	public void cargaListadoInformaEjecucion(){
		try{
			getSeguimientoSalvaguardaBean().setListadoInformaEjecucion(new ArrayList<>());
			for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion()) {
				getSeguimientoSalvaguardaBean().getListadoInformaEjecucion().add(catalogo.getCataText2());
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaListadoInformaEjecucion " + ": ").append(e.getMessage()));
		}
	}	


	public void buscarProyectos(){
		try{
			getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<>());
			if(getSeguimientoSalvaguardaBean().getCodigoBusquedaProyecto()==1){			
				getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().listarProyectosPorIdSocioImpl(getSeguimientoSalvaguardaBean().getCodigoSocioImplementador()));
				if(getSeguimientoSalvaguardaBean().getListaProyectos().size()==0){
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.noProyectos"));
				}
			}else if(getSeguimientoSalvaguardaBean().getCodigoBusquedaProyecto()==2){
				if (getSeguimientoSalvaguardaBean().getTituloProyecto().length()>5){
					getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().listarProyectosPorTextoTitulo(getSeguimientoSalvaguardaBean().getTituloProyecto()));
					if(getSeguimientoSalvaguardaBean().getListaProyectos().size()==0){
						Mensaje.actualizarComponente(":form:growl");				
						Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.noProyectos"));
					}
				}else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.longitudMinima"));
				}

			}else{
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR ,"" , getMensajesController().getPropiedad("error.seleccionBusqueda"));
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "buscarProyectos " + ": ").append(e.getMessage()));
		}
	}
	public void vaciaDatosBusqueda(){
		getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCodigoProyecto(null);
		getSeguimientoSalvaguardaBean().setCodigoSocioImplementador(0);

		getSeguimientoSalvaguardaBean().setTabActual(0);
		vaciarValores();
		getSeguimientoSalvaguardaBean().setListaPreguntasRespuestas(new ArrayList<>());

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
	public void imprimirSalvaguardasVacias(){
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String directorioArchivoPDF = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append(1).append(".pdf").toString();
		rutaPDF=directorioArchivoPDF;
		ResumenPDF.reporteSalvaguardaVacio(directorioArchivoPDF, getSeguimientoSalvaguardaBean());
	}
	


	/**
	 * Finaliza el reporte de salvaguardas y graba el resumen ejecutivo
	 */
	public void finalizarReporteSalvaguardas(){
		try{			
			if(getLoginBean().getTipoRol() == 3){
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexIsReported(false);
//				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexExecutiveSummary("");
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexReportedStatus(TipoEstadoReporteEnum.ENPROCESO.getCodigo());
			}else{
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexIsReported(true);
				getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexReportedStatus(TipoEstadoReporteEnum.FINALIZADO.getCodigo());
			}

			getAdvanceExecutionSafeguardsFacade().actualizaAvanceEjecucionGenero(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().setDatosProyecto(false);
			vaciaDatosBusqueda();
			getComponenteBuscarProyectos().getBuscaProyectosBean().setDatosProyecto(false);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setNuevoSeguimiento(false);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setMostrarTabs(false);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "finalizarReporteSalvaguardas " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",  getMensajesController().getPropiedad("error.grabar"));
		}
	}

	public void validaEdicionCobeneficios(int numeroCobeneficio){
		if(numeroCobeneficio == 1){
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().size() == 0)
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().add(new TableResponses());		
		}
	}


	/**
	 * Muestra el cuadro de dialogo para finalizar el reporte de ejecucion
	 * @param salvaguarda
	 */
	public void mostrarDialogoFinalizarReporte(int salvaguarda){
		
		if (salvaguarda == 1){
			if (validaCamposTablasSalvaguardas(salvaguarda)){
				grabarSalvaguardaA();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 2){
			if (validaDatosOpcionSiSalvaguardaB()){
				grabarSalvaguardaB();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 3){
			if (validaDatosOpcionSiSalvaguardaC()){		
				grabarSalvaguardaC();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 4){
			if (validaDatosOpcionSiSalvaguardaD()){
				grabarSalvaguardaD();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 5){
			if (validaDatosOpcionSiSalvaguardaE()){
				grabarSalvaguardaE();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}else if(salvaguarda == 6){
			if (validaDatosOpcionSiSalvaguardaF()){
				grabarSalvaguardaF();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		} else if(salvaguarda == 7){
			if (validaDatosOpcionSiSalvaguardaG()){
				grabarSalvaguardaG();
				if(getLoginBean().getTipoRol() == 3)
					Mensaje.verDialogo("dlgPresentado");
				else
					validaFinalizarReporte();
//					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			}
		}

	}
	public void validaFinalizarReporte(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getProjectsStrategicPartners() == null){
//				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
				getSeguimientoSalvaguardaBean().setListaPresentadosIniciados(new ArrayList<>());
//				List<AdvanceExecutionSafeguards> listaTemp= new ArrayList<>();
				getSeguimientoSalvaguardaBean().setListaPresentadosIniciados(getAdvanceExecutionSafeguardsFacade().listadoProyectosPresentadosIniciados(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards(), 1));
				if(getSeguimientoSalvaguardaBean().getListaPresentadosIniciados().size()==0 && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getProjectsStrategicPartners() == null)
					Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
				else{
					
//					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "",getMensajesController().getPropiedad("error.cerrarProyectos"));
//					Mensaje.actualizarComponente("growl");
					
					Mensaje.verDialogo("dlgReportesNoCerrados");
					Mensaje.actualizarComponente(":form:listaSegEstado");
					
				}
					
			}else if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getProjectsStrategicPartners() != null)
				Mensaje.verDialogo("dlgFinalizarReporteSalvaguarda");
			else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "",getMensajesController().getPropiedad("error.cerrarProyectos"));
				Mensaje.actualizarComponente("growl");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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

		getComponenteBuscarProyectos().getBuscaProyectosBean().setMostrarTabs(false);
		getComponenteBuscarProyectos().getBuscaProyectosBean().setNuevoSeguimiento(false);
		getComponenteBuscarProyectos().getBuscaProyectosBean().setTipoSocio(1);
	}
	/**
	 * Valida nuevas salvaguardas asignadas con el resumen del avance
	 * @param adex AvanceDeEjecucion
	 * @param listaAvance Lista del resumen del avance
	 * @return
	 */
	public List<AdvanceSummary> validaSalvaguardasAsignadas(AdvanceExecutionSafeguards adex,List<AdvanceSummary> listaAvance){
		List<AdvanceSummary> listaTemp = listaAvance;
		try{			
			Map<String,ProjectQuestions> mapaTemp=new HashMap<String,ProjectQuestions>();
			List<ProjectQuestions> listaSalvaguardasAsignadas= new ArrayList<>();
			List<Safeguards> listaSalvaguardas= new ArrayList<>();
			if(adex.getProjects()==null)
				listaSalvaguardasAsignadas = getProjectQuestionsFacade().listaPreguntasPartnerSeleccionadas(adex.getProjectsStrategicPartners().getPspaId());
			else
				listaSalvaguardasAsignadas = getProjectQuestionsFacade().listaPreguntasProyectoSeleccionadas(adex.getProjects().getProjId());
			for(ProjectQuestions sa:listaSalvaguardasAsignadas){
				mapaTemp.put(sa.getSafeguards().getSafeCode(), sa);
			}
			for(Entry<String,ProjectQuestions> sa:mapaTemp.entrySet()){
				listaSalvaguardas.add(sa.getValue().getSafeguards());
			}
			for (Safeguards sa : listaSalvaguardas) {
				boolean encontrado=false;
				for (AdvanceSummary asu : listaAvance) {
					if(sa.getSafeCode().equals(asu.getSafeguards().getSafeCode())){
						encontrado = true;
						break;
					}
				}
				if(!encontrado){
					AdvanceSummary avance= new AdvanceSummary();										
					avance.setSafeguards(sa);
					avance.setAdsuStatus(true);
					avance.setAdsuCreationDate(new Date());
					avance.setAdsuCreatorUser(getLoginBean().getUser().getUserName());
					avance.setAdvanceExecutionSafeguards(adex);
					getAdvanceSummaryFacade().create(avance);
					listaTemp.add(avance);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listaTemp;	
	}
	
	/**
	 * Carga la información de las salvaguardas del proyecto seleccionado.
	 */
	public void cargaDatosProyectoSeleccionado(){
		try{
			getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
			getSeguimientoSalvaguardaBean().setProyectoSeleccionado(getComponenteBuscarProyectos().getBuscaProyectosBean().getProyectoSeleccionado());
			
			getSeguimientoSalvaguardaBean().setListaAvanceResumen(new ArrayList<>());
			if(getComponenteBuscarProyectos().getAdvanceExecution().getAdexId()!=null){				
				getSeguimientoSalvaguardaBean().setListaAvanceResumen(getAdvanceSummaryFacade().listaAvancesPorAvanceEjecucion(getComponenteBuscarProyectos().getAdvanceExecution().getAdexId()));
				getSeguimientoSalvaguardaBean().setListaAvanceResumen(validaSalvaguardasAsignadas(getComponenteBuscarProyectos().getAdvanceExecution(), getSeguimientoSalvaguardaBean().getListaAvanceResumen()));
			}
			if(validaPeriodoReporteProyecto(getSeguimientoSalvaguardaBean().getProyectoSeleccionado(), getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte())){
				getSeguimientoSalvaguardaBean().setCodigoStrategicPartner(getComponenteBuscarProyectos().getBuscaProyectosBean().getCodigoStrategicPartner());
				if(getLoginBean().getTipoRol()==3)
					getSeguimientoSalvaguardaBean().setCodigoStrategicPartner(getComponenteBuscarProyectos().getAdvanceExecution().getProjectsStrategicPartners().getPspaId());
				else if(getLoginBean().getTipoRol()==2){
					if(getComponenteBuscarProyectos().getBuscaProyectosBean().getCodigoStrategicPartner()==null)
						getSeguimientoSalvaguardaBean().setCodigoStrategicPartner(getComponenteBuscarProyectos().getCodigoPartner());
				}

				getSeguimientoSalvaguardaBean().setSocioImplementador(getComponenteBuscarProyectos().getBuscaProyectosBean().getSocioImplementador());
				getSeguimientoSalvaguardaBean().setTabActual(0);

				getSeguimientoSalvaguardaBean().setListaComponentes(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setListaComponentes(getComponentsFacade().listaComponentesActivos(getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId()));
				Components componente = new Components();
				componente.setCompId(1000);
				componente.setCompStatus(true);
				componente.setCompCode("CEO5");
				componente.setCompName("Componentes Operativos");
				getSeguimientoSalvaguardaBean().getListaComponentes().add(componente);

				getSeguimientoSalvaguardaBean().setListaSectoresDisponibles(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresDisponibles());
				getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());

				if(getComponenteBuscarProyectos().getBuscaProyectosBean().isNuevoSeguimiento()){
					AdvanceExecutionSafeguards avance =getAdvanceExecutionSafeguardsFacade().buscarAvanceSalvaguardaGeneroReportado(getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId(), getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()==null?0:getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(), 1, String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde()), String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-12"));
					if(avance == null && getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados().size()>0){
						if(getLoginBean().getTipoRol()==3){
							for (ProjectsStrategicPartners psp : getLoginBean().getListaProyectosDelSocioEstrategico()) {
								if(psp.getProjects().getProjId()== getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId()){
									
									getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
									getSeguimientoSalvaguardaBean().setTabActual(0);
									getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
									

									getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());
									getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexId(null);

									obtenerSalvaguardasAsignadas(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(),0);
									if(cargarSalvaguardas()){
										getComponenteBuscarProyectos().getBuscaProyectosBean().setMostrarTabs(true);
										cargaValoresRespuestaPorSalvaguarda();
										getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
										getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
										getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
										getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
										getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));
										grabarSalvaguardaPrimeraVez();
										break;
									}
								}
							}
							iniciaResumenAvance();
						}else{
							
							getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
							getSeguimientoSalvaguardaBean().setTabActual(0);
							getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
							
							getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());
							getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexId(null);
							if(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()==null)
								obtenerSalvaguardasAsignadas(0,getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId());
							else
								obtenerSalvaguardasAsignadas(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(),0);

							if(cargarSalvaguardas()){
								getComponenteBuscarProyectos().getBuscaProyectosBean().setMostrarTabs(true);
								cargaValoresRespuestaPorSalvaguarda();
								getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
								getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
								getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
								getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
								getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));
								grabarSalvaguardaPrimeraVez();
							}

							iniciaResumenAvance();
						}

					}else{
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  "",getMensajesController().getPropiedad("error.proyectoReportado"));	
						getComponenteBuscarProyectos().getBuscaProyectosBean().setMostrarTabs(false);
					}
				}else{
					
					getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getAdvanceExecution());
					getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
					getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());			
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat(getSeguimientoSalvaguardaBean().getPeriodoDesde()));
					getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getSeguimientoSalvaguardaBean().getAnioReporte()).concat("-").concat("12"));	

					getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
					getSeguimientoSalvaguardaBean().setTabActual(0);
					getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
					

					getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getBuscaProyectosBean().getAdvanceExecution());

					if(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner()==null)
						obtenerSalvaguardasAsignadas(0,getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjId());
					else
						obtenerSalvaguardasAsignadas(getSeguimientoSalvaguardaBean().getCodigoStrategicPartner(),0);

					if(cargarSalvaguardas()){
						getComponenteBuscarProyectos().getBuscaProyectosBean().setMostrarTabs(true);
						cargaValoresRespuestaPorSalvaguarda();
						getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(getComponenteBuscarProyectos().getBuscaProyectosBean().getListaSectoresSeleccionados());
						getSeguimientoSalvaguardaBean().setAnioReporte(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte());
						getSeguimientoSalvaguardaBean().setPeriodoDesde(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
					}

					iniciaResumenAvance();
				}
				recuperaResumenAvance();
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,   "",getMensajesController().getPropiedad("error.fechaPeriodoReporte"));
//				Mensaje.actualizarComponente(":form:growl");
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargaDatosProyectoSeleccionado " + ": ").append(e.getMessage()));
		}
	}
	
	public void iniciaResumenAvance(){
		try{
			if(getComponenteBuscarProyectos().getAdvanceExecution().getAdexId()==null || getSeguimientoSalvaguardaBean().getListaAvanceResumen().size()==0){
				for (ProjectQuestions sa : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
					AdvanceSummary avance = new AdvanceSummary();
					avance.setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getAdvanceExecution());
					avance.setSafeguards(sa.getSafeguards());
					avance.setAdsuCreationDate(new Date());
					avance.setAdsuCreatorUser(getLoginBean().getUser().getUserName());
					avance.setAdsuStatus(true);
					getAdvanceSummaryFacade().create(avance);
					getSeguimientoSalvaguardaBean().getListaAvanceResumen().add(avance);
				}
				
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "iniciaResumenAvance " + ": ").append(e.getMessage()));
		}
	}
	
	public void recuperaResumenAvance(){
		for (AdvanceSummary avance : getSeguimientoSalvaguardaBean().getListaAvanceResumen()) {
			if(avance.getSafeguards().getSafeCode().equals("A"))
				getSeguimientoSalvaguardaBean().setAvanceResumenA(avance);
			else if(avance.getSafeguards().getSafeCode().equals("B"))
				getSeguimientoSalvaguardaBean().setAvanceResumenB(avance);
			else if(avance.getSafeguards().getSafeCode().equals("C"))
				getSeguimientoSalvaguardaBean().setAvanceResumenC(avance);
			else if(avance.getSafeguards().getSafeCode().equals("D"))
				getSeguimientoSalvaguardaBean().setAvanceResumenD(avance);
			else if(avance.getSafeguards().getSafeCode().equals("E"))
				getSeguimientoSalvaguardaBean().setAvanceResumenE(avance);
			else if(avance.getSafeguards().getSafeCode().equals("F"))
				getSeguimientoSalvaguardaBean().setAvanceResumenF(avance);
			else if(avance.getSafeguards().getSafeCode().equals("G"))
				getSeguimientoSalvaguardaBean().setAvanceResumenG(avance);
			
		}
	}
	
	
	public void grabarSalvaguardaPrimeraVez(){
		try{
			organizaSectoresSeleccionados();
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setValueAnswersList(getSeguimientoSalvaguardaBean().getListaValoresRespuestas());
			getAdvanceExecutionSafeguardsFacade().grabarAvanceEjecucionSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			for (AdvanceSummary avance : getSeguimientoSalvaguardaBean().getListaAvanceResumen()) {
				getAdvanceSummaryFacade().create(avance);
			}
			
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaPrimeraVez " + ": ").append(e.getMessage()));
		}
	}

	/**
	 * Obtiene las salvaguardas asignadas al socio estrategico o socio implementador
	 * @param codigoPartner Codigo del socio estrategico
	 * @param codigoProyecto Codigo del proyecto
	 */
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
			for(Entry<String,ProjectQuestions> sa:mapaTemp.entrySet())
				getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas().add(sa.getValue());

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "obtenerSalvaguardasAsignadas " + ": ").append(e.getMessage()));
		}
	}

	public boolean cargarSalvaguardas(){
		boolean existeSalvaguardas=false;
		if(getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()!=null && getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas().size()>0){
			existeSalvaguardas = true;
			getSeguimientoSalvaguardaBean().setDatosProyecto(true);
			numeroSalvaguardas=getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas().size();
			for (ProjectQuestions pq : getSeguimientoSalvaguardaBean().getListaSalvaguardasAsignadas()) {
				if(pq.getSafeguards().getSafeOrder()==1 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaA(true);
				}else if(pq.getSafeguards().getSafeOrder()==2 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaB(true);
				}else if(pq.getSafeguards().getSafeOrder()==3 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaC(true);
				}else if(pq.getSafeguards().getSafeOrder()==4 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaD(true);
				}else if(pq.getSafeguards().getSafeOrder()==5 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaE(true);
				}else if(pq.getSafeguards().getSafeOrder()==6 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaF(true);
				}else if(pq.getSafeguards().getSafeOrder()==7 && pq.getSafeguards().getSafeLevel()==1 && pq.getSafeguards().getSafeParentId()==null){
					getSeguimientoSalvaguardaBean().setSalvaguardaG(true);
				}
			}
		}else{
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.proyectoSinSalvaguardas"));
			existeSalvaguardas = false;			
		}
		return existeSalvaguardas;

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
	
			getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdvanceSectorsList().add(as);
		}

	}
	/**
	 * Ubica el nombre del componente en base al codigo
	 * @param codigoComponente Codigo del componente
	 * @return
	 */
	public String ubicaComponente(int codigoComponente){
		String respuesta="";
		for(Components com:getSeguimientoSalvaguardaBean().getListaComponentes()){
			if(com.getCompId()==codigoComponente){
				respuesta=com.getCompName();
				break;
			}
		}
		return respuesta;
	}
	
	public void habilitaOtroCatalogo121(){
		if(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogo121(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogo121(true);

		}
	}
	public void habilitaOtroCatalogoE371(){
		if(getSeguimientoSalvaguardaBean().getCodigoPublico()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE371(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE371().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoE371(true);
		}
	}
	public void habilitaOtroCatalogoF421(){		
		if(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoF421(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoF421(true);
		}
	}
	public void habilitaOtroCatalogoG461(){		
		if(getSeguimientoSalvaguardaBean().getCodigoRiesgo()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoG461(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoG461(true);
		}
	}
	public void habilitaOtroCatalogoA1(){		
		if(getSeguimientoSalvaguardaBean().getCodigoPlanGobierno()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoA1(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoA1(true);
		}
	}
	public void habilitaOtroCatalogoB81(){		
		if(getSeguimientoSalvaguardaBean().getCodigoActividad()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoB81(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoB81(true);
		}
	}
	public void habilitaOtroCatalogoB41(){		
		if(getSeguimientoSalvaguardaBean().getCodigoModalidad()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoB41(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoB41(true);
		}
	}
	public void habilitaOtroCatalogoB131(){		
		if(getSeguimientoSalvaguardaBean().getCodigoModalidad()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoB131(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoB131(true);
		}
	}
	public void habilitaOtroCatalogoC201(){		
		if(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoC201(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoC201(true);
		}
	}
	public void habilitaOtroCatalogoE351(){		
		if(getSeguimientoSalvaguardaBean().getCodigoHerramienta()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE351(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE351().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoE351(true);
		}
	}
	public void habilitaOtroCatalogoE381(){		
		if(getSeguimientoSalvaguardaBean().getCodigoServicio()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE381(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoE381(true);
		}
	}
	public void habilitaOtroCatalogoE391(){		
		if(getSeguimientoSalvaguardaBean().getCodigoRecursos()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoE391(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoE391(true);
		}
	}
	public void habilitaOtroCatalogoF441(){		
		if(getSeguimientoSalvaguardaBean().getCodigoRiesgo()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoF441(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoF441(true);
		}
	}
	public void habilitaOtroCatalogoG491(){		
		if(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoG491(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoG491(true);
		}
	}
	public void habilitaOtroCatalogoG471(){		
		if(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia()==0){			
			getSeguimientoSalvaguardaBean().setOtroCatalogoG471(false);
		}else{
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareAnotherCatalog("");
			getSeguimientoSalvaguardaBean().setOtroCatalogoG471(true);
		}
	}
	
	public void eliminaRegistroTblAux81(){
		try{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81Aux().size()>=2){
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81Aux().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getAdvanceExecutionSaveguards()!=null)
					getTableResponsesFacade().eliminarRespuestasTabla(getSeguimientoSalvaguardaBean().getRegistroTabla());
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.noEliminar"));
				Mensaje.actualizarComponente(":form:growl");
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaRegistroTblAux81 " + ": ").append(e.getMessage()));
		}
	}
	public void eliminaRegistroTblAux51(){
		try{
			if(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51Aux().size()>=2){
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51Aux().remove(getSeguimientoSalvaguardaBean().getRegistroTabla());
				if(getSeguimientoSalvaguardaBean().getRegistroTabla().getAdvanceExecutionSaveguards()!=null)
					getTableResponsesFacade().eliminarRespuestasTabla(getSeguimientoSalvaguardaBean().getRegistroTabla());
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.noEliminar"));
				Mensaje.actualizarComponente(":form:growl");
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminaRegistroTblAux51 " + ": ").append(e.getMessage()));
		}
	}

	public boolean validaPeriodoReporteProyecto(Projects proyecto,int desde){
		boolean resultado=false;
		String inicioProyecto="";
		String finProyecto="";
		inicioProyecto = proyecto.getProjTermFrom().substring(0, 4);
		finProyecto = proyecto.getProjTermTo().substring(0,4);
		if(desde>=Integer.valueOf(inicioProyecto) && desde<=Integer.valueOf(finProyecto))
			resultado = true;
		else
			resultado = false;
		return resultado;
	}

	public void nuevoCobeneficio(){
		getSeguimientoSalvaguardaBean().setHabilitaCobeneficio(true);
		getSeguimientoSalvaguardaBean().setRegistroTablaCobeneficios(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodProvincia(null);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(null);
		Mensaje.verDialogo("dlgCobeneficios");
	}
	public void nuevoCobeneficioCanton(){
		getSeguimientoSalvaguardaBean().setHabilitaCobeneficio27(true);
		getSeguimientoSalvaguardaBean().setRegistroTablaCobeneficios(new TableResponses());
		getSeguimientoSalvaguardaBean().setCodProvincia(null);
		getSeguimientoSalvaguardaBean().setListaCantones(new ArrayList<>());
		getSeguimientoSalvaguardaBean().setCodCanton(null);
		Mensaje.verDialogo("dlgCobeneficios27");
	}
	public void grabarCobeneficio27(){
		try{
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());					
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(true);
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberOne(), 1));
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareColumnNumberTwo(getSeguimientoSalvaguardaBean().getCodCanton());	
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberTwo(), 2));
			switch(getSeguimientoSalvaguardaBean().getCodigoCobeneficio()){
				case 27:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(40));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 36:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(49));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;	
			}
			Mensaje.ocultarDialogo("dlgCobeneficios27");
			getSeguimientoSalvaguardaBean().setHabilitaCobeneficio(false);
			getSeguimientoSalvaguardaBean().setHabilitaCobeneficio27(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarCobeneficio27 " + ": ").append(e.getMessage()));
		}
	}
	public void grabarCobeneficio(){
		try{
			
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareColumnNumberOne(getSeguimientoSalvaguardaBean().getCodProvincia());					
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(true);
			getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberOne(), 1));
			switch(getSeguimientoSalvaguardaBean().getCodigoCobeneficio()){
				case 1:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(14));
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareColumnNumberFive(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad());
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberFive()));
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 2:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(15));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 3:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(16));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 4:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(17));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 5:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(18));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 6:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(19));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 7:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(20));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 8:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(21));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 9:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(22));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 10:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(23));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 11:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(24));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 12:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(25));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 13:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(26));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 14:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(27));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 15:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(28));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 16:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(29));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 17:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(30));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 18:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(31));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 19:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(32));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 20:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(33));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 21:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(34));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 22:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(35));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 23:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(36));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 24:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(37));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;	
				case 25:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(38));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 26:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(39));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 28:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(41));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 29:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(42));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 30:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(43));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 31:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 32:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(45));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 33:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(46));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 34:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(47));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)						
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 35:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(48));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 37:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(50));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;
				case 38:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(51));																			
					if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareId()==null)
						getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().add(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
					break;	
			}
			Mensaje.ocultarDialogo("dlgCobeneficios");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",  getMensajesController().getPropiedad("info.infoGrabada"));
			getSeguimientoSalvaguardaBean().setHabilitaCobeneficio(false);
			getSeguimientoSalvaguardaBean().setHabilitaCobeneficio27(false);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarCobeneficio " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Edita un cobeneficio de la tabla
	 */
	public void editarCobeneficio(){
		getSeguimientoSalvaguardaBean().setHabilitaCobeneficio(true);
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberOne());
		if(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberFive()!=null)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberFive());
		Mensaje.verDialogo("dlgCobeneficios");			
	}
	public void editarCobeneficio27(){
		getSeguimientoSalvaguardaBean().setHabilitaCobeneficio27(true);
		getSeguimientoSalvaguardaBean().setCodProvincia(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberOne());
		filtraCantones();
		getSeguimientoSalvaguardaBean().setCodCanton(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().getTareColumnNumberTwo());
		Mensaje.verDialogo("dlgCobeneficios27");			
	}
	/**
	 * Elimina el cobeneficio seleccionado
	 */
	public void eliminarCobeneficio(){
		try{
			switch(getSeguimientoSalvaguardaBean().getCodigoCobeneficio()){
				case 1:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_1().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());										
					break;
				case 2:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_2().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 3:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_3().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 4:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_4().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 5:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_5().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 6:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_6().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 7:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_7().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 8:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_8().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 9:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_9().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 10:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_10().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 11:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_11().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 12:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_12().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 13:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_13().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 14:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_14().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 15:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_15().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 16:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_16().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 17:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_17().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 18:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_18().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 19:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_19().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 20:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_20().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 21:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_21().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 22:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_22().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 23:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_23().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 24:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_24().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 25:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_25().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 26:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_26().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 27:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_27().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 28:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_28().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 29:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_29().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 30:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_30().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 31:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_31().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 32:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_32().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 33:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_33().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 34:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_34().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 35:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_35().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 36:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_36().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 37:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_37().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				case 38:				
					getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios().setTareStatus(false);																													
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaE40_38().remove(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());					
					break;
				
			}
			getTableResponsesFacade().agregarEditarNuevaTableResponse(getSeguimientoSalvaguardaBean().getRegistroTablaCobeneficios());
			Mensaje.ocultarDialogo("dlgEliminaCobeneficio");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,   "",getMensajesController().getPropiedad("info.eliminar"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminarCobeneficio " + ": ").append(e.getMessage()));
		}		
	}
	public void nuevoRegistroInfoOpcional(int salvaguarda){
		switch (salvaguarda){
		case 1:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPA(new TableResponses());
			break;
		case 2:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPB(new TableResponses());
			break;	
		case 3:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPC(new TableResponses());
			break;	
		case 4:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPD(new TableResponses());
			break;	
		case 5:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPE(new TableResponses());
			break;	
		case 6:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPF(new TableResponses());
			break;	
		case 7:
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasOPG(new TableResponses());
			break;	
		}
		
	}
}
