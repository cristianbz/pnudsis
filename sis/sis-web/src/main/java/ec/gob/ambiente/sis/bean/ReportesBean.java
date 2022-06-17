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
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ReportesBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Integer anioReporte;
	
	@Getter
	@Setter
	private Integer tipoCargaInformacion;
	
	@Getter
	@Setter
	private List<Safeguards> listaSalvaguardas;
	
	@Getter
	@Setter
	private Safeguards salvaguardaSeleccionada;
	
	@Getter
	@Setter
	private List<Projects> listaProyectos;
	
	@Getter
	@Setter
	private Projects proyectoSeleccionado;

}

