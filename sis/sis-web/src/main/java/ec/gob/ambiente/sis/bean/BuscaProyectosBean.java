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
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
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
	
}

