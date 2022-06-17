/**
@autor proamazonia [Christian Báez]  16 jun. 2022

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.ReportesBean;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import lombok.Getter;
@Named()
@ViewScoped
public class ReportesController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ReportesController.class);
	
	@Inject
	@Getter
	private ReportesBean reportesBean;
	
	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;
	
	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;
	
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@PostConstruct
	public void init(){
		try{
			getReportesBean().setTipoCargaInformacion(1);
			getReportesBean().setListaSalvaguardas(new ArrayList<>());
			getReportesBean().setListaSalvaguardas(getSafeguardsFacade().cargaSalvaguardasActivas());
			getReportesBean().setListaProyectos(new ArrayList<>());
			getReportesBean().setListaProyectos(getProjectsFacade().buscarTodosLosProyectos());
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
		}
	}
}

