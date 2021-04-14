/**
@autor proamazonia [Christian Báez]  14 abr. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoEstadoEnum {
	INICIADO(1,"Iniciado"),
	ENPROCESO(2,"En proceso"),
	FINALIZADO(3,"Finalizado");	
	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoEstadoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}
	
	public static TipoEstadoEnum[] listaValores() {
		return values();
	}
}

