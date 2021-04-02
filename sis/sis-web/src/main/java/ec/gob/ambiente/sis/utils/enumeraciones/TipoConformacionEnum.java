/**
@autor proamazonia [Christian Báez]  31 mar. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoConformacionEnum {

	HOMBRES(1,"Hombres"),
	MUJERES(2,"Mujeres"),
	MIXTA(3,"Mixta");	
	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoConformacionEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}
	
	public static TipoConformacionEnum[] listaValores() {
		return values();
	}
}

