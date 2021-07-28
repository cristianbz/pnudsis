/**
@autor proamazonia [Christian Báez]  26 may. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.ProjectQuestions;
import ec.gob.ambiente.sis.model.Sectors;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class BuscaProyectosBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
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
	
	@Getter
	@Setter
	private List<Projects> listaProyectos;
	
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
	private boolean salvaguardasSociosEstrategicos;
	
	@Getter
	@Setter
	private boolean sinDatosProyectoPreguntas;
	
	@Getter
	@Setter
	private int tipoRol;
	
	@Getter
	@Setter
	private ExecutiveSummaries resumenEjecutivo;
	

}

