/**
@autor proamazonia [Christian Báez]  1 dic. 2021

**/
package ec.gob.ambiente.sis.dto;

import java.io.IOException;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import lombok.Getter;
import lombok.Setter;

public class DtoDatosSitioPublicoD extends DtoSitioPublico implements Jsonable {

	public DtoDatosSitioPublicoD(String salvaguarda) {
		super(salvaguarda);
		// TODO Auto-generated constructor stub
	}
	@Getter
	@Setter
	private Integer totalEventosHombres;
	@Getter
	@Setter
	private Integer totalEventosMujeres;
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toJson(Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("salvaguarda", super.salvaguarda);
        json.put("totalEventosHombresD", getTotalEventosHombres());
        json.put("totalEventosMujeresD", getTotalEventosMujeres());              
        json.toJson(writer);
    }

}

