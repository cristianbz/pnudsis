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
import ec.gob.ambiente.sis.model.Catalogs;
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
	private List<Catalogs> catalogoPoliticas;
	
	@Getter
	@Setter
	private List<String> catalogoPoliticasSeleccionado;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoLeyes;
	
	@Getter
	@Setter
	private List<String> ejemploLista;
	
	@Getter
	@Setter
	private List<String> catalogoLeyesSeleccionado;
	
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
	private List<QuestionsAnswers> listaPreguntasRespuestasA;
	
	@Getter
	@Setter
	private List<QuestionsAnswers> listaPreguntasRespuestasB;
	
	@Getter
	@Setter
	private List<QuestionsAnswers> listaPreguntasRespuestasC;
	
	@Getter
	@Setter
	private List<QuestionsAnswers> listaPreguntasRespuestasD;
	
	@Getter
	@Setter
	private List<QuestionsAnswers> listaPreguntasRespuestasE;
	
	@Getter
	@Setter
	private List<QuestionsAnswers> listaPreguntasRespuestasF;
	
	@Getter
	@Setter
	private List<QuestionsAnswers> listaPreguntasRespuestasG;
	
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
	private List<ValueAnswers> listaValoresRespuestasA;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasB;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasC;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasD;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasE;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasF;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasG;
	
	@Getter
	@Setter
	private List<TableResponses> listaValoresRespuestasTabla;
		
	@Getter
	@Setter
	private List<List<QuestionsAnswers>> listaDeListasPreguntasRespuestas;
	
	@Getter
	@Setter
	private List<List<ValueAnswers>> listaDeListasValoresRespuestas;
	
	@Getter
	@Setter
	private boolean salvaguardaA;
	
	@Getter
	@Setter
	private boolean salvaguardaB;
	
	@Getter
	@Setter
	private boolean salvaguardaC;
	
	@Getter
	@Setter
	private boolean datosAvanceEjecucion;
	
	@PostConstruct
	public void init(){
		listaProyectos=new ArrayList<>();
//		salvaguardasActivas=new Hashtable<Integer,Boolean>();
//		for(int contador=1;contador<8;contador++){
//			salvaguardasActivas.put(contador, true);
//		}
		listaValoresRespuestas= new ArrayList<>();
		listaValoresRespuestasTabla= new ArrayList<>();
		salvaguardaA=false;
		listaValoresRespuestasA= new ArrayList<>();
		listaValoresRespuestasB= new ArrayList<>();
		listaValoresRespuestasC= new ArrayList<>();
		listaValoresRespuestasD= new ArrayList<>();
		listaValoresRespuestasE= new ArrayList<>();
		listaValoresRespuestasF= new ArrayList<>();
		listaValoresRespuestasG= new ArrayList<>();
		listaDeListasPreguntasRespuestas=new ArrayList<>();
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasA());
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasB());
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasC());
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasD());
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasE());
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasF());
		listaDeListasPreguntasRespuestas.add(getListaPreguntasRespuestasG());
		
	}

}
