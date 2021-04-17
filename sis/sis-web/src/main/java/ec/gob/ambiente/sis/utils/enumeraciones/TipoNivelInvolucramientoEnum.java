/**
@autor proamazonia [Christian Báez]  15 abr. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoNivelInvolucramientoEnum {
	EJECUCIONTECNICA(1,"Ejecución Técnica"),
	EJECUCIONFINANCIERA(2,"Ejecución Financiera");

	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoNivelInvolucramientoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

	public static TipoNivelInvolucramientoEnum[] listaValores() {
		return values();
	}
}

