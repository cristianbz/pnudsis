/**
@autor proamazonia [Christian Báez]  16 mar. 2022

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoEstadoReporteEnum {
	INICIADO("I","Iniciado"),
	ENPROCESO("P","Presentado"),
	FINALIZADO("F","Finalizado");
	
	@Getter
	private final String codigo;

	@Getter
	private final String etiqueta;
	
	private TipoEstadoReporteEnum(String codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}
}

