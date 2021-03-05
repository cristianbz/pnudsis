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
import ec.gob.ambiente.sis.model.Questions;
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
	private Catalogs nuevaLeySA;
	
	@Getter
	@Setter
	private Catalogs nuevaPoliticaSA;
	
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
	private List<Questions> listaPreguntas;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasRespuestas;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasA;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasB;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasC;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasD;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasE;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasF;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasG;
	
	@Getter
	@Setter
	private List<String> listaSectoresSeleccionados;
	
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
	private List<TableResponses> tablaSalvaguardaA;
	
	@Getter
	@Setter
	private List<TableResponses> tablaActividadesSalvaguardaB;
	
	@Getter
	@Setter
	private List<TableResponses> tablaAccionesSalvaguardaB;
	
	@Getter
	@Setter
	private List<TableResponses> tablaInstrumentosSalvaguardaB;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB81;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB102;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB103;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB121;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB131;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB142;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB16;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB171;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB181;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB191;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB192;
	
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
	
	@Getter
	@Setter
	private boolean nuevaLey;
	
	@Getter
	@Setter
	private boolean nuevaPolitica;
	
	@PostConstruct
	public void init(){
		listaProyectos=new ArrayList<>();
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
		tablaSalvaguardaA=new ArrayList<>();
		
	}

}
