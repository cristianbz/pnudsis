/**
@autor proamazonia [Christian Báez]  25 ago. 2021

**/
package ec.gob.ambiente.sis.utils.dtos;

import lombok.Getter;
import lombok.Setter;

public class DtoComponenteSalvaguarda {
	@Getter
	@Setter
	private int codigoComponente;
	
	@Getter
	@Setter
	private int codigoSalvaguarda;

	public DtoComponenteSalvaguarda(int codigoComponente, int codigoSalvaguarda) {
		super();
		this.codigoComponente = codigoComponente;
		this.codigoSalvaguarda = codigoSalvaguarda;
	}
	
	
}

