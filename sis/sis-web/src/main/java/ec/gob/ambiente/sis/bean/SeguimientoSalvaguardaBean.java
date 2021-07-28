package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.tabview.TabView;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSafeguards;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoAccesoRecursoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoAreaConsolidadaEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoConformacionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoEstadoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoInstitucionEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoNivelInvolucramientoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoParticipanteEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoResultadoAcuerdoEnum;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class SeguimientoSalvaguardaBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Integer codigoStrategicPartner;
	
	@Getter
	@Setter
	private Integer anioReporte;
	
	@Getter
	@Setter
	private String periodoDesde;
	
	@Setter
	@Getter
	private boolean datosProyecto;
	
	@Setter
	@Getter
	private boolean mostrartabs;
	
	
	@Setter
	@Getter
	private List<Boolean> mensajeRequeridosTablasB;
	
	@Setter
	@Getter
	private List<Boolean> mensajeRequeridosTablasC;
	
	@Setter
	@Getter
	private List<Boolean> mensajeRequeridosTablasD;

	@Setter
	@Getter
	private List<Boolean> mensajeRequeridosTablasE;
	
	@Setter
	@Getter
	private List<Boolean> mensajeRequeridosTablasF;
	
	@Setter
	@Getter
	private List<Boolean> mensajeRequeridosTablasG;
	
	@Getter
	@Setter
	private List<ProjectQuestions> listaSalvaguardasAsignadas;

	@Setter
	@Getter
	private int tabActual;
	
	@Setter
	@Getter
	private int codigoBusquedaProyecto;
	
	@Setter
	@Getter
	private int codigoSocioImplementador;
	
	@Setter
	@Getter
	private String tituloProyecto;
	
	@Setter
	@Getter
	private Projects proyectoSeleccionado;
	
	@Setter
	@Getter
	private boolean habilitaPuebloNacionalidad;
	
	@Getter
	@Setter
	private List<Partners> listaSociosImplementadores;
	
	@Getter
	@Setter
	private List<String> listadoActividades;
	
	@Getter
	@Setter
	private List<String> listadoModalidades;
	
	@Getter
	@Setter
	private List<String> listadoInformaEjecucion;
	
	@Getter
	@Setter
	private List<String> listadoInformaComunica;
	
	@Getter
	@Setter
	private List<String> listadoNivelInvolucramiento;
	
	@Getter
	@Setter
	private String[] informaEjecucionSeleccionadas;
	
	@Getter
	@Setter
	private String[] informaComunicaSeleccionadas;
	
	
	@Getter
	@Setter
	private String[] actividadesSeleccionadas;
	
	@Getter
	@Setter
	private String[] modalidadesSeleccionadas;
	
	@Getter
	@Setter
	private List<String> listadoinstitucionAcompania;
	
	@Getter
	@Setter
	private String[] nivelInvolucramientoSeleccionados;
	
	@Getter
	@Setter
	private String[] institucionAcompaniaSeleccionados;
	
	@Getter
	@Setter
	private String[] informacionComunicaSeleccionados;
	
	@Getter
	@Setter
	private List<String> listadInformacionComunica;
	
	@Getter
	@Setter
	private String[] informacionEjecucionSeleccionados;
	
	@Getter
	@Setter
	private List<String> listadoInformacionEjecucion;
	
	@Getter
	@Setter
	private String[] monitoreoRemotoSeleccionados;
	
	@Getter
	@Setter
	private List<String> listadoMonitoreoRemoto;
	
	@Getter
	@Setter
	private String[] periodicidadSeleccionados;
	
	@Getter
	@Setter
	private List<String> listadoPeriodicidad;
	
	@Getter
	@Setter
	private String[] monitoreoInSituSeleccionados;
	
	@Getter
	@Setter
	private List<String> listadoMonitoreoInSitu;
	
	@Getter
	@Setter
	private Catalogs planGobierno;
	
	@Getter
	@Setter
	private Catalogs catalogoModalidad;
	
	@Getter
	@Setter
	private Catalogs catalogoActividad;
	
	@Getter
	@Setter
	private Catalogs catalogoAccionImplementada;
	
	@Getter
	@Setter
	private Catalogs nuevaEjecucionSB;
	
	@Getter
	@Setter
	private Catalogs nuevaComunicacionSB;
	
	@Getter
	@Setter
	private Catalogs nuevoMonitoreoRemoto;
	
	@Getter
	@Setter
	private Catalogs nuevoMonitoreoInSitu;
	
	@Getter
	@Setter
	private Map<Integer, Integer> mapaTabs; 
	
	@Setter
	private TipoParticipanteEnum[] tipoParticipanteEnum;
	
	@Setter
	private TipoConformacionEnum[] tipoConformacionEnum;
	
	@Setter
	private TipoInstitucionEnum[] tipoInstitucionEnum;
	
	@Setter
	private TipoEstadoEnum[] tipoEstadoEnum;
	
	@Setter
	private TipoAccesoRecursoEnum[] tipoAccesoRecursoEnum;
	
	@Setter
	private TipoResultadoAcuerdoEnum[] tipoResultadoAcuerdoEnum;
	
	@Setter
	private TipoNivelInvolucramientoEnum[] tipoNivelInvolucramientoEnum;
	
	@Setter
	private TipoAreaConsolidadaEnum[] tipoAreaConsolidadaEnum;
	
	@Getter
	@Setter
	private String codigoTablaDatos;
	
	@Getter
	@Setter
	private String codigoValoresRespuestas;
	
	@Getter
	@Setter
	private Integer codigoAlternativaEconomica;
	
	@Getter
	@Setter
	private Integer codigoTipoParticipante;
	
	@Getter
	@Setter
	private Integer codigoTipoConformacion;
	
	@Getter
	@Setter
	private Integer codigoTipoResultadoAcuerdo;
	
	@Getter
	@Setter
	private Integer codigoPuebloNacionalidad;
	
	@Getter
	@Setter
	private Integer codigoHerramienta;
	
	@Getter
	@Setter
	private Integer codigoPublico;
	
	@Getter
	@Setter
	private Integer codigoModalidad;
	
	@Getter
	@Setter
	private Integer codigoLineaAccion;
	
	@Getter
	@Setter
	private Integer codigoActividad;
	
	@Getter
	@Setter
	private Integer codigoAccionImplementada;
	
	@Getter
	@Setter
	private Integer codigoServicio;
	
	@Getter
	@Setter
	private Integer codigoRecursos;
	
	@Getter
	@Setter
	private Integer codigoPeriodicidad;
	
	@Getter
	@Setter
	private Integer codigoSistemas;
	
	@Getter
	@Setter
	private Integer codigoMetodo;
	
	@Getter
	@Setter
	private Integer codigoTipoInstitucion;
	
	@Getter
	@Setter
	private Integer codigoTipoAccesoRecurso;
	
	@Getter
	@Setter
	private Integer codigoTipoEstado;
	
	@Getter
	@Setter
	private Integer codigoTipoInvolucramiento;
	
	@Getter
	@Setter
	private Integer codigoTipoAreaConsolidada;
	
	@Getter
	@Setter
	private Integer codigoAccion;
	
	@Getter
	@Setter
	private Integer codigoAutoIdentificacion;
	
	@Getter
	@Setter
	private TableResponses registroTabla;
	
	@Getter
	@Setter
	private Integer codigoProyecto;
	
	@Getter
	@Setter
	private Integer codigoControlVigilancia;
	
	@Getter
	@Setter
	private Integer codigoMedidaTomada;
	
	@Getter
	@Setter
	private Integer codigoRiesgo;
	
	@Getter
	@Setter
	private Integer codProvincia;

	@Getter
	@Setter
	private Integer codCanton;

	@Getter
	@Setter
	private Integer codParroquia;


	@Getter
	@Setter
	private String codigoNivelProCanParr;
	
	@Getter
	@Setter
	private Catalogs nuevaLeySA;
	
	@Getter
	@Setter
	private Catalogs nuevoMJN;
	
	@Getter
	@Setter
	private Catalogs nuevaNormativaSN;
	
	@Getter
	@Setter
	private Catalogs nuevaHerramienta;
	
	@Getter
	@Setter
	private Catalogs nuevoServicio;
	
	@Getter
	@Setter
	private Catalogs nuevoRecurso;
	
	@Getter
	@Setter
	private Catalogs nuevoRiesgo;
	
	@Getter
	@Setter
	private Catalogs nuevoIncentivo;
	
	@Getter
	@Setter
	private Catalogs nuevoControlVigilancia;
	
//	@Getter
//	@Setter
//	private List<Catalogs> listaRecursos;
	
	@Getter
	@Setter
	private Catalogs nuevaPoliticaSA;
	
	@Getter
	@Setter
	private Catalogs nuevoTipoAcceso;
	
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
	private List<Catalogs> listaHerramientas;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogoModalidad;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogoActividad;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogoMedidaTomada;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogoAccionImplementada;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoPlanGobierno;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoPoliticas;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoMarcoJuridicoNacional;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoNormativaSecundariaNacional;
	
	@Getter
	@Setter
	private List<Integer> catalogoMarcoJuridicoNacionalSeleccionado;
	
	@Getter
	@Setter
	private List<Integer> catalogoNormativaSecundariaNacionalSeleccionado;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoInformacionComunica;
	
//	@Getter
//	@Setter
//	private List<Integer> catalogoInformacionComunicaSeleccionado;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoInformacionEjecucion;
	
//	@Getter
//	@Setter
//	private List<Catalogs> listaCatalogoRiesgo;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogoTipoAcceso;
	
	@Getter
	@Setter
	private List<Catalogs> listaServicio;
	
//	@Getter
//	@Setter
//	private List<Integer> catalogoInformacionEjecucionSeleccionado;
	
	@Getter
	@Setter
	private List<Integer> catalogoPoliticasSeleccionado;
	
	@Getter
	@Setter
	private List<Catalogs> catalogoLeyes;
	
	@Getter
	@Setter
	private List<String> ejemploLista;
	
	@Getter
	@Setter
	private List<Integer> catalogoLeyesSeleccionado;
	
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
	private List<Integer> listaSectoresSeleccionados;
	
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
	private List<TableResponses> tablaSalvaguardaB9;
	
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
	private List<TableResponses> tablaSalvaguardaE341;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE351;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE361;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE371;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE381;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE391;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_1;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_2;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_3;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_4;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_5;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_6;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_7;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_8;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_9;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_10;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_11;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_12;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_13;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_14;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_15;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_16;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_17;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_18;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_19;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_20;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_21;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_22;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_23;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_24;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_25;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_26;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_27;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_28;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_29;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_30;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_31;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_32;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_33;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_34;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_35;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_36;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_37;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaE40_38;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaF411;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaF421;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaF431;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaF441;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaF452;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG461;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG471;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG472;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG481;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG491;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG501;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaG512;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPA;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPB;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPC;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPD;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPE;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPF;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaOPG;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB61;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB62;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB11;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaB143;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaC242;
	
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
	private boolean nuevoIngresoMJN;
	
	@Getter
	@Setter
	private boolean nuevoIngresoNSN;
	
	@Getter
	@Setter
	private boolean nuevaPolitica;
	
//	@Getter
//	@Setter
//	private boolean nuevaEjecucion;
//	
//	@Getter
//	@Setter
//	private boolean nuevaComunicacion;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaA1;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPA;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPB;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPC;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPD;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPE;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPF;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaOPG;
	
	@Getter
	@Setter
	private boolean nuevaRegistroTablaD321;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB41;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB51;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB71;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB81;

	@Getter
	@Setter
	private boolean nuevoRegistroTablaB9;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB102;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB103;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB121;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB131;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC201;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC211;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC241;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC26;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC271;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC28;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC291;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC293;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC301;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC311;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaD321;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaD331;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaE341;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaE351;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaE361;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaE371;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaE381;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaE391;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaF411;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaF421;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaF431;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaF441;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaF452;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG461;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG471;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG472;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG481;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG491;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG501;
	
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaG512;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB61;
	
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB62;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB11;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaB143;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaC242;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestas;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasA;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB41;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB51;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB71;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB81;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB9;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB102;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB103;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB121;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB131;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC201;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC211;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC241;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC26;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC271;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC28;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC291;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC293;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC301;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC311;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasD321;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasD331;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasE341;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasE351;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasE361;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasE371;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasE381;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasE391;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasF411;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasF421;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasF431;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasF441;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasF452;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG461;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG471;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG472;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG481;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG491;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG501;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasG512;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPA;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPB;
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPC;
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPD;
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPE;
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPF;
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasOPG;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB61;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB62;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB11;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasB143;
	
	@Getter
	@Setter
	private TableResponses registroTablaRespuestasC242;
	
	@Getter
	@Setter
	private ExecutiveSummaries resumenEjecutivo;
	
	@PostConstruct
	public void init(){
		mapaTabs =new TreeMap<Integer,Integer>();
		registroTablaRespuestas= new TableResponses();
		registroTablaRespuestasA = new TableResponses();
		registroTablaRespuestasB102 = new TableResponses();
		registroTablaRespuestasB103 = new TableResponses();
		registroTablaRespuestasB121 = new TableResponses();
		registroTablaRespuestasB131 = new TableResponses();
		registroTablaRespuestasB41 = new TableResponses();
		registroTablaRespuestasB51 = new TableResponses();
		registroTablaRespuestasB71 = new TableResponses();
		registroTablaRespuestasB81 = new TableResponses();
		registroTablaRespuestasC201 = new TableResponses();
		registroTablaRespuestasC211 = new TableResponses();
		registroTablaRespuestasC241= new TableResponses();
		registroTablaRespuestasC26= new TableResponses();
		registroTablaRespuestasC271= new TableResponses();
		registroTablaRespuestasC28= new TableResponses();
		registroTablaRespuestasC291= new TableResponses();
		registroTablaRespuestasC293= new TableResponses();
		registroTablaRespuestasC301= new TableResponses();
		registroTablaRespuestasC311= new TableResponses();
		registroTablaRespuestasD321= new TableResponses();
		registroTablaRespuestasD331= new TableResponses();
		registroTablaRespuestasE341= new TableResponses();
		registroTablaRespuestasE351= new TableResponses();
		registroTablaRespuestasE361= new TableResponses();
		registroTablaRespuestasE371= new TableResponses();
		registroTablaRespuestasE381= new TableResponses();
		registroTablaRespuestasE391= new TableResponses();
		registroTablaRespuestasF411= new TableResponses();
		registroTablaRespuestasF421= new TableResponses();
		registroTablaRespuestasF431= new TableResponses();
		registroTablaRespuestasF441= new TableResponses();
		registroTablaRespuestasF452= new TableResponses();
		registroTablaRespuestasG461= new TableResponses();
		registroTablaRespuestasG471= new TableResponses();
		registroTablaRespuestasG472= new TableResponses();
		registroTablaRespuestasG481= new TableResponses();
		registroTablaRespuestasG491= new TableResponses();
		registroTablaRespuestasG501= new TableResponses();
		registroTablaRespuestasG512= new TableResponses();
		registroTablaRespuestasOPA= new TableResponses();
		registroTablaRespuestasOPB= new TableResponses();
		registroTablaRespuestasOPC= new TableResponses();
		registroTablaRespuestasOPD= new TableResponses();
		registroTablaRespuestasOPE= new TableResponses();
		registroTablaRespuestasOPF= new TableResponses();
		registroTablaRespuestasOPG= new TableResponses();
		registroTablaRespuestasB61= new TableResponses();
		registroTablaRespuestasB62= new TableResponses();
		registroTablaRespuestasB9= new TableResponses();
		registroTablaRespuestasB11= new TableResponses();
		registroTablaRespuestasB143= new TableResponses();
		registroTablaRespuestasC242= new TableResponses();
		
		
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
		tablaSalvaguardaE341= new ArrayList<>();
		tablaSalvaguardaE351= new ArrayList<>();
		tablaSalvaguardaE361= new ArrayList<>();
		tablaSalvaguardaE371= new ArrayList<>();
		tablaSalvaguardaE381= new ArrayList<>();
		tablaSalvaguardaE391= new ArrayList<>();
		tablaSalvaguardaF411= new ArrayList<>();
		tablaSalvaguardaF421= new ArrayList<>();
		tablaSalvaguardaF431= new ArrayList<>();
		tablaSalvaguardaF441= new ArrayList<>();
		tablaSalvaguardaF452= new ArrayList<>();
		tablaSalvaguardaG461= new ArrayList<>();
		tablaSalvaguardaG471= new ArrayList<>();
		tablaSalvaguardaG472= new ArrayList<>();
		tablaSalvaguardaG481= new ArrayList<>();
		tablaSalvaguardaG491= new ArrayList<>();
		tablaSalvaguardaG501= new ArrayList<>();
		tablaSalvaguardaG512= new ArrayList<>();
		tablaSalvaguardaOPA= new ArrayList<>();
		tablaSalvaguardaOPB= new ArrayList<>();
		tablaSalvaguardaOPC= new ArrayList<>();
		tablaSalvaguardaOPD= new ArrayList<>();
		tablaSalvaguardaOPE= new ArrayList<>();
		tablaSalvaguardaOPF= new ArrayList<>();
		tablaSalvaguardaOPG= new ArrayList<>();
		tablaSalvaguardaB61= new ArrayList<>();
		tablaSalvaguardaB62= new ArrayList<>();
		tablaSalvaguardaB9= new ArrayList<>();
		tablaSalvaguardaB11= new ArrayList<>();
		tablaSalvaguardaB143= new ArrayList<>();
		tablaSalvaguardaC242= new ArrayList<>();
		tablaSalvaguardaE40_1= new ArrayList<>();
		tablaSalvaguardaE40_2= new ArrayList<>();
		tablaSalvaguardaE40_3= new ArrayList<>();
		tablaSalvaguardaE40_4= new ArrayList<>();
		tablaSalvaguardaE40_5= new ArrayList<>();
		tablaSalvaguardaE40_6= new ArrayList<>();
		tablaSalvaguardaE40_7= new ArrayList<>();
		tablaSalvaguardaE40_8= new ArrayList<>();
		tablaSalvaguardaE40_9= new ArrayList<>();
		tablaSalvaguardaE40_10= new ArrayList<>();
		tablaSalvaguardaE40_11= new ArrayList<>();
		tablaSalvaguardaE40_12= new ArrayList<>();
		tablaSalvaguardaE40_13= new ArrayList<>();
		tablaSalvaguardaE40_14= new ArrayList<>();
		tablaSalvaguardaE40_15= new ArrayList<>();
		tablaSalvaguardaE40_16= new ArrayList<>();
		tablaSalvaguardaE40_17= new ArrayList<>();
		tablaSalvaguardaE40_18= new ArrayList<>();
		tablaSalvaguardaE40_19= new ArrayList<>();
		tablaSalvaguardaE40_20= new ArrayList<>();
		tablaSalvaguardaE40_21= new ArrayList<>();
		tablaSalvaguardaE40_22= new ArrayList<>();
		tablaSalvaguardaE40_23= new ArrayList<>();
		tablaSalvaguardaE40_24= new ArrayList<>();
		tablaSalvaguardaE40_25= new ArrayList<>();
		tablaSalvaguardaE40_26= new ArrayList<>();
		tablaSalvaguardaE40_27= new ArrayList<>();
		tablaSalvaguardaE40_28= new ArrayList<>();
		tablaSalvaguardaE40_29= new ArrayList<>();
		tablaSalvaguardaE40_30= new ArrayList<>();
		tablaSalvaguardaE40_31= new ArrayList<>();
		tablaSalvaguardaE40_32= new ArrayList<>();
		tablaSalvaguardaE40_33= new ArrayList<>();
		tablaSalvaguardaE40_34= new ArrayList<>();
		tablaSalvaguardaE40_35= new ArrayList<>();
		tablaSalvaguardaE40_36= new ArrayList<>();
		tablaSalvaguardaE40_37= new ArrayList<>();
		tablaSalvaguardaE40_38= new ArrayList<>();
		
		setMensajeRequeridosTablasB(new ArrayList<>());
		setMensajeRequeridosTablasC(new ArrayList<>());
		setMensajeRequeridosTablasD(new ArrayList<>());
		setMensajeRequeridosTablasE(new ArrayList<>());
		setMensajeRequeridosTablasF(new ArrayList<>());
		setMensajeRequeridosTablasG(new ArrayList<>());
		for(int i=0;i<11;i++){
			mensajeRequeridosTablasB.add(false);			
		}
		for(int i=0;i<8;i++){
			mensajeRequeridosTablasC.add(false);			
		}
		for(int i=0;i<2;i++){
			mensajeRequeridosTablasD.add(false);			
		}
		for(int i=0;i<6;i++){
			mensajeRequeridosTablasE.add(false);			
		}
		for(int i=0;i<5;i++){
			mensajeRequeridosTablasF.add(false);			
		}
		for(int i=0;i<6;i++){
			mensajeRequeridosTablasG.add(false);			
		}
		setDatosProyecto(false);
	}

	public TipoParticipanteEnum[] getTipoParticipanteEnum() {
		return TipoParticipanteEnum.listaValores();
	}
	
	public TipoConformacionEnum[] getTipoConformacionEnum() {
		return TipoConformacionEnum.listaValores();
	}

	public TipoInstitucionEnum[] getTipoInstitucionEnum() {
		return TipoInstitucionEnum.listaValores();
	}
	
	public TipoEstadoEnum[] getTipoEstadoEnum() {
		return TipoEstadoEnum.listaValores();
	}
	
	public TipoAccesoRecursoEnum[] getTipoAccesoRecursoEnum() {
		return TipoAccesoRecursoEnum.listaValores();
	}
	public TipoResultadoAcuerdoEnum[] getTipoResultadoAcuerdoEnum() {
		return TipoResultadoAcuerdoEnum.listaValores();
	}
	
	public TipoNivelInvolucramientoEnum[] getTipoNivelInvolucramientoEnum() {
		return TipoNivelInvolucramientoEnum.listaValores();
	}
	public TipoAreaConsolidadaEnum[] getTipoAreaConsolidadaEnum() {
		return TipoAreaConsolidadaEnum.listaValores();
	}
			
}
