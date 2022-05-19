/**
@autor proamazonia [Christian Báez]  3 ago. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.bean.LoginBean;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoRolesUsuarioEnum;
import ec.gob.ambiente.suia.model.RolesUser;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class RegistroSalvaguardasController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	@Getter	
	private ComponenteBuscaProyectos componenteBuscarProyectos;
	
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
	private ProjectsFacade projectsFacade;
	
	@PostConstruct
	public void init(){
		try{
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
			getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroSalvaguardas(true);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setEsRegistroGenero(false);
			getComponenteBuscarProyectos().setEsSeguimientoSalvaguardas(false);
			getComponenteBuscarProyectos().getBuscaProyectosBean().setMenuAdministrador(true);
			getAplicacionController().cargarSalvaguardas();
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	public boolean datosProyecto(){		
		return getComponenteBuscarProyectos().datosProyecto();
	}
	
	public void volverBuscarProyectos(){
		getComponenteBuscarProyectos().volverABuscarProyectos();
		getComponenteBuscarProyectos().getBuscaProyectosBean().setAsignacionSalvaguardas(false);
	
	}
}

