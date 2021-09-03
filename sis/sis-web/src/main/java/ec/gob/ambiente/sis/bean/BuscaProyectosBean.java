/**
@autor proamazonia [Christian Báez]  26 may. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSpecificObjectives;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.Indicators;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.utils.dtos.DtoComponenteSalvaguarda;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class BuscaProyectosBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private boolean esRegistroSalvaguardas;
	
	@Getter
	@Setter
	private boolean esRegistroGenero;
	
	@Setter
	@Getter
	private boolean datosProyecto;
	
	@Getter
	@Setter
	private Integer codigoProyecto;
	
	@Getter
	@Setter
	private Integer codigoStrategicPartner;
	
	@Getter
	@Setter
	private Integer posicionTab;
	
	@Getter
	@Setter
	private Integer posicionTabGenero;
	
	@Setter
	@Getter
	private int codigoBusquedaProyecto;
	
	@Setter
	@Getter
	private int codigoSocioImplementador;
	
	@Getter
	@Setter
	private List<Partners> listaSociosImplementadores;
	
	@Setter
	@Getter
	private String tituloProyecto;
	
	@Setter
	@Getter
	private String nombreSocioEstrategico;
	
	@Getter
	@Setter
	private List<Projects> listaProyectos;
	
	@Getter
	@Setter
	private List<AdvanceExecutionSafeguards> listaProyectosReportados;
	
	@Getter
	@Setter
	private List<Projects> listaProyectosSociosReportados;
	
	@Getter
	@Setter
	private List<ProjectsStrategicPartners> listaPartnersProyectos;
	
	@Getter
	@Setter
	private List<Integer> listaSectoresSeleccionados;
	
	@Getter
	@Setter
	private List<Catalogs> listaPreguntasSalvaguardas;
	
	@Getter
	@Setter
	private List<ProjectQuestions> listaPreguntasAsignadas;
	
	@Getter
	@Setter
	private List<ProjectQuestions> listaSalvaguardasAsignadas;
	
	@Getter
	@Setter
	private List<Integer> preguntasSelecionadas;
	
	@Getter
	@Setter
	private List<Sectors> listaSectoresDisponibles;
	
	@Setter
	@Getter
	private Projects proyectoSeleccionado;
	
	@Getter
	@Setter
	private Partners socioImplementador;
	
	@Getter
	@Setter
	private AdvanceExecutionSafeguards advanceExecution;
	
	@Getter
	@Setter
	private Integer anioReporte;
	
	@Getter
	@Setter
	private String periodoDesde;
	
	@Getter
	@Setter
	private boolean asignacionSalvaguardas;
	
	
	@Getter
	@Setter
	private boolean asignacionGenero;
	
	@Getter
	@Setter
	private boolean salvaguardasSociosEstrategicos;
	
	@Getter
	@Setter
	private boolean lineasGeneroSociosEstrategicos;
	
	@Getter
	@Setter
	private boolean sinDatosProyectoPreguntas;
	
	@Getter
	@Setter
	private int tipoRol;
	
	@Getter
	@Setter
	private ExecutiveSummaries resumenEjecutivo;
	
	@Getter
	@Setter
	private List<CatalogsType> listaLineasGenero;
	
	@Getter
	@Setter
	private boolean proyectoTienePlanGenero;

	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaTemaGenero1;
	
	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaTemaGenero2;
	
	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaTemaGenero3;
	
	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaTemaGeneroOtros;
	
	@Getter
	@Setter
	private List<Indicators> listaIndicadores;
		
	@Getter
	@Setter
	private List<ProjectsSpecificObjectives> listaObjetivosEspecificos;
	
	@Getter
	@Setter
	private ProjectsGenderInfo lineaGeneroSeleccionada;
	
	@Getter
	@Setter
	private boolean nuevoSeguimiento;
	
	@Getter
	@Setter
	private boolean proyectoReportado;
	
	@Getter
	@Setter
	private List<ProjectQuestions> listaPreguntasAsignadasAux;
	
	@Getter
	@Setter
	private List<ProjectsSpecificObjectives> listaComponentes;
	
	@Getter
	@Setter
	private List<DtoComponenteSalvaguarda> listaComponentesSalvaguardas;
	
	@Getter
	@Setter
	private int codigoComponente;
	
	@Getter
	@Setter
	private List<Safeguards> listadoSalvaguardasAsignadasProyecto;
	
	@Getter
	@Setter
	private boolean mostrarOpcionesBusqueda;
	
	@Getter
	@Setter
	private List<ProjectsSpecificObjectives> listaComponentesParaBusqueda;
	
	@Getter
	@Setter
	private String estadoReporte;
		
	@Getter
	@Setter
	private boolean mostrarTabs;
}

