/**
@autor proamazonia [Christian Báez]  16 jun. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.model.Indicators;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.suia.model.Role;
import ec.gob.ambiente.suia.model.Users;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class AdministracionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Integer codigoSalvaguarda;
	
	@Getter
	@Setter
	private Integer codigoPartner;
	
	@Getter
	@Setter
	private Integer codigoProyecto;
	
	@Getter
	@Setter
	private Integer codigoTipoRespuesta;
	
	@Getter
	@Setter
	private Integer codigoTipoCatalogo;
	
	@Getter
	@Setter
	private Integer codigoProyectoUsuario;
	
	@Getter
	@Setter
	private boolean nuevaPregunta; 
	
	@Getter
	@Setter
	private boolean nuevaPreguntaGenero; 
	
	@Getter
	@Setter
	private boolean nuevoCatalogo; 
	
	@Getter
	@Setter
	private boolean nuevoUsuario; 
	
	@Getter
	@Setter
	private boolean noEsSocioEstrategico;
	
	@Getter
	@Setter
	private String nombreUsuario;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntas;
	
	@Getter
	@Setter
	private List<Catalogs> listaTipoRespuestaPregunta;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogos;
	
	@Getter
	@Setter
	private List<Components> listaComponentes;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogosFiltrados;
	
	@Getter
	@Setter
	private List<CatalogsType> listaTipoCatalogo;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasGenero;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasFiltradas;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasGeneroFiltradas;
	
	@Getter
	@Setter
	private List<ProjectsStrategicPartners> listaPartners;
	
	@Getter
	@Setter
	private Questions preguntaSeleccionada;
	
	@Getter
	@Setter
	private Catalogs catalogoSeleccionado;
	
	@Getter
	@Setter
	private List<Safeguards> listaSalvaguardas;
	

	@Getter
	@Setter
	private Users usuarioSeleccionado;
	
	@Getter
	@Setter
	private List<Users> listaUsuarios;
	
	@Getter
	@Setter
	private List<Projects> listaProyectos;
	
		
	@Getter
	@Setter
	private List<Role> listaRolesUsuario;
	
	@Getter
	@Setter
	private boolean deshabilitaOrdenPregunta;
	
	@Getter
	@Setter
	private boolean deshabilitaOrdenCatalogo;
	
	@Getter
	@Setter
	private List<Indicators> listaIndicadores;
	
	@Getter
	@Setter
	private Indicators indicadorSeleccionado;
	
	@Getter
	@Setter
	private boolean nuevoIndicador;
}

