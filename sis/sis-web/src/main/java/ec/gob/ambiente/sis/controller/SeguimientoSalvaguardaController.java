package ec.gob.ambiente.sis.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsSafeguardsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
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
import ec.gob.ambiente.sis.utils.Archivos;
import ec.gob.ambiente.sis.utils.Fechas;
import ec.gob.ambiente.sis.utils.HeaderFooterPageEvent;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.OperacionesCatalogo;
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
	
	@Getter
	@Setter
	private int numeroSalvaguardas;
	
	private int nuevaActividad;
	private int nuevaModalidadActividad;
	private int nuevoRiesgo;

	private String rutaPDF;
	
	
	private Users usuario;
	
	@PostConstruct
	public void init(){
		try{
			numeroSalvaguardas=0;
			getSeguimientoSalvaguardaBean().setTabActual(0);
			vaciarValores();			
//			cargarProyectos();
			cargaSalvaguardasPlanAccion();
			cargaSectoresInteres();
			usuario=new Users();
			usuario.setUserName("Christian Báez");
			usuario.setUserId(0);
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
	
	public void cargaSalvaguardasDelProyectoSeleccionado(Projects proyecto){
		try{
			getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
//			if(proyecto.getProjTitle().length()>50)
//				proyecto.setProjTitle(proyecto.getProjTitle().substring(0, 49).concat(".."));
			AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
			avanceEjecucion = getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(proyecto.getProjId());			
			if((avanceEjecucion!=null && !avanceEjecucion.isAdexIsReported()) || avanceEjecucion==null){
				getSeguimientoSalvaguardaBean().setProyectoSeleccionado(proyecto);
				getSeguimientoSalvaguardaBean().setCodigoProyecto(proyecto.getProjId());
				getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
				cargaSalvaguardasProyecto();
				cargarAvanceEjecucionSalvaguarda();
				cargaValoresRespuestaPorSalvaguarda();
				getSeguimientoSalvaguardaBean().setDatosAvanceEjecucion(false);
	//			informacionFormularioPorSalvaguarda(getSeguimientoSalvaguardaBean().getListaSalvaguardasProyecto());
				getSeguimientoSalvaguardaBean().setTabActual(0);
				getSeguimientoSalvaguardaBean().setMapaTabs(new TreeMap<Integer, Integer>());
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.proyectoReportado"));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.cargarProyectos"));
				
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
			getSeguimientoSalvaguardaBean().setListaPreguntas(new ArrayList<>());
			if(lista.size()>0){
//				getSeguimientoSalvaguardaBean().setListaPreguntas(getQuestionsFacade().buscarPreguntasPorSalvaguardas(lista));  //OJO REVISAR ESTA LINEA DE CODIGO
			
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
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
					int salvaguardaB=salvaguarda.getSafeId();
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
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
					int salvaguardaC=salvaguarda.getSafeId();
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
					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
					int salvaguardaD=salvaguarda.getSafeId();
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
					//SALVAGUARDA E
				}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
					int salvaguardaE=salvaguarda.getSafeId();
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
					//SALVAGUARDA F
				}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
					int salvaguardaF=salvaguarda.getSafeId();
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
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
					int salvaguardaG=salvaguarda.getSafeId();
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
				}
			}
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.proyectoSinSalvaguardas"));
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
//		System.out.println(numeroSalvaguardas +"....");
//		System.out.println(getSeguimientoSalvaguardaBean().getTabActual() +"-------");
//		System.out.println(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual());
		if(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual()==1){			
			
			if(codigoSalvaguarda==1){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(codigoSalvaguarda)){				
					grabarSalvaguardaA();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");			
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.tablasSalvaguardaA"));
			}else if(codigoSalvaguarda==2){	
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB()){
					grabarSalvaguardaB();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");					
					Mensaje.verDialogo("dlgResumenEjecutivo");			
					for(int i=0;i<10;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, false);
					}
					renderizaMensajeRegistrosTablaB();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC()){				
					grabarSalvaguardaC();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
					for(int i=0;i<8;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(0, false);
					}
					renderizaMensajeRegistrosTablaC();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				
			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD()){				
					grabarSalvaguardaD();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
					for(int i=0;i<2;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(0, false);
					}
					renderizaMensajeRegistrosTablaD();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE()){				
					grabarSalvaguardaE();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
					for(int i=0;i<6;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(0, false);
					}
					renderizaMensajeRegistrosTablaE();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF()){				
					grabarSalvaguardaF();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
					for(int i=0;i<5;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(0, false);
					}
					renderizaMensajeRegistrosTablaF();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG()){				
					grabarSalvaguardaG();
					Mensaje.actualizarComponente(":form:panelResumenEjecutivo");
					Mensaje.verDialogo("dlgResumenEjecutivo");	
					for(int i=0;i<6;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(0, false);
					}
					renderizaMensajeRegistrosTablaG();
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
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaB()){
					Mensaje.verDialogo("dlgGrabaReporteB");
					for(int i=0;i<10;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasB().set(0, false);
					}
					renderizaMensajeRegistrosTablaB();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==3){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaC()){				
					Mensaje.verDialogo("dlgGrabaReporteC");
					for(int i=0;i<8;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasC().set(0, false);
					}
					renderizaMensajeRegistrosTablaC();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
				
			}else if(codigoSalvaguarda==4){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaD()){				
					Mensaje.verDialogo("dlgGrabaReporteD");
					for(int i=0;i<2;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasD().set(0, false);
					}
					renderizaMensajeRegistrosTablaD();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==5){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaE()){				
					Mensaje.verDialogo("dlgGrabaReporteE");
					for(int i=0;i<6;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasE().set(0, false);
					}
					renderizaMensajeRegistrosTablaE();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==6 ){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaF()){				
					Mensaje.verDialogo("dlgGrabaReporteF");
					for(int i=0;i<5;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasF().set(0, false);
					}
					renderizaMensajeRegistrosTablaF();
				}else
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.seleccionSi"));
			}else if(codigoSalvaguarda==7){
				if(validaDatosAvanceEjecucion() && validaDatosOpcionSiSalvaguardaG()){				
					Mensaje.verDialogo("dlgGrabaReporteG");
					for(int i=0;i<6;i++){
						getSeguimientoSalvaguardaBean().getMensajeRequeridosTablasG().set(0, false);
					}
					renderizaMensajeRegistrosTablaG();
				}else
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
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().setTareColumnDecimalOne(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasA().getTareColumnDecimalOne());
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
		Iterator iteraMJN =getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().iterator();
		while(iteraMJN.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(iteraMJN.next().toString());
			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1));
			respuesta.setTareStatus(true);
			respuestasTabla.add(respuesta);
		}
		Iterator iteraNSN =getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().iterator();
		while(iteraNSN.hasNext()){
			TableResponses respuesta=new TableResponses();
			respuesta.setTareColumnOne(iteraNSN.next().toString());
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

		return respuestasTabla;
	}
//	public List<TableResponses> preparaGrabarComunicaBeneficiariosSalvaguardaB(){
//		List<TableResponses> respuestasTabla=new ArrayList<>();
//		Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().iterator();
//		while(itera.hasNext()){
//			TableResponses respuesta=new TableResponses();
//			respuesta.setTareColumnOne(String.valueOf(itera.next()));
//			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(22));
//			respuesta.setTareStatus(true);
//			respuestasTabla.add(respuesta);
//		}
//
//		return respuestasTabla;
//	}
//	
//	public List<TableResponses> preparaGrabarEjecucionProyectoSalvaguardaB(){
//		List<TableResponses> respuestasTabla=new ArrayList<>();
//		Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado().iterator();
//		while(itera.hasNext()){
//			TableResponses respuesta=new TableResponses();
//			respuesta.setTareColumnOne(String.valueOf(itera.next()));
//			respuesta.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			respuesta.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23));
//			respuesta.setTareStatus(true);
//			respuestasTabla.add(respuesta);
//		}
//
//		return respuestasTabla;
//	}
	

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
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
			listaSalvaguardaA = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 1);
		}
//		getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
//		
//		listaLeyes= getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1).getTableResponsesList().stream().filter((leyes)->leyes.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaLeyes=listaSalvaguardaA.stream().filter((sc)->sc.getQuestions().getQuesId().equals(2)).collect(Collectors.toList());

//		listaPoliticas= getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3).getTableResponsesList().stream().filter((politicas)->politicas.getAdvanceExecutionSaveguards().getAdexId()==getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList());
		listaPoliticas=listaSalvaguardaA.stream().filter((sc)->sc.getQuestions().getQuesId().equals(4)).collect(Collectors.toList());
		listaLeyes.stream().forEach(res->{						
			if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoLeyes(), Integer.valueOf(res.getTareColumnOne())))
				getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
			else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), Integer.valueOf(res.getTareColumnOne())))
				getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
			else if(OperacionesCatalogo.ubicaCodigoEnCatalogo(getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), Integer.valueOf(res.getTareColumnOne())))
				getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().add(Integer.valueOf(res.getTareColumnOne()));
			
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
//		getSeguimientoSalvaguardaBean().setNuevaEjecucion(true);
	}
	public void nuevaComunicacionSB(){
		getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
//		getSeguimientoSalvaguardaBean().setNuevaComunicacion(true);
	}
//	public void noGrabarEjecucionSB(){
//		getSeguimientoSalvaguardaBean().setNuevaEjecucion(false);
//		getSeguimientoSalvaguardaBean().setNuevaEjecucionSB(new Catalogs());
//	}
//	public void noGrabarComunicacionSB(){
//		getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
//		getSeguimientoSalvaguardaBean().setNuevaComunicacion(false);
//	}
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosLey"));
			}else{
				catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional(), getSeguimientoSalvaguardaBean().getNuevoMJN(), usuario.getUserName());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional().add(catalogo);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
				getSeguimientoSalvaguardaBean().setNuevoMJN(new Catalogs());
				getSeguimientoSalvaguardaBean().setNuevoIngresoMJN(false);
			}
		}else if(categoria==3){
			if(getSeguimientoSalvaguardaBean().getNuevaNormativaSN().getCataText1().trim().length()==0){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosLey"));
			}else{
				catalogo=OperacionesCatalogo.agregarNuevoCatalogo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo(), getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional(), getSeguimientoSalvaguardaBean().getNuevaNormativaSN(), usuario.getUserName());
				catalogo=getCatalogsFacade().create(catalogo);
				getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional().add(catalogo);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
				getSeguimientoSalvaguardaBean().setNuevaNormativaSN(new Catalogs());
				getSeguimientoSalvaguardaBean().setNuevoIngresoNSN(false);
			}
		}else if(categoria==4){
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
//	public void cargaCatalogoRiesgo(){
//		try{
//			getSeguimientoSalvaguardaBean().setListaCatalogoRiesgo(new ArrayList<>());
//			getSeguimientoSalvaguardaBean().setListaCatalogoRiesgo(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.RIESGO.getCodigo()));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
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
	
//	public void cargaCatalogoRecursos(){
//		try{
//    		getAplicacionBean().setListaRecursos(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.RECURSOS.getCodigo()));
//    		getAplicacionBean().setListaRecursos(getAplicacionBean().getListaRecursos().stream().sorted((r1,r2)->r1.getCataText1().compareTo(r2.getCataText1())).collect(Collectors.toList()));
//		}catch(Exception e){
//			
//		}
//	}
	
	
	
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
		if(getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().size()==0){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosAvanceEjecucion"));			
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTwo()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTree()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoModalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFive()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFive())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnSix())));
					}
					
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoModalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTwo()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnTree()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()), 3));			
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFive()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFive())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnSix())));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSix("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareGenericoTres("");
					}

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
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB41 " + ": ").append(e.getMessage()));
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
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB51 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
			
	public void agregarFilasTablaSalvaguardaB61(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(5));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB61());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB61(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB61(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB61 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}

	public void agregarFilasTablaSalvaguardaB62(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(6));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB62().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB62());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB62(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB62(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB62 " + ": ").append(e.getMessage()));
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnSeven()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoTres(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFive())));
				}

				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81());

			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnSeven()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareGenericoTres(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFive())));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnFive("");
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
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB81 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB9(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(11));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFive())));
				}

				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9());

			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFive())));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnFive("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareGenericoDos("");
				}
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnTree()), 3));
				
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB9(new TableResponses());
			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB9(false);
			
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB9 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}

	
	public void agregarFilasTablaSalvaguardaB102(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive()) , getAplicacionBean().getListaAutoIdentificacion()) );
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoEstado()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(13));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTree()), 3));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoTres(ubicaDatosEstado(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnEight())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());

			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive()) , getAplicacionBean().getListaAutoIdentificacion()) );
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoEstado()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTree()), 3));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().setTareGenericoTres(ubicaDatosEstado(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnEight())));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB102(new TableResponses());
			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
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
	
//	public void agregarFilasTablaSalvaguardaB103(){
//		try{
//			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().getTareId()==null){
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14));		
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103().setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103());
//				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103());
//			}else{
//				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB103());
//			}
//			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB103(new TableResponses());
//			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB103(false);
//			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
//		}catch(Exception e){
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB103 " + ": ").append(e.getMessage()));
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
//		}
//	}
	
	public void agregarFilasTablaSalvaguardaB11(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(16));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB11());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB11(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB11(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB11 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaB143(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(22));	
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoActividad()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnFour(String.join(",", getSeguimientoSalvaguardaBean().getInformaComunicaSeleccionadas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnFive(String.join(",", getSeguimientoSalvaguardaBean().getInformaEjecucionSeleccionadas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
			}else{
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoActividad()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnFour(String.join(",", getSeguimientoSalvaguardaBean().getInformaComunicaSeleccionadas()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().setTareColumnFive(String.join(",", getSeguimientoSalvaguardaBean().getInformaEjecucionSeleccionadas()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB143(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB143(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaB143 " + ": ").append(e.getMessage()));
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
	
	public void nuevoRegistroTablaA1(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasA(new TableResponses());
	}

	public void agregarFilasTablaSalvaguardaB121(){
		try{
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				if(validaDatosAvanceEjecucion())
					grabarSalvaguardaB();				
			}else{
				if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareId()==null){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(18));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareStatus(true);
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFour()), getAplicacionBean().getListaPueblosNacionalidades()));	
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(ubicaConformacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFive())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTwo()), getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
					
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());				
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFour()), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFour("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoCuatro("");
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoConformacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccionImplementada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoDos(ubicaConformacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFive())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTwo()), getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB121(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB121(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(0);
				getSeguimientoSalvaguardaBean().setCodigoAccionImplementada(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(20));		
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoActividad()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnOne(String.join(",", getSeguimientoSalvaguardaBean().getModalidadesSeleccionadas()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareStatus(true);
				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
			}else{
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoActividad()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().setTareColumnOne(String.join(",", getSeguimientoSalvaguardaBean().getModalidadesSeleccionadas()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB131(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaB131(false);
			getSeguimientoSalvaguardaBean().setCodigoActividad(0);
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));					
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()) == 54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFive())));
					}
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTwelve(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso()));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTwelve()),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso() ));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
					
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()) == 54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFive())));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoDos("");
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareColumnTwelve(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoAccesoRecurso()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTwelve()),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso() ));
					
					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC201(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC201(false);
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}
				
				
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFour())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareGenericoDos("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().setTareColumnFive("");
				}
					
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC211(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC211(false);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
			
			vaciarDatosProvinciaCantonParroquia();
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC241().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(5));
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
	
	public void agregarFilasTablaSalvaguardaC242(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(6));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
			}else{
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC242());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC242(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC242(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC242 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaC26(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(8));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoLineaAccion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareColumnOne()), getAplicacionBean().getListaLineaAccion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoLineaAccion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC26().getTareColumnOne()), getAplicacionBean().getListaLineaAccion()));
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenerico( OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTree()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTree()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnFour())));
				}
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoLineaAccion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoTres( OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnOne()),getAplicacionBean().getListaLineaAccion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareStatus(true);

				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenerico( OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTree()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTree()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnFour())));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnFour("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoDos("");
				}
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoLineaAccion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().setTareGenericoTres( OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnOne()),getAplicacionBean().getListaLineaAccion()));

				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC271(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC271(false);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(11));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnTwo())));
				}	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareStatus(true);

				
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnTwo())));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareColumnTwo("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().setTareGenericoDos("");
				}
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoTres(ubicaResultadoAcuerdo(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));
				
				
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoDos("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnSix("");
				}
				
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenerico(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareGenericoTres(ubicaResultadoAcuerdo(getSeguimientoSalvaguardaBean().getCodigoTipoResultadoAcuerdo()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC291(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC291(false);
			vaciarDatosProvinciaCantonParroquia();
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setCodigoTipoResultadoAcuerdo(0);
			getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC291 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
//	public void agregarFilasTablaSalvaguardaC293(){
//		try{
//			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().getTareId()==null){
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17));			
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodCanton()));					
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareStatus(true);
//				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
//				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
//			}else{
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodCanton()));					
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
//				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293());
//			}
//			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC293(new TableResponses());
//			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC293(false);
//
////			getSeguimientoSalvaguardaBean().setCodigoCanton("");
//			getSeguimientoSalvaguardaBean().setCodCanton(0);
//			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
//		}catch(Exception e){
//			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaC293 " + ": ").append(e.getMessage()));
//			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
//		}
//	}
	
	public void agregarFilasTablaSalvaguardaC301(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareId()==null){
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));

				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15));	
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
			}else{
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscarCanton(getSeguimientoSalvaguardaBean().getCodigoCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));

				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC301(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC301(false);
			vaciarDatosProvinciaCantonParroquia();
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareCanton(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodCanton(), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareParroquia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodParroquia(), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnFour())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}
				
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
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnFour())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareGenericoDos("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().setTareColumnFive("");
				}

				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasC311(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaC311(false);
			getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
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
			
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnFour())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
			}
			//			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
//			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnSeven())));
			
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnFour()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnFour())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos(ubicaPuebloNacionalidad(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenericoDos("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnFive("");
				}
				//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnSeven())));

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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPB().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(23));	
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPC().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(18));	
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPF().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(10));	
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasOPG().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(15));	
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
		getSeguimientoSalvaguardaBean().setCodigoTipoEstado(0);
	}
	
	/**
	 * Permite agregar nueva fila a la tabla B81
	 */
	public void nuevaFilaTablaSalvaguardaB81(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB81(new TableResponses());

//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnEight("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnSix("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().setTareColumnSeven("0");

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoAccion(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaB9(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB9(new TableResponses());

//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnEight("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnSix("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().setTareColumnSeven("0");

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
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnSix("0");
		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().setTareColumnSeven("0");
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

//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSix("0");
//		getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().setTareColumnSeven("0");

		vaciarDatosProvinciaCantonParroquia();
		getSeguimientoSalvaguardaBean().setCodigoModalidad(0);
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
		getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
		getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
	}
	public void nuevaFilaTablaSalvaguardaB51(){
		getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasB51(new TableResponses());
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
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTree()),getAplicacionBean().getListaPueblosNacionalidades() ));
				}
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoInvolucramiento() ));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoDos(ubicaInvolucramiento(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnSix())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnSix(String.join(",", getSeguimientoSalvaguardaBean().getNivelInvolucramientoSeleccionados()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
				getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
			}else{
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTree()),getAplicacionBean().getListaPueblosNacionalidades() ));
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnTree("");
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoTres("");
				}
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoInvolucramiento() ));
//				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenericoDos(ubicaInvolucramiento(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnSix())));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareColumnSix(String.join(",", getSeguimientoSalvaguardaBean().getNivelInvolucramientoSeleccionados()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())));
				getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331());
			}
			getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasD331(new TableResponses());
			getSeguimientoSalvaguardaBean().setNuevoRegistroTablaD331(false);
			getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
			getSeguimientoSalvaguardaBean().setCodigoTipoInvolucramiento(0);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaD331 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}

	}
	
	public void agregarFilasTablaSalvaguardaE341(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareId()==null){
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoAreaConsolidada()));
			
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne()), 1));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo()), 2));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree()), 3));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareGenerico(ubicaTipoAreaConsolidada(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnSix())));
			getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareStatus(true);
			getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());

			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341());
			}else{
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoAreaConsolidada()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().setTareGenerico(ubicaTipoAreaConsolidada(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnSix())));
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
			getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(0);
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
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive())==54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnSix()), getAplicacionBean().getListaPueblosNacionalidades()));
				}
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
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnSix()), getAplicacionBean().getListaPueblosNacionalidades()));
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareColumnSix("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().setTareGenericoDos("");
					}
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(10));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoServicio()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnFive()),getSeguimientoSalvaguardaBean().getListaServicio()));
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE381().getTareColumnFive()),getSeguimientoSalvaguardaBean().getListaServicio()));

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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE391().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(12));
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFive()), getAplicacionBean().getListaPueblosNacionalidades()));	
				}
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()) == 54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFive()), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().setTareGenericoDos("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF411(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF411(false);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarFilasTablaSalvaguardaF411 " + ": ").append(e.getMessage()));
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabar"));
		}
	}
	
	public void agregarFilasTablaSalvaguardaF421(){
		try{
			if(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareId()==null){
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFive()), getAplicacionBean().getListaPueblosNacionalidades()));	
				}
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareProvincia(buscaProvinciaCantonParroquia(getSeguimientoSalvaguardaBean().getCodProvincia(), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenerico(buscaCatalogoMedidaTomada(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoMedidaTomada()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()) == 54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFive()), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().setTareGenericoTres("");
					}

					
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRiesgo()));
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()), 1));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()), 2));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()), 3));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnSeven()), getAplicacionBean().getListaCatalogoRiesgo()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()) == 54){
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFive()), getAplicacionBean().getListaPueblosNacionalidades()));	
				}
				
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareStatus(true);
				getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());

				getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}else{
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRiesgo()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnSeven()), getAplicacionBean().getListaCatalogoRiesgo()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()) == 54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFive()), getAplicacionBean().getListaPueblosNacionalidades()));	
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareColumnFive("");
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().setTareGenericoTres("");
					}

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasF441(new TableResponses());

				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaF441(false);
				getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
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
				getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF452().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(9));
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRiesgo()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnTree()), getAplicacionBean().getListaCatalogoRiesgo()));
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());
				}else
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoRiesgo()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnTree()), getAplicacionBean().getListaCatalogoRiesgo()));

					getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461());			
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG461(new TableResponses());
				getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG461(false);
				getSeguimientoSalvaguardaBean().setCodigoRiesgo(0);
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFive())));
					}
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia()));
					
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSeven()), getAplicacionBean().getListaControlVigilancia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
						if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour())==54){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFive())));
						}else{
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnFive("");
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos("");
						}
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoControlVigilancia()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFive()), getAplicacionBean().getListaAutoIdentificacion()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSix())));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSeven()), getAplicacionBean().getListaControlVigilancia()));

						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnOne()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()), 3));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().setTareGenerico(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSix())));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG471(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG471(false);
					getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
					getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
					getSeguimientoSalvaguardaBean().setCodigoControlVigilancia(0);
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()), 3));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoInstitucion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccion()));
//					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(ubicaInstitucion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoTres(ubicaAccion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnEight())));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFive())));
					}
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnSeven(String.join(",", getSeguimientoSalvaguardaBean().getInstitucionAcompaniaSeleccionados()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareStatus(true);
					
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()), 3));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoTipoInstitucion()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAccion()));
//						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(ubicaInstitucion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne())));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnSeven(String.join(",", getSeguimientoSalvaguardaBean().getInstitucionAcompaniaSeleccionados()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoTres(ubicaAccion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnEight())));
						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFour(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
						if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour())==54){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFive())));
						}else{
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().setTareColumnFive("");
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
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
					
					
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix())));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven()), getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnEight()), getAplicacionBean().getListaPueblosNacionalidades()));
						
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAlternativaEconomica()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnSeven(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));
						
					
						if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven())==54){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnEight(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnEight()), getAplicacionBean().getListaPueblosNacionalidades()));
							
						}else{
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoTres("");
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareColumnEight("");
						}
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()), 3));						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix())));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven()), getAplicacionBean().getListaAutoIdentificacion()));
						
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491());
					}
					getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG491(new TableResponses());
	
					vaciarDatosProvinciaCantonParroquia();
					getSeguimientoSalvaguardaBean().setNuevoRegistroTablaG491(false);
					getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(0);
					getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
					getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
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
								
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(12));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne()), 1));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTwo()), 2));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()), 3));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
					if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive())==54){
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));					
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnSix())));
					}
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTen(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoRemotoSeleccionados()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnEleven(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoInSituSeleccionados()));
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwelve(String.join(",", getSeguimientoSalvaguardaBean().getPeriodicidadSeleccionados()));
					
					getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareStatus(true);
					getTableResponsesFacade().create(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
	
					getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().add(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
					}else{
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnOne(String.valueOf(getSeguimientoSalvaguardaBean().getCodProvincia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwo(String.valueOf(getSeguimientoSalvaguardaBean().getCodCanton()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTree(String.valueOf(getSeguimientoSalvaguardaBean().getCodParroquia()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne()), 1));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTwo()), 2));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()), 3));
						
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnFive(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()));					
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
						if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive())==54){
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnSix(String.valueOf(getSeguimientoSalvaguardaBean().getCodigoPuebloNacionalidad()));					
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnSix())));
						}else{
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnSix("");					
							getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareGenericoDos("");

						}
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTen(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoRemotoSeleccionados()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnEleven(String.join(",", getSeguimientoSalvaguardaBean().getMonitoreoInSituSeleccionados()));
						getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().setTareColumnTwelve(String.join(",", getSeguimientoSalvaguardaBean().getPeriodicidadSeleccionados()));
						getTableResponsesFacade().edit(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512());
				}
				getSeguimientoSalvaguardaBean().setRegistroTablaRespuestasG512(new TableResponses());
	
				vaciarDatosProvinciaCantonParroquia();
				getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
				getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(0);
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
//			List<TableResponses> datosRespuestasTablasCheck=Stream.concat(preparaGrabarComunicaBeneficiariosSalvaguardaB().stream(), preparaGrabarEjecucionProyectoSalvaguardaB().stream()).collect(Collectors.toList());
			List<TableResponses> datosRespuestasTablasCheck= new ArrayList<>();
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
		try{
		cargaCatalogoModalidad();
		cargaCatalogoActividad();
		cargaCatalogoAccionImplementada();

		List<TableResponses> listaSalvaguardaB=new ArrayList<>();
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
			listaSalvaguardaB = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 2);
		
		getSeguimientoSalvaguardaBean().setListaPreguntasB(getSeguimientoSalvaguardaBean().getListaPreguntasB().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(7)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnFour()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnOne()), getSeguimientoSalvaguardaBean().getListaCatalogoModalidad()));
			res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnFive()) == 54){
				res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnSix()), getAplicacionBean().getListaPueblosNacionalidades()));
			}
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB81(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(16)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnSeven()), getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
			res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.parseInt(res.getTareColumnFour()) == 54)
				res.setTareGenericoTres(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB9(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(17)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.parseInt(res.getTareColumnFour())==54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(20)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.parseInt(res.getTareColumnFive())==54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnSix())));
			
			res.setTareGenericoTres(ubicaDatosEstado(Integer.parseInt(res.getTareColumnEight())));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(18).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB121(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(27)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121().forEach(res->{
			res.setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(res.getTareColumnTree())));
			res.setTareGenericoDos(ubicaConformacion(Integer.parseInt(res.getTareColumnFive())));
			res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnTwo()),getSeguimientoSalvaguardaBean().getListaCatalogoAccionImplementada()));
			if(Integer.valueOf(res.getTareColumnTree())==54){				
				res.setTareGenericoCuatro(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnFour()), getAplicacionBean().getListaPueblosNacionalidades()));	
			}
		});
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(16).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(23)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(9)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(11)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB62(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(6).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB62(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB62().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB62(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(12)).collect(Collectors.toList()));

//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));;
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB71(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(14)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(14).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(21)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(20).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB131(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(29)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131().forEach(res->{			
//			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnOne()),getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
//		});
		
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(22).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaB143(listaSalvaguardaB.stream().filter((sc)->sc.getQuestions().getQuesId().equals(30)).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143().forEach(res->{			
//			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnOne()),getSeguimientoSalvaguardaBean().getListaCatalogoActividad()));
//		});
		
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPB(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(24).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPB(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPB().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
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
		if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
			listaSalvaguardaC = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 9);
//		System.out.println(listaSalvaguardaC.size());
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(43)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnFour())==54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(res.getTareColumnFive())));
			res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnTwelve()),getSeguimientoSalvaguardaBean().getListaCatalogoTipoAcceso()));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(45)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnFour()) == 54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(res.getTareColumnFive())));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC291(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(57)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnFive())==54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnSix())));
			res.setTareGenericoTres(ubicaResultadoAcuerdo(Integer.parseInt(res.getTareColumnSeven())));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(59)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().forEach(res->{
			
			res.setTareCanton(buscarCanton(Integer.parseInt(res.getTareColumnOne())));
			
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(21).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC311(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(63)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnFour())==54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
		});
		

//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(49)).collect(Collectors.toList()));
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(8).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(50)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));

		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC26(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(52)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26().forEach(res->{
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnOne()),getAplicacionBean().getListaLineaAccion()));
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC271(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(54)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271().forEach(res->{
			res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnOne()),getAplicacionBean().getListaLineaAccion()));
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnTree()), getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnTree()) == 54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFour())));
			
		});
		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC28(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(55)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28().forEach(res->{			
			res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnOne()),getAplicacionBean().getListaAutoIdentificacion()));
			if(Integer.valueOf(res.getTareColumnOne())==54)
				res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnTwo())));			
		});

		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(19).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().setTablaSalvaguardaC301(listaSalvaguardaC.stream().filter((sc)->sc.getQuestions().getQuesId().equals(61)).collect(Collectors.toList()));
		getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301().forEach(res->{
			res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
			res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
			res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
		});

		
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPC(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(22).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//		getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPC(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPC().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
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
			e.printStackTrace();
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.grabarSalvaguarda"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarSalvaguardaD " + ": ").append(e.getMessage()));
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
		try{
			List<TableResponses> listaSalvaguardaD=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaD = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 16);

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD321(listaSalvaguardaD.stream().filter((sc)->sc.getQuestions().getQuesId().equals(65)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(res.getTareColumnFour())));
				if(Integer.valueOf(res.getTareColumnFour())==54)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnFive()),getAplicacionBean().getListaPueblosNacionalidades()));
				else
					res.setTareGenericoDos("");
			});
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaD331(listaSalvaguardaD.stream().filter((sc)->sc.getQuestions().getQuesId().equals(67)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331().forEach(res->{
				res.setTareGenerico(ubicaAutoidentificacion(Integer.parseInt(res.getTareColumnTwo())));
//				res.setTareGenericoDos(ubicaInvolucramiento(Integer.parseInt(res.getTareColumnSix())));
				if(Integer.valueOf(res.getTareColumnTwo())==54)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnTree()),getAplicacionBean().getListaPueblosNacionalidades()));
				else
					res.setTareGenericoTres("");
			});
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPD(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPD(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPD().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
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
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaE = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 20);
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE341(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(69)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaTipoAreaConsolidada(Integer.parseInt(res.getTareColumnSix())));
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE351(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(71)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351().forEach(res->{
				res.setTareGenerico(buscaNivelOrganizacion(res.getTareColumnOne()));
				res.setTareGenericoDos(ubicaHerramienta(Integer.parseInt(res.getTareColumnTwo())));
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE361(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(73)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaAutoidentificacion(Integer.valueOf(res.getTareColumnFive())));
				if(Integer.valueOf(res.getTareColumnFive())==54){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE371(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(75)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaMetodo(Integer.valueOf(res.getTareColumnSix())));
				res.setTareGenericoDos(ubicaPublico(Integer.valueOf(res.getTareColumnSeven())));
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(9).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE381(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(78)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnFive()),getSeguimientoSalvaguardaBean().getListaServicio()));
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(11).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaE391(listaSalvaguardaE.stream().filter((sc)->sc.getQuestions().getQuesId().equals(80)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaRecurso(Integer.parseInt(res.getTareColumnFive())));
				res.setTareGenericoDos(ubicaPeriodicidad(Integer.parseInt(res.getTareColumnSix())));
			});
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPE(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPE(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPE().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
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
//			cargaCatalogoRiesgo();
			List<TableResponses> listaSalvaguardaF=new ArrayList<>();
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null)
				listaSalvaguardaF = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 24);
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF411(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(113)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaAutoidentificacion(Integer.valueOf(res.getTareColumnFour())));
				if(Integer.valueOf(res.getTareColumnFour())==54){
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF421(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(115)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(buscaCatalogoMedidaTomada(Integer.parseInt(res.getTareColumnEight())));
				res.setTareGenericoDos(ubicaAutoidentificacion(Integer.valueOf(res.getTareColumnFour())));
				if(Integer.valueOf(res.getTareColumnFour())==54){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});		
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF431(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(117)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431().forEach(res->{
				res.setTareGenerico(ubicaSistema(Integer.parseInt(res.getTareColumnOne())));
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF441(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(119)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnSeven()), getAplicacionBean().getListaCatalogoRiesgo()));
				res.setTareGenericoDos(ubicaAutoidentificacion(Integer.valueOf(res.getTareColumnFour())));
				if(Integer.valueOf(res.getTareColumnFour())==54){
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()),getAplicacionBean().getListaPueblosNacionalidades()));
				}
			});
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(listaSalvaguardaF.stream().filter((sc)->sc.getQuestions().getQuesId().equals(122)).collect(Collectors.toList()));

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPF(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(11).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPF(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPF().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
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
			if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null) 
				listaSalvaguardaG = getTableResponsesFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 29);
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(124)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461().forEach(res->{			
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.parseInt(res.getTareColumnTree()),getAplicacionBean().getListaCatalogoRiesgo()));
			});

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(126)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.parseInt(res.getTareColumnTree()), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(res.getTareColumnFour())==54){
					res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
				}
				res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnSeven()), getAplicacionBean().getListaControlVigilancia()));
			});
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(4).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(127)).collect(Collectors.toList()));

//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG481(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(129)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFour()), getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(res.getTareColumnFour())==54){
					res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.parseInt(res.getTareColumnFive())));
				}	
				res.setTareGenericoTres(ubicaAccion(Integer.valueOf(res.getTareColumnEight())));
			});
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG491(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(131)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
				res.setTareGenerico(ubicaAlternativaEconomica(Integer.valueOf(res.getTareColumnSix())));
				res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnSeven()),getAplicacionBean().getListaAutoIdentificacion() ));
				if(Integer.valueOf(res.getTareColumnSeven())==54)
					res.setTareGenericoTres(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnEight()),getAplicacionBean().getListaPueblosNacionalidades() ));
			});
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG501(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(133)).collect(Collectors.toList()));
			
			
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(26).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList())); 
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().setTablaSalvaguardaG512(listaSalvaguardaG.stream().filter((sc)->sc.getQuestions().getQuesId().equals(136)).collect(Collectors.toList()));
			getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512().forEach(res->{
				res.setTareProvincia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnOne()), 1));
				res.setTareCanton(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTwo()), 2));
				res.setTareParroquia(buscaProvinciaCantonParroquia(Integer.valueOf(res.getTareColumnTree()), 3));
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(Integer.valueOf(res.getTareColumnFive()),getAplicacionBean().getListaAutoIdentificacion()));
				if(Integer.valueOf(res.getTareColumnFive())==54)
					res.setTareGenericoDos(ubicaPuebloNacionalidad(Integer.valueOf(res.getTareColumnSix())));
				
			});


//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPG(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(36).getTableResponsesList().stream().filter((r)->r.getAdvanceExecutionSaveguards().getAdexId()==seguimientoSalvaguardaBean.getAdvanceExecutionSafeguards().getAdexId()).collect(Collectors.toList()));
//			getSeguimientoSalvaguardaBean().setTablaSalvaguardaOPG(getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPG().stream().filter((sb)->sb.isTareStatus()).collect(Collectors.toList()));
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
					getSeguimientoSalvaguardaBean().setCatalogoLeyes(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICOINTERNACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoMarcoJuridicoNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MARCOJURIDICONACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setCatalogoNormativaSecundariaNacional(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.NORMATIVASECUNDARIANACIONAL.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasA(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasA(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaA));
//					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaA).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasA(getSeguimientoSalvaguardaBean().getListaPreguntasA().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
					if(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards()!=null && getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
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
					//salvaguarda B
				}else if(salvaguarda.getSafeOrder()==2 && salvaguarda.getSafeLevel()==1){
					int salvaguardaB=salvaguarda.getSafeId();
//					cargarProvincias();
					getSeguimientoSalvaguardaBean().setListaPreguntasB(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasB(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaB));
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
					//SALVAGUARDA C
				}else if(salvaguarda.getSafeOrder()==3 && salvaguarda.getSafeLevel()==1){
					int salvaguardaC=salvaguarda.getSafeId();
					
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
					}else{						
						if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC()==null || getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().isEmpty()){
//							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaC).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasC(getSeguimientoSalvaguardaBean().getListaPreguntasC().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
							valoresRespuestasPorDefecto(getSeguimientoSalvaguardaBean().getListaPreguntasC(), getSeguimientoSalvaguardaBean().getListaValoresRespuestasC());
						}						
					}

					//SALVAGUARDA D
				}else if(salvaguarda.getSafeOrder()==4 && salvaguarda.getSafeLevel()==1){
					int salvaguardaD=salvaguarda.getSafeId();
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
				}else if(salvaguarda.getSafeOrder()==5 && salvaguarda.getSafeLevel()==1){
					int salvaguardaE=salvaguarda.getSafeId();
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
				}else if(salvaguarda.getSafeOrder()==6 && salvaguarda.getSafeLevel()==1){
					int salvaguardaF=salvaguarda.getSafeId();
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setListaCatalogoMedidaTomada(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MEDIDATOMADA.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasF(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasF(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaF));
					
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
					//SALVAGUARDA G
				}else if(salvaguarda.getSafeOrder()==7 && salvaguarda.getSafeLevel()==1){
					int salvaguardaG=salvaguarda.getSafeId();
//					cargarProvincias();
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoRemoto(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOREMOTO.getCodigo()));
//					getSeguimientoSalvaguardaBean().setCatalogoMonitoreoInSitu(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo()));
					getSeguimientoSalvaguardaBean().setListaPreguntasG(new ArrayList<>());
					getSeguimientoSalvaguardaBean().setListaPreguntasG(getQuestionsFacade().buscaPreguntaPorSalvaguarda(salvaguardaG));
					
//					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
//					getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
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
//							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntas().stream().filter((q)->q.getSafeguards().getSafeId()==salvaguardaG).collect(Collectors.toList()));
//							getSeguimientoSalvaguardaBean().setListaPreguntasG(getSeguimientoSalvaguardaBean().getListaPreguntasG().stream().sorted((q1,q2)->q1.getQuesQuestionOrder().compareTo(q2.getQuesQuestionOrder())).collect(Collectors.toList()));
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
		getSeguimientoSalvaguardaBean().setCodigoModalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnOne()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFive()));

		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFive())==54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnSix()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}

		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB41().getTareColumnFour()) != 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFive()));
		}
	}
	
	public void editarTablaB81(){

		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnOne()));
		
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoActividad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnSeven()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFour()));
		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFour()) != 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB81().getTareColumnFive()));
		}
	}
	public void editarTablaB9(){

		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnOne()));
		
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFour()));
		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFour()) != 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB9().getTareColumnFive()));
		}
		
	}
	
	public void editarTablaB102(){

		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive()));
		if (Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnFive())==54)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnSix()));
		getSeguimientoSalvaguardaBean().setCodigoTipoEstado(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB102().getTareColumnEight()));
	}
	public void editarTablaB121(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTree())==54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFour()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}
		getSeguimientoSalvaguardaBean().setCodigoTipoConformacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnFive()));
		getSeguimientoSalvaguardaBean().setCodigoAccionImplementada(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB121().getTareColumnTwo()));
	}
	public void editarTablaB131(){
//		getSeguimientoSalvaguardaBean().setCodigoActividad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnOne()));
		getSeguimientoSalvaguardaBean().setModalidadesSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB131().getTareColumnOne().split(","));
		
	}
	public void editarTablaB143(){
//		getSeguimientoSalvaguardaBean().setCodigoActividad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnOne()));
		getSeguimientoSalvaguardaBean().setInformaComunicaSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnFour().split(","));
		getSeguimientoSalvaguardaBean().setInformaEjecucionSeleccionadas(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasB143().getTareColumnFive().split(","));
		
	}
	
	public void editarTablaC201(){		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnOne()));
		filtraCantones();	
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()));
		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFour()) != 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnFive()));
		}
		
		getSeguimientoSalvaguardaBean().setCodigoTipoAccesoRecurso(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC201().getTareColumnTwelve()));
	}
		
	public void editarTablaC211(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFour()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFour()) != 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}else{
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC211().getTareColumnFive()));
		}
		
		
		
	}
	public void editarTablaC271(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoLineaAccion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnOne()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnTree()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC271().getTareColumnFour()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaC28(){				
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne()));
		if (Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnOne())==54)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC28().getTareColumnTwo()));
	}

	public void editarTablaC291(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnFive())==54)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnSix()));
		getSeguimientoSalvaguardaBean().setCodigoTipoResultadoAcuerdo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC291().getTareColumnSeven()));
	}
	
	public void editarTablaC293(){
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC293().getTareColumnOne()));		
	}
	
	public void editarTablaC301(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC301().getTareColumnTree()));
	}
	
	public void editarTablaC311(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnTree()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasC311().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaD321(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnTree()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD321().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	
	public void editarTablaD331(){
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo()));
//		getSeguimientoSalvaguardaBean().setCodigoTipoInvolucramiento(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnSix()));
		getSeguimientoSalvaguardaBean().setNivelInvolucramientoSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnSix().split(","));
		if(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTwo())==54)
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasD331().getTareColumnTree()));
		
	}
	
	public void editarTablaE341(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoTipoAreaConsolidada(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE341().getTareColumnSix()));
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
		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnFive()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasE361().getTareColumnSix()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
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
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()));
		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF411().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaF421(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnOne()));
		filtraCantones();	
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnTree()));
		
		getSeguimientoSalvaguardaBean().setCodigoMedidaTomada(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnEight()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF421().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaF431(){
		getSeguimientoSalvaguardaBean().setCodigoSistemas(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF431().getTareColumnOne()));
	}
	public void editarTablaF441(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnSeven() ));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasF441().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaG461(){
		getSeguimientoSalvaguardaBean().setCodigoRiesgo(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG461().getTareColumnTree()));
	}	
	
	public void editarTablaG471(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnTree()));
		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()));
		getSeguimientoSalvaguardaBean().setCodigoControlVigilancia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnSeven()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG471().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
		
	}
	public void editarTablaG481(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnTree()));
//		getSeguimientoSalvaguardaBean().setCodigoTipoInstitucion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnOne()));
		getSeguimientoSalvaguardaBean().setInstitucionAcompaniaSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnSeven().split(","));
		getSeguimientoSalvaguardaBean().setCodigoAccion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnEight()));
		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()));		
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFour()) == 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.parseInt(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG481().getTareColumnFive()));
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(true);
		}else{
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(0);
			getSeguimientoSalvaguardaBean().setHabilitaPuebloNacionalidad(false);
		}
	}
	public void editarTablaG491(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnTree()));
		getSeguimientoSalvaguardaBean().setCodigoAlternativaEconomica(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSix()));
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnSeven())==54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG491().getTareColumnEight()));			
			habilitaPuebloNacionalidad();
		}
	}
	
	public void editarTablaG512(){
		
		getSeguimientoSalvaguardaBean().setCodProvincia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnOne()));
		filtraCantones();		
		getSeguimientoSalvaguardaBean().setCodCanton(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTwo()));
		filtraParroquias();
		getSeguimientoSalvaguardaBean().setCodParroquia(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTree()));		
		getSeguimientoSalvaguardaBean().setCodigoAutoIdentificacion(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive()));
		if(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnFive())== 54){
			getSeguimientoSalvaguardaBean().setCodigoPuebloNacionalidad(Integer.valueOf(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnSix()));
			habilitaPuebloNacionalidad();
		}
		getSeguimientoSalvaguardaBean().setMonitoreoRemotoSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTen().split(","));
		getSeguimientoSalvaguardaBean().setMonitoreoInSituSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnEleven().split(","));
		getSeguimientoSalvaguardaBean().setPeriodicidadSeleccionados(getSeguimientoSalvaguardaBean().getRegistroTablaRespuestasG512().getTareColumnTwelve().split(","));
	}
	
	public void siguienteTab(int salvaguarda){
		if(!(numeroSalvaguardas-getSeguimientoSalvaguardaBean().getTabActual()==1)){
			
			if(salvaguarda==1 ){
				if(validaDatosAvanceEjecucion() && validaCamposTablasSalvaguardas(1)){
					grabarSalvaguardaA();				
					getSeguimientoSalvaguardaBean().setTabActual(getSeguimientoSalvaguardaBean().getTabActual()+1);
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.tablasSalvaguardaA"));					
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
//				getSeguimientoSalvaguardaBean().setListaTempPregunta141(getTableResponsesFacade().buscaPreguntas14_1_2(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId()));
//				if (getSeguimientoSalvaguardaBean().getListaTempPregunta141().size()>0
				if(validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143())
						&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue()==false){
					Mensaje.verDialogo("dlgEliminaDatosTabla");
//				}else{
//					getSeguimientoSalvaguardaBean().setCatalogoInformacionComunicaSeleccionado(new ArrayList<>());
//					getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucionSeleccionado(new ArrayList<>());
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
//		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C293")){
//			if (validaDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293()) 
//					&& getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(9).isVaanYesnoAnswerValue()==false){
//				Mensaje.verDialogo("dlgEliminaDatosTabla");
//			}
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
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB41(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB41");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B51")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB51(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB51");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B612")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61());
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB62());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB61(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB62(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB61");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB62");
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
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B9")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB9(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB9");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B10")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102());
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB103());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB102(new ArrayList<>());
//				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB103(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB102");
//				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB103");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("B11")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaB11(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla11B");
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
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143());				
//				getSeguimientoSalvaguardaBean().setCatalogoInformacionComunicaSeleccionado(new ArrayList<>());
//				getSeguimientoSalvaguardaBean().setCatalogoInformacionEjecucionSeleccionado(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaB143");
				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C201")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC201(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla201C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C211")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC211(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
//				eliminaInformacionPregunta21123();
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla211C");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C241")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241());
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC241(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC242(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
//				eliminaInformacionPregunta241();
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla241C");
				Mensaje.actualizarComponente(":form:salvaguardas:panelTablaC242");
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
//			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C293")){
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293());				
//				getSeguimientoSalvaguardaBean().setTablaSalvaguardaC293(new ArrayList<>());				
//				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
//				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla293C");				
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
//				eliminaInformacionPreguntaF451();
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaF452(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
//				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla461G");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla452F");
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G461")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461());				
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG461(new ArrayList<>());				
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla461G");				
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G471")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471());				
//				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG472());
				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG471(new ArrayList<>());
//				getSeguimientoSalvaguardaBean().setTablaSalvaguardaG472(new ArrayList<>());
				getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");				
				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla471G");
//				Mensaje.actualizarComponente(":form:salvaguardas:panelTabla472G");
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
			}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("G511")){
				getTableResponsesFacade().eliminarDatosTabla(getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512());								
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
//			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaB103");
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
//		}else if(getSeguimientoSalvaguardaBean().getCodigoTablaDatos().equals("C293")){
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(9).setVaanYesnoAnswerValue(true);
//			getSeguimientoSalvaguardaBean().setCodigoTablaDatos("");
//			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC7"); 
//			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaC293");												
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
//			Mensaje.actualizarComponente(":form:salvaguardas:txtF451");
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
//			Mensaje.actualizarComponente(":form:salvaguardas:btnTablaG472");
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
	
//	public void eliminaInformacionPregunta21123(){
//		try{
//			ValueAnswers nuevaFila1=new ValueAnswers();
//			ValueAnswers nuevaFila2=new ValueAnswers();
//			nuevaFila1.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			nuevaFila1.setVaanStatus(true);
//			nuevaFila1.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(4));				
//			nuevaFila2.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
//			nuevaFila2.setQuestions(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(5));
//			nuevaFila2.setVaanStatus(true);
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).setVaanStatus(false);
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).setVaanStatus(false);
//			getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
//			getValueAnswersFacade().eliminaInformacionRespuesta(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
//			getValueAnswersFacade().editarInformacionRespuestas(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1));
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2));
//			Mensaje.actualizarComponente(":form:salvaguardas:txtC212");
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().remove(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3));
//			Mensaje.actualizarComponente(":form:salvaguardas:txtC213");
//			getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila1);
//			getValueAnswersFacade().agregarNuevaInformacionRespuesta(nuevaFila2);
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(nuevaFila1);
//			getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().add(nuevaFila2);				
//			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(new ArrayList<>());
//			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getValueAnswersFacade().buscarPorAvanceEjecucionYSalvaguarda(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(),nuevaFila1.getQuestions().getSafeguards().getSafeId()));
//			getSeguimientoSalvaguardaBean().setListaValoresRespuestasC(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
//			Mensaje.actualizarComponente(":form:salvaguardas:radiopSC2");
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
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
	
//	public String ubicaServicio(int codigo){
//		String servicio="";
//		Optional<Catalogs> resultado= getAplicacionController().getAplicacionBean().getListaServicio().stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
//		if(resultado.isPresent()){
//			Catalogs catalogo= resultado.get();
//			servicio=catalogo.getCataText1();
//		}
//		return servicio;
//	}
	
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
//					getSeguimientoSalvaguardaBean().getCatalogoInformacionComunicaSeleccionado().size()==0 || getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucionSeleccionado().size()==0 )){
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
//			}else if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(9).isVaanYesnoAnswerValue() && 
//					getSeguimientoSalvaguardaBean().getTablaSalvaguardaC293().size()==0){
//				informacionCompleta=false;
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
			getSeguimientoSalvaguardaBean().getResumenEjecutivo().setAdvanceExecutionSafeguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error"), getMensajesController().getPropiedad("error.datosPlanGobierno"));
				
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
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
//					Mensaje.actualizarComponente(":form:salvaguardas:comboActividadB131");
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
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
//				getSeguimientoSalvaguardaBean().setNuevaComunicacion(false);				
				getSeguimientoSalvaguardaBean().setNuevaComunicacionSB(new Catalogs());
				Mensaje.actualizarComponente(":form:salvaguardas:multipleSeleccionB141");
				Mensaje.ocultarDialogo("dlgNuevaFormaComunica");
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.infoGrabada"));
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void habilitaPuebloNacionalidad(){
		
		if(getSeguimientoSalvaguardaBean().getCodigoAutoIdentificacion()==54)
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
							TableResponses tablaRespuesta= getTableResponsesFacade().buscaLeyPolitica(String.valueOf(antiguo), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 2);
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
							nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(valorNuevo));
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
					nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(listaNuevos.get(0)));
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
							TableResponses tablaRespuesta= getTableResponsesFacade().buscaLeyPolitica(String.valueOf(antiguo), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 4);
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
							nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(valorNuevo));
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
					nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(listaNuevos.get(0)));
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
//		List<Object[]> lista=getCatalogsFacade().buscarDescripcionCatalogoPorTipo(TipoCatalogoEnum.INFORMECOMUNICACION.getCodigo());		
//		Iterator itera = lista.iterator();
//		while(itera.hasNext()){
//			Object dato= itera.next();
//			getSeguimientoSalvaguardaBean().getListadoInformaComunica().add(String.valueOf(dato));
//		}
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
//		List<Object[]> lista=getCatalogsFacade().buscarDescripcionCatalogoPorTipo(TipoCatalogoEnum.INFORMEEJECUCION.getCodigo());		
//		Iterator itera = lista.iterator();
//		while(itera.hasNext()){
//			Object dato= itera.next();
//			getSeguimientoSalvaguardaBean().getListadoInformaEjecucion().add(String.valueOf(dato));
//		}
		for (Catalogs catalogo : getSeguimientoSalvaguardaBean().getCatalogoInformacionEjecucion()) {
			getSeguimientoSalvaguardaBean().getListadoInformaEjecucion().add(catalogo.getCataText1());
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}	

public void controlaSectores(ValueChangeEvent event){
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
						TableResponses tablaRespuesta= getTableResponsesFacade().buscaLeyPolitica(String.valueOf(antiguo), getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().getAdexId(), 4);
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
						nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(valorNuevo));
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
				nuevoRegistroTableResponse.setTareColumnOne(String.valueOf(listaNuevos.get(0)));
				nuevoRegistroTableResponse.setAdvanceExecutionSaveguards(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
				getTableResponsesFacade().agregarEditarNuevaTableResponse(nuevoRegistroTableResponse);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	
}

public void buscarProyectos(){
	try{
		getSeguimientoSalvaguardaBean().setListaProyectos(new ArrayList<>());
		if(getSeguimientoSalvaguardaBean().getCodigoBusquedaProyecto()==1)			
			getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().listarProyectosPorIdSocioImpl(getSeguimientoSalvaguardaBean().getCodigoSocioImplementador()));
		else if(getSeguimientoSalvaguardaBean().getCodigoBusquedaProyecto()==2){
			if (getSeguimientoSalvaguardaBean().getTituloProyecto().length()>5)
				getSeguimientoSalvaguardaBean().setListaProyectos(getProjectsFacade().listarProyectosPorTextoTitulo(getSeguimientoSalvaguardaBean().getTituloProyecto()));
			else{
				Mensaje.actualizarComponente(":form:growl");				
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info"), getMensajesController().getPropiedad("info.longitudMinima"));
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
	getSeguimientoSalvaguardaBean().setListaSectoresSeleccionados(new ArrayList<>());
	getSeguimientoSalvaguardaBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());
	getSeguimientoSalvaguardaBean().setProyectoSeleccionado(new Projects());
	getSeguimientoSalvaguardaBean().setSocioImplementador(new Partners());
	getSeguimientoSalvaguardaBean().setTituloProyecto("");
}

public void imprime(){
	System.out.println(getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102().size());
}

public void imprimirResumenSalvaguardas(){
	ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	String directorioArchivoPDF = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append(1).append(".pdf").toString();
	rutaPDF=directorioArchivoPDF;

	try{
				Document document = new Document();
				document.setPageSize(PageSize.A4);
				document.setMargins(35, 35, 35, 35);
				document.setMarginMirroring(true);

				PdfWriter writer =PdfWriter.getInstance(document, new FileOutputStream(directorioArchivoPDF));
				Rectangle rect = new Rectangle(100, 30, 500, 800);
				writer.setBoxSize("art", rect);
				HeaderFooterPageEvent event = new HeaderFooterPageEvent("PIE");
				event.valoresPiePagina("", "Pag: ");
				writer.setPageEvent(event);
				document.open();

				Font fontContenido = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
				Font fontTitulos = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 7);
				Font fontTitulosSalvaguardas = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 12);
				Font fontCabecera = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
				Font fontCabeceraTabla = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
				Font fontContenidoTablas = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);



				Paragraph parrafoHoja = new Paragraph();
				parrafoHoja.add(new Phrase("RESUMEN DE LAS SALVAGUARDAS REPORTADAS", fontTitulosSalvaguardas));
				parrafoHoja.add(new Phrase(Chunk.NEWLINE));
				parrafoHoja.setAlignment(Element.ALIGN_CENTER);
				parrafoHoja.add(new Phrase(Chunk.NEWLINE));			
				document.add(parrafoHoja);
				
				PdfPTable tablaCabecera = new PdfPTable(2);
				
				PdfPCell celda = new PdfPCell(new Phrase("Título del Plan de implementación, Programa o Proyecto:", fontTitulos));
//				celda.setColspan(1);
				celda.setBorderColor(BaseColor.WHITE);
				celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
				tablaCabecera.addCell(celda);

				
				celda = new PdfPCell(new Phrase(getSeguimientoSalvaguardaBean().getProyectoSeleccionado().getProjTitle(), fontContenido));
//				celda.setColspan(1);
				celda.setBorderColor(BaseColor.WHITE);
				celda.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaCabecera.addCell(celda);
				
				celda = new PdfPCell(new Phrase("Socio implementador: ", fontTitulos));
//				celda.setColspan(1);
				celda.setBorderColor(BaseColor.WHITE);
				celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
				tablaCabecera.addCell(celda);
				
				celda = new PdfPCell(new Phrase(getSeguimientoSalvaguardaBean().getSocioImplementador().getPartName(), fontContenido));
//				celda.setColspan(1);
				celda.setBorderColor(BaseColor.WHITE);
				celda.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaCabecera.addCell(celda);
				
				celda = new PdfPCell(new Phrase("Con qué sector se identifica:  ", fontTitulos));
//				celda.setColspan(1);
				celda.setBorderColor(BaseColor.WHITE);
				celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
				tablaCabecera.addCell(celda);
				
				Paragraph sectores = new Paragraph();
				for(Sectors cata: getSeguimientoSalvaguardaBean().getListaSectoresDisponibles()){				
					Iterator itera = getSeguimientoSalvaguardaBean().getListaSectoresSeleccionados().iterator();
					while(itera.hasNext()){
						int key = Integer.valueOf(itera.next().toString());
						if(key == cata.getSectId()){
							sectores.add(new Phrase(cata.getSectName(), fontContenido));
							sectores.add(new Phrase(Chunk.NEWLINE));
						}
					}
				}
				celda = new PdfPCell(sectores);
//				celda.setColspan(1);
				celda.setBorderColor(BaseColor.WHITE);
				celda.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaCabecera.addCell(celda);

				
				document.add(tablaCabecera);
				
				if(getSeguimientoSalvaguardaBean().isSalvaguardaA()){				
					Paragraph salvaguardaA = new Paragraph();
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaA.add(new Phrase("SALVAGUARDA A", fontTitulosSalvaguardas));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					salvaguardaA.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					salvaguardaA.add(new Phrase("Marco Jurídico Internacional", fontTitulos));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaA);
					
					Paragraph marcoJuridico = new Paragraph();
					marcoJuridico.setIndentationLeft(20);
					for(Catalogs cata: getSeguimientoSalvaguardaBean().getCatalogoLeyes()){				
						Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().iterator();
						while(itera.hasNext()){
							int key = Integer.valueOf(itera.next().toString());
							if(key == cata.getCataId()){
								marcoJuridico.add(new Phrase(cata.getCataText1(), fontContenido));
								marcoJuridico.add(new Phrase(Chunk.NEWLINE));
							}
						}
					}
					document.add(marcoJuridico);
					salvaguardaA = new Paragraph();
					salvaguardaA.add(new Phrase("Marco Jurídico Nacional", fontTitulos));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaA);
					
					Paragraph marcoJuridicoNacional = new Paragraph();
					marcoJuridicoNacional.setIndentationLeft(20);
					for(Catalogs cata: getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacional()){				
						Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoMarcoJuridicoNacionalSeleccionado().iterator();
						while(itera.hasNext()){
							int key = Integer.valueOf(itera.next().toString());
							if(key == cata.getCataId()){
								marcoJuridicoNacional.add(new Phrase(cata.getCataText1(), fontContenido));
								marcoJuridicoNacional.add(new Phrase(Chunk.NEWLINE));
							}
						}
					}
					document.add(marcoJuridicoNacional);
					salvaguardaA = new Paragraph();
					salvaguardaA.add(new Phrase("Normativa Secundaria Nacional", fontTitulos));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaA);
					
					Paragraph normativa = new Paragraph();
					normativa.setIndentationLeft(20);
					for(Catalogs cata: getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacional()){				
						Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoNormativaSecundariaNacionalSeleccionado().iterator();
						while(itera.hasNext()){
							int key = Integer.valueOf(itera.next().toString());
							if(key == cata.getCataId()){
								normativa.add(new Phrase(cata.getCataText1(), fontContenido));
								normativa.add(new Phrase(Chunk.NEWLINE));
							}
						}
					}
					document.add(normativa);
					Paragraph politica = new Paragraph();
					politica.setIndentationLeft(20);
					salvaguardaA = new Paragraph();
					salvaguardaA.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaA);
					for(Catalogs cata: getSeguimientoSalvaguardaBean().getCatalogoPoliticas()){				
						Iterator itera = getSeguimientoSalvaguardaBean().getCatalogoPoliticasSeleccionado().iterator();
						while(itera.hasNext()){
							int key = Integer.valueOf(itera.next().toString());
							if(key == cata.getCataId()){
								politica.add(new Phrase(cata.getCataText1(), fontContenido));
								politica.add(new Phrase(Chunk.NEWLINE));
							}
						}
					}
					document.add(politica);
					salvaguardaA = new Paragraph();
					salvaguardaA.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasA().get(4).getQuesContentQuestion(), fontTitulos));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					salvaguardaA.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaA);

					PdfPTable tabla1A = new PdfPTable(new float[] { 7, 3 });
					tabla1A.setWidthPercentage(100);
					tabla1A.setHorizontalAlignment(Element.ALIGN_LEFT);
					tabla1A.getDefaultCell().setPadding(3);
					tabla1A.getDefaultCell().setUseAscender(true);
					tabla1A.getDefaultCell().setUseDescender(true);
					tabla1A.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tabla1A.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaA=new Paragraph();				
					encabezadoTablaA.add(new Phrase("Plan/Proyecto del Gobierno Nacional",fontCabeceraTabla));
					tabla1A.addCell(encabezadoTablaA);
					encabezadoTablaA=new Paragraph();	
					encabezadoTablaA.add(new Phrase("Presupuesto asignado",fontCabeceraTabla));
					tabla1A.addCell(encabezadoTablaA);

					Paragraph datosTablaA;
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaA()){
						datosTablaA=new Paragraph();
						datosTablaA.add(new Phrase(tabla.getTareCatPlanGobierno(),fontContenidoTablas));
						tabla1A.addCell(datosTablaA);
						datosTablaA=new Paragraph();
						datosTablaA.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()) ,fontContenidoTablas));
						tabla1A.addCell(datosTablaA);
					}
					document.add(tabla1A);

					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tabla2A = new PdfPTable(new float[] { 7, 7,7 });
					tabla2A.setWidthPercentage(100);
					tabla2A.setHorizontalAlignment(Element.ALIGN_LEFT);
					tabla2A.getDefaultCell().setPadding(3);
					tabla2A.getDefaultCell().setUseAscender(true);
					tabla2A.getDefaultCell().setUseDescender(true);
					tabla2A.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tabla2A.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaA=new Paragraph();				
					encabezadoTablaA.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tabla2A.addCell(encabezadoTablaA);
					encabezadoTablaA=new Paragraph();	
					encabezadoTablaA.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tabla2A.addCell(encabezadoTablaA);
					encabezadoTablaA=new Paragraph();	
					encabezadoTablaA.add(new Phrase("Link verificador",fontCabeceraTabla));
					tabla2A.addCell(encabezadoTablaA);

					datosTablaA=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPA()){
						datosTablaA.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tabla2A.addCell(datosTablaA);
						datosTablaA=new Paragraph();
						datosTablaA.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tabla2A.addCell(datosTablaA);
						datosTablaA=new Paragraph();
						datosTablaA.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tabla2A.addCell(datosTablaA);
					}
					document.add(tabla2A);
				}
				//SALVAGUARDA B
				if(getSeguimientoSalvaguardaBean().isSalvaguardaB()){	
					Paragraph salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaB.add(new Phrase("SALVAGUARDA B", fontTitulosSalvaguardas));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(0).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(0).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB41 = new PdfPTable(new float[] { 3, 3, 3, 3, 3, 3, 3, 3 ,3 ,3  });
					tablaB41.setWidthPercentage(100);
					tablaB41.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB41.getDefaultCell().setPadding(3);
					tablaB41.getDefaultCell().setUseAscender(true);
					tablaB41.getDefaultCell().setUseDescender(true);
					tablaB41.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB41.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Modalidad",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Autoidentificación étnica",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
					tablaB41.addCell(encabezadoTablaB);
					
					Paragraph datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB41()){
						datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaB41.addCell(datosTablaB);
						datosTablaB=new Paragraph();
					}
					document.add(tablaB41);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(2).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(1).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB51 = new PdfPTable(new float[] { 3, 3, 3  });
					tablaB51.setWidthPercentage(100);
					tablaB51.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB51.getDefaultCell().setPadding(3);
					tablaB51.getDefaultCell().setUseAscender(true);
					tablaB51.getDefaultCell().setUseDescender(true);
					tablaB51.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB51.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Institución",fontCabeceraTabla));
					tablaB51.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Actividades de coordinación",fontCabeceraTabla));
					tablaB51.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador del convenio",fontCabeceraTabla));
					tablaB51.addCell(encabezadoTablaB);
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB51()){
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB51.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaB51.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaB51.addCell(datosTablaB);						
						datosTablaB=new Paragraph();
					}
					document.add(tablaB51);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(4).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(5).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB61 = new PdfPTable(new float[] { 3, 3, 3  });
					tablaB61.setWidthPercentage(100);
					tablaB61.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB61.getDefaultCell().setPadding(3);
					tablaB61.getDefaultCell().setUseAscender(true);
					tablaB61.getDefaultCell().setUseDescender(true);
					tablaB61.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB61.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Institución",fontCabeceraTabla));
					tablaB61.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Objetivo",fontCabeceraTabla));
					tablaB61.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaB61.addCell(encabezadoTablaB);
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB61()){
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB61.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaB61.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaB61.addCell(datosTablaB);						
						datosTablaB=new Paragraph();
					}
					document.add(tablaB61);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(7).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(3).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(8).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB71 = new PdfPTable(new float[] { 3, 3, 3  });
					tablaB71.setWidthPercentage(100);
					tablaB71.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB71.getDefaultCell().setPadding(3);
					tablaB71.getDefaultCell().setUseAscender(true);
					tablaB71.getDefaultCell().setUseDescender(true);
					tablaB71.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB71.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Instrumento",fontCabeceraTabla));
					tablaB71.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Mecanismo de Institucionalización",fontCabeceraTabla));
					tablaB71.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaB71.addCell(encabezadoTablaB);
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB71()){
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB71.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaB71.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaB71.addCell(datosTablaB);						
						datosTablaB=new Paragraph();
					}
					document.add(tablaB71);

					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(9).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(4).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB81 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3, 3,3,3  });
					tablaB81.setWidthPercentage(100);
					tablaB81.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB81.getDefaultCell().setPadding(3);
					tablaB81.getDefaultCell().setUseAscender(true);
					tablaB81.getDefaultCell().setUseDescender(true);
					tablaB81.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Actividad",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Hectáreas",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
					tablaB81.addCell(encabezadoTablaB);
					
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB81()){
						datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaB81.addCell(datosTablaB);						
						datosTablaB=new Paragraph();
					}
					document.add(tablaB81);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(11).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
//					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(10).getQuesContentQuestion(), fontTitulos));
//					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB9 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3  });
					tablaB9.setWidthPercentage(100);
					tablaB9.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB9.getDefaultCell().setPadding(3);
					tablaB9.getDefaultCell().setUseAscender(true);
					tablaB9.getDefaultCell().setUseDescender(true);
					tablaB9.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaB9.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaB9.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaB9.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaB9.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaB9.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaB9.addCell(encabezadoTablaB);
					
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB9()){
						
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaB9.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaB9.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaB9.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaB9.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaB9.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaB9.addCell(datosTablaB);												
						datosTablaB=new Paragraph();
					}
					document.add(tablaB9);

					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(12).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(13).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB102 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3, 3,3  });
					tablaB102.setWidthPercentage(100);
					tablaB102.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB102.getDefaultCell().setPadding(3);
					tablaB102.getDefaultCell().setUseAscender(true);
					tablaB102.getDefaultCell().setUseDescender(true);
					tablaB102.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB102.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Hectáreas",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Estado",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
					tablaB102.addCell(encabezadoTablaB);
					
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB102()){
						
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaB102.addCell(datosTablaB);						
						datosTablaB=new Paragraph();
					}
					document.add(tablaB102);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(15).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(16).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB11 = new PdfPTable(new float[] { 3, 3, 3 ,3 });
					tablaB11.setWidthPercentage(100);
					tablaB11.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB11.getDefaultCell().setPadding(3);
					tablaB11.getDefaultCell().setUseAscender(true);
					tablaB11.getDefaultCell().setUseDescender(true);
					tablaB11.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Cómo funciona",fontCabeceraTabla));
					tablaB11.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Casos que se han reportado durante el período de reporte",fontCabeceraTabla));
					tablaB11.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Casos que se han atendido durante el período de reporte",fontCabeceraTabla));
					tablaB11.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaB11.addCell(encabezadoTablaB);
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB11()){
						
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB11.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaB11.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaB11.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaB11.addCell(datosTablaB);
						datosTablaB=new Paragraph();
					}
					document.add(tablaB11);

					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(17).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(18).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB121 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3 });
					tablaB121.setWidthPercentage(100);
					tablaB121.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB121.getDefaultCell().setPadding(3);
					tablaB121.getDefaultCell().setUseAscender(true);
					tablaB121.getDefaultCell().setUseDescender(true);
					tablaB121.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB121.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nombre de la organización",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Acciones implementadas",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Conformación de la organización",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaB121.addCell(encabezadoTablaB);
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB121()){
						
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoCuatro(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaB121.addCell(datosTablaB);												
						datosTablaB=new Paragraph();
					}
					document.add(tablaB121);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(19).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(20).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB131 = new PdfPTable(new float[] { 3, 3, 3,3 });
					tablaB131.setWidthPercentage(100);
					tablaB131.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB131.getDefaultCell().setPadding(3);
					tablaB131.getDefaultCell().setUseAscender(true);
					tablaB131.getDefaultCell().setUseDescender(true);
					tablaB131.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB131.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaB131.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Actividad",fontCabeceraTabla));
					tablaB131.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaB131.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Nro de personas que acceden a la info",fontCabeceraTabla));
					tablaB131.addCell(encabezadoTablaB);
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB131()){
						
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaB131.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB131.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaB131.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaB131.addCell(datosTablaB);																		
						datosTablaB=new Paragraph();
					}
					document.add(tablaB131);
					
					salvaguardaB = new Paragraph();
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(21).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue())
						salvaguardaB.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaB.add(new Phrase("NO", fontContenido));					
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasB().get(22).getQuesContentQuestion(), fontTitulos));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					salvaguardaB.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaB);
					
					PdfPTable tablaB143 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3});
					tablaB143.setWidthPercentage(100);
					tablaB143.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaB143.getDefaultCell().setPadding(3);
					tablaB143.getDefaultCell().setUseAscender(true);
					tablaB143.getDefaultCell().setUseDescender(true);
					tablaB143.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaB143.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaB143.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaB143.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Número de personas",fontCabeceraTabla));
					tablaB143.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaB143.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("¿Que información se comunica a los beneficiarios?",fontCabeceraTabla));
					tablaB143.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("¿Como se informa a la gente sobre la ejecución del proyecto/programa?",fontCabeceraTabla));
					tablaB143.addCell(encabezadoTablaB);
					
					
					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaB143()){
						
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaB143.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaB143.addCell(datosTablaB);					
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaB143.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaB143.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaB143.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaB143.addCell(datosTablaB);
						datosTablaB=new Paragraph();
					}
					document.add(tablaB143);
					
					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tablaOPB = new PdfPTable(new float[] { 7, 7,7 });
					tablaOPB.setWidthPercentage(100);
					tablaOPB.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaOPB.getDefaultCell().setPadding(3);
					tablaOPB.getDefaultCell().setUseAscender(true);
					tablaOPB.getDefaultCell().setUseDescender(true);
					tablaOPB.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaOPB.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaB=new Paragraph();				
					encabezadoTablaB.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tablaOPB.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tablaOPB.addCell(encabezadoTablaB);
					encabezadoTablaB=new Paragraph();	
					encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaOPB.addCell(encabezadoTablaB);

					datosTablaB=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPB()){
						datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaOPB.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaOPB.addCell(datosTablaB);
						datosTablaB=new Paragraph();
						datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaOPB.addCell(datosTablaB);
					}
					document.add(tablaOPB);
				}
				//SALVAGUARDA C
				if(getSeguimientoSalvaguardaBean().isSalvaguardaC()){
					Paragraph salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaC.add(new Phrase("SALVAGUARDA C", fontTitulosSalvaguardas));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(0).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(0).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC201 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3 ,3,3});
					tablaC201.setWidthPercentage(100);
					tablaC201.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC201.getDefaultCell().setPadding(3);
					tablaC201.getDefaultCell().setUseAscender(true);
					tablaC201.getDefaultCell().setUseDescender(true);
					tablaC201.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC201.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Hectáreas",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Inversión realizada",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Tipo de acceso",fontCabeceraTabla));
					tablaC201.addCell(encabezadoTablaC);
					
					
					
					Paragraph datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC201()){
						
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalTwo()),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnThirteen(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaC201.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC201);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(2).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(1).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC211 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3});
					tablaC211.setWidthPercentage(100);
					tablaC211.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC211.getDefaultCell().setPadding(3);
					tablaC211.getDefaultCell().setUseAscender(true);
					tablaC211.getDefaultCell().setUseDescender(true);
					tablaC211.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC211.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Práctica o Saber ancestral",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("¿Cómo se reconocieron estas prácticas?",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("¿Cómo se han promovido estas prácticas?",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaC211.addCell(encabezadoTablaC);
					
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC211()){
						
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaC211.addCell(datosTablaC);						
						datosTablaC=new Paragraph();
					}
					document.add(tablaC211);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(4).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(2).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(5).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC241 = new PdfPTable(new float[] { 3, 3, 3,3 ,3});
					tablaC241.setWidthPercentage(100);
					tablaC241.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC241.getDefaultCell().setPadding(3);
					tablaC241.getDefaultCell().setUseAscender(true);
					tablaC241.getDefaultCell().setUseDescender(true);
					tablaC241.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC241.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaC241.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Organizaciones que participan",fontCabeceraTabla));
					tablaC241.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Num participantes",fontCabeceraTabla));
					tablaC241.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Temática",fontCabeceraTabla));
					tablaC241.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaC241.addCell(encabezadoTablaC);
					
					
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC241()){
						
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaC241.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaC241.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaC241.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaC241.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaC241.addCell(datosTablaC);												
						datosTablaC=new Paragraph();
					}
					document.add(tablaC241);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(6).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC242 = new PdfPTable(new float[] { 3, 3, 3});
					tablaC242.setWidthPercentage(100);
					tablaC242.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC242.getDefaultCell().setPadding(3);
					tablaC242.getDefaultCell().setUseAscender(true);
					tablaC242.getDefaultCell().setUseDescender(true);
					tablaC242.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC242.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Medidas que se han tomado para motivar la participación de mujeres",fontCabeceraTabla));
					tablaC242.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("No de mujeres que participan en el intercambio de experiencias",fontCabeceraTabla));
					tablaC242.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaC242.addCell(encabezadoTablaC);

					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC242()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaC242.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaC242.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaC242.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC242);

					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(7).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(3).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(8).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC26 = new PdfPTable(new float[] { 3, 3, 3,3,3});
					tablaC26.setWidthPercentage(100);
					tablaC26.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC26.getDefaultCell().setPadding(3);
					tablaC26.getDefaultCell().setUseAscender(true);
					tablaC26.getDefaultCell().setUseDescender(true);
					tablaC26.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Línea de acción",fontCabeceraTabla));
					tablaC26.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC26.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Representantes (nombres completos)",fontCabeceraTabla));
					tablaC26.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Cargo",fontCabeceraTabla));
					tablaC26.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaC26.addCell(encabezadoTablaC);
					
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC26()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC26.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaC26.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaC26.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaC26.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaC26.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC26);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(9).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(10).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC271 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3});
					tablaC271.setWidthPercentage(100);
					tablaC271.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC271.getDefaultCell().setPadding(3);
					tablaC271.getDefaultCell().setUseAscender(true);
					tablaC271.getDefaultCell().setUseDescender(true);
					tablaC271.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC271.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Línea de acción",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Autoidentificación étnica",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Pueblo/Nacionalidad",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Objeto del convenio",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Hectáreas bajo actividad REDD+",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaC271.addCell(encabezadoTablaC);
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC271()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaC271.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC271);

					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(11).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC28 = new PdfPTable(new float[] { 3, 3, 3, 3, 3});
					tablaC28.setWidthPercentage(100);
					tablaC28.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC28.getDefaultCell().setPadding(3);
					tablaC28.getDefaultCell().setUseAscender(true);
					tablaC28.getDefaultCell().setUseDescender(true);
					tablaC28.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();		
					encabezadoTablaC.add(new Phrase("Autoidentificación étnica",fontCabeceraTabla));
					tablaC28.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Pueblo/Nacionalidad",fontCabeceraTabla));
					tablaC28.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Objeto de contratación",fontCabeceraTabla));
					tablaC28.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Figura de contratación",fontCabeceraTabla));
					tablaC28.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Presupuesto",fontCabeceraTabla));
					tablaC28.addCell(encabezadoTablaC);
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC28()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC28.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaC28.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaC28.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFour().equals("PERSONALPLANTA")?"Personal planta":"Personal consultoria",fontContenidoTablas));
						tablaC28.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaC28.addCell(datosTablaC);						
						datosTablaC=new Paragraph();
					}
					document.add(tablaC28);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(12).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(5).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(13).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC291 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3});
					tablaC291.setWidthPercentage(100);
					tablaC291.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC291.getDefaultCell().setPadding(3);
					tablaC291.getDefaultCell().setUseAscender(true);
					tablaC291.getDefaultCell().setUseDescender(true);
					tablaC291.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC291.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Resultado",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link documento respaldo",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Objeto del acuerdo",fontCabeceraTabla));
					tablaC291.addCell(encabezadoTablaC);
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC291()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaC291.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC291);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(14).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(15).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC301 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3});
					tablaC301.setWidthPercentage(100);
					tablaC301.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC301.getDefaultCell().setPadding(3);
					tablaC301.getDefaultCell().setUseAscender(true);
					tablaC301.getDefaultCell().setUseDescender(true);
					tablaC301.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC301.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Acciones reportadas que causan daños",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Medidas de remediación",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Medidas de compensación",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link de la Remediación",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link de la Compensación",fontCabeceraTabla));
					tablaC301.addCell(encabezadoTablaC);
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC301()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaC301.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC301);
					
					salvaguardaC = new Paragraph();
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(16).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue())
						salvaguardaC.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaC.add(new Phrase("NO", fontContenido));					
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasC().get(17).getQuesContentQuestion(), fontTitulos));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					salvaguardaC.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaC);
					
					PdfPTable tablaC311 = new PdfPTable(new float[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3});
					tablaC311.setWidthPercentage(100);
					tablaC311.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaC311.getDefaultCell().setPadding(3);
					tablaC311.getDefaultCell().setUseAscender(true);
					tablaC311.getDefaultCell().setUseDescender(true);
					tablaC311.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaC311.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);					
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Asistentes hombres",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Asistentes mujeres",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador participantes",fontCabeceraTabla));
					tablaC311.addCell(encabezadoTablaC);
					
					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaC311()){
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);					
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaC311.addCell(datosTablaC);
						datosTablaC=new Paragraph();
					}
					document.add(tablaC311);
					
					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tablaOPC = new PdfPTable(new float[] { 7, 7,7 });
					tablaOPC.setWidthPercentage(100);
					tablaOPC.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaOPC.getDefaultCell().setPadding(3);
					tablaOPC.getDefaultCell().setUseAscender(true);
					tablaOPC.getDefaultCell().setUseDescender(true);
					tablaOPC.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaOPC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaC=new Paragraph();				
					encabezadoTablaC.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tablaOPC.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tablaOPC.addCell(encabezadoTablaC);
					encabezadoTablaC=new Paragraph();	
					encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaOPC.addCell(encabezadoTablaC);

					datosTablaC=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPC()){
						datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaOPC.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaOPC.addCell(datosTablaC);
						datosTablaC=new Paragraph();
						datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaOPC.addCell(datosTablaC);
					}
					document.add(tablaOPC);

				}
				//SALVAGUARDA D
				if(getSeguimientoSalvaguardaBean().isSalvaguardaD()){
					Paragraph salvaguardaD = new Paragraph();
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaD.add(new Phrase("SALVAGUARDA D", fontTitulosSalvaguardas));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaD.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(0).getQuesContentQuestion(), fontTitulos));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(0).isVaanYesnoAnswerValue())
						salvaguardaD.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaD.add(new Phrase("NO", fontContenido));					
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					salvaguardaD.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaD);
					
					PdfPTable tablaD321 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3 ,3});
					tablaD321.setWidthPercentage(100);
					tablaD321.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaD321.getDefaultCell().setPadding(3);
					tablaD321.getDefaultCell().setUseAscender(true);
					tablaD321.getDefaultCell().setUseDescender(true);
					tablaD321.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaD321.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Espacio",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Actores",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Asistentes hombres",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Asistentes mujeres",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaD321.addCell(encabezadoTablaD);
					
					
					
					
					Paragraph datosTablaD=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaD321()){
						
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);					
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnTwelve(),fontContenidoTablas));
						tablaD321.addCell(datosTablaD);						
						datosTablaD=new Paragraph();
					}
					document.add(tablaD321);
					
					salvaguardaD = new Paragraph();
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaD.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(2).getQuesContentQuestion(), fontTitulos));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasD().get(1).isVaanYesnoAnswerValue())
						salvaguardaD.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaD.add(new Phrase("NO", fontContenido));					
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					salvaguardaD.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasD().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					salvaguardaD.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaD);
					
					PdfPTable tablaD331 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3});
					tablaD331.setWidthPercentage(100);
					tablaD331.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaD331.getDefaultCell().setPadding(3);
					tablaD331.getDefaultCell().setUseAscender(true);
					tablaD331.getDefaultCell().setUseDescender(true);
					tablaD331.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaD331.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Actor / Organización",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Objetivo",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Inversión realizada",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Nivel de involucramiento",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);					
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaD331.addCell(encabezadoTablaD);
					
					
					
					
					datosTablaD=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaD331()){
						
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);						
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaD331.addCell(datosTablaD);											
						datosTablaD=new Paragraph();
					}
					document.add(tablaD331);
					
					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tablaOPD = new PdfPTable(new float[] { 7, 7,7 });
					tablaOPD.setWidthPercentage(100);
					tablaOPD.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaOPD.getDefaultCell().setPadding(3);
					tablaOPD.getDefaultCell().setUseAscender(true);
					tablaOPD.getDefaultCell().setUseDescender(true);
					tablaOPD.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaOPD.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaD=new Paragraph();				
					encabezadoTablaD.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tablaOPD.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tablaOPD.addCell(encabezadoTablaD);
					encabezadoTablaD=new Paragraph();	
					encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaOPD.addCell(encabezadoTablaD);

					datosTablaD=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPD()){
						datosTablaD.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaOPD.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaOPD.addCell(datosTablaD);
						datosTablaD=new Paragraph();
						datosTablaD.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaOPD.addCell(datosTablaD);
					}
					document.add(tablaOPD);

				}	
				
				//SALVAGUARDA E
				if(getSeguimientoSalvaguardaBean().isSalvaguardaE()){
					Paragraph salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaE.add(new Phrase("SALVAGUARDA E", fontTitulosSalvaguardas));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(0).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(0).isVaanYesnoAnswerValue())
						salvaguardaE.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaE.add(new Phrase("NO", fontContenido));					
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaE);
					
					PdfPTable tablaE341 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 });
					tablaE341.setWidthPercentage(100);
					tablaE341.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaE341.getDefaultCell().setPadding(3);
					tablaE341.getDefaultCell().setUseAscender(true);
					tablaE341.getDefaultCell().setUseDescender(true);
					tablaE341.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaE341.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaE341.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaE341.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaE341.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaE341.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Hectáreas",fontCabeceraTabla));
					tablaE341.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Tipo de área consolidada",fontCabeceraTabla));
					tablaE341.addCell(encabezadoTablaE);
					
					Paragraph datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE341()){
						
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaE341.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaE341.addCell(datosTablaE);					
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaE341.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaE341.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaE341.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaE341.addCell(datosTablaE);												
						datosTablaE=new Paragraph();
					}
					document.add(tablaE341);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(2).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(1).isVaanYesnoAnswerValue())
						salvaguardaE.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaE.add(new Phrase("NO", fontContenido));					
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaE);
					
					PdfPTable tablaE351 = new PdfPTable(new float[] { 3, 3, 3});
					tablaE351.setWidthPercentage(100);
					tablaE351.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaE351.getDefaultCell().setPadding(3);
					tablaE351.getDefaultCell().setUseAscender(true);
					tablaE351.getDefaultCell().setUseDescender(true);
					tablaE351.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaE351.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Nivel",fontCabeceraTabla));
					tablaE351.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Herramienta (PDOT, ACUS, Ordenanza, etc)",fontCabeceraTabla));
					tablaE351.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaE351.addCell(encabezadoTablaE);
					
					datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE351()){
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaE351.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaE351.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaE351.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
					}
					document.add(tablaE351);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(4).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(2).isVaanYesnoAnswerValue())
						salvaguardaE.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaE.add(new Phrase("NO", fontContenido));					
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(5).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaE);
					
					PdfPTable tablaE361 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3});
					tablaE361.setWidthPercentage(100);
					tablaE361.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaE361.getDefaultCell().setPadding(3);
					tablaE361.getDefaultCell().setUseAscender(true);
					tablaE361.getDefaultCell().setUseDescender(true);
					tablaE361.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaE361.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Actores clave",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Hectareas",fontCabeceraTabla));
					tablaE361.addCell(encabezadoTablaE);
					
					datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE361()){
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);																							
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaE361.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
					}
					document.add(tablaE361);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(6).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(3).isVaanYesnoAnswerValue())
						salvaguardaE.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaE.add(new Phrase("NO", fontContenido));					
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(7).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaE);
					
					PdfPTable tablaE371 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3,3,3});
					tablaE371.setWidthPercentage(100);
					tablaE371.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaE371.getDefaultCell().setPadding(3);
					tablaE371.getDefaultCell().setUseAscender(true);
					tablaE371.getDefaultCell().setUseDescender(true);
					tablaE371.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaE371.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Temas",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Método",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Público",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Nro hombres",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Nro mujeres",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaE371.addCell(encabezadoTablaE);
					
					datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE371()){
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);																							
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);																							
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaE371.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
					}
					document.add(tablaE371);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(8).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(4).getVaanTextAnswerValue(),fontContenido));	
					document.add(salvaguardaE);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(9).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(5).isVaanYesnoAnswerValue())
						salvaguardaE.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaE.add(new Phrase("NO", fontContenido));					
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(10).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaE);
					
					PdfPTable tablaE381 = new PdfPTable(new float[] { 3, 3, 3,3,3,3});
					tablaE381.setWidthPercentage(100);
					tablaE381.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaE381.getDefaultCell().setPadding(3);
					tablaE381.getDefaultCell().setUseAscender(true);
					tablaE381.getDefaultCell().setUseDescender(true);
					tablaE381.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaE381.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaE381.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaE381.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaE381.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaE381.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Servicio",fontCabeceraTabla));
					tablaE381.addCell(encabezadoTablaE);					
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaE381.addCell(encabezadoTablaE);
					
					datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE381()){
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaE381.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaE381.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaE381.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaE381.addCell(datosTablaE);																					
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaE381.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaE381.addCell(datosTablaE);																																								
						datosTablaE=new Paragraph();
					}
					document.add(tablaE381);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(11).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(6).isVaanYesnoAnswerValue())
						salvaguardaE.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaE.add(new Phrase("NO", fontContenido));					
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(12).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaE);
					
					PdfPTable tablaE391 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3});
					tablaE391.setWidthPercentage(100);
					tablaE391.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaE391.getDefaultCell().setPadding(3);
					tablaE391.getDefaultCell().setUseAscender(true);
					tablaE391.getDefaultCell().setUseDescender(true);
					tablaE391.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaE391.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Recurso",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Periodicidad",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaE391.addCell(encabezadoTablaE);
					
					datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaE391()){
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);																	
						datosTablaE=new Paragraph();
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);																					
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaE391.addCell(datosTablaE);
						datosTablaE=new Paragraph();
					}
					document.add(tablaE391);

					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(13).getQuesContentQuestion(), fontTitulos));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaE.add(new Phrase("COBENEFICIOS POLÍTICAS Y GESTIÓN INSTITUCIONAL", fontTitulos));	
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					document.add(salvaguardaE);
					
					PdfPTable tablaCobeneficio1 = new PdfPTable(new float[] { 9, 1, 9, 1});
					tablaCobeneficio1.setWidthPercentage(100);
					tablaCobeneficio1.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaCobeneficio1.getDefaultCell().setPadding(3);
					tablaCobeneficio1.getDefaultCell().setUseAscender(true);
					tablaCobeneficio1.getDefaultCell().setUseDescender(true);
					tablaCobeneficio1.getDefaultCell().setBorderColor(BaseColor.WHITE);				
					tablaCobeneficio1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("SOCIALES",fontCabeceraTabla));
					tablaCobeneficio1.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio1.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("AMBIENTALES",fontCabeceraTabla));
					tablaCobeneficio1.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio1.addCell(encabezadoTablaE);										
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(14).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(8).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(18).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(12).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(15).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(9).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(19).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(13).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(16).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(10).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(20).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(14).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(17).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(11).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio1.addCell(datosTablaE);
					document.add(tablaCobeneficio1);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));										
					
					salvaguardaE.add(new Phrase("TRANSICIÓN A SISTEMAS PRODUCTIVOS SOSTENIBLES", fontTitulos));	
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					document.add(salvaguardaE);
					
					PdfPTable tablaCobeneficio2 = new PdfPTable(new float[] { 9, 1, 9, 1});
					tablaCobeneficio2.setWidthPercentage(100);
					tablaCobeneficio2.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaCobeneficio2.getDefaultCell().setPadding(3);
					tablaCobeneficio2.getDefaultCell().setUseAscender(true);
					tablaCobeneficio2.getDefaultCell().setUseDescender(true);
					tablaCobeneficio2.getDefaultCell().setBorderColor(BaseColor.WHITE);				
					tablaCobeneficio2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("SOCIALES",fontCabeceraTabla));
					tablaCobeneficio2.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio2.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("AMBIENTALES",fontCabeceraTabla));
					tablaCobeneficio2.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio2.addCell(encabezadoTablaE);										
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(21).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(15).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(27).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(21).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(22).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(16).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(28).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(22).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(23).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(17).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(29).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(23).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(24).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(18).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(30).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(24).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);					
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(25).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(19).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(26).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(20).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio2.addCell(datosTablaE);
					document.add(tablaCobeneficio2);
					
					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					
					salvaguardaE.add(new Phrase("MANEJO FORESTAL SOSTENIBLE", fontTitulos));	
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					document.add(salvaguardaE);
					PdfPTable tablaCobeneficio3 = new PdfPTable(new float[] { 9, 1, 9, 1});
					tablaCobeneficio3.setWidthPercentage(100);
					tablaCobeneficio3.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaCobeneficio3.getDefaultCell().setPadding(3);
					tablaCobeneficio3.getDefaultCell().setUseAscender(true);
					tablaCobeneficio3.getDefaultCell().setUseDescender(true);
					tablaCobeneficio3.getDefaultCell().setBorderColor(BaseColor.WHITE);				
					tablaCobeneficio3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("SOCIALES",fontCabeceraTabla));
					tablaCobeneficio3.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio3.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("AMBIENTALES",fontCabeceraTabla));
					tablaCobeneficio3.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio3.addCell(encabezadoTablaE);										
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(31).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(25).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(38).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(32).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(32).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(26).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(39).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(33).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(33).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(27).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(40).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(34).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(34).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(28).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(41).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(35).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);					
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(35).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(29).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(42).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(36).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(36).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(30).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(43).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(37).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(37).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(31).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio3.addCell(datosTablaE);					
					document.add(tablaCobeneficio3);

					salvaguardaE = new Paragraph();
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
					
					salvaguardaE.add(new Phrase("CONSERVACION Y RESTAURACION", fontTitulos));	
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));
					salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
					document.add(salvaguardaE);
					PdfPTable tablaCobeneficio4 = new PdfPTable(new float[] { 9, 1, 9, 1});
					tablaCobeneficio4.setWidthPercentage(100);
					tablaCobeneficio4.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaCobeneficio4.getDefaultCell().setPadding(3);
					tablaCobeneficio4.getDefaultCell().setUseAscender(true);
					tablaCobeneficio4.getDefaultCell().setUseDescender(true);
					tablaCobeneficio4.getDefaultCell().setBorderColor(BaseColor.WHITE);				
					tablaCobeneficio4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("SOCIALES",fontCabeceraTabla));
					tablaCobeneficio4.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio4.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("AMBIENTALES",fontCabeceraTabla));
					tablaCobeneficio4.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("",fontCabeceraTabla));
					tablaCobeneficio4.addCell(encabezadoTablaE);										
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(44).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(38).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(46).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(40).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(45).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(39).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(47).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(41).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(48).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(42).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(49).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(43).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);					
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase("",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasE().get(50).getQuesContentQuestion(),fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasE().get(44).isVaanYesnoAnswerValue()?"SI":"",fontContenidoTablas));
					tablaCobeneficio4.addCell(datosTablaE);
					document.add(tablaCobeneficio4);
					
					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tablaOPE = new PdfPTable(new float[] { 7, 7,7 });
					tablaOPE.setWidthPercentage(100);
					tablaOPE.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaOPE.getDefaultCell().setPadding(3);
					tablaOPE.getDefaultCell().setUseAscender(true);
					tablaOPE.getDefaultCell().setUseDescender(true);
					tablaOPE.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaOPE.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaE=new Paragraph();				
					encabezadoTablaE.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tablaOPE.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tablaOPE.addCell(encabezadoTablaE);
					encabezadoTablaE=new Paragraph();	
					encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaOPE.addCell(encabezadoTablaE);

					datosTablaE=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPE()){
						datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaOPE.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaOPE.addCell(datosTablaE);
						datosTablaE=new Paragraph();
						datosTablaE.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaOPE.addCell(datosTablaE);
					}
					document.add(tablaOPE);
				}	
				//SALVAGUARDA F
				
				if(getSeguimientoSalvaguardaBean().isSalvaguardaF()){
					Paragraph salvaguardaF = new Paragraph();
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaF.add(new Phrase("SALVAGUARDA F", fontTitulosSalvaguardas));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(0).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(0).isVaanYesnoAnswerValue())
						salvaguardaF.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaF.add(new Phrase("NO", fontContenido));					
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaF);
					
					PdfPTable tablaF411 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3});
					tablaF411.setWidthPercentage(100);
					tablaF411.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaF411.getDefaultCell().setPadding(3);
					tablaF411.getDefaultCell().setUseAscender(true);
					tablaF411.getDefaultCell().setUseDescender(true);
					tablaF411.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaF411.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Riesgo",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaF411.addCell(encabezadoTablaF);
					
					Paragraph datosTablaF=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaF411()){
						
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);					
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaF411.addCell(datosTablaF);
						datosTablaF=new Paragraph();
					}
					document.add(tablaF411);
					
					salvaguardaF = new Paragraph();
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(2).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(1).isVaanYesnoAnswerValue())
						salvaguardaF.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaF.add(new Phrase("NO", fontContenido));					
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaF);
					
					PdfPTable tablaF421 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3,3});
					tablaF421.setWidthPercentage(100);
					tablaF421.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaF421.getDefaultCell().setPadding(3);
					tablaF421.getDefaultCell().setUseAscender(true);
					tablaF421.getDefaultCell().setUseDescender(true);
					tablaF421.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaF421.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Riesgo asociado",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Medida tomada",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaF421.addCell(encabezadoTablaF);
					
					datosTablaF=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaF421()){
						
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);					
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaF421.addCell(datosTablaF);
						datosTablaF=new Paragraph();
					}
					document.add(tablaF421);
					
					salvaguardaF = new Paragraph();
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(4).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(2).isVaanYesnoAnswerValue())
						salvaguardaF.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaF.add(new Phrase("NO", fontContenido));					
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(5).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaF);
					
					PdfPTable tablaF431 = new PdfPTable(new float[] { 5, 3, 3,3 ,3, 3});
					tablaF431.setWidthPercentage(100);
					tablaF431.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaF431.getDefaultCell().setPadding(3);
					tablaF431.getDefaultCell().setUseAscender(true);
					tablaF431.getDefaultCell().setUseDescender(true);
					tablaF431.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaF431.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Sistemas REDD+",fontCabeceraTabla));
					tablaF431.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Acciones tomadas",fontCabeceraTabla));
					tablaF431.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Recursos invertidos",fontCabeceraTabla));
					tablaF431.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Actores clave",fontCabeceraTabla));
					tablaF431.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Resultado",fontCabeceraTabla));
					tablaF431.addCell(encabezadoTablaF);					
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaF431.addCell(encabezadoTablaF);
					
					datosTablaF=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaF431()){
						
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaF431.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaF431.addCell(datosTablaF);					
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaF431.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaF431.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaF431.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaF431.addCell(datosTablaF);						
						datosTablaF=new Paragraph();
					}
					document.add(tablaF431);
					
					salvaguardaF = new Paragraph();
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(6).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(3).isVaanYesnoAnswerValue())
						salvaguardaF.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaF.add(new Phrase("NO", fontContenido));					
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(7).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaF);
					
					PdfPTable tablaF441 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3,3});
					tablaF441.setWidthPercentage(100);
					tablaF441.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaF441.getDefaultCell().setPadding(3);
					tablaF441.getDefaultCell().setUseAscender(true);
					tablaF441.getDefaultCell().setUseDescender(true);
					tablaF441.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaF441.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Riesgo",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Actividad para mitigar",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaF441.addCell(encabezadoTablaF);
					
					datosTablaF=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaF441()){
						
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);					
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaF441.addCell(datosTablaF);
						datosTablaF=new Paragraph();
					}
					document.add(tablaF441);
					
					salvaguardaF = new Paragraph();
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(8).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasF().get(4).isVaanYesnoAnswerValue())
						salvaguardaF.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaF.add(new Phrase("NO", fontContenido));					
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasF().get(9).getQuesContentQuestion(), fontTitulos));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					salvaguardaF.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaF);
					
					PdfPTable tablaF452 = new PdfPTable(new float[] { 5, 3, 3,3 });
					tablaF452.setWidthPercentage(100);
					tablaF452.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaF452.getDefaultCell().setPadding(3);
					tablaF452.getDefaultCell().setUseAscender(true);
					tablaF452.getDefaultCell().setUseDescender(true);
					tablaF452.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaF452.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Sistemas",fontCabeceraTabla));
					tablaF452.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Nro. Casos reportados",fontCabeceraTabla));
					tablaF452.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Nro. Casos atendidos",fontCabeceraTabla));
					tablaF452.addCell(encabezadoTablaF);										
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaF452.addCell(encabezadoTablaF);
					
					datosTablaF=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaF452()){
						
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaF452.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaF452.addCell(datosTablaF);					
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaF452.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaF452.addCell(datosTablaF);												
						datosTablaF=new Paragraph();
					}
					document.add(tablaF452);
					
					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tablaOPF = new PdfPTable(new float[] { 7, 7,7 });
					tablaOPF.setWidthPercentage(100);
					tablaOPF.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaOPF.getDefaultCell().setPadding(3);
					tablaOPF.getDefaultCell().setUseAscender(true);
					tablaOPF.getDefaultCell().setUseDescender(true);
					tablaOPF.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaOPF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaF=new Paragraph();				
					encabezadoTablaF.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tablaOPF.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tablaOPF.addCell(encabezadoTablaF);
					encabezadoTablaF=new Paragraph();	
					encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaOPF.addCell(encabezadoTablaF);

					datosTablaF=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPF()){
						datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaOPF.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaOPF.addCell(datosTablaF);
						datosTablaF=new Paragraph();
						datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaOPF.addCell(datosTablaF);
					}
					document.add(tablaOPF);
				}
				//SALVAGUARDA G
				
				if(getSeguimientoSalvaguardaBean().isSalvaguardaG()){
					Paragraph salvaguardaG = new Paragraph();
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));	
					salvaguardaG.add(new Phrase("SALVAGUARDA G", fontTitulosSalvaguardas));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(0).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(1).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaG);
					
					PdfPTable tablaG461 = new PdfPTable(new float[] { 3, 3, 3,3 ,3});
					tablaG461.setWidthPercentage(100);
					tablaG461.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaG461.getDefaultCell().setPadding(3);
					tablaG461.getDefaultCell().setUseAscender(true);
					tablaG461.getDefaultCell().setUseDescender(true);
					tablaG461.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaG461.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					Paragraph encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Responsable del estudio",fontCabeceraTabla));
					tablaG461.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Autores de la investigación",fontCabeceraTabla));
					tablaG461.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Riesgo PrincipalIdentificado",fontCabeceraTabla));
					tablaG461.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nombre del estudio",fontCabeceraTabla));
					tablaG461.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Verificador",fontCabeceraTabla));
					tablaG461.addCell(encabezadoTablaG);
					
					
					Paragraph datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaG461()){
						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaG461.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaG461.addCell(datosTablaG);					
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaG461.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaG461.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaG461.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
					}
					document.add(tablaG461);
					
					salvaguardaG = new Paragraph();					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(2).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(1).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(3).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaG);
					
					PdfPTable tablaG471 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3});
					tablaG471.setWidthPercentage(100);
					tablaG471.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaG471.getDefaultCell().setPadding(3);
					tablaG471.getDefaultCell().setUseAscender(true);
					tablaG471.getDefaultCell().setUseDescender(true);
					tablaG471.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaG471.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Actividad",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nro hombres",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nro mujeres",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaG471.addCell(encabezadoTablaG);
					
					datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaG471()){
						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);					
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaG471.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
					}
					document.add(tablaG471);
					
					salvaguardaG = new Paragraph();					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(5).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(2).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(6).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaG);
					
					PdfPTable tablaG481 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3});
					tablaG481.setWidthPercentage(100);
					tablaG481.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaG481.getDefaultCell().setPadding(3);
					tablaG481.getDefaultCell().setUseAscender(true);
					tablaG481.getDefaultCell().setUseDescender(true);
					tablaG481.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaG481.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Institución acompaña",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Actividad ilicita reportada",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Resultado",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Fecha",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaG481.addCell(encabezadoTablaG);
					
					datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaG481()){
						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);					
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaG481.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
					}
					document.add(tablaG481);
					
					salvaguardaG = new Paragraph();					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(7).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(3).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(8).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaG);
					
					PdfPTable tablaG491 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3,3});
					tablaG491.setWidthPercentage(100);
					tablaG491.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaG491.getDefaultCell().setPadding(3);
					tablaG491.getDefaultCell().setUseAscender(true);
					tablaG491.getDefaultCell().setUseDescender(true);
					tablaG491.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaG491.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Organización Beneficiaria",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Tipo de incentivo",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Support value chain",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaG491.addCell(encabezadoTablaG);
					
					datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaG491()){
						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);					
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTwelve(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnThirteen(),fontContenidoTablas));
						tablaG491.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
					}
					document.add(tablaG491);
					
					salvaguardaG = new Paragraph();					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(9).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(4).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(10).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaG);
					
					PdfPTable tablaG501 = new PdfPTable(new float[] { 3, 3, 3,3});
					tablaG501.setWidthPercentage(100);
					tablaG501.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaG501.getDefaultCell().setPadding(3);
					tablaG501.getDefaultCell().setUseAscender(true);
					tablaG501.getDefaultCell().setUseDescender(true);
					tablaG501.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaG501.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Actividad",fontCabeceraTabla));
					tablaG501.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Presupuesto",fontCabeceraTabla));
					tablaG501.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Resultado",fontCabeceraTabla));
					tablaG501.addCell(encabezadoTablaG);					
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaG501.addCell(encabezadoTablaG);
					
					datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaG501()){
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaG501.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
						tablaG501.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaG501.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaG501.addCell(datosTablaG);																		
						datosTablaG=new Paragraph();
					}
					document.add(tablaG501);

					salvaguardaG = new Paragraph();					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(11).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(5).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(12).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					document.add(salvaguardaG);
					
					PdfPTable tablaG511 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3});
					tablaG511.setWidthPercentage(100);
					tablaG511.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaG511.getDefaultCell().setPadding(3);
					tablaG511.getDefaultCell().setUseAscender(true);
					tablaG511.getDefaultCell().setUseDescender(true);
					tablaG511.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaG511.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Auto identificación étnica",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Pueblo / Nacionalidad",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nro hombres",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Nro mujeres",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Monitoreo remoto",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Monitoreo in situ",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Periodicidad",fontCabeceraTabla));
					tablaG511.addCell(encabezadoTablaG);
					
					
					datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaG512()){
						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);					
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnSeven(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnEight(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTen(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnEleven(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTwelve(),fontContenidoTablas));
						tablaG511.addCell(datosTablaG);						
						datosTablaG=new Paragraph();
						
					}
					document.add(tablaG511);
					
					salvaguardaG = new Paragraph();					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(13).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					if(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(6).isVaanYesnoAnswerValue())
						salvaguardaG.add(new Phrase("SI", fontTitulos));
					else
						salvaguardaG.add(new Phrase("NO", fontContenido));					
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaPreguntasG().get(14).getQuesContentQuestion(), fontTitulos));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(Chunk.NEWLINE));
					salvaguardaG.add(new Phrase(getSeguimientoSalvaguardaBean().getListaValoresRespuestasG().get(7).getVaanTextAnswerValue(),fontContenido));
					document.add(salvaguardaG);
					
					Paragraph informacionOpcional = new Paragraph();
					informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					informacionOpcional.add(new Phrase(Chunk.NEWLINE));
					document.add(informacionOpcional);

					PdfPTable tablaOPG = new PdfPTable(new float[] { 7, 7,7 });
					tablaOPG.setWidthPercentage(100);
					tablaOPG.setHorizontalAlignment(Element.ALIGN_LEFT);
					tablaOPG.getDefaultCell().setPadding(3);
					tablaOPG.getDefaultCell().setUseAscender(true);
					tablaOPG.getDefaultCell().setUseDescender(true);
					tablaOPG.getDefaultCell().setBorderColor(BaseColor.BLACK);				
					tablaOPG.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

					encabezadoTablaG=new Paragraph();				
					encabezadoTablaG.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
					tablaOPG.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
					tablaOPG.addCell(encabezadoTablaG);
					encabezadoTablaG=new Paragraph();	
					encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
					tablaOPG.addCell(encabezadoTablaG);

					datosTablaG=new Paragraph();
					for(TableResponses tabla: getSeguimientoSalvaguardaBean().getTablaSalvaguardaOPG()){
						datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
						tablaOPG.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
						tablaOPG.addCell(datosTablaG);
						datosTablaG=new Paragraph();
						datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
						tablaOPG.addCell(datosTablaG);
					}
					document.add(tablaOPG);

					
				}

				Paragraph saltoLinea=new Paragraph();
				saltoLinea.add(new Phrase(Chunk.NEWLINE));

				document.add(saltoLinea);


				document.close();
				FacesContext context = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
				response.setContentType("application/pdf");				
				response.setHeader("Content-Disposition","attachment; filename=" + new StringBuilder().append("resumenSalvaguardas").append(".pdf").toString());				
				response.getOutputStream().write(Archivos.getBytesFromFile(new File(directorioArchivoPDF)));
				response.getOutputStream().flush();
				response.getOutputStream().close();
				context.responseComplete();

	}catch(IOException e){
//		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.generaPdf"));			
		log.error(new StringBuilder().append("RegistrarDespachoHojaControlBacking.generarPdfEntregaEquiposHojaControl ").append(e.getMessage()));									
	} catch (DocumentException e) {			
//		Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.generaPdf"));
		log.error(new StringBuilder().append("RegistrarDespachoHojaControlBacking.generarPdfEntregaEquiposHojaControl ").append(e.getMessage()));									
	} 
}

public void volverABuscarProyectos(){
	getSeguimientoSalvaguardaBean().setDatosProyecto(false);
	vaciarValores();
}

public void finalizarReporteSalvaguardas(){
	try{
		getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards().setAdexIsReported(true);
		getAdvanceExecutionSafeguardsFacade().edit(getSeguimientoSalvaguardaBean().getAdvanceExecutionSafeguards());
		getSeguimientoSalvaguardaBean().setDatosProyecto(false);
		vaciaDatosBusqueda();
	}catch(Exception e){
		e.printStackTrace();
	}
}


}
