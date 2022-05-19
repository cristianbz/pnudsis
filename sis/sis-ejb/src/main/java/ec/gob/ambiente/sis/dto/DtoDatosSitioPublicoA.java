/**
@autor proamazonia [Christian Báez]  1 dic. 2021

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

public class DtoDatosSitioPublicoA extends DtoSitioPublico implements Jsonable {
	
	public DtoDatosSitioPublicoA(String salvaguarda) {
		super(salvaguarda);
		// TODO Auto-generated constructor stub
	}
	
	@Getter
	@Setter
	private BigDecimal totalInversionProyectos;
	
	@Getter
	@Setter
	private Integer numeroProyectos;
	
//	@Getter
//	@Setter
//	private List<String> listadoProyectos;
	
	@Getter
	@Setter
	private List<DtoSalvaguardaA> listadoProyectos;
	
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
		final JsonArray jsonArray = new JsonArray();
		for (DtoSalvaguardaA dto : listadoProyectos) {
			JsonObject json = new JsonObject();
			json.put("proyecto",dto.proyecto);
			json.put("inversion", dto.presupuesto);
			jsonArray.add(json);
		}
		
        final JsonObject json = new JsonObject();
        json.put("salvaguarda", super.salvaguarda);
        json.put("totalInversionProyectosA", getTotalInversionProyectos());
        json.put("numeroProyectosA", getNumeroProyectos());    
//        json.put("proyectos", listadoProyectos.toArray());
        json.put("listadoProyectos", jsonArray);
        json.toJson(writer);

    }
}



