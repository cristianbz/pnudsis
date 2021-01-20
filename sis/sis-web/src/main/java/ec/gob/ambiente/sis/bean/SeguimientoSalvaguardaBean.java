package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;
import ec.gob.ambiente.sis.model.Projects;
import ec.gob.ambiente.sis.model.Questions;

@ManagedBean
@ViewScoped
public class SeguimientoSalvaguardaBean implements Serializable {

	private static final long serialVersionUID = 1L;
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
	

}
