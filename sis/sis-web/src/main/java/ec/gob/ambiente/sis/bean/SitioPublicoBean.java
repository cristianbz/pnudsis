/**
@autor proamazonia [Christian Báez]  10 nov. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.dto.DtoGenero;
import ec.gob.ambiente.sis.dto.DtoSalvaguardaA;
import ec.gob.ambiente.sis.dto.DtoSalvaguardaF;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class SitioPublicoBean implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Integer posicionSalvaguardas;
	
	@Getter
	@Setter
	private BigDecimal totalInversionProyectos;
	
	@Getter
	@Setter
	private BigDecimal totalHectareasCoberturaE;
	
	@Getter
	@Setter
	private Integer numeroProyectosSalvaguardaA;
	
	@Getter
	@Setter
	private Integer numeroComunidadesSalvaguardaB;
	
	@Getter
	@Setter
	private Integer numeroHombresSalvaguardaB;
	
	@Getter
	@Setter
	private Integer numeroMujeresSalvaguardaB;
	
	@Getter
	@Setter
	private Integer numeroComunidadesSalvaguardaC;
	
	@Getter
	@Setter
	private Integer numeroPracticasAncestralesC;
	
	@Getter
	@Setter
	private Integer numeroFomentoGestionComunitariaE;
	
	@Getter
	@Setter
	private BigDecimal totalRecursosInvertidos;
	
	@Getter
	@Setter
	private Integer numeroComunidadesSalvaguardaG;
	
	@Getter
	@Setter
	private Integer numeroAccionesGeneradasG;
	
	@Getter
	@Setter
	private Integer totalBeneficiariosG;
	
	@Getter
	@Setter
	private Integer totalEventosFortalecimientoHomD;
	
	@Getter
	@Setter
	private Integer totalEventosFortalecimientoMujD;
	
	@Getter
	@Setter
	private List<Safeguards> listaSalvaguardas;
	
	@Getter
	@Setter
	private String salvaguarda;
	
	@Getter
	@Setter
	private String tituloSalvaguarda;
	
	@Getter
	@Setter
	private String descripcionSalvaguarda;
	@Getter
	@Setter
	private String descripcionSalvaguardaA;
	@Getter
	@Setter
	private String descripcionSalvaguardaB;
	@Getter
	@Setter
	private String descripcionSalvaguardaC;
	@Getter
	@Setter
	private String descripcionSalvaguardaD;
	@Getter
	@Setter
	private String descripcionSalvaguardaE;
	@Getter
	@Setter
	private String descripcionSalvaguardaF;
	@Getter
	@Setter
	private String descripcionSalvaguardaG;
	@Getter
	@Setter
	private String descripcionSalvaguardaGE;
	
	@Getter
	@Setter
	private String resumenSalvaguarda;
	
	@Getter
	@Setter
	private String codigoSalvaguarda;
	
//	@Getter
//	@Setter
//	private List<String> listadoProyectosA;
	
	@Getter
	@Setter
	private List<DtoSalvaguardaA> listadoProyectosConservacion;
	
	@Getter
	@Setter
	private List<DtoSalvaguardaF> listadoRiesgoMedidaTomada;
	
	@Getter
	@Setter
	private Integer totalAccionesReversionF;
	
	@Getter
	@Setter
	private List<String> listadoMedidasTomadasF;
	
	@Getter
	@Setter
	private List<DtoGenero> listaTemasGenero;
	
	@Getter
	@Setter
	private List<DtoGenero> listaAccionesGenero;
	
	@Getter
	@Setter
	private Integer numeroTemasGenero;
	
	@Getter
	@Setter
	private Integer numeroAccionesGenero;
	
	@Getter
	@Setter
	private BigDecimal totalPresupuestoGenero;
	
	@Getter
	@Setter
	private List<String> listadoAccionesGenero;
	
	@Getter
	@Setter
	private List<String> colores;
}

