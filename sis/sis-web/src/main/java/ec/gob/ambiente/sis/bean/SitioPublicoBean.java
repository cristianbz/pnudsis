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
	private Integer totalEventosFortalecimientoD;
	
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
	private String resumenSalvaguarda;
	
	@Getter
	@Setter
	private String codigoSalvaguarda;
	
}

