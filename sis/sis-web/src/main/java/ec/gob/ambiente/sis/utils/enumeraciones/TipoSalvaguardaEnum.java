/**
@autor proamazonia [Christian Báez]  12 mar. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoSalvaguardaEnum {
	SALVAGUARDAA(1,"SALVAGUARDAA"),
	SALVAGUARDAB(1,"SALVAGUARDAB"),
	SALVAGUARDAC(1,"SALVAGUARDAC"),
	SALVAGUARDAD(1,"SALVAGUARDAD"),
	SALVAGUARDAE(1,"SALVAGUARDAE"),
	SALVAGUARDAF(1,"SALVAGUARDAF"),
	SALVAGUARDAG(1,"SALVAGUARDAG");
	
	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	private TipoSalvaguardaEnum(int codigo,String etiqueta){
		this.codigo = codigo;
		this.etiqueta = etiqueta;
	}

}

