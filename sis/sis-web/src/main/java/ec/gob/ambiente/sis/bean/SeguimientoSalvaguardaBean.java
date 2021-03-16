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
import ec.gob.ambiente.suia.model.GeographicalLocations;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class SeguimientoSalvaguardaBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private TableResponses registroTabla;
	
	@Getter
	@Setter
	private Integer codigoProyecto;
	
	@Getter
	@Setter
	private String codigoProvincia;
	
	@Getter
	@Setter
	private String codigoCanton;
	
	@Getter
	@Setter
	private String codigoParroquia;
		
	@Getter
	@Setter
	private Catalogs nuevaLeySA;
	
	@Getter
	@Setter
	private Catalogs nuevaPoliticaSA;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaProvincias;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaCantones;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaTodosCantones;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaTodasParroquias;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaParroquias;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoPlanGobierno;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoPoliticas;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoInformacionComunica;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoInformacionComunicaSeleccionado;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoInformacionEjecucion;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoInformacionEjecucionSeleccionado;
	
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
	private List<TableResponses> tablaSalvaguardaB41;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB51;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB71;
	
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
	private List<TableResponses> tablaSalvaguardaC201;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC211;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC241;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC26;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC271;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC28;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC291;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC293;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC301;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC311;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaD321;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaD331;
	
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
	private boolean salvaguardaD;
	
	@Getter
	@Setter
	private boolean salvaguardaE;
	
	@Getter
	@Setter
	private boolean salvaguardaF;
	
	@Getter
	@Setter
	private boolean salvaguardaG;
	
	@Getter
	@Setter
	private boolean datosAvanceEjecucion;
	
	@Getter
	@Setter
	private boolean nuevaLey;
	
	@Getter
	@Setter
	private boolean nuevaPolitica;
	
	@Getter
	@Setter
	private boolean nuevaRegistroTablaD321;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestas;
	
	@PostConstruct
	public void init(){
		registroTablaRespuestas= new TableResponses();
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
		tablaSalvaguardaB41=new ArrayList<>();
		tablaSalvaguardaB51=new ArrayList<>();
		tablaSalvaguardaB71= new ArrayList<>();
		tablaSalvaguardaB81= new ArrayList<>();
		tablaSalvaguardaB102= new ArrayList<>();
		tablaSalvaguardaB103= new ArrayList<>();
		tablaSalvaguardaB121= new ArrayList<>();
		tablaSalvaguardaB131= new ArrayList<>();
		tablaSalvaguardaC201= new ArrayList<>();
		tablaSalvaguardaC211= new ArrayList<>();
		tablaSalvaguardaC241= new ArrayList<>();
		tablaSalvaguardaC26= new ArrayList<>();
		tablaSalvaguardaC271= new ArrayList<>();
		tablaSalvaguardaC28= new ArrayList<>();
		tablaSalvaguardaC291= new ArrayList<>();
		tablaSalvaguardaC293= new ArrayList<>();
		tablaSalvaguardaC301= new ArrayList<>();
		tablaSalvaguardaC311= new ArrayList<>();
		tablaSalvaguardaD321= new ArrayList<>();
		tablaSalvaguardaD331= new ArrayList<>();
	}

}
