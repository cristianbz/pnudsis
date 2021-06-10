/**
@autor proamazonia [Christian Báez]  18 feb. 2021

**/
package ec.gob.ambiente.sis.utils.enumeraciones;

import lombok.Getter;

public enum TipoCatalogoEnum {
	TIPORESPUESTA(1,"TipoRespuesta"),
	POLITICAS(2,"Politicas"),
	MARCOJURIDICOINTERNACIONAL(3,"Marco Juridico Internacional"),
	PLANPROYECTO(4,"PlanProyecto"),
	INFORMEEJECUCION(5,"InformeEjecucion"),
	INFORMECOMUNICACION(6,"InformeComunicacion"),
	MEDIDATOMADA(7,"MedidaTomada"),
	PUEBLOSNACIONALIDADES(8,"PueblosNacionalidades"),
	AUTOIDENTIFICACION(9,"AutoIdentificacion"),
	HERRAMIENTA(10,"Herramienta"),
	PUBLICO(11,"Publico"),
	METODO(12,"Metodo"),
	SERVICIO(13,"Servicio"),
	PERIODICIDAD(14,"Periodicidad"),
	RECURSOS(15,"Recursos"),
	SISTEMAS(16,"Sistemas"),
	ACCION(17,"Accion"),
	CONTROLVIGILANCIA(18,"ControlVigilancia"),
	ALTERNATIVAECONOMICA(19,"AlternativaEconomica"),
	MODALIDAD(20,"Modalidad"),
	ACTIVIDAD(21,"Actividad"),
	ACCIONESIMPLEMENTADAS(22,"Acciones Implementadas"),
	RIESGO(23,"Riesgo"),
	MARCOJURIDICONACIONAL(24,"Marco Juridico Nacional"),
	NORMATIVASECUNDARIANACIONAL(25,"Normativa Secundaria Nacional"),
	TIPODEACCESO(26,"Tipo de acceso"),
	MONITOREOREMOTO(27,"Monitoreo remoto"),
	MONITOREOINSITU(28,"Monitoreo insitu"),
	LINEAACCION(29,"Linea de accion"),
	TIPOORGANIZACION(30,"Tipo de organizacion");
	
	@Getter
	private final int codigo;
	@Getter
	private final String etiqueta;
	
	private TipoCatalogoEnum(int codigo,String etiqueta){
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}
	
}

