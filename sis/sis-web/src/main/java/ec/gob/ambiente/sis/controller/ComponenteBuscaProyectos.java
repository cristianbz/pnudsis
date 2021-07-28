/**
@autor proamazonia [Christian Báez]  26 may. 2021

 **/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import com.sun.org.apache.bcel.internal.generic.AALOAD;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsStrategicPartnersFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.BuscaProyectosBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.ProjectUsers;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.services.AdvanceExecutionSafeguardsFacade;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.ExecutiveSummariesFacade;
import ec.gob.ambiente.sis.services.ProjectQuestionsFacade;
import ec.gob.ambiente.sis.services.ProjectUsersFacade;
import ec.gob.ambiente.sis.services.SectorsFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import lombok.Getter;
import lombok.Setter;

@Dependent
public class ComponenteBuscaProyectos implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ComponenteBuscaProyectos.class);

	@Inject
	@Getter
	private BuscaProyectosBean buscaProyectosBean;

	@Getter    
	@Inject
	private MensajesController mensajesController;

	@EJB
	@Getter
	private ExecutiveSummariesFacade executiveSummarieFacade;

	@EJB
	@Getter
	private PartnersFacade partnersFacade;

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
	private LoginBean loginBean;

	@EJB
	@Getter
	private AdvanceExecutionSafeguardsFacade advanceExecutionSafeguardsFacade;

	@EJB
	@Getter
	private ProjectsFacade projectsFacade;

	@EJB
	@Getter
	private ProjectUsersFacade projectUsersFacade;


	@EJB
	@Getter
	private CatalogsFacade catalogsFacade;

	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;

	@EJB
	@Getter
	private ProjectQuestionsFacade projectQuestionsFacade;

	@EJB
	@Getter
	private SectorsFacade sectorsFacade;

	@EJB
	@Getter
	private ProjectsStrategicPartnersFacade projectsStrategicPartnersFacade;

	@Getter
	@Setter
	private boolean esReporteGenero;

	@Getter
	@Setter
	private Integer codigoPartner;

	private boolean esProyecto;
	private boolean nuevoIngreso;

	private List<Safeguards> listadoSalvaguardas;

	@PostConstruct
	public void init(){
		cargaSociosImplementadores();
		getBuscaProyectosBean().setCodigoBusquedaProyecto(0);
		esProyecto=true;
		nuevoIngreso=true;
		getBuscaProyectosBean().setSinDatosProyectoPreguntas(false);
		indicaTipoRol();
	}
	/**
	 * Carga los socios implementadores
	 */
	public void cargaSociosImplementadores(){
		try{
			getBuscaProyectosBean().setListaSociosImplementadores(new ArrayList<>());
			getBuscaProyectosBean().setListaSociosImplementadores(getPartnersFacade().listarSociosImplementadores());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Carga los sectores de interes
	 */
	public void cargaSectoresInteres(){
		try{
			getBuscaProyectosBean().setListaSectoresDisponibles(new ArrayList<Sectors>());
			getBuscaProyectosBean().setListaSectoresDisponibles(getSectorsFacade().buscarTodosLosSectores());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Carga los sectores de interes del proyecto seleccionado
	 */
	public void sectoresInteresProyecto(){

		getBuscaProyectosBean().setListaSectoresSeleccionados(new ArrayList<>());
		if(getBuscaProyectosBean().getAdvanceExecution()!=null && getBuscaProyectosBean().getAdvanceExecution().getAdvanceSectorsList()!=null){
			for(AdvanceSectors as:getBuscaProyectosBean().getAdvanceExecution().getAdvanceSectorsList())
				if(as.isAdseSelectedSector())
					getBuscaProyectosBean().getListaSectoresSeleccionados().add(as.getSectors().getSectId());					
		}
	}

	public void vaciaDatosBusqueda(){
		getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
		getBuscaProyectosBean().setCodigoProyecto(0);
		getBuscaProyectosBean().setCodigoSocioImplementador(0);

		getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
		getBuscaProyectosBean().setSocioImplementador(new Partners());
		getBuscaProyectosBean().setTituloProyecto("");
	}
	/**
	 * Busca proyectos
	 */
	public void buscarProyectos(){
		try{
			getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
			if(getBuscaProyectosBean().getTipoRol()!=3){
				if(getBuscaProyectosBean().getCodigoBusquedaProyecto()==1){			
					getBuscaProyectosBean().setListaProyectos(getProjectsFacade().listarProyectosPorIdSocioImpl(getBuscaProyectosBean().getCodigoSocioImplementador()));
					if(getBuscaProyectosBean().getListaProyectos().size()==0){
						Mensaje.actualizarComponente(":form:growl");				
						Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.noProyectos"), "");
					}

				}else if(getBuscaProyectosBean().getCodigoBusquedaProyecto()==2){
					if (getBuscaProyectosBean().getTituloProyecto().length()>5){
						getBuscaProyectosBean().setListaProyectos(getProjectsFacade().listarProyectosPorTextoTitulo(getBuscaProyectosBean().getTituloProyecto()));
						if(getBuscaProyectosBean().getListaProyectos().size()==0){
							Mensaje.actualizarComponente(":form:growl");				
							Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.noProyectos"), "");
						}
					}else{
						Mensaje.actualizarComponente(":form:growl");				
						Mensaje.verMensaje(FacesMessage.SEVERITY_INFO,  getMensajesController().getPropiedad("info.longitudMinima"), "");
					}

				}else{
					Mensaje.actualizarComponente(":form:growl");				
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionBusqueda") ,"" );
				}
			}else{
				List<ProjectUsers> listaProyectousuarios=new ArrayList<>();
				getBuscaProyectosBean().setListaProyectos(new ArrayList<>());
				//				listaProyectousuarios=getProjectUsersFacade().listaProyectosDelUsuario(getLoginBean().getUser().getUserId());
				listaProyectousuarios=getLoginBean().getListaProyectosDelUsuario();
				for (ProjectUsers pu : listaProyectousuarios) {
					getBuscaProyectosBean().getListaProyectos().add(pu.getProjects());
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Busca el avance de ejecucion por genero o salvaguarda del proyecto
	 * @param proyecto
	 */	
	public void buscaAvanceEjecucionDelProyecto(Projects proyecto){
		try{
			getBuscaProyectosBean().setPeriodoDesde(null);
			getBuscaProyectosBean().setAnioReporte(null);
			getBuscaProyectosBean().setProyectoSeleccionado(new Projects());
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setAdvanceExecution(new AdvanceExecutionSafeguards());
			getBuscaProyectosBean().setSocioImplementador(getPartnersFacade().buscarPartnerPorCodigo(proyecto.getPartners().getPartId()) );
			if(esReporteGenero){
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarAvanceGeneroPorProyecto(proyecto.getProjId()));								
				if (getBuscaProyectosBean().getAdvanceExecution()!=null && !getBuscaProyectosBean().getAdvanceExecution().isAdexIsReported()){
					getBuscaProyectosBean().setDatosProyecto(true);
					getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0, 4)));
					getBuscaProyectosBean().setPeriodoDesde("01");
				}else{

					getBuscaProyectosBean().setDatosProyecto(true);
				}
			}else{
				if(getLoginBean().getTipoRol()==3){					
						cargaSectoresInteres();		
						cargaAvanceEjecucionSalvaguardas(getBuscaProyectosBean().getProyectoSeleccionado());
						getBuscaProyectosBean().setDatosProyecto(true);
						sectoresInteresProyecto();
				}else{					
					getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));					
					Mensaje.verDialogo("dlgSeleccionSocios");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void informacionAvanceEjecucion(){
		cargaSectoresInteres();	
		codigoPartner = null;
		if(getBuscaProyectosBean().getCodigoStrategicPartner()==null && getBuscaProyectosBean().isSalvaguardasSociosEstrategicos()){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionSocio") ,"" );
		}else{

			if(getBuscaProyectosBean().getCodigoStrategicPartner()== null){					
				cargarAvanceEjecucionSalvaguarda(0,getBuscaProyectosBean().getProyectoSeleccionado().getProjId());
			}else{			
				codigoPartner = getBuscaProyectosBean().getCodigoStrategicPartner();
				cargarAvanceEjecucionSalvaguarda(getBuscaProyectosBean().getCodigoStrategicPartner(),getBuscaProyectosBean().getProyectoSeleccionado().getProjId());
				getBuscaProyectosBean().setCodigoStrategicPartner(codigoPartner);
			}
			getBuscaProyectosBean().setDatosProyecto(true);
			sectoresInteresProyecto();
			getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			Mensaje.ocultarDialogo("dlgSeleccionSocios");
		}
	}

	public void buscaAsignacionSalvaguardas(Projects proyecto){
		try{
			getBuscaProyectosBean().setAsignacionSalvaguardas(true);

		}catch(Exception e){
			e.printStackTrace();
		}
	}	

	public AdvanceExecutionSafeguards getAdvanceExecution(){
		return getBuscaProyectosBean().getAdvanceExecution();
	}
	public void volverABuscarProyectos(){
		getBuscaProyectosBean().setDatosProyecto(false);
		getBuscaProyectosBean().setPosicionTab(0);
		getBuscaProyectosBean().setListaSalvaguardasAsignadas(null);
		getBuscaProyectosBean().setPreguntasSelecionadas(null);

	}
	public boolean datosProyecto(){
		return getBuscaProyectosBean().isDatosProyecto();
	}
	public Projects proyectoSeleccionado(){
		return getBuscaProyectosBean().getProyectoSeleccionado();
	}
	/**
	 * Carga los partners del proyecto
	 */
	public void cargaPartnersProyecto(Projects proyecto){
		try{
			getBuscaProyectosBean().setProyectoSeleccionado(proyecto);
			getBuscaProyectosBean().setListaPartnersProyectos(getProjectsStrategicPartnersFacade().listaPartnersActivos(proyecto.getProjId()));			
			getBuscaProyectosBean().setDatosProyecto(true);
			getBuscaProyectosBean().setAsignacionSalvaguardas(true);
			cargaListadoSalvaguardas();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean asignacionSalvaguardas(){
		return getBuscaProyectosBean().isAsignacionSalvaguardas();
	}
	public void tabChange() {
		getBuscaProyectosBean().setPosicionTab(0);

	}
	public void siguienteTabPreguntas(){

		if((getBuscaProyectosBean().isSalvaguardasSociosEstrategicos() && getBuscaProyectosBean().getCodigoStrategicPartner()!=null) ||
				!getBuscaProyectosBean().isSalvaguardasSociosEstrategicos()){
			getBuscaProyectosBean().setPosicionTab(1);
			preguntasAsignadas();
		}else{
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionSocio") ,"" );
		}
	}
	public void atrasTabSocios(){
		getBuscaProyectosBean().setPosicionTab(0);
	}
	public void atrasTabPreguntas(){
		getBuscaProyectosBean().setPosicionTab(1);
	}
	public void siguienteTabSalvaguardasSeleccionadas(){
		if(getBuscaProyectosBean().getPreguntasSelecionadas()!=null){
			getBuscaProyectosBean().setPosicionTab(2);
			asignarSalvaguardas();
		}else{
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.selecionPreguntas") ,"" );
		}
	}
	/**
	 * Lista de preguntas asignadas para asignacion salvaguardas
	 */
	public void preguntasAsignadas(){
		try{
			getBuscaProyectosBean().setPreguntasSelecionadas(new ArrayList<>());
			getBuscaProyectosBean().setListaPreguntasSalvaguardas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PREGUNTASASIGNACIONSALVAGUARDAS.getCodigo()));
			getBuscaProyectosBean().setListaPreguntasAsignadas(new ArrayList<>());
			if(getBuscaProyectosBean().isSalvaguardasSociosEstrategicos())
				getBuscaProyectosBean().setListaPreguntasAsignadas(getProjectQuestionsFacade().listaPreguntasPartnerSeleccionadas(getBuscaProyectosBean().getCodigoStrategicPartner()));
			else
				getBuscaProyectosBean().setListaPreguntasAsignadas(getProjectQuestionsFacade().listaPreguntasProyectoSeleccionadas(getBuscaProyectosBean().getProyectoSeleccionado().getProjId()));			
			if(getBuscaProyectosBean().getListaPreguntasAsignadas().size()==0){	
				getBuscaProyectosBean().setSinDatosProyectoPreguntas(true);
				nuevoIngreso = true;
			}else{
				//Aqui consultar si ya respondio preguntas
				nuevoIngreso= false;
				for(ProjectQuestions preguntas:getBuscaProyectosBean().getListaPreguntasAsignadas()){
					getBuscaProyectosBean().getPreguntasSelecionadas().add(preguntas.getCatalogs().getCataId());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Seleccion de salvaguardas en base a las preguntas seleccionadas
	 */
	public void asignarSalvaguardas(){
		int pregunta=0;
		List<Catalogs> catalogoPreguntas=new ArrayList<>();
		getBuscaProyectosBean().setListaSalvaguardasAsignadas(new ArrayList<>());
		Map<String,ProjectQuestions> mapaTemp=new HashMap<String,ProjectQuestions>();
		List<ProjectQuestions> listaTemporal=new ArrayList<>();	

		Iterator itera = getBuscaProyectosBean().getPreguntasSelecionadas().iterator();
		while(itera.hasNext()){
			pregunta = Integer.valueOf(itera.next().toString());
			for(Catalogs cat:getBuscaProyectosBean().getListaPreguntasSalvaguardas()){
				if(pregunta == cat.getCataId()){
					catalogoPreguntas.add(cat);
				}
			}
		}

		for(Catalogs catalogo:catalogoPreguntas){
			ProjectQuestions salvaguardaAsignada=new ProjectQuestions();
			for(Safeguards salvaguarda:listadoSalvaguardas){
				if(catalogo.getCataText1().equals(salvaguarda.getSafeCode())){
					if(esProyecto)
						salvaguardaAsignada.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
					else{
						ProjectsStrategicPartners partner=new ProjectsStrategicPartners();
						partner.setPspaId(getBuscaProyectosBean().getCodigoStrategicPartner());
						salvaguardaAsignada.setProjectsStrategicPartners(partner);
					}
					salvaguardaAsignada.setSafeguards(salvaguarda);
					salvaguardaAsignada.setPrquStatus(true);
					salvaguardaAsignada.setCatalogs(catalogo);
					listaTemporal.add(salvaguardaAsignada);
				}
			}
		}
		getBuscaProyectosBean().setListaPreguntasAsignadas(listaTemporal);			
		for(ProjectQuestions sa:listaTemporal){
			mapaTemp.put(sa.getSafeguards().getSafeCode(), sa);
		}
		for(Entry<String,ProjectQuestions> sa:mapaTemp.entrySet()){
			getBuscaProyectosBean().getListaSalvaguardasAsignadas().add(sa.getValue());
		}			
	}

	public void cargaListadoSalvaguardas(){
		try{
			List<Object[]> listaTemp=getAplicacionBean().getListaSalvaguardas();
			listadoSalvaguardas=new ArrayList<>();
			for (Object[] objects : listaTemp) {
				Safeguards salvaguarda=new Safeguards();
				salvaguarda.setSafeId(Integer.parseInt(objects[0].toString()));				
				salvaguarda.setSafeCode(objects[3].toString());
				salvaguarda.setSafeTitle(objects[4].toString());
				listadoSalvaguardas.add(salvaguarda);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void quitaSeleccionSalvaguardaSocioEstrategico(){
		if(!getBuscaProyectosBean().isSalvaguardasSociosEstrategicos()){
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			esProyecto = true;
		}else
			esProyecto = false;

	}
	/**
	 * Grabar la asignacion de preguntas y salvaguardas
	 */
	public void grabarAsignacionSalvaguardas(){
		try{
			int tipo=0;
			if(esProyecto)
				tipo=getBuscaProyectosBean().getProyectoSeleccionado().getProjId();
			else
				tipo = getBuscaProyectosBean().getCodigoStrategicPartner();
			getProjectQuestionsFacade().agregaPreguntasProyecto(getBuscaProyectosBean().getListaPreguntasAsignadas(),esProyecto,tipo,nuevoIngreso);
			getBuscaProyectosBean().setAsignacionSalvaguardas(false);
			volverABuscarProyectos();
			getBuscaProyectosBean().setCodigoStrategicPartner(null);
			getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada") ,"" );
		}catch(Exception e){
			Mensaje.actualizarComponente(":form:growl");				
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.grabar") ,"" );
		}
	}
	/**
	 * Setea el tipo de rol del usuario
	 */
	public void indicaTipoRol(){
		getBuscaProyectosBean().setTipoRol(getLoginBean().getTipoRol());
	}
	public void cargaAvanceEjecucionSalvaguardas(Projects proyecto){
		for (ProjectUsers pu : getLoginBean().getListaProyectosDelUsuario()) {
			if(pu.getProjects().getProjId()== proyecto.getProjId() && pu.getUsers().getUserId()== getLoginBean().getUser().getUserId()){
				if(pu.getPspaId()== null){					
					cargarAvanceEjecucionSalvaguarda(0,pu.getProjects().getProjId());
					break;
				}else{					
					cargarAvanceEjecucionSalvaguarda(pu.getPspaId(),pu.getProjects().getProjId());
					break;
				}
			}
		}
	}
	public void cargarAvanceEjecucionSalvaguarda(int codigoPartner,int codigoProyecto){
		try{

			getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
			getBuscaProyectosBean().setAdvanceExecution(new AdvanceExecutionSafeguards());
			if(codigoPartner==0)
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarPorProyecto(codigoProyecto));
			else
				getBuscaProyectosBean().setAdvanceExecution(getAdvanceExecutionSafeguardsFacade().buscarPorStrategicPartner(codigoPartner,codigoProyecto));
			if (getBuscaProyectosBean().getAdvanceExecution()==null){
				AdvanceExecutionSafeguards avanceEjecucion=new AdvanceExecutionSafeguards();
				avanceEjecucion.setAdexIsReported(false);
				avanceEjecucion.setAdexRegisterDate(new Date());
				avanceEjecucion.setAdexCreationDate(new Date());				
				avanceEjecucion.setProjects(getBuscaProyectosBean().getProyectoSeleccionado());
				if(codigoPartner>0){
					ProjectsStrategicPartners psp=new ProjectsStrategicPartners();
					psp.setPspaId(codigoPartner);
					avanceEjecucion.setProjectsStrategicPartners(psp);
				}
				avanceEjecucion.setUsers(getLoginBean().getUser());
				avanceEjecucion.setAdexStatus(true);
				avanceEjecucion.setAdexIsGender(false);
				avanceEjecucion.setAdexCreatorUser(getLoginBean().getUser().getUserName());
				avanceEjecucion.setAdexTermFrom(null);
				avanceEjecucion.setAdexTermTo(null);
				getBuscaProyectosBean().setAdvanceExecution(avanceEjecucion);
				getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
				getBuscaProyectosBean().getResumenEjecutivo().setExsuCreationDate(new Date());
				getBuscaProyectosBean().getResumenEjecutivo().setExsuUserCreator(getLoginBean().getUser().getUserName());
				getBuscaProyectosBean().getResumenEjecutivo().setExsuStatus(true);
				getBuscaProyectosBean().getResumenEjecutivo().setExsuRegisterDate(new Date());

			}else{
				getBuscaProyectosBean().setAnioReporte(Integer.valueOf(getBuscaProyectosBean().getAdvanceExecution().getAdexTermFrom().substring(0,4)));
				getBuscaProyectosBean().setPeriodoDesde("01");
				sectoresInteresProyecto();
				getBuscaProyectosBean().setResumenEjecutivo(getExecutiveSummarieFacade().buscaPorAvanceEjecucion(getBuscaProyectosBean().getAdvanceExecution().getAdexId()));
				if(getBuscaProyectosBean().getResumenEjecutivo()==null){
					getBuscaProyectosBean().setResumenEjecutivo(new ExecutiveSummaries());
					getBuscaProyectosBean().getResumenEjecutivo().setExsuCreationDate(new Date());
					getBuscaProyectosBean().getResumenEjecutivo().setExsuUserCreator(getLoginBean().getUser().getUserName());
					getBuscaProyectosBean().getResumenEjecutivo().setExsuStatus(true);
					getBuscaProyectosBean().getResumenEjecutivo().setExsuRegisterDate(new Date());
				}

			}


		}catch(NoResultException e){
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		} catch (Exception e) {
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarAvanceEjecucionSalvaguarda " + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Cancela la seleccion del socio estrategico
	 */
	public void cancelaSeleccionSocio(){
		getBuscaProyectosBean().setSalvaguardasSociosEstrategicos(false);
		getBuscaProyectosBean().setCodigoStrategicPartner(null);
	}

}