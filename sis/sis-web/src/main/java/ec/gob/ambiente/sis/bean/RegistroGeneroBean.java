/**
@autor proamazonia [Christian Báez]  31 may. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Catalog;
import ec.gob.ambiente.sigma.model.CatalogType;
import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.model.Projects;

import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.DetailAdvanceGender;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.GenderAdvances;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class RegistroGeneroBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Integer codigoLineaEstrategica;
	
	@Getter
	@Setter
	private Integer codigoLineaGenero;
	
	@Getter
	@Setter
	private Integer codigoProjectGenderInfo;
	
	@Getter
	@Setter
	private Integer codigoProjectGenderInfoTransformativo;
	
	@Getter
	@Setter
	private String codigoTablaDatos;
	
	@Getter
	@Setter
	private boolean datosGeneroParaMostrar;
	
	@Getter
	@Setter
	private boolean mostrarTabla1;
	
	@Getter
	@Setter
	private boolean preguntasGenero;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero1;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero2;
	
	@Getter
	@Setter
	private boolean nuevoRegistroPreguntasGenero;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero3;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero4;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero5;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero6;
	
	@Getter
	@Setter
	private boolean nuevoRegistroTablaGenero7;
	
	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaLineaGenero;
	
	@Getter
	@Setter
	private GenderAdvances avanceGeneroSeleccionado;
	
//	@Getter
//	@Setter
//	private GenderAdvances avanceGeneroTransformadorSeleccionado;
	
	@Getter
	@Setter
	private List<CatalogType> listadoLineaGenero;
	
	@Getter
	@Setter
	private List<Catalog> listaLineasAccion;
	
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
	private Integer codigoAutoIdentificacion;
	
	@Getter
	@Setter
	private int codigoPuebloNacionalidad;
	
	@Getter
	@Setter
	private boolean habilitaPuebloNacionalidad;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaProvincias;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaCantones;
	
	@Getter
	@Setter
	private List<GeographicalLocations> listaParroquias;
	
	@Getter
	@Setter
	private List<DetailAdvanceGender> listaDatosAvanceGenero;
	
//	@Getter
//	@Setter
//	private List<DetailAdvanceGender> listaDatosAvanceGeneroTransformador;
	
	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaInformacionGeneroSensible;
	
	@Getter
	@Setter
	private List<ProjectsGenderInfo> listaInformacionGeneroTransformador;
	
	@Getter
	@Setter
	private AdvanceExecutionSafeguards advanceExecutionSafeguards;
	
	@Getter
	@Setter
	private ProjectsGenderInfo informacionProyectoGeneroSeleccionado;
	
	@Getter
	@Setter
	private DetailAdvanceGender detailAdvanceGender;
	
	@Getter
	@Setter
	private DetailAdvanceGender detailAdvanceGenderTransformacion;
	
	@Getter
	@Setter
	private DetailAdvanceGender registroTablaGenero;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntas;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestas;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas1;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas2;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas3;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas4;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas5;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas6;
	
	@Getter
	@Setter
	private List<TableResponses> tablaRespuestas7;
	
	@Getter
	@Setter
	private TableResponses filaTabla3;
	
	@Getter
	@Setter
	private TableResponses filaTabla4;
	
	@Getter
	@Setter
	private TableResponses filaTabla5;
	
	@Getter
	@Setter
	private TableResponses filaTabla6;
	
	@Getter
	@Setter
	private TableResponses filaTabla7;
	
	@Getter
	@Setter
	private String[] tipoOrganizacionSeleccionados;
	
	@Getter
	@Setter
	private List<String> listadoTipoOrganizaciones;
	
	@Getter
	@Setter
	private String[] tipoIncentivoSeleccionado;
	
	@Getter
	@Setter
	private List<String> listadoTipoIncentivo;
	
	@Getter
	@Setter
	private ExecutiveSummaries resumenEjecutivo;
	
	@Getter
	@Setter
	private Projects proyectoSeleccionado;
	
	@Getter
	@Setter
	private Partners socioImplementador;
	
	@Getter
	@Setter
	private Integer posicionTab;
	
	@PostConstruct
	public void init(){
		setTablaRespuestas3(new ArrayList<>());
		setTablaRespuestas4(new ArrayList<>());
		setTablaRespuestas5(new ArrayList<>());
		setTablaRespuestas6(new ArrayList<>());
		setTablaRespuestas7(new ArrayList<>());
	}
}

