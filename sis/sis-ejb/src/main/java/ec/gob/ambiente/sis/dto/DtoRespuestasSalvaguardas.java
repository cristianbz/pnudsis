/**
@autor proamazonia [Christian Báez]  19 may. 2022

**/
package ec.gob.ambiente.sis.dto;

import lombok.Getter;
import lombok.Setter;

public class DtoRespuestasSalvaguardas {
	@Getter
	@Setter
	private Integer codigoPoliticaLey;	
	@Getter
	@Setter
	private Integer codigoPregunta;	
	@Getter
	@Setter
	private String texto1;
	@Getter
	@Setter
	private String texto2;
	@Getter
	@Setter
	private String texto3;
	@Getter
	@Setter
	private String politicaLey;
	@Getter
	@Setter
	private String catalogo1;
	@Getter
	@Setter
	private String otrocatalogo;
	@Getter
	@Setter
	private Integer codigoPolitica;
	@Getter
	@Setter
	private double  decimal1;
	@Getter
	@Setter
	private double decimal2;
	@Getter
	@Setter
	private Integer numeroHombres;
	@Getter
	@Setter
	private Integer numeroMujeres;
	@Getter
	@Setter
	private String provincia;
	@Getter
	@Setter
	private String canton;
	@Getter
	@Setter
	private String parroquia;
	@Getter
	@Setter
	private String pueblo;
	@Getter
	@Setter
	private String nacionalidad;
}

