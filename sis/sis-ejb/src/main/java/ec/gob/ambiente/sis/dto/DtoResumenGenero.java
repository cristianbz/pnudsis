/**
@autor proamazonia [Christian Báez]  21 jun. 2022

**/
package ec.gob.ambiente.sis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoResumenGenero {
	private String logoEscudo;
	private String logoMae;
	private String logoPie;
	private String fecha;	
	private String proyecto;	
	private String socioImplementador;
	private String socioEstrategico;
	private String tablaTemas;
	private String tablaUno;
	private String tablaDos;
	private String tablaTres;
	private String preguntaUno;
	private String preguntaDos;
	private String preguntaTres;
	public String tipoIndicador;
	public String codigoIndicador;
	public String valorAlcanzadoUno;
	public String valorAlcanzadoDos;
	public String valorMetaUno;
	public String valorMetaDos;
	public String linea;
	public String acciones;
	public String tema;
	public String meta;
	public String indicador;
	public String periodo;
}

