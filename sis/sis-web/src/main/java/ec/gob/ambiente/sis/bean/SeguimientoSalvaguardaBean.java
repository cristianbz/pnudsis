package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Questions;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class SeguimientoSalvaguardaBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	public Integer codigoProyecto;
	
	@Getter
	@Setter
	public Partners socioImplementador;
	
	@Getter
	@Setter
	public AdvanceExecutionSafeguards advanceExecutionSafeguards;
	
	@Getter
	@Setter
	public List<Projects> listaProyectos;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaA;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaB;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaC;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaD;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaE;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaF;
	
	@Getter
	@Setter
	public List<Questions> preguntasSalvaguardaG;
	
	@PostConstruct
	public void init(){
		listaProyectos=new ArrayList<>();
		
	}

}
