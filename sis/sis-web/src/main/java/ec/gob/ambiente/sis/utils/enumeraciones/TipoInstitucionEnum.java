/**
@autor proamazonia [Christian Báez]  3 abr. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoInstitucionEnum {
	POLICIANACIONAL(1,"Policia Nacional"),
	EJERCITO(2,"Ejercito");

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

