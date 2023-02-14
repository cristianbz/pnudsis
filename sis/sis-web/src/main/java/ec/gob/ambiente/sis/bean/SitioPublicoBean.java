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
@Getter
@Setter
public class SitioPublicoBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer posicionSalvaguardas;	
	private BigDecimal totalInversionProyectos;
	private BigDecimal totalPersonasAccesoInfoB;
	private BigDecimal totalHectareasCoberturaE;
	private Integer numeroProyectosSalvaguardaA;
	private Integer numeroComunidadesSalvaguardaB;
//	private Integer numeroHombresSalvaguardaB;
//	private Integer numeroMujeresSalvaguardaB;
	private Integer numeroEventosRendicionB;
	private Integer numeroAlianzasB;
	private Integer numeroComunidadesSalvaguardaC;
	private Integer numeroPracticasAncestralesC;
	private Integer numeroOrganizacionIndigenaC;
	private Integer numeroEventosCPLIC;
	private Integer totalEspaciosD;
	private Integer totalPersonasParticipacionD;
	private Integer numeroOrganizacionesLocalesD;
	private BigDecimal totalHectareasConservadoE;
	private BigDecimal totalHectareasRestauradoE;
	private Integer numeroInvestigacionesE;
	private Integer numeroInvestigacionesF;
	private Integer numeroMitigacionesF;
	private Integer numeroAccionesFortalecimientoF;
	private Integer numeroActividadesControlG;
	private Integer numeroFomentoGestionComunitariaE;
	private BigDecimal totalRecursosInvertidos;
	private Integer numeroComunidadesSalvaguardaG;
	private Integer numeroAccionesGeneradasG;
	private Integer totalBeneficiariosG;
	private Integer totalEventosFortalecimientoHomD;
	private Integer totalEventosFortalecimientoMujD;
	private List<Safeguards> listaSalvaguardas;
	private String salvaguarda;
	private String tituloSalvaguarda;
	private String descripcionSalvaguarda;
	private String descripcionSalvaguardaA;
	private String descripcionSalvaguardaB;
	private String descripcionSalvaguardaC;
	private String descripcionSalvaguardaD;
	private String descripcionSalvaguardaE;
	private String descripcionSalvaguardaF;
	private String descripcionSalvaguardaG;
	private String descripcionSalvaguardaGE;
	private String resumenSalvaguarda;
	private String codigoSalvaguarda;
	private List<DtoSalvaguardaA> listadoProyectosConservacion;
	private List<DtoSalvaguardaF> listadoRiesgoMedidaTomada;
	private Integer totalAccionesReversionF;
	private List<String> listadoMedidasTomadasF;
	private List<DtoGenero> listaTemasGenero;
	private List<DtoGenero> listaAccionesGenero;
	private Integer numeroTemasGenero;
	private Integer numeroAccionesGenero;
	private BigDecimal totalPresupuestoGenero;
	private List<String> listadoAccionesGenero;
	private List<String> colores;
	private List<DtoGenero> listaAportesProyectoGenero;
	private List<DtoGenero> listaLineasProyectoGenero;
}

