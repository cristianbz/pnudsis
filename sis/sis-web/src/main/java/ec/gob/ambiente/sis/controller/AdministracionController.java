/**
@autor proamazonia [Christian Báez]  16 jun. 2021

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

import ec.gob.ambiente.sis.bean.AdministracionBean;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import lombok.Getter;

@Named()
@ViewScoped
public class AdministracionController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AdministracionController.class);
	
	@EJB
	@Getter
	private QuestionsFacade questionsFacade;
	
	@Inject
	@Getter
	private AdministracionBean administracionBean;
	
	@PostConstruct
	public void init(){
		try{
			getAdministracionBean().setListaPreguntas(new ArrayList<>());
			getAdministracionBean().setListaPreguntas(getQuestionsFacade().listaPreguntasIngresadas());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}

