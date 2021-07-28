/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import ec.gob.ambiente.sigma.model.User;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.services.ProjectUsersFacade;
import ec.gob.ambiente.sis.services.UserFacade;
import ec.gob.ambiente.sis.utils.EncriptarSHA;
import ec.gob.ambiente.sis.utils.JsfUtil;
import ec.gob.ambiente.sis.utils.Mensaje;
import ec.gob.ambiente.suia.model.Menu;
import ec.gob.ambiente.suia.model.MenuVO;
import ec.gob.ambiente.suia.model.Role;
import ec.gob.ambiente.suia.model.RolesUser;
import ec.gob.ambiente.suia.service.MenuFacade;
import ec.gob.ambiente.suia.service.PeopleFacade;
import ec.gob.ambiente.suia.service.RoleFacade;
import ec.gob.ambiente.suia.service.RolesUserFacade;
import ec.gob.ambiente.suia.service.UsersFacade;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class LoginController implements Serializable {
	
	private static final long serialVersionUID = -8722324921427912257L;
	private static final Logger log = Logger.getLogger(LoginController.class);
	
	@EJB
	private MenuFacade menuFacade;
	
	@EJB
	private RoleFacade roleFacade;
	
	@EJB
	private UserFacade userFacade;
	
	@EJB
	@Getter
	private UsersFacade usersFacade;
	
	@EJB
	@Getter
	private ProjectUsersFacade projectUsersFacade;
	
	@EJB
	@Getter
	private RolesUserFacade rolesUserFacade;
	
	@Inject
	@Getter
	private LoginBean loginBean;
	
	@Getter
	@Setter
	@Inject
	private MensajesController mensajesController;
	
	/*@EJB
	private ContactFacade contactFacade;*/
	
	@EJB
	private PeopleFacade peopleFacade;
	
	/*@EJB
	private AnnouncementFacade announcementDao;*/
	
		
	private List<RolesUser> rolesUsers = new ArrayList<RolesUser>();
	private MenuModel menuModel;
	private String username;
	private String password;
	private boolean loggedIn = false;
	private String userEmail;
	private String prefijoRoles;
	
	private static String ROL_SOCIO_ESTRATEGICO="SIS_socio_estrategico";
	private static String ROL_SOCIO_IMPLEMENTADOR="SIS_socio_implementador";
	private static String ROL_ADMINISTRADOR="SIS_Administrador";
	

	
	private ExternalContext ec;
	
	@PostConstruct
	private void init() throws RuntimeException, IOException, ServletException
	{		
		if(JsfUtil.getRequestParameter("login") != null && JsfUtil.getRequestParameter("login") != "" && !Boolean.parseBoolean(JsfUtil.getRequestParameter("login")))
		{			
			HttpServletRequest request = JsfUtil.getRequest();
			if (request.getUserPrincipal() != null) {
		         request.logout();
		    }
		}
		else if(loginBean.getUsername() != null && loginBean.isLoggedIn())
		{
			JsfUtil.redirect("/pages/usuario/bandejaTareas.xhtml");
		}
		
		//antiguo
		viewLogin = false;
		hasRolStandard = false;
		hasRolAdmin = false;
		hasRolSuperAdmin = false;
		
		
	}
		
	/**
     * Método para realizar login contra el security domain especificado en
     * jboss-web.xml
     *
     * @param actionEvent
     * @throws Throwable
     */
    public void login() throws Throwable {
        
    	loggedIn = false;
    	rolesUsers = new ArrayList<RolesUser>();
    	prefijoRoles=(String) JsfUtil.getProperty("roles.consultores", true);
    	
    	if(prefijoRoles!=null)
    	rolesUsers=rolesUserFacade.findByUserNameAndRoleName(username,prefijoRoles +"%");        
         
		if(rolesUsers!=null)
		{   
		    try {
		    	
		    	HttpServletRequest request = JsfUtil.getRequest();
		    	      	
		        if (request.getUserPrincipal() != null) {
		            request.logout();
		        }
		        System.out.println("clave: " + JsfUtil.claveEncriptadaSHA1(this.password));
		        request.login(this.username, this.password);       
		       
	            loggedIn = true;            
	            loginBean.setUsername(username);
	            loginBean.setPassword(password);
	            loginBean.setLoggedIn(loggedIn);
	            loginBean.setTiempoSession(request.getSession().getMaxInactiveInterval());
//	            loginBean.setUsers(userFacade.findByUserName(this.getUsername()));
	            
	            fillMenuModel();             
	           
	            JsfUtil.redirect("/pages/usuario/bandejaTareas.jsf");	    
		
			} catch (ServletException ex) {
				ex.printStackTrace();
				 FacesContext.getCurrentInstance().addMessage("userMessages", 
						 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario o la contraseña ingresada son incorrectos"));
				 username = "";
				 password = "";
			}
			
		   PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);		 
		}
		else
		{		
			FacesContext.getCurrentInstance().addMessage("userMessages", 
					 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene asignados los perfiles para este sistema. Por favor comuníquese con Mesa de Ayuda."));
			username = "";
			password = "";			 
		}
    }
	
	public void fillMenuModel() throws Throwable
	{
		//TODO colocar el código de la generación del menú aquí
		String mnemonic = (String) JsfUtil.getProperty("roles.mnemonic", true);
		for (RolesUser roleUser : rolesUsers) {
			if(!roleUser.getRole().getRoleName().startsWith(prefijoRoles))
			{
				Role r =roleFacade.findByName(prefijoRoles+roleUser.getRole().getRoleName());
				if(r!=null)
				{
					roleUser.setRole(r);
				}					
				else
				{					
					Role rolSujetoControl =roleFacade.findByName(roleUser.getRole().getRoleName());
					
					if(rolSujetoControl != null ){
						roleUser.setRole(rolSujetoControl);
					}else{
						System.out.println("No se encontró el rol "+prefijoRoles+roleUser.getRole().getRoleName()+" para generar el menú");
						loginBean.logout();
						return;
					}
				}
			}
		}
		
		List<MenuVO> menus = menuFacade.getMenusByRoles(rolesUsers, mnemonic);
		if(menus==null)
		{
			System.out.println("Error al generar el menú con los roles de Usuario");
			JsfUtil.addErrorMessage("Error al generar el menú con los roles de Usuario");
			loginBean.logout();
			return;
		}
			
		menuModel = new DefaultMenuModel();
		Menu parentMenu = menuFacade.findByMnemonic(mnemonic);
		for (MenuVO menu : menus) {
			
			if(loginBean.getOrganization() == null){
			
				if(menu.getIdMenuParent() != null && menu.getIdMenuParent().equals(parentMenu.getMenuId()))
				{
					if(!menu.getEndNode())
					{
						DefaultSubMenu subMenu = new DefaultSubMenu();
						subMenu.setLabel(menu.getLabelMenu());
						subMenu.setIcon(menu.getIcon());						
						fillItems(menu, menus, subMenu, null);
						menuModel.addElement(subMenu);
					}
					else
					{
						DefaultMenuItem menuItem_ = new DefaultMenuItem();
						menuItem_.setValue(menu.getLabelMenu());
						menuItem_.setUrl(("S/N").equals(menu.getUrlMenu()) ? null : menu.getUrlMenu());					
						menuItem_.setCommand(menu.getActionMenu().isEmpty() ? null : menu.getActionMenu());
						menuItem_.setIcon(menu.getIcon());
						menuModel.addElement(menuItem_);					
					}
				}
			}else{
				if(menu.getIdMenuParent() != null && menu.getIdMenuParent().equals(parentMenu.getMenuId()))
				{
					if(!menu.getLabelMenu().equals("Paso 1: CONVOCATORIA") && !menu.getLabelMenu().equals("Paso 2: EVALUACIÓN")){
						if(!menu.getEndNode())
						{
							DefaultSubMenu subMenu = new DefaultSubMenu();
							subMenu.setLabel(menu.getLabelMenu());
							subMenu.setIcon(menu.getIcon());
							fillItems(menu, menus, subMenu, null);
							menuModel.addElement(subMenu);
						}
						else
						{
							DefaultMenuItem menuItem_ = new DefaultMenuItem();
							menuItem_.setValue(menu.getLabelMenu());
							menuItem_.setUrl(("S/N").equals(menu.getUrlMenu()) ? null : menu.getUrlMenu());					
							menuItem_.setCommand(menu.getActionMenu().isEmpty() ? null : menu.getActionMenu());
							menuItem_.setIcon(menu.getIcon());
							menuModel.addElement(menuItem_);					
						}
					}
				}
			}
		}
		
		loginBean.setMenuModel(menuModel);
	}
	
	private DefaultMenuItem fillItems(MenuVO menu_, List<MenuVO> menus, DefaultSubMenu menuParent, DefaultMenuItem menuItem) 
	{		
		for (MenuVO menu : menus) {
		    if (menu_.getIdMenu().equals(menu.getIdMenuParent())) 
		    {
		        if (menu.getEndNode()) 
		        {
		            DefaultMenuItem menuItem_ = new DefaultMenuItem();
		            menuItem_.setValue(menu.getLabelMenu());
		            boolean result = false;//applicationFacade. .verifyPendingReport(loginBean.getPeopleVo().getDocumentId());
					if(!result)
						menuItem_.setUrl(("S/N").equals(menu.getUrlMenu()) ? null : menu.getUrlMenu());
					else
						menuItem_.setUrl("/pages/application/pendingReport.xhtml");		           
		            menuItem_.setCommand(menu.getActionMenu().isEmpty() ? null : menu.getActionMenu());
		            //menuItem_.setIcon(menu.getIcon());
		            menuItem_.setStyleClass("");
		            menuParent.getElements().add(menuItem_);
	            }
		        else 
		        {
	                addChildElement(menuParent, menu, menus, menuItem);
	            }
	        }
	    }
	    return menuItem;
	}
	
	private void addChildElement(DefaultSubMenu menuParent, MenuVO menu_, List<MenuVO> menus, DefaultMenuItem menuItem) {
        DefaultSubMenu submenuChild = new DefaultSubMenu();
        submenuChild.setLabel(menu_.getLabelMenu());
        menuParent.getElements().add(submenuChild);
        //submenuChild.setIcon(menu_.getIcon());
        submenuChild.setStyleClass("");
        DefaultMenuItem menus_ = fillItems(menu_, menus, submenuChild, menuItem);
        if (menus_ != null) {
            submenuChild.getElements().add(menus_);
        }
    }
	
	/**
	 * Recuperar Contraseña
	 */
	/*public String recuperarClave()
	{
		try {			
			
			//Buscar el usuario
			User user=userFacade.findByUserName(username);		
			if (user == null) {
				JsfUtil.addErrorMessage("Usuario no encontrado: " + username);
				return "";
			}
						
			String nombreUsuario=user.getUserName();	
			
			String nombreDestino=validarEmail(user,this.userEmail);
			if(nombreDestino==null)			{
				JsfUtil.addErrorMessage("Usuario y Correo no coiciden");
				return "";
			}
		
			//cambiar la clave por una temporal generada automaticamente
			String claveTemporal=JsfUtil.generatePassword();
			user.setUserPassword(JsfUtil.claveEncriptadaSHA1(claveTemporal));
			user.setUserTempPassword(claveTemporal);
			userFacade.updateUser(user);
			
			//Diseniar y enviar el correo de notificacion					
			String mensajeCorreo="Confirmamos que su solicitud de recuperar contrase&ntilde;a  en el sistema SUIA fue aprobada con los siguientes datos: </p>"
			+ "<ul><li>Nombre de usuario:<b>"+nombreUsuario+"</b></li>" 
			+ "<li>Clave:<b>"+claveTemporal+"</b></li></ul>"
			+ "Es necesario cambiar la contraseña al momento de ingresar en el sistema.<br/>";				
			
			if(!Email.sendEmail(this.userEmail,"Solicitud de cambio de Clave", mensajeCorreo))
			{
				JsfUtil.addWarningMessage("No se pudo enviar correo, intente mas tarde nuevamente");
				return "";
			}
			
			//Mostrar Mensaje en pantalla
			String mensajeMostrar="Estimado "+nombreDestino +", Se ha enviado una clave temporal a su correo.";	
			JsfUtil.addInfoMessage(mensajeMostrar);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		return "../login.xhtml";
	}*/
	
	
	/*private String validarEmail(User usuario,String email){
		
		try {
			List<Contact> contactsUser=contactFacade.findEmailByUser(usuario,email);
			List<Contact> contactsOrganization=contactFacade.findEmailByOrganization(usuario,email);
			if (contactsUser.size() > 0) {
				return contactsUser.get(0).getPeople().getPeopName();
			} else if (contactsOrganization.size() > 0) {
				return contactsOrganization.get(0).getOrganization()
						.getOrgaNameOrganization();
			} else {				
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}*/

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	//ANTIGUO

	private boolean hasRolStandard, hasRolAdmin, hasRolSuperAdmin, userTmpPwd;	
	String msg = "";
	private boolean isUserPwd = false;
	private boolean viewLogin = false;
	public void acceder() throws Throwable {
		/*try {
			
			loggedIn = false;
	    	rolesUsers = new ArrayList<RolesUser>();
	    	prefijoRoles=(String) JsfUtil.getProperty("roles.consultores", true);
	    	
	    	if(prefijoRoles!=null){	    		
	    		
	    		rolesUsers=rolesUserFacade.findByUserNameAndRoleName(username,prefijoRoles +"%");  
	    		
	    		if(rolesUsers == null){
	    			rolesUsers=rolesUserFacade.findByUserNameAndRoleName(username,"sujeto de control" +"%");  
	    		}
	    	}
	    		         
			if(rolesUsers!=null)
			{   
				
				if (verificarUsuario(this.username, this.password)) {
					HttpServletRequest request = (HttpServletRequest) FacesContext
							.getCurrentInstance().getExternalContext()
							.getRequest();

					request.getSession(false).setAttribute("user", this.username);
					request.getSession(false).setAttribute("rolStandard","true");
					request.getSession(false).setAttribute("rolAdmin", "false");
					request.getSession(false).setAttribute("rolSuperAdmin", "false");
					request.getSession(false).setAttribute("userTmpPwd", "false");

					if (hasRolStandard)
						request.getSession(false).setAttribute("rolStandard","true");
					if (hasRolAdmin)
						request.getSession(false).setAttribute("rolAdmin", "true");
					if (hasRolSuperAdmin)
						request.getSession(false).setAttribute("rolSuperAdmin", "true");
					if (userTmpPwd)
						request.getSession(false).setAttribute("userTmpPwd", "true");
				

					loggedIn = true;
					loginBean.setUsername(username);
					loginBean.setPassword(password);
					loginBean.setLoggedIn(loggedIn);
					loginBean.setTiempoSession(request.getSession()
							.getMaxInactiveInterval());
					loginBean.setUser(userFacade.findByUserName(this.getUsername()));

					fillMenuModel();

					FacesContext
							.getCurrentInstance()
							.getExternalContext()
							.redirect(
									request.getContextPath()
											+ "/j_spring_security_check?j_username="
											+ username + "&j_password="
											+ username);

				} else {
					JsfUtil.addWarningMessage(msg);
					viewLogin = false;
				}
			}else{
				FacesContext.getCurrentInstance().addMessage("userMessages", 
						 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene asignados los perfiles para este sistema. Por favor comuníquese con Mesa de Ayuda."));
				username = "";
				password = "";	
			}
		} catch (IOException ex) {
			FacesContext.getCurrentInstance().addMessage("userMessages", 
					 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene asignados los perfiles para este sistema. Por favor comuníquese con Mesa de Ayuda."));
			username = "";
			password = "";	
		}catch (IllegalStateException e) {
			FacesContext.getCurrentInstance().addMessage("userMessages", 
					 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no tiene asignados los perfiles para este sistema. Por favor comuníquese con Mesa de Ayuda."));
			username = "";
			password = "";	
		}*/
		JsfUtil.redirect("/pages/inicio.xhtml");

	}
	public boolean verificarUsuario(String userName, String userPwd) {

		User user = userFacade.findByUserName(userName);
//		loginBean.setUsers(user);

		if(user.getUserId() == null) {
			msg = "Usuario no encontrado: " + userName;
			
			user=userFacade.findByUserNameDisabled(userName);
			if(user.getUserId() != null && (user.getUserStatus()==null || !user.getUserStatus())) {
				msg = "El usuario (" + userName + ") se encuentra desactivado. Por favor comuniquese con Mesa de Ayuda (mesadeayuda@ambiente.gob.ec)";			
			}
			
			return false;
		}
		// si el usuario está desactivado
		if(user.getUserStatus()==null || !user.getUserStatus()) {
			msg = "El usuario (" + userName + ") se encuentra desactivado. Por favor comuniquese con Mesa de Ayuda (mesadeayuda@ambiente.gob.ec)";
			return false;
		}

		try {// If para verificar si la clave ingresada coincide con la clave de
				// bdd
			if (user.getUserPassword().equals(
					JsfUtil.claveEncriptadaSHA1(this.password))
					|| (user.getUserPassword().equals(this.password) && this.password
							.length() == 40)) {

				if (user.getUserTempPassword() != null) {
					if (user.getUserPassword().equals(
							JsfUtil.claveEncriptadaSHA1(user
									.getUserTempPassword())))
						this.userTmpPwd = true;
					else
						this.userTmpPwd = false;
				}

				isUserPwd = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error Password: " + e.getMessage();
		}

		/*if (isUserPwd) {

			try {
				
				boolean hasRole=false;
				for (TipoUsuarioEnum tipoUsuario : TipoUsuarioEnum.values()) {
					
					Role role = roleFacade.findByName(JsfUtil.getProperty(tipoUsuario.getCodigo(),true));
					if (role != null) {		
						
						
						
						List<RolesUser> rolesUsuario = rolesUserFacade.findByUserNameAndRoleName(user.getUserName(),role.getRoleName());
						if (rolesUsuario!=null) {
							hasRole=true;
							
							if(tipoUsuario==TipoUsuarioEnum.ADMINISTRADOR)
								hasRolAdmin = true;								
								
							if(tipoUsuario==TipoUsuarioEnum.PROPONENTE)
								hasRolStandard = true;						
							
							
							
						}else{
							msg = "El usuario (" + userName + ") no tiene el perfil para el ingreso al sistema. Por favor comuniquese con Mesa de Ayuda (mesadeayuda@ambiente.gob.ec)";
						}
						
					}else{
						msg = "Rol no existe: "+JsfUtil.getProperty(tipoUsuario.getCodigo(),true);
					}
				}			
				
				if (hasRole)
					return true;
				msg = "El usuario (" + userName + ") no tiene el perfil para el ingreso al sistema. Por favor comuniquese con Mesa de Ayuda (mesadeayuda@ambiente.gob.ec)";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "Error Rol: " + e.getMessage();

			}
		} else {
			msg = "Clave de Usuario (" + userName + ") Incorrecta: ";
		}

		return false;*/
		//DAVID
		return true;
	}
	public void registrarse()
	{
		try {
			//cambio 25 de julio del 2017 para que se puedan registrar en el sistema 
			//sin convocatoria vigente
//			List<Announcement> annoucList = new ArrayList<Announcement>();
//			annoucList = announcementDao.findByAvailability(AnnouncementFacade.nowDate());
//			if (annoucList != null) {
				JsfUtil.redirect("/usuario/registro.xhtml");				
//			} else {
//				JsfUtil.addWarningMessage("No existe una convocatoria vigente.");
//			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void validarUsuario(){		
		try{
			
			getLoginBean().setUser(getUsersFacade().validarUsuario(username,EncriptarSHA.encriptarSHA1(password)));
			getLoginBean().setListaProyectosDelUsuario(new ArrayList<>());
			if(getLoginBean().getUser()!=null){
				ec = FacesContext.getCurrentInstance().getExternalContext();
				getLoginBean().setSesion( (HttpSession)ec.getSession(true));
				getLoginBean().getSesion().setAttribute("logeado", true);
				getLoginBean().setListaRolesUsuario(getRolesUserFacade().listRoleByUser(getLoginBean().getUser()));
				for (RolesUser ru : getLoginBean().getListaRolesUsuario()) {
					if(ru.getRole().getRoleName().equals(ROL_SOCIO_ESTRATEGICO)){
						getLoginBean().setListaProyectosDelUsuario(getProjectUsersFacade().listaProyectosDelUsuario(getLoginBean().getUser().getUserId()));
						getLoginBean().setTipoRol(3);
						break;
					}else if(ru.getRole().getRoleName().equals(ROL_SOCIO_IMPLEMENTADOR)){
						getLoginBean().setTipoRol(2);
						break;
					}else{
						getLoginBean().setTipoRol(1);
						break;
					}
				}
				
				JsfUtil.redirect("/pages/inicio.xhtml");
			}else{
				Mensaje.actualizarComponente(":indexForm:growl");
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR,  getMensajesController().getPropiedad("error.usuarionoValido"), "");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Valida si la sesion esta activa
	 */
	public void validarSesion() {
		try {
			if (!(getLoginBean().getSesion()!=null && (boolean) getLoginBean().getSesion().getAttribute("logeado"))) {
				ec=FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
			}
		}catch(IOException e) {
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "validarSesion" + ": ").append(e.getMessage()));
		}
	}	
}