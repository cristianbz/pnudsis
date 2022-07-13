/**
@autor proamazonia [Christian Báez]  1 dic. 2021

**/
package ec.gob.ambiente.sis.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import lombok.Getter;
import lombok.Setter;

public class DtoDatosSitioPublicoG extends DtoSitioPublico implements Jsonable {

	public DtoDatosSitioPublicoG(String salvaguarda) {
		super(salvaguarda);
		// TODO Auto-generated constructor stub
	}
	@Getter
	@Setter
	private Integer numeroComunidades;
	@Getter
	@Setter
	private Integer numeroAcciones;
	@Getter
	@Setter
	private Integer totalBeneficiarios;
	@Getter
	@Setter
	private Integer numeroActividadesControl;

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
        final JsonObject json = new JsonObject();
        json.put("salvaguarda", super.salvaguarda);
        json.put("numeroComunidadesG", getNumeroComunidades());
        json.put("numeroAccionesG", getNumeroAcciones());
        json.put("numeroActividadesControl", getNumeroActividadesControl());
        json.put("totalBeneficiariosG", getTotalBeneficiarios());
        json.toJson(writer);
    }

}

