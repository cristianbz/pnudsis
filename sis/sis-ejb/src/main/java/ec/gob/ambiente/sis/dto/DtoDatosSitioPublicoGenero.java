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
	
	@Getter
	@Setter
	private List<DtoGenero> listaTemasGenero;
	
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
		final JsonArray jsonArray = new JsonArray();
		for (DtoGenero dtoGenero : listaTemasGenero) {
			JsonObject json = new JsonObject();
			json.put("tema",dtoGenero.tema);
			json.put("numero", dtoGenero.numero);
			jsonArray.add(json);
		}
		 final JsonObject json = new JsonObject();
	        json.put("salvaguarda", super.salvaguarda);
	        json.put("totalAccionesImplementadas", getTotalAccionesImplementadas());
	        json.put("totalTemasAplicados", getTotalTemasAplicados());
	        json.put("listaTemasGenero", jsonArray);
	        json.put("accionesImplementadas", listaAccionesGenero.toArray());
	        json.put("totalPresupuesto", getTotalPresupuesto());
	        json.toJson(writer);		
	}

}

