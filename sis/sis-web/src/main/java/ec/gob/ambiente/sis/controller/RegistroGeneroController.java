/**
@autor proamazonia [Christian Báez]  26 may. 2021

 **/
package ec.gob.ambiente.sis.controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.tuple.entity.EntityMetamodel.GenerationStrategyPair;

import ec.gob.ambiente.sigma.model.CatalogType;
import ec.gob.ambiente.sigma.model.ProjectsGenderInfo;
import ec.gob.ambiente.sigma.services.CatalogFacade;
import ec.gob.ambiente.sigma.services.CatalogTypeFacade;
import ec.gob.ambiente.sigma.services.ProjectsGenderInfoFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.RegistroGeneroBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.DetailAdvanceGender;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.GenderAdvances;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.DetailAdvanceGenderFacade;
import ec.gob.ambiente.sis.services.ExecutiveSummariesFacade;
import ec.gob.ambiente.sis.services.GenderAdvancesFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.OperacionesCatalogo;
import ec.gob.ambiente.sis.utils.ResumenPDF;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRespuestaEnum;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import ec.gob.ambiente.suia.model.Users;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class RegistroGeneroController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(RegistroGeneroController.class);

	private static final int CODIGO_IDENTIFICACION_INDIGENA = 54;

	@Getter
	@Setter
	@Inject
	private MensajesController mensajesController;

	@Inject
	@Getter
	private ComponenteBuscaProyectos componenteBuscarProyectos;

	@Inject
	@Getter
	private RegistroGeneroBean registroGeneroBean;

	@Inject
	@Getter
	private AplicacionBean aplicacionBean;

	@Inject
	@Getter
	private AplicacionController aplicacionController;

	@EJB
	@Getter
	private ProjectsGenderInfoFacade projectsGenderInfoFacade;

	@EJB
	@Getter
	private GenderAdvancesFacade genderAdvancesFacade;

	@EJB
	@Getter
	private DetailAdvanceGenderFacade detailAdvanceGenderFacade;

	@EJB
	@Getter
	private AdvanceExecutionSafeguardsFacade avanceEjecucionGeneroFacade;

	@EJB
	@Getter
	private CatalogTypeFacade catalogTypeFacade;

	@EJB
	@Getter
	private CatalogFacade catalogFacade;

	@EJB
	@Getter
	private QuestionsFacade questionsFacade;

	@EJB
	@Getter
	private ValueAnswersFacade valueAnswersFacade;

	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;

	@EJB
	@Getter
	private ExecutiveSummariesFacade executiveSummarieFacade;

	private Users usuario;

	private String rutaPDF;

	private String datosProyectoSeleccionado;

	@PostConstruct
	private void init(){
		try{
			getComponenteBuscarProyectos().setEsReporteGenero(true);
			getRegistroGeneroBean().setListadoLineaGenero(new ArrayList<>());
			getRegistroGeneroBean().setListadoLineaGenero(getCatalogTypeFacade().listaLineasGenero());
			CatalogType catalogoTipoOtros = new CatalogType();
			catalogoTipoOtros.setCatyId(1000);
			catalogoTipoOtros.setCatyDescription("Otros temas");
			getRegistroGeneroBean().getListadoLineaGenero().add(catalogoTipoOtros);
			cargaProvincias();
			cargaTipoOrganizacion();
			cargaTipoIncentivo();
			getRegistroGeneroBean().setListaValoresRespuestas(new ArrayList<>());
			getRegistroGeneroBean().setListaPreguntas(new ArrayList<>());
			getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
			usuario=new Users();
			usuario.setUserName("Christian Báez");
			usuario.setUserId(0);
			getRegistroGeneroBean().setPosicionTab(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean datosProyecto(){		
		return getComponenteBuscarProyectos().datosProyecto();
	}
	/**
	 * Carga los datos de genero del proyecto seleccionado
	 */
	public void cargaDatosProyectoSeleccionado(){
		try{
			getRegistroGeneroBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getAdvanceExecution());
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null && getRegistroGeneroBean().getAdvanceExecutionSafeguards().isAdexIsReported() && getRegistroGeneroBean().getAdvanceExecutionSafeguards().isAdexIsGender()){
				getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
				Mensaje.actualizarComponente(":form:growl");
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.proyectoReportado"), "");
			}else{
				getRegistroGeneroBean().setCodigoLineaGenero(null);
				getRegistroGeneroBean().setCodigoProjectGenderInfo(null);
				getRegistroGeneroBean().setCodigoProjectGenderInfoTransformativo(null);
				getRegistroGeneroBean().setCodigoLineaEstrategica(null);
				getRegistroGeneroBean().setCodProvincia(null);
				getRegistroGeneroBean().setCodCanton(null);
				getRegistroGeneroBean().setCodParroquia(null);
				getRegistroGeneroBean().setListaLineaGenero(new ArrayList<>());
				getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(null);
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
				getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
				getRegistroGeneroBean().setListaPreguntas(new ArrayList<>());
				getRegistroGeneroBean().setListaValoresRespuestas(new ArrayList<>());
				getRegistroGeneroBean().setListaInformacionGeneroSensible(new ArrayList<>());
				getRegistroGeneroBean().setListaInformacionGeneroTransformador(new ArrayList<>());
				getRegistroGeneroBean().setTablaRespuestas3(new ArrayList<>());
				getRegistroGeneroBean().setTablaRespuestas4(new ArrayList<>());
				getRegistroGeneroBean().setTablaRespuestas5(new ArrayList<>());
				getRegistroGeneroBean().setTablaRespuestas6(new ArrayList<>());
				getRegistroGeneroBean().setTablaRespuestas7(new ArrayList<>());
				getRegistroGeneroBean().setListaPreguntas(getQuestionsFacade().buscaPreguntasGenero());
				getRegistroGeneroBean().setPreguntasGenero(false);
				if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null)
					getRegistroGeneroBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				else
					getRegistroGeneroBean().setResumenEjecutivo(new ExecutiveSummaries());
				if(datosProyecto()){
					if(getProjectsGenderInfoFacade().listaProjectsGenderInfo(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId()).size()>0){
						getRegistroGeneroBean().setDatosGeneroParaMostrar(true);
						getRegistroGeneroBean().setProyectoSeleccionado(getComponenteBuscarProyectos().proyectoSeleccionado());
						getRegistroGeneroBean().setSocioImplementador(getComponenteBuscarProyectos().getBuscaProyectosBean().getSocioImplementador());
					}else{
						getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
						Mensaje.actualizarComponente(":form:growl");
						Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.proyectoSinGenero"), "");					
					}
				}else{
					getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
				}
				cargaValoresPreguntasRespuestas();
				preparaRespuestasGenero();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * Busca informacion del proyecto genero informacion seleccionado
	 * @param projectGenderInfo
	 */
	public void buscaInformacionLineaAccion(ProjectsGenderInfo projectGenderInfo){
		try{
			getRegistroGeneroBean().setCodigoProjectGenderInfo(projectGenderInfo.getPginId());
			getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(projectGenderInfo);
			if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()!=null && getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null){
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(projectGenderInfo.getPginId(), getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()!=null){
					for (DetailAdvanceGender detalle : getRegistroGeneroBean().getAvanceGeneroSeleccionado().getDetailAdvanceGenderList()) {
						if (detalle.isDtagStatus())					
							getRegistroGeneroBean().getListaDatosAvanceGenero().add(detalle);					
					}
					preparaInformacionDetalleAvanceGenero();				
				}else{
					getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
				}
				cargaValoresPreguntasRespuestas();
				getRegistroGeneroBean().setPreguntasGenero(true);
				preparaRespuestasGenero();
				getRegistroGeneroBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getRegistroGeneroBean().getResumenEjecutivo()==null){
					getRegistroGeneroBean().setResumenEjecutivo(new ExecutiveSummaries());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuCreationDate(new Date());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuStatus(true);
					getRegistroGeneroBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				}
			}else{
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
				getRegistroGeneroBean().setPreguntasGenero(false);
				cargaValoresPreguntasRespuestas();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	public void preparaInformacionDetalleAvanceGenero(){
		for (DetailAdvanceGender detalle : getRegistroGeneroBean().getListaDatosAvanceGenero()) {
			detalle.setProvincia(ubicaProvinciaCantonParroquia(detalle.getDtagProvince(), 1));
			detalle.setCanton(ubicaProvinciaCantonParroquia(detalle.getDtagCanton(), 2));
			detalle.setParroquia(ubicaProvinciaCantonParroquia(detalle.getDtagParish(), 3));
			detalle.setEtnia(OperacionesCatalogo.ubicaDescripcionCatalogo(detalle.getDtagEthnicity(), getAplicacionBean().getListaAutoIdentificacion()));
			if(detalle.getDtagEthnicity() == CODIGO_IDENTIFICACION_INDIGENA){
				detalle.setPueblo(OperacionesCatalogo.ubicaDescripcionCatalogo(detalle.getDtagTown(), getAplicacionBean().getListaPueblosNacionalidades()));
			}
		}
	}

	public void cargaValoresPreguntasRespuestas(){
		try{
			getRegistroGeneroBean().setListaPreguntas(getQuestionsFacade().buscaPreguntasGenero());
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null && getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId() != null){
				getRegistroGeneroBean().setListaValoresRespuestas(getValueAnswersFacade().buscarPorAvanceEjecucionGenero(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				getRegistroGeneroBean().setListaValoresRespuestas(getRegistroGeneroBean().getListaValoresRespuestas().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				if(getRegistroGeneroBean().getListaValoresRespuestas().size()==0)
					valoresRespuestasPorDefecto(getRegistroGeneroBean().getListaPreguntas(), getRegistroGeneroBean().getListaValoresRespuestas());
			}else		
				valoresRespuestasPorDefecto(getRegistroGeneroBean().getListaPreguntas(), getRegistroGeneroBean().getListaValoresRespuestas());
			getRegistroGeneroBean().setPreguntasGenero(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void preparaRespuestasGenero(){
		try{
			List<TableResponses> listaRespuestas=new ArrayList<>();
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!= null && getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId() !=null )
				listaRespuestas = getTableResponsesFacade().buscarPorAvanceEjecucionYGenero(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId());
			getRegistroGeneroBean().setTablaRespuestas3(listaRespuestas.stream().filter((sc)->sc.getQuestions().getQuesId().equals(174)).collect(Collectors.toList()));
			getRegistroGeneroBean().getTablaRespuestas3().forEach(res->{
				res.setTareProvincia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(ubicaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));				
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
			});
			getRegistroGeneroBean().setTablaRespuestas4(listaRespuestas.stream().filter((sc)->sc.getQuestions().getQuesId().equals(175)).collect(Collectors.toList()));
			getRegistroGeneroBean().getTablaRespuestas4().forEach(res->{
				res.setTareProvincia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(ubicaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));				
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
			});
			getRegistroGeneroBean().setTablaRespuestas5(listaRespuestas.stream().filter((sc)->sc.getQuestions().getQuesId().equals(176)).collect(Collectors.toList()));
			getRegistroGeneroBean().getTablaRespuestas5().forEach(res->{
				res.setTareProvincia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(ubicaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));				
				if(res.getTareColumnNumberFour() == 54)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
			});
			getRegistroGeneroBean().setTablaRespuestas6(listaRespuestas.stream().filter((sc)->sc.getQuestions().getQuesId().equals(177)).collect(Collectors.toList()));
			getRegistroGeneroBean().getTablaRespuestas6().forEach(res->{
				res.setTareProvincia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(ubicaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));				
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
			});
			getRegistroGeneroBean().setTablaRespuestas7(listaRespuestas.stream().filter((sc)->sc.getQuestions().getQuesId().equals(178)).collect(Collectors.toList()));
			getRegistroGeneroBean().getTablaRespuestas7().forEach(res->{
				res.setTareProvincia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberOne(), 1));
				res.setTareCanton(ubicaProvinciaCantonParroquia(res.getTareColumnNumberTwo(), 2));
				res.setTareParroquia(ubicaProvinciaCantonParroquia(res.getTareColumnNumberThree(), 3));				
				res.setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));				
				if(res.getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA)
					res.setTareGenericoDos(OperacionesCatalogo.ubicaDescripcionCatalogo(res.getTareColumnNumberFive(),getAplicacionBean().getListaPueblosNacionalidades()));
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void valoresRespuestasPorDefecto(List<Questions> listaPreguntas, List<ValueAnswers> listaRespuestas){
		for (Questions  preguntas : listaPreguntas) {
			if(preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.CHECKBOX.getCodigo()) || preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.RADIOBUTTON.getCodigo())
					|| preguntas.getCatalogs().getCataId().equals(TipoRespuestaEnum.TEXTO.getCodigo())){
				ValueAnswers valoresRespuestas=new ValueAnswers();
				valoresRespuestas.setQuestions(preguntas);
				valoresRespuestas.setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
				valoresRespuestas.setVaanStatus(true);
				valoresRespuestas.setVaanNumericAnswerValue(0);
				valoresRespuestas.setVaanTextAnswerValue("");
				valoresRespuestas.setVaanYesnoAnswerValue(false);
				listaRespuestas.add(valoresRespuestas);
			}
		}
	}
	
	public void mostrarDialogoGrabarAvanceGenero1(){
		if(getRegistroGeneroBean().getListaLineaGenero().size()>0 ){
			if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()==null && getRegistroGeneroBean().getAdvanceExecutionSafeguards()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.agregaBeneficiarioGenero"), "");
			}else{
				Mensaje.verDialogo("dlgGrabaAvanceGenero");
			}
		}
	}
	
	public void mostrarDialogoGrabarAvanceGenero(){
		if(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado()!=null ){
			
			if(validaOpcionSiTablas()){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionSi"),"");
			}else if(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado()!=null){
				Mensaje.verDialogo("dlgGrabaAvanceGenero");
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.lineaAccion"), "");
			}
		}else{
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.lineaAccion"), "");
			Mensaje.actualizarComponente(":form:growl");
		}
	}

	/**
	 * Graba el avance de genero
	 */
	public void grabarAvanceGenero(){
		try{
			Users usuario=new Users();
			usuario.setUserId(0);
			usuario.setUserName("Christian Baez");
			AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
			getRegistroGeneroBean().getAvanceGeneroSeleccionado().setProjectsGenderInfo(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado());
			getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadCreationDate(new Date());
			getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadCreatorUser(usuario.getUserName());
			getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadStatus(true);
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards() == null || getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId() == null){				
				getRegistroGeneroBean().setAdvanceExecutionSafeguards(new AdvanceExecutionSafeguards());
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexCreationDate(new Date());
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexRegisterDate(new Date());
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexCreatorUser(usuario.getUserName());
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexIsGender(true);
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexIsReported(false);
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexStatus(true);
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setUsers(usuario);				
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setProjects(getComponenteBuscarProyectos().getBuscaProyectosBean().getProyectoSeleccionado());		
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setValueAnswersList(getRegistroGeneroBean().getListaValoresRespuestas());
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexTermFrom(String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde()));
				getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexTermTo(String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat("12"));
			}else{
				String desde="";
				String hasta="";
				desde = String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat(getComponenteBuscarProyectos().getBuscaProyectosBean().getPeriodoDesde());
				hasta = String.valueOf(getComponenteBuscarProyectos().getBuscaProyectosBean().getAnioReporte()).concat("-").concat("12");
				if(!getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexTermFrom().equals(desde)){
					getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexTermFrom(desde);
					getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexTermTo(hasta);
					getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexUpdateUser(usuario.getUserName());
					getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexUpdateDate(new Date());
					
				}
			}
			if(getRegistroGeneroBean().getResumenEjecutivo().getExsuId()==null){
				getRegistroGeneroBean().getResumenEjecutivo().setExsuCreationDate(new Date());
				getRegistroGeneroBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				getRegistroGeneroBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
				getRegistroGeneroBean().getResumenEjecutivo().setExsuSummaryContent("");
				getRegistroGeneroBean().getResumenEjecutivo().setExsuStatus(true);
			}
			avanceEjecucion = getAvanceEjecucionGeneroFacade().grabarEditarAvanceEjecucionGenero(getRegistroGeneroBean().getAdvanceExecutionSafeguards(),getRegistroGeneroBean().getAvanceGeneroSeleccionado(),getRegistroGeneroBean().getResumenEjecutivo());
			getRegistroGeneroBean().setAdvanceExecutionSafeguards(avanceEjecucion);
//			getRegistroGeneroBean().setPreguntasGenero(true);
			getRegistroGeneroBean().setAvanceGeneroSeleccionado(getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginId(), getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
			getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getGenderAdvancesList().set(0, getRegistroGeneroBean().getAvanceGeneroSeleccionado());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarAvanceGenero"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarAvanceGenero " + ": ").append(e.getMessage()));
		}
	}


	public void nuevoRegistroGeneroTabla1(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null && getRegistroGeneroBean().getAvanceGeneroSeleccionado().getGeadId()!=null){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero1(true);
			getRegistroGeneroBean().setDetailAdvanceGender(new DetailAdvanceGender());
			getRegistroGeneroBean().setCodCanton(null);
			getRegistroGeneroBean().setCodigoAutoIdentificacion(null);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);
			getRegistroGeneroBean().setCodProvincia(null);
			getRegistroGeneroBean().setCodParroquia(null);
		}else{
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.grabarGenero"), "");
		}
	}

	public void nuevoRegistroGeneroTabla3(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null ){
			//			getRegistroGeneroBean().setNuevoRegistroPreguntasGenero(true);
			getRegistroGeneroBean().setNuevoRegistroTablaGenero3(true);
			getRegistroGeneroBean().setFilaTabla3(new TableResponses());			
			getRegistroGeneroBean().setCodCanton(0);
			getRegistroGeneroBean().setCodProvincia(0);
			getRegistroGeneroBean().setCodParroquia(0);
			getRegistroGeneroBean().setCodigoAutoIdentificacion(0);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);

		}
	}

	public void nuevoRegistroGeneroTabla4(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null ){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero4(true);
			getRegistroGeneroBean().setFilaTabla4(new TableResponses());			
			getRegistroGeneroBean().setCodCanton(0);
			getRegistroGeneroBean().setCodProvincia(0);
			getRegistroGeneroBean().setCodParroquia(0);
			getRegistroGeneroBean().setCodigoAutoIdentificacion(0);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);

		}
	}

	public void nuevoRegistroGeneroTabla5(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null ){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero5(true);
			getRegistroGeneroBean().setFilaTabla5(new TableResponses());			
			getRegistroGeneroBean().setCodCanton(0);
			getRegistroGeneroBean().setCodProvincia(0);
			getRegistroGeneroBean().setCodParroquia(0);
			getRegistroGeneroBean().setCodigoAutoIdentificacion(0);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);

		}
	}

	public void nuevoRegistroGeneroTabla6(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null ){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero6(true);
			getRegistroGeneroBean().setFilaTabla6(new TableResponses());			
			getRegistroGeneroBean().setCodCanton(0);
			getRegistroGeneroBean().setCodProvincia(0);
			getRegistroGeneroBean().setCodParroquia(0);
			getRegistroGeneroBean().setCodigoAutoIdentificacion(0);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);

		}
	}
	public void nuevoRegistroGeneroTabla7(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null ){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero7(true);
			getRegistroGeneroBean().setFilaTabla7(new TableResponses());			
			getRegistroGeneroBean().setCodCanton(0);
			getRegistroGeneroBean().setCodProvincia(0);
			getRegistroGeneroBean().setCodParroquia(0);
			getRegistroGeneroBean().setCodigoAutoIdentificacion(0);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);

		}
	}

	public void habilitaPuebloNacionalidad(){

		if(getRegistroGeneroBean().getCodigoAutoIdentificacion() == CODIGO_IDENTIFICACION_INDIGENA)
			getRegistroGeneroBean().setHabilitaPuebloNacionalidad(true);
		else{
			getRegistroGeneroBean().setHabilitaPuebloNacionalidad(false);
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(0);
		}
	}

	public void cargaTipoOrganizacion(){
		try{
			getRegistroGeneroBean().setListadoTipoOrganizaciones(new ArrayList<>());
			for (Catalogs catalog : getAplicacionBean().getListaTipoOrganizacion()) {
				getRegistroGeneroBean().getListadoTipoOrganizaciones().add(catalog.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Carga los tipos de incentivos
	 */	
	public void cargaTipoIncentivo(){
		try{
			getRegistroGeneroBean().setListadoTipoIncentivo(new ArrayList<>());
			for (Catalogs catalog : getAplicacionBean().getListaTipoIncentivo()) {
				getRegistroGeneroBean().getListadoTipoIncentivo().add(catalog.getCataText1());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Agrega registros al detalle del avance de genero en la tabla 1
	 */
	public void agregaDetalleRegistroGeneroTabla1(){
		try{
			if(getRegistroGeneroBean().getDetailAdvanceGender().getDtagId()== null){				
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagProvince(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagCanton(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagParish(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getDetailAdvanceGender().setGenderAdvances(getRegistroGeneroBean().getAvanceGeneroSeleccionado());
				getRegistroGeneroBean().getDetailAdvanceGender().setProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagProvince(), 1));
				getRegistroGeneroBean().getDetailAdvanceGender().setCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagCanton(), 2));
				getRegistroGeneroBean().getDetailAdvanceGender().setParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagParish(), 3));
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagEthnicity(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getDetailAdvanceGender().setEtnia(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getDetailAdvanceGender().getDtagEthnicity(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getDetailAdvanceGender().getDtagEthnicity() == CODIGO_IDENTIFICACION_INDIGENA){
					getRegistroGeneroBean().getDetailAdvanceGender().setDtagTown(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
					getRegistroGeneroBean().getDetailAdvanceGender().setPueblo(ubicaPuebloNacionalidad(getRegistroGeneroBean().getDetailAdvanceGender().getDtagTown()));
				}else{
					getRegistroGeneroBean().getDetailAdvanceGender().setDtagTown(null);
				}
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagStatus(true);
				getDetailAdvanceGenderFacade().agregarEditarRegistroDetalleAvanceGenero(getRegistroGeneroBean().getDetailAdvanceGender());
				getRegistroGeneroBean().getListaDatosAvanceGenero().add(getRegistroGeneroBean().getDetailAdvanceGender());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}else{
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagProvince(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagCanton(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagParish(getRegistroGeneroBean().getCodParroquia());				
				getRegistroGeneroBean().getDetailAdvanceGender().setProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagProvince(), 1));
				getRegistroGeneroBean().getDetailAdvanceGender().setCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagCanton(), 2));
				getRegistroGeneroBean().getDetailAdvanceGender().setParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagParish(), 3));				
				getRegistroGeneroBean().getDetailAdvanceGender().setDtagEthnicity(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getDetailAdvanceGender().setEtnia(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getDetailAdvanceGender().getDtagEthnicity(), getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getDetailAdvanceGender().getDtagEthnicity() == CODIGO_IDENTIFICACION_INDIGENA){
					getRegistroGeneroBean().getDetailAdvanceGender().setDtagTown(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
					getRegistroGeneroBean().getDetailAdvanceGender().setPueblo(ubicaPuebloNacionalidad(getRegistroGeneroBean().getDetailAdvanceGender().getDtagTown()));
				}else{
					getRegistroGeneroBean().getDetailAdvanceGender().setDtagTown(null);
					getRegistroGeneroBean().getDetailAdvanceGender().setPueblo("");
				}
				getDetailAdvanceGenderFacade().agregarEditarRegistroDetalleAvanceGenero(getRegistroGeneroBean().getDetailAdvanceGender());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
			getRegistroGeneroBean().setNuevoRegistroTablaGenero1(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void agregaDetalleRegistroGeneroTabla3(){
		try{
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberOne(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberTwo(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberThree(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getFilaTabla3().setTareProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberOne(), 1));
				getRegistroGeneroBean().getFilaTabla3().setTareCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberTwo(), 2));
				getRegistroGeneroBean().getFilaTabla3().setTareParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberThree(), 3));
				getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberFour(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberFive(0);
				getRegistroGeneroBean().getFilaTabla3().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getFilaTabla3().getTareId()== null){				
					getRegistroGeneroBean().getFilaTabla3().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla3().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(0));					
					getRegistroGeneroBean().getFilaTabla3().setTareStatus(true);					
					if(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla3().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFive()));
					}else{
						getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberFive(0);
					}
					getRegistroGeneroBean().getFilaTabla3().setTareColumnTree(String.join(",", getRegistroGeneroBean().getTipoOrganizacionSeleccionados() ));
					getTableResponsesFacade().agregaRespuestaTabla(getRegistroGeneroBean().getFilaTabla3(), getRegistroGeneroBean().getListaValoresRespuestas().get(0));
					getRegistroGeneroBean().getTablaRespuestas3().add(getRegistroGeneroBean().getFilaTabla3());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}else{

					getRegistroGeneroBean().getFilaTabla3().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla3().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(0));					
					getRegistroGeneroBean().getFilaTabla3().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
					if(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla3().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFive()));
					}else
						getRegistroGeneroBean().getFilaTabla3().setTareColumnNumberFive(0);
					getRegistroGeneroBean().getFilaTabla3().setTareColumnTree(String.join(",", getRegistroGeneroBean().getTipoOrganizacionSeleccionados() ));
					getTableResponsesFacade().edit(getRegistroGeneroBean().getFilaTabla3());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}
				getRegistroGeneroBean().setNuevoRegistroTablaGenero3(false);

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void agregaDetalleRegistroGeneroTabla4(){
		try{
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberOne(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberTwo(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberThree(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getFilaTabla4().setTareProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberOne(), 1));
				getRegistroGeneroBean().getFilaTabla4().setTareCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberTwo(), 2));
				getRegistroGeneroBean().getFilaTabla4().setTareParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberThree(), 3));
				getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberFour(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberFive(0);
				getRegistroGeneroBean().getFilaTabla4().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getFilaTabla4().getTareId()== null){				
					getRegistroGeneroBean().getFilaTabla4().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla4().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(1));					
					getRegistroGeneroBean().getFilaTabla4().setTareStatus(true);					
					if(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla4().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFive()));
					}else{
						getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberFive(0);
					}

					getTableResponsesFacade().agregaRespuestaTabla(getRegistroGeneroBean().getFilaTabla4(), getRegistroGeneroBean().getListaValoresRespuestas().get(1));
					getRegistroGeneroBean().getTablaRespuestas4().add(getRegistroGeneroBean().getFilaTabla4());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}else{

					getRegistroGeneroBean().getFilaTabla4().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla4().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(1));					
					getRegistroGeneroBean().getFilaTabla4().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
					if(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla4().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFive()));
					}else
						getRegistroGeneroBean().getFilaTabla4().setTareColumnNumberFive(0);					
					getTableResponsesFacade().edit(getRegistroGeneroBean().getFilaTabla4());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}
				getRegistroGeneroBean().setNuevoRegistroTablaGenero4(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void agregaDetalleRegistroGeneroTabla5(){
		try{
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberOne(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberTwo(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberThree(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getFilaTabla5().setTareProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberOne(), 1));
				getRegistroGeneroBean().getFilaTabla5().setTareCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberTwo(), 2));
				getRegistroGeneroBean().getFilaTabla5().setTareParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberThree(), 3));
				getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberFour(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberFive(0);
				getRegistroGeneroBean().getFilaTabla5().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getFilaTabla5().getTareId()== null){				
					getRegistroGeneroBean().getFilaTabla5().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla5().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(2));					
					getRegistroGeneroBean().getFilaTabla5().setTareStatus(true);					
					if(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla5().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFive()));
					}else{
						getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberFive(0);
					}
					getRegistroGeneroBean().getFilaTabla5().setTareColumnTwo(String.join(",", getRegistroGeneroBean().getTipoIncentivoSeleccionado() ));
					getTableResponsesFacade().agregaRespuestaTabla(getRegistroGeneroBean().getFilaTabla5(), getRegistroGeneroBean().getListaValoresRespuestas().get(2));
					getRegistroGeneroBean().getTablaRespuestas5().add(getRegistroGeneroBean().getFilaTabla5());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}else{

					getRegistroGeneroBean().getFilaTabla5().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla5().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(2));					
					getRegistroGeneroBean().getFilaTabla5().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
					if(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla5().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFive()));
					}else
						getRegistroGeneroBean().getFilaTabla5().setTareColumnNumberFive(0);					
					getRegistroGeneroBean().getFilaTabla5().setTareColumnTwo(String.join(",", getRegistroGeneroBean().getTipoIncentivoSeleccionado() ));
					getTableResponsesFacade().edit(getRegistroGeneroBean().getFilaTabla5());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}
				getRegistroGeneroBean().setNuevoRegistroTablaGenero5(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void agregaDetalleRegistroGeneroTabla6(){
		try{
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberOne(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberTwo(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberThree(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getFilaTabla6().setTareProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberOne(), 1));
				getRegistroGeneroBean().getFilaTabla6().setTareCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberTwo(), 2));
				getRegistroGeneroBean().getFilaTabla6().setTareParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberThree(), 3));
				getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberFour(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberFive(0);
				getRegistroGeneroBean().getFilaTabla6().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getFilaTabla6().getTareId()== null){				
					getRegistroGeneroBean().getFilaTabla6().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla6().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(3));					
					getRegistroGeneroBean().getFilaTabla6().setTareStatus(true);					
					if(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla6().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFive()));
					}else{
						getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberFive(0);
					}

					getTableResponsesFacade().agregaRespuestaTabla(getRegistroGeneroBean().getFilaTabla6(), getRegistroGeneroBean().getListaValoresRespuestas().get(3));
					getRegistroGeneroBean().getTablaRespuestas6().add(getRegistroGeneroBean().getFilaTabla6());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}else{

					getRegistroGeneroBean().getFilaTabla6().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla6().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(3));					
					getRegistroGeneroBean().getFilaTabla6().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
					if(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla6().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFive()));
					}else
						getRegistroGeneroBean().getFilaTabla6().setTareColumnNumberFive(0);					
					getTableResponsesFacade().edit(getRegistroGeneroBean().getFilaTabla6());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}
				getRegistroGeneroBean().setNuevoRegistroTablaGenero6(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void agregaDetalleRegistroGeneroTabla7(){
		try{
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.primeroGrabarSalvaguarda"), "");
			}else{
				getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberOne(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberTwo(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberThree(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getFilaTabla7().setTareProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberOne(), 1));
				getRegistroGeneroBean().getFilaTabla7().setTareCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberTwo(), 2));
				getRegistroGeneroBean().getFilaTabla7().setTareParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberThree(), 3));
				getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberFour(getRegistroGeneroBean().getCodigoAutoIdentificacion());
				getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberFive(0);
				getRegistroGeneroBean().getFilaTabla7().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
				if(getRegistroGeneroBean().getFilaTabla7().getTareId()== null){				
					getRegistroGeneroBean().getFilaTabla7().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla7().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(4));					
					getRegistroGeneroBean().getFilaTabla7().setTareStatus(true);					
					if(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla7().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFive()));
					}else{
						getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberFive(0);
					}

					getTableResponsesFacade().agregaRespuestaTabla(getRegistroGeneroBean().getFilaTabla7(), getRegistroGeneroBean().getListaValoresRespuestas().get(3));
					getRegistroGeneroBean().getTablaRespuestas7().add(getRegistroGeneroBean().getFilaTabla7());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}else{

					getRegistroGeneroBean().getFilaTabla7().setAdvanceExecutionSaveguards(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
					getRegistroGeneroBean().getFilaTabla7().setQuestions(getRegistroGeneroBean().getListaPreguntas().get(4));					
					getRegistroGeneroBean().getFilaTabla7().setTareGenerico(OperacionesCatalogo.ubicaDescripcionCatalogo(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFour(),getAplicacionBean().getListaAutoIdentificacion()));
					if(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFour() == CODIGO_IDENTIFICACION_INDIGENA){
						getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberFive(getRegistroGeneroBean().getCodigoPuebloNacionalidad());
						getRegistroGeneroBean().getFilaTabla7().setTareGenericoDos(ubicaPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFive()));
					}else
						getRegistroGeneroBean().getFilaTabla7().setTareColumnNumberFive(0);					
					getTableResponsesFacade().edit(getRegistroGeneroBean().getFilaTabla7());
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
				}
				getRegistroGeneroBean().setNuevoRegistroTablaGenero7(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Edita un registro de la tabla 1
	 */
	public void editarRegistroTabla1(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagProvince());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getDetailAdvanceGender().getDtagCanton());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getDetailAdvanceGender().getDtagParish());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getDetailAdvanceGender().getDtagEthnicity());
		if(getRegistroGeneroBean().getCodigoAutoIdentificacion() == CODIGO_IDENTIFICACION_INDIGENA){
			getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getDetailAdvanceGender().getDtagTown());
			getRegistroGeneroBean().setHabilitaPuebloNacionalidad(true);
		}
	}
	public void editarRegistroTabla3(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberOne());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberTwo());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberThree());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFour());
		getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFive());
		getRegistroGeneroBean().setTipoOrganizacionSeleccionados(getRegistroGeneroBean().getFilaTabla3().getTareColumnTree().split(","));

	}
	public void editarRegistroTabla4(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberOne());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberTwo());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberThree());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFour());
		getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla4().getTareColumnNumberFive());
	}
	public void editarRegistroTabla5(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberOne());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberTwo());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberThree());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFour());
		getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla5().getTareColumnNumberFive());
		getRegistroGeneroBean().setTipoIncentivoSeleccionado(getRegistroGeneroBean().getFilaTabla5().getTareColumnTwo().split(","));
	}
	public void editarRegistroTabla6(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberOne());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberTwo());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberThree());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFour());
		getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla6().getTareColumnNumberFive());
	}
	public void editarRegistroTabla7(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberOne());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberTwo());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberThree());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFour());
		getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla7().getTareColumnNumberFive());
	}
	/**
	 * Elimina un registro de la tabla
	 */
	public void eliminarRegistroTablaGenero(int codigo){
		try{
			switch(codigo){
			case 1:
				getRegistroGeneroBean().getListaDatosAvanceGenero().remove(getRegistroGeneroBean().getRegistroTablaGenero());
				break;
				//				case 2:
				//					getRegistroGeneroBean().getListaDatosAvanceGeneroTransformador().remove(getRegistroGeneroBean().getRegistroTablaGenero());
				//					break;
			case 3:
				getRegistroGeneroBean().getTablaRespuestas3().remove(getRegistroGeneroBean().getFilaTabla3());
				break;
			case 4:
				getRegistroGeneroBean().getTablaRespuestas4().remove(getRegistroGeneroBean().getFilaTabla4());
				break;
			case 5:
				getRegistroGeneroBean().getTablaRespuestas5().remove(getRegistroGeneroBean().getFilaTabla5());
				break;
			case 6:
				getRegistroGeneroBean().getTablaRespuestas6().remove(getRegistroGeneroBean().getFilaTabla6());
				break;
			case 7:
				getRegistroGeneroBean().getTablaRespuestas7().remove(getRegistroGeneroBean().getFilaTabla7());
				break;	
			}
			getDetailAdvanceGenderFacade().eliminaRegistroDetalleAvanceGenero(getRegistroGeneroBean().getRegistroTablaGenero());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Muestra el cuadro de dialogo para eliminar segun el tipo
	 * @param codigo indica el tipo de dialogo a mostrar
	 */
	public void mostrarDialogoEliminaDatosTabla(int codigo){
		switch(codigo){
		case 1:
			Mensaje.verDialogo("dlgEliminaItemTabla1");
			break;
			//			case 2:
			//				Mensaje.verDialogo("dlgEliminaItemTabla2");
			//				break;
		case 3:
			Mensaje.verDialogo("dlgEliminaItemTabla3");
			break;
		case 4:
			Mensaje.verDialogo("dlgEliminaItemTabla4");
			break;	
		case 5:
			Mensaje.verDialogo("dlgEliminaItemTabla5");
			break;
		case 6:
			Mensaje.verDialogo("dlgEliminaItemTabla6");
			break;
		case 7:
			Mensaje.verDialogo("dlgEliminaItemTabla7");
			break;	
		}
	}

	/**
	 * Carga las provincias de la division politica
	 */
	public void cargaProvincias(){
		getRegistroGeneroBean().setListaProvincias(new ArrayList<>());

		for (Object[] objects : getAplicacionController().getAplicacionBean().getListaProvincias()) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			getRegistroGeneroBean().getListaProvincias().add(geo);
		}
	}
	public void filtraCantones(){
		getRegistroGeneroBean().setListaCantones(new ArrayList<>());
		getRegistroGeneroBean().setListaParroquias(new ArrayList<>());
		List<Object[]> listaTemporal =getAplicacionBean().getListaTodosCantones().stream().filter(canton->Integer.valueOf(canton[3].toString()).equals(getRegistroGeneroBean().getCodProvincia())).collect(Collectors.toList());		
		for (Object[] objects : listaTemporal) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			getRegistroGeneroBean().getListaCantones().add(geo);
		}
		Collections.sort(getRegistroGeneroBean().getListaCantones(), new Comparator<GeographicalLocations>(){
            @Override
            public int compare(GeographicalLocations o1, GeographicalLocations o2) {
                return o1.getGeloName().compareToIgnoreCase(o2.getGeloName());
            }
        });
	}

	public void filtraParroquias(){
		getRegistroGeneroBean().setListaParroquias(new ArrayList<>());
		List<Object[]> listaTemporal = getAplicacionBean().getListaTodasParroquias().stream().filter(canton->Integer.valueOf(canton[3].toString()).equals(getRegistroGeneroBean().getCodCanton())).collect(Collectors.toList());		
		for (Object[] objects : listaTemporal) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			getRegistroGeneroBean().getListaParroquias().add(geo);
		}
		Collections.sort(getRegistroGeneroBean().getListaParroquias(), new Comparator<GeographicalLocations>(){
            @Override
            public int compare(GeographicalLocations o1, GeographicalLocations o2) {
                return o1.getGeloName().compareToIgnoreCase(o2.getGeloName());
            }
        });
	}
	/**
	 * 
	 * @param codigo
	 * @param tipo 1 provincia  2 canton  3 parroquia
	 * @return
	 */
	public String ubicaProvinciaCantonParroquia(int codigo, int tipo){
		String resultado="";
		if(tipo == 1){
			for(Object[] provincia:getAplicacionBean().getListaProvincias()){
				if(Integer.valueOf(provincia[1].toString()) == codigo){
					resultado = provincia[0].toString();
					break;
				}
			}
		}else if(tipo == 2){
			for(Object[] canton:getAplicacionBean().getListaTodosCantones()){
				if(Integer.valueOf(canton[1].toString()) == codigo){
					resultado = canton[0].toString();
					break;
				}
			}
		}else if(tipo == 3){
			for(Object[] parroquia:getAplicacionBean().getListaTodasParroquias()){
				if(Integer.valueOf(parroquia[1].toString()) == codigo){
					resultado = parroquia[0].toString();
					break;
				}
			}
		}
		return resultado;
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

	public void mostrarDialogoEliminaValoresTabla(String codigo){
		getRegistroGeneroBean().setCodigoTablaDatos(codigo);
		if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G3")){
			if (validaDatosTabla(getRegistroGeneroBean().getTablaRespuestas3()) 
					&& getRegistroGeneroBean().getListaValoresRespuestas().get(0).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G4")){
			if (validaDatosTabla(getRegistroGeneroBean().getTablaRespuestas4()) 
					&& getRegistroGeneroBean().getListaValoresRespuestas().get(1).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G5")){
			if (validaDatosTabla(getRegistroGeneroBean().getTablaRespuestas5()) 
					&& getRegistroGeneroBean().getListaValoresRespuestas().get(2).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G6")){
			if (validaDatosTabla(getRegistroGeneroBean().getTablaRespuestas6()) 
					&& getRegistroGeneroBean().getListaValoresRespuestas().get(3).isVaanYesnoAnswerValue()==false){
				Mensaje.verDialogo("dlgEliminaDatosTabla");
			}
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G7")){
			if (validaDatosTabla(getRegistroGeneroBean().getTablaRespuestas7()) 
					&& getRegistroGeneroBean().getListaValoresRespuestas().get(4).isVaanYesnoAnswerValue()==false){
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
			if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G3")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getRegistroGeneroBean().getTablaRespuestas3(),getRegistroGeneroBean().getListaValoresRespuestas().get(0));
				getRegistroGeneroBean().setTablaRespuestas3(new ArrayList<>());
				getRegistroGeneroBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:panelTablaGenero3");
			}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G4")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getRegistroGeneroBean().getTablaRespuestas4(),getRegistroGeneroBean().getListaValoresRespuestas().get(1));
				getRegistroGeneroBean().setTablaRespuestas4(new ArrayList<>());
				getRegistroGeneroBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:panelTablaGenero4");
			}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G5")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getRegistroGeneroBean().getTablaRespuestas5(),getRegistroGeneroBean().getListaValoresRespuestas().get(2));
				getRegistroGeneroBean().setTablaRespuestas5(new ArrayList<>());				
				getRegistroGeneroBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:panelTablaGenero5");
			}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G6")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getRegistroGeneroBean().getTablaRespuestas6(),getRegistroGeneroBean().getListaValoresRespuestas().get(3));
				getRegistroGeneroBean().setTablaRespuestas6(new ArrayList<>());				
				getRegistroGeneroBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:panelTablaGenero6");
			}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G7")){
				getTableResponsesFacade().eliminarDatosTablaActualizaValueAnswer(getRegistroGeneroBean().getTablaRespuestas7(),getRegistroGeneroBean().getListaValoresRespuestas().get(4));
				getRegistroGeneroBean().setTablaRespuestas7(new ArrayList<>());				
				getRegistroGeneroBean().setCodigoTablaDatos("");
				Mensaje.actualizarComponente(":form:panelTablaGenero7");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cancelaEliminaValoresTablaDatos(){
		if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G3")){
			getRegistroGeneroBean().getListaValoresRespuestas().get(0).setVaanYesnoAnswerValue(true);
			getRegistroGeneroBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:radiopSB1");			
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G4")){
			getRegistroGeneroBean().getListaValoresRespuestas().get(1).setVaanYesnoAnswerValue(true);
			getRegistroGeneroBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:radiopSB2"); 			
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G5")){
			getRegistroGeneroBean().getListaValoresRespuestas().get(2).setVaanYesnoAnswerValue(true);
			getRegistroGeneroBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:radiopSB3"); 
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G6")){
			getRegistroGeneroBean().getListaValoresRespuestas().get(3).setVaanYesnoAnswerValue(true);
			getRegistroGeneroBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:radiopSB4"); 
		}else if(getRegistroGeneroBean().getCodigoTablaDatos().equals("G7")){
			getRegistroGeneroBean().getListaValoresRespuestas().get(4).setVaanYesnoAnswerValue(true);
			getRegistroGeneroBean().setCodigoTablaDatos("");
			Mensaje.actualizarComponente(":form:radiopSB5"); 
		}
	}
	public void cargaLineasGenero(){
		try{
			getRegistroGeneroBean().setListadoLineaGenero(new ArrayList<>());
			getRegistroGeneroBean().setListadoLineaGenero(getCatalogTypeFacade().listaLineasGenero());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void cargaInformacionGeneroProyecto(){
		try{
			List<ProjectsGenderInfo> listaAux=new ArrayList<>();
			getRegistroGeneroBean().setListaLineaGenero(new ArrayList<>());
			if(getRegistroGeneroBean().getCodigoLineaGenero()<1000){
				listaAux=getProjectsGenderInfoFacade().listaPorProyectoLineaGenero(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId(), getRegistroGeneroBean().getCodigoLineaGenero());
				for (ProjectsGenderInfo infoGenero : listaAux) {
					infoGenero.setGenderAdvancesList(new ArrayList<>());
					
					if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!= null && getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null){
						GenderAdvances generoAvances= getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(infoGenero.getPginId(), getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId());					
						if(generoAvances == null){						
							infoGenero.getGenderAdvancesList().add(new GenderAdvances());
						
						}else{
							infoGenero.getGenderAdvancesList().add(generoAvances);
						
						}
//						if (infoGenero.getPginResultsType() != null){
//							if(infoGenero.getPginOtherLine()==null)
//								infoGenero.setPginDescripcionLineaAccion(infoGenero.getCataId().getCataText2());
//							else if(infoGenero.getPginOtherLine()!=null)
//								infoGenero.setPginDescripcionLineaAccion(infoGenero.getPginOtherLine());
//							getRegistroGeneroBean().getListaLineaGenero().add(infoGenero);
//						}
					}else
						infoGenero.getGenderAdvancesList().add(new GenderAdvances());
					if (infoGenero.getPginResultsType() != null){
						if(infoGenero.getPginOtherLine()==null)
							infoGenero.setPginDescripcionLineaAccion(infoGenero.getCataId().getCataText2());
						else if(infoGenero.getPginOtherLine()!=null)
							infoGenero.setPginDescripcionLineaAccion(infoGenero.getPginOtherLine());
						getRegistroGeneroBean().getListaLineaGenero().add(infoGenero);
					}
				}
//				getRegistroGeneroBean().setListaLineaGenero(getProjectsGenderInfoFacade().listaPorProyectoLineaGenero(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId(), getRegistroGeneroBean().getCodigoLineaGenero()));
			}else if(getRegistroGeneroBean().getCodigoLineaGenero() == 1000){
//				getRegistroGeneroBean().setListaLineaGenero(getProjectsGenderInfoFacade().listaPorProyectoOtrasLineaGenero(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId()));
				listaAux = getProjectsGenderInfoFacade().listaPorProyectoOtrasLineaGenero(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId());
				for (ProjectsGenderInfo infoGenero : listaAux) {
					infoGenero.setPginDescripcionLineaAccion(infoGenero.getPginOtherLine());
					getRegistroGeneroBean().getListaLineaGenero().add(infoGenero);
				}
			}
			getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
			getRegistroGeneroBean().setCodigoProjectGenderInfo(null);
			getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
//			getRegistroGeneroBean().setPreguntasGenero(false);
			getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(null);
			if(getRegistroGeneroBean().getListaLineaGenero().size()==0)
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.noInformacion"), "");
			getRegistroGeneroBean().setMostrarTabla1(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void grabarResumenEjecutivo(AdvanceExecutionSafeguards avanceEjecucion){
		try{
			getRegistroGeneroBean().getResumenEjecutivo().setAdvanceExecutionSafeguards(avanceEjecucion);
			getExecutiveSummarieFacade().grabarResumenEjecutivo(getRegistroGeneroBean().getResumenEjecutivo());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Imprime el pdf del reporte de genero
	 */
	public void imprimirResumenGenero(){
		try{
			if(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado()!=null){
				ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
				String directorioArchivoPDF = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append(1).append(".pdf").toString();
				rutaPDF=directorioArchivoPDF;
				ResumenPDF.reporteGenero(directorioArchivoPDF, getRegistroGeneroBean());
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.lineaAccion"), "");
				Mensaje.actualizarComponente(":form:growl");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void volverBuscarProyectos(){
		getComponenteBuscarProyectos().volverABuscarProyectos();
		vaciaDatosGenero();
	}
	public void vaciaDatosGenero(){
		getRegistroGeneroBean().setCodigoLineaGenero(null);
		getRegistroGeneroBean().setCodigoProjectGenderInfo(null);
		getRegistroGeneroBean().setCodigoProjectGenderInfoTransformativo(null);
		getRegistroGeneroBean().setCodigoLineaEstrategica(null);
		getRegistroGeneroBean().setCodProvincia(null);
		getRegistroGeneroBean().setCodCanton(null);
		getRegistroGeneroBean().setCodParroquia(null);
		getRegistroGeneroBean().setListaLineaGenero(new ArrayList<>());
		getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(null);
		getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
		getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
		getRegistroGeneroBean().setListaPreguntas(new ArrayList<>());
		getRegistroGeneroBean().setListaValoresRespuestas(new ArrayList<>());
		getRegistroGeneroBean().setListaInformacionGeneroSensible(new ArrayList<>());
		getRegistroGeneroBean().setListaInformacionGeneroTransformador(new ArrayList<>());
		getRegistroGeneroBean().setTablaRespuestas3(new ArrayList<>());
		getRegistroGeneroBean().setTablaRespuestas4(new ArrayList<>());
		getRegistroGeneroBean().setTablaRespuestas5(new ArrayList<>());
		getRegistroGeneroBean().setTablaRespuestas6(new ArrayList<>());
		getRegistroGeneroBean().setTablaRespuestas7(new ArrayList<>());
		getRegistroGeneroBean().setPreguntasGenero(false);
		getRegistroGeneroBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getAdvanceExecution());
		getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
	}
	/**
	 * Muestra el dialogo para finalizar reporte de genero
	 */
	public void mostrarDialogoFinalizarReporte(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null ){
			if(validaOpcionSiTablas()){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionSi"),"");
			}else if(getRegistroGeneroBean().getResumenEjecutivo().getExsuSummaryContent().trim().length() < 10){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.ingresoResumenEjecutivo"),"");
			}else{
				grabarResumenEjecutivo(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
				Mensaje.verDialogo("dlgFinalizarReporteGenero");
			}
		}else{
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.avanceGenero"), "");
			Mensaje.actualizarComponente(":form:growl");
		}
	}
	/**
	 * Finaliza el reporte de genero del proyecto
	 */
	public void finalizaReporteGenero(){
		getRegistroGeneroBean().getAdvanceExecutionSafeguards().setAdexIsReported(true);
		getAvanceEjecucionGeneroFacade().edit(getRegistroGeneroBean().getAdvanceExecutionSafeguards());
		getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
		getComponenteBuscarProyectos().volverABuscarProyectos();
		vaciaDatosGenero();
	}
	
	public boolean validaOpcionSiTablas(){
		boolean nocumple=false;
		if((getRegistroGeneroBean().getListaValoresRespuestas().get(0).isVaanYesnoAnswerValue() && getRegistroGeneroBean().getTablaRespuestas3().size()==0)){
			nocumple = true; 
		}else if(getRegistroGeneroBean().getListaValoresRespuestas().get(1).isVaanYesnoAnswerValue() && getRegistroGeneroBean().getTablaRespuestas4().size() == 0){
			nocumple = true;
		}else if(getRegistroGeneroBean().getListaValoresRespuestas().get(2).isVaanYesnoAnswerValue() && getRegistroGeneroBean().getTablaRespuestas5().size() == 0){
			nocumple = true;
		}
		return nocumple;
	}
	
	public void mostrarDialogoDetallesAvanceGenero(ProjectsGenderInfo projectGenderInfo){
		try{			
			getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(projectGenderInfo);
			getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
			if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()!=null && getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null){
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(projectGenderInfo.getPginId(), getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()!=null){
					for (DetailAdvanceGender detalle : getRegistroGeneroBean().getAvanceGeneroSeleccionado().getDetailAdvanceGenderList()) {
						if (detalle.isDtagStatus())					
							getRegistroGeneroBean().getListaDatosAvanceGenero().add(detalle);					
					}
//					preparaInformacionDetalleAvanceGenero();				
				}else{
					getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
				}
				getRegistroGeneroBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getRegistroGeneroBean().getResumenEjecutivo()==null){
					getRegistroGeneroBean().setResumenEjecutivo(new ExecutiveSummaries());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuCreationDate(new Date());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuStatus(true);
					getRegistroGeneroBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				}
//				Mensaje.verDialogo("dlgInfoDetailGenero");
			}else{
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
				getRegistroGeneroBean().setResumenEjecutivo(new ExecutiveSummaries());
				getRegistroGeneroBean().getResumenEjecutivo().setExsuCreationDate(new Date());
				getRegistroGeneroBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
				getRegistroGeneroBean().getResumenEjecutivo().setExsuStatus(true);
				getRegistroGeneroBean().getResumenEjecutivo().setExsuRegisterDate(new Date());

				
//				getRegistroGeneroBean().setPreguntasGenero(false);
//				cargaValoresPreguntasRespuestas();
			}
			
			
			getRegistroGeneroBean().setMostrarTabla1(false);
			Mensaje.verDialogo("dlgInfoDetailGenero");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void mostrarDatosTablaBeneficiarios(ProjectsGenderInfo projectGenderInfo){
		try{
			getRegistroGeneroBean().setCodigoProjectGenderInfo(projectGenderInfo.getPginId());
			getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(projectGenderInfo);
			getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
			getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null)
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(projectGenderInfo.getPginId(), getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
			if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()!=null && getRegistroGeneroBean().getAvanceGeneroSeleccionado().getGeadId()!=null && getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null){
				getRegistroGeneroBean().setMostrarTabla1(true);
//				getRegistroGeneroBean().setAvanceGeneroSeleccionado(getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(projectGenderInfo.getPginId(), getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getRegistroGeneroBean().getAvanceGeneroSeleccionado()!=null){
					for (DetailAdvanceGender detalle : getRegistroGeneroBean().getAvanceGeneroSeleccionado().getDetailAdvanceGenderList()) {
						if (detalle.isDtagStatus())					
							getRegistroGeneroBean().getListaDatosAvanceGenero().add(detalle);					
					}
					preparaInformacionDetalleAvanceGenero();				
				}
				getRegistroGeneroBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
				if(getRegistroGeneroBean().getResumenEjecutivo()==null){
					getRegistroGeneroBean().setResumenEjecutivo(new ExecutiveSummaries());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuCreationDate(new Date());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuUserCreator(usuario.getUserName());
					getRegistroGeneroBean().getResumenEjecutivo().setExsuStatus(true);
					getRegistroGeneroBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				}
			}else{
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.agregaBeneficiarioGenero"), "");
				getRegistroGeneroBean().setPreguntasGenero(false);
				getRegistroGeneroBean().setMostrarTabla1(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void siguienteTab(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null)
			getRegistroGeneroBean().setPosicionTab(1);
		else
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.agregaBeneficiarioGenero"), "");
	}
	public void retrocederTab(){
		getRegistroGeneroBean().setPosicionTab(0);
	}
	public void tabChange() {
		getRegistroGeneroBean().setPosicionTab(0);
		
	}
}
	
