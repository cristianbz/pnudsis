/**
@autor proamazonia [Christian Báez]  16 jun. 2022

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.model.AdvanceSummary;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class ReportesBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String anioReporte;	
	private Integer anio;
	private Integer tipoCargaInformacion;
	private List<Safeguards> listaSalvaguardas;
	private List<AdvanceSummary> listaResumenAvance;
	private Safeguards salvaguardaSeleccionada;
	private List<Projects> listaProyectos;
	private Projects proyectoSeleccionado;
}

