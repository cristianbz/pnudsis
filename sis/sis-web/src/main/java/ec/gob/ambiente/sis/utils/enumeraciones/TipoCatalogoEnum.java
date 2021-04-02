/**
@autor proamazonia [Christian Báez]  18 feb. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoCatalogoEnum {
	TIPORESPUESTA(1,"TipoRespuesta"),
	POLITICAS(2,"Politicas"),
	LEYES(3,"Leyes"),
	PLANPROYECTO(4,"PlanProyecto"),
	INFORMEEJECUCION(5,"InformeEjecucion"),
	INFORMECOMUNICACION(6,"InformeComunicacion"),
	MEDIDATOMADA(7,"MedidaTomada"),
	PUEBLOSNACIONALIDADES(8,"PueblosNacionalidades"),
	AUTOIDENTIFICACION(9,"AutoIdentificacion"),
	HERRAMIENTA(10,"Herramienta"),
	PUBLICO(11,"Publico"),
	METODO(12,"Metodo");
	
	@Getter
	private final int codigo;
	@Getter
	private final String etiqueta;
	
	private TipoCatalogoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}
	
}

