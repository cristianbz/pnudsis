/**
@autor proamazonia [Christian Báez]  15 abr. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoResultadoAcuerdoEnum {
	FIRMOACUERDO(1,"Se firmó acuerdo de consentimiento"),
	NOFIRMOACUERDO(2,"No se firmó acuerdo de consentimiento");

	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoResultadoAcuerdoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

	public static TipoResultadoAcuerdoEnum[] listaValores() {
		return values();
	}
}

