/**
@autor proamazonia [Christian Báez]  3 abr. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoInstitucionEnum {
	POLICIANACIONAL(1,"Policía Nacional"),
	FISCALIA(2,"Fiscalía"),
	EJERCITO(3,"Ejército");

	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoInstitucionEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

	public static TipoInstitucionEnum[] listaValores() {
		return values();
	}
}

