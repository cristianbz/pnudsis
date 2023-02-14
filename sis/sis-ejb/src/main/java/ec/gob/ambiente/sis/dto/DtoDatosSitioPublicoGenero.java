/**
@autor proamazonia [Christian Báez]  23 dic. 2021

**/
package ec.gob.ambiente.sis.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import lombok.Getter;
import lombok.Setter;

public class DtoDatosSitioPublicoGenero extends DtoSitioPublico implements Jsonable {
	
	@Getter
	@Setter
	private Integer totalAccionesImplementadas;
	
	@Getter
	@Setter
	private Integer totalTemasAplicados;
	
	@Getter
	@Setter
	private BigDecimal totalPresupuesto;
	
//	@Getter
//	@Setter
//	private List<DtoGenero> listaTemasGenero;
	
	@Getter
	@Setter
	private List<DtoGenero> listaLineasAccionProyecto;
	
	@Getter
	@Setter
	private List<DtoGenero> listaAporteProyectoGenero;
	
	@Getter
	@Setter
	private List<String> listaAccionesGenero;

	public DtoDatosSitioPublicoGenero(String salvaguarda) {
		super(salvaguarda);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toJson() {
		final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
        	e.printStackTrace();
        }
        return writable.toString();

	}

	@Override
	public void toJson(Writer writer) throws IOException {		
		final JsonArray jsonArrayLA = new JsonArray();
		final JsonArray jsonArrayAP = new JsonArray();
//		for (DtoGenero dtoGenero : listaTemasGenero) {
//			JsonObject json = new JsonObject();
//			json.put("tema",dtoGenero.tema);
//			json.put("numero", dtoGenero.numero);
//			jsonArray.add(json);
//		}
		for (DtoGenero dtoGenero : listaLineasAccionProyecto) {
			JsonObject json = new JsonObject();
			json.put("linea",dtoGenero.lineaAccion);
			json.put("proyectos", dtoGenero.proyecto);
			jsonArrayLA.add(json);
		}
		for (DtoGenero dtoGenero : listaAporteProyectoGenero) {
			JsonObject json = new JsonObject();
			json.put("presupuesto",dtoGenero.presupuesto);
			json.put("proyecto", dtoGenero.proyecto);
			jsonArrayAP.add(json);
		}
		 final JsonObject json = new JsonObject();
	        json.put("salvaguarda", super.salvaguarda);
	        json.put("totalAccionesImplementadas", getTotalAccionesImplementadas());
	        json.put("totalTemasAplicados", getTotalTemasAplicados());
//	        json.put("listaTemasGenero", jsonArray);
//	        json.put("accionesImplementadas", listaAccionesGenero.toArray());
	        json.put("totalPresupuesto", getTotalPresupuesto());
	        json.put("listaLineasAccionProyecto", jsonArrayLA);
	        json.put("listaAporteProyectoGenero", jsonArrayAP);
	        json.toJson(writer);		
	}

}

