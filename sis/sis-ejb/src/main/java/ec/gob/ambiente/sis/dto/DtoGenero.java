/**
@autor proamazonia [Christian Báez]  23 dic. 2021

**/
package ec.gob.ambiente.sis.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DtoGenero {	
	public String tema;	
	public int numero;	
	public String acciones;
	public String indicador;
	public String meta;
	public String proyecto;
	public BigDecimal presupuesto;
	public String otraLineaAccion;
	public String lineaAccion;
}

