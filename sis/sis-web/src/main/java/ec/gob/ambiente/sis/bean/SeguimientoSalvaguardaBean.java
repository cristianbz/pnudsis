package ec.gob.ambiente.sis.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;
import ec.gob.ambiente.sis.model.Projects;
import ec.gob.ambiente.sis.model.Questions;

@ManagedBean
@ViewScoped
public class SeguimientoSalvaguardaBean  {

	
	
	@Getter
	@Setter
	public Integer codigoProyecto;
	
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
