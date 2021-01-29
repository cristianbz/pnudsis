package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Questions;
import lombok.Getter;
import lombok.Setter;


@Named(value="seguimientoSalvaguardaBean")
@ViewScoped
public class SeguimientoSalvaguardaBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer codigoProyecto;
	
	@Getter
	@Setter
	private Partners socioImplementador;
	
	@Getter
	@Setter
	private AdvanceExecutionSafeguards advanceExecutionSafeguards;
	
	@Getter
	@Setter
	private List<Projects> listaProyectos;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaA;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaB;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaC;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaD;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaE;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaF;
	
	@Getter
	@Setter
	private List<Questions> preguntasSalvaguardaG;
	
	@PostConstruct
	public void init(){
		listaProyectos=new ArrayList<>();
		
	}

}
