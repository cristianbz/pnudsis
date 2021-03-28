/**
@autor proamazonia [Christian Báez]  27 mar. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoParticipanteEnum {
	HOMBRESINDIGENAS(1,"Hombres Indigenas"),
	MUJERESINDIGENAS(2,"Mujeres Indigenas"),
	HOMBRESMESTIZOS(3,"Hombres Mestizos"),
	MUJERESMESTIZAS(4,"Mujeres Mestizas"),
	HOMBRESAFRO(5,"Hombres Afro"),
	MUJERESAFRO(6,"Mujeres Afro"),
	HOMBRESMONTUBIOS(7,"Hombres Montubios"),
	MUJERESMONTUBIAS(8,"Mujeres Montubias"),
	HOMBRESBLANCOS(9,"Hombres Blancos"),
	MUJERESBLANCAS(10,"Mujeres Blancas");	
	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	
	private TipoParticipanteEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

	public static TipoParticipanteEnum[] listaValores() {
		return values();
	}
}

