package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoRespuestaEnum {
	RADIOBUTTON("TIPRES01","Radio Button"),
	CHECKBOX("TIPRES02","Check Box"),
	TEXTO("TIPRES03","Texto"),
	TABLA("TIPRES04","Tabla");	
	@Getter
	private final String codigo;
	@Getter
	private final String etiqueta;
	
	private TipoRespuestaEnum(String codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

}
