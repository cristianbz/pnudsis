package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.QuestionsAnswers;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class SeguimientoSalvaguardaBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer codigoProyecto;
	
	@Getter
	@Setter
	private Hashtable<Integer,Boolean> salvaguardasActivas;
	
	@Getter
	@Setter
	private List<Safeguards> listaSalvaguardasplanAccion;
	
	@Getter
	@Setter
	private List<ProjectsSafeguards> listaSalvaguardasProyecto;
	
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
	private List<QuestionsAnswers> listaPreguntasRespuestas;
	
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
	
	@Getter
	@Setter
	private List<Sectors> listaSectoresSeleccionados;
	
	@Getter
	@Setter
	private List<Sectors> listaSectoresDisponibles;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestas;
	
	@Getter
	@Setter
	private List<TableResponses> listaValoresRespuestasTabla;
	
	@Getter
	@Setter
	private boolean salvaguardaA;
	
	@Getter
	@Setter
	private boolean salvaguardaB;
	
	@Getter
	@Setter
	private boolean salvaguardaC;
	
	@PostConstruct
	public void init(){
		listaProyectos=new ArrayList<>();
		salvaguardasActivas=new Hashtable<Integer,Boolean>();
		for(int contador=1;contador<8;contador++){
			salvaguardasActivas.put(contador, true);
		}
		listaValoresRespuestas= new ArrayList<>();
		listaValoresRespuestasTabla= new ArrayList<>();
		salvaguardaA=false;
		
		
	}

}
