/**
@autor proamazonia [Christian Báez]  18 feb. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoCatalogoEnum {
	TIPORESPUESTA(1,"TipoRespuesta"),
	POLITICAS(2,"Politicas"),
	LEYES(3,"Leyes");
	
	@Getter
	private final int codigo;
	@Getter
	private final String etiqueta;
	
	private TipoCatalogoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}
	
}

