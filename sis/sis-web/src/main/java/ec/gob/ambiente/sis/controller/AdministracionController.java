/**
@autor proamazonia [Christian Báez]  16 jun. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.ProjectsStrategicPartnersFacade;
import ec.gob.ambiente.sis.bean.AdministracionBean;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.ProjectUsers;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.services.CatalogsTypeFacade;
import ec.gob.ambiente.sis.services.ProjectUsersFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRolesUsuarioEnum;
import ec.gob.ambiente.suia.model.Role;
import ec.gob.ambiente.suia.model.Users;
import ec.gob.ambiente.suia.service.RoleFacade;
import ec.gob.ambiente.suia.service.UsersFacade;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class AdministracionController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AdministracionController.class);
	
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
	private ProjectUsersFacade projectUsersFacade;
	
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
			
			getAdministracionBean().setListaProyectoUsuarios(new ArrayList<>());
			getAdministracionBean().setListaPreguntas(new ArrayList<>());
			getAdministracionBean().setListaPreguntas(getQuestionsFacade().listaPreguntasIngresadas());		
			getAdministracionBean().setListaPreguntasGenero(getQuestionsFacade().buscaPreguntasGenero());
			getAdministracionBean().setListaTipoRespuestaPregunta(getCatalogsFacade().buscaCatalogosPorTipo(TIPO_RESPUESTA));
			getAdministracionBean().setListaCatalogos(getCatalogsFacade().buscaTodosCatalogos());
			getAdministracionBean().setListaTipoCatalogo(getCatalogsTypeFacade().listaTipoCatalogos());			
			getAdministracionBean().setListaProyectos(getProjectsFacade().buscarTodosLosProyectos());
			cargaProyectosUsuarios();
			getAdministracionBean().setNuevaPregunta(false);
			getAplicacionController().cargarSalvaguardas();
			cargaSalvaguardas();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaProyectosUsuarios(){
		getAdministracionBean().setListaProyectoUsuarios(new ArrayList<>());
		List<ProjectUsers> listaProyectoUsuario = new ArrayList<>();
		try{
			listaProyectoUsuario = getProjectUsersFacade().listaProyectoUsuarios();
			for(ProjectUsers pu:listaProyectoUsuario){
				List<Role> roles = getRoleFacade().listRoleByUser(pu.getUsers());
				StringBuilder rolUsuario= new StringBuilder();
				for(Role r:roles){
					rolUsuario.append(r.getRoleName()).append(",");
				}
				pu.setPrusRolUsuario(rolUsuario.toString().substring(0,rolUsuario.toString().length()-1));
				if(pu.getPrusRolUsuario().contains(TipoRolesUsuarioEnum.SIS_socio_estrategico.toString())){
					pu.setPrusPartner(getProjectsStrategicPartnersFacade().partnerDelProyecto(pu.getProjects().getProjId(), pu.getPspaId()).getPartners().getPartName());
				}

				getAdministracionBean().getListaProyectoUsuarios().add(pu);
			}
		}catch(Exception e){
			e.printStackTrace();
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

	public void nuevaPregunta(int tipo){
		if(tipo == 1){
			getAdministracionBean().setNuevaPregunta(true);
			getAdministracionBean().setNuevaPreguntaGenero(false);
		}else{
			getAdministracionBean().setNuevaPreguntaGenero(true);
			getAdministracionBean().setNuevaPregunta(false);
		}
		getAdministracionBean().setPreguntaSeleccionada(new Questions());
		getAdministracionBean().setCodigoSalvaguarda(null);
		getAdministracionBean().setCodigoTipoRespuesta(null);
	}
	
	public void nuevoCatalogo(){
		getAdministracionBean().setNuevoCatalogo(true);
		getAdministracionBean().setCatalogoSeleccionado(new Catalogs());
		getAdministracionBean().setCodigoTipoCatalogo(null);
	}
	
	public void nuevoUsuario(){
		getAdministracionBean().setListaPartners(new ArrayList<>());
		getAdministracionBean().setNuevoUsuario(true);
		getAdministracionBean().setProyectoUsuarioSeleccionado(new ProjectUsers());
		getAdministracionBean().setCodigoProyecto(null);
		getAdministracionBean().setListaUsuarios(new ArrayList<>());
		getAdministracionBean().setNombreUsuario("");
		getAdministracionBean().setNoEsSocioEstrategico(true);
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
	public void editarUsuario(ProjectUsers usuario){
		getAdministracionBean().setListaPartners(new ArrayList<>());
		getAdministracionBean().setNuevoUsuario(true);
		getAdministracionBean().setProyectoUsuarioSeleccionado(usuario);
		getAdministracionBean().setCodigoProyecto(usuario.getProjects().getProjId());
		getAdministracionBean().setNombreUsuario(usuario.getUsers().getUserName());
		if(rolSocioEstrategico(usuario.getPrusRolUsuario())){
			getAdministracionBean().setNoEsSocioEstrategico(false);
			getAdministracionBean().setCodigoPartner(usuario.getPspaId());
			cargaPartnersProyecto();
		}else{
			getAdministracionBean().setNoEsSocioEstrategico(true);
			
		}
		buscarUsuarios();
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
				pregunta.setQuesParentId(pregunta.getQuesId());
				pregunta.setQuesCreatorUser(getLoginBean().getUser().getUserName());
				pregunta.setQuesCreationDate(new Date());
				pregunta.setQuesIsGender(false);
				pregunta.setQuesImportantQuestion(false);
				pregunta.setQuesPrincipalQuestion(false);
			}
			getQuestionsFacade().crearEditarPregunta(pregunta);
			getAdministracionBean().setNuevaPregunta(false);			
			getAdministracionBean().setListaPreguntas(getQuestionsFacade().listaPreguntasIngresadas());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada"), "");						
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.grabar"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarEditarPregunta " + ": ").append(e.getMessage()));
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
			}
			getQuestionsFacade().crearEditarPregunta(pregunta);
			getAdministracionBean().setNuevaPreguntaGenero(false);
			getAdministracionBean().setListaPreguntasGenero(getQuestionsFacade().buscaPreguntasGenero());	
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada"), "");						
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.grabar"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarEditarPregunta " + ": ").append(e.getMessage()));
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
			getAdministracionBean().setNuevoCatalogo(false);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada"), "");						
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.grabar"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregaEditaCatalogo " + ": ").append(e.getMessage()));
		}
	}
	public void agregarEditarUsuario(){
		try{
			if(!getAdministracionBean().isNoEsSocioEstrategico() && getAdministracionBean().getCodigoPartner()==null){
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.seleccionSocio"), "");
			}else{
				ProjectUsers pu=getAdministracionBean().getProyectoUsuarioSeleccionado();
				Projects p=new Projects();
				p.setProjId(getAdministracionBean().getCodigoProyecto());
				Users u= new Users();			
				u=getAdministracionBean().getUsuarioSeleccionado();
				pu.setProjects(p);
				pu.setUsers(u);
				if(getAdministracionBean().isNoEsSocioEstrategico()){
					pu.setPspaId(null);
				}else{					
					pu.setPspaId(getAdministracionBean().getCodigoPartner());
				}
				if(getAdministracionBean().isNoEsSocioEstrategico() && pu.getPrusId() == null && getProjectUsersFacade().validaUsuarioAsignado(u.getUserId(), p.getProjId(), null) != null){
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.usuarioAsignado"), "");
				}else if(!getAdministracionBean().isNoEsSocioEstrategico() && pu.getPrusId() == null && getProjectUsersFacade().validaUsuarioAsignado(u.getUserId(), p.getProjId(), getAdministracionBean().getCodigoPartner())!= null){
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.usuarioAsignado"), "");
				}else{
					getProjectUsersFacade().agregarEditarProyectousuario(pu);				
					cargaProyectosUsuarios();
					getAdministracionBean().setNuevoUsuario(false);
					Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesController().getPropiedad("info.infoGrabada"), "");
				}
			}
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesController().getPropiedad("error.grabar"), "");
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "agregarEditarUsuario " + ": ").append(e.getMessage()));
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
			e.printStackTrace();
		}
	}
	public void buscarUsuarios(){
		try{
//			getAdministracionBean().setListaUsuarios(getUsersFacade().listaUsuariosFiltrados(getAdministracionBean().getNombreUsuario()));
			getAdministracionBean().setListaUsuarios(getUsersFacade().findByRolEstrategicoImplementador(getAdministracionBean().getNombreUsuario()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cargaPartnersProyecto(){
		try{
			if(!getAdministracionBean().isNoEsSocioEstrategico())
				getAdministracionBean().setListaPartners(getProjectsStrategicPartnersFacade().listaPartnersActivos(getAdministracionBean().getCodigoProyecto()));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void rolesUsuario(){
		try{
			boolean encontrado=false;
			getAdministracionBean().setListaRolesUsuario(getRoleFacade().listRoleByUser(getAdministracionBean().getUsuarioSeleccionado()));
			for (Role r : getAdministracionBean().getListaRolesUsuario()) {				
				if(r.getRoleName().equals(TipoRolesUsuarioEnum.SIS_socio_estrategico.toString())){
					getAdministracionBean().setNoEsSocioEstrategico(false);
					encontrado = true;
					break;
				}
			}
			if(encontrado == false){
				getAdministracionBean().setNoEsSocioEstrategico(true);
				getAdministracionBean().setListaPartners(new ArrayList<>());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

