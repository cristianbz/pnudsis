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

public class DtoDatosSitioPublicoC extends DtoSitioPublico implements Jsonable{

	public DtoDatosSitioPublicoC(String salvaguarda) {
		super(salvaguarda);
		// TODO Auto-generated constructor stub
	}
	@Getter
	@Setter
	private Integer numeroComunidadesC;
	@Getter
	@Setter
	private Integer numeroPracticas;

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toJson(Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("salvaguarda", super.salvaguarda);
        json.put("numeroComunidadesC", getNumeroComunidadesC());
        json.put("numeroPracticasC", getNumeroPracticas());              
        json.toJson(writer);

    }

}

