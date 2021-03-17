/**
@autor proamazonia [Christian Báez]  12 mar. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoSalvaguardaEnum {
	SALVAGUARDAA(1,"SALVAGUARDAA"),
	SALVAGUARDAB(2,"SALVAGUARDAB"),
	SALVAGUARDAC(3,"SALVAGUARDAC"),
	SALVAGUARDAD(4,"SALVAGUARDAD"),
	SALVAGUARDAE(5,"SALVAGUARDAE"),
	SALVAGUARDAF(6,"SALVAGUARDAF"),
	SALVAGUARDAG(7,"SALVAGUARDAG");
	
	@Getter
	private final int codigo;

	@Getter
	private final String etiqueta;
	private TipoSalvaguardaEnum(int codigo,String etiqueta){
		this.codigo = codigo;
		this.etiqueta = etiqueta;
	}

}

