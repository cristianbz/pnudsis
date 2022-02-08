/**
@autor proamazonia [Christian Báez]  14 ene. 2022

**/
package ec.gob.ambiente.sis.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoTableResponses {
	private String periodo;
	private String proyecto;
	private String socioImplementador;
	private String socioEstrategico;
	private String textoUno;
	private String textoDos;
	private String textoTres;
	private String textoCuatro;
	private String textoCinco;
	private String textoSeis;
	private String textoSiete;
	private String provincia;
	private String canton;
	private String parroquia;
	private String etnia;
	private String nacionalidad;
	private int numeroUno;
	private int numeroDos;
	private int numeroTres;
	private int numeroCuatro;
	private int numeroCinco;
	private int numeroSeis;
	private int numeroSiete;
	private int numeroOcho;
	private int numeroNueve;
	private BigDecimal decimalUno;
	private BigDecimal decimalDos;
	private Date fecha;

}

