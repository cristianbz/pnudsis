/**
@autor proamazonia [Christian Báez]  6 jul. 2022

**/
package ec.gob.ambiente.sis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoPreguntasPartners {
	private int id;
	private String pregunta;
	private String partner;
	private String salvaguarda;
	private String socioEstrategico;
	private String nombreProyecto;
	private String nombreCorto;
	private String periodo;
}

