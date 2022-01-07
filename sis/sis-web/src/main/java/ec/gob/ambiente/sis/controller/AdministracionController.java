/**
@autor proamazonia [Christian Báez]  16 jun. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.github.cliftonlabs.json_simple.Jsoner;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.MeetingsFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsStrategicPartnersFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AdministracionBean;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.bean.SitioPublicoBean;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoA;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoB;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoC;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoD;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoE;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoF;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoG;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoGenero;
import ec.gob.ambiente.sis.dto.DtoGenero;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.Indicators;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.services.AdvanceExecutionProjectGenderFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.CatalogsTypeFacade;
import ec.gob.ambiente.sis.services.IndicatorsFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRolesUsuarioEnum;
import ec.gob.ambiente.suia.service.RoleFacade;
import ec.gob.ambiente.suia.service.UsersFacade;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class AdministracionController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AdministracionController.class);
	
	private static final int TIPO_RESPUESTA=1;
	
	@EJB
	@Getter
	private ProjectsStrategicPartnersFacade projectsStrategicPartnersFacade;
	
	@EJB
	@Getter
	private QuestionsFacade questionsFacade;
	
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@EJB
	@Getter
	private IndicatorsFacade indicatorsFacade;
	

	@EJB
	@Getter
	private CatalogsFacade catalogsFacade;
	
	@EJB
	@Getter
	private UsersFacade usersFacade;
	
	@EJB
	@Getter
	private CatalogsTypeFacade catalogsTypeFacade;
	
	@EJB
	@Getter
	private RoleFacade roleFacade;
	
    @Getter
    @Setter
    @Inject
    private AplicacionController aplicacionController;
	
	@Inject
	@Getter
	private AdministracionBean administracionBean;
	
	@Inject
	@Getter
	private AplicacionBean aplicacionBean;
	
    @Getter
    @Setter
    @Inject
    private MensajesController mensajesController;

	@Getter
	@Setter
	@Inject
	private LoginBean loginBean;
	
	@Inject
	@Getter
	private SitioPublicoBean sitioPublicoBean;

	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;

	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;

	@EJB
	@Getter
	private MeetingsFacade meetingsFacade;

	@EJB
	@Getter
	private AdvanceExecutionProjectGenderFacade avanceExecutionFacade;
	
	private ResourceBundle rb;
	
	@PostConstruct
	public void init(){
		try{
			getAdministracionBean().setNoEsSocioEstrategico(true);
			getAdministracionBean().setDeshabilitaOrdenPregunta(true);
			getAdministracionBean().setDeshabilitaOrdenCatalogo(true);
			getAdministracionBean().setListaPreguntas(new ArrayList<>());
			getAdministracionBean().setListaIndicadores(new ArrayList<>());
			getAdministracionBean().setListaIndicadores(getIndicatorsFacade().listaTodosIndicadoresGenero());
			getAdministracionBean().setListaPreguntas(getQuestionsFacade().listaPreguntasIngresadas());		
			getAdministracionBean().setListaPreguntasGenero(getQuestionsFacade().buscaTodasPreguntasGenero());
			getAdministracionBean().setListaTipoRespuestaPregunta(getCatalogsFacade().buscaCatalogosPorTipo(TIPO_RESPUESTA));
			getAdministracionBean().setListaCatalogos(getCatalogsFacade().buscaTodosCatalogos());
			Collections.sort(getAdministracionBean().getListaCatalogos(), new Comparator<Catalogs>(){
				@Override
				public int compare(Catalogs o1, Catalogs o2) {
					return o1.getCatalogsType().getCatyDescription().compareToIgnoreCase(o2.getCatalogsType().getCatyDescription());
				}
			});
			getAdministracionBean().setListaTipoCatalogo(getCatalogsTypeFacade().listaTipoCatalogos());
			Collections.sort(getAdministracionBean().getListaTipoCatalogo(), new Comparator<CatalogsType>(){
				@Override
				public int compare(CatalogsType o1, CatalogsType o2) {
					return o1.getCatyDescription().compareToIgnoreCase(o2.getCatyDescription());
				}
			});
			getAdministracionBean().setListaProyectos(getProjectsFacade().buscarTodosLosProyectos());
			getAdministracionBean().setNuevaPregunta(false);
			getAplicacionController().cargarSalvaguardas();
			cargaSalvaguardas();
			rb = ResourceBundle.getBundle("resources.indicadores");
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
		}
	}
	
	public void cargaSalvaguardas(){
		getAdministracionBean().setListaSalvaguardas(new ArrayList<>());
		for (Object[] sf : getAplicacionBean().getListaSalvaguardas() ) {
			Safeguards safe = new Safeguards();
			safe.setSafeId(Integer.valueOf( sf[0].toString()));
			safe.setSafeCode(sf[3].toString());
			getAdministracionBean().getListaSalvaguardas().add(safe);
		}
	}
	/**
	 * Habilita crear nueva pregunta
	 * @param tipo 1 Pregunta salvaguarda  2 Pregunta de genero
	 */
	public void nuevaPregunta(int tipo){
		try{
			int numeroOrden=0;
			getAdministracionBean().setDeshabilitaOrdenPregunta(true);
			getAdministracionBean().setPreguntaSeleccionada(new Questions());
			if(tipo == 1){
				getAdministracionBean().setNuevaPregunta(true);			
				getAdministracionBean().setNuevaPreguntaGenero(false);

			}else{
				getAdministracionBean().setNuevaPreguntaGenero(true);
				getAdministracionBean().setNuevaPregunta(false);
				numeroOrden= getQuestionsFacade().campoOrdenPreguntaGenero();
				if (numeroOrden==0)
					numeroOrden=1;
				else
					numeroOrden++;
				getAdministracionBean().getPreguntaSeleccionada().setQuesQuestionOrder(numeroOrden);
			}			
			getAdministracionBean().setCodigoSalvaguarda(null);
			getAdministracionBean().setCodigoTipoRespuesta(null);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "nuevaPregunta " + ": ").append(e.getMessage()));
		}
	}
	
	public void obtieneValorOrden(){
		try{
			int numeroOrden=0;
			numeroOrden= getQuestionsFacade().campoOrdenPregunta(getAdministracionBean().getCodigoSalvaguarda());
			if (numeroOrden==0)
				numeroOrden=1;
			else
				numeroOrden++;
			getAdministracionBean().getPreguntaSeleccionada().setQuesQuestionOrder(numeroOrden);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "obtieneValorOrden " + ": ").append(e.getMessage()));
		}
	}
	
	public void obtieneValorOrdenCatalogo(){
		try{
			int numeroOrden=0;
			numeroOrden= getCatalogsFacade().campoOrdenCatalogo(getAdministracionBean().getCodigoTipoCatalogo());
			if (numeroOrden==0)
				numeroOrden=1;
			else
				numeroOrden++;
			getAdministracionBean().getCatalogoSeleccionado().setCataOrder(numeroOrden);
			getAdministracionBean().getCatalogoSeleccionado().setCataNumber(numeroOrden);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "obtieneValorOrdenCatalogo " + ": ").append(e.getMessage()));
		}
	}
	
	public void nuevoCatalogo(){
		getAdministracionBean().setNuevoCatalogo(true);
		getAdministracionBean().setCatalogoSeleccionado(new Catalogs());
		getAdministracionBean().setCodigoTipoCatalogo(null);
	}
	public void nuevoIndicador(){
		getAdministracionBean().setNuevoIndicador(true);
		getAdministracionBean().setIndicadorSeleccionado(new Indicators());
	}
	
	/**
	 * Edita una pregunta
	 * @param pregunta
	 */
	public void editarPregunta(Questions pregunta){
		getAdministracionBean().setPreguntaSeleccionada(pregunta);
		getAdministracionBean().setCodigoSalvaguarda(getAdministracionBean().getPreguntaSeleccionada().getSafeguards().getSafeId());
		getAdministracionBean().setNuevaPregunta(true);
		getAdministracionBean().setCodigoTipoRespuesta(getAdministracionBean().getPreguntaSeleccionada().getCatalogs().getCataId());
	}
	public void editarPreguntaGenero(Questions pregunta){
		getAdministracionBean().setPreguntaSeleccionada(pregunta);		
		getAdministracionBean().setNuevaPreguntaGenero(true);
		getAdministracionBean().setCodigoTipoRespuesta(getAdministracionBean().getPreguntaSeleccionada().getCatalogs().getCataId());
	}
	public void editarcatalogo(Catalogs catalogo){
		getAdministracionBean().setCatalogoSeleccionado(catalogo);
		getAdministracionBean().setCodigoTipoCatalogo(catalogo.getCatalogsType().getCatyId());
		getAdministracionBean().setNuevoCatalogo(true);
	}
	public void editarIndicador(Indicators indicador){
		getAdministracionBean().setIndicadorSeleccionado(indicador);
		getAdministracionBean().setNuevoIndicador(true);
	}
	
	public boolean rolSocioEstrategico(String roles){
		boolean respuesta=false;
		
		if(roles.contains(",")){
			String[] cadenas=roles.split(",");
			for(int i=0;i<cadenas.length;i++){
				if(cadenas[i].equals(TipoRolesUsuarioEnum.SIS_socio_estrategico)){
					respuesta= true;
					break;
				}
			}
		}else{
			if(roles.equals(TipoRolesUsuarioEnum.SIS_socio_estrategico.toString())){
				respuesta = true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Agrega o edita una pregunta
	 */
	public void agregarEditarPregunta(){
		try{
			Questions pregunta = getAdministracionBean().getPreguntaSeleccionada();
			Safeguards salvaguarda = new Safeguards();
			Catalogs catalogo = new Catalogs();
			catalogo.setCataId(getAdministracionBean().getCodigoTipoRespuesta());
			salvaguarda.setSafeId(getAdministracionBean().getCodigoSalvaguarda());
			pregunta.setSafeguards(salvaguarda);
			pregunta.setCatalogs(catalogo);
			if(pregunta.getQuesId()!=null){
				pregunta.setQuesUpdateDate(new Date());
				pregunta.setQuesUpdateUser(getLoginBean().getUser().getUserName());
			}else{
				pregunta.setQuesCreatorUser(getLoginBean().getUser().getUserName());
				pregunta.setQuesCreationDate(new Date());
				pregunta.setQuesIsGender(false);
				pregunta.setQuesImportantQuestion(false);
				pregunta.setQuesPrincipalQuestion(false);
				List<Questions> listaHijos=new ArrayList<>();				
				listaHijos.add(pregunta);
				pregunta.setQuestionsList(listaHijos);
				pregunta.setQuesStatus(true);
			}
			getQuestionsFacade().crearEditarPregunta(pregunta);
			getAdministracionBean().setNuevaPregunta(false);			
			getAdministracionBean().setListaPreguntas(getQuestionsFacade().listaPreguntasIngresadas());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "",getMensajesController().getPropiedad("info.infoGrabada"));						
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.grabar"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarEditarPregunta " + ": ").append(e.getMessage()));
		}
	}
	
	public void agregaEditaIndicador(){
		try{
			Indicators indicador=getAdministracionBean().getIndicadorSeleccionado();
			indicador.setIndiCreationDate(new Date());
			indicador.setIndiCreatorUser(getLoginBean().getUser().getUserName());
			if(indicador.getIndiId()!=null){
				indicador.setIndiUpdateDate(new Date());
				indicador.setIndiUpdateUser(getLoginBean().getUser().getUserName());
			}
			getIndicatorsFacade().agregaEditaIndicador(indicador);
			getAdministracionBean().setNuevoIndicador(false);
			getAdministracionBean().setListaIndicadores(getIndicatorsFacade().listaTodosIndicadoresGenero());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "",getMensajesController().getPropiedad("info.infoGrabada"));
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.grabar"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaEditaIndicador " + ": ").append(e.getMessage()));
		}
	}
	
	public void agregarEditarPreguntaGenero(){
		try{
			Questions pregunta = getAdministracionBean().getPreguntaSeleccionada();
			
			Catalogs catalogo = new Catalogs();
			catalogo.setCataId(getAdministracionBean().getCodigoTipoRespuesta());
			
			pregunta.setSafeguards(null);
			pregunta.setCatalogs(catalogo);
			if(pregunta.getQuesId()!=null){
				pregunta.setQuesUpdateDate(new Date());
				pregunta.setQuesUpdateUser(getLoginBean().getUser().getUserName());
			}else{
				
				pregunta.setQuesCreatorUser(getLoginBean().getUser().getUserName());
				pregunta.setQuesCreationDate(new Date());
				pregunta.setQuesIsGender(true);
				pregunta.setQuesImportantQuestion(false);
				pregunta.setQuesPrincipalQuestion(false);
				List<Questions> listaHijos=new ArrayList<>();
				
				listaHijos.add(pregunta);
				pregunta.setQuestionsList(listaHijos);
				pregunta.setQuesStatus(true);
			}
			getQuestionsFacade().crearEditarPregunta(pregunta);
			getAdministracionBean().setNuevaPreguntaGenero(false);
			getAdministracionBean().setListaPreguntasGenero(getQuestionsFacade().buscaTodasPreguntasGenero());	
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  "",getMensajesController().getPropiedad("info.infoGrabada"));						
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.grabar"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarEditarPregunta " + ": ").append(e.getMessage()));
		}
	}
	
	public void agregaEditaCatalogo(){
		try{
			Catalogs catalogo = getAdministracionBean().getCatalogoSeleccionado();
			CatalogsType tipo= new CatalogsType();
			tipo.setCatyId(getAdministracionBean().getCodigoTipoCatalogo());
			catalogo.setCatalogsType(tipo);
			catalogo.setCataNumber(catalogo.getCataOrder());
			if (catalogo.getCataId() == null){
				catalogo.setCataCreatorUser(getLoginBean().getUser().getUserName());
				catalogo.setCataCreationDate(new Date());
				catalogo.setCataStatus(true);
			}else{
				catalogo.setCataUserUpdate(getLoginBean().getUser().getUserName());
				catalogo.setCatalDateUpdate(new Date());
				
			}
			getCatalogsFacade().agregaEditaCatalogo(catalogo);
			getAdministracionBean().setListaCatalogos(getCatalogsFacade().buscaTodosCatalogos());
			Collections.sort(getAdministracionBean().getListaCatalogos(), new Comparator<Catalogs>(){
				@Override
				public int compare(Catalogs o1, Catalogs o2) {
					return o1.getCatalogsType().getCatyDescription().compareToIgnoreCase(o2.getCatalogsType().getCatyDescription());
				}
			});
			getAdministracionBean().setNuevoCatalogo(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.infoGrabada"));						
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.grabar"));
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaEditaCatalogo " + ": ").append(e.getMessage()));
		}
	}
	
	public void eliminarPregunta(){
		try{
			Questions pregunta = getAdministracionBean().getPreguntaSeleccionada();
			pregunta.setQuesUpdateDate(new Date());
			pregunta.setQuesUpdateUser(getLoginBean().getUser().getUserName());
			pregunta.setQuesStatus(false);
			getQuestionsFacade().edit(pregunta);
			getAdministracionBean().setListaPreguntas(getQuestionsFacade().listaPreguntasIngresadas());	
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "eliminarPregunta " + ": ").append(e.getMessage()));
		}
	}
	
	public void generarResumen(){

		List<TableResponses> listaTemp= new ArrayList<>();
		List<TableResponses> listaTempComunidades= new ArrayList<>();		
		List<String> listaComunidades=new ArrayList<>();
		List<TableResponses> listaTempProvincias= new ArrayList<>();
		try{
			List<Integer> listaProyectos=new ArrayList<>();
			BigDecimal totalInversion = new BigDecimal(0);
			listaTemp = getTableResponsesFacade().listaProyectosValoresSalvaguardaA();
			Map<Integer,BigDecimal> mapaTemp=new HashMap<Integer,BigDecimal>();
			for(TableResponses tr: listaTemp){
				mapaTemp.put(tr.getTareColumnNumberSix(), tr.getTareColumnDecimalOne());
				totalInversion = totalInversion.add(tr.getTareColumnDecimalOne());
			}
			for(Entry<Integer,BigDecimal> proy: mapaTemp.entrySet()){
				listaProyectos.add(proy.getKey());
			}			
			DtoDatosSitioPublicoA dtoSalvaguardaA = new DtoDatosSitioPublicoA("A");
			dtoSalvaguardaA.setNumeroProyectos(listaProyectos.size());
			dtoSalvaguardaA.setTotalInversionProyectos(totalInversion);
			dtoSalvaguardaA.setListadoProyectos(getTableResponsesFacade().listadoProyectos());
			////B
			DtoDatosSitioPublicoB dtoSalvaguardaB = new DtoDatosSitioPublicoB("B");
			listaTempComunidades = new ArrayList<>();
			listaTempComunidades = getTableResponsesFacade().listaMaximoHombresMujeresSalvaguardaB();

			for(TableResponses tr: listaTempComunidades){
				if(tr.getTareColumnNumberOne() != null)
					dtoSalvaguardaB.setNumeroHombresB(tr.getTareColumnNumberOne());
				else
					dtoSalvaguardaB.setNumeroHombresB(0);
				if(tr.getTareColumnNumberTwo() != null)	
					dtoSalvaguardaB.setNumeroMujeresB(tr.getTareColumnNumberTwo());
				else
					dtoSalvaguardaB.setNumeroMujeresB(0);
			}

			///// C
			DtoDatosSitioPublicoC dtoSalvaguardaC = new DtoDatosSitioPublicoC("C");
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(57);
			Map<String,Integer> mapaTempC=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTempC.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTempC.entrySet()){
				listaComunidades.add(proy.getKey());
			}			
			dtoSalvaguardaC.setNumeroComunidadesC(listaComunidades.size());
			dtoSalvaguardaC.setNumeroPracticas(getTableResponsesFacade().listaSaberesAncestralesSalvaguardaC());

			///// D
			DtoDatosSitioPublicoD dtoSalvaguardaD = new DtoDatosSitioPublicoD("D");

			dtoSalvaguardaD.setTotalEventosHombres(getMeetingsFacade().listaEventosFortalecimientoHombres());
			dtoSalvaguardaD.setTotalEventosMujeres(getMeetingsFacade().listaEventosFortalecimientoMujeres());

			// E
			DtoDatosSitioPublicoE dtoSalvaguardaE = new DtoDatosSitioPublicoE("E");
			listaTempProvincias = getTableResponsesFacade().listaFomentoGestionComunitariaE();

			dtoSalvaguardaE.setNumeroFomentoGestionComunitaria(listaTempProvincias.size());
			dtoSalvaguardaE.setTotalHectareasCobertura(getTableResponsesFacade().totalHectareasCoberturaE());

			// F
			DtoDatosSitioPublicoF dtoSalvaguardaF = new DtoDatosSitioPublicoF("F");			
			dtoSalvaguardaF.setTotalAccionesReversion(getTableResponsesFacade().numeroAccionesEvitarRiesgos_F());
			dtoSalvaguardaF.setListadoMedidasTomadas(getTableResponsesFacade().listaMedidasTomadas_F());

			// G
			DtoDatosSitioPublicoG dtoSalvaguardaG = new DtoDatosSitioPublicoG("G");
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(136);
			Map<String,Integer> mapaTempG=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTempG.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTempG.entrySet()){
				listaComunidades.add(proy.getKey());
			}

			dtoSalvaguardaG.setNumeroAcciones(getTableResponsesFacade().listaAccionesGeneradasSalvaguardaG());
			dtoSalvaguardaG.setNumeroComunidades(listaComunidades.size());

			DtoDatosSitioPublicoGenero dtoGenero = new DtoDatosSitioPublicoGenero("GENERO");
			List<DtoGenero> listTempTemas = getAvanceExecutionFacade().listaTemasGenero();
			List<String> listTempAcciones = getAvanceExecutionFacade().listadoAccionesGenero();
			dtoGenero.setTotalPresupuesto(getAvanceExecutionFacade().presupuestoGenero());
			int totalTemas = 0;			
			if(listTempTemas!=null && listTempTemas.size()>0){
				dtoGenero.setListaTemasGenero(listTempTemas);
				for (DtoGenero genero : listTempTemas) {
					totalTemas = totalTemas + genero.getNumero();
				}
				dtoGenero.setTotalTemasAplicados(totalTemas);
			}else{
				dtoGenero.setTotalTemasAplicados(0);
				dtoGenero.setListaTemasGenero(null);
			}

			if(listTempAcciones!=null && listTempAcciones.size()>0){
				dtoGenero.setListaAccionesGenero(listTempAcciones);
				dtoGenero.setTotalAccionesImplementadas(listTempAcciones.size());
			}else{
				dtoGenero.setListaAccionesGenero(null);
				dtoGenero.setTotalAccionesImplementadas(0);
			}

			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();
			List<Object> lista = Arrays.asList(dtoSalvaguardaA.toJson(), dtoSalvaguardaB.toJson(),dtoSalvaguardaC.toJson(),dtoSalvaguardaD.toJson(), dtoSalvaguardaE.toJson() , dtoSalvaguardaF.toJson() , dtoSalvaguardaG.toJson(),dtoGenero.toJson());
			try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
				Jsoner.serialize(lista, fileWriter);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.generaArchivo"));
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarResumen " + ": ").append(e.getMessage()));
		}
	}
	public void generaArchivoExcel(){
		try{
			HSSFWorkbook workbook = new HSSFWorkbook();			
			HSSFFont bold = workbook.createFont();
			bold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle styleBold = workbook.createCellStyle();
			styleBold.setFont( bold );
			HSSFSheet sheet = workbook.createSheet("SALVAGUARDA_A");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			HSSFRow row = sheet.createRow(1);
			HSSFCell cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("A_3"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroInstrumentosPolitica_A());
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 4000);
			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("A_4"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(getTableResponsesFacade().cantidadProyectosInvertidos_A().toString()));
			cell.setCellStyle(styleBold);

			
			
			sheet = workbook.createSheet("SALVAGUARDA_B");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_6"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(7));
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_8"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(11));
			cell.setCellStyle(styleBold);

//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_10"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(getTableResponsesFacade().numeroDeHectareas(20).toString()));
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_12"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(20));
			cell.setCellStyle(styleBold);

//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(17);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_14"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(21);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_18"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(25);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_19"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(29);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_23"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(27));
			cell.setCellStyle(styleBold);

//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(33);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_25"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().totalActores());
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(37);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_26"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(29));
			cell.setCellStyle(styleBold);

			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(37);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_27"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroCanalesHabilitados());
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(41);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_30"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(31));
			cell.setCellStyle(styleBold);

//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(45);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_32"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(49);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_34"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
//			sheet.setColumnWidth((short) 0,(short) 30200);
//			sheet.setColumnWidth((short) 1,(short) 8000);
			row = sheet.createRow(49);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_37"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			sheet = workbook.createSheet("SALVAGUARDA_C");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_41"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble( getTableResponsesFacade().numeroAccionesTenenciaTierra().toString()));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_44"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_45"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(54));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_46"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(45));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(17);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_49"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(21);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_51"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(25);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_53"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(57));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(29);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_54"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(33);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("C_57"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(61));
			cell.setCellStyle(styleBold);

			sheet = workbook.createSheet("SALVAGUARDA_D");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_58"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(65));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_61"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().totalActoresDialogo());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_63"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_64"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(17);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_67"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(getTableResponsesFacade().totalMujeresDialogo().toString()));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(22);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_69"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(67));
			cell.setCellStyle(styleBold);
			
			sheet = workbook.createSheet("SALVAGUARDA_E");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("E_71"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(69));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("E_72"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(getTableResponsesFacade().numeroHectareasConsolidadas().toString()));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("E_76"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("E_77"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(17);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("E_79"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(21);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("E_80"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			sheet = workbook.createSheet("SALVAGUARDA_F");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("F_83"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(113));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("F_86"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("F_89"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().numeroDeRegistros(119));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("F_91"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			sheet = workbook.createSheet("SALVAGUARDA_G");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_93"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().totalActoresMonitoreo());
			cell.setCellStyle(styleBold);

			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_94"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_97"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(getTableResponsesFacade().totalBeneficiariosAlternativasEconomicas());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_100"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(getTableResponsesFacade().totalPresupuestoSNMB().toString()));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_102"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BaseDatosSIS").append(".xls").toString();
			FileOutputStream file = new FileOutputStream(archivo);
	        workbook.write(file);
	        file.close();
	        Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.generaArchivo"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

