/**
@autor proamazonia [Christian Báez]  17 may. 2022

**/
package ec.gob.ambiente.sis.dto;

import lombok.Getter;
import lombok.Setter;

public class DtoResumenSalvaguarda {
	@Getter
	@Setter
	private String fecha;
	@Getter
	@Setter
	private String proyecto;	
	@Getter
	@Setter
	private String socioImplementador;
	@Getter
	@Setter
	private String sectores;
	@Getter
	@Setter
	private String pregunta1;
	@Getter
	@Setter
	private String pregunta2;
	@Getter
	@Setter
	private String pregunta3;
	@Getter
	@Setter
	private String tablaJuridicoInternacional;
	@Getter
	@Setter
	private String tablaJuridicoNacional;
	@Getter
	@Setter
	private String tablaNormativaNacional;
	
}

