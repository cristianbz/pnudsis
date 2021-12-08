/**
@autor proamazonia [Christian Báez]  16 jun. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsStrategicPartnersFacade;
import ec.gob.ambiente.sis.bean.AdministracionBean;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.Indicators;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.CatalogsTypeFacade;
import ec.gob.ambiente.sis.services.IndicatorsFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
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
}

