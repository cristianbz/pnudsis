/**
@autor proamazonia [Christian Báez]  4 ago. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRolesUsuarioEnum;
import ec.gob.ambiente.suia.model.RolesUser;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class RegistroDeGeneroController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(RegistroDeGeneroController.class);

	

	@Getter
	@Setter
	@Inject
	private MensajesController mensajesController;

	@Inject
	@Getter
	private ComponenteBuscaProyectos componenteBuscarProyectos;
	
	@Getter
    @Setter
    @Inject
    private LoginBean loginBean;
    
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;

	@PostConstruct
	private void init(){
		try{
			getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroSalvaguardas(false);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroGenero(true);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setMenuAdministrador(true);
			getComponenteBuscarProyectos().setEsSeguimientoSalvaguardas(false);
			if(getLoginBean().getListaRolesUsuario().size()>1){
				for (RolesUser ru : getLoginBean().getListaRolesUsuario()) {						
					if(ru.getRole().getRoleName().equals(TipoRolesUsuarioEnum.SIS_socio_implementador.getEtiqueta())){					
						getLoginBean().setTipoRol(2);
						getComponenteBuscarProyectos().getBuscaProyectosBean().setRolUsuarioSeleccionado(ru);
						getLoginBean().setListaProyectosDelSocioImplementador(getProjectsFacade().listarProyectosSocioImplementador(ru.getRousDescription()));
						break;
					}
				}
			}
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init" + ": ").append(e.getMessage()));
		}
	}
	/**
	 * Regresa a la busqueda de proyectos
	 */
	public void volverBuscarProyectos(){
		getComponenteBuscarProyectos().volverABuscarProyectos();		
	}
	public boolean datosProyecto(){		
		return getComponenteBuscarProyectos().datosProyecto();
	}
}

