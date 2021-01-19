package ec.gob.ambiente.sis.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;




import org.apache.log4j.Logger;

import lombok.Getter;
import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;
import ec.gob.ambiente.sis.services.ProjectsFacade;

@ManagedBean
@ViewScoped
public class SeguimientoSalvaguardaController implements Serializable {


	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SeguimientoSalvaguardaController.class);
	
	@Inject
	@Getter
	private SeguimientoSalvaguardaBean seguimientoSalvaguardaBean;
	
	@EJB
	private ProjectsFacade projectsFacade;
	
	@PostConstruct
	public void init(){
		try{
			System.out.println(projectsFacade.findAll());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saludar(){
		System.out.println("HOLA");
	}
}
