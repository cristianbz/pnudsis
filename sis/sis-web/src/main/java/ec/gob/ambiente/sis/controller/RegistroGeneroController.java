/**
@autor proamazonia [Christian Báez]  26 may. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.ProjectsGenderInfo;
import ec.gob.ambiente.sigma.services.CatalogFacade;
import ec.gob.ambiente.sigma.services.CatalogTypeFacade;
import ec.gob.ambiente.sigma.services.ProjectsGenderInfoFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.RegistroGeneroBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.DetailAdvanceGender;
import ec.gob.ambiente.sis.model.GenderAdvances;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.DetailAdvanceGenderFacade;
import ec.gob.ambiente.sis.services.GenderAdvancesFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.OperacionesCatalogo;
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
   	
		
	@PostConstruct
	private void init(){
		try{
			getComponenteBuscarProyectos().setEsReporteGenero(true);
			getRegistroGeneroBean().setListadoLineaGenero(new ArrayList<>());
			getRegistroGeneroBean().setListadoLineaGenero(getCatalogTypeFacade().listaLineasGenero());
			cargaProvincias();
			cargaTipoOrganizacion();
			getRegistroGeneroBean().setListaValoresRespuestas(new ArrayList<>());
			getRegistroGeneroBean().setListaPreguntas(new ArrayList<>());
			getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
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
			getRegistroGeneroBean().setCodigoProjectGenderInfo(null);
			getRegistroGeneroBean().setCodigoProjectGenderInfoTransformativo(null);
			getRegistroGeneroBean().setCodigoLineaEstrategica(null);
			getRegistroGeneroBean().setCodProvincia(null);
			getRegistroGeneroBean().setCodCanton(null);
			getRegistroGeneroBean().setCodParroquia(null);
			getRegistroGeneroBean().setLineaEstrategica(null);
			getRegistroGeneroBean().setListaLineaGenero(new ArrayList<>());
			if(getComponenteBuscarProyectos().proyectoSeleccionado()!=null)
				getRegistroGeneroBean().setListaLineaGenero(getProjectsGenderInfoFacade().listaProjectsGenderInfo(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId()));
			getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(null);
			getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
			getRegistroGeneroBean().setAvanceGeneroTransformadorSeleccionado(new GenderAdvances());
			getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
			getRegistroGeneroBean().setListaDatosAvanceGeneroTransformador(new ArrayList<>());
			getRegistroGeneroBean().setListaPreguntas(new ArrayList<>());
			getRegistroGeneroBean().setListaValoresRespuestas(new ArrayList<>());
			getRegistroGeneroBean().setListaInformacionGeneroSensible(new ArrayList<>());
			getRegistroGeneroBean().setListaInformacionGeneroTransformador(new ArrayList<>());
			getRegistroGeneroBean().setTablaRespuestas3(new ArrayList<>());
			getRegistroGeneroBean().setTablaRespuestas4(new ArrayList<>());
			getRegistroGeneroBean().setTablaRespuestas5(new ArrayList<>());
			getRegistroGeneroBean().setTablaRespuestas6(new ArrayList<>());
			getRegistroGeneroBean().setTablaRespuestas7(new ArrayList<>());
			if(getRegistroGeneroBean().getListaLineaGenero().size() > 0 && datosProyecto()){
				getRegistroGeneroBean().setDatosGeneroParaMostrar(true);				
				getRegistroGeneroBean().setAdvanceExecutionSafeguards(getComponenteBuscarProyectos().getAdvanceExecution());
				organizaValoresInformacionGeneroPorTipoResultado(getProjectsGenderInfoFacade().listaProjectsGenderInfo(getComponenteBuscarProyectos().proyectoSeleccionado().getProjId()));
//				Mensaje.actualizarComponente(":form:panelTablaGenero1");				
				getRegistroGeneroBean().setListaPreguntas(getQuestionsFacade().buscaPreguntasGenero());
				if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null ){
					getRegistroGeneroBean().setListaValoresRespuestas(getValueAnswersFacade().buscarPorAvanceEjecucionGenero(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()));
					getRegistroGeneroBean().setListaValoresRespuestas(getRegistroGeneroBean().getListaValoresRespuestas().stream().sorted((vr1,vr2)->vr1.getQuestions().getQuesQuestionOrder().compareTo(vr2.getQuestions().getQuesQuestionOrder())).collect(Collectors.toList()));
				}
				if(getRegistroGeneroBean().getListaValoresRespuestas()==null || getRegistroGeneroBean().getListaValoresRespuestas().isEmpty())
					valoresRespuestasPorDefecto(getRegistroGeneroBean().getListaPreguntas(), getRegistroGeneroBean().getListaValoresRespuestas());
				preparaRespuestasGenero();
			}else{
				getRegistroGeneroBean().setDatosGeneroParaMostrar(false);
//				Mensaje.actualizarComponente(":form:growl");
//				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
								
			}
			Mensaje.actualizarComponente(":form:informacionGenero1");
			Mensaje.actualizarComponente(":form:informacionGenero2");
			Mensaje.actualizarComponente(":form:panelTablasGenero");
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
	
	public void organizaValoresInformacionGeneroPorTipoResultado(List<ProjectsGenderInfo> listaValores){
		getRegistroGeneroBean().setListaInformacionGeneroSensible(new ArrayList<>());
		getRegistroGeneroBean().setListaInformacionGeneroTransformador(new ArrayList<>());
		for (ProjectsGenderInfo genero : listaValores) {
			if(genero.getPginResultsType().equals("1"))
				getRegistroGeneroBean().getListaInformacionGeneroSensible().add(genero);
			else if(genero.getPginResultsType().equals("2"))
				getRegistroGeneroBean().getListaInformacionGeneroTransformador().add(genero);
		}
	}
	/**
	 * Recupera datos de linea estrategica de genero
	 */
	public void obtieneLineaEstrategica(){
		for (ProjectsGenderInfo genero : getRegistroGeneroBean().getListaInformacionGeneroSensible()) {
			if(getRegistroGeneroBean().getCodigoProjectGenderInfo() == genero.getPginId()){
				getRegistroGeneroBean().setLineaEstrategica(genero.getCataId().getCatyId().getCatyDescription());
				getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(genero);
			}
		}		
	}
	public void obtieneLineaEstrategicaGeneroTransformador(){		
		for (ProjectsGenderInfo genero : getRegistroGeneroBean().getListaInformacionGeneroTransformador()) {
			if(getRegistroGeneroBean().getCodigoProjectGenderInfoTransformativo() == genero.getPginId()){
				getRegistroGeneroBean().setLineaEstrategica(genero.getCataId().getCatyId().getCatyDescription());
				getRegistroGeneroBean().setInformacionProyectoGeneroSeleccionado(genero);
			}
		}
	}
	
	
	public void buscaDatosAvanceGenero(){
		try{		
			getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());			
			obtieneLineaEstrategica();
			
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null){
				GenderAdvances avancesGenero = getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(getRegistroGeneroBean().getCodigoProjectGenderInfo(),getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId());				
				if(avancesGenero == null){
					getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
					getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadIndicator(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginIndicator());
					getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadGoalProgram(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginResults());
					getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadExecutedBudget(Double.parseDouble(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginBudget()));
					getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadPercentageAdvance(0);

				}else{
					getRegistroGeneroBean().setAvanceGeneroSeleccionado(avancesGenero);
					buscaDatosDetalleAvanceGenero(1);
				}
			}else{
				getRegistroGeneroBean().setAvanceGeneroSeleccionado(new GenderAdvances());
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadIndicator(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginIndicator());
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadGoalProgram(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginResults());
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadExecutedBudget(Double.parseDouble(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginBudget()));
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadPercentageAdvance(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void buscaDatosAvanceGeneroTransformador(){
		try{					
			getRegistroGeneroBean().setListaDatosAvanceGeneroTransformador(new ArrayList<>());			
			obtieneLineaEstrategicaGeneroTransformador();
			if(getRegistroGeneroBean().getAdvanceExecutionSafeguards()!=null){
				GenderAdvances avancesGenero = getGenderAdvancesFacade().buscaPorProjectGenderInfoAvanceExecution(getRegistroGeneroBean().getCodigoProjectGenderInfoTransformativo(),getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId());				
				if(avancesGenero == null){
					getRegistroGeneroBean().setAvanceGeneroTransformadorSeleccionado(new GenderAdvances());
					getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadIndicator(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginIndicator());
					getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadGoalProgram(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginResults());
					getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadExecutedBudget(Double.parseDouble(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginBudget()));
					getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadPercentageAdvance(0);
				}else{
					getRegistroGeneroBean().setAvanceGeneroTransformadorSeleccionado(avancesGenero);
					buscaDatosDetalleAvanceGenero(2);
				}
			}else{
				getRegistroGeneroBean().setAvanceGeneroTransformadorSeleccionado(new GenderAdvances());				
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadIndicator(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginIndicator());
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadGoalProgram(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginResults());
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadExecutedBudget(Double.parseDouble(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado().getPginBudget()));
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadPercentageAdvance(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Busca datos del detalle avance genero
	 */
	public void buscaDatosDetalleAvanceGenero(int tipo){
		try{
			getRegistroGeneroBean().setListaDatosAvanceGenero(new ArrayList<>());
			if ( tipo ==1){
				for (DetailAdvanceGender detalle : getRegistroGeneroBean().getAvanceGeneroSeleccionado().getDetailAdvanceGenderList()) {
					if (detalle.isDtagStatus())					
						getRegistroGeneroBean().getListaDatosAvanceGenero().add(detalle);					
				}
			}else{
				for (DetailAdvanceGender detalle : getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().getDetailAdvanceGenderList()) {
					if (detalle.isDtagStatus())					
							getRegistroGeneroBean().getListaDatosAvanceGeneroTransformador().add(detalle);
				}
			}
			preparaInformacionTablaGenero(tipo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void preparaInformacionTablaGenero(int tipo){
		if(tipo == 1){
			for (DetailAdvanceGender detalle : getRegistroGeneroBean().getListaDatosAvanceGenero()) {
				detalle.setProvincia(ubicaProvinciaCantonParroquia(detalle.getDtagProvince(), 1));
				detalle.setCanton(ubicaProvinciaCantonParroquia(detalle.getDtagCanton(), 2));
				detalle.setParroquia(ubicaProvinciaCantonParroquia(detalle.getDtagParish(), 3));
			}
		}else{
			for (DetailAdvanceGender detalle : getRegistroGeneroBean().getListaDatosAvanceGeneroTransformador()) {
				detalle.setProvincia(ubicaProvinciaCantonParroquia(detalle.getDtagProvince(), 1));
				detalle.setCanton(ubicaProvinciaCantonParroquia(detalle.getDtagCanton(), 2));
				detalle.setParroquia(ubicaProvinciaCantonParroquia(detalle.getDtagParish(), 3));
			}
		}
	}
	public void mostrarDialogoGrabarAvanceGenero(int tipo){
		getRegistroGeneroBean().setTipoAvanceGeneroGrabar(tipo);
		Mensaje.verDialogo("dlgGrabaAvanceGenero");
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
			if(getRegistroGeneroBean().getTipoAvanceGeneroGrabar() == 1){
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setProjectsGenderInfo(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado());
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadCreationDate(new Date());
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadCreatorUser(usuario.getUserName());
				getRegistroGeneroBean().getAvanceGeneroSeleccionado().setGeadStatus(true);
			}else{
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setProjectsGenderInfo(getRegistroGeneroBean().getInformacionProyectoGeneroSeleccionado());
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadCreationDate(new Date());
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadCreatorUser(usuario.getUserName());
				getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().setGeadStatus(true);
			}
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
			}
			if(getRegistroGeneroBean().getTipoAvanceGeneroGrabar() == 1)
				avanceEjecucion = getAvanceEjecucionGeneroFacade().grabarEditarAvanceEjecucionGenero(getRegistroGeneroBean().getAdvanceExecutionSafeguards(),getRegistroGeneroBean().getAvanceGeneroSeleccionado());
			else if(getRegistroGeneroBean().getTipoAvanceGeneroGrabar() == 2)
				avanceEjecucion = getAvanceEjecucionGeneroFacade().grabarEditarAvanceEjecucionGenero(getRegistroGeneroBean().getAdvanceExecutionSafeguards(),getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado());
			getRegistroGeneroBean().setAdvanceExecutionSafeguards(avanceEjecucion);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.grabarAvanceGenero"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarAvanceGenero " + ": ").append(e.getMessage()));
		}
	}
	
	
	public void nuevoRegistroGeneroTabla1(){
		if(getComponenteBuscarProyectos().getAdvanceExecution().getAdexId()!=null && getRegistroGeneroBean().getAvanceGeneroSeleccionado().getGeadId()!=null){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero1(true);
			getRegistroGeneroBean().setDetailAdvanceGender(new DetailAdvanceGender());
		}
	}
	public void nuevoRegistroGeneroTabla2(){
		if(getComponenteBuscarProyectos().getAdvanceExecution().getAdexId()!=null && getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado().getGeadId() !=null){
			getRegistroGeneroBean().setNuevoRegistroTablaGenero2(true);
			getRegistroGeneroBean().setDetailAdvanceGenderTransformacion(new DetailAdvanceGender());
		}
	}
	public void nuevoRegistroGeneroTabla3(){
		if(getRegistroGeneroBean().getAdvanceExecutionSafeguards().getAdexId()!=null ){
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
		
		if(getRegistroGeneroBean().getCodigoAutoIdentificacion()==54)
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
				getDetailAdvanceGenderFacade().agregarEditarRegistroDetalleAvanceGenero(getRegistroGeneroBean().getDetailAdvanceGender());
				preparaInformacionTablaGenero(1);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
			getRegistroGeneroBean().setNuevoRegistroTablaGenero1(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregaDetalleRegistroGeneroTabla2(){
		try{
			if(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagId()== null){				
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagProvince(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagCanton(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagParish(getRegistroGeneroBean().getCodParroquia());
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setGenderAdvances(getRegistroGeneroBean().getAvanceGeneroTransformadorSeleccionado());
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagProvince(), 1));
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagCanton(), 2));
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagParish(), 3));
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagStatus(true);
				getDetailAdvanceGenderFacade().agregarEditarRegistroDetalleAvanceGenero(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion());
				getRegistroGeneroBean().getListaDatosAvanceGeneroTransformador().add(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}else{
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagProvince(getRegistroGeneroBean().getCodProvincia());
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagCanton(getRegistroGeneroBean().getCodCanton());
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setDtagParish(getRegistroGeneroBean().getCodParroquia());				
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setProvincia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagProvince(), 1));
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setCanton(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagCanton(), 2));
				getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().setParroquia(ubicaProvinciaCantonParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagParish(), 3));				
				getDetailAdvanceGenderFacade().agregarEditarRegistroDetalleAvanceGenero(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion());
				preparaInformacionTablaGenero(2);
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.infoGrabada"), "");
			}
			getRegistroGeneroBean().setNuevoRegistroTablaGenero2(false);
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
//					preparaInformacionTablaGenero(2);
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
//					preparaInformacionTablaGenero(2);
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
					getTableResponsesFacade().edit(getRegistroGeneroBean().getFilaTabla5());
//					preparaInformacionTablaGenero(2);
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
//					preparaInformacionTablaGenero(2);
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
//					preparaInformacionTablaGenero(2);
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
	}
	public void editarRegistroTabla2(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagProvince());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagCanton());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getDetailAdvanceGenderTransformacion().getDtagParish());
	}
	public void editarRegistroTabla3(){
		getRegistroGeneroBean().setCodProvincia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberOne());
		filtraCantones();
		getRegistroGeneroBean().setCodCanton(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberTwo());
		filtraParroquias();
		getRegistroGeneroBean().setCodParroquia(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberThree());
		getRegistroGeneroBean().setCodigoAutoIdentificacion(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFour());
		getRegistroGeneroBean().setCodigoPuebloNacionalidad(getRegistroGeneroBean().getFilaTabla3().getTareColumnNumberFive());
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
				case 2:
					getRegistroGeneroBean().getListaDatosAvanceGeneroTransformador().remove(getRegistroGeneroBean().getRegistroTablaGenero());
					break;
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
			case 2:
				Mensaje.verDialogo("dlgEliminaItemTabla2");
				break;
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
			
		}
	}
}

