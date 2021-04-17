/**
@autor proamazonia [Christian Báez]  15 abr. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoAccesoRecursoEnum {
	ACCESOPROPIEDADTIERRA(1,"Acceso a propiedad de la tierra"),
	ACCESOARECURSOSNATURALES(2,"Acceso a recursos naturales");

	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoAccesoRecursoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

	public static TipoAccesoRecursoEnum[] listaValores() {
		return values();
	}
}

