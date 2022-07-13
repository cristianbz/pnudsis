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

public class DtoDatosSitioPublicoF extends DtoSitioPublico implements Jsonable {

	public DtoDatosSitioPublicoF(String salvaguarda) {
		super(salvaguarda);
		// TODO Auto-generated constructor stub
	}

	@Getter
	@Setter
	private BigDecimal totalRecursosInvertidos;
	
	@Getter
	@Setter
	private Integer totalAccionesReversion;
	@Getter
	@Setter
	private Integer numeroInvestigacionesF;
	@Getter
	@Setter
	private Integer numeroMitigacionesF;
	@Getter
	@Setter
	private Integer numeroAccionesFortalecimientoF;
	
	@Getter
	@Setter
	private List<DtoSalvaguardaF> listadoMedidasTomadas;
	
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
		for (DtoSalvaguardaF dto : listadoMedidasTomadas) {
			JsonObject json = new JsonObject();
			json.put("riesgo",dto.textoUno);
			json.put("medidatomada", dto.textoDos);
			jsonArray.add(json);
		}
        final JsonObject json = new JsonObject();
        json.put("salvaguarda", super.salvaguarda);
        json.put("totalAccionesReversionF", getTotalAccionesReversion());
        json.put("numeroInvestigacionesF", getNumeroInvestigacionesF());
        json.put("numeroMitigacionesF", getNumeroMitigacionesF());
        json.put("numeroAccionesFortalecimientoF", getNumeroAccionesFortalecimientoF());
//        json.put("medidasTomadas", listadoMedidasTomadas.toArray());
        json.put("medidasTomadas", jsonArray);
        json.toJson(writer);
    }
	
}

